package ins.platform.common.service.facade;

import ins.platform.vo.SysCodeDictVo;

import java.util.List;

public interface CodeDictService {

	/**
	 * 根据PrpDcodeCom和prpdcodeRisk结果交集查询对应类型列表
	 * @param codeType 代码类型
	 * @param riskCode 险种代码
	 * @param comCode 归属机构
	 * @return
	 * @modified:
	 * ☆qianxin(2014年7月11日 下午7:35:13): <br>
	 */
	public List<SysCodeDictVo> findCodeListByRiskCom(String codeType,String riskCode,String comCode);

	public List<SysCodeDictVo> findCodeListByQuery(String codeType,List codeList);
	public void clearCache();//清除缓存
}
