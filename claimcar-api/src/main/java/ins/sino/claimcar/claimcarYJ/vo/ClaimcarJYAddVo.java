package ins.sino.claimcar.claimcarYJ.vo;

import java.util.List;

public class ClaimcarJYAddVo implements java.io.Serializable{
 private static final long serialVersionUID = 1L;
 private String thirdInquiryNo;//三方询价单号
 private String brandName;//车辆品牌名称
 private String modelName;//车型名称
 private String vin;//车架号
 private String plateno;//车牌号
 private String branch;//保险公司
 private String garageName;//修理厂名称
 private String garageTel;//修理厂电话
 private String garageCon;//修理厂联系人
 private String inquiryName;//询价人姓名
 private String inquiryTel;//询价人电话
 private String submitTime;//发起询价时间时间戳
 private String remark;//询价备注	
 private String proname;//省名称
 private String areaname;//市名称
 private String reportno;//报案号
 private String carId;//车辆Id
 private String[] pic;//事故车损照片
 private String[] drivepic;//行驶证照片地址
 private List<CarspareAddVo> carspareList;//配件信息清单列表(List)
public String getThirdInquiryNo() {
	return thirdInquiryNo;
}
public void setThirdInquiryNo(String thirdInquiryNo) {
	this.thirdInquiryNo = thirdInquiryNo;
}
public String getBrandName() {
	return brandName;
}
public void setBrandName(String brandName) {
	this.brandName = brandName;
}
public String getModelName() {
	return modelName;
}
public void setModelName(String modelName) {
	this.modelName = modelName;
}
public String getVin() {
	return vin;
}
public void setVin(String vin) {
	this.vin = vin;
}
public String getPlateno() {
	return plateno;
}
public void setPlateno(String plateno) {
	this.plateno = plateno;
}
public String getBranch() {
	return branch;
}
public void setBranch(String branch) {
	this.branch = branch;
}
public String getGarageName() {
	return garageName;
}
public void setGarageName(String garageName) {
	this.garageName = garageName;
}
public String getGarageTel() {
	return garageTel;
}
public void setGarageTel(String garageTel) {
	this.garageTel = garageTel;
}
public String getGarageCon() {
	return garageCon;
}
public void setGarageCon(String garageCon) {
	this.garageCon = garageCon;
}
public String getInquiryName() {
	return inquiryName;
}
public void setInquiryName(String inquiryName) {
	this.inquiryName = inquiryName;
}
public String getInquiryTel() {
	return inquiryTel;
}
public void setInquiryTel(String inquiryTel) {
	this.inquiryTel = inquiryTel;
}
public String getSubmitTime() {
	return submitTime;
}
public void setSubmitTime(String submitTime) {
	this.submitTime = submitTime;
}
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}
public String getProname() {
	return proname;
}
public void setProname(String proname) {
	this.proname = proname;
}
public String getAreaname() {
	return areaname;
}
public void setAreaname(String areaname) {
	this.areaname = areaname;
}
public String getReportno() {
	return reportno;
}
public void setReportno(String reportno) {
	this.reportno = reportno;
}
public String[] getPic() {
	return pic;
}
public void setPic(String[] pic) {
	this.pic = pic;
}
public String[] getDrivepic() {
	return drivepic;
}
public void setDrivepic(String[] drivepic) {
	this.drivepic = drivepic;
}
public List<CarspareAddVo> getCarspareList() {
	return carspareList;
}
public void setCarspareList(List<CarspareAddVo> carspareList) {
	this.carspareList = carspareList;
}
public String getCarId() {
	return carId;
}
public void setCarId(String carId) {
	this.carId = carId;
}


 
}
