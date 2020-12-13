/******************************************************************************
 * CREATETIME : 2016年5月24日 上午10:04:15
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 理算核赔登记(商业)
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class BiVClaimReqBodyVo {

	@XmlElement(name = "BasePart")
	private BiVClaimBasePartVo basePart;

	@XmlElement(name = "AdjustmentData")
	private List<BiVClaimAdjustmentDataVo> adjustmentDataList;

	/**
	 * @return 返回 basePart。
	 */
	public BiVClaimBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(BiVClaimBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 adjustmentDataList。
	 */
	public List<BiVClaimAdjustmentDataVo> getAdjustmentDataList() {
		return adjustmentDataList;
	}

	/**
	 * @param adjustmentDataList 要设置的 adjustmentDataList。
	 */
	public void setAdjustmentDataList(List<BiVClaimAdjustmentDataVo> adjustmentDataList) {
		this.adjustmentDataList = adjustmentDataList;
	}

}
