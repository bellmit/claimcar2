package ins.sino.claimcar.claim.po;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
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
 * POJO Class PrplLaborInfo
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLLABORINFO_PK", allocationSize = 10)
@Table(name = "PRPLLABORINFO")
public class PrplLaborInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private PrplTestinfoMain prplTestinfoMain;
	private String laborItem;
	private String laborName;
	private String laborPrice;
	private String laborCount;
	private String labortotalPrice;
	private String cePrice;
	private String ceCount;
	private String cetotalPrice;
	private String savingPrice;
	private String savingDescs;
	private Date createTime;
	private String createUser;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "ID", unique = true, nullable = false, precision = 12, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INFOMAINID", nullable = false)
	public PrplTestinfoMain getPrplTestinfoMain() {
		return this.prplTestinfoMain;
	}

	public void setPrplTestinfoMain(PrplTestinfoMain prplTestinfoMain) {
		this.prplTestinfoMain = prplTestinfoMain;
	}

	@Column(name = "LABORITEM", length = 100)
	public String getLaborItem() {
		return this.laborItem;
	}

	public void setLaborItem(String laborItem) {
		this.laborItem = laborItem;
	}

	@Column(name = "LABORNAME", length = 100)
	public String getLaborName() {
		return this.laborName;
	}

	public void setLaborName(String laborName) {
		this.laborName = laborName;
	}

	@Column(name = "LABORPRICE", length = 20)
	public String getLaborPrice() {
		return this.laborPrice;
	}

	public void setLaborPrice(String laborPrice) {
		this.laborPrice = laborPrice;
	}

	@Column(name = "LABORCOUNT", length = 25)
	public String getLaborCount() {
		return this.laborCount;
	}

	public void setLaborCount(String laborCount) {
		this.laborCount = laborCount;
	}

	@Column(name = "LABORTOTALPRICE", length = 25)
	public String getLabortotalPrice() {
		return this.labortotalPrice;
	}

	public void setLabortotalPrice(String labortotalPrice) {
		this.labortotalPrice = labortotalPrice;
	}

	@Column(name = "CEPRICE", length = 20)
	public String getCePrice() {
		return this.cePrice;
	}

	public void setCePrice(String cePrice) {
		this.cePrice = cePrice;
	}

	@Column(name = "CECOUNT", length = 25)
	public String getCeCount() {
		return this.ceCount;
	}

	public void setCeCount(String ceCount) {
		this.ceCount = ceCount;
	}

	@Column(name = "CETOTALPRICE", length = 25)
	public String getCetotalPrice() {
		return this.cetotalPrice;
	}

	public void setCetotalPrice(String cetotalPrice) {
		this.cetotalPrice = cetotalPrice;
	}

	@Column(name = "SAVINGPRICE", length = 25)
	public String getSavingPrice() {
		return this.savingPrice;
	}

	public void setSavingPrice(String savingPrice) {
		this.savingPrice = savingPrice;
	}

	@Column(name = "SAVINGDESCS", length = 500)
	public String getSavingDescs() {
		return this.savingDescs;
	}

	public void setSavingDescs(String savingDescs) {
		this.savingDescs = savingDescs;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME", length = 7)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATEUSER", length = 25)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	
}