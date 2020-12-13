package ins.sino.claimcar.ciitc.vo.accident;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("PartyInfo") 
public class PartyInfo implements Serializable{
	 private static final long serialVersionUID = 1L;

		@XStreamAlias("ID")
		private String id;//当事人序号
		@XStreamAlias("UUID")
		private String uUID;//车辆ID
		@XStreamAlias("partyName")
		private String partyName;//当事人姓名
		@XStreamAlias("partyVehicle")
		private String partyVehicle;//当事人交通方式
		@XStreamAlias("partyIDType")
		private String partyIDType;//当事人证件类型
		@XStreamAlias("partyID")
		private String partyID;//车辆ID
		@XStreamAlias("partyLicenseTime")
		private String partyLicenseTime;//驾驶证检验有效期至
		@XStreamAlias("licenseStatus")
		private String licenseStatus;//驾驶证状态
		@XStreamAlias("partyCarType")
		private String partyCarType;//驾驶证准驾车型
		@XStreamAlias("partyPhone")
		private String partyPhone;//当事人联系方式
		@XStreamAlias("partyAddress")
		private String partyAddress;//当事人联系住址
		@XStreamAlias("partyInjured")
		private String partyInjured;//当事人是否受伤
		@XStreamAlias("partyDeath")
		private String partyDeath;//当事人是否死亡
		@XStreamAlias("acciDuty")
		private String acciDuty;//事故责任
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getuUID() {
			return uUID;
		}
		public void setuUID(String uUID) {
			this.uUID = uUID;
		}
		public String getPartyName() {
			return partyName;
		}
		public void setPartyName(String partyName) {
			this.partyName = partyName;
		}
		public String getPartyVehicle() {
			return partyVehicle;
		}
		public void setPartyVehicle(String partyVehicle) {
			this.partyVehicle = partyVehicle;
		}
		public String getPartyIDType() {
			return partyIDType;
		}
		public void setPartyIDType(String partyIDType) {
			this.partyIDType = partyIDType;
		}
		public String getPartyID() {
			return partyID;
		}
		public void setPartyID(String partyID) {
			this.partyID = partyID;
		}
		public String getPartyLicenseTime() {
			return partyLicenseTime;
		}
		public void setPartyLicenseTime(String partyLicenseTime) {
			this.partyLicenseTime = partyLicenseTime;
		}
		public String getLicenseStatus() {
			return licenseStatus;
		}
		public void setLicenseStatus(String licenseStatus) {
			this.licenseStatus = licenseStatus;
		}
		public String getPartyCarType() {
			return partyCarType;
		}
		public void setPartyCarType(String partyCarType) {
			this.partyCarType = partyCarType;
		}
		public String getPartyPhone() {
			return partyPhone;
		}
		public void setPartyPhone(String partyPhone) {
			this.partyPhone = partyPhone;
		}
		public String getPartyAddress() {
			return partyAddress;
		}
		public void setPartyAddress(String partyAddress) {
			this.partyAddress = partyAddress;
		}
		public String getPartyInjured() {
			return partyInjured;
		}
		public void setPartyInjured(String partyInjured) {
			this.partyInjured = partyInjured;
		}
		public String getPartyDeath() {
			return partyDeath;
		}
		public void setPartyDeath(String partyDeath) {
			this.partyDeath = partyDeath;
		}
		public String getAcciDuty() {
			return acciDuty;
		}
		public void setAcciDuty(String acciDuty) {
			this.acciDuty = acciDuty;
		}
		
		
}
