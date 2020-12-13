/******************************************************************************
* CREATETIME : 2016年3月15日 下午7:34:51
******************************************************************************/
package ins.sino.claimcar.carplatform.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DocumentResult;

/**
 * 车险平台xml工具,将对象转换为xml
 * @author ★LiuPing
 * @CreateTime 2016年3月15日
 */
public class PlatformMarshaller {

	private Object headVo;
	private Object bodyVo;

	public PlatformMarshaller(Object headVo,Object bodyVo){
		super();
		this.headVo = headVo;
		this.bodyVo = bodyVo;
	}

	public String toXml() throws JAXBException {
		Document packDoc = DocumentHelper.createDocument();
		Element rootElm = packDoc.addElement("Packet");
		packDoc.setXMLEncoding("GBK");
		rootElm.addAttribute("type","REQUEST");
		rootElm.addAttribute("version","1.0");

		DocumentResult headResult = new DocumentResult();
		JAXBContext headJaxbContext = JAXBContext.newInstance(headVo.getClass());
		Marshaller hreadJaxbMarshaller = headJaxbContext.createMarshaller();
		// output pretty printed
		// hreadJaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
		// hreadJaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING,"GBK");
		hreadJaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT,true);// 是否省略xm头声明信息
		hreadJaxbMarshaller.marshal(headVo,headResult);
		Document headDoc = headResult.getDocument();

		DocumentResult bodyResult = new DocumentResult();
		JAXBContext jaxbContext = JAXBContext.newInstance(bodyVo.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.marshal(bodyVo,bodyResult);
		Document bodyDoc = bodyResult.getDocument();

		rootElm.add(headDoc.getRootElement());
		rootElm.add(bodyDoc.getRootElement());

		String xml = packDoc.asXML();
		return xml;
	}
}
