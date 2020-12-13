package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CiLossResVehicleRiskInfoVo {

	@XmlElement(name = "VEHICLE_RISK_TYPE", required = true)
	private String vehicleRiskType;// 车辆风险类型，参见代码

	/**
	 * @return 返回 vehicleRiskType 车辆风险类型，参见代码
	 */
	public String getVehicleRiskType() {
		return vehicleRiskType;
	}

	/**
	 * @param vehicleRiskType 要设置的 车辆风险类型，参见代码
	 */
	public void setVehicleRiskType(String vehicleRiskType) {
		this.vehicleRiskType = vehicleRiskType;
	}

}
