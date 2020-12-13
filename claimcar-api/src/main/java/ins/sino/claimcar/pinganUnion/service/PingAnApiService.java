package ins.sino.claimcar.pinganUnion.service;

import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;

/**
 * @Description 平安联盟接口请求服务类
 * @Author liuys
 * @Date 2020/7/20 18:49
 */
public interface PingAnApiService {
    /**
     * 平安联盟接口请求入口
     * @param pingAnDataNoticeVo
     * @return
     */
    ResultBean service(PingAnDataNoticeVo pingAnDataNoticeVo);
}
