package ins.sino.claimcar.platform.service;

import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;

/**
 * 立案、案件注销、理算、赔款支付送平台接口服务
 * @author ★Luwei
 * @CreateTime 2016年6月28日
 */
public interface ClaimToPaltformService {

	/**
	 * <pre>立案送平台</pre>
	 * @param PrpLClaimVo
	 * @modified:
	 * ☆Luwei(2016年9月23日 下午9:10:55): <br>
	 */
	public void claimToPaltform(String claimNo);
	
	/**
	 * 立案平台交互补传
	 * @param CiClaimPlatformLogVo
	 * @throws Exception 
	 */
	public void sendClaimToPaltform(CiClaimPlatformLogVo logVo);
	
	/**
	 * 案件注销平台交互补传
	 * @param CiClaimPlatformLogVo
	 * @throws Exception 
	 */
	public void sendCancelToPaltform(CiClaimPlatformLogVo logVo);
	
	/**
	 * 案件注销--报案注销,保单关联和注销
	 * @param registNo
	 * @param policyNo
	 * @param cancelType--注销类型（1,注销当前保单-2,关联该保单）
	 */
	public void sendCancelToPaltformRe(String registNo,String policyNo,String cancelType);
	
	/**
	 * 理算送平台
	 * @param CiClaimPlatformLogVo
	 * @throws Exception 
	 */
	public void vClaimToPaltform(String compeNo);
	
	/**
	 * 理算平台交互补传
	 * @param CiClaimPlatformLogVo
	 * @throws Exception 
	 */
	public void sendVClaimToPaltform(CiClaimPlatformLogVo logVo);
	
	/**
	 * 赔款支付平台交互补传
	 * @param CiClaimPlatformLogVo
	 * @throws Exception 
	 */
	public void sendPaymentToPaltform(CiClaimPlatformLogVo logVo);
	
	/**
	 * 结案平台交互补传
	 * @param CiClaimPlatformLogVo
	 * @throws Exception 
	 */
	public void sendEndCaseToPaltform(CiClaimPlatformLogVo logVo);
	
	/**
	 * 结案追加平台交互补传
	 * @param CiClaimPlatformLogVo
	 * @throws Exception 
	 */
	public void sendEndCaseAddToPaltform(CiClaimPlatformLogVo logVo);
	
	
	/**
	 * 重开赔案平台交互补传
	 * @param CiClaimPlatformLogVo
	 * @throws Exception 
	 */
	public void sendReOpenAppToPaltform(CiClaimPlatformLogVo logVo);
	
	/**
	 * <pre>重开赔案送平台</pre>
	 * @param endCaseNo
	 * @modified:
	 * ☆Luwei(2016年9月24日 下午4:52:10): <br>
	 */
	public void reOpenAppToPaltform(String endCaseNo);
	
	/**
	 * 赔款支付批量补传方法
	 * @param compeVo
	 * @return CiClaimPlatformLogVo
	 * ☆Luwei(2017年3月9日 上午11:45:21): <br>
	 * @throws Exception 
	 */
	public CiClaimPlatformLogVo paymentListResend(PrpLCompensateVo compeVo) throws Exception;
}
