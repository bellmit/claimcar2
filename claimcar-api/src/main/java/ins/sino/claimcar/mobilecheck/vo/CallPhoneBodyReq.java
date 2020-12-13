package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * 3.7查勘员号码更新请求体对象
 */
@XStreamAlias("BODY")
public class CallPhoneBodyReq implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("OUTDATE")
	private CallPhoneOutdateReq outdate;

	public CallPhoneOutdateReq getOutdate() {
		return outdate;
	}

	public void setOutdate(CallPhoneOutdateReq outdate) {
		this.outdate = outdate;
	}
	

}
