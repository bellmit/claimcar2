package ins.sino.claimcar.genilex.service;

import java.math.BigDecimal;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;

public interface ClaimToGenilexService {

	/**
	 * 查勘提交后送精励联讯
	 * @param checkVo
	 */
	public void checkToGenilex(PrpLCheckVo checkVo,Long flowTaskId);
	
	
	/**
	 * 结案提交后送精励联讯
	 * @param endCaseVo
	 */
	public void endCaseToGenilex(PrpLEndCaseVo endCaseVo,PrpLCompensateVo compensateVo,String taskId);
	
	/**
	 * 批量补送精励联讯
	 * @param registNoArray
	 * @param SysUserVo
	 * @return	String
	 */
	public String uploadGenilex(String[] registNoArray,SysUserVo userVo);
	
}
