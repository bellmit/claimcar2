package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BIReOpenAppResVehicleRiskInfoVo {
	
	@XmlElement(name = "VehicleRiskType")
	private String vehicleRiskType;// 车辆风险类型

	
	public String getVehicleRiskType() {
		return vehicleRiskType;
	}

	public void setVehicleRiskType(String vehicleRiskType) {
		this.vehicleRiskType = vehicleRiskType;
	}

}
