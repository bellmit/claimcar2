/******************************************************************************
 * CREATETIME : 2016年6月1日 上午11:39:06
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHBIVClaimReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private SHBIVClaimBasePartVo basePart;

	@XmlElementWrapper(name = "CLAIM_COVER_LIST")
	@XmlElement(name = "CLAIM_COVER_DATA")
	private List<SHBIVClaimCoverDataVo> claimCoverList;

	@XmlElementWrapper(name = "PERSON_LIST")
	@XmlElement(name = "PERSON_DATA")
	private List<SHBIVClaimPersonDataVo> personList;

	@XmlElementWrapper(name = "VEHICLE_LIST")
	@XmlElement(name = "VEHICLE_DATA")
	private List<SHBIVClaimVehicleDataVo> vehicleList;

	@XmlElementWrapper(name = "OBJ_LIST")
	@XmlElement(name = "OBJ_DATA")
	private List<SHBIVClaimObjDataVo> objList;
	
	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private List<SHBIVClaimSubrogationDataVo> subrogationList;

	@XmlElementWrapper(name = "DISPUTE_LIST")
	@XmlElement(name = "DISPUTE_DATA")
	private List<SHBIVClaimDisputeDataVo> disputeList;

	/**
	 * @return 返回 basePart。
	 */
	public SHBIVClaimBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(SHBIVClaimBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 claimCoverList。
	 */
	public List<SHBIVClaimCoverDataVo> getClaimCoverList() {
		return claimCoverList;
	}

	/**
	 * @param claimCoverList 要设置的 claimCoverList。
	 */
	public void setClaimCoverList(List<SHBIVClaimCoverDataVo> claimCoverList) {
		this.claimCoverList = claimCoverList;
	}

	/**
	 * @return 返回 personList。
	 */
	public List<SHBIVClaimPersonDataVo> getPersonList() {
		return personList;
	}

	/**
	 * @param personList 要设置的 personList。
	 */
	public void setPersonList(List<SHBIVClaimPersonDataVo> personList) {
		this.personList = personList;
	}

	/**
	 * @return 返回 vehicleList。
	 */
	public List<SHBIVClaimVehicleDataVo> getVehicleList() {
		return vehicleList;
	}

	/**
	 * @param vehicleList 要设置的 vehicleList。
	 */
	public void setVehicleList(List<SHBIVClaimVehicleDataVo> vehicleList) {
		this.vehicleList = vehicleList;
	}

	/**
	 * @return 返回 objList。
	 */
	public List<SHBIVClaimObjDataVo> getObjList() {
		return objList;
	}

	/**
	 * @param objList 要设置的 objList。
	 */
	public void setObjList(List<SHBIVClaimObjDataVo> objList) {
		this.objList = objList;
	}

	/**
	 * @return 返回 subrogationList。
	 */
	public List<SHBIVClaimSubrogationDataVo> getSubrogationList() {
		return subrogationList;
	}

	/**
	 * @param subrogationList 要设置的 subrogationList。
	 */
	public void setSubrogationList(List<SHBIVClaimSubrogationDataVo> subrogationList) {
		this.subrogationList = subrogationList;
	}

	/**
	 * @return 返回 disputeList。
	 */
	public List<SHBIVClaimDisputeDataVo> getDisputeList() {
		return disputeList;
	}

	/**
	 * @param disputeList 要设置的 disputeList。
	 */
	public void setDisputeList(List<SHBIVClaimDisputeDataVo> disputeList) {
		this.disputeList = disputeList;
	}

}
