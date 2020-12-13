/******************************************************************************

 * CREATETIME : 2015年12月16日 下午6:24:00
 ******************************************************************************/
package ins.sino.claimcar.schedule.web.action;

import freemarker.core.ParseException;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.saa.service.facade.SaaUserGradeService;
import ins.platform.saa.vo.SaaGradeVo;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.DataUtils;
import ins.platform.utils.DateUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysAreaDictVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.IsSingleAccident;
import ins.sino.claimcar.CodeConstants.ScheduleType;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.carchild.service.CarchildService;
import ins.sino.claimcar.carchild.vo.PrplCarchildScheduleVo;
import ins.sino.claimcar.carchild.vo.RegistInformationVo;
import ins.sino.claimcar.carchild.vo.RevokeBodyVo;
import ins.sino.claimcar.carchild.vo.RevokeInfoReqVo;
import ins.sino.claimcar.carchild.vo.RevokeInfoResVo;
import ins.sino.claimcar.carchild.vo.RevokeTaskInfoVo;
import ins.sino.claimcar.carchildCommon.vo.CarchildHeadVo;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimSummaryService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpsmsMessageVo;
import ins.sino.claimcar.common.filter.Timed;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WFMobileService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfFlowService;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.hnbxrest.service.QuickUserService;
import ins.sino.claimcar.hnbxrest.vo.PrplQuickUserVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossprop.service.PropLossService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.mobilecheck.service.MobileCheckService;
import ins.sino.claimcar.mobilecheck.service.SendMsgToMobileService;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneBodyReq;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneHeadReq;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneOutdateReq;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneReqVo;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneResVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleDOrGBackReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleDOrGReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqDOrGBody;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleDOrG;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleItemDOrG;
import ins.sino.claimcar.mobilecheck.vo.HeadReq;
import ins.sino.claimcar.other.service.MsgModelService;
import ins.sino.claimcar.regist.service.FounderCustomService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.service.ScheduleHandlerService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPersonLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpDScheduleDOrGMainVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTasklogVo;
import ins.sino.claimcar.schedule.web.utils.RabbitUtil;
import ins.sino.claimcar.sms.service.SmsService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * <pre></pre>
 * @author ★Luwei
 */
@Controller
@RequestMapping(value = "/schedule")
public class ScheduleAction {

    private static Logger logger = LoggerFactory.getLogger(ScheduleAction.class);
    private static final String CT_02="dhDockingService.cancelCase";
    public static final String INSCOMCODE = "DHIC";
	public static final String INSCOMPANY = "鼎和财产保险股份有限公司";
    
	@Autowired
	ScheduleTaskService scheduleTaskService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	ScheduleHandlerService scheduleHandlerService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	RegistService registService;
	@Autowired
	CheckTaskService checkService;
	@Autowired
	ClaimTaskService claimService;
	@Autowired
	PersTraceDubboService persTraceDubboService;
	@Autowired
	FounderCustomService founderService;
	@Autowired
	PrpLCMainService prpLCMainService;
	@Autowired
	MobileCheckService mobileCheckService;
	@Autowired
	EndCasePubService endCasePubService;
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	AreaDictService areaDictService;
	@Autowired
	SaaUserGradeService saaUserGradeService;
	@Autowired
	SysUserService sysUserService;
	@Autowired
	CodeTranService codeTranService;

	@Autowired
	WfMainService wfMainService;
	@Autowired
	SmsService smsService;	
	@Autowired
    private SendMsgToMobileService sendMsgToMobileService;
	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	@Autowired
	MsgModelService msgModelService;
	@Autowired
	QuickUserService quickUserService;
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	ManagerService managerService;
	@Autowired
	CarchildService carchildService;
	@Autowired
    PayCustomService payCustomService;
	@Autowired
    ClaimSummaryService claimSummaryService;
	@Autowired
	ClaimInterfaceLogService claimInterfaceLogService;

	@Autowired
    LossCarService lossCarService;
    @Autowired
    PropLossService propLossService;

    @Autowired
    WfFlowService wfFlowService;
    @Autowired
    WfTaskQueryService wfTaskQueryService;
    @Autowired
    CheckHandleService checkHandleService;
    @Autowired
    private WFMobileService wFMobileService;

	public static final String HANDLSCHEDDORGULE_URL_METHOD = "prplschedule/claimSubmissionOrReassignment.do";
	
	/**
	 * <pre>
	 * 调度处理
	 * </pre>
	 * @modified:
	 */
	//
	@Timed("调度处理")
	@RequestMapping(value = "/scheduleTaskList.do")
	@ResponseBody
	public ModelAndView scheduleTaskList(Model model) {
		ModelAndView mv = new ModelAndView();
		Date nowDate = new Date();
		Date monthAgoDate = DateUtils.addMonths(nowDate,-1);
		mv.addObject("taskInStartTime",monthAgoDate);
		mv.addObject("taskInEndTime",nowDate);
		mv.addObject("registStartTime",monthAgoDate);
		mv.addObject("registEndTime",nowDate);
		mv.setViewName("schedule/scheduleQuery/scheduleTaskQuery");
		return mv;
	}

	/**
	 * <pre>
	 * 已调度未接收任务查询
	 * </pre>
	 * @modified:
	 */
	@RequestMapping(value = "/sheduledUnRecQueryList.do")
	@ResponseBody
	public ModelAndView scheduleTaskQuery(Model model) {
		ModelAndView mv = new ModelAndView();
		Date nowDate = new Date();
		Date monthAgoDate = DateUtils.addMonths(nowDate,-1);
		mv.addObject("taskInStartTime",monthAgoDate);
		mv.addObject("taskInEndTime",nowDate);
		mv.addObject("registStartTime",monthAgoDate);
		mv.addObject("registEndTime",nowDate);
		mv.setViewName("schedule/scheduleQuery/sheduledUnRecQuery");
		return mv;
		
	}

	
	@RequestMapping(value = "/preScheduleEdit.do")
	public ModelAndView preScheduleEdit(Double flowTaskId,String registNo) {
		PrpLWfTaskVo taskVo=wfTaskHandleService.queryTask(flowTaskId);
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(taskVo.getRegistNo());
		String handStatus=taskVo.getHandlerStatus();
		if(StringUtils.equals(prpLRegistVo.getTempRegistFlag(), CodeConstants.TempReport.TEMPREPORT)){
		    return scheduleEdit(taskVo);
		}
		if("0".equals(handStatus)){
			return addCheckTask(taskVo);
		}else{
			return scheduleEdit(taskVo);
		}
	}
	
