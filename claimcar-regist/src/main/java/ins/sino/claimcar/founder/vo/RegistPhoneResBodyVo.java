package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 车险理赔报案获取电话号码
 * @author yzyuan
 *
 */
@XStreamAlias("BODY")
public class RegistPhoneResBodyVo {
	
	@XStreamAlias("OUTDATE")
	private RegistPhoneResOutDateVo outdate;

	public RegistPhoneResOutDateVo getOutdate() {
		return outdate;
	}

	public void setOutdate(RegistPhoneResOutDateVo outdate) {
		this.outdate = outdate;
	}
	
	

}
