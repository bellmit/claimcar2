package ins.sino.claimcar.newpayment.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 收付支付成功回写理赔数据实体
 *
 * @author maofengning 2020年4月29日15:37:30
 */
public class BasePart implements Serializable {
    private static final long serialVersionUID = -3461271898632052355L;

    /** 业务类型 必录标识:Y 说明:Y预赔、C实赔、Z追偿 */
    private String certiType;
    /** 业务号 必录标识:Y 说明:计算书号、预赔款 */
    private String certiNo;

    /** 支付数据对象 */
    private List<PayData> payData;

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

    public List<PayData> getPayData() {
        return payData;
    }

    public void setPayData(List<PayData> payData) {
        this.payData = payData;
    }
}
