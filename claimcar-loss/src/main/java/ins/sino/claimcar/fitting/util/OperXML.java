/******************************************************************************
* CREATETIME : 2015年12月18日 上午11:40:23
* FILE       : ins.sino.claimcar.losscar.OperXML
******************************************************************************/
package ins.sino.claimcar.fitting.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;


/**
 * <pre></pre>
 * @author ★yangkun
 */
public class OperXML {


	private boolean blHasParent = false;
	private String strParentTag = "";
	private String strParentValue = "";
	private Document doc = null;
	private Element root = null;

	/**
	 * 默认的构造函数
	 */
	public OperXML(){
	}

	/**
	 * 带参数的构造函数
	 * 
	 * @param iParentTag 父节点的标签
	 * @param iParentValue 父节点的值
	 */

	public OperXML(String iParentTag,String iParentValue){
		this.blHasParent = true;
		this.strParentTag = iParentTag;
		this.strParentValue = iParentValue;
	}

	/**
	 * 解析XML文件
	 * 
	 * @param iFileSrc 要解析的XML文件路径
	 * @throws IOException
	 * @throws JDOMException
	 * @throws Exception
	 */
	public void parser(String iFileSrc) throws IOException,JDOMException,Exception {
		try{
			SAXBuilder sb = new SAXBuilder();
			doc = sb.build(new FileInputStream(iFileSrc));
			root = doc.getRootElement();
		}catch(IOException ioe){
			throw new Exception("解析不到此文件："+iFileSrc,ioe);
		}catch(JDOMException jdome){
			jdome = new JDOMException("不是有效的XML文档!");
			throw jdome;
		}catch(Exception e){
			throw e;
		}
	}

	/**
	 * 解析XML字符串
	 * 
	 * @param iXMLStr 要解析的XML字符串
	 * @throws IOException
	 * @throws JDOMException
	 * @throws Exception
	 */
	public void parserFromXMLString(String iXMLStr) throws IOException,JDOMException,Exception {
		try{
			SAXBuilder sb = new SAXBuilder();
			doc = sb.build(new StringReader(iXMLStr));
			root = doc.getRootElement();
		}catch(IOException ioe){
			throw ioe;
		}catch(JDOMException jdome){
			jdome = new JDOMException("不是有效的XML文档!");
			throw jdome;
		}catch(Exception e){
			throw e;
		}
	}

	/**
	 * 获取(根节点下)标签为iKeyCode的节点的个数
	 * 
	 * @param iKeyCode 节点标签
	 * @return int 节点个数
	 * @throws JDOMException
	 * @throws Exception
	 */
	public int getCountByTag(String iKeyCode) throws JDOMException,Exception {
		int iCount = 0;
		List lKeyList = null;
		try{
			lKeyList = root.getChildren(iKeyCode);
			if(lKeyList.size()>0){
				iCount = lKeyList.size();
			}else{
				iCount = 0;
			}
		}catch(Exception e){
			throw e;
		}
		return iCount;
	}

	/**
	 * 获取(节点iElem下)标签为iKeyCode的节点的个数
	 * 
	 * @param iKeyCode 节点标签
	 * @param iElem 节点Element
	 * @return int 节点个数
	 * @throws JDOMException
	 * @throws Exception
	 */
	public int getCountByTag(Element iElem,String iKeyCode) throws JDOMException,Exception {
		int iCount = 0;
		List lKeyList = null;
		try{
			lKeyList = iElem.getChildren(iKeyCode);
			if(lKeyList.size()>0){
				iCount = lKeyList.size();
			}else{
				iCount = 0;
			}
		}catch(Exception e){
			throw e;
		}
		return iCount;
	}

	/**
	 * 获取根节点的下级子节点的个数
	 * 
	 * @return int 节点个数
	 * @throws JDOMException
	 * @throws Exception
	 */
	public int getCount() throws JDOMException,Exception {
		int iCount = 0;
		List lKeyList = null;
		try{
			lKeyList = root.getChildren();
			if(lKeyList.size()>0){
				iCount = lKeyList.size();
			}else{
				iCount = 0;
			}
		}catch(Exception e){
			throw e;
		}
		return iCount;
	}

	/**
	 * 获取节点iElem的下级子节点的个数
	 * 
	 * @param iElem 节点Element
	 * @return int 节点个数
	 * @throws JDOMException
	 * @throws Exception
	 */
	public int getCount(Element iElem) throws JDOMException,Exception {
		int iCount = 0;
		List lKeyList = null;
		try{
			lKeyList = iElem.getChildren();
			if(lKeyList.size()>0){
				iCount = lKeyList.size();
			}else{
				iCount = 0;
			}
		}catch(Exception e){
			throw e;
		}
		return iCount;
	}

