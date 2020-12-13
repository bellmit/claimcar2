package ins.sino.claimcar.policyLinkage.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * <pre></pre>
 * @author ★niuqiang
 */
@XStreamAlias("result")
public class CaseDetailResultVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("code")
	private String code;
	
	
	@XStreamAlias("info")
	private CaseDetailInfoVo info;
	
	@XStreamAlias("msg")
	private String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public CaseDetailInfoVo getInfo() {
		return info;
	}

	public void setInfo(CaseDetailInfoVo info) {
		this.info = info;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	

}
