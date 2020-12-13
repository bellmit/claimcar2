package ins.sino.claimcar.newpayment.vo;

import java.io.Serializable;
import java.math.BigDecimal;



public class PrpJlossPlanSubVatVo implements Serializable{

    private static final long serialVersionUID = 734755865769786338L;

    /** 业务类型 是否必传：Y 说明：见码表4.1.5 */
    private String certiType;
    /** 业务号 是否必传：Y 说明：计算书号，预赔号 */
    private String certiNo;
    /** 保单号 是否必传：Y 说明： */
    private String policyNo;
    /** 序号 是否必传：Y 说明： */
    private String serialNo;
    /** 赔付类型 是否必传：Y 说明：见码表4.1.2 */
    private String lossType;
    /** 费用类型 是否必传：Y 说明：*/
    private String chargeCode;

    /** 退回重付关联ID 是否必传：N 说明： */
    private String correlateID;
    /** 险类 是否必传：Y 说明： */
    private String classCode;
    /** 险种 是否必传：Y 说明： */
    private String riskCode;
    /** 条款代码 是否必传：N 说明： */
    private String kindCode;
    /** 收付金额 是否必传：Y 说明： */
    private BigDecimal planFee;
    /** 不含税金额 是否必传：Y 说明： */
    private BigDecimal sumAmountNT;
    /** 税率 是否必传：Y 说明： */
    private BigDecimal taxRate;
    /** 总税额 是否必传：Y 说明： */
    private BigDecimal sumAmountTax;
	public String getCertiType() {
		return certiType;
	}
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}
	public String getCertiNo() {
		return certiNo;
	}
	public void setCertiNo(String certiNo) {
		this.certiNo = certiNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getLossType() {
		return lossType;
	}
	public void setLossType(String lossType) {
		this.lossType = lossType;
	}
	public String getChargeCode() {
		return chargeCode;
	}
	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}
	public String getCorrelateID() {
		return correlateID;
	}
	public void setCorrelateID(String correlateID) {
		this.correlateID = correlateID;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getRiskCode() {
		return riskCode;
	}
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	public String getKindCode() {
		return kindCode;
	}
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}
	public BigDecimal getPlanFee() {
		return planFee;
	}
	public void setPlanFee(BigDecimal planFee) {
		this.planFee = planFee;
	}
	public BigDecimal getSumAmountNT() {
		return sumAmountNT;
	}
	public void setSumAmountNT(BigDecimal sumAmountNT) {
		this.sumAmountNT = sumAmountNT;
	}
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	public BigDecimal getSumAmountTax() {
		return sumAmountTax;
	}
	public void setSumAmountTax(BigDecimal sumAmountTax) {
		this.sumAmountTax = sumAmountTax;
	}
    
    
}
