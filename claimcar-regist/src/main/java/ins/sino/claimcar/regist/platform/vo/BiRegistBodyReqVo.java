package ins.sino.claimcar.regist.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 商业报案请求平台信息BodyVo类
 * @author dengkk
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiRegistBodyReqVo {

	/**
	 * 基本信息
	 */
	@XmlElement(name="BasePart")
	private BiRegistBasePartReqVo biRegistBasePartReqVo;
	
	/**
	 * 三者车辆情况列表
	 */
	//@XmlElementWrapper(name = "THIRD_VEHICLE_LIST")
	@XmlElement(name = "ThirdVehicleData")
	private List<BiRegistThirdVehicleDataReqVo> biRegistThirdVehicleDataReqs;
	
	/**
	 * 损失情况列表
	 */
	//@XmlElementWrapper(name = "LOSS_LIST")
	@XmlElement(name = "LossData")
	private List<BiRegistLossDataReqVo> biRegistLossDataReqs;
	
	@XmlElement(name = "SubrogationData")
	private List<BiSubrogationDataReqVo> biSubrogationDataReqs;
	
	/**
	 * 报案电话号码列表
	 */
	@XmlElement(name = "ReportPhoneNoData")
	private List<BiReportPhoneNoDataReqVo> biReportPhoneNoDataReqVo;
	
	public BiRegistBasePartReqVo getBiRegistBasePartReqVo() {
		return biRegistBasePartReqVo;
	}

	public void setBiRegistBasePartReqVo(BiRegistBasePartReqVo biRegistBasePartReqVo) {
		this.biRegistBasePartReqVo = biRegistBasePartReqVo;
	}

	public List<BiRegistThirdVehicleDataReqVo> getBiRegistThirdVehicleDataReqs() {
		return biRegistThirdVehicleDataReqs;
	}

	public void setBiRegistThirdVehicleDataReqs(
			List<BiRegistThirdVehicleDataReqVo> biRegistThirdVehicleDataReqs) {
		this.biRegistThirdVehicleDataReqs = biRegistThirdVehicleDataReqs;
	}

	public List<BiRegistLossDataReqVo> getBiRegistLossDataReqs() {
		return biRegistLossDataReqs;
	}

	public void setBiRegistLossDataReqs(
			List<BiRegistLossDataReqVo> biRegistLossDataReqs) {
		this.biRegistLossDataReqs = biRegistLossDataReqs;
	}

	public List<BiSubrogationDataReqVo> getBiSubrogationDataReqs() {
		return biSubrogationDataReqs;
	}

	public void setBiSubrogationDataReqs(
			List<BiSubrogationDataReqVo> biSubrogationDataReqs) {
		this.biSubrogationDataReqs = biSubrogationDataReqs;
	}

	public List<BiReportPhoneNoDataReqVo> getBiReportPhoneNoDataReqVo() {
		return biReportPhoneNoDataReqVo;
	}

	public void setBiReportPhoneNoDataReqVo(
			List<BiReportPhoneNoDataReqVo> biReportPhoneNoDataReqVo) {
		this.biReportPhoneNoDataReqVo = biReportPhoneNoDataReqVo;
	}
    

}
