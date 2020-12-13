/******************************************************************************
 * CREATETIME : 2016年5月30日 下午1:06:19
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 立案--请求上海平台信息BodyVo类-商业
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHBIClaimReqBodyVo {

	/**
	 * 基本信息
	 */
	@XmlElement(name = "BASE_PART")
	private SHBIClaimReqBasePartVo basePartVo;

	/**
	 * 第三方车辆情况（多条）
	 */
	@XmlElementWrapper(name = "THIRD_VEHICLE_LIST")
	@XmlElement(name = "THIRD_VEHICLE_DATA")
	private List<SHBIClaimReqThirdVehicleDataVo> ThirdVehicleDataVo;

	/**
	 * 损失情况列表（多条）
	 */
	@XmlElementWrapper(name = "OBJ_LIST")
	@XmlElement(name = "OBJ_DATA")
	private List<SHBIClaimReqObjDataVo> objDataVo;

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

	
	
	
	public SHBIClaimReqBasePartVo getBasePartVo() {
		return basePartVo;
	}

	public void setBasePartVo(SHBIClaimReqBasePartVo basePartVo) {
		this.basePartVo = basePartVo;
	}

	public List<SHBIClaimReqThirdVehicleDataVo> getThirdVehicleDataVo() {
		return ThirdVehicleDataVo;
	}

	public void setThirdVehicleDataVo(List<SHBIClaimReqThirdVehicleDataVo> thirdVehicleDataVo) {
		ThirdVehicleDataVo = thirdVehicleDataVo;
	}

	public List<SHBIClaimReqObjDataVo> getObjDataVo() {
		return objDataVo;
	}

	public void setObjDataVo(List<SHBIClaimReqObjDataVo> objDataVo) {
		this.objDataVo = objDataVo;
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
