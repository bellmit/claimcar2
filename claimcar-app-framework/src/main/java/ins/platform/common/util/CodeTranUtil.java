/******************************************************************************
 * CREATETIME : 2015年4月23日 上午9:15:30
 ******************************************************************************/
package ins.platform.common.util;

import ins.framework.cache.CacheManager;
import ins.framework.cache.CacheService;
import ins.framework.lang.Springs;
import ins.platform.common.service.facade.CodeDictService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.DataUtils;
import ins.platform.utils.ObjectUtils;
import ins.platform.vo.SysCodeDictVo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 代码翻译工具
 * 
 * @author ★LiuPing
 */
public class CodeTranUtil {

	/**
	 * code-name 的缓存
	 */
	private static CacheService codeNameCache = CacheManager.getInstance("T_CodeNameTrans_Map");
	/**
	 * code-SysCodeDictVo 的缓存
	 */
	private static CacheService codeDictCache = CacheManager.getInstance("T_CodeDictTrans_Map");
	
	/**
	 * 清理缓存
	 * 
	 * @param transType
	 * @modified: ☆LiuPing(2015年4月23日 上午9:43:22): <br>
	 */
	public static void clearCache(String transType) {
		codeNameCache.remove(transType);
		codeDictCache.remove(transType);
	}

	/**
	 * 初始化多个cache
	 * 
	 * @param transType
	 * @modified: ☆LiuPing(2015年4月23日 下午12:23:44): <br>
	 */
	public static void initCache(String... transTypes) {
		CodeTranService codeTranService = (CodeTranService)Springs.getBean("codeTranService");
		for(String transType:transTypes){
			codeTranService.findCodeDictTransMap(transType,null);
		}
	}

	/**
	 * 代码翻译
	 * 
	 * @param codeType
	 * @param code
	 * @return
	 * @modified: ☆hezheng(May 9, 2014 4:35:25 PM): <br>
	 */
	public static String transCode(String codeType,String code) {

		String codeName = code;
		Map<String,String> codeMap = findCodeNameMap(codeType);
		if(codeMap==null) return codeName;

		codeName = codeMap.get(code);
		if(codeName==null||"".equals(codeName)) codeName = code;
		return codeName;
	}
	
	/**
	 * 获取类型的所有翻译
	 * 
	 * @param transType
	 * @return
	 * @modified: ☆LiuPing(2015年4月23日 上午9:16:52): <br>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> findCodeNameMap(String transType) {
		Map<String,String> codeNameMap = null;
		// 缓存处理
		codeNameMap = (Map<String,String>)codeNameCache.getCache(transType);

		if(codeNameMap==null||codeNameMap.size()<1){
			CodeTranService codeTranService = (CodeTranService)Springs.getBean("codeTranService");
			
			codeNameMap = codeTranService.findCodeNameMap(transType);
		}
		return codeNameMap;
	}

	/**
	 * 获取类型的所有翻译
	 * 
	 * @param transType
	 * @return
	 * @modified: ☆LiuPing(2015年4月23日 上午9:22:59): <br>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,SysCodeDictVo> findCodeDictTransMap(final String transType,final String upperCode) {
		Map<String,SysCodeDictVo> codeDictMap = null;
		if(DataUtils.isEmpty(transType)) return codeDictMap;
		String key = transType+"_"+upperCode;
		// 缓存处理
		codeDictMap = (Map<String,SysCodeDictVo>)codeDictCache.getCache(key);

		if(codeDictMap==null||codeDictMap.size()<1){
			CodeTranService codeTranService = (CodeTranService)Springs.getBean("codeTranService");
			codeDictMap = codeTranService.findCodeDictTransMap(transType,upperCode);

		}
		return codeDictMap;
	}

	/**
	 * 根据机构险种的翻译
	 * 
	 * @param codeType
	 * @param riskCode
	 * @param comCode
	 * @return
	 * @modified: ☆LiuPing(2015年4月23日 上午10:15:29): <br>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,SysCodeDictVo> findCodeMapByRiskCom(String codeType,String riskCode,String comCode) {
		String cacheKey = codeType+"_"+riskCode+"_"+comCode;
		Map<String,SysCodeDictVo> codeDictMap = null;
		if(DataUtils.isEmpty(codeType)) return codeDictMap;
		// 缓存处理
		codeDictMap = (Map<String,SysCodeDictVo>)codeDictCache.getCache(cacheKey);
		if(codeDictMap==null||codeDictMap.size()<1){
			CodeDictService codeDictService = (CodeDictService)Springs.getBean("codeDictService");
			List<SysCodeDictVo> codeDictList = codeDictService.findCodeListByRiskCom(codeType,riskCode,comCode);
			if( !ObjectUtils.isEmpty(codeDictList)){
				codeDictMap = new LinkedHashMap<String,SysCodeDictVo>();
				for(SysCodeDictVo codeDict:codeDictList){
					codeDictMap.put(codeDict.getCodeCode(),codeDict);
				}
			}
			codeDictCache.putCache(cacheKey,codeDictMap);
		}
		return codeDictMap;
	}
	
	/**
	 * 
	 * @param codeType
	 * @param code
	 * @return
	 */
	public static SysCodeDictVo findTransCodeDictVo(String codeType, String code) {
		CodeTranService codeTranService = (CodeTranService)Springs.getBean("codeTranService");
		return codeTranService.findTransCodeDictVo(codeType, code);
	}
	
	
	/**
	 * 代码翻译 缓存对象数量超过1000条数据
	 * 
	 * @param codeType
	 * @param code
	 * @return
	 */
	public static String transCodeBigData(String codeType,String code) {
		String codeName = code;
		CodeTranService codeTranService = (CodeTranService)Springs.getBean("codeTranService");
		String returnName = codeTranService.findCodeName(codeType,code);
		if(StringUtils.isNotBlank(returnName)){
			return returnName;
		}
		return codeName;
	}
	
	public static SysCodeDictVo findTransCodeDict(String codeType, String code) {
		CodeTranService codeTranService = (CodeTranService)Springs.getBean("codeTranService");
		return codeTranService.findCodeDictVo(codeType, code);
	}
	
	public static SysCodeDictVo findTransCodeDict(String codeType, String code,String upperCode) {
		CodeTranService codeTranService = (CodeTranService)Springs.getBean("codeTranService");
		return codeTranService.findCodeDictVoByParams(codeType, code, upperCode);
	}
	
	//清除缓存
    public void clearCache(){
	  codeNameCache.clearAllCacheManager();
	  codeDictCache.clearAllCacheManager();
  }

}
