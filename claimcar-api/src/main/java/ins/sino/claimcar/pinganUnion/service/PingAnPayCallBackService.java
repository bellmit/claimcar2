package ins.sino.claimcar.pinganUnion.service;

import java.util.Map;

/**
 * description: PingAnPayCallBackService 平安联盟 支付结果回调数据处理
 * date: 2020/8/18 10:35
 * author: lk
 * version: 1.0
 */
public interface PingAnPayCallBackService {
    /**
     * description: payCallBackDataBuild 平安支付结果回调数据组装
     * version: 1.0
     * date: 2020/8/18 15:43
     * author: objcat
     *
     * @param callBackDataMap 回调数据
     * @return void
     */
    void payCallBackDataBuild(Map<String, Object> callBackDataMap);
}
