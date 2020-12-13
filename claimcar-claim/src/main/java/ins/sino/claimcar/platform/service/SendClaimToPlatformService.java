/******************************************************************************
 * CREATETIME : 2016年5月25日 下午3:22:35
 ******************************************************************************/
package ins.sino.claimcar.platform.service;

import ins.platform.utils.DataUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.claim.service.ClaimKindHisService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.ClaimToReinsuranceService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLClaimKindHisVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.platform.vo.BiClaimBasePartVo;
import ins.sino.claimcar.platform.vo.BiClaimReqBodyVo;
import ins.sino.claimcar.platform.vo.BiClaimSubrogationDataVo;
import ins.sino.claimcar.platform.vo.CiClaimBasePartVo;
import ins.sino.claimcar.platform.vo.CiClaimReqBodyVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationCarVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ★XMSH
 */
@Service("sendClaimToPlatformService")
public class SendClaimToPlatformService {

	private Logger logger = LoggerFactory.getLogger(SendClaimToPlatformService.class);

	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	private ClaimKindHisService claimKindHisService;
	@Autowired
	ClaimToReinsuranceService claimToReinsuranceService;
	@Autowired
	private SubrogationService subrogationService;
	@Autowired
	CiClaimPlatformLogService platformLogService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	SendClaimToSHPlatformService sendClaimToSH;
	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	@Autowired
	RegistQueryService registQueryService;

	/**
	 * 全国平台的调用方法
	 */
	public void sendClaimToPlatform(PrpLClaimVo claimVo) {
		String registNo = claimVo.getRegistNo();
		String policyNo = claimVo.getPolicyNo();
		// 立案送平台
		try {
			PrpLCMainVo cMainVo = policyViewService.getPolicyInfo(registNo,policyNo);
			String classType = Risk.DQZ.equals(claimVo.getRiskCode())?"11":"12";
			String comCode = policyViewService.findPolicyComCode(registNo,classType);
			String claimNo = claimVo.getClaimNo();
			String reqType = Risk.DQZ.equals(claimVo.getRiskCode())?
			RequestType.ClaimCI.getCode():RequestType.ClaimBI.getCode();
			//查询
			CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(reqType,claimNo,comCode);
			if (logVo != null) {
				logger.info("立案"+claimNo+"上传已存在成功记录，无需重复上传！");
			}else{
				if (cMainVo.getComCode().startsWith("22")) {// 上海平台
					sendClaimToSH.sendClaimToPlatFormSH(registNo, claimNo);
				} else {
					if (Risk.DQZ.equals(cMainVo.getRiskCode())) {// 交强
						sendClaimCIToPlatform(registNo,claimNo);
					} else {// 商业
						sendClaimBIToPlatform(registNo,claimNo);
					}
				}
			}
			
		} catch (Exception e) {
			logger.error("立案送平台失败，原因："+e);
			//e.printStackTrace();
			// throw new IllegalArgumentException("提交失败！立案送平台失败！<br/>"+e);
		}

		// 立案送再保未决数据分摊试算 niuqiang
		try {
			List<PrpLClaimKindHisVo> kindHisVoList = claimKindHisService
					.findKindHisVoList(claimVo.getClaimNo(), "1");
			ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // 填写日志表
			claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
			claimInterfaceLogVo.setRegistNo(registNo);
			claimInterfaceLogVo.setComCode(claimVo.getComCode());
			claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
			claimInterfaceLogVo.setCreateTime(new Date());
			claimInterfaceLogVo.setOperateNode(FlowNode.Claim.getName());
			interfaceAsyncService.TransDataForClaimVo(claimVo,kindHisVoList, claimInterfaceLogVo);
		} catch (Exception e) {
			logger.error("立案送再保未决数据分摊试算失败，原因："+e);
//			e.printStackTrace();
			// throw new IllegalArgumentException("立案送再保未决数据分摊试算送平台失败！<br/>"+e);
		}
	}
	
