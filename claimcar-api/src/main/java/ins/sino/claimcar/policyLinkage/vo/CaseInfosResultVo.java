package ins.sino.claimcar.policyLinkage.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class CaseInfosResultVo implements Serializable{

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("code")
	private String code;
	
	@XStreamAlias("info")
	private CaseInfosInfoVo info;
	
	@XStreamAlias("msg")
	private String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public CaseInfosInfoVo getInfo() {
		return info;
	}

	public void setInfo(CaseInfosInfoVo info) {
		this.info = info;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	

}
