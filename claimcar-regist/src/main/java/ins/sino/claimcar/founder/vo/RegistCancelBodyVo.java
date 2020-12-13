package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 报案注销接口vo（理赔请求客服系统）
 * @author Luwei
 */
@XStreamAlias("BODY")
public class RegistCancelBodyVo {

	@XStreamAlias("OUTDATE")
	private RegistCancelOutDateVo outDate;

	public RegistCancelOutDateVo getOutDate() {
		return outDate;
	}

	public void setOutDate(RegistCancelOutDateVo outDate) {
		this.outDate = outDate;
	}

}
