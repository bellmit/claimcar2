/******************************************************************************
* CREATETIME : 2016年7月25日 上午10:00:38
******************************************************************************/
package ins.sino.claimcar.flow.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 对应旧理赔的 SwfLogDtoBase 对象，用于查询旧理赔流程数据
 * @author ★LiuPing
 * @CreateTime 2016年7月25日
 */
public class SwfFlowLogDto implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 属性流程编号 */
	private String flowID = "";
	/** 属性序号 */
	private int logNo = 0;
	/** 属性模板号 */
	private int modelNo = 0;
	/** 属性当前节点号 */
	private int nodeNo = 0;
	/** 属性当前节点名称 */
	private String nodeName = "";
	/** 属性业务号 */
	private String businessNo = "";
	/** 属性处理部门 */
	private String handleDept = "";
	/** 属性处理人员代码 */
	private String handlerCode = "";
	/** 属性处理人员名称 */
	private String handlerName = "";
	/** 属性流入时间 */
	private String flowInTime = "";
	/** 属性处理时限 */
	private int timeLimit = 0;
	/** 属性处理时间 */
	private String handleTime = "";
	/** 属性提交时间 */
	private String submitTime = "";
	/** 属性节点状态 0/1/2/3都是修改工作流 ,4提交工作流 ,5回退 ，3表示退回的 */
	private String nodeStatus = "";
	/** 属性流状态 */
	private String flowStatus = "";
	/** 属性明细信息包ID */
	private String packageID = "";
	/** 属性备用标志 */
	private String flag = "";
	/** 属性任务编号 */
	private int taskNo = 0;
	/** 属性任务类型 */
	private String taskType = "";
	/** 属性节点类型 */
	private String nodeType = "";
	/** 属性任务备注 */
	private String titleStr = "";
	/** 属性业务类型 */
	private String businessType = "";
	/** 属性险种代码 */
	private String riskCode = "";
	/** 属性任务接收载体键值 */
	private String keyIn = "";
	/** 属性记录流出时的业务编码 */
	private String keyOut = "";
	/** 属性部门名称 */
	private String deptName = "";
	/** 属性主流程编号 */
	private String mainFlowID = "";
	/** 属性子流程编号 */
	private String subFlowID = "";
	/** 属性节点X坐标 */
	private int posX = 0;
	/** 属性节点Y坐标 */
	private int posY = 0;
	/** 属性结束标志 */
	private String endFlag = "";
	/** 属性上个处理人员代码 */
	private String beforeHandlerCode = "";
	/** 属性上个处理人员名称 */
	private String beforeHandlerName = "";
	/** 属性保单号码 */
	private String policyNo = "";
	/** 属性类型标志 */
	private String typeFlag = "";
	/** 属性归属机构 */
	private String comCode = "";
	/** 属性调度号码 */
	private int scheduleID = 0;
	/** 属性标的序号 */
	private String lossItemCode = "";
	/** 属性车牌号码 */
	private String lossItemName = "";
	/** 属性是否为本保单车辆 */
	private String insureCarFlag = "";
	/** 属性可操作/处理的级别 */
	private String handlerRange = "";
	/** 属性紧急程度 */
	private String exigenceGree = "";
	/** 属性报案号码 */
	private String registNo = "";
	/** 属性被保险人名称 */
	private String insuredName = "";
	/** 属性简易赔案标记 */
	private String claimTypeFlag = "";
	/** 出险地点 */
	private String damageAddress = "";
	/** 属性报案时间 */
	private Date reportTime = new Date();
	/** 属性报案时间 */
	private Date damageTime = new Date();

	/** 报案关联的保单 */
	private String othPolicyno = "";

	/** 通赔处理标志 */
	private String devolveFlag = "0";

	/** 超时处理标识 */
	private String timeShowFlag = "";

	// 新业务板块:0-市场业务，1-电力业务，2-直销业务
	private String businessPlate = "";

	public String getBusinessPlate() {
		return businessPlate;
	}

	public void setBusinessPlate(String businessPlate) {
		this.businessPlate = businessPlate;
	}

	public String getTimeShowFlag() {
		return timeShowFlag;
	}

	public void setTimeShowFlag(String timeShowFlag) {
		this.timeShowFlag = timeShowFlag;
	}

	public String getDevolveFlag() {
		if(devolveFlag==null||"".equals(devolveFlag)) devolveFlag = "0";
		return devolveFlag;
	}

	public void setDevolveFlag(String devolveFlag) {
		this.devolveFlag = devolveFlag;
	}

	/**
	 * 设置属性流程编号
	 * @param flowID 待设置的属性流程编号的值
	 */
	public void setFlowID(String flowID) {
		this.flowID = ( flowID );
	}

	/**
	 * 获取属性流程编号
	 * @return 属性流程编号的值
	 */
	public String getFlowID() {
		return flowID;
	}

	/**
	 * 设置属性序号
	 * @param logNo 待设置的属性序号的值
	 */
	public void setLogNo(int logNo) {
		this.logNo = logNo;
	}

	/**
	 * 获取属性序号
	 * @return 属性序号的值
	 */
	public int getLogNo() {
		return logNo;
	}

	/**
	 * 设置属性模板号
	 * @param modelNo 待设置的属性模板号的值
	 */
	public void setModelNo(int modelNo) {
		this.modelNo = modelNo;
	}

	/**
	 * 获取属性模板号
	 * @return 属性模板号的值
	 */
	public int getModelNo() {
		return modelNo;
	}

	/**
	 * 设置属性当前节点号
	 * @param nodeNo 待设置的属性当前节点号的值
	 */
	public void setNodeNo(int nodeNo) {
		this.nodeNo = nodeNo;
	}

	/**
	 * 获取属性当前节点号
	 * @return 属性当前节点号的值
	 */
	public int getNodeNo() {
		return nodeNo;
	}

	/**
	 * 设置属性当前节点名称
	 * @param nodeName 待设置的属性当前节点名称的值
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = ( nodeName );
	}

	/**
	 * 获取属性当前节点名称
	 * @return 属性当前节点名称的值
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * 设置属性业务号
	 * @param businessNo 待设置的属性业务号的值
	 */
	public void setBusinessNo(String businessNo) {
		this.businessNo = ( businessNo );
	}

	/**
	 * 获取属性业务号
	 * @return 属性业务号的值
	 */
	public String getBusinessNo() {
		return businessNo;
	}

	/**
	 * 设置属性处理部门
	 * @param handleDept 待设置的属性处理部门的值
	 */
	public void setHandleDept(String handleDept) {
		this.handleDept = ( handleDept );
	}

	/**
	 * 获取属性处理部门
	 * @return 属性处理部门的值
	 */
	public String getHandleDept() {
		return handleDept;
	}

	/**
	 * 设置属性处理人员代码
	 * @param handlerCode 待设置的属性处理人员代码的值
	 */
	public void setHandlerCode(String handlerCode) {
		this.handlerCode = ( handlerCode );
	}

	/**
	 * 获取属性处理人员代码
	 * @return 属性处理人员代码的值
	 */
	public String getHandlerCode() {
		return handlerCode;
	}

	/**
	 * 设置属性处理人员名称
	 * @param handlerName 待设置的属性处理人员名称的值
	 */
	public void setHandlerName(String handlerName) {
		this.handlerName = ( handlerName );
	}

	/**
	 * 获取属性处理人员名称
	 * @return 属性处理人员名称的值
	 */
	public String getHandlerName() {
		return handlerName;
	}

	/**
	 * 设置属性流入时间
	 * @param flowInTime 待设置的属性流入时间的值
	 */
	public void setFlowInTime(String flowInTime) {
		this.flowInTime = ( flowInTime );
	}

	/**
	 * 获取属性流入时间
	 * @return 属性流入时间的值
	 */
	public String getFlowInTime() {
		return flowInTime;
	}

	/**
	 * 设置属性处理时限
	 * @param timeLimit 待设置的属性处理时限的值
	 */
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	/**
	 * 获取属性处理时限
	 * @return 属性处理时限的值
	 */
	public int getTimeLimit() {
		return timeLimit;
	}

	/**
	 * 设置属性处理时间
	 * @param handleTime 待设置的属性处理时间的值
	 */
	public void setHandleTime(String handleTime) {
		this.handleTime = ( handleTime );
	}

	/**
	 * 获取属性处理时间
	 * @return 属性处理时间的值
	 */
	public String getHandleTime() {
		return handleTime;
	}

	/**
	 * 设置属性提交时间
	 * @param submitTime 待设置的属性提交时间的值
	 */
	public void setSubmitTime(String submitTime) {
		this.submitTime = ( submitTime );
	}

	/**
	 * 获取属性提交时间
	 * @return 属性提交时间的值
	 */
	public String getSubmitTime() {
		return submitTime;
	}

	/**
	 * 设置属性节点状态
	 * @param nodeStatus 待设置的属性节点状态的值
	 */
	public void setNodeStatus(String nodeStatus) {
		this.nodeStatus = ( nodeStatus );
	}

	/**
	 * 获取属性节点状态
	 * @return 属性节点状态的值
	 */
	public String getNodeStatus() {
		return nodeStatus;
	}

	/**
	 * 设置属性流状态
	 * @param flowStatus 待设置的属性流状态的值
	 */
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = ( flowStatus );
	}

	/**
	 * 获取属性流状态
	 * @return 属性流状态的值
	 */
	public String getFlowStatus() {
		return flowStatus;
	}

	/**
	 * 设置属性明细信息包ID
	 * @param packageID 待设置的属性明细信息包ID的值
	 */
	public void setPackageID(String packageID) {
		this.packageID = ( packageID );
	}

	/**
	 * 获取属性明细信息包ID
	 * @return 属性明细信息包ID的值
	 */
	public String getPackageID() {
		return packageID;
	}

	/**
	 * 设置属性备用标志
	 * @param flag 待设置的属性备用标志的值
	 */
	public void setFlag(String flag) {
		this.flag = ( flag );
	}

	/**
	 * 获取属性备用标志
	 * @return 属性备用标志的值
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * 设置属性任务编号
	 * @param taskNo 待设置的属性任务编号的值
	 */
	public void setTaskNo(int taskNo) {
		this.taskNo = taskNo;
	}

	/**
	 * 获取属性任务编号
	 * @return 属性任务编号的值
	 */
	public int getTaskNo() {
		return taskNo;
	}

	/**
	 * 设置属性任务类型
	 * @param taskType 待设置的属性任务类型的值
	 */
	public void setTaskType(String taskType) {
		this.taskType = ( taskType );
	}

	/**
	 * 获取属性任务类型
	 * @return 属性任务类型的值
	 */
	public String getTaskType() {
		return taskType;
	}

	/**
	 * 设置属性节点类型
	 * @param nodeType 待设置的属性节点类型的值
	 */
	public void setNodeType(String nodeType) {
		this.nodeType = ( nodeType );
	}

	/**
	 * 获取属性节点类型
	 * @return 属性节点类型的值
	 */
	public String getNodeType() {
		return nodeType;
	}

	/**
	 * 设置属性任务备注
	 * @param titleStr 待设置的属性任务备注的值
	 */
	public void setTitleStr(String titleStr) {
		this.titleStr = ( titleStr );
	}

	/**
	 * 获取属性任务备注
	 * @return 属性任务备注的值
	 */
	public String getTitleStr() {
		return titleStr;
	}

	/**
	 * 设置属性业务类型
	 * @param businessType 待设置的属性业务类型的值
	 */
	public void setBusinessType(String businessType) {
		this.businessType = ( businessType );
	}

	/**
	 * 获取属性业务类型
	 * @return 属性业务类型的值
	 */
	public String getBusinessType() {
		return businessType;
	}

	/**
	 * 设置属性险种代码
	 * @param riskCode 待设置的属性险种代码的值
	 */
	public void setRiskCode(String riskCode) {
		this.riskCode = ( riskCode );
	}

	/**
	 * 获取属性险种代码
	 * @return 属性险种代码的值
	 */
	public String getRiskCode() {
		return riskCode;
	}

	/**
	 * 设置属性任务接收载体键值
	 * @param keyIn 待设置的属性任务接收载体键值的值
	 */
	public void setKeyIn(String keyIn) {
		this.keyIn = ( keyIn );
	}

	/**
	 * 获取属性任务接收载体键值
	 * @return 属性任务接收载体键值的值
	 */
	public String getKeyIn() {
		return keyIn;
	}

	/**
	 * 设置属性记录流出时的业务编码
	 * @param keyOut 待设置的属性记录流出时的业务编码的值
	 */
	public void setKeyOut(String keyOut) {
		this.keyOut = ( keyOut );
	}

	/**
	 * 获取属性记录流出时的业务编码
	 * @return 属性记录流出时的业务编码的值
	 */
	public String getKeyOut() {
		return keyOut;
	}

	/**
	 * 设置属性部门名称
	 * @param deptName 待设置的属性部门名称的值
	 */
	public void setDeptName(String deptName) {
		this.deptName = ( deptName );
	}

	/**
	 * 获取属性部门名称
	 * @return 属性部门名称的值
	 */
	public String getDeptName() {
		return deptName;
	}

	/**
	 * 设置属性主流程编号
	 * @param mainFlowID 待设置的属性主流程编号的值
	 */
	public void setMainFlowID(String mainFlowID) {
		this.mainFlowID = ( mainFlowID );
	}

	/**
	 * 获取属性主流程编号
	 * @return 属性主流程编号的值
	 */
	public String getMainFlowID() {
		return mainFlowID;
	}

	/**
	 * 设置属性子流程编号
	 * @param subFlowID 待设置的属性子流程编号的值
	 */
	public void setSubFlowID(String subFlowID) {
		this.subFlowID = ( subFlowID );
	}

	/**
	 * 获取属性子流程编号
	 * @return 属性子流程编号的值
	 */
	public String getSubFlowID() {
		return subFlowID;
	}

	/**
	 * 设置属性节点X坐标
	 * @param posX 待设置的属性节点X坐标的值
	 */
	public void setPosX(int posX) {
		this.posX = posX;
	}

	/**
	 * 获取属性节点X坐标
	 * @return 属性节点X坐标的值
	 */
	public int getPosX() {
		return posX;
	}

	/**
	 * 设置属性节点Y坐标
	 * @param posY 待设置的属性节点Y坐标的值
	 */
	public void setPosY(int posY) {
		this.posY = posY;
	}

	/**
	 * 获取属性节点Y坐标
	 * @return 属性节点Y坐标的值
	 */
	public int getPosY() {
		return posY;
	}

	/**
	 * 设置属性结束标志
	 * @param endFlag 待设置的属性结束标志的值
	 */
	public void setEndFlag(String endFlag) {
		this.endFlag = ( endFlag );
	}

	/**
	 * 获取属性结束标志
	 * @return 属性结束标志的值
	 */
	public String getEndFlag() {
		return endFlag;
	}

	/**
	 * 设置属性上个处理人员代码
	 * @param beforeHandlerCode 待设置的属性上个处理人员代码的值
	 */
	public void setBeforeHandlerCode(String beforeHandlerCode) {
		this.beforeHandlerCode = ( beforeHandlerCode );
	}

	/**
	 * 获取属性上个处理人员代码
	 * @return 属性上个处理人员代码的值
	 */
	public String getBeforeHandlerCode() {
		return beforeHandlerCode;
	}

	/**
	 * 设置属性上个处理人员名称
	 * @param beforeHandlerName 待设置的属性上个处理人员名称的值
	 */
	public void setBeforeHandlerName(String beforeHandlerName) {
		this.beforeHandlerName = ( beforeHandlerName );
	}

	/**
	 * 获取属性上个处理人员名称
	 * @return 属性上个处理人员名称的值
	 */
	public String getBeforeHandlerName() {
		return beforeHandlerName;
	}

	/**
	 * 设置属性保单号码
	 * @param policyNo 待设置的属性保单号码的值
	 */
	public void setPolicyNo(String policyNo) {
		this.policyNo = ( policyNo );
	}

	/**
	 * 获取属性保单号码
	 * @return 属性保单号码的值
	 */
	public String getPolicyNo() {
		return policyNo;
	}

	/**
	 * 设置属性类型标志
	 * @param typeFlag 待设置的属性类型标志的值
	 */
	public void setTypeFlag(String typeFlag) {
		this.typeFlag = ( typeFlag );
	}

	/**
	 * 获取属性类型标志
	 * @return 属性类型标志的值
	 */
	public String getTypeFlag() {
		return typeFlag;
	}

	/**
	 * 设置属性归属机构
	 * @param comCode 待设置的属性归属机构的值
	 */
	public void setComCode(String comCode) {
		this.comCode = ( comCode );
	}

	/**
	 * 获取属性归属机构
	 * @return 属性归属机构的值
	 */
	public String getComCode() {
		return comCode;
	}

	/**
	 * 设置属性调度号码
	 * @param scheduleID 待设置的属性调度号码的值
	 */
	public void setScheduleID(int scheduleID) {
		this.scheduleID = scheduleID;
	}

	/**
	 * 获取属性调度号码
	 * @return 属性调度号码的值
	 */
	public int getScheduleID() {
		return scheduleID;
	}

	/**
	 * 设置属性标的序号
	 * @param lossItemCode 待设置的属性标的序号的值
	 */
	public void setLossItemCode(String lossItemCode) {
		this.lossItemCode = ( lossItemCode );
	}

	/**
	 * 获取属性标的序号
	 * @return 属性标的序号的值
	 */
	public String getLossItemCode() {
		return lossItemCode;
	}

	/**
	 * 设置属性车牌号码
	 * @param lossItemName 待设置的属性车牌号码的值
	 */
	public void setLossItemName(String lossItemName) {
		this.lossItemName = ( lossItemName );
	}

	/**
	 * 获取属性车牌号码
	 * @return 属性车牌号码的值
	 */
	public String getLossItemName() {
		return lossItemName;
	}

	/**
	 * 设置属性是否为本保单车辆
	 * @param insureCarFlag 待设置的属性是否为本保单车辆的值
	 */
	public void setInsureCarFlag(String insureCarFlag) {
		this.insureCarFlag = ( insureCarFlag );
	}

	/**
	 * 获取属性是否为本保单车辆
	 * @return 属性是否为本保单车辆的值
	 */
	public String getInsureCarFlag() {
		return insureCarFlag;
	}

	/**
	 * 设置属性可操作/处理的级别
	 * @param handlerRange 待设置的属性可操作/处理的级别的值
	 */
	public void setHandlerRange(String handlerRange) {
		this.handlerRange = ( handlerRange );
	}

	/**
	 * 获取属性可操作/处理的级别
	 * @return 属性可操作/处理的级别的值
	 */
	public String getHandlerRange() {
		return handlerRange;
	}

	/**
	 * 设置属性紧急程度
	 * @param exigenceGree 待设置的属性紧急程度的值
	 */
	public void setExigenceGree(String exigenceGree) {
		this.exigenceGree = ( exigenceGree );
	}

	/**
	 * 获取属性紧急程度
	 * @return 属性紧急程度的值
	 */
	public String getExigenceGree() {
		return exigenceGree;
	}

	/**
	 * 设置属性报案号码
	 * @param registNo 待设置的属性报案号码的值
	 */
	public void setRegistNo(String registNo) {
		this.registNo = ( registNo );
	}

	/**
	 * 获取属性报案号码
	 * @return 属性报案号码的值
	 */
	public String getRegistNo() {
		return registNo;
	}

	/**
	 * 设置属性被保险人名称
	 * @param insuredName 待设置的属性被保险人名称的值
	 */
	public void setInsuredName(String insuredName) {
		this.insuredName = ( insuredName );
	}

	/**
	 * 获取属性被保险人名称
	 * @return 属性被保险人名称的值
	 */
	public String getInsuredName() {
		return insuredName;
	}

	/**
	 * 设置属性简易赔案标记
	 * @param claimTypeFlag 待设置的属性简易赔案标记的值
	 */
	public void setClaimTypeFlag(String claimTypeFlag) {
		this.claimTypeFlag = ( claimTypeFlag );
	}

	/**
	 * 获取属性简易赔案标记
	 * @return 属性简易赔案标记的值
	 */
	public String getClaimTypeFlag() {
		return claimTypeFlag;
	}

	public String getDamageAddress() {
		return damageAddress;
	}

	public void setDamageAddress(String damageAddress) {
		this.damageAddress = damageAddress;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public Date getDamageTime() {
		return damageTime;
	}

	public void setDamageTime(Date damageTime) {
		this.damageTime = damageTime;
	}

	/**
	 * @return 返回 othPolicyno。
	 */
	public String getOthPolicyno() {
		return othPolicyno;
	}

	/**
	 * @param othPolicyno 要设置的 othPolicyno。
	 */
	public void setOthPolicyno(String othPolicyno) {
		this.othPolicyno = othPolicyno;
	}

}
