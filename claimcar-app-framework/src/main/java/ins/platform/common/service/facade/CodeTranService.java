package ins.platform.common.service.facade;

import ins.platform.vo.SysCodeDictVo;

import java.util.Map;

public interface CodeTranService extends ins.framework.service.CodeTranService {

	/**
	 * 获取类型的所有翻译
	 * 
	 * @param transType 类型
	 * @return 类型的所有翻译
	 */
	public Map<String,SysCodeDictVo> findCodeDictTransMap(String transType,String upperCode);
	
	/**
	 * 
	 * @param codeType
	 * @param code
	 * @return
	 */
	public SysCodeDictVo findTransCodeDictVo(String codeType, String code);

	/**
	 * 获取transType类型code的翻译
	 * 
	 * @param transType
	 * @return
	 */
	public String findCodeName(String codeType, String code);
	
	public SysCodeDictVo findCodeDictVo (String transType,String code);
	/**
	 * 多个参数的翻译
	 * @param transType
	 * @param code
	 * @param upperCode
	 * @return
	 */
	public SysCodeDictVo findCodeDictVoByParams (String transType,String code,String upperCode);
	/**
	 * 清除缓存
	 */
	public void clearCache();
	
	public boolean validEffictiveValue(String value,String type);

	/** 从再保获取巨灾一级代码 */
	public Map<String,SysCodeDictVo> findLevelOneDisasterInfo();

	/** 从再保获取巨灾二级代码 */
	public Map<String,SysCodeDictVo> findLevelTwoDisasterInfo();

	/** 获取所有费用类型 */
	public Map<String, String> findFeeTypes();
	/** 获取银行大类信息*/
	public Map<String, SysCodeDictVo> findAccBankCode();
}
