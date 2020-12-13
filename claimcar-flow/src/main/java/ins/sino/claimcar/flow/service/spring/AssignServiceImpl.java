package ins.sino.claimcar.flow.service.spring;

import freemarker.template.utility.StringUtil;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.util.ConfigUtil;
import ins.platform.saa.service.facade.SaaUserGradeService;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaUserGradeVo;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.po.PrpLTaskAssign;
import ins.sino.claimcar.flow.po.PrpLUserHoliday;
import ins.sino.claimcar.flow.service.AssignRuleService;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.util.AssignConstants;
import ins.sino.claimcar.flow.vo.AssignUserVo;
import ins.sino.claimcar.manager.vo.PrpLUserHolidayVo;
import ins.sino.claimcar.rule.utils.Drools5RuleAgent;

import java.math.BigDecimal;
import java.util.*;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 组权限统一分配类
 * @author zhouyanbin
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "AssignService")
public class AssignServiceImpl implements AssignService {
	
	private Logger logger = LoggerFactory.getLogger(AssignServiceImpl.class);
	
	@Autowired
	private DatabaseDao databaseDao;

	@Autowired
	private AssignRuleService assignRuleService;
	
	@Autowired
	private SaaUserGradeService saaUserGradeService;
	
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SaaUserPowerService saaUserPowerService;
	
