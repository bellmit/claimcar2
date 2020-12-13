package ins.sino.claimcar.platform.service.spring;

import ins.platform.utils.DataUtils;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformTaskService;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformTaskVo;
import ins.sino.claimcar.claim.service.ClaimCancelService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLcancelTraceVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.platform.service.ClaimToPaltformService;
import ins.sino.claimcar.platform.service.SendCancelToPlatformService;
import ins.sino.claimcar.platform.service.SendCancelToSHPlatformService;
import ins.sino.claimcar.platform.service.SendClaimToPlatformService;
import ins.sino.claimcar.platform.service.SendClaimToSHPlatformService;
import ins.sino.claimcar.platform.service.SendEndCaseAddToSHPlatformService;
import ins.sino.claimcar.platform.service.SendEndCaseToPlatformService;
import ins.sino.claimcar.platform.service.SendEndCaseToSHPlatformService;
import ins.sino.claimcar.platform.service.SendPaymentToPlatformService;
import ins.sino.claimcar.platform.service.SendPaymentToSHPlatformService;
import ins.sino.claimcar.platform.service.SendReOpenAppToPlatformService;
import ins.sino.claimcar.platform.service.SendVClaimToPlatformService;
import ins.sino.claimcar.platform.service.SendVClaimToSHPlatformService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.trafficplatform.service.RegistToCarRiskPaltformService;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 立案、案件注销、理算、赔款支付送平台接口实现
 * @author Luwei
 * @CreateTime
 */
@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" }, timeout = 1000000)
@Path("claimToPaltformService")
public class ClaimToPaltformServiceImpl implements ClaimToPaltformService {

	private Logger logger = LoggerFactory.getLogger(ClaimToPaltformServiceImpl.class);
	
	@Autowired
	SendClaimToPlatformService sendClaimToAll;

	@Autowired
	SendClaimToSHPlatformService sendClaimToSH;
	
	@Autowired
	SendCancelToPlatformService cancelToAll;
	
	@Autowired
	SendCancelToSHPlatformService cancelToSH;
	
	@Autowired
	SendVClaimToPlatformService sendVClaimToAll;
	
	@Autowired
	SendVClaimToSHPlatformService sendVClaimToSH;
	
	@Autowired
	SendPaymentToPlatformService paymentToAll;
	@Autowired
	SendPaymentToSHPlatformService paymentToSH;
	@Autowired
	SendEndCaseToPlatformService sendEndCaseToAll;
	@Autowired
	SendEndCaseToSHPlatformService sendEndCaseToSH;
	// 结案追加
	@Autowired
	SendEndCaseAddToSHPlatformService endCaseAddToSH;
	@Autowired
	SendReOpenAppToPlatformService reOpenAppToAll;
	@Autowired
	private CompensateService compensateService;
	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	EndCasePubService endCasePubService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	ClaimCancelService claimCancelService;
	@Autowired
	CompensateTaskService compeTaskService;
    @Autowired
    RegistToCarRiskPaltformService registToCarRiskPaltformService;
    @Autowired
    CiClaimPlatformTaskService ciClaimPlatformTaskService;
	
	/**
	 * 立案平台交互补传
	 * @param CiClaimPlatformLogVo
	 */
	@Override
	public void sendClaimToPaltform(CiClaimPlatformLogVo logVo){
		String claimNo = logVo.getBussNo();
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
//		String registNo = claimVo.getRegistNo();
//		logger.info("bussNo:"+registNo);
		String requestType = logVo.getRequestType();
		String comCode = logVo.getComCode();
		if( !"22".equals(comCode.substring(0,2))){// 全国平台
			String registNo = claimVo.getRegistNo();
			if(RequestType.ClaimBI.getCode().equals(requestType)){// 商业立案登记
				sendClaimToAll.sendClaimBIToPlatform(registNo,claimNo);
			}else{// 交强立案登记
				sendClaimToAll.sendClaimCIToPlatform(registNo,claimNo);
			}
		}else{// 上海平台
			sendClaimToSH.sendClaimToSH(logVo.getBussNo(),logVo.getRequestType());
		}
	}

