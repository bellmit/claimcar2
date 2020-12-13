package ins.sino.claimcar.base.po;

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

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPDLAWFIRM_PK", allocationSize = 10)
@Table(name = "PRPDLAWFIRM")

public class PrpdLawFirm {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String lawFirmCode;
	private String lawFirmName;
	private String lawFirmAddress;
	private String mobileNo;
	private String principal;
	private String contacts;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	private String validFlag;
	private String flag;
	private String remark;
	private Long payCustomId;
	private String comCode;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")   //自动生成主键
	@Column(name = "ID", unique = true, nullable = false, precision=13, scale=0)
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLawFirmCode() {
		return lawFirmCode;
	}
	public void setLawFirmCode(String lawFirmCode) {
		this.lawFirmCode = lawFirmCode;
	}
	public String getLawFirmName() {
		return lawFirmName;
	}
	public void setLawFirmName(String lawFirmName) {
		this.lawFirmName = lawFirmName;
	}
	public String getLawFirmAddress() {
		return lawFirmAddress;
	}
	public void setLawFirmAddress(String lawFirmAddress) {
		this.lawFirmAddress = lawFirmAddress;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getPayCustomId() {
		return payCustomId;
	}
	public void setPayCustomId(Long payCustomId) {
		this.payCustomId = payCustomId;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	

}
