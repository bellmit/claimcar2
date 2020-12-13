package ins.sino.claimcar.base.po;

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
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PK_PRPLLOSSPERSONFEEDETAIL", allocationSize = 10)
@Table(name = "PRPLLOSSPERSONFEEDETAIL")
public class PrpLLossPersonFeeDetail {

	private static final long serialVersionUID = 1L;
	private Long id;
	private PrpLLossPersonFee prpLLossPersonFee;
	private String lossItemNo;
	private String feeTypeCode;
	private String feeTypeName;
	private BigDecimal lossFee;
	private BigDecimal realPay;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence") 
	@Column(name = "ID", unique = true, nullable = false, precision=13, scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PERSONFEEID")
	public PrpLLossPersonFee getPrpLLossPersonFee() {
		return prpLLossPersonFee;
	}
	public void setPrpLLossPersonFee(PrpLLossPersonFee prpLLossPersonFee) {
		this.prpLLossPersonFee = prpLLossPersonFee;
	}
	@Column(name = "LOSSITEMNO", length=5)
	public String getLossItemNo() {
		return lossItemNo;
	}
	public void setLossItemNo(String lossItemNo) {
		this.lossItemNo = lossItemNo;
	}
	@Column(name = "FEETYPECODE", length=10)
	public String getFeeTypeCode() {
		return feeTypeCode;
	}
	public void setFeeTypeCode(String feeTypeCode) {
		this.feeTypeCode = feeTypeCode;
	}
	@Column(name = "FEETYPENAME", length=120)
	public String getFeeTypeName() {
		return feeTypeName;
	}
	public void setFeeTypeName(String feeTypeName) {
		this.feeTypeName = feeTypeName;
	}
	@Column(name = "LOSSFEE", precision=14)
	public BigDecimal getLossFee() {
		return lossFee;
	}
	public void setLossFee(BigDecimal lossFee) {
		this.lossFee = lossFee;
	}
	@Column(name = "REALPAY", precision=14)
	public BigDecimal getRealPay() {
		return realPay;
	}
	public void setRealPay(BigDecimal realPay) {
		this.realPay = realPay;
	}
	
	
}
