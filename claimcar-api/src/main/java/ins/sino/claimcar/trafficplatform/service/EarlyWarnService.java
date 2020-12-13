package ins.sino.claimcar.trafficplatform.service;

/**
 * 山东预警系统
 * @author WURH
 *
 */
public interface EarlyWarnService {

	/**
	 * 请求山东预警系统，返回报文
	 * @param requestXML
	 * @param urlStr
	 * @param seconds
	 * @return
	 */
	public String requestSDEW(String requestXML,String urlStr)throws Exception ;
	
}
