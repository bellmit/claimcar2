package ins.sino.claimcar.claim.selfHelpClaimCar.po;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLAUTOCASESTATEINFO_PK", allocationSize = 10)
@Table(name = "PRPLAUTOCASESTATEINFO")
public class PrpLAutocasestateInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String registNo;
	private String status;
	private Date createTime;
	private Date updateTime;
	private String flag;
	private String dlosscarFlag;//车辆类型--1-标的,3-三者
	private String licenseNo;//车牌号
	private BigDecimal feepayMoney;//自助定损金额
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="utiSequence" )
	@Column(name = "ID", unique = true, nullable = false, precision = 12, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "REGISTNO", length = 35)
	public String getRegistNo() {
		return this.registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	@Column(name = "STATUS", length = 10)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME", length = 7)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME", length = 7)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name = "FLAG", length = 10)
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	@Column(name = "DLOSSCARFLAG", length = 20)
	public String getDlosscarFlag() {
		return dlosscarFlag;
	}

	public void setDlosscarFlag(String dlosscarFlag) {
		this.dlosscarFlag = dlosscarFlag;
	}
	@Column(name = "LICENSENO", length = 50)
	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	@Column(name = "FEEPAYMONEY", precision=14)
	public BigDecimal getFeepayMoney() {
		return feepayMoney;
	}

	public void setFeepayMoney(BigDecimal feepayMoney) {
		this.feepayMoney = feepayMoney;
	}
	
}
