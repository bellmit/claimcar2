package ins.sino.claimcar.certify.vo;

import java.math.BigDecimal;

/**
 * Custom VO class of PO PrpLCertifyMain
 */ 
public class PrpLCertifyMainVo extends PrpLCertifyMainVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private BigDecimal defLossCar;
	private BigDecimal defLossProp;
	private BigDecimal defLossPersion;
	private String isFraud;  //是否欺诈
	private String fraudLogo;  //欺诈标志
	private String fraudType;  //欺诈类型
	private BigDecimal fraudRecoverAmount;  //欺诈挽回损失金额
	private String isJQFraud; //交强拒赔
	private String isSYFraud; //商业拒赔
	private String fraudRefuseReason; //fraudRefuseReason
	
	public BigDecimal getDefLossCar() {
		return defLossCar;
	}
	public void setDefLossCar(BigDecimal defLossCar) {
		this.defLossCar = defLossCar;
	}
	public BigDecimal getDefLossProp() {
		return defLossProp;
	}
	public void setDefLossProp(BigDecimal defLossProp) {
		this.defLossProp = defLossProp;
	}
	public BigDecimal getDefLossPersion() {
		return defLossPersion;
	}
	public void setDefLossPersion(BigDecimal defLossPersion) {
		this.defLossPersion = defLossPersion;
	}
	public String getIsFraud() {
		return isFraud;
	}
	public void setIsFraud(String isFraud) {
		this.isFraud = isFraud;
	}
	public String getFraudLogo() {
		return fraudLogo;
	}
	public void setFraudLogo(String fraudLogo) {
		this.fraudLogo = fraudLogo;
	}
	public String getFraudType() {
		return fraudType;
	}
	public void setFraudType(String fraudType) {
		this.fraudType = fraudType;
	}
	public BigDecimal getFraudRecoverAmount() {
		return fraudRecoverAmount;
	}
	public void setFraudRecoverAmount(BigDecimal fraudRecoverAmount) {
		this.fraudRecoverAmount = fraudRecoverAmount;
	}
	public String getIsJQFraud() {
		return isJQFraud;
	}
	public void setIsJQFraud(String isJQFraud) {
		this.isJQFraud = isJQFraud;
	}
	public String getIsSYFraud() {
		return isSYFraud;
	}
	public void setIsSYFraud(String isSYFraud) {
		this.isSYFraud = isSYFraud;
	}
	public String getFraudRefuseReason() {
		return fraudRefuseReason;
	}
	public void setFraudRefuseReason(String fraudRefuseReason) {
		this.fraudRefuseReason = fraudRefuseReason;
	}
	
}