	public SysUserVo execute(FlowNode nodeCode,String comCode,SysUserVo userVo, String isExcludeCurrentUser) {
		logger.info("------------------------------------------------------>进入轮询人员逻辑/n");
		// 查询开关表
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.AssignDis, comCode);
		if ("1".equals(configValueVo.getConfigValue()) && !"0".equals(isExcludeCurrentUser)) {
			SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(userVo.getUserCode());
			List<String> roleList = userPowerVo.getRoleList();
			if (roleList.contains(nodeCode.getRoleCode())) {
				//当前人存在下一节点权限，则指派给当前人
				return userVo;
			} else {
				return executeAss(nodeCode, comCode, null, "");
			}
		} else {
		    String excludeUserCode = "";
		    if ("0".equals(isExcludeCurrentUser)) {
                excludeUserCode = userVo.getUserCode();
            }
			return executeAss(nodeCode, comCode, null, excludeUserCode);
		}
	}

	/**
	 * 判断该工号是否可以处理当前任务的下一节点任务
	 * @param nodeCode 下一节点信息
	 * @param comCode 处理机构
	 * @param userCode 关联任务的处理人工号
	 * @return
	 */
	@Override
	public boolean isUserCouldHandle(FlowNode nodeCode, String comCode, String userCode) {
		SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(userCode);
		List<String> roleList = userPowerVo.getRoleList();
		boolean couldHandleTask = false;
		if (roleList.contains(nodeCode.getRoleCode())) {
			// 如果该工号具有当前节点的处理权限
			couldHandleTask = true;
		} else {
			// 如果不具有当前节点的处理权限，那么看是否有更高等级的权限
			couldHandleTask = isUserHasHigerLevel(nodeCode, roleList);
		}
		return couldHandleTask;
	}

	/**
	 * 判断工号是否拥有比任务要求更高的权限
	 * @param taskNode
	 * @param roleList
	 * @return
	 */
	public boolean isUserHasHigerLevel(FlowNode taskNode, List<String> roleList) {
		boolean hasHigerLevel = false;
		// 获取当前任务的节点名称
		String nodeName = taskNode.name();
		if (StringUtils.isBlank(nodeName)) {
			return false;
		}
		String[] nodeNameArr = null;
		if (nodeName.indexOf("_LV") > 0) {
			nodeNameArr = nodeName.split("_LV");
		} else {
			return false;
		}
		// 获取当前节点任务的等级
		int level = Integer.parseInt(nodeNameArr[1]);
		// 需要提交到总公司
		if (level >= CodeConstants.TopOfficeVerifyLevel.LEVEL9) {
			return false;
		}
		Map<String, Integer> taskLevelMap =  CodeConstants.TASKNODEMAP.get(nodeNameArr[0]);
		// 未知任务类型
		if (taskLevelMap == null) {
			return false;
		}
		// 节点等级集合
		List<String> levelList = new ArrayList<String>(taskLevelMap.keySet());
		// 节点等级集合保留其与工号所拥有权限等级的交集
		levelList.retainAll(roleList);
		for (String roleCode : levelList) {
			int getLevel = taskLevelMap.get(roleCode);
			// 如果具有总公司权限，且高于任务要求的等级
			if (getLevel < CodeConstants.TopOfficeVerifyLevel.LEVEL9 && getLevel > level) {
				hasHigerLevel = true;
				break;
			}
		}

		return hasHigerLevel;
	}
	
	/**
	 * 组权限统一分配入口
	 * @param groupId 组ID
	 * @param nodeId 当前节点ID
	 * @param taskCode 权限代码
	 * @param comCodes 机构代码
	 * @param riskCodes 险种代码 其中组ID、当前节点ID用于组内分配，分配完毕后更新任务数和任务分配最新时间 其中上一节点ID、上一节点处理人用于更新上一节点人员名下的任务数
	 * @param    excludeUserCode 被排除处理人编码
	 * @return
	 */
	public SysUserVo executeAss(FlowNode nodeCode,String comCode,String riskCode, String excludeUserCode) {
		// 根据NodeCode得到gradeID
		String gradeId = nodeCode.getRoleCode();
		
		if(gradeId == null){
			return null;
		}
	
		// 组内人员列表
		
		// 在SaaUserGrade表中获取符合条件的userGrade
		List<SaaUserGradeVo> saaUserGradeVoList = saaUserGradeService.findSaaUserGradeListByGradeIdAndComCode(gradeId,comCode);
		if(saaUserGradeVoList==null||saaUserGradeVoList.size()==0){
			return null;
		}
		//根据相同条件查询PrpLTaskAssign表是否有数据
		List<PrpLTaskAssign> prpLTaskAssignList = getPrpLAssignCountList(comCode, gradeId);
		//使用SaaUserGradeVoList对PrpLTaskAssign表的数据进行实时更新
		prpLTaskAssignList = updatePrpLAssignCountList(saaUserGradeVoList,prpLTaskAssignList,comCode);
		//排除休假员工
		prpLTaskAssignList = getValidAssignList(prpLTaskAssignList,comCode,gradeId);
		
		// 2、对象转化（由于底层的分配逻辑中，AssignUserVo是抽象出来的，所以需由PrpLassignCount转化）
		List<AssignUserVo> assignUserVolist = new ArrayList<AssignUserVo>();
		// 组的分配方式
		String assignType = AssignConstants.LOOP;
		// 该PO为员工岗位表
		for (PrpLTaskAssign taskAssign : prpLTaskAssignList) {
			if("0000000000".equals(taskAssign.getUserCode())){
				//系统管理员不参与轮询
				continue;
			}
			// 如果 被排除处理人编码不为空，就排除此人
			if (StringUtils.isNotBlank(excludeUserCode)) {
				if (excludeUserCode.equals(taskAssign.getUserCode())) {
					continue;
				}
			}
			// 底层分配逻辑使用的vo
			AssignUserVo assignUserVo = new AssignUserVo();
			String userCode = taskAssign.getUserCode();
			// 人员状态表，记录人员的岗位和最后一次分配任务的时间
			Date lastTime = taskAssign.getLastTime();
			assignUserVo = orgnAssignUserVo(userCode, lastTime);
			assignUserVolist.add(assignUserVo);
		}
		// 3、调用分配逻辑
		AssignUserVo assignUserVo = assignRuleService.assignTask(assignType, assignUserVolist);
		if(assignUserVo == null){
			return null;
		}
		
		SysUserVo userVo = sysUserService.findByUserCode(assignUserVo.getUserCode());
		
		updatePrpLAssignCountAfterPolling(userVo,comCode,nodeCode);

		return userVo;
	}
	
	/**
	 * 更新PrpLAssignCount表中被分配任务用户的数据
	 * <pre></pre>
	 * @param assignUserVo
	 * @param comCode
	 * @param gradeId
	 * @modified:
	 * ☆WLL(2016年8月8日 下午5:48:55): <br>
	 */
	public void updatePrpLAssignCountAfterPolling(SysUserVo userVo,String comCode,FlowNode nodeCode){
		List<PrpLTaskAssign> prpLTaskAssignList = getPrpLAssignCountList(comCode, nodeCode.getRoleCode());
		if(prpLTaskAssignList!=null && !prpLTaskAssignList.isEmpty()){
			
			for(PrpLTaskAssign taskAssign:prpLTaskAssignList){
				if(taskAssign.getUserCode().equals(userVo.getUserCode())){
					taskAssign.setAssignCount(DataUtils.NullToZero(taskAssign.getAssignCount()).add(BigDecimal.ONE));
					taskAssign.setLastTime(new Date());
					taskAssign.setUpdateTime(new Date());
				}
			}
		}
	}
	
	/**
	 * 使用SaaUserGradeVoList对PrpLTaskAssign表的数据进行实时更新，并返回可以参与轮询的用户岗位数据
	 * <pre></pre>
	 * @param SaaUserGradeVoList
	 * @param PrpLTaskAssignList
	 * @return
	 * @modified:
	 * ☆WLL(2016年8月8日 下午5:00:44): <br>
	 */
	private List<PrpLTaskAssign> updatePrpLAssignCountList(List<SaaUserGradeVo> SaaUserGradeVoList,List<PrpLTaskAssign> PrpLTaskAssignList,String comCode){
		List<PrpLTaskAssign> rePrpLTaskAssignList = new ArrayList<PrpLTaskAssign>();
		boolean exsitFlag = false;
		
		for(SaaUserGradeVo saaUserVo:SaaUserGradeVoList){
			exsitFlag = false;
			for(PrpLTaskAssign taskAss: PrpLTaskAssignList){
				if(saaUserVo.getUserCode().equals(taskAss.getUserCode())){
					//判断saa中的用户岗位数据再taskAss表中是否存在
					exsitFlag = true;
					rePrpLTaskAssignList.add(taskAss);
					break;
				}
			}
			if(!exsitFlag){
				//taskAss表中不存在就新增一条记录
				PrpLTaskAssign newTaskAss = new PrpLTaskAssign();
				
				//TODO 设置最后一次分配任务时间为15天前，需要核实这样赋值是否正确
				Date date = new Date();//取时间 
			    Calendar calendar = new GregorianCalendar();  
			    calendar.setTime(date); 
			    calendar.add(calendar.DATE,-15);//把日期往前面推15天
			    date=calendar.getTime();
			    
				newTaskAss.setLastTime(date);
				newTaskAss.setUserCode(saaUserVo.getUserCode());
				newTaskAss.setGradeId(saaUserVo.getSaaGrade().getId().toString());
				newTaskAss.setComCode(comCode);
				newTaskAss.setAssignCount(BigDecimal.ZERO);
				newTaskAss.setValidStatus("1");
				newTaskAss.setCreateTime(new Date());
				newTaskAss.setUpdateTime(new Date());
				
				databaseDao.save(PrpLTaskAssign.class,newTaskAss);
				
				rePrpLTaskAssignList.add(newTaskAss);
				
			}
		}
		return rePrpLTaskAssignList;
	}
	
	/**
	 * 根据机构和岗位Id查询岗位下的员工
	 * @param gradeId
	 * @return
	 */
	public List<PrpLTaskAssign> getPrpLAssignCountList(String comCode,String gradeId) {
		String comCodePre = comCode.substring(0,4)+"%";
		List<PrpLTaskAssign> prpLTaskAssignList = new ArrayList<PrpLTaskAssign>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqualIfExist("gradeId", gradeId);
		queryRule.addLikeIfExist("comCode", comCodePre);
		queryRule.addEqualIfExist("validStatus", CodeConstants.ValidFlag.VALID);
		prpLTaskAssignList = databaseDao.findAll(PrpLTaskAssign.class,queryRule);
		return prpLTaskAssignList;
	}
	
	/**
	 * 排除掉休假员工
	 * @param comCode
	 * @param gradeId
	 * @return
	 */
	public List<PrpLTaskAssign> getValidAssignList(List<PrpLTaskAssign> prpLTaskAssignList,String comCode,String gradeId) {
		
		List<PrpLTaskAssign> reAssignCountList = new ArrayList<PrpLTaskAssign>();
		List<PrpLUserHolidayVo> userHoliList = findAssignUserHolidayListByTime();
		List<String> userHoliCodeList = new ArrayList<String>();
		for(PrpLUserHolidayVo userHoli:userHoliList){
			userHoliCodeList.add(userHoli.getUserCode());
		}
		for(PrpLTaskAssign taskAss:prpLTaskAssignList){
			if(!userHoliCodeList.contains(taskAss.getUserCode())){
				reAssignCountList.add(taskAss);
			}
		}
		
		return reAssignCountList;
	}
	
	/**
	 * 查询正在休假期间的员工
	 * @return
	 */
	public List<PrpLUserHolidayVo> findAssignUserHolidayListByTime() {
		List<PrpLUserHolidayVo> userHoliList = new ArrayList<PrpLUserHolidayVo>();
		Date date = new Date();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addGreaterThan("endDate",date);
		queryRule.addLessThan("startDate",date);
		queryRule.addEqual("checkStatus","1");
		List<PrpLUserHoliday> userPoList = new ArrayList<PrpLUserHoliday>();
		userPoList = databaseDao.findAll(PrpLUserHoliday.class,queryRule);
		userHoliList = Beans.copyDepth().from(userPoList).toList(PrpLUserHolidayVo.class);
		return userHoliList;
	}

	/**
	 * 查询岗位ID和员工Code查询人员状态表
	 * @param gradeId
	 * @return
	 */
	public PrpLTaskAssign findPrpLAssignCountByUserCodeAndNodeId(String userCode,String gradeId) {
		PrpLTaskAssign PrpLAssignCount = new PrpLTaskAssign();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqualIfExist("gradeId", gradeId);
		queryRule.addEqualIfExist("userCode", userCode);
		PrpLAssignCount = databaseDao.findUnique(PrpLTaskAssign.class, queryRule);
		return PrpLAssignCount;
	}
	
	/**
	 * 查询员工Code是否存在休假记录
	 * @param userCode
	 * @return true-没有休假，false-正在休假
	 */
	@Override
	public boolean findHolidayByUserCode(String userCode){
		boolean isHoliday = true;//初始化没有休假
		Date date = new Date();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("userCode",userCode);
		queryRule.addGreaterThan("endDate",date);
		queryRule.addLessThan("startDate",date);
		queryRule.addEqual("checkStatus","1");
		List<PrpLUserHoliday> holidayList = databaseDao.findAll(PrpLUserHoliday.class,queryRule);
		
		if(holidayList != null && holidayList.size() > 0){
			isHoliday = false;//正在休假
		}
		return isHoliday;
	}
	
	/**
	 * 判断有效的人员（SysUserVo有效且没有休假）
	 * @param userCode
	 * @return
	 */
	@Override
	public boolean validUserCode(String userCode){
		boolean isValid = false;//默认无效
		SysUserVo userVo = sysUserService.findByUserCode(userCode);
		if(userVo != null && "1".equals(userVo.getValidStatus())){
			isValid = findHolidayByUserCode(userCode);
		}
		return isValid;
	}

	/**
	 * 组织数据
	 * @param userCode 员工编号
	 * @param taskCount 当前任务数
	 * @param assignTime 最后一次分配时间
	 * @return
	 */
	private static AssignUserVo orgnAssignUserVo(String userCode,Date assignTime){
		AssignUserVo assignUserVo = new AssignUserVo();
		// 用户编码
		assignUserVo.setUserCode(userCode);

		// 最后一次任务分配时间
		assignUserVo.setAssignTime(assignTime);
		return assignUserVo;
	}
	
	/**
	 * 该级别是否可以删除
	 * @param nodeCode
	 * @param comCode
	 * @return
	 */
	public boolean existsGradeUser(FlowNode nodeCode,String comCode) {
		// 根据NodeCode得到gradeID
		String gradeId = nodeCode.getRoleCode();
		
		if(gradeId == null){
			return false;
		}
	
		// 组内人员列表
		
		// 在SaaUserGrade表中获取符合条件的userGrade
		List<SaaUserGradeVo> saaUserGradeVoList = saaUserGradeService.findSaaUserGradeListByGradeIdAndComCode(gradeId,comCode);
		if(saaUserGradeVoList==null||saaUserGradeVoList.size()==0){
			return false;
		}
		
		return true;
	}	

	/**
    * 清除规则数据缓存
    */
	@Override
	public void clearRule() {
		
		Drools5RuleAgent DrA=new Drools5RuleAgent();
		DrA.clearCache();
		
	}
	
	/**
	 * 根据岗位和机构返回人员
	 * @param nodeCode
	 * @param comCode
	 * @return
	 */
	@Override
	public List<AssignUserVo> returnUserVoByGrade(FlowNode nodeCode,String comCode){
		// 根据NodeCode得到gradeID
        String gradeId = nodeCode.getRoleCode();
		
		if(gradeId == null){
			return null;
		}
	
		// 组内人员列表
		
		// 在SaaUserGrade表中获取符合条件的userGrade
		List<SaaUserGradeVo> saaUserGradeVoList = saaUserGradeService.findSaaUserGradeListByGradeIdAndComCode(gradeId,comCode);
		if(saaUserGradeVoList==null||saaUserGradeVoList.size()==0){
			return null;
		}
		//根据相同条件查询PrpLTaskAssign表是否有数据
		List<PrpLTaskAssign> prpLTaskAssignList = getPrpLAssignCountList(comCode, gradeId);
		//使用SaaUserGradeVoList对PrpLTaskAssign表的数据进行实时更新
		prpLTaskAssignList = updatePrpLAssignCountList(saaUserGradeVoList,prpLTaskAssignList,comCode);
		//排除休假员工
		prpLTaskAssignList = getValidAssignList(prpLTaskAssignList,comCode,gradeId);
		
		// 2、对象转化（由于底层的分配逻辑中，AssignUserVo是抽象出来的，所以需由PrpLassignCount转化）
		List<AssignUserVo> assignUserVolist = new ArrayList<AssignUserVo>();
		// 组的分配方式
		String assignType = AssignConstants.LOOP;
		// 该PO为员工岗位表
		for (PrpLTaskAssign taskAssign : prpLTaskAssignList) {
			if("0000000000".equals(taskAssign.getUserCode())){
				//系统管理员不参与轮询
				continue;
			}
			// 底层分配逻辑使用的vo
			AssignUserVo assignUserVo = new AssignUserVo();
			String userCode = taskAssign.getUserCode();
			// 人员状态表，记录人员的岗位和最后一次分配任务的时间
			Date lastTime = taskAssign.getLastTime();
			assignUserVo = orgnAssignUserVo(userCode, lastTime);
			assignUserVolist.add(assignUserVo);
		}
		
		return assignUserVolist;
	}
}
