package ins.sino.claimcar.claim.vo;

import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ThirdPartyDepreRate implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String licenseNo;

	/** 折旧率 */
	private double depreRate;

	/** 折旧车辆类型 */
	private String carKindCode;

	/** 新车购置价 */
	private double purchasePrice;

	/** 条款代码 */
	private String clauseType;

	/** 出险时车辆实际价值 */
	private double actualValue;
	/** 新增设备MAP key deviceId,value 0 deviceName,1  新设备购置价 2 出险时新设备实际价值 */
	private Map<Long,String[]> deviceMap = new HashMap<Long,String[]>();
	
	/** 新设备购置价,用于计算X险 */
	private double devicePurchasePrice;

	/** 出险时新设备实际价值 */
	private double deviceActualValue;

	/** 使用月数 */
	private int useMonths;

	/** 车辆信息 */
	private PrpLCItemCarVo prpLCItemCarVo;
	
	/** 使用年数 */
	private int useYears;

	private String policyNo;//保单号，防止规则并发串号增加校验使用
	private String registNo;//报案号，防止规则并发串号增加校验使用
	/** 座位个数 */
	private BigDecimal searCount;
	
	public double getDepreRate() {
		return depreRate;
	}
	
	public void setDepreRate(double depreRate) {
		this.depreRate = depreRate;
	}
	
	public String getCarKindCode() {
		return carKindCode;
	}
	
	public void setCarKindCode(String carKindCode) {
		this.carKindCode = carKindCode;
	}
	
	public double getPurchasePrice() {
		return purchasePrice;
	}
	
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	
	public double getDevicePurchasePrice() {
		return devicePurchasePrice;
	}
	
	public void setDevicePurchasePrice(double devicePurchasePrice) {
		this.devicePurchasePrice = devicePurchasePrice;
	}
	
	public String getClauseType() {
		return clauseType;
	}
	
	public void setClauseType(String clauseType) {
		this.clauseType = clauseType;
	}
	
	public double getActualValue() {
		return actualValue;
	}
	
	public void setActualValue(double actualValue) {
		this.actualValue = actualValue;
	}
	
	public double getDeviceActualValue() {
		return deviceActualValue;
	}
	
	public void setDeviceActualValue(double deviceActualValue) {
		this.deviceActualValue = deviceActualValue;
	}
	
	public int getUseMonths() {
		return useMonths;
	}
	
	public void setUseMonths(int useMonths) {
		this.useMonths = useMonths;
	}
	
	public PrpLCItemCarVo getPrpLCItemCarVo() {
		return prpLCItemCarVo;
	}
	
	public void setPrpLCItemCarVo(PrpLCItemCarVo prpLCItemCarVo) {
		this.prpLCItemCarVo = prpLCItemCarVo;
	}
	
	public int getUseYears() {
		return useYears;
	}
	
	public void setUseYears(int useYears) {
		this.useYears = useYears;
	}
	
	public String getPolicyNo() {
		return policyNo;
	}
	
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public String getRegistNo() {
		return registNo;
	}
	
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public Map<Long,String[]> getDeviceMap() {
		return deviceMap;
	}

	public void setDeviceMap(Map<Long,String[]> deviceMap) {
		this.deviceMap = deviceMap;
	}

	public BigDecimal getSearCount() {
		return searCount;
	}

	public void setSearCount(BigDecimal searCount) {
		this.searCount = searCount;
	}
	
}
