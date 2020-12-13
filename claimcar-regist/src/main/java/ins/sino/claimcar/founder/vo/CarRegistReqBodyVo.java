package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 车险报案vo（理赔请求客服系统-请求body,体信息）
 * @author Luwei
 *
 */
@XStreamAlias("BODY")
public class CarRegistReqBodyVo {

	/**  */
	@XStreamAlias("OUTDATE")
	private CarRegistReqOutDateVo outDate;

	
	public CarRegistReqOutDateVo getOutDate() {
		return outDate;
	}

	public void setOutDate(CarRegistReqOutDateVo outDate) {
		this.outDate = outDate;
	}

}
