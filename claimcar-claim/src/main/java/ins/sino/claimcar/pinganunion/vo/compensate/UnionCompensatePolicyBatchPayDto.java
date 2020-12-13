package ins.sino.claimcar.pinganunion.vo.compensate;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 平安联盟中心-理算查询接口-查询结果-保单批次赔付信息
 *
 * @author mfn
 * @date 2020/7/29 16:15
 */
public class UnionCompensatePolicyBatchPayDto implements Serializable {
	private static final long serialVersionUID = 1089352573466167579L;

	/** 赔案号 是否非空：Y 编码：N */
	private String caseNo;
	/** 赔付次数 是否非空：Y 编码：N */
	private String caseTimes;
	/** 赔付类型 代码定义3.14 是否非空：Y 编码：Y */
	private String claimType;
	/** 预陪/垫付/追偿 次数 是否非空：N 编码：N */
	private String subTimes;
	/** 赔付批次号 是否非空：N 编码：N */
	private String idClmBatch;
	/** 保单号 是否非空：N 编码：N */
	private String policyNo;
	/** 保单本次理算赔付金额 不包含费用及注销、拒赔 是否非空：N 编码：N */
	private String policyPayAmount;
	/** 保单本次理算费用金额 不含减损费用 是否非空：N 编码：N */
	private String policyFee;
	/** 最终赔付金额 不包含费用及注销、拒赔,支付赔款 是否非空：N 编码：N */
	private String finalPayAmount;
	/** 最终费用 不含减损费用,支付费用 是否非空：N 编码：N */
	private String finalFee;
	/** 减损费用 是否非空：N 编码：N */
	private String decreaseFee;
	/** 备注 是否非空：N 编码：N */
	private String remark;
	/** 核赔说明 是否非空：N 编码：N */
	private String settlementOpinion;
	/** 超限金额 是否非空：N 编码：N */
	private String consultAmount;
	/** 追偿费用支出 是否非空：N 编码：N */
	private String chaseFeeOut;
	/** 共保摊回赔款 是否非空：N 编码：N */
	private String claimCoinsuranceAmount;
	/** 共保摊回费用 是否非空：N 编码：N */
	private String claimCoinsuranceFee;
	/** 代位追偿金额 是否非空：N 编码：N */
	private String subrogationAmount;
	/** 币种 01-人民币 02-港币 03-美元 是否非空：N 编码：Y */
	private String currencyCode;
	/** 罚息收入 是否非空：N 编码：N */
	private String amercement;
	/** 追偿费用转回 是否非空：N 编码：N */
	private String chaseFeeBack;

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getCaseTimes() {
		return caseTimes;
	}

	public void setCaseTimes(String caseTimes) {
		this.caseTimes = caseTimes;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getSubTimes() {
		return subTimes;
	}

	public void setSubTimes(String subTimes) {
		this.subTimes = subTimes;
	}

	public String getIdClmBatch() {
		return idClmBatch;
	}

	public void setIdClmBatch(String idClmBatch) {
		this.idClmBatch = idClmBatch;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getPolicyPayAmount() {
		return policyPayAmount;
	}

	public void setPolicyPayAmount(String policyPayAmount) {
		this.policyPayAmount = policyPayAmount;
	}

	public String getPolicyFee() {
		return policyFee;
	}

	public void setPolicyFee(String policyFee) {
		this.policyFee = policyFee;
	}

	public String getFinalPayAmount() {
		return finalPayAmount;
	}

	public void setFinalPayAmount(String finalPayAmount) {
		this.finalPayAmount = finalPayAmount;
	}

	public String getFinalFee() {
		return finalFee;
	}

	public void setFinalFee(String finalFee) {
		this.finalFee = finalFee;
	}

	public String getDecreaseFee() {
		return decreaseFee;
	}

	public void setDecreaseFee(String decreaseFee) {
		this.decreaseFee = decreaseFee;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSettlementOpinion() {
		return settlementOpinion;
	}

	public void setSettlementOpinion(String settlementOpinion) {
		this.settlementOpinion = settlementOpinion;
	}

	public String getConsultAmount() {
		return consultAmount;
	}

	public void setConsultAmount(String consultAmount) {
		this.consultAmount = consultAmount;
	}

	public String getChaseFeeOut() {
		return chaseFeeOut;
	}

	public void setChaseFeeOut(String chaseFeeOut) {
		this.chaseFeeOut = chaseFeeOut;
	}

	public String getClaimCoinsuranceAmount() {
		return claimCoinsuranceAmount;
	}

	public void setClaimCoinsuranceAmount(String claimCoinsuranceAmount) {
		this.claimCoinsuranceAmount = claimCoinsuranceAmount;
	}

	public String getClaimCoinsuranceFee() {
		return claimCoinsuranceFee;
	}

	public void setClaimCoinsuranceFee(String claimCoinsuranceFee) {
		this.claimCoinsuranceFee = claimCoinsuranceFee;
	}

	public String getSubrogationAmount() {
		return subrogationAmount;
	}

	public void setSubrogationAmount(String subrogationAmount) {
		this.subrogationAmount = subrogationAmount;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getAmercement() {
		return amercement;
	}

	public void setAmercement(String amercement) {
		this.amercement = amercement;
	}

	public String getChaseFeeBack() {
		return chaseFeeBack;
	}

	public void setChaseFeeBack(String chaseFeeBack) {
		this.chaseFeeBack = chaseFeeBack;
	}

}
