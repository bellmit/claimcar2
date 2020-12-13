package ins.platform.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Xstream实例工厂
 * 
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2014-7-12
 * @since (2014-7-12 下午03:38:23): <br>
 */
public class XstreamFactory {

	private static String xmlVersion = "<?xml version=\"1.0\" encoding=\"GBK\"?>";

	private static String xmlVersionUtf = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	/**
	 * XStream实例
	 * 
	 * @return
	 * @author ☆HuangYi(2014-7-12 下午03:42:51): <br>
	 */
	public static XStream getInstance() {
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		return stream;
	}

	/**
	 * 将对象转换成XML
	 * 
	 * @param o
	 * @return
	 * @author ☆HuangYi(2014-6-22 下午02:33:00): <br>
	 */
	public static String objToXml(Object o) {
		XStream stream = getInstance();
		String xmlString = xmlVersion+stream.toXML(o);
		return xmlString;
	}

	/**
	 * 将对象转换成XML,utf-8的编码格式
	 * 
	 * @param o
	 * @return
	 * @author ☆HuangYi(2014-6-22 下午02:33:00): <br>
	 */
	public static String objToXmlUtf(Object o) {
		XStream stream = getInstance();
		String xmlString = xmlVersionUtf+stream.toXML(o);
		return xmlString;
	}
	
	/**
	 * 将XML转换成对象
	 * 
	 * @param <T>
	 * @param xml
	 * @param decodeType
	 * @return
	 * @author ☆HuangYi(2014-6-22 下午02:54:19): <br>
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xmlToObj(String xml,Class<T> decodeType) {
		XStream stream = getInstance();
		stream.processAnnotations(decodeType);
		return (T)stream.fromXML(xml);
	}
}
