package ins.sino.claimcar.policyLinkage.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * <pre></pre>
 * @author ★niuqiang
 */
@XStreamAlias("baseinfo")
public class CaseDetailBaseInfoVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("externaltype")
	private String externaltype;
	
	@XStreamAlias("caseNumber")
	private String caseNumber;
	
	@XStreamAlias("accidentTime")
	private String accidentTime;   //出险时间
	
	@XStreamAlias("accidentAddress")
	private String accidentAddress;  // 出险地点
	
	@XStreamAlias("accidentDescribe")
	private String accidentDescribe;  // 出险经过
	
	@XStreamAlias("caseType")
	private String caseType;    //案件类型
	
	//@XmlJavaTypeAdapter(DateConvert.class)
	@XStreamAlias("respTime")
	private String respTime;  //报案时间
	
	@XStreamAlias("respUserName")
	private String respUserName;  //报案人姓名
	
	@XStreamAlias("respUserPhone")
	private String respUserPhone;  //报案人联系方式
	
	@XStreamAlias("id")
	private String id;  // id
	
	@XStreamAlias("startTime")
	private String startTime;  // 出发时间 HH:mm:ss
	
	@XStreamAlias("arriveTime")
	private String arriveTime;  // 到达时间 HH：mm:ss
	
	@XStreamAlias("leaveTime")
	private String leaveTime;  // 撤离时间 HH：mm:ss
	
	@XStreamAlias("lng")
	private String lng; //经度
	
	@XStreamAlias("lat")
	private String lat;  //维度
	
	@XStreamAlias("status")
	private String status;  //案件状态
	
	@XStreamAlias("district")
	private String district;  //  区域
	
	@XStreamAlias("surveyMembers")
	private String surveyMembers; //查勘员
	
	@XStreamAlias("surveyMembersPhone")
	private String surveyMembersPhone; //查勘员手机号

	public String getAccidentTime() {
		return accidentTime;
	}

	public void setAccidentTime(String accidentTime) {
		this.accidentTime = accidentTime;
	}

	public String getAccidentAddress() {
		return accidentAddress;
	}

	public void setAccidentAddress(String accidentAddress) {
		this.accidentAddress = accidentAddress;
	}

	public String getAccidentDescribe() {
		return accidentDescribe;
	}

	public void setAccidentDescribe(String accidentDescribe) {
		this.accidentDescribe = accidentDescribe;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getRespTime() {
		return respTime;
	}

	public void setRespTime(String respTime) {
		this.respTime = respTime;
	}

	public String getRespUserName() {
		return respUserName;
	}

	public void setRespUserName(String respUserName) {
		this.respUserName = respUserName;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getRespUserPhone() {
		return respUserPhone;
	}

	public void setRespUserPhone(String respUserPhone) {
		this.respUserPhone = respUserPhone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}

	public String getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(String leaveTime) {
		this.leaveTime = leaveTime;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getSurveyMembers() {
		return surveyMembers;
	}

	public void setSurveyMembers(String surveyMembers) {
		this.surveyMembers = surveyMembers;
	}

	public String getSurveyMembersPhone() {
		return surveyMembersPhone;
	}

	public void setSurveyMembersPhone(String surveyMembersPhone) {
		this.surveyMembersPhone = surveyMembersPhone;
	}

	public String getExternaltype() {
		return externaltype;
	}

	public void setExternaltype(String externaltype) {
		this.externaltype = externaltype;
	}
	
	 
	
	
	
	

}
