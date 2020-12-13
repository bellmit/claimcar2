package ins.platform.saa.service.spring;

import ins.platform.saa.service.facade.SaaDataPowerService;
import ins.platform.vo.SqlParamVo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EmbeddedId;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;

/**
 * 备用的实现，原始方法
 */
public class SaaDataPowerServiceBakImpl implements SaaDataPowerService {
	
//	@Autowired
//	DatabaseDao databaseDao;
	
	@Resource
	private HibernateTemplate hibernateTemplate;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private List<Object> paramsList = null;
	
	private Map<String,String> entityMap = new HashMap<String, String>();

	/**
	 * 处理find条件和数据（对应条件添加业务范围限制HQL，数据添加业务权限范围限制值）
	 * 
	 * @param queryString
	 *            传入的hql条件
	 * @param params
	 *            传入的参数
	 * @return KeyValueVo
	 */
	public SqlParamVo processFind(String queryString,Object... params) {
		entityMap = this.findEntityName();
		paramsList = new ArrayList<Object>();
		if(params != null && params.length > 0){
			for(int i=0;i<params.length;i++){  
				paramsList.add(params[i]);  
			} 
		}
		queryString = genQueryString(queryString);
		System.out.println(queryString);
		SqlParamVo keyValueVo = new SqlParamVo();
		keyValueVo.setSql(queryString);
		keyValueVo.getParams().addAll(paramsList);
		return keyValueVo;
	}
	
	/**
	 * 生成返回的queryString，处理order by等情况
	 * @param queryString
	 * @param powerHql
	 * @return
	 */
	public String genQueryString(String queryString){
		queryString = queryString.replaceAll("\\s+"," ");
		Map<String, String> tableMap = findTableAndAliasFromHql(queryString);
		String powerHql = this.genPowerHql(tableMap);
		if("".equals(powerHql)){
			return queryString;
		}
		queryString = queryString.replaceAll("\\s+"," ");
		if(queryString.toLowerCase().indexOf(" group by ") != -1){
			int orderIndex = queryString.toLowerCase().indexOf("group by");
			if(queryString.toLowerCase().indexOf(" where ") != -1){
				queryString = queryString.substring(0, orderIndex) + powerHql + queryString.substring(orderIndex, queryString.length());
			}else{
				queryString = queryString.substring(0, orderIndex) + " where 1=1 " + powerHql + queryString.substring(orderIndex, queryString.length());
			}
			return queryString;
		}
		if(queryString.toLowerCase().indexOf(" order by ") != -1){
			int orderIndex = queryString.toLowerCase().indexOf("order by");
			if(queryString.toLowerCase().indexOf(" where ") != -1){
				queryString = queryString.substring(0, orderIndex) + powerHql + queryString.substring(orderIndex, queryString.length());
			}else{
				queryString = queryString.substring(0, orderIndex) + " where 1=1 " + powerHql + queryString.substring(orderIndex, queryString.length());
			}
			return queryString;
		}
		if(queryString.toLowerCase().indexOf(" where ") != -1){
			return queryString + powerHql;
		}else{
			return queryString + " where 1=1 " + powerHql;
		}
	}
	
	/**
	 * 生成权限hql
	 * @param tableMap
	 * @return
	 */
	public String genPowerHql(Map<String, String> tableMap){
		StringBuffer hql = new StringBuffer();
		Iterator<String> it = tableMap.keySet().iterator();
		while (it.hasNext()) {
			String alias = it.next();
			String table = tableMap.get(alias);
			List<String> fieldList = findTableFields(table, alias);
			List<Object[]> factorFieldList = this.findFactorField(fieldList, table);
			for(Object[] object : factorFieldList){
				String fieldCode = (String)object[0];
				String dataOper = (String)object[1];
				String dataValue = (String)object[2];
				String dataType = (String)object[3];
				hql.append(this.genSubPowerHql(fieldCode, dataOper, dataValue, dataType, alias));
			}
		}
		return hql.toString();
	}
	
