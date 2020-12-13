package ins.sino.claimcar.pinganUnion.dto;

import java.io.Serializable;

/**
 * @Description 险种险别列表
 * @Author liuys
 * @Date 2020/7/21 17:28
 */
public class PolicyDutyDTO implements Serializable {
    //责任代码
    private String dutyCode;
    //赔偿限额
    private String payLimit;
    //保费
    private String premium;
    //数量
    private String quantity;
    //单个赔偿限额
    private String perLimit;
    //无责赔偿限额
    private String nodutyPayLimit;
    //赔偿限额系数    目前针对车辆重置险A、B款
    private String payLimitCoefficient;
    //绝对免陪额
    private String absNopayAmount;
    //免陪级别  1-i级别 2-ii级别 3-iii级别
    private String nopayLevel;
    //是否多次事故免陪  Y:是, N:否
    private String isMutiAccidentNopay;
    //费率
    private String premiumRate;

    public String getDutyCode() {
        return dutyCode;
    }

    public void setDutyCode(String dutyCode) {
        this.dutyCode = dutyCode;
    }

    public String getPayLimit() {
        return payLimit;
    }

    public void setPayLimit(String payLimit) {
        this.payLimit = payLimit;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPerLimit() {
        return perLimit;
    }

    public void setPerLimit(String perLimit) {
        this.perLimit = perLimit;
    }

    public String getNodutyPayLimit() {
        return nodutyPayLimit;
    }

    public void setNodutyPayLimit(String nodutyPayLimit) {
        this.nodutyPayLimit = nodutyPayLimit;
    }

    public String getPayLimitCoefficient() {
        return payLimitCoefficient;
    }

    public void setPayLimitCoefficient(String payLimitCoefficient) {
        this.payLimitCoefficient = payLimitCoefficient;
    }

    public String getAbsNopayAmount() {
        return absNopayAmount;
    }

    public void setAbsNopayAmount(String absNopayAmount) {
        this.absNopayAmount = absNopayAmount;
    }

    public String getNopayLevel() {
        return nopayLevel;
    }

    public void setNopayLevel(String nopayLevel) {
        this.nopayLevel = nopayLevel;
    }

    public String getIsMutiAccidentNopay() {
        return isMutiAccidentNopay;
    }

    public void setIsMutiAccidentNopay(String isMutiAccidentNopay) {
        this.isMutiAccidentNopay = isMutiAccidentNopay;
    }

    public String getPremiumRate() {
        return premiumRate;
    }

    public void setPremiumRate(String premiumRate) {
        this.premiumRate = premiumRate;
    }
}
