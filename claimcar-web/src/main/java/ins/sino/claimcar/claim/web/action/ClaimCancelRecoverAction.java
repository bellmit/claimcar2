package ins.sino.claimcar.claim.web.action;

import ins.framework.web.AjaxResult;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.FlowStatus;
import ins.sino.claimcar.carchild.service.CarchildService;
import ins.sino.claimcar.carchild.vo.RegistInformationVo;
import ins.sino.claimcar.carchild.vo.RevokeRestoreBodyVo;
import ins.sino.claimcar.carchild.vo.RevokeRestoreInfoReqVo;
import ins.sino.claimcar.carchild.vo.RevokeRestoreInfoResVo;
import ins.sino.claimcar.carchild.vo.RevokeRestoreTaskInfoVo;
import ins.sino.claimcar.carchildCommon.vo.CarchildHeadVo;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.claim.service.ClaimCancelRecoverService;
import ins.sino.claimcar.claim.service.ClaimCancelService;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ClaimSummaryService;
import ins.sino.claimcar.claim.service.ClaimTaskExtService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLClaimCancelVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLcancelTraceVo;
import ins.sino.claimcar.claim.vo.PrpLrejectClaimTextVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossprop.service.PropLossService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.platform.service.ClaimToPaltformService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.rule.service.ClaimRuleApiService;
import ins.sino.claimcar.rule.vo.VerifyClaimCancelRuleVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/claimRecover")
public class ClaimCancelRecoverAction {
    private static Logger logger = LoggerFactory.getLogger(ClaimCancelRecoverAction.class);
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private ClaimService claimService;
	@Autowired
	private ClaimCancelService claimCancelService;
	@Autowired
	private ClaimCancelRecoverService claimCancelRecoverService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private ClaimTaskExtService claimTaskExtService;
	@Autowired
	private ClaimRuleApiService claimRuleApiService;//规则
	@Autowired
	ClaimSummaryService claimSummaryService;
	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	ClaimToPaltformService claimToPaltformService;
	@Autowired
	private AssignService assignService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	@Autowired
	WfMainService wfMainService;
	@Autowired
	CarchildService carchildService;
	@Autowired
	LossCarService lossCarService;
	@Autowired
	PropLossService propLossService;
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	ClaimInterfaceLogService claimInterfaceLogService;
	@Autowired
	ScheduleTaskService scheduleTaskService;
	//公共按钮发起注销拒赔恢复
	@RequestMapping("/claimCancelApply.do")
	public ModelAndView claimEdit(@RequestParam(value = "registNo") String registNo,@RequestParam(value = "taskId") String taskId) {
		List<PrpLClaimVo> prpLClaimVoList = claimService.findClaimListByRegistNo(registNo);
		List<PrpLCMainVo> prpLCMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);
		if (prpLClaimVoList != null) {
			for (PrpLClaimVo prpLClaimVo : prpLClaimVoList) {
				for (PrpLCMainVo prpLCMainVo : prpLCMainVoList) {
					if (prpLClaimVo.getPolicyNo().equals(prpLCMainVo.getPolicyNo())) {
						prpLClaimVo.setInsuredName(prpLCMainVo.getInsuredName());
						prpLClaimVo.setStartDate(prpLCMainVo.getStartDate());
						prpLClaimVo.setEndDate(prpLCMainVo.getEndDate());
					}
				}
			}
		}
		List<PrpLcancelTraceVo> prpLcancelTraceVos = new ArrayList<PrpLcancelTraceVo>();
		if(prpLClaimVoList != null) {
			//添加个立案的条件
			for (PrpLClaimVo prpLClaimVo : prpLClaimVoList) {
				if(prpLClaimVo.getValidFlag()!=null&&prpLClaimVo.getValidFlag().equals("0")){
					PrpLcancelTraceVo prpLcancelTraceVo= claimCancelRecoverService.findByClaimNo(prpLClaimVo.getClaimNo());
					if(prpLcancelTraceVo != null){
					prpLcancelTraceVos.add(prpLcancelTraceVo);
					}
				}
			}
		}
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("taskId", taskId);
		modelAndView.addObject("prpLcancelTraceVos", prpLcancelTraceVos);
		modelAndView.setViewName("claimCRecover/claimCRecoverApply");
		return modelAndView;
	}

	// 注销拒赔恢复申请发起
	@RequestMapping("/claimCancelInit.do")
	public ModelAndView claimCancelInit(@RequestParam(value = "claimNo") String claimNo,
			@RequestParam(value = "taskId") String taskId,
			@RequestParam(value = "workStatus") String workStatus,
			@RequestParam(value = "handlerIdKey") String handlerIdKey) {
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(claimNo);
		ModelAndView modelAndView = new ModelAndView();
		
		String handlerStatusById = "";
		//if(!StringUtils.isNotBlank(taskId)){
		if("c".equals(taskId)){
			String subNodeCode = FlowNode.ClaimBI.name();
			if ("1101".equals(prpLClaimVo.getRiskCode())) {
				subNodeCode = FlowNode.ClaimCI.name();
			}
			PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTaskByAny(prpLClaimVo.getRegistNo(),claimNo, subNodeCode, CodeConstants.HandlerStatus.CANCEL);
			taskId = prpLWfTaskVo.getTaskId().toString();
			
			
			
		}else{
			//查询工作流状态
			PrpLWfTaskVo prpLWfTaskVo= wfTaskHandleService.queryTaskForHandlerStatus(Double.valueOf(taskId));
			handlerStatusById = prpLWfTaskVo.getHandlerStatus();
		}
		
		if (prpLClaimVo.getRegistNo() != null) {
			String registNo = prpLClaimVo.getRegistNo();
			PrpLCMainVo prpLCMainVo = policyViewService.getRegistNoAndRiskCodeInfo(registNo,prpLClaimVo.getRiskCode());
			if (prpLCMainVo != null) {
				List<PrpLCItemCarVo> prpCItemCars = prpLCMainVo.getPrpCItemCars();
				if (prpCItemCars != null) {
					for(PrpLCItemCarVo vo : prpCItemCars){
						if(vo.getRiskCode().equals(prpLClaimVo.getRiskCode())){
							modelAndView.addObject("prpCItemCar", vo);
						}
					}
					
				}
				//赋值出险次数
				BigDecimal bigDecimal = new BigDecimal(claimSummaryService.claimNumber(prpLCMainVo.getPolicyNo()));
				prpLCMainVo.setRegistTimes(bigDecimal);
			}

			PrpLRegistExtVo prpLRegistExtVo = registQueryService.getPrpLRegistExtInfo(registNo);
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
			modelAndView.addObject("prpLCMainVo", prpLCMainVo);
			modelAndView.addObject("prpLRegistExtVo", prpLRegistExtVo);
			//脱敏处理
			//reportorPhone
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);
			if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){//开关 
				prpLRegistVo.setReportorPhone(DataUtils .replacePrivacy(prpLRegistVo.getReportorPhone()));
			
			}
			
			modelAndView.addObject("prpLRegistVo", prpLRegistVo);
		}
		modelAndView.addObject("prpLClaimVo", prpLClaimVo);
		modelAndView.addObject("taskId", taskId);
		//
		BigDecimal traceIds = claimCancelRecoverService.findId(prpLClaimVo.getRiskCode(), claimNo);
		if(traceIds==null){
		BigDecimal traceId = claimCancelService.findId(prpLClaimVo.getRiskCode(), claimNo);
		// 判断是否已经申请注销拒赔恢复
		
			PrpLcancelTraceVo prpLcancelTraceVo = claimCancelRecoverService.findByCancelTraceId(traceId);
			
			modelAndView.addObject("id", traceId);
			modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
			if (prpLcancelTraceVo.getFlags().trim().equals("2")) {
				// 恢复提交
				modelAndView.addObject("Status", "2");
				modelAndView.setViewName("claimCRecover/claimCancelApplyEdits");
			} else if (prpLcancelTraceVo.getFlags().trim().equals("3")) {
				// 恢复暂存
				modelAndView.addObject("Status", "3");
				modelAndView.setViewName("claimCRecover/claimCancelApplyEdits");
			}
			//恢复再次发起界面
			else {
				//初始化
				modelAndView.setViewName("claimCRecover/claimCancelApplyEdit");
			}
		
		}else{
			PrpLcancelTraceVo prpLcancelTraceVo = claimCancelRecoverService.findByCancelTraceId(traceIds);
			//==========
			PrpLcancelTraceVo cancelTraceVo = claimCancelService.findPrpLcancelTraceVo(prpLClaimVo.getRiskCode(), claimNo,"02 ");
			//============开始
			if(cancelTraceVo!=null){
				if(!"c".equals(handlerIdKey)){
				//if(StringUtils.isNotBlank(handlerIdKey)){
					PrpLcancelTraceVo cancelTraceVoByIdKey = new PrpLcancelTraceVo();
					cancelTraceVoByIdKey = this.findByHandlerIdKey(handlerIdKey);
					if(cancelTraceVoByIdKey!=null){
						prpLcancelTraceVo = cancelTraceVoByIdKey;
					}else{
						prpLcancelTraceVo = cancelTraceVo;
					}
				}else{
					prpLcancelTraceVo = cancelTraceVo;
				}
			}
			//=========
			
			List<PrpLrejectClaimTextVo> prpLrejectClaimTextVos = claimCancelRecoverService.findById(traceIds);
			List<PrpLrejectClaimTextVo> prpLrejectClaimTextVoList = new ArrayList<PrpLrejectClaimTextVo>();
			if (prpLrejectClaimTextVos != null) {
				for (int i = 0; i < prpLrejectClaimTextVos.size(); i++) {
					if (prpLrejectClaimTextVos.get(i).getDescription() != null) {
						prpLrejectClaimTextVoList.add(prpLrejectClaimTextVos.get(i));
					}
				}
			}
			//modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
			
			
			if (prpLcancelTraceVo.getFlags().trim().equals("2")&&!workStatus.equals("6")) {
				// 恢复提交
				modelAndView.addObject("Status", "2");
				//审核/退回意见列表
				modelAndView.addObject("id", traceIds);
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				modelAndView.setViewName("claimCRecover/claimCancelApplyEdits");
			} else if (prpLcancelTraceVo.getFlags().trim().equals("3")) {
				// 恢复暂存
				modelAndView.addObject("Status", "3");
				modelAndView.addObject("id", traceIds);
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				//审核/退回意见列表
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				modelAndView.setViewName("claimCRecover/claimCancelApplyEdits");
			} else if(prpLcancelTraceVo.getFlags().trim().equals("4") && workStatus.equals("3")){
				//恢复显示claimCancelApplyEdit界面
				/*modelAndView.addObject("Status", "4");//初始化
				modelAndView.setViewName("claimCRecover/claimCancelApplyEdit");*/
				// 恢复提交
				modelAndView.addObject("id", traceIds);
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				modelAndView.addObject("Status", "2");
				//审核/退回意见列表
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				modelAndView.setViewName("claimCRecover/claimCancelApplyEdits");
			}else if("3".equals(handlerStatusById)) {
				// 恢复提交
				modelAndView.addObject("Status", "2");
				//审核/退回意见列表
				modelAndView.addObject("id", traceIds);
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				modelAndView.setViewName("claimCRecover/claimCancelApplyEdits");
			} else if(workStatus.equals("6")){
				modelAndView.addObject("Status", "3");//提交
				modelAndView.addObject("id", traceIds);
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				modelAndView.setViewName("claimCRecover/claimCancelApplyEdits");
				//审核/退回意见列表
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				return modelAndView;
			}else {
				//初始化
				modelAndView.setViewName("claimCRecover/claimCancelApplyEdit");
			}
		}
		return modelAndView;
	}

	// 公共按钮发起暂存
	@RequestMapping("/claimInitZhanCun.do")
	@ResponseBody
	public void claimInitZhanCun(PrpLcancelTraceVo prpLcancelTraceVo) {
		BigDecimal traceIds = claimCancelRecoverService.findId(prpLcancelTraceVo.getRiskCode(), prpLcancelTraceVo.getClaimNo());
		SysUserVo userVo = WebUserUtils.getUser();
		if (traceIds == null) {
			claimCancelRecoverService.zhanCun(prpLcancelTraceVo,userVo);
			this.claimInitZhanCunToF(prpLcancelTraceVo);
		} else {
			PrpLcancelTraceVo prpLcancelTraceDVo = claimCancelService.findByCancelTraceId(traceIds);
			if(prpLcancelTraceDVo.getFlags().trim().equals("4")){
				claimCancelRecoverService.zhanCun(prpLcancelTraceVo,userVo);
				this.claimInitZhanCunToF(prpLcancelTraceVo);
			}else{
				claimCancelRecoverService.claimInitZhanC(prpLcancelTraceVo);
			}
			
		}
	}
	
	
	// 公共按钮发起暂存
	@RequestMapping("/claimInitZhanC.do")
	@ResponseBody
	public void claimInitZhanC(PrpLcancelTraceVo prpLcancelTraceVo) {
		claimCancelRecoverService.claimInitZhanC(prpLcancelTraceVo);
	}

	// 注销拒赔申请处理
	@RequestMapping("/claimCancelSave.do")
	@ResponseBody
	public void claimCancelSave(PrpLcancelTraceVo prpLcancelTraceVo) {
		
		BigDecimal traceId = claimCancelRecoverService.findId(prpLcancelTraceVo.getRiskCode(),prpLcancelTraceVo.getClaimNo());
					//this.setUpdate(prpLcancelTraceVo);
					
		
		//BigDecimal traceId = claimCancelService.findId(prpLcancelTraceVo.getRiskCode(),prpLcancelTraceVo.getClaimNo());
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
		PrpLWfTaskVo vo = new PrpLWfTaskVo();
		String workStatus = "";
		if(StringUtils.isNotBlank(String.valueOf(prpLcancelTraceVo.getTaskId()))){
			vo = wfTaskHandleService.queryTaskForHandlerStatus(prpLcancelTraceVo.getTaskId());
			workStatus = vo.getWorkStatus();
		}
		if(traceId==null){
			this.cancelApply(prpLcancelTraceVo, prpLClaimVo, "2",vo,FlowNode.ReCanApp);
		}else{
			this.cancelApply(prpLcancelTraceVo, prpLClaimVo, prpLcancelTraceVo.getFlags().trim(),vo,FlowNode.ReCanApp);
		}
		
		/*if (traceId == null) {
			this.setSave(prpLcancelTraceVo);
		} else {
			PrpLcancelTraceVo cancelTraceVo = claimCancelService.findByCancelTraceId(traceId);
			//判断是否恢复立案调用setSave
			if(cancelTraceVo.getFlags().trim().equals("4")){
				this.setSave(prpLcancelTraceVo);
		}else{
			this.setUpdate(prpLcancelTraceVo);
		}
			// 判断是否恢复立案调用setSave
		}*/
	}
	
	private void setSave(PrpLcancelTraceVo prpLcancelTraceVo) {
		SysUserVo userVo = WebUserUtils.getUser();
		BigDecimal A = claimCancelRecoverService.save(prpLcancelTraceVo,userVo,FlowNode.ReCanApp.getName());
		Long id = A.longValue();
		String subNodeCode = FlowNode.ClaimBI.name();
		if ("1101".equals(prpLcancelTraceVo.getRiskCode().trim())) {
			subNodeCode = FlowNode.ClaimCI.name();
		}
		// 提交工作流 regist subnodecode
		PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTaskId(prpLcancelTraceVo.getFlowTask(), subNodeCode);
		// 提交工作流
		WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
		taskVo.setRegistNo(prpLWfTaskVo.getRegistNo());
		taskVo.setHandlerIdKey(id.toString());
		taskVo.setClaimNo(prpLWfTaskVo.getClaimNo());
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(prpLWfTaskVo.getFlowId());

		//submitVo.setCurrentNode(FlowNode.valueOf(subNodeCode));
		submitVo.setCurrentNode(FlowNode.ReCanApp);
		String policyNoComCode = policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo());
		submitVo.setAssignCom(policyNoComCode);
		submitVo.setComCode(policyNoComCode);
		//submitVo.setComCode(WebUserUtils.getComCode());
		submitVo.setTaskInKey(prpLWfTaskVo.getRegistNo());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
		submitVo.setHandleruser(WebUserUtils.getUserCode());
		PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
		pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
		pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
		pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
		pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
		
		pClaimCancelVo.setRecoverReason(prpLcancelTraceVo.getCancelCode());//注销/拒赔恢复原因
		pClaimCancelVo.setClaimRecoverTime(new Date());//注销/拒赔恢复时间
		submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
		submitVo.setNextNode(FlowNode.END);
		submitVo.setHandleIdKey(id.toString());
		wfTaskHandleService.submitClaimTask(taskVo, submitVo,pClaimCancelVo);
		submitVo.setNextNode(FlowNode.ReCanVrf_LV1);
		wfTaskHandleService.addCancelTask(taskVo, submitVo, pClaimCancelVo);
	}
	
	private void setUpdate(PrpLcancelTraceVo prpLcancelTraceVo) {
		SysUserVo userVo = WebUserUtils.getUser();
		BigDecimal A = claimCancelRecoverService.updates(prpLcancelTraceVo,userVo,FlowNode.ReCanApp.getName());
		Long id = A.longValue();
		String subNodeCode = FlowNode.ClaimBI.name();
		if ("1101".equals(prpLcancelTraceVo.getRiskCode().trim())) {
			subNodeCode = FlowNode.ClaimCI.name();
		}
		// 提交工作流 regist subnodecode
		PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTaskId(
				prpLcancelTraceVo.getFlowTask(), subNodeCode);
		// 提交工作流
		WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
		taskVo.setRegistNo(prpLWfTaskVo.getRegistNo());
		taskVo.setHandlerIdKey(id.toString());

		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(prpLWfTaskVo.getFlowId());

		//submitVo.setCurrentNode(FlowNode.valueOf(subNodeCode));
		submitVo.setCurrentNode(FlowNode.ReCanApp);
		submitVo.setNextNode(FlowNode.ReCanVrf_LV1);
		String policyNoComCode = policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo());
		submitVo.setAssignCom(policyNoComCode);
		submitVo.setHandleruser(WebUserUtils.getUserCode());
		submitVo.setComCode(policyNoComCode);
		//submitVo.setComCode(WebUserUtils.getComCode());
		submitVo.setTaskInKey(prpLWfTaskVo.getRegistNo());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		//submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
		submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
		PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
		pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
		pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
		pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
		pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
		pClaimCancelVo.setRecoverReason(prpLcancelTraceVo.getCancelCode());//注销/拒赔恢复原因
		pClaimCancelVo.setClaimRecoverTime(new Date());//注销/拒赔恢复时间
		submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
		submitVo.setNextNode(FlowNode.END);
		submitVo.setHandleIdKey(id.toString());
		wfTaskHandleService.submitClaimTask(taskVo, submitVo,pClaimCancelVo);
		//wfTaskHandleService.submitSimpleTask(taskVo, submitVo);
		submitVo.setNextNode(FlowNode.ReCanVrf_LV1);
		wfTaskHandleService.addCancelTask(taskVo, submitVo, pClaimCancelVo);
	}

	// 拒赔申请处理
	@RequestMapping("/claimCancelJuPei.do")
	@ResponseBody
	public void claimCancelJuPei(PrpLcancelTraceVo prpLcancelTraceVo) {
		BigDecimal traceId = claimCancelRecoverService.findId(prpLcancelTraceVo.getRiskCode(),prpLcancelTraceVo.getClaimNo());
		if (traceId == null) {
			SysUserVo userVo = WebUserUtils.getUser();
			BigDecimal A = claimCancelRecoverService.save(prpLcancelTraceVo,userVo,FlowNode.VClaim.getName());
			Long id = A.longValue();
			String subNodeCode = FlowNode.ClaimBI.name();
			if ("1101".equals(prpLcancelTraceVo.getRiskCode().trim())) {
				subNodeCode = FlowNode.ClaimCI.name();
			}
			// 提交工作流 regist subnodecode
			PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTaskId(prpLcancelTraceVo.getFlowTask(), subNodeCode);
			// 提交工作流
			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
			taskVo.setRegistNo(prpLWfTaskVo.getRegistNo());
			taskVo.setHandlerIdKey(id.toString());

			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			submitVo.setFlowId(prpLWfTaskVo.getFlowId());

			submitVo.setCurrentNode(FlowNode.valueOf(subNodeCode));
			submitVo.setNextNode(FlowNode.VClaim);
			String policyNoComCode = policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo());
			submitVo.setAssignCom(policyNoComCode);
			submitVo.setComCode(policyNoComCode);
			//submitVo.setComCode(WebUserUtils.getComCode());
			submitVo.setTaskInKey(prpLWfTaskVo.getRegistNo());
			submitVo.setTaskInUser(WebUserUtils.getUserCode());
			submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
			PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
			pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
			pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
			PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
			pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setRecoverReason(prpLcancelTraceVo.getCancelCode());//注销/拒赔恢复原因
			pClaimCancelVo.setClaimRecoverTime(new Date());//注销/拒赔恢复时间
			wfTaskHandleService.addCancelTask(taskVo, submitVo, pClaimCancelVo);

		}
	}

	// 分公司审核点击立案号显示的界面
	@RequestMapping("/claimCancelCheckInit.do")
	public ModelAndView claimCancelCheckInit(
			@RequestParam(value = "claimNo") String claimNo,
			@RequestParam(value = "types") String types,
			@RequestParam(value = "flowTaskId") String flowTaskId,
			String handlerStatus,String subNodeCode) {
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(claimNo);
		ModelAndView modelAndView = new ModelAndView();
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.valueOf(flowTaskId));
		//查询工作流状态
		//String handlerStatusById = wfTaskHandleService.queryTaskForHandlerStatus(Double.valueOf(flowTaskId));
		//查询工作流状态
		PrpLWfTaskVo prpLWfTaskVo= wfTaskHandleService.queryTaskForHandlerStatus(Double.valueOf(flowTaskId));
		String handlerStatusById = prpLWfTaskVo.getHandlerStatus();
		if(handlerStatusById != "" && handlerStatusById != null){
			handlerStatus = handlerStatusById;
		}
		if (prpLClaimVo.getRegistNo() != null) {
			String registNo = prpLClaimVo.getRegistNo();
			PrpLCMainVo prpLCMainVo = policyViewService.getRegistNoInfo(registNo);
			if (prpLCMainVo != null) {
				List<PrpLCItemCarVo> prpCItemCars = prpLCMainVo.getPrpCItemCars();
				if (prpCItemCars != null) {
					modelAndView.addObject("prpCItemCar", prpCItemCars.get(0));
				}
				//赋值出险次数
				BigDecimal bigDecimal = new BigDecimal(claimSummaryService.claimNumber(prpLCMainVo.getPolicyNo()));
				prpLCMainVo.setRegistTimes(bigDecimal);
			}

			PrpLRegistExtVo prpLRegistExtVo = registQueryService.getPrpLRegistExtInfo(registNo);
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
			modelAndView.addObject("prpLCMainVo", prpLCMainVo);
			modelAndView.addObject("prpLRegistExtVo", prpLRegistExtVo);
			//脱敏处理
			//reportorPhone
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);
			if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){//开关
				prpLRegistVo.setReportorPhone(DataUtils .replacePrivacy(prpLRegistVo.getReportorPhone()));
			
			}
			modelAndView.addObject("prpLRegistVo", prpLRegistVo);
			modelAndView.addObject("taskId", flowTaskId);
			//根据工作流的id显示原因start
			String ids = wfTaskVo.getHandlerIdKey();
			BigDecimal ides = new BigDecimal(ids);
			PrpLrejectClaimTextVo prpLrejectClaimTextVo = claimCancelService.findByCancelClaimTextId(ides);
			BigDecimal id = prpLrejectClaimTextVo.getPrplcancelTraceId();
			//根据工作流的id显示原因end
			// 根据险种跟立案号确认id
			String riskCode = prpLClaimVo.getRiskCode();//
			//BigDecimal id = claimCancelRecoverService.findId(riskCode, claimNo);
			PrpLcancelTraceVo prpLcancelTraceVo = claimCancelRecoverService.findByCancelTraceId(id);
			modelAndView.addObject("id", id);
			modelAndView.addObject("handlerStatus", handlerStatus);
			List<PrpLrejectClaimTextVo> prpLrejectClaimTextVos = claimCancelRecoverService.findById(id);
			List<PrpLrejectClaimTextVo> prpLrejectClaimTextVoList = new ArrayList<PrpLrejectClaimTextVo>();
			if (prpLrejectClaimTextVos != null) {
				for (int i = 0; i < prpLrejectClaimTextVos.size(); i++) {
					if (prpLrejectClaimTextVos.get(i).getDescription() != null) {
						prpLrejectClaimTextVoList.add(prpLrejectClaimTextVos.get(i));
					}
				}
			}
			modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
			modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
			modelAndView.addObject("flages", prpLcancelTraceVo.getFlag());
		}
		
		modelAndView.addObject("prpLClaimVo", prpLClaimVo);
		modelAndView.addObject("wfTaskVo", wfTaskVo);
		modelAndView.addObject("subNodeCode", subNodeCode);
		

		if (types.equals("2")) {
			modelAndView.setViewName("claimCRecover/CancelApplyCheckLast");
		} else {
			modelAndView.setViewName("claimCRecover/ClaimCancelApplyCheck");
		}
		return modelAndView;
	}

	// 注销拒赔申请处理提交给总公司
	@RequestMapping("/claimCancelEnd.do")
	@ResponseBody
	public void claimCancelEnd(PrpLcancelTraceVo prpLcancelTraceVo) {
		
		
		
		String currentNode = prpLcancelTraceVo.getSubNodeCode();
		Integer nowLevel = Integer.parseInt(currentNode.substring(11));
		
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
		wfTaskVo.setHandlerStatus("3");
		Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
		// TODO zjd 调用工作流
		WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
		taskVo.setRegistNo(wfTaskVo.getRegistNo());
		taskVo.setHandlerIdKey(id.toString());
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(wfTaskVo.getFlowId());
		/*submitVo.setCurrentNode(FlowNode.ReCanVrf_LV1);
		submitVo.setNextNode(FlowNode.ReCanVrf_LV1);*/
		submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
		submitVo.setNextNode(FlowNode.valueOf(currentNode));
		submitVo.setHandleruser(WebUserUtils.getUserCode());
        submitVo.setComCode(policyViewService.getPolicyComCode(wfTaskVo.getRegistNo()));
		submitVo.setTaskInKey(wfTaskVo.getRegistNo());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
		submitVo.setNextNode(FlowNode.END);
		//if (!(wfTaskVo.getWorkStatus().equals("6"))) {
			PrpLClaimCancelVo pClaimCancelVos = new PrpLClaimCancelVo();
			String applyReason = CodeTranUtil.transCode("ApplyReason",
					prpLcancelTraceVo.getApplyReason());
			pClaimCancelVos.setApplyReason(applyReason);
			pClaimCancelVos.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
			PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVos.setClaimTime(prpLClaimVo.getClaimTime());
			pClaimCancelVos.setClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVos.setClaimCancelTime(new Date());
			wfTaskHandleService.submitClaimTask(taskVo, submitVo,pClaimCancelVos);
		//	wfTaskHandleService.submitSimpleTask(taskVo, submitVo);
		//}

		// 提交总公司
		taskVo.setRegistNo(wfTaskVo.getRegistNo());
		taskVo.setHandlerIdKey(id.toString());
		submitVo.setFlowId(wfTaskVo.getFlowId());
		if(nowLevel==1){//第一次调用规则
			//start
			//规则VerifyClaimCancelRuleVo    claimNo
		PrpLClaimVo prpLClaimVo1 = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
		
		VerifyClaimCancelRuleVo ruleVo = new VerifyClaimCancelRuleVo();
		ruleVo.setSumLossFee(prpLClaimVo1.getSumClaim().doubleValue());
		//设置vo
		ruleVo = claimRuleApiService.claimCanCelToPriceRule(ruleVo);
		Integer backLevel = ruleVo.getBackLevel();
		//end
			if(backLevel > 2){
				submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
				submitVo.setNextNode(FlowNode.ReCanLVrf_LV11);
			}else{
				submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
				Integer a = backLevel+1;
				//submitVo.setNextNode(FlowNode.ReCanVrf_LV2);
				if(backLevel<2){
					if(nowLevel<backLevel){
						submitVo.setNextNode(FlowNode.valueOf("ReCanVrf_LV"+backLevel));
					}else{
						Integer k = nowLevel+1;
						submitVo.setNextNode(FlowNode.valueOf("ReCanVrf_LV"+k));
					}
				//submitVo.setNextNode(FlowNode.valueOf("ReCanVrf_LV"+a));
				}else{
					submitVo.setNextNode(FlowNode.valueOf("ReCanVrf_LV"+backLevel));
				}
			}
		}else{
			submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
			Integer a = Integer.parseInt(currentNode.substring(11))+1;
			//submitVo.setNextNode(FlowNode.ReCanVrf_LV2);
			if(a<=2){
			submitVo.setNextNode(FlowNode.valueOf("ReCanVrf_LV"+a));
			}else{
				submitVo.setNextNode(FlowNode.ReCanLVrf_LV11);
			}
		}
		SysUserVo userVo = WebUserUtils.getUser();
	/*	submitVo.setAssignUser(WebUserUtils.getUserCode());*/
		submitVo.setAssignCom(CodeConstants.TOPCOM);
		submitVo.setHandleruser(WebUserUtils.getUserCode());
		submitVo.setComCode(policyViewService.getPolicyComCode(wfTaskVo.getRegistNo()));
		//submitVo.setComCode(WebUserUtils.getComCode());
		submitVo.setTaskInKey(wfTaskVo.getRegistNo());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
		PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
		pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
		pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
		PrpLClaimVo prpLClaimVo1 = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
		pClaimCancelVo.setClaimTime(prpLClaimVo1.getClaimTime());
		pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
		pClaimCancelVo.setRecoverReason(prpLcancelTraceVo.getCancelCode());//注销/拒赔恢复原因
		pClaimCancelVo.setClaimRecoverTime(new Date());//注销/拒赔恢复时间
		wfTaskHandleService.addCancelTask(taskVo, submitVo, pClaimCancelVo);

		// 改变PrpLrejectClaimText状态
		prpLcancelTraceVo.setId(prpLcancelTraceVo.getId());
		prpLcancelTraceVo.setOpinionCode("03");
		//岗位
		prpLcancelTraceVo.setStationName(submitVo.getCurrentNode().getName());
		claimCancelRecoverService.savePrpLrejectClaimText(prpLcancelTraceVo,userVo);

	}

	// 分公司注销拒赔退回申请处理提交给总公司
	@RequestMapping("/claimCancelTui.do")
	@ResponseBody
	public void claimCancelTui(PrpLcancelTraceVo prpLcancelTraceVo) {

		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
		wfTaskVo.setHandlerStatus("3");
		Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
		// TODO zjd 调用工作流
		WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		// 提交总公司
		SysUserVo userVo = WebUserUtils.getUser();
		taskVo.setRegistNo(wfTaskVo.getRegistNo());
		taskVo.setHandlerIdKey(id.toString());
		submitVo.setFlowId(wfTaskVo.getFlowId());
		submitVo.setCurrentNode(FlowNode.ReCanVrf_LV1);
		submitVo.setNextNode(FlowNode.ReCanVrf_LV2);
		//保单机构
		String policyNoComCode = policyViewService.getPolicyComCode(wfTaskVo.getRegistNo());
		submitVo.setAssignCom(policyNoComCode);
		submitVo.setHandleruser(WebUserUtils.getUserCode());
		//submitVo.setComCode(WebUserUtils.getComCode());
		submitVo.setTaskInKey(wfTaskVo.getRegistNo());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
		PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
		pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
		pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
		pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
		pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
		wfTaskHandleService.addCancelTask(taskVo, submitVo, pClaimCancelVo);

		// 改变PrpLrejectClaimText状态
		prpLcancelTraceVo.setId(prpLcancelTraceVo.getId());
		prpLcancelTraceVo.setOpinionCode("03");
		//岗位
		prpLcancelTraceVo.setStationName(submitVo.getCurrentNode().getName());
		claimCancelRecoverService.savePrpLrejectClaimText(prpLcancelTraceVo,userVo);

	}

	// 分公司注销拒赔申请处理暂存
	@RequestMapping("/claimCancelZhanCun.do")
	@ResponseBody
	public void claimCancelZhanCun(PrpLcancelTraceVo prpLcancelTraceVo) {
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
		wfTaskVo.setHandlerStatus("2");
		Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
		wfTaskHandleService.tempSaveTask(prpLcancelTraceVo.getTaskId(),id.toString(), WebUserUtils.getUserCode(),WebUserUtils.getComCode());
		// 暂存（正在处理）

		/*
		 * //改变PrpLrejectClaimText状态
		 */
		prpLcancelTraceVo.setFlag("3");
		claimCancelRecoverService.updateCancelTrace(prpLcancelTraceVo);
	}

	// 总公司注销拒赔申请处理暂存
	@RequestMapping("/claimCancelZCLast.do")
	@ResponseBody
	public void claimCancelZCLast(PrpLcancelTraceVo prpLcancelTraceVo) {
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
		wfTaskVo.setHandlerStatus("2");
		Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
		wfTaskHandleService.tempSaveTask(prpLcancelTraceVo.getTaskId(),id.toString(), WebUserUtils.getUserCode(),WebUserUtils.getComCode());
		// 暂存（正在处理）
		prpLcancelTraceVo.setFlag("4");
		claimCancelRecoverService.updateCancelTrace(prpLcancelTraceVo);
	}

	// 总公司退回分公司
	@RequestMapping("/claimCancelBack.do")
	@ResponseBody
	public void claimCancelBack(PrpLcancelTraceVo prpLcancelTraceVo) {
		SysUserVo userVo = WebUserUtils.getUser();
		PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
		// 提交工作流
		List<PrpLWfTaskVo> prpLWfTaskVo1 = wfTaskHandleService.findEndTask(prpLcancelTraceVo.getRegistNo(), prpLWfTaskVo.getHandlerIdKey(), FlowNode.Cancel);
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(prpLWfTaskVo.getFlowId());
		submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
		submitVo.setAssignCom(prpLWfTaskVo1.get(0).getHandlerCom());
		submitVo.setComCode(policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo()));
		//submitVo.setComCode(WebUserUtils.getComCode());
		submitVo.setTaskInKey(prpLWfTaskVo.getRegistNo());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		//退回给谁
		/*submitVo.setAssignUser(WebUserUtils.getUserCode());*/
		submitVo.setHandleruser(WebUserUtils.getUserCode());
		//start
		String currentNode = prpLcancelTraceVo.getSubNodeCode();
		Integer nowLevel = Integer.parseInt(currentNode.substring(12));
		if(nowLevel==11){
			submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
			submitVo.setNextNode(FlowNode.ReCanVrf_LV1);
		}else{
			submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
			nowLevel = nowLevel-1;
			submitVo.setNextNode(FlowNode.valueOf("ReCanVrf_LV"+nowLevel));
		}
		//end
		/*submitVo.setCurrentNode(FlowNode.ReCanVrf_LV2);
		submitVo.setNextNode(FlowNode.ReCanVrf_LV1);*/
		if(prpLcancelTraceVo.getOpinionName().equals("1")){
			submitVo.setHandleIdKey(prpLWfTaskVo.getHandlerIdKey());
			PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
			pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
			pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
			PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
			pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setRecoverReason(prpLcancelTraceVo.getCancelCode());//注销/拒赔恢复原因
			pClaimCancelVo.setClaimRecoverTime(new Date());//注销/拒赔恢复时间
			wfTaskHandleService.backCancelHandle(submitVo, pClaimCancelVo);
		}else{
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
			wfTaskVo.setHandlerStatus("3");
			Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
			// TODO zjd 调用工作流
			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
			taskVo.setRegistNo(wfTaskVo.getRegistNo());
			taskVo.setHandlerIdKey(id.toString());
			submitVo.setFlowId(wfTaskVo.getFlowId());
			submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
			submitVo.setNextNode(FlowNode.ReCanApp);
			submitVo.setHandleIdKey(prpLWfTaskVo.getHandlerIdKey());
			/*submitVo.setAssignUser(wfTaskVo.getHandlerUser());
			submitVo.setAssignCom(WebUserUtils.getComCode());*/
			submitVo.setComCode(policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo()));
			//submitVo.setComCode(WebUserUtils.getComCode());
			submitVo.setTaskInKey(wfTaskVo.getRegistNo());
			submitVo.setTaskInUser(WebUserUtils.getUserCode());
			submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
			//submitVo.setNextNode(FlowNode.END);
			PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
			pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
			pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
			PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
			pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setRecoverReason(prpLcancelTraceVo.getCancelCode());//注销/拒赔恢复原因
			pClaimCancelVo.setClaimRecoverTime(new Date());//注销/拒赔恢复时间
			wfTaskHandleService.backCancelHandle(submitVo, pClaimCancelVo);
		}
		// 改变PrpLrejectClaimText状态
		prpLcancelTraceVo.setId(prpLcancelTraceVo.getId());
		prpLcancelTraceVo.setOpinionCode("04");
		//岗位
		prpLcancelTraceVo.setStationName(submitVo.getCurrentNode().getName());
		claimCancelRecoverService.savePrpLrejectClaimText(prpLcancelTraceVo,userVo);
	}

	// 分公司退回
	@RequestMapping("/CancelBack.do")
	@ResponseBody
	public void CancelBack(PrpLcancelTraceVo prpLcancelTraceVo) {
		//判断是否为第一级start
		PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
		SysUserVo userVo = WebUserUtils.getUser();
		// 提交工作流
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(prpLWfTaskVo.getFlowId());
		submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
		submitVo.setComCode(policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo()));
		//submitVo.setComCode(WebUserUtils.getComCode());
		submitVo.setTaskInKey(prpLWfTaskVo.getRegistNo());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		//退回给谁
		/*submitVo.setAssignUser(WebUserUtils.getUserCode());*/
		submitVo.setHandleruser(WebUserUtils.getUserCode());
		//start
		String currentNode = prpLcancelTraceVo.getSubNodeCode();
		Integer nowLevel = Integer.parseInt(currentNode.substring(11));
		if(prpLcancelTraceVo.getOpinionName().equals("1")){
			if(nowLevel!=1){
				submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
				nowLevel = nowLevel-1;
				submitVo.setNextNode(FlowNode.valueOf("ReCanVrf_LV"+nowLevel));
				submitVo.setHandleIdKey(prpLWfTaskVo.getHandlerIdKey());
				PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
				pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
				pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
				PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
				pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
				pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
				pClaimCancelVo.setRecoverReason(prpLcancelTraceVo.getCancelCode());//注销/拒赔恢复原因
				pClaimCancelVo.setClaimRecoverTime(new Date());//注销/拒赔恢复时间
				wfTaskHandleService.backCancelHandle(submitVo, pClaimCancelVo);
			}else{
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
			wfTaskVo.setHandlerStatus("3");
			Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
			// TODO zjd 调用工作流
			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
			taskVo.setRegistNo(wfTaskVo.getRegistNo());
			taskVo.setHandlerIdKey(id.toString());
			//WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			submitVo.setFlowId(wfTaskVo.getFlowId());
		/*	submitVo.setCurrentNode(FlowNode.ReCanVrf_LV1);
			submitVo.setNextNode(FlowNode.ReCanVrf_LV1);*/
			submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
			submitVo.setNextNode(FlowNode.ReCanApp);
			submitVo.setHandleIdKey(prpLWfTaskVo.getHandlerIdKey());
			/*submitVo.setAssignUser(wfTaskVo.getHandlerUser());
			submitVo.setAssignCom(WebUserUtils.getComCode());*/
			submitVo.setComCode(policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo()));
			//submitVo.setComCode(WebUserUtils.getComCode());
			submitVo.setTaskInKey(wfTaskVo.getRegistNo());
			submitVo.setTaskInUser(WebUserUtils.getUserCode());
			submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
			//submitVo.setNextNode(FlowNode.END);
			PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
			pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
			pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
			PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
			pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setRecoverReason(prpLcancelTraceVo.getCancelCode());//注销/拒赔恢复原因
			pClaimCancelVo.setClaimRecoverTime(new Date());//注销/拒赔恢复时间
			wfTaskHandleService.backCancelHandle(submitVo, pClaimCancelVo);
			}
		}else{
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
			wfTaskVo.setHandlerStatus("3");
			Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
			// TODO zjd 调用工作流
			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
			taskVo.setRegistNo(wfTaskVo.getRegistNo());
			taskVo.setHandlerIdKey(id.toString());
			submitVo.setFlowId(wfTaskVo.getFlowId());
			submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
			submitVo.setNextNode(FlowNode.ReCanApp);
			submitVo.setHandleIdKey(prpLWfTaskVo.getHandlerIdKey());
		/*	submitVo.setAssignUser(wfTaskVo.getHandlerUser());
			submitVo.setAssignCom(WebUserUtils.getComCode());*/
			submitVo.setComCode(policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo()));
			//submitVo.setComCode(WebUserUtils.getComCode());
			submitVo.setTaskInKey(wfTaskVo.getRegistNo());
			submitVo.setTaskInUser(WebUserUtils.getUserCode());
			submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
			//submitVo.setNextNode(FlowNode.END);
			PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
			pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
			pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
			PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
			pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setRecoverReason(prpLcancelTraceVo.getCancelCode());//注销/拒赔恢复原因
			pClaimCancelVo.setClaimRecoverTime(new Date());//注销/拒赔恢复时间
			wfTaskHandleService.backCancelHandle(submitVo, pClaimCancelVo);
		}
		prpLcancelTraceVo.setId(prpLcancelTraceVo.getId());
		prpLcancelTraceVo.setOpinionCode("04");
		//岗位
		prpLcancelTraceVo.setStationName(submitVo.getCurrentNode().getName());
		claimCancelRecoverService.savePrpLrejectClaimText(prpLcancelTraceVo,userVo);

	}

	// 注销拒赔申请处理完成
	@RequestMapping("/claimCancel.do")
	@ResponseBody
	public void claimCancel(PrpLcancelTraceVo prpLcancelTraceVo) {
		// PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		SysUserVo userVo = WebUserUtils.getUser();
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo
				.getTaskId());
		wfTaskVo.setHandlerStatus("3");
		Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
		// TODO zjd 调用工作流
		WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
		//是否提交公司下一级
		String next ="0";
		taskVo.setRegistNo(wfTaskVo.getRegistNo());
		taskVo.setHandlerIdKey(id.toString());
		taskVo.setClaimNo(wfTaskVo.getClaimNo());
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(wfTaskVo.getFlowId());
		
		String currentNode = prpLcancelTraceVo.getSubNodeCode();
		
		
		/*submitVo.setCurrentNode(FlowNode.ReCanVrf_LV2);
		submitVo.setNextNode(FlowNode.ReCanVrf_LV2);*/
		submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
		submitVo.setNextNode(FlowNode.valueOf(currentNode));

		submitVo.setHandleruser(WebUserUtils.getUserCode());
		String policyNoComCode = policyViewService.getPolicyComCode(wfTaskVo.getRegistNo());
		submitVo.setComCode(policyViewService.getPolicyComCode(wfTaskVo.getRegistNo()));
		submitVo.setTaskInKey(wfTaskVo.getRegistNo());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
		//submitVo.setFlowTaskId(wfTaskVo.getTaskId());
		submitVo.setNextNode(FlowNode.END);
		wfTaskHandleService.submitSimpleTask(taskVo, submitVo);

	    //可否调用车童标识
        String flagMTAOrCT = "0";
		//判断是否提交下一级
		Integer nowLevel = Integer.parseInt(currentNode.substring(12));
		if(nowLevel==11){//第一次调用规则
			//start
			//规则VerifyClaimCancelRuleVo    claimNo
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
		
		VerifyClaimCancelRuleVo ruleVo = new VerifyClaimCancelRuleVo();
		ruleVo.setSumLossFee(prpLClaimVo.getSumClaim().doubleValue());
		//设置vo
		ruleVo = claimRuleApiService.claimCanCelToPriceRule(ruleVo);
		Integer backLevel = ruleVo.getBackLevel();
		//end
		if(nowLevel<11){//判断当前等级是否小于最高级，现在最高级为1，小于可以提交上级，否则不能
			if(backLevel>=1){
			//if(backLevel==3){
				submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
				submitVo.setNextNode(FlowNode.ReCanLVrf_LV11);
				next = "1";
			}else{
				submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
				Integer a = backLevel+1;
				//submitVo.setNextNode(FlowNode.ReCanVrf_LV2);
		/*		if(backLevel!=3){
				submitVo.setNextNode(FlowNode.valueOf("ReCanLVrf_LV1"+backLevel));
				next = "1";
				}else{
					submitVo.setNextNode(FlowNode.ReCanLVrf_LV13);
					next = "1";
				}*/
				if(backLevel<1){
					Integer back  = backLevel+10;
					if(nowLevel<back){
						submitVo.setNextNode(FlowNode.valueOf("ReCanLVrf_LV1"+backLevel));
					}else{
						Integer kk = nowLevel+1;
						submitVo.setNextNode(FlowNode.valueOf("ReCanLVrf_LV"+kk));
					}
				next = "1";
				}
			}
		}else{
			submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
			Integer a = Integer.parseInt(currentNode.substring(12));
			//submitVo.setNextNode(FlowNode.ReCanVrf_LV2);
			if(a!=11){
				a = Integer.parseInt(currentNode.substring(12))+1;
				submitVo.setNextNode(FlowNode.valueOf("ReCanLVrf_LV"+a));
				next = "1";
			}else{
				//submitVo.setNextNode(FlowNode.ReCanLVrf_LV13);
			}
		}
		

		//开始
		// 提交总公司
		if(next.equals("1")){
				taskVo.setRegistNo(wfTaskVo.getRegistNo());
				taskVo.setHandlerIdKey(id.toString());
				submitVo.setFlowId(wfTaskVo.getFlowId());
				submitVo.setAssignCom(policyNoComCode);
				submitVo.setComCode(policyNoComCode);
				submitVo.setTaskInKey(wfTaskVo.getRegistNo());
				submitVo.setTaskInUser(WebUserUtils.getUserCode());
				submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
				PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
				pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
				pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
				//PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
				pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
				pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
				pClaimCancelVo.setRecoverReason(prpLcancelTraceVo.getCancelCode());//注销/拒赔恢复原因
				pClaimCancelVo.setClaimRecoverTime(new Date());//注销/拒赔恢复时间
				wfTaskHandleService.addCancelTask(taskVo, submitVo, pClaimCancelVo);
		}
		//结束
		
		// 改变PrpLrejectClaimText状态
		prpLcancelTraceVo.setId(prpLcancelTraceVo.getId());
		prpLcancelTraceVo.setOpinionCode("05");
		//岗位
		prpLcancelTraceVo.setStationName(submitVo.getCurrentNode().getName());
		claimCancelRecoverService.savePrpLrejectClaimText(prpLcancelTraceVo,userVo);
		// 提交拒赔恢复
		
		
		
		
		
		//更新PrpLcancelTrace表的申请状态
		PrpLcancelTraceVo prpLcancelTraceDVo = claimCancelService.findByCancelTraceId(prpLcancelTraceVo.getId());
		prpLcancelTraceDVo.setFlags("4");//恢复标志
		claimCancelService.updateCancelDTrace(prpLcancelTraceDVo);
		//申请
		BigDecimal ids = claimCancelService.findId(prpLcancelTraceVo.getRiskCode(), prpLcancelTraceVo.getClaimNo());
		PrpLcancelTraceVo prpLcancelTracesVo = claimCancelService.findByCancelTraceId(ids);
		prpLcancelTracesVo.setFlags("4");//恢复标志
		claimCancelService.updateCancelDTrace(prpLcancelTracesVo);
		
		
		// 回写立案表
		String subNodeCode = FlowNode.ClaimBI.name();
		if ("1101".equals(prpLcancelTraceVo.getRiskCode().trim())) {
			subNodeCode = FlowNode.ClaimCI.name();
		}
		//BigDecimal flowTaskId=null;
		BigDecimal flowTaskId = wfTaskHandleService.findTaskId(prpLcancelTraceVo.getClaimNo(), subNodeCode);

		//判断立案是否可以全部恢复其它节点
		 
		List<PrpLClaimVo> prpLClaimVoList = claimService.findClaimListByRegistNo(wfTaskVo.getRegistNo());
		if(prpLClaimVoList.size()==2){//两个注销了就可以恢复其它节点
			if((("0").equals(prpLClaimVoList.get(0).getValidFlag())||("2").equals(prpLClaimVoList.get(0).getValidFlag())) && 
					(("0").equals(prpLClaimVoList.get(1).getValidFlag())||("2").equals(prpLClaimVoList.get(1).getValidFlag()))){
				//判断工作流是否有未处理立案注销Cancel
				//List<PrpLWfTaskVo> PrpLWfTaskListVo = wfFlowQueryService.findPrpWfTaskVo(wfTaskVo.getRegistNo(), FlowNode.Cancel.getName());
				//if(PrpLWfTaskListVo==null || PrpLWfTaskListVo.size()==0){
					claimTaskService.recoverClaimForOther(wfTaskVo.getRegistNo(), WebUserUtils.getUserCode(),flowTaskId);
					submitVo.setFlowStatus(FlowStatus.NORMAL);//案件状态恢复正常状态
					flagMTAOrCT = "1";
				//}
			}
		}else if(("0").equals(prpLClaimVoList.get(0).getValidFlag())){//只有一条数据注销就可以恢复其它节点
			submitVo.setFlowStatus(FlowStatus.NORMAL);//案件状态恢复正常状态
			claimTaskService.recoverClaimForOther(wfTaskVo.getRegistNo(), WebUserUtils.getUserCode(),flowTaskId);
			flagMTAOrCT = "1";
		}
		//claimTaskExtService.cancleClaimRecover(prpLcancelTraceVo.getClaimNo(),"1",flowTaskId,userVo);

		
		claimTaskExtService.cancleClaimRecover(prpLcancelTraceVo.getClaimNo(),"1",flowTaskId,userVo,submitVo);

		//立案注销恢复审核完成，重新补传立案平台
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(prpLcancelTraceVo.getClaimNo());
		sendClaimToPlatform(claimVo);
	}
		if("1".equals(flagMTAOrCT)){
		    try{
		        interfaceAsyncService.sendClaimCancelRestoreCTorMTA(wfTaskVo.getRegistNo(),userVo);
	        }
	        catch(Exception e){
	            e.printStackTrace();
	        }
		}
	}
	
	
	
	private void sendClaimToPlatform(PrpLClaimVo claimVo){
		String riskCode = claimVo.getRiskCode();
		CiClaimPlatformLogVo logVo = new CiClaimPlatformLogVo();
		logVo.setBussNo(claimVo.getClaimNo());
		String policyType = Risk.DQZ.equals(claimVo.getRiskCode()) ? "11" : "12";
		String comCode = policyViewService.findPolicyComCode(claimVo.getRegistNo(), policyType);
		String requestType = RequestType.ClaimBI.getCode();
		if(comCode.startsWith("22")){
			requestType = Risk.DQZ.equals(riskCode) ? RequestType.ClaimInfoCI_SH.getCode() : RequestType.ClaimInfoBI_SH.getCode();
			logVo.setBussNo(claimVo.getRegistNo());
		}else{
			if(Risk.DQZ.equals(claimVo.getRiskCode())){
				requestType = RequestType.ClaimCI.getCode();
			}
		}
		logVo.setRequestType(requestType);
		logVo.setComCode(comCode);
		interfaceAsyncService.sendClaimToPaltform(logVo);
	}
	
	//撤销恢复信息通知车童网/民太安
    private void sendClaimCancelRestoreCTorMTA(String registNo) throws Exception{
        //获取in表的所有车童网/民太安任务
        String flagCT = "0";
        String flagMTA = "0";
        //List<PrpLWfTaskVo> volist = wfFlowQueryService.findPrpWfTaskVoForOut(registNo, FlowNode.Check.toString(),FlowNode.DLoss.toString());
      /*  List<PrpLWfTaskVo> checkVolist = wfFlowQueryService.findTaskVoForOutByNodeCode(registNo, FlowNode.Check.toString());
        List<PrpLWfTaskVo> dLossVolist = wfFlowQueryService.findTaskVoForOutByNodeCode(registNo, FlowNode.DLoss.toString());*/
        List<PrpLWfTaskVo> checkVolist = wfFlowQueryService.findTaskVoForInByNodeCode(registNo, FlowNode.Check.toString());
        List<PrpLWfTaskVo> dLossVolist = wfFlowQueryService.findTaskVoForInByNodeCode(registNo, FlowNode.DLoss.toString());
        List<PrpLWfTaskVo> volist = new ArrayList<PrpLWfTaskVo>();
        if(checkVolist != null && checkVolist.size() > 0){
            volist.addAll(checkVolist);
        }
        if(dLossVolist != null && dLossVolist.size() > 0){
            volist.addAll(dLossVolist);
        }
        List<PrpLWfTaskVo> prpLWfTaskCTVos = new ArrayList<PrpLWfTaskVo>();
        List<PrpLWfTaskVo> prpLWfTaskMTAVos = new ArrayList<PrpLWfTaskVo>();
        if(volist != null && volist.size() > 0){
            for(PrpLWfTaskVo vo : volist){
                if("0".equals(vo.getHandlerStatus()) || "2".equals(vo.getHandlerStatus())){
                    if("2".equals(vo.getIsMobileAccept())){
                        flagCT = "1";
                        prpLWfTaskCTVos.add(vo);
                    }else if("3".equals(vo.getIsMobileAccept())){
                        flagMTA = "1";
                        prpLWfTaskMTAVos.add(vo);
                    }
                }
            }
        }
        RevokeRestoreInfoReqVo reqVo = new RevokeRestoreInfoReqVo();
        if("1".equals(flagCT)){
            CarchildHeadVo head = new CarchildHeadVo();
            String businessType = null;
            //理赔请求车童网  
            head.setRequestType("CT_007");
            head.setUser("claim_user");
            head.setPassWord("claim_psd");
            businessType = BusinessType.CT_claimCancelRestore.name();
            reqVo.setHead(head);
            this.organizationAndSendCTorMTA(prpLWfTaskCTVos,reqVo,businessType);
        }
        if( "1".equals(flagMTA)){
            CarchildHeadVo head = new CarchildHeadVo();
            String businessType = null;
            //理赔请求民太安
            head.setRequestType("MTA_007");
            head.setUser("claim_user");
            head.setPassWord("claim_psd");
            businessType = BusinessType.MTA_claimCancelRestore.name();
            reqVo.setHead(head);
            this.organizationAndSendCTorMTA(prpLWfTaskMTAVos,reqVo,businessType);
        }
    }
    
    /**
     * 车童网/民太安撤销恢复信息交互日志保存
     * <pre></pre>
     * @param reqVo
     * @param resVo
     * @param url
     * @param BusinessType
     * @param date
     * @modified:
     * ☆LinYi(2017年10月18日 下午3:50:05): <br>
     */
   /* private void saveCarchildInterfaceLog(RevokeRestoreInfoReqVo reqVo,RevokeRestoreInfoResVo resVo,String url,String BusinessType,Date date) {
        ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
        XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
        String requestXml = stream.toXML(reqVo);
        String responseXml = stream.toXML(resVo);
        CarchildResponseHeadVo carChildResponseHead = resVo.getHead();
        if("1".equals(carChildResponseHead.getErrNo())){
            logVo.setStatus("1");
            logVo.setErrorCode("true");
        }else {
            logVo.setStatus("0");
            logVo.setErrorCode("false");
        }
        logVo.setErrorMessage(resVo.getHead().getErrMsg());
        logVo.setRegistNo(reqVo.getBody().getRevokeRestoreTaskInfos().get(0).getRegistNo());
        logVo.setBusinessType(BusinessType);   
        logVo.setOperateNode(FlowNode.ReCanLVrf.name());
        logVo.setComCode(WebUserUtils.getComCode());
        logVo.setRequestTime(date);
        logVo.setRequestUrl(url);
        logVo.setCreateUser(WebUserUtils.getUserCode());
        logVo.setCreateTime(date);
        logVo.setRequestXml(requestXml);
        logVo.setResponseXml(responseXml);
        claimInterfaceLogService.save(logVo);
    }*/
    
	
	// 注销拒赔恢复申请发起
	@RequestMapping("/claimCancelInits.do")
	@ResponseBody
	public AjaxResult claimCancelInits(@RequestParam(value = "claimNo") String claimNo,
			@RequestParam(value = "taskId") String taskId,
			@RequestParam(value = "workStatus") String workStatus) {
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(claimNo);
		AjaxResult ar = new AjaxResult();
		List<PrpLWfTaskVo> prpLWfTaskVos = new ArrayList<PrpLWfTaskVo>();
		prpLWfTaskVos = wfTaskHandleService.findCanCelTask(claimNo, FlowNode.ReCanApp);
		
		BigDecimal traceId = claimCancelService.findId(prpLClaimVo.getRiskCode(), claimNo);
		// 判断是否已经申请注销拒赔
		if (prpLWfTaskVos != null&&prpLWfTaskVos.size()>0) {
			//不能发起
			ar.setStatus(HttpStatus.SC_OK);
			ar.setData("2");
		} else{
				if (traceId == null) {
					//可以发起
					ar.setStatus(HttpStatus.SC_OK);
					ar.setData("1");
				} else {
					PrpLcancelTraceVo prpLcancelTraceVo = claimCancelService.findByCancelTraceId(traceId);

					if(prpLcancelTraceVo.getFlags().trim().equals("2")||prpLcancelTraceVo.getFlags().trim().equals("3")||prpLcancelTraceVo.getFlags().trim().equals("7")){
						//不能发起
						ar.setStatus(HttpStatus.SC_OK);
						ar.setData("2");
						
						
					}else{
						//可以发起
						ar.setStatus(HttpStatus.SC_OK);
						ar.setData("1");
					}
				}
		}
		if(ar.getData().equals("1")){
			String subNodeCode = FlowNode.ClaimBI.name();
			if ("1101".equals(prpLClaimVo.getRiskCode())) {
				subNodeCode = FlowNode.ClaimCI.name();
			}
			// 提交工作流 regist subnodecode
			PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTaskId(prpLClaimVo.getFlowId(), subNodeCode);
			// 新增一个节点并且结束CancelVrf_LV
			/*
			 * wfTaskHandleService.addSimpleTask(taskVo, submitVo);
			 * wfTaskHandleService.submitSimpleTask(taskVo, submitVo);
			 */
			
			// 提交工作流
			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
			taskVo.setRegistNo(prpLWfTaskVo.getRegistNo());
			taskVo.setHandlerIdKey(prpLWfTaskVo.getTaskId().toString());
	
			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			submitVo.setFlowId(prpLWfTaskVo.getFlowId());
	
			submitVo.setCurrentNode(FlowNode.valueOf(subNodeCode));
	
			submitVo.setComCode(policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo()));
			submitVo.setTaskInKey(prpLWfTaskVo.getRegistNo());
			submitVo.setTaskInUser(WebUserUtils.getUserCode());
			submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
			// 新增一个节点并且结束CancelVrf_LV
			submitVo.setNextNode(FlowNode.ReCanApp);
	
			PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
			pClaimCancelVo.setClaimCancelTime(new Date());
			pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
			pClaimCancelVo.setClaimNo(prpLClaimVo.getClaimNo());
			PrpLWfTaskVo prpLWfTask = wfTaskHandleService.addCancelTask(taskVo,submitVo, pClaimCancelVo);
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLWfTask.getTaskId().doubleValue());
			wfTaskVo.setHandlerStatus("2");
			Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
			wfTaskHandleService.tempSaveTask(prpLWfTask.getTaskId().doubleValue(),
					id.toString(), WebUserUtils.getUserCode(),
					WebUserUtils.getComCode());
		}
		/*ar.setStatus(HttpStatus.SC_OK);
		ar.setData("Y");*/
		return ar;
	}
	
	
	@RequestMapping("/claimCancelApplyByOne.do")
	@ResponseBody
	public AjaxResult claimCancelApplyByOne(String registNo,String taskId,String claimNo) {
		AjaxResult ajaxResult = new AjaxResult();
		List<PrpLClaimVo> prpLClaimVoList = claimService.findClaimListByRegistNo(registNo);
		List<PrpLCMainVo> prpLCMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);
		if (prpLClaimVoList != null) {
			for (PrpLClaimVo prpLClaimVo : prpLClaimVoList) {
				for (PrpLCMainVo prpLCMainVo : prpLCMainVoList) {
					if (prpLClaimVo.getPolicyNo().equals(prpLCMainVo.getPolicyNo())) {
						prpLClaimVo.setInsuredName(prpLCMainVo.getInsuredName());
						prpLClaimVo.setStartDate(prpLCMainVo.getStartDate());
						prpLClaimVo.setEndDate(prpLCMainVo.getEndDate());
					}
				}
			}
		}
		List<PrpLcancelTraceVo> prpLcancelTraceVos = new ArrayList<PrpLcancelTraceVo>();
		if(prpLClaimVoList != null) {
			for (PrpLClaimVo prpLClaimVo : prpLClaimVoList) {
				if(prpLClaimVo.getValidFlag()!=null&&prpLClaimVo.getValidFlag().equals("0")
						&& prpLClaimVo.getClaimNo().equals(claimNo)){
					PrpLcancelTraceVo prpLcancelTraceVo= claimCancelRecoverService.findByClaimNo(prpLClaimVo.getClaimNo());
					if(prpLcancelTraceVo != null){
					prpLcancelTraceVos.add(prpLcancelTraceVo);
					}
				}
			}
		}
		if(prpLcancelTraceVos.size() > 0 && prpLcancelTraceVos != null){
			ajaxResult.setStatusText("1");
		}else{
			ajaxResult.setStatusText("0");
		}
		
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	
	
	// 公共按钮发起暂存
			@RequestMapping("/claimInitZhanCunToF.do")
			@ResponseBody
			public void claimInitZhanCunToF(PrpLcancelTraceVo prpLcancelTraceVo) {
				
				
				PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());

				String subNodeCode = FlowNode.ClaimBI.name();
				if ("1101".equals(prpLClaimVo.getRiskCode())) {
					subNodeCode = FlowNode.ClaimCI.name();
				}
				// 提交工作流 regist subnodecode
				PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTaskId(prpLClaimVo.getFlowId(), subNodeCode);
				// 新增一个节点并且结束CancelVrf_LV
				/*
				 * wfTaskHandleService.addSimpleTask(taskVo, submitVo);
				 * wfTaskHandleService.submitSimpleTask(taskVo, submitVo);
				 */
				
				// 提交工作流
				WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
				taskVo.setRegistNo(prpLWfTaskVo.getRegistNo());
				taskVo.setHandlerIdKey(prpLWfTaskVo.getTaskId().toString());
		
				WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
				submitVo.setFlowId(prpLWfTaskVo.getFlowId());
		
				submitVo.setCurrentNode(FlowNode.valueOf(subNodeCode));
		
				String policyNoComCode = policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo());
				submitVo.setAssignCom(policyNoComCode);
				submitVo.setComCode(policyNoComCode);
				//submitVo.setComCode(WebUserUtils.getComCode());
				submitVo.setTaskInKey(prpLWfTaskVo.getRegistNo());
				submitVo.setTaskInUser(WebUserUtils.getUserCode());
				submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
				// 新增一个节点并且结束CancelVrf_LV
				submitVo.setNextNode(FlowNode.ReCanApp);
		
				PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
				pClaimCancelVo.setClaimCancelTime(new Date());
				pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
				pClaimCancelVo.setClaimNo(prpLClaimVo.getClaimNo());
				PrpLWfTaskVo prpLWfTask = wfTaskHandleService.addCancelTask(taskVo,submitVo, pClaimCancelVo);
				PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLWfTask.getTaskId().doubleValue());
				wfTaskVo.setHandlerStatus("2");
				Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
				wfTaskHandleService.tempSaveTask(prpLWfTask.getTaskId().doubleValue(),
						id.toString(), WebUserUtils.getUserCode(),
						WebUserUtils.getComCode());
			}
			
			
			public void cancelApply(PrpLcancelTraceVo prpLcancelTraceVos,PrpLClaimVo prpLClaimVo,String flags,PrpLWfTaskVo lWfTaskVo,FlowNode NextNode) {
				// 保存原因
				
				SysUserVo userVo = WebUserUtils.getUser();
				if("3".equals(flags) ){//暂存
					/*if(lWfTaskVo!=null){
						//发起审核需求
						prpLcancelTraceVos.setTaskId(lWfTaskVo.getTaskId().doubleValue());
					}else{
						PrpLWfTaskVo prpLWfTask = wfTaskHandleService.queryTaskByAny(prpLClaimVo.getClaimNo(), NextNode.toString(), CodeConstants.HandlerStatus.DOING);
						//发起审核需求
						prpLcancelTraceVos.setTaskId(prpLWfTask.getTaskId().doubleValue());
					}*/
					PrpLWfTaskVo prpLWfTask = wfTaskHandleService.queryTaskByAny(prpLClaimVo.getRegistNo(),prpLClaimVo.getClaimNo(), NextNode.toString(), CodeConstants.HandlerStatus.DOING);
					//发起审核需求
					prpLcancelTraceVos.setTaskId(prpLWfTask.getTaskId().doubleValue());
					/*if(FlowNode.CancelAppJuPei.equals(NextNode)){
						prpLcancelTraceVos.setFlowTask(prpLWfTask.getFlowId());
					}else{*/
					// 提交工作流
						this.claimCancelReSave(prpLcancelTraceVos);
					//}
				}else if(CodeConstants.WorkStatus.BYBACK.equals(lWfTaskVo.getWorkStatus())){
					if(FlowNode.CancelAppJuPei.equals(NextNode)){
						}else{
						//发起审核需求
						prpLcancelTraceVos.setTaskId(lWfTaskVo.getTaskId().doubleValue());
						this.claimCancelReSave(prpLcancelTraceVos);
					}
				}else{
					BigDecimal A = claimCancelRecoverService.save(prpLcancelTraceVos,userVo,"N");
					String subNodeCode = FlowNode.ClaimBI.name();
					if ("1101".equals(prpLClaimVo.getRiskCode())) {
						subNodeCode = FlowNode.ClaimCI.name();
					}
					// 提交工作流 regist subnodecode
					PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTaskId(
							prpLClaimVo.getFlowId(), subNodeCode);

					// 提交工作流
					WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
					taskVo.setRegistNo(prpLWfTaskVo.getRegistNo());
					taskVo.setHandlerIdKey(prpLWfTaskVo.getTaskId().toString());

					WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
					submitVo.setFlowId(prpLWfTaskVo.getFlowId());

					submitVo.setCurrentNode(FlowNode.valueOf(subNodeCode));
					String policyNoComCode = policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo());
					submitVo.setAssignCom(policyNoComCode);
					submitVo.setComCode(policyNoComCode);
					submitVo.setTaskInKey(prpLWfTaskVo.getRegistNo());
					submitVo.setTaskInUser(WebUserUtils.getUserCode());

					submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
					// 新增一个节点并且结束CancelVrf_LV
					submitVo.setNextNode(FlowNode.ReCanApp);

					PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
					pClaimCancelVo.setClaimCancelTime(new Date());
					pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
					pClaimCancelVo.setClaimNo(prpLClaimVo.getClaimNo());
					String applyReason = CodeTranUtil.transCode("ApplyReason",prpLcancelTraceVos.getApplyReason());
					pClaimCancelVo.setApplyReason(applyReason);
					PrpLWfTaskVo prpLWfTask = wfTaskHandleService.addCancelTask(taskVo,submitVo, pClaimCancelVo);
					PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLWfTask.getTaskId().doubleValue());
					//wfTaskVo.setHandlerStatus("2");
					submitVo.setNextNode(FlowNode.END);
					Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
				/*	wfTaskHandleService.tempSaveTask(prpLWfTask.getTaskId().doubleValue(), id.toString(), WebUserUtils.getUserCode(),
							WebUserUtils.getComCode());*/
					
					//wfTaskHandleService.submitClaimTask(taskVo, submitVo, pClaimCancelVo);
					//发起审核需求
					PrpLrejectClaimTextVo vo = claimCancelService.findByCancelClaimTextId(A);
					PrpLcancelTraceVo prpLcancelTraceVo = claimCancelService.findByCancelTraceId(vo.getPrplcancelTraceId());
					
					prpLcancelTraceVo.setFlowTask(prpLcancelTraceVos.getFlowTask());
					prpLcancelTraceVo.setRegistNo(prpLcancelTraceVos.getRegistNo());
					prpLcancelTraceVo.setTaskId(prpLWfTask.getTaskId().doubleValue());
					prpLcancelTraceVo.setDescription(prpLcancelTraceVos.getDescription());
					
					this.claimCancelReSave(prpLcancelTraceVo);
					if(FlowNode.CancelAppJuPei.equals(NextNode)){
					}else{
					
						/*if (prpLcancelTraceVo.getFlags().trim().equals("1")) {
							// 暂存
						} else if (prpLcancelTraceVo.getFlags().trim().equals("0")&& !wfTaskVo.getWorkStatus().equals("6")|| prpLcancelTraceVo.getFlags().trim().equals("5")) {
							this.claimCancelSave(prpLcancelTraceVo);// 提交
						} else if (prpLcancelTraceVo.getFlags().trim().equals("4")) {
							// 恢复显示claimCancelApplyEdit界面
							this.claimCancelSave(prpLcancelTraceVo);// 提交
						} else if (wfTaskVo.getWorkStatus().equals("6")) {
							this.claimCancelSave(prpLcancelTraceVo);// 提交
						} else if (prpLcancelTraceVo.getFlags().trim().equals("9")) {
							this.claimCancelSave(prpLcancelTraceVo);// 提交
						} else if (prpLcancelTraceVo.getFlags().trim().equals("8")) {
							this.claimCancelSave(prpLcancelTraceVo);// 提交
						} else if (prpLcancelTraceVo.getFlags().trim().equals("11")) {
							
						}*/
					}
				}
				
			}
			
			public void claimCancelReSave(PrpLcancelTraceVo prpLcancelTraceVo) {
				
				BigDecimal traceId = claimCancelRecoverService.findId(prpLcancelTraceVo.getRiskCode(),prpLcancelTraceVo.getClaimNo());
				
				PrpLWfTaskVo vo = new PrpLWfTaskVo();
				if(StringUtils.isNotBlank(String.valueOf(prpLcancelTraceVo.getTaskId()))){
					vo = wfTaskHandleService.queryTaskForHandlerStatus(prpLcancelTraceVo.getTaskId());
				}
				
				if (traceId == null) {
					this.setSave(prpLcancelTraceVo);
				} else {
					PrpLcancelTraceVo cancelTraceVo = claimCancelService.findByCancelTraceId(traceId);
					//判断是否恢复立案调用setSave
					if("4".equals(cancelTraceVo.getFlags().trim())){
						this.setSave(prpLcancelTraceVo);
				}else{
					this.setUpdate(prpLcancelTraceVo);
				}
					// 判断是否恢复立案调用setSave
				}
			}
			// 根据工作流的id显示原因start
			@RequestMapping("/findByHandlerIdKey.do")
			@ResponseBody
			public PrpLcancelTraceVo findByHandlerIdKey(String handlerIdKey) {
				BigDecimal ides = new BigDecimal(handlerIdKey);
				PrpLrejectClaimTextVo prpLrejectClaimTextVo = claimCancelService.findByCancelClaimTextId(ides);
				if(prpLrejectClaimTextVo==null){
					return null;
				}
				BigDecimal id = prpLrejectClaimTextVo.getPrplcancelTraceId();
				// 根据工作流的id显示原因end
				PrpLcancelTraceVo prpLcancelTraceVo = claimCancelService.findByCancelTraceId(id);
				return prpLcancelTraceVo;
				
			}
	       private void organizationAndSendCTorMTA(List<PrpLWfTaskVo> prpLWfTaskVos,RevokeRestoreInfoReqVo reqVo,String businessType) {
	            RevokeRestoreBodyVo body = new RevokeRestoreBodyVo();
	            List<RevokeRestoreTaskInfoVo> revokeRestoreTaskInfos = new ArrayList<RevokeRestoreTaskInfoVo>();
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	            Date date = new Date();
	            String timeStamp = dateFormat.format(date);
	           if(prpLWfTaskVos != null && prpLWfTaskVos.size()>0){
	                for(PrpLWfTaskVo prpLWfTask:prpLWfTaskVos){
	                    RevokeRestoreTaskInfoVo revokeRestoreTaskInfoVo = new RevokeRestoreTaskInfoVo(); 
	                    if("0".equals(prpLWfTask.getHandlerStatus())){
	                        revokeRestoreTaskInfoVo.setTaskId(prpLWfTask.getHandlerIdKey());
	                    }else if ("2".equals(prpLWfTask.getHandlerStatus())) {
	                        if(FlowNode.DLoss.name().equals(prpLWfTask.getNodeCode())){
	                            if(FlowNode.DLCar.equals(prpLWfTask.getSubNodeCode())){
	                                PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(prpLWfTask.getHandlerIdKey()));
	                                revokeRestoreTaskInfoVo.setTaskId(prpLDlossCarMainVo.getScheduleDeflossId().toString());
	                            }else if(FlowNode.DLProp.name().equals(prpLWfTask.getSubNodeCode())){
	                                PrpLdlossPropMainVo prpLdlossPropMainVo = propLossService.findPropMainVoById(Long.parseLong(prpLWfTask.getHandlerIdKey()));
	                                //revokeRestoreTaskInfoVo.setTaskId(prpLdlossPropMainVo.getId().toString());
	                                //根据序号，报案号，定损类别查询
	                                PrpLScheduleDefLossVo vo = scheduleTaskService.findPrpLScheduleDefLossVoByOther(prpLdlossPropMainVo.getRegistNo(),prpLdlossPropMainVo.getSerialNo(),"2");
	                                revokeRestoreTaskInfoVo.setTaskId(vo.getId().toString());
	                            }
	                        }else if(FlowNode.Chk.equals(prpLWfTask.getSubNodeCode())){
	                            //PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.getScheduleTask(prpLWfTask.getRegistNo(),ScheduleStatus.CHECK_SCHEDULED);
	                            PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskByOther(prpLWfTask.getRegistNo(),"1","1");
	                            revokeRestoreTaskInfoVo.setTaskId(prpLScheduleTaskVo.getId().toString());
	                        }
	                    }
	                    revokeRestoreTaskInfoVo.setRegistNo(prpLWfTask.getRegistNo());
	                    revokeRestoreTaskInfoVo.setTimeStamp(timeStamp);
	                    revokeRestoreTaskInfos.add(revokeRestoreTaskInfoVo);
	                }
	            }
	            body.setRevokeRestoreTaskInfos(revokeRestoreTaskInfos);
	            reqVo.setBody(body);
	            
	            String url = SpringProperties.getProperty("MTA_URL_RECOVERYCANCEL");
	            logger.info("url=============="+url);
	            RevokeRestoreInfoResVo resVo = new RevokeRestoreInfoResVo();
	            try{
	                resVo = carchildService.sendRevokeRestoreInformation(reqVo,url);
	            }
	            catch(Exception e){
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	            finally{
	                //交互日志保存
	                SysUserVo userVo = new SysUserVo();
	                userVo.setComCode(WebUserUtils.getComCode());
	                userVo.setUserCode(WebUserUtils.getUserCode());
	                RegistInformationVo registInformationVo = new RegistInformationVo();
	                registInformationVo.setReqVo(reqVo);
	                registInformationVo.setResVo(resVo);
	                registInformationVo.setBusinessType(businessType);
	                registInformationVo.setSchType("2");
	                carchildService.saveCTorMTACarchildInterfaceLog(registInformationVo,url,null,userVo);
	            }
	       }
}
