package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CICheckResRiskInfoVo {

	/** 系统提示信息 **/
	@XmlElement(name = "RISK_SYSTEM_INFO")
	private String riskSystemInfo;

	/** 车辆风险类型提示信息 **/
	@XmlElement(name = "VEHICLE_RISK_INFO")
	private CICheckResVehicleRiskInfoVo vehicleRiskInfo;

	/** 人员风险类型提示信息 **/
	@XmlElement(name = "PERSON_RISK_INFO")
	private CICheckResPersonRiskInfoVo personRiskInfo;

	/** 机构风险类型提示信息 **/
	@XmlElement(name = "INSTITUTION_RISK_INFO")
	private CICheckResInstitutionRiskInfoVo institutionRiskInfo;

	public String getRiskSystemInfo() {
		return riskSystemInfo;
	}

	public void setRiskSystemInfo(String riskSystemInfo) {
		this.riskSystemInfo = riskSystemInfo;
	}

	public CICheckResVehicleRiskInfoVo getVehicleRiskInfo() {
		return vehicleRiskInfo;
	}

	public void setVehicleRiskInfo(CICheckResVehicleRiskInfoVo vehicleRiskInfo) {
		this.vehicleRiskInfo = vehicleRiskInfo;
	}

	public CICheckResPersonRiskInfoVo getPersonRiskInfo() {
		return personRiskInfo;
	}

	public void setPersonRiskInfo(CICheckResPersonRiskInfoVo personRiskInfo) {
		this.personRiskInfo = personRiskInfo;
	}

	public CICheckResInstitutionRiskInfoVo getInstitutionRiskInfo() {
		return institutionRiskInfo;
	}

	public void setInstitutionRiskInfo(CICheckResInstitutionRiskInfoVo institutionRiskInfo) {
		this.institutionRiskInfo = institutionRiskInfo;
	}

}
