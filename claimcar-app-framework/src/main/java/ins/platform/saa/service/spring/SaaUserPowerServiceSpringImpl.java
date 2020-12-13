/******************************************************************************
* CREATETIME : 2016年6月27日 下午5:34:32
******************************************************************************/
package ins.platform.saa.service.spring;

import ins.framework.cache.CacheManager;
import ins.framework.cache.CacheService;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.saa.schema.ResourceTask;
import ins.platform.saa.schema.SaaTask;
import ins.platform.saa.schema.SaaUserGrade;
import ins.platform.saa.schema.SaaUserPermitCompany;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaFactorPowerVo;
import ins.platform.saa.vo.SaaRoleTaskVo;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.utils.SqlJoinUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年6月27日
 */
@Service(value = "saaUserPowerService")
public class SaaUserPowerServiceSpringImpl implements SaaUserPowerService {

	private static CacheService userPowerCache = CacheManager.getInstance("UserPower_Cache");
	@Autowired
	private DatabaseDao databaseDao;
	
	@Autowired
	BaseDaoService baseDaoService;


	@Resource
	private HibernateTemplate hibernateTemplate;
	/* 
	 * @see ins.platform.saa.service.facade.SaaUserPowerService#findUserPowerVo()
	 * @return
	 */
	@Override
	public SaaUserPowerVo findUserPower(String userCode) {

		String cacheKey = userCode;
		SaaUserPowerVo userPowerVo = (SaaUserPowerVo)userPowerCache.getCache(cacheKey);
		if(userPowerVo!=null) return userPowerVo;

		List<String> roleList = findUserRoles(userCode);
		List<String> taskList = findUserTasks(userCode);

		userPowerVo = new SaaUserPowerVo();
		userPowerVo.setUserCode(userCode);
		userPowerVo.setRoleList(roleList);
		userPowerVo.setTaskList(taskList);
		Map<String,List<SaaFactorPowerVo>> permitFactorMap = findPermitFactorMap(userCode);
		userPowerVo.setPermitFactorMap(permitFactorMap);
		Map<String,String> maxVerifyLVMap=findMaxVerifyLVMap(userCode);
		userPowerVo.setMaxVerifyLVMap(maxVerifyLVMap);
		userPowerVo.setAllVerifyLVMap(findAllVerifyLVMap(userCode));
		userPowerCache.putCache(cacheKey,userPowerVo);
		return userPowerVo;
	}


	private List<String> findUserRoles(String userCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("userCode",userCode);
		List<SaaUserGrade> poList = databaseDao.findAll(SaaUserGrade.class,queryRule);

		List<String> roleCodeList = new ArrayList<String>(poList.size());
		for(SaaUserGrade po:poList){
			roleCodeList.add(po.getSaaGrade().getId().toString());
		}
		return roleCodeList;
	}

	@SuppressWarnings("unchecked")
	private List<String> findUserTasks(String userCode) {
		String queryString = "select  t.taskCode from SaaGradeTask g,SaaUserGrade u,SaaTask t where u.userCode=? and u.saaGrade.id=g.saaGrade.id  and g.saaTask.id=t.id Group By t.taskCode";
		List<String> userTaskList = (List<String>)hibernateTemplate.find(queryString,userCode);
		return userTaskList;
	}

	/**
	 * 查询用户每个因子对应的权限，当前支持的因子有 FF_COMCODE，FF_USERCODE，FF_RISKCODE
	 * @param userCode
	 * @return
	 * @modified: ☆LiuPing(2016年6月28日 ): <br>
	 */
	private Map<String,List<SaaFactorPowerVo>> findPermitFactorMap(String userCode) {
		Map<String,List<SaaFactorPowerVo>> factorPowerMap = new HashMap<String,List<SaaFactorPowerVo>>();

		SaaFactorPowerVo userPower = new SaaFactorPowerVo();
		userPower.setFactorCode("FF_USERCODE");
		userPower.setDataType("String");
		userPower.setDataOper("IN");
		userPower.setDataValue(userCode+",ANYONE");//报案案件查询特殊处理,taskIn表assignUser和handlerUser赋值为ANYONE,允许全部报案权限工号查询
		List<SaaFactorPowerVo> userFacList = new ArrayList<SaaFactorPowerVo>();
		userFacList.add(userPower);
		factorPowerMap.put(userPower.getFactorCode(),userFacList);

		// 机构权限
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("userCode",userCode);
		List<SaaUserPermitCompany> permitCompanyList = databaseDao.findAll(SaaUserPermitCompany.class,queryRule);
		Map<String,String> comOperMap = new HashMap<String,String>();

		for(SaaUserPermitCompany permitCompany:permitCompanyList){
			String comCode = permitCompany.getComCode();
			String comCodeKey = null;
			String comOper = null;
			if(comCode.equals("00000000")){// 全公司， 不用处理
				continue;
			}else if(comCode.endsWith("000000")){// 分公司
				comCodeKey = comCode.substring(0,2)+"%";
				comOper = "LIKE";
			}else if(comCode.endsWith("0000")){// 分公司
				comCodeKey = comCode.substring(0,4)+"%";
				comOper = "LIKE";
			}else{
				comCodeKey = comCode;// 查询这个机构的所有下级是否更加合理
				comOper = "=";
			}
			comOperMap.put(comCodeKey,comOper);
		}

		List<SaaFactorPowerVo> comFacList = new ArrayList<SaaFactorPowerVo>();
		for(String comCodeKey:comOperMap.keySet()){
			SaaFactorPowerVo comPower = new SaaFactorPowerVo();
			comPower.setFactorCode("FF_COMCODE");
			comPower.setDataType("String");
			comPower.setDataOper(comOperMap.get(comCodeKey));
			comPower.setDataValue(comCodeKey);
			comFacList.add(comPower);
		}
		factorPowerMap.put("FF_COMCODE",comFacList);

		return factorPowerMap;
	}

