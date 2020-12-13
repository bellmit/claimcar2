package ins.sino.claimcar.trafficplatform.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 山东预警报文
 * <pre></pre>
 * @author ★zhujunde
 */
@XStreamAlias("ThirdVehicleData")
public class CarRiskRegistThirdVehicleDataReqVo {
	
    @XStreamAlias("LicensePlateNo")
	private String licensePlateNo;//三者车辆号牌号码
    @XStreamAlias("LicensePlateType")
	private String licensePlateType;//三者车辆号牌种类；参见代码
    @XStreamAlias("DriverName")
	private String driverName;//三者车辆驾驶员姓名
    
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
    
    public String getDriverName() {
        return driverName;
    }
    
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
	
}
