package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CiLossResRiskInfoVo {

	@XmlElement(name = "RISK_TYPE", required = true)
	private String riskType;// 风险提示代码；参见代码

	@XmlElement(name = "RISK_SYSTEM_INFO")
	private String riskSystemInfo;// 系统提示信息；参见代码

	@XmlElement(name = "VEHICLE_RISK_INFO")
	private List<CiLossResVehicleRiskInfoVo> vehicleRiskInfos;// 车辆风险类型提示信息

	@XmlElement(name = "PERSON_RISK_INFO")
	private List<CiLossResPersonRiskInfoVo> personRiskInfos;// 人员风险类型提示信息

	@XmlElement(name = "INSTITUTION_RISK_INFO")
	private List<CiLossResInstitutionRiskInfoVo> institutionRiskInfos;// 机构风险类型提示信息

	/**
	 * @return 返回 riskType 风险提示代码；参见代码
	 */
	public String getRiskType() {
		return riskType;
	}

	/**
	 * @param riskType 要设置的 风险提示代码；参见代码
	 */
	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}

	/**
	 * @return 返回 riskSystemInfo 系统提示信息；参见代码
	 */
	public String getRiskSystemInfo() {
		return riskSystemInfo;
	}

	/**
	 * @param riskSystemInfo 要设置的 系统提示信息；参见代码
	 */
	public void setRiskSystemInfo(String riskSystemInfo) {
		this.riskSystemInfo = riskSystemInfo;
	}

	public List<CiLossResVehicleRiskInfoVo> getVehicleRiskInfos() {
		return vehicleRiskInfos;
	}

	public void setVehicleRiskInfos(List<CiLossResVehicleRiskInfoVo> vehicleRiskInfos) {
		this.vehicleRiskInfos = vehicleRiskInfos;
	}

	public List<CiLossResPersonRiskInfoVo> getPersonRiskInfos() {
		return personRiskInfos;
	}

	public void setPersonRiskInfos(List<CiLossResPersonRiskInfoVo> personRiskInfos) {
		this.personRiskInfos = personRiskInfos;
	}

	public List<CiLossResInstitutionRiskInfoVo> getInstitutionRiskInfos() {
		return institutionRiskInfos;
	}

	public void setInstitutionRiskInfos(List<CiLossResInstitutionRiskInfoVo> institutionRiskInfos) {
		this.institutionRiskInfos = institutionRiskInfos;
	}

}
