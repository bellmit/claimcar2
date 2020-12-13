package ins.sino.claimcar.regist.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 交强报案请求平台信息ThirdVehicleDataVo类
 * @author dengkk
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiRegistThirdVehicleDataReqVo {
	
	@XmlElement(name="CAR_MARK")
	private String carMark;//三者车辆号牌号码
	@XmlElement(name="VEHICLE_TYPE")
	private String vehicleType;//三者车辆号牌种类；参见代码
	@XmlElement(name="DRIVER_NAME")
	private String driverName;//三者车辆驾驶员姓名
	public String getCarMark() {
		return carMark;
	}
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	
	
}
