package ins.sino.claimcar.newpayment.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * 理赔响应新收付报文对象
 *
 * @author maofengning
 * @date 2020/5/11 11:00
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDto implements Serializable {

    private static final long serialVersionUID = -6707862666244874565L;

    /** 状态码 必录标识:Y 说明:00：成功，01：失败 */
    private String responseCode  ;
    /** 返回信息 必录标识:Y 说明: */
    private String errorMessage ;
    /** 自动推送到理算资金的时候必传*/
    private List<PayRefNoDto> payRefNos;

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

    public List<PayRefNoDto> getPayRefNos() {
        return payRefNos;
    }

    public void setPayRefNos(List<PayRefNoDto> payRefNos) {
        this.payRefNos = payRefNos;
    }
}
