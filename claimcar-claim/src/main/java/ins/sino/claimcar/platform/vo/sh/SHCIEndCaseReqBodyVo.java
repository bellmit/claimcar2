/******************************************************************************
 * CREATETIME : 2016年6月6日 下午3:59:00
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
public class SHCIEndCaseReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private SHCIEndCaseBasePartVo basePart;

	@XmlElementWrapper(name = "THIRD_VEHICLE_LIST")
	@XmlElement(name = "THIRD_VEHICLE_DATA")
	private List<SHCIEndCaseThirdVehicleDataVo> thirdVehicleList;

	@XmlElementWrapper(name = "RECOVERY_LIST")
	@XmlElement(name = "RECOVERY_DATA")
	private List<SHCIEndCaseRecoveryDataVo> recoveryList;

	@XmlElementWrapper(name = "DISPUTE_LIST")
	@XmlElement(name = "DISPUTE_DATA")
	private List<SHCIEndCaseDisputeDataVo> disputeList;
	
	/*牛强  2017-03-15 改*/
	@XStreamImplicit  //欺诈类型列表（多条）FraudTypeData
	@XmlElement(name = "FraudTypeData")
	private List<SHCIEndCaseFraudTypeDataVo> fraudTypeData;

	/**
	 * @return 返回 basePart。
	 */
	public SHCIEndCaseBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(SHCIEndCaseBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 thirdVehicleList。
	 */
	public List<SHCIEndCaseThirdVehicleDataVo> getThirdVehicleList() {
		return thirdVehicleList;
	}

	/**
	 * @param thirdVehicleList 要设置的 thirdVehicleList。
	 */
	public void setThirdVehicleList(List<SHCIEndCaseThirdVehicleDataVo> thirdVehicleList) {
		this.thirdVehicleList = thirdVehicleList;
	}

	/**
	 * @return 返回 recoveryList。
	 */
	public List<SHCIEndCaseRecoveryDataVo> getRecoveryList() {
		return recoveryList;
	}

	/**
	 * @param recoveryList 要设置的 recoveryList。
	 */
	public void setRecoveryList(List<SHCIEndCaseRecoveryDataVo> recoveryList) {
		this.recoveryList = recoveryList;
	}

	/**
	 * @return 返回 disputeList。
	 */
	public List<SHCIEndCaseDisputeDataVo> getDisputeList() {
		return disputeList;
	}

	/**
	 * @param disputeList 要设置的 disputeList。
	 */
	public void setDisputeList(List<SHCIEndCaseDisputeDataVo> disputeList) {
		this.disputeList = disputeList;
	}

	public List<SHCIEndCaseFraudTypeDataVo> getFraudTypeData() {
		return fraudTypeData;
	}

	public void setFraudTypeData(List<SHCIEndCaseFraudTypeDataVo> fraudTypeData) {
		this.fraudTypeData = fraudTypeData;
	}

}
