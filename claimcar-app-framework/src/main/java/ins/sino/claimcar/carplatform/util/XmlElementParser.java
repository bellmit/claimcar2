/******************************************************************************
* CREATETIME : 2016年6月24日 下午1:12:47
******************************************************************************/
package ins.sino.claimcar.carplatform.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年6月24日
 */
public class XmlElementParser {

	private static Logger logger = LoggerFactory.getLogger(XmlElementParser.class);
	private PropertyUtilsBean beanUtil;
	private Map<Integer,Object> elmValueMap = null;

	public XmlElementParser(){
		beanUtil = BeanUtilsBean.getInstance().getPropertyUtils();
	}

	/**
	 * 同时获取多个注解对应的值，返回注解名和值的Map，注解名可以不区分大小写
	 * @param object
	 * @param propertys
	 * @return
	 * @modified: ☆LiuPing(2016年6月24日 ): <br>
	 */
	public void findElementValue(Object object,String... elmNames) {
		if(object==null) return;
		elmValueMap = new HashMap<Integer,Object>();

		Map<String,Integer> elmMap = new HashMap<String,Integer>();
		int i = 1;
		for(String elm:elmNames){
			elmMap.put(elm.toUpperCase(),i);// key都是大写，value是原入参的值
			i++ ;
		}
		findElementValue(object,Object.class,elmMap);
	}

	/**
	 * 递归多次寻找值，放到 elmValueMap 里面
	 * @param object
	 * @param parentClazz
	 * @param elmMap
	 * @modified: ☆LiuPing(2016年6月24日 ): <br>
	 */
	private void findElementValue(Object object,Class<?> parentClazz,Map<String,Integer> elmMap) {

		Class<?> objClazz = object.getClass();
		Class<?> clazz = object.getClass();// 用于循环的

		if(elmMap.isEmpty()) return;// 没有需要获取的属性了，直接返回

		// 递归父类
		while(clazz!=Object.class&& !elmMap.isEmpty()){
			Field[] fields = clazz.getDeclaredFields();

			for(Field fld:fields){
				if(elmMap.isEmpty()) return;// 没有需要获取的属性了，直接返回

				XmlElement xmlEmt = fld.getAnnotation(XmlElement.class);
				if(xmlEmt!=null&&elmMap.containsKey(xmlEmt.name().toUpperCase())){// 匹配到了Element
					Object obj = getPropertyValue(object,fld.getName());
					String emlKey = xmlEmt.name().toUpperCase();
					elmValueMap.put(elmMap.get(emlKey),obj);// 把值放到map
					elmMap.remove(emlKey);// 已经有值的就删除
					continue;
				}

				Class<?> type = fld.getType();
				if(type.isAssignableFrom(parentClazz)){// 当前类型==上层类型，表示是反向关联,不用再处理
					continue;
				}else if(isXmlClass(type)){
					Object obj = getPropertyValue(object,fld.getName());
					if(obj==null) continue;
					findElementValue(obj,objClazz,elmMap);
				}else if(type.isAssignableFrom(List.class)){
					continue;
					/*暂时不需要寻找 list中 的关键值
					System.out.println(fld.getName()+"=isList");
					List<?> list = (List<?>)getPropertyValue(object,fld.getName());
					if(list==null) continue;
					for(Object obj:list){
						if(isXmlClass(obj.getClass())){
							elmValueMap.putAll(findElementValue(obj,objClazz,elmMap));
						}
					}*/
				}
			}
			clazz = clazz.getSuperclass();
		}

	}

	/**
	 * 判断是否为Xml类
	 * @param clazz
	 * @return
	 * @modified: ☆LiuPing(2016年6月24日 ): <br>
	 */
	private static boolean isXmlClass(Class<?> clazz) {
		XmlAccessorType xmlType = clazz.getAnnotation(XmlAccessorType.class);
		if(xmlType!=null){
			return true;
		}
		return false;
	}

	private Object getPropertyValue(Object bean,String name) {
		Object obj = null;
		try{
			obj = beanUtil.getProperty(bean,name);
		}catch(IllegalAccessException e){
			logger.error(e.getMessage(),e);
		}catch(InvocationTargetException e){
			logger.error(e.getMessage(),e);
		}catch(NoSuchMethodException e){
			logger.error(e.getMessage(),e);
		}
		return obj;
	}

	/**
	 * 已游标的方式获取值，从1开始
	 * @param idx
	 * @return
	 * @modified: ☆LiuPing(2016年6月24日 ): <br>
	 */
	public String getString(int idx) {
		return getObject(idx,String.class);
	}

	/**
	 * 已游标的方式获取值，从1开始
	 * @param idx
	 * @param clazz
	 * @return
	 * @modified: ☆LiuPing(2016年6月24日 ): <br>
	 */
	@SuppressWarnings("unchecked")
	public <T> T getObject(int idx,Class<T> clazz) {
		Object obj = elmValueMap.get(idx);
		return (T)obj;
	}
}
