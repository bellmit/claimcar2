package ins.sino.claimcar.ilog.rule.service;

import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.ilog.rule.vo.IlogDataProcessingVo;


public interface RuleReturnDataSaveService {
		
	/**
	 * ILOG返回信息处理 保存到数据库
	 * @param vPriceResVo
	 * @param ilogDataProcessingVo  
	 * @param userVo
	 * @author ☆luows(2018年1月12日 上午10:40:05): <br>
	 */
	public void dealILogResReturnData(LIlogRuleResVo lIlogRuleResVo,IlogDataProcessingVo ilogDataProcessingVo) throws Exception;

}