	/**
	 * <pre>
	 * 查询结果跳转到编辑页面
	 * </pre>
	 * @modified: ☆Luwei(2015年12月16日 下午6:27:15): <br>
	 */
	public ModelAndView scheduleEdit(PrpLWfTaskVo taskVo) {
		ModelAndView mv = new ModelAndView();
		// 获取报案时保单信息
		List<PrpLCMainVo> prpLCMains = policyViewService.getPolicyAllInfo(taskVo.getRegistNo());
		for (int i = 0; i < prpLCMains.size(); i ++) {
			if (StringUtils.equals(prpLCMains.get(i).getValidFlag(), CodeConstants.ValidFlag.INVALID)) {
				prpLCMains.remove(i);
				i --;
			}else{
				Map<String,String> businessClassCheckMsg = codeTranService.findCodeNameMap("BusinessClassCheckMsg");
				if(businessClassCheckMsg.containsKey(prpLCMains.get(i).getBusinessClass())){																					
					prpLCMains.get(i).setMemberFlag("1");
				}
			}
			
		}
		// 获取报案信息
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(taskVo.getRegistNo());
		// .zhubin添加自助理赔判断
		if(prpLRegistVo != null){
			if(prpLRegistVo.getSelfRegistFlag() == null){
				prpLRegistVo.setSelfRegistFlag("0");
			}
		}
		// 获取调度主信息，该信息在报案提交调度时已经保存，所以直接获取
		PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(taskVo.getRegistNo());
		// .zhubin添加自助查勘判断
		if(prpLScheduleTaskVo != null){
			if(prpLScheduleTaskVo.getIsAutoCheck() == null){
				prpLScheduleTaskVo.setIsAutoCheck("0");
			}
		}
		List<PrpLScheduleItemsVo> prpLScheduleItemses = scheduleTaskService.getPrpLScheduleItemsesVoByRegistNo(taskVo.getRegistNo());
		List<PrpLScheduleDefLossVo> prpLScheduleDefLosses = scheduleTaskService.getPrpLScheduleDefLossesVoByRegistNo(taskVo.getRegistNo());
		List<PrpLScheduleTasklogVo> prpLScheduleTasklogs = scheduleTaskService.findScheduleTaskLogsByRegistNo(taskVo.getRegistNo());
		// 车童网/民太安公估师轨迹信息
		List<PrplCarchildScheduleVo> prplCarchildScheduleVos = carchildService.findCarchildScheduleByRegistNo(taskVo.getRegistNo());
		if(prplCarchildScheduleVos!=null&&prplCarchildScheduleVos.size()>0){
            for(PrplCarchildScheduleVo prplCarchildScheduleVo:prplCarchildScheduleVos){
                if(FlowNode.Check.equals(prplCarchildScheduleVo.getNodeType())){
                    //PrpLScheduleTaskVo prpLScheduleTask = scheduleService.getScheduleTask(prplCarchildScheduleVo.getRegistNo(),ScheduleStatus.CHECK_SCHEDULED);
                    PrpLScheduleTaskVo prpLScheduleTask = scheduleService.findScheduleTaskByOther(prplCarchildScheduleVo.getRegistNo(),"1","1");
                    if(prpLScheduleTask == null){
                        prpLScheduleTask = scheduleService.findScheduleTaskByOther(prplCarchildScheduleVo.getRegistNo(),"1","2");
                    }
                    if(prpLScheduleTask != null){
                        prplCarchildScheduleVo.setLossCountent(prpLScheduleTask.getLossContent());
                        SysUserVo sysUserVo = scheduleTaskService.findPrpduserByUserCode(prpLScheduleTask.getScheduledUsercode(),"");
                        prplCarchildScheduleVo.setScheduleUserName(sysUserVo.getUserName());
                        prplCarchildScheduleVo.setScheduleUserPhone(sysUserVo.getMobile());
                    }

                }else if(FlowNode.DLCar.equals(prplCarchildScheduleVo.getNodeType()) ||
                        FlowNode.DLProp.equals(prplCarchildScheduleVo.getNodeType())){
                    PrpLScheduleDefLossVo vo = scheduleService.findScheduleDefLossByPk(Long.parseLong(prplCarchildScheduleVo.getTaskId()));
                    if(vo != null){
                        prplCarchildScheduleVo.setLossCountent(vo.getItemsContent());
                        SysUserVo sysUserVo = scheduleTaskService.findPrpduserByUserCode(vo.getScheduledUsercode(),"");
                        Long  taskId = scheduleService.findTaskIdByDefLossId(Long.parseLong(prplCarchildScheduleVo.getTaskId()));
                        PrpLScheduleTaskVo scheduleTaskVo = scheduleService.findScheduleTaskVoByPK(taskId);
                        prplCarchildScheduleVo.setScheduleUserName(sysUserVo.getUserName());
                        prplCarchildScheduleVo.setScheduleUserPhone(sysUserVo.getMobile());
                    }
                }
            }
        }
		
		// 如果是未处理任务，修改状态为正在处理
		if (StringUtils.equals(prpLScheduleTaskVo.getScheduleStatus(), CodeConstants.ScheduleStatus.NOT_SCHEDULED)) {
			scheduleHandlerService.updateScheduleStatus(prpLScheduleTaskVo);
		}
		// 校验当前案件是否已经提交查勘调度 1-是，0-否
		if (checkScheduleCheck(taskVo.getRegistNo())) {
			mv.addObject("checkScheduled", "1");
		} else {
			mv.addObject("checkScheduled", "0");
		}
		// 加管控，若所有立案已注销，或拒赔，应该也不显示调度新增等按钮
		List<PrpLClaimVo> vos = claimService.findClaimListByRegistNo(taskVo.getRegistNo());
		String claimCancel = "1";
		if(vos!=null && vos.size()>0){
			for(PrpLClaimVo vo : vos){
				if("1".equals(vo.getValidFlag())){
					claimCancel = "0";// 显示
					break;
				}
			}
		}else{
			claimCancel = "0";// 显示
		}
		if (checkPersonFinishes(taskVo.getRegistNo())){
			// 加报案注销了不能注销管控
			PrpLRegistVo registVo = registService.findRegistByRegistNo(taskVo.getRegistNo());
			if(CodeConstants.RegistTaskFlag.CANCELED.equals(registVo.getRegistTaskFlag())||"1".equals(claimCancel)){// 已经注销
				mv.addObject("checkPersonFinishes","0");// 不能编辑
			}else{
				mv.addObject("checkPersonFinishes","1");// 能编辑
			}
		} else {
			mv.addObject("checkPersonFinishes","0");// 不能编辑
		}
		if (checkScheduleDLoss(taskVo.getRegistNo())) {
			mv.addObject("dLossScheduled", "1");
		} else {
			mv.addObject("dLossScheduled", "0");
		}
		/*		//立案表都注销了 或者 都结案
				String endCase = "0";
				List<PrpLClaimVo> vos = claimService.findClaimListByRegistNo(taskVo.getRegistNo());
				if(vos.size()> 0 ){
					for(PrpLClaimVo vo : vos){
						if(vo.getEndCaseTime() != null || ("0").equals(vo.getCancelCode())){//立案结案或者注销
							endCase = "1";
						}
					}
				}*/
		/*	//判断是否能够发起调度taskin表没有理算数据 并且 prplcompensate 都核赔通过或者注销了   控制能否发起调度
					boolean state = wfTaskHandleService.existTaskByNode(registNo, FlowNode.Compe.getName());
					String states="";
					List<PrpLCompensateVo> vos = compensateTaskService.simpleQueryCompensate(registNo, "N");
					if(!state){
						for(PrpLCompensateVo vo:vos){
							if("1".equals(vo.getUnderwriteFlag())||"7".equals(vo.getUnderwriteFlag())){
								state = true;
							}
						}
					}
					if(state){
						states="0";//taskin表没有理算数据 并且 prplcompensate 都核赔通过或者注销了能发起
					}else{
						states="1";
					}*/
		boolean state = wfTaskHandleService.existTaskByNode(taskVo.getRegistNo(), FlowNode.Compe.toString());
		// 立案表都注销了 或者 都结案
		/*	String endCase = "0";
			if(state){
				List<PrpLClaimVo> vos = claimService.findClaimListByRegistNo(taskVo.getRegistNo());
				if(vos!=null&&vos.size()> 0 ){
					for(PrpLClaimVo vo : vos){
						if(vo.getEndCaseTime() != null){//未重开
							endCase = "1";
						}
					}
				}
			}*/
		String endCase = "1";
		/*	if(state){
				List<PrpLClaimVo> vos = claimService.findClaimListByRegistNo(taskVo.getRegistNo());
				if(vos!=null&&vos.size()> 0 ){
					for(PrpLClaimVo vo : vos){
						if(vo.getEndCaseTime() == null){//重开
							endCase = "0";
						}
					}
				}
			}*/
		// 判断当前工号有无处理调度的权限
		List<SaaGradeVo> saaGradeVoList = saaUserGradeService.findUserGrade(WebUserUtils.getUserCode());
		String powerFlag = "0";
		if(saaGradeVoList != null && saaGradeVoList.size() > 0){
			for(SaaGradeVo saaGradeVo:saaGradeVoList){
				if(FlowNode.Sched.getRoleCode().equals(saaGradeVo.getId().toString())){// 调度岗的ID是5003
					powerFlag = "1";
					break;
				}
			}
		}
		mv.addObject("powerFlag", powerFlag);
		mv.addObject("endCase", endCase);
		mv.addObject("prpLCMains", prpLCMains);
		mv.addObject("flowId", taskVo.getFlowId());
		mv.addObject("flowTaskId", taskVo.getTaskId());
		mv.addObject("nodeCode", taskVo.getNodeCode());
		// 脱敏处理
		if(CodeConstants.WorkStatus.END.equals(taskVo.getWorkStatus())){
			//reportorPhone
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);
			if(configValueVo!=null&&"1".equals(configValueVo.getConfigValue())){// 开关
				prpLRegistVo.setReportorPhone(DataUtils .replacePrivacy(prpLRegistVo.getReportorPhone()));
				prpLRegistVo.setLinkerMobile(DataUtils .replacePrivacy(prpLRegistVo.getLinkerMobile()));
				prpLRegistVo.setLinkerPhone(DataUtils .replacePrivacy(prpLRegistVo.getLinkerPhone()));
				for(PrpLScheduleTasklogVo vo: prpLScheduleTasklogs){
					vo.setLinkerManPhone(DataUtils .replacePrivacy(vo.getLinkerManPhone()));
				}
			}
			mv.addObject("prpLRegistVo", prpLRegistVo);
		}else{
			mv.addObject("prpLRegistVo", prpLRegistVo);
		}
		List<SysCodeDictVo> sysVos=checkHandleService.findByComCodeAndGradeid(prpLRegistVo.getComCode(),Long.valueOf(CodeConstants.Gradeid));
		mv.addObject("sysVos",sysVos);
		mv.addObject("prpLRegistExtVo", prpLRegistVo.getPrpLRegistExt());
		mv.addObject("prpLScheduleTaskVo", prpLScheduleTaskVo);
		mv.addObject("prpLScheduleItemses", prpLScheduleItemses);
		mv.addObject("prpLScheduleDefLosses", prpLScheduleDefLosses);
		mv.addObject("prplCarchildScheduleVos", prplCarchildScheduleVos);
		mv.addObject("prpLScheduleTasklogs", prpLScheduleTasklogs);
		mv.addObject("checkDate",new Date());
		mv.setViewName("schedule/scheduleEdit/ScheduleEdit");
		return mv;
	}

	/**
	 * <pre>
	 * 新增查勘
	 * </pre>
	 * @param taskVo
	 * @modified: ☆Luwei(2015年12月16日 下午6:27:15): <br>
	 */
	@Timed("新增查堪")
	@RequestMapping(value = "/addCheckTask.do")
	public ModelAndView addCheckTask(PrpLWfTaskVo taskVo) {
		ModelAndView mv = new ModelAndView();
		String userCode = WebUserUtils.getUserCode();
		String comCode = WebUserUtils.getComCode();
		
		PrpLScheduleTaskVo scheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(taskVo.getRegistNo());
		// .zhubin添加自助查勘判断
		if(scheduleTaskVo != null){
			if(scheduleTaskVo.getIsAutoCheck() == null){
				scheduleTaskVo.setIsAutoCheck("0");
			}
		}
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(taskVo.getRegistNo());
		// .zhubin添加自助理赔判断
		if(prpLRegistVo != null){
			if(prpLRegistVo.getSelfRegistFlag() == null){
				prpLRegistVo.setSelfRegistFlag("0");
			}
		}
		// 如果是未处理任务，修改状态为正在处理
		if (StringUtils.equals(scheduleTaskVo.getScheduleStatus(),
				CodeConstants.ScheduleStatus.NOT_SCHEDULED)) {
			scheduleHandlerService.updateScheduleStatus(scheduleTaskVo);
			Double taskId = taskVo.getTaskId().doubleValue();
			wfTaskHandleService.tempSaveTask(taskId,taskVo.getRegistNo(),userCode,comCode);
		}
		//
		String finshFlag = checkPersonFinish(taskVo.getRegistNo())?"1":"0";
		mv.addObject("checkPersonFinish",finshFlag);
		mv.addObject("handlerstatus", taskVo.getHandlerStatus());
		/*	if (checkPersonFinishes(taskVo.getRegistNo())){
				mv.addObject("checkPersonFinishes", "1");//能编辑
			} else {
				mv.addObject("checkPersonFinishes", "0");//不能编辑
			}*/
		String finshsFlag = checkPersonFinishs(taskVo.getRegistNo())?"1":"0";
		mv.addObject("checkPersonFinishs",finshsFlag);
		
		// 校验当前案件是否已经提交查勘调度 1-是，0-否
		if (checkScheduleCheck(taskVo.getRegistNo())) {
			mv.addObject("checkScheduled", "1");
			
		} else {
			mv.addObject("checkScheduled", "0");
			mv.addObject("prpLScheduleItemses", scheduleTaskVo.getPrpLScheduleItemses());
			
			Map<String,String> carNoMap = new HashMap<String,String>();
			carNoMap.put("地面","财损（地面）");
			carNoMap.put(prpLRegistVo.getPrpLRegistCarLosses().get(0).getLicenseNo(),
"财损（标的车"+prpLRegistVo.getPrpLRegistCarLosses().get(0)
					.getLicenseNo()+")");
			// 拼装财产损失，损失方
			if (prpLRegistVo.getPrpLRegistCarLosses().size() > 1) {
				for (int i = 1; i<prpLRegistVo.getPrpLRegistCarLosses().size();i++){
					carNoMap.put(prpLRegistVo.getPrpLRegistCarLosses().get(i).getLicenseNo(),
"财损（三者车"+prpLRegistVo.getPrpLRegistCarLosses().get(i)
							.getLicenseNo()+")");
				}
			}
			mv.addObject("carNoMap", carNoMap);
		}
	
		// 设置自定义区域编码
		//if(scheduleTaskVo.getSelfDefinareaCode()!=null&&scheduleTaskVo.getSelfDefinareaCode()!=""){
		List<PrpLScheduleTaskVo> scheduleTaskListVo = scheduleTaskService.findScheduleTaskListByRegistNo(taskVo.getRegistNo());
		PrpLScheduleTaskVo scheduleVo = new PrpLScheduleTaskVo();
		if(scheduleTaskListVo!=null&&scheduleTaskListVo.size()>0){
			for(PrpLScheduleTaskVo scheduleTask :scheduleTaskListVo){
				if("1".equals(scheduleTask.getTypes())){
					scheduleVo = scheduleTask;
				}
			}
		}
		if(!StringUtils.isNotEmpty(scheduleVo.getSelfDefinareaCode())){
			scheduleVo.setSelfDefinareaCode(prpLRegistVo.getSelfDefinareaCode());
		}
		mv.addObject("prpLScheduleVo", scheduleVo);
		mv.addObject("prpLScheduleTaskVo", scheduleTaskVo);
		mv.addObject("flowId", taskVo.getFlowId());
		mv.addObject("flowTaskId", taskVo.getTaskId());
		mv.addObject("userName", WebUserUtils.getUserName());
		mv.addObject("checkDate",new Date());
		mv.addObject("comCode", taskVo.getComCode());
		// 添加是否自助报案信息，回显
		mv.addObject("prpLRegistVo", prpLRegistVo);
		PrpLConfigValueVo configValueVoByMap = ConfigUtil.findConfigByCode(CodeConstants.SWITCHMAP);
		if(configValueVoByMap!=null&&"1".equals(configValueVoByMap.getConfigValue())){// 开关
			mv.addObject("switchMap","1");// 地图的开关
		}
		mv.addObject("oldClaim","0");// 默认不是旧理赔
		if("oldClaim".equals(prpLRegistVo.getFlag())){
			mv.addObject("oldClaim","1");// 旧理赔标识
		}
		// mv.addObject("switchMap","1");//地图的开关
		for(PrpLScheduleItemsVo vo : scheduleTaskVo.getPrpLScheduleItemses()){
			if(StringUtils.isNotBlank(vo.getScheduledUsercode())){
				SysUserVo sysUserVo = sysUserService.findByUserCode(vo.getScheduledUsercode());
				if(sysUserVo != null && StringUtils.isNotBlank(sysUserVo.getMobile())){
					vo.setMoblie(sysUserVo.getMobile());
				}
			}
		}
		// 添加联共保--地图开关，是否自定义按钮禁用
		if(CodeConstants.GBFlag.MINORCOMMON.equals(prpLRegistVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(prpLRegistVo.getIsGBFlag())){
			// 从共、从联案件
			mv.addObject("switchMap","1");// 地图禁用
		}
		// 是否自助查勘单选按钮锁定状态标志位 0-可以选，1-锁定 如果存在查勘任务则锁定
		String isAutoCheckFlag = "0";
		List<PrpLWfTaskVo> prpLWfTaskVos = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(taskVo.getRegistNo(),"Chk");
		if(prpLWfTaskVos != null && prpLWfTaskVos.size() > 0){
			isAutoCheckFlag = "1";
		}
		List<SysCodeDictVo> sysVos=checkHandleService.findByComCodeAndGradeid(prpLRegistVo.getComCode(),Long.valueOf(CodeConstants.Gradeid));
		mv.addObject("sysVos",sysVos);
		mv.addObject("isAutoCheckFlag",isAutoCheckFlag);
		mv.setViewName("schedule/scheduleEdit/SchAddCheck");
		return mv;
	}
	
	@RequestMapping(value = "/isQuickUser.ajax")
	@ResponseBody
	public AjaxResult isQuickUser(String registNo,String userCode){
	    String flag = "1";
	    AjaxResult ajaxResult = new AjaxResult();
	    PrpLRegistVo registVo= registService.findRegistByRegistNo(registNo);
	    if("1".equals(registVo.getIsQuickCase())){
            PrplQuickUserVo prplQuickUserVo = quickUserService.findQuickUserByUserCode(userCode);
            if(StringUtils.isBlank(prplQuickUserVo.getUserCode())){             
                flag = "0";
            }
        }
	    ajaxResult.setData(flag);
	    ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
	    return ajaxResult;
	}
	
	/**
	 * 调度查勘提交
	 * @throws Exception
	 */
	@Timed("调度查勘提交")
	@RequestMapping(value = "/checkSave.do")
	@ResponseBody
	public AjaxResult checkSave(String checkAreaCode,String lngXlatY,
			@FormModel(value = "submitVo") WfTaskSubmitVo submitVo,
			@FormModel(value = "prpLScheduleTaskVo") PrpLScheduleTaskVo prpLScheduleTaskVo,
			@FormModel(value = "prpLScheduleItemses") List<PrpLScheduleItemsVo> prpLScheduleItemses,
			@FormModel(value = "prpLRegistPersonLosses") List<PrpLRegistPersonLossVo> prpLRegistPersonLosses) throws Exception {
		
		boolean isPerSchedule = false;
		if(prpLScheduleItemses != null && !prpLScheduleItemses.isEmpty()){
			if(prpLScheduleItemses.size() == 1 && "4".equals(prpLScheduleItemses.get(0).getItemType())){
				prpLScheduleItemses.get(0).setSerialNo("-1");// 设置人伤序号-1
			    isPerSchedule = true;
			}
		}
		if( !isPerSchedule){// 排除单独调人伤
			// 已发起查勘任务，不能发起
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService.findPrpWfTaskVo(prpLScheduleTaskVo.getRegistNo(), FlowNode.Check.name(), FlowNode.Chk.name());
			if(prpLWfTaskVoList != null && !prpLWfTaskVoList.isEmpty()){
				throw new IllegalArgumentException("查勘任务已调出，请刷新页面！");
			}
		}
		PrpLScheduleTaskVo oldScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(prpLScheduleTaskVo.getRegistNo());
		
		Beans.copy().from(prpLScheduleTaskVo).excludeEmpty().excludeNull().to(oldScheduleTaskVo);
		String comcode = submitVo.getComCode();
		submitVo.setTaskInKey(prpLScheduleTaskVo.getRegistNo());
		if("".equals(policyViewService.getPolicyComCode(prpLScheduleTaskVo.getRegistNo()))){
			submitVo.setComCode(WebUserUtils.getComCode());
		}else{
			submitVo.setComCode(policyViewService.getPolicyComCode(prpLScheduleTaskVo.getRegistNo()));
		}
		
		//submitVo.setComCode(WebUserUtils.getComCode());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setSubmitType(SubmitType.N);
		
		
		// 写入通赔标志
		PrpLRegistVo registVo= registService.findRegistByRegistNo(prpLScheduleTaskVo.getRegistNo());
		/*if(prpLScheduleTaskVo.getScheduledComcode()!=null){*/
		/*//根据区代码查询机构
		String code = areaDictService.findAreaList("areaCode",registVo.getDamageAreaCode());*/
		String code = prpLScheduleTaskVo.getScheduledComcode();// 调度查勘员的机构
		// 调度地区
		
		List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
		prpLCMainVoList = policyViewService.getPolicyAllInfo(prpLScheduleTaskVo.getRegistNo());
		if(prpLCMainVoList.size() > 0 && prpLCMainVoList!=null){
			// 承保地区
			String comCode = "";
			if(prpLCMainVoList.size()==2){
				for(PrpLCMainVo vo:prpLCMainVoList){
					if(("12").equals(vo.getRiskCode().substring(0, 2))){
						comCode = vo.getComCode();
					}
				}
			}else{
				comCode = prpLCMainVoList.get(0).getComCode();
			}
			if(code != null && comCode!=""){
				if("0002".equals(code.substring(0,4))){// 深圳
					if(!code.substring(0, 4).equals(comCode.substring(0, 4))){
						
						registVo.setTpFlag("1");
						registVo.setIsoffSite("1");
						registService.saveOrUpdate(registVo);
					}
					
				}else{
					if(!code.substring(0, 2).equals(comCode.substring(0, 2))){
						registVo.setTpFlag("1");
						registVo.setIsoffSite("1");
						registService.saveOrUpdate(registVo);
					}
				}
			}
		}
		
		oldScheduleTaskVo.setTypes("1");
		// 回写人伤计数表--PrpLRegistPersonLoss
		if(prpLRegistPersonLosses!=null && prpLRegistPersonLosses.size()>0){
			PrpLRegistVo vo = registQueryService.findByRegistNo(prpLScheduleTaskVo.getRegistNo());
			vo.getPrpLRegistExt().setIsPersonLoss("1");
			if(vo.getPrpLRegistPersonLosses()!=null && vo.getPrpLRegistPersonLosses().size()>0){
				for(PrpLRegistPersonLossVo personLossVo : vo.getPrpLRegistPersonLosses()){
					for(PrpLRegistPersonLossVo registPersonLossVo : prpLRegistPersonLosses){
						if(personLossVo.getLossparty().equals(registPersonLossVo.getLossparty())){
							personLossVo.setInjuredcount(personLossVo.getInjuredcount()+registPersonLossVo.getInjuredcount());
							personLossVo.setDeathcount(personLossVo.getDeathcount()+registPersonLossVo.getDeathcount());
						}
					}
				}
				// 更新vo
				registService.saveOrUpdate(vo);
			}else{
				for(PrpLRegistPersonLossVo registPersonLossVo : prpLRegistPersonLosses){
					registPersonLossVo.setCreateTime(new Date());
					registPersonLossVo.setCreateUser(WebUserUtils.getUserCode());
					registPersonLossVo.setUpdateTime(new Date());
					registPersonLossVo.setUpdateUser(WebUserUtils.getUserCode());
				}
				vo.setPrpLRegistPersonLosses(prpLRegistPersonLosses);
				registService.saveOrUpdate(vo);
			}
			// 回写报案人伤标识
			
		}
		oldScheduleTaskVo.setScheduledTime(new Date());
		scheduleHandlerService.saveScheduleItemTask(oldScheduleTaskVo, prpLScheduleItemses, submitVo);
		// 如果是是否资助查勘案件，回写prplscheduletask，prplregist表是否自助查勘字段，是否自助理赔字段
		String selfClaimFlag = IsSingleAccident.NOT;
		if(!"1".equals(prpLScheduleTaskVo.getIsAutoCheckFlag())){
			if("1".equals(oldScheduleTaskVo.getIsAutoCheck())){
				selfClaimFlag = IsSingleAccident.YES;
				// prplregist表
				PrpLRegistVo rv= registService.findRegistByRegistNo(oldScheduleTaskVo.getRegistNo());
				rv.setSelfClaimFlag("1");
				rv.setSelfRegistFlag("0");
				registService.saveOrUpdate(rv);
				// prplscheduletask表
				PrpLScheduleTaskVo plstv = scheduleTaskService.findScheduleTaskByRegistNo(oldScheduleTaskVo.getRegistNo());
				plstv.setIsAutoCheck("1");
				scheduleTaskService.updatePrpLScheduleTaskVo(plstv);
			}
		}

		
		
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		
		
		// 调度信息送方正客服系统（调度查勘）
		try{
			//查询调度信息发送给智能外呼start
			List<Map<String,String>> list = policyViewService.getCaseInfo(prpLScheduleTaskVo.getRegistNo());
			for (int i = 0; i < list.size(); i++) {
				Map<String,String> map = list.get(i);
				StringBuilder sb = new StringBuilder();
				sb.append("{\"ID\":").append("\""+UUID.randomUUID().toString()+"\",");
				sb.append("\"PolicyNo\":").append("\""+map.get("PolicyNo")+"\",");
				sb.append("\"ClmNo\":").append("\""+map.get("ClmNo")+"\",");
				sb.append("\"LicenseNo\":").append("\""+map.get("LicenseNo")+"\",");
				sb.append("\"InsuredName\":").append("\""+map.get("InsuredName")+"\",");
				sb.append("\"ReporterName\":").append("\""+map.get("ReporterName")+"\",");
				sb.append("\"ReporterPhone\":").append("\""+map.get("ReporterPhone")+"\",");
				sb.append("\"LinkerName\":").append("\""+map.get("LinkerName")+"\",");
				sb.append("\"LinkerPhone\":").append("\""+map.get("LinkerPhone")+"\",");
				sb.append("\"ReportTime\":").append("\""+map.get("ReportTime")+"\",");
				sb.append("\"AccidentCourse\":").append("\""+map.get("AccidentCourse")+"\",");
				sb.append("\"DamageTime\":").append("\""+map.get("DamageTime")+"\",");
				sb.append("\"ExamineAddress\":").append("\""+map.get("ExamineAddress")+"\",");
				sb.append("\"FirstSiteFlag\":").append("\""+map.get("FirstSiteFlag")+"\",");
				sb.append("\"DamageCode\":").append("\""+map.get("DamageCode")+"\",");
				sb.append("\"SubCertiType\":").append("\""+map.get("SubCertiType")+"\",");
				sb.append("\"SelfClaimFlag\":").append("\""+map.get("SelfClaimFlag")+"\",");
				sb.append("\"SignTime\":").append("\""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\"}");
				RabbitUtil.send(sb.toString(), "DH_REPORT_VISIT","KF_REPORT_VISIT");
			}
			//调度信息发送给智能外呼end
			
			if(prpLScheduleItemses != null && prpLScheduleItemses.size() > 0){
				for(PrpLScheduleItemsVo vo : prpLScheduleItemses){
					if(StringUtils.equals(vo.getItemType(),"4")){
						oldScheduleTaskVo.setScheduleStatus("3");
						oldScheduleTaskVo.setIsPersonFlag("1");
						oldScheduleTaskVo.setPrpLScheduleItemses(prpLScheduleItemses);
					}
				}
			}
			interfaceAsyncService.scheduleInfoToFounder(oldScheduleTaskVo,ScheduleType.CHECK_SCHEDULE);
		}catch(Exception e){
			// TODO: handle exception
		}
		try {
			// 从共从联不请求移动查勘
			if(!CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag()) && !CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
				// 理赔调度提交/改派提交接口（理赔请求快赔系统）List<PrpLScheduleItemsVo> prpLScheduleItemses
				this.setReassignments(prpLScheduleTaskVo, checkAreaCode, lngXlatY,"Check",prpLScheduleItemses);
			}
		} catch (Exception e) {
			logger.info("移动查勘返回数据失败------>"+e.getMessage());
		}
		// 发送短信
		SysUserVo sysUserVo = WebUserUtils.getUser();
		scheduleHandlerService.sendMsg(prpLScheduleTaskVo,prpLScheduleItemses, sysUserVo,"1");
		
		// 新的一键呼出
		if(StringUtils.isNotBlank(prpLScheduleTaskVo.getCallNumber())){
			CallPhoneReqVo vo = new CallPhoneReqVo();
			CallPhoneResVo callPhoneResVo = new CallPhoneResVo();
			CallPhoneBodyReq callPhoneBodyReq = new CallPhoneBodyReq();
			CallPhoneHeadReq callPhoneHeadReq = new CallPhoneHeadReq();
			callPhoneHeadReq.setSystemCode("CC1007");
			CallPhoneOutdateReq callPhoneOutdateReq = new CallPhoneOutdateReq();
			callPhoneOutdateReq.setClmNo(prpLScheduleTaskVo.getRegistNo());
			callPhoneOutdateReq.setExaminePhone(prpLScheduleTaskVo.getCallNumber());
			callPhoneBodyReq.setOutdate(callPhoneOutdateReq);
			vo.setHead(callPhoneHeadReq);
			vo.setBody(callPhoneBodyReq);
			String url = SpringProperties.getProperty("FOUNDER_URL");
			if(StringUtils.isEmpty(url)){
				throw new Exception("未配置方正客服系统服务地址！");
			}
			try {
				callPhoneResVo = founderService.sendCallPhoneToFounder(vo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 自助理赔不送泛华或者民太安
		if(IsSingleAccident.YES.equals(selfClaimFlag) || IsSingleAccident.YES.equals(registVo.getSelfClaimFlag())){
			selfClaimFlag = IsSingleAccident.YES;
		}
		// 是否移动端案件
		Boolean isMobileWhileListCase = wFMobileService.findWhileListCase(CodeConstants.WhiteList.ISMOBILE, prpLScheduleTaskVo.getScheduledUsercode());
		// 从共丛联不请求车童赔伴
		if(!IsSingleAccident.YES.equals(selfClaimFlag) && !isMobileWhileListCase && !CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())
				&& !CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			// 判断查勘是否调度给泛华或者民太安
			PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByUserCode(prpLScheduleTaskVo.getScheduledUsercode());
			PrpLConfigValueVo configValueMTACheckVo = ConfigUtil.findConfigByCode(CodeConstants.MTACheck,registVo.getComCode());
			PrpLConfigValueVo configValueCTCheckVo = ConfigUtil.findConfigByCode(CodeConstants.CTCheck,registVo.getComCode());
			String cTMTACheck = "0";
			if(prplIntermMainVo!=null&&"0003".equals(prplIntermMainVo.getIntermCode())){
				if(configValueCTCheckVo!=null&&"1".equals(configValueCTCheckVo.getConfigValue())){
					cTMTACheck = "1";
				}
			}else if(prplIntermMainVo!=null&&"0005".equals(prplIntermMainVo.getIntermCode())){
				if(configValueMTACheckVo!=null&&"1".equals(configValueMTACheckVo.getConfigValue())){
					cTMTACheck = "1";
				}
			}
			if(prplIntermMainVo!=null&&"1".equals(cTMTACheck)){
				if("0003".equals(prplIntermMainVo.getIntermCode())||"0005".equals(prplIntermMainVo.getIntermCode())){
					RegistInformationVo registInformationVo = new RegistInformationVo();
					registInformationVo.setPrpLScheduleTaskVo(prpLScheduleTaskVo);
					registInformationVo.setScheduleItemVos(prpLScheduleItemses);
					registInformationVo.setSchType("Check");
					SysUserVo userVo = new SysUserVo();
					userVo.setComCode(WebUserUtils.getComCode());
					userVo.setUserCode(WebUserUtils.getUserCode());
					registInformationVo.setUser(userVo);
					List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(prpLScheduleTaskVo.getRegistNo(),
							FlowNode.Chk.toString());
					if(prpLWfTaskVoList!=null&&prpLWfTaskVoList.size()>0){
						// 流入时间降序排
						Collections.sort(prpLWfTaskVoList,new Comparator<PrpLWfTaskVo>() {

							@Override
							public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
								return o2.getTaskInTime().compareTo(o1.getTaskInTime());
							}
						});
						/*    Long  scheduleTaskId=Long.parseLong(prpLWfTaskVoList.get(0).getHandlerIdKey());
						Double taskId = submitVo.getFlowTaskId().doubleValue();
						PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTask(taskId);*/
					}
					List<PrpLWfTaskVo> prpLWfTaskVoResult = new ArrayList<PrpLWfTaskVo>();
					prpLWfTaskVoResult.add(prpLWfTaskVoList.get(0));
					carchildService.sendRegistInformation(prplIntermMainVo,registVo,prpLCMainVoList,prpLWfTaskVoResult,registInformationVo);
				}
			}
		}
		return ajaxResult;
	}
	
	
	/**
	 * <pre>
	 * 新增定损
	 * </pre>
	 * @modified: ☆Luwei(2015年12月16日 下午6:27:15): <br>
	 */
	@Timed("调度新增定损")
	@RequestMapping(value = "/addDeflossTask.do")
	@ResponseBody
	public ModelAndView addDeflossTask(PrpLWfTaskVo taskVo) {
		ModelAndView mv = new ModelAndView();
		int size = 0;
		PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(taskVo.getRegistNo());
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(taskVo.getRegistNo());
		// 校验当前案件查勘是否已经 1-是，0-否
		if (checkCheckSubmit(taskVo.getRegistNo())) {
			Map<Integer,String> carNoMap = new HashMap<Integer,String>();
			List<PrpLScheduleDefLossVo> defLossList = scheduleTaskService.getPrpLScheduleDefLossesVoByRegistNo(taskVo.getRegistNo());
			String thisCarNo = "";
			List<String> carNoList = new ArrayList<String>();
			for(PrpLScheduleDefLossVo prpLScheduleDefLossVo:defLossList){
				if(prpLScheduleDefLossVo.getDeflossType().equals("1") && (prpLScheduleDefLossVo.getScheduleStatus().equals(CodeConstants.ScheduleStatus.DEFLOSS_SCHEDULED)
||prpLScheduleDefLossVo.getScheduleStatus().equals(
						CodeConstants.ScheduleStatus.SCHEDULED_CHANGE) )){// 车损
					if(prpLScheduleDefLossVo.getSerialNo()==1){// 标的车
						thisCarNo = prpLScheduleDefLossVo.getItemsContent();
					}else{
						carNoList.add(prpLScheduleDefLossVo.getItemsContent());
						carNoMap.put(prpLScheduleDefLossVo.getSerialNo(),prpLScheduleDefLossVo.getItemsContent());
					}
				}
				if(prpLScheduleDefLossVo.getSerialNo()==2){// 调度过三者车
					size = size + 1;
				}
			}
			/*carNoMap.put("0","财损（地面）");
			carNoMap.put(prpLRegistVo.getPrpLRegistCarLosses().get(0).getLicenseNo(),"财损（标的车"+prpLRegistVo.getPrpLRegistCarLosses().get(0).getLicenseNo()+"）");
			String thisCarNo = prpLRegistVo.getPrpLRegistCarLosses().get(0).getLicenseNo();
			*/
			/*//拼装财产损失，损失方
			if (prpLRegistVo.getPrpLRegistCarLosses().size() > 1) {
				for (int i = 1; i<prpLRegistVo.getPrpLRegistCarLosses().size();i++){
					carNoMap.put(prpLRegistVo.getPrpLRegistCarLosses().get(i).getLicenseNo(),"财损（三者车"+prpLRegistVo.getPrpLRegistCarLosses().get(i).getLicenseNo()+"）");
					carNoList.add(prpLRegistVo.getPrpLRegistCarLosses().get(i).getLicenseNo());
				}
			}*/
			if(size>0){
				mv.addObject("sanZhe", "1");
			}else{
				mv.addObject("sanZhe", "0");
			}
			mv.addObject("carNoMap", carNoMap);
			mv.addObject("thisCarNo", thisCarNo);
			mv.addObject("carNoList", carNoList);
			mv.addObject("defLossSize", 0);
		} else {
			mv.addObject("checkSubmited", "0");
			String thisCarNo = "";
			List<String> carNoList = new ArrayList<String>();
			List<PrpLScheduleItemsVo> scheduleItemsVoList = scheduleTaskService.getPrpLScheduleItemsesVoByRegistNo(taskVo.getRegistNo());
			for(PrpLScheduleItemsVo vo:scheduleItemsVoList){
				if("2".equals(vo.getItemType())){
					size = size + 1;
				}
				if("1".equals(vo.getItemType())){// 标的车
					thisCarNo = vo.getItemsContent();
				}else if("2".equals(vo.getItemType())){
					carNoList.add(vo.getItemsContent());
				}
			}
			if(size>0){
				mv.addObject("sanZhe", "1");
			}else{
				mv.addObject("sanZhe", "0");
			}
			mv.addObject("thisCarNo", thisCarNo);
			mv.addObject("carNoList", carNoList);
			List<PrpLScheduleItemsVo> itemVoList = scheduleTaskService.getPrpLScheduleItemsesVoByRegistNo(taskVo.getRegistNo());
			// 过滤掉报案带过来的财产定损
			List<PrpLScheduleDefLossVo> prpLScheduleDefLosses = supplementInfo(itemVoList, taskVo.getRegistNo());
			mv.addObject("prpLScheduleDefLosses", prpLScheduleDefLosses);
			mv.addObject("defLossSize", prpLScheduleDefLosses.size());
		}
		List<PrpLScheduleTaskVo> scheduleTaskListVo = scheduleTaskService.findScheduleTaskListByRegistNo(taskVo.getRegistNo());
		PrpLScheduleTaskVo scheduleVo = new PrpLScheduleTaskVo();
		if(scheduleTaskListVo!=null&&scheduleTaskListVo.size()>0){
			for(PrpLScheduleTaskVo scheduleTask :scheduleTaskListVo){
				if("2".equals(scheduleTask.getTypes())){
					scheduleVo = scheduleTask;
				}
			}
		}
		// 设置自定义区域编码
		if(!StringUtils.isNotEmpty(scheduleVo.getSelfDefinareaCode())){
			scheduleVo.setSelfDefinareaCode(prpLRegistVo.getSelfDefinareaCode());
		}
		mv.addObject("prpLScheduleVo", scheduleVo);
		mv.addObject("registExt", prpLRegistVo.getPrpLRegistExt());
		
		if(StringUtils.isNotBlank(prpLScheduleTaskVo.getScheduledUsercode())){
			SysUserVo sysUserVo = sysUserService.findByUserCode(prpLScheduleTaskVo.getScheduledUsercode());
			if(StringUtils.isNotBlank(sysUserVo.getMobile())){
				mv.addObject("mobile", sysUserVo.getMobile());
			}
		}
			
		mv.addObject("prpLScheduleTaskVo", prpLScheduleTaskVo);
		mv.addObject("flowId", taskVo.getFlowId());
		mv.addObject("flowTaskId", taskVo.getTaskId());
		mv.addObject("userName", WebUserUtils.getUserName());
		PrpLConfigValueVo configValueVoByMap = ConfigUtil.findConfigByCode(CodeConstants.SWITCHMAP);
		if(configValueVoByMap!=null&&"1".equals(configValueVoByMap.getConfigValue())){// 开关
			mv.addObject("switchMap","1");// 地图的开关
		}
		mv.addObject("oldClaim","0");// 默认不是旧理赔
		if("oldClaim".equals(prpLRegistVo.getFlag())){
			mv.addObject("oldClaim","1");// 旧理赔标识
		}
		// mv.addObject("switchMap","1");//地图的开关
		mv.setViewName("schedule/scheduleEdit/SchAddDefloss");
		return mv;
	}
	
	/**
	 * 拼装调度定损项数据
	 * @param itemVoList
	 * @return
	 */
	private List<PrpLScheduleDefLossVo> supplementInfo(List<PrpLScheduleItemsVo> itemVoList, String registNo) {
		List<PrpLScheduleDefLossVo> defLossVoList = scheduleTaskService.getPrpLScheduleDefLossesVoByRegistNo(registNo);
		List<PrpLScheduleDefLossVo> returnVoList = new ArrayList<PrpLScheduleDefLossVo>();
		PrpLScheduleDefLossVo defLossVo = new PrpLScheduleDefLossVo();
		if (itemVoList != null && itemVoList.size() > 0) {
			out:for (PrpLScheduleItemsVo itemVo : itemVoList) {
					for (PrpLScheduleDefLossVo lossVo : defLossVoList) {
						if (StringUtils.equals(itemVo.getSerialNo(), lossVo.getSerialNo().toString())) {
							continue out;
						}
					}
					if (StringUtils.equals(itemVo.getItemType(), CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_MAINCAR)
							|| StringUtils.equals(itemVo.getItemType(), CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_THIRDCAR)) {
						
						defLossVo = Beans.copyDepth().from(itemVo).to(PrpLScheduleDefLossVo.class);
						//defLossVo.setId(null);
						defLossVo.setScheduleStatus(CodeConstants.ScheduleStatus.NOT_SCHEDULED);
						defLossVo.setDeflossType(CodeConstants.DeflossType.CarLoss);
						defLossVo.setSourceFlag(CodeConstants.ScheduleDefSource.SCHEDULEDEF);
						if (StringUtils.equals(itemVo.getItemType(), CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_MAINCAR)) {
							defLossVo.setLossitemType(CodeConstants.LossParty.TARGET);
						} else {
							defLossVo.setLossitemType(CodeConstants.LossParty.THIRD);
						}
						defLossVo.setCreateUser(null);
						defLossVo.setCreateTime(null);
						defLossVo.setUpdateUser(null);
						defLossVo.setUpdateTime(null);
						returnVoList.add(defLossVo);
					} 
				}
		}
		return returnVoList;
	}

	/**
	 * 调度定损提交
	 * @throws Exception
	 */
	@Timed("调度定损提交")
	@RequestMapping(value = "/defLossSave.do")
	@ResponseBody
	public AjaxResult defLossSave(String checkAreaCode,String lngXlatY,
			@FormModel(value = "submitVo") WfTaskSubmitVo submitVo,
			@FormModel(value = "prpLScheduleTaskVo") PrpLScheduleTaskVo prpLScheduleTaskVo,
			@FormModel(value = "prpLScheduleDefLosses") List<PrpLScheduleDefLossVo> prpLScheduleDefLosses) throws Exception {
		
		submitVo.setTaskInKey(prpLScheduleTaskVo.getRegistNo());
		if("".equals(policyViewService.getPolicyComCode(prpLScheduleTaskVo.getRegistNo()))){
			submitVo.setComCode(WebUserUtils.getComCode());
		}else{
			submitVo.setComCode(policyViewService.getPolicyComCode(prpLScheduleTaskVo.getRegistNo()));
		}
		//submitVo.setComCode(policyViewService.getPolicyComCode(prpLScheduleTaskVo.getRegistNo()));
		//submitVo.setComCode(WebUserUtils.getComCode());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setSubmitType(SubmitType.N);
		
		/*prpLScheduleTaskVo.setScheduledComcode(SecurityUtils.getComCode());
		prpLScheduleTaskVo.setScheduledComname(SecurityUtils.getComName());
		prpLScheduleTaskVo.setScheduledUsername(SecurityUtils.getUserName());
		prpLScheduleTaskVo.setScheduledUsercode(SecurityUtils.getUserCode());*/
		prpLScheduleTaskVo.setOperatorCode(WebUserUtils.getUserCode());
		prpLScheduleTaskVo.setOperatorName(WebUserUtils.getUserName());
		prpLScheduleTaskVo.setScheduleStatus(CodeConstants.ScheduleStatus.DEFLOSS_SCHEDULED);
		
		String theSameScheduled = "";
		
		int serialNo = 1;// 取得一定调度定损的最高序号
		// 根据报案号查出查勘调度项，如果有查勘调度到人状态，则判断已经提交查勘
        List<PrpLScheduleItemsVo> voList = scheduleTaskService.getPrpLScheduleItemsesVoByRegistNo(prpLScheduleTaskVo.getRegistNo());
        if(voList!=null && voList.size()>0){
            serialNo = voList.size();
        }
        
		// 查勘的序号
        List<PrpLCheckCarVo> prpLCheckCarVos = checkTaskService.findCheckCarVoByRegistNoAndSerialNo(prpLScheduleTaskVo.getRegistNo(),"serialNo");
		if(prpLCheckCarVos != null && prpLCheckCarVos.size() > 0){
		    if(serialNo < prpLCheckCarVos.get(0).getSerialNo()){
		        serialNo = prpLCheckCarVos.get(0).getSerialNo();
		    }
		}
		Map<String,Integer> propMap = new HashMap<String,Integer>();// 存放三者车的车牌号和序号
		List<PrpLScheduleDefLossVo> prpLScheduleDefLossVoList = scheduleTaskService.getPrpLScheduleDefLossesVoByRegistNo(prpLScheduleTaskVo.getRegistNo());
		for(PrpLScheduleDefLossVo defLossVo:prpLScheduleDefLossVoList){
			// 根据损失项和损失内容判断调度是否已经调度过
			if( !CodeConstants.ScheduleStatus.SCHEDULED_CANCEL.equals(defLossVo.getScheduleStatus())){// 排除注销的任务
				for(PrpLScheduleDefLossVo prpLScheduleDefLossVo:prpLScheduleDefLosses){
					if(defLossVo.getLossitemType().equals("1") && prpLScheduleDefLossVo.getLossitemType().equals("2")
&&defLossVo.getItemsContent()
							.equals(prpLScheduleDefLossVo.getItemsContent())){// 三者车和标的车牌相等
						theSameScheduled = "标的车牌"+prpLScheduleDefLossVo.getItemsContent();
						break;
					}
					if(defLossVo.getScheduleStatus().equals(CodeConstants.ScheduleStatus.DEFLOSS_SCHEDULED)
							&&defLossVo.getItemsContent().equals(prpLScheduleDefLossVo.getItemsContent() )
							&&defLossVo.getLossitemType().equals(prpLScheduleDefLossVo.getLossitemType())
							   &&defLossVo.getDeflossType().equals(prpLScheduleDefLossVo.getDeflossType())){
						theSameScheduled = prpLScheduleDefLossVo.getItemsContent();
						break;
					}
				}
			}
			// 已经调度过的三者车
			if(defLossVo.getScheduleStatus().equals(CodeConstants.ScheduleStatus.SCHEDULED_FINISH)
					|| defLossVo.getScheduleStatus().equals(CodeConstants.ScheduleStatus.DEFLOSS_SCHEDULED) 
					|| defLossVo.getScheduleStatus().equals(CodeConstants.ScheduleStatus.SCHEDULED_CHANGE)
					|| defLossVo.getScheduleStatus().equals(CodeConstants.ScheduleStatus.SCHEDULED_CANCEL)
							&& defLossVo.getDeflossType().equals("1")){
				// 先比较调度查勘表PrpLscheduleItems的SerialNo
			    
		        if(serialNo < defLossVo.getSerialNo()){
				    serialNo = defLossVo.getSerialNo();
			    }
				propMap.put(defLossVo.getItemsContent(), defLossVo.getSerialNo());
			}
		}
		// 存在theSameScheduled已经被调度过
		if(StringUtils.isNotBlank(theSameScheduled)){
			AjaxResult ajaxResult = new AjaxResult();
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
			ajaxResult.setData(theSameScheduled);
			return ajaxResult;
		}
		// 若已生成某三者车定损任务节点，还调度新增相同车牌的三者车定损任务，则提交时提示“该三者车定损任务已调度，不能重复调度
		for(PrpLScheduleDefLossVo defLossVo:prpLScheduleDefLossVoList){
			if( !CodeConstants.ScheduleStatus.SCHEDULED_CANCEL.equals(defLossVo.getScheduleStatus())){// 排除注销的任务
				for(PrpLScheduleDefLossVo prpLScheduleDefLossVo:prpLScheduleDefLosses){
					if(prpLScheduleDefLossVo.getDeflossType().equals("1")){// 车辆定损
						if(prpLScheduleDefLossVo.getItemsContent().equals(defLossVo.getItemsContent())&&
								prpLScheduleDefLossVo.getDeflossType().equals(defLossVo.getDeflossType())){
							theSameScheduled = prpLScheduleDefLossVo.getItemsContent();
							break;
						}
					}else{// 财产定损
						if(!prpLScheduleDefLossVo.getLossitemType().equals("2")){
							if(prpLScheduleDefLossVo.getLossitemType().equals(defLossVo.getLossitemType())&&
									prpLScheduleDefLossVo.getDeflossType().equals(defLossVo.getDeflossType())){
								theSameScheduled = "该损失方";
								break;
							}
							// 增加管控，首次调度提交查勘处理完defLossVo.getLossitemType()为0
							if(prpLScheduleDefLossVo.getDeflossType().equals(defLossVo.getDeflossType())){
								if("3".equals(prpLScheduleDefLossVo.getLossitemType())){
									if("3".equals(defLossVo.getLossitemType())|| "0".equals(defLossVo.getLossitemType())){
										theSameScheduled = "该损失方";
										break;
									}
								}
								
							}
						}else{// 三者车
							if(defLossVo.getScheduleStatus().equals(CodeConstants.ScheduleStatus.SCHEDULED_FINISH)
									|| defLossVo.getScheduleStatus().equals(CodeConstants.ScheduleStatus.DEFLOSS_SCHEDULED)  
									&& defLossVo.getDeflossType().equals("2")){
								if(prpLScheduleDefLossVo.getItemsContent().equals(defLossVo.getItemsContent())){
									theSameScheduled = prpLScheduleDefLossVo.getItemsContent();
									break;
								}
								// 区分查勘提交的三者车propMap.get(prpLScheduleDefLossVo.getItemsContent())
								String itemContent = propMap.get(prpLScheduleDefLossVo.getItemsContent())+"";
								if(StringUtils.isNotBlank(itemContent)){
									String serialNoFlags = defLossVo.getSerialNo()+"";
									if(itemContent.equals(serialNoFlags)){
										theSameScheduled = prpLScheduleDefLossVo.getItemsContent();
										break;
									}	
								}
							}
						}
					}
				}
			}
		}
		
		if(StringUtils.isNotBlank(theSameScheduled)){
			AjaxResult ajaxResult = new AjaxResult();
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
			ajaxResult.setData(theSameScheduled);
			return ajaxResult;
		}
		
		for(PrpLScheduleDefLossVo prpLScheduleDefLossVo:prpLScheduleDefLosses){// id置为空
			prpLScheduleDefLossVo.setId(null);
			if(StringUtils.isBlank(prpLScheduleDefLossVo.getRegistNo())){
				prpLScheduleDefLossVo.setRegistNo(prpLScheduleTaskVo.getRegistNo());
			}
			if(prpLScheduleDefLossVo.getSerialNo() == null){
				if(prpLScheduleDefLossVo.getLossitemType().equals(CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_THIRDCAR)){
					if(prpLScheduleDefLossVo.getDeflossType().equals("1")){// 车损
						//serialNo ++;
						++ serialNo;
						prpLScheduleDefLossVo.setSerialNo(serialNo);
					}else{// 物损
						prpLScheduleDefLossVo.setSerialNo(propMap.get(prpLScheduleDefLossVo.getItemsContent()));
						//prpLScheduleDefLossVo.setSerialNo(propSerialNo);
					}
					prpLScheduleDefLossVo.setItemsName("三者车");
					prpLScheduleDefLossVo.setLicenseNo(prpLScheduleDefLossVo.getItemsContent());
				}else if(prpLScheduleDefLossVo.getLossitemType().equals(CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PROP)){
					prpLScheduleDefLossVo.setSerialNo(0);
					prpLScheduleDefLossVo.setLossitemType("0");// 修改成跟查勘的保存一致
					prpLScheduleDefLossVo.setItemsName("地面");
					prpLScheduleDefLossVo.setLicenseNo(prpLScheduleDefLossVo.getItemsContent());
				}else if(prpLScheduleDefLossVo.getLossitemType().equals(CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_MAINCAR)){
					prpLScheduleDefLossVo.setSerialNo(1);
					prpLScheduleDefLossVo.setItemsName("标的车");
					prpLScheduleDefLossVo.setLicenseNo(prpLScheduleDefLossVo.getItemsContent());
				}
			}
			
		}
		//this.setReassignmentes(prpLScheduleTaskVo, checkAreaCode, lngXlatY, "DLoss", prpLScheduleDefLosses);
		prpLScheduleTaskVo.setTypes("2");
		List<PrpLScheduleTaskVo> prpLScheduleTaskVoList = scheduleHandlerService.saveScheduleDefLossTask(prpLScheduleTaskVo, prpLScheduleDefLosses, submitVo);
		
		// 接口改造
		for(PrpLScheduleTaskVo vo :prpLScheduleTaskVoList){
			// 调度定损提交powerFlag传null，不做任何管控
	        this.setReassignmentes(vo, checkAreaCode, lngXlatY, "DLoss", vo.getPrpLScheduleDefLosses(),null);
		}
		
        
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		ajaxResult.setData("");
		
		try{
			// 调度定损送方正客服系统（调度信息）
			interfaceAsyncService.scheduleInfoToFounder(prpLScheduleTaskVo,ScheduleType.DEFLOSS_SCHEDULE);
		}catch(Exception e){
			// TODO: handle exception
		}
		// 是否移动端案件
		
		PrpLRegistVo registVo = registService.findRegistByRegistNo(prpLScheduleTaskVo.getRegistNo());
		Boolean selfClaimFlag = isSelfClaimCase(registVo);// 自助理赔案子
		Boolean isMobileWhileListCase = wFMobileService.findWhileListCase(CodeConstants.WhiteList.ISMOBILE, prpLScheduleTaskVo.getScheduledUsercode());
		// 调度定损送车童网/民太安（报案信息）
		if(!isMobileWhileListCase && !selfClaimFlag){
		    List<PrpLCMainVo> prpLCMainVoList = policyViewService.getPolicyAllInfo(prpLScheduleTaskVo.getRegistNo());
	        PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLScheduleTaskVo.getRegistNo());
	        PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByUserCode(prpLScheduleTaskVo.getScheduledUsercode());
	        PrpLConfigValueVo configValueMTACheckVo = ConfigUtil.findConfigByCode(CodeConstants.MTACheck,prpLRegistVo.getComCode());
	        PrpLConfigValueVo configValueCTCheckVo = ConfigUtil.findConfigByCode(CodeConstants.CTCheck,prpLRegistVo.getComCode());
	        String cTMTACheck = "0";
	        if(prplIntermMainVo != null && "0003".equals(prplIntermMainVo.getIntermCode())){
	            if(configValueCTCheckVo != null && "1".equals(configValueCTCheckVo.getConfigValue())){
	                cTMTACheck = "1";
	            }
	        }else if(prplIntermMainVo != null && "0005".equals(prplIntermMainVo.getIntermCode())){
	            if(configValueMTACheckVo != null && "1".equals(configValueMTACheckVo.getConfigValue())){
	                cTMTACheck = "1";
	            }
	        }
	        if(prplIntermMainVo != null && "1".equals(cTMTACheck)){
	            if("0003".equals(prplIntermMainVo.getIntermCode())||"0005".equals(prplIntermMainVo.getIntermCode())){
	                RegistInformationVo registInformationVo = new RegistInformationVo();
	                registInformationVo.setPrpLScheduleTaskVo(prpLScheduleTaskVo);
	                registInformationVo.setPrpLScheduleTaskVoList(prpLScheduleTaskVoList);
	                registInformationVo.setSchType("DLoss");
	                SysUserVo userVo = new SysUserVo();
	                userVo.setComCode(WebUserUtils.getComCode());
	                userVo.setUserCode(WebUserUtils.getUserCode());
	                registInformationVo.setUser(userVo);
					// 根据prpLScheduleTaskVoList的id查工作流表
	                List<PrpLWfTaskVo> prpLWfTaskVoResult = new ArrayList<PrpLWfTaskVo>();
	                for(PrpLScheduleDefLossVo vo : prpLScheduleTaskVoList.get(0).getPrpLScheduleDefLosses()){
	                    List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findInTaskByOther(vo.getRegistNo(),vo.getId().toString(),"DLoss");
	                    prpLWfTaskVoResult.addAll(prpLWfTaskVoList);
	                }
	                carchildService.sendRegistInformation(prplIntermMainVo,prpLRegistVo,prpLCMainVoList,prpLWfTaskVoResult,registInformationVo);
	            }
	        }
		}
		// 发送短信
		SysUserVo sysUserVo = WebUserUtils.getUser();
		prpLScheduleTaskVo.setPrpLScheduleDefLosses(prpLScheduleDefLosses);
		scheduleHandlerService.sendMsg(prpLScheduleTaskVo,null, sysUserVo,"2");
		return ajaxResult;
	}
	
	/**
	 * <pre>
	 * 跳转到调度改派界面
	 * </pre>
	 * @modified: ☆Luwei(2015年12月16日 下午6:27:15): <br>
	 */
	@Timed("跳转到调度改派页面")
	@RequestMapping(value = "/schReassignment")
	@ResponseBody
	public ModelAndView schReassignment(PrpLWfTaskVo taskVo) {
		
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(taskVo.getRegistNo());
		PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(taskVo.getRegistNo());
		
		List<Long> defLossVoIds = new ArrayList<Long>();
		List<Long> taskVoIds = new ArrayList<Long>();
		List<Long> itemVoIds = new ArrayList<Long>();
		Map<String,String> idTaskIdMap = new HashMap<String,String>();
		setYwId(taskVo.getRegistNo(), defLossVoIds, taskVoIds, itemVoIds, idTaskIdMap);
		
		ModelAndView mv = new ModelAndView();
		if (defLossVoIds.size() > 0) {
			List<PrpLScheduleDefLossVo> scheduleDefLossVos = scheduleTaskService.getPrpLScheduleDefLossesVoByIds(defLossVoIds);
			for(PrpLScheduleDefLossVo prpLScheduleDefLossVo:scheduleDefLossVos){
				for(String key:idTaskIdMap.keySet()){
					if(prpLScheduleDefLossVo.getId().toString().equals(key)){
						prpLScheduleDefLossVo.setFlowTaskId(idTaskIdMap.get(key));
					}
				}
			}
			mv.addObject("scheduleDefLossVos", scheduleDefLossVos);
		}
		if (itemVoIds.size() > 0) {
			List<PrpLScheduleItemsVo> scheduleItemVos = scheduleTaskService.getPrpLScheduleItemsVoByIds(itemVoIds);
			for(PrpLScheduleItemsVo prpLScheduleItemsVo:scheduleItemVos){
				for(String key:idTaskIdMap.keySet()){
					if(prpLScheduleItemsVo.getId().toString().equals(key)){
						prpLScheduleItemsVo.setFlowTaskId(idTaskIdMap.get(key));
					}
				}
			}
			mv.addObject("scheduleItemVos", scheduleItemVos);
		}
		if (taskVoIds.size() > 0) {
			List<PrpLScheduleTaskVo> scheduleTaskVos = scheduleTaskService.getPrpLScheduleTaskVoByIds(taskVoIds);
			for(PrpLScheduleTaskVo scheduleTaskVo:scheduleTaskVos){
				for(String key:idTaskIdMap.keySet()){
					if(scheduleTaskVo.getId().toString().equals(key)){
						scheduleTaskVo.setFlowTaskId(idTaskIdMap.get(key));
					}
				}
			}
			mv.addObject("scheduleTaskVos", scheduleTaskVos);
		}
		
		List<PrpLScheduleTaskVo> scheduleTaskListVo = scheduleTaskService.findScheduleTaskListByRegistNo(taskVo.getRegistNo());
		PrpLScheduleTaskVo scheduleVo = new PrpLScheduleTaskVo();
		if(scheduleTaskListVo!=null&&scheduleTaskListVo.size()>0){
			for(PrpLScheduleTaskVo scheduleTask :scheduleTaskListVo){
				if("3".equals(scheduleTask.getTypes())){
					scheduleVo = scheduleTask;
				}
			}
		}
		// 设置自定义区域编码
		//if(scheduleTaskVo.getSelfDefinareaCode()!=null&&scheduleTaskVo.getSelfDefinareaCode()!=""){
		if(!StringUtils.isNotEmpty(scheduleVo.getSelfDefinareaCode())){
			scheduleVo.setSelfDefinareaCode(prpLRegistVo.getSelfDefinareaCode());
		}
		mv.addObject("prpLScheduleVo", scheduleVo);
		if(StringUtils.isNotBlank(prpLScheduleTaskVo.getScheduledUsercode())){
			SysUserVo sysUserVo = sysUserService.findByUserCode(prpLScheduleTaskVo.getScheduledUsercode());
			if(StringUtils.isNotBlank(sysUserVo.getMobile())){
				mv.addObject("mobile", sysUserVo.getMobile());
			}
		}
		mv.addObject("prpLScheduleTaskVo", prpLScheduleTaskVo);
		mv.addObject("flowId", taskVo.getFlowId());
		mv.addObject("flowTaskId", taskVo.getTaskId());
		// 通赔
		mv.addObject("comCode", taskVo.getComCode());
		PrpLConfigValueVo configValueVoByMap = ConfigUtil.findConfigByCode(CodeConstants.SWITCHMAP);
		if(configValueVoByMap!=null&&"1".equals(configValueVoByMap.getConfigValue())){// 开关
			mv.addObject("switchMap","1");// 地图的开关
		}
		mv.addObject("oldClaim","0");// 默认不是旧理赔
		if("oldClaim".equals(prpLRegistVo.getFlag())){
			mv.addObject("oldClaim","1");// 旧理赔标识
		}
		// mv.addObject("switchMap","1");//地图的开关
		mv.setViewName("schedule/scheduleEdit/SchReassignment");
		return mv;
	}
	
	/**
	 * 调度改派处理
	 * @param id
	 * @param schType
	 * @param submitVo
	 * @param prpLScheduleTaskVo
	 * @return
	 * @throws Exception
	 */
	@Timed("调度改派处理")
	@RequestMapping(value = "/reassignmentSubmit.do")
	@ResponseBody
	public AjaxResult reassignmentSubmit(@RequestParam("id")String id, String flowTaskId,String schType,String checkAreaCode,String lngXlatY,
			@FormModel(value = "submitVo") WfTaskSubmitVo submitVo,
			@FormModel(value = "prpLScheduleTaskVo") PrpLScheduleTaskVo prpLScheduleTaskVo) throws Exception {
		List<Long> defLossVoIds = new ArrayList<Long>();
		List<Long> taskVoIds = new ArrayList<Long>();
		List<Long> itemVoIds = new ArrayList<Long>();
		Map<String,String> idTaskIdMap = new HashMap<String,String>();
		setYwId(prpLScheduleTaskVo.getRegistNo(), defLossVoIds, taskVoIds, itemVoIds, idTaskIdMap);
		String ywFlags = "0";// 不能改派
		 if (FlowNode.DLCar.equals(schType) || FlowNode.DLProp.equals(schType)) {
			 for(Long defLossVoId : defLossVoIds){
				 if(defLossVoId.toString().equals(id)){
					 ywFlags = "1";
					 break;
				 }
			 }
		 }else if(FlowNode.PLoss.equals(schType)){
			 for(Long itemVoId : itemVoIds){
				 if(itemVoId.toString().equals(id)){
					 ywFlags = "1";
					 break;
				 }
			 }
		 }else{
			 for(Long taskVoId : taskVoIds){
				 if(taskVoId.toString().equals(id)){
					 ywFlags = "1";
					 break;
				 }
			 }
		 }
		 if(ywFlags.equals("0")){
			 AjaxResult ajaxResult = new AjaxResult();
			ajaxResult.setData("此任务不是未处理，不能改派!");
			 ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
			 return ajaxResult;
		 }
		String comcode = submitVo.getComCode();
		schType = schType.trim();
		//List<PrpLWfTaskVo> prpLWfTaskInVos = wfFlowQueryService.findUnAcceptTask(prpLScheduleTaskVo.getRegistNo(), FlowNode.Sched);
	    PrpLWfTaskVo prpLWfTaskInVo = wfFlowService.findPrpLWfTaskQueryByTaskId(new BigDecimal(flowTaskId));
		PrpLScheduleTaskVo findScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(prpLScheduleTaskVo.getRegistNo());
		String subCheckFlag = wfTaskQueryService.getSubCheckFlag(prpLWfTaskInVo.getRegistNo(), prpLScheduleTaskVo.getScheduledUsercode(),prpLScheduleTaskVo.getProvinceCityAreaCode());
		PrpLScheduleTaskVo newScheduleTaskVo = new PrpLScheduleTaskVo();
		Beans.copy().from(findScheduleTaskVo).excludeEmpty().excludeNull().to(newScheduleTaskVo);
		Beans.copy().from(prpLScheduleTaskVo).excludeEmpty().excludeNull().to(newScheduleTaskVo);
		
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setAssignCom(prpLScheduleTaskVo.getScheduledComcode());
		submitVo.setAssignUser(prpLScheduleTaskVo.getScheduledUsercode());
		submitVo.setFlowTaskId(new BigDecimal(flowTaskId));
		submitVo.setSubCheckFlag(subCheckFlag);
		
		
		List<Long> ids = new ArrayList<Long>();
		ids.add(Long.valueOf(id));
		
		//String flag = scheduleHandlerService.scheduleCancel(submitVo, ids, prpLWfTaskInVos, schType);
		
		newScheduleTaskVo.setTypes("3");
		String scheduleType = "1";
		
		List<PrpLCMainVo> prpLCMainVoList = policyViewService.getPolicyAllInfo(prpLScheduleTaskVo.getRegistNo());
		PrpLRegistVo registVo= registService.findRegistByRegistNo(prpLScheduleTaskVo.getRegistNo());
		Double taskId = submitVo.getFlowTaskId().doubleValue();
		PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTask(taskId);
		String flags = "1";// 民太安车童送撤销接口是否成功标识
        PrpLConfigValueVo configValueMTACheckVo = ConfigUtil.findConfigByCode(CodeConstants.MTACheck,registVo.getComCode());
        PrpLConfigValueVo configValueCTCheckVo = ConfigUtil.findConfigByCode(CodeConstants.CTCheck,registVo.getComCode());

        if (FlowNode.DLCar.equals(schType) || FlowNode.DLProp.equals(schType)) {
			PrpLScheduleDefLossVo scheduleDefLossVo = scheduleTaskService.findPrpLScheduleDefLossVoById(new Long(id));
			//scheduleDefLossVos.get(0).setScheduleFlag(CodeConstants.ValidFlag.VALID);
			// 理赔报案信息接口（理赔请求车童网/民太安）
			PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByUserCode(prpLScheduleTaskVo.getScheduledUsercode());
			if(prplIntermMainVo != null && isSelfClaimCase(registVo)){
        		AjaxResult ajaxResult = new AjaxResult();
  				ajaxResult.setData("自助理赔案件不能移交公估！");
  				ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
  				return ajaxResult;
        	}
			PrpdIntermMainVo intermMainCancelVo = managerService.findIntermByUserCode(prpLWfTaskInVo.getAssignUser());
			Boolean cTMTAFlags = false;
	        String cTMTACheck = "0";
	        String cTMTACheckCancel = "0";
	        if(prplIntermMainVo !=null && "0003".equals(prplIntermMainVo.getIntermCode())){
	            if(configValueCTCheckVo !=null && "1".equals(configValueCTCheckVo.getConfigValue())){
	                cTMTACheck = "1";
	            }
	        }else if(prplIntermMainVo !=null && "0005".equals(prplIntermMainVo.getIntermCode())){
	            if(configValueMTACheckVo !=null && "1".equals(configValueMTACheckVo.getConfigValue())){
	                cTMTACheck = "1";
	            }
	        }else{
	        	submitVo.setIsMobileAccept("0");
	        }
	        
           if(intermMainCancelVo !=null && "0003".equals(intermMainCancelVo.getIntermCode())){
                if(configValueCTCheckVo != null && "1".equals(configValueCTCheckVo.getConfigValue())){
                    cTMTACheckCancel = "1";
                }
            }else if(intermMainCancelVo !=null && "0005".equals(intermMainCancelVo.getIntermCode())){
                if(configValueMTACheckVo != null && "1".equals(configValueMTACheckVo.getConfigValue())){
                    cTMTACheckCancel = "1";
                }
            }
			// 是否移动端案件
		   Boolean isMobileWhileListCase = wFMobileService.findWhileListCase(CodeConstants.WhiteList.ISMOBILE,prpLScheduleTaskVo.getScheduledUsercode());
           if(!isMobileWhileListCase && prplIntermMainVo != null && "1".equals(cTMTACheck)){
               if("0003".equals(prplIntermMainVo.getIntermCode())||"0005".equals(prplIntermMainVo.getIntermCode())){
            	   cTMTAFlags = true;
               }
           }
			// 改派
			List<PrpLScheduleTaskVo> scheduleTaskVoList = scheduleHandlerService.reassignmentDefLossTask(newScheduleTaskVo, scheduleDefLossVo, submitVo);
			// 理赔调度提交/改派提交接口（理赔请求快赔系统）
			List<PrpLScheduleDefLossVo> prpLScheduleDefLosses = new ArrayList<PrpLScheduleDefLossVo>();
			prpLScheduleDefLosses.add(scheduleDefLossVo);
			
			// 判断当前工号有无自助查勘岗的Id----5195
			String powerFlag = scheduleService.findSelfCheckPower(prpLScheduleTaskVo.getScheduledUsercode());
			if(IsSingleAccident.YES.equals(registVo.getSelfClaimFlag()) &&
					!IsSingleAccident.YES.equals(powerFlag)){
				registVo.setSelfClaimFlag(IsSingleAccident.NOT);
				registService.saveOrUpdate(registVo);
			}
			// 接口改造
	        for(PrpLScheduleTaskVo vo : scheduleTaskVoList){
	            this.setReassignmentes(vo, checkAreaCode, lngXlatY, "reassig", vo.getPrpLScheduleDefLosses(),powerFlag);
	        }
			prpLScheduleDefLosses.get(0).setScheduleFlag("1");
			newScheduleTaskVo.setPrpLScheduleDefLosses(prpLScheduleDefLosses);
			scheduleType = "2";
			
			
			if(intermMainCancelVo != null && "1".equals(cTMTACheckCancel)){
	            if("0003".equals(intermMainCancelVo.getIntermCode())||"0005".equals(intermMainCancelVo.getIntermCode())){
	                if("2".equals(prpLWfTaskInVo.getIsMobileAccept()) || "3".equals(prpLWfTaskInVo.getIsMobileAccept())){
						// 理赔调度改派提交接口（理赔请求车童网/民太安）注销接口,撤销不用管控是否自助理赔标识
    	                RegistInformationVo registInformationVo = new RegistInformationVo();
    	                registInformationVo.setSchType(schType);
    	                registInformationVo.setNewHandlerUser(prpLScheduleTaskVo.getScheduledUsercode());
    	                flags = this.sendReassignmentCTorMTA(intermMainCancelVo.getIntermCode(),comcode,scheduleDefLossVo,prpLScheduleTaskVo,registInformationVo);
    	                if("1".equals(flags)){
                        	prpLWfTaskVo.setIsMobileAccept("0");
                        	wfTaskHandleService.updateTaskIn(prpLWfTaskVo);
                        }
	                }
	             }
			}
			
			if (cTMTAFlags && "1".equals(flags)) {
				// 理赔调度改派提交接口（理赔请求车童网/民太安）报案接口
				RegistInformationVo registInformationVo = new RegistInformationVo();
                registInformationVo.setNewHandlerUser(prpLScheduleTaskVo.getScheduledUsercode());
                registInformationVo.setPrpLScheduleTaskVoList(scheduleTaskVoList);
				// 这里跟定损一样方法DLoss所以传DLoss
                registInformationVo.setSchType("DLoss");
                SysUserVo userVo = new SysUserVo();
                userVo.setComCode(WebUserUtils.getComCode());
                userVo.setUserCode(WebUserUtils.getUserCode());
                registInformationVo.setUser(userVo);
                List<PrpLWfTaskVo> prpLWfTaskVoResult = new ArrayList<PrpLWfTaskVo>();
				// 获取最新in表的数据更新
                List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.findInTask(prpLWfTaskVo.getRegistNo(),prpLWfTaskVo.getHandlerIdKey(),prpLWfTaskVo.getSubNodeCode());
                prpLWfTaskVoResult.add(wfTaskVoList.get(0));
                carchildService.sendRegistInformation(prplIntermMainVo,registVo,prpLCMainVoList,prpLWfTaskVoResult,registInformationVo);
            }
		} else if (FlowNode.PLoss.equals(schType)) {
			List<PrpLScheduleItemsVo> scheduleItemVos = scheduleTaskService.getPrpLScheduleItemsVoByIds(ids);
			if(scheduleItemVos != null && scheduleItemVos.size() > 0){
				scheduleHandlerService.reassignmentScheduleItemTask(prpLScheduleTaskVo, scheduleItemVos, submitVo);
				// 理赔调度提交/改派提交接口（理赔请求快赔系统）
				this.setReassignments(prpLScheduleTaskVo, checkAreaCode, lngXlatY, "reassig", scheduleItemVos);
			}else{
				AjaxResult ajaxResult = new AjaxResult();
				ajaxResult.setData("未能查找到相关调度任务，请核实id"+id);
				ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
				return ajaxResult;
			}
		} else {
			List<PrpLScheduleTaskVo> scheduleTaskVos = scheduleTaskService.getPrpLScheduleTaskVoByIds(ids);
			if(scheduleTaskVos != null && scheduleTaskVos.size() > 0){
				// 理赔报案信息接口（理赔请求车童网/民太安）
	            PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByUserCode(prpLScheduleTaskVo.getScheduledUsercode());
				if(prplIntermMainVo != null && isSelfClaimCase(registVo)){
	        		AjaxResult ajaxResult = new AjaxResult();
	  				ajaxResult.setData("自助理赔案件不能移交公估！");
	  				ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
	  				return ajaxResult;
	        	}
	            PrpdIntermMainVo intermMainCancelVo = managerService.findIntermByUserCode(prpLWfTaskInVo.getAssignUser());
	            String cTMTACheck = "0";
	            String cTMTACheckCancel = "0";
	            Boolean cTMTAFlags = false;
				if(prplIntermMainVo!=null&&"0003".equals(prplIntermMainVo.getIntermCode())){// 车童
	                if(configValueCTCheckVo!=null && "1".equals(configValueCTCheckVo.getConfigValue())){
	                    cTMTACheck = "1";
	                }
				}else if(prplIntermMainVo!=null&&"0005".equals(prplIntermMainVo.getIntermCode())){// 民太安
	                if(configValueMTACheckVo!=null && "1".equals(configValueMTACheckVo.getConfigValue())){
	                    cTMTACheck = "1";
	                }
	            }else{
	            	submitVo.setIsMobileAccept("0");
	            }
	            
				if (intermMainCancelVo != null
						&& "0003".equals(intermMainCancelVo.getIntermCode())) {
					if (configValueCTCheckVo != null
							&& "1".equals(configValueCTCheckVo.getConfigValue())) {
						cTMTACheckCancel = "1";
					}
				} else if (intermMainCancelVo != null
						&& "0005".equals(intermMainCancelVo.getIntermCode())) {
					if (configValueMTACheckVo != null
							&& "1".equals(configValueMTACheckVo.getConfigValue())) {
						cTMTACheckCancel = "1";
					}
				}
		           
				// 是否移动端案件
				Boolean isMobileWhileListCase = wFMobileService.findWhileListCase(CodeConstants.WhiteList.ISMOBILE,prpLScheduleTaskVo.getScheduledUsercode());
                if(!isMobileWhileListCase && prplIntermMainVo != null && "1".equals(cTMTACheck)){
                    if("0003".equals(prplIntermMainVo.getIntermCode())||"0005".equals(prplIntermMainVo.getIntermCode())){
                    	cTMTAFlags = true;
                    }
                }
				scheduleHandlerService.reassignmentScheduleItemTask(newScheduleTaskVo, scheduleTaskVos.get(0).getPrpLScheduleItemses(), submitVo);
				// 理赔调度提交/改派提交接口（理赔请求快赔系统）
				// 判断当前工号有无自助查勘岗的Id----5195
				String powerFlag = scheduleService.findSelfCheckPower(prpLScheduleTaskVo.getScheduledUsercode());
				if(IsSingleAccident.YES.equals(registVo.getSelfClaimFlag()) &&
						!IsSingleAccident.YES.equals(powerFlag)){
					registVo.setSelfClaimFlag(IsSingleAccident.NOT);
					registService.saveOrUpdate(registVo);
				}
				
				this.setReassignmentCheck(prpLScheduleTaskVo, checkAreaCode, lngXlatY, "reassig",flowTaskId,powerFlag);
				
	            
	            

	            if(intermMainCancelVo != null && "1".equals(cTMTACheckCancel)){
	                if("0003".equals(intermMainCancelVo.getIntermCode())||"0005".equals(intermMainCancelVo.getIntermCode())){
	                    if("2".equals(prpLWfTaskInVo.getIsMobileAccept()) || "3".equals(prpLWfTaskInVo.getIsMobileAccept())){
							// 理赔调度改派提交接口（理赔请求车童网/民太安）注销接口,撤销不用管控是否自助理赔标识
							// 设置taskid
	                         String reassigTaskId = "";
	                         List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(prpLScheduleTaskVo.getRegistNo(), FlowNode.Chk.toString());
	                         if(prpLWfTaskVoList!=null && prpLWfTaskVoList.size()>0){
								// 流入时间降序排
	                             Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
	                             @Override
	                             public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
	                                     return o2.getTaskInTime().compareTo(o1.getTaskInTime());
	                                 }
	                             });
	                             reassigTaskId = prpLWfTaskVoList.get(0).getHandlerIdKey().toString();
	                         }
	 	                    RegistInformationVo registInformationVo = new RegistInformationVo();
	 	                    registInformationVo.setNewTaskId(reassigTaskId);
	 	                    registInformationVo.setNewHandlerUser(prpLScheduleTaskVo.getScheduledUsercode());
	 	                    registInformationVo.setFlag("reassig");
	 	                    registInformationVo.setSchType(schType);
	 	                    prpLScheduleTaskVo.setId(Long.valueOf(id));
	 	                    flags = this.sendReassignmentCTorMTA(intermMainCancelVo.getIntermCode(),comcode,null,prpLScheduleTaskVo,registInformationVo);
	 	                    if("1".equals(flags)){
		                       	 prpLWfTaskVo.setIsMobileAccept("0");
		                       	 wfTaskHandleService.updateTaskIn(prpLWfTaskVo);
	                        }
	                    }
	                }
	            }


            	if(cTMTAFlags && "1".equals(flags)){
					// 理赔调度改派提交接口（理赔请求车童网/民太安）报案接口
					// 设置taskid
                    String reassigTaskId = "";
                    List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(prpLScheduleTaskVo.getRegistNo(), FlowNode.Chk.toString());
                    if(prpLWfTaskVoList!=null && prpLWfTaskVoList.size()>0){
						// 流入时间降序排
                        Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
                        @Override
                        public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
                                return o2.getTaskInTime().compareTo(o1.getTaskInTime());
                            }
                        });
                        reassigTaskId = prpLWfTaskVoList.get(0).getHandlerIdKey().toString();
                    }
                    RegistInformationVo registInformationVo = new RegistInformationVo();
                    registInformationVo.setNewTaskId(reassigTaskId);
                    registInformationVo.setNewHandlerUser(prpLScheduleTaskVo.getScheduledUsercode());
                    registInformationVo.setFlag("reassig");
                    
                    prpLScheduleTaskVo.setId(Long.valueOf(reassigTaskId));
                    registInformationVo.setPrpLScheduleTaskVo(prpLScheduleTaskVo);
                    registInformationVo.setSchType("reassig");
                    SysUserVo userVo = new SysUserVo();
                    userVo.setComCode(WebUserUtils.getComCode());
                    userVo.setUserCode(WebUserUtils.getUserCode());
                    registInformationVo.setUser(userVo);
                    List<PrpLWfTaskVo> prpLWfTaskVoResult = new ArrayList<PrpLWfTaskVo>();
					// 获取最新in表的数据更新
                    List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.findInTaskByOther(prpLWfTaskVo.getRegistNo(),reassigTaskId,prpLWfTaskVo.getNodeCode());
                    prpLWfTaskVoResult.add(wfTaskVoList.get(0));
                    carchildService.sendRegistInformation(prplIntermMainVo,registVo,prpLCMainVoList,prpLWfTaskVoResult,registInformationVo);
                 }
			}else{
				AjaxResult ajaxResult = new AjaxResult();
				ajaxResult.setData("未能查找到相关调度任务，请核实"+id);
				ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
				return ajaxResult;
			}
		}
		// 发送短信
		SysUserVo sysUserVo = WebUserUtils.getUser();
		scheduleHandlerService.sendMsgByReassignment(newScheduleTaskVo, schType, sysUserVo,scheduleType);
		
		try{
			// 调度改派调用方正客服系统(调度信息接口)
			interfaceAsyncService.scheduleInfoToFounder(findScheduleTaskVo,"3");
		}catch(Exception e){
			// TODO: handle exception
		}
		
		
		
		
		// 通赔回写
		/*	String comcodes = comcode.substring(0, 4);
			String scheduledComcode = prpLScheduleTaskVo.getScheduledComcode().substring(0, 4);
			if(comcodes.equals("0002")){//深圳
				if(!scheduledComcode.equals("0002")){
					PrpLRegistVo registVo= registService.findRegistByRegistNo(prpLScheduleTaskVo.getRegistNo());
					registVo.setTpFlag("1");
					registVo.setIsoffSite("1");
					registService.saveOrUpdate(registVo);
				}else{
					scheduledComcode = prpLScheduleTaskVo.getScheduledComcode().substring(0, 2);
				}
			}else{
				if(scheduledComcode.equals("0002")){
					
						PrpLRegistVo registVo= registService.findRegistByRegistNo(prpLScheduleTaskVo.getRegistNo());
						registVo.setTpFlag("1");
						registVo.setIsoffSite("1");
						registService.saveOrUpdate(registVo);
				}else{
					comcodes = comcode.substring(0, 2);
					scheduledComcode = prpLScheduleTaskVo.getScheduledComcode().substring(0, 2);
					if(!comcodes.equals(scheduledComcode)){
						PrpLRegistVo registVo= registService.findRegistByRegistNo(prpLScheduleTaskVo.getRegistNo());
						registVo.setTpFlag("1");
						registVo.setIsoffSite("1");
						registService.saveOrUpdate(registVo);
					}
				}
				
			}*/
		/*//写入通赔标志
				PrpLRegistVo registVo= registService.findRegistByRegistNo(prpLScheduleTaskVo.getRegistNo());
				if(prpLScheduleTaskVo.getScheduledComcode()!=null){
				//根据区代码查询机构
				String code = areaDictService.findAreaList("areaCode",registVo.getDamageAreaCode());
				//调度地区
				
				List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
				prpLCMainVoList = policyViewService.getPolicyAllInfo(prpLScheduleTaskVo.getRegistNo());
				//承保地区
				String comCode = "";
				if(prpLCMainVoList.size()==2){
					for(PrpLCMainVo vo:prpLCMainVoList){
						if(("12").equals(vo.getRiskCode().substring(0, 2))){
							comCode = vo.getComCode();
						}
					}
				}else{
					comCode = prpLCMainVoList.get(0).getComCode();
				}
				if(code != null && comCode!=""){
					if("0002".equals(code.substring(0, 4))){//深圳
						if(!code.substring(0, 4).equals(comCode.substring(0, 4))){
							
							registVo.setTpFlag("1");
							registVo.setIsoffSite("1");
							registService.saveOrUpdate(registVo);
						}
					}else{
						if(!code.substring(0, 2).equals(comCode.substring(0, 2))){
							registVo.setTpFlag("1");
							registVo.setIsoffSite("1");
							registService.saveOrUpdate(registVo);
						}
					}
				}*/
		// 理赔调度提交/改派提交接口（理赔请求快赔系统）
		//this.setReassignment(prpLScheduleTaskVo, checkAreaCode, lngXlatY,schType);
		//this.setReassignments(prpLScheduleTaskVo, checkAreaCode, lngXlatY, schType, prpLScheduleItemses);
		
		
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setData("");
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	// 调度改派通知车童网/民太安
		private String sendReassignmentCTorMTA(String intermCode,String comCode,PrpLScheduleDefLossVo scheduleDefLossVo,PrpLScheduleTaskVo prpLScheduleTaskVo,RegistInformationVo registInformationVo) throws Exception{
	        String flags = "1";
		    if("0003".equals(intermCode)||"0005".equals(intermCode)){        
	            String url = null;
	            RevokeInfoReqVo reqVo = new RevokeInfoReqVo();
	            CarchildHeadVo head = new CarchildHeadVo();
	            RevokeBodyVo body = new RevokeBodyVo();
	            List<RevokeTaskInfoVo> revokeTaskInfoVos = new ArrayList<RevokeTaskInfoVo>();
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	            Date date = new Date();
	            String timeStamp = dateFormat.format(date);
	            BusinessType businessType = null;
	            if("0003".equals(intermCode)){
				// 理赔请求车童网
	                head.setRequestType("CT_006");
	                head.setUser("claim_user");
	                head.setPassWord("claim_psd");
	                //url = SpringProperties.getProperty("");
	                businessType = BusinessType.CT_scheduleChange;
	            }else {
				// 理赔请求民太安
	                head.setRequestType("MTA_006");
	                head.setUser("claim_user");
	                head.setPassWord("claim_psd");
	                //url = SpringProperties.getProperty("");
	                businessType = BusinessType.MTA_scheduleChange;
	            }
	            reqVo.setHead(head);                 
	            RevokeTaskInfoVo revokeTaskInfoVo = new RevokeTaskInfoVo();
	            String nodeType = registInformationVo.getSchType();
	            RegistInformationVo informationVo = new RegistInformationVo();
	            if(FlowNode.Check.equals(nodeType)){
	                informationVo.setOperateNode(FlowNode.Check.name());
	                revokeTaskInfoVo.setTaskId(prpLScheduleTaskVo.getId().toString());
	                revokeTaskInfoVo.setRegistNo(prpLScheduleTaskVo.getRegistNo());
	                revokeTaskInfoVo.setNewTaskId(registInformationVo.getNewTaskId());
	                revokeTaskInfoVo.setNewHandlerUser(registInformationVo.getNewHandlerUser());
	            }else {
	                informationVo.setOperateNode(FlowNode.DLoss.name());
	                revokeTaskInfoVo.setTaskId(scheduleDefLossVo.getId().toString());
	                revokeTaskInfoVo.setRegistNo(scheduleDefLossVo.getRegistNo());
	                revokeTaskInfoVo.setNewTaskId(scheduleDefLossVo.getId().toString());
	                revokeTaskInfoVo.setNewHandlerUser(registInformationVo.getNewHandlerUser());
	            }          
	            
	            revokeTaskInfoVo.setNodeType(nodeType);
	            revokeTaskInfoVo.setRevokeType("1");

	            if(prpLScheduleTaskVo != null){
	                if(StringUtils.isNotBlank(prpLScheduleTaskVo.getCancelOrReassignCode())){
	                    revokeTaskInfoVo.setReason(prpLScheduleTaskVo.getCancelOrReassignCode());
	                }else{
	                    revokeTaskInfoVo.setReason("");
	                }
	                if(StringUtils.isNotBlank(prpLScheduleTaskVo.getCancelOrReassignContent())){
	                    revokeTaskInfoVo.setRemark(prpLScheduleTaskVo.getCancelOrReassignContent());
	                }else{
	                    revokeTaskInfoVo.setRemark("");  
	                }
	            }
	            
	            revokeTaskInfoVo.setTimeStamp(timeStamp);
	            revokeTaskInfoVos.add(revokeTaskInfoVo);
	            body.setRevokeTaskInfos(revokeTaskInfoVos);
	            reqVo.setBody(body);
			url = SpringProperties.getProperty("MTA_URL_CANCELTASK");// 请求地址
	            logger.info("url=============="+url);
	            RevokeInfoResVo resVo = new RevokeInfoResVo();
	            try{
	                resVo = carchildService.sendRevokeInformation(reqVo,url);
	            }
	            catch(Exception e){
	                flags = "0";
	                e.printStackTrace();
	            }
	            finally{
				// 交互日志保存
	                SysUserVo userVo = new SysUserVo();
	                userVo.setComCode(WebUserUtils.getComCode());
	                userVo.setUserCode(WebUserUtils.getUserCode());
	                informationVo.setRevokeInfoReqVo(reqVo);
	                informationVo.setRevokeInfoResVo(resVo);
	                informationVo.setSchType("1");
	                carchildService.saveCTorMTACarchildInterfaceLog(informationVo,url,businessType,userVo);
	                if(resVo != null && "1".equals(resVo.getHead().getErrNo())){
	                    flags = "1";
	                }else{
	                    flags = "0";  
	                }
	            }
	        }
	        return flags;
		}
	
	/**
	 * 设置头部信息
	 * @return
	 */
	private HeadReq setHeadReq(){
		HeadReq head = new HeadReq();
		head.setRequestType("ScheduleSubmit");
		head.setPassword("mclaim_psd");
		head.setUser("mclaim_user");
		return head;
	}
	
	/**
	 * <pre>
	 * 跳转到调度注销界面
	 * </pre>
	 * @modified: ☆Luwei(2015年12月16日 下午6:27:15): <br>
	 */
	@Timed("跳转到调度注销界面")
	@RequestMapping(value = "/schLogout")
	@ResponseBody
	public ModelAndView schLogout(PrpLWfTaskVo taskVo) {
		ModelAndView mv = new ModelAndView();
		List<PrpLWfTaskVo> prpLWfTaskVos = new ArrayList<PrpLWfTaskVo>();
		List<PrpLScheduleDefLossVo> scheduleDefLossVos = new ArrayList<PrpLScheduleDefLossVo>();
		/*List<PrpLWfTaskVo> prpDLCartaskInVos = new ArrayList<PrpLWfTaskVo>();
		if("1".equals(taskVo.getRemark())){//车辆定损注销
			prpDLCartaskInVos = wfFlowQueryService.findPrpWfTaskVo(taskVo.getRegistNo(), FlowNode.DLoss.name());
			for(PrpLWfTaskVo vo : prpDLCartaskInVos){
				if(FlowNode.DLCar.equals(vo.getSubNodeCode()) || FlowNode.DLCarMod.equals(vo.getSubNodeCode())){
					prpLWfTaskInVos.add(vo);
				}
			}
			mv.addObject("dlCarFlags", "1");
		}else if("2".equals(taskVo.getRemark())){//财产定损
			prpDLCartaskInVos = wfFlowQueryService.findPrpWfTaskVo(taskVo.getRegistNo(), FlowNode.DLoss.name());
			for(PrpLWfTaskVo vo : prpDLCartaskInVos){
				if(FlowNode.DLProp.equals(vo.getSubNodeCode()) || FlowNode.DLPropMod.equals(vo.getSubNodeCode())){
					prpLWfTaskInVos.add(vo);
				}
			}
			mv.addObject("dlCarFlags", "2");
		}else{
			prpLWfTaskInVos = wfFlowQueryService.findUnAcceptTask(taskVo.getRegistNo(), FlowNode.DLoss.name());
			mv.addObject("dlCarFlags", "0");
		}*/
		if("Sched".equals(taskVo.getNodeCode())){
			List<PrpLScheduleDefLossVo> scheVoList = scheduleTaskService.getPrpLScheduleDefLossesVoByRegistNo(taskVo.getRegistNo());
			prpLWfTaskVos = wfFlowQueryService.findUnAcceptTask(taskVo.getRegistNo(), FlowNode.DLoss.name());
			for(PrpLScheduleDefLossVo scheVo:scheVoList){
				for(PrpLWfTaskVo WftaskVo:prpLWfTaskVos){
					if(WftaskVo.getItemName().contains(scheVo.getItemsContent())){
						scheVo.setFlag(String.valueOf(scheVo.getId())+"D"+String.valueOf(WftaskVo.getTaskId()));
						scheduleDefLossVos.add(scheVo);
					}
				}
			}
			mv.addObject("dlCarFlags", "0");
		}else{
			try{
				PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.valueOf(String.valueOf(taskVo.getTaskId())));
				prpLWfTaskVos.add(wfTaskVo);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if (prpLWfTaskVos != null && prpLWfTaskVos.size() > 0) {
				for(PrpLWfTaskVo vo:prpLWfTaskVos){
					// 注销成功会把PrpLScheduleDefLoss的ID赋给HandlerIdKey
					if (("0".equals(vo.getWorkStatus()) && ("DLCar".equals(vo.getSubNodeCode()) || "DLProp".equals(vo.getSubNodeCode()))) || "9".equals(vo.getWorkStatus())) {
						PrpLScheduleDefLossVo scheduleDefLossVo = scheduleTaskService.findPrpLScheduleDefLossVoById(Long.valueOf(vo.getHandlerIdKey()));
						scheduleDefLossVo.setFlag(String.valueOf(scheduleDefLossVo.getId())+"D"+String.valueOf(vo.getTaskId()));
						scheduleDefLossVos.add(scheduleDefLossVo);
					}else if(vo.getSubNodeCode().startsWith("DLProp")){
						PrpLdlossPropMainVo propVo = propLossService.findPropMainVoById(Long.valueOf(vo.getHandlerIdKey()));
						PrpLScheduleDefLossVo scheduleDefLossVo = scheduleTaskService.findPrpLScheduleDefLossVoById(propVo.getScheduleTaskId());
						scheduleDefLossVo.setFlag(String.valueOf(scheduleDefLossVo.getId())+"D"+String.valueOf(vo.getTaskId()));
						scheduleDefLossVos.add(scheduleDefLossVo);
					}else{
						PrpLDlossCarMainVo carVo = lossCarService.findLossCarMainById(Long.valueOf(vo.getHandlerIdKey()));
						PrpLScheduleDefLossVo scheduleDefLossVo = scheduleTaskService.findPrpLScheduleDefLossVoById(carVo.getScheduleDeflossId());
						scheduleDefLossVo.setFlag(String.valueOf(scheduleDefLossVo.getId())+"D"+String.valueOf(vo.getTaskId()));
						scheduleDefLossVos.add(scheduleDefLossVo);
					}
				}
				mv.addObject("workStatus", prpLWfTaskVos.get(0).getWorkStatus());
			}
			mv.addObject("dlCarFlags", taskVo.getRemark());
		}
		
		mv.addObject("scheduleDefLossVos", scheduleDefLossVos);
		mv.addObject("flowId", taskVo.getFlowId());
		mv.addObject("flowTaskId", taskVo.getTaskId());
		mv.setViewName("schedule/scheduleEdit/SchLogout");
		return mv;
	}
	
	/**
	 * 调度注销处理
	 * @param policyNo
	 * @param registNo
	 * @return
	 * @throws Exception
	 */
	@Timed("调度注销处理")
	@RequestMapping(value = "/logoutSubmit.ajax")
	@ResponseBody
	public String logoutSubmit(String dLossIds, String flowId, Long flowTaskId, String registNo,
	                           String cancelCode,String cancelRemark) throws Exception {
		List<PrpLWfTaskVo> otherPrpLWfTaskIns = wfFlowQueryService.findOtherPrpWfTaskVo(registNo,flowTaskId);
		//除当前任务之外，没有其他可操作任务，禁止注销！
		if(otherPrpLWfTaskIns == null || otherPrpLWfTaskIns.isEmpty()){
			return "7";
		}
		List<PrpLWfTaskVo> prpLWfTaskInVos = wfFlowQueryService.findPrpWfTaskVo(registNo,  FlowNode.DLoss.name());
		List<PrpLWfTaskVo> cancelTaskVoList = new ArrayList<PrpLWfTaskVo>();
		PrpLScheduleTaskVo findScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(registNo);
		PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(flowId);
		submitVo.setFlowTaskId(BigDecimal.valueOf(flowTaskId));
		submitVo.setAssignUser(WebUserUtils.getUserCode());
		submitVo.setAssignCom(WebUserUtils.getComCode());
		
		String[] arrDLossIds = dLossIds.split(",");
		List<Long> dLossIdList = new ArrayList<Long>();
		List<BigDecimal> taskIdList = new ArrayList<BigDecimal>();
		Map<Long,BigDecimal> idMap = new HashMap<Long, BigDecimal>();
		for (String str:arrDLossIds) {
			String[] strArray = str.split("D");
			dLossIdList.add(Long.valueOf(strArray[0]));
			taskIdList.add(new BigDecimal(strArray[1]));
			idMap.put(Long.valueOf(strArray[0]), new BigDecimal(strArray[1]));
		}
		
		List<RevokeTaskInfoVo> revokeTaskInfoVos = new ArrayList<RevokeTaskInfoVo>();
		RevokeInfoReqVo reqVo = new RevokeInfoReqVo();
        CarchildHeadVo head = new CarchildHeadVo();
        RevokeBodyVo body = new RevokeBodyVo();
        BusinessType businessType = null;
        
        
        PrpLConfigValueVo configValueMTACheckVo = ConfigUtil.findConfigByCode(CodeConstants.MTACheck,prpLRegistVo.getComCode());
        PrpLConfigValueVo configValueCTCheckVo = ConfigUtil.findConfigByCode(CodeConstants.CTCheck,prpLRegistVo.getComCode());
        String cTMTACheck = "0";
		for(Long key:idMap.keySet()){
			for(PrpLWfTaskVo wfTaskVo:prpLWfTaskInVos){
				if(idMap.get(key).toString().equals(wfTaskVo.getTaskId().toString())){
					cancelTaskVoList.add(wfTaskVo);
					PrpLScheduleDefLossVo scheduleDefLossVo = scheduleTaskService.findPrpLScheduleDefLossVoById(key);
					if(scheduleDefLossVo!=null && scheduleDefLossVo.getId()!=null){
						// 移动端案件理赔处理要通知快赔 并回写理赔处理标识
		                 PrpLWfTaskVo prpLWfTaskVo = sendMsgToMobileService.isMobileCaseAcceptInClaim(registNo,wfTaskVo.getTaskId());
						if(prpLWfTaskVo!=null){ // 发送通知
							prpLWfTaskVo.setHandlerStatus("9"); // 已注销
							prpLWfTaskVo.setWorkStatus("9"); // 注销
		                     prpLWfTaskVo.setMobileNo(scheduleDefLossVo.getSerialNo().toString());
		                     prpLWfTaskVo.setMobileName(scheduleDefLossVo.getLicenseNo());
		                     prpLWfTaskVo.setMobileOperateType(CodeConstants.MobileOperationType.LOSSCANCLE);
		                     String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
		                     interfaceAsyncService.packMsg(prpLWfTaskVo,url);
		                 }
		                 
						// 定损注销通知车童网/民太安
		                 if("2".equals(wfTaskVo.getIsMobileAccept())||"3".equals(wfTaskVo.getIsMobileAccept())){
		                     
		                     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		                     Date date = new Date();
		                     String timeStamp = dateFormat.format(date);
		                     
		                     if(configValueCTCheckVo != null && "2".equals(wfTaskVo.getIsMobileAccept()) && "1".equals(configValueCTCheckVo.getConfigValue())){
								// 理赔请求车童网
		                         head.setRequestType("CT_006");
		                         head.setUser("claim_user");
		                         head.setPassWord("claim_psd");
		                         businessType = BusinessType.CT_cancelDfloss;
		                         cTMTACheck = "1";
		                     }else if(configValueMTACheckVo != null && "1".equals(configValueMTACheckVo.getConfigValue())){
								// 理赔请求民太安
		                         head.setRequestType("MTA_006");
		                         head.setUser("claim_user");
		                         head.setPassWord("claim_psd");
		                         businessType = BusinessType.MTA_cancelDfloss;
		                         cTMTACheck = "1";
		                     }
		                     if("1".equals(cTMTACheck)){
			                     reqVo.setHead(head);
			                     RevokeTaskInfoVo revokeTaskInfoVo = new RevokeTaskInfoVo();
			                     revokeTaskInfoVo.setTaskId(scheduleDefLossVo.getId().toString());
			                     revokeTaskInfoVo.setRegistNo(scheduleDefLossVo.getRegistNo());
			                     revokeTaskInfoVo.setNodeType(FlowNode.DLoss.name());
			                     revokeTaskInfoVo.setRevokeType("3");
			                     revokeTaskInfoVo.setReason(cancelCode);
			                     revokeTaskInfoVo.setRemark(cancelRemark);
			                     revokeTaskInfoVo.setTimeStamp(timeStamp);
		                         revokeTaskInfoVos.add(revokeTaskInfoVo); 
		                     }
		                 }
					}
				}
			}
		}
		
		PrpLScheduleTaskVo newScheduleTaskVo = new PrpLScheduleTaskVo();
		Beans.copy().excludeEmpty().excludeNull().from(findScheduleTaskVo).to(newScheduleTaskVo);
		newScheduleTaskVo.setCancelOrReassignCode(cancelCode);
		newScheduleTaskVo.setCancelOrReassignContent(cancelRemark);
		String flag = scheduleHandlerService.scheduleCancel(newScheduleTaskVo,submitVo, idMap, cancelTaskVoList);
		
		interfaceAsyncService.sendLossToPlatform(registNo,null);// 定损任务注销，看此案件定核损任务是否送平台
		
        if(revokeTaskInfoVos!=null && revokeTaskInfoVos.size() > 0 ){
            String url = null;
            url = SpringProperties.getProperty("");
    	    body.setRevokeTaskInfos(revokeTaskInfoVos);
            reqVo.setBody(body);
            RevokeInfoResVo resVo = new RevokeInfoResVo();
            try{
				url = SpringProperties.getProperty("MTA_URL_CANCELTASK");// 请求地址MTA_URL
                logger.info("url=============="+url);
                resVo = carchildService.sendRevokeInformation(reqVo,url);
            }
            catch(Exception e){
                e.printStackTrace();
            }finally{
				// 交互日志保存
                SysUserVo userVo = new SysUserVo();
                userVo.setComCode(WebUserUtils.getComCode());
                userVo.setUserCode(WebUserUtils.getUserCode());
                RegistInformationVo informationVo = new RegistInformationVo();
                informationVo.setRevokeInfoReqVo(reqVo);
                informationVo.setRevokeInfoResVo(resVo);
                informationVo.setSchType("5");
                informationVo.setOperateNode(FlowNode.DLoss.name());
                if("CT_006".equals(reqVo.getHead().getRequestType())){
                    url = SpringProperties.getProperty("CT_URL") + CT_02;
                }
                carchildService.saveCTorMTACarchildInterfaceLog(informationVo,url,businessType,userVo);
            }

    	}
		try{
			// 调度注销定损任务调用方正系统
			founderService.cancelDflossTaskToFounder(findScheduleTaskVo);
		}catch(Exception e){
			// TODO: handle exception
		}	
		return flag;
	}

	
	/**
	 * 校验当前报案号是否已经提交查勘调度
	 */
	private boolean checkScheduleCheck(String registNo) {
		boolean flag = false;
		// 根据报案号查出查勘调度项，如果有查勘调度到人状态，则判断已经提交查勘
		List<PrpLScheduleItemsVo> voList = scheduleTaskService.getPrpLScheduleItemsesVoByRegistNo(registNo);
		if (voList != null && voList.size() > 0) {
			for (PrpLScheduleItemsVo vo : voList) {
				
				
				if ((!StringUtils.equals(vo.getScheduleStatus(), CodeConstants.ScheduleStatus.NOT_SCHEDULED))
						&&(!StringUtils.equals(vo.getScheduleStatus(), CodeConstants.ScheduleStatus.SCHEDULING))) {
					flag = true;
					break;
				}
			}
		}
		
		
		return flag;
	}
	
	private boolean checkScheduleDLoss(String registNo) {
		boolean flag = false;
		// 根据报案号查出查勘调度项，如果有查勘调度到人状态，则判断已经提交查勘
		List<PrpLScheduleDefLossVo> voList = scheduleTaskService.getPrpLScheduleDefLossesVoByRegistNo(registNo);
		if (voList != null && voList.size() > 0) {
			//for (PrpLScheduleDefLossVo vo : voList) {
				//if (vo != null && StringUtils.equals(vo.getScheduleStatus(), CodeConstants.ScheduleStatus.CHECK_SCHEDULED)) {
			return true;
				//}
			//}
		}
		return flag;
	}
	
	/**
	 * 校验当前报案号是否已经查勘提交定损
	 */
	private boolean checkCheckSubmit(String registNo) {
		boolean flag = false;
		PrpLCheckVo vo = checkTaskService.findCheckVoByRegistNo(registNo);
		if (vo != null && vo.getChkSubmitTime() != null) {
			flag = true;
		}
		//TODO
		return flag;
	}
	
	/**
	 * 增加查勘调度损失项前校验是否可以新增人伤
	 * @return
	 */
	public boolean checkPersonFinish(String registNo){
		boolean flag = true;
		/*		String endCase="0";
				List<PrpLEndCaseVo> listVo = endCasePubService.queryAllByRegistNo(registNo);
				if(listVo!=null && listVo.size()>0){
				List<PrpLClaimVo> vos = claimService.findClaimListByRegistNo(registNo);
				if(vos.size()> 0 ){
					for(PrpLClaimVo vo : vos){
						if(vo.getEndCaseTime() != null){//重开
							endCase = "1";
						}
					}
				}
				}*/
		
		// 根据报案号查出查勘调度项
		/*	List<PrpLScheduleItemsVo> voList = scheduleTaskService.getPrpLScheduleItemsesVoByRegistNo(registNo);
			//判断是否已经调度人伤任务，如果没有直接返回true
			if (voList != null && voList.size() > 0) {
				for (PrpLScheduleItemsVo vo : voList) {
					//如果有人伤调度项，需要后续判断，如果没有，直接返回true
					if (vo != null && StringUtils.equals(vo.getItemType(), CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PERSON)
							&& !StringUtils.equals(vo.getScheduleStatus(), CodeConstants.ScheduleStatus.NO_SCHEDULE)) {
						flag = false;
					}
				}
				if(flag) {
					return flag;
				}
			}*/
		// 判断是否存在人伤在in表，在就不能发起
        List<PrpLWfTaskVo> volist = wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.PLoss.toString());
        if(volist!=null&&volist.size()>0){
            flag = false;
        }
		List<PrpLClaimVo> vos = claimService.findClaimListByRegistNo(registNo,"1");// 有效
		String flags = "";
		if(vos.size()!=2){
			for(PrpLClaimVo vo:vos){
				flags = vo.getRiskCode().startsWith("11")?"CI":"BI";
			}
		}else{
			flags = "BC";
		}
		List<PrpLDlossPersTraceMainVo> List = persTraceDubboService.findPersTraceMainVoList(registNo);
		if(List!=null&&List.size()>0){
		    if(List.size() > 1){
		        Collections.sort(List, new Comparator<PrpLDlossPersTraceMainVo>() {
		            @Override
		            public int compare(PrpLDlossPersTraceMainVo o1,PrpLDlossPersTraceMainVo o2) {
		                    return o2.getCreateTime().compareTo(o1.getCreateTime());
		                }
		            });
		    }
		    PrpLDlossPersTraceMainVo persTraceMainVo = List.get(0);
			if( !"7".equals(persTraceMainVo.getUnderwriteFlag())){// 注销可以发起
		        if(flags.equals("BC")){
	                if(!persTraceMainVo.getLossState().equals("11")){
	                    flag = false;
	                }
	            }else if(flags.equals("BI")){
	                if(!persTraceMainVo.getLossState().substring(1, 2).equals("1")){
	                    flag = false;
	                }
	            }else{
	                if(!persTraceMainVo.getLossState().substring(0, 1).equals("1")){
	                    flag = false;
	                }
	            }
		    }
		}
		// TODO 如果有人伤调度项，且状态不是无需调度，则判断是否人伤核损完毕（带添加）
		return flag;
		
		/*boolean flag = false;
		boolean result = false;
		//根据报案号查出查勘调度项
		List<PrpLScheduleItemsVo> voList = scheduleTaskService.getPrpLScheduleItemsesVoByRegistNo(registNo);
		//判断是否已经调度人伤任务，如果没有直接返回true
		if (voList != null && voList.size() > 0) {
			for (PrpLScheduleItemsVo vo : voList) {
				//如果有人伤调度项，需要后续判断，如果没有，直接返回true
				if (StringUtils.equals(vo.getItemType(), CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PERSON)
						&& StringUtils.equals(vo.getScheduleStatus(), CodeConstants.ScheduleStatus.CHECK_SCHEDULED)) {
					flag = true;
					break;
				}
			}
		}
		//TODO 如果有人伤调度项，且状态不是无需调度，则判断是否人伤核损完毕（带添加）
		if(flag) {
			result = wfFlowQueryService.isVLossALlEnd(registNo,null);
		}
		return result;*/
	}

	//
	private boolean checkPersonFinishs(String registNo) {
		boolean flag = true;
		// zjd添加查勘、定损、单证收集环节标为互陪自赔案件，则不允许调度人伤任务
		List<PrpLCheckDutyVo> vos = 
		checkTaskService.findCheckDutyByRegistNo(registNo);
		if(vos!=null&&vos.size()>0){
			for(PrpLCheckDutyVo vo:vos){
				if(( "1" ).equals(vo.getIsClaimSelf()))
				flag = false;
			}
		}
		PrpLCheckVo checkVo = checkService.findCheckVoByRegistNo(registNo);
		if(checkVo!=null){
			if(( "1" ).equals(checkVo.getIsClaimSelf()))
			flag = false;
		}
		if(!flag)return flag;
		return flag;
	}
	
	/**
	 * 增加查勘调度损失项
	 * @return
	 */
	@Timed("增加查勘调度损失项")
	@RequestMapping(value = "/addItem.ajax")
	@ResponseBody
	public ModelAndView addItem(int itemSize) throws ParseException {
		ModelAndView mv = new ModelAndView();
		List<PrpLScheduleItemsVo> prpLScheduleItemses = new ArrayList<PrpLScheduleItemsVo>();
		PrpLScheduleItemsVo prpLScheduleItems = new PrpLScheduleItemsVo();
		prpLScheduleItems.setItemsContent("0");
		prpLScheduleItems.setItemRemark("0");
		prpLScheduleItems.setItemType(CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PERSON);
		prpLScheduleItems.setCreateUser(WebUserUtils.getUserCode());
		prpLScheduleItems.setCreateTime(new Date());
		prpLScheduleItems.setUpdateUser(WebUserUtils.getUserCode());
		prpLScheduleItems.setUpdateTime(new Date());
		prpLScheduleItemses.add(prpLScheduleItems);
		mv.addObject("prpLScheduleItemses",prpLScheduleItemses);
		mv.addObject("itemSize",itemSize);
		mv.setViewName("schedule/scheduleEdit/AddItems");
		return mv;
	}
	
	/**
	 * 增加定损调度损失项
	 * @return
	 */
	@Timed("增加定损调度损失项")
	@RequestMapping(value = "/addDefLoss.ajax")
	@ResponseBody
	public ModelAndView addDefLoss(int defLossSize) throws ParseException {
		ModelAndView mv = new ModelAndView();
		List<PrpLScheduleDefLossVo> prpLScheduleDefLosses = new ArrayList<PrpLScheduleDefLossVo>();
		PrpLScheduleDefLossVo prpLScheduleDefLossVo = new PrpLScheduleDefLossVo();
		/*prpLScheduleDefLossVo.setCreateUser(SecurityUtils.getUserCode());
		prpLScheduleDefLossVo.setCreateTime(new Date());
		prpLScheduleDefLossVo.setUpdateUser(SecurityUtils.getUserCode());
		prpLScheduleDefLossVo.setUpdateTime(new Date());*/
		Map<String,String> itemTypeMap = new HashMap<String,String>();
		/*itemTypeMap.put("1", "标的车");*/
		itemTypeMap.put("2","三者车");
		prpLScheduleDefLosses.add(prpLScheduleDefLossVo);
		mv.addObject("prpLScheduleDefLosses",prpLScheduleDefLosses);
		mv.addObject("itemTypeMap",itemTypeMap);
		mv.addObject("defLossSize",defLossSize);
		mv.addObject("checkSubmited","1");// 能增加定损必定查勘提交
		mv.setViewName("schedule/scheduleEdit/AddDefLoss");
		return mv;
	}
	
	/**
	 * 结案了控制整个界面不能编辑
	 * @return
	 */
	public boolean checkPersonFinishes(String registNo){
		boolean state = wfTaskHandleService.existTaskByNode(registNo, FlowNode.Compe.toString());

		// 重开前，工作流中有理算数据了，就不行，除非理算注销了。重开后，可以新增

		boolean flag = true;
		if(state){
			// 未重开，查结案表为空
			List<PrpLEndCaseVo> listVo = endCasePubService.queryAllByRegistNo(registNo);
			if(listVo==null||listVo.size()==0){
				// 理算注销可以发起return true;
				
				// 查询理算数据
				List<PrpLCompensateVo> prpLCompensateVos = compensateTaskService.queryCompensateByRegistNo(registNo);
				if(prpLCompensateVos != null && prpLCompensateVos.size() > 0){
					for(PrpLCompensateVo vo : prpLCompensateVos){
						if(!CodeConstants.UnderWriteFlag.CANCELFLAG.equals(vo.getUnderwriteFlag())){
							return false;
						}
					}
					
					// 管控暂存后注销再有理算
					List<PrpLWfTaskVo>  inList = wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.Compe.toString());
					if(inList!=null && inList.size() >0){
						return false;
					}
				}
				// 工作流有数据业务表没有数据，即未处理
				if(prpLCompensateVos==null || prpLCompensateVos.size() ==0){
					// 有可能注销了业务表没有数据,再查下工作流表
					// in表没有数据out表的数据全部注销返回true
					List<PrpLWfTaskVo>  inList = wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.Compe.toString());
					if(inList!=null && inList.size() >0){
						return false;
					}else{
						List<PrpLWfTaskVo>  OutList = wfFlowQueryService.findPrpWfTaskVoForOut(registNo, FlowNode.Compe.toString());
						if(OutList!=null && OutList.size() >0){
							for(PrpLWfTaskVo vo : OutList){
								if(!CodeConstants.WorkStatus.CANCEL.equals(vo.getWorkStatus())){
									return false;
								}
							}
						}else{
							return true;
						}
					}
					return true;
				}
				/*//查询没有注销的
				 * if(prpLCompensateVos.size()==0){//0为注销了
					return true;
				}*/
				return true;
			}
			if(listVo!=null && listVo.size()>0){
				List<PrpLClaimVo> vos = claimService.findClaimListByRegistNo(registNo);
				if(vos.size()==0||vos==null){
					return true;
				}else{
					for(PrpLClaimVo vo : vos){
						if(vo.getEndCaserCode()==null&&vo.getCaseNo()==null){// 重开
							// 如果有两个理算一个重开了，只要存在一个未处理完的任务就不能再定损
							List<PrpLWfTaskVo>  inList = wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.Compe.toString());
							if(inList!=null && inList.size() >0){
								return false;
							} else {
								return true;
							}
							
						}
					}
					return false;// 未重开
				}
			
			}
			
		}
		
		//
		/*	List<PrpLClaimVo> vos = claimService.findClaimListByRegistNo(registNo,"1");//有效
			if(vos.size()==0||vos==null){
				return true;
			}else{
				for(PrpLClaimVo vo : vos){
					if(vo.getEndCaseTime()==null){//结案了控制整个界面不能编辑
						return true;
					}
				}
			}*/

		return flag;

	}
	
	
	// 理赔调度提交/改派提交接口（理赔请求快赔系统）新增定损
		public void setReassignmentes(PrpLScheduleTaskVo prpLScheduleTaskVo,
				String checkAreaCode, String lngXlatY,String schType,List<PrpLScheduleDefLossVo> prpLScheduleDefLosses,String powerFlag) throws Exception {
			
		// 获取报案信息
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLScheduleTaskVo.getRegistNo());
			PrpLCMainVo vo = prpLCMainService.findPrpLCMain(prpLScheduleTaskVo.getRegistNo(), prpLRegistVo.getPolicyNo());
			HandleScheduleDOrGReqVo reqVo = new HandleScheduleDOrGReqVo();
		HeadReq head = setHeadReq();// 设置头部信息
			HandleScheduleReqDOrGBody body = new HandleScheduleReqDOrGBody();
			HandleScheduleReqScheduleDOrG scheduleDOrG = new HandleScheduleReqScheduleDOrG();
	        PrpDScheduleDOrGMainVo prpDScheduleDOrGMainVo = new PrpDScheduleDOrGMainVo();
	        List<PrpLCMainVo> prpLCMainVoList = policyViewService.getPolicyAllInfo(prpLRegistVo.getRegistNo());
	        prpDScheduleDOrGMainVo.setScheduleDOrG(scheduleDOrG);
	        prpDScheduleDOrGMainVo.setPrpLCMainVoList(prpLCMainVoList);
	        prpDScheduleDOrGMainVo.setPrpLRegistVo(prpLRegistVo);
	        prpDScheduleDOrGMainVo.setPrpLScheduleTaskVo(prpLScheduleTaskVo);
	        prpDScheduleDOrGMainVo.setSchType(schType);
	        prpDScheduleDOrGMainVo.setCheckAreaCode(checkAreaCode);
	        prpDScheduleDOrGMainVo.setLngXlatY(lngXlatY);
	        scheduleDOrG = scheduleTaskService.setScheduleDOrG(prpDScheduleDOrGMainVo);
			
			PrpLScheduleTaskVo selfScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(prpLRegistVo.getRegistNo());
	        scheduleDOrG.setCaseFlag("3");
	        scheduleDOrG.setOrderNo(prpLRegistVo.getPrpLRegistExt().getOrderNo());
	        if(IsSingleAccident.NOT.equals(powerFlag)){
	        	selfScheduleTaskVo.setIsAutoCheck(IsSingleAccident.NOT);
	        }
	        if("0".equals(prpLRegistVo.getSelfRegistFlag()) &&
"1".equals(selfScheduleTaskVo.getIsAutoCheck())){// 电话直赔
	        	scheduleDOrG.setIsMobileCase("0");
	        	scheduleDOrG.setCaseFlag("1");
	        }else if("1".equals(prpLRegistVo.getSelfRegistFlag()) &&
"1".equals(selfScheduleTaskVo.getIsAutoCheck())){// 微信自助理赔
	        	scheduleDOrG.setIsMobileCase("0");
	        	scheduleDOrG.setCaseFlag("2");
	        }else{			
			// 是否移动端案件
				PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.MobileCheck,vo.getComCode());
				Boolean isMobileCase = false;
				Boolean isMobileWhileListCase = wFMobileService.findWhileListCase(CodeConstants.WhiteList.ISMOBILE,prpLScheduleTaskVo.getScheduledUsercode());
			if(isMobileWhileListCase){// 移动端案件，不送民太安车童接口
					isMobileCase = true;
				}else{
					isMobileCase = sendMsgToMobileService.isMobileCase(prpLRegistVo, prpLScheduleTaskVo.getScheduledUsercode());
				}
				if("1".equals(configValueVo.getConfigValue())){
				if(isMobileCase){// 移动端案件
		                  scheduleDOrG.setIsMobileCase("1");
		              }else{
		                  scheduleDOrG.setIsMobileCase("0");
		              }
					}else{
		              scheduleDOrG.setIsMobileCase("0");
					}
			}
			
		// 任务id
			int id = 1;
			List<HandleScheduleReqScheduleItemDOrG> scheduleItemList = new ArrayList<HandleScheduleReqScheduleItemDOrG>();
			for(PrpLScheduleDefLossVo ItemsVo :prpLScheduleDefLosses){
				HandleScheduleReqScheduleItemDOrG scheduleItemDOrG =new HandleScheduleReqScheduleItemDOrG();
				
			// 传工作流taskid
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
				List<PrpLWfTaskVo> oldTaskVoList = wfTaskHandleService.findEndTask(prpLScheduleTaskVo.getRegistNo(), String.valueOf(ItemsVo.getId()), FlowNode.valueOf(subNodeCode));
				if(oldTaskVoList!=null && oldTaskVoList.size()>0){
					scheduleItemDOrG.setOriginalTaskId(String.valueOf(oldTaskVoList.get(0).getTaskId()));
				}
			scheduleItemDOrG.setSerialNo(String.valueOf(ItemsVo.getSerialNo()));// 添加SerialNo给移动端定损用
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
				if("3".equals(scheduleDOrG.getCaseFlag())){
					scheduleItemDOrG.setNextHandlerCode(prpLScheduleTaskVo.getScheduledUsercode());
					scheduleItemDOrG.setNextHandlerName(prpLScheduleTaskVo.getScheduledUsername());
					scheduleItemDOrG.setScheduleObjectId(prpLScheduleTaskVo.getScheduledComcode());
					scheduleItemDOrG.setScheduleObjectName(prpLScheduleTaskVo.getScheduledComname());
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
			String url = SpringProperties.getProperty("MClaimPlatform_URL_IN")+HANDLSCHEDDORGULE_URL_METHOD;
			HandleScheduleDOrGBackReqVo voS = mobileCheckService.getHandelScheduleDOrDUrl(reqVo,url);
		
		}
		
	// 理赔调度提交/改派提交接口（理赔请求快赔系统）调度初始化或者新增查勘
		public void setReassignments(PrpLScheduleTaskVo prpLScheduleTaskVo,
				String checkAreaCode, String lngXlatY,String schType,List<PrpLScheduleItemsVo> prpLScheduleItemses) throws Exception {
			
		// 获取报案信息
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLScheduleTaskVo.getRegistNo());
			PrpLCMainVo vo = prpLCMainService.findPrpLCMain(prpLScheduleTaskVo.getRegistNo(), prpLRegistVo.getPolicyNo());
			
			HandleScheduleDOrGReqVo reqVo = new HandleScheduleDOrGReqVo();
			
		HeadReq head = setHeadReq();// 设置头部信息
			
			HandleScheduleReqDOrGBody body = new HandleScheduleReqDOrGBody();
			HandleScheduleReqScheduleDOrG scheduleDOrG = new HandleScheduleReqScheduleDOrG();
			
	        PrpDScheduleDOrGMainVo prpDScheduleDOrGMainVo = new PrpDScheduleDOrGMainVo();
	        List<PrpLCMainVo> prpLCMainVoList = policyViewService.getPolicyAllInfo(prpLRegistVo.getRegistNo());
	        prpDScheduleDOrGMainVo.setScheduleDOrG(scheduleDOrG);
	        prpDScheduleDOrGMainVo.setPrpLCMainVoList(prpLCMainVoList);
	        prpDScheduleDOrGMainVo.setPrpLRegistVo(prpLRegistVo);
	        prpDScheduleDOrGMainVo.setPrpLScheduleTaskVo(prpLScheduleTaskVo);
	        prpDScheduleDOrGMainVo.setSchType(schType);
	        prpDScheduleDOrGMainVo.setCheckAreaCode(checkAreaCode);
	        prpDScheduleDOrGMainVo.setLngXlatY(lngXlatY);
	        scheduleDOrG = scheduleTaskService.setScheduleDOrG(prpDScheduleDOrGMainVo);
			
		// 是否移动端案件
			PrpLScheduleTaskVo selfScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(prpLRegistVo.getRegistNo());
	        scheduleDOrG.setCaseFlag("3");
	        scheduleDOrG.setOrderNo(prpLRegistVo.getPrpLRegistExt().getOrderNo());
	        if("0".equals(prpLRegistVo.getSelfRegistFlag()) &&
"1".equals(selfScheduleTaskVo.getIsAutoCheck())){// 电话直赔
	        	scheduleDOrG.setIsMobileCase("0");
	        	scheduleDOrG.setCaseFlag("1");
	        }else if("1".equals(prpLRegistVo.getSelfRegistFlag()) &&
"1".equals(selfScheduleTaskVo.getIsAutoCheck())){// 微信自助理赔
	        	scheduleDOrG.setIsMobileCase("0");
	        	scheduleDOrG.setCaseFlag("2");
	        }else{
				PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.MobileCheck,vo.getComCode());
				if("1".equals(configValueVo.getConfigValue())){
				    // 是否移动端案件
					String scheduledUsercode = "";
					if(StringUtils.isNotBlank(prpLScheduleTaskVo.getScheduledUsercode())){
						scheduledUsercode = prpLScheduleTaskVo.getScheduledUsercode();
					}else{
						scheduledUsercode = prpLScheduleTaskVo.getPersonScheduledUsercode();
					}
					Boolean isMobileWhileListCase = wFMobileService.findWhileListCase(CodeConstants.WhiteList.ISMOBILE,scheduledUsercode);
					if(isMobileWhileListCase){// 移动端案件，不送民太安车童接口
						scheduleDOrG.setIsMobileCase("1");
					}else{
						Boolean isMobileCase = sendMsgToMobileService.isMobileCase(prpLRegistVo, scheduledUsercode);
						if(isMobileCase){
							scheduleDOrG.setIsMobileCase("1");
						}else{
							PrpLWfMainVo lWfMainVo = wfMainService.findPrpLWfMainVoByRegistNo(prpLScheduleTaskVo.getRegistNo());
							if(lWfMainVo!=null){
							if("1".equals(lWfMainVo.getIsMobileCase())){// 移动端案件
							        scheduleDOrG.setIsMobileCase("1");
							    }else{
							        scheduleDOrG.setIsMobileCase("0");
							    }
							}else{
							    scheduleDOrG.setIsMobileCase("0");
							}
						}
					}
				}else{
					scheduleDOrG.setIsMobileCase("0");
				}
	        }
			scheduleDOrG.setRegistNo(prpLRegistVo.getRegistNo());
			if("reassig".equals(schType)){
				scheduleDOrG.setScheduleType("1");
			}else{
				scheduleDOrG.setScheduleType("0");
			}
			// 任务id
			String scheduleTaskId = "1";
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(prpLRegistVo.getRegistNo(), FlowNode.Chk.toString());
			if(prpLWfTaskVoList!=null && prpLWfTaskVoList.size()>0){
			// 流入时间降序排
				Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
				@Override
				public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
						return o2.getTaskInTime().compareTo(o1.getTaskInTime());
					}
				});
				scheduleTaskId=prpLWfTaskVoList.get(0).getTaskId().toString();
			}
			//查询工作流表人伤节点taskId
			String personTaskId = "1";
			 PrpLWfTaskVo wfTaskVo = wfTaskHandleService.findWftaskInByRegistnoAndSubnode(prpLRegistVo.getRegistNo(), "PLFirst");
			 if(wfTaskVo != null){
				 personTaskId = wfTaskVo.getTaskId().toString();
			 }
			
			List<HandleScheduleReqScheduleItemDOrG> scheduleItemList = new ArrayList<HandleScheduleReqScheduleItemDOrG>();
			for(PrpLScheduleItemsVo ItemsVo:prpLScheduleItemses){
				HandleScheduleReqScheduleItemDOrG scheduleItemDOrG = new HandleScheduleReqScheduleItemDOrG();
				if(ItemsVo.getItemType().equals("4")){// 人伤
					scheduleItemDOrG.setTaskId(personTaskId);
					scheduleItemDOrG.setNodeType("PLoss");
					if("3".equals(scheduleDOrG.getCaseFlag())){
						if(StringUtils.isNotBlank(prpLScheduleTaskVo.getPersonScheduledUsercode())){
							scheduleItemDOrG.setNextHandlerCode(prpLScheduleTaskVo.getPersonScheduledUsercode());
							scheduleItemDOrG.setNextHandlerName(prpLScheduleTaskVo.getPersonScheduledUsername());
							scheduleItemDOrG.setScheduleObjectId(prpLScheduleTaskVo.getPersonScheduledComcode());
							scheduleItemDOrG.setScheduleObjectName(prpLScheduleTaskVo.getPersonScheduledComname());
						}else{
							scheduleItemDOrG.setNextHandlerCode(prpLScheduleTaskVo.getScheduledUsercode());
							scheduleItemDOrG.setNextHandlerName(prpLScheduleTaskVo.getScheduledUsername());
							scheduleItemDOrG.setScheduleObjectId(prpLScheduleTaskVo.getScheduledComcode());
							scheduleItemDOrG.setScheduleObjectName(prpLScheduleTaskVo.getScheduledComname());
						}
						
					}else{
						scheduleItemDOrG.setNextHandlerCode("");
						scheduleItemDOrG.setNextHandlerName("");
						scheduleItemDOrG.setScheduleObjectId("");
						scheduleItemDOrG.setScheduleObjectName("");
					}
				}else{// 查勘
					scheduleItemDOrG.setTaskId(scheduleTaskId);
					scheduleItemDOrG.setNodeType("Check");
					if("3".equals(scheduleDOrG.getCaseFlag())){
						scheduleItemDOrG.setNextHandlerCode(prpLScheduleTaskVo.getScheduledUsercode());
						scheduleItemDOrG.setNextHandlerName(prpLScheduleTaskVo.getScheduledUsername());
						scheduleItemDOrG.setScheduleObjectId(prpLScheduleTaskVo.getScheduledComcode());
						scheduleItemDOrG.setScheduleObjectName(prpLScheduleTaskVo.getScheduledComname());
					}else{
						scheduleItemDOrG.setNextHandlerCode("");
						scheduleItemDOrG.setNextHandlerName("");
						scheduleItemDOrG.setScheduleObjectId("");
						scheduleItemDOrG.setScheduleObjectName("");
					}
				}
				scheduleItemDOrG.setIsObject("0");
				
				scheduleItemList.add(scheduleItemDOrG);
			}
			List<HandleScheduleReqScheduleItemDOrG> scheduleItemListResult = new ArrayList<HandleScheduleReqScheduleItemDOrG>();
			HandleScheduleReqScheduleItemDOrG scheduleItemResult = new HandleScheduleReqScheduleItemDOrG();
			HandleScheduleReqScheduleItemDOrG personScheduleItemResult = new HandleScheduleReqScheduleItemDOrG();
		// 只传查勘或者人伤
			for(HandleScheduleReqScheduleItemDOrG vos : scheduleItemList){
				if(vos.getNodeType().equals("PLoss")){
					personScheduleItemResult = vos;
				}
				if(vos.getNodeType().equals("Check")){
					scheduleItemResult = vos;
				}
			}
			if(personScheduleItemResult !=null && personScheduleItemResult.getNodeType() != null){
				scheduleItemListResult.add(personScheduleItemResult);
			}
			if(scheduleItemResult !=null && scheduleItemResult.getNodeType() != null){
				scheduleItemListResult.add(scheduleItemResult);
			}
			scheduleDOrG.setScheduleItemList(scheduleItemListResult);
			body.setScheduleDOrG(scheduleDOrG);
			reqVo.setHead(head);
			reqVo.setBody(body);
			
			//String xmlToSend = ClaimBaseCoder.objToXml(reqVo);
			String url = SpringProperties.getProperty("MClaimPlatform_URL_IN")+HANDLSCHEDDORGULE_URL_METHOD;
			HandleScheduleDOrGBackReqVo voS = mobileCheckService.getHandelScheduleDOrDUrl(reqVo,url);
			if(voS!=null){
				if(StringUtils.isNotBlank(voS.getBody().getScheduleSD().getOrderNo())){
					prpLRegistVo.getPrpLRegistExt().setOrderNo(voS.getBody().getScheduleSD().getOrderNo());
					registService.updatePrpLRegistExt(prpLRegistVo.getPrpLRegistExt());
				}
				
			}
			
		
		}
		
		
		
	// 理赔调度提交/改派提交接口（理赔请求快赔系统）改派的查勘
		public void setReassignmentCheck(PrpLScheduleTaskVo prpLScheduleTaskVo,
				String checkAreaCode, String lngXlatY,String schType,String flowTaskId,String powerFlag) throws Exception {
		// 获取报案信息
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLScheduleTaskVo.getRegistNo());
			
			HandleScheduleDOrGReqVo reqVo = new HandleScheduleDOrGReqVo();
			
		HeadReq head = setHeadReq();// 设置头部信息
			
			HandleScheduleReqDOrGBody body = new HandleScheduleReqDOrGBody();
			HandleScheduleReqScheduleDOrG scheduleDOrG = new HandleScheduleReqScheduleDOrG();
	        PrpLCMainVo vo = prpLCMainService.findPrpLCMain(prpLScheduleTaskVo.getRegistNo(), prpLRegistVo.getPolicyNo());
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.MobileCheck,vo.getComCode());

	        PrpDScheduleDOrGMainVo prpDScheduleDOrGMainVo = new PrpDScheduleDOrGMainVo();
	        List<PrpLCMainVo> prpLCMainVoList = policyViewService.getPolicyAllInfo(prpLRegistVo.getRegistNo());
	        prpDScheduleDOrGMainVo.setScheduleDOrG(scheduleDOrG);
	        prpDScheduleDOrGMainVo.setPrpLCMainVoList(prpLCMainVoList);
	        prpDScheduleDOrGMainVo.setPrpLRegistVo(prpLRegistVo);
	        prpDScheduleDOrGMainVo.setPrpLScheduleTaskVo(prpLScheduleTaskVo);
	        prpDScheduleDOrGMainVo.setSchType(schType);
	        prpDScheduleDOrGMainVo.setCheckAreaCode(checkAreaCode);
	        prpDScheduleDOrGMainVo.setLngXlatY(lngXlatY);
	        scheduleDOrG = scheduleTaskService.setScheduleDOrG(prpDScheduleDOrGMainVo);
				
			PrpLScheduleTaskVo selfScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(prpLRegistVo.getRegistNo());
	        scheduleDOrG.setCaseFlag("3");
	        scheduleDOrG.setOrderNo(prpLRegistVo.getPrpLRegistExt().getOrderNo());
	        if(IsSingleAccident.NOT.equals(powerFlag)){
	        	selfScheduleTaskVo.setIsAutoCheck(IsSingleAccident.NOT);
	        }
	        if("0".equals(prpLRegistVo.getSelfRegistFlag())&&
"1".equals(selfScheduleTaskVo.getIsAutoCheck())){// 电话直赔
	        	scheduleDOrG.setIsMobileCase("0");
	        	scheduleDOrG.setCaseFlag("1");
	        }else if("1".equals(prpLRegistVo.getSelfRegistFlag()) &&
"1".equals(selfScheduleTaskVo.getIsAutoCheck())){// 微信自助理赔
	        	scheduleDOrG.setIsMobileCase("0");
	        	scheduleDOrG.setCaseFlag("2");
	        }else{
			// 是否移动端案件
				Boolean isMobileCase = false;
				Boolean isMobileWhileListCase = wFMobileService.findWhileListCase(CodeConstants.WhiteList.ISMOBILE,prpLScheduleTaskVo.getScheduledUsercode());
			if(isMobileWhileListCase){// 移动端案件，不送民太安车童接口
					isMobileCase = true;
				}else{
					isMobileCase = sendMsgToMobileService.isMobileCase(prpLRegistVo, prpLScheduleTaskVo.getScheduledUsercode());
				}
	            if("1".equals(configValueVo.getConfigValue())){
				if(isMobileCase){// 移动端案件
	                    scheduleDOrG.setIsMobileCase("1");
	                }else{
	                    scheduleDOrG.setIsMobileCase("0");
	                }
	            }else{
	                scheduleDOrG.setIsMobileCase("0");
	            }
	        }
            
			scheduleDOrG.setScheduleType("1");
		// 二期改造start
			//setPolicyNo(scheduleDOrG,prpLRegistVo.getRegistNo());
		// 二期改造end
	        
		// 任务id
			int id = 1;
			String taskId = "";
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(prpLRegistVo.getRegistNo(), FlowNode.Chk.toString());
			if(prpLWfTaskVoList!=null && prpLWfTaskVoList.size()>0){
			// 流入时间降序排
				Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
				@Override
				public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
						return o2.getTaskInTime().compareTo(o1.getTaskInTime());
					}
				});
				taskId=prpLWfTaskVoList.get(0).getTaskId().toString();
			}
			List<HandleScheduleReqScheduleItemDOrG> scheduleItemList = new ArrayList<HandleScheduleReqScheduleItemDOrG>();
				HandleScheduleReqScheduleItemDOrG scheduleItemDOrG =new HandleScheduleReqScheduleItemDOrG();
				scheduleItemDOrG.setTaskId(taskId);
				scheduleItemDOrG.setOriginalTaskId(flowTaskId);
				scheduleItemDOrG.setNodeType("Check");
				if("3".equals(scheduleDOrG.getCaseFlag())){
					scheduleItemDOrG.setNextHandlerCode(prpLScheduleTaskVo.getScheduledUsercode());
					scheduleItemDOrG.setNextHandlerName(prpLScheduleTaskVo.getScheduledUsername());
					scheduleItemDOrG.setScheduleObjectId(prpLScheduleTaskVo.getScheduledComcode());
					scheduleItemDOrG.setScheduleObjectName(prpLScheduleTaskVo.getScheduledComname());
				}else{
					scheduleItemDOrG.setNextHandlerCode("");
					scheduleItemDOrG.setNextHandlerName("");
					scheduleItemDOrG.setScheduleObjectId("");
					scheduleItemDOrG.setScheduleObjectName("");
				}
				
				scheduleItemDOrG.setIsObject("0");
				scheduleItemList.add(scheduleItemDOrG);
			scheduleDOrG.setScheduleItemList(scheduleItemList);
			body.setScheduleDOrG(scheduleDOrG);
			reqVo.setHead(head);
			reqVo.setBody(body);
			String url = SpringProperties.getProperty("MClaimPlatform_URL_IN")+HANDLSCHEDDORGULE_URL_METHOD;
			HandleScheduleDOrGBackReqVo voS = mobileCheckService.getHandelScheduleDOrDUrl(reqVo,url);
		
		}
		
		@RequestMapping(value = "/switchMapGpsCode.do")
		@ResponseBody
		public AjaxResult switchMapGpsCode(String areaCode) throws Exception {
			AjaxResult ajaxResult = new AjaxResult();
			List<SysAreaDictVo> sysAreaDictVo = areaDictService.findAreaCode(areaCode);
			if(sysAreaDictVo != null && sysAreaDictVo.size()>0){
				ajaxResult.setStatusText(sysAreaDictVo.get(0).getGpsCode());
			}
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
			return ajaxResult;
		}
		
		@RequestMapping(value = "/findSysUserVo.do")
		@ResponseBody
		public AjaxResult findSysUserVo(String userCode) throws Exception {
			AjaxResult ajaxResult = new AjaxResult();
			SysUserVo sysUserVo = sysUserService.findByUserCode(userCode);
			if(StringUtils.isNotBlank(sysUserVo.getMobile())){
				ajaxResult.setStatusText(sysUserVo.getMobile());
			}
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
			return ajaxResult;
		}
		
		public HandleScheduleReqScheduleDOrG setPolicyNo(HandleScheduleReqScheduleDOrG scheduleDOrG,String registNo){
		    List<PrpLCMainVo> cMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
            for(PrpLCMainVo cMainVo : cMainVoList){
                if("12".equals(cMainVo.getRiskCode().subSequence(0,2))){
                    scheduleDOrG.setBusiPolicyNo(cMainVo.getPolicyNo());
                }else{
                    scheduleDOrG.setPolicyNo(cMainVo.getPolicyNo());
                }
            }
            return scheduleDOrG;
		}

		
		/**
	 * 初始化短信记录页面
	 * 
	 * <pre></pre>
	 * @param request
	 * @return
	 * @modified: ☆LinYi(2017年8月18日 下午3:41:50): <br>
	 */
		@RequestMapping(value = "/initMsgRecord.do")
		public ModelAndView initMsgRecord(HttpServletRequest request){
		    String businessNo = request.getParameter("businessNo");
		    ModelAndView mv = new ModelAndView();
		    List<PrpsmsMessageVo> smsMessageList = msgModelService.findPrpSmsMessageByBusinessNo(businessNo);
		    mv.addObject("smsMessageList", smsMessageList);
            mv.setViewName("schedule/scheduleEdit/MsgRecord");
            return mv;
		}
		
		/**
	 * 根据id查询补发信息内容
	 * 
	 * <pre></pre>
	 * @param request
	 * @return
	 * @modified: ☆LinYi(2017年8月18日 下午3:41:38): <br>
	 */
        @RequestMapping(value = "/checkMsg.do")
        public ModelAndView checkMsg(HttpServletRequest request){
            String misId = request.getParameter("misId");
            ModelAndView mv = new ModelAndView();
            PrpsmsMessageVo smsMessage = msgModelService.findPrpSmsMessageByPrimaryKey(new BigDecimal(misId));
            mv.addObject("smsMessage", smsMessage);
            mv.setViewName("schedule/scheduleEdit/CheckMsg");
            return mv;
        }
        
        /**
	 * 发送短信
	 * 
	 * <pre></pre>
	 * @param request
	 * @modified: ☆LinYi(2017年8月18日 下午3:41:21): <br>
	 */
        @RequestMapping(value = "/sendMsg.do")
        @ResponseBody
        public AjaxResult sendMsg(HttpServletRequest request){
            AjaxResult ajaxResult = new AjaxResult();
            SysUserVo sysUserVo = WebUserUtils.getUser();
            String businessNo = request.getParameter("businessNo");
            String phoneCode = request.getParameter("phoneCode");
            String sendText =  request.getParameter("sendText");
            String sendNodecode = request.getParameter("sendNodecode");
            Date sendTime0 = new Date();
            String status="";
            boolean  index=false;
            index = smsService.sendSMSContent(businessNo, phoneCode, sendText, sysUserVo.getUserCode(), sysUserVo.getComCode(),sendTime0);
            if(index){
			status = "1";// 推送短信平台成功
            }else{
			status = "0";// 推送短信平台失败
            }
		Date trueSendTime = new Date();// 真实发送时间
            Date sendTime1=null;
            Date nowTime=new Date();
            if(sendTime0!=null){
			sendTime1 = DateUtils.addMinutes(sendTime0, -5);// 短信平台发送时间
               if(nowTime.getTime()<sendTime1.getTime()){
                    trueSendTime=sendTime1;
                }
            }
            PrpsmsMessageVo prpsmsMessageVo=new PrpsmsMessageVo();
            prpsmsMessageVo.setBusinessNo(businessNo);
            prpsmsMessageVo.setComCode(sysUserVo.getComCode());
            prpsmsMessageVo.setCreateTime(nowTime);
            prpsmsMessageVo.setPhoneCode(phoneCode);
            prpsmsMessageVo.setSendNodecode(sendNodecode);
            prpsmsMessageVo.setSendText(sendText);
            prpsmsMessageVo.setTruesendTime(trueSendTime);
            prpsmsMessageVo.setUserCode(sysUserVo.getUserCode());
            prpsmsMessageVo.setBackTime(nowTime);
            prpsmsMessageVo.setStatus(status);

            msgModelService.saveorUpdatePrpSmsMessage(prpsmsMessageVo);
            
            ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
            
            return ajaxResult;
        }
        public void setYwId(String registNo,List<Long> defLossVoIds,List<Long> taskVoIds,List<Long> itemVoIds,Map<String,String> idTaskIdMap) {
    		List<PrpLWfTaskVo> prpLWfTaskInVos = wfFlowQueryService.findUnAcceptTask(registNo,  FlowNode.Check.name(),FlowNode.DLoss.name(),FlowNode.PLoss.name());
    		
    		if (prpLWfTaskInVos != null && prpLWfTaskInVos.size() > 0) {
    			for (int i = 0; i < prpLWfTaskInVos.size(); i ++) {
    				if (FlowNode.DLoss.equals(prpLWfTaskInVos.get(i).getNodeCode()) && WorkStatus.INIT.equals(prpLWfTaskInVos.get(i).getWorkStatus())) {
    					if(FlowNode.DLProp.equals(prpLWfTaskInVos.get(i).getSubNodeCode())||FlowNode.DLCar.equals(prpLWfTaskInVos.get(i).getSubNodeCode())){
    						defLossVoIds.add(Long.valueOf(prpLWfTaskInVos.get(i).getHandlerIdKey()));
    						idTaskIdMap.put(prpLWfTaskInVos.get(i).getHandlerIdKey(), prpLWfTaskInVos.get(i).getTaskId().toString());
    					}
    					
    				} else if (FlowNode.PLoss.equals(prpLWfTaskInVos.get(i).getNodeCode())&& WorkStatus.INIT.equals(prpLWfTaskInVos.get(i).getWorkStatus())) {
    					if(FlowNode.PLFirst.equals(prpLWfTaskInVos.get(i).getSubNodeCode())){
    						itemVoIds.add(Long.valueOf(prpLWfTaskInVos.get(i).getHandlerIdKey()));
    						idTaskIdMap.put(prpLWfTaskInVos.get(i).getHandlerIdKey(), prpLWfTaskInVos.get(i).getTaskId().toString());
    					}
    				} else if(FlowNode.Chk.equals(prpLWfTaskInVos.get(i).getSubNodeCode())&& WorkStatus.INIT.equals(prpLWfTaskInVos.get(i).getWorkStatus())){
    						taskVoIds.add(Long.valueOf(prpLWfTaskInVos.get(i).getHandlerIdKey()));
    						idTaskIdMap.put(prpLWfTaskInVos.get(i).getHandlerIdKey(), prpLWfTaskInVos.get(i).getTaskId().toString());
    				}
    			}
    		}
        }
        public Boolean isSelfClaimCase(PrpLRegistVo registVo){
		Boolean selfClaimFlag = false;// 自助理赔案子
    		if(StringUtils.isNotBlank(registVo.getSelfClaimFlag()) &&
    				IsSingleAccident.YES.equals(registVo.getSelfClaimFlag())){
    			selfClaimFlag = true;
    		}
			return selfClaimFlag;
        }
}