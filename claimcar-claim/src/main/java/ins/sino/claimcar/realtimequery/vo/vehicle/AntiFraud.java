package ins.sino.claimcar.realtimequery.vo.vehicle;

import java.io.Serializable;

public class AntiFraud implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String reportNo;  //报案号
	private String vinNo;  //车架号
	private String vehicleType;  //号码种类
	private String carMark;  //号牌号码
	private String riskType;  //风险类型代码
	private String riskSource;  //风险信息来源
	private String Time;  //风险信息入库时间
	private String Name;  //人员姓名	
	private String certiType;  //证件类型	
	private String certiCode;  //证件号码	
	private String reportPhoneNo;  //电话号码	
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getVinNo() {
		return vinNo;
	}
	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getCarMark() {
		return carMark;
	}
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}
	public String getRiskType() {
		return riskType;
	}
	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}
	public String getRiskSource() {
		return riskSource;
	}
	public void setRiskSource(String riskSource) {
		this.riskSource = riskSource;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getCertiType() {
		return certiType;
	}
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}
	public String getCertiCode() {
		return certiCode;
	}
	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}
	public String getReportPhoneNo() {
		return reportPhoneNo;
	}
	public void setReportPhoneNo(String reportPhoneNo) {
		this.reportPhoneNo = reportPhoneNo;
	}
	
	
	
}
