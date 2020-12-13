package ins.sino.claimcar.lossprop.vo;

import java.math.BigDecimal;

/**
 * Custom VO class of PO PrpLdlossPropMain
 */ 
public class PrpLdlossPropMainVo extends PrpLdlossPropMainVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private Double flowTaskId;//工作流任务id
	private String handlerStatus;//处理状态
	private String haveKindD ="0";	//是否承保了车上货物损失险  1 承保了 0 未承保
	private BigDecimal assessorFee;  //公估费用
    private BigDecimal invoiceFee;  //发票金额
	public String getHandlerStatus() {
		return handlerStatus;
	}
	public void setHandlerStatus(String handlerStatus) {
		this.handlerStatus = handlerStatus;
	}
	public Double getFlowTaskId() {
		return flowTaskId;
	}
	public void setFlowTaskId(Double flowTaskId) {
		this.flowTaskId = flowTaskId;
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
	public String getHaveKindD() {
		return haveKindD;
	}
	public void setHaveKindD(String haveKindD) {
		this.haveKindD = haveKindD;
	}
	private String currentNode;//当前节点
	private String nextNode;//下一个节点
	public BigDecimal getAssessorFee() {
		return assessorFee;
	}
	public void setAssessorFee(BigDecimal assessorFee) {
		this.assessorFee = assessorFee;
	}
	public BigDecimal getInvoiceFee() {
		return invoiceFee;
	}
	public void setInvoiceFee(BigDecimal invoiceFee) {
		this.invoiceFee = invoiceFee;
	}
	
}
