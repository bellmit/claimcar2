package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BIReOpenAppResRiskInfoVo {
	
	@XmlElement(name = "RiskSystemInfo")
	private String riskSystemInfo;// 系统提示信息；参见代码

	@XmlElement(name = "VehicleRiskInfo")
	private BIReOpenAppResVehicleRiskInfoVo VehicleRiskInfoVo;// 车辆风险类型提示信息

	@XmlElement(name = "PersonRiskInfo")
	private BIReOpenAppResPersonRiskInfoVo PersonRiskInfoVo;// 人员风险类型提示信息

	@XmlElement(name = "InstituionRiskInfo")
	private BIReOpenAppResInstituionRiskInfoVo InstituionRiskInfoVo;// 机构风险类型提示信息

	public String getRiskSystemInfo() {
		return riskSystemInfo;
	}

	public void setRiskSystemInfo(String riskSystemInfo) {
		this.riskSystemInfo = riskSystemInfo;
	}

	public BIReOpenAppResVehicleRiskInfoVo getVehicleRiskInfoVo() {
		return VehicleRiskInfoVo;
	}

	public void setVehicleRiskInfoVo(
			BIReOpenAppResVehicleRiskInfoVo vehicleRiskInfoVo) {
		VehicleRiskInfoVo = vehicleRiskInfoVo;
	}

	public BIReOpenAppResPersonRiskInfoVo getPersonRiskInfoVo() {
		return PersonRiskInfoVo;
	}

	public void setPersonRiskInfoVo(BIReOpenAppResPersonRiskInfoVo personRiskInfoVo) {
		PersonRiskInfoVo = personRiskInfoVo;
	}

	public BIReOpenAppResInstituionRiskInfoVo getInstituionRiskInfoVo() {
		return InstituionRiskInfoVo;
	}

	public void setInstituionRiskInfoVo(
			BIReOpenAppResInstituionRiskInfoVo instituionRiskInfoVo) {
		InstituionRiskInfoVo = instituionRiskInfoVo;
	}

}
