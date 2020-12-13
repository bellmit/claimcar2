package ins.sino.claimcar.carplatform.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

/**
 * xml解析工具类
 */
public class XmlUtil {
    private static Logger logger = LoggerFactory.getLogger(XmlUtil.class);

    /**
     * 解析xml文本
     * @param xml
     * @return
     */
    public static Map<String, Object> parseXml(String xml) {
        if (StringUtils.isBlank(xml)) {
            return null;
        }
        try {
            return parseXml(xml.getBytes("UTF-8"), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析xml文本
     * @param xml
     * @return
     */
    public static Map<String, String> parseXmlToStrMap(String xml) {
        if (StringUtils.isBlank(xml)) {
            return null;
        }
        Map<String, String> result = new HashMap<String, String>();
        try {
            Map<String, Object> map = parseXml(xml);
            for (String key : map.keySet()) {
                Object value = map.get(key);
                result.put(key, String.valueOf(value));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析xml数据
     * @param xmlBytes
     * @param charset
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseXml(byte[] xmlBytes, String charset) {
        SAXReader reader = new SAXReader(false);
        InputSource source = new InputSource(new ByteArrayInputStream(xmlBytes));
        source.setEncoding(charset);
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Document doc = reader.read(source);
            Iterator<Element> iter = doc.getRootElement().elementIterator();
            while (iter.hasNext()) {
                Element e = iter.next();
                if (!e.elementIterator().hasNext()) {
                    map.put(e.getName(), e.getTextTrim());
                    continue;
                }
                Iterator<Element> iterator = e.elementIterator();
                Map<String, String> param = new HashMap<String, String>();
                while (iterator.hasNext()) {
                    Element el = iterator.next();
                    param.put(el.getName(), el.getTextTrim());
                }
                map.put(e.getName(), param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 加载本地XML文件
     *
     * @param file
     * @return
     */
    public  static Document LoadXML(File file){
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(file);
        } catch (DocumentException e) {
            logger.error("加载文件路径出错:" + e);
        }
        return document;
    }
    /**
     * 读xml文件内容并输出
     *
     * @param xmlContent
     * @param
     * @return
     */
    public static  Map<String, String> readXML(String xmlContent) throws DocumentException {
        Map map = new HashMap();
        if (org.springframework.util.StringUtils.isEmpty(xmlContent)) {
            return map;
        }
        Document document = DocumentHelper.parseText(xmlContent);
        //获取根节点元素对象
        Element element = document.getRootElement();
        //遍历所有的元素节点
        int i = -1;
        listAllNodes(element, map, i);
        return map;
    }
    /**
     * 将xml文件的内容转换为Hashmap
     *
     * @param path
     * @param key
     * @return dictionary
     */
    public HashMap xmlToHashMap(String path,String key,String type,HashMap dictionary){
        Document data=LoadXML(new File(path));
        Element element = data.getRootElement();
        xmlElementToHashmapUrl(data,key,dictionary);
        return dictionary;
    }


    /**
     * 遍历当前节点元素下面的所有(元素的)子节点
     *
     * @param node
     */
    public static void listAllNodes(Element node, Map map, int j) {
        // 获取当前节点的所有属性节点
        List<Attribute> list = node.attributes();
        // 遍历属性节点
        for (Attribute attr : list) {
            map.put(node.getName()+ j +"#" + attr.getName(), attr.getText());
        }

        //查询节点信息不包括url等属性
        if (!(node.getTextTrim().equals("")) && CollectionUtils.isEmpty(list)) {
            map.put(node.getName(), node.getText());
        }

        // 当前节点下面子节点迭代器
        Iterator<Element> it = node.elementIterator();
        // 遍历
        while (it.hasNext()) {
            Element e = it.next();
            List<Attribute> pageAttr = e.attributes();
            //仅对pages -> page -> attr
            if (CollectionUtils.isNotEmpty(pageAttr)){
                j++;
            }
            listAllNodes(e, map, j);
        }
    }
    /**
     * @parameter: document
     * @parameter: treeUrl
     * @parameter: dictionary
     * @return:
     */
    public void xmlElementToHashmapUrl(Document document, String treeUrl, HashMap dictionary) {

        List list = document.selectNodes(treeUrl);
        Iterator iter = list.iterator();
        while (iter.hasNext()) {

            Element el = (Element) iter.next();
            // 获取当前节点的所有属性节点
            List<Attribute> sttrlist = el.attributes();
            // 遍历属性节点
            for (Attribute attr : sttrlist) {
                if (!(el.getTextTrim().equals(""))) {
                    dictionary.put(attr.getText(), el.getText());
                }
            }

        }
    }
    /**
     * 修改某个节点的值
     *
     * @param document  模板xml document
     * @param filename 生成的临时xml
     * @param treeUrl 待写入的层级
     * @param value  待写入的key， value
     */
    public static void modifyXMLSingleNode(Document document,String filename,String treeUrl,String value) throws Exception {
        if (StringUtils.isBlank(value)){
            return;
        }
        Element e = (Element)document.selectSingleNode(treeUrl);
        e.setText(value);
        XMLWriter output = new XMLWriter(
                new FileWriter(new File(filename)));
        output.write( document );
        output.close();
    }

    /**
     * @parameter:
     * @return:  生成xml报文
     */

    public static String transferXml(Object object) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = context.createMarshaller(); // 根据上下文获取marshaller对象

        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(object, baos);
        String xmlObj = new String(baos.toByteArray());  // 生成XML字符串
        return xmlObj;
    }

    /**
     * xml文件配置转换为对象
     *
     * @param xmlPath xml文件路径
     * @param load    java对象.Class
     * @return java对象
     * @throws JAXBException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <T> T xmlToBean(String xmlPath, Class<T> load) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(load);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new StringReader(xmlPath));
    }
}