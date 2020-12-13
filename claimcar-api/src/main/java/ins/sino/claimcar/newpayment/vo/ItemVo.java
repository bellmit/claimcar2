package ins.sino.claimcar.newpayment.vo;

import java.io.Serializable;

/**
 * 公估费计算书明细数据
 *
 * @author maofengning
 * @date 2020/5/2 17:23
 */
public class ItemVo implements Serializable {

    private static final long serialVersionUID = 3964954779810418044L;

    /** 计算书号 必录标识:Y 说明: */
    private String certiNo;
    /** 收付原因 必录标识:Y 说明: */
    private String payRefReason;
    /** 序列号 必录标识:N 说明: */
    private Integer serialNo;

    public String getCertiNo() {
        return certiNo;
    }

    public void setCertiNo(String certiNo) {
        this.certiNo = certiNo;
    }

    public String getPayRefReason() {
        return payRefReason;
    }

    public void setPayRefReason(String payRefReason) {
        this.payRefReason = payRefReason;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

}
