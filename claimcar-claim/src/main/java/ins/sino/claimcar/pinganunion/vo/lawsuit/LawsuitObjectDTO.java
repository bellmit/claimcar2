package ins.sino.claimcar.pinganunion.vo.lawsuit;

import java.io.Serializable;
/**
 * 
 * @Description: 涉诉对象信息
 * @author: zhubin
 * @date: 2020年7月31日 下午6:41:46
 */
public class LawsuitObjectDTO implements Serializable{
	private static final long serialVersionUID = -5000461981022046359L;
	private String lawsuitNo;//诉讼号
	private String lossObjectNo;//损失对象号
	private String objectName;//实体名称
	private String taskType;//任务类型
	private String appealAmount;//诉请金额
	private String responseAmount;//应诉/拟调解金额）
	private String appealPersonAmount;//诉请金额（人伤）
	private String lawsuitStage;//诉讼阶段：1-一审、2-二审
	private String firstTrialAmount;//法院定损金额（一审）
	private String secondTrialAmount;//法院定损金额（二审）
	private String paymentAmount;//赔付金额
	private String nextLawsuitFlag;//伤者是否会二次诉讼：Y:是，N:否
	public String getLawsuitNo() {
		return lawsuitNo;
	}
	public void setLawsuitNo(String lawsuitNo) {
		this.lawsuitNo = lawsuitNo;
	}
	public String getLossObjectNo() {
		return lossObjectNo;
	}
	public void setLossObjectNo(String lossObjectNo) {
		this.lossObjectNo = lossObjectNo;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getAppealAmount() {
		return appealAmount;
	}
	public void setAppealAmount(String appealAmount) {
		this.appealAmount = appealAmount;
	}
	public String getResponseAmount() {
		return responseAmount;
	}
	public void setResponseAmount(String responseAmount) {
		this.responseAmount = responseAmount;
	}
	public String getAppealPersonAmount() {
		return appealPersonAmount;
	}
	public void setAppealPersonAmount(String appealPersonAmount) {
		this.appealPersonAmount = appealPersonAmount;
	}
	public String getLawsuitStage() {
		return lawsuitStage;
	}
	public void setLawsuitStage(String lawsuitStage) {
		this.lawsuitStage = lawsuitStage;
	}
	public String getFirstTrialAmount() {
		return firstTrialAmount;
	}
	public void setFirstTrialAmount(String firstTrialAmount) {
		this.firstTrialAmount = firstTrialAmount;
	}
	public String getSecondTrialAmount() {
		return secondTrialAmount;
	}
	public void setSecondTrialAmount(String secondTrialAmount) {
		this.secondTrialAmount = secondTrialAmount;
	}
	public String getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getNextLawsuitFlag() {
		return nextLawsuitFlag;
	}
	public void setNextLawsuitFlag(String nextLawsuitFlag) {
		this.nextLawsuitFlag = nextLawsuitFlag;
	}
	
	
}
