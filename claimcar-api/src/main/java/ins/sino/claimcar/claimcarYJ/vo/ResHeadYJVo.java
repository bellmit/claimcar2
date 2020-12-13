package ins.sino.claimcar.claimcarYJ.vo;

import com.alibaba.dubbo.common.json.JSONObject;

public class ResHeadYJVo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
    private String success;//值为true和false，为true是业务处理成功（不是请求成功），false表示业务处理失败
	private String error;//错误码，000000表示成功
	private String msg;//错误信息
	private String total;//data数据的条数
	private JSONObject data;//返回数据集
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public JSONObject getData() {
		return data;
	}
	public void setData(JSONObject data) {
		this.data = data;
	}
	
}