	/**
	 * 案件注销平台---平台交互
	 * @param CiClaimPlatformLogVo
	 */
	@Override
	public void sendCancelToPaltform(CiClaimPlatformLogVo logVo) {
		String requestType = logVo.getRequestType();
		logger.debug(logVo.getBussNo());
		if("22".equals(logVo.getComCode().substring(0,2))){// 上海机构
			List<PrpLClaimVo> claimVoList = claimTaskService.findClaimListByRegistNo(logVo.getBussNo());
			// 上海平台立案注销
			if (claimVoList != null && !claimVoList.isEmpty()) {
				for (PrpLClaimVo claimVo : claimVoList) {
					if (Risk.DQZ.equals(claimVo.getRiskCode()) 
							&& RequestType.CancelInfoCI_SH.getCode().equals(requestType)) {
						cancelToSH.sendClaimCancelTo_SH(claimVo.getClaimNo());
					} else if (!Risk.DQZ.equals(claimVo.getRiskCode()) 
							&& RequestType.CancelInfoBI_SH.getCode().equals(requestType)) {
						cancelToSH.sendClaimCancelTo_SH(claimVo.getClaimNo());
					}
				}
			}else{// 上海平台报案注销
				List<PrpLCMainVo> cmainVoList = policyViewService.getPolicyAllInfo(logVo.getBussNo());
				if ( cmainVoList != null && !cmainVoList.isEmpty() ) {
					for ( PrpLCMainVo cmainVo : cmainVoList ) {
						if ( Risk.DQZ.equals(cmainVo.getRiskCode()) && 
								RequestType.CancelInfoCI_SH.getCode().equals(requestType) ) {
							this.sendCancelToPaltformRe
							(cmainVo.getRegistNo(),cmainVo.getPolicyNo(),"1");
						} else if (!Risk.DQZ.equals(cmainVo.getRiskCode()) 
								&& RequestType.CancelInfoBI_SH.getCode().equals(requestType)) {
							this.sendCancelToPaltformRe
							(cmainVo.getRegistNo(),cmainVo.getPolicyNo(),"1");
						}
					}
				}
			}
			
		} else {
			List<PrpLClaimVo> claimVoList = claimTaskService.findClaimListByRegistNo(logVo.getBussNo());
			if(claimVoList != null && !claimVoList.isEmpty()){
				String claimNo = "";
				String riskCode = "";
				for(PrpLClaimVo claimVo : claimVoList){
					if(Risk.DQZ.equals(claimVo.getRiskCode()) && RequestType.CancelInfoCI.getCode().equals(requestType)){
						claimNo = claimVo.getClaimNo();
						riskCode = claimVo.getRiskCode();
					}
					if(!Risk.DQZ.equals(claimVo.getRiskCode()) && RequestType.CancelInfoBI.getCode().equals(requestType)){
						claimNo = claimVo.getClaimNo();
						riskCode = claimVo.getRiskCode();
					}
				}
				PrpLcancelTraceVo traceVo = claimCancelService.findByClaimNo(claimNo);
				if(traceVo != null){
					PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(traceVo.getClaimNo());
					Double sum = DataUtils.NullToZero(traceVo.getSwindleSum()).doubleValue();
					String cause = traceVo.getApplyReason();// 注销，拒赔原因
					// ValidFlag:1-正常，0-注销，2-拒赔 ,
					String cancelCause = "21";// 平台：21-保险公司注销案件，
					if("2".equals(claimVo.getValidFlag())){
						cancelCause = "22";// 22-保险公司拒赔
					}else if(StringUtils.isNotBlank(cause)&&cause.startsWith("0")){// 除外责任
						cancelCause = "02";
					}else if(StringUtils.isNotBlank(cause)&&cause.startsWith("9")){// 0-注销
						cancelCause = "99";// 其他
					}
					cancelToAll.sendClaimCancelToPlatform(riskCode,logVo.getBussNo(),cancelCause,sum);
				}
			}else{
				List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(logVo.getBussNo());
				if(cMainVoList != null && !cMainVoList.isEmpty()){
					for(PrpLCMainVo cMainVo : cMainVoList){
						if(Risk.DQZ.equals(cMainVo.getRiskCode()) && RequestType.CancelInfoCI.getCode().equals(requestType)){
							sendCancelToPaltformRe(logVo.getBussNo(), cMainVo.getPolicyNo(), "1");
						}
						if(!Risk.DQZ.equals(cMainVo.getRiskCode()) && RequestType.CancelInfoBI.getCode().equals(requestType)){
							sendCancelToPaltformRe(logVo.getBussNo(), cMainVo.getPolicyNo(), "1");
						}
					}
				}
			}
		}
	}
	
