package ins.sino.claimcar.regist.vo;

import java.io.Serializable;
import java.util.Date;


public class PrpCcarDriverVo implements Serializable{
    /**  */
    private static final long serialVersionUID = 1L;
    /** 属性保单号码 */
    private String policyNo = "";
    /** 属性险种代码 */
    private String riskCode = "";
    /** 属性标的序号 */
    private int itemNo = 0;
    /** 属性顺序号 */
    private int serialNo = 0;
    /** 属性驾驶证号码 */
    private String drivingLicenseNo = "";
    /** 属性是否固定驾驶员标志 */
    private String changelessFlag = "";
    /** 属性驾驶员姓名 */
    private String driverName = "";
    /** 属性身份证号码 */
    private String identifynumber = "";
    /** 属性性别 */
    private String sex = "";
    /** 属性年龄 */
    private int age = 0;
    /** 属性婚姻状况 */
    private String marriage = "";
    /** 属性单位或地址 */
    private String driverAddress = "";
    /** 属性工作单位性质代码 */
    private String possessnature = "";
    /** 属性从业类别代码 */
    private String businessSource = "";
    /** 属性是否有违章扣分 */
    private int peccancy = 0;
    /** 属性初次领证日期 */
    private Date acceptLicenseDate;
    /** 属性领驾驶证年数 */
    private int receivelicenseyear = 0;
    /** 属性驾龄 */
    private int drivingYears = 0;
    /** 属性近两年肇事次数 */
    private int causetroubletimes = 0;
    /** 属性颁证机关 */
    private String awardLicenseOrgan = "";
    /** 属性准驾车型 */
    private String drivingCarType = "";
    /** 属性标志字段 */
    private String flag = "";

    /**
     *  默认构造方法,构造一个默认的PrpCcarDriverDtoBase对象
     */
    public PrpCcarDriverVo(){
    }

    
    public String getPolicyNo() {
        return policyNo;
    }

    
    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    
    public String getRiskCode() {
        return riskCode;
    }

    
    public void setRiskCode(String riskCode) {
        this.riskCode = riskCode;
    }

    
    public int getItemNo() {
        return itemNo;
    }

    
    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }

    
    public int getSerialNo() {
        return serialNo;
    }

    
    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    
    public String getDrivingLicenseNo() {
        return drivingLicenseNo;
    }

    
    public void setDrivingLicenseNo(String drivingLicenseNo) {
        this.drivingLicenseNo = drivingLicenseNo;
    }

    
    public String getChangelessFlag() {
        return changelessFlag;
    }

    
    public void setChangelessFlag(String changelessFlag) {
        this.changelessFlag = changelessFlag;
    }

    
    public String getDriverName() {
        return driverName;
    }

    
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    
    public String getIdentifynumber() {
        return identifynumber;
    }

    
    public void setIdentifynumber(String identifynumber) {
        this.identifynumber = identifynumber;
    }

    
    public String getSex() {
        return sex;
    }

    
    public void setSex(String sex) {
        this.sex = sex;
    }

    
    public int getAge() {
        return age;
    }

    
    public void setAge(int age) {
        this.age = age;
    }

    
    public String getMarriage() {
        return marriage;
    }

    
    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    
    public String getDriverAddress() {
        return driverAddress;
    }

    
    public void setDriverAddress(String driverAddress) {
        this.driverAddress = driverAddress;
    }

    
    public String getPossessnature() {
        return possessnature;
    }

    
    public void setPossessnature(String possessnature) {
        this.possessnature = possessnature;
    }

    
    public String getBusinessSource() {
        return businessSource;
    }

    
    public void setBusinessSource(String businessSource) {
        this.businessSource = businessSource;
    }

    
    public int getPeccancy() {
        return peccancy;
    }

    
    public void setPeccancy(int peccancy) {
        this.peccancy = peccancy;
    }

    
    public Date getAcceptLicenseDate() {
        return acceptLicenseDate;
    }

    
    public void setAcceptLicenseDate(Date acceptLicenseDate) {
        this.acceptLicenseDate = acceptLicenseDate;
    }

    
    public int getReceivelicenseyear() {
        return receivelicenseyear;
    }

    
    public void setReceivelicenseyear(int receivelicenseyear) {
        this.receivelicenseyear = receivelicenseyear;
    }

    
    public int getDrivingYears() {
        return drivingYears;
    }

    
    public void setDrivingYears(int drivingYears) {
        this.drivingYears = drivingYears;
    }

    
    public int getCausetroubletimes() {
        return causetroubletimes;
    }

    
    public void setCausetroubletimes(int causetroubletimes) {
        this.causetroubletimes = causetroubletimes;
    }

    
    public String getAwardLicenseOrgan() {
        return awardLicenseOrgan;
    }

    
    public void setAwardLicenseOrgan(String awardLicenseOrgan) {
        this.awardLicenseOrgan = awardLicenseOrgan;
    }

    
    public String getDrivingCarType() {
        return drivingCarType;
    }

    
    public void setDrivingCarType(String drivingCarType) {
        this.drivingCarType = drivingCarType;
    }

    
    public String getFlag() {
        return flag;
    }

    
    public void setFlag(String flag) {
        this.flag = flag;
    }

  
}