	public String genSubPowerHql(String fieldCode,String dataOper,String dataValue,String dataType,String alias){
		StringBuffer hql = new StringBuffer();
		if("=".equals(dataOper) || "<".equals(dataOper) || ">".equals(dataOper) 
				|| "<=".equals(dataOper) || ">=".equals(dataOper) || "like".equals(dataOper)){//不同操作符不同处理
			hql.append(" and ").append(alias).append(".").append(fieldCode).append(" ").append(dataOper).append(" ? ");
			paramsList.add(dataValue);
		}else if("in".equals(dataOper.toLowerCase())){
			hql.append(" and ").append(alias).append(".").append(fieldCode).append(" ").append(dataOper);
			StringBuffer inHql = new StringBuffer();
			inHql.append(" (");
			String[] dataValues = dataValue.split(",");
			for(String value : dataValues){
				inHql.append("?,");
				paramsList.add(value);
			}
			hql.append(inHql.toString().substring(0, inHql.toString().length() - 1) + ") ");
		}else if("between".equals(dataOper.toLowerCase())){
			hql.append(" and ").append(alias).append(".").append(fieldCode).append(" ").append(dataOper);
			String[] dataValues = dataValue.split(",");
			hql.append(" ? and ? ");
			paramsList.add(dataValues[0]);
			paramsList.add(dataValues[1]);
		}
		return hql.toString();
	}
	
	/**
	 * 查找对应因子
	 * @param fieldList
	 * @param table
	 * @return
	 */
	public List<Object[]> findFactorField(List<String> fieldList,String table){
		List<Object> fieldParamsList = new ArrayList<Object>();
		fieldParamsList.add("tanjianwei");//先写死用户
		StringBuffer sql = new StringBuffer();
//		sql.append("select factorfield.field_code,userpower.data_oper,userpower.data_value,factor.data_type")
//		   .append(" from saa_user_power userpower,saa_factor factor,saa_factor_field factorfield ")
//		   .append(" where userpower.user_code= ?")
//		   .append(" and userpower.factor_code = factor.factor_code")
//		   .append(" and factor.factor_code = factorfield.factor_code")
//		   .append(" and factorfield.field_code in ").append(findFieldIn(fieldList,fieldParamsList))
//		   .append(" and factorfield.entity_code = ?");
		sql.append("select factorfield.fieldCode,userpower.dataOper,userpower.dataValue,factor.dataType")
		.append(" from SaaUserPower userpower,SaaFactor factor,SaaFactorField factorfield ")
		.append(" where userpower.userCode= ?")
		.append(" and userpower.saaFactor.factorCode = factor.factorCode")
		.append(" and factor.factorCode = factorfield.saaFactor.factorCode")
		.append(" and factorfield.saaFactor.factorCode in ").append(findFieldIn(fieldList,fieldParamsList))
		.append(" and factorfield.entityCode = ?");
		fieldParamsList.add(table);
		List<Object[]> list = (List<Object[]>) hibernateTemplate.find(sql.toString(), fieldParamsList.toArray());
		return list;
	}
	
	public String findFieldIn(List<String> fieldList,List<Object> fieldParamsList){
		StringBuffer sql = new StringBuffer();
		sql.append("(");
		for(String field : fieldList){
			sql.append("?,");
			fieldParamsList.add(field);
		}
		return sql.toString().substring(0, sql.toString().length() - 1) + ") ";
	}
	
	/**
	 * 通过hql获取表和别名
	 * 
	 * @param hql
	 * @return
	 */
	public Map<String, String> findTableAndAliasFromHql(String hql) {
		Map<String, String> tableMap = new HashMap<String, String>();
		String poString = "";
		hql = hql.replaceAll(" [a|A][s|S] ", " ");
		if (hql.toLowerCase().indexOf(" where ") != -1) {//存在where
			poString = hql.substring(hql.toLowerCase().indexOf(" from ") + 5, hql
					.toLowerCase().indexOf(" where "));
		} else {
			if (hql.toLowerCase().indexOf(" group by ") != -1) {//不存在where 存在 group by 
				poString = hql.substring(hql.toLowerCase().indexOf(" from ") + 5, hql
						.toLowerCase().indexOf(" group by "));
			}else{
				if (hql.toLowerCase().indexOf(" order by ") != -1) {//不存在 group by 存在order by
					poString = hql.substring(hql.toLowerCase().indexOf(" from ") + 5, hql
							.toLowerCase().indexOf(" order by "));
				}else{
					poString = hql.substring(hql.toLowerCase().indexOf(" from ") + 5,
							hql.length());
				}
			}
		}
		if (poString.indexOf(",") != -1) {
			String[] poArray = poString.split(",");
			for (String po : poArray) {
				po = po.trim();
				String key = po.substring(po.indexOf(" "), po.length()).trim();
				String value = po.substring(0, po.indexOf(" "));
				tableMap.put(key, value);
			}
		} else {
			poString = poString.trim();
			if(poString.indexOf(" ") != -1){//有别名
				String key = poString.substring(poString.indexOf(" "),
						poString.length()).trim();
				String value = poString.substring(0, poString.indexOf(" "));
				tableMap.put(key, value);
			}else{//没有别名 则key为表名
				tableMap.put(poString, poString);
			}
		}
		return tableMap;
	}
	
