package ins.sino.claimcar.genilex.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CoverageItem")
public class CoverageItem implements Serializable {

	/**被保险人信息  */
	private static final long serialVersionUID = 6606450331852430142L;
	
	@XStreamAlias("PlanCode")
	private String planCode="";  //产品代码
	
    @XStreamAlias("CoverageCode")
    private String coverageCode="";  //险种代码
    
    @XStreamAlias("MainRiderFlag")
    private String mainRiderFlag="";  //主附险标识
    
    @XStreamAlias("SumLimit")
    private String sumLimit="";  //保额/限额
    
    @XStreamAlias("DiscountValue")
    private String discountValue="";  //折扣系数
    
    @XStreamAlias("BasicPremium")
    private String basicPremium="";  //基础(表定)保费
    
    @XStreamAlias("StandardPremuim")
    private String standardPremuim="";  //标准(应缴)保费
    
    @XStreamAlias("Premuim")
    private String premuim="";  //签单(实收)保费
	   
    @XStreamAlias("UnderInsuranceFlag")
    private String underInsuranceFlag="";  //不足额投保标志
    
    @XStreamAlias("LimitAmount")
    private String limitAmount="";  //赔偿限额
    
    @XStreamAlias("StartDate")
    private String startDate="";  //保险起期
    
    @XStreamAlias("EndDate")
    private String endDate="";  //保险止期
    
    @XStreamAlias("Discount")
    private String discount="";  //赔款优待
    
    @XStreamAlias("ConfirmTime")
    private String confirmTime="";  //投保确认时间
    
    @XStreamAlias("AccdntDeductibleRate")
    private String accdntDeductibleRate="";  //事故责任免赔率描述
    
    @XStreamAlias("StraightDeductibleRate")
    private String straightDeductibleRate="";  //绝对免赔率描述
    
    @XStreamAlias("StraightDeductible")
    private String straightDeductible="";  //绝对免赔额
    
    @XStreamAlias("AppSit")
    private String appSit="";  //约定座位数
    
    @XStreamAlias("CompensationFreeExcepted")
    private String compensationFreeExcepted="";  //是否投保不计免赔
    
    @XStreamAlias("Currency")
    private String currency="";  //币种类别代码
    
    @XStreamAlias("CancleDate")
    private String cancleDate="";  //实际终止日期
    
    @XStreamAlias("CancleReason")
    private String cancleReason="";  //终止原因

    @XStreamAlias("CancleReasonMemo")
    private String cancleReasonMemo="";  //终止原因描述
    
    @XStreamAlias("CreateBy")
    private String createBy="";  //创建者
    
    @XStreamAlias("CreateTime")
    private String createTime="";  //创建日期
    
    @XStreamAlias("UpdateBy")
    private String updateBy="";  //更新者
    
    @XStreamAlias("UpdateTime")
    private String updateTime="";  //更新日期

    
    public String getPlanCode() {
        return planCode;
    }

    
    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    
    public String getCoverageCode() {
        return coverageCode;
    }

    
    public void setCoverageCode(String coverageCode) {
        this.coverageCode = coverageCode;
    }

    
    public String getMainRiderFlag() {
        return mainRiderFlag;
    }

    
    public void setMainRiderFlag(String mainRiderFlag) {
        this.mainRiderFlag = mainRiderFlag;
    }

    
    public String getSumLimit() {
        return sumLimit;
    }

    
    public void setSumLimit(String sumLimit) {
        this.sumLimit = sumLimit;
    }

    
    public String getDiscountValue() {
        return discountValue;
    }

    
    public void setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
    }

    
    public String getBasicPremium() {
        return basicPremium;
    }

    
    public void setBasicPremium(String basicPremium) {
        this.basicPremium = basicPremium;
    }

    
    public String getStandardPremuim() {
        return standardPremuim;
    }

    
    public void setStandardPremuim(String standardPremuim) {
        this.standardPremuim = standardPremuim;
    }

    
    public String getPremuim() {
        return premuim;
    }

    
    public void setPremuim(String premuim) {
        this.premuim = premuim;
    }

    
    public String getUnderInsuranceFlag() {
        return underInsuranceFlag;
    }

    
    public void setUnderInsuranceFlag(String underInsuranceFlag) {
        this.underInsuranceFlag = underInsuranceFlag;
    }

    
    public String getLimitAmount() {
        return limitAmount;
    }

    
    public void setLimitAmount(String limitAmount) {
        this.limitAmount = limitAmount;
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

    
    public String getDiscount() {
        return discount;
    }

    
    public void setDiscount(String discount) {
        this.discount = discount;
    }

    
    public String getConfirmTime() {
        return confirmTime;
    }

    
    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    
    public String getAccdntDeductibleRate() {
        return accdntDeductibleRate;
    }

    
    public void setAccdntDeductibleRate(String accdntDeductibleRate) {
        this.accdntDeductibleRate = accdntDeductibleRate;
    }

    
    public String getStraightDeductibleRate() {
        return straightDeductibleRate;
    }

    
    public void setStraightDeductibleRate(String straightDeductibleRate) {
        this.straightDeductibleRate = straightDeductibleRate;
    }

    
    public String getStraightDeductible() {
        return straightDeductible;
    }

    
    public void setStraightDeductible(String straightDeductible) {
        this.straightDeductible = straightDeductible;
    }

    
    public String getAppSit() {
        return appSit;
    }

    
    public void setAppSit(String appSit) {
        this.appSit = appSit;
    }

    
    public String getCompensationFreeExcepted() {
        return compensationFreeExcepted;
    }

    
    public void setCompensationFreeExcepted(String compensationFreeExcepted) {
        this.compensationFreeExcepted = compensationFreeExcepted;
    }

    
    public String getCurrency() {
        return currency;
    }

    
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    
    public String getCancleDate() {
        return cancleDate;
    }

    
    public void setCancleDate(String cancleDate) {
        this.cancleDate = cancleDate;
    }

    
    public String getCancleReason() {
        return cancleReason;
    }

    
    public void setCancleReason(String cancleReason) {
        this.cancleReason = cancleReason;
    }

    
    public String getCancleReasonMemo() {
        return cancleReasonMemo;
    }

    
    public void setCancleReasonMemo(String cancleReasonMemo) {
        this.cancleReasonMemo = cancleReasonMemo;
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

    
 
}
