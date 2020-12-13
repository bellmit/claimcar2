/******************************************************************************
 * CREATETIME : 2016年6月6日 下午3:08:51
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author ★XMSH
 */
public class SHBIPaymentResLostGroupVo {

	@XmlElementWrapper(name = "PAY_LOST_LIST")
	@XmlElement(name = "PAY_LOST_DATA")
	private List<SHBIPaymentResPayLostDataVo> payLostData;

	/**
	 * @return 返回 payLostData。
	 */
	public List<SHBIPaymentResPayLostDataVo> getPayLostData() {
		return payLostData;
	}

	/**
	 * @param payLostData 要设置的 payLostData。
	 */
	public void setPayLostData(List<SHBIPaymentResPayLostDataVo> payLostData) {
		this.payLostData = payLostData;
	}

}
