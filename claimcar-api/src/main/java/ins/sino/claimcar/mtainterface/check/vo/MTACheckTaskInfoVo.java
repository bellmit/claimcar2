package ins.sino.claimcar.mtainterface.check.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CHECKTASKINFO")
public class MTACheckTaskInfoVo implements Serializable{

private static final long serialVersionUID = 1L;
	
	@XStreamAlias("REGISTNO")
	private String registNo; //报案号
	
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
	
	@XStreamAlias("ISUNILACCIDENT")
	private String isunilAccident; //是否单车事故
	
	@XStreamAlias("DAMAGETYPECODE")
	private String damageTypeCode; //事故类型
	
	@XStreamAlias("MANAGETYPENAME")
	private String manageTypeName; //事故处理类型
	
	@XStreamAlias("DAMAGECODE")
	private String damageCode; //出险原因
	
	@XStreamAlias("DAMOTHERCODE")
	private String damOtherCode; //二级出险原因
	
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
	private String indemnityDuty; //事故责任
	
	@XStreamAlias("INDEMNITYDUTYRATE")
	private String indemnityDutyRate; //事故比例
	
	@XStreamAlias("BIGCASE")
	private String bigCase; //大案上报信息
	
	@XStreamAlias("ISBIGCASE")
	private String isBigCase; //是否重大赔案上报
	
	@XStreamAlias("CHECKFEE")
	private String checkFee; //查勘费用
	
	@XStreamAlias("SUBROGATETYPE")
	private String subrogateType; //是否代位求偿
	
	@XStreamAlias("TEXTTYPE")
	private String textType; //本次查勘报告
	
    @XStreamAlias("ACCIDENTREASON")
    private String accidentReason; //事故原因
    
	@XStreamAlias("CARLIST")
	private List<MTACarInfoVo> carInfo; //车辆信息
	
	@XStreamAlias("PERSONLIST")
	private List<MTAPersonInfoVo> personInfo; //人伤信息
	
	@XStreamAlias("PROPLIST")
	private List<MTAPropInfoVo> propInfo; //物损信息
	
	@XStreamAlias("SUBROGATIONLIST")
	private List<MTASubrogationInfoVo> subrogationInfo;	//代位求偿

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getCheckId() {
		return checkId;
	}

	public void setCheckId(String checkId) {
		this.checkId = checkId;
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

	public String getIsFire() {
		return isFire;
	}

	public void setIsFire(String isFire) {
		this.isFire = isFire;
	}

	public String getBigCase() {
		return bigCase;
	}

	public void setBigCase(String bigCase) {
		this.bigCase = bigCase;
	}

	public String getDamOtherCode() {
		return damOtherCode;
	}

	public void setDamOtherCode(String damOtherCode) {
		this.damOtherCode = damOtherCode;
	}

	public List<MTACarInfoVo> getCarInfo() {
		return carInfo;
	}

	public void setCarInfo(List<MTACarInfoVo> carInfo) {
		this.carInfo = carInfo;
	}

	public List<MTAPersonInfoVo> getPersonInfo() {
		return personInfo;
	}

	public void setPersonInfo(List<MTAPersonInfoVo> personInfo) {
		this.personInfo = personInfo;
	}

	public List<MTAPropInfoVo> getPropInfo() {
		return propInfo;
	}

	public void setPropInfo(List<MTAPropInfoVo> propInfo) {
		this.propInfo = propInfo;
	}

	public List<MTASubrogationInfoVo> getSubrogationInfo() {
		return subrogationInfo;
	}

	public void setSubrogationInfo(List<MTASubrogationInfoVo> subrogationInfo) {
		this.subrogationInfo = subrogationInfo;
	}

    
    public String getAccidentReason() {
        return accidentReason;
    }

    
    public void setAccidentReason(String accidentReason) {
        this.accidentReason = accidentReason;
    }


}
