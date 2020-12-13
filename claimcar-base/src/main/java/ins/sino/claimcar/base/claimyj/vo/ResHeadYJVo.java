package ins.sino.claimcar.base.claimyj.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("HEAD")
public class ResHeadYJVo implements java.io.Serializable {
    @XStreamAlias("DATA")
	private String data;
    @XStreamAlias("ERROR")
	private String error;
    @XStreamAlias("MSG")
	private String msg;
    @XStreamAlias("SUCCESS")
	private String success;
    @XStreamAlias("TOTAL")
	private String total;
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
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
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
    
}
