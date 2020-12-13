package ins.platform.saa.service.spring;

import ins.framework.lang.Springs;
import ins.framework.utils.Beans;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.saa.schema.SaaFactorField;
import ins.platform.saa.service.facade.SaaDataPowerService;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaFactorFieldVo;
import ins.platform.saa.vo.SaaFactorPowerVo;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.vo.SqlParamVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service(value = "saaDataPowerService")
public class SaaDataPowerServiceSpringImpl implements SaaDataPowerService {

	private static final Logger logger = LoggerFactory.getLogger(SaaDataPowerService.class);
	// @Autowired
	// DatabaseDao databaseDao;

	@Resource
	private HibernateTemplate hibernateTemplate;

	@Autowired
	private SessionFactory sessionFactory;

	private List<Object> paramsList = null;

	private Map<String,String> entityMap = new HashMap<String,String>();
	private static Map<String,List<SaaFactorPowerVo>> factorPowerMap = new HashMap<String,List<SaaFactorPowerVo>>();
	static{
		// jUnit测试的时候使用
		SaaFactorPowerVo comPower = new SaaFactorPowerVo();
		comPower.setFactorCode("FF_COMCODE");
		comPower.setDataType("String");
		comPower.setDataOper("LIKE");
		comPower.setDataValue("0002%");

		SaaFactorPowerVo comPower2 = new SaaFactorPowerVo();
		comPower2.setFactorCode("FF_COMCODE");
		comPower2.setDataType("String");
		comPower2.setDataOper("LIKE");
		comPower2.setDataValue("0003%");
		List<SaaFactorPowerVo> comFacList = new ArrayList<SaaFactorPowerVo>();
		comFacList.add(comPower);
		comFacList.add(comPower2);
		factorPowerMap.put(comPower.getFactorCode(),comFacList);

		SaaFactorPowerVo userPower = new SaaFactorPowerVo();
		userPower.setFactorCode("FF_USERCODE");
		userPower.setDataType("String");
		userPower.setDataOper("IN");
		userPower.setDataValue("0000000000");
		List<SaaFactorPowerVo> userFacList = new ArrayList<SaaFactorPowerVo>();
		userFacList.add(userPower);
		factorPowerMap.put(userPower.getFactorCode(),userFacList);
	}

	/**
	 * 处理find条件和数据（对应条件添加业务范围限制HQL，数据添加业务权限范围限制值）
	 * @param queryString 传入的hql条件
	 * @param params 传入的参数
	 * @return KeyValueVo
	 */
	public SqlParamVo processFind(String queryString,Object... params) {
		entityMap = this.findEntityName();
		paramsList = new ArrayList<Object>();
		if(params!=null&&params.length>0){
			for(int i = 0; i<params.length; i++ ){
				paramsList.add(params[i]);
			}
		}
		SqlParamVo sqlVo = new SqlParamVo(queryString,paramsList);
		sqlVo = genUserPowerSql(sqlVo);
		if(sqlVo==null) return null;
		System.out.println("===PowerData.Sql===="+sqlVo.getSql().toString());
		System.out.println("===PowerData.Params="+ArrayUtils.toString(sqlVo.getParams().toArray()));
		return sqlVo;
	}

