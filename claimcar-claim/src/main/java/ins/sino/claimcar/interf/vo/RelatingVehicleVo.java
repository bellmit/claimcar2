package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/*本案其它涉案车辆*/
@XStreamAlias("RelatingVehicle")
public class RelatingVehicleVo {
	/*定损信息*/
	@XStreamAlias("ConfirmLoss")
	private ConfirmLossVo confirmLossVo;
	/*车辆信息*/
	@XStreamAlias("Vehicle")
	private VehicleVo vehicleVo;
	public ConfirmLossVo getConfirmLossVo() {
		return confirmLossVo;
	}
	public void setConfirmLossVo(ConfirmLossVo confirmLossVo) {
		this.confirmLossVo = confirmLossVo;
	}
	public VehicleVo getVehicleVo() {
		return vehicleVo;
	}
	public void setVehicleVo(VehicleVo vehicleVo) {
		this.vehicleVo = vehicleVo;
	}
	

}
