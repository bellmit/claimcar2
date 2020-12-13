package ins.sino.claimcar.check.vo;

/**
 * Custom VO class of PO PrpLIndiQuotaInfoVo
 */
public class PrpLIndiQuotaInfoVoBase {
    /** ID */
    private Integer id;
    /** 用户代码 */
    private String userCode;
    /** 姓名 */
    private String userName;
    /** 所属机构 */
    private String comCode;
    /** 职级 */
    private String rankLevel;
    /** 月度平均工作量 */
    private String monthAverageWorkLoad;
    /** 及时查勘率 */
    private String checkRateInTime;
    /** 及时定损率 */
    private String dLossRateInTime;
    /** 万元以下案均报案支付周期 */
    private String caseAveragePaymentCycle;
    /** 一次性通过率 */
    private String oneTimePassRate;
    /** 定损偏差率 */
    private String dLossDeviationRate;
    /** 配件系统点选率 */
    private String fittingSelectRate;
    /** 万元以下车均换件数量 */
    private String componentNumAverage;
    /** 备选指标 */
    private String optionalQuota;
    /** 更新时间 */
    private String updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    public String getRankLevel() {
        return rankLevel;
    }

    public void setRankLevel(String rankLevel) {
        this.rankLevel = rankLevel;
    }

    public String getMonthAverageWorkLoad() {
        return monthAverageWorkLoad;
    }

    public void setMonthAverageWorkLoad(String monthAverageWorkLoad) {
        this.monthAverageWorkLoad = monthAverageWorkLoad;
    }

    public String getCheckRateInTime() {
        return checkRateInTime;
    }

    public void setCheckRateInTime(String checkRateInTime) {
        this.checkRateInTime = checkRateInTime;
    }

    public String getdLossRateInTime() {
        return dLossRateInTime;
    }

    public void setdLossRateInTime(String dLossRateInTime) {
        this.dLossRateInTime = dLossRateInTime;
    }

    public String getCaseAveragePaymentCycle() {
        return caseAveragePaymentCycle;
    }

    public void setCaseAveragePaymentCycle(String caseAveragePaymentCycle) {
        this.caseAveragePaymentCycle = caseAveragePaymentCycle;
    }

    public String getOneTimePassRate() {
        return oneTimePassRate;
    }

    public void setOneTimePassRate(String oneTimePassRate) {
        this.oneTimePassRate = oneTimePassRate;
    }

    public String getdLossDeviationRate() {
        return dLossDeviationRate;
    }

    public void setdLossDeviationRate(String dLossDeviationRate) {
        this.dLossDeviationRate = dLossDeviationRate;
    }

    public String getFittingSelectRate() {
        return fittingSelectRate;
    }

    public void setFittingSelectRate(String fittingSelectRate) {
        this.fittingSelectRate = fittingSelectRate;
    }

    public String getComponentNumAverage() {
        return componentNumAverage;
    }

    public void setComponentNumAverage(String componentNumAverage) {
        this.componentNumAverage = componentNumAverage;
    }

    public String getOptionalQuota() {
        return optionalQuota;
    }

    public void setOptionalQuota(String optionalQuota) {
        this.optionalQuota = optionalQuota;
    }

    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {

        this.updateTime = updateTime;
    }
}
