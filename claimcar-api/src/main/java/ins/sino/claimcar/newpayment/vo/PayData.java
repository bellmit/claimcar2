package ins.sino.claimcar.newpayment.vo;

import java.io.Serializable;

/**
 * 支付数据对象明细
 *
 * @author maofengning
 * @date 2020-04-29 16:04:02
 */
public class PayData implements Serializable {

    private static final long serialVersionUID = -3891977260894959169L;

    /** 序列号 必录标识:Y 说明: */
    private Integer serialNo;
    /** 支付时间  必录标识:Y 说明: yyyy-MM-dd HH:mm:ss */
    private String payTime;
    /** 收付原因 见PaymentConstants */
    private String payRefReason;

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayRefReason() {
        return payRefReason;
    }

    public void setPayRefReason(String payRefReason) {
        this.payRefReason = payRefReason;
    }
}
