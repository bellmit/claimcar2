package ins.sino.claimcar.claimMarketing.service;

import ins.sino.claimcar.claimMarketing.vo.ClaimMarketingQueryVo;
import ins.sino.claimcar.claimMarketing.vo.ClaimMarketingResponseDataVo;

/**
 * description: ClaimMarketingService 销管系统查询理赔信息接口(对外)
 * date: 2020/9/25 16:32
 * author: lk
 * version: 1.0
 */
public interface ClaimMarketingService {
    ClaimMarketingResponseDataVo getClaimInfo(ClaimMarketingQueryVo queryVo);
}
