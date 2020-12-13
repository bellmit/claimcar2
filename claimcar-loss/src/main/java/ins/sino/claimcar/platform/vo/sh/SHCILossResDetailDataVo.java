/******************************************************************************
 * CREATETIME : 2016年5月26日 下午6:43:27
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * <pre></pre>
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCILossResDetailDataVo {

	@XmlElement(name = "CLAIM_FEE_TYPE")
	private String claimFeeType;

	@XmlElement(name = "DETAIL_FEE_TYPE")
	private String detailFeeType;

	@XmlElement(name = "LOSS_AMOUNT")
	private Double lossAmount;

	@XmlElement(name = "CLAIM_AMOUNT")
	private Double claimAmount;

	@XmlElement(name = "LIABILITY_RATE")
	private String liabilityRate;

	public String getClaimFeeType() {
		return claimFeeType;
	}

	public void setClaimFeeType(String claimFeeType) {
		this.claimFeeType = claimFeeType;
	}

	public String getDetailFeeType() {
		return detailFeeType;
	}

	public void setDetailFeeType(String detailFeeType) {
		this.detailFeeType = detailFeeType;
	}

	public Double getLossAmount() {
		return lossAmount;
	}

	public void setLossAmount(Double lossAmount) {
		this.lossAmount = lossAmount;
	}

	public Double getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	public String getLiabilityRate() {
		return liabilityRate;
	}

	public void setLiabilityRate(String liabilityRate) {
		this.liabilityRate = liabilityRate;
	}

}
