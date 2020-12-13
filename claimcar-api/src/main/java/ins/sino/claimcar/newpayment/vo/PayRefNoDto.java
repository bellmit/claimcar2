package ins.sino.claimcar.newpayment.vo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 收付自动回写结算单接收类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayRefNoDto{

    /** 业务号*/
    private String certiNo;
    /** 序列号*/
    private String serialNo;
    /** 收付原因*/
    private String payRefReason;
    /** 结算单号*/
    private String payRefNo;

    public String getCertiNo() {
        return certiNo;
    }

    public void setCertiNo(String certiNo) {
        this.certiNo = certiNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getPayRefReason() {
        return payRefReason;
    }

    public void setPayRefReason(String payRefReason) {
        this.payRefReason = payRefReason;
    }

    public String getPayRefNo() {
        return payRefNo;
    }

    public void setPayRefNo(String payRefNo) {
        this.payRefNo = payRefNo;
    }
}
