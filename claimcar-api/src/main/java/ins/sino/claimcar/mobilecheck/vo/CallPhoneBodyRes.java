package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 查勘员号码更新的返回体对象
 */
@XStreamAlias("BODY")
public class CallPhoneBodyRes implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("OUTDATE")
	private CallPhoneOutdate outdate;

	public CallPhoneOutdate getOutdate() {
		return outdate;
	}

	public void setOutdate(CallPhoneOutdate outdate) {
		this.outdate = outdate;
	}


}
