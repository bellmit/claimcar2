package ins.sino.claimcar.regist.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 交强报案请求平台信息BodyVo类
 * @author dengkk
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CiRegistBodyReqVo {

	/**
	 * 基本信息
	 */
	@XmlElement(name="BASE_PART")
	private CiRegistBasePartReqVo ciRegistBasePartReqVo;
	
	/**
	 * 三者车辆情况列表
	 */
	@XmlElementWrapper(name = "THIRD_VEHICLE_LIST")
	@XmlElement(name = "THIRD_VEHICLE_DATA")
	private List<CiRegistThirdVehicleDataReqVo> ciRegistThirdVehicleDataReqs;
	
	/**
	 * 损失情况列表
	 */
	@XmlElementWrapper(name = "REPORT_PHONE_NO_LIST")
	@XmlElement(name = "REPORT_PHONE_NO_DATA")
	private List<CiRrportPhoneDataVo> reportPhoneList;
	
	/**
	 * 损失情况列表
	 */
	@XmlElementWrapper(name = "LOSS_LIST")
	@XmlElement(name = "LOSS_DATA")
	private List<CiRegistLossDataReqVo> ciRegistLossDataReqs;

	public CiRegistBasePartReqVo getCiRegistBasePartReqVo() {
		return ciRegistBasePartReqVo;
	}

	public void setCiRegistBasePartReqVo(CiRegistBasePartReqVo ciRegistBasePartReqVo) {
		this.ciRegistBasePartReqVo = ciRegistBasePartReqVo;
	}

	public List<CiRegistThirdVehicleDataReqVo> getCiRegistThirdVehicleDataReqs() {
		return ciRegistThirdVehicleDataReqs;
	}

	public void setCiRegistThirdVehicleDataReqs(
			List<CiRegistThirdVehicleDataReqVo> ciRegistThirdVehicleDataReqs) {
		this.ciRegistThirdVehicleDataReqs = ciRegistThirdVehicleDataReqs;
	}

	public List<CiRegistLossDataReqVo> getCiRegistLossDataReqs() {
		return ciRegistLossDataReqs;
	}

	public void setCiRegistLossDataReqs(
			List<CiRegistLossDataReqVo> ciRegistLossDataReqs) {
		this.ciRegistLossDataReqs = ciRegistLossDataReqs;
	}

	public List<CiRrportPhoneDataVo> getReportPhoneList() {
		return reportPhoneList;
	}

	public void setReportPhoneList(List<CiRrportPhoneDataVo> reportPhoneList) {
		this.reportPhoneList = reportPhoneList;
	}

    
}
