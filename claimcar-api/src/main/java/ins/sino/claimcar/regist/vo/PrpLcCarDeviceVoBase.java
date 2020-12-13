package ins.sino.claimcar.regist.vo;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.math.BigDecimal;
import java.util.Date;


/**
 * Vo Base Class of PO PrpLcCarDevice
 */ 
public class PrpLcCarDeviceVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long deviceId;
	private String registNo;
	private String policyNo;
	private String riskCode;
	private Long itemNo;
	private Long serialNo;
	private String deviceName;
	private String currency;
	private Long quanTity;
	private BigDecimal purChasePrice;
	private BigDecimal actualValue;
	private String reMark;
	private String flag;
	private Date buyDate;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;

    protected PrpLcCarDeviceVoBase() {
	
    }

    
	public Long getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public String getRegistNo() {
		return this.registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}


	public String getPolicyNo() {
		return this.policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public Long getItemNo() {
		return this.itemNo;
	}

	public void setItemNo(Long itemNo) {
		this.itemNo = itemNo;
	}

	public Long getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(Long serialNo) {
		this.serialNo = serialNo;
	}

	public String getDeviceName() {
		return this.deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Long getQuanTity() {
		return this.quanTity;
	}

	public void setQuanTity(Long quanTity) {
		this.quanTity = quanTity;
	}

	public BigDecimal getPurChasePrice() {
		return this.purChasePrice;
	}

	public void setPurChasePrice(BigDecimal purChasePrice) {
		this.purChasePrice = purChasePrice;
	}

	public BigDecimal getActualValue() {
		return this.actualValue;
	}

	public void setActualValue(BigDecimal actualValue) {
		this.actualValue = actualValue;
	}

	public String getReMark() {
		return this.reMark;
	}

	public void setReMark(String reMark) {
		this.reMark = reMark;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Date getBuyDate() {
		return this.buyDate;
	}

	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}