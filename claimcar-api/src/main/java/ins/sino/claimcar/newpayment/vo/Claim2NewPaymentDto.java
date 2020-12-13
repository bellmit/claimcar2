package ins.sino.claimcar.newpayment.vo;

import java.io.Serializable;
import java.util.List;

/**
 *  理赔应收应付送收付最终对象，需要转成json
 * @author maofengning 2020年4月28日16:11:01
 */
public class Claim2NewPaymentDto implements Serializable {

    private static final long serialVersionUID = -272309516929888598L;

    /** 送收付主对象 */
    private List<PrpJlossPlanDto> prpJlossPlanDtos;

    public List<PrpJlossPlanDto> getPrpJlossPlanDtos() {
        return prpJlossPlanDtos;
    }

    public void setPrpJlossPlanDtos(List<PrpJlossPlanDto> prpJlossPlanDtos) {
        this.prpJlossPlanDtos = prpJlossPlanDtos;
    }
}
