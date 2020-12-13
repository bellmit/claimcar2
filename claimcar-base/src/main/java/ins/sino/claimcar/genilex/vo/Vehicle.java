package ins.sino.claimcar.genilex.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Vehicle")
public class Vehicle implements Serializable {

	/**  */
	private static final long serialVersionUID = 6606450331852430142L;
	
	@XStreamAlias("VehicleID")
	private String vehicleID;  //车辆ID
	
    @XStreamAlias("LicensePlateNo")
    private String licensePlateNo;  //号牌号码
    
    @XStreamAlias("LicensePlateColorCode")
    private String licensePlateColorCode;  //号牌底色
    
    @XStreamAlias("LicensePlateType")
    private String licensePlateType;  //号牌种类代码
    
    @XStreamAlias("MotorTypeCode")
    private String motorTypeCode;  //车辆种类代码
    
    @XStreamAlias("MotorUsageTypeCode")
    private String motorUsageTypeCode;  //使用性质代码
    
    @XStreamAlias("FirstRegisterDate")
    private String firstRegisterDate;  //车辆初始登记日期
    
    @XStreamAlias("VIN")
    private String vIN;  //车辆识别代号
    
    @XStreamAlias("EngineNo")
    private String engineNo;  //发动机号
	   
    @XStreamAlias("BodyColor")
    private String bodyColor;  //车身颜色
    
    @XStreamAlias("VehicleNature")
    private String vehicleNature;  //车辆所属性质
    
    @XStreamAlias("ImportFlag")
    private String importFlag;  //车型种类
    
    @XStreamAlias("VehicleValue")
    private String vehicleValue="";  //新车购置价
    
    @XStreamAlias("WholeWeight")
    private String wholeWeight="";  //整备质量
    
    @XStreamAlias("RatedPassengerCapacity")
    private String ratedPassengerCapacity="";  //核定载客人数
    
    @XStreamAlias("Tonnage")
    private String tonnage="";  //核定载质量
    
    @XStreamAlias("MadeFactory")
    private String madeFactory="";  //制造厂名称
    
    @XStreamAlias("Model")
    private String model="";  //车辆型号
    
    @XStreamAlias("BrandName")
    private String brandName="";  //品牌名称
    
    @XStreamAlias("BrandCode")
    private String brandCode="";  //英文编码
    
    @XStreamAlias("Haulage")
    private String haulage="";  //准牵引总质量
    
    @XStreamAlias("Displacement")
    private String displacement="";  //排量
    
    @XStreamAlias("Power")
    private String power="";  //功率

    @XStreamAlias("LfDate")
    private String lfDate="";  //出厂日期
    
    @XStreamAlias("GlassType")
    private String glassType="";  //玻璃类型
    
    @XStreamAlias("PmQueryNo")
    private String pmQueryNo="";  //交管车辆查询码
    
    @XStreamAlias("VehicleType")
    private String vehicleType="";  //交管车辆种类
    
    @XStreamAlias("IneffectualDate")
    private String ineffectualDate="";  //检验有效日期止
    
    @XStreamAlias("RejectDate")
    private String rejectDate="";  //强制有效期止

    @XStreamAlias("LastCheckDate")
    private String lastCheckDate="";  //最近定检日期
    
    @XStreamAlias("TransferDate")
    private String transferDate="";  //转移登记日期
    
    @XStreamAlias("NoLicenseFlag")
    private String noLicenseFlag="";  //未上牌车辆标志
    
    @XStreamAlias("NewVehicleFlag")
    private String newVehicleFlag="";  //新车标志
    
    @XStreamAlias("ChgOwnerFlag")
    private String chgOwnerFlag="";  //过户车辆标志
    
    @XStreamAlias("EcdemicVehicleFlag")
    private String ecdemicVehicleFlag="";  //外地车标志
    
    @XStreamAlias("LoanVehicleFlag")
    private String loanVehicleFlag="";  //是否车贷投保多年标志
    
    @XStreamAlias("FleetFlag")
    private String fleetFlag="";  //车队标志
    
    @XStreamAlias("FleetNo")
    private String fleetNo="";  //车队号
    
