/******************************************************************************
* CREATETIME : 2015年3月9日 下午7:57:19
* FILE       : ins.platform.utils.ReflectUtils
******************************************************************************/
package ins.platform.utils;


/**
 * <pre> 反射工具类 </pre>
 * @author ★qianxin
 * @modified:
 * ☆qianxin(2015年3月9日 下午7:57:19): <br>
 */
public class ReflectUtils {

	/**
	 * 根据方法名,参数,实例获取方法返回对象
	 * @param methodName 方法名
	 * @param parameters 参数
	 * @param obj 实例
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆qianxin(2015年3月9日 下午8:24:15): <br>
	 */
	public static Object getMethodValue(String methodName,Class<?> paramClass,Object[] parameters,Object obj) throws Exception{
		Class<?> clazz = obj.getClass();
		Object object = null;
		if(paramClass == null){
			object = clazz.getMethod(methodName).invoke(obj,parameters);
		}else{
			object = clazz.getMethod(methodName,paramClass).invoke(obj,parameters);
		}
		return object;
	}
	
	/**
	 * 根据变量名,实例获取getter
	 * @param fieldName
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ☆qianxin(2015年3月10日 上午8:54:59): <br>
	 */
	public static Object getFieldValue(String fieldName,Object obj) throws Exception{
		String methodName = getFieldGetterMethodName(fieldName);
		return getMethodValue(methodName,null,null,obj);
	}
	
	public static void setFieldValue(String fieldName,Class<?> paramClass,Object obj,Object subObject) throws Exception{
		String methodName = getFieldSetterMethodName(fieldName);
		Object[] parameters = new Object[]{subObject};
		getMethodValue(methodName,paramClass,parameters,obj);
	}
	
	/**
	 * 将属性名拼装为 get方法
	 * 
	 * @param fieldName
	 * @return
	 * @modified: ☆hezheng(Mar 19, 2014 6:19:24 PM): <br>
	 */
	private static String getFieldGetterMethodName(String fieldName) {
		String firstLetter = fieldName.substring(0, 1);
		return "get" + firstLetter.toUpperCase() + fieldName.substring(1);
	}
	
	/**
	 * 将属性名拼装为 get方法
	 * 
	 * @param fieldName
	 * @return
	 * @modified: ☆hezheng(Mar 19, 2014 6:19:24 PM): <br>
	 */
	private static String getFieldSetterMethodName(String fieldName) {
		String firstLetter = fieldName.substring(0, 1);
		return "set" + firstLetter.toUpperCase() + fieldName.substring(1);
	}
	
}
