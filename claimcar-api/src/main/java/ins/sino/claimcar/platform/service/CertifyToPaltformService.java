package ins.sino.claimcar.platform.service;

import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;


/**
 * 单证送平台接口服务
 * @author ★LuWei
 * @CreateTime 2016年7月1日
 */
public interface CertifyToPaltformService {

	/**
	 * <pre>单证上传平台</pre>
	 * @param registNo
	 * @modified:
	 * ☆Luwei(2016年9月23日 下午7:41:44): <br>
	 */
	public void certifyToPaltform(String registNo,String riskCode);
	
	/**
	 * 单证平台交互补传
	 * @param CiClaimPlatformLogVo
	 * @throws Exception 
	 */
	public void uploadCertifyToPaltform(CiClaimPlatformLogVo logVo);
	
}