    @XStreamAlias("NoDamageYears")
    private String noDamageYears="";  //跨省首年投保未出险证明的年数   
    
    @XStreamAlias("Status")
    private String status="";  //机动车状态代码
    
    @XStreamAlias("ActualValue")
    private String actualValue="";  //车辆实际价值
    
    @XStreamAlias("ConsultativeValue")
    private String consultativeValue="";  //车辆协商价值

    
    @XStreamAlias("CreateBy")
    private String createBy;  //创建者
    
    @XStreamAlias("CreateTime")
    private String createTime;  //创建日期
    
    @XStreamAlias("UpdateBy")
    private String updateBy;  //更新者
    
    @XStreamAlias("UpdateTime")
    private String updateTime;  //更新日期
    
    @XStreamAlias("VehicleViolations")
    private List<VehicleViolation> vehicleViolations;  //车辆违法信息列表

    
    public String getVehicleID() {
        return vehicleID;
    }

    
    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    
    public String getLicensePlateNo() {
        return licensePlateNo;
    }

    
    public void setLicensePlateNo(String licensePlateNo) {
        this.licensePlateNo = licensePlateNo;
    }

    
    public String getLicensePlateColorCode() {
        return licensePlateColorCode;
    }

    
    public void setLicensePlateColorCode(String licensePlateColorCode) {
        this.licensePlateColorCode = licensePlateColorCode;
    }

    
    public String getLicensePlateType() {
        return licensePlateType;
    }

    
    public void setLicensePlateType(String licensePlateType) {
        this.licensePlateType = licensePlateType;
    }

    
    public String getMotorTypeCode() {
        return motorTypeCode;
    }

    
    public void setMotorTypeCode(String motorTypeCode) {
        this.motorTypeCode = motorTypeCode;
    }

    
    public String getMotorUsageTypeCode() {
        return motorUsageTypeCode;
    }

    
    public void setMotorUsageTypeCode(String motorUsageTypeCode) {
        this.motorUsageTypeCode = motorUsageTypeCode;
    }

    
    public String getFirstRegisterDate() {
        return firstRegisterDate;
    }

    
    public void setFirstRegisterDate(String firstRegisterDate) {
        this.firstRegisterDate = firstRegisterDate;
    }

    
    public String getvIN() {
        return vIN;
    }

    
    public void setvIN(String vIN) {
        this.vIN = vIN;
    }

    
    public String getEngineNo() {
        return engineNo;
    }

    
    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    
    public String getBodyColor() {
        return bodyColor;
    }

    
    public void setBodyColor(String bodyColor) {
        this.bodyColor = bodyColor;
    }

    
    public String getVehicleNature() {
        return vehicleNature;
    }

    
    public void setVehicleNature(String vehicleNature) {
        this.vehicleNature = vehicleNature;
    }

    
    public String getImportFlag() {
        return importFlag;
    }

    
    public void setImportFlag(String importFlag) {
        this.importFlag = importFlag;
    }

    
    public String getVehicleValue() {
        return vehicleValue;
    }

    
    public void setVehicleValue(String vehicleValue) {
        this.vehicleValue = vehicleValue;
    }

    
    public String getWholeWeight() {
        return wholeWeight;
    }

    
    public void setWholeWeight(String wholeWeight) {
        this.wholeWeight = wholeWeight;
    }

    
    public String getRatedPassengerCapacity() {
        return ratedPassengerCapacity;
    }

    
    public void setRatedPassengerCapacity(String ratedPassengerCapacity) {
        this.ratedPassengerCapacity = ratedPassengerCapacity;
    }

    
    public String getTonnage() {
        return tonnage;
    }

    
    public void setTonnage(String tonnage) {
        this.tonnage = tonnage;
    }

    
    public String getMadeFactory() {
        return madeFactory;
    }

    
    public void setMadeFactory(String madeFactory) {
        this.madeFactory = madeFactory;
    }

    
    public String getModel() {
        return model;
    }

    
    public void setModel(String model) {
        this.model = model;
    }

    
    public String getBrandName() {
        return brandName;
    }

    
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    
    public String getBrandCode() {
        return brandCode;
    }

    
    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    
    public String getHaulage() {
        return haulage;
    }

    
    public void setHaulage(String haulage) {
        this.haulage = haulage;
    }

    
    public String getDisplacement() {
        return displacement;
    }

    
    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    
    public String getPower() {
        return power;
    }

    
    public void setPower(String power) {
        this.power = power;
    }

    
    public String getLfDate() {
        return lfDate;
    }

    
    public void setLfDate(String lfDate) {
        this.lfDate = lfDate;
    }

    
    public String getGlassType() {
        return glassType;
    }

    
    public void setGlassType(String glassType) {
        this.glassType = glassType;
    }

    
    public String getPmQueryNo() {
        return pmQueryNo;
    }

    
    public void setPmQueryNo(String pmQueryNo) {
        this.pmQueryNo = pmQueryNo;
    }

    
    public String getVehicleType() {
        return vehicleType;
    }

    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    
    public String getIneffectualDate() {
        return ineffectualDate;
    }

    
    public void setIneffectualDate(String ineffectualDate) {
        this.ineffectualDate = ineffectualDate;
    }

    
    public String getRejectDate() {
        return rejectDate;
    }

    
    public void setRejectDate(String rejectDate) {
        this.rejectDate = rejectDate;
    }

    
    public String getLastCheckDate() {
        return lastCheckDate;
    }

    
    public void setLastCheckDate(String lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    
    public String getTransferDate() {
        return transferDate;
    }

    
    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    
    public String getNoLicenseFlag() {
        return noLicenseFlag;
    }

    
    public void setNoLicenseFlag(String noLicenseFlag) {
        this.noLicenseFlag = noLicenseFlag;
    }

    
    public String getNewVehicleFlag() {
        return newVehicleFlag;
    }

    
    public void setNewVehicleFlag(String newVehicleFlag) {
        this.newVehicleFlag = newVehicleFlag;
    }

    
    public String getChgOwnerFlag() {
        return chgOwnerFlag;
    }

    
    public void setChgOwnerFlag(String chgOwnerFlag) {
        this.chgOwnerFlag = chgOwnerFlag;
    }

    
    public String getEcdemicVehicleFlag() {
        return ecdemicVehicleFlag;
    }

    
    public void setEcdemicVehicleFlag(String ecdemicVehicleFlag) {
        this.ecdemicVehicleFlag = ecdemicVehicleFlag;
    }

    
    public String getLoanVehicleFlag() {
        return loanVehicleFlag;
    }

    
    public void setLoanVehicleFlag(String loanVehicleFlag) {
        this.loanVehicleFlag = loanVehicleFlag;
    }

    
    public String getFleetFlag() {
        return fleetFlag;
    }

    
    public void setFleetFlag(String fleetFlag) {
        this.fleetFlag = fleetFlag;
    }

    
    public String getFleetNo() {
        return fleetNo;
    }

    
    public void setFleetNo(String fleetNo) {
        this.fleetNo = fleetNo;
    }

    
    public String getNoDamageYears() {
        return noDamageYears;
    }

    
    public void setNoDamageYears(String noDamageYears) {
        this.noDamageYears = noDamageYears;
    }

    
    public String getStatus() {
        return status;
    }

    
    public void setStatus(String status) {
        this.status = status;
    }

    
    public String getActualValue() {
        return actualValue;
    }

    
    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    
    public String getConsultativeValue() {
        return consultativeValue;
    }

    
    public void setConsultativeValue(String consultativeValue) {
        this.consultativeValue = consultativeValue;
    }

    
    public String getCreateBy() {
        return createBy;
    }

    
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    
    public String getCreateTime() {
        return createTime;
    }

    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    
    public String getUpdateBy() {
        return updateBy;
    }

    
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    
    public String getUpdateTime() {
        return updateTime;
    }

    
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    
    public List<VehicleViolation> getVehicleViolations() {
        return vehicleViolations;
    }

    
    public void setVehicleViolations(List<VehicleViolation> vehicleViolations) {
        this.vehicleViolations = vehicleViolations;
    }
    
    
}
