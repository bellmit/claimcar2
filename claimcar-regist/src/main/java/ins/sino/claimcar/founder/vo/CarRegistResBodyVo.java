package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 车险报案vo（理赔请求客服系统-返回body,体信息）
 * @author Luwei
 *
 */
@XStreamAlias("BODY")
public class CarRegistResBodyVo {

	/**  */
	@XStreamAlias("OUTDATE")
	private CarRegistResOutDateVo outDate;

	public CarRegistResOutDateVo getOutDate() {
		return outDate;
	}

	public void setOutDate(CarRegistResOutDateVo outDate) {
		this.outDate = outDate;
	}

}
