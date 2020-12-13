package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class BiLossResRiskInfoVo {

	@XmlElement(name = "RiskSystemInfo")
	private String riskSystemInfo;// 系统提示信息；参见代码

	@XmlElement(name = "VehicleRiskInfo")
	private List<BiLossResVehicleRiskInfoVo> vehicleRiskInfoVos;// 车辆风险类型提示信息

	@XmlElement(name = "PersonRiskInfo")
	private List<BiLossResPersonRiskInfoVo> personRiskInfoVos;// 人员风险类型提示信息

	@XmlElement(name = "InstituionRiskInfo")
	private List<BiLossResInstituionRiskInfoVo> instituionRiskInfoVos;// 机构风险类型提示信息

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

	public List<BiLossResVehicleRiskInfoVo> getVehicleRiskInfoVos() {
		return vehicleRiskInfoVos;
	}

	public void setVehicleRiskInfoVos(List<BiLossResVehicleRiskInfoVo> vehicleRiskInfoVos) {
		this.vehicleRiskInfoVos = vehicleRiskInfoVos;
	}

	public List<BiLossResPersonRiskInfoVo> getPersonRiskInfoVos() {
		return personRiskInfoVos;
	}

	public void setPersonRiskInfoVos(List<BiLossResPersonRiskInfoVo> personRiskInfoVos) {
		this.personRiskInfoVos = personRiskInfoVos;
	}

	public List<BiLossResInstituionRiskInfoVo> getInstituionRiskInfoVos() {
		return instituionRiskInfoVos;
	}

	public void setInstituionRiskInfoVos(List<BiLossResInstituionRiskInfoVo> instituionRiskInfoVos) {
		this.instituionRiskInfoVos = instituionRiskInfoVos;
	}

}
