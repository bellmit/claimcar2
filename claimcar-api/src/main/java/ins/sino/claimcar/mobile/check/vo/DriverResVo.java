package ins.sino.claimcar.mobile.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("DRIVER")
public class DriverResVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("DRIVERID")
	private String driverId; //理赔驾驶员ID
	
	@XStreamAlias("DRIVERSERIALNO")
	private String driverSerialNo; //移动终端驾驶员序号

    @XStreamAlias("CARID")
    private String carId; //理赔车辆ID
    
    public String getDriverId() {
        return driverId;
    }

    
    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    
    public String getDriverSerialNo() {
        return driverSerialNo;
    }

    
    public void setDriverSerialNo(String driverSerialNo) {
        this.driverSerialNo = driverSerialNo;
    }


    
    public String getCarId() {
        return carId;
    }


    
    public void setCarId(String carId) {
        this.carId = carId;
    }
	

	
}
