/******************************************************************************
* CREATETIME : 2016年5月30日 上午11:08:41
******************************************************************************/
package ins.sino.claimcar.platform.service;

import ins.platform.common.service.facade.CodeTranService;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.LossParty;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.platform.vo.sh.SHBIClaimReqBasePartVo;
import ins.sino.claimcar.platform.vo.sh.SHBIClaimReqBodyVo;
import ins.sino.claimcar.platform.vo.sh.SHBIClaimReqObjDataVo;
import ins.sino.claimcar.platform.vo.sh.SHBIClaimReqThirdVehicleDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCIClaimReqBasePartVo;
import ins.sino.claimcar.platform.vo.sh.SHCIClaimReqBodyVo;
import ins.sino.claimcar.platform.vo.sh.SHCIClaimReqObjDataVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPersonLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 立案--上海机构案件--送上海平台服务类
 * @author ★Luwei
 */
@Service("claimToSHPlatformService")
public class SendClaimToSHPlatformService {

	private Logger logger = LoggerFactory.getLogger(SendClaimToSHPlatformService.class);

	@Autowired
	CodeTranService codeTranService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	RegistQueryService registService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	CiClaimPlatformLogService platformLogService;
	
	
	/**
	 * 立案送上海平台，registNo--报案号，reqType--请求类型（交强/商业）[BI-CI]
	 * @param registNo
	 * @param claimNo
	 */
	public void sendClaimToPlatFormSH(String registNo,String claimNo){
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
		String classType = Risk.DQZ.equals(claimVo.getRiskCode())?"11":"12";
		String comCode = policyViewService.findPolicyComCode(registNo,classType);
		
		String code = Risk.DQZ.equals(claimVo.getRiskCode())?
		RequestType.RegistInfoCI_SH.getCode():RequestType.RegistInfoBI_SH.getCode();
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(code,registNo,comCode);
		if (logVo == null) {
			logger.info("立案上传上海平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
		String claimSeqNo = logVo==null?"":logVo.getClaimSeqNo();
		String validNo = policyViewService.findValidNo(registNo,classType);
		
		PlatformController controller = null;
		if (Risk.DQZ.equals(claimVo.getRiskCode())) {// 交强
			controller = PlatformFactory.getInstance(comCode,RequestType.ClaimInfoCI_SH);
			SHCIClaimReqBodyVo request_BodyVo = this.setBodyCI_SH(claimSeqNo,validNo,claimVo);
			// 发送报文
			controller.callPlatform(request_BodyVo);
		} else {// 商业
			controller = PlatformFactory.getInstance(comCode,RequestType.ClaimInfoBI_SH);
			SHBIClaimReqBodyVo request_BodyVo = this.setBodyBI_SH(claimSeqNo,validNo,claimVo);
			// 发送报文
			controller.callPlatform(request_BodyVo);
		}
		// 接收
//		CiResponseHeadVo resHeadVo = controller.getHeadVo(CiResponseHeadVo.class);
//		if (!"1".equals(resHeadVo.getResponseCode())) {
//			logger.info("立案上传上海平台失败！");
//			// throw new RuntimeException("立案上传上海平台失败！");
//			// String msg = resHeadVo.getErrorDesc();
//			// throw new BusinessException("风险预警",msg);
//		}

	}
	
	/**
	 * <pre>上海平台补送</pre>
	 * @param registNo
	 * @param reqType
	 * @modified:
	 * ☆Luwei(2016年7月28日 上午11:19:27): <br>
	 */
	public void sendClaimToSH(String registNo,String reqType){
		PrpLRegistVo registVo = registService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		List<PrpLClaimVo> claimVoList = 
		claimTaskService.findClaimListByRegistNo(registNo,"1");
		PrpLClaimVo claimVo = null;
		if(claimVoList!=null&&claimVoList.size()>0){
			for(PrpLClaimVo claimVoTemp : claimVoList){
				if(Risk.DQZ.equals(claimVoTemp.getRiskCode())
					&&RequestType.ClaimInfoCI_SH.getCode().equals(reqType)){
					claimVo = claimVoTemp;
				}
				if(!Risk.DQZ.equals(claimVoTemp.getRiskCode())
						&&RequestType.ClaimInfoBI_SH.getCode().equals(reqType)){
					claimVo = claimVoTemp;
				}
			}	
		}
		if(claimVo==null){return;}
		
		String classType = Risk.DQZ.equals(claimVo.getRiskCode())?"11":"12";
		String comCode = policyViewService.findPolicyComCode(registNo,classType);
		
		String code = Risk.DQZ.equals(claimVo.getRiskCode()) ? RequestType.RegistInfoCI_SH.getCode() 
				: RequestType.RegistInfoBI_SH.getCode();
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(code,registNo,comCode);
		if (logVo == null) {
			logger.info("立案上传上海平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
		String claimSeqNo = logVo==null?"":logVo.getClaimSeqNo();
		
		String validNo = policyViewService.findValidNo(registNo,classType);
		
		PlatformController controller = null;
		if (Risk.DQZ.equals(claimVo.getRiskCode())) {// 交强
			controller = PlatformFactory.getInstance(comCode,RequestType.ClaimInfoCI_SH);
			SHCIClaimReqBodyVo request_BodyVo = this.setBodyCI_SH(claimSeqNo,validNo,claimVo);
			// 发送报文
			controller.callPlatform(request_BodyVo);
		} else {// 商业
			controller = PlatformFactory.getInstance(comCode,RequestType.ClaimInfoBI_SH);
			SHBIClaimReqBodyVo request_BodyVo = this.setBodyBI_SH(claimSeqNo,validNo,claimVo);
			// 发送报文
			controller.callPlatform(request_BodyVo);
		}
	}
	
	
	/**
	 * 组装立案的交强报文
	 * @param registNo
	 * @return
	 * @modified:
	 */
	public SHCIClaimReqBodyVo setBodyCI_SH(String claimSeqNo,String validNo,PrpLClaimVo claimVo){
//		PrpLCheckDutyVo check = checkTaskService.findCheckDuty(claimVo.getRegistNo(),1);
		PrpLCheckDutyVo dutyVo = checkTaskService.findCheckDuty(claimVo.getRegistNo(),1);
		PrpLRegistVo registVo = registService.findByRegistNo(claimVo.getRegistNo());
		PrpLCMainVo cMainVo = policyViewService.findPolicyInfoByPaltform(claimVo.getRegistNo(),claimVo.getPolicyNo());
		PrpLCItemCarVo carVo = null;
		if(cMainVo != null){
			carVo = cMainVo.getPrpCItemCars().get(0);
		}
		
		Integer i = 0;Integer y = 0;
		for(PrpLRegistPersonLossVo personVo : registVo.getPrpLRegistPersonLosses()){
			i += personVo.getDeathcount()==null ? 0 : personVo.getDeathcount();
			y += personVo.getDeathcount()==null ? 0 : personVo.getDeathcount();
			y += personVo.getInjuredcount()==null ? 0 : personVo.getInjuredcount();
		}
		
		//bodyVo
		SHCIClaimReqBodyVo bodyVo = new SHCIClaimReqBodyVo();
		
		SHCIClaimReqBasePartVo basePartVo = new SHCIClaimReqBasePartVo();
		basePartVo.setConfirmSequenceNo(validNo);
		basePartVo.setClaimCode(claimSeqNo);
		basePartVo.setEstimateAmount(NullToZero(claimVo.getSumClaim()).doubleValue());
		basePartVo.setReportNo(claimVo.getRegistNo());
		basePartVo.setClaimNo(claimVo.getClaimNo());
		basePartVo.setRegistrationDate(claimVo.getClaimTime());
		basePartVo.setClaimType(dutyVo==null ? "1" : "0".equals(dutyVo.getCiDutyFlag()) ? "2" : i >= 1 ? "0" : "1");
		basePartVo.setCarMark(carVo == null ? toTrimLicno(dutyVo.getLicenseNo()) : toTrimLicno(carVo.getLicenseNo()));
		basePartVo.setVehicleType(carVo == null ? "02" : carVo.getLicenseKindCode());
		basePartVo.setAccidentTime(claimVo.getDamageTime());
		basePartVo.setManageType(null);
		basePartVo.setPolicyPlace(StringUtils.isEmpty(registVo.getIsoffSite()) ? "1" : registVo.getIsoffSite());
		basePartVo.setSubrogationFlag(claimVo.getIsSubRogation());
		basePartVo.setPersonNum(y);
		
		/*牛强 2017-03*16 立案接口 送出险原因 出险经过*/
		basePartVo.setAccidentDescription(registVo.getPrpLRegistExt().getDangerRemark());
		basePartVo.setAccidentReason(registVo.getAccidentReason());   
	
		
		//损失情况列表
		List<SHCIClaimReqObjDataVo> objDataListVo = new ArrayList<SHCIClaimReqObjDataVo>();
		for(PrpLRegistCarLossVo registCarLossVo : registVo.getPrpLRegistCarLosses()){
			if(LossParty.THIRD.equals(registCarLossVo.getLossparty())){
				SHCIClaimReqObjDataVo objDataVo = new SHCIClaimReqObjDataVo();
				objDataVo.setObjName(toTrimLicno(registCarLossVo.getLicenseNo()));
				String licType = registCarLossVo.getLicenseType();
				if(StringUtils.isEmpty(licType)){
					licType = "02";
				}
				objDataVo.setVehicleType(licType);
				objDataVo.setObjType("1");
				
				objDataListVo.add(objDataVo);
			}
		}
		
		bodyVo.setBasePartVo(basePartVo);
		bodyVo.setObjDataVo(objDataListVo);
		bodyVo.setSubrogationDataVo(null);
		bodyVo.setDisputeDataVo(null);
		return bodyVo;
	}
	
	/**
	 * 组装立案的商业报文
	 * @param registNo
	 * @return
	 * @modified:
	 */
	public SHBIClaimReqBodyVo setBodyBI_SH(String claimSeqNo,String validNo,PrpLClaimVo claimVo){
		PrpLCheckDutyVo check = checkTaskService.findCheckDuty(claimVo.getRegistNo(),1);
		PrpLRegistVo registVo = registService.findByRegistNo(claimVo.getRegistNo());
		PrpLCMainVo cMainVo = policyViewService.findPolicyInfoByPaltform(claimVo.getRegistNo(),claimVo.getPolicyNo());
		PrpLCItemCarVo carVo = null;
		if(cMainVo != null){
			carVo = cMainVo.getPrpCItemCars().get(0);
		}
		Integer i = 0;Integer y = 0;
		for(PrpLRegistPersonLossVo personVo : registVo.getPrpLRegistPersonLosses()){
			i += personVo.getDeathcount()==null ? 0 : personVo.getDeathcount();
			y += personVo.getDeathcount()==null ? 0 : personVo.getDeathcount();
			y += personVo.getInjuredcount()==null ? 0 : personVo.getInjuredcount();
		}
		//bodyVo
		SHBIClaimReqBodyVo bodyVo = new SHBIClaimReqBodyVo();
		
		SHBIClaimReqBasePartVo basePartVo = new SHBIClaimReqBasePartVo();
		basePartVo.setConfirmSequenceNo(validNo);
		basePartVo.setClaimCode(claimSeqNo);
		basePartVo.setReportNo(claimVo.getRegistNo());
		basePartVo.setAccidentTime(registVo.getDamageTime());
		basePartVo.setRegistrationDate(claimVo.getClaimTime());
		basePartVo.setAccidentDescription(registVo.getPrpLRegistExt().getDangerRemark());
		basePartVo.setCarMark(carVo == null ? toTrimLicno(check.getLicenseNo()):toTrimLicno(carVo.getLicenseNo()));
		basePartVo.setVehicleType(carVo == null ? "02" : carVo.getLicenseKindCode());
		basePartVo.setPolicyNo(claimVo.getPolicyNo());
		basePartVo.setEstimate(claimVo.getSumClaim().toString());
		basePartVo.setReporter(registVo.getReportorName());
		basePartVo.setOptionType(null);
		SysCodeDictVo sysCodeDictVo = codeTranService.findTransCodeDictVo("AccidentManageType",
				registVo.getPrpLRegistExt().getManageType());
		basePartVo.setOptionType("1".equals(registVo.getPrpLRegistExt().getIsClaimSelf()) ? "5" : sysCodeDictVo.getProperty2());// 上海事故处理类型
		basePartVo.setAccidentReason(registVo.getAccidentReason());// 上海平台的需要匹配--对应
		basePartVo.setPolicyPlace(StringUtils.isEmpty(registVo.getIsoffSite()) ? "1" : registVo.getIsoffSite());
		basePartVo.setSubrogationFlag(claimVo.getIsSubRogation());
		basePartVo.setPersonNum(y);
		
		// 第三方车辆情况（多条）
		List<SHBIClaimReqThirdVehicleDataVo> thirdVehicleDataVos = new ArrayList<SHBIClaimReqThirdVehicleDataVo>();
		// 损失情况列表（多条）
		List<SHBIClaimReqObjDataVo> objDataListVo = new ArrayList<SHBIClaimReqObjDataVo>();

		for(PrpLRegistCarLossVo lossVo:registVo.getPrpLRegistCarLosses()){
			String licType = lossVo.getLicenseType();
			if(StringUtils.isEmpty(licType)){
				licType = "02";
			}
			if(LossParty.THIRD.equals(lossVo.getLossparty())){
				SHBIClaimReqThirdVehicleDataVo thirdVehicleDataVo = new SHBIClaimReqThirdVehicleDataVo();

				thirdVehicleDataVo.setCarMark(toTrimLicno(lossVo.getLicenseNo()));
				thirdVehicleDataVo.setVehicleType(licType);
				thirdVehicleDataVo.setVehicleCategory("");
				String InspolicyBI = lossVo.getInspolicybi();
				String company = StringUtils.isEmpty(InspolicyBI) ? "" : codeTranService.findTransCodeDictVo(
						"CIInsurerCompany",InspolicyBI).getProperty2();
				thirdVehicleDataVo.setCompanyCode(company);
				thirdVehicleDataVo.setPolicyNo("");
				thirdVehicleDataVo.setName(lossVo.getThriddrivername());
				thirdVehicleDataVo.setCertiCode("");
				thirdVehicleDataVo.setLicenseNo("");

				thirdVehicleDataVos.add(thirdVehicleDataVo);
			}else{
				SHBIClaimReqObjDataVo objDataVo = new SHBIClaimReqObjDataVo();
				objDataVo.setObjName(toTrimLicno(lossVo.getLicenseNo()));
				objDataVo.setVehicleType(licType);
				objDataVo.setObjType("1");
				objDataVo.setMainThird("1");

				objDataListVo.add(objDataVo);
			}
		}
		
		bodyVo.setBasePartVo(basePartVo);
		bodyVo.setThirdVehicleDataVo(thirdVehicleDataVos);
		bodyVo.setObjDataVo(objDataListVo);
		bodyVo.setSubrogationDataVo(null);
		bodyVo.setDisputeDataVo(null);
		return bodyVo;
	}
	
	
	public static BigDecimal NullToZero(BigDecimal strNum) {
		return strNum==null ? new BigDecimal("0") : strNum;
	}
	public static void main(String[] args) {
		System.out.println(new BigDecimal(17802.00).toString());
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
