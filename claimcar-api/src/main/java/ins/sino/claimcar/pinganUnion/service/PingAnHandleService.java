package ins.sino.claimcar.pinganUnion.service;

import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;

/**
 * @Description 平安联盟接口具体业务处理服务接口
 * @Author liuys
 * @Date 2020/7/21 8:37
 */
public interface PingAnHandleService {

    /**
     * 接口具体业务处理方法
     * @param registNo 内部报案号
     * @param respData 平安返回报文json
     * @param pingAnDataNoticeVo 通知对象
     * @return
     */
    ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData);
}
