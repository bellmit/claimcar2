/******************************************************************************
 * CREATETIME : 2016年5月31日 下午2:55:16
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 上海理算核赔
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHCIVClaimReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private SHCIVClaimBasePartVo basePart;

	@XmlElementWrapper(name = "CLAIM_COVER_LIST")
	@XmlElement(name = "CLAIM_COVER_DATA")
	private List<SHCIVClaimCoverDataVo> claimCoverList;

	@XmlElementWrapper(name = "PERSON_LIST")
	@XmlElement(name = "PERSON_DATA")
	private List<SHCIVClaimPersonDataVo> personList;

	@XmlElementWrapper(name = "VEHICLE_LIST")
	@XmlElement(name = "VEHICLE_DATA")
	private List<SHCIVClaimVehicleDataVo> vehicleList;

	@XmlElementWrapper(name = "OBJ_LIST")
	@XmlElement(name = "OBJ_DATA")
	private List<SHCIVClaimObjDataVo> objList;

	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private List<SHCIVClaimSubrogationDataVo> subrogationList;

	@XmlElementWrapper(name = "DISPUTE_LIST")
	@XmlElement(name = "DISPUTE_DATA")
	private List<SHCIVClaimDisputeDataVo> disputeList;

	/**
	 * @return 返回 basePart。
	 */
	public SHCIVClaimBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(SHCIVClaimBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 personList。
	 */
	public List<SHCIVClaimPersonDataVo> getPersonList() {
		return personList;
	}

	/**
	 * @param personList 要设置的 personList。
	 */
	public void setPersonList(List<SHCIVClaimPersonDataVo> personList) {
		this.personList = personList;
	}

	/**
	 * @return 返回 vehicleList。
	 */
	public List<SHCIVClaimVehicleDataVo> getVehicleList() {
		return vehicleList;
	}

	/**
	 * @param vehicleList 要设置的 vehicleList。
	 */
	public void setVehicleList(List<SHCIVClaimVehicleDataVo> vehicleList) {
		this.vehicleList = vehicleList;
	}

	/**
	 * @return 返回 objList。
	 */
	public List<SHCIVClaimObjDataVo> getObjList() {
		return objList;
	}

	/**
	 * @param objList 要设置的 objList。
	 */
	public void setObjList(List<SHCIVClaimObjDataVo> objList) {
		this.objList = objList;
	}

	/**
	 * @return 返回 subrogationList。
	 */
	public List<SHCIVClaimSubrogationDataVo> getSubrogationList() {
		return subrogationList;
	}

	/**
	 * @param subrogationList 要设置的 subrogationList。
	 */
	public void setSubrogationList(List<SHCIVClaimSubrogationDataVo> subrogationList) {
		this.subrogationList = subrogationList;
	}

	/**
	 * @return 返回 disputeList。
	 */
	public List<SHCIVClaimDisputeDataVo> getDisputeList() {
		return disputeList;
	}

	/**
	 * @param disputeList 要设置的 disputeList。
	 */
	public void setDisputeList(List<SHCIVClaimDisputeDataVo> disputeList) {
		this.disputeList = disputeList;
	}

	/**
	 * @return 返回 claimCoverList。
	 */
	public List<SHCIVClaimCoverDataVo> getClaimCoverList() {
		return claimCoverList;
	}

	/**
	 * @param claimCoverList 要设置的 claimCoverList。
	 */
	public void setClaimCoverList(List<SHCIVClaimCoverDataVo> claimCoverList) {
		this.claimCoverList = claimCoverList;
	}
}
