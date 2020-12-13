package ins.sino.claimcar.trafficplatform.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;

public interface SdpoliceCaseService {
	/**
	 * 山东预警案件注销（接口）
	 * @param prpLCMainVo
	 * @param cancleType
	 * @param reason
	 * @throws Exception
	 */
	public void sendCaseCancleRegister(PrpLCMainVo prpLCMainVo,String cancleType,String reason,SysUserVo userVo)throws Exception;
	/**
	 * 山东预警重开赔案登记功能(接口)
	 * @param endCaseNo-结案号
	 * @param policyNo
	 * @param userVo
	 * @throws Exception
	 */
	public void sendReopenCaseRegister(String endCaseNo,String policyNo,SysUserVo userVo)throws Exception;
}
