/******************************************************************************
 * CREATETIME : 2016年5月26日 下午3:21:50
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
public class SHCILossReqPersonLossPartDataVo {

	@XmlElement(name = "FEE_TYPE")
	private String feeType;// 费用类型

	@XmlElement(name = "LOSS_AMOUNT")
	private String lossAmount;// 损失金额
	
	
	@XmlElement(name = "UnderDefLoss")
	private String underDefLoss; // 核损金额

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getLossAmount() {
		return lossAmount;
	}

	public void setLossAmount(String lossAmount) {
		this.lossAmount = lossAmount;
	}

	public String getUnderDefLoss() {
		return underDefLoss;
	}

	public void setUnderDefLoss(String underDefLoss) {
		this.underDefLoss = underDefLoss;
	}

}
