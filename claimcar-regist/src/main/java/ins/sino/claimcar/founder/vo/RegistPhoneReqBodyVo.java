package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class RegistPhoneReqBodyVo {
	/**
	 * 报案获取电话号码的Body
	 */
	@XStreamAlias("OUTDATE")
	private RegistPhoneReqOutDateVo outDate;

	public RegistPhoneReqOutDateVo getOutDate() {
		return outDate;
	}

	public void setOutDate(RegistPhoneReqOutDateVo outDate) {
		this.outDate = outDate;
	}
}
