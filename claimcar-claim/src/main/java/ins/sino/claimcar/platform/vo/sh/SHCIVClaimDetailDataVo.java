/******************************************************************************
 * CREATETIME : 2016年6月1日 上午10:58:05
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 上海损失赔偿情况明细
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIVClaimDetailDataVo {

	@XmlElement(name = "CLAIM_FEE_TYPE", required = true)
	private String claimFeeType;// 损失赔偿类型

	@XmlElement(name = "DETAIL_FEE_TYPE", required = true)
	private String detailFeeType;// 损失赔偿类型细分

	@XmlElement(name = "LOSS_AMOUNT")
	private Double lossAmount;// 损失金额

	@XmlElement(name = "CLAIM_AMOUNT")
	private Double claimAmount;// 赔偿金额

	@XmlElement(name = "LIABILITY_RATE")
	private String liabilityRate;// 赔偿责任比例

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
	 * @return 返回 detailFeeType 损失赔偿类型细分
	 */
	public String getDetailFeeType() {
		return detailFeeType;
	}

	/**
	 * @param detailFeeType 要设置的 损失赔偿类型细分
	 */
	public void setDetailFeeType(String detailFeeType) {
		this.detailFeeType = detailFeeType;
	}

	/**
	 * @return 返回 lossAmount 损失金额
	 */
	public Double getLossAmount() {
		return lossAmount;
	}

	/**
	 * @param lossAmount 要设置的 损失金额
	 */
	public void setLossAmount(Double lossAmount) {
		this.lossAmount = lossAmount;
	}

	/**
	 * @return 返回 claimAmount 赔偿金额
	 */
	public Double getClaimAmount() {
		return claimAmount;
	}

	/**
	 * @param claimAmount 要设置的 赔偿金额
	 */
	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
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

}
