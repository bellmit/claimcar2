package ins.sino.claimcar.pinganunion.vo.payCallback;

import java.io.Serializable;
import java.util.List;

/**
 * description: UnionPayCallbackRequestParamDto 平安联盟中心-支付结果回调请求参数  paramObj
 * date: 2020/8/18 8:48
 * author: lk
 * version: 1.0
 */
public class UnionPayCallbackRequestParamDto implements Serializable {

    private static final long serialVersionUID = 4356860690175770392L;
    /**
     * 支付结果回调列表    是否非空：Y  编码：N
     */
    private List<UnionPayCallbackRequestListDto> list;

    public List<UnionPayCallbackRequestListDto> getList() {
        return list;
    }

    public void setList(List<UnionPayCallbackRequestListDto> list) {
        this.list = list;
    }
}
