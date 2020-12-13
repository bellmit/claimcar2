package ins.sino.claimcar.pinganunion.vo.investigation;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
/**
 * 
 * @Description: 隶属于调查信息查询接口,respData查询结果
 * @author: zhubin
 * @date: 2020年8月3日 上午11:50:13
 */
public class PingAnSurveyDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5879269239422459761L;
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="createdDate", format="yyyy-MM-dd hh:mm:ss")
	private Date createdDate;//创建时间：
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="updatedDate", format="yyyy-MM-dd hh:mm:ss")
	private Date updatedDate;//更新时间：
	private String createdBy;//创建人：
	private String updatedBy;//更新人：
	private String idClmInvestigationTask;//调查任务主键：
	private String reportNo;//报案号：
	private String caseTimes;//赔付次数：
	private String investigationStatus;//调查任务状态：1-待分配，2-待处理，3-处理中，4-待审核，5-需修改，6-已完成
	private String initiateInvestigationUser;//提交人
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="initiateInvestigationDate", format="yyyy-MM-dd hh:mm:ss")
	private Date initiateInvestigationDate;//提交时间
	private String documentGroupId;//附件组ID
	private String isBlacklist;//是否加入黑名单Y-是 N-否
	private String refuseReasonCode;//拒赔原因代码定义3.23
	private String dispatchUserOperate;//调查人分配1-机构内，2-转机构，3-公司外部机构
	private String investigationOpinion;//调查意见1-赔付 2-扣减 3-整案拒赔 4-商业险拒赔 5-转公司外部调查 6-其他
	private String auditOpinion;//审核意见1-通过 2-修改 3-转派调查 4-转公司外部机构
	private String investigationNode;//提调环节
	private String caseZeroApply;//申请零结Y-是 N-否
	private String investigationFrom;//提调来源环节代码定义3.22
	private String investigationRuleType;//提调类型标识
	private String deductionType;//扣减类型1-扣减比例，2-扣减金额
	private String investigationType;//提调类型标识(车物0 人伤调查1 人伤专业2)
	private String isRiskCase;//是否风险案件1是，0否
	private String isBigCase;//是否大案Y-是 N-否
	private String isAccusationCase;//是否举报人案件Y-是 N-否
	private List<TransferVo> transferVOS;//流传列表
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getIdClmInvestigationTask() {
		return idClmInvestigationTask;
	}
	public void setIdClmInvestigationTask(String idClmInvestigationTask) {
		this.idClmInvestigationTask = idClmInvestigationTask;
	}
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
	public String getInvestigationStatus() {
		return investigationStatus;
	}
	public void setInvestigationStatus(String investigationStatus) {
		this.investigationStatus = investigationStatus;
	}
	public String getInitiateInvestigationUser() {
		return initiateInvestigationUser;
	}
	public void setInitiateInvestigationUser(String initiateInvestigationUser) {
		this.initiateInvestigationUser = initiateInvestigationUser;
	}
	public Date getInitiateInvestigationDate() {
		return initiateInvestigationDate;
	}
	public void setInitiateInvestigationDate(Date initiateInvestigationDate) {
		this.initiateInvestigationDate = initiateInvestigationDate;
	}
	public String getDocumentGroupId() {
		return documentGroupId;
	}
	public void setDocumentGroupId(String documentGroupId) {
		this.documentGroupId = documentGroupId;
	}
	public String getIsBlacklist() {
		return isBlacklist;
	}
	public void setIsBlacklist(String isBlacklist) {
		this.isBlacklist = isBlacklist;
	}
	public String getRefuseReasonCode() {
		return refuseReasonCode;
	}
	public void setRefuseReasonCode(String refuseReasonCode) {
		this.refuseReasonCode = refuseReasonCode;
	}
	public String getDispatchUserOperate() {
		return dispatchUserOperate;
	}
	public void setDispatchUserOperate(String dispatchUserOperate) {
		this.dispatchUserOperate = dispatchUserOperate;
	}
	public String getInvestigationOpinion() {
		return investigationOpinion;
	}
	public void setInvestigationOpinion(String investigationOpinion) {
		this.investigationOpinion = investigationOpinion;
	}
	public String getAuditOpinion() {
		return auditOpinion;
	}
	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}
	public String getInvestigationNode() {
		return investigationNode;
	}
	public void setInvestigationNode(String investigationNode) {
		this.investigationNode = investigationNode;
	}
	public String getCaseZeroApply() {
		return caseZeroApply;
	}
	public void setCaseZeroApply(String caseZeroApply) {
		this.caseZeroApply = caseZeroApply;
	}
	public String getInvestigationFrom() {
		return investigationFrom;
	}
	public void setInvestigationFrom(String investigationFrom) {
		this.investigationFrom = investigationFrom;
	}
	public String getInvestigationRuleType() {
		return investigationRuleType;
	}
	public void setInvestigationRuleType(String investigationRuleType) {
		this.investigationRuleType = investigationRuleType;
	}
	public String getDeductionType() {
		return deductionType;
	}
	public void setDeductionType(String deductionType) {
		this.deductionType = deductionType;
	}
	public String getInvestigationType() {
		return investigationType;
	}
	public void setInvestigationType(String investigationType) {
		this.investigationType = investigationType;
	}
	public String getIsRiskCase() {
		return isRiskCase;
	}
	public void setIsRiskCase(String isRiskCase) {
		this.isRiskCase = isRiskCase;
	}
	public String getIsBigCase() {
		return isBigCase;
	}
	public void setIsBigCase(String isBigCase) {
		this.isBigCase = isBigCase;
	}
	public String getIsAccusationCase() {
		return isAccusationCase;
	}
	public void setIsAccusationCase(String isAccusationCase) {
		this.isAccusationCase = isAccusationCase;
	}
	public List<TransferVo> getTransferVOS() {
		return transferVOS;
	}
	public void setTransferVOS(List<TransferVo> transferVOS) {
		this.transferVOS = transferVOS;
	}
	
	
}
