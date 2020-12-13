/******************************************************************************
 * CREATETIME : 2016年5月30日 上午11:21:56
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 立案--请求上海平台信息BodyVo类-交强
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHCIClaimReqBodyVo {

	/**
	 * 基本信息
	 */
	@XmlElement(name = "BASE_PART")
	private SHCIClaimReqBasePartVo basePartVo;

	/**
	 * 损失情况列表（多条）
	 */
	@XmlElementWrapper(name = "OBJ_LIST")
	@XmlElement(name = "OBJ_DATA")
	private List<SHCIClaimReqObjDataVo> ObjDataVo;

	/**
	 * 被追偿保险公司（多条）
	 */
	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private List<SHCIClaimReqSubrogationDataVo> subrogationDataVo;

	/**
	 * 争议信息列表（多条）为空
	 */
	@XmlElementWrapper(name = "DISPUTE_LIST")
	@XmlElement(name = "DISPUTE_DATA")
	private List<SHCIClaimReqDisputeDataVo> disputeDataVo;

	
	
	public SHCIClaimReqBasePartVo getBasePartVo() {
		return basePartVo;
	}

	public void setBasePartVo(SHCIClaimReqBasePartVo basePartVo) {
		this.basePartVo = basePartVo;
	}

	public List<SHCIClaimReqObjDataVo> getObjDataVo() {
		return ObjDataVo;
	}

	public void setObjDataVo(List<SHCIClaimReqObjDataVo> objDataVo) {
		ObjDataVo = objDataVo;
	}

	public List<SHCIClaimReqSubrogationDataVo> getSubrogationDataVo() {
		return subrogationDataVo;
	}

	public void setSubrogationDataVo(List<SHCIClaimReqSubrogationDataVo> subrogationDataVo) {
		this.subrogationDataVo = subrogationDataVo;
	}

	public List<SHCIClaimReqDisputeDataVo> getDisputeDataVo() {
		return disputeDataVo;
	}

	public void setDisputeDataVo(List<SHCIClaimReqDisputeDataVo> disputeDataVo) {
		this.disputeDataVo = disputeDataVo;
	}

}
