/******************************************************************************
* CREATETIME : 2016年7月5日 上午10:40:13
******************************************************************************/
package ins.platform.saa.service.spring;

import ins.framework.cache.CacheManager;
import ins.framework.cache.CacheService;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.platform.saa.schema.SaaGrade;
import ins.platform.saa.schema.SaaUserGrade;
import ins.platform.saa.service.facade.SaaUserGradeService;
import ins.platform.saa.vo.SaaGradeVo;
import ins.platform.saa.vo.SaaUserGradeVo;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysAreaDictVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年7月5日
 */
@Service(value = "saaUserGradeService")
public class SaaUserGradeServiceSpringImpl implements SaaUserGradeService {
	
	private static CacheService userGradeCache = CacheManager.getInstance("UserGrade_Cache");
	@Autowired
	private DatabaseDao databaseDao;
	private static Logger logger = LoggerFactory.getLogger(SaaUserGradeServiceSpringImpl.class);
	
	@Override
	public List<String> findGradeUsers(String gradeId){
		List<String> userCodes = new ArrayList<String>();
		SaaGrade saaGrade = databaseDao.findByPK(SaaGrade.class, gradeId);
		for(SaaUserGrade saaUserGrade:saaGrade.getSaaUserGrades()){
			userCodes.add(saaUserGrade.getUserCode());
		}
		return userCodes;
	}
	
	
	@Override
	public List<SaaGradeVo> findUserGrade(String userCode){
		String cacheKey = userCode;
		
		List<SaaGradeVo> saaGradeVos = (List<SaaGradeVo>)userGradeCache.getCache(cacheKey);
		if(saaGradeVos != null){
			return saaGradeVos;
		}
		Long start = System.currentTimeMillis();
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" FROM SaaGrade grade,SaaUserGrade userGrade ");
		sqlUtil.append(" where grade.id = userGrade.saaGrade.id  ");
		sqlUtil.append(" and userGrade.validStatus = ? ");
		sqlUtil.append(" and userGrade.userCode = ? ");
		String sql = sqlUtil.getSql();
		List<Object[]> list = databaseDao.findAllByHql(sql,"1",userCode);
		Long end = System.currentTimeMillis();
//		System.out.println(end -start);
		logger.info((end -start)+"");
		List<SaaGradeVo> saaGradeVoList = new ArrayList<SaaGradeVo>();
		List<SaaGrade> saaGradeList = new ArrayList<SaaGrade>();
		for(int i=0;i<list.size();i++){
			SaaGrade saaGrade = (SaaGrade)list.get(i)[0];
//			saaGradeVo = Beans.copyDepth().from(obj).to(SaaGradeVo.class);
			saaGradeList.add(saaGrade);
		}
		saaGradeVoList = Beans.copyDepth().from(saaGradeList).toList(SaaGradeVo.class);
		userGradeCache.putCache(userCode, saaGradeVoList);
		Long end2 = System.currentTimeMillis();
		logger.info((end2 - end)+"");
		return saaGradeVoList;
	}
	
	@Override
	public List<SaaUserGradeVo> findSaaUserGradeListByGradeIdAndComCode(String gradeId,String comCode) {
		String comCodePre = comCode.substring(0,4)+"%";
		List<SaaUserGradeVo> saaUserGradeVoList = null;
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		Date date = new Date();
		sqlUtil.append(" FROM SaaUserGrade userGrade ");
		sqlUtil.append(" Where gradeid=? ");
		sqlUtil.append(" And exists (select 1 from SaaUserPermitCompany where userCode = userGrade.userCode ");
		sqlUtil.append(" And comCode like ? ) ");
		//排除无效的工号
		sqlUtil.append(" And exists (select 1 from SysUser where userCode = userGrade.userCode ");
		sqlUtil.append(" And validStatus = ? )");
		
		sqlUtil.append(" And not exists (select 1 from PrpLUserHoliday where userCode = userGrade.userCode ");
		sqlUtil.append(" And endDate >= ? ");
		sqlUtil.append(" And startDate <= ? ");
		sqlUtil.append(" And checkStatus = ? ) ");
//		List<PrpLUserHolidayVo> userHoliList = new ArrayList<PrpLUserHolidayVo>();
//		Date date = new Date();
//		QueryRule queryRule = QueryRule.getInstance();
//		queryRule.addGreaterThan("endDate",date);
//		queryRule.addLessThan("startDate",date);
//		queryRule.addEqual("checkStatus","1");
		String sql = sqlUtil.getSql();
		
		List<SaaUserGrade> SaaUserGradePoList = databaseDao.findAllByHql(SaaUserGrade.class,sql,gradeId,comCodePre,"1",date,date,"1");
		
		if(SaaUserGradePoList!=null&&SaaUserGradePoList.size()>0){
			saaUserGradeVoList = Beans.copyDepth().from(SaaUserGradePoList).toList(SaaUserGradeVo.class);
		}else if(!"0002%".equals(comCodePre)){//深分不往上查询
			// 查询不到人员时查询机构上级
			String comCode_Level2 = comCode.substring(0,2)+"000000";
			SqlJoinUtils sqlUtil_Level2 = new SqlJoinUtils();
			sqlUtil_Level2.append(" FROM SaaUserGrade userGrade ");
			sqlUtil_Level2.append(" Where gradeid=? ");
			sqlUtil_Level2.append(" And exists (select 1 from SaaUserPermitCompany where userCode = userGrade.userCode ");
			sqlUtil_Level2.append(" And comCode = ? )");
			//排除无效的工号
			sqlUtil_Level2.append(" And exists (select 1 from SysUser where userCode = userGrade.userCode ");
			sqlUtil_Level2.append(" And validStatus = ? )");
			
			sqlUtil_Level2.append(" And not exists (select 1 from PrpLUserHoliday where userCode = userGrade.userCode ");
			sqlUtil_Level2.append(" And endDate >= ? ");
			sqlUtil_Level2.append(" And startDate <= ? ");
			sqlUtil_Level2.append(" And checkStatus = ? ) ");
			
			String sql_Level2 = sqlUtil_Level2.getSql();
			
			SaaUserGradePoList = databaseDao.findAllByHql(SaaUserGrade.class,sql_Level2,gradeId,comCode_Level2,"1",date,date,"1");
			
			if(SaaUserGradePoList!=null&&SaaUserGradePoList.size()>0){
				saaUserGradeVoList = Beans.copyDepth().from(SaaUserGradePoList).toList(SaaUserGradeVo.class);
			}
		}
		
		return saaUserGradeVoList;
	}

}
