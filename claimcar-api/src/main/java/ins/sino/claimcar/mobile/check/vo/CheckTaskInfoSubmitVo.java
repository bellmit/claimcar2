package ins.sino.claimcar.mobile.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("CHECKTASKINFO")
public class CheckTaskInfoSubmitVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("REGISTNO")
	private String registNo; //报案号
	
	@XStreamAlias("SYSTEMTYPE")
	private String systemType;//请求子系统

	@XStreamAlias("CHECKID")
	private String checkId; //理赔查勘ID
	
	@XStreamAlias("TASKID")
	private String taskId; //任务id
	
	@XStreamAlias("NODETYPE")
	private String nodeType; //调度节点
	
	@XStreamAlias("ITEMNO")
	private String itemNo; //标的序号
	
	@XStreamAlias("ITEMNONAME")
	private String itemnoName; //标的名称
	
	@XStreamAlias("ISOBJECT")
	private String isObject; //是否标的
	
	@XStreamAlias("NEXTHANDLERCODE")
	private String nextHandlerCode; //处理人代码
	
	@XStreamAlias("NEXTHANDLERNAME")
	private String nextHandlerName; //处理人名称
	
	@XStreamAlias("SCHEDULEOBJECTID")
	private String scheduleObjectId; //处理人员归属机构编码
	
	@XStreamAlias("SCHEDULEOBJECTNAME")
	private String scheduleObjectName; //处理人员归属机构名称
	
	@XStreamAlias("OPTIONTYPE")
	private String optionType; //操作类型
	
	@XStreamAlias("ISUNILACCIDENT")
	private String isunilAccident; //是否单车事故
	
	@XStreamAlias("DAMAGETYPECODE")
	private String damageTypeCode; //事故类型
	
	@XStreamAlias("MANAGETYPENAME")
	private String manageTypeName; //事故处理类型
	
	@XStreamAlias("ACCIDENTREASON")
	private String accidentReason; //事故原因
	
	@XStreamAlias("DAMAGECODE")
	private String damageCode; //出险原因
	
	@XStreamAlias("ISLOSS")
	private String isLoss; //是否全损
	
	@XStreamAlias("ISFIRE")
	private String isFire; //是否火自爆
	
	@XStreamAlias("ISWATER")
	private String isWater; //是否水淹
	
	@XStreamAlias("WATERLEVEL")
	private String waterLevel; //水淹等级
	
	@XStreamAlias("CHECKSITE")
	private String checkSite; //查勘地点
	
	@XStreamAlias("CHECKDATE")
	private String checkDate; //查勘日期
	
	@XStreamAlias("CHECKTYPE")
	private String checkType; //查勘类型
	
    @XStreamAlias("CHECKIDENTIFYNUMBER")
    private String checkIdentifyNumber; //查勘人身份证号
    
    @XStreamAlias("CHECKPHONENUMBER")
    private String checkPhoneNumber; //查勘人联系号码
    
	@XStreamAlias("FIRSTSITEFLAG")
	private String firstSiteFlag; //是否第一现场查勘
	
	@XStreamAlias("EXCESSTYPE")
	private String excessType; //是否互碰自赔
	
	@XStreamAlias("ISINCLUDEPROP")
	private String isIncludeProp; //是否包含财损
	
	@XStreamAlias("ISINCLUDEPERSON")
	private String isIncludePerson; //是否包含人伤
	
	@XStreamAlias("INDEMNITYDUTY")
	private String indemnityDuty; //事故责任比例
	
	/*@XStreamAlias("INDEMNITYDUTY")
	private String indemnityDuty; //事故责任
*/	
	@XStreamAlias("INDEMNITYDUTYRATE")
	private String indemnityDutyRate; //事故责任
	
	@XStreamAlias("ISBIGCASE")
	private String isBigCase; //是否重大赔案上报
	
	@XStreamAlias("CHECKFEE")
	private String checkFee; //查勘费用
	
	@XStreamAlias("SUBROGATETYPE")
	private String subrogateType; //是否代位求偿
	
	@XStreamAlias("TEXTTYPE")
	private String textType; //本次查勘报告
	
	/*@XStreamAlias("CHECKOPTION")
	private String checkOption; //查勘意见
*/	

	@XStreamAlias("DAMOTHERCODE")
    private String damOtherCode; //二级出险原因

	@XStreamAlias("NODUTYFLAG")
	private String noDutyFlag; //是否免责情形

	@XStreamAlias("NODUTYREASON")
	private String noDutyReason; //免责原因(下拉框)

	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getItemnoName() {
		return itemnoName;
	}

	public void setItemnoName(String itemnoName) {
		this.itemnoName = itemnoName;
	}

	public String getIsObject() {
		return isObject;
	}

	public void setIsObject(String isObject) {
		this.isObject = isObject;
	}

	public String getNextHandlerCode() {
		return nextHandlerCode;
	}

	public void setNextHandlerCode(String nextHandlerCode) {
		this.nextHandlerCode = nextHandlerCode;
	}

	public String getNextHandlerName() {
		return nextHandlerName;
	}

	public void setNextHandlerName(String nextHandlerName) {
		this.nextHandlerName = nextHandlerName;
	}

	public String getScheduleObjectId() {
		return scheduleObjectId;
	}

	public void setScheduleObjectId(String scheduleObjectId) {
		this.scheduleObjectId = scheduleObjectId;
	}

	public String getScheduleObjectName() {
		return scheduleObjectName;
	}

	public void setScheduleObjectName(String scheduleObjectName) {
		this.scheduleObjectName = scheduleObjectName;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public String getIsunilAccident() {
		return isunilAccident;
	}

	public void setIsunilAccident(String isunilAccident) {
		this.isunilAccident = isunilAccident;
	}

	public String getDamageTypeCode() {
		return damageTypeCode;
	}

	public void setDamageTypeCode(String damageTypeCode) {
		this.damageTypeCode = damageTypeCode;
	}

	public String getManageTypeName() {
		return manageTypeName;
	}

	public void setManageTypeName(String manageTypeName) {
		this.manageTypeName = manageTypeName;
	}

    public String getAccidentReason() {
        return accidentReason;
    }

    
    public void setAccidentReason(String accidentReason) {
        this.accidentReason = accidentReason;
    }

    public String getDamageCode() {
		return damageCode;
	}

	public void setDamageCode(String damageCode) {
		this.damageCode = damageCode;
	}

	public String getIsLoss() {
		return isLoss;
	}

	public void setIsLoss(String isLoss) {
		this.isLoss = isLoss;
	}

	public String getIsFire() {
		return isFire;
	}

	public void setIsFire(String isFire) {
		this.isFire = isFire;
	}

	public String getIsWater() {
		return isWater;
	}

	public void setIsWater(String isWater) {
		this.isWater = isWater;
	}

	public String getWaterLevel() {
		return waterLevel;
	}

	public void setWaterLevel(String waterLevel) {
		this.waterLevel = waterLevel;
	}

	public String getCheckSite() {
		return checkSite;
	}

	public void setCheckSite(String checkSite) {
		this.checkSite = checkSite;
	}

	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getFirstSiteFlag() {
		return firstSiteFlag;
	}

	public void setFirstSiteFlag(String firstSiteFlag) {
		this.firstSiteFlag = firstSiteFlag;
	}

	public String getExcessType() {
		return excessType;
	}

	public void setExcessType(String excessType) {
		this.excessType = excessType;
	}

	public String getIsIncludeProp() {
		return isIncludeProp;
	}

	public void setIsIncludeProp(String isIncludeProp) {
		this.isIncludeProp = isIncludeProp;
	}

	public String getIsIncludePerson() {
		return isIncludePerson;
	}

	public void setIsIncludePerson(String isIncludePerson) {
		this.isIncludePerson = isIncludePerson;
	}

	public String getIndemnityDuty() {
		return indemnityDuty;
	}

	public void setIndemnityDuty(String indemnityDuty) {
		this.indemnityDuty = indemnityDuty;
	}

	public String getIndemnityDutyRate() {
		return indemnityDutyRate;
	}

	public void setIndemnityDutyRate(String indemnityDutyRate) {
		this.indemnityDutyRate = indemnityDutyRate;
	}

	public String getIsBigCase() {
		return isBigCase;
	}

	public void setIsBigCase(String isBigCase) {
		this.isBigCase = isBigCase;
	}

	public String getCheckFee() {
		return checkFee;
	}

	public void setCheckFee(String checkFee) {
		this.checkFee = checkFee;
	}

	public String getSubrogateType() {
		return subrogateType;
	}

	public void setSubrogateType(String subrogateType) {
		this.subrogateType = subrogateType;
	}

	public String getTextType() {
		return textType;
	}

	public void setTextType(String textType) {
		this.textType = textType;
	}

    
    public String getCheckIdentifyNumber() {
        return checkIdentifyNumber;
    }

    
    public void setCheckIdentifyNumber(String checkIdentifyNumber) {
        this.checkIdentifyNumber = checkIdentifyNumber;
    }

    
    public String getCheckPhoneNumber() {
        return checkPhoneNumber;
    }

    
    public void setCheckPhoneNumber(String checkPhoneNumber) {
        this.checkPhoneNumber = checkPhoneNumber;
    }

    public String getDamOtherCode() {
        return damOtherCode;
    }
    
    public void setDamOtherCode(String damOtherCode) {
        this.damOtherCode = damOtherCode;
    }

	public String getNoDutyFlag() {
		return noDutyFlag;
	}

	public void setNoDutyFlag(String noDutyFlag) {
		this.noDutyFlag = noDutyFlag;
	}

	public String getNoDutyReason() {
		return noDutyReason;
	}

	public void setNoDutyReason(String noDutyReason) {
		this.noDutyReason = noDutyReason;
	}




}
