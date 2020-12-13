/******************************************************************************
* CREATETIME : 2016年6月6日 下午3:10:40
******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;


/**
 * 
 * @author ★XMSH
 */
public class SHBIPaymentResBasePartVo {

	@XmlElement(name = "SUCCEED_GROUP")
	private SHBIPaymentResSucceedGroupVo succeedGroup;

	@XmlElement(name="LOST_GROUP")
	private SHBIPaymentResLostGroupVo lostGroup;
	
	/**
	 * @return 返回 succeedGroup。
	 */
	public SHBIPaymentResSucceedGroupVo getSucceedGroup() {
		return succeedGroup;
	}

	/**
	 * @param succeedGroup 要设置的 succeedGroup。
	 */
	public void setSucceedGroup(SHBIPaymentResSucceedGroupVo succeedGroup) {
		this.succeedGroup = succeedGroup;
	}
}
