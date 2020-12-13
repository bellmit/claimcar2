/******************************************************************************
 * CREATETIME : 2016年6月6日 下午8:05:39
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author ★XMSH
 */
public class SHBIEndCaseResBodyVo {

	@XmlElement(name = "BASE_PART")
	private SHBIEndCaseResBasePartVo basePart;

	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private List<SHBIEndCaseResSubrogationDataVo> subrogationList;

	/**
	 * @return 返回 basePart。
	 */
	public SHBIEndCaseResBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(SHBIEndCaseResBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 subrogationList。
	 */
	public List<SHBIEndCaseResSubrogationDataVo> getSubrogationList() {
		return subrogationList;
	}

	/**
	 * @param subrogationList 要设置的 subrogationList。
	 */
	public void setSubrogationList(List<SHBIEndCaseResSubrogationDataVo> subrogationList) {
		this.subrogationList = subrogationList;
	}
}
