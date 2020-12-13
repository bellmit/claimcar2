package ins.sino.claimcar.claim.vo;

import java.io.Serializable;
import java.math.BigDecimal;


public class ClaimFeeExt implements Serializable {
	private static final long serialVersionUID = 1L;

	private String kindCode;
	
	private String riskCode;

	private String feeTypeCode;
	
	private Integer itemKindNo;

	private BigDecimal sumClaim;

	private BigDecimal estiPaid;
	
	private BigDecimal personSumClaim;// 为了B险存人员损失（为了将财产与人员损失分开,便于强制立案决定是取案均损失还是取实际损失金额）

	private BigDecimal personEstiPaid;// 为了B险存人员赔款（为了将财产与人员损失分开,便于强制立案决定是取案均赔款还是取实际赔款金额）
	
	private BigDecimal rescueFee;

	private BigDecimal sumRest;

	private BigDecimal deductRate;// 不计免赔率
	private BigDecimal deductLoss;// 不计免赔金额

	private BigDecimal sumAmount;

	private BigDecimal unitAmount;

	private String adjustReason;
	
	private String modeCode;//D1使用 01.司机 02.乘客
	
	private BigDecimal indemnityDutyRate;//事故责任比例，同一立案下的值相同，为了在DWR返回的list中能有事故责任比例，加入此成员变量

	private Long id;
	
	private boolean mKindCode;
	
	private String roundFlag;//四舍五入标志 2:五入 1：四舍 0：不需四舍五入

	private String mKindCodeOld;//不计免赔险对应的险别

	private BigDecimal carLossRate; // 车辆损失金额占车财总损失金额的比例
	
	public String getKindCode() {
		return kindCode;
	}

	
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	
	public String getRiskCode() {
		return riskCode;
	}

	
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	
	public String getFeeTypeCode() {
		return feeTypeCode;
	}

	
	public void setFeeTypeCode(String feeTypeCode) {
		this.feeTypeCode = feeTypeCode;
	}

	
	public Integer getItemKindNo() {
		return itemKindNo;
	}

	
	public void setItemKindNo(Integer itemKindNo) {
		this.itemKindNo = itemKindNo;
	}

	
	public BigDecimal getSumClaim() {
		return sumClaim;
	}

	
	public void setSumClaim(BigDecimal sumClaim) {
		this.sumClaim = sumClaim;
	}

	
	public BigDecimal getEstiPaid() {
		return estiPaid;
	}

	
	public void setEstiPaid(BigDecimal estiPaid) {
		this.estiPaid = estiPaid;
	}

	
	public BigDecimal getPersonSumClaim() {
		return personSumClaim;
	}

	
	public void setPersonSumClaim(BigDecimal personSumClaim) {
		this.personSumClaim = personSumClaim;
	}

	
	public BigDecimal getPersonEstiPaid() {
		return personEstiPaid;
	}

	
	public void setPersonEstiPaid(BigDecimal personEstiPaid) {
		this.personEstiPaid = personEstiPaid;
	}

	
	public BigDecimal getRescueFee() {
		return rescueFee;
	}

	
	public void setRescueFee(BigDecimal rescueFee) {
		this.rescueFee = rescueFee;
	}

	
	public BigDecimal getSumRest() {
		return sumRest;
	}

	
	public void setSumRest(BigDecimal sumRest) {
		this.sumRest = sumRest;
	}

	
	public BigDecimal getDeductRate() {
		return deductRate;
	}

	
	public void setDeductRate(BigDecimal deductRate) {
		this.deductRate = deductRate;
	}

	
	public BigDecimal getSumAmount() {
		return sumAmount;
	}

	
	public void setSumAmount(BigDecimal sumAmount) {
		this.sumAmount = sumAmount;
	}

	
	public BigDecimal getUnitAmount() {
		return unitAmount;
	}

	
	public void setUnitAmount(BigDecimal unitAmount) {
		this.unitAmount = unitAmount;
	}

	
	public String getAdjustReason() {
		return adjustReason;
	}

	
	public void setAdjustReason(String adjustReason) {
		this.adjustReason = adjustReason;
	}

	
	public String getModeCode() {
		return modeCode;
	}

	
	public void setModeCode(String modeCode) {
		this.modeCode = modeCode;
	}

	
	public BigDecimal getIndemnityDutyRate() {
		return indemnityDutyRate;
	}

	
	public void setIndemnityDutyRate(BigDecimal indemnityDutyRate) {
		this.indemnityDutyRate = indemnityDutyRate;
	}

	
	public Long getId() {
		return id;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	
	public boolean ismKindCode() {
		return mKindCode;
	}

	
	public void setmKindCode(boolean mKindCode) {
		this.mKindCode = mKindCode;
	}

	
	public String getRoundFlag() {
		return roundFlag;
	}

	
	public void setRoundFlag(String roundFlag) {
		this.roundFlag = roundFlag;
	}

	
	public String getmKindCodeOld() {
		return mKindCodeOld;
	}

	
	public void setmKindCodeOld(String mKindCodeOld) {
		this.mKindCodeOld = mKindCodeOld;
	}

	public BigDecimal getDeductLoss() {
		return deductLoss;
	}

	public void setDeductLoss(BigDecimal deductLoss) {
		this.deductLoss = deductLoss;
	}


	public BigDecimal getCarLossRate() {
		return carLossRate;
	}


	public void setCarLossRate(BigDecimal carLossRate) {
		this.carLossRate = carLossRate;
	}
	
}
