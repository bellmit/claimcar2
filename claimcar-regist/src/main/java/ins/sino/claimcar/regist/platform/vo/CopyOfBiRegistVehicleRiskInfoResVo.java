package ins.sino.claimcar.regist.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/*
 * 车辆风险类型提示信息
 * niuqiang
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CopyOfBiRegistVehicleRiskInfoResVo{
	@XmlElement(name="VehicleRiskType", required = true)
	private String vehicleRiskType;

	public String getVehicleRiskType() {
		return vehicleRiskType;
	}

	public void setVehicleRiskType(String vehicleRiskType) {
		this.vehicleRiskType = vehicleRiskType;
	}

}
