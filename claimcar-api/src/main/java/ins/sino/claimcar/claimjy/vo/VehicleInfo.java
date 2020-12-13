package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "VehicleInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class VehicleInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "EngineType")
	private String engineType;//发动机型号
	@XmlElement(name = "FuelType")
	private String fuelType;//燃料类型 01- 汽油 02-柴油
	@XmlElement(name = "VehicleOrigin")
	private String vehicleOrigin;//车型产地 01- 国产 02-进口
	@XmlElement(name = "VehicleType")
	private String vehicleType;//定损车种  A-客车类 B-货车类 C-轿车类 D-其他车 E-摩托 F-挖掘机 G-起重机
	
	public String getEngineType() {
		return engineType;
	}
	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}
	public String getFuelType() {
		return fuelType;
	}
	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}
	public String getVehicleOrigin() {
		return vehicleOrigin;
	}
	public void setVehicleOrigin(String vehicleOrigin) {
		this.vehicleOrigin = vehicleOrigin;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
}
