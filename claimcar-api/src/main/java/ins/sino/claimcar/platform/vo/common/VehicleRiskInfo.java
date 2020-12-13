package ins.sino.claimcar.platform.vo.common;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("VehicleRiskInfo")
public class VehicleRiskInfo {

	@XStreamAlias("VehicleRiskType")
	private String vehicleRiskType;
	
	public String getVehicleRiskType() {
		return vehicleRiskType;
	}

	public void setVehicleRiskType(String vehicleRiskType) {
		this.vehicleRiskType = vehicleRiskType;
	} 

}
