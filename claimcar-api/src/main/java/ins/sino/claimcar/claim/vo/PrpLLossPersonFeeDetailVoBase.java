package ins.sino.claimcar.claim.vo;


import java.math.BigDecimal;

public class PrpLLossPersonFeeDetailVoBase implements java.io.Serializable  {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String lossItemNo;
	private String feeTypeCode;
	private String feeTypeName;
	private BigDecimal lossFee;
	private BigDecimal realPay;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLossItemNo() {
		return lossItemNo;
	}
	public void setLossItemNo(String lossItemNo) {
		this.lossItemNo = lossItemNo;
	}
	public String getFeeTypeCode() {
		return feeTypeCode;
	}
	public void setFeeTypeCode(String feeTypeCode) {
		this.feeTypeCode = feeTypeCode;
	}
	public String getFeeTypeName() {
		return feeTypeName;
	}
	public void setFeeTypeName(String feeTypeName) {
		this.feeTypeName = feeTypeName;
	}
	public BigDecimal getLossFee() {
		return lossFee;
	}
	public void setLossFee(BigDecimal lossFee) {
		this.lossFee = lossFee;
	}
	public BigDecimal getRealPay() {
		return realPay;
	}
	public void setRealPay(BigDecimal realPay) {
		this.realPay = realPay;
	}
	
	
}
