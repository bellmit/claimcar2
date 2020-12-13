package ins.sino.claimcar.regist.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
/**
 * 
 * <pre></pre>
 * @author ★niuqiang
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiRegistRiskInfoResVo {
	@XmlElement(name="RiskSystemInfo", required = true)
	private String riskSystemInfo;
	
	@XmlElement(name = "VehicleRiskInfo")
	private List<BiRegistVehicleRiskInfoResVo> vehicleRiskInfo;
	
	@XmlElement(name = "PersonRiskInfo")
	private List<BiRegistPersonRiskInfoResVo> personRiskInfo;
	
	@XmlElement(name = "InstituionRiskInfo")
	private List<BiRegistInstituionRiskInfoResVo> instituionRiskInfo;

	public String getRiskSystemInfo() {
		return riskSystemInfo;
	}

	public List<BiRegistVehicleRiskInfoResVo> getVehicleRiskInfo() {
		return vehicleRiskInfo;
	}

	public void setVehicleRiskInfo(
			List<BiRegistVehicleRiskInfoResVo> vehicleRiskInfo) {
		this.vehicleRiskInfo = vehicleRiskInfo;
	}

	public List<BiRegistPersonRiskInfoResVo> getPersonRiskInfo() {
		return personRiskInfo;
	}

	public void setPersonRiskInfo(List<BiRegistPersonRiskInfoResVo> personRiskInfo) {
		this.personRiskInfo = personRiskInfo;
	}

	public List<BiRegistInstituionRiskInfoResVo> getInstituionRiskInfo() {
		return instituionRiskInfo;
	}

	public void setInstituionRiskInfo(
			List<BiRegistInstituionRiskInfoResVo> instituionRiskInfo) {
		this.instituionRiskInfo = instituionRiskInfo;
	}

	public void setRiskSystemInfo(String riskSystemInfo) {
		this.riskSystemInfo = riskSystemInfo;
	}


}
