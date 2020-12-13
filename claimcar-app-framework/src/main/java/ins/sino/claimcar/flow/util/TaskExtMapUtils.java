package ins.sino.claimcar.flow.util;

import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.DataUtils;
import ins.platform.utils.DateUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * BussTag,ShowInfoXML拼装与解析工具
 * @author ★LiuPing
 * @CreateTime 2016年1月26日
 */
public class TaskExtMapUtils {

	private static Logger logger = LoggerFactory.getLogger(TaskExtMapUtils.class);
	private static Map<String,String[]> bussTagShowMap = null;// // 控制显示的数据和显示内容
	private static Map<String,String> xmlPropMap = null;// 将不同业务字段映射到工作流的同一个字段
	private static Map<String,String> xmlTransferMap = null;// 存储需要翻译的字段 value值为翻译类型
	// 1-默认default 2-主要primary ,3-次要secondary,4-成功绿success ，5-警告warning，6-危险danger
	private static String[] classLevel = {"","default","primary","secondary","success","warning","danger"};
	private Map<String,String> bussTagMap = null;
	private Map<String,String> showInfoXMLMap = null;
	private PropertyUtilsBean beanUtil = null;

	static{
		initTagShowMap();
	}

	public TaskExtMapUtils(){
		bussTagMap = new HashMap<String,String>();
		showInfoXMLMap = new HashMap<String,String>();
		beanUtil = BeanUtilsBean.getInstance().getPropertyUtils();
	}

	/**
	 * 添加属性到TagMap
	 * @param bean
	 * @param propertys
	 * @modified: ☆LiuPing(2016年1月26日 ): <br>
	 */
	public void addTagMap(Object bean,String... propertys) {
		if(bean==null) return;
		Object value = null;
		String valueStr = null;
		for(String prop:propertys){
			try{
				value = beanUtil.getSimpleProperty(bean,prop);
			}catch(Exception e){
				logger.error(prop,e);
			}
			valueStr = objToString(value);
			if(StringUtils.isNotBlank(valueStr)){
				bussTagMap.put(prop,valueStr);
			}
		}
	}

	/**
	 * 添加属性到 showInfoXMLMap
	 * @param bean
	 * @param propertys
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @modified: ☆LiuPing(2016年1月26日 ): <br>
	 */
	public void addXmlMap(Object bean,String... propertys) {
		Object value = null;
		String valueStr = null;
		for(String prop:propertys){
			try{
				value = beanUtil.getSimpleProperty(bean,prop);
			}catch(Exception e){
				logger.error(prop,e);
			}
			if(xmlPropMap.containsKey(prop)){
				prop = xmlPropMap.get(prop);
			}
			if(xmlTransferMap.containsKey(prop)){
				String transType = xmlTransferMap.get(prop);
				value = CodeTranUtil.transCode(transType, objToString(value));
			}
			valueStr = objToString(value);
			showInfoXMLMap.put(prop,valueStr);

		}
	}

	public void addXmlMap(String key,String value) {
		showInfoXMLMap.put(key,value);
	}

	public void putXmlMap(Map<String,String> xmlMap) {
		showInfoXMLMap.putAll(xmlMap);
	}

	private String objToString(Object value) {
		String valueStr = null;
		if(value==null){
			return "";
		}else if(value instanceof java.lang.Number){
			valueStr = DataUtils.toString((java.lang.Number)value);
		}else if(value instanceof java.util.Date){
			valueStr = DateUtils.dateToStr((java.util.Date)value,DateUtils.YToSec);
		}else{
			valueStr = (String)value;
		}
		return valueStr;
	}

	public String getBussTag() {
		return toJsonString(bussTagMap);
	}
	public Map<String,String> getBussTagMap() {
		return bussTagMap;
	}
	

	public String getShowInfoXML() {
		return toJsonString(showInfoXMLMap);
	}
	
	public Map<String,String> getShowInfoXMLMap() {
		return showInfoXMLMap;
	}

	/**
	 * 将数据库中的 bussTag 与bussTagMap合并，如果key冲突已Map中新的为准
	 * @param bussTag
	 * @param bussTagMap
	 * @return
	 * @modified: ☆LiuPing(2016年1月26日 ): <br>
	 */
	public static String joinBussTag(String bussTag,Map<String,String> bussTagMap) {
		if(bussTagMap==null) bussTagMap = new HashMap<String,String>();
		if( !StringUtils.isBlank(bussTag)){
			Map<String,String> oldTagMap = jsonToMap(bussTag);
			for(String key:oldTagMap.keySet()){
				if( !bussTagMap.containsKey(key)){
					bussTagMap.put(key,oldTagMap.get(key));
				}
			}
		}
		return toJsonString(bussTagMap);
	}

	public static String toJsonString(Map<String,String> map) {
		return JSONObject.toJSONString(map);
	}

	@SuppressWarnings("unchecked")
	public static Map<String,String> jsonToMap(String mapStr) {
		JSONObject json = JSONObject.parseObject(mapStr);
		Map<String,String> map = JSONObject.toJavaObject(json,Map.class);

		return map;
	}

	public static void main(String[] args) {
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("1","A");
		map.put("2","B");
		map.put("3","C");
		map.put("4","D");
		String str = TaskExtMapUtils.toJsonString(map);
		System.out.println(str);
		Map<String,String> map2 = TaskExtMapUtils.jsonToMap(str);
		System.out.println(map2+"==="+map2.get("4"));
		String bustag = "{\"refuce\":\"1\",\"mercyFlag\":\"1\",\"isSubRogation\":\"0\",\"isClaimSelf\":\"1\"}";
		System.out.println(TaskExtMapUtils.jsonTagToHtml(bustag,null));
	}

