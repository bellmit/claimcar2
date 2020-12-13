package ins.sino.claimcar.base.po;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * POJO Class PrpLKindAmtSummary
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLKINDAMTSUMMARY_PK", allocationSize = 10)
@Table(name = "PRPLKINDAMTSUMMARY")
public class PrpLKindAmtSummary implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private PrpLCompensate prpLCompensate;
	private String kindCode;
	private String lossKind;
	private BigDecimal sumLoss;
	private BigDecimal sumRealPay;
	private Date createTime;
	private Date updateTime;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence") 
	@Column(name = "ID", unique = true, nullable = false, precision=13, scale=0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="COMPENSATENO", nullable = false)
	public PrpLCompensate getPrpLCompensate() {
		return this.prpLCompensate;
	}

	public void setPrpLCompensate(PrpLCompensate prpLCompensate) {
		this.prpLCompensate = prpLCompensate;
	}

	@Column(name = "KINDCODE", nullable = false, length=6)
	public String getKindCode() {
		return this.kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	@Column(name = "LOSSKIND", length=2)
	public String getLossKind() {
		return this.lossKind;
	}

	public void setLossKind(String lossKind) {
		this.lossKind = lossKind;
	}

	@Column(name = "SUMLOSS", precision=14)
	public BigDecimal getSumLoss() {
		return this.sumLoss;
	}

	public void setSumLoss(BigDecimal sumLoss) {
		this.sumLoss = sumLoss;
	}

	@Column(name = "SUMREALPAY", precision=14)
	public BigDecimal getSumRealPay() {
		return this.sumRealPay;
	}

	public void setSumRealPay(BigDecimal sumRealPay) {
		this.sumRealPay = sumRealPay;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME", length=7)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME", length=7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}