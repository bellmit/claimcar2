package ins.sino.claimcar.pinganunion.vo.investigation;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
/**
 * 
 * @Description: 隶属于transferVOS流传列表
 * @author: zhubin
 * @date: 2020年8月3日 上午11:22:29
 */
public class TransferVo implements Serializable{
	private static final long serialVersionUID = -2945044581808665588L;
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="createdDate", format="yyyy-MM-dd hh:mm:ss")
	private Date createdDate;//创建时间：
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="updatedDate", format="yyyy-MM-dd hh:mm:ss")
	private Date updatedDate;//更新时间：
	private String createdBy;//创建人：
	private String updatedBy;//更新人：
	private String idClmInvestigationTransfr;//流转主键：
	private String idClmInvestigationTask;//调查任务ID：
	private String investigationTache;//任务所属状态：1-PC已发送，2-暂存,3-APP已发送
	private String investigationUser;//处理人：
	private String investigationUserEole;//处理人角色
	private String dispatchUserOperate;//调查分配人操作1-机构内，2-转机构，3-公司外部机构
	private String dispatchUser;//分配调查人
	private String investigationDepartment;//调查机构
	private String investigationOpinion;//调查意见1-赔付  2-扣减  3-整案拒赔  4-商业险拒赔 5-其他
	private String auditOpinion;//审核意见调查审核：1-通过 2-修改 3-转派调查 4-转公司外部机构；提调审核：5-同意 6-不同意
	private String remark;//备注
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="investigationDate", format="yyyy-MM-dd hh:mm:ss")
	private Date investigationDate;//处理时间
	private String decreaseType;//减损类型代码定义3.21
	private String dutyCoefficient;//责任系数
	private String investigatorMemo;//调查员备忘信息
	private String collegiateDutyCoefficient;//合议责任系数
	private String intervenedDutyCoefficient;//介入后的责任系数
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
	public String getIdClmInvestigationTransfr() {
		return idClmInvestigationTransfr;
	}
	public void setIdClmInvestigationTransfr(String idClmInvestigationTransfr) {
		this.idClmInvestigationTransfr = idClmInvestigationTransfr;
	}
	public String getIdClmInvestigationTask() {
		return idClmInvestigationTask;
	}
	public void setIdClmInvestigationTask(String idClmInvestigationTask) {
		this.idClmInvestigationTask = idClmInvestigationTask;
	}
	public String getInvestigationTache() {
		return investigationTache;
	}
	public void setInvestigationTache(String investigationTache) {
		this.investigationTache = investigationTache;
	}
	public String getInvestigationUser() {
		return investigationUser;
	}
	public void setInvestigationUser(String investigationUser) {
		this.investigationUser = investigationUser;
	}
	public String getInvestigationUserEole() {
		return investigationUserEole;
	}
	public void setInvestigationUserEole(String investigationUserEole) {
		this.investigationUserEole = investigationUserEole;
	}
	public String getDispatchUserOperate() {
		return dispatchUserOperate;
	}
	public void setDispatchUserOperate(String dispatchUserOperate) {
		this.dispatchUserOperate = dispatchUserOperate;
	}
	public String getDispatchUser() {
		return dispatchUser;
	}
	public void setDispatchUser(String dispatchUser) {
		this.dispatchUser = dispatchUser;
	}
	public String getInvestigationDepartment() {
		return investigationDepartment;
	}
	public void setInvestigationDepartment(String investigationDepartment) {
		this.investigationDepartment = investigationDepartment;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getInvestigationDate() {
		return investigationDate;
	}
	public void setInvestigationDate(Date investigationDate) {
		this.investigationDate = investigationDate;
	}
	public String getDecreaseType() {
		return decreaseType;
	}
	public void setDecreaseType(String decreaseType) {
		this.decreaseType = decreaseType;
	}
	public String getDutyCoefficient() {
		return dutyCoefficient;
	}
	public void setDutyCoefficient(String dutyCoefficient) {
		this.dutyCoefficient = dutyCoefficient;
	}
	public String getInvestigatorMemo() {
		return investigatorMemo;
	}
	public void setInvestigatorMemo(String investigatorMemo) {
		this.investigatorMemo = investigatorMemo;
	}
	public String getCollegiateDutyCoefficient() {
		return collegiateDutyCoefficient;
	}
	public void setCollegiateDutyCoefficient(String collegiateDutyCoefficient) {
		this.collegiateDutyCoefficient = collegiateDutyCoefficient;
	}
	public String getIntervenedDutyCoefficient() {
		return intervenedDutyCoefficient;
	}
	public void setIntervenedDutyCoefficient(String intervenedDutyCoefficient) {
		this.intervenedDutyCoefficient = intervenedDutyCoefficient;
	}
	
	

}
