package ins.sino.claimcar.trafficplatform.service;

import java.util.Map;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;


/**
 * 报案推送山东平台
 * <pre></pre>
 * @author ★zhujunde
 */
public interface RegistToCarRiskPaltformService {

	/**
	 * 报案推送山东平台
	 * @param prpLRegistVo
	 * @param prpLCMainVo
	 */
	public void sendRegistToCarRiskPlatform(String registNo,SysUserVo userVo,Map<String,String> map); 

	public void sendRegistToCarRiskPlatformRe(String registNo,String policyNo,SysUserVo userVo);
	
	public void sendRegistToCarRiskPlatformByCmain(PrpLRegistVo prpLRegistVo,PrpLCMainVo prpLCMainVo,SysUserVo userVo);
}
