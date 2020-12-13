package ins.sino.claimcar.claimjy.vo;


import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("Item")
public class LossPolicyItemVo implements Serializable {
	private static final long serialVersionUID = -7242051068123211106L;
	@XStreamAlias("Id")
	private String id;
	@XStreamAlias("PolicyCode")
	private String policyCode;
	@XStreamAlias("ReportCode")
	private String reportCode;
	@XStreamAlias("InsureBgnDate")
	private String insureBgnDate;
	@XStreamAlias("InsureEndDate")
	private String insureEndDate;
	@XStreamAlias("VehicleCode")
	private String vehicleCode;
	@XStreamAlias("VehicleName")
	private String vehicleName;
	@XStreamAlias("InsuredPerson")
	private String insuredPerson;
	@XStreamAlias("CompanyCode")
	private String companyCode;
	@XStreamAlias("CompanyName")
	private String companyName;
	@XStreamAlias("TotalInsSum")
	private String totalInsSum;
	@XStreamAlias("RiskType")
	private String riskType;
	@XStreamAlias("RiskCode")
	private String riskCode;
	@XStreamAlias("RiskName")
	private String riskName;
	@XStreamAlias("CustomerTypeCode")
	private String customerTypeCode;
	@XStreamAlias("CustomerTypeName")
	private String customerTypeName;
	@XStreamAlias("SpecifyAssume")
	private String specifyAssume;
	@XStreamAlias("ChannelCode")
	private String channelCode;
	@XStreamAlias("ChannelName")
	private String channelName;
	@XStreamAlias("BillDate")
	private String billDate;
	@XStreamAlias("PolicyHoldDate")
	private String policyHoldDate;
	@XStreamAlias("CarOwner")
	private String carOwner;
	@XStreamAlias("IdentityNo")
	private String identityNo;
	@XStreamAlias("DriverName")
	private String driverName;
	@XStreamAlias("AccidentNum")
	private String accidentNum;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPolicyCode() {
		return policyCode;
	}
	public void setPolicyCode(String policyCode) {
		this.policyCode = policyCode;
	}
	public String getReportCode() {
		return reportCode;
	}
	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
	public String getVehicleCode() {
		return vehicleCode;
	}
	public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode;
	}
	public String getVehicleName() {
		return vehicleName;
	}
	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}
	public String getInsuredPerson() {
		return insuredPerson;
	}
	public void setInsuredPerson(String insuredPerson) {
		this.insuredPerson = insuredPerson;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getTotalInsSum() {
		return totalInsSum;
	}
	public void setTotalInsSum(String totalInsSum) {
		this.totalInsSum = totalInsSum;
	}
	public String getRiskType() {
		return riskType;
	}
	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}
	public String getRiskCode() {
		return riskCode;
	}
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	public String getRiskName() {
		return riskName;
	}
	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}
	public String getCustomerTypeCode() {
		return customerTypeCode;
	}
	public void setCustomerTypeCode(String customerTypeCode) {
		this.customerTypeCode = customerTypeCode;
	}
	public String getCustomerTypeName() {
		return customerTypeName;
	}
	public void setCustomerTypeName(String customerTypeName) {
		this.customerTypeName = customerTypeName;
	}
	public String getSpecifyAssume() {
		return specifyAssume;
	}
	public void setSpecifyAssume(String specifyAssume) {
		this.specifyAssume = specifyAssume;
	}
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getInsureBgnDate() {
		return insureBgnDate;
	}
	public void setInsureBgnDate(String insureBgnDate) {
		this.insureBgnDate = insureBgnDate;
	}
	public String getInsureEndDate() {
		return insureEndDate;
	}
	public void setInsureEndDate(String insureEndDate) {
		this.insureEndDate = insureEndDate;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getPolicyHoldDate() {
		return policyHoldDate;
	}
	public void setPolicyHoldDate(String policyHoldDate) {
		this.policyHoldDate = policyHoldDate;
	}
	public String getCarOwner() {
		return carOwner;
	}
	public void setCarOwner(String carOwner) {
		this.carOwner = carOwner;
	}
	public String getIdentityNo() {
		return identityNo;
	}
	public void setIdentityNo(String identityNo) {
		this.identityNo = identityNo;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getAccidentNum() {
		return accidentNum;
	}
	public void setAccidentNum(String accidentNum) {
		this.accidentNum = accidentNum;
	}
}
