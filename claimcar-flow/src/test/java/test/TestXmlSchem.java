/******************************************************************************
* CREATETIME : 2016年2月29日 下午3:44:27
******************************************************************************/
package test;

import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.util.PlatformMarshaller;
import ins.sino.claimcar.carplatform.util.PlatformUnMarshaller;
import ins.sino.claimcar.carplatform.vo.RegistBaseVo;
import ins.sino.claimcar.carplatform.vo.RegistBodyVo;
import ins.sino.claimcar.carplatform.vo.RegistCarVehiceleVo;
import ins.sino.claimcar.carplatform.vo.RequestHeadVo;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Node;



/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年2月29日
 */
public class TestXmlSchem {


	// @Test
		public void testVoToXml() throws Exception {
			RequestHeadVo headVo = new RequestHeadVo();
			headVo.setRequestType("001");
			headVo.setUser("用户");

			RegistBaseVo baseVo = new RegistBaseVo();
			baseVo.setComCode("001002003");
			baseVo.setClassCode("23");
			baseVo.setMaxPartFee(1000d);
			baseVo.setSumLossFee(3000d);
			baseVo.setSelfConfigFlag(0);

			RegistCarVehiceleVo carVo = new RegistCarVehiceleVo();
			carVo.setCarMark("AUTO001");
			carVo.setDreivName("驾驶员");
			carVo.setVehcType("BMW001");

			RegistCarVehiceleVo carVo2 = new RegistCarVehiceleVo();
			carVo2.setCarMark("AUTO001");
			carVo2.setDreivName("DRIVE001");
			carVo2.setVehcType("BMW001");

			List<RegistCarVehiceleVo> carvehList = new ArrayList<RegistCarVehiceleVo>();
			carvehList.add(carVo);
			carvehList.add(carVo2);
			RegistBodyVo bodyVo = new RegistBodyVo();
			bodyVo.setBaseVo(baseVo);
			bodyVo.setCarVehList(carvehList);

		PlatformMarshaller marsh = new PlatformMarshaller(headVo,bodyVo);
		String xml = marsh.toXml();
		System.out.println("PlatformMarshaller.xml.="+xml);

		// JAXBContext jc = JAXBContext.newInstance(PurchaseOrder.class, USAddress.class, Address.class);
	}

	// @Test
	public void testXmlToVo() throws Exception {
		String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><PACKET type=\"REQUEST\" version=\"1.0\"><HEAD><requestType>001</requestType><user>用户</user></HEAD><BODY><BASE_PART><classCode>23</classCode><COM_CODE>001002003</COM_CODE><maxPartFee>1000.0</maxPartFee><DATE>20160314172640</DATE><selfConfigFlag>0</selfConfigFlag><sumLossFee>3000.0</sumLossFee></BASE_PART><THIRD_VEHICLE_LIST><THIRD_VEHICLE_DATA><carMark>AUTO001</carMark><dreivName>驾驶员</dreivName><vehcType>BMW001</vehcType></THIRD_VEHICLE_DATA><THIRD_VEHICLE_DATA><carMark>AUTO001</carMark><dreivName>DRIVE001</dreivName><vehcType>BMW001</vehcType></THIRD_VEHICLE_DATA></THIRD_VEHICLE_LIST></BODY></PACKET>";

		PlatformUnMarshaller umMarsh = new PlatformUnMarshaller(RequestType.CancelInfoCI);
		umMarsh.parseXml(xmlStr);

		RequestHeadVo headVo = umMarsh.getHeadVo(RequestHeadVo.class);// ResponseHeadVo.class;

		System.out.println("headVo.user="+headVo.getUser());

		RegistBodyVo bodyVo = umMarsh.getBodyVo(RegistBodyVo.class);
		System.out.println("bodyVo.listchar.="+bodyVo.getCarVehList().get(0).getDreivName());
	}


	public static <T> T fromXML(String xml,Class<T> valueType) {
		try{
			JAXBContext context = JAXBContext.newInstance(valueType);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (T)unmarshaller.unmarshal(new StringReader(xml));
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	public static <T> T fromNode(Node node,Class<T> valueType) {
		try{
			JAXBContext context = JAXBContext.newInstance(valueType);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return unmarshaller.unmarshal(node,valueType).getValue();
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	public static String toXML(Object obj) {
		try{
			JAXBContext context = JAXBContext.newInstance(obj.getClass());

			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING,"UTF-8");// //编码格式
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);// 是否格式化生成的xml串
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT,false);// 是否省略xm头声明信息
			StringWriter writer = new StringWriter();
			marshaller.marshal(obj,writer);
			return writer.toString();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/*
	 * 
	 * // Document packDoc = DocumentHelper.parseText(xmlStr);
		// Element headElm = packDoc.getRootElement().element("HEAD");
		// Element bodyEml = packDoc.getRootElement().element("BODY");
		// System.out.println("==testXmlToVo.toXml====\n"+headElm.asXML());
		 * 
		public void testW3cXmlToVo() throws Exception {
			String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><PACKET type=\"REQUEST\" version=\"1.0\"><HEAD><requestType>001</requestType><user>用户</user></HEAD><BODY><BASE_PART><classCode>23</classCode><COM_CODE>001002003</COM_CODE><maxPartFee>1000.0</maxPartFee><DATE>20160314172640</DATE><selfConfigFlag>0</selfConfigFlag><sumLossFee>3000.0</sumLossFee></BASE_PART><THIRD_VEHICLE_LIST><THIRD_VEHICLE_DATA><carMark>AUTO001</carMark><dreivName>驾驶员</dreivName><vehcType>BMW001</vehcType></THIRD_VEHICLE_DATA><THIRD_VEHICLE_DATA><carMark>AUTO001</carMark><dreivName>DRIVE001</dreivName><vehcType>BMW001</vehcType></THIRD_VEHICLE_DATA></THIRD_VEHICLE_LIST></BODY></PACKET>";

			// w3c的解析方式
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			InputSource source = new InputSource(new StringReader(xmlStr));
			Document doc = dbBuilder.parse(source);
			Node headElm = doc.getElementsByTagName("HEAD").item(0);
			Node bodyEml = doc.getElementsByTagName("BODY").item(0);
		

			JAXBContext context = JAXBContext.newInstance(RequestHeadVo.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<RequestHeadVo> headVoEml = unmarshaller.unmarshal(headElm,RequestHeadVo.class);
			RequestHeadVo headVo = headVoEml.getValue();

			System.out.println("headVo.user="+headVo.getUser());

			RegistBodyVo bodyVo = fromNode(bodyEml,RegistBodyVo.class);
			System.out.println("bodyVo.listchar.="+bodyVo.getCarVehList().get(0).getDreivName());
		}
	*/
}