	/**
	 * 全国平台-交强立案
	 * @param registNo
	 * @param claimNo
	 * @throws Exception
	 */
	public void sendClaimCIToPlatform(String registNo, String claimNo) {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		String comCode = policyViewService.findPolicyComCode(registNo,"11");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo
				(RequestType.RegistInfoCI.getCode(),registNo,comCode);
		if (logVo == null) {
			logger.error("交强立案上传全国平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
		String claimSeqNo = logVo == null ? "" : logVo.getClaimSeqNo();
		
		String validNo = policyViewService.findValidNo(registNo,"11");
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
		
		PlatformController controller = PlatformFactory.getInstance
				(comCode, RequestType.ClaimCI);

		CiClaimReqBodyVo bodyVo = new CiClaimReqBodyVo();

		CiClaimBasePartVo basePart = new CiClaimBasePartVo();
		basePart.setConfirmSequenceNo(validNo);// validNo投保确认码
		basePart.setClaimCode(claimSeqNo);
//		String sumTot = new DecimalFormat("######0.00").format(DataUtils.NullToZero(claimVo.getSumDefLoss()));
		basePart.setEstimateAmount(DataUtils.NullToZero(claimVo.getSumDefLoss()).toString());
		basePart.setRegistrationNo(claimVo.getClaimNo());
		basePart.setRegistrationTime(claimVo.getClaimTime());
		basePart.setClaimType(claimVo.getClaimType());
		String caseFlag = claimVo.getCaseFlag();
		basePart.setPaySelfFlag(StringUtils.isEmpty(caseFlag)?"0":caseFlag);
		basePart.setReportNo(claimVo.getRegistNo());

		bodyVo.setBasePart(basePart);
		if(SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"1"))){
			controller.callPlatform(bodyVo);
		}
		logger.debug("");
	}

	public void sendClaimBIToPlatform(String registNo,String claimNo){
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		String comCode = policyViewService.findPolicyComCode(registNo,"12");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(
				RequestType.RegistInfoBI.getCode(),registNo,comCode);
		if (logVo == null) {
			logger.debug("商业立案上传全国平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
		String claimSeqNo = logVo == null ? "" : logVo.getClaimSeqNo();
		
		String validNo = policyViewService.findValidNo(registNo,"12");
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
		PrpLSubrogationMainVo subrogationMainVo = subrogationService.find(registNo);
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.ClaimBI);

		BiClaimReqBodyVo bodyVo = new BiClaimReqBodyVo();

		BiClaimBasePartVo basePart = new BiClaimBasePartVo();
		basePart.setConfirmSequenceNo(validNo);
		basePart.setClaimSequenceNo(claimSeqNo);
		basePart.setClaimNotificationNo(claimVo.getRegistNo());
		basePart.setClaimRegistrationNo(claimVo.getClaimNo());
		basePart.setClaimType(claimVo.getClaimType());
		basePart.setClaimRegistrationTime(claimVo.getClaimTime());
//		String sumTot = new DecimalFormat("######0.00").format(DataUtils.NullToZero(claimVo.getSumDefLoss()));
		basePart.setEstimatedLossAmount(DataUtils.NullToZero(claimVo.getSumDefLoss()).toString());
		
		List<PrpLSubrogationCarVo> subRCarVoList = null;
		if(subrogationMainVo != null){
			subRCarVoList = subrogationMainVo.getPrpLSubrogationCars();
		}
		String subR = claimVo.getIsSubRogation();
		String sub = StringUtils.isNotBlank(subR) ? subR : subRCarVoList 
				!= null && subRCarVoList.size() > 0 ? "1" : "0";
		basePart.setSubrogationFlag(sub);

		List<BiClaimSubrogationDataVo> subrogationList = new ArrayList<BiClaimSubrogationDataVo>();
		if(subRCarVoList != null && subRCarVoList.size() > 0 && "1".equals(subR)){
			for(PrpLSubrogationCarVo carVo:subrogationMainVo.getPrpLSubrogationCars()){
				BiClaimSubrogationDataVo subrogationDataVo = new BiClaimSubrogationDataVo();
				subrogationDataVo.setLinkerName(carVo.getLinkerName());
				subrogationDataVo.setLicensePlateNo(toTrimLicno(carVo.getLicenseNo()));
				String licType = carVo.getLicenseType();
				if("25".equals(licType)){
					licType = "99";
				}
				subrogationDataVo.setLicensePlateType(licType);
				subrogationDataVo.setVIN(carVo.getVinNo());
				subrogationDataVo.setEngineNo(carVo.getEngineNo());
				subrogationDataVo.setCaInsurerCode(carVo.getBiInsurerCode());
				subrogationDataVo.setCaInsurerArea(carVo.getBiInsurerArea());
				subrogationList.add(subrogationDataVo);
			}
		}
		bodyVo.setBasePart(basePart);
		bodyVo.setSubrogationDataList(subrogationList);

		if(SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"2"))){
			controller.callPlatform(bodyVo);
		}
//		controller.callPlatform(bodyVo);
	}
	
	//去掉全角与半角空格
	private  String toTrimLicno(String licenseNo) {
		if(StringUtils.isNotBlank(licenseNo)){
			licenseNo = licenseNo.replace((char)12288,' ').replace(" " ,"");
			return licenseNo;
		}else{
			return licenseNo;
		}
		
		
	}

}
