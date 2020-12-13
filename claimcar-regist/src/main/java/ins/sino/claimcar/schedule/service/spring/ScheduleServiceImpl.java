/******************************************************************************
* CREATETIME : 2015年12月14日 下午3:10:35
* FILE       : ins.sino.claimcar.schedule.service.ScheduleServiceImpl
******************************************************************************/
package ins.sino.claimcar.schedule.service.spring;


import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.ConfigUtil;
import ins.platform.saa.service.facade.SaaUserGradeService;
import ins.platform.saa.vo.SaaGradeVo;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.IsSingleAccident;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WFMobileService;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.mobilecheck.service.MobileCheckService;
import ins.sino.claimcar.mobilecheck.service.SendMsgToMobileService;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleDOrGReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqDOrGBody;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleDOrG;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleItemDOrG;
import ins.sino.claimcar.mobilecheck.vo.HeadReq;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.po.PrpLScheduleDefLoss;
import ins.sino.claimcar.schedule.po.PrpLScheduleItems;
import ins.sino.claimcar.schedule.po.PrpLScheduleTask;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpDScheduleDOrGMainVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * <pre></pre>
 * @author ★yangkun
 */
@Service(protocol = {"dubbo"}, validation = "true",registry = {"default"})
@Path("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	DatabaseDao databaseDao;
    @Autowired
    MobileCheckService mobileCheckService;
    @Autowired
    RegistQueryService registQueryService;
    @Autowired
    AreaDictService areaDictService;
    @Autowired
    PrpLCMainService prpLCMainService;
    @Autowired
    WfTaskHandleService wfTaskHandleService;
    @Autowired
    WfMainService wfMainService;
    @Autowired
    ScheduleTaskService scheduleTaskService;
    @Autowired
    SendMsgToMobileService sendMsgToMobileService; 
    @Autowired
    private WFMobileService wFMobileService;
    @Autowired
    PolicyViewService policyViewService;
	@Autowired
	SaaUserGradeService saaUserGradeService;
    public static final String HANDLSCHEDDORGULE_URL_METHOD = "prplschedule/claimSubmissionOrReassignment.do";
    public static final String INSCOMCODE = "DHIC";
    public static final String INSCOMPANY = "鼎和财产保险股份有限公司";
    

	@Override
	public PrpLScheduleDefLossVo findScheduleDefLossByPk(Long id) {
		PrpLScheduleDefLossVo scheduleDefLossVo = new PrpLScheduleDefLossVo();
		PrpLScheduleDefLoss scheduleDefLoss = databaseDao.findByPK(PrpLScheduleDefLoss.class,id);
		Beans.copy().from(scheduleDefLoss).to(scheduleDefLossVo);
		
		return scheduleDefLossVo;
	}

	@Override
	public PrpLScheduleTaskVo findScheduleTaskVoByPK(Long id) {
		PrpLScheduleTaskVo prpLScheduleTaskVo =null;
		PrpLScheduleTask prpLScheduleTask = databaseDao.findByPK(PrpLScheduleTask.class,id);
		if(prpLScheduleTask != null){
			prpLScheduleTaskVo=Beans.copyDepth().from(prpLScheduleTask).to(PrpLScheduleTaskVo.class);
		}
		return prpLScheduleTaskVo;
	}

	@Override
	public Long saveScheduleTaskByVo(PrpLScheduleTaskVo personScheduleTaskVo) {
		PrpLScheduleTask po = Beans.copyDepth().from(personScheduleTaskVo).to(PrpLScheduleTask.class);
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
		if(po.getId()!= null){
		    databaseDao.update(PrpLScheduleTask.class,po);
		}else {
	        databaseDao.save(PrpLScheduleTask.class,po);
        }
		return po.getId();
	}

	/**
	 * 根据报案号、车辆序号查询出唯一的定损车辆
	 */
	@Override
	public Long findCarDefLoss(String registNo,Integer serialNo,String scheduleStatus) {
		Long scheduleDefLossId = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("serialNo",serialNo);
		queryRule.addEqual("scheduleStatus",scheduleStatus);
		queryRule.addEqual("deflossType","1");
		PrpLScheduleDefLoss scheduleDefLoss=databaseDao.findUnique(PrpLScheduleDefLoss.class,queryRule);
		if(scheduleDefLoss!=null){
			scheduleDefLossId = scheduleDefLoss.getId();
		}
		return scheduleDefLossId;
	}
	
	public boolean isExistDefLossTask(String registNo,Integer serialNo,String deflossType,String... scheduleStatus){
		boolean returnVal = false;//默认不存在
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("serialNo",serialNo);
		if (scheduleStatus != null && scheduleStatus.length > 0) {
			queryRule.addIn("scheduleStatus",scheduleStatus);
		}
		queryRule.addEqual("deflossType",deflossType);
		List<PrpLScheduleDefLoss> defLossPoList = databaseDao.findAll(PrpLScheduleDefLoss.class,queryRule);
		if (defLossPoList != null && !defLossPoList.isEmpty()) {
			returnVal = true;//
		}
		return returnVal;
	}

	//查询是否有标的车的定损数据
	@Override
	public PrpLScheduleDefLossVo findCarDefLossBySerialNo(String registNo,Integer serialNo) {
		PrpLScheduleDefLossVo schDefLossVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("serialNo",serialNo);
		List<PrpLScheduleDefLoss> schDefLossPoList = databaseDao.findAll
				(PrpLScheduleDefLoss.class,queryRule);
		if(schDefLossPoList==null||schDefLossPoList.size()==0){
			return null;
		}
		schDefLossVo = Beans.copyDepth().from(schDefLossPoList.get(0)).to
				(PrpLScheduleDefLossVo.class);
		return schDefLossVo;
	}

	/**
	 * 更新定损信息
	 */
	@Override
	public void updateScheduleDefLoss(PrpLScheduleDefLossVo scheduleDefLossVo) {
		PrpLScheduleDefLoss scheduleDefLossPo=databaseDao.findByPK
				(PrpLScheduleDefLoss.class,scheduleDefLossVo.getId());
		Beans.copy().from(scheduleDefLossVo).excludeNull().to(scheduleDefLossPo);
		databaseDao.update(PrpLScheduleDefLoss.class,scheduleDefLossPo);
	}

	@Override
	public Long findTaskIdByDefLossId(Long id) {
		Long scheduleTaskId = null;
		PrpLScheduleDefLoss scheduleDefLossPo = databaseDao.findByPK(PrpLScheduleDefLoss.class,id);
		if(scheduleDefLossPo!=null){
			scheduleTaskId = scheduleDefLossPo.getPrpLScheduleTask().getId();
		}
		return scheduleTaskId;
	}

	@Override
	public List<PrpLScheduleDefLossVo> findPrpLScheduleDefLossList(
			String registNo) {
		List<PrpLScheduleDefLossVo> prpLScheduleDefLossVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLScheduleDefLoss> prpLScheduleDefLossPoList = databaseDao.findAll(PrpLScheduleDefLoss.class,queryRule);
		if(prpLScheduleDefLossPoList != null && prpLScheduleDefLossPoList.size() > 0){
			prpLScheduleDefLossVoList = Beans.copyDepth().from(prpLScheduleDefLossPoList).toList(PrpLScheduleDefLossVo.class);
		}
		return prpLScheduleDefLossVoList;
	}

	@Override
	public PrpLScheduleTaskVo getScheduleTask(String registNo,String status) {
		PrpLScheduleTaskVo scheduleTaskVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("scheduleStatus",status);
		List<PrpLScheduleTask> scheduleTaskPo = databaseDao.findAll(PrpLScheduleTask.class,queryRule);
		if(scheduleTaskPo!=null&&scheduleTaskPo.size()>0){
			scheduleTaskVo = Beans.copyDepth().from(scheduleTaskPo.get(0)).to(PrpLScheduleTaskVo.class);
		}
		return scheduleTaskVo;
	}

	/**
	 * 查勘提交失败，工作流回滚，需删除已生成的定损数据
	 */
	@Override
	public void rollBackDefLossTask(PrpLScheduleTaskVo taskVo) {
		//PrpLScheduleTaskVo taskVo = findScheduleTaskVoByPK(scheduleTaskId);
		List<PrpLScheduleDefLossVo> lossVo = taskVo.getPrpLScheduleDefLosses();
		if (lossVo != null && !lossVo.isEmpty()) {
			for (PrpLScheduleDefLossVo loss : lossVo) {
				if ("0".equals(loss.getSourceFlag())) {
					databaseDao.deleteByPK(PrpLScheduleDefLoss.class,loss.getId());
				}
			}
		}
//		QueryRule queryRule = QueryRule.getInstance();
//		queryRule.addEqual("registNo",registNo);
//		queryRule.addEqual("scheduleStatus","5");
//		queryRule.addEqual("sourceFlag","0");
//		List<PrpLScheduleDefLoss> list = null;
//		list = databaseDao.findAll(PrpLScheduleDefLoss.class,queryRule);
//		if(list!=null&&list.size()>0){
//			for(PrpLScheduleDefLoss loss : list){
//				databaseDao.deleteByPK(PrpLScheduleDefLoss.class,loss.getId());
//			}
//			Long taskId = list.get(0).getPrpLScheduleTask().getId();
//			databaseDao.deleteByPK(PrpLScheduleTask.class,taskId);
//		}
	}

	@Override
	public List<PrpLScheduleDefLossVo> findScheduleDefLossByCheck(String registNo) {
		// TODO Auto-generated method stub
		List<PrpLScheduleDefLossVo> defLossVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("scheduleStatus","5");
		queryRule.addEqual("sourceFlag","0");
		List<PrpLScheduleDefLoss> defLossPoList = databaseDao.findAll(PrpLScheduleDefLoss.class,queryRule);
		if(defLossPoList != null && defLossPoList.size() > 0){
			defLossVoList = Beans.copyDepth().from(defLossPoList).toList(PrpLScheduleDefLossVo.class);
		}
		return defLossVoList;
	}
	
	@Override
	public List<PrpLScheduleDefLossVo> findScheduleDefLossList(String registNo,String deflossType){
		List<PrpLScheduleDefLossVo> defLossVoList = new ArrayList<PrpLScheduleDefLossVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addSql(" flag is null ");
		if(StringUtils.isNotBlank(deflossType)){
			queryRule.addEqual("deflossType",deflossType);
		}
		List<PrpLScheduleDefLoss> defLossPoList = databaseDao.findAll(PrpLScheduleDefLoss.class,queryRule);
		if(defLossPoList != null && defLossPoList.size() > 0){
			defLossVoList = Beans.copyDepth().from(defLossPoList).toList(PrpLScheduleDefLossVo.class);
		}
		return defLossVoList;
	}

	@Override
	public List<PrpLScheduleTaskVo> getScheduleTaskByregistNo(String registNo) {
		List<PrpLScheduleTaskVo> scheduleTaskVoList = new ArrayList<PrpLScheduleTaskVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLScheduleTask> scheduleTaskPo = databaseDao.findAll(PrpLScheduleTask.class,queryRule);
		if(scheduleTaskPo!=null&&scheduleTaskPo.size()>0){
			scheduleTaskVoList = Beans.copyDepth().from(scheduleTaskPo).toList(PrpLScheduleTaskVo.class);
		}
		return scheduleTaskVoList;
	}

    @Override
    public void reassignmentes(PrpLScheduleTaskVo prpLScheduleTaskVo,String schType,List<PrpLScheduleDefLossVo> prpLScheduleDefLosses,String url) throws Exception {

        
        //获取报案信息
        PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLScheduleTaskVo.getRegistNo());
        PrpLCMainVo vo = prpLCMainService.findPrpLCMain(prpLScheduleTaskVo.getRegistNo(), prpLRegistVo.getPolicyNo());

        
        
        HandleScheduleDOrGReqVo reqVo = new HandleScheduleDOrGReqVo();
        
        HeadReq head = new HeadReq();//设置头部信息
        head.setRequestType("ScheduleSubmit");
        head.setPassword("mclaim_psd");
        head.setUser("mclaim_user");
        
        HandleScheduleReqDOrGBody body = new HandleScheduleReqDOrGBody();
        HandleScheduleReqScheduleDOrG scheduleDOrG = new HandleScheduleReqScheduleDOrG();
        PrpDScheduleDOrGMainVo prpDScheduleDOrGMainVo = new PrpDScheduleDOrGMainVo();
        List<PrpLCMainVo> prpLCMainVoList = policyViewService.getPolicyAllInfo(prpLRegistVo.getRegistNo());
        prpDScheduleDOrGMainVo.setScheduleDOrG(scheduleDOrG);
        prpDScheduleDOrGMainVo.setPrpLCMainVoList(prpLCMainVoList);
        prpDScheduleDOrGMainVo.setPrpLRegistVo(prpLRegistVo);
        prpDScheduleDOrGMainVo.setPrpLScheduleTaskVo(prpLScheduleTaskVo);
        prpDScheduleDOrGMainVo.setSchType(schType);
        scheduleDOrG = scheduleTaskService.setScheduleDOrG(prpDScheduleDOrGMainVo);
		
		PrpLScheduleTaskVo selfScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(prpLRegistVo.getRegistNo());
        scheduleDOrG.setCaseFlag("3");
        scheduleDOrG.setOrderNo(prpLRegistVo.getPrpLRegistExt().getOrderNo());
        if("0".equals(prpLRegistVo.getSelfRegistFlag()) &&
        		"1".equals(selfScheduleTaskVo.getIsAutoCheck())){//电话直赔
        	scheduleDOrG.setIsMobileCase("0");
        	scheduleDOrG.setCaseFlag("1");
        }else if("1".equals(prpLRegistVo.getSelfRegistFlag()) &&
        		"1".equals(selfScheduleTaskVo.getIsAutoCheck())){//微信自助理赔
        	scheduleDOrG.setIsMobileCase("0");
        	scheduleDOrG.setCaseFlag("2");
        }else{
            //是否移动端案件
            PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.MobileCheck,vo.getComCode());
    		Boolean isMobileCase = false;
    		Boolean  isMobileWhileListCase = wFMobileService.findWhileListCase(CodeConstants.WhiteList.ISMOBILE,prpLScheduleTaskVo.getScheduledUsercode());
    		if(isMobileWhileListCase){//移动端案件，不送民太安车童接口
    			isMobileCase = true;
    		}else{
    			isMobileCase = sendMsgToMobileService.isMobileCase(prpLRegistVo, prpLScheduleTaskVo.getScheduledUsercode());
    		}
            if("1".equals(configValueVo.getConfigValue())){
              	if(isMobileCase){//移动端案件
                      scheduleDOrG.setIsMobileCase("1");
                  }else{
                      scheduleDOrG.setIsMobileCase("0");
                  }
    		}else{
              scheduleDOrG.setIsMobileCase("0");
		    }
        }
        //任务id
        int id = 1;
        List<HandleScheduleReqScheduleItemDOrG> scheduleItemList = new ArrayList<HandleScheduleReqScheduleItemDOrG>();
        for(PrpLScheduleDefLossVo ItemsVo :prpLScheduleDefLosses){
            HandleScheduleReqScheduleItemDOrG scheduleItemDOrG =new HandleScheduleReqScheduleItemDOrG();
            //传工作流taskid
            String deflossType = ItemsVo.getDeflossType();
            String subNodeCode = FlowNode.DLCar.toString();
            if("1".equals(deflossType)){
                subNodeCode = FlowNode.DLCar.toString();
            }else{
                subNodeCode = FlowNode.DLProp.toString();
            }
            List<PrpLWfTaskVo> PrpLWfTaskVos = wfTaskHandleService.findInTask(prpLScheduleTaskVo.getRegistNo(),String.valueOf(ItemsVo.getId()),subNodeCode);
            if(PrpLWfTaskVos!=null && PrpLWfTaskVos.size()>0){
                scheduleItemDOrG.setTaskId(String.valueOf(PrpLWfTaskVos.get(0).getTaskId()));
            }else{
                scheduleItemDOrG.setTaskId(String.valueOf(id++));
            }
            
            //scheduleItemDOrG.setTaskId(String.valueOf(id++));
            scheduleItemDOrG.setSerialNo(String.valueOf(ItemsVo.getSerialNo()));
            if(ItemsVo.getDeflossType().equals("1")){
                scheduleItemDOrG.setNodeType("DLCar");
                if(ItemsVo.getLossitemType().equals("0")){
                    scheduleItemDOrG.setIsObject("0");
                    scheduleItemDOrG.setItemNo("0");
                    scheduleItemDOrG.setItemName("地面");
                }else if(ItemsVo.getLossitemType().equals("1")){
                    scheduleItemDOrG.setIsObject("1");
                    scheduleItemDOrG.setItemNo("1");
                    scheduleItemDOrG.setItemName(ItemsVo.getItemsContent());
                }else{
                    scheduleItemDOrG.setIsObject("0");
                    scheduleItemDOrG.setItemNo("2");
                    scheduleItemDOrG.setItemName(ItemsVo.getItemsContent());
                }
            }else if(ItemsVo.getDeflossType().equals("2")){
                scheduleItemDOrG.setNodeType("DLProp");
                if(ItemsVo.getLossitemType().equals("0")){
                    scheduleItemDOrG.setIsObject("0");
                    scheduleItemDOrG.setItemNo("0");
                    scheduleItemDOrG.setItemName("地面");
                }else if(ItemsVo.getLossitemType().equals("1")){
                    scheduleItemDOrG.setIsObject("1");
                    scheduleItemDOrG.setItemNo("1");
                    scheduleItemDOrG.setItemName(ItemsVo.getItemsContent());
                }else{
                    scheduleItemDOrG.setIsObject("0");
                    scheduleItemDOrG.setItemNo("2");
                    scheduleItemDOrG.setItemName(ItemsVo.getItemsContent());
                }
            }
            
            if("3".equals(scheduleDOrG.getCaseFlag())){//案件标识为普通案件的时候，才传值
            	 if(StringUtils.isNotBlank(ItemsVo.getScheduledUsercode())){
                     String scheduledUsercode[] = ItemsVo.getScheduledUsercode().split(",");
                     scheduleItemDOrG.setNextHandlerCode(scheduledUsercode[0]);
                     //缓存取不到值时再查一下表
                     String  nextHandlerName= CodeTranUtil.transCode("UserCode", scheduledUsercode[0]);
                     if(StringUtils.isNotBlank(nextHandlerName) && nextHandlerName.equals(scheduledUsercode[0])){
                         SysUserVo sysUserVo = scheduleTaskService.findPrpduserByUserCode(scheduledUsercode[0],"");
                         scheduleItemDOrG.setNextHandlerName(sysUserVo.getUserName());
                     }else{
                         scheduleItemDOrG.setNextHandlerName(nextHandlerName);
                     }
                 }
                 String  comCodeName= CodeTranUtil.transCode("ComCode", ItemsVo.getScheduledComcode());
                 scheduleItemDOrG.setScheduleObjectId(ItemsVo.getScheduledComcode());
                 scheduleItemDOrG.setScheduleObjectName(comCodeName);
			}else{
				scheduleItemDOrG.setNextHandlerCode("");
				scheduleItemDOrG.setNextHandlerName("");
				scheduleItemDOrG.setScheduleObjectId("");
				scheduleItemDOrG.setScheduleObjectName("");
			}
            scheduleItemList.add(scheduleItemDOrG);
        }
        scheduleDOrG.setScheduleItemList(scheduleItemList);
        body.setScheduleDOrG(scheduleDOrG);
        reqVo.setHead(head);
        reqVo.setBody(body);
        url = url+HANDLSCHEDDORGULE_URL_METHOD;
        mobileCheckService.getHandelScheduleDOrDUrl(reqVo,url);
    
    
    }
    
	
    @Override
    public List<PrpLScheduleDefLossVo> findPrpLScheduleCarLossList(
            String registNo) {
        List<PrpLScheduleDefLossVo> prpLScheduleDefLossVoList = null;
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        queryRule.addNotEqual("scheduleStatus","9");
        queryRule.addNotEqual("lossitemType","0");
        List<PrpLScheduleDefLoss> prpLScheduleDefLossPoList = databaseDao.findAll(PrpLScheduleDefLoss.class,queryRule);
        if(prpLScheduleDefLossPoList != null && prpLScheduleDefLossPoList.size() > 0){      
            prpLScheduleDefLossVoList = Beans.copyDepth().from(prpLScheduleDefLossPoList).toList(PrpLScheduleDefLossVo.class);
        }
        return prpLScheduleDefLossVoList;
    }

    @Override
    public PrpLScheduleTaskVo findScheduleTaskByOther(String registNo,String isPersonFlag,String scheduleType) {
        PrpLScheduleTaskVo scheduleTaskVo = null;
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        queryRule.addNotEqual("isPersonFlag",isPersonFlag);
        queryRule.addEqual("scheduleType",scheduleType);
        queryRule.addDescOrder("createTime");
        List<PrpLScheduleTask> scheduleTaskPo = databaseDao.findAll(PrpLScheduleTask.class,queryRule);
        if(scheduleTaskPo != null && scheduleTaskPo.size() > 0){
            scheduleTaskVo = Beans.copyDepth().from(scheduleTaskPo.get(0)).to(PrpLScheduleTaskVo.class);
        }else{
        	//如果查勘已提交，ScheduleType会变成2
        	QueryRule queryRule2 = QueryRule.getInstance();
        	queryRule2.addEqual("registNo",registNo);
        	queryRule2.addNotEqual("isPersonFlag",isPersonFlag);
        	queryRule2.addEqual("scheduleType",CodeConstants.ScheduleType.DEFLOSS_SCHEDULE);
        	queryRule2.addDescOrder("createTime");
        	List<PrpLScheduleTask> scheduleTaskPo2 = databaseDao.findAll(PrpLScheduleTask.class,queryRule2);
        	if(scheduleTaskPo2!=null && scheduleTaskPo2.size()>0){
        		scheduleTaskVo = Beans.copyDepth().from(scheduleTaskPo2.get(0)).to(PrpLScheduleTaskVo.class);
        	}
        }
        return scheduleTaskVo;
    }
    
    @Override
    public List<PrpLScheduleTaskVo> findScheduleTask(String registNo,String isPersonFlag,String scheduleType){
    	List<PrpLScheduleTaskVo> scheduleTaskVoList = new ArrayList<PrpLScheduleTaskVo>();
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        queryRule.addEqual("isPersonFlag",isPersonFlag);
        queryRule.addEqual("scheduleType",scheduleType);
        queryRule.addDescOrder("createTime");
        List<PrpLScheduleTask> scheduleTaskPo = databaseDao.findAll(PrpLScheduleTask.class,queryRule);
        if(scheduleTaskPo != null && scheduleTaskPo.size() > 0){
        	scheduleTaskVoList = Beans.copyDepth().from(scheduleTaskPo).toList(PrpLScheduleTaskVo.class);
        }
        return scheduleTaskVoList;
    }

	@Override
	public String findSelfCheckPower(String userCode) {
		//判断当前工号有无自助查勘岗的Id----5195
		List<SaaGradeVo> saaGradeVoList = saaUserGradeService.findUserGrade(userCode);
		String powerFlag = IsSingleAccident.NOT;
		if(saaGradeVoList != null && saaGradeVoList.size() > 0){
			for(SaaGradeVo saaGradeVo:saaGradeVoList){
				if("5195".equals(saaGradeVo.getId().toString())){
					powerFlag = IsSingleAccident.YES;
					break;
				}
			}
		}
		return powerFlag;
	}

	@Override
	public PrpLScheduleDefLossVo findCarDefLossByLicenseNo(String registNo,String licenseNo) {
		PrpLScheduleDefLossVo prpLScheduleDefLossVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("itemsContent",licenseNo);
		List<PrpLScheduleDefLoss> prpLScheduleDefLossList = databaseDao.findAll(PrpLScheduleDefLoss.class,queryRule);
		if(prpLScheduleDefLossList != null && prpLScheduleDefLossList.size()>0){
			prpLScheduleDefLossVo=new PrpLScheduleDefLossVo();
			prpLScheduleDefLossVo=Beans.copyDepth().from(prpLScheduleDefLossList.get(0)).to(PrpLScheduleDefLossVo.class);
		}
		return prpLScheduleDefLossVo;
	}
    
}
