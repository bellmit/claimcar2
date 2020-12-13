package ins.sino.claimcar.newpayment.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 价税明细数据
 *
 * @author maofengning
 * @date 2020/5/19 19:48
 */
public class BasePartDetail implements Serializable {

    private static final long serialVersionUID = 1537189310000911047L;

    /** 业务号 必录标识:Y 说明: */
    private String certiNo;
    /** 序号 必录标识:Y 说明: */
    private Integer serialNo;
    /** 发票类型 必录标识:Y 说明: */
    private String invoicetype;
    /** 不含税金额 必录标识:Y 说明: */
    private BigDecimal sumAmountNT;
    /** 税率 必录标识:Y 说明: */
    private BigDecimal taxRate;
    /** 总税额 必录标识:Y 说明: */
    private BigDecimal sumAmountTax;
    /** 收付原因 必录标识:N 说明: */
    private String payRefReason;

    public String getCertiNo() {
        return certiNo;
    }

    public void setCertiNo(String certiNo) {
        this.certiNo = certiNo;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getInvoicetype() {
        return invoicetype;
    }

    public void setInvoicetype(String invoicetype) {
        this.invoicetype = invoicetype;
    }

    public BigDecimal getSumAmountNT() {
        return sumAmountNT;
    }

    public void setSumAmountNT(BigDecimal sumAmountNT) {
        this.sumAmountNT = sumAmountNT;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getSumAmountTax() {
        return sumAmountTax;
    }

    public void setSumAmountTax(BigDecimal sumAmountTax) {
        this.sumAmountTax = sumAmountTax;
    }

    public String getPayRefReason() {
        return payRefReason;
    }

    public void setPayRefReason(String payRefReason) {
        this.payRefReason = payRefReason;
    }
}
