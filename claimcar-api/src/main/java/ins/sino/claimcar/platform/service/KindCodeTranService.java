package ins.sino.claimcar.platform.service;

import ins.sino.claimcar.platform.vo.transKindCodeVo.SysKindcodeconvertVo;

public interface KindCodeTranService {
	
	/**
	 * 
	 * <pre></pre>
	 * @param comCode 机构 全国 00 上海 22
	 * @param riskCode  险别
	 * @param codeType  代码类型  全国CovergeCode 上海CovergeCodeSH
	 * @param codeCode  险种
	 * @return  
	 * @modified:
	 * *牛强(2017年5月10日 上午11:43:52): <br>
	 */
	public SysKindcodeconvertVo findTransKindCodeCovertVo(String comCode,String riskCode,String codeType,String codeCode);
	
	/**
	 * 
	 * <pre></pre>
	 * @param comCode 机构 全国 00 上海 22
	 * @param riskCode  险别
	 * @param codeType  代码类型  全国CovergeCode 上海CovergeCodeSH
	 * @param codeCode  险种
	 * @return
	 * @modified:
	 * *牛强(2017年5月10日 上午11:50:18): <br>
	 */
	public String findTransCiCode(String comCode,String riskCode,String codeType,String codeCode);

}
