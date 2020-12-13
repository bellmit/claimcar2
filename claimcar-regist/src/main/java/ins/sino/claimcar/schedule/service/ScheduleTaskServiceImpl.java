package ins.sino.claimcar.schedule.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleDOrG;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.po.PrpLScheduleDefLoss;
import ins.sino.claimcar.schedule.po.PrpLScheduleItems;
import ins.sino.claimcar.schedule.po.PrpLScheduleTask;
import ins.sino.claimcar.schedule.po.PrpLScheduleTasklog;
import ins.sino.claimcar.schedule.vo.PrpDScheduleDOrGMainVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTasklogVo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.record.PageBreakRecord.Break;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 调度任务查询服务接口实现类
 * @author Dengkk
 * @CreateTime 2015年12月23日
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path(value = "scheduleTaskService")
public class ScheduleTaskServiceImpl implements ScheduleTaskService {
	private Logger logger = LoggerFactory.getLogger(ScheduleTaskServiceImpl.class);

	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	BaseDaoService baseDaoService;
	@Autowired
    CodeTranService codeTranService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	RegistQueryService registQueryService;
    @Autowired
    AreaDictService areaDictService;
    @Autowired
    PrpLCMainService prpLCMainService;
    @Autowired
    PolicyViewService policyViewService;
    public static final String INSCOMCODE = "DHIC";
    public static final String INSCOMPANY = "鼎和财产保险股份有限公司";
    
