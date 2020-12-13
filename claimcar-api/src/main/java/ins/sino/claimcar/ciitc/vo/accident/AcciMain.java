package ins.sino.claimcar.ciitc.vo.accident;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("AcciMain") 
public class AcciMain implements Serializable{
	 private static final long serialVersionUID = 1L;

		@XStreamAlias("acciNo")
		private String acciNo;//事故编号
		@XStreamAlias("regAcciNo")
		private String regAcciNo;//事故编号（自建）
		@XStreamAlias("acciAreaCode")
		private String acciAreaCode;//事故地区代码
		@XStreamAlias("reportDate")
		private String reportDate;//事故发生时间
		@XStreamAlias("weather")
		private String weather;//天气
		@XStreamAlias("acciAddress")
		private String acciAddress;//事故地点
		@XStreamAlias("longitude")
		private String longitude;//经度
		@XStreamAlias("latitude")
		private String latitude;//纬度
		@XStreamAlias("coordType")
		private String coordType;//经纬度类型
		@XStreamAlias("provinceCode")
		private String provinceCode;//事故行政区域（省级）
		@XStreamAlias("cityCode")
		private String cityCode;//事故行政区域（地市级）
		@XStreamAlias("countryCode")
		private String countryCode;//事故行政区域（区县级）
		@XStreamAlias("acciSituation")
		private String acciSituation;//事故经过
		@XStreamAlias("acciDescribe")
		private String acciDescribe;//事故描述
		@XStreamAlias("acciType")
		private String acciType;//事故类型
		@XStreamAlias("acciSolution")
		private String acciSolution;//事故处理方式
		@XStreamAlias("acciHandleOrgan")
		private String acciHandleOrgan;//事故处理机关
		@XStreamAlias("police")
		private String police;//事故处理民警
		@XStreamAlias("caseFlag")
		private String caseFlag;//业务类型
		@XStreamAlias("dutyTime")
		private String dutyTime;//定责时间
		public String getAcciNo() {
			return acciNo;
		}
		public void setAcciNo(String acciNo) {
			this.acciNo = acciNo;
		}
		public String getAcciAreaCode() {
			return acciAreaCode;
		}
		public void setAcciAreaCode(String acciAreaCode) {
			this.acciAreaCode = acciAreaCode;
		}
		public String getReportDate() {
			return reportDate;
		}
		public void setReportDate(String reportDate) {
			this.reportDate = reportDate;
		}
		public String getWeather() {
			return weather;
		}
		public void setWeather(String weather) {
			this.weather = weather;
		}
		public String getAcciAddress() {
			return acciAddress;
		}
		public void setAcciAddress(String acciAddress) {
			this.acciAddress = acciAddress;
		}
		public String getProvinceCode() {
			return provinceCode;
		}
		public void setProvinceCode(String provinceCode) {
			this.provinceCode = provinceCode;
		}
		public String getCityCode() {
			return cityCode;
		}
		public void setCityCode(String cityCode) {
			this.cityCode = cityCode;
		}
		public String getCountryCode() {
			return countryCode;
		}
		public void setCountryCode(String countryCode) {
			this.countryCode = countryCode;
		}
		public String getAcciSituation() {
			return acciSituation;
		}
		public void setAcciSituation(String acciSituation) {
			this.acciSituation = acciSituation;
		}
		public String getAcciDescribe() {
			return acciDescribe;
		}
		public void setAcciDescribe(String acciDescribe) {
			this.acciDescribe = acciDescribe;
		}
		public String getAcciType() {
			return acciType;
		}
		public void setAcciType(String acciType) {
			this.acciType = acciType;
		}
		public String getAcciSolution() {
			return acciSolution;
		}
		public void setAcciSolution(String acciSolution) {
			this.acciSolution = acciSolution;
		}
		public String getAcciHandleOrgan() {
			return acciHandleOrgan;
		}
		public void setAcciHandleOrgan(String acciHandleOrgan) {
			this.acciHandleOrgan = acciHandleOrgan;
		}
		public String getPolice() {
			return police;
		}
		public void setPolice(String police) {
			this.police = police;
		}
		public String getCaseFlag() {
			return caseFlag;
		}
		public void setCaseFlag(String caseFlag) {
			this.caseFlag = caseFlag;
		}
		public String getDutyTime() {
			return dutyTime;
		}
		public void setDutyTime(String dutyTime) {
			this.dutyTime = dutyTime;
		}

	public String getRegAcciNo() {
		return regAcciNo;
	}

	public void setRegAcciNo(String regAcciNo) {
		this.regAcciNo = regAcciNo;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getCoordType() {
		return coordType;
	}

	public void setCoordType(String coordType) {
		this.coordType = coordType;
	}
}
