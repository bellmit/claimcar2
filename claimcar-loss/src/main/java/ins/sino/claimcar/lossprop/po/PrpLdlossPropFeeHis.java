package ins.sino.claimcar.lossprop.po;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * POJO Class PrpLdlossPropFeeHis
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLDLOSSPROPFEEHIS_PK", allocationSize = 10)
@Table(name = "PRPLDLOSSPROPFEEHIS")
public class PrpLdlossPropFeeHis implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private PrpLdlossPropMainHis prpLdlossPropMainHis;
	private Long propFeeId;
	private String registNo;
	private String riskCode;
	private String lossItemName;
	private String lossSpeciesCode;
	private String lossSpeciesName;
	private String feeTypeCode;
	private String feeTypeName;
	private BigDecimal lossQuantity;
	private BigDecimal unitPrice;
	private String recycleFlag;
	private BigDecimal recyclePrice;
	private BigDecimal sumDefLoss;
	private BigDecimal veriLossQuantity;
	private BigDecimal veriUnitPrice;
	private BigDecimal veriRecylePrice;
	private BigDecimal sumVeriLoss;
	private String validFlag;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	private String unit;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "ID", unique = true, nullable = false, precision=12, scale=0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROPMAINHISID", nullable = false)
	public PrpLdlossPropMainHis getPrpLdlossPropMainHis() {
		return this.prpLdlossPropMainHis;
	}

	public void setPrpLdlossPropMainHis(PrpLdlossPropMainHis prpLdlossPropMainHis) {
		this.prpLdlossPropMainHis = prpLdlossPropMainHis;
	}

	@Column(name = "PROPFEEID", nullable = false, precision=12, scale=0)
	public Long getPropFeeId() {
		return this.propFeeId;
	}

	public void setPropFeeId(Long propFeeId) {
		this.propFeeId = propFeeId;
	}

	@Column(name = "REGISTNO", nullable = false, length=30)
	public String getRegistNo() {
		return this.registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	@Column(name = "RISKCODE", length=9)
	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	@Column(name = "LOSSITEMNAME")
	public String getLossItemName() {
		return this.lossItemName;
	}

	public void setLossItemName(String lossItemName) {
		this.lossItemName = lossItemName;
	}

	@Column(name = "LOSSSPECIESCODE", length=20)
	public String getLossSpeciesCode() {
		return this.lossSpeciesCode;
	}

	public void setLossSpeciesCode(String lossSpeciesCode) {
		this.lossSpeciesCode = lossSpeciesCode;
	}

	@Column(name = "LOSSSPECIESNAME", length=320)
	public String getLossSpeciesName() {
		return this.lossSpeciesName;
	}

	public void setLossSpeciesName(String lossSpeciesName) {
		this.lossSpeciesName = lossSpeciesName;
	}

	@Column(name = "FEETYPECODE", length=10)
	public String getFeeTypeCode() {
		return this.feeTypeCode;
	}

	public void setFeeTypeCode(String feeTypeCode) {
		this.feeTypeCode = feeTypeCode;
	}

	@Column(name = "FEETYPENAME")
	public String getFeeTypeName() {
		return this.feeTypeName;
	}

	public void setFeeTypeName(String feeTypeName) {
		this.feeTypeName = feeTypeName;
	}

	@Column(name = "LOSSQUANTITY", precision=14)
	public BigDecimal getLossQuantity() {
		return this.lossQuantity;
	}

	public void setLossQuantity(BigDecimal lossQuantity) {
		this.lossQuantity = lossQuantity;
	}

	@Column(name = "UNITPRICE", precision=14)
	public BigDecimal getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Column(name = "RECYCLEFLAG", length=1)
	public String getRecycleFlag() {
		return this.recycleFlag;
	}

	public void setRecycleFlag(String recycleFlag) {
		this.recycleFlag = recycleFlag;
	}

	@Column(name = "RECYCLEPRICE", precision=14)
	public BigDecimal getRecyclePrice() {
		return this.recyclePrice;
	}

	public void setRecyclePrice(BigDecimal recyclePrice) {
		this.recyclePrice = recyclePrice;
	}

	@Column(name = "SUMDEFLOSS", precision=14, scale=3)
	public BigDecimal getSumDefLoss() {
		return this.sumDefLoss;
	}

	public void setSumDefLoss(BigDecimal sumDefLoss) {
		this.sumDefLoss = sumDefLoss;
	}

	@Column(name = "VERILOSSQUANTITY", precision=14)
	public BigDecimal getVeriLossQuantity() {
		return this.veriLossQuantity;
	}

	public void setVeriLossQuantity(BigDecimal veriLossQuantity) {
		this.veriLossQuantity = veriLossQuantity;
	}

	@Column(name = "VERIUNITPRICE", precision=14)
	public BigDecimal getVeriUnitPrice() {
		return this.veriUnitPrice;
	}

	public void setVeriUnitPrice(BigDecimal veriUnitPrice) {
		this.veriUnitPrice = veriUnitPrice;
	}

	@Column(name = "VERIRECYLEPRICE", precision=14)
	public BigDecimal getVeriRecylePrice() {
		return this.veriRecylePrice;
	}

	public void setVeriRecylePrice(BigDecimal veriRecylePrice) {
		this.veriRecylePrice = veriRecylePrice;
	}

	@Column(name = "SUMVERILOSS", precision=14)
	public BigDecimal getSumVeriLoss() {
		return this.sumVeriLoss;
	}

	public void setSumVeriLoss(BigDecimal sumVeriLoss) {
		this.sumVeriLoss = sumVeriLoss;
	}

	@Column(name = "VALIDFLAG", length=1)
	public String getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	@Column(name = "CREATEUSER", length=10)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME", length=7)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "UPDATEUSER", length=10)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME", length=7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "UNIT", length=5)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}