package ins.sino.claimcar.claim.service;

import ins.platform.vo.SysUserVo;

public interface DlclaimTaskService {

	/**
	 * 请求德联易控接口
	 * @param registNo
	 * @param licenseNo
	 * @param lossFlag
	 * @param url
	 * @throws Exception
	 */
	public void SendControlExpert(String registNo,SysUserVo userVo,String licenseNo,String lossCarMainId,String nodeFlag,String url);
	
	
}
