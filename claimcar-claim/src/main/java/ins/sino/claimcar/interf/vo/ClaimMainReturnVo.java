package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;

@XStreamAlias("ClaimReturn")
public class ClaimMainReturnVo {

    /** true �ɹ� false ʧ�� **/
    @XStreamAlias("ResponseCode")
    @XStreamConverter(value = BooleanConverter.class, booleans = {false}, strings = {"1","0"})
    private boolean responseCode;
    /** ����˵��,���ɹ����ؿ� **/
    @XStreamAlias("ErrorMessage")
    private String errorMessage;
    
    public boolean isResponseCode() {
        return responseCode;
    }
    
    public void setResponseCode(boolean responseCode) {
        this.responseCode = responseCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
