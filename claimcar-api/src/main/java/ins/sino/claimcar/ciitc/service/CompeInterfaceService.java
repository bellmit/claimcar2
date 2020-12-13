package ins.sino.claimcar.ciitc.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

public interface CompeInterfaceService {

	/**
	 * 报案，报案注销送中保信
	 * @param registVo
	 * @param userVo
	 * @param reportType
	 * @throws Exception 
	 */
	public void reqByRegist(PrpLRegistVo registVo,SysUserVo userVo,String reportType) throws Exception;
	
	/**
	 * 结案，拒赔送中保信
	 * @param endCaseVo
	 * @param userVo
	 */
	public void reqByEndCase(PrpLEndCaseVo endCaseVo,SysUserVo userVo);
	
	/**
	 * 立案注销送中保信
	 * @param claimVo
	 * @param userVo
	 */
	public void reqByCancel(PrpLClaimVo claimVo,SysUserVo userVo);
	
}
