package ins.sino.claimcar.platform.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.CancelType;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.platform.vo.BIClaimCancelReqBasePartyVo;
import ins.sino.claimcar.platform.vo.BIClaimCancelReqBodyVo;
import ins.sino.claimcar.platform.vo.CIClaimCancelReqBasePartyVo;
import ins.sino.claimcar.platform.vo.CIClaimCancelReqBodyVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.trafficplatform.service.RegistToCarRiskPaltformService;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sendCancelToPlatformService")
public class SendCancelToPlatformService {
	@Autowired
	CiClaimPlatformLogService platformLogService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	RegistToPaltformService registToPaltformService;
    @Autowired
    RegistToCarRiskPaltformService registToCarRiskPaltformService;
	
	private Logger logger = LoggerFactory.getLogger(SendCancelToPlatformService.class);
	
	/**
	 * 案件注销--报案注销,保单关联和注销
	 * @param policyInfo-保单信息
	 * @param cancelType--注销类型（1,注销当前保单-2,关联该保单）
	 */
	public void sendCancelToPlatform(PrpLCMainVo policyInfo,String cancelType) {
		String registNo = policyInfo.getRegistNo();
		if ("1".equals(cancelType)) {// 保单注销
			if (Risk.DQZ.equals(policyInfo.getRiskCode())) {
				sendClaimCancelToPlatformCI(registNo,CancelType.REGIST_CANCEL,"51");
			} else {
				sendClaimCancelToPlatformBI(registNo,CancelType.REGIST_CANCEL,"51",0.0);
			}
		} else {// 保单关联
			registToPaltformService.sendRegistCancelToPlatform
			(registNo,policyInfo.getPolicyNo());
	        //山东平台
			if(policyInfo.getComCode().startsWith("62") && SendPlatformUtil.isMor(policyInfo)){
                try{
                    SysUserVo userVo = new SysUserVo();
                    userVo.setComCode("00000000");
                    userVo.setUserCode("0000000000");
                    PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
                    List<PrpLCMainVo> prpLCMains = policyViewService.getPolicyAllInfo(registNo);
                    for(PrpLCMainVo cMainVo:prpLCMains){
                        if(policyInfo.getRiskCode().equals(cMainVo.getRiskCode())){
                            registToCarRiskPaltformService.sendRegistToCarRiskPlatformByCmain(registVo,cMainVo,userVo);
                            break;
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
	       
		}
	}
	
	/**
	 * <pre>立案注销送平台</pre>
	 * @param reqType
	 * @param registNo
	 * @param textType
	 * @param sum
	 * ☆Luwei(2016年8月12日 下午8:32:14): <br>
	 */
	public void sendClaimCancelToPlatform(String reqType,String registNo,String cancelCause,Double sum) {
		if(Risk.DQZ.equals(reqType)){
			sendClaimCancelToPlatformCI(registNo,CancelType.CLAIM_CANCEL,cancelCause);
		}else{
			sendClaimCancelToPlatformBI(registNo,CancelType.CLAIM_CANCEL,cancelCause,sum);
		}
	}
	
	
	/**
	 * 组装商业险案件注销报文
	 * @param claimDemandVo
	 * @param compensateVo
	 * @param cancelTraceVo
	 * @return
	 */
	private void sendClaimCancelToPlatformBI(String registNo,String type,String reason,Double amount){
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		String comCode = policyViewService.findPolicyComCode(registNo,"12");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo
				(RequestType.RegistInfoBI.getCode(),registNo,comCode);
		if (logVo == null) {
			logger.error("案件注销上传全国平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
		
		String claimSeqNo = logVo == null ? "" : logVo.getClaimSeqNo();
		//投保确认码
		String validNo = policyViewService.findValidNo(registNo,"12");
		logger.debug("投保确认码："+validNo);
//		String comCode = logVo==null ? "" : logVo.getComCode();
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.CancelInfoBI);
		
		//Body
		BIClaimCancelReqBodyVo bodyVo = new BIClaimCancelReqBodyVo();
		
		//BasePartyVo
		BIClaimCancelReqBasePartyVo bICancelVo=new BIClaimCancelReqBasePartyVo();
		bICancelVo.setConfirmSequenceNo(validNo);
		bICancelVo.setClaimSequenceNo(claimSeqNo);
		bICancelVo.setReportNo(registNo);
		bICancelVo.setCancelType(type);
		if("2".equals(reason)){
			reason = "51";
		}
		bICancelVo.setCancelCause(StringUtils.isEmpty(reason) ? "99" : reason);
		bICancelVo.setCancelDate(new Date());
		String desc = "客户放弃索赔";
		if(reason.equals("22")){
			desc = "保险公司拒赔,拒赔案件！";
		}else if("21".equals(reason)){
			desc = "保险公司注销案件,注销该案件,重复报案或出险时间不在保险有效期限内！";
		}
		bICancelVo.setCancelDesc(desc);//拒赔理由
		bICancelVo.setDirectClaimAmount(amount);
		
		bodyVo.setReqBasePartyVo(bICancelVo);
		
		if(SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"2"))){
			controller.callPlatform(bodyVo);
		}
	}
	
	
	/**
	 * 组装交强险报文
	 * @param claimDemandVo
	 * @param cancelTraceVo
	 * @return
	 */
	private void sendClaimCancelToPlatformCI(String registNo,String type,String reason){
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 联共保 从共，从联不交互平台
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
    	String comCode = policyViewService.findPolicyComCode(registNo,"11");
    	CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo
				(RequestType.RegistInfoCI.getCode(),registNo,comCode);
		if (logVo == null) {
			logger.info("案件注销上传全国平台失败！该案件理赔编码不存在或报案未成功上传平台！");
		}
		
		String claimSeqNo = logVo == null ? "" : logVo.getClaimSeqNo();
		//投保确认码
		String validNo = policyViewService.findValidNo(registNo,"11");
		logger.debug("投保确认码："+validNo);
//		String comCode = logVo==null ? "" : logVo.getComCode();
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.CancelInfoCI);
    	
		//Body
		CIClaimCancelReqBodyVo bodyVo = new CIClaimCancelReqBodyVo();
		
		//BasePartyVo
	    CIClaimCancelReqBasePartyVo cICancelVo=new CIClaimCancelReqBasePartyVo();
	    cICancelVo.setConfirmSequenceNo(validNo);
	    cICancelVo.setClaimCode(claimSeqNo);
	    cICancelVo.setCancelTime(new Date());
	    if("2".equals(reason)){
	    	reason = "51";
	    }
	    cICancelVo.setCancelCause(StringUtils.isEmpty(reason) ? "99" : reason);
	    String desc = "客户放弃索赔";
		if(reason.equals("22")){
			desc = "保险公司拒赔,拒赔案件！";
		}else if("21".equals(reason)){
			desc = "保险公司注销案件,注销该案件,重复报案或出险时间不在保险有效期限内！";
		}
		cICancelVo.setCancelDesc(desc);//拒赔理由
	    cICancelVo.setCancelType(type);
	    cICancelVo.setReportNo(registNo);
	    
	    bodyVo.setBasePartyVo(cICancelVo);
	    
	    if(SendPlatformUtil.isMor(policyViewService.getPolicyInfoByType(registNo,"1"))){
	    	controller.callPlatform(bodyVo);
	    }
	}

}
