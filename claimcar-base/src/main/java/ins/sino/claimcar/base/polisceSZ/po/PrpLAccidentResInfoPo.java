package ins.sino.claimcar.base.polisceSZ.po;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "PRPLACCIDENTRESINFOPO")
public class PrpLAccidentResInfoPo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String registNo;// 事故编号
	private String accidentNo;// 事故编号
	private String reporterName;// 报案人姓名
	private String reporterPhoneNo;// 报案人电话
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
	private Date createTime;
	private Date updateTime;
	private List<PrpLPlateDataPo> plateData;// 车牌信息
	private List<PrpLPhotoDataPo> photoData;// 图片信息
	
	@Column(name = "REGISTNO", length=30)
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	@Id
	@Column(name = "ACCIDENTNO", unique = true, nullable = false, length = 50)
	public String getAccidentNo() {
		return accidentNo;
	}
	public void setAccidentNo(String accidentNo) {
		this.accidentNo = accidentNo;
	}
	@Column(name = "REPORTERNAME", length=50)
	public String getReporterName() {
		return reporterName;
	}
	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}
	@Column(name = "REPORTERPHONENO", length=20)
	public String getReporterPhoneNo() {
		return reporterPhoneNo;
	}
	public void setReporterPhoneNo(String reporterPhoneNo) {
		this.reporterPhoneNo = reporterPhoneNo;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACCIDENTDATE", length = 7)
	public Date getAccidentDate() {
		return accidentDate;
	}
	public void setAccidentDate(Date accidentDate) {
		this.accidentDate = accidentDate;
	}
	@Column(name = "ACCIDENTPLACEPROVINCE", length=100)
	public String getAccidentPlaceProvince() {
		return accidentPlaceProvince;
	}
	public void setAccidentPlaceProvince(String accidentPlaceProvince) {
		this.accidentPlaceProvince = accidentPlaceProvince;
	}
	@Column(name = "ACCIDENTPLACEDISTRICT", length=50)
	public String getAccidentPlaceDistrict() {
		return accidentPlaceDistrict;
	}
	public void setAccidentPlaceDistrict(String accidentPlaceDistrict) {
		this.accidentPlaceDistrict = accidentPlaceDistrict;
	}
	@Column(name = "ACCIDENTADDRESS", length=500)
	public String getAccidentAddress() {
		return accidentAddress;
	}
	public void setAccidentAddress(String accidentAddress) {
		this.accidentAddress = accidentAddress;
	}
	@Column(name = "ACCIDENTPLACELATITUDE", length=20)
	public String getAccidentPlaceLatitude() {
		return accidentPlaceLatitude;
	}
	public void setAccidentPlaceLatitude(String accidentPlaceLatitude) {
		this.accidentPlaceLatitude = accidentPlaceLatitude;
	}
	@Column(name = "ACCIDENTPLACELONGTITUDE", length=20)
	public String getAccidentPlaceLongtitude() {
		return accidentPlaceLongtitude;
	}
	public void setAccidentPlaceLongtitude(String accidentPlaceLongtitude) {
		this.accidentPlaceLongtitude = accidentPlaceLongtitude;
	}
	@Column(name = "ACCIDENTTYPE", length=20)
	public String getAccidentType() {
		return accidentType;
	}
	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}
	@Column(name = "CHANNELTYPES", length=50)
	public String getChannelTypes() {
		return channelTypes;
	}
	public void setChannelTypes(String channelTypes) {
		this.channelTypes = channelTypes;
	}
	@Column(name = "ACCIDENTCAUSE", length=20)
	public String getAccidentCause() {
		return accidentCause;
	}
	public void setAccidentCause(String accidentCause) {
		this.accidentCause = accidentCause;
	}
	@Column(name = "ISREPORTONPORT", length=20)
	public String getIsReportOnPort() {
		return isReportOnPort;
	}
	public void setIsReportOnPort(String isReportOnPort) {
		this.isReportOnPort = isReportOnPort;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME", nullable = false, length = 7)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME", nullable = false, length = 7)
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="prpLAccidentResInfoPo")
	public List<PrpLPlateDataPo> getPlateData() {
		return plateData;
	}
	public void setPlateData(List<PrpLPlateDataPo> plateData) {
		this.plateData = plateData;
	}
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="prpLAccidentResInfoPo")
	public List<PrpLPhotoDataPo> getPhotoData() {
		return photoData;
	}
	public void setPhotoData(List<PrpLPhotoDataPo> photoData) {
		this.photoData = photoData;
	}

	@Column(name = "REPORTMODE", length = 50)
	public String getReportMode() {
		return reportMode;
	}

	public void setReportMode(String reportMode) {
		this.reportMode = reportMode;
	}
	
	

}
