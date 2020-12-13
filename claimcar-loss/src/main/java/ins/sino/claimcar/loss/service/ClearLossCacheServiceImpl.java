package ins.sino.claimcar.loss.service;

import com.alibaba.dubbo.config.annotation.Service;
import ins.platform.common.service.facade.CodeDictService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.common.util.AreaSelectUtil;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.ConfigUtil;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.other.service.ClearCacheService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;

/**
 * description: ClearLossCacheServiceImpl
 * date: 2020/12/8 15:10
 * author: lk
 * version: 1.0
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, group = "clearLossCacheService")
@Path("clearLossCacheService")
public class ClearLossCacheServiceImpl implements ClearCacheService {
    @Autowired
    private SaaUserPowerService saaUserPowerService;
    @Autowired
    private CodeTranService codeTranService;
    @Autowired
    private CodeDictService codeDictService;
    @Autowired
    private PingAnDictService pingAnDictService;
    @Autowired
    private PayCustomService payCustomService;
    @Autowired
    private AssignService assignService;

    @Override
    public void clearCache(String type) {
        if ("1".equals(type)) {
            CodeTranUtil codeTranUtil=new CodeTranUtil();
            ConfigUtil configUtil=new ConfigUtil();
            codeTranService.clearCache();
            codeDictService.clearCache();
            codeTranUtil.clearCache();
            configUtil.clearCache();
            pingAnDictService.clearCache();
        } else if ("2".equals(type)) {
            AreaSelectUtil areaSelectUtil=new AreaSelectUtil();
            areaSelectUtil.clearCache();
            payCustomService.clearCache();
        } else if ("3".equals(type)) {
            saaUserPowerService.clearCache();
        } else if ("4".equals(type)) {
            assignService.clearRule();
        }
    }
}
