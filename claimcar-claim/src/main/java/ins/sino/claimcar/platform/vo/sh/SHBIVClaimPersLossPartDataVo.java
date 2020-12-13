/******************************************************************************
 * CREATETIME : 2016年6月1日 下午5:02:33
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIVClaimPersLossPartDataVo {

	@XmlElement(name = "FEE_TYPE", required = true)
	private String feeType;// 费用类型

	@XmlElement(name = "LOSS_AMOUNT")
	private Double lossAmount;// 损失金额

	@XmlElement(name = "INJURY_TYPE", required = true)
	private String injuryType;// 伤情类别

	@XmlElement(name = "INJURY_LEVEL", required = true)
	private String injuryLevel;// 伤残程度

	@XmlElement(name = "INJURY_PART")
	private String injuryPart;// 受伤部位

	@XmlElement(name = "HOSPITAL")
	private String hospital;// 治疗机构名称

	@XmlElement(name = "UnderDefLoss")
	private double underDefLoss; // 核损金额
	/**
	 * @return 返回 feeType 费用类型
	 */
	public String getFeeType() {
		return feeType;
	}

	/**
	 * @param feeType 要设置的 费用类型
	 */
	public void setFeeType(String feeType) {
		this.feeType = feeType;
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
	 * @return 返回 injuryType 伤情类别
	 */
	public String getInjuryType() {
		return injuryType;
	}

	/**
	 * @param injuryType 要设置的 伤情类别
	 */
	public void setInjuryType(String injuryType) {
		this.injuryType = injuryType;
	}

	/**
	 * @return 返回 injuryLevel 伤残程度
	 */
	public String getInjuryLevel() {
		return injuryLevel;
	}

	/**
	 * @param injuryLevel 要设置的 伤残程度
	 */
	public void setInjuryLevel(String injuryLevel) {
		this.injuryLevel = injuryLevel;
	}

	/**
	 * @return 返回 injuryPart 受伤部位
	 */
	public String getInjuryPart() {
		return injuryPart;
	}

	/**
	 * @param injuryPart 要设置的 受伤部位
	 */
	public void setInjuryPart(String injuryPart) {
		this.injuryPart = injuryPart;
	}

	/**
	 * @return 返回 hospital 治疗机构名称
	 */
	public String getHospital() {
		return hospital;
	}

	/**
	 * @param hospital 要设置的 治疗机构名称
	 */
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public double getUnderDefLoss() {
		return underDefLoss;
	}

	public void setUnderDefLoss(double underDefLoss) {
		this.underDefLoss = underDefLoss;
	}

}
