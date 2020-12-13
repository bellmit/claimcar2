package ins.sino.claimcar.regist.service;

import ins.platform.vo.SysUserVo;

public interface PolicyInfoRegisterService {
	/**
	 * 保单理赔信息写入接口
	 * @param registNo--报案号
	 * @param type--保单类型(B--商业，C--交强)
	 */
	public void policyInfoRegister(String registNo,String type,SysUserVo userVo);

}
