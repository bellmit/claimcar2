package ins.platform.common.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.SpringProperties;

import ins.framework.cache.CacheManager;
import ins.framework.cache.CacheService;
import ins.framework.lang.Springs;
import ins.platform.common.service.facade.CodeConfigService;
import ins.platform.saa.util.CodeConstants;
import ins.platform.utils.ObjectUtils;
import ins.platform.vo.PrpLConfigValueVo;

public class ConfigUtil {
	
	private static CacheService configCodeMap = CacheManager.getInstance("configCodeMap");
//	private CodeConfigService configService = null;
//
//	public ConfigUtil(){
//		super();
//		configService = (CodeConfigService)Springs.getBean("codeConfigService");
//	}
	
	/**
	 * 配置信息，支持从缓存获取数据
	 * @param upperCode
	 * @return
	 */
	public static PrpLConfigValueVo findConfigByCode(String configCode) {
		return findConfigByCode(configCode, "00000000");//总公司
	}

	/**
	 * 配置信息，支持从缓存获取数据
	 * @param upperCode
	 * @return
	 */
	public static PrpLConfigValueVo findConfigByCode(String configCode,String comCode) {
		if(StringUtils.isBlank(comCode)){
			comCode = "00000000";//总公司
		}else{
			if(comCode.startsWith("00")){
				comCode = comCode.substring(0, 4)+"0000";
			}else{
				comCode = comCode.substring(0, 2)+"000000";
			}
		}
		
		String cacheKey = configCode+"-"+comCode;
		PrpLConfigValueVo configValueVo = (PrpLConfigValueVo)configCodeMap.getCache(cacheKey);
		//PrpLConfigValueVo configValueVo = null;//暂时注释掉 不用缓存
		String  noCache= SpringProperties.getProperty(CodeConstants.noCache);
		if(configValueVo == null || noCache.contains(configCode)){
			CodeConfigService configService = (CodeConfigService)Springs.getBean("codeConfigService");
			configValueVo = configService.findConfigValueByCode(configCode, comCode);
			if( !ObjectUtils.isEmpty(configValueVo) &&  !noCache.contains(configCode)){
				configCodeMap.putCache(cacheKey,configValueVo);
			}
		}
		return configValueVo;
	}
	/**
	 * 清除缓存
	 */
	public void clearCache(){
		configCodeMap.clearAllCacheManager();
	}
}
