package ins.sino.claimcar.base.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.saa.service.facade.SaaUserGradeService;
import ins.platform.saa.vo.SaaGradeVo;
import ins.platform.saa.vo.SaaUserGradeVo;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.base.po.PrpLUserHoliday;
import ins.sino.claimcar.base.po.PrpLUserHolidayGrade;
import ins.sino.claimcar.manager.vo.PrpLUserHolidayVo;
import ins.sino.claimcar.other.service.HolidayManageService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"})
@Path(value = "holidayManageService")
public class HolidayManageServiceImpl implements HolidayManageService {
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	SaaUserGradeService saaUserGradeService;
	
	/**
	 * 休假申请查询
	 */
	@Override
	public List<PrpLUserHolidayVo> findAllHolidayManage(
			PrpLUserHolidayVo prpLUserHolidayVo,int start,int length) {
		// 定义参数list，执行查询时需要转换成object数组
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqualIfExist("userCode",prpLUserHolidayVo.getUserCode());
		queryRule.addEqualIfExist("userName",prpLUserHolidayVo.getUserName());
		queryRule.addEqualIfExist("comCode",prpLUserHolidayVo.getComCode());
		queryRule.addDescOrder("createTime");
		List<PrpLUserHoliday> prpLUserHoliday = databaseDao.findAll(PrpLUserHoliday.class,queryRule);
		List<PrpLUserHolidayVo> prpLUserHolidayVoList = new ArrayList<PrpLUserHolidayVo>();
		if(prpLUserHoliday!=null||prpLUserHoliday.size()>0){
			prpLUserHolidayVoList=Beans.copyDepth().from(prpLUserHoliday).toList(PrpLUserHolidayVo.class);
		}
		//判断是否可以撤销休假
		Date date = new Date();
		for(PrpLUserHolidayVo userHolidayVo:prpLUserHolidayVoList){
			if(("1".equals(userHolidayVo.getCheckStatus())||"2".equals(userHolidayVo.getCheckStatus())) && 
					date.getTime() <= userHolidayVo.getEndDate().getTime()){
				userHolidayVo.setStatus("1");
			}else{
				userHolidayVo.setStatus("0");
			}
		}
		return prpLUserHolidayVoList;
		
	}
	
	/**
	 * 休假审核查询
	 */
	@Override
	public List<PrpLUserHolidayVo> findAllHolidayManageByHql(
			PrpLUserHolidayVo prpLUserHolidayVo,String handleStatus,Date timeEnd,
			Date timeStart,int start,int length,String userCode) {
		// 定义参数list，执行查询时需要转换成object数组
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addLikeIfExist("userCode", prpLUserHolidayVo.getUserCode());
		queryRule.addLikeIfExist("userName", prpLUserHolidayVo.getUserName());
		queryRule.addEqualIfExist("comCode",prpLUserHolidayVo.getComCode());
		queryRule.addNotEqual("userCode", userCode);
		queryRule.addBetween("createTime", timeStart,DateUtils.toDateEnd(timeEnd));
		if(handleStatus.equals("0")){
			queryRule.addEqualIfExist("checkStatus", "2");
		}else if(handleStatus.equals("3")){
			queryRule.addInIfExist("checkStatus", "1","0");
			queryRule.addEqual("checkerCode", userCode);
		}else{
			queryRule.addEqualIfExist("checkStatus", "3");
		}
		List<PrpLUserHoliday> prpLUserHoliday = databaseDao.findAll(PrpLUserHoliday.class,queryRule);
		List<PrpLUserHolidayVo> prpLUserHolidayVos = new ArrayList<PrpLUserHolidayVo>();
		if(prpLUserHoliday!=null||prpLUserHoliday.size()>0){
			for(int i=1;i<=prpLUserHoliday.size();i++){
				prpLUserHolidayVos=Beans.copyDepth().from(prpLUserHoliday).toList(PrpLUserHolidayVo.class);
			}
		}
		return prpLUserHolidayVos;
		
	}
	
	/**
	 * 根据主键查询PrpLUserHolidayVo
	 * @param id
	 * @return PrpLUserHolidayVo
	 */
	@Override
	public PrpLUserHolidayVo findPrpLUserHolidayVoByPK(Long id) {
		PrpLUserHolidayVo prpLUserHolidayVo = new PrpLUserHolidayVo();
		if (id != null) {
			PrpLUserHoliday prpLUserHoliday = databaseDao.findByPK(
					PrpLUserHoliday.class, id);

			prpLUserHolidayVo = Beans.copyDepth().from(prpLUserHoliday)
					.to(PrpLUserHolidayVo.class);
		}

		return prpLUserHolidayVo;

	}
	
	/**
	 * 保存或更新休假处理
	 * @param PrpLUserHolidayVo
	 * @return PrpLUserHoliday
	 */
	@Override
	public void saveOrUpdateHolidayManage(PrpLUserHolidayVo prpLUserHolidayVo,SysUserVo userVo) {
		Date date = new Date();
		String userName = userVo.getUserName();// 获取用户
		String userCode=userVo.getUserCode();
		PrpLUserHoliday prpLUserHoliday = Beans.copyDepth().from(prpLUserHolidayVo)
				.to(PrpLUserHoliday.class);
		for(PrpLUserHolidayGrade rd:prpLUserHoliday.getPrpLUserHolidayGrades()){
	    	rd.setPrpLUserHoliday(prpLUserHoliday);
	    	if(rd.getId() != null && "".equals(rd.getId())){
	    		rd.setUpdateTime(date);
		    	rd.setUpdateUser(userVo.getUserCode());
	    	}else{
	    		rd.setCreateTime(date);
		    	rd.setCreateUser(userVo.getUserCode());
	    	}
	    }
		if(prpLUserHoliday.getId()!=null){
			prpLUserHoliday.setUpdateTime(date);
			prpLUserHoliday.setUpdateUser(userCode);
			prpLUserHoliday.setCheckerCode(userCode);
			prpLUserHoliday.setCheckerName(userName);
			prpLUserHoliday.setCheckTime(date);
			databaseDao.update(PrpLUserHoliday.class, prpLUserHoliday);
		}else{
			prpLUserHoliday.setCreateTime(date);
			prpLUserHoliday.setCreateUser(userCode);
			prpLUserHoliday.setCheckStatus("2");
			databaseDao.save(PrpLUserHoliday.class, prpLUserHoliday);
		}
	}
	
