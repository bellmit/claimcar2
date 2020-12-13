package ins.sino.claimcar.interf.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
@XStreamAlias("BaseReturn")
public class PolicySumClaimReturnVo implements Serializable {

	private static final long serialVersionUID = 1L;
	/** true 成功 false 失败 **/
	@XStreamAlias("ResponseCode")
	@XStreamConverter(value = BooleanConverter.class, booleans = {false}, strings = {"1","0"})
	private boolean responseCode;
	/** 错误说明,如果成功返回空 **/
	@XStreamAlias("ErrorMessage")
	private String errorMessage;
	
	@XStreamAlias("ClaimFeeVo")
	private ClaimFeeVo claimFeeVo;

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

	public ClaimFeeVo getClaimFeeVo() {
		return claimFeeVo;
	}

	public void setClaimFeeVo(ClaimFeeVo claimFeeVo) {
		this.claimFeeVo = claimFeeVo;
	}
	
	
	
}
