package ins.sino.claimcar.other.vo;

import java.io.Serializable;
import java.util.Date;

public class CheckQueryVo implements Serializable{
	private static final long serialVersionUID = 1L;

	private String registNo;// 报案号
	private String policyNo;// 保单号
	private String licenseNo;// 车牌号
	private String vin;// vin码
	private String insuredName;// 被保险人
	private String taskType;// 任务类型
	private String checkCode;// 查勘机构
	private Date lossDateStart;// 核损开始时间
	private Date lossDateEnd;// 核损结束时间
	private Date endCaseTimeStart;
	private Date endCaseTimeEnd;
	private String comCode;
	private String caseType;// 案件类型
	private Date caseTimeEnd;
	private Date caseTimeStart;
	private String handlerCode; //处理人

	
	public String getHandlerCode() {
		return handlerCode;
	}
	
	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}
	// 分页
	private Integer start;// 记录起始位置
	private Integer length;// 记录数
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	public Date getLossDateStart() {
		return lossDateStart;
	}
	public void setLossDateStart(Date lossDateStart) {
		this.lossDateStart = lossDateStart;
	}
	public Date getLossDateEnd() {
		return lossDateEnd;
	}
	public void setLossDateEnd(Date lossDateEnd) {
		this.lossDateEnd = lossDateEnd;
	}
	public Date getEndCaseTimeStart() {
		return endCaseTimeStart;
	}
	public void setEndCaseTimeStart(Date endCaseTimeStart) {
		this.endCaseTimeStart = endCaseTimeStart;
	}
	public Date getEndCaseTimeEnd() {
		return endCaseTimeEnd;
	}
	public void setEndCaseTimeEnd(Date endCaseTimeEnd) {
		this.endCaseTimeEnd = endCaseTimeEnd;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public Date getCaseTimeEnd() {
		return caseTimeEnd;
	}
	public void setCaseTimeEnd(Date caseTimeEnd) {
		this.caseTimeEnd = caseTimeEnd;
	}
	public Date getCaseTimeStart() {
		return caseTimeStart;
	}
	public void setCaseTimeStart(Date caseTimeStart) {
		this.caseTimeStart = caseTimeStart;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	
	

}
