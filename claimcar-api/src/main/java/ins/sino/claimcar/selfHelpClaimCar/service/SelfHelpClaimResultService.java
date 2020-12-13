package ins.sino.claimcar.selfHelpClaimCar.service;

import ins.platform.vo.SysUserVo;

public interface SelfHelpClaimResultService {
	/**
	    * 
	    * @param registNo
	    * @param userVo
	    * @param status 1-结案，2-拒赔，3-零结，4-注销
	    * @param nodeFlag 1.查勘完成  2.定损金额通知  3.赔付通知
	    * @param policyNo
	    */
		public void sendSelfHelpClaimResult(String registNo,SysUserVo userVo,String status,String nodeFlag,String policyNo);

}
