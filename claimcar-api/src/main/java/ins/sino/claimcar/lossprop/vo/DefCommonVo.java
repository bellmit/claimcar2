package ins.sino.claimcar.lossprop.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DefCommonVo implements Serializable{
	private static final long serialVersionUID = 1L; 
	
	private String acceptFlag;	//接收状态
	private BigDecimal amount;	//车上货物损失险 保额
	private Date damageDate;	//出险时间
	private Double flowTaskId;	//工作流任务id
	private String handleStatus;	//处理状态
	private String flowId;	//工作流主id
	private String currentNode;	//当前节点
	private String nextNode;	//下一个节点
	private String intermStanders;	//公估资费标准
	public String getIntermStanders() {
		return intermStanders;
	}
	public void setIntermStanders(String intermStanders) {
		this.intermStanders = intermStanders;
	}
	public String getHandleStatus() {
		return handleStatus;
	}
	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
	}
	public String getCurrentNode() {
		return currentNode;
	}
	public void setCurrentNode(String currentNode) {
		this.currentNode = currentNode;
	}
	public String getNextNode() {
		return nextNode;
	}
	public void setNextNode(String nextNode) {
		this.nextNode = nextNode;
	}

	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public Double getFlowTaskId() {
		return flowTaskId;
	}
	public void setFlowTaskId(Double flowTaskId) {
		this.flowTaskId = flowTaskId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getAcceptFlag() {
		return acceptFlag;
	}
	public void setAcceptFlag(String acceptFlag) {
		this.acceptFlag = acceptFlag;
	}
	public Date getDamageDate() {
		return damageDate;
	}
	public void setDamageDate(Date damageDate) {
		this.damageDate = damageDate;
	}
	

}
