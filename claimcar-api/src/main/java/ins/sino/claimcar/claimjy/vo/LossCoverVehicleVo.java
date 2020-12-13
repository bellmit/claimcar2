package ins.sino.claimcar.claimjy.vo;


import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("LossCoverVehicle")
public class LossCoverVehicleVo implements Serializable {
	private static final long serialVersionUID = -2295952364468613037L;
	@XStreamAlias("Id")
	private String id;
	@XStreamAlias("RealPrice")
	private String realPrice;
	@XStreamAlias("VehiclePrice")
	private String vehiclePrice;
	@XStreamAlias("InsuredAmount")
	private String insuredAmount;
	@XStreamAlias("IsImport")
	private String isImport;
	@XStreamAlias("VehicleType")
	private String vehicleType;
	@XStreamAlias("VehicleTypeName")
	private String vehicleTypeName;
	@XStreamAlias("CarColor")
	private String carColor;
	@XStreamAlias("CarColorName")
	private String carColorName;
	@XStreamAlias("EnrolDate")
	private String enrolDate;
	@XStreamAlias("UseProperty")
	private String useProperty;
	@XStreamAlias("Seat")
	private String seat;
	@XStreamAlias("VinNo")
	private String vinNo;
	@XStreamAlias("EngineNo")
	private String engineNo;
	@XStreamAlias("VehicleModel")
	private String vehicleModel;
	@XStreamAlias("PlateNum")
	private String plateNum;
	@XStreamAlias("Power")
	private String power;
	@XStreamAlias("Displacement")
	private String displacement;
	@XStreamAlias("Tonnage")
	private String tonnage;
	@XStreamAlias("PlateColor")
	private String plateColor;
	@XStreamAlias("CreateTime")
	private String createTime;
	@XStreamAlias("MakeDate")
	private String makeDate;
	@XStreamAlias("GuardAlarm")
	private String guardAlarm;
	@XStreamAlias("ExemptFlag")
	private String exemptFlag;
	@XStreamAlias("BelongProperty")
	private String belongProperty;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(String realPrice) {
		this.realPrice = realPrice;
	}

	public String getVehiclePrice() {
		return vehiclePrice;
	}

	public void setVehiclePrice(String vehiclePrice) {
		this.vehiclePrice = vehiclePrice;
	}

	public String getInsuredAmount() {
		return insuredAmount;
	}

	public void setInsuredAmount(String insuredAmount) {
		this.insuredAmount = insuredAmount;
	}

	public String getIsImport() {
		return isImport;
	}

	public void setIsImport(String isImport) {
		this.isImport = isImport;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehicleTypeName() {
		return vehicleTypeName;
	}

	public void setVehicleTypeName(String vehicleTypeName) {
		this.vehicleTypeName = vehicleTypeName;
	}

	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public String getCarColorName() {
		return carColorName;
	}

	public void setCarColorName(String carColorName) {
		this.carColorName = carColorName;
	}

	public String getEnrolDate() {
		return enrolDate;
	}

	public void setEnrolDate(String enrolDate) {
		this.enrolDate = enrolDate;
	}

	public String getUseProperty() {
		return useProperty;
	}

	public void setUseProperty(String useProperty) {
		this.useProperty = useProperty;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public String getVinNo() {
		return vinNo;
	}

	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public String getPlateNum() {
		return plateNum;
	}

	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}

	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}

	public String getDisplacement() {
		return displacement;
	}

	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}

	public String getTonnage() {
		return tonnage;
	}

	public void setTonnage(String tonnage) {
		this.tonnage = tonnage;
	}

	public String getPlateColor() {
		return plateColor;
	}

	public void setPlateColor(String plateColor) {
		this.plateColor = plateColor;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(String makeDate) {
		this.makeDate = makeDate;
	}

	public String getGuardAlarm() {
		return guardAlarm;
	}

	public void setGuardAlarm(String guardAlarm) {
		this.guardAlarm = guardAlarm;
	}

	public String getExemptFlag() {
		return exemptFlag;
	}

	public void setExemptFlag(String exemptFlag) {
		this.exemptFlag = exemptFlag;
	}

	public String getBelongProperty() {
		return belongProperty;
	}

	public void setBelongProperty(String belongProperty) {
		this.belongProperty = belongProperty;
	}


}