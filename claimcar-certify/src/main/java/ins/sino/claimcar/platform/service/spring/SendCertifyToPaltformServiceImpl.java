package ins.sino.claimcar.platform.service.spring;

import ins.platform.common.util.StringOperationUtil;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.CheckStatus;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.certify.vo.PrpLCertifyDirectVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyItemVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.platform.service.CertifyToPaltformService;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.platform.vo.BiCertifyBasePartVo;
import ins.sino.claimcar.platform.vo.BiCertifyDocDetailDataVo;
import ins.sino.claimcar.platform.vo.BiCertifyReqBodyVo;
import ins.sino.claimcar.platform.vo.BiCertifySubrogationDataVo;
import ins.sino.claimcar.platform.vo.CiCertifyBasePartVo;
import ins.sino.claimcar.platform.vo.CiCertifyDocDetailDataVo;
import ins.sino.claimcar.platform.vo.CiCertifyReqBodyVo;
import ins.sino.claimcar.platform.vo.sh.SHBiCertifyReqBasePartVo;
import ins.sino.claimcar.platform.vo.sh.SHBiCertifyReqBodyVo;
import ins.sino.claimcar.platform.vo.sh.SHBiCertifyReqDisputeDataVo;
import ins.sino.claimcar.platform.vo.sh.SHBiCertifyReqDocDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCiCertifyReqBasePartVo;
import ins.sino.claimcar.platform.vo.sh.SHCiCertifyReqBodyVo;
import ins.sino.claimcar.platform.vo.sh.SHCiCertifyReqDisputeDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCiCertifyReqDocDataVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationCarVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationPersonVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 单证平台交互服务接口实现
 * @author Luwei
 * @CreateTime
 */
@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" }, timeout = 1000000)
@Path("certifyToPaltformService")
public class SendCertifyToPaltformServiceImpl implements CertifyToPaltformService{

	private Logger logger = LoggerFactory.getLogger(SendCertifyToPaltformServiceImpl.class);
	
	@Autowired
	private CertifyPubService certifyPubService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	private SubrogationService subrogationService;
	@Autowired
	CiClaimPlatformLogService platformLogService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	CertifyService certifyService;
	@Autowired
	EndCasePubService endCaseService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	EndCasePubService endCasePubService;
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	
	