	/**
	 * 获取表属性
	 * @param table
	 * @param alias
	 * @return
	 */
	public List<String> findTableFields(String table,String alias){ 
		List<String> fieldList = new ArrayList<String>();
		Class<?> classs = null;
		try {
			classs = Class.forName(entityMap.get(table));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Field[] fields = classs.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String typeName = fields[i].getType().getName();
			String idType = this.findIdType(classs, fields[i]);
			if(EmbeddedId.class.getSimpleName().equals(idType)){//属性为实体类且为id
				try {
					Class<?> idClass = Class.forName(typeName);
					Field[] idFields = idClass.getDeclaredFields();
					for(Field idField : idFields){
						fieldList.add(fields[i].getName() + "." + idField.getName());
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}else{
				if (isEntityName(typeName)) {//属性为实体类 不为id 需要获取中对应属性id
					fieldList.add(fields[i].getName() + "." + this.findTableId(typeName));
				} else {
					fieldList.add(fields[i].getName());
				}
			}
		}
		return fieldList;
	}
	
	/**
	 * id类型
	 * @param classs
	 * @param field
	 * @return
	 */
	public String findIdType(Class<?> classs,Field field){
		Annotation[] annotations = findAnnotations(classs, field);
		for (Annotation anno : annotations) {
			if (anno.toString().contains("@javax.persistence.Id()")) {
				return "Id";
			}
			if (anno.toString().contains("@javax.persistence.EmbeddedId()")) {
				return "EmbeddedId";
			}
		}
		return "";
	}
	
	/**
	 * 判断属性类型是否为实体
	 * @param name
	 * @return
	 */
	public boolean isEntityName(String name){
		Iterator<String> it = entityMap.values().iterator();
		while(it.hasNext()){
			String entityName = it.next();
			if(name.equals(entityName)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取所有实体名称
	 * @return
	 */
	public Map<String,String> findEntityName(){
		Map<String, ClassMetadata> map = sessionFactory.getAllClassMetadata();
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String entityName = it.next();
			entityMap.put(entityName.substring(entityName.lastIndexOf(".") + 1, entityName.length()), entityName);
		}
		return entityMap;
	}
	
	/**
	 * 查找属性注解
	 * @param classs
	 * @param field
	 * @return
	 */
	public Annotation[] findAnnotations(Class<?> classs,Field field){
		Annotation[] annotations = field.getAnnotations();
		if (annotations.length <= 0) {
			String name = field.getName();
			String setMethodName = "get"
					+ StringUtils.left(name, 1).toUpperCase()
					+ StringUtils.substring(name, 1);
			try {
				Method method = classs.getDeclaredMethod(setMethodName);
				annotations = method.getAnnotations();
			} catch (Exception e) {
				// do nothing.
			}
		}
		return annotations;
	}
	
	/**
	 * 获取id
	 * @param po
	 * @return
	 */
	public String findTableId(String po) {
		Class<?> classs = null;
		try {
			if(po.indexOf(".") == -1){
				po = entityMap.get(po);
			}
			classs = Class.forName(po);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Field[] fields = classs.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Annotation[] annotations = findAnnotations(classs, fields[i]);
			for (Annotation anno : annotations) {
				if (anno.toString().contains("@javax.persistence.Id()")) {
					return fields[i].getName();
				}
				if (anno.toString().contains("@javax.persistence.EmbeddedId()")) {
					return fields[i].getName();
				}
			}
		}
		return null;
	}
}