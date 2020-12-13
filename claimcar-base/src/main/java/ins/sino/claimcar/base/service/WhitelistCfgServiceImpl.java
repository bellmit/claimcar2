package ins.sino.claimcar.base.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.base.po.PrpLKeyWordsCfg;
import ins.sino.claimcar.base.po.PrpLWhiteListCfg;
import ins.sino.claimcar.claim.vo.PrpLKeyWordsCfgVo;
import ins.sino.claimcar.claim.vo.PrpLWhiteListCfgVo;
import ins.sino.claimcar.other.service.WhitelistCfgService;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("whitelistCfgService")
public class WhitelistCfgServiceImpl implements WhitelistCfgService {
	@Autowired
	DatabaseDao databaseDao;

	@Override
	public List<PrpLKeyWordsCfgVo> findAllValidKeyWords(PrpLKeyWordsCfgVo keyWordVo) {
		
		List<PrpLKeyWordsCfgVo> keyWordsCfgVos = new ArrayList<PrpLKeyWordsCfgVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("status", "1");
		List<PrpLKeyWordsCfg> keyWordsList = databaseDao.findAll(PrpLKeyWordsCfg.class, queryRule);
		if (keyWordsList != null && keyWordsList.size() > 0) {
			keyWordsCfgVos = Beans.copyDepth().from(keyWordsList).toList(PrpLKeyWordsCfgVo.class);
		}
		
		return keyWordsCfgVos;
	}

	@Override
	public void saveWhitelistInfo(PrpLWhiteListCfgVo whitelistCfgVo) throws Exception {
		
		PrpLWhiteListCfg  whitelistCfg = Beans.copyDepth().from(whitelistCfgVo).to(PrpLWhiteListCfg.class);
		databaseDao.save(PrpLWhiteListCfg.class, whitelistCfg);
		
	}

	@Override
	public PrpLWhiteListCfgVo findWhitelistInfoByURL(String url) {
		PrpLWhiteListCfgVo whitelistVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("url", url);
		
		List<PrpLWhiteListCfg> whitelistPos = databaseDao.findAll(PrpLWhiteListCfg.class, queryRule);
		if (whitelistPos != null && whitelistPos.size() > 0) {
			whitelistVo = Beans.copyDepth().from(whitelistPos.get(0)).to(PrpLWhiteListCfgVo.class);
		}
		
		return whitelistVo;
	}


}
