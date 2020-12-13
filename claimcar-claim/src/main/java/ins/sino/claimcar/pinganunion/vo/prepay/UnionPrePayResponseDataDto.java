package ins.sino.claimcar.pinganunion.vo.prepay;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * 联盟预付响应数据
 *
 * @author mfn
 * @date 2020/7/20 18:25
 */

public class UnionPrePayResponseDataDto implements Serializable {

    private static final long serialVersionUID = 5422748501449158681L;

    /**  预赔信息    是否非空：N  编码：N */
    private UnionPrePayInfo prepayInfo;
    /**  预赔对象列表    是否非空：N  编码：N */
    private List<UnionPrePayChannelInfo> prepayChannelInfoList;
    /**  预赔金额列表    是否非空：N  编码：N */
    private List<UnionPrePayMoney> prepayMoneyList;
    /**  预赔费用列表    是否非空：N  编码：N */
    private List<UnionPrePayFeeItem> feeItemList;

    public UnionPrePayInfo getPrepayInfo() {
        return prepayInfo;
    }

    public void setPrepayInfo(UnionPrePayInfo prepayInfo) {
        this.prepayInfo = prepayInfo;
    }

    public List<UnionPrePayChannelInfo> getPrepayChannelInfoList() {
        return prepayChannelInfoList;
    }

    public void setPrepayChannelInfoList(List<UnionPrePayChannelInfo> prepayChannelInfoList) {
        this.prepayChannelInfoList = prepayChannelInfoList;
    }

    public List<UnionPrePayMoney> getPrepayMoneyList() {
        return prepayMoneyList;
    }

    public void setPrepayMoneyList(List<UnionPrePayMoney> prepayMoneyList) {
        this.prepayMoneyList = prepayMoneyList;
    }

    public List<UnionPrePayFeeItem> getFeeItemList() {
        return feeItemList;
    }

    public void setFeeItemList(List<UnionPrePayFeeItem> feeItemList) {
        this.feeItemList = feeItemList;
    }
}
