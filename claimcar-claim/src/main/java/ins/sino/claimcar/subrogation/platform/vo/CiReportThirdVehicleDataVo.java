/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//三者车辆情况列表(隶属于报案信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiReportThirdVehicleDataVo {

	/** 三者车辆号牌号码 **/ 
	@XmlElement(name="CAR_MARK")
	private String carMark; 

	/** 损失车辆号牌种类代码 **/ 
	@XmlElement(name="VEHICLE_TYPE")
	private String vehicleType; 

	/** 三者车辆驾驶员姓名 **/ 
	@XmlElement(name="DRIVER_NAME")
	private String driverName;

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
