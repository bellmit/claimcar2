package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BiLossPersonLossFeeDataVo {

	@XmlElement(name = "FeeType", required = true)
	private String feeType;// 损失赔偿类型明细代码；参见代码

	@XmlElement(name = "UnderDefLoss", required = true)
	private String underDefLoss;// 核损金额

	/**
	 * @return 返回 feeType 损失赔偿类型明细代码；参见代码
	 */
	public String getFeeType() {
		return feeType;
	}

	/**
	 * @param feeType 要设置的 损失赔偿类型明细代码；参见代码
	 */
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	/**
	 * @return 返回 underDefLoss 核损金额
	 */
	public String getUnderDefLoss() {
		return underDefLoss;
	}

	/**
	 * @param underDefLoss 要设置的 核损金额
	 */
	public void setUnderDefLoss(String underDefLoss) {
		this.underDefLoss = underDefLoss;
	}

}
