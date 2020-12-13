package ins.sino.claimcar.claim.po;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * POJO Class PrpDDeprecateRate
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPDDEPRECATERATE_PK", allocationSize = 10)
@Table(name = "PRPDDEPRECATERATE")
public class PrpDDeprecateRate implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String riskCode;
	private String clauseType;
	private String carKindCode;
	private String carKindName;
	private BigDecimal perYearRate;
	private String flag;
	private BigDecimal perMonthRate;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "ID", unique = true, nullable = false, precision=0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "RISKCODE", nullable = false, length=4)
	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	@Column(name = "CLAUSETYPE", nullable = false, length=3)
	public String getClauseType() {
		return this.clauseType;
	}

	public void setClauseType(String clauseType) {
		this.clauseType = clauseType;
	}

	@Column(name = "CARKINDCODE", nullable = false, length=4)
	public String getCarKindCode() {
		return this.carKindCode;
	}

	public void setCarKindCode(String carKindCode) {
		this.carKindCode = carKindCode;
	}

	@Column(name = "CARKINDNAME", length=30)
	public String getCarKindName() {
		return this.carKindName;
	}

	public void setCarKindName(String carKindName) {
		this.carKindName = carKindName;
	}

	@Column(name = "PERYEARRATE", precision=8, scale=4)
	public BigDecimal getPerYearRate() {
		return this.perYearRate;
	}

	public void setPerYearRate(BigDecimal perYearRate) {
		this.perYearRate = perYearRate;
	}

	@Column(name = "FLAG", length=2)
	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Column(name = "PERMONTHRATE", precision=8, scale=4)
	public BigDecimal getPerMonthRate() {
		return this.perMonthRate;
	}

	public void setPerMonthRate(BigDecimal perMonthRate) {
		this.perMonthRate = perMonthRate;
	}
}