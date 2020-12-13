package ins.sino.claimcar.pinganUnion.service;

import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;

/**
 * 平安联盟中心-支付信息处理
 *
 * @author mfn
 * @date 2020/8/5 14:45
 */
public interface PingAnPaymentService {
	/**
     * 接口具体业务处理方法
     * @param registNo 内部报案号
     * @param respData 平安返回报文json
     * @param pingAnDataNoticeVo 通知对象
     * @param compensateNo 鼎和计算书号
     * @return
     */
    ResultBean handle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData, String compensateNo);
}