	/**通过反射，用属性名称获得属性值
	 * @param thisClass需要获取属性值的类
	 * @param fieldName该类的属性名称
	 * @return
	 * @throws Exception
	 */
	private Object getFieldValue(Object thisClass, String fieldName) throws Exception{
		Object value = new Object();
		Method method = null;
		String methodName = fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
		method = thisClass.getClass().getMethod("get" + methodName);
		value = method.invoke(thisClass);
		return value;
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.schedule.service.ScheduleTaskService#findScheduleTaskByRegistNo(java.lang.String)
	 */
	@Override
	public PrpLScheduleTaskVo findScheduleTaskByRegistNo(String registNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("registNo", registNo);
		queryRule.addIn("validFlag", CodeConstants.ValidFlag.INVALID);
		List<PrpLScheduleTask> scheduleTasks = databaseDao.findAll(PrpLScheduleTask.class, queryRule);
		if (scheduleTasks != null && scheduleTasks.size() > 0) {
			PrpLScheduleTaskVo vo = Beans.copyDepth().from(scheduleTasks.get(0)).to(PrpLScheduleTaskVo.class);
			return vo;
		} else {
			return null;
		}
		
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.schedule.service.ScheduleTaskService#saveScheduleTaskByVo(ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo)
	 */
	@Override
	public PrpLScheduleTaskVo saveScheduleTaskByVo(PrpLScheduleTaskVo scheduleTaskVo) {
		PrpLScheduleTask po = Beans.copyDepth().from(scheduleTaskVo).to(PrpLScheduleTask.class);
		if (po.getPrpLScheduleItemses() != null && po.getPrpLScheduleItemses().size() > 0) {
			for (PrpLScheduleItems item: po.getPrpLScheduleItemses()) {
				item.setPrpLScheduleTask(po);
			}
		}
		if (po.getPrpLScheduleDefLosses() != null && po.getPrpLScheduleDefLosses().size() > 0) {
			for (PrpLScheduleDefLoss loss: po.getPrpLScheduleDefLosses()) {
				loss.setPrpLScheduleTask(po);
			}
		}
		if (po.getPrpLScheduleTasklogs() != null && po.getPrpLScheduleTasklogs().size() > 0) {
			for (PrpLScheduleTasklog log: po.getPrpLScheduleTasklogs()) {
				log.setPrpLScheduleTask(po);
			}
		}
		
		
		if (po.getId() != null) {
			databaseDao.update(PrpLScheduleTask.class, po);
		} else {
			databaseDao.save(PrpLScheduleTask.class, po);
		}
		
		scheduleTaskVo = Beans.copyDepth().from(po).to(PrpLScheduleTaskVo.class);
		
		return scheduleTaskVo;
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.schedule.service.ScheduleTaskService#getPrpLScheduleItemsesVoByRegistNo(java.lang.String)
	 */
	@Override
	public List<PrpLScheduleItemsVo> getPrpLScheduleItemsesVoByRegistNo(String registNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("registNo", registNo);
		List<PrpLScheduleItems> poList = databaseDao.findAll(PrpLScheduleItems.class, queryRule);
		List<PrpLScheduleItemsVo> voList = Beans.copyDepth().from(poList).toList(PrpLScheduleItemsVo.class);
		return voList;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.schedule.service.ScheduleTaskService#getPrpLScheduleDefLossesVoByRegistNo(java.lang.String)
	 */
	@Override
	public List<PrpLScheduleDefLossVo> getPrpLScheduleDefLossesVoByRegistNo(String registNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("registNo", registNo);
		List<PrpLScheduleDefLoss> poList = databaseDao.findAll(PrpLScheduleDefLoss.class, queryRule);
		List<PrpLScheduleDefLossVo> voList = new ArrayList<PrpLScheduleDefLossVo>();
		if(poList!=null && poList.size()>0){
			for(PrpLScheduleDefLoss po:poList){
				PrpLScheduleDefLossVo vo = new PrpLScheduleDefLossVo();
				vo = Beans.copyDepth().from(po).to(PrpLScheduleDefLossVo.class);
				vo.setScheduleTaskId(po.getPrpLScheduleTask().getId());
				voList.add(vo);
			}
		}
		return voList;
	}
	
	@Override
	public List<PrpLScheduleDefLossVo> getScheduleDefLossByLossType(String registNo,String deflossType){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("registNo", registNo);
		queryRule.addIn("deflossType", deflossType);
		List<PrpLScheduleDefLoss> poList = databaseDao.findAll(PrpLScheduleDefLoss.class, queryRule);
		List<PrpLScheduleDefLossVo> voList = Beans.copyDepth().from(poList).toList(PrpLScheduleDefLossVo.class);
		
		return voList;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.schedule.service.ScheduleTaskService#getPrpLScheduleDefLossesVoByIds(java.util.List)
	 */
	@Override
	public List<PrpLScheduleDefLossVo> getPrpLScheduleDefLossesVoByIds(List<Long> defLossVoIds) {
		QueryRule queryRule = QueryRule.getInstance();
		if(defLossVoIds == null || defLossVoIds.size() == 0){
			return new ArrayList<PrpLScheduleDefLossVo>();
		}
		queryRule.addIn("id", defLossVoIds);
		List<PrpLScheduleDefLoss> poList = databaseDao.findAll(PrpLScheduleDefLoss.class, queryRule);
		List<PrpLScheduleDefLossVo> voList = Beans.copyDepth().from(poList).toList(PrpLScheduleDefLossVo.class);
		return voList;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.schedule.service.ScheduleTaskService#getPrpLScheduleTaskVoByIds(java.util.List)
	 */
	@Override
	public List<PrpLScheduleTaskVo> getPrpLScheduleTaskVoByIds(List<Long> taskVoIds) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("id", taskVoIds);
		List<PrpLScheduleTask> poList = databaseDao.findAll(PrpLScheduleTask.class, queryRule);
		List<PrpLScheduleTaskVo> voList = Beans.copyDepth().from(poList).toList(PrpLScheduleTaskVo.class);
		return voList;
	}
	
	@Override
	public PrpLScheduleTaskVo findPrpLScheduleTaskVoById(Long id){
		PrpLScheduleTaskVo vo = new PrpLScheduleTaskVo();
		PrpLScheduleTask po = databaseDao.findByPK(PrpLScheduleTask.class, id);
		if(po != null){
			vo = Beans.copyDepth().from(po).to(PrpLScheduleTaskVo.class);
		}
		return vo;
	}
	
	@Override
	public PrpLScheduleDefLossVo findScheduleDefLossById(Long id){
		
		PrpLScheduleDefLossVo vo = new PrpLScheduleDefLossVo();
		PrpLScheduleDefLoss po = databaseDao.findByPK(PrpLScheduleDefLoss.class, id);
		
		if(po != null){
			vo = Beans.copyDepth().from(po).to(PrpLScheduleDefLossVo.class);
		}
		return vo;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.schedule.service.ScheduleTaskService#getPrpLScheduleItemsVoByIds(java.util.List)
	 */
	@Override
	public List<PrpLScheduleItemsVo> getPrpLScheduleItemsVoByIds(List<Long> itemVoIds) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("id", itemVoIds);
		List<PrpLScheduleItems> poList = databaseDao.findAll(PrpLScheduleItems.class, queryRule);
		List<PrpLScheduleItemsVo> voList = Beans.copyDepth().from(poList).toList(PrpLScheduleItemsVo.class);
		return voList;
	}
	
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.schedule.service.ScheduleTaskService#findPrpLScheduleDefLossVoById(java.lang.Long)
	 */
	@Override
	public PrpLScheduleDefLossVo findPrpLScheduleDefLossVoById(Long id) {
		PrpLScheduleDefLossVo prpLScheduleDefLossVo = null;
		PrpLScheduleDefLoss prpLScheduleDefLoss = databaseDao.findByPK(PrpLScheduleDefLoss.class, id);
		if(prpLScheduleDefLoss != null){
			prpLScheduleDefLossVo = new PrpLScheduleDefLossVo();
			prpLScheduleDefLossVo = Beans.copyDepth().from(prpLScheduleDefLoss).to(PrpLScheduleDefLossVo.class);
		}
		return prpLScheduleDefLossVo;
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.schedule.service.ScheduleTaskService#findScheduleTaskLogsByRegistNo(java.lang.String)
	 */
	@Override
	public List<PrpLScheduleTasklogVo> findScheduleTaskLogsByRegistNo(String registNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("registNo", registNo);
		queryRule.addDescOrder("scheduledTime");
		List<PrpLScheduleTasklog> poList = databaseDao.findAll(PrpLScheduleTasklog.class, queryRule);
		List<PrpLScheduleTasklogVo> voList = Beans.copyDepth().from(poList).toList(PrpLScheduleTasklogVo.class);
		return voList;
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.schedule.service.ScheduleTaskService#cancelDLossByDLossIds(ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo, java.util.List)
	 */
	@Override
	public void cancelDLossByDLossIds(PrpLScheduleTaskVo newScheduleTaskVo,List<Long> toUpdateIdList) {
		for (Long id : toUpdateIdList) {
			List<PrpLScheduleDefLoss> prpLScheduleDefLosses = new ArrayList<PrpLScheduleDefLoss>();
			PrpLScheduleDefLoss scheduleDefLossPo = databaseDao.findByPK(PrpLScheduleDefLoss.class, id);
			scheduleDefLossPo.setScheduleStatus(CodeConstants.ScheduleStatus.SCHEDULED_CANCEL);
			PrpLScheduleTask prpLScheduleTask = new PrpLScheduleTask();
			//设置主表信息
			Beans.copy().excludeNull().excludeEmpty().from(newScheduleTaskVo).to(prpLScheduleTask);
			prpLScheduleTask.setScheduledUsercode(scheduleDefLossPo.getScheduledUsercode());
			prpLScheduleTask.setScheduledComcode(scheduleDefLossPo.getScheduledComcode());
			prpLScheduleTask.setScheduledComname(null);
			prpLScheduleTask.setScheduledUsername(null);
			prpLScheduleTask.setLossContent(scheduleDefLossPo.getItemsName()+"("+scheduleDefLossPo.getItemsContent()+")");
		    //添加日志信息
			PrpLScheduleTasklog scheduleTasklog = new PrpLScheduleTasklog();
			Beans.copy().excludeNull().excludeEmpty().from(prpLScheduleTask).to(scheduleTasklog);
			List<PrpLScheduleTasklog> prpLScheduleTasklogs = new ArrayList<PrpLScheduleTasklog>();
			scheduleTasklog.setPrpLScheduleTask(prpLScheduleTask);
			prpLScheduleTasklogs.add(scheduleTasklog);
			prpLScheduleTask.setPrpLScheduleTasklogs(prpLScheduleTasklogs);
			prpLScheduleTask.setPrpLScheduleDefLosses(prpLScheduleDefLosses);
			scheduleDefLossPo.setPrpLScheduleTask(prpLScheduleTask);
			//databaseDao.update(PrpLScheduleDefLoss.class,scheduleDefLossPo);
			databaseDao.save(PrpLScheduleTask.class, prpLScheduleTask);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.schedule.service.ScheduleTaskService#cancelItemByItemIds(java.util.List)
	 */
	@Override
	public void cancelItemByItemIds(List<Long> toUpdateIdList) {
		// TODO Auto-generated method stub
		for (Long id : toUpdateIdList) {
			PrpLScheduleItems scheduleItemPo=databaseDao.findByPK(PrpLScheduleItems.class, id);
			scheduleItemPo.setScheduleStatus(CodeConstants.ScheduleStatus.SCHEDULED_CANCEL);
			databaseDao.update(PrpLScheduleItems.class,scheduleItemPo);
		}
		
	}
	
	
	@Override
	public List<PrpLScheduleTaskVo> findScheduleTaskListByRegistNo(String registNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("registNo", registNo);
		List<PrpLScheduleTask> scheduleTasks = databaseDao.findAll(PrpLScheduleTask.class, queryRule);
		if (scheduleTasks != null && scheduleTasks.size() > 0) {
			List<PrpLScheduleTaskVo> voList = Beans.copyDepth().from(scheduleTasks).toList(PrpLScheduleTaskVo.class);
			return voList;
		} else {
			return null;
		}
	}
	@Override
	public List<PrpLScheduleTasklogVo> getScheduleTaskLogs(List<PrpLScheduleTasklogVo> prpLScheduleTasklogs){
//		List<PrpLScheduleTasklogVo> scheduleTasklogs = prpLScheduleTasklogs;
		String[] lossContent = null;
		PrpLScheduleTasklogVo prpLScheduleTasklogVo = new PrpLScheduleTasklogVo();
		for(PrpLScheduleTasklogVo scheduleTaskLogVo:prpLScheduleTasklogs){
			if("1".equals(scheduleTaskLogVo.getScheduleType())){
				lossContent = scheduleTaskLogVo.getLossContent().split(",");
				Beans.copy().from(scheduleTaskLogVo).to(prpLScheduleTasklogVo);
				prpLScheduleTasklogVo.setScheduleType("2");
			}
		}
		if(lossContent != null){
			for(int i=0;i<lossContent.length;i++){
				PrpLScheduleTasklogVo scheduleTasklogVo = new PrpLScheduleTasklogVo();
				Beans.copy().from(prpLScheduleTasklogVo).to(scheduleTasklogVo);
				scheduleTasklogVo.setLossContent(lossContent[i]);
				prpLScheduleTasklogs.add(scheduleTasklogVo);
			}
		}
		return prpLScheduleTasklogs;
	}
	
	public PrpLScheduleTaskVo findScheduleTaskListByScheduleType(String registNo,String scheduleType){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addEqual("scheduleType", scheduleType);
		List<PrpLScheduleTask> scheduleTasks = databaseDao.findAll(PrpLScheduleTask.class, queryRule);
		if (scheduleTasks != null && scheduleTasks.size() > 0) {
			PrpLScheduleTaskVo vo = Beans.copyDepth().from(scheduleTasks).toList(PrpLScheduleTaskVo.class).get(0);
			return vo;
		} else {
			return null;
		}
	}
	
	public void writePrplScheduleTasklog(String registNo,SysUserVo userVo,PrpLCheckVo checkVo,List<PrpLScheduleDefLossVo> defLossVos){
		Date date = new Date();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addEqual("scheduleType", "2");	
		List<PrpLScheduleTask> scheduleTasks = databaseDao.findAll(PrpLScheduleTask.class, queryRule);
		//排除首次调度新增的车
		if(scheduleTasks.get(0).getPrpLScheduleDefLosses() != null && 
				scheduleTasks.get(0).getPrpLScheduleDefLosses().size() > 0){
			PrpLScheduleTask prpLScheduleTask = scheduleTasks.get(0);
			String[] tempU = null;
			for(PrpLScheduleDefLossVo prpLScheduleDefLossVo : defLossVos){
				if(StringUtils.isNotBlank(prpLScheduleDefLossVo.getScheduledUsercode())){
					tempU = prpLScheduleDefLossVo.getScheduledUsercode().split(",");
					 if(tempU.length > 1){
						 break;
					 }
				}
			}
			String userCode = "";
			String userName = "";
			String comCode = "";
			String comName = "";
			if(tempU.length>0){
				 userCode = tempU[0];// 处理人代码
			     userName = codeTranService.transCode("UserCode",userCode);// 处理人名称
			}else {
				userCode = prpLScheduleTask.getScheduledUsercode();// 处理人代码
				userName = prpLScheduleTask.getScheduledUsername();// 处理人名称
			}
			if(tempU.length>1){
		    	 comCode = tempU[1];// 机构代码       
			     comName = codeTranService.transCode("ComCode",comCode);// 机构名称
		    }else{//没有数据就用原来PrpLScheduleTask的数据
		    	 comCode = prpLScheduleTask.getScheduledComcode();// 机构代码
			     comName = prpLScheduleTask.getScheduledComname();// 机构名称
		    }
			SysUserVo tempSysUserVo = new SysUserVo();
			tempSysUserVo.setComCode(comCode);
			tempSysUserVo.setComName(comName);
			tempSysUserVo.setUserCode(userCode);
			tempSysUserVo.setUserName(userName);
			tempSysUserVo.setCreateTime(date);
		    PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
		    List<PrpLCheckCarVo> checkCarVoList = checkTaskVo.getPrpLCheckCars();
		    List<PrpLCheckPropVo> prpLCheckPropVoList = checkTaskVo.getPrpLCheckProps();
		    
		    //车损失
		    if(checkCarVoList != null && checkCarVoList.size() > 0){
				for(PrpLCheckCarVo prpLCheckCarVo : checkCarVoList){
					PrpLScheduleTasklogVo prpLScheduleTasklogVo = setPrpLScheduleTasklog(prpLScheduleTask,checkTaskVo,userVo,tempSysUserVo);
					prpLScheduleTasklogVo.setLossContent(prpLCheckCarVo.getPrpLCheckCarInfo().getLicenseNo());
					PrpLScheduleTasklog prpLScheduleTasklog = new PrpLScheduleTasklog();
					Beans.copy().from(prpLScheduleTasklogVo).to(prpLScheduleTasklog);
					prpLScheduleTasklog.setPrpLScheduleTask(prpLScheduleTask);
					scheduleTasks.get(0).getPrpLScheduleTasklogs().add(prpLScheduleTasklog);
				}
		    }
			
			//财损失
		    if(prpLCheckPropVoList != null && prpLCheckPropVoList.size() > 0){
				for(PrpLCheckPropVo prpLCheckPropVo : prpLCheckPropVoList){
					PrpLScheduleTasklogVo prpLScheduleTasklogVo = setPrpLScheduleTasklog(prpLScheduleTask,checkTaskVo,userVo,tempSysUserVo);
					prpLScheduleTasklogVo.setLossContent(prpLCheckPropVo.getLossItemName());
					PrpLScheduleTasklog prpLScheduleTasklog = new PrpLScheduleTasklog();
					Beans.copy().from(prpLScheduleTasklogVo).to(prpLScheduleTasklog);
					prpLScheduleTasklog.setPrpLScheduleTask(prpLScheduleTask);
					scheduleTasks.get(0).getPrpLScheduleTasklogs().add(prpLScheduleTasklog);
				}
		    }
		}
		databaseDao.update(PrpLScheduleTask.class, scheduleTasks.get(0));
	}

	@Override
	public void setCheckDutyByRegistNo(String registNo,List<Long> toUpdateIdList) {
		logger.info("报案号={},进入调度注销处理回写prplcheckduty的标志位validflag的方法",registNo);
		List<PrpLCheckDutyVo> prpLCheckDutyVoList = checkTaskService.findCheckDutyByRegistNo(registNo);
		List<PrpLCheckCarVo> prpLCheckCarVos = checkTaskService.findCheckCarVo(registNo);
		List<PrpLWfTaskVo> taskVoList = wfTaskHandleService.findEndTask(registNo, null, FlowNode.Check);
		for (Long id : toUpdateIdList) {
			PrpLScheduleDefLoss scheduleDefLossPo = databaseDao.findByPK(PrpLScheduleDefLoss.class, id);
			scheduleDefLossPo.setFlag("0");
			databaseDao.update(PrpLScheduleDefLoss.class, scheduleDefLossPo);
			if("1".equals(scheduleDefLossPo.getDeflossType())){//车才回写
				if(prpLCheckDutyVoList!=null && prpLCheckDutyVoList.size()>0){
					for(PrpLCheckDutyVo vo:prpLCheckDutyVoList){
						if(vo.getSerialNo().equals(scheduleDefLossPo.getSerialNo())){
							logger.info("报案号={},进行prplcheckduty的标志位validflag的回写,validflag=0",registNo);
							vo.setValidFlag("0");//无效
						}
					}
					//设置
					checkTaskService.saveOrUpdateCheckDutyList(prpLCheckDutyVoList);
				}
				if(prpLCheckCarVos!=null && prpLCheckCarVos.size()>0){
					//如果查勘未提交，直接删除PrpLCheckCar的数据
					if(taskVoList == null){
						for(PrpLCheckCarVo vo:prpLCheckCarVos){
							if(vo.getSerialNo().equals(scheduleDefLossPo.getSerialNo())){
								checkTaskService.deleteCheckCar(vo);
							}
						}
					}else{
						for(PrpLCheckCarVo vo:prpLCheckCarVos){
							if(vo.getSerialNo().equals(scheduleDefLossPo.getSerialNo())){
								vo.setValidFlag("0");//无效
								vo.getPrpLCheckCarInfo().setValidFlag("0");
								vo.getPrpLCheckDriver().setValidFlag("0");
								checkTaskService.updateCheckCar(vo);
							}
						}
					}
				}
			}
		}
		logger.info("报案号={},结束调度注销处理回写prplcheckduty的标志位validflag的方法",registNo);
	}
	
	@Override
	public SysUserVo findPrpduserByUserCode(String userCode,String validStatus) {
		List<Object[]> paramValues = new ArrayList<Object[]>();
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" SELECT USERNAME,USERCODE,IDENTIFYNUMBER,MOBILE,COMCODE FROM ywuser.PrpDuser WHERE 1=1");
		sqlUtil.append(" AND userCode = ?");
		sqlUtil.addParamValue(userCode);
		paramValues = baseDaoService.getAllBySql(sqlUtil.getSql(),sqlUtil.getParamValues());
		SysUserVo sysUserVo = new SysUserVo();
		if(paramValues !=null && paramValues.size()>0){
			Object[] obj = paramValues.get(0);
			sysUserVo.setUserName(obj[0]==null ? "" : obj[0].toString());
			sysUserVo.setUserCode(obj[1]==null ? "" : obj[1].toString());
			sysUserVo.setIdentifyNumber(obj[2]==null ? "" : obj[2].toString());
			sysUserVo.setMobile(obj[3]==null ? "" : obj[3].toString());
			sysUserVo.setComCode(obj[4]==null ? "" : obj[4].toString());
		}
		return sysUserVo;
	}

    @Override
    public PrpLScheduleTaskVo findCheckScheduleTaskByRegistNo(String registNo,String isPersonFlag) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addIn("registNo", registNo);
        queryRule.addIn("validFlag", CodeConstants.ValidFlag.VALID);
        queryRule.addEqual("isPersonFlag",isPersonFlag);
        queryRule.addAscOrder("createTime");
        List<PrpLScheduleTask> scheduleTasks = databaseDao.findAll(PrpLScheduleTask.class, queryRule);
        if (scheduleTasks != null && scheduleTasks.size() > 0) {
            PrpLScheduleTaskVo vo = Beans.copyDepth().from(scheduleTasks.get(0)).to(PrpLScheduleTaskVo.class);
            return vo;
        } else {
            return null;
        }
        
    }

    @Override
    public PrpLScheduleDefLossVo findPrpLScheduleDefLossVoByOther(String registNo,Integer serialNo,String deflossType) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo", registNo);
        queryRule.addEqual("serialNo", serialNo);
        queryRule.addEqual("deflossType",deflossType);
        queryRule.addAscOrder("createTime");
        List<PrpLScheduleDefLoss> scheduleDefLoss = databaseDao.findAll(PrpLScheduleDefLoss.class, queryRule);
        if (scheduleDefLoss != null && scheduleDefLoss.size() > 0) {
            PrpLScheduleDefLossVo vo = Beans.copyDepth().from(scheduleDefLoss.get(0)).to(PrpLScheduleDefLossVo.class);
            return vo;
        } else {
            return null;
        }
    }
    
    @Override
    public void updatePrpLScheduleItems(PrpLScheduleItemsVo prpLScheduleItemsVo){
    	PrpLScheduleItems po = databaseDao.findByPK(PrpLScheduleItems.class, prpLScheduleItemsVo.getId());
    	Beans.copy().from(prpLScheduleItemsVo).to(po);
    	databaseDao.update(PrpLScheduleItems.class, po);
    }

	@Override
	public void updatePrpLScheduleTaskVo(PrpLScheduleTaskVo vo) {
		PrpLScheduleTask po = Beans.copyDepth().from(vo).to(PrpLScheduleTask.class);
		databaseDao.update(PrpLScheduleTask.class, po);
	}

	@Override
	public HandleScheduleReqScheduleDOrG setScheduleDOrG(PrpDScheduleDOrGMainVo prpDScheduleDOrGMainVo) {
		HandleScheduleReqScheduleDOrG scheduleDOrG = prpDScheduleDOrGMainVo.getScheduleDOrG();
		List<PrpLCMainVo> prpLCMainVoList = prpDScheduleDOrGMainVo.getPrpLCMainVoList();
		PrpLRegistVo prpLRegistVo = prpDScheduleDOrGMainVo.getPrpLRegistVo();
		PrpLScheduleTaskVo prpLScheduleTaskVo = prpDScheduleDOrGMainVo.getPrpLScheduleTaskVo();
		String schType = prpDScheduleDOrGMainVo.getSchType();
		PrpLCMainVo cMainVo = new PrpLCMainVo();
		
		if(prpLCMainVoList != null && prpLCMainVoList.size() > 0){
			if(prpLCMainVoList.size()==2){
				for(PrpLCMainVo prpLCMainVo : prpLCMainVoList){
					if(("12").equals(prpLCMainVo.getRiskCode().substring(0, 2))){
						cMainVo = prpLCMainVoList.get(0);
					}
				}
			}else{
				cMainVo = prpLCMainVoList.get(0);
			}
			//二期改造start
			for(PrpLCMainVo prpLCMainVo : prpLCMainVoList){
	            if("12".equals(cMainVo.getRiskCode().subSequence(0,2))){
	                scheduleDOrG.setBusiPolicyNo(prpLCMainVo.getPolicyNo());
	            }else{
	                scheduleDOrG.setPolicyNo(prpLCMainVo.getPolicyNo());
	            }
	        }
			//二期改造end
		}else{
			scheduleDOrG.setAgentCode("all");
		}
		
		String businessPlate ="";
		businessPlate = CodeTranUtil.transCode("businessPlate", cMainVo.getBusinessPlate());
		//代理人编码
		if(cMainVo.getAgentCode()!=null){
			scheduleDOrG.setAgentCode(cMainVo.getAgentCode());
		}
		//保单归属地编码
		if(cMainVo.getComCode() != null){
			//保单归属地编码
			scheduleDOrG.setComCode(cMainVo.getComCode());
		}
		
		if(businessPlate !="" && businessPlate != null){
			scheduleDOrG.setBusiNessType(businessPlate);//业务类型
		}else{
			scheduleDOrG.setBusiNessType("all");//业务类型
		}
		//TODO 到时取大客户的值
		if(prpLCMainVoList != null && prpLCMainVoList.size() > 0){
			if(StringUtils.isNotBlank(cMainVo.getAgentName()) ||
					StringUtils.isNotBlank(cMainVo.getAgentCode())){
				scheduleDOrG.setCustomType("2");//客户分类
			}else{
				scheduleDOrG.setCustomType("3");//客户分类
			}
		
		}else{
			scheduleDOrG.setCustomType("all");//客户分类
		}
		
        //自助理赔start
		List<PrpLCInsuredVo> prpLCInsuredVos = cMainVo.getPrpCInsureds();
		if(prpLCInsuredVos != null && prpLCInsuredVos.size() >0 ){
			for(PrpLCInsuredVo lCInsuredVo : prpLCInsuredVos){
				if("1".equals(lCInsuredVo.getInsuredFlag())){
					scheduleDOrG.setIdentifyType(lCInsuredVo.getIdentifyType());//被保人证件类型
					scheduleDOrG.setIdentifyNumber(lCInsuredVo.getIdentifyNumber());//被保人证件号码
					break;
				}
			}
		}
        PrpLRegistExtVo registExtVo = prpLRegistVo.getPrpLRegistExt();
        scheduleDOrG.setAccidentType(registExtVo.getAccidentTypes());
        scheduleDOrG.setDamageCode(prpLRegistVo.getDamageCode());
        scheduleDOrG.setWeather("");
        scheduleDOrG.setAccidentDesc(registExtVo.getDangerRemark());
        scheduleDOrG.setDutyType(registExtVo.getObliGation());
        scheduleDOrG.setInsComcode(INSCOMCODE);
        scheduleDOrG.setInsCompany(INSCOMPANY);
        PrpLCItemCarVo prpLCItemCarVo = registQueryService.findCItemCarByRegistNo(prpLScheduleTaskVo.getRegistNo());
        scheduleDOrG.setFrameNo(prpLCItemCarVo.getFrameNo());
        scheduleDOrG.setEngineNo(prpLCItemCarVo.getEngineNo());
        scheduleDOrG.setCarownName(prpLCItemCarVo.getCarOwner());
        scheduleDOrG.setCarownPhone("");
        //自助理赔end
        
        scheduleDOrG.setRegistNo(prpLRegistVo.getRegistNo());
        //待定
        if("reassig".equals(schType)){
            scheduleDOrG.setScheduleType("1");
        }else{
            scheduleDOrG.setScheduleType("0");
        }
        String lngXlatY = prpLScheduleTaskVo.getCheckAddressMapCode();
        //获取报案信息
        PrpLCMainVo vo = prpLCMainService.findPrpLCMain(prpLScheduleTaskVo.getRegistNo(), prpLRegistVo.getPolicyNo());
        String[] code = areaDictService.findAreaByAreaCode(prpLScheduleTaskVo.getCheckorDeflossAreaCode(),"");
        String provinceCode="";
        String cityCode="";
        if(code!=null && code.length>0){
        	provinceCode = code[0];
            cityCode = code[1];
        }
        
        String regionCode = prpLScheduleTaskVo.getCheckorDeflossAreaCode();
        scheduleDOrG.setDamageTime(prpLRegistVo.getDamageTime());
        scheduleDOrG.setDamagePlace(prpLScheduleTaskVo.getDamageAddress());
        if(vo!=null){
			scheduleDOrG.setInuredName(vo.getInsuredName());
		}else{
			if("1".equals(prpLRegistVo.getTempRegistFlag())){
				List<PrpLCMainVo> prpLCMainVos = policyViewService.getPolicyForPrpLTmpCMain(prpLScheduleTaskVo.getRegistNo());
				if(prpLCMainVos!=null && prpLCMainVos.size() > 0){
					if(StringUtils.isNotBlank(prpLCMainVos.get(0).getInsuredName())){
						scheduleDOrG.setInuredName(prpLCMainVos.get(0).getInsuredName());
					}
				}
			}
		}
        scheduleDOrG.setInuredPhone(prpLRegistVo.getInsuredPhone());
        scheduleDOrG.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
        scheduleDOrG.setLinkerName(prpLScheduleTaskVo.getLinkerMan());
        scheduleDOrG.setLinkerPhone(prpLScheduleTaskVo.getLinkerManPhone());
        scheduleDOrG.setPolicyNo(prpLRegistVo.getPolicyNo());
        scheduleDOrG.setProvinceCode(provinceCode);
        scheduleDOrG.setCityCode(cityCode);
        scheduleDOrG.setRegionCode(regionCode);
        scheduleDOrG.setReportorName(prpLRegistVo.getReportorName());
        scheduleDOrG.setReportorPhone(prpLRegistVo.getReportorPhone());
        scheduleDOrG.setReportTime(prpLRegistVo.getReportTime());
        scheduleDOrG.setLngXlatY(lngXlatY != null ? lngXlatY : "");
        //设置自定义区域代码
        if(StringUtils.isNotEmpty(prpLScheduleTaskVo.getSelfDefinareaCode())){
            scheduleDOrG.setSelfDefinareaCode(prpLScheduleTaskVo.getSelfDefinareaCode());
        }
		return scheduleDOrG;
	}
	
	public PrpLScheduleTasklogVo setPrpLScheduleTasklog(PrpLScheduleTask prpLScheduleTask,PrpLCheckTaskVo checkTaskVo,SysUserVo userVo,SysUserVo tempSysUserVo) {
		PrpLScheduleTasklogVo prpLScheduleTasklogVo = new PrpLScheduleTasklogVo();
		Beans.copy().from(prpLScheduleTask).to(prpLScheduleTasklogVo);
		prpLScheduleTasklogVo.setScheduleStatus(CodeConstants.ScheduleStatus.DEFLOSS_SCHEDULED);
		prpLScheduleTasklogVo.setCreateTime(tempSysUserVo.getCreateTime());
		prpLScheduleTasklogVo.setCreateUser(userVo.getUserCode());
		prpLScheduleTasklogVo.setUpdateTime(tempSysUserVo.getCreateTime());
		prpLScheduleTasklogVo.setUpdateUser(userVo.getUserCode());
		prpLScheduleTasklogVo.setId(null);
		prpLScheduleTasklogVo.setScheduledComcode(tempSysUserVo.getComCode());
		prpLScheduleTasklogVo.setScheduledComname(tempSysUserVo.getComName());
		prpLScheduleTasklogVo.setScheduledUsername(tempSysUserVo.getUserName());
		prpLScheduleTasklogVo.setScheduledUsercode(tempSysUserVo.getUserCode());
		prpLScheduleTasklogVo.setLinkerMan(checkTaskVo.getLinkerName());
		prpLScheduleTasklogVo.setLinkerManPhone(checkTaskVo.getLinkerMobile());
		prpLScheduleTasklogVo.setCheckareaName(checkTaskVo.getCheckAddress());
		prpLScheduleTasklogVo.setCheckAddress(checkTaskVo.getCheckAddress());
		return prpLScheduleTasklogVo;
	}
	
}
