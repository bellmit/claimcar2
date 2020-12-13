package ins.sino.claimcar.web.service.impl;

import ins.sino.claimcar.other.service.ClearCacheService;
import ins.sino.claimcar.web.service.ClearWebCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * description: ClearWebCacheServiceImpl
 * date: 2020/12/8 15:36
 * author: lk
 * version: 1.0
 */
@Service
public class ClearWebCacheServiceImpl implements ClearWebCacheService {
    @Autowired
    private ClearCacheService clearBaseCacheService;

    @Autowired
    private ClearCacheService clearCertifyCacheService;

    @Autowired
    private ClearCacheService clearCheckCacheService;

    @Autowired
    private ClearCacheService clearClaimCacheService;

    @Autowired
    private ClearCacheService clearFlowCacheService;

    @Autowired
    private ClearCacheService clearLossCacheService;

    @Autowired
    private ClearCacheService clearRegistCacheService;
    @Override
    public void clearOtherServiceCache(String type) {
        clearBaseCacheService.clearCache(type);
        clearCertifyCacheService.clearCache(type);
        clearCheckCacheService.clearCache(type);
        clearClaimCacheService.clearCache(type);
        clearFlowCacheService.clearCache(type);
        clearLossCacheService.clearCache(type);
        clearRegistCacheService.clearCache(type);
    }
}
