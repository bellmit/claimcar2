package ins.sino.claimcar.pinganUnion.service;

import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;

/**
 * 平安联盟中心-理算通知接口数据处理
 *
 * @author mfn
 * @date 2020/8/5 14:45
 */
public interface PingAnCompensateHandleService {
    /**
     * 接口具体业务处理方法
     * @param pingAnDataNoticeVo 理算查询接口请求参数（包含支付信息查询接口请求参数）
     * @return
     */
    ResultBean pingAnHandle(PingAnDataNoticeVo pingAnDataNoticeVo);
}
