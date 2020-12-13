package ins.sino.claimcar.mobile.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("CARINFO")
public class CarInfoResVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("IFOBJECT")
	private String ifObject; //类别
	
	@XStreamAlias("SERIALNO")
	private String serialNo; //序号

    @XStreamAlias("CARID")
    private String carId; //理赔车辆ID
    
    @XStreamAlias("CARSERIALNO")
    private String carSerialNo; //移动端车辆序号
    
	@XStreamAlias("DRIVER")
    private DriverResVo driver; //驾驶员信息
    
    @XStreamAlias("CHECK")
    private CheckResVo check; //查勘信息
    

	public String getIfObject() {
        return ifObject;
    }

    
    public void setIfObject(String ifObject) {
        this.ifObject = ifObject;
    }

    
    public String getSerialNo() {
        return serialNo;
    }

    
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    

    
    
    public String getCarId() {
        return carId;
    }


    
    public void setCarId(String carId) {
        this.carId = carId;
    }


    public DriverResVo getDriver() {
        return driver;
    }


    
    public void setDriver(DriverResVo driver) {
        this.driver = driver;
    }


    
    public CheckResVo getCheck() {
        return check;
    }


    
    public void setCheck(CheckResVo check) {
        this.check = check;
    }


    
    public String getCarSerialNo() {
        return carSerialNo;
    }


    
    public void setCarSerialNo(String carSerialNo) {
        this.carSerialNo = carSerialNo;
    }

	
}
