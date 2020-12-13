package ins.sino.claimcar.lossperson.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 人伤费用审核修改查询结果VO
 * 
 * @author ★XMSH
 */
public class ChargeAdjustQueryVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String registNo;// 报案号
	private String policyNo;// 保单号
	private String licenseNo;// 车牌号
	private String personName;// 人员名称
	private String hospitalName;// 医院名称
	private String insuredName;// 被保险人名称
	private Date inHospitalDate;// 住院时间
	private String riskCode;// 险种代码
	private String mercyFlag;// 案件紧急程度
	private String customerLevel;// 客户等级
	private Date inHospitalDateStart;//入院开始时间
	private Date inHospitalDateEnd;//入院结束时间

	// 分页
	private Integer start;// 记录起始位置
	private Integer length;// 记录数

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public Date getInHospitalDate() {
		return inHospitalDate;
	}

	public void setInHospitalDate(Date inHospitalDate) {
		this.inHospitalDate = inHospitalDate;
	}

	public String getRiskCode() {
		return riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getMercyFlag() {
		return mercyFlag;
	}

	public void setMercyFlag(String mercyFlag) {
		this.mercyFlag = mercyFlag;
	}

	public String getCustomerLevel() {
		return customerLevel;
	}

	public void setCustomerLevel(String customerLevel) {
		this.customerLevel = customerLevel;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	
	/**
	 * @return 返回 inHospitalDateStart。
	 */
	public Date getInHospitalDateStart() {
		return inHospitalDateStart;
	}

	
	/**
	 * @param inHospitalDateStart 要设置的 inHospitalDateStart。
	 */
	public void setInHospitalDateStart(Date inHospitalDateStart) {
		this.inHospitalDateStart = inHospitalDateStart;
	}

	
	/**
	 * @return 返回 inHospitalDateEnd。
	 */
	public Date getInHospitalDateEnd() {
		return inHospitalDateEnd;
	}

	
	/**
	 * @param inHospitalDateEnd 要设置的 inHospitalDateEnd。
	 */
	public void setInHospitalDateEnd(Date inHospitalDateEnd) {
		this.inHospitalDateEnd = inHospitalDateEnd;
	}

}
