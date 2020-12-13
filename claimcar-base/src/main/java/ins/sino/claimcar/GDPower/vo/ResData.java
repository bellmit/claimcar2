package ins.sino.claimcar.GDPower.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author: Gusheng Huang
 * @Date: Create in 上午10:29 19-2-21
 * @Modified By:
 */

@XStreamAlias("Data")
public class ResData {
    @XStreamAlias("CAR_NUM")
    private String carNum;//车牌号
    @XStreamAlias("BE_THREATENED_TIME")
    private String beThreatenedTime;//出险时间
    @XStreamAlias("BE_THREATENED_ADDRESS")
    private String beThreatenedAddress;//出险地点
    @XStreamAlias("BE_THREATENED_REASON")
    private String beThreatenedReason;//出险原因
    @XStreamAlias("REPORT_TIME")
    private String reportTime;//报案时间
    @XStreamAlias("CASE_NUMBER")
    private String caseNumber;//立案号(理赔唯一案件号)
    @XStreamAlias("CASE_TIME")
    private String caseTime;//立案时间
    @XStreamAlias("CLOSING_TIME")
    private String closingTime;//结案时间
    @XStreamAlias("CLAIM_TYPE")
    private String claimType;//理赔类型
    @XStreamAlias("FACTORY")
    private String factory;//维修厂名称
    @XStreamAlias("CLAIM_AMOUNT")
    private BigDecimal claimAmount;//理赔金额

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getBeThreatenedTime() {
        return beThreatenedTime;
    }

    public void setBeThreatenedTime(String beThreatenedTime) {
        this.beThreatenedTime = beThreatenedTime;
    }

    public String getBeThreatenedAddress() {
        return beThreatenedAddress;
    }

    public void setBeThreatenedAddress(String beThreatenedAddress) {
        this.beThreatenedAddress = beThreatenedAddress;
    }

    public String getBeThreatenedReason() {
        return beThreatenedReason;
    }

    public void setBeThreatenedReason(String beThreatenedReason) {
        this.beThreatenedReason = beThreatenedReason;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getCaseTime() {
        return caseTime;
    }

    public void setCaseTime(String caseTime) {
        this.caseTime = caseTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }
}
