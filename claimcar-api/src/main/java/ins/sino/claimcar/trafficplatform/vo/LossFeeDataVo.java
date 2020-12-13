package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("LossFeeData")
public class LossFeeDataVo implements Serializable{
	
private static final long serialVersionUID = 1L;
/**
 * 损失赔偿类型明细代码
 */
@XStreamAlias("FeeType")
private String feeType;

/**
 * 核损金额
 */
@XStreamAlias("UnderDefLoss")
private String underDefLoss;

public String getFeeType() {
	return feeType;
}

public void setFeeType(String feeType) {
	this.feeType = feeType;
}

public String getUnderDefLoss() {
	return underDefLoss;
}

public void setUnderDefLoss(String underDefLoss) {
	this.underDefLoss = underDefLoss;
}


}