	/**
	 * 查询正在休假期间的员工
	 * @return
	 */
	@Override
	public List<PrpLUserHolidayVo> findAssignUserHolidayListByTime() {
		List<PrpLUserHolidayVo> userHoliList = new ArrayList<PrpLUserHolidayVo>();
		Date date = new Date();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addGreaterThan("endDate",date);
		queryRule.addLessThan("startDate",date);
		List<PrpLUserHoliday> userPoList = new ArrayList<PrpLUserHoliday>();
		userPoList = databaseDao.findAll(PrpLUserHoliday.class, queryRule);
		System.out.println(queryRule);
		userHoliList = Beans.copyDepth().from(userPoList).toList(PrpLUserHolidayVo.class);
		return userHoliList;
	}
	
	@Override
	public List<PrpLUserHolidayVo> findAllHolidayManageByUserCode(String userCode){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqualIfExist("userCode",userCode);
		List<PrpLUserHoliday> prpLUserHoliday = databaseDao.findAll(PrpLUserHoliday.class,queryRule);
		List<PrpLUserHolidayVo> prpLUserHolidayVos = new ArrayList<PrpLUserHolidayVo>();
		if(prpLUserHoliday!=null||prpLUserHoliday.size()>0){
				prpLUserHolidayVos=Beans.copyDepth().from(prpLUserHoliday).toList(PrpLUserHolidayVo.class);
		}
		return prpLUserHolidayVos;
	}
	
	@Override
	public Boolean existHoliday(String userCode){
		Boolean bl = false;
		Date date = new Date();
		List<PrpLUserHolidayVo> userHolidayVos = this.findHolidayByUserCode(userCode);
		if(userHolidayVos.size() > 0){
			for(PrpLUserHolidayVo userHolidayVo:userHolidayVos){
				int difference = DateUtils.compareDays(userHolidayVo.getLeaveDate(), date);
				if(difference > 0){
					bl = true;
					break;
				}
			}
		}
		return bl;
	}
	/**
	 * 根据userCode查询审核通过的申请
	 * @param userCode
	 * @return
	 */
	@Override
	public List<PrpLUserHolidayVo> findHolidayByUserCode(String userCode){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqualIfExist("userCode",userCode);
		queryRule.addEqualIfExist("checkStatus","1");
		List<PrpLUserHoliday> prpLUserHoliday = databaseDao.findAll(PrpLUserHoliday.class,queryRule);
		List<PrpLUserHolidayVo> prpLUserHolidayVos = new ArrayList<PrpLUserHolidayVo>();
		if(prpLUserHoliday!=null||prpLUserHoliday.size()>0){
				prpLUserHolidayVos=Beans.copyDepth().from(prpLUserHoliday).toList(PrpLUserHolidayVo.class);
		}
		return prpLUserHolidayVos;
	}
	
	@Override
	public Map<String,Map> organizeMap(SysUserVo userVo){
		//查询出所有的岗位
		List<SaaGradeVo> saaGradeVos = saaUserGradeService.findUserGrade(userVo.getUserCode());
		Map<String,Map> userMap = new HashMap<String,Map>();
		if(saaGradeVos!=null&&saaGradeVos.size()>0){
			for(SaaGradeVo saaGradeVo:saaGradeVos){
				Map<String,String> userCodes = new HashMap<String,String>();
				Boolean bl = true;
				//根据gradeid判断岗位是否到人
				if(CodeConstants.ISTOGRADE_MAP.get(String.valueOf(saaGradeVo.getId())) != null){
					bl = CodeConstants.ISTOGRADE_MAP.get(String.valueOf(saaGradeVo.getId()));
				}
				if(bl){//如果该岗位是到人的
					//根据岗位ID和comcode查询出员工
					List<SaaUserGradeVo> userGradeVoList = 
							saaUserGradeService.findSaaUserGradeListByGradeIdAndComCode(String.valueOf(saaGradeVo.getId()), userVo.getComCode());
					if(userGradeVoList != null && userGradeVoList.size() > 0){
						for(SaaUserGradeVo saaUserGradeVo:userGradeVoList){
							userCodes.put(saaUserGradeVo.getUserCode(), saaUserGradeVo.getUserCode());
						}
						userMap.put(saaGradeVo.getGradeCName(), userCodes);
					}
				}
			}
		}
		return userMap;
	}
	
	@Override
	public void cancelHoliday(String id,SysUserVo userVo){
		Date date = new Date();
		PrpLUserHoliday prpLUserHoliday = databaseDao.findByPK(PrpLUserHoliday.class, Long.valueOf(id));
		prpLUserHoliday.setCheckStatus("3");
		prpLUserHoliday.setCancelTime(date);
		prpLUserHoliday.setUpdateTime(date);
		prpLUserHoliday.setUpdateUser(userVo.getUserCode());
	}
}
