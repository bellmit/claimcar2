package ins.sino.claimcar.claim.service;

import ins.platform.vo.PrpLLawSuitVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.vo.ClaimFeeCondition;
import ins.sino.claimcar.claim.vo.ClaimFeeExt;
import ins.sino.claimcar.claim.vo.ClaimVO;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindFeeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrplTestinfoMainVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.pinganUnion.dto.EstimateRespData;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ClaimTaskExtService {

	public static final int PERCENT = 100;

	// 强制立案
	public abstract PrpLClaimVo forceClaim(String registNo,String policyNo,String flowId);

	/**
	 * 自动立案提交工作流
	 */
	public abstract PrpLClaimVo submitClaim(String registNo,String policyNo,String flowId,String claimFlag) throws ParseException;

	/**
	 * 自动立案保存数据
	 */
	public abstract ClaimVO autoClaim(String registNo,String policyNo)throws ParseException ;

	// 查勘的定责信息，只返回一条信息
	public abstract PrpLCheckDutyVo findDutyByCheck(PrpLCheckVo prpLCheckVo,Integer serialNo);

	/**
	 * 转换ClaimFeeExt到PrpLclaimFee
	 * @param claimFeeExts
	 * @param lossType "personloss" 只组织人员(因为案均只有人), "proploss" 只组织财产(因为案均只有财产), "" 人伤与财产都组织
	 * @return
	 */
	public abstract List<PrpLClaimKindVo> loadClaimFee(List<ClaimFeeExt> claimFeeExts,String lossType,String registNo,FlowNode node);

	/**
	 * 查勘刷新立案环节判断B险赔款是否大于保额 大于则返回分摊金额
	 * <pre></pre>
	 * @param registNo
	 * @param prpLClaimKindVoBPers
	 * @param prpLClaimKindVoBProp
	 * @return
	 * @modified:
	 * ☆WLL(2016年8月23日 下午2:57:47): <br>
	 */
	public abstract Map<String,BigDecimal> getClaimLossOfKindB(String registNo,PrpLClaimKindVo prpLClaimKindVoBPers,
																PrpLClaimKindVo prpLClaimKindVoBProp);

	/**
	 * 保存或更新立案,更新估损次数
	 */
	public abstract PrpLClaimVo addOrUpdateClaim(PrpLClaimVo prpLClaimVo,PrpLWfTaskVo prpLWfTaskVo,String taskId);

	// 主表子表关联
	public abstract PrpLClaimVo updateClaimFeeByClaim(PrpLClaimVo prpLClaimVo,List<PrpLClaimKindVo> prpLClaimKindVoList);

	// 主表子表关联方法重载,关联两个子表
	public abstract PrpLClaimVo updateClaimFeeByClaim(PrpLClaimVo prpLClaimVo,List<PrpLClaimKindVo> prpLClaimKindVoList,
														List<PrpLClaimKindFeeVo> prpLClaimKindFeeVoList);

	/**
	 * 立案与理算公式保持一致刷新立案
	 * @param prpLcMain
	 * @param claimCond
	 * @return
	 */
	public abstract List<ClaimFeeExt> getKindLossByCondAndPolicyForNewClaimRefresh(PrpLCMainVo prpLCMainVo,ClaimFeeCondition claimFeeCondition);

	// 根据分损失类计算的中间结果生成分险别估损信息(初始化使用) ----改造
	public abstract List<ClaimFeeExt> getKindLossByCondAndPolicy(PrpLCMainVo prpLCMainVo,ClaimFeeCondition claimFeeCondition);

	/**
	 * 其他环节用来刷新立案估损金额
	 * <pre></pre>
	 * @param registNo
	 * @param userCode
	 * @param node
	 * @modified:
	 */
	public abstract void updateClaimFee(String registNo,String userCode,FlowNode node) throws Exception;

	/**
	 * 理算核赔通过后刷新立案估损金额
	 * @param registNo
	 * @param userCode
	 * @param node
	 * @modified: ☆weilanlei(2016年6月22日 ): <br>
	 */
	public abstract void updateClaimAfterCompe(String compensateNo,String userCode,FlowNode node);
	/**
	 * 理算冲销核赔通过后0结案，刷新立案估损金额
	 * @param registNo
	 * @param userCode
	 * @param node
	 * @modified: ☆weilanlei(2016年12月1日 ): <br>
	 */
	public abstract void updateClaimAfterCompeWfZero(String compensateNo,String userCode,FlowNode node);

	/**
	 * 根据charge来源的定损主表判断该费用是否理算未扣减且核损通过
	 * 返回值表示该费用是否刷入立案
	 * <pre></pre>
	 * @param chargeVo
	 * @param bcFlag
	 * @return
	 * @modified:
	 * @author WLL
	 */
	public abstract boolean adjustUnderWriDeductForCharge(PrpLDlossChargeVo chargeVo,String riskCode);

	/**
	 * 核损、查勘之后刷新立案费用金额表PrpLClaimFee数据的方法
	 * @param registNo
	 * @param userCode
	 * @param node
	 * @modified: ☆weilanlei(2016年6月23日 ): <br>
	 */
	public abstract List<PrpLClaimKindFeeVo> updateClaimFeeAmt(PrpLClaimVo claimVo,String registNo,String userCode,FlowNode node);

	/**
	 * 核赔之后刷新立案费用金额表PrpLClaimFee数据的方法
	 * @param registNo
	 * @param userCode
	 * @param node
	 * @modified: ☆weilanlei(2016年6月23日 ): <br>
	 */
	public abstract List<PrpLClaimKindFeeVo> updateClaimFeeAmtAfterComp(PrpLClaimVo claimVo,PrpLCompensateVo compeVo,String registNo,String userCode,
																		FlowNode node);

	// 保存或者更新立案
	public abstract void saveOrUpdatePrpLClaimVo(PrpLClaimVo prpLClaimVo);

	/**
	 * 立案注销
	 * 
	 * <pre></pre>
	 * @param claimNo
	 * @param validFlag 0-立案注销 2-立案拒赔 1-正常数据
	 * @modified: ☆ZhouYanBin(2016年5月10日 下午4:51:53): <br>
	 */
	public abstract void cancleClaim(String claimNo,String validFlag,BigDecimal flowTaskId,SysUserVo userVo,WfTaskSubmitVo submitVo);

	/**
	 * 立案注销恢复
	 * 
	 * <pre></pre>
	 * @param claimNo
	 * @param validFlag 0-立案注销 1-正常案件 2-立案拒赔
	 * @modified: ☆ZhouYanBin(2016年5月10日 下午4:51:53): <br>
	 */
	public abstract void cancleClaimRecover(String claimNo,String validFlag,BigDecimal flowTaskId,SysUserVo userVo, WfTaskSubmitVo submitVo);

	/**
	 * 15天案均上浮，刷新车财估损
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @modified willingRan
	 * @author: ☆ZhouYanBin(2016年5月18日 上午11:22:40): <br>
	 */
	public abstract void updateClaimFeeForFifteen(PrpLRegistVo registVo);

	/**
	 * 判断人伤上浮时 估计赔款是否需要分摊
	 * <pre></pre>
	 * @param registNo 报案号
	 * @param claimLossProp 车物赔款
	 * @param amountB B险保额 
	 * @return
	 * @modified:
	 * ☆WLL(2016年8月22日 下午5:25:56): <br>
	 */
	public abstract Map<String,BigDecimal> getPersLossFeeKindB(String registNo,BigDecimal claimLossPers,PrpLCMainVo cMainVo);

	/**
	 * 判断车物上浮时 估计赔款是否需要分摊
	 * <pre></pre>
	 * @param registNo 报案号
	 * @param claimLossProp 车物赔款
	 * @param amountB B险保额 
	 * @return
	 * @modified:
	 * ☆WLL(2016年8月22日 下午3:40:37): <br>
	 */
	public abstract Map<String,BigDecimal> getPropLossFeeKindB(String registNo,BigDecimal claimLossProp,PrpLCMainVo cMainVo);

	/**
	 * 核赔-结案 回写立案
	 * @param claimVo
	 * @modified: ☆Luwei(2016年6月7日 上午9:27:03): <br>
	 */
	public abstract void claimWirteBack(PrpLClaimVo claimVo);

	public abstract List<PrpLLawSuitVo> findPrpLLawSuitVoByRegistNo(String RegistNo);

	/**
	 * 重开赔案回写
	 * @param claimNo
	 */
	public abstract void reOpenClaimWirteBack(String claimNo,FlowNode node,String endCaserCode);

	/**
	 * 判断该案件的车物核损是否全部通过  用于案均上浮
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ☆WLL(2016年8月17日 下午4:18:06): <br>
	 */
	public abstract boolean adjustPropLossState(String registNo);

	/**
	 * 30天案均上浮 人伤估损翻150%
	 * <pre></pre>
	 * @param registVo
	 * @modified:
	 * ☆WLL(2016年8月17日 下午5:46:37): <br>
	 */
	public abstract void updateClaimFeeForThirty(PrpLRegistVo registVo);

	/**
	 * 获取交强责任限额Map
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆WLL(2016年8月19日 下午3:23:17): <br>
	 */
	public abstract Map<String,BigDecimal> getCIDutyAmt(String registNo);

	/**
	 * 判断案件是否符合30天人伤上浮条件
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆WLL(2016年8月17日 下午5:27:59): <br>
	 */
	public abstract boolean adjustPersLossState(String registNo);

	/**
	 * 立案发起注销
	 * 
	 */
	public abstract void cancleClaim(BigDecimal flowTaskId,String userCode,SysUserVo userVo);
	/**
	 * 根据车财损失计算车损占比字段值carLossRate
	 * <pre></pre>
	 * @param compensateVo 组织汇总了车损和财损数据的Vo
	 * @return
	 * @modified:
	 * ☆WLL(2017年1月13日 下午5:25:47): <br>
	 */
	public BigDecimal getCarLossRate(CompensateVo compensateVo);
	
	public void regsitAddCaseAfterClaim(String registNo,String policyNo);
	/**
	 * 保存德联易控信息
	 * @param prplTestinfoMainVo
	 */
	public void saveToPrplTestinfoMain(PrplTestinfoMainVo prplTestinfoMainVo);

	/**
	 * 自动立案保存数据（平安联盟对接）
	 */
	public PrpLClaimVo autoClaimForPingAnCase(String registNo,String policyNo, String flowId, Date registerDate);

	/**
	 * 刷新立案估损金额（平安联盟对接）
	 * @param estimateRespData
	 */
	public void updateClaimFeeForPingAnCase(String registNo, EstimateRespData estimateRespData);
}
