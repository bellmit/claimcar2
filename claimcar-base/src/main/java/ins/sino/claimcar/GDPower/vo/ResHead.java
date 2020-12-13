package ins.sino.claimcar.GDPower.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @Description:
 * @Author: Gusheng Huang
 * @Date: Create in 上午10:09 19-2-21
 * @Modified By:
 */
@XStreamAlias("Head")
public class ResHead {
    @XStreamAlias("ResponseType")
    private String responseType;
    @XStreamAlias("ResponseCode")
    private String responseCode;
    @XStreamAlias("ErrorMessage")
    private String errorMessage;

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

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
}
