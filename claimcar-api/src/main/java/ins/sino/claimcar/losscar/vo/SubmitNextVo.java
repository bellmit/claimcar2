package ins.sino.claimcar.losscar.vo;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.flow.constant.SubmitType;

import java.util.Map;


public class SubmitNextVo implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String registNo;
	
	// 必填参数
	/** 提交动作类型，默认为普通提交 */
	private SubmitType submitType = SubmitType.N;
	/** 工作流id */
	private String flowId;
	/** 当前的工作流TaskID，报案时为0 */
	private String flowTaskId;
	/** 归属处理机构 */
	private String comCode;
	/** 提交人 */
	private String taskInUser;
	/** 进入的关键业务号 */
	private String taskInKey;

	// 可选参数
	/** 指定处理人 */
	private String assignUser;
	/** 处理人机构 */
	private String assignCom;
	
	// 定损，核损，核损特殊参数
	/** 当前节点 */
	private String currentNode;
	
	private String currentName;
	/** 下一个节点 */
	private String nextNode;
	
	private String nextName;
	/** 提交到第几级 */
	private Integer submitLevel;
	/** 意见 */
	private String auditStatus;
	
	private Map<String,String> nodeMap;
	/** 自动核价标志 **/
	private String autoPriceFlag;
	/** 自动核价标志 **/
	private String autoLossFlag;
	/** 结束标志  **/
	private String endFlag;
	/** 最后一个节点 **/
	private String finalNextNode;
	
	private String verifyLevel;
	
	private String maxLevel;
	/** 当前处理人 */
	private SysUserVo userVo;
	
	private String nodeLevel;
	/** 精友地址 **/
	private String jyUrl;
	
	private Boolean notModPrice;

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public SubmitType getSubmitType() {
		return submitType;
	}

	public void setSubmitType(SubmitType submitType) {
		this.submitType = submitType;
	}

	public Integer getSubmitLevel() {
		return submitLevel;
	}

	public void setSubmitLevel(Integer submitLevel) {
		this.submitLevel = submitLevel;
	}

	public String getTaskInUser() {
		return taskInUser;
	}

	public void setTaskInUser(String taskInUser) {
		this.taskInUser = taskInUser;
	}

	public String getTaskInKey() {
		return taskInKey;
	}

	public void setTaskInKey(String taskInKey) {
		this.taskInKey = taskInKey;
	}

	public String getAssignUser() {
		return assignUser;
	}

	public void setAssignUser(String assignUser) {
		this.assignUser = assignUser;
	}

	public String getAssignCom() {
		return assignCom;
	}

	public void setAssignCom(String assignCom) {
		this.assignCom = assignCom;
	}

	public String getFlowTaskId() {
		return flowTaskId;
	}

	public void setFlowTaskId(String flowTaskId) {
		this.flowTaskId = flowTaskId;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	
	public String getCurrentNode() {
		return currentNode;
	}

	
	public void setCurrentNode(String currentNode) {
		this.currentNode = currentNode;
	}

	
	public String getCurrentName() {
		return currentName;
	}

	
	public void setCurrentName(String currentName) {
		this.currentName = currentName;
	}

	
	public String getNextNode() {
		return nextNode;
	}

	
	public void setNextNode(String nextNode) {
		this.nextNode = nextNode;
	}

	
	public String getNextName() {
		return nextName;
	}

	
	public void setNextName(String nextName) {
		this.nextName = nextName;
	}

	
	public String getRegistNo() {
		return registNo;
	}

	
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	
	public String getAuditStatus() {
		return auditStatus;
	}

	
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	public Map<String,String> getNodeMap() {
		return nodeMap;
	}
	
	public void setNodeMap(Map<String,String> nodeMap) {
		this.nodeMap = nodeMap;
	}

	
	public String getAutoPriceFlag() {
		return autoPriceFlag;
	}
	
	public void setAutoPriceFlag(String autoPriceFlag) {
		this.autoPriceFlag = autoPriceFlag;
	}
	
	public String getAutoLossFlag() {
		return autoLossFlag;
	}
	
	public void setAutoLossFlag(String autoLossFlag) {
		this.autoLossFlag = autoLossFlag;
	}
	
	public String getEndFlag() {
		return endFlag;
	}
	
	public void setEndFlag(String endFlag) {
		this.endFlag = endFlag;
	}

	
	public String getFinalNextNode() {
		return finalNextNode;
	}

	
	public void setFinalNextNode(String finalNextNode) {
		this.finalNextNode = finalNextNode;
	}

	public String getVerifyLevel() {
		return verifyLevel;
	}

	public void setVerifyLevel(String verifyLevel) {
		this.verifyLevel = verifyLevel;
	}

	public String getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(String maxLevel) {
		this.maxLevel = maxLevel;
	}

	public SysUserVo getUserVo() {
		return userVo;
	}

	public void setUserVo(SysUserVo userVo) {
		this.userVo = userVo;
	}

	public String getNodeLevel() {
		return nodeLevel;
	}

	public void setNodeLevel(String nodeLevel) {
		this.nodeLevel = nodeLevel;
	}

	public String getJyUrl() {
		return jyUrl;
	}

	public void setJyUrl(String jyUrl) {
		this.jyUrl = jyUrl;
	}

	public Boolean getNotModPrice() {
		return notModPrice;
	}

	public void setNotModPrice(Boolean notModPrice) {
		this.notModPrice = notModPrice;
	}

	
	
	
}
