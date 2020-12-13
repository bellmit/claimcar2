package ins.sino.claimcar.claimjy.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.losscar.vo.ClaimFittingVo;


/**
 * 
 * @author linyi
 *
 */
public interface JyDlossService {

	/**
	 * 3.2.3定损请求接口
	 * @param registNo
	 * @param dmgVhclId
	 * @param userVo
	 * @return String
	 */
	public String dlossAskService(ClaimFittingVo claimFittingVo,SysUserVo sysUserVo) throws Exception;
}
