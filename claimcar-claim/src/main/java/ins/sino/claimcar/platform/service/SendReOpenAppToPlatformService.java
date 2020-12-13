package ins.sino.claimcar.platform.service;

import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.spring.EndCasePubServiceImpl;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.platform.vo.BIReOpenAppReqBasePartVo;
import ins.sino.claimcar.platform.vo.BIReOpenAppReqBodyVo;
import ins.sino.claimcar.platform.vo.CIReOpenAppReqBasePartVo;
import ins.sino.claimcar.platform.vo.CIReOpenAppReqBodyVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.reopencase.service.ReOpenCaseServiceImpl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("reOpenAppToPlatformService")
public class SendReOpenAppToPlatformService {

	private Logger logger = LoggerFactory
			.getLogger(SendReOpenAppToPlatformService.class);

	@Autowired
	ReOpenCaseServiceImpl reOpenCaseService;

	@Autowired
	PolicyViewService policyViewService;

	@Autowired
	CiClaimPlatformLogService platformLogService;
	
	@Autowired
	EndCasePubServiceImpl endCasePubService;
	
	@Autowired
	RegistQueryService registQueryService;

	/**
	 * 平台交互补传
	 * @param logVo
	 */
	public void sendReOpenAppToPlatform(CiClaimPlatformLogVo logVo) {
		String registNo = logVo.getBussNo();
		String requestType = logVo.getRequestType();
		List<PrpLEndCaseVo> endCaseVoList = endCasePubService
				.queryAllByRegistNo(registNo);
		String ciEndCaseNo = "";
		String biEndCaseNo = "";
		if (endCaseVoList == null)
			return;
		for (PrpLEndCaseVo endCaseVo : endCaseVoList) {
			String endCaseNo = endCaseVo.getEndCaseNo();
			if (Risk.DQZ.equals(endCaseVo.getRiskCode())
					&&RequestType.ReOpenAppCI.getCode().equals(requestType)) {
				ciEndCaseNo = endCaseNo;
			}
			if(!Risk.DQZ.equals(endCaseVo.getRiskCode())
					&&RequestType.ReOpenAppBI.getCode().equals(requestType)){
				biEndCaseNo = endCaseNo;
			}
		}

		if (RequestType.ReOpenAppCI.getCode().equals(logVo.getRequestType())) {
			// 交强
			this.sendCIReOpenAppToPlatform(ciEndCaseNo);
		} else {
			this.sendBIReOpenAppToPlatform(biEndCaseNo);
		}

	}

	/**
	 * 组装交强报文
	 * @param endCaseNo-结案号
	 * @return
	 */
	public void sendCIReOpenAppToPlatform(String endCaseNo) {
		PrpLReCaseVo reCaseVo = reOpenCaseService
				.findReCaseVoByEndCaseNo(endCaseNo);
		String registNo = reCaseVo.getRegistNo();
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		String comCode = policyViewService.findPolicyComCode(registNo, "11");
		String validNo = policyViewService.findValidNo(registNo, "11");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(
				RequestType.RegistInfoCI.getCode(),registNo,comCode);
		String claimSeqNo = logVo == null ? "" : logVo.getClaimSeqNo();
		logger.info("claimSeqNo:" + claimSeqNo);

		PlatformController controller = PlatformFactory.getInstance(comCode,
				RequestType.ReOpenAppCI);

		// Body
		CIReOpenAppReqBodyVo bodyVo = new CIReOpenAppReqBodyVo();

		CIReOpenAppReqBasePartVo basePartVo = new CIReOpenAppReqBasePartVo();
		basePartVo.setConfirmSequenceNo(validNo);
		basePartVo.setClaimCode(claimSeqNo);
		basePartVo.setRegistNo(registNo);
		basePartVo.setReOpenCause(reCaseVo.getOpenReason());
		Date reOpenDate = reCaseVo.getUpdateTime();
		if(reOpenDate == null){
			reOpenDate = reCaseVo.getDealCaseDate();
			if(reOpenDate == null){
				reOpenDate = new Date();
			}
		}
		basePartVo.setReOpenDate(reOpenDate);

		bodyVo.setBasePartVo(basePartVo);

		if(SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"1"))){
			controller.callPlatform(bodyVo);
		}
	}

	/**
	 * 组装商业报文
	 * @param endCaseNo--结案号
	 * @return
	 */
	public void sendBIReOpenAppToPlatform(String endCaseNo) {
		PrpLReCaseVo reCaseVo = reOpenCaseService
				.findReCaseVoByEndCaseNo(endCaseNo);
		String registNo = reCaseVo.getRegistNo();
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		String comCode = policyViewService.findPolicyComCode(registNo, "12");
		String validNo = policyViewService.findValidNo(registNo, "12");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(
				RequestType.RegistInfoBI.getCode(),registNo,comCode);
		String claimSeqNo = logVo == null ? "" : logVo.getClaimSeqNo();

		PlatformController controller = PlatformFactory.getInstance(comCode,
				RequestType.ReOpenAppBI);

		// BODY
		BIReOpenAppReqBodyVo bodyVo = new BIReOpenAppReqBodyVo();

		BIReOpenAppReqBasePartVo basePartVo = new BIReOpenAppReqBasePartVo();

		basePartVo.setConfirmSequenceNo(validNo);
		basePartVo.setClaimCode(claimSeqNo);
		basePartVo.setRegistNo(reCaseVo.getRegistNo());
		basePartVo.setReOpenCause(reCaseVo.getOpenReason());
		Date reOpenDate = reCaseVo.getUpdateTime();
		if(reOpenDate == null){
			reOpenDate = reCaseVo.getDealCaseDate();
			if(reOpenDate == null){
				reOpenDate = new Date();
			}
		}
		basePartVo.setReOpenDate(reOpenDate);

		bodyVo.setBasePartVo(basePartVo);

		if(SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"2"))){
			controller.callPlatform(bodyVo);
		}
	}

}
