package ins.sino.claimcar.genilex.vo.scoreVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ResponseSummary")
public class ResponseSummary implements Serializable {

	/**  */
	private static final long serialVersionUID = 6606450331852430142L;
	
    @XStreamAlias("AccountNumberStatus") 
    private AccountNumberStatus accountNumberStatus;
    
    @XStreamAlias("ResponseResult") 
    private ResponseResult responseResult;

    
    public AccountNumberStatus getAccountNumberStatus() {
        return accountNumberStatus;
    }

    
    public void setAccountNumberStatus(AccountNumberStatus accountNumberStatus) {
        this.accountNumberStatus = accountNumberStatus;
    }

    
    public ResponseResult getResponseResult() {
        return responseResult;
    }

    
    public void setResponseResult(ResponseResult responseResult) {
        this.responseResult = responseResult;
    }
    
    
}
