package ins.platform.common.service.facade;


import ins.platform.vo.PiccCodeDictVo;

import java.util.List;
import java.util.Map;

/**
 * description: PingAnDictService 公共类
 * date: 2020/7/21 17:37
 * author: lk
 * version: 1.0
 */
public interface PingAnDictService {
    /**
     * description: getDictData 根据代码类型和平安数据字典代码 获取鼎和数据字典
     * version: 1.0 
     * date: 2020/7/21 17:51 
     * author: lk
     * 
     * @param codeType 代码类型
 * @param piccCodeCode 平安代码
     * @return ins.sino.claimcar.pinganUnion.vo.PiccCodeDictVo
     */
    PiccCodeDictVo getDictData(String codeType, String piccCodeCode);

    void clearCache();//清除缓存

    /**
     * 将平安传过来的KINDCODE存入到缓存
     * @param compensateNo
     * @param cacheMap
     */
    void saveKindCodeToCatche(String compensateNo , Map<String,List<Map<Long,String>>> cacheMap);

    /**
     * 从缓存中取出平安数据传过来的KINDCODE
     * @param compensateNo
     * @return
     */
    Map<String,List<Map<Long,String>>> findKindCodeToCatche(String compensateNo);

    /**
     * 清除KINDCODE缓存
     */
    void clearKindCodeCache(String compensateNo);
}