	/**
	 * 获取(根节点下)标签为iKeyCode的所有节点的值
	 * 
	 * @param iKeyCode 节点标签
	 * @return String[] 节点的值
	 * @throws JDOMException
	 * @throws Exception
	 */
	public String[] getKeyValues(String iKeyCode) throws JDOMException,Exception {
		String[] strArrValue = null;
		List lKeyList = null;
		try{
			lKeyList = root.getChildren(iKeyCode);
			if(lKeyList.size()>0){
				strArrValue = new String[lKeyList.size()];
				for(int i = 0; i<lKeyList.size(); i++ ){
					Element item = (Element)lKeyList.get(i);
					strArrValue[i] = item.getText();
				}
			}else{
				throw new Exception("根下没有此节点："+iKeyCode);
			}
		}catch(Exception e){
			throw e;
		}
		return strArrValue;
	}

	/**
	 * 获取(节点iElem下)标签为iKeyCode的所有节点的值
	 * 
	 * @param iKeyCode 节点标签
	 * @param iElem 节点Element
	 * @return String[] 节点的值
	 * @throws JDOMException
	 * @throws Exception
	 */
	public String[] getKeyValues(Element iElem,String iKeyCode) throws JDOMException,Exception {
		String[] strArrValue = null;
		List lKeyList = null;
		try{
			lKeyList = iElem.getChildren(iKeyCode);
			if(lKeyList.size()>0){
				strArrValue = new String[lKeyList.size()];
				for(int i = 0; i<lKeyList.size(); i++ ){
					Element item = (Element)lKeyList.get(i);
					strArrValue[i] = item.getText();
				}
			}else{
				throw new Exception("节点"+iElem.getName()+"下没有此子节点："+iKeyCode);
			}
		}catch(Exception e){
			throw e;
		}
		return strArrValue;
	}

	/**
	 * 获取(根节点下)标签为iKeyCode的第一个节点的值
	 * 
	 * @param iKeyCode 节点标签
	 * @return String 第一个节点的值
	 * @throws JDOMException
	 * @throws Exception
	 */
	public String getKeyValue(String iKeyCode) throws JDOMException,Exception {
		String strValue = null;
		List lKeyList = null;
		try{
			lKeyList = root.getChildren(iKeyCode);
			if(lKeyList.size()>0){
				Element item = (Element)lKeyList.get(0);
				strValue = item.getText();
			}else{
				throw new Exception("根下没有此节点："+iKeyCode);
			}
		}catch(Exception e){
			throw e;
		}
		return strValue;
	}

	/**
	 * 获取(根节点下)标签为iKeyCode的第index+1个节点的值
	 * 
	 * @param iKeyCode 节点标签
	 * @param index 节点的下标
	 * @return String 第index+1个节点的值
	 * @throws JDOMException
	 * @throws Exception
	 */
	public String getKeyValue(String iKeyCode,int index) throws JDOMException,Exception {
		String strValue = null;
		List lKeyList = null;
		try{
			lKeyList = root.getChildren(iKeyCode);
			if(lKeyList.size()>0){
				if(index>=lKeyList.size())
					throw new Exception("下标越界："+index);
				else{
					Element item = (Element)lKeyList.get(index);
					strValue = item.getText();
				}
			}else{
				throw new Exception("根下没有此节点："+iKeyCode);
			}
		}catch(Exception e){
			throw e;
		}
		return strValue;
	}

	/**
	 * 获取(节点iElem下)标签为iKeyCode的第一个节点的值
	 * 
	 * @param iKeyCode 节点标签
	 * @param iElem 节点Element
	 * @return String 第一个节点的值
	 * @throws JDOMException
	 * @throws Exception
	 */
	public String getKeyValue(Element iElem,String iKeyCode) throws JDOMException,Exception {
		String strValue = null;
		List lKeyList = null;
		try{
			lKeyList = iElem.getChildren(iKeyCode);
			if(lKeyList.size()>0){
				Element item = (Element)lKeyList.get(0);
				strValue = item.getText();
			}else{
				throw new Exception("节点"+iElem.getName()+"下没有此子节点："+iKeyCode);
			}
		}catch(Exception e){
			throw e;
		}
		return strValue;
	}

