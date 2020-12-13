package ins.sino.claimcar.newpayment.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 支付信息修改接口
 * @author maofengning 2020年4月29日14:58:08
 */
public class ModifyPaymentInfoDto implements Serializable {

    private static final long serialVersionUID = 7592642143777737286L;

    /** 退票送收付数据封装 */
    private List<PrpjPayForCapitalDetailDto> payForCapitalDetailDtoList;

    public List<PrpjPayForCapitalDetailDto> getPayForCapitalDetailDtoList() {
        return payForCapitalDetailDtoList;
    }

    public void setPayForCapitalDetailDtoList(List<PrpjPayForCapitalDetailDto> payForCapitalDetailDtoList) {
        this.payForCapitalDetailDtoList = payForCapitalDetailDtoList;
    }
}
