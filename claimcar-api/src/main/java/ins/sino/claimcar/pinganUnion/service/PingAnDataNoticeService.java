package ins.sino.claimcar.pinganUnion.service;

import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description 平安联盟接口对接，数据下发接口
 * @Author liuys
 * @Date 2020/7/20 11:44
 */
public interface PingAnDataNoticeService {

    /**
     * 保存及更新数据下发通知记录
     *
     * @param pingAnDataNoticeVo
     */
    PingAnDataNoticeVo saveOrUpdateDataNotice(PingAnDataNoticeVo pingAnDataNoticeVo);

    /**
     * 根据id查询数据
     *
     * @param id
     * @return
     */
    PingAnDataNoticeVo queryById(Long id);

    /**
     * description: getUntreatedDataNotice 获取未处理的平安下发通知数据
     * version: 1.0
     * date: 2020/8/3 16:04
     * author: lk
     *
     * @param
     */
    void untreatedDataNoticesHandle();

}
