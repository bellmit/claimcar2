package ins.sino.claimcar.claimjy.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.losscar.vo.ClaimFittingVo;

public interface ClaimToJyService {
	
	/**
	 * 核价请求精友
	 * @param claimFittingVo
	 * @param userVo
	 * @return
	 * @throws Exception
	 */
	public String priceToJy(ClaimFittingVo claimFittingVo,SysUserVo userVo) throws Exception;
	
	/**
	 * 核损请求精友
	 * @param claimFittingVo
	 * @param userVo
	 * @return
	 * @throws Exception
	 */
	public String vlossToJy(ClaimFittingVo claimFittingVo,SysUserVo userVo) throws Exception;

}
