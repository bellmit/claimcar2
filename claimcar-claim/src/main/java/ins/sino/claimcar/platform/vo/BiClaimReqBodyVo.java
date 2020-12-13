/******************************************************************************
 * CREATETIME : 2016年5月23日 上午11:38:05
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiClaimReqBodyVo {

	@XmlElement(name = "BasePart")
	private BiClaimBasePartVo basePart;

	@XmlElement(name = "SubrogationData")
	private List<BiClaimSubrogationDataVo> subrogationDataList;

	/**
	 * @return 返回 basePart。
	 */
	public BiClaimBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(BiClaimBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 subrogationDataList。
	 */
	public List<BiClaimSubrogationDataVo> getSubrogationDataList() {
		return subrogationDataList;
	}

	/**
	 * @param subrogationDataList 要设置的 subrogationDataList。
	 */
	public void setSubrogationDataList(List<BiClaimSubrogationDataVo> subrogationDataList) {
		this.subrogationDataList = subrogationDataList;
	}

}
