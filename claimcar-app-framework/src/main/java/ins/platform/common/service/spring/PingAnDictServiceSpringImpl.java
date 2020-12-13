package ins.platform.common.service.spring;

import ins.framework.cache.CacheManager;
import ins.framework.cache.CacheService;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.schema.PiccCodeDict;
import ins.platform.vo.PiccCodeDictVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * description: PingAnCommonServiceImpl 公共类
 * date: 2020/7/21 17:52
 * author: lk
 * version: 1.0
 */
@Service(value = "PingAnDictService")
public class PingAnDictServiceSpringImpl implements PingAnDictService {
    private static Logger logger = LoggerFactory.getLogger(PingAnDictServiceSpringImpl.class);
    @Autowired
    private DatabaseDao databaseDao;
    /**
     * code-PiccCodeDict 的缓存
     */
    private static CacheService pingAnDictCache = CacheManager.getInstance("T_PICCDictTrans_Map");

    private static CacheService pingAnKindCode = CacheManager.getInstance("T_KINDCODE_Map");

    @Override
    public PiccCodeDictVo getDictData(String codeType, String piccCodeCode) {
        String key = codeType;
        //从缓存里取
        Map<String, PiccCodeDictVo> codeDictMap = (Map<String, PiccCodeDictVo>) pingAnDictCache.getCache(key);
        if (codeDictMap == null || codeDictMap.get(piccCodeCode) == null) {
            codeDictMap = new LinkedHashMap<String, PiccCodeDictVo>();

            QueryRule queryRule = QueryRule.getInstance();
            queryRule.addEqual("codeType", codeType);
            queryRule.addEqual("isValid", "Y");
            List<PiccCodeDict> piccCodeDicts = databaseDao.findAll(PiccCodeDict.class, queryRule);
            if (piccCodeDicts != null && !piccCodeDicts.isEmpty()) {
                List<PiccCodeDictVo> piccCodeDictVos = Beans.copyDepth().from(piccCodeDicts).toList(PiccCodeDictVo.class);
                for (PiccCodeDictVo piccCodeDictVo : piccCodeDictVos) {
                    codeDictMap.put(piccCodeDictVo.getPiccCodeCode(), piccCodeDictVo);
                }
            }
            //存进缓存
            pingAnDictCache.putCache(key, codeDictMap);
        }

        PiccCodeDictVo piccCodeDictVo = codeDictMap.get(piccCodeCode);
        if (null != piccCodeDictVo) {
            return piccCodeDictVo;
        } else {
            return new PiccCodeDictVo();
        }
    }

    /**
     * 将平安传过来的KINDCODE存入到缓存Map<所属表，List<Map<表ID，kindCode>>></>
     * @param compensateNo
     * @param cacheMap
     */
    public void saveKindCodeToCatche(String compensateNo , Map<String,List<Map<Long,String>>> cacheMap){
        //存进缓存
        pingAnKindCode.putCache(compensateNo, cacheMap);
    }

    /**
     * 从缓存中取出平安数据传过来的KINDCODE
     * @param compensateNo
     * @return
     */
    public Map<String,List<Map<Long,String>>> findKindCodeToCatche(String compensateNo) {
        logger.info("取缓存开始。。。。。。。{}",compensateNo);
        Map<String,List<Map<Long,String>>> kindCodeMap = (Map<String,List<Map<Long,String>>>) pingAnKindCode.getCache(compensateNo);
        if (kindCodeMap != null && kindCodeMap.size() > 0) {
            logger.info("取缓存结束。。。。。。。{}",compensateNo);
            return kindCodeMap;
        }
        return null;
    }

    /**
     * 清除KINDCODE缓存
     */
    public void clearKindCodeCache(String compensateNo) {
        logger.info("清理缓存开始。。。。。。。{}",compensateNo);
        pingAnKindCode.clearCache(compensateNo);
        Map<String,List<Map<Long,String>>> kindCodeMap = this.findKindCodeToCatche(compensateNo);
        if(kindCodeMap != null && !kindCodeMap.isEmpty()){
            logger.info("清理缓存未成功。。。。。。。{}",compensateNo);
        }else{
            logger.info("清理缓存成功结束。。。。。。。{}",compensateNo);
        }
    }

    @Override
    public void clearCache() {
        pingAnDictCache.clearAllCacheManager();
    }
}
