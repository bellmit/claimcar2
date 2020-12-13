package ins.sino.claimcar.genilex.vo.scoreVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ResponseResult")
public class ResponseResult implements Serializable {

	/**  */
	private static final long serialVersionUID = 6606450331852430142L;
	
    @XStreamAlias("ResultCode") 
    private String resultCode;
    
    @XStreamAlias("ResultDesc") 
    private String resultDesc;

    
    public String getResultCode() {
        return resultCode;
    }

    
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    
    public String getResultDesc() {
        return resultDesc;
    }

    
    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }
    
   
    
}
