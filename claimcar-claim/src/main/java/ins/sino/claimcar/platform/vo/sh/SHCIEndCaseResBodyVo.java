/******************************************************************************
 * CREATETIME : 2016年6月6日 下午5:16:20
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author ★XMSH
 */
public class SHCIEndCaseResBodyVo {

	@XmlElement(name = "BASE_PART")
	private SHCIEndCaseResBasePartVo basePart;

	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private List<SHCIEndCaseResSubrogationDataVo> subrogationList;

}