	/**
	 * 单证平台交互补传
	 * @param CiClaimPlatformLogVo
	 * @throws Exception 
	 */
	@Override
	public void uploadCertifyToPaltform(CiClaimPlatformLogVo logVo){
		String reqType = logVo.getRequestType();
		String bussNo = logVo.getBussNo();//bussNo -- 需控制为报案号（优先立案号）
		String comCode = logVo.getComCode();
		if("22".equals(comCode.substring(0,2))){//上海机构
			if(RequestType.CertifyBI_SH.getCode().equals(reqType)){//上海商业
				String comCodeBI = policyViewService.findPolicyComCode(bussNo, "12");
				if(comCodeBI != null && !"".equals(comCodeBI)){
					sendCertifyToPlatformSH_BI(bussNo);
				}
				
			}
			if(RequestType.CertifyCI_SH.getCode().equals(reqType)){//上海交强
				String comCodeCI = policyViewService.findPolicyComCode(bussNo, "11");
				if(comCodeCI != null && !"".equals(comCodeCI)){
					sendCertifyToPlatformSH_CI(bussNo);
				}
				;
			}
		}else{
			if(RequestType.CertifyBI.getCode().equals(reqType)){//全国商业
				sendCertifyToPlarformAll_BI(bussNo);
			}
			if(RequestType.CertifyCI.getCode().equals(reqType)){//全国交强
				sendCertifyToPlarformAll_CI(bussNo);
			}
		}
	}

	
	// 单证送平台
	@Override
	public void certifyToPaltform(String registNo,String riskCode){
		List<PrpLClaimVo> claimVoList = claimTaskService.findClaimListByRegistNo(registNo,"1");
		int flag = 0;
		Map<String,String> queryMap = new HashMap<String,String>();
		queryMap.put("registNo", registNo);
		queryMap.put("checkStatus",CheckStatus.Pass);// 审核通过
		// 查找审核通过的重开赔案
		List<PrpLReCaseVo> prpLReCaseVoList = endCasePubService.findReCaseVoListByqueryMap(queryMap);
		if(prpLReCaseVoList!=null&&prpLReCaseVoList.size()>0){// 重开
			flag = 1;
		}
		if(claimVoList != null && !claimVoList.isEmpty()){
			for(PrpLClaimVo claimVo : claimVoList){
				int value = 0;
				if(riskCode!=null&&!riskCode.equals(claimVo.getRiskCode())){
					// 只需要上传其中一个平台时使用
					continue;
				}
				for(PrpLReCaseVo prpLReCaseVo: prpLReCaseVoList){//判断是否重开，并且没结案
					if(claimVo.getEndCaseTime()==null && claimVo.getClaimNo().equals(prpLReCaseVo.getClaimNo())){
						value = 1; 
						break;
					}
				}
				if(flag == 0 || (flag == 1 && value == 1)) {
					if ("22".equals(claimVo.getComCode().substring(0, 2))) {// 上海平台
						if(isCertifyToSH(claimVo)){
							if (Risk.DQZ.equals(claimVo.getRiskCode())) {
								sendCertifyToPlatformSH_CI(registNo);
							} else {
								sendCertifyToPlatformSH_BI(registNo);
							}
						}
					} else {
						if (Risk.DQZ.equals(claimVo.getRiskCode())) {
							sendCertifyToPlarformAll_CI(registNo);
						} else {
							sendCertifyToPlarformAll_BI(registNo);
						}
					}
					
				}
			}
		}
//		List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(registNo);
//		for (PrpLCMainVo cMainVo : cMainVoList) {
//			if ("22".equals(cMainVo.getComCode().substring(0, 2))) {// 上海平台
//				if (Risk.DQZ.equals(cMainVo.getRiskCode())) {
//					sendCertifyToPlatformSH_CI(registNo);
//				} else {
//					sendCertifyToPlatformSH_BI(registNo);
//				}
//			} else {
//				if (Risk.DQZ.equals(cMainVo.getRiskCode())) {
//					sendCertifyToPlarformAll_CI(registNo);
//				} else {
//					sendCertifyToPlarformAll_BI(registNo);
//				}
//			}
//		}
	}
				

	//--------------------------------全国平台-----------------------------------//
	
	/**
	 * <pre>交强送全国平台</pre>
	 * @param registNo
	 * @modified:
	 * ☆Luwei(2016年9月23日 下午7:30:45): <br>
	 */
	private void sendCertifyToPlarformAll_CI(String registNo) {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		String comCode = policyViewService.findPolicyComCode(registNo,"11");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo
				(RequestType.RegistInfoCI.getCode(),registNo,comCode);
		if(logVo==null){
			logger.error("交强单证上传全国平台失败！该案件理赔编码不存在或报案未成功上传平台！");
			// return;
			// throw new IllegalArgumentException("该案件的报案环节送平台不存在成功的记录信息！");
		}
		String claimSeqNo = logVo==null ? "" : logVo.getClaimSeqNo();

		String validNo = policyViewService.findValidNo(registNo,"11");

		PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.CertifyCI);

		CiCertifyReqBodyVo bodyVo = new CiCertifyReqBodyVo();
		CiCertifyBasePartVo basePart = new CiCertifyBasePartVo();
		basePart.setConfirmSequenceNo(validNo);
		basePart.setClaimCode(claimSeqNo);
		basePart.setReportNo(registNo);
		Date endTime = certifyMainVo.getClaimEndTime();
		List<PrpLWfTaskVo> prpLWfTaskVos=wfFlowQueryService.findTaskVoForOutByNodeCode(registNo,FlowNode.ReOpen.name());
		if(endTime==null){
			endTime = certifyMainVo.getCreateTime();
		}else if(prpLWfTaskVos!=null && prpLWfTaskVos.size()>0){//重开后的案件取单证创建时间，因为重开后首次收集时间会被更新，导致理算核赔时间小于了单证收集齐全时间了
			endTime = certifyMainVo.getCreateTime();
		}
		basePart.setDocEndTime(endTime);
		basePart.setPaySelfFlag(checkVo.getIsClaimSelf());

