package ins.sino.claimcar.claimjy.vo.repair;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("FactoryInfo")
public class FactoryInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @XStreamAlias("FactoryId")//主键，修理厂唯一标识
    private String factoryId = "";
    @XStreamAlias("ComCode")//分公司编码
    private String comCode = "";
    @XStreamAlias("ComName")//分公司名称
    private String comName = "";
    @XStreamAlias("CityCode")//中支公司编码
    private String cityCode = "";
    @XStreamAlias("CityName")//中支公司名称
    private String cityName = "";
    @XStreamAlias("FactoryCode")//修理厂代码
    private String factoryCode = "";
    @XStreamAlias("FactoryName")//修理厂名称
    private String factoryName = "";
    @XStreamAlias("RankCode")//修理厂简称
    private String rankCode = "";
    @XStreamAlias("FactoryQualification")//修理厂资质
    private String factoryQualification = "";
    @XStreamAlias("FactoryType")//修理厂类型
    private String factoryType = "";
    @XStreamAlias("CooperateType")//修理厂合作类型
    private String cooperateType = "";
    @XStreamAlias("LinkMan")//联系人
    private String linkMan = "";
    @XStreamAlias("Mobile")//手机
    private String mobile = "";
    @XStreamAlias("TelNo")//座机
    private String telNo = "";
    @XStreamAlias("Address")//地址
    private String address = "";
    @XStreamAlias("ValidStatus")//有效标志
    private String validStatus = "";
    @XStreamAlias("ValidBeginDate")//有效开始日期
    private String validBeginDate = "";
    @XStreamAlias("ValidEndDate")//有效终止日期
    private String validEndDate = "";
    @XStreamAlias("CooperateFactory")//是否合作车商
    private String cooperateFactory = "";
    @XStreamAlias("BlacklistFlag")//是否黑名单
    private String blacklistFlag = "";
    @XStreamAlias("Remark")//备注
    private String remark = "";
    @XStreamAlias("Status")//状态
    private String status = "";
    @XStreamAlias("FitsDiscountRate")//换件折扣
    private String fitsDiscountRate = "";
    @XStreamAlias("RepairDiscountRate")//工时折扣
    private String repairDiscountRate = "";
    @XStreamAlias("AssemblyFacManHour")//拆装工时单价
    private String assemblyFacManHour = "";
    @XStreamAlias("PaintFacManHour")//喷漆工时单价
    private String paintFacManHour = "";
    @XStreamAlias("RepairFacManHour")//修理工时单价
    private String repairFacManHour = "";
    /**
     * 修理厂所在省编码  必传
     */
    @XStreamAlias("ProvinceCode")
    private String provinceCode = "";

    /**
     * 修理厂所在市编码  必传
     */
    @XStreamAlias("City")
    private String city = "";

    /**
     * 是否外修修理厂  1：是   0：否
     */
    @XStreamAlias("OuterFactory")
    private String outerFactory = "";

    /**
     * 价格方案模式 必传  1-标准模式 2-标记品牌模式 3-承修品牌模式 4-所有品牌模式 5-4S店全品牌模式
     */
    @XStreamAlias("PriceSchemaMode")
    private String priceSchemaMode = "";

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPriceSchemaMode() {
        return priceSchemaMode;
    }

    public void setPriceSchemaMode(String priceSchemaMode) {
        this.priceSchemaMode = priceSchemaMode;
    }

    public String getOuterFactory() {
        return outerFactory;
    }

    public void setOuterFactory(String outerFactory) {
        this.outerFactory = outerFactory;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getRankCode() {
        return rankCode;
    }

    public void setRankCode(String rankCode) {
        this.rankCode = rankCode;
    }

    public String getFactoryQualification() {
        return factoryQualification;
    }

    public void setFactoryQualification(String factoryQualification) {
        this.factoryQualification = factoryQualification;
    }

    public String getFactoryType() {
        return factoryType;
    }

    public void setFactoryType(String factoryType) {
        this.factoryType = factoryType;
    }

    public String getCooperateType() {
        return cooperateType;
    }

    public void setCooperateType(String cooperateType) {
        this.cooperateType = cooperateType;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(String validStatus) {
        this.validStatus = validStatus;
    }

    public String getValidBeginDate() {
        return validBeginDate;
    }

    public void setValidBeginDate(String validBeginDate) {
        this.validBeginDate = validBeginDate;
    }

    public String getValidEndDate() {
        return validEndDate;
    }

    public void setValidEndDate(String validEndDate) {
        this.validEndDate = validEndDate;
    }

    public String getCooperateFactory() {
        return cooperateFactory;
    }

    public void setCooperateFactory(String cooperateFactory) {
        this.cooperateFactory = cooperateFactory;
    }

    public String getBlacklistFlag() {
        return blacklistFlag;
    }

    public void setBlacklistFlag(String blacklistFlag) {
        this.blacklistFlag = blacklistFlag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFitsDiscountRate() {
        return fitsDiscountRate;
    }

    public void setFitsDiscountRate(String fitsDiscountRate) {
        this.fitsDiscountRate = fitsDiscountRate;
    }

    public String getRepairDiscountRate() {
        return repairDiscountRate;
    }

    public void setRepairDiscountRate(String repairDiscountRate) {
        this.repairDiscountRate = repairDiscountRate;
    }

    public String getAssemblyFacManHour() {
        return assemblyFacManHour;
    }

    public void setAssemblyFacManHour(String assemblyFacManHour) {
        this.assemblyFacManHour = assemblyFacManHour;
    }

    public String getPaintFacManHour() {
        return paintFacManHour;
    }

    public void setPaintFacManHour(String paintFacManHour) {
        this.paintFacManHour = paintFacManHour;
    }

    public String getRepairFacManHour() {
        return repairFacManHour;
    }

    public void setRepairFacManHour(String repairFacManHour) {
        this.repairFacManHour = repairFacManHour;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
