package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;

/**
 *  三者车辆情况列表(隶属于报案信息)
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class ReportThirdVehicleDataVo implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 三者车辆号牌号码 **/ 
	private String carMark; 

	/** 损失车辆号牌种类代码 **/ 
	private String vehicleType; 

	/** 三者车辆驾驶员姓名 **/ 
	private String driverName;

	private String licensePlateNo;//三者车辆号牌号码

	private String licensePlateType;//三者车辆号牌种类代码；参见代码
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

	public String getLicensePlateNo() {
		return licensePlateNo;
	}

	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo = licensePlateNo;
	}

	public String getLicensePlateType() {
		return licensePlateType;
	}

	public void setLicensePlateType(String licensePlateType) {
		this.licensePlateType = licensePlateType;
	}

}
