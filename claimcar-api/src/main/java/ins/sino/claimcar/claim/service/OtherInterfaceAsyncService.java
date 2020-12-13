package ins.sino.claimcar.claim.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;

public interface OtherInterfaceAsyncService {

	public void claimToHN(PrpLClaimVo prpLClaimVo,SysUserVo userVo) throws Exception;
	
	public void endCaseToHN(PrpLEndCaseVo endCaseVo,String userCode);
	
	public void claimToGZ(PrpLClaimVo prpLClaimVo,SysUserVo userVo) throws Exception;
	
	public void endCaseToGZ(PrpLEndCaseVo endCaseVo,String userCode);
	
	/**
	 * 结案 重开 拒赔 注销 送再保接口
	 * @param businessType
	 * @param claimVo
	 * @param claimInterfaceLogVo
	 * @throws Exception
	 */
	public abstract void TransDataForReinsCaseVo(String businessType,
			PrpLClaimVo claimVo,ClaimInterfaceLogVo claimInterfaceLogVo) throws Exception;
	
	/**
     * 查勘提交后送精励联讯
     * @param checkVo
     * @param flowTaskId
     */
    public void checkToGenilex(PrpLCheckVo checkVo,Long flowTaskId);
    
    /**
	 * 结案提交后送精励联讯
	 * @param endCaseVo
	 */
	public void endCaseToGenilex(PrpLEndCaseVo endCaseVo,PrpLCompensateVo compensateVo,String taskId);
	
	/**
	 * 结案，拒赔送中保信
	 * @param endCaseVo
	 * @param userVo
	 */
    public void reqByEndCase(PrpLEndCaseVo endCaseVo,SysUserVo userVo);
	
}
