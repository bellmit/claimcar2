package ins.sino.claimcar.policyInfo.register.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommercialClaimPeerVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String InsurerCode;//公司代码
	private String ClaimSequenceNo;//理赔编码
	private String DriverName;//三者车驾驶员姓名
	private String CertiCode;//三者车驾驶证件号码
	private String LicensePlateNo;//三者车辆号牌号码
	private String LicensePlateType;//三者车辆号码种类
	private String VIN;//三者车辆车架号
	private String EngineNo;//三者车辆发动机号
	private String RepairFactoryName;//修理机构名称
	private String EstimateType;//定损方式
	public String getInsurerCode() {
		return InsurerCode;
	}
	public void setInsurerCode(String insurerCode) {
		InsurerCode = insurerCode;
	}
	public String getClaimSequenceNo() {
		return ClaimSequenceNo;
	}
	public void setClaimSequenceNo(String claimSequenceNo) {
		ClaimSequenceNo = claimSequenceNo;
	}
	public String getDriverName() {
		return DriverName;
	}
	public void setDriverName(String driverName) {
		DriverName = driverName;
	}
	public String getCertiCode() {
		return CertiCode;
	}
	public void setCertiCode(String certiCode) {
		CertiCode = certiCode;
	}
	public String getLicensePlateNo() {
		return LicensePlateNo;
	}
	public void setLicensePlateNo(String licensePlateNo) {
		LicensePlateNo = licensePlateNo;
	}
	public String getLicensePlateType() {
		return LicensePlateType;
	}
	public void setLicensePlateType(String licensePlateType) {
		LicensePlateType = licensePlateType;
	}
	public String getVIN() {
		return VIN;
	}
	public void setVIN(String vIN) {
		VIN = vIN;
	}
	public String getEngineNo() {
		return EngineNo;
	}
	public void setEngineNo(String engineNo) {
		EngineNo = engineNo;
	}
	public String getRepairFactoryName() {
		return RepairFactoryName;
	}
	public void setRepairFactoryName(String repairFactoryName) {
		RepairFactoryName = repairFactoryName;
	}
	public String getEstimateType() {
		return EstimateType;
	}
	public void setEstimateType(String estimateType) {
		EstimateType = estimateType;
	}
	

}
