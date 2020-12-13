/******************************************************************************
* CREATETIME : 2019年3月19日 下午2:25:33
******************************************************************************/
package ins.sino.claimcar.claim.vo;

import java.util.Date;


/**
 * <pre></pre>
 * @author ★XHY
 */
public class PrpsmsEmailVo extends PrpsmsEmailVoBase  implements java.io.Serializable{

	/**  */
	private static final long serialVersionUID = 1L;
	private Date createTimeStart;
	private Date createTimeEnd;
	
	public Date getCreateTimeStart() {
		return createTimeStart;
	}
	
	public void setCreateTimeStart(Date createTimeStart) {
		this.createTimeStart = createTimeStart;
	}
	
	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}
	
	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

}
