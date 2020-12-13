package ins.sino.claimcar.trafficplatform.vo;

import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 山东预警-结案登记-ClaimCoverData
 * <pre></pre>
 * @author ★WeiLanlei
 */
@XStreamAlias("ClaimCoverData")
public class EWEndCaseClaimCoverDataVo {
	
	private static final long serialVersionUID = 1L;

	@XStreamAlias("LiabilityRate")
	private BigDecimal liabilityRate;// 赔偿责任比例
	
	@XStreamAlias("LossFeeType")
	private String lossFeeType;// 损失赔偿类型
	
	@XStreamAlias("CoverageCode")
	private String coverageCode;// 赔偿险种代码

	@XStreamAlias("ClaimAmount")
	private BigDecimal claimAmount;// 赔款金额（含施救费）

	@XStreamAlias("SalvageFee")
	private BigDecimal salvageFee;// 施救费
	
	@XStreamAlias("IsDeviceItem")
	private String isDeviceItem;// 是否新增设备


	public String getCoverageCode() {
		return coverageCode;
	}

	
	public void setCoverageCode(String coverageCode) {
		this.coverageCode = coverageCode;
	}

	
	public String getLossFeeType() {
		return lossFeeType;
	}

	
	public void setLossFeeType(String lossFeeType) {
		this.lossFeeType = lossFeeType;
	}

	
	public BigDecimal getLiabilityRate() {
		return liabilityRate;
	}

	
	public void setLiabilityRate(BigDecimal liabilityRate) {
		this.liabilityRate = liabilityRate;
	}

	
	public BigDecimal getClaimAmount() {
		return claimAmount;
	}

	
	public void setClaimAmount(BigDecimal claimAmount) {
		this.claimAmount = claimAmount;
	}

	
	public BigDecimal getSalvageFee() {
		return salvageFee;
	}

	
	public void setSalvageFee(BigDecimal salvageFee) {
		this.salvageFee = salvageFee;
	}
	
	
	public String getIsDeviceItem() {
		return isDeviceItem;
	}


	public void setIsDeviceItem(String isDeviceItem) {
		this.isDeviceItem = isDeviceItem;
	}



}
