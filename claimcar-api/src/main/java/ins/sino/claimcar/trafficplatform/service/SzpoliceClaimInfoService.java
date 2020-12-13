package ins.sino.claimcar.trafficplatform.service;


public interface SzpoliceClaimInfoService {
	/**
	 * 系统请求深圳警保，上传结案信息
	 */
	public void settleClaimUpload();
	
	/**
	 * 补送--深圳警保，上传结案信息
	 */
	public void settleClaimReUpload(String claimNo,String sign,Long LogId);
	
	/**
	 * 批量补送--深圳警保，上传结案信息
	 */
	public String settleClaimPLReUpload(String claimNo);
}
