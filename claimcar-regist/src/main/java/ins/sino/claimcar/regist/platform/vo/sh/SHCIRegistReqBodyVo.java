/******************************************************************************
 * CREATETIME : 2016年5月24日 下午3:53:48
 ******************************************************************************/
package ins.sino.claimcar.regist.platform.vo.sh;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 交强报案请求平台信息BodyVo类-上海平台
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHCIRegistReqBodyVo {

	/**
	 * 基本信息
	 */
	@XmlElement(name = "BASE_PART")
	private SHCIRegistReqBasePartVo request_basePartVo;

	/**
	 * 损失情况列表（多条）
	 */
	@XmlElementWrapper(name = "OBJ_LIST")
	@XmlElement(name = "OBJ_DATA")
	private List<SHCIRegistReqObjDataVo> request_objDataVo;

	/**
	 * 被追偿保险公司（多条）
	 */
	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private List<SHCIRegistReqSubrogationDataVo> request_SubrogationDataVo;

	/**
	 * 争议信息列表（多条）
	 */
	@XmlElementWrapper(name = "DISPUTE_LIST")
	@XmlElement(name = "DISPUTE_DATA")
	private List<SHCIRegistReqDisputeDataVo> DisputeDataVo;

	
	public SHCIRegistReqBasePartVo getRequest_basePartVo() {
		return request_basePartVo;
	}

	public void setRequest_basePartVo(SHCIRegistReqBasePartVo request_basePartVo) {
		this.request_basePartVo = request_basePartVo;
	}

	public List<SHCIRegistReqObjDataVo> getRequest_objDataVo() {
		return request_objDataVo;
	}

	public void setRequest_objDataVo(List<SHCIRegistReqObjDataVo> request_objDataVo) {
		this.request_objDataVo = request_objDataVo;
	}

	public List<SHCIRegistReqSubrogationDataVo> getRequest_SubrogationDataVo() {
		return request_SubrogationDataVo;
	}

	public void setRequest_SubrogationDataVo(List<SHCIRegistReqSubrogationDataVo> request_SubrogationDataVo) {
		this.request_SubrogationDataVo = request_SubrogationDataVo;
	}

	public List<SHCIRegistReqDisputeDataVo> getDisputeDataVo() {
		return DisputeDataVo;
	}

	public void setDisputeDataVo(List<SHCIRegistReqDisputeDataVo> disputeDataVo) {
		DisputeDataVo = disputeDataVo;
	}

}
