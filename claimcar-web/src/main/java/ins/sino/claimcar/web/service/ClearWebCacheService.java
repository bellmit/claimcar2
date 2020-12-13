package ins.sino.claimcar.web.service;

/**
 * description: ClearWebCacheService
 * date: 2020/12/8 15:35
 * author: lk
 * version: 1.0
 */
public interface ClearWebCacheService {
    /**
     * 清除其他模块缓存
     * @param type 1-清除字典缓存 2-清除地区缓存 3-清除用户缓存 4-清除规则缓存
     */
    void clearOtherServiceCache(String type);
}
