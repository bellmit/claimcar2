/******************************************************************************
 * CREATETIME : 2016年5月26日 下午12:01:52
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 结案追加请求上海平台信息BodyVo类-交强
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHCIEndCaseAddReqBodyVo {

	/**
	 * 基本信息
	 */
	@XmlElement(name = "BASE_PART")
	private SHCIEndCaseAddReqBasePartVo basePartVo;

	/**
	 * 追加损失赔偿情况（多条）
	 */
	@XmlElementWrapper(name = "CLAIM_COVER_LIST")
	@XmlElement(name = "CLAIM_COVER_DATA")
	private List<SHCIEndCaseAddReqClaimCoverListVo> claimCoverListVo;

	/**
	 * 人员损失情况（多条）
	 */
	@XmlElementWrapper(name = "PERSON_LIST")
	@XmlElement(name = "PERSON_DATA")
	private List<SHCIEndCaseAddReqPersonDataVo> personDataVo;

	/**
	 * 车辆损失情况（多条）VEHICLE_LIST
	 */
	@XmlElementWrapper(name = "VEHICLE_LIST")
	@XmlElement(name = "VEHICLE_DATA")
	private List<SHCIEndCaseAddReqVehicleDataVo> vehicleDataVo;

	/**
	 * 物损损失情况（多条）OBJ_LIST
	 */
	@XmlElementWrapper(name = "OBJ_LIST")
	@XmlElement(name = "OBJ_DATA")
	private List<SHCIEndCaseAddReqObjDataVo> objDataVo;

	/**
	 * 单证明细（多条）
	 */
	@XmlElementWrapper(name = "DOC_LIST")
	@XmlElement(name = "DOC_DATA")
	private List<SHCIEndCaseAddReqDocDataVo> docDataVo;

	public SHCIEndCaseAddReqBasePartVo getBasePartVo() {
		return basePartVo;
	}

	public void setBasePartVo(SHCIEndCaseAddReqBasePartVo basePartVo) {
		this.basePartVo = basePartVo;
	}

	public List<SHCIEndCaseAddReqClaimCoverListVo> getClaimCoverListVo() {
		return claimCoverListVo;
	}

	public void setClaimCoverListVo(List<SHCIEndCaseAddReqClaimCoverListVo> claimCoverListVo) {
		this.claimCoverListVo = claimCoverListVo;
	}

	public List<SHCIEndCaseAddReqPersonDataVo> getPersonDataVo() {
		return personDataVo;
	}

	public void setPersonDataVo(List<SHCIEndCaseAddReqPersonDataVo> personDataVo) {
		this.personDataVo = personDataVo;
	}

	public List<SHCIEndCaseAddReqVehicleDataVo> getVehicleDataVo() {
		return vehicleDataVo;
	}

	public void setVehicleDataVo(List<SHCIEndCaseAddReqVehicleDataVo> vehicleDataVo) {
		this.vehicleDataVo = vehicleDataVo;
	}

	public List<SHCIEndCaseAddReqObjDataVo> getObjDataVo() {
		return objDataVo;
	}

	public void setObjDataVo(List<SHCIEndCaseAddReqObjDataVo> objDataVo) {
		this.objDataVo = objDataVo;
	}

	public List<SHCIEndCaseAddReqDocDataVo> getDocDataVo() {
		return docDataVo;
	}

	public void setDocDataVo(List<SHCIEndCaseAddReqDocDataVo> docDataVo) {
		this.docDataVo = docDataVo;
	}

}
