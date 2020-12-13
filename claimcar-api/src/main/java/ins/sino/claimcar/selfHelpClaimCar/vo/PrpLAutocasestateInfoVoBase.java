package ins.sino.claimcar.selfHelpClaimCar.vo;

import java.math.BigDecimal;
import java.util.Date;

public class PrpLAutocasestateInfoVoBase implements java.io.Serializable {
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
	protected PrpLAutocasestateInfoVoBase() {

	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getRegistNo() {
		return this.registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getDlosscarFlag() {
		return dlosscarFlag;
	}

	public void setDlosscarFlag(String dlosscarFlag) {
		this.dlosscarFlag = dlosscarFlag;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public BigDecimal getFeepayMoney() {
		return feepayMoney;
	}

	public void setFeepayMoney(BigDecimal feepayMoney) {
		this.feepayMoney = feepayMoney;
	}
	
	
}
