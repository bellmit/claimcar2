package ins.sino.claimcar.regist.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 保单实收收付响应数据实体
 *
 * @author maofengning
 * @date 2020/7/8 15:28
 */
public class ReturnInfo implements Serializable {

    private static final long serialVersionUID = -6317397291514637097L;

    /** 状态码 00-成功；01-失败 */
    private String responseCode;
    /** 返回信息  */
    private String errorMessage;
    /** 业务信息 收付返回的保单实收详细信息  */
    private List<PaymentInfo> paymentInfos;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<PaymentInfo> getPaymentInfos() {
        return paymentInfos;
    }

    public void setPaymentInfos(List<PaymentInfo> paymentInfos) {
        this.paymentInfos = paymentInfos;
    }
}
