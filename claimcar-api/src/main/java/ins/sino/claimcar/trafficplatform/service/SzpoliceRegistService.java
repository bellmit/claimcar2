package ins.sino.claimcar.trafficplatform.service;


public interface SzpoliceRegistService {

	/**
	 * 深圳市警保信息共享平台:报案信息上传请求接口
	 * 
	 * <pre></pre>
	 * @modified: ☆LiYi(2018年9月27日 上午11:01:36): <br>
	 */
	public void reportCaseUpload(String logId);

	/**
	 * 报案时信息上传 深圳警保
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @modified: ☆LiYi(2018年9月28日 下午5:46:27): <br>
	 */
	public void sendRegistInfoToSZJB(String registNo);

	/**
	 * 深圳警保批量补传
	 * 
	 * <pre></pre>
	 * @param bussArray
	 * @modified: ☆LiYi(2018年9月29日 下午6:29:16): <br>
	 */
	public String batchSendToSZJB(String[] bussArray);

}