	/**
	 * 获取(节点iElem下)标签为iKeyCode的第index+1个节点的值
	 * 
	 * @param iKeyCode 节点标签
	 * @param iElem 节点Element
	 * @param index 节点的下标
	 * @return String 第index+1个节点的值
	 * @throws JDOMException
	 * @throws Exception
	 */
	public String getKeyValue(Element iElem,String iKeyCode,int index) throws JDOMException,Exception {
		String strValue = null;
		List lKeyList = null;
		try{
			lKeyList = iElem.getChildren(iKeyCode);
			if(lKeyList.size()>0){
				if(index>=lKeyList.size())
					throw new Exception("下标越界："+index);
				else{
					Element item = (Element)lKeyList.get(index);
					strValue = item.getText();
				}
			}else{
				throw new Exception("根下没有此节点："+iKeyCode);
			}
		}catch(Exception e){
			throw e;
		}
		return strValue;
	}

	/**
	 * 设置节点iElem属性为iAttributeCode的值
	 * 
	 * @param iAttributeCode 节点的值
	 * @param iElem 节点Element
	 * @throws JDOMException
	 * @throws Exception
	 */
	public String getAttribute(Element iElem,String iAttributeCode) throws JDOMException,Exception {
		Attribute attr = null;
		String strAttributeValue = "";
		try{
			attr = iElem.getAttribute(iAttributeCode);
			if(attr==null){
				throw new Exception("");
			}else
				strAttributeValue = attr.getValue();
		}catch(Exception e){
			throw e;
		}
		return strAttributeValue;
	}

	/**
	 * 获取(节点iElem下)标签为iKeyCode的第一个节点
	 * 
	 * @param iKeyCode 节点标签
	 * @param iElem 节点Element
	 * @return Element 第一个节点
	 * @throws JDOMException
	 * @throws Exception
	 */
	public Element getElement(Element iElem,String iKeyCode) throws JDOMException,Exception {
		Element element = null;
		List lKeyList = null;
		try{
			lKeyList = iElem.getChildren(iKeyCode);
			if(lKeyList.size()>0){
				element = (Element)lKeyList.get(0);
			}else{
				throw new Exception("节点"+iElem.getName()+"下没有此子节点："+iKeyCode);
			}
		}catch(Exception e){
			throw e;
		}
		return element;
	}

	/**
	 * 获取(节点iElem下)标签为iKeyCode的第index+1个节点
	 * 
	 * @param iKeyCode 节点标签
	 * @param iElem 节点Element
	 * @param index 节点的下标
	 * @return Element 第index+1个节点
	 * @throws JDOMException
	 * @throws Exception
	 */
	public Element getElement(Element iElem,String iKeyCode,int index) throws JDOMException,Exception {
		Element element = null;
		List lKeyList = null;
		try{
			lKeyList = iElem.getChildren(iKeyCode);
			if(lKeyList.size()>0){
				if(index>=lKeyList.size())
					throw new Exception("下标越界："+index);
				else
					element = (Element)lKeyList.get(index);
			}else{
				throw new Exception("节点"+iElem.getName()+"下没有此子节点："+iKeyCode);
			}
		}catch(Exception e){
			throw e;
		}
		return element;
	}

	/**
	 * 获取(根节点下)标签为iKeyCode的第一个节点
	 * 
	 * @param iKeyCode 节点标签
	 * @return Element 第一个节点
	 * @throws JDOMException
	 * @throws Exception
	 */
	public Element getElement(String iKeyCode) throws JDOMException,Exception {
		Element element = null;
		List lKeyList = null;
		try{
			lKeyList = root.getChildren(iKeyCode);
			if(lKeyList.size()>0){
				element = (Element)lKeyList.get(0);
			}else{
				throw new Exception("根下没有此子节点："+iKeyCode);
			}
		}catch(Exception e){
			throw e;
		}
		return element;
	}

	/**
	 * 获取(根节点下)标签为iKeyCode的第index+1个节点
	 * 
	 * @param iKeyCode 节点标签
	 * @param index 节点的下标
	 * @return Element 第index+1个节点
	 * @throws JDOMException
	 * @throws Exception
	 */
	public Element getElement(String iKeyCode,int index) throws JDOMException,Exception {
		Element element = null;
		List lKeyList = null;
		try{
			lKeyList = root.getChildren(iKeyCode);
			if(lKeyList.size()>0){
				if(index>=lKeyList.size())
					throw new Exception("下标越界："+index);
				else
					element = (Element)lKeyList.get(index);
			}else{
				throw new Exception("根下没有此子节点："+iKeyCode);
			}
		}catch(Exception e){
			throw e;
		}
		return element;
	}

	/**
	 * 设置节点iElem的值
	 * 
	 * @param iKeyValue 节点的值
	 * @param iElem 节点Element
	 * @throws JDOMException
	 * @throws Exception
	 */
	public void setKeyValue(Element iElem,String iKeyValue) throws JDOMException,Exception {
		List lKeyList = null;
		try{
			lKeyList = iElem.getChildren();
			if(lKeyList.size()>0){
				throw new Exception("节点"+iElem.getName()+"下还有子节点，不能设置该节点的值!");
			}else
				iElem.setText(iKeyValue);
		}catch(Exception e){
			throw e;
		}
	}

