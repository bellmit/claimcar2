/******************************************************************************
* CREATETIME : 2016年1月10日 下午8:10:30
******************************************************************************/
package ins.platform.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;

/**
 * sql拼装工具
 * @author ★LiuPing
 * @CreateTime 2016年1月10日
 */
public class SqlJoinUtils {

	private StringBuffer queryStr = null;
	private List<Object> paramValues = null;
	private PropertyUtilsBean beanUtil = null;
	private static final String AND = " AND ";

	// private static final String OR = " OR ";

	public SqlJoinUtils(){
		super();
		this.paramValues = new ArrayList<Object>();
		this.queryStr = new StringBuffer();
		beanUtil = BeanUtilsBean.getInstance().getPropertyUtils();
	}

	public void append(String str) {
		queryStr.append(str);
	}

	public void addParamValue(Object value) {
		paramValues.add(value);
	}

	public String getSql() {
		return queryStr.toString();
	}

	public Object[] getParamValues() {
		return paramValues.toArray();
	}

	/**
	 * 添加日期区间条件 ,自动去查找 xxxStart属性和xxxxEnd属性<br>
	 * 使用方法 sqlUtil.andDate(taskQueryVo,table_query,"reportTime","damageTime");
	 * @modified: ☆LiuPing(2016年1月10日 下午8:48:36): <br>
	 */
	public void andDate(Object bean,String tableAlias,String... datePropertys) throws IllegalAccessException,InvocationTargetException,NoSuchMethodException {

		for(String dateProp:datePropertys){

			Date startDate = (Date)beanUtil.getSimpleProperty(bean,dateProp+"Start");
			Date endDate = (Date)beanUtil.getSimpleProperty(bean,dateProp+"End");
			if(startDate!=null){
				queryStr.append(AND+tableAlias+"."+dateProp+">= ? ");
				paramValues.add(startDate);
			}

			if(endDate!=null){
				endDate = DateUtils.addDays(endDate,1);
				queryStr.append(AND+tableAlias+"."+dateProp+"< ? ");
				paramValues.add(endDate);
			}
		}

	}

	/**
	 * 添加相等的条件，仅支持字符串类型<br>
	 * 用法：sqlUtil.andEquals(taskQueryVo,table_query,"mercyFlag","customerLevel","reporterPhone");
	 * @modified: ☆LiuPing(2016年1月10日 下午9:00:37): <br>
	 */
	public void andEquals(Object bean,String tableAlias,String... propertys) throws IllegalAccessException,InvocationTargetException,NoSuchMethodException {

		Object value;
		String valueStr = null;
		for(String prop:propertys){
			value = beanUtil.getSimpleProperty(bean,prop);
			if(value!=null&&value instanceof String){
				valueStr = ((String)value).trim();
				if(StringUtils.isNotBlank(valueStr)){
					queryStr.append(AND+tableAlias+"."+prop+"= ? ");
					paramValues.add(valueStr);
				}
			}

		}
	}

	/**
	 * 添加Like条件,propertys的属性必须为String
	 * @modified: ☆LiuPing(2016年1月10日 下午9:00:37): <br>
	 */
	public void andLike(Object bean,String tableAlias,String... propertys) throws IllegalAccessException,InvocationTargetException,NoSuchMethodException {

		Object value;
		String valueStr = null;
		for(String prop:propertys){
			value = beanUtil.getSimpleProperty(bean,prop);
			if(value!=null&&value instanceof String){
				valueStr = (String)value;
				if(StringUtils.isNotBlank(valueStr)){
					queryStr.append(AND+tableAlias+"."+prop+" LIKE  ? ");
					paramValues.add("%"+valueStr+"%");
				}
			}
		}
	}

