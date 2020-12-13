/******************************************************************************
 * CREATETIME : 2016年4月29日 下午3:44:49
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 商业-->返回报文-->风险信息-->车辆风险类型（车辆风险类型提示信息）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BICheckResVehicleRiskInfoVo {

	@XmlElement(name = "VehicleRiskType")
	private String vehicleRiskType;// 车辆风险类型

	
	public String getVehicleRiskType() {
		return vehicleRiskType;
	}

	public void setVehicleRiskType(String vehicleRiskType) {
		this.vehicleRiskType = vehicleRiskType;
	}

}
