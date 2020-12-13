package ins.sino.claimcar.trafficplatform.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Body")
public class CarRiskCaseWriteReqBodyVo {

    @XStreamAlias("BaseInfo")
	private CarRiskCaseWriteReqBasePartVo basePart;

    
    public CarRiskCaseWriteReqBasePartVo getBasePart() {
        return basePart;
    }

    
    public void setBasePart(CarRiskCaseWriteReqBasePartVo basePart) {
        this.basePart = basePart;
    }

	
}
