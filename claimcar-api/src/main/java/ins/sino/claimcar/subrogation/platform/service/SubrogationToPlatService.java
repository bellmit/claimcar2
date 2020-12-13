package ins.sino.claimcar.subrogation.platform.service;

import ins.sino.claimcar.subrogation.platform.vo.AccountsInfoVo;
import ins.sino.claimcar.subrogation.vo.BeSubrogationVo;
import ins.sino.claimcar.subrogation.vo.CClaimDataVo;
import ins.sino.claimcar.subrogation.vo.ClaimDataVo;
import ins.sino.claimcar.subrogation.vo.ClaimRiskWarnVo;
import ins.sino.claimcar.subrogation.vo.ConfirmQueryVo;
import ins.sino.claimcar.subrogation.vo.PolicyRiskWarnVo;
import ins.sino.claimcar.subrogation.vo.PrpLLockedPolicyVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.RecoveryResultVo;
import ins.sino.claimcar.subrogation.vo.RiskWarnQueryVo;
import ins.sino.claimcar.subrogation.vo.SCheckQueryVo;
import ins.sino.claimcar.subrogation.vo.SubrogationCheckVo;
import ins.sino.claimcar.subrogation.vo.SubrogationDataVo;
import ins.sino.claimcar.subrogation.vo.SubrogationQueryVo;

import java.math.BigDecimal;
import java.util.List;

public interface SubrogationToPlatService {

	/**
	 * 保单风险信息查询  交强
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午4:47:54): <br>
	 */
	public abstract List<PolicyRiskWarnVo> sendPolicyRiskWarnCI(
			RiskWarnQueryVo queryVo) throws Exception;

	/**
	 * 保单风险信息查询  商业
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午4:48:39): <br>
	 */
	public abstract List<PolicyRiskWarnVo> sendPolicyRiskWarnBI(
			RiskWarnQueryVo queryVo) throws Exception;

	/**
	 * 理赔风险信息查询  交强
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午4:48:51): <br>
	 */
	public abstract List<ClaimRiskWarnVo> sendClaimRiskWarnCI(
			RiskWarnQueryVo queryVo) throws Exception;

	/**
	 * 理赔风险信息查询  商业
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午4:49:10): <br>
	 */
	public abstract List<ClaimRiskWarnVo> sendClaimRiskWarnBI(
			RiskWarnQueryVo queryVo) throws Exception;

	/**
	 * 被代位求偿查询 交强
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午4:58:15): <br>
	 */
	public abstract List<BeSubrogationVo> sendBeSubrogationQueryCI(
			SubrogationQueryVo queryVo) throws Exception;

	/**
	 * 被代位求偿查询 商业
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午4:58:35): <br>
	 */
	public abstract List<BeSubrogationVo> sendBeSubrogationQueryBI(
			SubrogationQueryVo queryVo) throws Exception;

	/**
	 * 结算码接口查询 交强 
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午5:35:09): <br>
	 */
	public abstract List<RecoveryResultVo> sendRecoveryQueryCI(
			SubrogationQueryVo queryVo) throws Exception;

	/**
	 * 结算码接口查询 商业
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午5:35:09): <br>
	 */
	public abstract List<RecoveryResultVo> sendRecoveryQueryBI(
			SubrogationQueryVo queryVo) throws Exception;

	public abstract List<PrpLLockedPolicyVo> sendLockConfirmQueryBI(
			SubrogationQueryVo queryVo) throws Exception;

	/**
	 * 锁定确认发送报文
	 * @param lockedPolicyVo
	 * @modified:
	 * ☆YangKun(2016年3月30日 上午11:38:12): <br>
	 */
	public abstract String sendLockedConfirmBI(
			PrpLLockedPolicyVo lockedPolicyVo, String comCode) throws Exception;

	/**
	 * 锁定取消发送报文
	 * @param lockedPolicyVo
	 * @modified:
	 * ☆YangKun(2016年3月30日 上午11:38:12): <br>
	 */
	public abstract String sendLockedCancelBI(String recoveryCode,
			String failureCause, String comCode) throws Exception;

	/**
	 * 案件互审
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	public abstract List<SubrogationCheckVo> sendSubrogationCheck(
			SCheckQueryVo queryVo) throws Exception;

	/**
	 * 互审确认 查询平台 是 先删除后插入，故不保留数据 
	 * @param queryVo
	 * @throws Exception
	 */
	public abstract void sendCheck(SCheckQueryVo queryVo) throws Exception;

	/**
	 *开始追偿确认
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午4:47:54): <br>
	 */
	public abstract String sendRecoveryConfirmBI(ConfirmQueryVo queryVo)
			throws Exception;

	/**
	 * 结算查询接口
	 * @param recoveryCode
	 * @modified:
	 * ☆YangKun(2016年4月1日 上午11:23:34): <br>
	 */
	public abstract List<AccountsInfoVo> sendAccountQuery(String comCode,String recoveryCode) throws Exception;

	/**
	 * 代位求偿理赔信息交强查询
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	public abstract SubrogationDataVo sendSubrogationClaimCI(
			SubrogationQueryVo queryVo) throws Exception;

	/**
	 * 代位求偿理赔信息商业查询
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	public abstract SubrogationDataVo sendSubrogationClaimBI(
			SubrogationQueryVo queryVo) throws Exception;

	/**
	 * 保单交强查询
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	public abstract CClaimDataVo sendSubrogationBaodanCI(
			SubrogationQueryVo queryVo) throws Exception;

	/**
	 * 保单商业查询
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	public abstract ClaimDataVo sendSubrogationBaodanBI(
			SubrogationQueryVo queryVo) throws Exception;

	/**
	 * 追偿回款确认
	 * @param basePart
	 * @param comCode
	 * @throws Exception 
	 * @modified:
	 * ☆YangKun(2016年4月8日 下午7:19:24): <br>
	 */
	public abstract void recoveryReturnConfirm(String comCode,PrpLPlatLockVo platLockVo, 
			String claimSequenceNo,BigDecimal recoverAmount);

	public abstract List<AccountsInfoVo> sendAccountSearch(String comCode,
			SubrogationQueryVo queryVo)throws Exception;

}