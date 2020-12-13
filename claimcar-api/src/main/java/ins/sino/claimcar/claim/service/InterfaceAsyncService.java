package ins.sino.claimcar.claim.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carchild.vo.RegistInformationVo;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.claim.vo.*;
import ins.sino.claimcar.claimjy.vo.JyResVo;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.hnbxrest.vo.ReceiveauditingresultVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.other.vo.PrpLAcheckMainVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import ins.sino.claimcar.sunyardimage.vo.response.ResNumRootVo;

import java.util.List;
import java.util.Map;

/**
 * 异步接口调用服务
 * 
 * <pre></pre>
 * @author ★niuqiang
 */
public interface InterfaceAsyncService {
	

	/**
	 * 公估付送发票
	 * 
	 * <pre></pre>
	 * @param assMainVo
	 * @param userVo
	 * @throws Exception
	 * @modified: *牛强(2017年1月19日 上午8:57:16): <br>
	 */ 
	public void assessorToInvoice(PrpLAssessorMainVo assessorMainVo,
			SysUserVo userVo) throws Exception;

	/**
	 * 公估费送收付
	 * 
	 * <pre></pre>
	 * @param assessorMainVo
	 * @throws Exception
	 * @modified: *牛强(2017年1月19日 上午9:01:03): <br>
	 */
	public void assessorToPayment(PrpLAssessorMainVo assessorMainVo)
			throws Exception;

	/**
	 * 公估费送再保
	 * 
	 * <pre></pre>
	 * @param assessorMainVo
	 * @throws Exception
	 * @modified: *牛强(2017年1月19日 上午9:02:43): <br>
	 */
	public void assessorToReins(PrpLAssessorMainVo assessorMainVo)
			throws Exception;

	
	/**
	 * 查勘费送发票
	 * 
	 * <pre></pre>
	 * @param prpLAcheckMainVo
	 * @param userVo
	 * @throws Exception
	 * @modified: *yzy(2019年8月7日 上午8:57:16): <br>
	 */
	public void checkFeeToInvoice(PrpLAcheckMainVo prpLAcheckMainVo,
			SysUserVo userVo) throws Exception;

	/**
	 * 查勘费送收付
	 * 
	 * <pre></pre>
	 * @param prpLAcheckMainVo
	 * @throws Exception
	 * @modified: *yzy(2019年8月7日 上午9:01:03): <br>
	 */
	public void checkFeeToPayment(PrpLAcheckMainVo prpLAcheckMainVo)throws Exception;
    
	/**
	 * 查勘费送再保
	 * 
	 * <pre></pre>
	 * @param assessorMainVo
	 * @throws Exception
	 * @modified: *yzy(2019年8月12日 上午9:02:43): <br>
	 */
	public void checkFeeToReins(PrpLAcheckMainVo prpLAcheckMainVo)throws Exception;
	
	/**
	 * 单证上传平台
	 * @param registNo
	 * @param riskCode
	 */
	public void certifyToPaltform(String registNo, String riskCode);

	/**
	 * 查勘登记 交强送平台
	 * @param registNo
	 * @throws Exception
	 */
	public void sendCheckToPlatformCI(String registNo) throws Exception;

	/**
	 * 查勘登记 商业送平台
	 * @param registNo
	 * @throws Exception
	 */
	public void sendCheckToPlatformBI(String registNo) throws Exception;

	/**
	 * 案件注销--报案注销,保单关联和注销
	 * @param registNo
	 * @param policyNo
	 * @param cancelType --注销类型（1,注销当前保单-2,关联该保单）
	 */
	public void sendCancelToPaltformRe(String registNo, String policyNo,
			String cancelType);

	/**
	 * 重开赔案送平台
	 * @param endCaseNo
	 */
	public void reOpenAppToPaltform(String endCaseNo);

	/**
	 * 定核损送平台
	 * @param endCaseNo
	 */
	public void sendLossToPlatform(String registNo, String riskCode);

	/**
	 * 报案推送平台
	 * @param prpLRegistVo
	 * @param prpLCMainVo
	 */
	public void sendRegistToPlatform(String registNo);
	
