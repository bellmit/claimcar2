/******************************************************************************
 * CREATETIME : 2016年5月24日 下午2:46:18
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiEndCaseClaimCoverDataVo {

	@XmlElement(name = "LIABILITY_RATE", required = true)
	private String liabilityRate;// 赔偿责任比例

	@XmlElement(name = "CLAIM_FEE_TYPE", required = true)
	private String claimFeeType;// 损失赔偿类型代码；参见代码

	@XmlElement(name = "COVERAGE_CODE", required = true)
	private String coverageCode;// 赔偿险种代码；参见代码

	@XmlElement(name = "CLAIM_AMOUNT", required = true)
	private Double claimAmount;// 赔款金额

	@XmlElement(name = "RECOVERY_OR_PAY_FLAG", required = true)
	private String recoveryOrPayFlag;// 追偿/清付标志；参见代码

	@XmlElement(name = "SALVAGE_FEE")
	private Double salvageFee;// 施救费

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
	 * @return 返回 claimFeeType 损失赔偿类型代码；参见代码
	 */
	public String getClaimFeeType() {
		return claimFeeType;
	}

	/**
	 * @param claimFeeType 要设置的 损失赔偿类型代码；参见代码
	 */
	public void setClaimFeeType(String claimFeeType) {
		this.claimFeeType = claimFeeType;
	}

	/**
	 * @return 返回 coverageCode 赔偿险种代码；参见代码
	 */
	public String getCoverageCode() {
		return coverageCode;
	}

	/**
	 * @param coverageCode 要设置的 赔偿险种代码；参见代码
	 */
	public void setCoverageCode(String coverageCode) {
		this.coverageCode = coverageCode;
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
	 * @return 返回 recoveryOrPayFlag 追偿/清付标志；参见代码
	 */
	public String getRecoveryOrPayFlag() {
		return recoveryOrPayFlag;
	}

	/**
	 * @param recoveryOrPayFlag 要设置的 追偿/清付标志；参见代码
	 */
	public void setRecoveryOrPayFlag(String recoveryOrPayFlag) {
		this.recoveryOrPayFlag = recoveryOrPayFlag;
	}

	/**
	 * @return 返回 salvageFee 施救费
	 */
	public Double getSalvageFee() {
		return salvageFee;
	}

	/**
	 * @param salvageFee 要设置的 施救费
	 */
	public void setSalvageFee(Double salvageFee) {
		this.salvageFee = salvageFee;
	}

}
