/******************************************************************************
* CREATETIME : 2016年9月23日 下午8:09:43
******************************************************************************/
package ins.sino.claimcar.platform.service;

import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;


/**
 * 定核损送平台接口服务
 * @author ★Luwei
 * @CreateTime 2016年7月1日
 */
public interface LossToPlatformService {

	/**
	 * <pre>定核损送平台</pre>
	 * @param registNo
	 * @modified:
	 * ☆Luwei(2016年9月23日 下午8:21:27): <br>
	 */
	public void sendLossToPlatform(String registNo,String riskCode);
	
	/**
	 * 定核损平台交互补传
	 * @param CiClaimPlatformLogVo
	 * @throws Exception 
	 */
	public void uploadLossToPaltform(CiClaimPlatformLogVo logVo) throws Exception;
}