	/**
	 * 山东预警
	 */
	public void sendSdRiskWarning(String registNo, SysUserVo userVo,Map<String,String> map);

	/**
	 * 立案平台交互补传
	 * @param CiClaimPlatformLogVo
	 * @throws Exception
	 */
	public void sendClaimToPaltform(CiClaimPlatformLogVo logVo);

	/**
	 * 车险报案送客服系统接口
	 * @param registNo
	 * @throws Exception
	 */
	public abstract void carRegistToFounder(String registNo) throws Exception;

	/**
	 * 调度信息更新推送至客服系统接口
	 * @param taskVo
	 * @param scheduleType
	 * @throws Exception
	 */
	public abstract void scheduleInfoToFounder(PrpLScheduleTaskVo taskVo,
			String scheduleType) throws Exception;

	/**
	 * 报案注销推送至客服系统接口
	 * @param registNo
	 * @throws Exception
	 */
	public abstract void registCancelToFounder(String registNo)
			throws Exception;

	/**
	 * 保单关联推送至客服系统接口
	 * @param registNo
	 * @param conPolicyNo
	 * @param isConnect
	 * @throws Exception
	 */
	public abstract void PolicyRelationToFounder(String registNo,
			String conPolicyNo, String isConnect) throws Exception;

	/**
	 * 调度注销定损任务
	 * @param taskVo
	 * @param examineId
	 * @throws Exception
	 */
//	public abstract void cancelDflossTaskToFounder(PrpLScheduleTaskVo taskVo,
//			Long examineId) throws Exception;

	/**
	 * 预付调用收付接口
	 * @param compensateVo
	 * @throws Exception
	 */
	public void prePayToPayment(PrpLCompensateVo compensateVo) throws Exception;
	/**
	 * 平安预付调用收付接口，第一次默认失败
	 * @param compensateVo
	 * @param registNo
	 * @throws Exception
	 */
	public void prePayToNewPaymentPingAn(PrpLCompensateVo compensateVo,String registNo) throws Exception;
    /**
     * 平安预付调用收付接口
     * @param compensateVo
     * @throws Exception
     */
    public void pingAnPrePayToPayment(PrpLCompensateVo compensateVo,List<PrpLPrePayVo> prePayVos) throws Exception;
	/**
	 * 垫付调用收付接口
	 * @param padPayMainVo
	 * @throws Exception
	 */
	public void padPayToPayment(PrpLPadPayMainVo padPayMainVo) throws Exception;
	/**
	 * 平安垫付调用收付接口，第一次默认失败
	 * @param padPayMainVo
	 * @param registNo
	 * @throws Exception
	 */
	public void padPayToNewPaymentPingAn(PrpLPadPayMainVo padPayMainVo,String registNo) throws Exception;
	/**
	 * 理算调用收付接口
	 * @param compensateVo
	 * @throws Exception
	 */
	public void compensateToPayment(PrpLCompensateVo compensateVo)
			throws Exception;

	/**
	 * 平安数据送收付，默认失败
	 * @param compensateVo
	 * @param registNo
	 * @throws Exception
	 */
	public void compensateToPaymentPingAn(PrpLCompensateVo compensateVo,String registNo)throws Exception;
	/**
	 * 预付数据更新接口，排除重开的结案
	 * @param compensateVo
	 * @throws Exception
	 */
	public void updatePrePayToPayment(PrpLCompensateVo compensateVo)
			throws Exception;

	/**
	 * 送再保未决数据 接口
	 * @param claimVo
	 * @param kindHisVoList
	 * @param claimInterfaceLogVo
	 * @throws Exception
	 */
	public abstract void TransDataForClaimVo(PrpLClaimVo claimVo,
			List<PrpLClaimKindHisVo> kindHisVoList,
			ClaimInterfaceLogVo claimInterfaceLogVo) throws Exception;

	/**
	 * 送再保已决数据接口
	 * @param claimVo
	 * @param prpLCompensate
	 * @throws Exception
	 */
	public abstract void TransDataForCompensateVo(PrpLClaimVo claimVo,
			PrpLCompensateVo prpLCompensate) throws Exception;

