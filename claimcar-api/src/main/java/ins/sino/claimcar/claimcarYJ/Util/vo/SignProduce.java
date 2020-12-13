package ins.sino.claimcar.claimcarYJ.Util.vo;


public class SignProduce {
	public static String appKey="f6aad27d-6bcc-4d93-b0ae-1a2a464af433";
	
   
	/**
	 * 
	 * @param appId 分配给保险公司的id
	 * @param method 访问接口（add）
	 * @param timeStamp 时间戳long值S
	 * @return url明文？后的签名字符串
	 */
	public static String getSignurl(String appId,String method,long timeStamp){
		
		String sign=MD5Util.getMd5("appId="+appId+"&appKey="+appKey+"&timeStamp="+timeStamp).toUpperCase();
		return "appId="+appId+"&method="+method+"&timeStamp="+timeStamp+"&sign="+sign;
	}
	
	
	
}