	/**
	 * 案件注销--报案注销,保单关联和注销
	 * @param registNo
	 * @param policyNo
	 * @param cancelType--注销类型（1,注销当前保单-2,关联该保单）
	 */
	@Override
	public void sendCancelToPaltformRe(String registNo,String policyNo,String cancelType) {
		PrpLCMainVo policyInfo = policyViewService.findPolicyInfoByPaltform(registNo,policyNo);
		if("22".equals(policyInfo.getComCode().substring(0,2))){// 上海机构
			cancelToSH.sendCancelToPlatform(policyInfo,cancelType);
		}else{
			cancelToAll.sendCancelToPlatform(policyInfo,cancelType);
		}
	}

	/**
	 * 理算核赔补送平台
	 * @param CiClaimPlatformLogVo
	 */
	@Override
	public void sendVClaimToPaltform(CiClaimPlatformLogVo logVo) {
		List<PrpLClaimVo> claimVoList = claimTaskService.findClaimListByRegistNo(logVo.getBussNo(),"1");
		CiClaimPlatformTaskVo platformTaskVo = ciClaimPlatformTaskService.findPlatformTaskByPK(logVo.getTaskId());
		if(platformTaskVo!=null){
			platformTaskVo.setStartDate(new Date());
		}
		for (PrpLClaimVo claimVo : claimVoList) {
			PrpLCompensateVo compeVo = compensateService.findCompByClaimNo(claimVo.getClaimNo(), "N");
			if(compeVo == null){
				continue;
			}
			String compeNo = compeVo.getCompensateNo();
			if(logVo.getComCode().startsWith("22")){// 上海机构//上海平台
				if (RequestType.VClaimCI_SH.getCode().equals(logVo.getRequestType())
						&& Risk.DQZ.equals(claimVo.getRiskCode())) {
					sendVClaimToSH.sendVClaimCIToSHPlatform(compeVo,platformTaskVo);
				}
				if (RequestType.VClaimBI_SH.getCode().equals(logVo.getRequestType())
						&& !Risk.DQZ.equals(claimVo.getRiskCode())) {
					sendVClaimToSH.sendVClaimBIToSHPlatform(compeVo,platformTaskVo);
				}
			}else{// 全国平台
				if (RequestType.VClaimCI.getCode().equals(logVo.getRequestType())
						&& Risk.DQZ.equals(claimVo.getRiskCode())) {
					sendVClaimToAll.sendVClaimCIToPlatform(compeNo,platformTaskVo);
				}
				if (RequestType.VClaimBI.getCode().equals(logVo.getRequestType())
						&& !Risk.DQZ.equals(claimVo.getRiskCode())) {
					sendVClaimToAll.sendVClaimBIToPlatform(compeNo,platformTaskVo);
				}
			}
		}
	}
	
	/**
	 * 赔款支付平台交互补传
	 * @param CiClaimPlatformLogVo
	 */
	@Override
	public void sendPaymentToPaltform(CiClaimPlatformLogVo logVo){
		if("22".equals(logVo.getComCode().substring(0,2))){// 上海机构
			paymentToSH.sendPaymentToSHPlatform(logVo);
		}else{// 全国平台
			paymentToAll.sendPaymentToPlatform(logVo);
		}
	}

