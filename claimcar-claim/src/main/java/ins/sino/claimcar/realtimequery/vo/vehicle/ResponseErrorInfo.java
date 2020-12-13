package ins.sino.claimcar.realtimequery.vo.vehicle;

import java.io.Serializable;

public class ResponseErrorInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;  //异常应答码
	private String message;  //状态信息
	private String solution;  //异常提示
	private String subError;  //异常描述
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSolution() {
		return solution;
	}
	public void setSolution(String solution) {
		this.solution = solution;
	}
	public String getSubError() {
		return subError;
	}
	public void setSubError(String subError) {
		this.subError = subError;
	}
	
	
	
}
