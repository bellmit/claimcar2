package ins.sino.claimcar.claim.vo;


import java.math.BigDecimal;

public class PrpLCoinsVoBase  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String compensateNo;
	private String policyNo;
	private String coinsFlag;	//0-独家承保，1-主共，2-主联，3-从共，4-主联
	private String calculateType;	//0,1,2分别表示份额计入，全额计入，全额承担
	private String payReason;	//赔付类型
	private String coinsType;	//联共保身份
	private String chiefFlag;	//首席标志
	private String coinsCode;	//共保人机构代码
	private String coinsName;	//共保人名称
	private BigDecimal coinsRate;	//共保份额
	private String currency;	//币别
	private BigDecimal shareAmt;	//分摊金额
	
	
	
	public String getCompensateNo() {
		return compensateNo;
	}
	public void setCompensateNo(String compensateNo) {
		this.compensateNo = compensateNo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getCoinsFlag() {
		return coinsFlag;
	}
	public void setCoinsFlag(String coinsFlag) {
		this.coinsFlag = coinsFlag;
	}
	public String getCalculateType() {
		return calculateType;
	}
	public void setCalculateType(String calculateType) {
		this.calculateType = calculateType;
	}
	public String getPayReason() {
		return payReason;
	}
	public void setPayReason(String payReason) {
		this.payReason = payReason;
	}
	public String getCoinsType() {
		return coinsType;
	}
	public void setCoinsType(String coinsType) {
		this.coinsType = coinsType;
	}
	public String getChiefFlag() {
		return chiefFlag;
	}
	public void setChiefFlag(String chiefFlag) {
		this.chiefFlag = chiefFlag;
	}
	public String getCoinsCode() {
		return coinsCode;
	}
	public void setCoinsCode(String coinsCode) {
		this.coinsCode = coinsCode;
	}
	public String getCoinsName() {
		return coinsName;
	}
	public void setCoinsName(String coinsName) {
		this.coinsName = coinsName;
	}
	public BigDecimal getCoinsRate() {
		return coinsRate;
	}
	public void setCoinsRate(BigDecimal coinsRate) {
		this.coinsRate = coinsRate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public BigDecimal getShareAmt() {
		return shareAmt;
	}
	public void setShareAmt(BigDecimal shareAmt) {
		this.shareAmt = shareAmt;
	}
	
	

}