		List<CiCertifyDocDetailDataVo> docDetailDataList = new ArrayList<CiCertifyDocDetailDataVo>();
		for(PrpLCertifyItemVo itemVo:certifyMainVo.getPrpLCertifyItems()){
			for(PrpLCertifyDirectVo directVo:itemVo.getPrpLCertifyDirects()){
				CiCertifyDocDetailDataVo docDetailDataVo = new CiCertifyDocDetailDataVo();
				String lossItemName = StringOperationUtil.cutOutString(directVo.getLossItemName(),30);
				docDetailDataVo.setDocName(lossItemName);
				docDetailDataVo.setDocType("2");
				docDetailDataVo.setRemark(directVo.getRemark());
				docDetailDataList.add(docDetailDataVo);
			}
		}
		bodyVo.setBasePart(basePart);
		bodyVo.setDocDetailDatas(docDetailDataList);
		if(SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"1"))){
			controller.callPlatform(bodyVo);
		}
	}

	/**
	 * <pre>商业险送平台</pre>
	 * @param registNo
	 * @modified:
	 * ☆Luwei(2016年9月23日 下午7:32:04): <br>
	 */
	private void sendCertifyToPlarformAll_BI(String registNo) {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		String comCode = policyViewService.findPolicyComCode(registNo,"12");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(RequestType.RegistInfoBI.getCode(),registNo,
				comCode);
		if(logVo==null){
			logger.error("商业单证上传全国平台失败！该案件理赔编码不存在或报案未成功上传平台！");
			// return;
		}
		String claimSeqNo = logVo==null ? "" : logVo.getClaimSeqNo();

		String validNo = policyViewService.findValidNo(registNo,"12");

		PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
//		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		PrpLSubrogationMainVo subrogationMainVo = subrogationService.find(registNo);
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.CertifyBI);

		BiCertifyReqBodyVo bodyVo = new BiCertifyReqBodyVo();
		BiCertifyBasePartVo basePart = new BiCertifyBasePartVo();
		basePart.setClaimSequenceNo(claimSeqNo);
		basePart.setClaimNotificationNo(registNo);
		basePart.setConfirmSequenceNo(validNo);
		Date endTime = certifyMainVo.getClaimEndTime();
		List<PrpLWfTaskVo> prpLWfTaskVos=wfFlowQueryService.findTaskVoForOutByNodeCode(registNo,FlowNode.ReOpen.name());
		if(endTime==null){
			endTime = certifyMainVo.getCreateTime();
		}else if(prpLWfTaskVos!=null && prpLWfTaskVos.size()>0){//重开后的案件取单证创建时间，因为重开后首次收集时间会被更新，导致理算核赔时间小于了单证收集齐全时间了
			endTime = certifyMainVo.getCreateTime();
		}
		basePart.setDocEndTime(endTime);

		// 是否代位
		basePart.setSubrogationFlag(subrogationMainVo.getSubrogationFlag());
		
		basePart.setSubCertiType("1");
		basePart.setSubClaimFlag(subrogationMainVo.getSubrogationFlag());

		List<BiCertifyDocDetailDataVo> docDetailDataList = new ArrayList<BiCertifyDocDetailDataVo>();
		for(PrpLCertifyItemVo itemVo:certifyMainVo.getPrpLCertifyItems()){
			for(PrpLCertifyDirectVo directVo:itemVo.getPrpLCertifyDirects()){
				
				if("C0201".equals(directVo.getLossItemCode())){
					basePart.setSubCertiType("1");
				}else if("C0202".equals(directVo.getLossItemCode())){
					basePart.setSubCertiType("2");
				}else if("C0203".equals(directVo.getLossItemCode())){
					basePart.setSubCertiType("3");
				}else if("C0102".equals(directVo.getLossItemCode())){
//					basePart.setSubClaimFlag("1");
				}
				
				BiCertifyDocDetailDataVo docDetailDataVo = new BiCertifyDocDetailDataVo();
				String lossItemName = StringOperationUtil.cutOutString(directVo.getLossItemName(),30);
				docDetailDataVo.setDocName(lossItemName);
				docDetailDataVo.setDocType("2");
				docDetailDataVo.setRemark(directVo.getRemark());
				docDetailDataList.add(docDetailDataVo);
			}
		}

		// 代位信息列表
		List<BiCertifySubrogationDataVo> subrogationDataVos = new ArrayList<BiCertifySubrogationDataVo>();
		if("1".equals(subrogationMainVo.getSubrogationFlag())){
			if(subrogationMainVo.getPrpLSubrogationCars()!=null&&subrogationMainVo.getPrpLSubrogationCars().size()>0){
				for(PrpLSubrogationCarVo carVo:subrogationMainVo.getPrpLSubrogationCars()){
					BiCertifySubrogationDataVo subrogationDataVo = new BiCertifySubrogationDataVo();
					subrogationDataVo.setLinkerName(carVo.getLinkerName());
					subrogationDataVo.setLicensePlateNo(carVo.getLicenseNo());
					subrogationDataVo.setLicensePlateType(carVo.getLicenseType());
					subrogationDataVo.setVIN(carVo.getVinNo());
					subrogationDataVo.setEngineNo(carVo.getEngineNo());
					subrogationDataVo.setCaInsurerCode(carVo.getBiInsurerCode());
					subrogationDataVo.setCaInsurerArea(carVo.getBiInsurerArea());
					subrogationDataVo.setIaInsurerCode(carVo.getCiInsurerCode());
					subrogationDataVo.setIaInsurerArea(carVo.getCiInsurerArea());
					subrogationDataVos.add(subrogationDataVo);
				}
			}

			if(subrogationMainVo.getPrpLSubrogationPersons()!=null&&subrogationMainVo.getPrpLSubrogationPersons()
					.size()>0){
				for(PrpLSubrogationPersonVo personVo:subrogationMainVo.getPrpLSubrogationPersons()){
					BiCertifySubrogationDataVo subrogationDataVo = new BiCertifySubrogationDataVo();
					subrogationDataVo.setLinkerName(personVo.getName());
					subrogationDataVos.add(subrogationDataVo);
				}
			}
		}

		bodyVo.setBasePart(basePart);
		bodyVo.setDocDetailDataList(docDetailDataList);
		bodyVo.setSubrogationDataList(subrogationDataVos);
		if(SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"2"))){
			controller.callPlatform(bodyVo);
		}
	}
	
	//--------------------------------全国平台结束-----------------------------------//
	
	
	
	//--------------------------------上海平台开始-----------------------------------//
	
	/**
	 * 上海平台：true;//没有成功或者没有,false-存在成功的，不需要上传
	 * @param claimVo
	 */
	public boolean isCertifyToSH(PrpLClaimVo claimVo){
		boolean val = true;
//		String registNo = claimVo.getRegistNo();
//		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(reqType,registNo,comCode);
//		if(logVo != null){//判断是否存在成功的数据
//			val = false;//存在成功或者已上传的
//		}
		List<PrpLEndCaseVo> endCaseVoList = endCaseService.queryAllByRegistNo(claimVo.getRegistNo());
		if(endCaseVoList != null && endCaseVoList.size() > 0){
			val = false;//已存在结案，上海平台重开不需要再次送单证
		}
		return val;
	}
	
	/**
	 * 发送交强单证--上海平台
	 * @param registNo
	 */
	private void sendCertifyToPlatformSH_CI(String registNo) {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		String comCode = policyViewService.findPolicyComCode(registNo, "11");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(
				RequestType.RegistInfoCI_SH.getCode(), registNo, comCode);
		if (logVo == null) {
			logger.error("交强单证上传上海平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
		String claimSeqNo = logVo == null ? "" : logVo.getClaimSeqNo();

		PrpLCertifyMainVo certifyMainVo = certifyPubService
				.findPrpLCertifyMainVoByRegistNo(registNo);

		PlatformController controller = PlatformFactory.getInstance(comCode,
				RequestType.CertifyCI_SH);

		SHCiCertifyReqBodyVo bodyVo = new SHCiCertifyReqBodyVo();
		SHCiCertifyReqBasePartVo basePart = new SHCiCertifyReqBasePartVo();
		basePart.setClaimCode(claimSeqNo);
		basePart.setReportNo(registNo);
		basePart.setDocStartTime(certifyMainVo.getStartTime());
		basePart.setDocEndTime(certifyMainVo.getEndTime());

		List<SHCiCertifyReqDocDataVo> douDataVos = new ArrayList<SHCiCertifyReqDocDataVo>();
		for (PrpLCertifyItemVo itemVo : certifyMainVo.getPrpLCertifyItems()) {
			if(itemVo.getPrpLCertifyDirects()!=null&&itemVo.getPrpLCertifyDirects().size()>0){
				for (PrpLCertifyDirectVo directVo : itemVo.getPrpLCertifyDirects()) {
					SHCiCertifyReqDocDataVo douDataVo = new SHCiCertifyReqDocDataVo();
					String lossItemName = StringOperationUtil.cutOutString(directVo.getLossItemName(),30);
					douDataVo.setDocName(lossItemName);
					douDataVos.add(douDataVo);
				}
			}
		}
		List<SHCiCertifyReqDisputeDataVo> disputeDataVos = new ArrayList<SHCiCertifyReqDisputeDataVo>();
		PrpLSubrogationMainVo subrogationMainVo = subrogationService.find(registNo);
		if(subrogationMainVo!=null){
			SHCiCertifyReqDisputeDataVo disputeDataVo = new SHCiCertifyReqDisputeDataVo();
			disputeDataVo.setDisputeFlag(subrogationMainVo.getDisputeFlag());
			disputeDataVos.add(disputeDataVo);
		}

		bodyVo.setBasePart(basePart);
		bodyVo.setDocDatas(douDataVos);
		bodyVo.setDisputeDatas(disputeDataVos);

		controller.callPlatform(bodyVo);
	}
	
	/**
	 * 发送商业单证--上海平台
	 * @param registNo
	 */
	private void sendCertifyToPlatformSH_BI(String registNo){
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		String comCode = policyViewService.findPolicyComCode(registNo, "12");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(
				RequestType.RegistInfoBI_SH.getCode(), registNo, comCode);
		if (logVo == null) {
			logger.error("交强单证上传上海平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
		String claimSeqNo = logVo == null ? "" : logVo.getClaimSeqNo();

		PrpLCertifyMainVo certifyMainVo = certifyPubService
				.findPrpLCertifyMainVoByRegistNo(registNo);

		PlatformController controller = PlatformFactory.getInstance(comCode,
				RequestType.CertifyBI_SH);
		
		SHBiCertifyReqBodyVo bodyVo=new SHBiCertifyReqBodyVo();
		SHBiCertifyReqBasePartVo basePart=new SHBiCertifyReqBasePartVo();
		basePart.setClaimCode(claimSeqNo);
		basePart.setReportNo(registNo);
		basePart.setDocStartTime(certifyMainVo.getStartTime());
		basePart.setDocEndTime(certifyMainVo.getEndTime());
		
		List<SHBiCertifyReqDocDataVo> douDataVos=new ArrayList<SHBiCertifyReqDocDataVo>();
		for(PrpLCertifyItemVo itemVo : certifyMainVo.getPrpLCertifyItems()){
			if(itemVo.getPrpLCertifyDirects()!=null&&itemVo.getPrpLCertifyDirects().size()>0){
				for(PrpLCertifyDirectVo directVo : itemVo.getPrpLCertifyDirects()){
					SHBiCertifyReqDocDataVo douDataVo=new SHBiCertifyReqDocDataVo();
					String lossItemName = StringOperationUtil.cutOutString(directVo.getLossItemName(),30);
					douDataVo.setDocName(lossItemName);
					douDataVos.add(douDataVo);
				}
			}
		}
		
		List<SHBiCertifyReqDisputeDataVo> disputeDataVos=new ArrayList<SHBiCertifyReqDisputeDataVo>();
		PrpLSubrogationMainVo subrogationMainVo = subrogationService.find(registNo);
		if(subrogationMainVo!=null){
			SHBiCertifyReqDisputeDataVo disputeDataVo=new SHBiCertifyReqDisputeDataVo();
			disputeDataVo.setDisputeFlag(subrogationMainVo.getDisputeFlag());
			disputeDataVos.add(disputeDataVo);
		}
		
		bodyVo.setBasePart(basePart);
		bodyVo.setDocDatas(douDataVos);
		bodyVo.setDisputeDatas(disputeDataVos);
		
		controller.callPlatform(bodyVo);
	}
	
	//--------------------------------上海平台结束-----------------------------------//
}
