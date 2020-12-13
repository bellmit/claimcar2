package ins.sino.claimcar.claim.service;

import ins.framework.common.ResultPage;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.vo.CompensateListVo;
import ins.sino.claimcar.claim.vo.PrpDAccidentDeductVo;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLCoinsVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLEndorVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPayHisVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.claim.vo.PrploldCompensateVo;
import ins.sino.claimcar.claim.vo.ThirdPartyLossInfo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;
import ins.sino.claimcar.other.vo.SendMsgParamVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.rule.vo.VerifyClaimRuleVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public interface CompensateTaskService {

	public List<PrpLCompensateVo> findCompByRegistNo(String number);

	public PrpLCompensateVo findCompByClaimNo(String ClaimNo);
	
	/**
	 * 根据逐渐查询唯一一条记录
	 * <pre></pre>
	 * @param id
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月12日 下午3:10:17): <br>
	 */
	public PrpLCompensateVo findPrpLCompensateVoByPK(String id);

	/**	
	 * @param registNo
	 * @param type "N"-正常理赔，“Y”-预付
	 * @return
	 * @modified:
	 * ☆Luwei(2016年4月25日 下午4:56:05): <br>
	 */
	public List<PrpLCompensateVo> queryCompensate(String registNo,String type);
	
	public List<PrpLCompensateVo> simpleQueryCompensate(String registNo,String type);
	
	public List<PrpLCompensateVo> findCompensatevosByRegistNo(String registNo);
	/**
	 * 通过立案号查询理算表的所有理算计算书数据信息
	 * @param claimNo
	 * @return
	 */
	public List<PrpLCompensateVo> findCompensatevosByclaimNo(String claimNo);
	
	
	
	/**	
	 * @param compeNo
	 * @return
	 * ☆Luwei(2016年4月25日 下午4:56:05): <br>
	 */
	public PrpLCompensateVo queryCompByPK(String compeNo);
	
	
	/** 获取预付信息
	 * @param compeNo
	 * @return
	 * @modified:
	 * ☆Luwei(2016年4月25日 下午5:11:58): <br>
	 */
	public List<PrpLPrePayVo> queryPrePay(String compeNo);
	
	
	/**	根据计算书号 --查询预付信息（type ： p:赔款 ，F:费用）
	 * @param compeNo
	 * @param type
	 * @return
	 * @modified:
	 * ☆Luwei(2016年4月14日 下午3:13:46): <br>
	 */
	public List<PrpLPrePayVo> getPrePayVo(String compeNo,String type);
	
	
	/** 核赔回写理算
	 * @param compeVo
	 * @modified:
	 * ☆Luwei(2016年4月16日 下午5:28:54): <br>
	 */
	public void writeBackCompesate(PrpLCompensateVo compeVo);
	
	/**
	 * 查询历史预付信息
	 * <pre></pre>
	 * @param ClaimNo
	 * @param compensateType
	 * @return
	 * @modified:
	 * ☆XMSH(2016年4月16日 下午5:12:12): <br>
	 */
	public List<PrpLCompensateVo> findCompListByClaimNo(String claimNo,String compensateType);
	
	
	/**
	  * 写入冲减保额表
	  * @param
	  * @author lanlei
	  */
	public void savePrpLEndor(String compensateNo,SysUserVo userVo);
	
	
	/**
	 * 通过保单号查冲减保额表
	 * @param policyNo
	 * @return
	 * @modified:
	 * @author yezy
	 */
	public List<PrpLEndorVo> findPrpEndorListByPolicyNo(String policyNo);
	
	/**
	 * 冲减保额
	 * @param policyNo
	 * @param kindCode
	 * @return
	 * @modified:
	 * ☆YangKun(2016年6月15日 上午11:47:32): <br>
	 */
	public BigDecimal queryEmotorKindLossfee(String policyNo,String kindCode);
	
	public ResultPage<PrpLCompensateVo> findCompensatePage(String registNo,String claimNo,String compensateNo,int start,int length);
	
	
	public ResultPage<PrpLPaymentVo> findPrpLPaymentPage(String compensateNo,int start,int length);
	
	/**
	 * 冲减保额数据
	 * @param policyNo
	 * @return
	 * @modified:
	 * ☆YangKun(2016年7月9日 下午3:23:27): <br>
	 */
	public Map<String,BigDecimal> queryEmotorMap(String policyNo);
	
	/**
	 * 已赔付数据
	 * @param claimNo
	 * @param compensateNo
	 * @return
	 * @modified:
	 * ☆YangKun(2016年7月9日 下午3:23:41): <br>
	 */
	public Map<String,BigDecimal> querySumRealPay(String claimNo,String compensateNo);
	
	/**
	 * 预付费用信息
	 * @param compensateNo
	 * @return
	 * @modified:
	 * ☆XMSH(2016年8月4日 下午2:45:55): <br>
	 */
	public List<PrpLPrePayVo> findPrePayList(String compensateNo);
	
	/**
	 * 收付回写收付状态
	 * @param compensateNo
	 * @param serialNo
	 * @throws Exception 
	 * @modified:
	 * ☆XMSH(2016年8月11日 下午4:54:13): <br>
	 */
	public void paymentWriteBackPrePay(String compensateNo,String serialNo,Date payTime) throws Exception;
	
	public void paymentWriteBackCompVo(PrpLCompensateVo compensateVo) throws Exception;
	public List<PrpLCompensateVo> queryCompensateByRegistNo(String registNo);
	
	/**
	 * 预付冲销
	 * **/
	public void prePayCancel(String taskId,SysUserVo user);
	
	/**
	 * 根据险别汇总金额
	 * @param prpLLossItemVoList
	 * @param prpLLossPropVoList
	 * @param prpLLossPersonVoList
	 * @param prpLChargeVoList
	 * @return
	 */
	public Map<String, BigDecimal> getCompKindAmtMap(List<PrpLLossItemVo> prpLLossItemVoList,List<PrpLLossPropVo> prpLLossPropVoList,
			List<PrpLLossPersonVo> prpLLossPersonVoList,List<PrpLChargeVo> prpLChargeVoList);
	
//	/**
//	 * 赔款计算
//	 */
//	public CompensateListVo calculate(CompensateListVo compensateListVo);
//	/**
//	 * 交强计算公式(商业险需扣交强使用)
//	 */
//	public CompensateListVo calCulatorCi(CompensateVo compensateVo,PrpLClaimVo prpLClaimVo);
	
	/**
	 * 交强试算
	 */
	public CompensateListVo calCulator(PrpLCompensateVo prpLCompensateVo,List<PrpLLossItemVo> prpLLossItemVoList,List<PrpLLossPropVo> prpLLossPropVoList,
	                                   List<PrpLLossPersonVo> prpLLossPersonVoList,String compensateType);
	
	public List<ThirdPartyLossInfo> getThirdPartyLossInfolistBz(CompensateListVo returnCompensateList,String calculateType);
	
	public List<PrpLCItemKindVo> getDeductOffKindList(String registNo);
	
	public List<PrpLClaimDeductVo> findDeductCond(String registNo,String riskCode);
	
	public Map<String,List<PrpLPrePayVo>> getPrePayMap(String ClaimNo,String bcFlag,String prePaidFlag) throws Exception;
	
	public List<PrpLPadPayPersonVo> getPadPayPersons(String registNo,String prePaidFlag);
	
	public List<PrpLDlossPersTraceMainVo> getValidLossPersTraceMain(String registNo,String bcFlag);

	public boolean checkLossState(String lossState, String bcFlag);
	
	public void cancelCompensates(PrpLCompensateVo compVo, BigDecimal flowTaskId,SysUserVo userVo);
	
	public void cancelCompensates( BigDecimal flowTaskId,SysUserVo userVo);
	
	public VerifyClaimRuleVo organizeRuleVo(String compensateId,SysUserVo userVo);
	
	public PrpLCompensateVo findCompByPK(String compensateNo);

	/**
	 * 计算查勘责任
	 * @param checkDutyList
	 * @param calType
	 * @return
	 * @modified: ☆LiuPing(2016年9月24日 ): <br>
	 */
	public List<PrpLCheckDutyVo> calcCheckDutyList(Map<String,Double> leftAmountMap,List<PrpLCheckDutyVo> checkDutyList,String calType);
	/**
	 * 查询计算书信息，根据创建时间降序
	 * @param ClaimNo
	 * @return
	 */
	public PrpLCompensateVo searchCompByClaimNo(String ClaimNo);
	
	/**
	 * 案件下所有预赔是否全部审核通过
	 * @param registNo
	 * @return
	 */
	public boolean isPrepayAllPassed(String registNo);
	
	/**
	 * 案件下所有理算是否核赔通过或者注销
	 * @param registNo
	 * @return
	 */
	public boolean isCompepayAllPassed(String registNo);
	
	public PrpDAccidentDeductVo finPrpDAccidentDeductVo(String riskCode, String kindCode,String  clauseType,String indemnityDuty);
	
	/*
	 * 是否满足自动支付
	 */
	public List<SendMsgParamVo> getsendMsgParamVo(PrpLRegistVo prpLRegistVo,List<PrpLPaymentVo> paymentVo);


	
	public PrpLPadPayMainVo findPadPayMainByComp(String registNo,String compensateNo);
	public List<PrpLPaymentVo> findPrpLPayment(PrpLPayCustomVo prpLPayCustomVo,String compensateNo);
	
	/**
	 * 账号修改回写prplpayment和prplcharge表的payeeid
	 * @param compensateNo
	 * @param oldPayeeId
	 * @param payeeId
	 * @param userVo
	 */
	public abstract void modAccountWritePayeeId(PrpLPayBankVo payBankVo,Long oldPayeeId,Long payeeId,SysUserVo userVo);

	public List<PrpLPayHisVo> showPayHis(Long otherId,String hisType);
	
	/**
	 * 查询预付或垫付计算书数据
	 * <pre></pre>
	 * @param registNo
	 * @param claimNo
	 * @param compensateNo
	 * @param compeType
	 * @param start
	 * @param length
	 * @return
	 * @modified:
	 * ☆WLL(2017年3月10日 下午2:16:16): <br>
	 */
	public ResultPage<PrpLCompensateVo> findPreOrPadPayPage(String registNo,String claimNo,String compensateNo,String compeType,
			int start,int length);
	
	public List<PrpLCompensateVo> queryCompensateByOther(String registNo,String type,String policyNo,String underwriteFlag);
	/**
	 * 查询旧理赔理算表y
	 * @param policyNo
	 * @return
	 */
	public List<PrploldCompensateVo> findPrpoldCompensateBypolicyNo(String policyNo);
	/**
	 * 查询新理赔理算表y
	 * @param policyNo
	 * @return
	 */
	public List<PrpLCompensateVo> findPrpCompensateBypolicyNo(String policyNo);
	/**
	 * 查询旧理赔车辆赔款表prplloss
	 * @param policyNo
	 * @return
	 */
	public Double findPrpllossBypolicyNo(String policyNo);
	

	/**
	 * 根据立案号判断理算任务是否注销，0-查询不到数据，1-注销，2-不是注销
	 * @param claimNo
	 * @return
	 */
	public String isCompeCancelByClaimNo(String claimNo);
	
	/**
	 * 更新预付或理算的结算单号
	 * @param settleNo
	 * @param accountNo
	 * @param operateType
	 * @param compensateNo
	 * @param serialNo
	 * @param payRefReason
	 */
	public void saveOrUpdateSettleNo(String settleNo,String accountNo,String operateType,
			String compensateNo,String serialNo,String payRefReason);

	/**
	 * 更新预付结算单号
	 * @param settleNo 结算单号
	 * @param accountNo 银行账号
	 * @param operateType 操作类型
	 * @param compensateNo 计算书号
	 * @param serialNo 序号
	 * @param payRefReason 收付原因
	 * @return 返回更新的数据总数
	 */
	public int updatePrePaySettleNo(String settleNo,String accountNo,String operateType,
									 String compensateNo,String serialNo,String payRefReason);

	/**
	 * 更新理算赔款结算单号
	 * @param settleNo 结算单号
	 * @param accountNo 银行账号
	 * @param operateType 操作类型
	 * @param compensateNo 计算书号
	 * @param serialNo 序号
	 * @param payRefReason 收付原因
	 * @return 返回更新的数据总数
	 */
	public int updateCompensateSettleNo(String settleNo,String accountNo,String operateType,
									 String compensateNo,String serialNo,String payRefReason);

	/**
	 * 更新理算费用结算单号
	 * @param settleNo 结算单号
	 * @param accountNo 银行账号
	 * @param operateType 操作类型
	 * @param compensateNo 计算书号
	 * @param serialNo 序号
	 * @param payRefReason 收付原因
	 * @return 返回更新的数据总数
	 */
	public int updateCompensateChargeSettleNo(String settleNo,String accountNo,String operateType,
									 String compensateNo,String serialNo,String payRefReason);
	
	/**
	 * 根据结算单号查询计算书信息，flag=P查询payment子表，flag=F查询charge子表
	 * @param settleNo
	 * @param flag
	 * @return
	 */
	public List<PrpLCompensateVo> findCompensateBySettleNo(String settleNo,String flag);
	/**
	 * 为被保险人电话设置  设置规则 存在的有效理算任务>最近注销的理算任务>收款人信息中的被保险人电话。
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * *牛强(2017年6月21日 下午3:21:15): <br>
	 */
	public String findInsuredPhone(String registNo);
	/**
	 * 
	 * 根据结算单号查询PrpLPrePayVo
	 * @param settleNo
	 * @return
	 * @modified:
	 * ☆zhujunde(2017年6月20日 下午7:19:54): <br>
	 */
    public List<PrpLPrePayVo> findPrpLPrePayVoBySettleNo(String settleNo);
    
    /**
     * 
     * 根据结算单号查询PrpLPaymentVo
     * @param settleNo
     * @return
     * @modified:
     * ☆zhujunde(2017年6月20日 下午7:19:54): <br>
     */
    public List<PrpLPaymentVo> findPrpLPaymentVoBySettleNo(String settleNo);
    
    /**
     * 
     * 根据结算单号查询PrpLChargeVo
     * @param settleNo
     * @return
     * @modified:
     * ☆zhujunde(2017年6月20日 下午7:19:54): <br>
     */
    public List<PrpLChargeVo> findPrpLChargeVoBySettleNo(String settleNo);
    
    /**
     * 根据计算书号和费用代码查询PrpLChargeVo
     * @param compensateNo
     * @param chargeCode
     * @return
     */
    public List<PrpLChargeVo> findChargeVoByChargeCode(String compensateNo,String chargeCode);
    
    /**
     * 
     * 退票的快赔
     * @param prpLRegistVo
     * @param paymentVo
     * @param payCustomVo
     * @return
     * @modified:
     * ☆zhujunde(2017年6月26日 下午5:28:47): <br>
     */
    public List<SendMsgParamVo> getSendMsgParamVoByRefund(PrpLRegistVo prpLRegistVo,List<PrpLPaymentVo> paymentVo,PrpLPayCustomVo payCustomVo);
    /**
     * 通过收款人和账号判断是否这个账号被支付过，并返回prplpaymentVo
     * @return
     */
    public PrpLPaymentVo findByPayeeNameAndAccountNo(String payeeName,String accountNo,Long paycustmId);

    /**
     * 
     * <pre>通过报案号查询未注销理算书</pre>
     * @param registNo
     * @return
     * @modified:
     * ☆LinYi(2017年7月20日 上午11:46:04): <br>
     */
    List<PrpLCompensateVo> findNotCancellCompensatevosByRegistNo(String registNo);

    /**
     * 
     * <pre>通过报案号查询垫付信息</pre>
     * @param registNo
     * @return
     * @modified:
     * ☆LinYi(2017年7月27日 上午10:22:24): <br>
     */
    PrpLPadPayMainVo findPadPayMain(String registNo);
    /**
	 * 根据报案号查询车辆损失信息
	 * 
	 * @param registNo
	 * @return
	 */
    public List<PrpLLossItemVo> findLossItemsByRegistNo(String registNo);
	/**
	 * 判断立案号下是否存在未注销的冲销计算书（包含未核赔计算书）
	 * <pre></pre>
	 * @param claimNo 立案号
	 * @param compeType 计算书类型
	 * @return
	 * @modified:
	 * ☆WLL(2017年12月13日 上午11:14:40): <br>
	 */
    public boolean isExistWriteOff(String claimNo,String compeType);
    
	/**
	 * 自动理算
	 * <pre></pre>
	 * @param flowTaskId
	 * @param user
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ☆WLL(2018年2月12日 下午3:49:17): <br>
	 */
	public PrpLCompensateVo autoCompensate(Double flowTaskId,SysUserVo user) throws Exception;
	/**
	 * 判断是否符合自动理算条件方法
	 * <pre></pre>
	 * @param wfTaskVo 工作流Vo
	 * @param registNo
	 * @param claimNo
	 * @param sumAmtNow 当前理算金额
	 * @param userVo 当前用户
	 * @return AutoVerifyFlag 自动理算标识位 true or false
	 * @modified:
	 * ☆WLL(2018年2月28日 下午8:32:19): <br>
	 */
	public boolean getAutoVerifyFlag(PrpLWfTaskVo wfTaskVo,PrpLCompensateVo compeVo,SysUserVo userVo);
	/**
	 * 获取理算工作流提交的任务Vo
	 * <pre></pre>
	 * @param compensateId 计算书号
	 * @param flowTaskId 当前理算工作流任务taskid
	 * @param wfTaskVo 当前理算任务Vo
	 * @param registNo
	 * @param claimNo
	 * @param sumAmtNow 当前理算金额
	 * @param userVo 当前用户
	 * @param isSubmitHeadOffice 
	 * @return WfTaskSubmitVo nextVo 下一个节点任务信息Vo
	 * @modified:
	 * ☆WLL(2018年2月28日 下午8:44:04): <br>
	 */
	public WfTaskSubmitVo getCompensateSubmitNextVo(String compensateId, Double flowTaskId,PrpLWfTaskVo wfTaskVo,
	                                                SysUserVo userVo,boolean autoVerifyFlag, String isSubmitHeadOffice);
	/**
	 * 提交理算工作流任务
	 * <pre></pre>
	 * @param prpLCompensateVo
	 * @param upTaskVo
	 * @param nextVo
	 * @param autoVerifyFlag
	 * @return
	 * @modified:
	 * ☆WLL(2018年2月28日 下午9:20:02): <br>
	 */
	public PrpLWfTaskVo submitCompeWfTaskVo(PrpLCompensateVo prpLCompensateVo,PrpLWfTaskVo upTaskVo,WfTaskSubmitVo nextVo,
	                                        String autoVerifyFlag,SysUserVo userVo);
	/**
	 * 理算节点提交的全部流程操作
	 * <pre></pre>
	 * @param wfTaskVo
	 * @param compVo
	 * @param user
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ☆WLL(2018年3月31日 下午1:26:51): <br>
	 */
	public PrpLWfTaskVo submitAutoCompTask(PrpLWfTaskVo wfTaskVo,PrpLCompensateVo compVo,SysUserVo user) throws Exception;

	/**
	 * 自动理算的全流程操作
	 * <pre></pre>
	 * @param taskVo
	 * @param userVo
	 * @modified:
	 * ☆zhujunde(2018年4月24日 下午4:16:22): <br>
	 */
	public PrpLCompensateVo autoCompTask(PrpLWfTaskVo taskVo,SysUserVo userVo);
	/**
	 * 判断商业险是否标的车无损失或无险别赔付
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆WLL(2018年5月29日 下午1:16:35): <br>
	 */
	public boolean adjustNotExistObj(String registNo);
	
	/**
	 * 根据保单号查询承保的联共保信息
	 * @param policyNo
	 * @return
	 */
	public List<PrpLCoinsVo> findPrpLCoins(String policyNo);
	
	/**
	 * 根据计算书号查询联共保分摊金额
	 * @param compensateNo
	 * @return
	 */
	public List<PrpLCoinsVo> findPrpLCoinsByCompensateNo(String compensateNo);
	
	public void savePrpLCoins(List<PrpLCoinsVo> prpLCoinsVoList);
	
	/**
	 * 根据立案号查询新理赔理算表
	 * @param claimNo
	 * @param riskCode
	 * @param compensateType
	 * @return
	 */
	public List<PrpLCompensateVo> findCompensateVoList(String claimNo,String riskCode,String compensateType);
	/**
	 * 根据报案号和保单号查理算主表
	 * @param registNo
	 * @param policyNo
	 * @return
	 */
	public List<PrpLCompensateVo> findPrplCompensateByRegistNoAndPolicyNo(String registNo,String policyNo);

	public List<PrpLCompensateVo> queryValidCompensate(String registNo,
			String type);

	    
    

	/**
	 * <pre>判断历史计算书号 的总赔款金额（总赔付金额之和）与损失赔偿情况列表（损失分项之和= 人伤+财损+车损）总金额是否一致</pre>
	 * @param compeVo
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年1月26日 下午2:00:26): <br>
	 */
	public String validateAmoutIsSame(PrpLCompensateVo compeVo);

	/**
	 * 查询报案号下计算书的核赔状态
	 * @param registNo
	 * @return
	 * WLL
	 */
	public Map<String, String> getCompUnderWriteFlagByRegNo(String registNo);

	/**
	 * <pre>判断同一计算书是否录入了相同收款人</pre>
	 * @param accountNo
	 * @param compensateNo
	 * @param payeeId
	 * @param payReason
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年2月21日 下午5:20:14): <br>
	 */
	boolean existPayCustom(String accountNo,String compensateNo,Long payeeId,String payReason);

	/**
	 * <pre></pre>
	 * @param compeVo
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年3月13日 下午7:24:54): <br>
	 */
	public String validAccountIsSame(PrpLCompensateVo compeVo,SysUserVo userVo) throws Exception;



	/**
	 * <pre>获取预付冲销轨迹</pre>
	 * @param prePayNo
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ☆XiaoHuYao(2019年2月2日 下午5:42:01): <br>
	 */
	public List<Map<String,Object>> getPrePayWfHis(String prePayNo) throws Exception;

	/**
	 * <pre></pre>
	 * @param prePayNo
	 * @param string
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年3月12日 下午3:13:00): <br>
	 */
	public List<PrpLPrePayVo> getPrePayWriteVo(String prePayNo,String feeType) throws Exception ;

	/**
	 * <pre></pre>
	 * @param compensateNo
	 * @param c
	 * @modified:
	 * ☆XiaoHuYao(2019年4月12日 下午4:43:51): <br>
	 */
	public List<PrpLCompensateVo> findOppCompensates(String compensateNo,String c);
	
	/**
	 * <pre></pre>
	 * @param prePayNo
	 * @param string
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ☆XiaoHuYao(2019年4月15日 下午4:51:49): <br>
	 */
	public List<PrpLPrePayVo> getRemainPrePayWriteVo(String prePayNo,String compensateType) throws Exception;
	
	/**
	 * 根据保单号查出所有underwriteflag 为1和3的数据
	 * @param policyNo
	 * @return
	 */
	public List<PrpLCompensateVo> queryCompensateByPolicyNo(String policyNo);
	
	public List<PrpLLossItemVo> findLossItemsByRegistNoAndpolicyNo(String registNo,String policyNo);
	/**
	 *     通过时间分界点与计算书号判断返回值
	 * @param compensateNo
	 * @param datesign
	 * @return
	 */
	public boolean findsingcompay(String compensateNo,Date datesign);
	/**
	 * 根据报案号、保单号、赔付次数查询理算
	 * @param registNo
	 * @param policyNo
	 * @param times
	 * @return
	 */
	public List<PrpLCompensateVo> findPrplCompensateByRegistNoAndPolicyNo(String registNo,String policyNo,Integer times);

	/**
	 * 根据报案号、立案号、查询理算
	 * @param registNo
	 * @param policyNo
	 * @param times
	 * @return
	 */
	public List<PrpLCompensateVo> findPrplCompensateByRegistNoAndClaimNo(String registNo,String claimNo);
}
