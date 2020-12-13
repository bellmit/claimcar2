package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;
import java.util.List;

public class ClaimVo implements Serializable{
private static final long serialVersionUID = 1L;
private String license_plate_no;//车牌号
private String subject_third;//是否标的车
private String vehicle_brand_code;//车辆品牌编码
private String vehicle_brand_name;//车辆品牌名称
private String model_name;//车型名称
private String vin;//车架号
private String detection_start_time;//案件检测开始时间
private String detection_end_time;//案件检测结束时间
private PriceSummaryVo priceSummary;//价格概述相关的信息
private List<SparepartResVo> sparepart;//配件列表信息
private List<LaborResVo> labor;//工时列表
private List<SmallSparepartResVo> smallSparepart;//辅料列表
private List<FraudRisksVo> fraudRisks;//欺诈风险列表
private List<NonStandardOperationsVo> nonStandardOperations;//操作不规范提示列表
private String claimResult;//检测数值
private String reportURL;//案件检测报告的url地址
private String totalRestValue;//总减损金额
public String getLicense_plate_no() {
	return license_plate_no;
}
public void setLicense_plate_no(String license_plate_no) {
	this.license_plate_no = license_plate_no;
}
public String getSubject_third() {
	return subject_third;
}
public void setSubject_third(String subject_third) {
	this.subject_third = subject_third;
}
public String getVehicle_brand_code() {
	return vehicle_brand_code;
}
public void setVehicle_brand_code(String vehicle_brand_code) {
	this.vehicle_brand_code = vehicle_brand_code;
}
public String getVehicle_brand_name() {
	return vehicle_brand_name;
}
public void setVehicle_brand_name(String vehicle_brand_name) {
	this.vehicle_brand_name = vehicle_brand_name;
}
public String getModel_name() {
	return model_name;
}
public void setModel_name(String model_name) {
	this.model_name = model_name;
}
public String getVin() {
	return vin;
}
public void setVin(String vin) {
	this.vin = vin;
}
public String getDetection_start_time() {
	return detection_start_time;
}
public void setDetection_start_time(String detection_start_time) {
	this.detection_start_time = detection_start_time;
}
public String getDetection_end_time() {
	return detection_end_time;
}
public void setDetection_end_time(String detection_end_time) {
	this.detection_end_time = detection_end_time;
}
public PriceSummaryVo getPriceSummary() {
	return priceSummary;
}
public void setPriceSummary(PriceSummaryVo priceSummary) {
	this.priceSummary = priceSummary;
}
public List<SparepartResVo> getSparepart() {
	return sparepart;
}
public void setSparepart(List<SparepartResVo> sparepart) {
	this.sparepart = sparepart;
}
public List<LaborResVo> getLabor() {
	return labor;
}
public void setLabor(List<LaborResVo> labor) {
	this.labor = labor;
}
public List<SmallSparepartResVo> getSmallSparepart() {
	return smallSparepart;
}
public void setSmallSparepart(List<SmallSparepartResVo> smallSparepart) {
	this.smallSparepart = smallSparepart;
}
public List<FraudRisksVo> getFraudRisks() {
	return fraudRisks;
}
public void setFraudRisks(List<FraudRisksVo> fraudRisks) {
	this.fraudRisks = fraudRisks;
}
public List<NonStandardOperationsVo> getNonStandardOperations() {
	return nonStandardOperations;
}
public void setNonStandardOperations(
		List<NonStandardOperationsVo> nonStandardOperations) {
	this.nonStandardOperations = nonStandardOperations;
}
public String getClaimResult() {
	return claimResult;
}
public void setClaimResult(String claimResult) {
	this.claimResult = claimResult;
}
public String getReportURL() {
	return reportURL;
}
public void setReportURL(String reportURL) {
	this.reportURL = reportURL;
}
public String getTotalRestValue() {
	return totalRestValue;
}
public void setTotalRestValue(String totalRestValue) {
	this.totalRestValue = totalRestValue;
}


}
