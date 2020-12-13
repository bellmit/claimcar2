/******************************************************************************
 * CREATETIME : 2016年6月1日 上午10:32:47
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 人员损失费用明细（多条）LOSS_PART_LIST（隶属于人员损失情况）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIEndCaseAddReqPersonLossPartDataVo {

	@XmlElement(name = "FEE_TYPE")
	private String feeType;// 费用类型

	@XmlElement(name = "LOSS_AMOUNT")
	private Double lossAmount;// 损失金额
	
	/*牛强  2017-03-15 改*/
	@XmlElement(name="UnderDefLoss")
	private double underDefLoss; //核损金额

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public Double getLossAmount() {
		return lossAmount;
	}

	public void setLossAmount(Double lossAmount) {
		this.lossAmount = lossAmount;
	}

	public double getUnderDefLoss() {
		return underDefLoss;
	}

	public void setUnderDefLoss(double underDefLoss) {
		this.underDefLoss = underDefLoss;
	}
}
