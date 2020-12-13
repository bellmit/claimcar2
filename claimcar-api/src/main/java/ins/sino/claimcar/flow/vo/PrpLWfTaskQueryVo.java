package ins.sino.claimcar.flow.vo;

import java.util.Date;

/**
 * Custom VO class of PO PrpLWfTaskQuery
 */
public class PrpLWfTaskQueryVo extends PrpLWfTaskQueryVoBase implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String querySystem;// 查询的系统，OldClaim表示查询旧系统数据
	// 必须条件
	private String nodeCode;// 节点名称
	private String subNodeCode;
 	private String taskInNode;// 查询核价，核损任务时，标记来自哪个节点

	private String handleStatus;// 任务状态
	private String userCode;// 当前登陆人用户名
	private String comCode;// 案件归属机构

	private String keyProperty;// 关键条件属性
	
	// 赔案流程查询增加start
	private String claimNo;// 立案号
	private String compensateNo;//计算书号
	private String flowStatus;// 工作流状态 1-正常流转，2结束流转
	// 赔案流程查询增加end
    
	private String queryRange;// 查询范围 0-个人1-所有,2-下级
	private Date damageTimeStart;// 出险开始时间
	private Date damageTimeEnd;// 出险结束时间
	private Date reportTimeStart;// 报案开始时间
	private Date reportTimeEnd;// 报案结束时间
	private Date taskInTimeStart;// 流入开始时间
	private Date taskInTimeEnd;// 流入结束时间
	private String taskInUser;//

	// 分页
	private Integer start;// 记录起始位置
	private Integer length;// 记录数
	// 车俩核损添加
	private String includeLower;// 是否包含下级

	// 人伤添加，相关字段
	private String personName;// 人员姓名 存放在PrplWfTaskQuery.LossPersonInfo
	private String hospitalName;// 医院名称 存放在PrplWfTaskQuery.LossPersonInfo
	private Date appointmentTimeStart;// 预约时间开始时间 存放在PrplWfTaskQuery.AppointmentTime
	private Date appointmentTimeEnd;// 预约时间结束时间
	private String deflossCarType;
	private Date inHospitalDateStart;// 住院开始时间
	private Date inHospitalDateEnd;// 住院结束时间
	private String engineNo;

	//大案审核等级
	private String plBig;
 	private String ChkBig_LV1;
 	private String ChkBig_LV2;
 	private String ChkBig_LV3;
 	private String ChkBig_LV4;
 	private String ChkBig_LV5;
 	
 	//业务任务类型
 	private String ywTaskType;
 	
 	//平级移交
 	private String assignUser;
 	
 	//预付
 	private String prePayType;
 	private Date underwriteDateStart;// 核赔通过开始时间
	private Date underwriteDateEnd;// 核赔通过结束时间
 	
	// 三者车车架号
	private String ThirdFrameNo;
	// 三者车车牌号
    private String ThirdLicenseNo;
    // 三者车发动机号
    private String ThirdEngineNo;
    
    
    //公估机构代码
    private String intermCode;
    //公估处理案件
    private String intermFlag;
    
    //查勘机构代码
    private String checkCode;
    
    //查勘处理案件
    private String checkFlag;
    
    
	

	//任务号
    private String taskNo;
    
    private String assignCode;//到岗查的机构
    
    private String oldClaimUrl;//旧理赔URL
    
    private String sorting;//排序种类
    private String sortType;//排序类型
    private String autoType;//是否自动核陪 0-否，1-是
    private String isPerson;
    private String compensateAmount;//理算金额

    private String bussNo;//业务号

    private String isVLoss;//是否核损完成
    
    private String payeeName;//收款人名称
    
    private String accountNo;//收款人账号
    
    private Date operateTime;//处理时间
    private String operateUser;//处理人
    
    private String subCheckFlag;//司内代查勘
    private String assessSubCheckFlag;//公估代查勘
    private String checkType;//代查勘类型0-公估代查勘 1-司内代查勘
    private String dcheckTaskType;//代查勘任务类型
    private String dcheckHandlerCom;//代查勘任务处理机构
    private String dcheckHandlerName;//代查勘任务处理人
    
   

	public String getDcheckHandlerCom() {
		return dcheckHandlerCom;
	}

	public void setDcheckHandlerCom(String dcheckHandlerCom) {
		this.dcheckHandlerCom = dcheckHandlerCom;
	}

	/**
	 * @return the intermCode
	 */
	public String getIntermCode() {
		return intermCode;
	}

	/**
	 * @param intermCode the intermCode to set
	 */
	public void setIntermCode(String intermCode) {
		this.intermCode = intermCode;
	}
    
	/**
	 * @return the taskNo
	 */
	public String getTaskNo() {
		return taskNo;
	}

	/**
	 * @param taskNo the taskNo to set
	 */
	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public String getPlBig() {
		return plBig;
	}

	public void setPlBig(String plBig) {
		this.plBig = plBig;
	}

	
	public String getChkBig_LV1() {
		return ChkBig_LV1;
	}

	public void setChkBig_LV1(String chkBig_LV1) {
		ChkBig_LV1 = chkBig_LV1;
	}

	public String getChkBig_LV2() {
		return ChkBig_LV2;
	}

	public void setChkBig_LV2(String chkBig_LV2) {
		ChkBig_LV2 = chkBig_LV2;
	}

	public String getChkBig_LV3() {
		return ChkBig_LV3;
	}

	public void setChkBig_LV3(String chkBig_LV3) {
		ChkBig_LV3 = chkBig_LV3;
	}

	public String getChkBig_LV4() {
		return ChkBig_LV4;
	}

	public void setChkBig_LV4(String chkBig_LV4) {
		ChkBig_LV4 = chkBig_LV4;
	}

	public String getChkBig_LV5() {
		return ChkBig_LV5;
	}

	public void setChkBig_LV5(String chkBig_LV5) {
		ChkBig_LV5 = chkBig_LV5;
	}

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public String getSubNodeCode() {
		return subNodeCode;
	}

	public void setSubNodeCode(String subNodeCode) {
		this.subNodeCode = subNodeCode;
	}

	public String getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
	}


	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public Integer getStart() {
		if(start==null) start = 0;
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		if(length==null) length = 10;
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getKeyProperty() {
		return keyProperty;
	}

	public void setKeyProperty(String keyProperty) {
		this.keyProperty = keyProperty;
	}
	

	public String getQueryRange() {
		return queryRange;
	}

	public void setQueryRange(String queryRange) {
		this.queryRange = queryRange;
	}

	public Date getDamageTimeStart() {
		return damageTimeStart;
	}

	public void setDamageTimeStart(Date damageTimeStart) {
		this.damageTimeStart = damageTimeStart;
	}

	public Date getDamageTimeEnd() {
		return damageTimeEnd;
	}

	public void setDamageTimeEnd(Date damageTimeEnd) {
		this.damageTimeEnd = damageTimeEnd;
	}

	public Date getReportTimeStart() {
		return reportTimeStart;
	}

	public void setReportTimeStart(Date reportTimeStart) {
		this.reportTimeStart = reportTimeStart;
	}

	public Date getReportTimeEnd() {
		return reportTimeEnd;
	}

	public void setReportTimeEnd(Date reportTimeEnd) {
		this.reportTimeEnd = reportTimeEnd;
	}

	public Date getTaskInTimeStart() {
		return taskInTimeStart;
	}

	public void setTaskInTimeStart(Date taskInTimeStart) {
		this.taskInTimeStart = taskInTimeStart;
	}

	public Date getTaskInTimeEnd() {
		return taskInTimeEnd;
	}

	public void setTaskInTimeEnd(Date taskInTimeEnd) {
		this.taskInTimeEnd = taskInTimeEnd;
	}

	public String getIncludeLower() {
		return includeLower;
	}

	public void setIncludeLower(String includeLower) {
		this.includeLower = includeLower;
	}
	
	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	
	
	public String getCompensateNo() {
		return compensateNo;
	}

	
	public void setCompensateNo(String compensateNo) {
		this.compensateNo = compensateNo;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public Date getAppointmentTimeStart() {
		return appointmentTimeStart;
	}

	public void setAppointmentTimeStart(Date appointmentTimeStart) {
		this.appointmentTimeStart = appointmentTimeStart;
	}

	public Date getAppointmentTimeEnd() {
		return appointmentTimeEnd;
	}

	public void setAppointmentTimeEnd(Date appointmentTimeEnd) {
		this.appointmentTimeEnd = appointmentTimeEnd;
	}

	public String getTaskInUser() {
		return taskInUser;
	}

	public void setTaskInUser(String taskInUser) {
		this.taskInUser = taskInUser;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getTaskInNode() {
		return taskInNode;
	}

	public void setTaskInNode(String taskInNode) {
		this.taskInNode = taskInNode;
	}

	public String getDeflossCarType() {
		return deflossCarType;
	}

	public void setDeflossCarType(String deflossCarType) {
		this.deflossCarType = deflossCarType;
	}

	public String getAssignUser() {
		return assignUser;
	}

	public void setAssignUser(String assignUser) {
		this.assignUser = assignUser;
	}

	
	public Date getInHospitalDateStart() {
		return inHospitalDateStart;
	}

	public void setInHospitalDateStart(Date inHospitalDateStart) {
		this.inHospitalDateStart = inHospitalDateStart;
	}

	public Date getInHospitalDateEnd() {
		return inHospitalDateEnd;
	}

	public void setInHospitalDateEnd(Date inHospitalDateEnd) {
		this.inHospitalDateEnd = inHospitalDateEnd;
	}

	
	public Date getUnderwriteDateStart() {
		return underwriteDateStart;
	}

	
	public void setUnderwriteDateStart(Date underwriteDateStart) {
		this.underwriteDateStart = underwriteDateStart;
	}

	
	public Date getUnderwriteDateEnd() {
		return underwriteDateEnd;
	}
	
	public String getCheckCode() {
		return checkCode;
	}

	
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	
	public String getCheckFlag() {
		return checkFlag;
	}

	
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

	
	public void setUnderwriteDateEnd(Date underwriteDateEnd) {
		this.underwriteDateEnd = underwriteDateEnd;
	}

	
	public String getPrePayType() {
		return prePayType;
	}

	
	public void setPrePayType(String prePayType) {
		this.prePayType = prePayType;
	}

	public String getThirdFrameNo() {
		return ThirdFrameNo;
	}

	public void setThirdFrameNo(String thirdFrameNo) {
		ThirdFrameNo = thirdFrameNo;
	}

	public String getThirdLicenseNo() {
		return ThirdLicenseNo;
	}

	public void setThirdLicenseNo(String thirdLicenseNo) {
		ThirdLicenseNo = thirdLicenseNo;
	}


	public String getThirdEngineNo() {
		return ThirdEngineNo;
	}

	public void setThirdEngineNo(String thirdEngineNo) {
		ThirdEngineNo = thirdEngineNo;
	}

	public String getQuerySystem() {
		return querySystem;
	}

	public void setQuerySystem(String querySystem) {
		this.querySystem = querySystem;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getYwTaskType() {
		return ywTaskType;
	}

	public void setYwTaskType(String ywTaskType) {
		this.ywTaskType = ywTaskType;
	}

	public String getAssignCode() {
		return assignCode;
	}

	public void setAssignCode(String assignCode) {
		this.assignCode = assignCode;
	}

	public String getIntermFlag() {
		return intermFlag;
	}

	public void setIntermFlag(String intermFlag) {
		this.intermFlag = intermFlag;
	}

	public String getOldClaimUrl() {
		return oldClaimUrl;
	}

	public void setOldClaimUrl(String oldClaimUrl) {
		this.oldClaimUrl = oldClaimUrl;
	}

	public String getSorting() {
		return sorting;
	}

	public void setSorting(String sorting) {
		this.sorting = sorting;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getIsPerson() {
		return isPerson;
	}

	public void setIsPerson(String isPerson) {
		this.isPerson = isPerson;
	}

	public String getCompensateAmount() {
		return compensateAmount;
	}

	public void setCompensateAmount(String compensateAmount) {
		this.compensateAmount = compensateAmount;
	}


	public String getBussNo() {
		return bussNo;
	}

	public void setBussNo(String bussNo) {
		this.bussNo = bussNo;
	}

    
    public String getIsVLoss() {
        return isVLoss;
    }

    
    public void setIsVLoss(String isVLoss) {
        this.isVLoss = isVLoss;
    }

    
    public String getPayeeName() {
        return payeeName;
    }

    
    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    
    public String getAccountNo() {
        return accountNo;
    }

    
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

	public String getAutoType() {
		return autoType;
	}

	public void setAutoType(String autoType) {
		this.autoType = autoType;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperateUser() {
		return operateUser;
	}

	public void setOperateUser(String operateUser) {
		this.operateUser = operateUser;
	}

	public String getSubCheckFlag() {
		return subCheckFlag;
	}

	public void setSubCheckFlag(String subCheckFlag) {
		this.subCheckFlag = subCheckFlag;
	}

	public String getAssessSubCheckFlag() {
		return assessSubCheckFlag;
	}

	public void setAssessSubCheckFlag(String assessSubCheckFlag) {
		this.assessSubCheckFlag = assessSubCheckFlag;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getDcheckTaskType() {
		return dcheckTaskType;
	}

	public void setDcheckTaskType(String dcheckTaskType) {
		this.dcheckTaskType = dcheckTaskType;
	}

	public String getDcheckHandlerName() {
		return dcheckHandlerName;
	}

	public void setDcheckHandlerName(String dcheckHandlerName) {
		this.dcheckHandlerName = dcheckHandlerName;
	}
    
	
	
/*	public String[] getSubNodes() {
		return subNodes;
	}

	public void setSubNodes(String[] subNodes) {
		this.subNodes = subNodes;
	}*/
	
	
	
}
