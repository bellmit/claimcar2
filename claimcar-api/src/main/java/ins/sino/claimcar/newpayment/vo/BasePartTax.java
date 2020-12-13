package ins.sino.claimcar.newpayment.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 收付价税拆分数据推送理赔 数据实体
 *
 * @author maofengning
 * @date 2020/5/2 17:31
 */
public class BasePartTax implements Serializable {
    private static final long serialVersionUID = -5578731736527620113L;

    /** 结算单号 必录标识:Y 说明: */
    private String settleNo;
    /** 价税明细数据 必录标识:Y 说明: */
    private List<BasePartDetail> basePartDetails;

    public String getSettleNo() {
        return settleNo;
    }

    public void setSettleNo(String settleNo) {
        this.settleNo = settleNo;
    }

    public List<BasePartDetail> getBasePartDetails() {
        return basePartDetails;
    }

    public void setBasePartDetails(List<BasePartDetail> basePartDetails) {
        this.basePartDetails = basePartDetails;
    }
}
