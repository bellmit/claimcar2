package ins.platform.common.service.facade;

import ins.platform.vo.PrpLConfigValueVo;

public interface CodeConfigService {

	public PrpLConfigValueVo findConfigValueByCode(String configCode,String comCode);
}
