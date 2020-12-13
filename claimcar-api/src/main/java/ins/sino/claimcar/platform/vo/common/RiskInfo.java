package ins.sino.claimcar.platform.vo.common;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XmlAccessorType(XmlAccessType.FIELD)
public class RiskInfo {
		
		@XmlElement(name = "RiskSystemInfo")
		private String riskSystemInfo; // 系统提示信息
		
		@XStreamImplicit
		private List<VehicleRiskInfo> vehicleRiskInfo;
		
		@XStreamImplicit
		private List<PersonRiskInfo> personRiskInfo;
		
		@XStreamImplicit
		private List<InstituionRiskInfo> instituionRiskInfo;

		public String getRiskSystemInfo() {
			return riskSystemInfo;
		}

		public void setRiskSystemInfo(String riskSystemInfo) {
			this.riskSystemInfo = riskSystemInfo;
		}

		public List<VehicleRiskInfo> getVehicleRiskInfo() {
			return vehicleRiskInfo;
		}

		public void setVehicleRiskInfo(List<VehicleRiskInfo> vehicleRiskInfo) {
			this.vehicleRiskInfo = vehicleRiskInfo;
		}

		public List<PersonRiskInfo> getPersonRiskInfo() {
			return personRiskInfo;
		}

		public void setPersonRiskInfo(List<PersonRiskInfo> personRiskInfo) {
			this.personRiskInfo = personRiskInfo;
		}

		public List<InstituionRiskInfo> getInstituionRiskInfo() {
			return instituionRiskInfo;
		}

		public void setInstituionRiskInfo(List<InstituionRiskInfo> instituionRiskInfo) {
			this.instituionRiskInfo = instituionRiskInfo;
		}
}
