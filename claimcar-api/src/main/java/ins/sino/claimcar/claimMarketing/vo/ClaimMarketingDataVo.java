package ins.sino.claimcar.claimMarketing.vo;

import java.io.Serializable;
import java.util.List;

/**
 * description: ClaimMarketingDataVo
 * date: 2020/9/25 14:51
 * author: lk
 * version: 1.0
 */
public class ClaimMarketingDataVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 年度
     */
    private String year;
    /**
     * 保费收入
     */
    private String sumPremium;
    /**
     * 已决件数
     */
    private String isDealNum;
    /**
     * 已决赔款
     */
    private String sumDealStanding;
    /**
     * 未决件数
     */
    private String outStandingNum;
    /**
     * 未决赔款
     */
    private String sumOutStanding;
    /**
     * 赔付率
     */
    private String expirationDateRate;

    /**
     * 赔案集合
     */
    private List<ClaimInfoVo> claimInfoList;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSumPremium() {
        return sumPremium;
    }

    public void setSumPremium(String sumPremium) {
        this.sumPremium = sumPremium;
    }

    public String getIsDealNum() {
        return isDealNum;
    }

    public void setIsDealNum(String isDealNum) {
        this.isDealNum = isDealNum;
    }

    public String getSumDealStanding() {
        return sumDealStanding;
    }

    public void setSumDealStanding(String sumDealStanding) {
        this.sumDealStanding = sumDealStanding;
    }

    public String getOutStandingNum() {
        return outStandingNum;
    }

    public void setOutStandingNum(String outStandingNum) {
        this.outStandingNum = outStandingNum;
    }

    public String getSumOutStanding() {
        return sumOutStanding;
    }

    public void setSumOutStanding(String sumOutStanding) {
        this.sumOutStanding = sumOutStanding;
    }

    public String getExpirationDateRate() {
        return expirationDateRate;
    }

    public void setExpirationDateRate(String expirationDateRate) {
        this.expirationDateRate = expirationDateRate;
    }

    public List<ClaimInfoVo> getClaimInfoList() {
        return claimInfoList;
    }

    public void setClaimInfoList(List<ClaimInfoVo> claimInfoList) {
        this.claimInfoList = claimInfoList;
    }
}
