package ins.platform.utils;



/**
 * 报文转换基本类
 * @author zy
 *
 */
public class ClaimBaseCoder {
	
	public static void main(String[] args) {
//		FixedPositionReqVo vo = new FixedPositionReqVo();
//		
//		HeadReq head = new HeadReq();
//		head.setRequestType("FixedPosition");
//		
//		FixedPositionReqBody body = new FixedPositionReqBody();
//		body.setProvinceCode("1");
//		body.setCityCode("1");
//		body.setRegionCode("2");
//		body.setDamageAddress("2");
//		body.setIsSHPolicy("1");
//		
//		vo.setHead(head);
//		vo.setBody(body);
//		
//		ClaimBaseCoder code = new ClaimBaseCoder();
//		String xml = code.objToXml(vo);
//		System.out.println(xml);
		
		/*String xml = "<?xml version='1.0' encoding='UTF-8'?><PACKET type='REQUEST' version='1.0'>   <HEAD>       <REQUESTTYPE>FIXEDPOSITION</REQUESTTYPE>       <USER>MCLAIM_USER</USER>       <PASSWORD>MCLAIM_PSD</PASSWORD>   </HEAD>   <BODY>       <PROVINCECODE>520000</PROVINCECODE>       <CITYCODE>520100</CITYCODE>       <REGIONCODE>520103</REGIONCODE>       <DAMAGEADDRESS>贵州省贵阳市云岩区G6001(贵阳绕城高速公路)</DAMAGEADDRESS>       <LNGXLATY>106.77697,26.599226</LNGXLATY>   <ISSHPOLICY>1</ISSHPOLICY></BODY></PACKET>";
		FixedPositionBackReqBody fixedPositionBackReqBody = fixedPositionReqCoder.xmlToObj(xml, FixedPositionBackReqBody.class);*/
	}

	/**
	 * 将对象转换成XML
	 * @author zy
	 */
	public static String objToXml(Object o) {
		String xmlString = XstreamFactory.objToXml(o);
		return xmlString;
	}

	/**
	 * 将对象转换成XML
	 * @author zy
	 */
	public static String objToXmlUtf(Object o) {
		String xmlString = XstreamFactory.objToXmlUtf(o);
		return xmlString;
	}
	
	/**
	 * 将XML转换成对象
	 * @author  zy
	 */
	public static <T> T xmlToObj(String xml,Class<T> decodeType) {
		T t = XstreamFactory.xmlToObj(xml,decodeType);
		return t;
	}
}