	/**
	 * 结案
	 * @param CiClaimPlatformLogVo
	 */
	@Override
	public void sendEndCaseToPaltform(CiClaimPlatformLogVo logVo) {
		String bussNo = logVo.getBussNo();
		String reqType = logVo.getRequestType();
		String comCode = logVo.getComCode();
		CiClaimPlatformTaskVo platformTaskVo = ciClaimPlatformTaskService.findPlatformTaskByPK(logVo.getTaskId());
		
		if(logVo.getTaskId()!=null){
			platformTaskVo.setStartDate(new Date());
		}
		String endCaseType = "1";
		if(RequestType.EndCaseCI_SH.getCode().equals(reqType)){
			endCaseType = "1";
		}else if(RequestType.EndCaseBI_SH.getCode().equals(reqType)){
			endCaseType = "2";
		}else if(RequestType.EndCaseCI.getCode().equals(reqType)){
			endCaseType = "1";
		}else if(RequestType.EndCaseBI.getCode().equals(reqType)){
			endCaseType = "2";
		}
		PrpLEndCaseVo caseVo = null;
		if("22".equals(comCode.substring(0,2))){
			// 上海机构平台日志bussno为立案号
			caseVo = endCasePubService.findEndCaseByClaimNo(logVo.getBussNo());
		}else{
			caseVo = endCasePubService.findEndCaseByType(bussNo,endCaseType);
		}
		if(caseVo != null){
			if("22".equals(comCode.substring(0,2))){// 上海机构
				if(RequestType.EndCaseCI_SH.getCode().equals(reqType)){
					sendEndCaseToSH.sendEndCaseToSH(caseVo.getRegistNo(),bussNo,platformTaskVo);
				}
				if(RequestType.EndCaseBI_SH.getCode().equals(reqType)){
					sendEndCaseToSH.sendEndCaseToSH(caseVo.getRegistNo(),bussNo,platformTaskVo);
				}
			}else{// 全国平台
				if(RequestType.EndCaseCI.getCode().equals(reqType)){
					sendEndCaseToAll.sendEndCaseToPlatform(caseVo,platformTaskVo);
				}
				if(RequestType.EndCaseBI.getCode().equals(reqType)){
					sendEndCaseToAll.sendEndCaseToPlatform(caseVo,platformTaskVo);
				}
			}
		}
	}

	/**
	 * 结案追加--上海平台
	 * @param CiClaimPlatformLogVo
	 */
	@Override
	public void sendEndCaseAddToPaltform(CiClaimPlatformLogVo logVo){
		endCaseAddToSH.sendEndCaseAddTo_SHPlatform(logVo);
	}

	/**
	 * 全国平台重开赔案
	 * @param CiClaimPlatformLogVo
	 */
	@Override
	public void sendReOpenAppToPaltform(CiClaimPlatformLogVo logVo){
		String comCode = logVo.getComCode();
		if( !"22".equals(comCode.substring(0,2))){// 全国平台
			reOpenAppToAll.sendReOpenAppToPlatform(logVo);
		}
	}

	/* 
	 * @see ins.sino.claimcar.claim.service.SendClaimToPaltformService#claimToPaltform(java.lang.String)
	 * @param registNo
	 */
	@Override
	public void claimToPaltform(String claimNo) {
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
		// TODO Auto-generated method stub -- 立案送平台
		String registNo = claimVo.getRegistNo();
		String policyNo = claimVo.getPolicyNo();
		try{
			PrpLCMainVo cMainVo = policyViewService.getPolicyInfo(registNo,policyNo);
			if(cMainVo.getComCode().startsWith("22")){// 上海平台
				sendClaimToSH.sendClaimToPlatFormSH(registNo,claimNo);
			}else{
				if(Risk.DQZ.equals(cMainVo.getRiskCode())){// 交强
					sendClaimToAll.sendClaimCIToPlatform(registNo,claimNo);
				}else{// 商业
					sendClaimToAll.sendClaimBIToPlatform(registNo,claimNo);
				}
			}
		}
		catch(Exception e){
			throw new IllegalArgumentException("提交失败！立案送平台失败！<br/>"+e);
		}
	}

