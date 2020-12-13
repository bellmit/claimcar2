package ins.sino.claimcar.pinganUnion.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description 描述
 * @Author liuys
 * @Date 2020/7/21 15:31
 */
public class PolicyInfoDTO implements Serializable {
    //保单险种信息
    private List<PolicyPlanDTO> policyPlanDTOList;
    //险种险别列表
    private List<PolicyDutyDTO> policyDutyDTOList;
    //保单号
    private String policyNo;
    //批单号
    private String endorseNo;
    //报案号
    private String reportNo;
    //承保机构
    private String departMentCode;
    //被保人类型 0-团体 1-个人
    private String insuredAttribute;
    //保险价值/新车购置价
    private String insuredValue;
    //被保人编码
    private String insuredCode;
    //被保险人名称
    private String insuredName;
    //被保险人手机号码
    private String insuredMobile;
    //被保人证件号码
    private String insuredCertificateNo;
    //被保人证件类型
    private String insuredCertificateType;
    //投保人名称
    private String insuranceApplyName;
    //保险起始日期
    private Date insuredBeginTime;
    //保险结束日期
    private Date insuredEndTime;
    //车损险保额
    private String insuredCarAmount;
    //保额币种
    private String insuredAmountCurrency;
    //保单属性  0-商业险 1-交强险
    private String policyAttribute;
    //保单汇率种类    (费率自由化以前的保单或不存在费率表套号的保单) 1-A套费率 2-C套费率 3-深圳用费率 4-D套费率 5-2005套费率 6-2006套费率 7-07费率
    private String rateClassFlag;
    //车牌号
    private String carMark;
    //发动机号
    private String engineNo;
    //车架号
    private String rackNo;
    //特别约定
    private String assumpsit;
    //行驶区域
    private String driveArea;
    //行驶证初次登记日期
    private Date firstRegisterTime;
    //行驶证车主
    private String licenseOwner;
    //车辆种类编码
    private String carKindCode;
    //车辆使用性质编码
    private String carUseKindCode;
    //车辆使用性质名称
    private String carUseKindName;
    //车型编码
    private String carTypeCode;
    //车型名称
    private String carTypeName;
    //车辆所属性质编码
    private String carBelongKindCode;
    //车辆所属性质名称
    private String carBelongKindName;
    //渠道编码
    private String channelCode;
    //渠道名称
    private String channelName;
    //保单确认码
    private String applyConfirmCode;
    //理赔编号(平台返回唯一案件标识)
    private String claimCode;
    //被保险人家庭电话号码
    private String insuredHomeTel;
    //被保险人工作电话号码
    private String insuredOfficeTel;
    //三者险保额
    private String insuredThirdAmount;
    //车牌号种类
    private String carMarkType;
    //结案校验码
    private String claimConfirmCode;
    //业务来源编码
    private String businessSourceCode;
    //标的车座位数
    private String seatNum;
    //投保人证件号码
    private String applicantCertificateNo;
    //行驶证车主证件号码
    private String licenseOwnerCertNo;
    //投保人证件类型
    private String applicantCertificateType;
    //行驶证车主证件类型
    private String licenseOwnerCertType;
    //保障范围
    private String guaranteeRange;
    //车辆类型
    private String vehicleType;
    //投保人个/团标识  0-团，1-个
    private String applyPersonAttribute;
    //家用商用车 0-家用，1-商用
    private String carBusinessType;
    //客户性别
    private String insuredSex;
    //结案追加码
    private String claimAddCode;
    //品牌代码简称
    private String brandCodeAbb;
    //车辆品牌
    private String brand;
    //省份    口岸1-口岸2（如:广东-深圳福田口岸-深圳罗湖口岸）
    private String exitPort;
    //国家    区域1-区域2（如:越南-北宁-河内）注:区域最多5个字段
    private String abroadDrivingArea;
    //行驶区域系数    共4档(1-（0~200公里）,2-（200~500公里）,3-（500~1000公里）,4-（1000公里以上）
    private String drivingAreaFactor;
    //共保标志  0：非共保1：共保
    private String coinsuranceMark;

    public List<PolicyPlanDTO> getPolicyPlanDTOList() {
        return policyPlanDTOList;
    }

    public void setPolicyPlanDTOList(List<PolicyPlanDTO> policyPlanDTOList) {
        this.policyPlanDTOList = policyPlanDTOList;
    }

    public List<PolicyDutyDTO> getPolicyDutyDTOList() {
        return policyDutyDTOList;
    }

