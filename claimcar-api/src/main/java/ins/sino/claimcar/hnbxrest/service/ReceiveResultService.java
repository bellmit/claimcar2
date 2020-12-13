package ins.sino.claimcar.hnbxrest.service;


import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.hnbxrest.vo.ReceiveauditingresultVo;
import ins.sino.claimcar.hnbxrest.vo.ReceivegusunresultVo;
import ins.sino.claimcar.hnbxrest.vo.RespondMsg;

public interface ReceiveResultService {

	/**
	 * 保险公司发送定损照片审核结果信息
	 * @param receiveauditingresultVo
	 * @return
	 */
	public RespondMsg receiveauditingresult(ReceiveauditingresultVo receiveauditingresultVo);
	
		 
	 /**
	  * 保险公司发送理赔结果信息
	  * @param registNo
	  * @param Status --理赔状态--1-结案2-拒赔3-零结4-注销
	  * @param userVo
	  */
	public void receivecpsresult(String registNo,String status,SysUserVo sysUserVo);
	
	
	/**
	 * 保险公司发送定损结果信息(定损金额)
	 * @param receivegusunresultVo
	 * @return
	 */
	public RespondMsg receivegusunresult(String registNo,SysUserVo sysUserVo);
	
	
	
}
