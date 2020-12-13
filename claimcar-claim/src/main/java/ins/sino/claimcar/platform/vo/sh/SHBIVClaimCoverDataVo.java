/******************************************************************************
 * CREATETIME : 2016年6月1日 下午4:57:50
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIVClaimCoverDataVo {

	@XmlElement(name = "POLICY_NO")
	private String policyNo;// 保单号

	@XmlElement(name = "LIABILITY_RATE")
	private String liabilityRate;// 赔偿责任比例

	@XmlElement(name = "CLAIM_FEE_TYPE", required = true)
	private String claimFeeType;// 损失赔偿类型

	@XmlElement(name = "COVERAGE_TYPE", required = true)
	private String coverageType;// 赔偿险种类型

	@XmlElement(name = "COVERAGE_CODE", required = true)
	private String coverageCode;// 赔偿平台险种代码

	@XmlElement(name = "COM_COVERAGE_CODE")
	private String comCoverageCode;// 赔偿公司险种代码

	@XmlElement(name = "LOSS_AMOUNT")
	private Double lossAmount;// 估损金额未决估计赔款

	@XmlElement(name = "CLAIM_AMOUNT")
	private Double claimAmount;// 赔款金额

	@XmlElement(name = "SalvageFee")
	private Double salvagefee;// 施救费
	
	@XmlElement(name = "IsDeviceItem")
	private String IsDeviceItem;//是否新增设备

	/**
	 * @return 返回 policyNo 保单号
	 */
	public String getPolicyNo() {
		return policyNo;
	}

	/**
	 * @param policyNo 要设置的 保单号
	 */
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	/**
	 * @return 返回 liabilityRate 赔偿责任比例
	 */
	public String getLiabilityRate() {
		return liabilityRate;
	}

	/**
	 * @param liabilityRate 要设置的 赔偿责任比例
	 */
	public void setLiabilityRate(String liabilityRate) {
		this.liabilityRate = liabilityRate;
	}

	/**
	 * @return 返回 claimFeeType 损失赔偿类型
	 */
	public String getClaimFeeType() {
		return claimFeeType;
	}

	/**
	 * @param claimFeeType 要设置的 损失赔偿类型
	 */
	public void setClaimFeeType(String claimFeeType) {
		this.claimFeeType = claimFeeType;
	}

	/**
	 * @return 返回 coverageType 赔偿险种类型
	 */
	public String getCoverageType() {
		return coverageType;
	}

	/**
	 * @param coverageType 要设置的 赔偿险种类型
	 */
	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}

	/**
	 * @return 返回 coverageCode 赔偿平台险种代码
	 */
	public String getCoverageCode() {
		return coverageCode;
	}

	/**
	 * @param coverageCode 要设置的 赔偿平台险种代码
	 */
	public void setCoverageCode(String coverageCode) {
		this.coverageCode = coverageCode;
	}

	/**
	 * @return 返回 comCoverageCode 赔偿公司险种代码
	 */
	public String getComCoverageCode() {
		return comCoverageCode;
	}

	/**
	 * @param comCoverageCode 要设置的 赔偿公司险种代码
	 */
	public void setComCoverageCode(String comCoverageCode) {
		this.comCoverageCode = comCoverageCode;
	}

	/**
	 * @return 返回 lossAmount 估损金额未决估计赔款
	 */
	public Double getLossAmount() {
		return lossAmount;
	}

	/**
	 * @param lossAmount 要设置的 估损金额未决估计赔款
	 */
	public void setLossAmount(Double lossAmount) {
		this.lossAmount = lossAmount;
	}

	/**
	 * @return 返回 claimAmount 赔款金额
	 */
	public Double getClaimAmount() {
		return claimAmount;
	}

	/**
	 * @param claimAmount 要设置的 赔款金额
	 */
	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	/**
	 * @return 返回 salvagefee 施救费
	 */
	public Double getSalvagefee() {
		return salvagefee;
	}

	/**
	 * @param salvagefee 要设置的 施救费
	 */
	public void setSalvagefee(Double salvagefee) {
		this.salvagefee = salvagefee;
	}

	public String getIsDeviceItem() {
		return IsDeviceItem;
	}
	/**
	 * @param salvagefee 是否新增设备
	 */
	public void setIsDeviceItem(String isDeviceItem) {
		IsDeviceItem = isDeviceItem;
	}

	
}