	/**
	 * 添加前后双Like条件,propertys的属性必须为String
	 * @param bean 对象
	 * @param tableAlias 表别名
	 * @param propertys 参数
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public void andLike2(Object bean,String tableAlias,String... propertys) throws IllegalAccessException,InvocationTargetException,NoSuchMethodException {

		Object value;
		String valueStr = null;
		for(String prop:propertys){
			value = beanUtil.getSimpleProperty(bean,prop);
			if(value!=null&&value instanceof String){
				valueStr = (String)value;
				if(StringUtils.isNotBlank(valueStr)){
					queryStr.append(AND+tableAlias+"."+prop+" LIKE  ? ");
					paramValues.add("%"+valueStr.trim()+"%");
				}
			}
		}
	}

	/**
	 * 多个属性，用同一个字段like
	 * @param bean 对象
	 * @param tableAlias 表别名
	 * @param fixField 指定的一个字段
	 * @param propertys 参数
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public void andLikeFixOne(String fixField, Object bean,String tableAlias,String... propertys) throws IllegalAccessException,InvocationTargetException,NoSuchMethodException {
		Object value;
		String valueStr = null;
		for(String prop:propertys){
			value = beanUtil.getSimpleProperty(bean,prop);
			if(value!=null&&value instanceof String){
				valueStr = (String)value;
				if(StringUtils.isNotBlank(valueStr)){
					queryStr.append(AND+tableAlias+"."+fixField+" LIKE  ? ");
					paramValues.add("%"+valueStr+"%");
				}
			}
		}
	}
	/**
	 * 添加后几位反转查询条件,propertys的属性必须为String
	 * @modified: ☆LiuPing(2016年1月10日 下午9:00:37): <br>
	 */
	public void andReverse(Object bean,String tableAlias,int revLength,String... propertys) throws IllegalAccessException,InvocationTargetException,NoSuchMethodException {

		Object value;
		String valueStr;
		for(String prop:propertys){
			value = beanUtil.getSimpleProperty(bean,prop);
			if(value!=null&&value instanceof String){
				valueStr = StringUtils.reverse(value.toString()).trim();
				if(valueStr.length()>0&&valueStr.length()<=revLength){
					queryStr.append(AND+tableAlias+"."+prop+"Rev LIKE ? ");
					paramValues.add(valueStr+"%");
				}else if(valueStr.length()>0){
					queryStr.append(AND+tableAlias+"."+prop+"Rev = ? ");
					paramValues.add(valueStr);
				}
			}
		}
	}

	/**
	 * 根据数组长度，得到相应的问号
	 * @return
	 * @modified: ☆LiuPing(2016年1月11日 ): <br>
	 */
	public static String arrayToMask(String[] values) {
		StringBuffer maskBuff = new StringBuffer();
		for(int i = 0; i<values.length; i++ ){
			if(i==0){
				maskBuff.append("?");
			}else{
				maskBuff.append(",?");
			}
		}
		return maskBuff.toString();
	}

	/**
	 * 根据数组长度，得到相应的问号
	 * @return
	 * @modified: ☆LiuPing(2016年1月11日 ): <br>
	 */
	public static List<String> arrayToList(String[] values) {
		List<String> list = new ArrayList<String>();
		for(String value:values){
			list.add(value);
		}
		return list;
	}

	/**
	 * 增加机构条件
	 * @param table_task
	 * @param string
	 * @param comCode
	 * @modified: ☆LiuPing(2016年1月30日 ): <br>
	 */
	public void andComSql(String tableAlias,String prop,String comCode) {

		if(StringUtils.isBlank(comCode)) return;
		String valueStr = null;
		if(comCode.equals("00000000")){
			return;
		}else if(comCode.endsWith("000000")){
			valueStr = comCode.substring(0,2);
		}else{
			valueStr = comCode.substring(0,4);
		}
		queryStr.append(AND+tableAlias+"."+prop+" LIKE ? ");
		paramValues.add(valueStr+"%");
	}
	
	public void andTopComSql(String tableAlias,String prop,String comCode) {

		if(StringUtils.isBlank(comCode)) return;
		String valueStr = null;
		if(comCode.equals("00000000")){
			return;
		}else if(comCode.startsWith("0002")){
			valueStr = comCode.substring(0,4);
		}else{
			valueStr = comCode.substring(0,2);
		}
		queryStr.append(AND+tableAlias+"."+prop+" LIKE ? ");
		paramValues.add(valueStr+"%");
	}

	/**
	 * 添加in条件
	 * @param tableAlias
	 * @param fieldName
	 * @param inData
	 * @modified: ☆LiuPing(2016年2月26日 ): <br>
	 */
	public void addIn(String tableAlias,String fieldName,String... values) {
		if(values==null||values.length==0) return;
		String inMask = arrayToMask(values);
		queryStr.append(AND+tableAlias+"."+fieldName+" IN  ("+inMask+") ");
		for(String value:values){
			paramValues.add(value);
		}
	}
	
	/**
	 * 添加not in条件
	 * @param tableAlias
	 * @param fieldName
	 * @param values
	 */
	public void addNotIn(String tableAlias,String fieldName,String... values) {
		if(values==null||values.length==0) return;
		String inMask = arrayToMask(values);
		queryStr.append(AND+tableAlias+"."+fieldName+" NOT IN  ("+inMask+") ");
		for(String value:values){
			paramValues.add(value);
		}
	}
	


}
