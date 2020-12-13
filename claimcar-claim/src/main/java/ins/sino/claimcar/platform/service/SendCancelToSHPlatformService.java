package ins.sino.claimcar.platform.service;

import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.platform.vo.sh.SHBIClaimCancelReqBasePartyVo;
import ins.sino.claimcar.platform.vo.sh.SHBIClaimCancelReqBodyVo;
import ins.sino.claimcar.platform.vo.sh.SHCIClaimCancelReqBasePartyVo;
import ins.sino.claimcar.platform.vo.sh.SHCIClaimCancelReqBodyVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 案件注销-上海平台
 * @author CHENMQ
 *
 */
@Service("sendCancelToSHPlatformService")
public class SendCancelToSHPlatformService {
	
	@Autowired
	ClaimTaskService claimTaskService;
	
	@Autowired
	CiClaimPlatformLogService platformLogService;
	
	@Autowired
	PolicyViewService policyViewService;
	
	@Autowired
	RegistToPaltformService registToPaltformService;
	
	@Autowired
	RegistQueryService registQueryService;
	
	private Logger logger = LoggerFactory.getLogger(SendClaimToSHPlatformService.class);
	
	/**
	 * 案件注销--报案注销,保单关联和注销
	 * @param policyInfo-保单信息
	 * @param cancelType--注销类型（1,注销当前保单-2,关联该保单）
	 */
	public void sendCancelToPlatform(PrpLCMainVo policyInfo, String cancelType) {
		String registNo = policyInfo.getRegistNo();
		if ("1".equals(cancelType)) {// 保单注销
			if (Risk.DQZ.equals(policyInfo.getRiskCode())) {
				sendCancelToPlatformCI_SH(registNo);
			} else {
				sendCancelToPlatformBI_SH(registNo);
			}
		} else {// 保单关联
			registToPaltformService.sendRegistCancelToPlatform
			(registNo,policyInfo.getPolicyNo());
		}
	}
	
	/**
	 * 立案注销送平台
	 * @param registNo
	 * @param claimNo
	 */
	public void sendClaimCancelTo_SH(String claimNo){
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
		if(claimVo==null)return;
		String registNo = claimVo.getRegistNo();
//		Double claimAmount = claimVo.getSumDefLoss().doubleValue();
		if(Risk.DQZ.equals(claimVo.getRiskCode())){
			sendCancelToPlatformCI_SH(registNo);
		}else{
			sendCancelToPlatformBI_SH(registNo);
		}
	}
	
	
	/**
	 * 组装案件注销的商业报文
	 * @param claimDemandVo
	 * @param claimVo
	 * @return
	 */
	public void sendCancelToPlatformBI_SH(String registNo){
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
//		String registNo = claimVo.getRegistNo();
		String comCode = policyViewService.findPolicyComCode(registNo,"12");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo
				(RequestType.RegistInfoBI_SH.getCode(),registNo,comCode);
		if (logVo == null) {
			logger.info("案件注销上传上海平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
		String claimSeqNo = logVo == null ? "" : logVo.getClaimSeqNo();
		String validNo = policyViewService.findValidNo(registNo,"12");
		logger.info("投保确认码："+validNo);
		
		PlatformController controller = PlatformFactory.getInstance
				(comCode,RequestType.CancelInfoBI_SH);
		
		SHBIClaimCancelReqBodyVo bodyVo = new SHBIClaimCancelReqBodyVo();
		
		SHBIClaimCancelReqBasePartyVo basePartyVo = new SHBIClaimCancelReqBasePartyVo();
		basePartyVo.setClaimCode(claimSeqNo);
		basePartyVo.setReportNo(registNo);
		basePartyVo.setRejectReason("客户放弃索赔！");//拒赔理由
		basePartyVo.setDirectClaimAmount(0.0);
		basePartyVo.setComCancelTime(new Date());
		
		bodyVo.setBasePartVo(basePartyVo);
		
		controller.callPlatform(bodyVo);
	}
	
	/**
	 * 组装案件注销的交强报文
	 * @param claimDemandVo
	 * @param claimVo
	 * @return
	 */
	public void sendCancelToPlatformCI_SH(String registNo){
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		String comCode = policyViewService.findPolicyComCode(registNo,"11");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo
				(RequestType.RegistInfoCI_SH.getCode(),registNo,comCode);
		if (logVo == null) {
			logger.info("案件注销上传上海平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
		String claimSeqNo = logVo == null ? "" : logVo.getClaimSeqNo();
		String validNo = policyViewService.findValidNo(registNo,"11");
		logger.info("投保确认码："+validNo);
		
		PlatformController controller = PlatformFactory.getInstance
				(comCode,RequestType.CancelInfoCI_SH);
		SHCIClaimCancelReqBodyVo bodyVo = new SHCIClaimCancelReqBodyVo();
		
		SHCIClaimCancelReqBasePartyVo basePartyVo = new SHCIClaimCancelReqBasePartyVo();
		basePartyVo.setReportNo(registNo);
		basePartyVo.setClaimCode(claimSeqNo);
		basePartyVo.setDirectclaimamount(0.0);
		basePartyVo.setRejectReason("客户放弃索赔！");//拒赔理由
		basePartyVo.setComCancelTime(new Date());
		
		bodyVo.setBasePartyVo(basePartyVo);
		
		controller.callPlatform(bodyVo);
	}

}