	/* 
	 * @see ins.sino.claimcar.claim.service.SendClaimToPaltformService#vClaimToPaltform(java.lang.String)
	 * @param compeNo
	 */
	@Override
	public void vClaimToPaltform(String compeNo) {
		// TODO Auto-generated method stub
		PrpLCompensateVo compeVo = compeTaskService.findCompByPK(compeNo);
		String classType = Risk.DQZ.equals(compeVo.getRiskCode()) ? "11" : "12";
		String policyComCode = policyViewService.findPolicyComCode(compeVo.getRegistNo(),classType);
		if(policyComCode.startsWith("22")){
			// 上海平台
			if(Risk.DQZ.equals(compeVo.getRiskCode())){// 交强
				sendVClaimToSH.sendVClaimCIToSHPlatform(compeVo,null);
			}else{// 商业
				sendVClaimToSH.sendVClaimBIToSHPlatform(compeVo,null);
			}
		}else{// 全国平台
			if(Risk.DQZ.equals(compeVo.getRiskCode())){// 交强
				sendVClaimToAll.sendVClaimCIToPlatform(compeNo,null);
			}else{// 商业
				sendVClaimToAll.sendVClaimBIToPlatform(compeNo,null);
			}
		}
	}

	/* 
	 * @see ins.sino.claimcar.claim.service.
	 * ClaimToPaltformService#endCaseToPaltform
	 * (java.lang.String)
	 * @param claimNo
	 */
//	@Override
//	public void endCaseToPaltform(String claimNo) {
//		// TODO Auto-generated method stub
//		try{
//			PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
//			String registNo = claimVo.getRegistNo();
//			String classType = Risk.DQZ.equals(claimVo.getRiskCode()) ? "11" : "12";
//			String policyComCode = policyViewService.findPolicyComCode(registNo,classType);
//
	// if( !policyComCode.startsWith("22")){// 全国平台
//				sendEndCaseToAll.sendEndCaseToPlatform(registNo,claimNo);
	// }else{// 上海平台
//				sendEndCaseToSH.sendEndCaseToSH(registNo,claimNo);
//			}
//
//		}
//		catch(Exception e){
//			e.printStackTrace();
	// //throw new IllegalArgumentException("再保处理结案业务送平台失败！ businessType=0 <br/>"+e);
//		}
//	}

	@Override
	public void reOpenAppToPaltform(String endCaseNo) {
		// TODO Auto-generated method stub
		PrpLEndCaseVo endCaseVo = endCasePubService.findEndCaseByPK(null,endCaseNo);
		// 重开赔案审核通过送平台
		String type = Risk.DQZ.equals(endCaseVo.getRiskCode())?"11":"12";
		String comCode = policyViewService.findPolicyComCode(endCaseVo.getRegistNo(),type);
		if( !comCode.startsWith("22")){// 全国平台
			if(Risk.DQZ.equals(endCaseVo.getRiskCode())){
				reOpenAppToAll.sendCIReOpenAppToPlatform(endCaseNo);
			}else{
				reOpenAppToAll.sendBIReOpenAppToPlatform(endCaseNo);
			}
		}else{// 上海平台
//			endCaseAddToSH.sendEndCaseAddToSH(endCaseNo);
		}
	}

	/**
	 * 赔款支付批量补传方法实现
	 */
	@Override
	public CiClaimPlatformLogVo paymentListResend(PrpLCompensateVo compeVo) {
		// TODO Auto-generated method stub
		CiClaimPlatformLogVo logVo = new CiClaimPlatformLogVo();
		String comCode = compeVo.getComCode();
		String compensateNo = compeVo.getCompensateNo();
		if(comCode.startsWith("22")){// 上海
			try {
				logVo = paymentToSH.sendPaymentToSH(compensateNo);
			} catch (Exception e) {
				logger.info("计算书号：" + compensateNo + "赔款支付上海平台补传异常！", e);
			}
		}else{// 全国
			if (Risk.DQZ.equals(compeVo.getRiskCode())) {
				logVo = paymentToAll.sendPaymentCIToPlatform(compensateNo);
			} else {
				logVo = paymentToAll.sendPaymentBIToPlatform(compensateNo);
			}
		}
		return logVo;
	}

}
