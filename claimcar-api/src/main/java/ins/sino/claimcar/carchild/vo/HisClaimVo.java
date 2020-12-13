package ins.sino.claimcar.carchild.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * 历史出险信息
 * <pre></pre>
 * @author ★LinYi
 */
@XStreamAlias("HISCLAIM")
public class HisClaimVo implements Serializable{

    /**  */
    private static final long serialVersionUID = -5816517459017674693L;

    @XStreamAlias("REGISTNO")
    private String registNo;//报案号
    
    @XStreamAlias("POLICYNO")
    private String policyNo;//交强险保单号
    
    @XStreamAlias("BUSIPOLICYNO")
    private String busiPolicyNo;//商业险保单号
    
    @XStreamAlias("LICENSENO")
    private String licenseNo;//车牌号
    
    @XStreamAlias("DAMAGECODE")
    private String damageCode;//出险原因
    
    @XStreamAlias("DAMAGEDATE")
    private String damageDate;//出险时间
    
    @XStreamAlias("DAMAGEADDRESS")
    private String damageAddress;//出险地点
    
    @XStreamAlias("REPORTTIME")
    private String reportTime;//报案时间
    
    @XStreamAlias("CLOSEDATE")
    private String closeDate;//结案时间
    
    @XStreamAlias("PAYMENTAMOUNT")
    private String paymentAmount;//赔付金额
    
    @XStreamAlias("CASESTATE")
    private String caseState;//赔案状态
    
    @XStreamAlias("CASETYPE")
    private String caseType;//案件类型
    
    @XStreamAlias("COLLISIONSITE")
    private String collisionSite;//碰撞部位
    
    @XStreamAlias("ACCOUNTLIST")
    private List<AccountVo> accounts;//历史赔付记录

    
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

    
    public String getBusiPolicyNo() {
        return busiPolicyNo;
    }

    
    public void setBusiPolicyNo(String busiPolicyNo) {
        this.busiPolicyNo = busiPolicyNo;
    }

    
    public String getLicenseNo() {
        return licenseNo;
    }

    
    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    
    public String getDamageCode() {
        return damageCode;
    }

    
    public void setDamageCode(String damageCode) {
        this.damageCode = damageCode;
    }

    
    public String getDamageDate() {
        return damageDate;
    }

    
    public void setDamageDate(String damageDate) {
        this.damageDate = damageDate;
    }

    
    public String getDamageAddress() {
        return damageAddress;
    }

    
    public void setDamageAddress(String damageAddress) {
        this.damageAddress = damageAddress;
    }

    
    public String getReportTime() {
        return reportTime;
    }

    
    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    
    public String getCloseDate() {
        return closeDate;
    }

    
    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    
    public String getPaymentAmount() {
        return paymentAmount;
    }

    
    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    
    public String getCaseState() {
        return caseState;
    }

    
    public void setCaseState(String caseState) {
        this.caseState = caseState;
    }

    
    public String getCaseType() {
        return caseType;
    }

    
    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    
    public String getCollisionSite() {
        return collisionSite;
    }

    
    public void setCollisionSite(String collisionSite) {
        this.collisionSite = collisionSite;
    }


    
    public List<AccountVo> getAccounts() {
        return accounts;
    }

 
    public void setAccounts(List<AccountVo> accounts) {
        this.accounts = accounts;
    }
    
}
