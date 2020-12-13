package ins.sino.claimcar.trafficplatform.vo;

import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 山东预警-理算核赔登记-ClaimCoverData
 * <pre></pre>
 * @author ★WeiLanlei
 */
@XStreamAlias("ClaimCoverData")
public class EWVClaimClaimCoverDataVo {
	
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CoverageCode")
	private String coverageCode;// 赔偿险种代码

	@XStreamAlias("LossFeeType")
	private String lossFeeType;// 损失赔偿类型

	@XStreamAlias("LiabilityRate")
	private BigDecimal liabilityRate;// 赔偿责任比例

	@XStreamAlias("ClaimAmount")
	private BigDecimal claimAmount;// 赔款金额（含施救费）

	@XStreamAlias("SalvageFee")
	private BigDecimal salvageFee;// 施救费

	@XStreamAlias("LossAmount")
	private BigDecimal lossAmount;// 损失金额（不乘责任比例，不剔除免赔）
	
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

	
	public BigDecimal getLossAmount() {
		return lossAmount;
	}

	
	public void setLossAmount(BigDecimal lossAmount) {
		this.lossAmount = lossAmount;
	}

	
	
	public String getIsDeviceItem() {
		return isDeviceItem;
	}


	public void setIsDeviceItem(String isDeviceItem) {
		this.isDeviceItem = isDeviceItem;
	}


}