	/**
	 * 将tag转换为html
	 * @param bussTag
	 * @param otherTagMap 其他tag的Map。如果重名会覆盖bussTag
	 * @return
	 * @modified: ☆LiuPing(2016年1月28日 ): <br>
	 */
	public static StringBuffer jsonTagToHtml(String bussTag,Map<String,String> otherTagMap) {
		StringBuffer bussTagBf = new StringBuffer();
		if(bussTag==null) return bussTagBf;
		Map<String,String> bussTagMap = jsonToMap(bussTag);
		if(otherTagMap!=null){
			bussTagMap.putAll(otherTagMap);
		}
		String key;
		String[] show;
		for(String prop:bussTagMap.keySet()){
			key = prop+bussTagMap.get(prop);
			show = bussTagShowMap.get(key);
			if(show==null||show.length==0) continue;
			bussTagBf.append("<span class='badge label-").append(show[1]).append(" radius' ");
			bussTagBf.append("title='").append(show[2]).append("'>");
			bussTagBf.append(show[0]).append("</span>");
		}
		return bussTagBf;
	}
	
	/**
	 * 流程图中的业务标示
	 * @param bussTag
	 * @return
	 */
	public static StringBuffer jsonTagToHtmlInFlow(String bussTag) {
		StringBuffer bussTagBf = new StringBuffer();
		if(bussTag==null) return bussTagBf;
		Map<String,String> bussTagMap = jsonToMap(bussTag);
		String key;
		String[] show;
		for(String prop:bussTagMap.keySet()){
			key = prop+bussTagMap.get(prop);
			show = bussTagShowMap.get(key);
			if(show==null||show.length==0) continue;
			if("isRePair".equals(prop)){
				bussTagBf.append("<a class='badge label-").append(show[1]).append(" radius' ").append(" style='font-size:14px;margin-left:5px;' ").append(" onclick='messageSearch()' ");
				bussTagBf.append("title='").append(show[2]).append("'>");
				bussTagBf.append(show[2]).append("</a>");
			}else{
				bussTagBf.append("<span class='badge label-").append(show[1]).append(" radius' ").append(" style='font-size:14px;margin-left:5px;' ");
				bussTagBf.append("title='").append(show[2]).append("'>");
				bussTagBf.append(show[2]).append("</span>");
			}
			
		}
		return bussTagBf;
	}

	private static void initTagShowMap() {
		xmlPropMap = new HashMap<String,String>();
		xmlPropMap.put("licenseNo","license");//车牌号
		xmlPropMap.put("sumVeriLossFee","sumVeriLoss");//核损金额
		xmlPropMap.put("sumDefloss","sumVeripLoss");//财产待核价金额
		
		xmlTransferMap = new HashMap<String,String>();
		xmlTransferMap.put("serialNo","DefLossItemType");//损失方

		bussTagShowMap = new LinkedHashMap<String,String[]>();
		// 这里的顺序控制的是显示的顺序
		// 1-默认default 2-主要primary ,3-次要secondary,4-成功绿success ，5-警告warning，6-危险danger
		putToShowMap("tempRegistFlag","1","临时报案","临",2);//
		putToShowMap("isAlarm","1","已报警","警",5);//
		putToShowMap("mercyFlag","1","紧急案件","急",6);// 案件紧急程度
		putToShowMap("isSubRogation","1","代位求偿","代",3);// 代位求偿标志
		putToShowMap("isClaimSelf","1","互碰自赔","互",2);// 是否互碰自赔
		putToShowMap("tpFlag","1","通赔","通",3);// 通赔标识
		putToShowMap("IsMajorCase","1","重大案件","重",3);// 是否重大案件
		putToShowMap("qdcaseType","1"," 快速处理","快",4);// 快速处理类型
		putToShowMap("customerLevel","V1","VIP1","V1",2);// 客户等级
		putToShowMap("customerLevel","V2","VIP2","V2",2);// 客户等级
		putToShowMap("customerLevel","V3","VIP3","V3",2);// 客户等级
		putToShowMap("isJQFraud","1","结案拒赔(交强)","拒",5);//  结案拒赔
        putToShowMap("isSYFraud","1","结案拒赔(商业)","拒",5);//  结案拒赔
		putToShowMap("surveyFlag","1","调查","调",6);// 调查
		putToShowMap("isMobileCase","1","移动端案件","移",5);//是否为移动端案件
		putToShowMap("isMobileCase","2","移动端案件","移",5);//车童网
		putToShowMap("isMobileCase","3","移动端案件","移",5);//民太安
		putToShowMap("timeOut","1","超时","超",6);//是否为超时案件
		putToShowMap("isQuickCase","1","快赔案件","快",5);//是否为快赔案件	
		putToShowMap("isOldClaim","1","旧理赔案件","旧",6);//是否为旧理赔案件
		putToShowMap("selfClaimFlag","1","自助理赔案件","自",5);//是否为自助理赔案件
		putToShowMap("isGBFlag","1","联共保业务","共",3);//是否为联共保业务
		putToShowMap("isRePair","1","推送修","推",3);// 推送修
	}
	
	/**
	 * @param prop 属性
	 * @param value 值
	 * @param title 显示的title
	 * @param shortTxt 简短文字
	 * @param level 显示图标等级
	 * @modified: ☆LiuPing(2016年1月28日 ): <br>
	 */
	private static void putToShowMap(String prop,String value,String title,String shortTxt,int level) {
		String[] show = {shortTxt,classLevel[level],title};
		String key = prop+value;
		bussTagShowMap.put(key,show);
	}



}