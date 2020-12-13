/******************************************************************************
 * CREATETIME : 2016年8月4日 下午7:06:25
 ******************************************************************************/
package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★Luwei
 */
@XStreamAlias("BODY")
public class CancelDflossTaskBodyVo {

	/**  */
	@XStreamAlias("OUTDATE")
	private CancelDflossTaskReqOutDateVo outDate;

	/**
	 * @return 返回 outDate。
	 */
	public CancelDflossTaskReqOutDateVo getOutDate() {
		return outDate;
	}

	/**
	 * @param outDate 要设置的 outDate。
	 */
	public void setOutDate(CancelDflossTaskReqOutDateVo outDate) {
		this.outDate = outDate;
	}

}
