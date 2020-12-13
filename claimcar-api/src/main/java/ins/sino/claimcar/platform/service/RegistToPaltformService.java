package ins.sino.claimcar.platform.service;

import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;

/**
 * 报案送平台接口服务
 * @author ★LuWei
 * @CreateTime 2016年6月28日
 */
public interface RegistToPaltformService {

	/**
	 * 报案推送平台
	 * @param prpLRegistVo
	 * @param prpLCMainVo
	 */
	public void sendRegistToPlatform(String registNo);
	
	/**
	 * 报案平台交互补传
	 * @param CiClaimPlatformLogVo
	 * @throws Exception
	 */
	public void uploadRegistToPaltform(CiClaimPlatformLogVo logVo);
	
	/**
	 * 保单关联与取消送平台调查勘服务
	 * @param registNo
	 * @param policyNo
	 */
	public void sendRegistCancelToPlatform(String registNo,String policyNo);

	/**
	 * 报案成功送平台却存交互日志数据补送 后续编码报错9201理赔编号 不允许为空
	 * 
	 * <pre></pre>
	 * @param bussArray
	 * @modified: ☆LiYi(2018年10月10日 下午7:21:50): <br>
	 */
	public String sendRegistToPlatform2(String[] bussArray);

	

}
