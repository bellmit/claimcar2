package ins.sino.claimcar.interf.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Report")
public class ReportVo {
	/*联系人*/
	@XStreamAlias("ContactPerson")
	private String contactPerson;
	/*联系人电话*/
	@XStreamAlias("ContactTel")
	private String contactTel;
	/*报案阶段-驾驶员信息*/
	@XStreamAlias("Driver")
	private DriverVo driverVo;
	/*出险地址*/
	@XStreamAlias("EventAddress")
	private String eventAddress;
	/*报案阶段-道路类型*/
	@XStreamAlias("EventAddressType")
	private String eventAddressType;
	/*出险时间*/
	@XStreamAlias("EventDate")
	private String eventDate;
	/*出险经过*/
	@XStreamAlias("EventDescription")
	private String eventDescription;
	/*报案阶段-出险原因*/
	@XStreamAlias("EventReason")
	private String eventReason;
	/*报案阶段-事故原因*/
	@XStreamAlias("EventReasonType")
	private String eventReasonType;
	/*报案阶段-责任类型*/
	@XStreamAlias("EventResponsibility")
	private String EventResponsibility;
	/*报案阶段-事故类型*/
	@XStreamAlias("EventType")
	private String eventType;
	/*报案人*/
	@XStreamAlias("Informant")
	private String informant;
	/*报案人是否驾驶员*/
	@XStreamAlias("InformantIsDriver")
	private String informantIsDriver;
	/*报案人是否被保险人*/
	@XStreamAlias("InformantIsInsured")
	private String informantIsInsured;
	/*报案人电话*/
	@XStreamAlias("InformantTel")
	private String informantTel;
	/*是否结案*/
	@XStreamAlias("IsClosed")
	private String isClosed;
	/*是否互碰自赔*/
	@XStreamAlias("IsMutualCollisionSelfCompensation")
	private String isMutualCollisionSelfCompensation;
	/*是否现场报案*/
	@XStreamAlias("IsScene")
	private String isScene;
	/*简易案件标识*/
	@XStreamAlias("IsSimpleClaim")
	private String isSimpleClaim;
	/*案件紧急程度*/
	@XStreamAlias("MercyFlag")
	private String mercyFlag;
	/*报案时间*/
	@XStreamAlias("ReportDate")
	private String reportDate;
	/*自定义部分-区分当前发送节点*/
	@XStreamAlias("CustomData")
	private List<DataVo> datas;
	
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
	public DriverVo getDriverVo() {
		return driverVo;
	}
	public void setDriverVo(DriverVo driverVo) {
		this.driverVo = driverVo;
	}
	public String getEventAddress() {
		return eventAddress;
	}
	public void setEventAddress(String eventAddress) {
		this.eventAddress = eventAddress;
	}
	public String getEventAddressType() {
		return eventAddressType;
	}
	public void setEventAddressType(String eventAddressType) {
		this.eventAddressType = eventAddressType;
	}
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	public String getEventDescription() {
		return eventDescription;
	}
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
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
		return EventResponsibility;
	}
	public void setEventResponsibility(String eventResponsibility) {
		EventResponsibility = eventResponsibility;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getInformant() {
		return informant;
	}
	public void setInformant(String informant) {
		this.informant = informant;
	}
	public String getInformantIsDriver() {
		return informantIsDriver;
	}
	public void setInformantIsDriver(String informantIsDriver) {
		this.informantIsDriver = informantIsDriver;
	}
	public String getInformantIsInsured() {
		return informantIsInsured;
	}
	public void setInformantIsInsured(String informantIsInsured) {
		this.informantIsInsured = informantIsInsured;
	}
	public String getInformantTel() {
		return informantTel;
	}
	public void setInformantTel(String informantTel) {
		this.informantTel = informantTel;
	}
	public String getIsClosed() {
		return isClosed;
	}
	public void setIsClosed(String isClosed) {
		this.isClosed = isClosed;
	}
	public String getIsMutualCollisionSelfCompensation() {
		return isMutualCollisionSelfCompensation;
	}
	public void setIsMutualCollisionSelfCompensation(
			String isMutualCollisionSelfCompensation) {
		this.isMutualCollisionSelfCompensation = isMutualCollisionSelfCompensation;
	}
	public String getIsScene() {
		return isScene;
	}
	public void setIsScene(String isScene) {
		this.isScene = isScene;
	}
	public String getIsSimpleClaim() {
		return isSimpleClaim;
	}
	public void setIsSimpleClaim(String isSimpleClaim) {
		this.isSimpleClaim = isSimpleClaim;
	}
	public String getMercyFlag() {
		return mercyFlag;
	}
	public void setMercyFlag(String mercyFlag) {
		this.mercyFlag = mercyFlag;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public List<DataVo> getDatas() {
		return datas;
	}
	public void setDatas(List<DataVo> datas) {
		this.datas = datas;
	}
	
	
	
}
