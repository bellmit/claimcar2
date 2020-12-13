package ins.sino.claimcar.mobile.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("CARINFO")
public class CarInfoVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("IFOBJECT")
	private String ifObject; //类别
	
	@XStreamAlias("CARID")
	private String carId; //理赔车辆ID
	
	@XStreamAlias("SERIALNO")
	private String serialNo; //序号
	
	@XStreamAlias("CARSERIALNO")
    private String carSerialNo; //移动端车辆序号
	
	@XStreamAlias("LICENSENO")
	private String licenseNo; //车牌号
	
	@XStreamAlias("LICENSETYPE")
	private String licenseType; //车牌种类
	
	@XStreamAlias("ENGINENO")
	private String engineNo; //发动机号
	
	@XStreamAlias("FRAMENO")
	private String frameNo; //车架号
	
	@XStreamAlias("VIN")
	private String vin; //VIN码
	
	@XStreamAlias("INSURCOMCODE")
	private String insurComcode; //承保公司
	
	@XStreamAlias("REGISTEDATE")
	private String registeDate; //初次登记日期
	
	@XStreamAlias("OWERN")
	private String owern; //车主
	
	@XStreamAlias("LICENSECOLOR")
	private String licenseColor; //号牌底色
	
	@XStreamAlias("COLORCODE")
	private String colorCode; //车身颜色
	
	@XStreamAlias("VEHICLEMODLENAME")
	private String vehiclemodleName; //车型名称
	
	@XStreamAlias("DRIVER")
	private DriverVo driver; //驾驶员信息
	
	@XStreamAlias("CHECK")
	private CheckVo check; //查勘信息
	
    @XStreamAlias("CARKINDCODE")
    private String carKindCode; //车辆种类
    

	public String getCarKindCode() {
		return carKindCode;
	}

	public void setCarKindCode(String carKindCode) {
		this.carKindCode = carKindCode;
	}

	public String getIfObject() {
		return ifObject;
	}

	public void setIfObject(String ifObject) {
		this.ifObject = ifObject;
	}

	

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	
	
    public String getCarSerialNo() {
        return carSerialNo;
    }

    
    public void setCarSerialNo(String carSerialNo) {
        this.carSerialNo = carSerialNo;
    }

    public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getFrameNo() {
		return frameNo;
	}

	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getInsurComcode() {
		return insurComcode;
	}

	public void setInsurComcode(String insurComcode) {
		this.insurComcode = insurComcode;
	}

	public String getRegisteDate() {
		return registeDate;
	}

	public void setRegisteDate(String registeDate) {
		this.registeDate = registeDate;
	}

	public String getOwern() {
		return owern;
	}

	public void setOwern(String owern) {
		this.owern = owern;
	}

	public String getLicenseColor() {
		return licenseColor;
	}

	public void setLicenseColor(String licenseColor) {
		this.licenseColor = licenseColor;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	public String getVehiclemodleName() {
		return vehiclemodleName;
	}

	public void setVehiclemodleName(String vehiclemodleName) {
		this.vehiclemodleName = vehiclemodleName;
	}

	public DriverVo getDriver() {
		return driver;
	}

	public void setDriver(DriverVo driver) {
		this.driver = driver;
	}

	public CheckVo getCheck() {
		return check;
	}

	public void setCheck(CheckVo check) {
		this.check = check;
	}
	
	
	
}
