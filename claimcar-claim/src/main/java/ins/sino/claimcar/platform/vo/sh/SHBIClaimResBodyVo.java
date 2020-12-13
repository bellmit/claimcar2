/******************************************************************************
 * CREATETIME : 2016年5月30日 下午2:48:04
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 立案--请求上海平台信息--返回BodyVo类-商业
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHBIClaimResBodyVo {

	/**
	 * 基本信息
	 */
	@XmlElement(name = "BASE_PART")
	private SHCIClaimResBasePartVo basePartVo;

	/**
	 * 损失情况列表（多条）
	 */
	@XmlElementWrapper(name = "CLAIM_LIST")
	@XmlElement(name = "CLAIM_DATA")
	private List<SHBIClaimResClaimDataVo> claimDataVo;

	/**
	 * 代位求偿码信息（多条）
	 */
	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private List<SHCIClaimResSubrogationDataVo> subrogationDataVo;

	
	public SHCIClaimResBasePartVo getBasePartVo() {
		return basePartVo;
	}

	public void setBasePartVo(SHCIClaimResBasePartVo basePartVo) {
		this.basePartVo = basePartVo;
	}

	public List<SHBIClaimResClaimDataVo> getClaimDataVo() {
		return claimDataVo;
	}

	public void setClaimDataVo(List<SHBIClaimResClaimDataVo> claimDataVo) {
		this.claimDataVo = claimDataVo;
	}

	public List<SHCIClaimResSubrogationDataVo> getSubrogationDataVo() {
		return subrogationDataVo;
	}

	public void setSubrogationDataVo(List<SHCIClaimResSubrogationDataVo> subrogationDataVo) {
		this.subrogationDataVo = subrogationDataVo;
	}

}
