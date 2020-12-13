package com.sinosoft.sff.interf;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
/**
 * 返回信息
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2016年7月18日
 * @since (2016年7月18日 上午9:26:05): <br>
 */
@XStreamAlias("JPlanReturn")
public class JPlanReturnVo implements Serializable {

	private static final long serialVersionUID = 1L;
	/** true 成功 false 失败 **/
	@XStreamAlias("ResponseCode")
	@XStreamConverter(value = BooleanConverter.class, booleans = {false}, strings = {"1","0"})
	private boolean responseCode;
	/** 错误说明,如果成功返回空 **/
	@XStreamAlias("ErrorMessage")
	private String errorMessage;
	@XStreamAlias("accountNo")
	private String accountNo;

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

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

}
