package ins.sino.claimcar.carchild.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("POLICYINFO")
public class PolicyInfoVo implements Serializable {

    /**  */
    private static final long serialVersionUID = 8862342024948686420L;
    
    
    @XStreamAlias("REGISTNO")
    private String registNo;//报案号
    
    @XStreamAlias("POLICYNO")
    private String policyNo;//保单号
    
    @XStreamAlias("LICENSENO")
    private String licenseNo;//车牌号
    
    @XStreamAlias("FRAMENO")
    private String frameNo;//车架号
    
    @XStreamAlias("ENGINENO")
    private String engineNo;//发动机号
    
    @XStreamAlias("MODELCODE")
    private String modelCode;//车型
    
    @XStreamAlias("INSUREDNAME")
    private String insuredName;//被保险人名称
    
    @XStreamAlias("STARTDATE")
    private String startDate;//起保日期
    
    @XStreamAlias("ENDDATE")
    private String endDate;//终保日期
    
    @XStreamAlias("CLAIMTYPE")
    private String claimType;//保单类别
    
    @XStreamAlias("ISVIP")
    private String isVip;//是否VIP
    
    @XStreamAlias("DAMAGECOUNT")
    private String damageCount;//历次出险总次数
    
    @XStreamAlias("COMCODE")
    private String comCode;//机构
    
    @XStreamAlias("KINDLIST")
    private List<KindInfoVo> kindInfos;//险别信息
    
    @XStreamAlias("ENGAGELIST")
    private List<EngageInfoVo> engageInfos;//特别约定
    
    @XStreamAlias("ENDORLIST")
    private List<EndorInfoVo> endorInfos;//批单信息
    


    
    public String getRegistNo() {
        return registNo;
    }

    
    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }

    
    public String getPolicyNo() {
        return policyNo;
    }

    
    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    
    public String getLicenseNo() {
        return licenseNo;
    }

    
    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    
    public String getFrameNo() {
        return frameNo;
    }

    
    public void setFrameNo(String frameNo) {
        this.frameNo = frameNo;
    }

    
    public String getEngineNo() {
        return engineNo;
    }

    
    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    
    public String getModelCode() {
        return modelCode;
    }

    
    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    
    public String getInsuredName() {
        return insuredName;
    }

    
    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    
    public String getStartDate() {
        return startDate;
    }

    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    
    public String getEndDate() {
        return endDate;
    }

    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    
    public String getClaimType() {
        return claimType;
    }

    
    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    
    public String getIsVip() {
        return isVip;
    }

    
    public void setIsVip(String isVip) {
        this.isVip = isVip;
    }

    
    public String getDamageCount() {
        return damageCount;
    }

    
    public void setDamageCount(String damageCount) {
        this.damageCount = damageCount;
    }

    
    
    public String getComCode() {
        return comCode;
    }


    
    public void setComCode(String comCode) {
        this.comCode = comCode;
    }


    public List<KindInfoVo> getKindInfos() {
        return kindInfos;
    }

    
    public void setKindInfos(List<KindInfoVo> kindInfos) {
        this.kindInfos = kindInfos;
    }

    
    public List<EngageInfoVo> getEngageInfos() {
        return engageInfos;
    }

    
    public void setEngageInfos(List<EngageInfoVo> engageInfos) {
        this.engageInfos = engageInfos;
    }

    
    public List<EndorInfoVo> getEndorInfos() {
        return endorInfos;
    }

    
    public void setEndorInfos(List<EndorInfoVo> endorInfos) {
        this.endorInfos = endorInfos;
    }

    
}