	/**
	 * 生成返回的queryString，处理order by等情况
	 * @param queryString
	 * @param powerHql
	 * @return
	 */
	private SqlParamVo genUserPowerSql(SqlParamVo sqlVo) {
		String queryString = sqlVo.getSql().toString();
		queryString = queryString.replaceAll("\\s+"," ");
		Map<String,String> tableMap = findTableAndAliasFromHql(queryString);
		// 得到权限语句
		SqlParamVo permitPowerSqlVo = this.genTablePermitPowerHql(tableMap);
		if(permitPowerSqlVo.isEmpty()){
			return null;
		}
		// 把权限控制语句放入原sql中
		queryString = queryString.replaceAll("\\s+"," ");
		String powerHql = " AND "+permitPowerSqlVo.getSql();
		if(queryString.toLowerCase().indexOf(" group by ")!= -1){
			int orderIndex = queryString.toLowerCase().indexOf("group by");
			if(queryString.toLowerCase().indexOf(" where ")!= -1){
				queryString = queryString.substring(0,orderIndex)+powerHql+queryString.substring(orderIndex,queryString.length());
			}else{
				queryString = queryString.substring(0,orderIndex)+" where 1=1 "+powerHql+queryString.substring(orderIndex,queryString.length());
			}
			sqlVo.setSql(queryString);
			sqlVo.getParams().addAll(permitPowerSqlVo.getParams());
			return sqlVo;
		}
		if(queryString.toLowerCase().indexOf(" order by ")!= -1){
			int orderIndex = queryString.toLowerCase().indexOf("order by");
			if(queryString.toLowerCase().indexOf(" where ")!= -1){
				queryString = queryString.substring(0,orderIndex)+powerHql+queryString.substring(orderIndex,queryString.length());
			}else{
				queryString = queryString.substring(0,orderIndex)+" where 1=1 "+powerHql+queryString.substring(orderIndex,queryString.length());
			}
			sqlVo.setSql(queryString);
			sqlVo.getParams().addAll(permitPowerSqlVo.getParams());
			return sqlVo;
		}
		if(queryString.toLowerCase().indexOf(" where ")!= -1){
			queryString = queryString+powerHql;
			sqlVo.setSql(queryString);
			sqlVo.getParams().addAll(permitPowerSqlVo.getParams());
			return sqlVo;
		}else{
			queryString = queryString+" where 1=1 "+powerHql;
			sqlVo.setSql(queryString);
			sqlVo.getParams().addAll(permitPowerSqlVo.getParams());
			return sqlVo;
		}
	}

	/**
	 * 生成表对应的权限hql（允许权限部分）
	 * @param tableMap
	 * @return
	 */
	public SqlParamVo genTablePermitPowerHql(Map<String,String> tableMap) {

		SqlParamVo tablePowerSqlVo = new SqlParamVo();
		Iterator<String> it = tableMap.keySet().iterator();
		SaaUserPowerService saaUserPowerService = Springs.getBean(SaaUserPowerService.class);
		SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(ServiceUserUtils.getUserCode());
		if(userPowerVo==null){
			return tablePowerSqlVo;
			// throw new IllegalArgumentException("未找到用户权限");
		}
		Map<String,List<SaaFactorPowerVo>> factorPowerMap = userPowerVo.getPermitFactorMap();
		while(it.hasNext()){// 表的权限是and
			String alias = it.next();
			String table = tableMap.get(alias);
			List<SaaFactorFieldVo> factorFieldVos = findTableFactorField(table);
			if(factorFieldVos.size()==0) continue;
			SqlParamVo factorPowerSqlVo = new SqlParamVo();// 因子之间的权限是or
			for(SaaFactorFieldVo factorFieldVo:factorFieldVos){

				List<SaaFactorPowerVo> factorPowerVoList = factorPowerMap.get(factorFieldVo.getFactorCode());
				if(factorPowerVoList==null||factorPowerVoList.size()==0) continue;
				for(SaaFactorPowerVo factorPowerVo:factorPowerVoList){
					String fieldCode = factorFieldVo.getFieldCode();
					SqlParamVo subPowerSqlVo = this.genSubPowerHql(factorPowerVo,fieldCode,alias);// 得到alias. fieldCode = ?
					factorPowerSqlVo.addOr(subPowerSqlVo);
				}
			}
			tablePowerSqlVo.addAnd(factorPowerSqlVo.bracket());
			break;// 重要提示：当前只支持控制单个表权限，编写查询语句时需要把优先拼装权限的放到前面

			// table,对应的SaaFactorField( FactorCode, filedCode)
			// FactorCode 对应的SQL（目前支持 COMCODE,USERCODE,RISKCODE）
			// String subPowSql=this.genSubPowerHql(alias,table,factorFieldVo);
			// String subPowSql = " AND "+alias+".comCode LIKE ? ";
			// tablePowerSqlVo.addSql(subPowSql,"00%");

		}
		return tablePowerSqlVo;
	}