	/**
	 * 结案 重开 拒赔 注销 送再保接口
	 * @param businessType
	 * @param claimVo
	 * @param claimInterfaceLogVo
	 * @throws Exception
	 */
	public abstract void TransDataForReinsCaseVo(String businessType,
			PrpLClaimVo claimVo, ClaimInterfaceLogVo claimInterfaceLogVo)
			throws Exception;

	public void pushCharge(String compensateNo);

    public void pingAnPushCharge(PrpLCompensateVo compeVo);

	public void pushPreCharge(String compensateNo);
	
	/**
	 * <pre>
	 * 理赔调度提交/改派提交接口（理赔请求快赔系统）
	 * </pre>
	 * @param prpLScheduleTaskVo
	 * @param schType
	 * @param prpLScheduleDefLosses
	 * @param url
	 * @throws Exception
	 * @modified: ☆zhujunde(2017年6月13日 下午2:36:55): <br>
	 */
	public void reassignmentes(PrpLScheduleTaskVo prpLScheduleTaskVo,String schType,List<PrpLScheduleDefLossVo> prpLScheduleDefLosses,String url) throws Exception;

	
	/**
	 * <pre>
	 * 理赔调度提交/改派提交接口（理赔请求快赔系统）
	 * </pre>
	 * @param prpLScheduleTaskVo
	 * @param schType
	 * @param prpLScheduleDefLosses
	 * @param url
	 * @throws Exception
	 * @modified: ☆zhujunde(2017年6月13日 下午2:36:55): <br>
	 */
   /* public void reassignmentes(PrpLScheduleTaskVo prpLScheduleTaskVo,
            String schType, List<PrpLScheduleDefLossVo> prpLScheduleDefLosses,
            String url) throws Exception;*/
    
    /**
	 * 异步保存反洗钱数据Insuredrela
	 * 
	 * <pre></pre>
	 * @param payCustomVo
	 * @param userVo
	 * @modified: ☆zhujunde(2017年7月19日 上午10:36:08): <br>
	 */
    public void installInsuredrela(PrpLPayCustomVo payCustomVo,SysUserVo userVo);

    /**
	 * 理赔通知移动端接口
	 * @param wfTakVo
	 * @param url
	 */
    public void packMsg(PrpLWfTaskVo wfTakVo,String url);
	public void sendCheckSubmitToPlatform(String registNo) throws Exception;


    /**
	 * 发送案件理赔结果给河南快赔
	 * @param registNo
	 * @param status
	 * @param userVo
	 */
    public void receivecpsresult(String registNo,String status,SysUserVo userVo);

    /**
	 * 保险公司发送定损照片审核结果信息
	 * @param receiveauditingresultVo
	 * @return
	 */
	public void receiveauditingresult(ReceiveauditingresultVo receiveauditingresultVo);
	
	/**
	 * 保险公司发送定损结果信息(定损金额)
	 * @param receivegusunresultVo
	 * @return
	 */
	public void receivegusunresult(String registNo,SysUserVo sysUserVo);

    /**
	 * 立案注销调车童网民太安
	 * 
	 * <pre></pre>
	 * @param prpLWfTaskVoList
	 * @param pClaimCancelVo
	 * @param prpLWfTaskVo
	 * @param reqVo
	 * @throws Exception
	 * @modified: ☆zhujunde(2017年11月8日 下午4:18:35): <br>
	 */
    public void organizationAndSendCTorMTA(PrpLClaimCancelVo pClaimCancelVo,PrpLWfTaskVo prpLWfTaskVo,SysUserVo userVo) throws Exception;

    
   	/**
	 * 立案注销恢复调车童网民太安
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @throws Exception
	 * @modified: ☆zhujunde(2017年11月8日 下午4:48:48): <br>
	 */
    public void sendClaimCancelRestoreCTorMTA(String registNo,SysUserVo userVo) throws Exception;
    
    
    
    /**
	 * 查勘推定损任务时调车童网民太安的报案接口
	 * 
	 * <pre></pre>
	 * @param registInformationVo
	 * @throws Exception
	 * @modified: ☆zhujunde(2017年12月12日 上午10:33:07): <br>
	 */
    public void sendRegistCTorMTA(RegistInformationVo registInformationVo) throws Exception;
    
