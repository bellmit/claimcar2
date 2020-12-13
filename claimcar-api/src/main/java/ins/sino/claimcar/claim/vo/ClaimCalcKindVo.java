package ins.sino.claimcar.claim.vo;

/**
 * @author LiZhaoYang
 * date: 2016-02-24
 * 
 * 此类，用于计算的险别传参
 * 
 * */
public class ClaimCalcKindVo  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	/**险种代码*/
	private String RiskCode;
	
	/**险别代码*/
	private String kindCode;
	
	/**责任比例*/
	private double indemnityDutyRate;
	
	/**责任免赔率*/
	private double dutyDeductibleRate;
	
	/**绝对免赔率*/
	private double deductibleRate;
	
	/**可选免赔率/加扣免赔率*/
	private double selectDeductibleRate;
	
	/**绝对免赔额*/
	private double deductibleAmt;
	
	/**不计免赔率   针对购买不计免赔险*/
	private double exceptDeductibleRate;
	
	/**保额/限额*/
	private double amount;
	
	/**保额是否需冲减险种赔款*/
	private String deductKindFlag;
	
	/**保额冲减金额*/
	private double decuctKindAmount;
	
	/**(责任免赔或绝对免赔)是否购买免赔*/
	private String exceptFlag;
	
	/**(可选免赔或加扣免赔)是否购买免赔*/
	private String selectExceptFlag;
	
	/**新车购置价*/
	private double pureCarPrice;
	
	/**不足额投保赔付比例*/
	private double claimRate;
	
	/**是否全损*/
	private String allLossFlag;
	
	/**单位保额*/
	private double unitAmount;
	
	/**单位数量*/
	private double unitQuantity;
	
	/**施救费*/
	private double rescueFee;
	
	/**交强险或第三方 已赔付 施救费金额*/
	private double rescueFeeBZPaid;
	
	
	/**核定损失金额*/
	private double lossAmt;
	
	/**残值/损余*/
	private double restAmt;

	/**交强险已赔付金额*/
	private double BZPaid;
	
	/**该险别已经结案赔付金额*/
	private double closePaid;
	
	/**代位标志  1:代位*/
	private String subrogationFlag;
	
	/**自担金额 */
	private double takeonPay;
	/**代位金额 */
	private double subrogationPay;
	
	/**不计免赔 自担金额 */
	private double exceptTakeonPay;
	/**不计免赔 代位金额 */
	private double exceptSubrogationPay;
	
	/**计算后赔付金额=自担金额+代位金额*/
	private double calcAmt;
	
	/**不计免赔计算金额*/
	private double exceptCalcAmt;

	/**计算说明*/
	private String formulaNote;
	
	/**计算公式过程*/
	private String formulaProc;
	
	/**不计免赔 计算说明*/
	private String exceptFormulaNote;
	
	/**不计免赔 计算公式过程*/
	private String exceptFormulaProc;
	
	/**是否计算通过   1：通过  0：失败*/
	private String calcFlag;

	
	
	
	public double getExceptTakeonPay() {
		return exceptTakeonPay;
	}





	
	public void setExceptTakeonPay(double exceptTakeonPay) {
		this.exceptTakeonPay = exceptTakeonPay;
	}





	
	public double getExceptSubrogationPay() {
		return exceptSubrogationPay;
	}





	
	public void setExceptSubrogationPay(double exceptSubrogationPay) {
		this.exceptSubrogationPay = exceptSubrogationPay;
	}





	public double getExceptCalcAmt() {
		return exceptCalcAmt;
	}




	
	public void setExceptCalcAmt(double exceptCalcAmt) {
		this.exceptCalcAmt = exceptCalcAmt;
	}

	
	public String getExceptFormulaNote() {
		return exceptFormulaNote;
	}



	
	public void setExceptFormulaNote(String exceptFormulaNote) {
		this.exceptFormulaNote = exceptFormulaNote;
	}



	
	public String getExceptFormulaProc() {
		return exceptFormulaProc;
	}



	
	public void setExceptFormulaProc(String exceptFormulaProc) {
		this.exceptFormulaProc = exceptFormulaProc;
	}



	public String getSubrogationFlag() {
		return subrogationFlag;
	}


	
	public void setSubrogationFlag(String subrogationFlag) {
		this.subrogationFlag = subrogationFlag;
	}


	
	public double getTakeonPay() {
		return takeonPay;
	}


	
	public void setTakeonPay(double takeonPay) {
		this.takeonPay = takeonPay;
	}


	
	public double getSubrogationPay() {
		return subrogationPay;
	}


	
	public void setSubrogationPay(double subrogationPay) {
		this.subrogationPay = subrogationPay;
	}


	public String getRiskCode() {
		return RiskCode;
	}

	
	public void setRiskCode(String riskCode) {
		RiskCode = riskCode;
	}

	
	public String getKindCode() {
		return kindCode;
	}

	
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	
	public double getIndemnityDutyRate() {
		return indemnityDutyRate;
	}

	
	public void setIndemnityDutyRate(double indemnityDutyRate) {
		this.indemnityDutyRate = indemnityDutyRate;
	}

	
	public double getDutyDeductibleRate() {
		return dutyDeductibleRate;
	}

	
	public void setDutyDeductibleRate(double dutyDeductibleRate) {
		this.dutyDeductibleRate = dutyDeductibleRate;
	}

	
	public double getDeductibleRate() {
		return deductibleRate;
	}

	
	public void setDeductibleRate(double deductibleRate) {
		this.deductibleRate = deductibleRate;
	}

	
	public double getSelectDeductibleRate() {
		return selectDeductibleRate;
	}

	
	public void setSelectDeductibleRate(double selectDeductibleRate) {
		this.selectDeductibleRate = selectDeductibleRate;
	}

	
	

	
	
	public double getDeductibleAmt() {
		return deductibleAmt;
	}






	
	public void setDeductibleAmt(double deductibleAmt) {
		this.deductibleAmt = deductibleAmt;
	}






	public double getExceptDeductibleRate() {
		return exceptDeductibleRate;
	}

	
	public void setExceptDeductibleRate(double exceptDeductibleRate) {
		this.exceptDeductibleRate = exceptDeductibleRate;
	}

	
	
	public double getAmount() {
		return amount;
	}

	
	public void setAmount(double amount) {
		this.amount = amount;
	}

	

	
	
	public String getDeductKindFlag() {
		return deductKindFlag;
	}






	
	public void setDeductKindFlag(String deductKindFlag) {
		this.deductKindFlag = deductKindFlag;
	}






	
	public double getDecuctKindAmount() {
		return decuctKindAmount;
	}






	
	public void setDecuctKindAmount(double decuctKindAmount) {
		this.decuctKindAmount = decuctKindAmount;
	}






	public String getExceptFlag() {
		return exceptFlag;
	}

	
	public void setExceptFlag(String exceptFlag) {
		this.exceptFlag = exceptFlag;
	}

	
	public String getSelectExceptFlag() {
		return selectExceptFlag;
	}

	
	public void setSelectExceptFlag(String selectExceptFlag) {
		this.selectExceptFlag = selectExceptFlag;
	}

	
	public double getPureCarPrice() {
		return pureCarPrice;
	}

	
	public void setPureCarPrice(double pureCarPrice) {
		this.pureCarPrice = pureCarPrice;
	}

	
	public double getClaimRate() {
		return claimRate;
	}

	
	public void setClaimRate(double claimRate) {
		this.claimRate = claimRate;
	}

	
	public String getAllLossFlag() {
		return allLossFlag;
	}

	
	public void setAllLossFlag(String allLossFlag) {
		this.allLossFlag = allLossFlag;
	}

	
	public double getUnitAmount() {
		return unitAmount;
	}

	
	public void setUnitAmount(double unitAmount) {
		this.unitAmount = unitAmount;
	}

	
	public double getUnitQuantity() {
		return unitQuantity;
	}

	
	public void setUnitQuantity(double unitQuantity) {
		this.unitQuantity = unitQuantity;
	}

	
	public double getRescueFee() {
		return rescueFee;
	}

	
	public void setRescueFee(double rescueFee) {
		this.rescueFee = rescueFee;
	}

	
	public double getRescueFeeBZPaid() {
		return rescueFeeBZPaid;
	}

	
	public void setRescueFeeBZPaid(double rescueFeeBZPaid) {
		this.rescueFeeBZPaid = rescueFeeBZPaid;
	}

	
	public double getLossAmt() {
		return lossAmt;
	}

	
	public void setLossAmt(double lossAmt) {
		this.lossAmt = lossAmt;
	}

	
	public double getRestAmt() {
		return restAmt;
	}

	
	public void setRestAmt(double restAmt) {
		this.restAmt = restAmt;
	}

	
	public double getBZPaid() {
		return BZPaid;
	}

	
	public void setBZPaid(double bZPaid) {
		BZPaid = bZPaid;
	}

	
	public double getClosePaid() {
		return closePaid;
	}

	
	public void setClosePaid(double closePaid) {
		this.closePaid = closePaid;
	}

	
	public double getCalcAmt() {
		return calcAmt;
	}

	
	public void setCalcAmt(double calcAmt) {
		this.calcAmt = calcAmt;
	}

	
	

	
	public String getFormulaNote() {
		return formulaNote;
	}



	
	public void setFormulaNote(String formulaNote) {
		this.formulaNote = formulaNote;
	}



	
	public String getFormulaProc() {
		return formulaProc;
	}



	
	public void setFormulaProc(String formulaProc) {
		this.formulaProc = formulaProc;
	}



	public String getCalcFlag() {
		return calcFlag;
	}

	
	public void setCalcFlag(String calcFlag) {
		this.calcFlag = calcFlag;
	}
	
	
}
