package ins.sino.claimcar.claim.po;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PK_PRPLCOINS", allocationSize = 10)
@Table(name = "PRPLCOINS")
public class PrpLCoins implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String compensateNo;
	private String policyNo;
	private String coinsFlag;	//0-独家承保，1-主共，2-主联，3-从共，4-主联
	private String calculateType;	//手续费计入方式，0,1,2分别表示份额计入，全额计入，全额承担
	private String payReason;	//赔付类型
	private String coinsType;	//联共保身份
	private String chiefFlag;	//首席标志
	private String coinsCode;	//共保人机构代码
	private String coinsName;	//共保人名称
	private BigDecimal coinsRate;	//共保份额
	private String currency;	//币别
	private BigDecimal shareAmt;	//分摊金额
	
	
	@Column(name = "COMPENSATENO", nullable = false, length=25)
	public String getCompensateNo() {
		return compensateNo;
	}
	public void setCompensateNo(String compensateNo) {
		this.compensateNo = compensateNo;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence") 
	@Column(name = "ID", unique = true, nullable = false, precision=13, scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "POLICYNO", nullable = false, length=23)
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	@Column(name = "COINSFLAG", length=2)
	public String getCoinsFlag() {
		return coinsFlag;
	}
	public void setCoinsFlag(String coinsFlag) {
		this.coinsFlag = coinsFlag;
	}
	@Column(name = "CALCULATETYPE", length=4)
	public String getCalculateType() {
		return calculateType;
	}
	public void setCalculateType(String calculateType) {
		this.calculateType = calculateType;
	}
	@Column(name = "PAYREASON", length=10)
	public String getPayReason() {
		return payReason;
	}
	public void setPayReason(String payReason) {
		this.payReason = payReason;
	}
	@Column(name = "COINSTYPE", length=2)
	public String getCoinsType() {
		return coinsType;
	}
	public void setCoinsType(String coinsType) {
		this.coinsType = coinsType;
	}
	@Column(name = "CHIEFFLAG", length=2)
	public String getChiefFlag() {
		return chiefFlag;
	}
	public void setChiefFlag(String chiefFlag) {
		this.chiefFlag = chiefFlag;
	}
	@Column(name = "COINSCODE", length=30)
	public String getCoinsCode() {
		return coinsCode;
	}
	public void setCoinsCode(String coinsCode) {
		this.coinsCode = coinsCode;
	}
	@Column(name = "COINSNAME", length=100)
	public String getCoinsName() {
		return coinsName;
	}
	public void setCoinsName(String coinsName) {
		this.coinsName = coinsName;
	}
	@Column(name = "COINSRATE", precision=14)
	public BigDecimal getCoinsRate() {
		return coinsRate;
	}
	public void setCoinsRate(BigDecimal coinsRate) {
		this.coinsRate = coinsRate;
	}
	@Column(name = "CURRENCY", length=3)
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	@Column(name = "SHAREAMT", precision=14)
	public BigDecimal getShareAmt() {
		return shareAmt;
	}
	public void setShareAmt(BigDecimal shareAmt) {
		this.shareAmt = shareAmt;
	}
	
	
	

}