	/**
	 * 设置节点iElem属性iAttributeCode的值iAttributeValue
	 * 
	 * @param iAttributeCode 节点的属性的名称
	 * @param iAttributeValue 节点的值
	 * @param iElem 节点Element
	 * @param blFlag 标志，如果不在在该属性时true增加/false不增加并抛出异常
	 * @throws JDOMException
	 * @throws Exception
	 */
	public void setAttribute(Element iElem,String iAttributeCode,String iAttributeValue,boolean blFlag) throws JDOMException,Exception {
		Attribute attr = null;
		try{
			attr = iElem.getAttribute(iAttributeCode);
			if(attr==null){
				if(blFlag){
					iElem.setAttribute(iAttributeCode,iAttributeValue);
				}else
					throw new Exception("节点"+iElem.getName()+"没有属性"+iAttributeCode+"，不能设置该属性的值!");
			}else
				iElem.setAttribute(iAttributeCode,iAttributeValue);
		}catch(Exception e){
			throw e;
		}
	}

	/**
	 * 设置节点iElem属性iAttributeCode的值iAttributeValue
	 * 
	 * @param iAttributeCode 节点的属性的名称
	 * @param iAttributeValue 节点的值
	 * @param iElem 节点Element
	 * @throws JDOMException
	 * @throws Exception
	 */
	public void setAttribute(Element iElem,String iAttributeCode,String iAttributeValue) throws JDOMException,Exception {
		try{
			setAttribute(iElem,iAttributeCode,iAttributeValue,false);
		}catch(Exception e){
			throw e;
		}
	}

	/**
	 * 在节点iParentElem下增加一个子节点
	 * 
	 * @param iParentElem 父节点
	 * @param iSubElem 子节点
	 * @throws JDOMException
	 * @throws Exception
	 */
	public void addElement(Element iParentElem,Element iSubElem) throws JDOMException,Exception {
		try{
			iParentElem.addContent(iSubElem);
		}catch(Exception e){
			throw e;
		}
	}

	/**
	 * 在节点iParentElem下增加一个子节点
	 * 
	 * @param iParentElem 父节点
	 * @param iSubElem 子节点
	 * @param index 子节点添加的位置
	 * @throws JDOMException
	 * @throws Exception
	 */
	public void addElement(Element iParentElem,Element iSubElem,int index) throws JDOMException,Exception {
		try{
			iParentElem.addContent(index,iSubElem);
		}catch(Exception e){
			throw e;
		}
	}

	/**
	 * 创建节点
	 * 
	 * @param iElemCode 节点的名称
	 * @param iElemValue 节点的值
	 * @return Element 要创建的节点
	 * @throws JDOMException
	 * @throws Exception
	 */
	public Element createElement(String iElemCode,String iElemValue) throws JDOMException,Exception {
		Element elem = null;
		try{
			elem = new Element(iElemCode,iElemValue);
		}catch(Exception e){
			throw e;
		}
		return elem;
	}

	/**
	 * 创建文档
	 * 
	 * @param iRootName 根节点的名称
	 */
	public void createDocument(String iRootName) throws JDOMException,Exception {
		try{
			if(doc==null) doc = new Document(new Element(iRootName));
			root = doc.getRootElement();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 保存文档
	 * 
	 * @param outputFileSrc 要保存的文档的路径
	 */

	public void saveDocument(String outputFileSrc) throws IOException,JDOMException,Exception {
		try{
			XMLOutputter outputter;
//			outputter = new XMLOutputter("  ",false,"GB2312");
			outputter = new XMLOutputter();
			outputter.output(doc,new FileOutputStream(outputFileSrc));
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/*
	 * <p>Description: 获取根节点下相同节点list对象，满足多条纪录XML格式</p> <p>Copyright: Copyright (c) 2004</p>
	 * <p>Company: sinosoft 东安项目组</p>
	 * 
	 * @author luyang
	 * 
	 * @date 2004-11-01
	 */
	public List getChildrenElement(String strXML,String strElement) throws IOException,JDOMException,Exception {
		List list = null;
		try{
			SAXBuilder sb = new SAXBuilder();
			doc = sb.build(new StringReader(strXML));
			root = doc.getRootElement();
			list = root.getChildren(strElement);
		}catch(IOException ioe){
			throw ioe;
		}catch(JDOMException jdome){
			jdome = new JDOMException("不是有效的XML文档!");
			throw jdome;
		}catch(Exception e){
			throw e;
		}
		return list;
	}

	/**
	 * 主函数
	 */
	public static void main(String[] args) {

	}

}
