package ins.sino.claimcar.policyInfo.register.vo;

import java.io.Serializable;

public class RespVo implements Serializable{
	private static final long serialVersionUID = 1L;
    private String success;//查询成功标记
	private String errcode;//返回结果代码
	private String message;//提示信息
	private String data;//校验不通过的字段信息	当写入成功时，data为null
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	
}
