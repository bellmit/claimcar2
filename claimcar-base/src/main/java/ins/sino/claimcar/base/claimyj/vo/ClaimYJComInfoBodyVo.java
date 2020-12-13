package ins.sino.claimcar.base.claimyj.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class ClaimYJComInfoBodyVo {
	@XStreamAlias("ENQUIRYNO")
	private String enquiryNo;
	@XStreamAlias("THIRDINQUIRYNO")
	private String thirdinquiryNo;
	@XStreamAlias("PLATENO")
	private String plateNo;
	@XStreamAlias("VIN")
	private String vin;
	@XStreamAlias("BRANDNAME")
	private String brandName;
	@XStreamAlias("MODELNAME")
	private String modelName;
	@XStreamAlias("OPERATORNAME")
	private String operatorName;
	@XStreamAlias("OPERATETIME")
	private String operateTime;
	@XStreamAlias("AUTONOSTOCK")
	private String autoNoStock;
	@XStreamAlias("CARID")
	private String carId;
	@XStreamAlias("REPORTNO")
	private String reportNo;
	@XStreamAlias("PARTOFFERS")
	private List<PartOffersVo> PartOffersVo;
	
	
	
	public String getEnquiryNo() {
		return enquiryNo;
	}
	public void setEnquiryNo(String enquiryNo) {
		this.enquiryNo = enquiryNo;
	}
	public String getThirdinquiryNo() {
		return thirdinquiryNo;
	}
	public void setThirdinquiryNo(String thirdinquiryNo) {
		this.thirdinquiryNo = thirdinquiryNo;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
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
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	public String getAutoNoStock() {
		return autoNoStock;
	}
	public void setAutoNoStock(String autoNoStock) {
		this.autoNoStock = autoNoStock;
	}
	public List<PartOffersVo> getPartOffersVo() {
		return PartOffersVo;
	}
	public void setPartOffersVo(List<PartOffersVo> partOffersVo) {
		PartOffersVo = partOffersVo;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	
	
	
	
}
