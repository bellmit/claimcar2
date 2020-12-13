package ins.sino.claimcar.pinganunion.vo.payment;

import java.io.Serializable;
import java.util.List;

/**
 * 平安联盟中心-支付信息查询接口-查询结果
 *
 * @author mfn
 * @date 2020/8/6 15:25
 */
public class UnionPaymentResponseDataDto implements Serializable {
    private static final long serialVersionUID = -5275869963099007050L;
    /**  支付信息列表    是否非空：N  编码：N */
    private List<UnionPaymentResultDto> paymentResultList;

    public List<UnionPaymentResultDto> getPaymentResultList() {
        return paymentResultList;
    }

    public void setPaymentResultList(List<UnionPaymentResultDto> paymentResultList) {
        this.paymentResultList = paymentResultList;
    }
}
