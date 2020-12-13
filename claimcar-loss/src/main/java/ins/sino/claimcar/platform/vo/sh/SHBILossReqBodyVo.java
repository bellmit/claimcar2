/******************************************************************************
 * CREATETIME : 2016年5月26日 下午6:51:26
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 查勘、定损、核损请求上海平台信息BodyVo类-商业
 * 
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHBILossReqBodyVo {

	/**
	 * 基本信息
	 */
	@XmlElement(name = "BASE_PART")
	private SHBILossReqBasePartVo basePartVo;

	/**
	 * 人员损失情况（多条）
	 */
	@XmlElementWrapper(name = "PERSON_LIST")
	@XmlElement(name = "PERSON_DATA")
	private List<SHCILossReqPersonDataVo> personDataVo;

	/**
	 * 车辆损失情况（多条）VEHICLE_LIST
	 */
	@XmlElementWrapper(name = "VEHICLE_LIST")
	@XmlElement(name = "VEHICLE_DATA")
	private List<SHCILossReqVehicleDataVo> vehicleDataVo;

	/**
	 * 物损损失情况（多条）OBJ_LIST
	 */
	@XmlElementWrapper(name = "OBJ_LIST")
	@XmlElement(name = "OBJ_DATA")
	private List<SHCILossReqObjDataVo> ObjDataVo;

	/**
	 * 追偿保险公司（多条）
	 */
	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private List<SHCILossReqSubrogationDataVo> subrogationDataVo;

	/**
	 * 争议信息列表（多条）
	 */
	@XmlElementWrapper(name = "DISPUTE_LIST")
	@XmlElement(name = "DISPUTE_DATA")
	private List<SHCILossReqDisputeDataVo> DisputeDataVo;

	public SHBILossReqBasePartVo getBasePartVo() {
		return basePartVo;
	}

	public void setBasePartVo(SHBILossReqBasePartVo basePartVo) {
		this.basePartVo = basePartVo;
	}

	public List<SHCILossReqPersonDataVo> getPersonDataVo() {
		return personDataVo;
	}

	public void setPersonDataVo(List<SHCILossReqPersonDataVo> personDataVo) {
		this.personDataVo = personDataVo;
	}

	public List<SHCILossReqVehicleDataVo> getVehicleDataVo() {
		return vehicleDataVo;
	}

	public void setVehicleDataVo(List<SHCILossReqVehicleDataVo> vehicleDataVo) {
		this.vehicleDataVo = vehicleDataVo;
	}

	public List<SHCILossReqObjDataVo> getObjDataVo() {
		return ObjDataVo;
	}

	public void setObjDataVo(List<SHCILossReqObjDataVo> objDataVo) {
		ObjDataVo = objDataVo;
	}

	public List<SHCILossReqSubrogationDataVo> getSubrogationDataVo() {
		return subrogationDataVo;
	}

	public void setSubrogationDataVo(List<SHCILossReqSubrogationDataVo> subrogationDataVo) {
		this.subrogationDataVo = subrogationDataVo;
	}

	public List<SHCILossReqDisputeDataVo> getDisputeDataVo() {
		return DisputeDataVo;
	}

	public void setDisputeDataVo(List<SHCILossReqDisputeDataVo> disputeDataVo) {
		DisputeDataVo = disputeDataVo;
	}

}
