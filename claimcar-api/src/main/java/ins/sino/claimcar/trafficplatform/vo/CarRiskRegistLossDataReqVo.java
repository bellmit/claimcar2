package ins.sino.claimcar.trafficplatform.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * 山东预警报文
 * <pre></pre>
 * @author ★zhujunde
 */
@XStreamAlias("LossData")
public class CarRiskRegistLossDataReqVo {

    @XStreamAlias("LossFeeType")
	private String LossFeeType;

    
    public String getLossFeeType() {
        return LossFeeType;
    }

    
    public void setLossFeeType(String lossFeeType) {
        LossFeeType = lossFeeType;
    }

	
	
}