    public void setPolicyDutyDTOList(List<PolicyDutyDTO> policyDutyDTOList) {
        this.policyDutyDTOList = policyDutyDTOList;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getEndorseNo() {
        return endorseNo;
    }

    public void setEndorseNo(String endorseNo) {
        this.endorseNo = endorseNo;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getDepartMentCode() {
        return departMentCode;
    }

    public void setDepartMentCode(String departMentCode) {
        this.departMentCode = departMentCode;
    }

    public String getInsuredAttribute() {
        return insuredAttribute;
    }

    public void setInsuredAttribute(String insuredAttribute) {
        this.insuredAttribute = insuredAttribute;
    }

    public String getInsuredValue() {
        return insuredValue;
    }

    public void setInsuredValue(String insuredValue) {
        this.insuredValue = insuredValue;
    }

    public String getInsuredCode() {
        return insuredCode;
    }

    public void setInsuredCode(String insuredCode) {
        this.insuredCode = insuredCode;
    }

    public String getInsuredName() {
        return insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public String getInsuredMobile() {
        return insuredMobile;
    }

    public void setInsuredMobile(String insuredMobile) {
        this.insuredMobile = insuredMobile;
    }

    public String getInsuredCertificateNo() {
        return insuredCertificateNo;
    }

    public void setInsuredCertificateNo(String insuredCertificateNo) {
        this.insuredCertificateNo = insuredCertificateNo;
    }

    public String getInsuredCertificateType() {
        return insuredCertificateType;
    }

    public void setInsuredCertificateType(String insuredCertificateType) {
        this.insuredCertificateType = insuredCertificateType;
    }

    public String getInsuranceApplyName() {
        return insuranceApplyName;
    }

    public void setInsuranceApplyName(String insuranceApplyName) {
        this.insuranceApplyName = insuranceApplyName;
    }

    public Date getInsuredBeginTime() {
        return insuredBeginTime;
    }

    public void setInsuredBeginTime(Date insuredBeginTime) {
        this.insuredBeginTime = insuredBeginTime;
    }

    public Date getInsuredEndTime() {
        return insuredEndTime;
    }

    public void setInsuredEndTime(Date insuredEndTime) {
        this.insuredEndTime = insuredEndTime;
    }

    public String getInsuredCarAmount() {
        return insuredCarAmount;
    }

    public void setInsuredCarAmount(String insuredCarAmount) {
        this.insuredCarAmount = insuredCarAmount;
    }

    public String getInsuredAmountCurrency() {
        return insuredAmountCurrency;
    }

    public void setInsuredAmountCurrency(String insuredAmountCurrency) {
        this.insuredAmountCurrency = insuredAmountCurrency;
    }

    public String getPolicyAttribute() {
        return policyAttribute;
    }

    public void setPolicyAttribute(String policyAttribute) {
        this.policyAttribute = policyAttribute;
    }

    public String getRateClassFlag() {
        return rateClassFlag;
    }

    public void setRateClassFlag(String rateClassFlag) {
        this.rateClassFlag = rateClassFlag;
    }

    public String getCarMark() {
        return carMark;
    }

    public void setCarMark(String carMark) {
        this.carMark = carMark;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getRackNo() {
        return rackNo;
    }

    public void setRackNo(String rackNo) {
        this.rackNo = rackNo;
    }

    public String getAssumpsit() {
        return assumpsit;
    }

    public void setAssumpsit(String assumpsit) {
        this.assumpsit = assumpsit;
    }

    public String getDriveArea() {
        return driveArea;
    }

    public void setDriveArea(String driveArea) {
        this.driveArea = driveArea;
    }

    public Date getFirstRegisterTime() {
        return firstRegisterTime;
    }

    public void setFirstRegisterTime(Date firstRegisterTime) {
        this.firstRegisterTime = firstRegisterTime;
    }

    public String getLicenseOwner() {
        return licenseOwner;
    }

    public void setLicenseOwner(String licenseOwner) {
        this.licenseOwner = licenseOwner;
    }

    public String getCarKindCode() {
        return carKindCode;
    }

    public void setCarKindCode(String carKindCode) {
        this.carKindCode = carKindCode;
    }

    public String getCarUseKindCode() {
        return carUseKindCode;
    }

    public void setCarUseKindCode(String carUseKindCode) {
        this.carUseKindCode = carUseKindCode;
    }

    public String getCarUseKindName() {
        return carUseKindName;
    }

    public void setCarUseKindName(String carUseKindName) {
        this.carUseKindName = carUseKindName;
    }

    public String getCarTypeCode() {
        return carTypeCode;
    }

    public void setCarTypeCode(String carTypeCode) {
        this.carTypeCode = carTypeCode;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public String getCarBelongKindCode() {
        return carBelongKindCode;
    }

    public void setCarBelongKindCode(String carBelongKindCode) {
        this.carBelongKindCode = carBelongKindCode;
    }

    public String getCarBelongKindName() {
        return carBelongKindName;
    }

    public void setCarBelongKindName(String carBelongKindName) {
        this.carBelongKindName = carBelongKindName;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getApplyConfirmCode() {
        return applyConfirmCode;
    }

    public void setApplyConfirmCode(String applyConfirmCode) {
        this.applyConfirmCode = applyConfirmCode;
    }

    public String getClaimCode() {
        return claimCode;
    }

    public void setClaimCode(String claimCode) {
        this.claimCode = claimCode;
    }

    public String getInsuredHomeTel() {
        return insuredHomeTel;
    }

    public void setInsuredHomeTel(String insuredHomeTel) {
        this.insuredHomeTel = insuredHomeTel;
    }

    public String getInsuredOfficeTel() {
        return insuredOfficeTel;
    }

    public void setInsuredOfficeTel(String insuredOfficeTel) {
        this.insuredOfficeTel = insuredOfficeTel;
    }

    public String getInsuredThirdAmount() {
        return insuredThirdAmount;
    }

    public void setInsuredThirdAmount(String insuredThirdAmount) {
        this.insuredThirdAmount = insuredThirdAmount;
    }

    public String getCarMarkType() {
        return carMarkType;
    }

    public void setCarMarkType(String carMarkType) {
        this.carMarkType = carMarkType;
    }

    public String getClaimConfirmCode() {
        return claimConfirmCode;
    }

    public void setClaimConfirmCode(String claimConfirmCode) {
        this.claimConfirmCode = claimConfirmCode;
    }

    public String getBusinessSourceCode() {
        return businessSourceCode;
    }

    public void setBusinessSourceCode(String businessSourceCode) {
        this.businessSourceCode = businessSourceCode;
    }

    public String getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(String seatNum) {
        this.seatNum = seatNum;
    }

    public String getApplicantCertificateNo() {
        return applicantCertificateNo;
    }

    public void setApplicantCertificateNo(String applicantCertificateNo) {
        this.applicantCertificateNo = applicantCertificateNo;
    }

    public String getLicenseOwnerCertNo() {
        return licenseOwnerCertNo;
    }

    public void setLicenseOwnerCertNo(String licenseOwnerCertNo) {
        this.licenseOwnerCertNo = licenseOwnerCertNo;
    }

    public String getApplicantCertificateType() {
        return applicantCertificateType;
    }

    public void setApplicantCertificateType(String applicantCertificateType) {
        this.applicantCertificateType = applicantCertificateType;
    }

    public String getLicenseOwnerCertType() {
        return licenseOwnerCertType;
    }

    public void setLicenseOwnerCertType(String licenseOwnerCertType) {
        this.licenseOwnerCertType = licenseOwnerCertType;
    }

    public String getGuaranteeRange() {
        return guaranteeRange;
    }

    public void setGuaranteeRange(String guaranteeRange) {
        this.guaranteeRange = guaranteeRange;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getApplyPersonAttribute() {
        return applyPersonAttribute;
    }

    public void setApplyPersonAttribute(String applyPersonAttribute) {
        this.applyPersonAttribute = applyPersonAttribute;
    }

    public String getCarBusinessType() {
        return carBusinessType;
    }

    public void setCarBusinessType(String carBusinessType) {
        this.carBusinessType = carBusinessType;
    }

    public String getInsuredSex() {
        return insuredSex;
    }

    public void setInsuredSex(String insuredSex) {
        this.insuredSex = insuredSex;
    }

    public String getClaimAddCode() {
        return claimAddCode;
    }

    public void setClaimAddCode(String claimAddCode) {
        this.claimAddCode = claimAddCode;
    }

    public String getBrandCodeAbb() {
        return brandCodeAbb;
    }

    public void setBrandCodeAbb(String brandCodeAbb) {
        this.brandCodeAbb = brandCodeAbb;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getExitPort() {
        return exitPort;
    }

    public void setExitPort(String exitPort) {
        this.exitPort = exitPort;
    }

    public String getAbroadDrivingArea() {
        return abroadDrivingArea;
    }

    public void setAbroadDrivingArea(String abroadDrivingArea) {
        this.abroadDrivingArea = abroadDrivingArea;
    }

    public String getDrivingAreaFactor() {
        return drivingAreaFactor;
    }

    public void setDrivingAreaFactor(String drivingAreaFactor) {
        this.drivingAreaFactor = drivingAreaFactor;
    }

    public String getCoinsuranceMark() {
        return coinsuranceMark;
    }

    public void setCoinsuranceMark(String coinsuranceMark) {
        this.coinsuranceMark = coinsuranceMark;
    }
}
