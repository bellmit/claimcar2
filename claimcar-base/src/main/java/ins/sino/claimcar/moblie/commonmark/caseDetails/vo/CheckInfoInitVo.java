package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;



import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("CHECKTASKINFO")
public class CheckInfoInitVo implements Serializable{
    
    /**  */
    private static final long serialVersionUID = 1L;
    
    
    @XStreamAlias("REGISTNO")
    private String registNo; //报案号
    
    @XStreamAlias("ISUNILACCIDENT")
    private String isunilAccident; //是否单车事故
    
    @XStreamAlias("DAMAGETYPECODE")
    private String damageTypeCode; //事故类型
    
    @XStreamAlias("MANAGETYPENAME")
    private String manageTypeName; //事故处理类型
    
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
    
    /*@XStreamAlias("INDEMNITYDUTYRATE")
    private String indemnityDutyRate; //事故责任比例
*/
    
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
    
    @XStreamAlias("CHECKOPTION")
    private String checkOption; //查勘意见
    
    @XStreamAlias("NODUTYFLAG")
    private String noDutyFlag; //是否免责情形

    @XStreamAlias("DAMOTHERCODE")
    private String damOtherCode; //二级出险原因
    
    @XStreamAlias("NODUTYREASON")
    private String noDutyReason; //免责原因
    
    @XStreamAlias("CHECKIDENTIFYNUMBER")
    private String checkIdenTifyNumber; //查勘人身份证号

    @XStreamAlias("CHECKPHONENUMBER")
    private String checkPhoneNumber; //查勘人联系电话
    
    /*自助理赔*/
    @XStreamAlias("CHECKERCODE")
    private String checkerCode; //查勘人员代码

    @XStreamAlias("CHECKERNAME")
    private String checkerName; //查勘人员名称
    
    @XStreamAlias("CARLIST")
    private List<CarInfoVo> carInfo; //车辆信息
    
    @XStreamAlias("PERSONLIST")
    private List<PersonInfoVo> personInfo; //人伤信息
    
    @XStreamAlias("PROPLIST")
    private List<PropInfoVo> propInfo; //物损信息
    
    @XStreamAlias("ACCOUNTLIST")
    private List<AccountInfoVo> accountInfo; //收款人信息
    
    @XStreamAlias("EXTENDINFO")
    private ExtendInfoVo extendInfoVo;//查勘扩展信息

    public String getRegistNo() {
        return registNo;
    }

    
    public void setRegistNo(String registNo) {
        this.registNo = registNo;
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

    
    public String getBigCase() {
        return bigCase;
    }

    
    public void setBigCase(String bigCase) {
        this.bigCase = bigCase;
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

    
    public String getCheckOption() {
        return checkOption;
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

    
    public String getCheckIdenTifyNumber() {
		return checkIdenTifyNumber;
	}


	public void setCheckIdenTifyNumber(String checkIdenTifyNumber) {
		this.checkIdenTifyNumber = checkIdenTifyNumber;
	}


	public String getCheckPhoneNumber() {
		return checkPhoneNumber;
	}


	public void setCheckPhoneNumber(String checkPhoneNumber) {
		this.checkPhoneNumber = checkPhoneNumber;
	}


	public void setCheckOption(String checkOption) {
        this.checkOption = checkOption;
    }

    
 


	public String getCheckerCode() {
		return checkerCode;
	}


	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}


	public String getCheckerName() {
		return checkerName;
	}


	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}



	public List<CarInfoVo> getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(List<CarInfoVo> carInfo) {
        this.carInfo = carInfo;
    }

    public List<PersonInfoVo> getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(List<PersonInfoVo> personInfo) {
        this.personInfo = personInfo;
    }

    public List<PropInfoVo> getPropInfo() {
        return propInfo;
    }

    public void setPropInfo(List<PropInfoVo> propInfo) {
        this.propInfo = propInfo;
    }

    public List<AccountInfoVo> getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(List<AccountInfoVo> accountInfo) {
        this.accountInfo = accountInfo;
    }
    
    public ExtendInfoVo getExtendInfoVo() {
        return extendInfoVo;
    }  
    
    public void setExtendInfoVo(ExtendInfoVo extendInfoVo) {
        this.extendInfoVo = extendInfoVo;
    }
}
