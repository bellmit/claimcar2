package ins.platform.common.service.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.CodeConfigService;
import ins.platform.schema.PrpLConfigValue;
import ins.platform.vo.PrpLConfigValueVo;

@Service(value = "codeConfigService")
public class CodeConfigServiceSpringImpl implements CodeConfigService{

	@Autowired
	private DatabaseDao databaseDao;
	
	@Override
	public PrpLConfigValueVo findConfigValueByCode(String configCode, String comCode) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("prpLRiskConfig.configCode", configCode);
		qr.addEqual("comCode", comCode);
		//qr.addEqual("prpLRiskConfig.validStatus", "1");
		//qr.addEqual("validFlag", "1");
		
		
		PrpLConfigValue configValuePo = databaseDao.findUnique(PrpLConfigValue.class, qr);
		if(configValuePo == null){
			return null;
		}
		
		PrpLConfigValueVo configValueVo = Beans.copyDepth().from(configValuePo).to(PrpLConfigValueVo.class);
		
		return configValueVo;
	}

}
