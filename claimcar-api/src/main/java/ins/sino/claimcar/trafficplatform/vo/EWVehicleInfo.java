package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Vehicle")
public class EWVehicleInfo  implements Serializable{

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("VehicleProperty")
	private String vehicleProperty;
	
	@XStreamAlias("LicensePlateNo")
	private String licensePlateNo;
	
	@XStreamAlias("VIN")
	private String vin;

	public String getVehicleProperty() {
		return vehicleProperty;
	}

	public void setVehicleProperty(String vehicleProperty) {
		this.vehicleProperty = vehicleProperty;
	}

	public String getLicensePlateNo() {
		return licensePlateNo;
	}

	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo = licensePlateNo;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}
	
}