	private SqlParamVo genSubPowerHql(SaaFactorPowerVo factorPowerVo,String fieldCode,String alias) {

		SqlParamVo subPowerSqlVo = new SqlParamVo();
		String dataOper = factorPowerVo.getDataOper().toLowerCase();
		String dataValue = factorPowerVo.getDataValue();

		if("=".equals(dataOper)||"<".equals(dataOper)||">".equals(dataOper)||"<=".equals(dataOper)||">=".equals(dataOper)||"like".equals(dataOper)){// 不同操作符不同处理
			subPowerSqlVo.getSql().append(alias).append(".").append(fieldCode).append(" ").append(dataOper).append(" ? ");
			subPowerSqlVo.addParams(dataValue);
		}else if("in".equals(dataOper.toLowerCase())){
			subPowerSqlVo.getSql().append(alias).append(".").append(fieldCode).append(" ").append(dataOper);
			StringBuffer inHql = new StringBuffer();
			inHql.append(" (");
			String[] dataValues = dataValue.split(",");
			for(String value:dataValues){
				inHql.append("?,");
				subPowerSqlVo.addParams(value);
			}
			subPowerSqlVo.getSql().append(inHql.toString().substring(0,inHql.toString().length()-1)+") ");
		}else if("between".equals(dataOper.toLowerCase())){
			subPowerSqlVo.getSql().append(alias).append(".").append(fieldCode).append(" ").append(dataOper);
			String[] dataValues = dataValue.split(",");
			subPowerSqlVo.getSql().append(" ? and ? ");
			subPowerSqlVo.addParams(dataValues[0],dataValues[1]);
		}
		return subPowerSqlVo;

	}

	/**
	 * 查询某个表需要控制权限的因子
	 * @param table
	 * @return
	 * @modified: ☆LiuPing(2016年6月28日 ): <br>
	 */
	private List<SaaFactorFieldVo> findTableFactorField(String table) {
		// TODO [这个方法在后期正常运行后放入缓存]
		List<SaaFactorFieldVo> factorFieldVos = new ArrayList<SaaFactorFieldVo>();
		String sql = "select f from SaaFactorField f where f.tableEntity=?";
		List<SaaFactorField> ffObjList = (List<SaaFactorField>)hibernateTemplate.find(sql,table);
		if(ffObjList==null||ffObjList.size()==0) return factorFieldVos;
		factorFieldVos = Beans.copyDepth().from(ffObjList).toList(SaaFactorFieldVo.class);

		return factorFieldVos;
	}

	/**
	 * 通过hql获取表和别名
	 * @param hql
	 * @return
	 */
	private Map<String,String> findTableAndAliasFromHql(String hql) {
		Map<String,String> tableMap = new LinkedHashMap<String,String>();
		String poString = "";
		hql = hql.replaceAll(" [a|A][s|S] "," ");
		if(hql.toLowerCase().indexOf(" where ")!= -1){// 不存在where
			poString = hql.substring(hql.toLowerCase().indexOf(" from ")+5,hql.toLowerCase().indexOf(" where "));
		}else{
			if(hql.toLowerCase().indexOf(" group by ")!= -1){// 存在where 存在 group by
				poString = hql.substring(hql.toLowerCase().indexOf(" from ")+5,hql.toLowerCase().indexOf(" group by "));
			}else{
				if(hql.toLowerCase().indexOf(" order by ")!= -1){// 存在 group by 存在order by
					poString = hql.substring(hql.toLowerCase().indexOf(" from ")+5,hql.toLowerCase().indexOf(" order by "));
				}else{
					poString = hql.substring(hql.toLowerCase().indexOf(" from ")+5,hql.length());
				}
			}
		}
		if(poString.indexOf(",")!= -1){
			String[] poArray = poString.split(",");
			for(String po:poArray){
				po = po.trim();
				String key = po.substring(po.indexOf(" "),po.length()).trim();
				String value = po.substring(0,po.indexOf(" "));
				tableMap.put(key,value);
			}
		}else{
			poString = poString.trim();
			if(poString.indexOf(" ")!= -1){// 有别名
				String key = poString.substring(poString.indexOf(" "),poString.length()).trim();
				String value = poString.substring(0,poString.indexOf(" "));
				tableMap.put(key,value);
			}else{// 没有别名 则key为表名
				tableMap.put(poString,poString);
			}
		}
		return tableMap;
	}

	/**
	 * 获取所有实体名称
	 * @return
	 */
	private Map<String,String> findEntityName() {
		Map<String,ClassMetadata> map = sessionFactory.getAllClassMetadata();
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String entityName = it.next();
			entityMap.put(entityName.substring(entityName.lastIndexOf(".")+1,entityName.length()),entityName);
		}
		return entityMap;
	}

}
