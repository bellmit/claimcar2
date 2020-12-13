package ins.sino.claimcar.claimcarYJ.vo;

import java.util.List;

public class ClaimcarSupplyVo implements java.io.Serializable{
	private String thirdinquiryno;//三方平台询价单编号
	private String supplyid;//供货主键
	private String vin;//VIN码
	private String plateno;//车牌号
	private String brandName;//品牌
	private String modelName;//车型
	private String reportno;//报案号
	private String branch;//保险公司
	private String garagename;//修理厂名称
	private String garagetel;//修理厂电话
	private String garagecon;//修理厂联系人
	private String areaname;//修理厂省市区
	private String garagetype;//修理单位类型
	private String supplyname;//下单人姓名
	private String supplytel;//下单人电话
	private String submittime;//发起下单时间
	private String remark;//备注
	private List<CompVo> carsparelist;
	public String getThirdinquiryno() {
		return thirdinquiryno;
	}
	public void setThirdinquiryno(String thirdinquiryno) {
		this.thirdinquiryno = thirdinquiryno;
	}
	public String getSupplyid() {
		return supplyid;
	}
	public void setSupplyid(String supplyid) {
		this.supplyid = supplyid;
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
	public String getReportno() {
		return reportno;
	}
	public void setReportno(String reportno) {
		this.reportno = reportno;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getGaragename() {
		return garagename;
	}
	public void setGaragename(String garagename) {
		this.garagename = garagename;
	}
	public String getGaragetel() {
		return garagetel;
	}
	public void setGaragetel(String garagetel) {
		this.garagetel = garagetel;
	}
	public String getGaragecon() {
		return garagecon;
	}
	public void setGaragecon(String garagecon) {
		this.garagecon = garagecon;
	}
	public String getAreaname() {
		return areaname;
	}
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	public String getGaragetype() {
		return garagetype;
	}
	public void setGaragetype(String garagetype) {
		this.garagetype = garagetype;
	}
	public String getSupplyname() {
		return supplyname;
	}
	public void setSupplyname(String supplyname) {
		this.supplyname = supplyname;
	}
	public String getSupplytel() {
		return supplytel;
	}
	public void setSupplytel(String supplytel) {
		this.supplytel = supplytel;
	}
	public String getSubmittime() {
		return submittime;
	}
	public void setSubmittime(String submittime) {
		this.submittime = submittime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<CompVo> getCarsparelist() {
		return carsparelist;
	}
	public void setCarsparelist(List<CompVo> carsparelist) {
		this.carsparelist = carsparelist;
	}
	
	
}
