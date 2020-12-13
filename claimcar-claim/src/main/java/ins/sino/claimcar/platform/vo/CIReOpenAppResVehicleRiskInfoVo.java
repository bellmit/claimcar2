package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CIReOpenAppResVehicleRiskInfoVo {
	
	/** 车辆风险类型提示信息 **/
	@XmlElement(name = "VEHICLE_RISK_TYPE")
	private String vehicleRiskType;

	public String getVehicleRiskType() {
		return vehicleRiskType;
	}

	public void setVehicleRiskType(String vehicleRiskType) {
		this.vehicleRiskType = vehicleRiskType;
	}

}
