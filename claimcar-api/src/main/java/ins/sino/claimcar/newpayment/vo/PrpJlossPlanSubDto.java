package ins.sino.claimcar.newpayment.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 理赔应收应付送收付子类 拆分到险别信息
 *
 * @author maofengning 2020年4月28日16:21:00
 */
public class PrpJlossPlanSubDto implements Serializable {

    private static final long serialVersionUID = 734755865769786338L;

    /** 业务类型 必录标识:Y 说明:见码表4.1.5 */
    private String certiType;
    /** 业务号 必录标识:Y 说明:计算书号，预赔号 */
    private String certiNo;
    /** 保单号 必录标识:Y 说明: */
    private String policyNo;
    /** 序号 必录标识:Y 说明: */
    private Integer serialNo;
    /** 赔付类型 必录标识:Y 说明:见码表4.1.2 */
    private String lossType;
    /** 退回重付标识 必录标识:N 说明:1-是；0-否 */
    private String repayType;
    /** 退回重付关联ID 必录标识:N 说明: */
    private String correlateID;
    /** 险类 必录标识:Y 说明: */
    private String classCode;
    /** 险种 必录标识:Y 说明: */
    private String riskCode;
    /** 条款代码 必录标识:Y 说明: */
    private String kindCode;
    /** 收付金额 必录标识:Y 说明: */
    private BigDecimal planFee;
    /** 收付原因 必录标识:Y 说明：见码表4.1.1 */
    private String payRefReason;
    /** 费用类型-新收付提供的接口文档命名 */
    private String chargeCode;
    /** 缴税状态  1-应税 2-免税 */
    private String isTaxable;

    public String getChargeCode() {
        return chargeCode;
    }

    public void setChargeCode(String chargeCode) {
        this.chargeCode = chargeCode;
    }

    public String getCertiType() {
        return certiType;
    }

    public void setCertiType(String certiType) {
        this.certiType = certiType;
    }

    public String getCertiNo() {
        return certiNo;
    }

    public void setCertiNo(String certiNo) {
        this.certiNo = certiNo;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getLossType() {
        return lossType;
    }

    public void setLossType(String lossType) {
        this.lossType = lossType;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getCorrelateID() {
        return correlateID;
    }

    public void setCorrelateID(String correlateID) {
        this.correlateID = correlateID;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getRiskCode() {
        return riskCode;
    }

    public void setRiskCode(String riskCode) {
        this.riskCode = riskCode;
    }

    public String getKindCode() {
        return kindCode;
    }

    public void setKindCode(String kindCode) {
        this.kindCode = kindCode;
    }

    public BigDecimal getPlanFee() {
        return planFee;
    }

    public void setPlanFee(BigDecimal planFee) {
        this.planFee = planFee;
    }

    public String getPayRefReason() {
        return payRefReason;
    }

    public void setPayRefReason(String payRefReason) {
        this.payRefReason = payRefReason;
    }

    public String getIsTaxable() {
        return isTaxable;
    }

    public void setIsTaxable(String isTaxable) {
        this.isTaxable = isTaxable;
    }
}
