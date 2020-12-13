package ins.sino.claimcar.carchildCommon.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("HEAD")
public class CarchildResponseHeadVo implements Serializable{

    /**  */
    private static final long serialVersionUID = 2117070256643736363L;

    @XStreamAlias("RESPONSETYPE")
    private String responseType;
    
    @XStreamAlias("ERRNO")
    private String errNo;
    
    @XStreamAlias("ERRMSG")
    private String errMsg;

    
    public String getResponseType() {
        return responseType;
    }

    
    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }
    
    
    public String getErrNo() {
        return errNo;
    }


    
    public void setErrNo(String errNo) {
        this.errNo = errNo;
    }


    public String getErrMsg() {
        return errMsg;
    }

    
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
    
    
}
