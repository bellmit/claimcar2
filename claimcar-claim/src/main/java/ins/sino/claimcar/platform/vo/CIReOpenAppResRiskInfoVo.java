package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CIReOpenAppResRiskInfoVo {
	
	/** 系统提示信息 **/
	@XmlElement(name = "RISK_SYSTEM_INFO")
	private String riskSystemInfo;
	
	/** 车辆风险类型提示信息 **/
	@XmlElement(name = "VEHICLE_RISK_INFO")
	private CIReOpenAppResVehicleRiskInfoVo vehicleRiskInfo;

	/** 人员风险类型提示信息 **/
	@XmlElement(name = "PERSON_RISK_INFO")
	private CIReOpenAppResPersonRiskInfoVo personRiskInfo;

	/** 机构风险类型提示信息 **/
	@XmlElement(name = "INSTITUTION_RISK_INFO")
	private CIReOpenAppResInstitutionRiskInfoVo institutionRiskInfo;

	public String getRiskSystemInfo() {
		return riskSystemInfo;
	}

	public void setRiskSystemInfo(String riskSystemInfo) {
		this.riskSystemInfo = riskSystemInfo;
	}

	public CIReOpenAppResVehicleRiskInfoVo getVehicleRiskInfo() {
		return vehicleRiskInfo;
	}

	public void setVehicleRiskInfo(CIReOpenAppResVehicleRiskInfoVo vehicleRiskInfo) {
		this.vehicleRiskInfo = vehicleRiskInfo;
	}

	public CIReOpenAppResPersonRiskInfoVo getPersonRiskInfo() {
		return personRiskInfo;
	}

	public void setPersonRiskInfo(CIReOpenAppResPersonRiskInfoVo personRiskInfo) {
		this.personRiskInfo = personRiskInfo;
	}

	public CIReOpenAppResInstitutionRiskInfoVo getInstitutionRiskInfo() {
		return institutionRiskInfo;
	}

	public void setInstitutionRiskInfo(
			CIReOpenAppResInstitutionRiskInfoVo institutionRiskInfo) {
		this.institutionRiskInfo = institutionRiskInfo;
	}

}