    /**
	 * 报案推送山东平台
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @param userVo
	 * @modified: ☆zhujunde(2017年12月22日 上午10:04:27): <br>
	 */
    public void sendRegistToCarRiskPlatform(String registNo,SysUserVo userVo,Map<String,String> map); 
   	
	/**
	 * 保单关联推送山东平台
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @param policyNo
	 * @param userVo
	 * @modified: ☆zhujunde(2017年12月22日 上午10:04:35): <br>
	 */
    public void sendRegistToCarRiskPlatformRe(String registNo,String policyNo,SysUserVo userVo);
   
	/**
	 * 保单关联推送山东平台
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @param policyNo
	 * @modified: ☆zhujunde(2017年12月14日 下午12:00:21): <br>
	 */
    //public void sendRegistToCarRiskPlatformRe(String registNo,String policyNo);

    /**
	 * 报案送精励联讯接口
	 * @param vo
	 * @param userVo
	 * @param prpLCMainVo
	 */
    public void sendRegistForGenilex(PrpLRegistVo vo,SysUserVo userVo,PrpLCMainVo prpLCMainVo);
    
    /**
	 * 普通定损，定损修改，定损追加送精励联讯接口
	 * @param registNo
	 * @param taskId
	 * @param userVo
	 */
    public void sendCarLossForGenilex(String registNo, String taskId, SysUserVo userVo);
	
	/**
	 * 山东预警定核损登记功能(接口)
	 * @param prplcmain
	 * @throws Exception
	 */
    public void sendDlossRegister(PrpLCMainVo prpLCMainVo,SysUserVo userVo)throws Exception;
    
    /**
	 * 山东预警案件注销（接口）
	 * @param prpLCMainVo
	 * @param cancleType
	 * @param reason
	 * @throws Exception
	 */
	public void sendCaseCancleRegister(PrpLCMainVo prpLCMainVo,String cancleType,String reason,SysUserVo userVo)throws Exception;
	
	/**
	 * 山东预警重开赔案登记功能(接口)
	 * @param endCaseNo-结案号
	 * @param policyNo
	 * @param userVo
	 * @throws Exception
	 */
	public void SendReopenCaseRegister(String endCaseNo,String policyNo,SysUserVo userVo)throws Exception;
    
    /**
	 * 立案送山东预警
	 * @param registNo
	 */
    public void claimToWarn(String registNo,String policyNo);
    
	/**
	 * 山东图片上传接口
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @param userVo
	 * @param riskCode
	 * @param claimSequenceNo
	 * @modified: ☆zhujunde(2018年7月2日 上午9:30:28): <br>
	 */
     public void carRiskImagesUpdate(String registNo,SysUserVo userVo,String riskCode,String claimSequenceNo);
     
     /**
	 * 报案注销送重复/虚假案件给山东预警
	 * @param cancelReason
	 * @param registNo
	 */
     public void sendFalseCaseToEWByRegist(String cancelReason, String registNo,String policyNo); 
     
     /**
	 * 查勘送预警系统
	 * @param registNo
	 */
 	public void checkToWarn(Long checkTaskId,Long flowTaskId,String policyNo);
 	
 	/**
	 * 拒赔审核通过送山东预警
	 * @param handleIdKey
	 */
 	public void sendFalseCaseToEWByCancel(String handleIdKey);

 	/**
	 * 报案推送深圳警保平台
	 * 
	 * <pre></pre>
	 * @param logId
	 * @modified: ☆LiYi(2018年9月28日 下午5:37:01): <br>
	 */
	public void sendRegistInfoToSZJB(String logId);
    
	/**
	 * 任务状态同步接口（从理赔系统请求定损系统）
	 * @param registNo
	 * @param dmgVhclId
	 * @param operationLink
	 * @param operationResults
	 * @param subNodeCode
	 * @param userVo
	 * @return
	 */
	public JyResVo sendTaskInfoService(String registNo,String dmgVhclId,String operationLink,String operationResults,String subNodeCode,SysUserVo userVo);

