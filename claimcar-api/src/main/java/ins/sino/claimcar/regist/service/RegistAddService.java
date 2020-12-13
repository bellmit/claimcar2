package ins.sino.claimcar.regist.service;

import ins.framework.common.ResultPage;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;


public interface RegistAddService {
	
	public ResultPage<PrpLRegistVo> findRegistAddForPage(PrpLRegistVo prpLRegistVo,Integer start,Integer length,SysUserVo userVo) throws Exception;
	public String isRegistAdd(String registNo);

}
