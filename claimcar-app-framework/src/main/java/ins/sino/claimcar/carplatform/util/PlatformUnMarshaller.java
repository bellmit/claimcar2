/******************************************************************************
* CREATETIME : 2016年3月15日 下午7:28:32
******************************************************************************/
package ins.sino.claimcar.carplatform.util;

import ins.sino.claimcar.carplatform.constant.PlatfromType;
import ins.sino.claimcar.carplatform.constant.RequestType;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 车险平台将xml准话为对象
 * @author ★LiuPing
 * @CreateTime 2016年3月15日
 */
public class PlatformUnMarshaller {

	private Node headElm;
	private Node bodyElm;
	private RequestType requestType;

	public PlatformUnMarshaller(RequestType requestType){
		headElm = null;
		bodyElm = null;
		this.requestType = requestType;
	}

	public void parseXml(String xmlStr) throws SAXException,IOException,Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
		InputSource source = new InputSource(new StringReader(xmlStr));
		Document doc = dbBuilder.parse(source);
		if(PlatfromType.CI==requestType.getPlatformType()){
			headElm = doc.getElementsByTagName("HEAD").item(0);
			bodyElm = doc.getElementsByTagName("BODY").item(0);
		}else{
			headElm = doc.getElementsByTagName("Head").item(0);
			bodyElm = doc.getElementsByTagName("Body").item(0);
		}
		String reqType = requestType.toString();
		if ("SH".equals(reqType.substring(reqType.length() - 2,reqType.length()))) {
			headElm = doc.getElementsByTagName("HEAD").item(0);
			bodyElm = doc.getElementsByTagName("BODY").item(0);
			if(headElm == null && bodyElm == null){
				headElm = doc.getElementsByTagName("Head").item(0);
				bodyElm = doc.getElementsByTagName("Body").item(0);
			}
		}
	}

	public <T> T getHeadVo(Class<T> valueType) {
		return nodeToObj(headElm,valueType);
	}

	public <T> T getBodyVo(Class<T> valueType) {
		return nodeToObj(bodyElm,valueType);
	}

	private static <T> T nodeToObj(Node node,Class<T> valueType) {
		try{
			JAXBContext context = JAXBContext.newInstance(valueType);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return unmarshaller.unmarshal(node,valueType).getValue();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public String getClaimSeqNo() {
		String reqCode = requestType.getCode();
		String elmName = "CLAIM_CODE";
		String claimSequenceNo = "";
		if(RequestType.RegistInfoBI.getCode().equals(reqCode)
				||RequestType.RegistInfoCI.getCode().equals(reqCode)
				||RequestType.RegistInfoBI_SH.getCode().equals(reqCode)){
			if(RequestType.RegistInfoBI.getCode().equals(reqCode)&& PlatfromType.BI==requestType.getPlatformType()){
				elmName = "ClaimSequenceNo";
			}
			Element elm = (Element)bodyElm;
			if(elm != null){
				claimSequenceNo = elm.getElementsByTagName(elmName).item(0).getTextContent();
			}
		}
		if(StringUtils.isEmpty(claimSequenceNo)){//
			return null;
		}
		return claimSequenceNo;
	}
	
	public String getClaimConfirmCode(){
		String reqCode = requestType.getCode();
		String elmName = "ClaimConfirmCode";
		String claimConfirmCode = "";
		if(RequestType.EndCaseBI.getCode().equals(reqCode)
				||RequestType.EndCaseCI.getCode().equals(reqCode)){
			if(RequestType.EndCaseCI.getCode().equals(reqCode)){
				elmName = "CLAIM_CONFIRM_CODE";
			}
			Element elm = (Element)bodyElm;
			if(elm!=null){
				claimConfirmCode = elm.getElementsByTagName(elmName).item(0).getTextContent();
			}
		}
		if(StringUtils.isEmpty(claimConfirmCode)){// 
			return null;
		}
		return claimConfirmCode;
	}
	
	public String getErrorCode() {
		String elmName = "ErrorCode";
		String reqType = requestType.toString();
		if (PlatfromType.CI == requestType.getPlatformType()) {
			elmName = "ERROR_CODE";
		}
		if ("SH".equals(reqType.substring(reqType.length() - 2,reqType.length()))) {
			elmName = "RESPONSE_CODE";
		}
		
		String errorCode = "0";
		Element elm = (Element)headElm;
		if(elm != null){
			Node node = elm.getElementsByTagName(elmName).item(0);
			if(node != null){
				errorCode = node.getTextContent();
			}else if("SH".equals(reqType.substring(reqType.length() - 2,reqType.length()))){
				elmName = "ResponseCode";
				node = elm.getElementsByTagName(elmName).item(0);
				if(node != null){
					errorCode = node.getTextContent();
				}
			}
			if(errorCode==null||errorCode.equals("0000")||"1".equals(errorCode)){// 成功的error返回空
				return null;
			}
		}
		return errorCode;
	}

	public String getErrorMessage() {
		String elmName = "ErrorMessage";
		String reqType = requestType.toString();
		if (PlatfromType.CI == requestType.getPlatformType()) {// 交强平台
			elmName = "ERROR_MESSAGE";
		} else if (PlatfromType.CA == requestType.getPlatformType()
				|| PlatfromType.HS == requestType.getPlatformType()) {// 结算平台
			elmName = "ErrorDesc";
		}
		if ("SH".equals(reqType.substring(reqType.length() - 2,reqType.length()))) {
			elmName = "ERROR_MESSAGE";
		}
		
		String errorMsg = "解析异常";
		Node node = ((Element) headElm).getElementsByTagName(elmName).item(0);
		if(node != null){
			errorMsg = node.getTextContent();
		}else if("SH".equals(reqType.substring(reqType.length() - 2,reqType.length()))){
			elmName = "ErrorMessage";
			node = ((Element) headElm).getElementsByTagName(elmName).item(0);
			if(node != null){
				errorMsg = node.getTextContent();
			}
		}
		return errorMsg;
	}
	
}
