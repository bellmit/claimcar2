package ins.sino.claimcar.interf.vo;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Check")
public class CheckVo {
	/*查勘地址*/
	@XStreamAlias("CheckAddress")
	private String checkAddress;
	/*查勘时间*/
	@XStreamAlias("CheckDate")
	private String checkDate;
	/*查勘员1代码*/
	@XStreamAlias("CheckEmployee1Code")
	private String checkEmployee1Code;
	/*查勘员1*/
	@XStreamAlias("CheckEmployee1Name")
	private String checkEmployee1Name;
	/*查勘员2代码*/
	@XStreamAlias("CheckEmployee2Code")
	private String checkEmployee2Code;
	/*查勘员2*/
	@XStreamAlias("CheckEmployee2Name")
	private String checkEmployee2Name;
	/*查勘报告*/
	@XStreamAlias("CheckOpinion")
	private String checkopinion;
	/*查勘类型*/
	@XStreamAlias("CheckType")
	private String checkType;
	/*查勘阶段-赔案类型*/
	@XStreamAlias("ClaimType")
	private String claimType;
	/*联系人*/
	@XStreamAlias("ContactPerson")
	private String contactPerson;
	/*联系人电话*/
	@XStreamAlias("ContactTel")
	private String contactTel;
	/*事故处理方式*/
	@XStreamAlias("DisposeType")
	private String disposeType;
	/*查勘阶段-驾驶员*/
	@XStreamAlias("Driver")
	private DriverVo driverVo;
	/*查勘阶段-道路类型*/
	@XStreamAlias("EventAddressType")
	private String eventAddressType;
	/*查勘阶段-出险原因*/
	@XStreamAlias("EventReason")
	private String eventReason;
	/*查勘阶段-事故原因*/
	@XStreamAlias("EventReasonType")
	private String eventReasonType;
	/*查勘阶段-责任类型*/
	@XStreamAlias("EventResponsibility")
	private String eventResponsibility;
	/*查勘阶段-事故类型*/
	@XStreamAlias("EventType")
	private String eventType;
	/*事故类别*/
	@XStreamAlias("EventCategory")
	private String eventCategory;
	/*是否第一现场查勘*/
	@XStreamAlias("IsFirstScene")
	private String isFirstScene;
	/*损失类别*/
	@XStreamAlias("LossType")
	private String lossType;
	public String getCheckAddress() {
		return checkAddress;
	}
	public void setCheckAddress(String checkAddress) {
		this.checkAddress = checkAddress;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public String getCheckEmployee1Code() {
		return checkEmployee1Code;
	}
	public void setCheckEmployee1Code(String checkEmployee1Code) {
		this.checkEmployee1Code = checkEmployee1Code;
	}
	public String getCheckEmployee1Name() {
		return checkEmployee1Name;
	}
	public void setCheckEmployee1Name(String checkEmployee1Name) {
		this.checkEmployee1Name = checkEmployee1Name;
	}
	public String getCheckEmployee2Code() {
		return checkEmployee2Code;
	}
	public void setCheckEmployee2Code(String checkEmployee2Code) {
		this.checkEmployee2Code = checkEmployee2Code;
	}
	public String getCheckEmployee2Name() {
		return checkEmployee2Name;
	}
	public void setCheckEmployee2Name(String checkEmployee2Name) {
		this.checkEmployee2Name = checkEmployee2Name;
	}
	public String getCheckopinion() {
		return checkopinion;
	}
	public void setCheckopinion(String checkopinion) {
		this.checkopinion = checkopinion;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	public String getDisposeType() {
		return disposeType;
	}
	public void setDisposeType(String disposeType) {
		this.disposeType = disposeType;
	}
	public DriverVo getDriverVo() {
		return driverVo;
	}
	public void setDriverVo(DriverVo driverVo) {
		this.driverVo = driverVo;
	}
	public String getEventAddressType() {
		return eventAddressType;
	}
	public void setEventAddressType(String eventAddressType) {
		this.eventAddressType = eventAddressType;
	}
	public String getEventReason() {
		return eventReason;
	}
	public void setEventReason(String eventReason) {
		this.eventReason = eventReason;
	}
	public String getEventReasonType() {
		return eventReasonType;
	}
	public void setEventReasonType(String eventReasonType) {
		this.eventReasonType = eventReasonType;
	}
	public String getEventResponsibility() {
		return eventResponsibility;
	}
	public void setEventResponsibility(String eventResponsibility) {
		this.eventResponsibility = eventResponsibility;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventCategory() {
		return eventCategory;
	}
	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}
	public String getIsFirstScene() {
		return isFirstScene;
	}
	public void setIsFirstScene(String isFirstScene) {
		this.isFirstScene = isFirstScene;
	}
	public String getLossType() {
		return lossType;
	}
	public void setLossType(String lossType) {
		this.lossType = lossType;
	}
	
	

}
