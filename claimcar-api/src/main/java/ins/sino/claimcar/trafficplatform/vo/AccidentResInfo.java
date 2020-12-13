package ins.sino.claimcar.trafficplatform.vo;

import java.util.Date;
import java.util.List;

public class AccidentResInfo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String registNo;// 报案号
	private String accidentNo;// 事故编号
	private String reporterName;// 报案人姓名
	private String reporterPhoneNo;// 报案人电话
	private String accidentTime;// 出险时间
	private Date accidentDate;// 出险时间
	private String accidentPlaceProvince;// 出险地点城市
	private String accidentPlaceDistrict;// 出险地点行政区
	private String accidentAddress;// 出险地点详细地址
	private String accidentPlaceLatitude;// 出险地点经度
	private String accidentPlaceLongtitude;// 出险地点纬度
	private String accidentType;// 事故类型
	private String channelTypes;// 损失类型
	private String accidentCause;// 出险原因
	private String isReportOnPort;// 是否现场报案
	private String reportMode;// 交警系统/行业平台
	private List<PlateData> plateData;// 车牌信息
	private List<PhotoData> photoData;// 图片信息
	
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getAccidentNo() {
		return accidentNo;
	}
	public void setAccidentNo(String accidentNo) {
		this.accidentNo = accidentNo;
	}
	public String getReporterName() {
		return reporterName;
	}
	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}
	public String getReporterPhoneNo() {
		return reporterPhoneNo;
	}
	public void setReporterPhoneNo(String reporterPhoneNo) {
		this.reporterPhoneNo = reporterPhoneNo;
	}
	public String getAccidentTime() {
		return accidentTime;
	}
	public void setAccidentTime(String accidentTime) {
		this.accidentTime = accidentTime;
	}
	public String getAccidentPlaceProvince() {
		return accidentPlaceProvince;
	}
	public void setAccidentPlaceProvince(String accidentPlaceProvince) {
		this.accidentPlaceProvince = accidentPlaceProvince;
	}
	public String getAccidentPlaceDistrict() {
		return accidentPlaceDistrict;
	}
	public void setAccidentPlaceDistrict(String accidentPlaceDistrict) {
		this.accidentPlaceDistrict = accidentPlaceDistrict;
	}
	public String getAccidentAddress() {
		return accidentAddress;
	}
	public void setAccidentAddress(String accidentAddress) {
		this.accidentAddress = accidentAddress;
	}
	public String getAccidentPlaceLatitude() {
		return accidentPlaceLatitude;
	}
	public void setAccidentPlaceLatitude(String accidentPlaceLatitude) {
		this.accidentPlaceLatitude = accidentPlaceLatitude;
	}
	public String getAccidentPlaceLongtitude() {
		return accidentPlaceLongtitude;
	}
	public void setAccidentPlaceLongtitude(String accidentPlaceLongtitude) {
		this.accidentPlaceLongtitude = accidentPlaceLongtitude;
	}
	public String getAccidentType() {
		return accidentType;
	}
	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}
	public String getChannelTypes() {
		return channelTypes;
	}
	public void setChannelTypes(String channelTypes) {
		this.channelTypes = channelTypes;
	}
	public String getAccidentCause() {
		return accidentCause;
	}
	public void setAccidentCause(String accidentCause) {
		this.accidentCause = accidentCause;
	}
	public String getIsReportOnPort() {
		return isReportOnPort;
	}
	public void setIsReportOnPort(String isReportOnPort) {
		this.isReportOnPort = isReportOnPort;
	}
	public List<PlateData> getPlateData() {
		return plateData;
	}
	public void setPlateData(List<PlateData> plateData) {
		this.plateData = plateData;
	}
	public List<PhotoData> getPhotoData() {
		return photoData;
	}
	public void setPhotoData(List<PhotoData> photoData) {
		this.photoData = photoData;
	}
	public Date getAccidentDate() {
		return accidentDate;
	}
	public void setAccidentDate(Date accidentDate) {
		this.accidentDate = accidentDate;
	}
	
	public String getReportMode() {
		return reportMode;
	}

	public void setReportMode(String reportMode) {
		this.reportMode = reportMode;
	}

}
