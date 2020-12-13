/******************************************************************************
 * CREATETIME : 2016年6月6日 下午7:25:01
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHBIEndCaseReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private SHBIEndCaseBasePartVo basePart;

	@XmlElementWrapper(name = "THIRD_VEHICLE_LIST")
	@XmlElement(name = "THIRD_VEHICLE_DATA")
	private List<SHBIEndCaseThirdVehicleDataVo> thirdVehicleList;

	@XmlElementWrapper(name = "OBJ_LIST")
	@XmlElement(name = "OBJ_DATA")
	private List<SHBIEndCaseObjDataVo> objList;

	@XmlElementWrapper(name = "RECOVERY_LIST")
	@XmlElement(name = "RECOVERY_DATA")
	private List<SHBIEndCaseRecoveryDataVo> recoveryList;

	@XmlElementWrapper(name = "DISPUTE_LIST")
	@XmlElement(name = "DISPUTE_DATA")
	private List<SHBIEndCaseDisputeDataVo> disputeList;
	
	@XStreamImplicit  //欺诈类型列表（多条）FraudTypeData
	@XmlElement(name = "FraudTypeData")
	private List<SHCIEndCaseFraudTypeDataVo> fraudTypeData;

	/**
	 * @return 返回 basePart。
	 */
	public SHBIEndCaseBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(SHBIEndCaseBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 thirdVehicleList。
	 */
	public List<SHBIEndCaseThirdVehicleDataVo> getThirdVehicleList() {
		return thirdVehicleList;
	}

	/**
	 * @param thirdVehicleList 要设置的 thirdVehicleList。
	 */
	public void setThirdVehicleList(List<SHBIEndCaseThirdVehicleDataVo> thirdVehicleList) {
		this.thirdVehicleList = thirdVehicleList;
	}

	/**
	 * @return 返回 objList。
	 */
	public List<SHBIEndCaseObjDataVo> getObjList() {
		return objList;
	}

	/**
	 * @param objList 要设置的 objList。
	 */
	public void setObjList(List<SHBIEndCaseObjDataVo> objList) {
		this.objList = objList;
	}

	/**
	 * @return 返回 recoveryList。
	 */
	public List<SHBIEndCaseRecoveryDataVo> getRecoveryList() {
		return recoveryList;
	}

	/**
	 * @param recoveryList 要设置的 recoveryList。
	 */
	public void setRecoveryList(List<SHBIEndCaseRecoveryDataVo> recoveryList) {
		this.recoveryList = recoveryList;
	}

	/**
	 * @return 返回 disputeList。
	 */
	public List<SHBIEndCaseDisputeDataVo> getDisputeList() {
		return disputeList;
	}

	/**
	 * @param disputeList 要设置的 disputeList。
	 */
	public void setDisputeList(List<SHBIEndCaseDisputeDataVo> disputeList) {
		this.disputeList = disputeList;
	}

	public List<SHCIEndCaseFraudTypeDataVo> getFraudTypeData() {
		return fraudTypeData;
	}

	public void setFraudTypeData(List<SHCIEndCaseFraudTypeDataVo> fraudTypeData) {
		this.fraudTypeData = fraudTypeData;
	}

}
