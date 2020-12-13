package ins.sino.claimcar.autoClaim.service;

import ins.platform.vo.SysUserVo;

public interface AutoClaimService {
   /**
    * 
    * @param registNo
    * @param userVo
    * @param status 1-结案，2-拒赔，3-零结，4-注销
    */
	public void sendAutoClaimResult(String registNo,SysUserVo userVo,String status);
}