	/**
	 * 推定全损数据清空接口
	 * @param registNo
	 * @param dmgVhclId
	 * @param userVo
	 * @return
	 */
	public JyResVo sendCleanDataService(String registNo, String dmgVhclId, SysUserVo userVo);
	
	/**
	 * 定损零结通知接口
	 * @param registNo
	 * @param dmgVhclId
	 * @param userVo
	 * @return
	 */
	public JyResVo sendZeroNoticeService(String registNo,String dmgVhclId,SysUserVo userVo);

	/**
	 * 报案请求接口
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified: ☆LiYi(2018年9月5日 下午2:47:55): <br>
	 */
	public String sendRegistDataToJy(String registNo);
    
    /**
     * 报案，报案注销送中保信
     * @param registVo
     * @param userVo
     * @param reportType
     * @throws Exception 
     */
    public void reqByRegist(PrpLRegistVo registVo,SysUserVo userVo,String reportType) throws Exception;
    
    /**
	 * 结案，拒赔送中保信
	 * @param endCaseVo
	 * @param userVo
	 */
    public void reqByEndCase(PrpLEndCaseVo endCaseVo,SysUserVo userVo);
    
    /**
     * 立案注销送中保信
     * @param claimVo
     * @param userVo
     */
    public void reqByCancel(PrpLClaimVo claimVo,SysUserVo userVo);
	
	/**
	 * 发送案件理赔结果给自助理赔
	 * @param registNo
	 * @param userVo
	 * @param status
	 * @param nodeFlag
	 * @param policyNo
	 */
    public void sendClaimResultToSelfClaim(String registNo,SysUserVo userVo,String status,String nodeFlag,String policyNo);

    
    /**
     * 垫付赔款推送vat
     * <pre></pre>
     * @param padPayMainVo
     * @modified:
     * ☆zhujunde(2019年3月22日 上午8:45:40): <br>
     */
    public void pushPadPay(PrpLPadPayMainVo padPayMainVo);


    /**
     * 通过接口查询某个批次下每个资料类型拥有的影像数量
     * @param user
     * @param role
     * @param bussNo
     * @param assessorId
     * @param url
     * @param appName
     * @param appCode
     * @return
     */
    public ResNumRootVo getReqImageNum(SysUserVo user,String role,String bussNo,String assessorId,String url,String appName,String appCode);
    
    
    /**
     * 通过接口查询查勘机构人员上传的影像数量
     * @param user
     * @param role
     * @param bussNo
     * @param url
     * @param appName
     * @param appCode
     * @return
     */
    public void getReqCheckUserImageNum(SysUserVo user,String role,String bussNo,String url,String appName,String appCode);
	/**
	 * 
	 * @param id
	 * @param userVo
	 */
	public void claimcarYJAskPriceAdd(Long id, SysUserVo userVo);
	
	/**
	 * 请求德联易控接口
	 * @param registNo
	 * @param licenseNo
	 * @param nodeFlag--01:报案，03：查勘，04：定损，15：核价，14：核损
	 * @throws Exception
	 */
	public void SendControlExpert(String registNo,SysUserVo userVo,String licenseNo,String lossCarMainId,String nodeFlag,String url);
	
	/**
	 * 送再保冲销接口
	 * @param claimVo
	 * @param prpLCompensate
	 * @throws Exception
	 */
	public void transDataForWashTransaction(PrpLClaimVo claimVo,
			PrpLCompensateVo prpLCompensate) throws Exception;
	/**
	 * 埋点消息推送到中台
	 * @param registNo
	 * @param node
	 */
	public void middleStageQuerys(String registNo, String node)throws Exception;
	/**
	 * 商业险/交强险理赔信息写入接口
	 * @param registNo 
	 * @param type
	 * @param userVo
	 */
	public void policyInfoRegister(String registNo,String type,SysUserVo userVo);

	/**
	 * 反欺诈-车辆信息,人员信息，报案电话查询
	 * @param registNo
	 * @param type
	 * @param userVo
	 */
	public void sendRealTimeInfoQuery(PrpLWfTaskVo wfTaskVo,SysUserVo userVo) throws Exception;

}