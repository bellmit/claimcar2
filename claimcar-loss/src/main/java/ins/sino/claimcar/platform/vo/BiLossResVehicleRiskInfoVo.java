package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlElement;

public class BiLossResVehicleRiskInfoVo {

	@XmlElement(name = "VehicleRiskType", required = true)
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
