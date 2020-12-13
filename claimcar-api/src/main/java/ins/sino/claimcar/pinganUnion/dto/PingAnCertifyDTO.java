package ins.sino.claimcar.pinganUnion.dto;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @Description: 单证完成查询接口  隶属于respData查询结果
 * @author: zhubin
 * @date: 2020年8月4日 上午8:53:53
 */
public class PingAnCertifyDTO implements Serializable{
	private String reportNo;//报案号：
	private String caseTimes;//赔付次数：
	private String wholeCaseStatus;//整案件状态：0-已结案 1-已报案 2-已理算
	private String indemnityConclusion;//赔付结论：1-赔付 2-零结 3-商业险拒赔 4-整案拒赔 5-注销
	private String indemnityModel;//赔付方式：1-按责赔付 2-互碰自赔 3-各自修车 4-特殊调解
	private String isAgentCase;//是否代位案件：Y-是,N-否
	private String isRegister;//是否立案：Y-是 N-否
	private String registerUm;//立案人：
	private String registerDate;//立案时间：
	private String settlerUm;//理算人：
	private String settleEndDate;//理算结束时间：
	private String caseFinisherUm;//结案人：
	private String endCaseDate;//结案时间：
	private String documentFullDate;//整案单证齐全时间：
	private String caseCancelReason;//案件注销原因：
	private String receiveVoucherUm;//收单人：
	private String settleStartDate;//整案理算开始时间：判断最后一个理算(整案批次的工作流是否进入理算节点，如果空则没有进入，反之则反)
	private String isHugeAccident;//是否大灾：Y-是 N-否
	private String isImportantCase;//是否重大案件：Y-是 N-否
	private String caseType;//案件类型：01-直赔案件 02-快赔案件 03-标准案件 04-复杂案件
	private String firstMindDesc;//大案初核意见说明：
	private String firstAuditingUser;//大案初核人：
	private String firstAuditingDate;//大案初核时间：
	private String agentType;//代位类型：1-线下代位，2线上代位
	private String verifyUm;//核赔人：
	private String verifyDate;//核赔时间：
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getCaseTimes() {
		return caseTimes;
	}
	public void setCaseTimes(String caseTimes) {
		this.caseTimes = caseTimes;
	}
	public String getWholeCaseStatus() {
		return wholeCaseStatus;
	}
	public void setWholeCaseStatus(String wholeCaseStatus) {
		this.wholeCaseStatus = wholeCaseStatus;
	}
	public String getIndemnityConclusion() {
		return indemnityConclusion;
	}
	public void setIndemnityConclusion(String indemnityConclusion) {
		this.indemnityConclusion = indemnityConclusion;
	}
	public String getIndemnityModel() {
		return indemnityModel;
	}
	public void setIndemnityModel(String indemnityModel) {
		this.indemnityModel = indemnityModel;
	}
	public String getIsAgentCase() {
		return isAgentCase;
	}
	public void setIsAgentCase(String isAgentCase) {
		this.isAgentCase = isAgentCase;
	}
	public String getIsRegister() {
		return isRegister;
	}
	public void setIsRegister(String isRegister) {
		this.isRegister = isRegister;
	}
	public String getRegisterUm() {
		return registerUm;
	}
	public void setRegisterUm(String registerUm) {
		this.registerUm = registerUm;
	}
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	public String getSettlerUm() {
		return settlerUm;
	}
	public void setSettlerUm(String settlerUm) {
		this.settlerUm = settlerUm;
	}
	public String getSettleEndDate() {
		return settleEndDate;
	}
	public void setSettleEndDate(String settleEndDate) {
		this.settleEndDate = settleEndDate;
	}
	public String getCaseFinisherUm() {
		return caseFinisherUm;
	}
	public void setCaseFinisherUm(String caseFinisherUm) {
		this.caseFinisherUm = caseFinisherUm;
	}
	public String getEndCaseDate() {
		return endCaseDate;
	}
	public void setEndCaseDate(String endCaseDate) {
		this.endCaseDate = endCaseDate;
	}
	public String getDocumentFullDate() {
		return documentFullDate;
	}
	public void setDocumentFullDate(String documentFullDate) {
		this.documentFullDate = documentFullDate;
	}
	public String getCaseCancelReason() {
		return caseCancelReason;
	}
	public void setCaseCancelReason(String caseCancelReason) {
		this.caseCancelReason = caseCancelReason;
	}
	public String getReceiveVoucherUm() {
		return receiveVoucherUm;
	}
	public void setReceiveVoucherUm(String receiveVoucherUm) {
		this.receiveVoucherUm = receiveVoucherUm;
	}
	public String getSettleStartDate() {
		return settleStartDate;
	}
	public void setSettleStartDate(String settleStartDate) {
		this.settleStartDate = settleStartDate;
	}
	public String getIsHugeAccident() {
		return isHugeAccident;
	}
	public void setIsHugeAccident(String isHugeAccident) {
		this.isHugeAccident = isHugeAccident;
	}
	public String getIsImportantCase() {
		return isImportantCase;
	}
	public void setIsImportantCase(String isImportantCase) {
		this.isImportantCase = isImportantCase;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public String getFirstMindDesc() {
		return firstMindDesc;
	}
	public void setFirstMindDesc(String firstMindDesc) {
		this.firstMindDesc = firstMindDesc;
	}
	public String getFirstAuditingUser() {
		return firstAuditingUser;
	}
	public void setFirstAuditingUser(String firstAuditingUser) {
		this.firstAuditingUser = firstAuditingUser;
	}
	public String getFirstAuditingDate() {
		return firstAuditingDate;
	}
	public void setFirstAuditingDate(String firstAuditingDate) {
		this.firstAuditingDate = firstAuditingDate;
	}
	public String getAgentType() {
		return agentType;
	}
	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
	public String getVerifyUm() {
		return verifyUm;
	}
	public void setVerifyUm(String verifyUm) {
		this.verifyUm = verifyUm;
	}
	public String getVerifyDate() {
		return verifyDate;
	}
	public void setVerifyDate(String verifyDate) {
		this.verifyDate = verifyDate;
	}
	
	
	
	
}
