package ins.sino.claimcar.regist.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 保单实付详情实体
 *
 * @author maofengning
 * @date 2020/7/8 15:25
 */
public class PaymentInfo implements Serializable {

    private static final long serialVersionUID = 5490271759022609348L;

    /** 业务类型 见码表4.1.5 */
    private String certiType;
    /** 业务号 投保单号码/保单号码/批单号码/计算书号 */
    private String certiNo;
    /** 保单号  */
    private String policyNo;
    /** 序号  */
    private String serialNo;
    /** 分期缴费日期  */
    private String planDate;
    /** 实收付金额  */
    private BigDecimal realPayRefFee;
    /** 是否实收 1-已实收，0-未实收 */
    private String payRefFlag;
    /** 实收实付日期  */
    private String payrefDate;
    /** 缴费期次  */
    private Integer planSerialNo;
    /** 缴费期数  */
    private Integer payNo;

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

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    public BigDecimal getRealPayRefFee() {
        return realPayRefFee;
    }

    public void setRealPayRefFee(BigDecimal realPayRefFee) {
        this.realPayRefFee = realPayRefFee;
    }

    public String getPayRefFlag() {
        return payRefFlag;
    }

    public void setPayRefFlag(String payRefFlag) {
        this.payRefFlag = payRefFlag;
    }

    public String getPayrefDate() {
        return payrefDate;
    }

    public void setPayrefDate(String payrefDate) {
        this.payrefDate = payrefDate;
    }

    public Integer getPlanSerialNo() {
        return planSerialNo;
    }

    public void setPlanSerialNo(Integer planSerialNo) {
        this.planSerialNo = planSerialNo;
    }

    public Integer getPayNo() {
        return payNo;
    }

    public void setPayNo(Integer payNo) {
        this.payNo = payNo;
    }
}
