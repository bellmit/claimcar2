package ins.sino.claim.powerGridCarClaimLog.vo;

public class GDPowerGridCarClaimMessageVo {

	private String code;//状态
	private int count;//数量
	private String data;//返回信息主体
	private String msg;//返回信息
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
