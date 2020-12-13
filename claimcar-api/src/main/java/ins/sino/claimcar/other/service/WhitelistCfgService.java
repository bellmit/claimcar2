package ins.sino.claimcar.other.service;

import java.util.List;

import ins.sino.claimcar.claim.vo.PrpLKeyWordsCfgVo;
import ins.sino.claimcar.claim.vo.PrpLWhiteListCfgVo;

public interface WhitelistCfgService {
	/**
	 * 	查询所有有效的关键字
	 * @param keyWordVo
	 * @return
	 */
	public List<PrpLKeyWordsCfgVo> findAllValidKeyWords(PrpLKeyWordsCfgVo keyWordVo);
	
	/**
	 * 	保存白名单信息
	 * @param whitelistCfgVo
	 * @throws Exception
	 */
	public void saveWhitelistInfo(PrpLWhiteListCfgVo whitelistCfgVo) throws Exception;
	
	/**
	 * 	通过url查询白名单信息
	 * @param url
	 * @return
	 */
	public PrpLWhiteListCfgVo findWhitelistInfoByURL(String url);
}
