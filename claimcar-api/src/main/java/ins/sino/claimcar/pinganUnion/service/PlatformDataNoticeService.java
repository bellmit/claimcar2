package ins.sino.claimcar.pinganUnion.service;

import ins.sino.claimcar.pinganUnion.vo.PlatformDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;

/**
 * @Description 平安联盟接口对接，上传平台报文推送服务接口
 * @Author liuys
 * @Date 2020/7/20 15:57
 */
public interface PlatformDataNoticeService {

    PlatformDataNoticeVo saveOrUpdatePlatformDataNotice(PlatformDataNoticeVo platformDataNoticeVo);

    /**
     * 请求平台上传报文
     * @param platformDataNoticeVo
     */
    ResultBean requestPlatform(PlatformDataNoticeVo platformDataNoticeVo);
}
