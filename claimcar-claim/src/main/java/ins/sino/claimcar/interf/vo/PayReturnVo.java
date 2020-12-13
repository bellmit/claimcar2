/******************************************************************************
* CREATETIME : 2016年8月10日 下午5:37:49
******************************************************************************/
package ins.sino.claimcar.interf.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;


/**
 * @author ★XMSH
 */
@XStreamAlias("PayReturn")
public class PayReturnVo implements Serializable {

	private static final long serialVersionUID = 1L;
	/** true 成功 false 失败 **/
	@XStreamAlias("ResponseCode")
	@XStreamConverter(value = BooleanConverter.class, booleans = {false}, strings = {"1","0"})
	private boolean responseCode;
	/** 错误说明,如果成功返回空 **/
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
