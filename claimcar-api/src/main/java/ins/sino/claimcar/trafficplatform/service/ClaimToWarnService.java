package ins.sino.claimcar.trafficplatform.service;

/**
 * 推送预警系统
 * @author wurh
 *
 */
public interface ClaimToWarnService {

	/**
	 * 立案送预警系统
	 */
	public void claimToWarn(String registNo,String policyNo);
	
	/**
	 * 报案注销送重复/虚假案件
	 * @param cancelReason
	 * @param registNo
	 */
	public void sendFalseCaseToEWByRegist(String cancelReason, String registNo,String policyNo);
	
	/**
	 * 拒赔审核通过送山东预警
	 * @param handleIdKey
	 */
	public void sendFalseCaseToEWByCancel(String handleIdKey);
}