	/* 
	 * @see ins.platform.saa.service.facade.SaaUserPowerService#findAllSaaRoleTask()
	 * @return
	 */
	@Override
	public List<SaaRoleTaskVo> findAllSaaRoleTask() {
		List<SaaRoleTaskVo> saaRoleTaskVoList = new ArrayList<SaaRoleTaskVo>();
		// TODO 返回URL对应的角色
		return saaRoleTaskVoList;
	}

	private Map<String,String> findMaxVerifyLVMap(String userCode) {
		/*
		 Select UPPERNODE, Max(NODECODE)  From  prpdnode  n Where  NODECODE Like '%_LV%' 
And Exists (Select  1 From saa_usergrade  g Where g.gradeid=n.gradeid And usercode='0000001508' )
Group By UPPERNODE 
		 */
		String sql = "select n.upperNode,MAX(nodeCode)  nodeCode from PrpDNode n where nodeCode  LIKE ? ";
		sql += "AND Exists (Select 1 from  Saa_UserGrade g where g.gradeId=n.gradeId and userCode=? ) ";
		sql += " Group By n.upperNode  ";
		List<Object[]> result = databaseDao.findTopBySql(sql,10,"%_LV%",userCode);
		Map<String,String> maxVerifyLVMap = new HashMap<String,String>();
		for(Object[] objs:result){
			maxVerifyLVMap.put(objs[0].toString(),objs[1].toString());
		}
		// System.out.println(maxVerifyLVMap);
		return maxVerifyLVMap;
		
	}

	private Map<String,List<String>> findAllVerifyLVMap(String userCode){
		Map<String,List<String>> allVerifyLVMap = new HashMap<String,List<String>>();
		
		String sql = "select n.upperNode,n.nodeCode from PrpDNode n where nodeCode  LIKE ? ";
//		sql += " AND n.upperNode = ? ";
		sql += "AND Exists (Select 1 from  Saa_UserGrade g where g.gradeId=n.gradeId and userCode=? ) ";
		List<Object[]> result = databaseDao.findTopBySql(sql,1000,"%_LV%",userCode);
		
		
		for(Object[] objs : result){
//			allVerifyLVMap.put(objs[0].toString(),objs[1].toString());
			String upperNode = objs[0].toString();
			String nodeCode = objs[1].toString();
			if(allVerifyLVMap.containsKey(upperNode)){
				List<String> temp = allVerifyLVMap.get(upperNode);
				temp.add(nodeCode);
				allVerifyLVMap.put(upperNode,temp);
			}else{
				List<String> allVerifyLVList = new ArrayList<String>();
				allVerifyLVList.add(nodeCode);
				allVerifyLVMap.put(upperNode,allVerifyLVList);
			}
		}
		return allVerifyLVMap;
	}


//	@Override
//	public List<String> findUserLoginCom(String userCode) {
//		// TODO Auto-generated method stub
//		String sql = "Select  Distinct  comcode From  utiusergrade Where usercode=?  Order By 1 ";
//		List<String> loginComs = databaseDao.findAllBySql(String.class,sql,userCode);
//		return loginComs;
//	}
	
	@Override
	public List<String> findUserLoginCom(String userCode) {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("Select  Distinct  comcode From  utiusergrade Where usercode=?  Order By 1 ");
		sqlUtil.addParamValue(userCode);
		
		// 查询参数
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		// 执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		
		List<String> loginComs = new ArrayList<String>();
		// 对象转换
		for(int i = 0; i<objects.size(); i++ ){

			Object obj = objects.get(i);
			loginComs.add(obj.toString());

		}
		
		// TODO Auto-generated method stub
//		String sql = "Select  Distinct  comcode From  utiusergrade Where usercode=?  Order By 1 ";
//		List<String> loginComs = databaseDao.findAllBySql(String.class,sql,userCode);
		return loginComs;
	}
	/**
	 * 清除缓存
	 */
	@Override
	public void clearCache(){
		userPowerCache.clearAllCacheManager();
	}


	@Override
	public List<SaaTask> findAllSaaTask() {
		List<SaaTask> saaTaskList = databaseDao.findAll(SaaTask.class);
		return saaTaskList;
	}


	@Override
	public List<ResourceTask> findAllResUrl() {
		List<ResourceTask> resTaskList = databaseDao.findAll(ResourceTask.class);
		return resTaskList;
	}

}
