package ins.sino.claimcar.ciitc.vo.accident;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("CarInfo") 
public class CarInfo implements Serializable{
	 private static final long serialVersionUID = 1L;

		@XStreamAlias("UUID")
		private String uUID;//车辆ID
		@XStreamAlias("licenseNo")
		private String licenseNo;//车辆号牌号码
		@XStreamAlias("licenseType")
		private String licenseType;//车辆号牌种类
		@XStreamAlias("engineNo")
		private String engineNo;//车辆发动机号
		@XStreamAlias("vin")
		private String vin;//车辆车架号
		@XStreamAlias("reportDate")
		private String reportDate;//事故发生时间
		@XStreamAlias("partyCardate")
		private String partyCardate;//驾驶车辆检验有效期至
		@XStreamAlias("partyCarStatus")
		private String partyCarStatus;//机动车状态
		public String getuUID() {
			return uUID;
		}
		public void setuUID(String uUID) {
			this.uUID = uUID;
		}
		public String getLicenseNo() {
			return licenseNo;
		}
		public void setLicenseNo(String licenseNo) {
			this.licenseNo = licenseNo;
		}
		public String getLicenseType() {
			return licenseType;
		}
		public void setLicenseType(String licenseType) {
			this.licenseType = licenseType;
		}
		public String getEngineNo() {
			return engineNo;
		}
		public void setEngineNo(String engineNo) {
			this.engineNo = engineNo;
		}
		public String getVin() {
			return vin;
		}
		public void setVin(String vin) {
			this.vin = vin;
		}
		public String getReportDate() {
			return reportDate;
		}
		public void setReportDate(String reportDate) {
			this.reportDate = reportDate;
		}
		public String getPartyCardate() {
			return partyCardate;
		}
		public void setPartyCardate(String partyCardate) {
			this.partyCardate = partyCardate;
		}
		public String getPartyCarStatus() {
			return partyCarStatus;
		}
		public void setPartyCarStatus(String partyCarStatus) {
			this.partyCarStatus = partyCarStatus;
		}
		
		
}
