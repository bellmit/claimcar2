package ins.sino.claimcar.claim.web.action;

import ins.framework.web.AjaxResult;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carchild.service.CarchildService;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.service.ClaimCancelService;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ClaimSummaryService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.ClaimToReinsuranceService;
import ins.sino.claimcar.claim.service.CompensateHandleServiceIlogService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLClaimCancelVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLcancelTraceVo;
import ins.sino.claimcar.claim.vo.PrpLrejectClaimTextVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfFlowService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.lossprop.service.PropLossService;
import ins.sino.claimcar.middlestagequery.service.ClaimToMiddleStageOfCaseService;
import ins.sino.claimcar.mobilecheck.service.SendMsgToMobileService;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.rule.service.ClaimRuleApiService;
import ins.sino.claimcar.rule.vo.VerifyClaimCancelRuleVo;
import ins.sino.claimcar.rule.vo.VerifyClaimRuleVo;
import ins.sino.claimcar.schedule.service.ScheduleService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
@RequestMapping("/claim")
public class ClaimCancelAction {
    private static Logger logger = LoggerFactory.getLogger(ClaimCancelAction.class);
    
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private ClaimService claimService;
	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	private ClaimCancelService claimCancelService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private ClaimRuleApiService claimRuleApiService;// ??????

	@Autowired
	ClaimToReinsuranceService claimToReinsuranceService;
	@Autowired
	ClaimSummaryService claimSummaryService;
	@Autowired
	private AssignService assignService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	WfFlowService wfFlowService;



	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	CarchildService carchildService;
	@Autowired
	ClaimInterfaceLogService claimInterfaceLogService;
	@Autowired
	LossCarService lossCarService;
	@Autowired
	PropLossService propLossService;
	@Autowired
	ScheduleTaskService scheduleTaskService;
    @Autowired
    CompensateHandleServiceIlogService conpensateHandleServiceIlogService;
	

	@Autowired
    private SendMsgToMobileService sendMsgToMobileService;
    @Autowired
    ClaimToMiddleStageOfCaseService claimToMiddleStageOfCaseService;

	
	@RequestMapping("/claimCancelApply.do")
	public ModelAndView claimEdit(
			@RequestParam(value = "registNo") String registNo,
			@RequestParam(value = "taskId") String taskId) {
		List<PrpLClaimVo> prpLClaimVoList = claimService.findClaimListByRegistNo(registNo);
		List<PrpLCMainVo> prpLCMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);
		List<PrpLClaimVo> prpLClaimVoLists = new ArrayList<PrpLClaimVo>();
		if (prpLClaimVoList != null) {
			for (PrpLClaimVo prpLClaimVo : prpLClaimVoList) {
				if (prpLClaimVo.getValidFlag() != null && (!prpLClaimVo.getValidFlag().equals("0"))) {
					for (PrpLCMainVo prpLCMainVo : prpLCMainVoList) {
						if (prpLClaimVo.getPolicyNo().equals(prpLCMainVo.getPolicyNo())) {
							prpLClaimVo.setInsuredName(prpLCMainVo.getInsuredName());
							prpLClaimVo.setStartDate(prpLCMainVo.getStartDate());
							prpLClaimVo.setEndDate(prpLCMainVo.getEndDate());
						}
					}
					prpLClaimVoLists.add(prpLClaimVo);
				}
			}
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("taskId", taskId);
		modelAndView.addObject("prpLClaimVoList", prpLClaimVoLists);
		modelAndView.setViewName("claimCancel/claimCancelApply");
		return modelAndView;
	}

	// ????????????????????????
	@RequestMapping("/claimCancelInits.do")
	public ModelAndView claimCancelInits(
			@RequestParam(value = "claimNo") String claimNo,
			@RequestParam(value = "taskId") String taskId,
			@RequestParam(value = "workStatus") String workStatus) {

		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(claimNo);
		ModelAndView modelAndView = new ModelAndView();
		if (prpLClaimVo.getRegistNo() != null) {
			String registNo = prpLClaimVo.getRegistNo();
			PrpLCMainVo prpLCMainVo = policyViewService.getRegistNoInfo(registNo);
			if (prpLCMainVo != null) {
				List<PrpLCItemCarVo> prpCItemCars = prpLCMainVo.getPrpCItemCars();
				if (prpCItemCars != null) {
					modelAndView.addObject("prpCItemCar", prpCItemCars.get(0));
				}
				//??????????????????
				BigDecimal bigDecimal = new BigDecimal(claimSummaryService.claimNumber(prpLCMainVo.getPolicyNo()));
				prpLCMainVo.setRegistTimes(bigDecimal);
			}

			PrpLRegistExtVo prpLRegistExtVo = registQueryService.getPrpLRegistExtInfo(registNo);
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
			modelAndView.addObject("prpLCMainVo", prpLCMainVo);
			modelAndView.addObject("prpLRegistExtVo", prpLRegistExtVo);
			modelAndView.addObject("prpLRegistVo", prpLRegistVo);
			String flags = claimCancelService.validCheckClaim(registNo,claimNo);
			modelAndView.addObject("flags", flags);
		}
		modelAndView.addObject("prpLClaimVo", prpLClaimVo);
		modelAndView.addObject("taskId", taskId);

		BigDecimal traceId = claimCancelService.findId(
				prpLClaimVo.getRiskCode(), claimNo);
		// ????????????????????????????????????
		if (traceId == null) {
			modelAndView.setViewName("claimCancel/claimCancelApplyEdit");
		} else {
			PrpLcancelTraceVo prpLcancelTraceVo = claimCancelService.findByCancelTraceId(traceId);

			List<PrpLrejectClaimTextVo> prpLrejectClaimTextVos = claimCancelService.findById(traceId);
			List<PrpLrejectClaimTextVo> prpLrejectClaimTextVoList = new ArrayList<PrpLrejectClaimTextVo>();
			if (prpLrejectClaimTextVos != null) {
				for (int i = 0; i < prpLrejectClaimTextVos.size(); i++) {
					if (prpLrejectClaimTextVos.get(i).getDescription() != null) {
						prpLrejectClaimTextVoList.add(prpLrejectClaimTextVos.get(i));
					}
				}
			}

			if (prpLcancelTraceVo.getFlags().trim().equals("1")) {
				// ??????
				modelAndView.addObject("Status", "1");
				modelAndView.addObject("id", traceId);
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				modelAndView.setViewName("claimCancel/claimCancelApplyEdits");
				// ??????/??????????????????
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				return modelAndView;
			} else if (prpLcancelTraceVo.getFlags().trim().equals("0")&& !workStatus.equals("6")|| prpLcancelTraceVo.getFlags().trim().equals("5")) {
				modelAndView.addObject("Status", "0");// ??????
				modelAndView.addObject("id", traceId);
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				modelAndView.setViewName("claimCancel/claimCancelApplyEdits");
				// ??????/??????????????????
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				return modelAndView;
			} else if (prpLcancelTraceVo.getFlags().trim().equals("4")) {
				// ????????????claimCancelApplyEdit??????
				modelAndView.addObject("Status", "4");// ?????????
				modelAndView.setViewName("claimCancel/claimCancelApplyEdit");
				return modelAndView;
			} else if (workStatus.equals("6")) {
				modelAndView.addObject("Status", "1");// ??????
				modelAndView.addObject("id", traceId);
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				modelAndView.setViewName("claimCancel/claimCancelApplyEdits");
				// ??????/??????????????????
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				return modelAndView;
			} else if (prpLcancelTraceVo.getFlags().trim().equals("9")) {
				modelAndView.addObject("Status", "1");// ??????
				modelAndView.addObject("id", traceId);
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				modelAndView.setViewName("claimCancel/claimCancelApplyEdits");
				// ??????/??????????????????
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				return modelAndView;
			} else if (prpLcancelTraceVo.getFlags().trim().equals("8")) {
				modelAndView.addObject("Status", "1");// ??????
				modelAndView.addObject("id", traceId);
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				modelAndView.setViewName("claimCancel/claimCancelApplyEdits");
				// ??????/??????????????????
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				return modelAndView;
			} else if (prpLcancelTraceVo.getFlags().trim().equals("11")) {
				modelAndView.addObject("Status", "0");// ??????
				modelAndView.addObject("id", traceId);
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				modelAndView.setViewName("claimCancel/claimCancelApplyEdits");
				// ??????/??????????????????
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				return modelAndView;
			}

		}
		return modelAndView;
	}

	// ????????????????????????
	@RequestMapping("/claimInitZhanCun.do")
	@ResponseBody
	public void claimInitZhanCun(PrpLcancelTraceVo prpLcancelTraceVo) {
		BigDecimal traceIds = claimCancelService.findId(prpLcancelTraceVo.getRiskCode(),prpLcancelTraceVo.getClaimNo());
		SysUserVo userVo = WebUserUtils.getUser();
		
		if (traceIds == null) {
			claimCancelService.zhanCun(prpLcancelTraceVo,userVo);
			this.claimInitZhanCunToF(prpLcancelTraceVo);
		} else {
			
			PrpLcancelTraceVo prpLcancelTraceDVo = claimCancelService.findByCancelTraceId(traceIds);
			if (prpLcancelTraceDVo.getFlags().trim().equals("4")||prpLcancelTraceDVo.getFlags().trim().equals("11")) {
				claimCancelService.zhanCun(prpLcancelTraceVo,userVo);
				this.claimInitZhanCunToF(prpLcancelTraceVo);
			} else {
				claimCancelService.claimInitZhanC(prpLcancelTraceDVo);
			}

		}
	}

	// ????????????????????????
	@RequestMapping("/claimInitZhanC.do")
	@ResponseBody
	public void claimInitZhanC(PrpLcancelTraceVo prpLcancelTraceVo) {
		claimCancelService.claimInitZhanC(prpLcancelTraceVo);
	}

	// ????????????????????????
	@RequestMapping("/claimCancelSave.do")
	@ResponseBody
	public void claimCancelSave(PrpLcancelTraceVo prpLcancelTraceVo) {
		BigDecimal traceId = claimCancelService.findId(prpLcancelTraceVo.getRiskCode(),prpLcancelTraceVo.getClaimNo());
		if (traceId == null) {
			this.setSave(prpLcancelTraceVo);
		} else {
			PrpLcancelTraceVo cancelTraceVo = claimCancelService.findByCancelTraceId(traceId);
			// ????????????????????????????????????
			// ??????????????????????????????setSave
			if (cancelTraceVo.getFlags().trim().equals("4")) {
				this.setSave(prpLcancelTraceVo);
			} else {
				this.setUpdate(prpLcancelTraceVo);
			}
		}
	}

	private void setSave(PrpLcancelTraceVo prpLcancelTraceVo) {
		SysUserVo userVo = WebUserUtils.getUser();
		BigDecimal A = claimCancelService.save(prpLcancelTraceVo,userVo,FlowNode.CancelApp.getName());
		//??????????????????
		//prpLcancelTraceVo.setId(A);
		Long id = A.longValue();
		String subNodeCode = FlowNode.ClaimBI.name();
		if ("1101".equals(prpLcancelTraceVo.getRiskCode().trim())) {
			subNodeCode = FlowNode.ClaimCI.name();
		}
		// ??????????????? regist subnodecode
		PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTaskId(
				prpLcancelTraceVo.getFlowTask(), subNodeCode);
		// ??????????????????????????????CancelVrf_LV
		// ???????????????
		WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
		taskVo.setRegistNo(prpLWfTaskVo.getRegistNo());
		taskVo.setHandlerIdKey(id.toString());
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(prpLWfTaskVo.getFlowId());

		submitVo.setCurrentNode(FlowNode.CancelApp);
		
		submitVo.setHandleruser(WebUserUtils.getUserCode());
		//????????????
		String policyNoComCode = policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo());
		submitVo.setComCode(policyNoComCode);
		submitVo.setTaskInKey(prpLWfTaskVo.getRegistNo());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
		submitVo.setAssignCom(policyNoComCode);
		PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
		String applyReason = CodeTranUtil.transCode("ApplyReason",prpLcancelTraceVo.getApplyReason());
		pClaimCancelVo.setApplyReason(applyReason);
		pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
		pClaimCancelVo.setClaimCancelTime(new Date());
		pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
		pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
		// ??????????????????????????????CancelVrf_LV
		/*
		 * submitVo.setNextNode(FlowNode.CancelApp); PrpLWfTaskVo prpLWfTask =
		 * wfTaskHandleService.addSimpleTask(taskVo, submitVo);
		 */
		// PrpLWfTaskVo wfTaskVo =
		// wfTaskHandleService.queryTask(Double.parseDouble(prpLcancelTraceVo.getFlowTask()));
		submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
		submitVo.setNextNode(FlowNode.END);
		submitVo.setHandleIdKey(id.toString());
		// wfTaskHandleService.submitSimpleTask(taskVo, submitVo);
		wfTaskHandleService.submitClaimTask(taskVo, submitVo, pClaimCancelVo);
		submitVo.setNextNode(FlowNode.CancelVrf_LV1);
		submitVo.setAssignCom(policyNoComCode);
		submitVo.setHandleruser(WebUserUtils.getUserCode());
		//??????
		/*prpLcancelTraceVo.setStationName(submitVo.getCurrentNode().getName());
		claimCancelService.savePrpLrejectClaimText(prpLcancelTraceVo,userVo);*/
		wfTaskHandleService.addCancelTask(taskVo, submitVo, pClaimCancelVo);

	}

	private void setUpdate(PrpLcancelTraceVo prpLcancelTraceVo) {
		SysUserVo userVo = WebUserUtils.getUser();
		BigDecimal A = claimCancelService.updates(prpLcancelTraceVo,userVo,FlowNode.CancelApp.getName());
		//??????????????????
		//prpLcancelTraceVo.setId(A);
		Long id = A.longValue();
		String subNodeCode = FlowNode.ClaimBI.name();
		if ("1101".equals(prpLcancelTraceVo.getRiskCode().trim())) {
			subNodeCode = FlowNode.ClaimCI.name();
		}
		// ??????????????? regist subnodecode
		PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTaskId(
				prpLcancelTraceVo.getFlowTask(), subNodeCode);
		// ??????????????????????????????
		// ???????????????
		WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
		taskVo.setRegistNo(prpLWfTaskVo.getRegistNo());
		taskVo.setHandlerIdKey(id.toString());

		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(prpLWfTaskVo.getFlowId());

		submitVo.setCurrentNode(FlowNode.CancelApp);
		submitVo.setNextNode(FlowNode.CancelVrf_LV1);
		//????????????
		String policyNoComCode = policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo());
		submitVo.setAssignCom(policyNoComCode);
		submitVo.setHandleruser(WebUserUtils.getUserCode());

		submitVo.setComCode(policyNoComCode);
		submitVo.setTaskInKey(prpLWfTaskVo.getRegistNo());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
		PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
		String applyReason = CodeTranUtil.transCode("ApplyReason",prpLcancelTraceVo.getApplyReason());
		pClaimCancelVo.setApplyReason(applyReason);
		pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
		pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
		pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
		pClaimCancelVo.setClaimCancelTime(new Date());
		submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
		submitVo.setNextNode(FlowNode.END);
		submitVo.setHandleIdKey(id.toString());
		// wfTaskHandleService.submitSimpleTask(taskVo, submitVo);
		wfTaskHandleService.submitClaimTask(taskVo, submitVo, pClaimCancelVo);
		submitVo.setNextNode(FlowNode.CancelVrf_LV1);
		submitVo.setAssignCom(policyNoComCode);
		submitVo.setHandleruser(WebUserUtils.getUserCode());
		//??????
		/*prpLcancelTraceVo.setStationName(submitVo.getCurrentNode().getName());
		claimCancelService.savePrpLrejectClaimText(prpLcancelTraceVo,userVo);*/
		wfTaskHandleService.addCancelTask(taskVo, submitVo, pClaimCancelVo);
	}

	// ??????????????????
	@RequestMapping("/claimCancelJuPei.do")
	@ResponseBody
	public void claimCancelJuPei(PrpLcancelTraceVo prpLcancelTraceVo) {
		BigDecimal traceId = claimCancelService.findId(prpLcancelTraceVo.getRiskCode(),prpLcancelTraceVo.getClaimNo());
		BigDecimal A = new BigDecimal(0);
		SysUserVo userVo = WebUserUtils.getUser();
		if (traceId == null) {
			A = claimCancelService.save(prpLcancelTraceVo,userVo,FlowNode.CancelAppJuPei.getName());
		} else {
			A = claimCancelService.updates(prpLcancelTraceVo,userVo,FlowNode.CancelAppJuPei.getName());
		}
		//??????????????????
		//prpLcancelTraceVo.setId(A);
		Long id = A.longValue();
		String subNodeCode = FlowNode.ClaimBI.name();
		if ("1101".equals(prpLcancelTraceVo.getRiskCode().trim())) {
			subNodeCode = FlowNode.ClaimCI.name();
		}
		// ??????????????? regist subnodecode
		PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTaskId(prpLcancelTraceVo.getFlowTask(), subNodeCode);

		// ???????????????
		WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
		taskVo.setRegistNo(prpLWfTaskVo.getRegistNo());
		taskVo.setHandlerIdKey(id.toString());

		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(prpLWfTaskVo.getFlowId());
		submitVo.setCurrentNode(FlowNode.CancelAppJuPei);
		//????????????
		String policyNoComCode = policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo());
		submitVo.setComCode(policyNoComCode);
		submitVo.setTaskInKey(prpLWfTaskVo.getRegistNo());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
		submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
		submitVo.setNextNode(FlowNode.END);
		submitVo.setHandleIdKey(id.toString());
		// wfTaskHandleService.submitSimpleTask(taskVo, submitVo);
		submitVo.setHandleruser(WebUserUtils.getUserCode());
		PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
		String applyReason = CodeTranUtil.transCode("ApplyReason",prpLcancelTraceVo.getApplyReason());
		pClaimCancelVo.setApplyReason(applyReason);
		// pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
		pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
		pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
		pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
		wfTaskHandleService.submitClaimTask(taskVo, submitVo, pClaimCancelVo);
		submitVo.setCurrentNode(FlowNode.CancelAppJuPei);
	/*	submitVo.setNextNode(FlowNode.VClaim_BI_LV1);
		if ("1101".equals(prpLcancelTraceVo.getRiskCode().trim())) {
			submitVo.setNextNode(FlowNode.VClaim_CI_LV1);
		}*/
		
		Double sumAmt =  prpLClaimVo.getSumClaim().doubleValue();
		SysUserVo assUserVo = null;
        PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,WebUserUtils.getComCode());
        if("1".equals(configValueVo.getConfigValue())){//??????ilog
            //ilog????????????start====================
            LIlogRuleResVo vPriceResVo = null;
            try{
                vPriceResVo = conpensateHandleServiceIlogService.organizaForClaimCancelJuPei(prpLClaimVo,new BigDecimal(sumAmt),"2","05",new BigDecimal(prpLcancelTraceVo.getTaskId()),FlowNode.CancelAppJuPei.name(),userVo);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            String nextNode = "";
            if(Integer.parseInt(vPriceResVo.getMinUndwrtNode()) > Integer.parseInt(vPriceResVo.getMaxUndwrtNode())){
                if ("1101".equals(prpLcancelTraceVo.getRiskCode().trim())) {
                    nextNode = "VClaim_CI_LV"+vPriceResVo.getMaxUndwrtNode();
                    submitVo.setNextNode(FlowNode.valueOf(nextNode));
                }else{
                    nextNode = "VClaim_BI_LV"+vPriceResVo.getMaxUndwrtNode();
                    submitVo.setNextNode(FlowNode.valueOf(nextNode));
                }
            }else{
                if ("1101".equals(prpLcancelTraceVo.getRiskCode().trim())) {
                    nextNode = "VClaim_CI_LV"+vPriceResVo.getMinUndwrtNode();
                    submitVo.setNextNode(FlowNode.valueOf(nextNode));
                }else{
                    nextNode = "VClaim_BI_LV"+vPriceResVo.getMinUndwrtNode();
                    submitVo.setNextNode(FlowNode.valueOf(nextNode));
                }
            }
            if(assUserVo == null){
                int level = Integer.parseInt(vPriceResVo.getMinUndwrtNode())+1;
                for(; level < 9; level++){
                    if ("1101".equals(prpLcancelTraceVo.getRiskCode().trim())) {
                        nextNode = "VClaim_CI_LV"+ String.valueOf(level);
                        submitVo.setNextNode(FlowNode.valueOf(nextNode));
                    }else{
                        nextNode = "VClaim_BI_LV"+String.valueOf(level);
                        submitVo.setNextNode(FlowNode.valueOf(nextNode));
                    }
                    assUserVo = assignService.execute(submitVo.getNextNode(),policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo()),userVo, "");
    
                    if(assUserVo != null){
                        break;
                    }
                }
                
            }
        }//else{
        //????????????
        PrpLConfigValueVo configRuleValueVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,WebUserUtils.getComCode());
        if("1".equals(configRuleValueVo.getConfigValue())){
            VerifyClaimRuleVo ruleVo = new VerifyClaimRuleVo();
            
            ruleVo.setSumAmt(sumAmt);
            ruleVo.setComCode(policyNoComCode);
            if("1101".equals(prpLClaimVo.getRiskCode().trim())){
                ruleVo.setClassCode("11");
            }else{
                ruleVo.setClassCode("12");
            }
            ruleVo = claimRuleApiService.compToVClaim(ruleVo);
            //??????
            // ????????????
            String nextNode = "";
            if(ruleVo.getBackLevel() > ruleVo.getMaxLevel()){
                if ("1101".equals(prpLcancelTraceVo.getRiskCode().trim())) {
                    nextNode = "VClaim_CI_LV"+ruleVo.getMaxLevel();
                    submitVo.setNextNode(FlowNode.valueOf(nextNode));
                }else{
                    nextNode = "VClaim_BI_LV"+ruleVo.getMaxLevel();
                    submitVo.setNextNode(FlowNode.valueOf(nextNode));
                }
            }else{
                if ("1101".equals(prpLcancelTraceVo.getRiskCode().trim())) {
                    nextNode = "VClaim_CI_LV"+ruleVo.getBackLevel();
                    submitVo.setNextNode(FlowNode.valueOf(nextNode));
                }else{
                    nextNode = "VClaim_BI_LV"+ruleVo.getBackLevel();
                    submitVo.setNextNode(FlowNode.valueOf(nextNode));
                }
            }
            if(assUserVo == null){
                int level = ruleVo.getBackLevel()+1;
                for(; level < 9; level++){
                    if ("1101".equals(prpLcancelTraceVo.getRiskCode().trim())) {
                        nextNode = "VClaim_CI_LV"+ String.valueOf(level);
                        submitVo.setNextNode(FlowNode.valueOf(nextNode));
                    }else{
                        nextNode = "VClaim_BI_LV"+String.valueOf(level);
                        submitVo.setNextNode(FlowNode.valueOf(nextNode));
                    }
                    assUserVo = assignService.execute(submitVo.getNextNode(),policyNoComCode,userVo, "");
    
                    if(assUserVo != null){
                        break;
                    }
                }
                
            }
        }

		if(assUserVo == null){
			throw new IllegalArgumentException(submitVo.getNextNode().getName()+"???????????????");
		}
		submitVo.setAssignUser(assUserVo.getUserCode());
		submitVo.setAssignCom(assUserVo.getComCode());
		//submitVo.setAssignCom(policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo()));
		//??????
		/*prpLcancelTraceVo.setStationName(submitVo.getCurrentNode().getName());
		claimCancelService.savePrpLrejectClaimText(prpLcancelTraceVo,userVo);*/
		wfTaskHandleService.addCancelTask(taskVo, submitVo, pClaimCancelVo);

	}

	// ?????????????????????????????????????????????
	@RequestMapping("/claimCancelCheckInit.do")
	public ModelAndView claimCancelCheckInit(
			@RequestParam(value = "claimNo") String claimNo,
			@RequestParam(value = "types") String types,
			@RequestParam(value = "flowTaskId") String flowTaskId,
			String handlerStatus, String subNodeCode) {
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(claimNo);
		ModelAndView modelAndView = new ModelAndView();
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.valueOf(flowTaskId));
		
		//?????????????????????
		PrpLWfTaskVo prpLWfTaskVo= wfTaskHandleService.queryTaskForHandlerStatus(Double.valueOf(flowTaskId));
		String handlerStatusById = prpLWfTaskVo.getHandlerStatus();
		if(handlerStatusById != "" && handlerStatusById != null){
			handlerStatus = handlerStatusById;
		}
		PrpLcancelTraceVo prpLcancelTraceVo = new PrpLcancelTraceVo();
		List<PrpLrejectClaimTextVo> claimTextVoListByTaskInUser = new ArrayList<PrpLrejectClaimTextVo>();
		if (prpLClaimVo.getRegistNo() != null) {
			String registNo = prpLClaimVo.getRegistNo();
			PrpLCMainVo prpLCMainVo = policyViewService.getRegistNoInfo(registNo);
			if (prpLCMainVo != null) {
				List<PrpLCItemCarVo> prpCItemCars = prpLCMainVo.getPrpCItemCars();
				if (prpCItemCars != null) {
					modelAndView.addObject("prpCItemCar", prpCItemCars.get(0));
				}
				//??????????????????
				BigDecimal bigDecimal = new BigDecimal(claimSummaryService.claimNumber(prpLCMainVo.getPolicyNo()));
				prpLCMainVo.setRegistTimes(bigDecimal);
			}

			PrpLRegistExtVo prpLRegistExtVo = registQueryService
					.getPrpLRegistExtInfo(registNo);
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
			modelAndView.addObject("prpLCMainVo", prpLCMainVo);
			modelAndView.addObject("prpLRegistExtVo", prpLRegistExtVo);
			//????????????
			
			//reportorPhone
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);
			if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){//??????
				prpLRegistVo.setReportorPhone(DataUtils .replacePrivacy(prpLRegistVo.getReportorPhone()));
			
			}
			modelAndView.addObject("prpLRegistVo", prpLRegistVo);
			modelAndView.addObject("taskId", flowTaskId);
			// ??????????????????id????????????start
			String ids = wfTaskVo.getHandlerIdKey();
			BigDecimal ides = new BigDecimal(ids);
			PrpLrejectClaimTextVo prpLrejectClaimTextVo = claimCancelService.findByCancelClaimTextId(ides);
			BigDecimal id = prpLrejectClaimTextVo.getPrplcancelTraceId();
			// ??????????????????id????????????end
			// ??????????????????????????????id
			String riskCode = prpLClaimVo.getRiskCode();//
			// BigDecimal id = claimCancelService.findId(riskCode, claimNo);
			prpLcancelTraceVo = claimCancelService.findByCancelTraceId(id);
			modelAndView.addObject("id", id);
			modelAndView.addObject("handlerStatus", handlerStatus);
			List<PrpLrejectClaimTextVo> prpLrejectClaimTextVos = claimCancelService.findById(id);
			List<PrpLrejectClaimTextVo> prpLrejectClaimTextVoList = new ArrayList<PrpLrejectClaimTextVo>();
			
			//????????????????????????
			String handlerUser = prpLWfTaskVo.getHandlerUser();
			if (prpLrejectClaimTextVos != null) {
				for (int i = 0; i < prpLrejectClaimTextVos.size(); i++) {
					if (prpLrejectClaimTextVos.get(i).getDescription() != null) {
						prpLrejectClaimTextVoList.add(prpLrejectClaimTextVos.get(i));
					}
					if(handlerUser.equals(prpLrejectClaimTextVos.get(i).getOperatorCode()) &&
							StringUtils.isNotBlank(prpLrejectClaimTextVos.get(i).getReasonCode())){
						claimTextVoListByTaskInUser.add(prpLrejectClaimTextVos.get(i));
					}
				}
			}
			
			
			modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
			modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
			modelAndView.addObject("flages", prpLcancelTraceVo.getFlag());
		}
		// PrpLWfTaskVo wfTaskVo =
		// wfTaskHandleService.queryTask(Double.valueOf(flowTaskId));
		modelAndView.addObject("prpLClaimVo", prpLClaimVo);
		modelAndView.addObject("wfTaskVo", wfTaskVo);
		modelAndView.addObject("subNodeCode", subNodeCode);
		if("2".equals(handlerStatus)){
			PrpLrejectClaimTextVo prpLrejectClaimTextVo = new  PrpLrejectClaimTextVo();
			prpLrejectClaimTextVo.setDescription(prpLcancelTraceVo.getAandelCode());
			prpLrejectClaimTextVo.setReasonCode(prpLcancelTraceVo.getReasonCode());
			modelAndView.addObject("claimTextVoByTaskInUser", prpLrejectClaimTextVo);
		}else{
			if(claimTextVoListByTaskInUser!=null && claimTextVoListByTaskInUser.size() > 0){
				//??????????????????????????????????????????????????????
				Collections.sort(claimTextVoListByTaskInUser, new Comparator<PrpLrejectClaimTextVo>() {
				@Override
				public int compare(PrpLrejectClaimTextVo o1,PrpLrejectClaimTextVo o2) {
						return o2.getOperateDate().compareTo(o1.getOperateDate());
					}
				});
				modelAndView.addObject("claimTextVoByTaskInUser", claimTextVoListByTaskInUser.get(0));
			}else{
				PrpLrejectClaimTextVo prpLrejectClaimTextVo = new  PrpLrejectClaimTextVo();
				prpLrejectClaimTextVo.setDescription(prpLcancelTraceVo.getAandelCode());
				prpLrejectClaimTextVo.setReasonCode(prpLcancelTraceVo.getReasonCode());
				modelAndView.addObject("claimTextVoByTaskInUser", prpLrejectClaimTextVo);
			}
		}
		
		
		if (types.equals("2")) {
			modelAndView.setViewName("claimCancel/CancelApplyCheckLast");
		} else {
			modelAndView.setViewName("claimCancel/ClaimCancelApplyCheck");
		}
		return modelAndView;
	}

	// ??????????????????????????????????????????
	@RequestMapping("/claimCancelEnd.do")
	@ResponseBody
	public AjaxResult claimCancelEnd(PrpLcancelTraceVo prpLcancelTraceVo) {
		AjaxResult ajax=new AjaxResult();
		try{
			boolean flag=wfFlowService.findValidPrePay(prpLcancelTraceVo.getClaimNo());
	   	    if(flag){
	   		 throw new IllegalArgumentException("????????????????????????????????????????????????????????????");
	   	    }
			String currentNode = prpLcancelTraceVo.getSubNodeCode();
			Integer nowLevel = Integer.parseInt(currentNode.substring(12));
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
			wfTaskVo.setHandlerStatus("3");
			Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
			// TODO zjd ???????????????
			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
			taskVo.setRegistNo(wfTaskVo.getRegistNo());
			taskVo.setHandlerIdKey(id.toString());
			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			submitVo.setFlowId(wfTaskVo.getFlowId());
			submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
			submitVo.setNextNode(FlowNode.valueOf(currentNode));
			submitVo.setHandleruser(WebUserUtils.getUserCode());
			submitVo.setComCode(policyViewService.getPolicyComCode(wfTaskVo.getRegistNo()));
			//submitVo.setComCode(WebUserUtils.getComCode());
			submitVo.setTaskInKey(wfTaskVo.getRegistNo());
			submitVo.setTaskInUser(WebUserUtils.getUserCode());
			submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
			submitVo.setNextNode(FlowNode.END);
			// if (!(wfTaskVo.getWorkStatus().equals("6"))) {
			PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
			String applyReason = CodeTranUtil.transCode("ApplyReason",prpLcancelTraceVo.getApplyReason());
			pClaimCancelVo.setApplyReason(applyReason);
			pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
			PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
			pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setClaimCancelTime(new Date());
			// wfTaskHandleService.submitSimpleTask(taskVo, submitVo);
			wfTaskHandleService.submitClaimTask(taskVo, submitVo, pClaimCancelVo);
			// }

			// ???????????????
			taskVo.setRegistNo(wfTaskVo.getRegistNo());
			taskVo.setHandlerIdKey(id.toString());
			if (nowLevel == 1) {// ?????????????????????
				// start
				// ??????VerifyClaimCancelRuleVo claimNo
				// PrpLClaimVo prpLClaimVo =
				// claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());

				VerifyClaimCancelRuleVo ruleVo = new VerifyClaimCancelRuleVo();
				ruleVo.setSumLossFee(prpLClaimVo.getSumClaim().doubleValue());
				// ??????vo
				ruleVo = claimRuleApiService.claimCanCelToPriceRule(ruleVo);
				Integer backLevel = ruleVo.getBackLevel();
				// end
				if (backLevel > 2) {
					submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
					submitVo.setNextNode(FlowNode.CancelLVrf_LV1);
				} else {
					submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
					// Integer a = backLevel+1;
					// submitVo.setNextNode(FlowNode.ReCanVrf_LV2);

					if (backLevel < 2) {
						if (nowLevel < backLevel) {
							submitVo.setNextNode(FlowNode.valueOf("CancelVrf_LV"+ backLevel));
						} else {
							Integer a = nowLevel + 1;
							submitVo.setNextNode(FlowNode.valueOf("CancelVrf_LV"+ a));
						}
					} else {
						submitVo.setNextNode(FlowNode.valueOf("CancelVrf_LV"+ backLevel));
					}
				}
			} else {
				submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
				Integer a = Integer.parseInt(currentNode.substring(12)) + 1;
				// submitVo.setNextNode(FlowNode.ReCanVrf_LV2);
				if (a <= 2) {
					submitVo.setNextNode(FlowNode.valueOf("CancelVrf_LV" + a));
				} else {
					submitVo.setNextNode(FlowNode.CancelLVrf_LV1);
				}
			}
			submitVo.setFlowId(wfTaskVo.getFlowId());
			/*
			 * submitVo.setCurrentNode(FlowNode.CancelVrf_LV1);
			 * submitVo.setNextNode(FlowNode.CancelVrf_LV2);
			 */
			/*submitVo.setAssignUser(WebUserUtils.getUserCode());*/
			submitVo.setAssignCom(CodeConstants.TOPCOM);
			submitVo.setComCode(policyViewService.getPolicyComCode(wfTaskVo.getRegistNo()));
			//submitVo.setComCode(WebUserUtils.getComCode());
			submitVo.setTaskInKey(wfTaskVo.getRegistNo());
			submitVo.setTaskInUser(WebUserUtils.getUserCode());
			submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
			/*
			 * PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo(); String
			 * applyReason = CodeTranUtil.transCode("ApplyReason",
			 * prpLcancelTraceVo.getApplyReason());
			 * pClaimCancelVo.setApplyReason(applyReason);
			 */
			// pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
			pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
			// PrpLClaimVo prpLClaimVo =
			// claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
			pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setClaimCancelTime(new Date());
			wfTaskHandleService.addCancelTask(taskVo, submitVo, pClaimCancelVo);
			SysUserVo userVo = WebUserUtils.getUser();
			// ??????PrpLrejectClaimText??????
			prpLcancelTraceVo.setId(prpLcancelTraceVo.getId());
			prpLcancelTraceVo.setOpinionCode("03");
			//??????
			prpLcancelTraceVo.setStationName(submitVo.getCurrentNode().getName());
			claimCancelService.savePrpLrejectClaimText(prpLcancelTraceVo,userVo);
			ajax.setStatus(HttpStatus.SC_OK);
		}catch(Exception e){
			ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajax.setStatusText("???????????????"+e.getMessage());
		}
		return ajax;
	}

	// ?????????????????????????????????????????????????????????
	@RequestMapping("/claimCancelTui.do")
	@ResponseBody
	public void claimCancelTui(PrpLcancelTraceVo prpLcancelTraceVo) {
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo
				.getTaskId());
		wfTaskVo.setHandlerStatus("3");
		Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
		// TODO zjd ???????????????
		WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		// ???????????????
		taskVo.setRegistNo(wfTaskVo.getRegistNo());
		taskVo.setHandlerIdKey(id.toString());

		submitVo.setFlowId(wfTaskVo.getFlowId());
		submitVo.setCurrentNode(FlowNode.CancelVrf_LV1);
		submitVo.setNextNode(FlowNode.CancelVrf_LV2);
		//????????????
		String policyNoComCode = policyViewService.getPolicyComCode(wfTaskVo.getRegistNo());
		submitVo.setComCode(policyNoComCode);
		//submitVo.setComCode(WebUserUtils.getComCode());
		submitVo.setTaskInKey(wfTaskVo.getRegistNo());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setHandleruser(WebUserUtils.getUserCode());
		submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
		PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
		String applyReason = CodeTranUtil.transCode("ApplyReason",prpLcancelTraceVo.getApplyReason());
		pClaimCancelVo.setApplyReason(applyReason);
		// pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
		pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
		pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
		pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
		
		submitVo.setAssignCom(policyNoComCode);
		wfTaskHandleService.addCancelTask(taskVo, submitVo, pClaimCancelVo);
		// ??????PrpLrejectClaimText??????
		prpLcancelTraceVo.setId(prpLcancelTraceVo.getId());
		prpLcancelTraceVo.setOpinionCode("03");
		SysUserVo userVo = WebUserUtils.getUser();
		//??????
		prpLcancelTraceVo.setStationName(submitVo.getNextNode().getName());
		claimCancelService.savePrpLrejectClaimText(prpLcancelTraceVo,userVo);

	}

	// ???????????????????????????????????????
	@RequestMapping("/claimCancelZhanCun.do")
	@ResponseBody
	public void claimCancelZhanCun(PrpLcancelTraceVo prpLcancelTraceVo) {
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
		wfTaskVo.setHandlerStatus("2");
		Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
		wfTaskHandleService.tempSaveTask(prpLcancelTraceVo.getTaskId(),id.toString(), WebUserUtils.getUserCode(),WebUserUtils.getComCode());
		// ????????????????????????
		prpLcancelTraceVo.setFlag("1");
		claimCancelService.updateCancelTrace(prpLcancelTraceVo);
	}

	// ???????????????????????????????????????
	@RequestMapping("/claimCancelZCLast.do")
	@ResponseBody
	public void claimCancelZCLast(PrpLcancelTraceVo prpLcancelTraceVo) {
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo
				.getTaskId());
		wfTaskVo.setHandlerStatus("2");
		Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
		wfTaskHandleService.tempSaveTask(prpLcancelTraceVo.getTaskId(),id.toString(), WebUserUtils.getUserCode(),WebUserUtils.getComCode());
		// ????????????????????????
		prpLcancelTraceVo.setFlag("2");
		claimCancelService.updateCancelTrace(prpLcancelTraceVo);
	}

	// ????????????????????????
	@RequestMapping("/claimCancelBack.do")
	@ResponseBody
	public void claimCancelBack(PrpLcancelTraceVo prpLcancelTraceVo) {
		// wfTaskHandleService.queryTaskId(prpLcancelTraceVo.getFlowTask(),subNodeCode);
		PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
		List<PrpLWfTaskVo> prpLWfTaskVo1 = wfTaskHandleService.findEndTask(prpLcancelTraceVo.getRegistNo(), prpLWfTaskVo.getHandlerIdKey(), FlowNode.Cancel);
		
		// ???????????????
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(prpLWfTaskVo.getFlowId());
		submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
		submitVo.setAssignCom(prpLWfTaskVo1.get(0).getHandlerCom());
		submitVo.setComCode(policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo()));
		//submitVo.setComCode(WebUserUtils.getComCode());
		submitVo.setTaskInKey(prpLWfTaskVo.getRegistNo());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		//????????????
		PrpLWfTaskVo prpLWfTaskVoByOne = wfTaskHandleService.findEndTask(prpLcancelTraceVo.getRegistNo(),
				prpLWfTaskVo.getHandlerIdKey(), FlowNode.CancelVrf_LV1).get(0);
		/*submitVo.setAssignUser(WebUserUtils.getUserCode());*/
		submitVo.setHandleruser(prpLWfTaskVoByOne.getHandlerUser());
		submitVo.setAssignCom(prpLWfTaskVoByOne.getComCode());
		System.out.println("=====prpLWfTaskVoByOne.getHandlerUser()"+prpLWfTaskVoByOne.getHandlerUser());
		// start
		String currentNode = prpLcancelTraceVo.getSubNodeCode();
		Integer nowLevel = Integer.parseInt(currentNode.substring(13));
		if (nowLevel == 1) {
			submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
			submitVo.setNextNode(FlowNode.CancelVrf_LV1);
		}/*
		 * else{ submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
		 * nowLevel = nowLevel-1;
		 * submitVo.setNextNode(FlowNode.valueOf("ReCanVrf_LV"+nowLevel)); }
		 */
		// end
		/*
		 * submitVo.setCurrentNode(FlowNode.CancelVrf_LV2);
		 * submitVo.setNextNode(FlowNode.CancelVrf_LV1);
		 */
		if (prpLcancelTraceVo.getOpinionName().equals("1")) {
			submitVo.setHandleIdKey(prpLWfTaskVo.getHandlerIdKey());
			PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
			String applyReason = CodeTranUtil.transCode("ApplyReason",prpLcancelTraceVo.getApplyReason());
			pClaimCancelVo.setApplyReason(applyReason);
			// pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
			pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
			PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
			pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
			wfTaskHandleService.backCancelHandle(submitVo, pClaimCancelVo);
		} else {
			// start
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
			wfTaskVo.setHandlerStatus("3");
			Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
			// TODO zjd ???????????????
			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
			taskVo.setRegistNo(wfTaskVo.getRegistNo());
			taskVo.setHandlerIdKey(id.toString());
			submitVo.setFlowId(wfTaskVo.getFlowId());
			submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
			submitVo.setNextNode(FlowNode.CancelApp);
			submitVo.setHandleIdKey(prpLWfTaskVo.getHandlerIdKey());
			prpLWfTaskVoByOne = wfTaskHandleService.findEndTask(prpLcancelTraceVo.getRegistNo(),
					prpLWfTaskVo.getHandlerIdKey(), FlowNode.CancelApp).get(0);
			submitVo.setHandleruser(prpLWfTaskVoByOne.getHandlerUser());
			PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
			String applyReason = CodeTranUtil.transCode("ApplyReason",prpLcancelTraceVo.getApplyReason());
			pClaimCancelVo.setApplyReason(applyReason);
			// pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
			pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
			PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
			pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setRecoverReason(prpLcancelTraceVo.getCancelCode());// ??????/??????????????????
			pClaimCancelVo.setClaimRecoverTime(new Date());// ??????/??????????????????
			wfTaskHandleService.backCancelHandle(submitVo, pClaimCancelVo);
		}
		//
		// ??????PrpLrejectClaimText??????
		prpLcancelTraceVo.setId(prpLcancelTraceVo.getId());
		prpLcancelTraceVo.setOpinionCode("04");
		SysUserVo userVo = WebUserUtils.getUser();
		//??????
		prpLcancelTraceVo.setStationName(submitVo.getCurrentNode().getName());
		claimCancelService.savePrpLrejectClaimText(prpLcancelTraceVo,userVo);
	}

	// ???????????????
	@RequestMapping("/CancelBack.do")
	@ResponseBody
	public void CancelBack(PrpLcancelTraceVo prpLcancelTraceVo) {
		// ????????????????????????start
		PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService
				.queryTask(prpLcancelTraceVo.getTaskId());
		// ???????????????
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(prpLWfTaskVo.getFlowId());
		submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
		submitVo.setComCode(policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo()));
		//submitVo.setComCode(WebUserUtils.getComCode());
		submitVo.setTaskInKey(prpLWfTaskVo.getRegistNo());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		//????????????
		PrpLWfTaskVo prpLWfTaskVoByOne = wfTaskHandleService.findEndTask(prpLcancelTraceVo.getRegistNo(),
				prpLWfTaskVo.getHandlerIdKey(), FlowNode.CancelApp).get(0);
		/*submitVo.setAssignUser(WebUserUtils.getUserCode());*/
		submitVo.setHandleruser(prpLWfTaskVoByOne.getHandlerUser());
		submitVo.setAssignCom(prpLWfTaskVoByOne.getComCode());
		// start
		String currentNode = prpLcancelTraceVo.getSubNodeCode();
		Integer nowLevel = Integer.parseInt(currentNode.substring(12));
		// ?????????????????????????????????????????????
		if (prpLcancelTraceVo.getOpinionName().equals("1")) {
			if (nowLevel != 1) {
				submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
				nowLevel = nowLevel - 1;
				submitVo.setNextNode(FlowNode.valueOf("CancelVrf_LV" + nowLevel));
				submitVo.setHandleIdKey(prpLWfTaskVo.getHandlerIdKey());
				PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
				String applyReason = CodeTranUtil.transCode("ApplyReason",
						prpLcancelTraceVo.getApplyReason());
				pClaimCancelVo.setApplyReason(applyReason);
				// pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
				pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
				PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
				pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
				pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
				pClaimCancelVo.setRecoverReason(prpLcancelTraceVo.getCancelCode());// ??????/??????????????????
				pClaimCancelVo.setClaimRecoverTime(new Date());// ??????/??????????????????
				wfTaskHandleService.backCancelHandle(submitVo, pClaimCancelVo);
			} else {
				PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
				wfTaskVo.setHandlerStatus("3");
				Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
				// TODO zjd ???????????????
				WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
				taskVo.setRegistNo(wfTaskVo.getRegistNo());
				taskVo.setHandlerIdKey(id.toString());
				// WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
				submitVo.setFlowId(wfTaskVo.getFlowId());
				// submitVo.setCurrentNode(FlowNode.CancelVrf_LV1);
				submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
				submitVo.setNextNode(FlowNode.CancelApp);
				/*
				 * submitVo.setAssignUser(wfTaskVo.getHandlerUser());
				 * submitVo.setAssignCom(SecurityUtils.getComCode());
				 * submitVo.setComCode(SecurityUtils.getComCode());
				 * submitVo.setTaskInKey(wfTaskVo.getRegistNo());
				 * submitVo.setTaskInUser(SecurityUtils.getUserName());
				 * submitVo.setFlowTaskId(new
				 * BigDecimal(prpLcancelTraceVo.getTaskId()));
				 * submitVo.setNextNode(FlowNode.END);
				 * wfTaskHandleService.submitSimpleTask(taskVo, submitVo);
				 */
				submitVo.setHandleIdKey(prpLWfTaskVo.getHandlerIdKey());
				PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
				String applyReason = CodeTranUtil.transCode("ApplyReason",prpLcancelTraceVo.getApplyReason());
				pClaimCancelVo.setApplyReason(applyReason);
				// /pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
				pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
				PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
				pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
				pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
				pClaimCancelVo.setRecoverReason(prpLcancelTraceVo.getCancelCode());// ??????/??????????????????
				pClaimCancelVo.setClaimRecoverTime(new Date());// ??????/??????????????????
				wfTaskHandleService.backCancelHandle(submitVo, pClaimCancelVo);
			}
		} else {// ????????????
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
			wfTaskVo.setHandlerStatus("3");
			Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
			// TODO zjd ???????????????
			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
			taskVo.setRegistNo(wfTaskVo.getRegistNo());
			taskVo.setHandlerIdKey(id.toString());
			submitVo.setFlowId(wfTaskVo.getFlowId());
			submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
			submitVo.setNextNode(FlowNode.CancelApp);
			submitVo.setHandleIdKey(prpLWfTaskVo.getHandlerIdKey());
			PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
			String applyReason = CodeTranUtil.transCode("ApplyReason",prpLcancelTraceVo.getApplyReason());
			pClaimCancelVo.setApplyReason(applyReason);
			// pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
			pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
			PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
			pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
			pClaimCancelVo.setRecoverReason(prpLcancelTraceVo.getCancelCode());// ??????/??????????????????
			pClaimCancelVo.setClaimRecoverTime(new Date());// ??????/??????????????????
			wfTaskHandleService.backCancelHandle(submitVo, pClaimCancelVo);
		}
		prpLcancelTraceVo.setId(prpLcancelTraceVo.getId());
		prpLcancelTraceVo.setOpinionCode("04");
		SysUserVo userVo = WebUserUtils.getUser();
		//??????
		prpLcancelTraceVo.setStationName(submitVo.getCurrentNode().getName());
		claimCancelService.savePrpLrejectClaimText(prpLcancelTraceVo,userVo);

	}

	// ??????????????????????????????
	@RequestMapping("/claimCancel.do")
	@ResponseBody
	public AjaxResult claimCancel(PrpLcancelTraceVo prpLcancelTraceVo) {
		AjaxResult ajax=new AjaxResult();
		try{
			boolean flag=wfFlowService.findValidPrePay(prpLcancelTraceVo.getClaimNo());
	   	    if(flag){
	   		 throw new IllegalArgumentException("????????????????????????????????????????????????????????????");
	   	    }
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
			PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
			wfTaskVo.setHandlerStatus("3");
			Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
			// ????????????
			// wfTaskHandleService.cancelTask(SecurityUtils.getUserName(),wfTaskVo.getUpperTaskId());//
			// ??????
			// wfTaskHandleService.cancelTask(SecurityUtils.getUserName(),wfTaskVo.getTaskId());//
			// ??????

			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
			taskVo.setRegistNo(wfTaskVo.getRegistNo());
			taskVo.setHandlerIdKey(id.toString());
			taskVo.setClaimNo(wfTaskVo.getClaimNo());
			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			submitVo.setFlowId(wfTaskVo.getFlowId());
			submitVo.setCurrentNode(FlowNode.CancelLVrf_LV1);
			submitVo.setNextNode(FlowNode.CancelLVrf_LV1);
			//????????????
			String policyNoComCode = policyViewService.getPolicyComCode(wfTaskVo.getRegistNo());
			submitVo.setHandleruser(WebUserUtils.getUserCode());
			submitVo.setComCode(policyNoComCode);
			submitVo.setTaskInKey(wfTaskVo.getRegistNo());
			submitVo.setTaskInUser(WebUserUtils.getUserCode());
			submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
			submitVo.setNextNode(FlowNode.END);
			wfTaskHandleService.submitSimpleTask(taskVo, submitVo);
			// ???????????????????????????
			String next = "0";

			String currentNode = prpLcancelTraceVo.getSubNodeCode();
			// ???????????????????????????
			Integer nowLevel = Integer.parseInt(currentNode.substring(13));
			if (nowLevel == 1) {// ?????????????????????
				// start
				// ??????VerifyClaimCancelRuleVo claimNo

				VerifyClaimCancelRuleVo ruleVo = new VerifyClaimCancelRuleVo();
				ruleVo.setSumLossFee(prpLClaimVo.getSumClaim().doubleValue());
				// ??????vo
				ruleVo = claimRuleApiService.claimCanCelToPriceRule(ruleVo);
				Integer backLevel = ruleVo.getBackLevel();
				// end
				if (nowLevel < 1) {// ????????????????????????????????????????????????????????????1??????????????????????????????????????????
					if (backLevel >= 1) {
						submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
						submitVo.setNextNode(FlowNode.CancelLVrf_LV1);
						next = "1";
					} else {
						submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
						Integer a = backLevel + 1;
						// submitVo.setNextNode(FlowNode.ReCanVrf_LV2);
						if (backLevel < 1) {
							if (nowLevel < backLevel) {
								submitVo.setNextNode(FlowNode.valueOf("CancelLVrf_LV" + backLevel));
							} else {
								Integer kk = nowLevel + 1;
								submitVo.setNextNode(FlowNode.valueOf("CancelLVrf_LV" + kk));
							}
							// submitVo.setNextNode(FlowNode.valueOf("CancelLVrf_LV"+backLevel));
							next = "1";
						}/*
						 * else{ submitVo.setNextNode(FlowNode.CancelLVrf_LV1); next
						 * = "1"; }
						 */
					}
				} else {
					submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
					Integer a = Integer.parseInt(currentNode.substring(13));
					// submitVo.setNextNode(FlowNode.ReCanVrf_LV2);
					if (a != 1) {
						a = Integer.parseInt(currentNode.substring(13)) + 1;
						submitVo.setNextNode(FlowNode.valueOf("CancelLVrf_LV" + a));
						next = "1";
					} else {
						// submitVo.setNextNode(FlowNode.ReCanLVrf_LV13);
					}
				}
			}
			// ??????
			// ???????????????
			PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
			if (next.equals("1")) {
				taskVo.setRegistNo(wfTaskVo.getRegistNo());
				taskVo.setHandlerIdKey(id.toString());
				submitVo.setFlowId(wfTaskVo.getFlowId());
				submitVo.setComCode(policyViewService.getPolicyComCode(wfTaskVo.getRegistNo()));
				//submitVo.setComCode(WebUserUtils.getComCode());
				submitVo.setTaskInKey(wfTaskVo.getRegistNo());
				submitVo.setTaskInUser(WebUserUtils.getUserCode());
				submitVo.setFlowTaskId(new BigDecimal(prpLcancelTraceVo.getTaskId()));
				submitVo.setAssignCom(policyNoComCode);
				
				String applyReason = CodeTranUtil.transCode("ApplyReason",prpLcancelTraceVo.getApplyReason());
				pClaimCancelVo.setApplyReason(applyReason);
				// pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
				pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
				pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
				pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
				pClaimCancelVo.setRecoverReason(prpLcancelTraceVo.getCancelCode());// ??????/??????????????????
				pClaimCancelVo.setClaimRecoverTime(new Date());// ??????/??????????????????
				wfTaskHandleService.addCancelTask(taskVo, submitVo, pClaimCancelVo);
			}
			// ??????PrpLrejectClaimText??????
			prpLcancelTraceVo.setId(prpLcancelTraceVo.getId());
			// ??????PrpLcancelTrace??????????????????
			PrpLcancelTraceVo prpLcancelTraceDVo = claimCancelService
					.findByCancelTraceId(prpLcancelTraceVo.getId());
			prpLcancelTraceDVo.setFlags("5");// ????????????
			claimCancelService.updateCancelDTrace(prpLcancelTraceDVo);
			SysUserVo userVo = WebUserUtils.getUser();
			prpLcancelTraceVo.setOpinionCode("05");
			//??????
			prpLcancelTraceVo.setStationName(submitVo.getCurrentNode().getName());
			claimCancelService.savePrpLrejectClaimText(prpLcancelTraceVo,userVo);
			// ???????????????
			String subNodeCode = FlowNode.ClaimBI.name();
			if ("1101".equals(prpLcancelTraceVo.getRiskCode().trim())) {
				subNodeCode = FlowNode.ClaimCI.name();
			}
			// BigDecimal flowTaskId=null;
			BigDecimal flowTaskId = wfTaskHandleService.findTaskId(
					prpLcancelTraceVo.getClaimNo(), subNodeCode);

			//????????????????????????????????????????????????
			List<PrpLClaimVo> prpLClaimVoList = claimService.findClaimListByRegistNo(wfTaskVo.getRegistNo());
			//????????????????????????
			String flagMTAOrCT = "0";
			if(prpLClaimVoList.size()==2){//?????????????????????????????????????????????
				for(PrpLClaimVo vo:prpLClaimVoList){
					if(vo.getValidFlag().equals("0") || vo.getValidFlag().equals("2")){
					        flagMTAOrCT = "1";
							claimTaskService.cancleClaimForOther(wfTaskVo.getRegistNo(), WebUserUtils.getUserCode());
					}
				}
			}else{//?????????????????????????????????????????????
			    flagMTAOrCT = "1";
				claimTaskService.cancleClaimForOther(wfTaskVo.getRegistNo(), WebUserUtils.getUserCode());
			}
			
			//?????????????????????????????????????????? ???????????????????????????
	        //????????????
	         PrpLWfTaskVo prpLWfTaskVo = sendMsgToMobileService.isMobileCaseAcceptInClaim(wfTaskVo.getRegistNo(),wfTaskVo.getTaskId());
	         if(prpLWfTaskVo != null){ //????????????
	             prpLWfTaskVo.setHandlerStatus("9"); //?????????
	             prpLWfTaskVo.setWorkStatus("9");  // ????????????
	             prpLWfTaskVo.setMobileOperateType(CodeConstants.MobileOperationType.CLAIMCANCLE);
	             String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
	             interfaceAsyncService.packMsg(prpLWfTaskVo,url);
	         }
			
			claimTaskService.cancleClaim(prpLcancelTraceVo.getClaimNo(),"0",
					flowTaskId,userVo,wfTaskVo.getRegistNo(),submitVo);
			
			// ????????????????????????
			prpLcancelTraceVo.setRegistNo(wfTaskVo.getRegistNo());
			prpLcancelTraceVo.setCreateUser(WebUserUtils.getUserCode());
			prpLcancelTraceVo.setComCode(WebUserUtils.getComCode());
			claimCancelService.sendToClaimPlatform(prpLcancelTraceVo);
			
			//??????????????????????????????????????????
			PrpLRegistVo registVo=registQueryService.findByRegistNo(wfTaskVo.getRegistNo());
			try {
				if(registVo!=null && "1".equals(registVo.getIsQuickCase())){
				   interfaceAsyncService.receivecpsresult(wfTaskVo.getRegistNo(),"4",userVo);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			 //??????????????????????????????????????????
				if(registVo!=null && "1".equals(registVo.getSelfClaimFlag())){
					if(prpLClaimVo!=null){
						 interfaceAsyncService.sendClaimResultToSelfClaim(wfTaskVo.getRegistNo(),userVo,"4","4",prpLClaimVo.getPolicyNo());
					}
				}
			
			//?????????????????????????????????
			if("1".equals(flagMTAOrCT)){
			    try{
	                interfaceAsyncService.organizationAndSendCTorMTA(pClaimCancelVo,wfTaskVo,userVo);
	            }
	            catch(Exception e){
	                e.printStackTrace();
	            }
			}
			PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(prpLClaimVo.getRegistNo(), prpLClaimVo.getPolicyNo());
			if(SendPlatformUtil.isMor(prpLCMainVo)){
				//????????????
				String warnswitch = SpringProperties.getProperty("WARN_SWITCH");// 62,10,50
				if(warnswitch.contains(prpLCMainVo.getComCode().substring(0,2))){// prpLCMainVo.getComCode().startsWith("62")
					try {
						//???????????????????????????
						interfaceAsyncService.sendCaseCancleRegister(prpLCMainVo, "claimCancle", prpLcancelTraceVo.getCancelCode(), userVo);	
					} catch (Exception e) {
						logger.info("????????????????????????????????????-------------->"+e.getMessage());
						e.printStackTrace();
					}
					try {
						//???????????????????????????????????????
						interfaceAsyncService.sendFalseCaseToEWByCancel(id.toString());
					} catch (Exception e) {
						logger.info("??????/?????????????????????????????????????????????-------------->"+e.getMessage());
						e.printStackTrace();
					}
				}
			}
			
			//???????????????????????????????????????rabbitMq???????????????????????????
			claimToMiddleStageOfCaseService.middleStageQuery(wfTaskVo.getRegistNo(), "cance");

			//????????????????????????
			interfaceAsyncService.reqByCancel(prpLClaimVo, userVo);
			ajax.setStatus(HttpStatus.SC_OK);
		}catch(Exception e){
			ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajax.setStatusText("???????????????"+e.getMessage());
		}
		
		return ajax;
	}


	// ??????????????????
	@RequestMapping("/claimCancelF.do")
	@ResponseBody
	public AjaxResult claimCancelF(PrpLcancelTraceVo prpLcancelTraceVos) {
		AjaxResult ar = new AjaxResult();
		try {
			PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVos.getClaimNo());
			String workStatus = "";
			List<PrpLWfTaskVo> prpLWfTaskVos = new ArrayList<PrpLWfTaskVo>();
			prpLWfTaskVos = wfTaskHandleService.findCanCelTask(prpLcancelTraceVos.getClaimNo(), FlowNode.CancelApp);
			BigDecimal traceId = claimCancelService.findId(prpLClaimVo.getRiskCode(), prpLcancelTraceVos.getClaimNo());
			PrpLcancelTraceVo prpLcancelTraceVo = new PrpLcancelTraceVo();
			PrpLWfTaskVo vo = new PrpLWfTaskVo();
			if(StringUtils.isNotBlank(String.valueOf(prpLcancelTraceVos.getTaskId()))){
				vo = wfTaskHandleService.queryTaskForHandlerStatus(prpLcancelTraceVos.getTaskId());
				workStatus = vo.getWorkStatus();
			}

			if (traceId == null) {
				// ????????????
				ar.setStatus(HttpStatus.SC_OK);
				ar.setData("1");
			} else {
				prpLcancelTraceVo = claimCancelService.findByCancelTraceId(traceId);
				if(workStatus.equals("6")){
					// ????????????
					ar.setStatus(HttpStatus.SC_OK);
					ar.setData("1");
				}else if (prpLcancelTraceVo.getFlags().trim().equals("0")
						|| prpLcancelTraceVo.getFlags().trim().equals("8")
						|| prpLcancelTraceVo.getFlags().trim().equals("9")
						|| prpLcancelTraceVo.getFlags().trim().equals("7")) {
					// ????????????
					ar.setStatus(HttpStatus.SC_OK);
					ar.setData("2");

				} else {
					// ????????????
					ar.setStatus(HttpStatus.SC_OK);
					ar.setData("1");
				}
			}
			if (ar.getData().equals("1")) {

				prpLcancelTraceVos.setFaQi("8");
				if(traceId==null){
					this.cancelApply(prpLcancelTraceVos, prpLClaimVo, "2",vo,FlowNode.CancelApp);
				}else{
					this.cancelApply(prpLcancelTraceVos, prpLClaimVo, prpLcancelTraceVo.getFlags().trim(),vo,FlowNode.CancelApp);
				}

				ar.setStatus(HttpStatus.SC_OK);
				ar.setData("1");
			}
		} catch (Exception e) {
			logger.info("?????????: {} ???????????????????????????", prpLcancelTraceVos == null ? "" : prpLcancelTraceVos.getClaimNo(), e);
			throw new IllegalArgumentException("???????????????????????????", e);
		}
		return ar;
	}

	// ??????????????????
	@RequestMapping("/claimCancelJuPeiF.do")
	@ResponseBody
	public AjaxResult claimCancelJuPeiF(PrpLcancelTraceVo prpLcancelTraceVos) {
		AjaxResult ar = new AjaxResult();
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVos.getClaimNo());
		try {
			String workStatus = "";
			List<PrpLWfTaskVo> prpLWfTaskVos = new ArrayList<PrpLWfTaskVo>();
			PrpLcancelTraceVo prpLcancelTraceVo = new PrpLcancelTraceVo();
			prpLWfTaskVos = wfTaskHandleService.findCanCelTask(prpLcancelTraceVos.getClaimNo(), FlowNode.CancelAppJuPei);
			BigDecimal traceId = claimCancelService.findId(prpLClaimVo.getRiskCode(), prpLcancelTraceVos.getClaimNo());
			PrpLWfTaskVo vo = new PrpLWfTaskVo();
			if(StringUtils.isNotBlank(String.valueOf(prpLcancelTraceVos.getTaskId()))){
				vo = wfTaskHandleService.queryTaskForHandlerStatus(prpLcancelTraceVos.getTaskId());
				workStatus = vo.getWorkStatus();
			}
			// ????????????????????????????????????
			if (traceId == null) {
				// ????????????
				ar.setStatus(HttpStatus.SC_OK);
				ar.setData("1");
			} else {

				prpLcancelTraceVo = claimCancelService.findByCancelTraceId(traceId);
				if(workStatus.equals("6")){
					// ????????????
					ar.setStatus(HttpStatus.SC_OK);
					ar.setData("1");
				}else if (prpLcancelTraceVo.getFlags().trim().equals("0")
						//|| prpLcancelTraceVo.getFlags().trim().equals("1")
						|| prpLcancelTraceVo.getFlags().trim().equals("9")
						|| prpLcancelTraceVo.getFlags().trim().equals("7")) {
					// ????????????
					ar.setStatus(HttpStatus.SC_OK);
					ar.setData("2");
				} else {
					// ????????????
					ar.setStatus(HttpStatus.SC_OK);
					ar.setData("1");
				}
			}

			if (ar.getData().equals("1")) {
				// ????????????
				prpLcancelTraceVos.setFaQi("9");
				if(traceId==null){
					this.cancelApply(prpLcancelTraceVos, prpLClaimVo, "2",vo,FlowNode.CancelAppJuPei);
				}else{
					this.cancelApply(prpLcancelTraceVos, prpLClaimVo, prpLcancelTraceVo.getFlags().trim(),vo,FlowNode.CancelAppJuPei);
				}

				ar.setStatus(HttpStatus.SC_OK);
				ar.setData("1");
			}
		} catch (Exception e) {
			logger.info("?????????:{} ???????????????????????????", prpLClaimVo == null ? "" : prpLClaimVo.getClaimNo(), e);
			throw new IllegalArgumentException("???????????????????????????");
		}

		// ???????????????????????? niuqiang businessType=2
		try {
			String claimNo = prpLClaimVo.getClaimNo();
			PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
			ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); //???????????????
			claimInterfaceLogVo.setClaimNo(claimNo);
			claimInterfaceLogVo.setRegistNo(prpLClaimVo.getRegistNo());
			claimInterfaceLogVo.setComCode(prpLClaimVo.getComCode());
			claimInterfaceLogVo.setCreateUser(prpLClaimVo.getCreateUser());
			claimInterfaceLogVo.setCreateTime(new Date());
			claimInterfaceLogVo.setOperateNode("??????");
			interfaceAsyncService.TransDataForReinsCaseVo("2", claimVo,claimInterfaceLogVo);
		}catch(Exception e){
			logger.info("?????????: {} ????????????????????????", prpLClaimVo == null ? "" : prpLClaimVo.getClaimNo(), e);
			e.printStackTrace();
			//throw new IllegalArgumentException("???????????????????????? ???????????????businessType=2 <br/>"+e);

		}
		return ar;
	}

	// ????????????????????????
	@RequestMapping("/claimCancelInit.do")
	@ResponseBody
	public ModelAndView claimCancelInit(
			@RequestParam(value = "claimNo") String claimNo,
			@RequestParam(value = "taskId") String taskId,
			@RequestParam(value = "workStatus") String workStatus,
			@RequestParam(value = "handlerIdKey") String handlerIdKey) {
		//???????????????
		/*String[] claimNo1 = claimNo.split(",");
		PrpLClaimVo prpLClaimVo = new PrpLClaimVo();
		PrpLClaimVo prpLClaimVo1 = new PrpLClaimVo();
		if(claimNo1.length>1){
			prpLClaimVo = claimService.findByClaimNo(claimNo1[0]);
			prpLClaimVo1 = claimService.findByClaimNo(claimNo1[1]);
		} else {
			prpLClaimVo = claimService.findByClaimNo(claimNo1[0]);
		}*/
		//?????????????????????
		
		
 		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(claimNo);
		///PrpLClaimVo prpLClaimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
		String handlerStatusById = "";
		if("c".equals(taskId)){
			String subNodeCode = FlowNode.ClaimBI.name();
			if ("1101".equals(prpLClaimVo.getRiskCode().trim())) {
				subNodeCode = FlowNode.ClaimCI.name();
			}
			PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTaskByAny(prpLClaimVo.getRegistNo(),claimNo, subNodeCode, CodeConstants.HandlerStatus.END);
			taskId = prpLWfTaskVo.getTaskId().toString();
			
			
			
		}else{
			//?????????????????????
			PrpLWfTaskVo prpLWfTaskVo= wfTaskHandleService.queryTaskForHandlerStatus(Double.valueOf(taskId));
			handlerStatusById = prpLWfTaskVo.getHandlerStatus();
		}
	
		
		ModelAndView modelAndView = new ModelAndView();

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
				//??????????????????
				BigDecimal bigDecimal = new BigDecimal(claimSummaryService.claimNumber(prpLCMainVo.getPolicyNo()));
				prpLCMainVo.setRegistTimes(bigDecimal);
			}

			PrpLRegistExtVo prpLRegistExtVo = registQueryService.getPrpLRegistExtInfo(registNo);
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
			modelAndView.addObject("prpLCMainVo", prpLCMainVo);
			modelAndView.addObject("prpLRegistExtVo", prpLRegistExtVo);
			//????????????
			//reportorPhone
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);
			if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){//??????
				prpLRegistVo.setReportorPhone(DataUtils .replacePrivacy(prpLRegistVo.getReportorPhone()));
			
			}
			modelAndView.addObject("prpLRegistVo", prpLRegistVo);
			String flags = claimCancelService.validCheckClaim(registNo,claimNo);
			modelAndView.addObject("flags", flags);
		}
		modelAndView.addObject("prpLClaimVo", prpLClaimVo);
		modelAndView.addObject("taskId", taskId);
		BigDecimal traceId = claimCancelService.findId(prpLClaimVo.getRiskCode(),claimNo);
		
		
		// ????????????????????????????????????
		if (traceId == null) {
			modelAndView.setViewName("claimCancel/claimCancelApplyEditX");
		} else {
			PrpLcancelTraceVo cancelTraceVo = claimCancelService.findPrpLcancelTraceVo(prpLClaimVo.getRiskCode(), claimNo,"01 ");
			PrpLcancelTraceVo prpLcancelTraceVo = claimCancelService.findByCancelTraceId(traceId);

			//============??????
			if(cancelTraceVo!=null){
				//if(StringUtils.isNotBlank(handlerIdKey)){
				if(!"c".equals(handlerIdKey)){
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
			
			
			//===============??????
			
			
			
			
			List<PrpLrejectClaimTextVo> prpLrejectClaimTextVos = claimCancelService.findById(traceId);
			List<PrpLrejectClaimTextVo> prpLrejectClaimTextVoList = new ArrayList<PrpLrejectClaimTextVo>();
			if (prpLrejectClaimTextVos != null) {
				for (int i = 0; i < prpLrejectClaimTextVos.size(); i++) {
					if (prpLrejectClaimTextVos.get(i).getDescription() != null) {
						prpLrejectClaimTextVoList.add(prpLrejectClaimTextVos.get(i));
					}
				}
			}
			//if (prpLcancelTraceVo.getFlags().trim().equals("1")||prpLcancelTraceVo.getFlags().trim().equals("11")) {
			if (prpLcancelTraceVo.getFlags().trim().equals("1")) {
			// ??????
				modelAndView.addObject("Status", "1");
				modelAndView.addObject("id", traceId);
				modelAndView.addObject("forCancel", "1");
				
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				
				modelAndView.setViewName("claimCancel/claimCancelApplyEditXs");
				// ??????/??????????????????
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				return modelAndView;
			}else if("3".equals(handlerStatusById)){
				modelAndView.addObject("Status", "0");// ??????
				modelAndView.addObject("id", traceId);
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				modelAndView.setViewName("claimCancel/claimCancelApplyEditXs");
				// ??????/??????????????????
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				return modelAndView;
			}else if("6".equals(workStatus)){
				modelAndView.addObject("tuiHui", "1");
				modelAndView.addObject("Status", "1");
				modelAndView.addObject("id", traceId);
				modelAndView.addObject("forCancel", "1");
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				modelAndView.setViewName("claimCancel/claimCancelApplyEditXs");
				// ??????/??????????????????
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				return modelAndView;
			}else if (prpLcancelTraceVo.getFlags().trim().equals("0")
					|| prpLcancelTraceVo.getFlags().trim().equals("7")) {
				modelAndView.addObject("Status", "0");// ??????
				modelAndView.addObject("id", traceId);
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				modelAndView.setViewName("claimCancel/claimCancelApplyEditXs");
				// ??????/??????????????????
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				return modelAndView;
			} else if (prpLcancelTraceVo.getFlags().trim().equals("4")||prpLcancelTraceVo.getFlags().trim().equals("11")
					||prpLcancelTraceVo.getFlags().trim().equals("5")) {
				// ????????????claimCancelApplyEdit??????
				modelAndView.addObject("Status", "4");// ?????????
				modelAndView.setViewName("claimCancel/claimCancelApplyEditX");
				return modelAndView;
			} else if (prpLcancelTraceVo.getFlags().trim().equals("9")
					|| prpLcancelTraceVo.getFlags().trim().equals("8")) {
				modelAndView.addObject("Status", "0");// ??????
				modelAndView.addObject("id", traceId);
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				modelAndView.setViewName("claimCancel/claimCancelApplyEditXs");
				// ??????/??????????????????
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				return modelAndView;
			} else {
				modelAndView.addObject("Status", "0");// ??????
				modelAndView.addObject("id", traceId);
				modelAndView.addObject("prpLcancelTraceVo", prpLcancelTraceVo);
				modelAndView.setViewName("claimCancel/claimCancelApplyEditXs");
				// ??????/??????????????????
				modelAndView.addObject("prpLrejectClaimTextVos",prpLrejectClaimTextVoList);
				return modelAndView;
			}
		}
		return modelAndView;
	}
	
	//????????????
		@RequestMapping("/cancel.do")
		@ResponseBody
		public void cancel(PrpLcancelTraceVo prpLcancelTraceVo) {
			SysUserVo userVo = WebUserUtils.getUser();
			 claimCancelService.cancelUpdates(prpLcancelTraceVo,userVo);
			BigDecimal flowTaskId = wfTaskHandleService.findTaskId(prpLcancelTraceVo.getClaimNo(), "CancelApp");
		claimTaskService.cancleClaim(flowTaskId,userVo.getUserCode(),userVo);
		}
		@RequestMapping("/cancelJ.do")
		@ResponseBody
		public void cancelJ(PrpLcancelTraceVo prpLcancelTraceVo) {
			SysUserVo userVo = WebUserUtils.getUser();
			claimCancelService.cancelUpdates(prpLcancelTraceVo,userVo);
			BigDecimal flowTaskId = wfTaskHandleService.findTaskId(prpLcancelTraceVo.getClaimNo(), "CancelAppJuPei");
			claimTaskService.cancleClaim(flowTaskId,userVo.getUserCode(),userVo);
		}
	
		
		
		@RequestMapping("/claimCancelApplyByOne.do")
		@ResponseBody
		public AjaxResult claimCancelApplyByOne(String registNo,String taskId,String claimNo) {
			AjaxResult ajaxResult = new AjaxResult();
			List<PrpLClaimVo> prpLClaimVoList = claimService.findClaimListByRegistNo(registNo);
			List<PrpLCMainVo> prpLCMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);
			List<PrpLClaimVo> prpLClaimVoLists = new ArrayList<PrpLClaimVo>();
			if (prpLClaimVoList != null) {
				for (PrpLClaimVo prpLClaimVo : prpLClaimVoList) {
					if (prpLClaimVo.getValidFlag() != null && (!prpLClaimVo.getValidFlag().equals("0"))&&claimNo.equals(prpLClaimVo.getClaimNo())) {
						for (PrpLCMainVo prpLCMainVo : prpLCMainVoList) {
							if (prpLClaimVo.getPolicyNo().equals(prpLCMainVo.getPolicyNo())) {
								prpLClaimVo.setInsuredName(prpLCMainVo.getInsuredName());
								prpLClaimVo.setStartDate(prpLCMainVo.getStartDate());
								prpLClaimVo.setEndDate(prpLCMainVo.getEndDate());
							}
						}
						prpLClaimVoLists.add(prpLClaimVo);
					}
				}
			}
			if(prpLClaimVoLists.size() > 0 && prpLClaimVoLists != null){
				ajaxResult.setStatusText("1");
			}else{
				ajaxResult.setStatusText("0");
			}
			
			ajaxResult.setStatus(HttpStatus.SC_OK);
			return ajaxResult;
		}
		
		
		
		public void cancelApply(PrpLcancelTraceVo prpLcancelTraceVos,PrpLClaimVo prpLClaimVo,String flags,PrpLWfTaskVo lWfTaskVo,FlowNode NextNode) throws Exception {
			// ????????????
			
			SysUserVo userVo = WebUserUtils.getUser();
			if("1".equals(flags) ){//??????
				PrpLWfTaskVo prpLWfTask = wfTaskHandleService.queryTaskByAny(prpLClaimVo.getRegistNo(),prpLClaimVo.getClaimNo(), NextNode.toString(), CodeConstants.HandlerStatus.DOING);
				//??????????????????
				prpLcancelTraceVos.setTaskId(prpLWfTask.getTaskId().doubleValue());
				if(FlowNode.CancelAppJuPei.equals(NextNode)){
					prpLcancelTraceVos.setFlowTask(prpLWfTask.getFlowId());
					this.claimCancelJuPei(prpLcancelTraceVos);
				}else{
				// ???????????????
				this.claimCancelSave(prpLcancelTraceVos);// ??????
				}
			}else if(CodeConstants.WorkStatus.BYBACK.equals(lWfTaskVo.getWorkStatus())){
				if(FlowNode.CancelAppJuPei.equals(NextNode)){
					this.claimCancelJuPei(prpLcancelTraceVos);
					}else{
					//??????????????????
					prpLcancelTraceVos.setTaskId(lWfTaskVo.getTaskId().doubleValue());
					this.claimCancelSave(prpLcancelTraceVos);// ??????
				}
			}else{
				BigDecimal A = claimCancelService.saveF(prpLcancelTraceVos,userVo);
				String subNodeCode = FlowNode.ClaimBI.name();
				if ("1101".equals(prpLClaimVo.getRiskCode().trim())) {
					subNodeCode = FlowNode.ClaimCI.name();
				}
				// ??????????????? regist subnodecode
				PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTaskId(
						prpLClaimVo.getFlowId(), subNodeCode);

				// ???????????????
				WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
				taskVo.setRegistNo(prpLWfTaskVo.getRegistNo());
				taskVo.setHandlerIdKey(prpLWfTaskVo.getTaskId().toString());

				WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
				submitVo.setFlowId(prpLWfTaskVo.getFlowId());

				submitVo.setCurrentNode(FlowNode.valueOf(subNodeCode));
				//????????????
				String policyNoComCode = policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo());
				submitVo.setAssignCom(policyNoComCode);
				submitVo.setComCode(policyNoComCode);
				submitVo.setTaskInKey(prpLWfTaskVo.getRegistNo());
				submitVo.setTaskInUser(WebUserUtils.getUserCode());

				submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
				// ??????????????????????????????CancelVrf_LV
				submitVo.setNextNode(NextNode);

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
				//??????????????????
				PrpLrejectClaimTextVo vo = claimCancelService.findByCancelClaimTextId(A);
				PrpLcancelTraceVo prpLcancelTraceVo = claimCancelService.findByCancelTraceId(vo.getPrplcancelTraceId());
				
				prpLcancelTraceVo.setFlowTask(prpLcancelTraceVos.getFlowTask());
				prpLcancelTraceVo.setRegistNo(prpLcancelTraceVos.getRegistNo());
				prpLcancelTraceVo.setTaskId(prpLWfTask.getTaskId().doubleValue());
				if(FlowNode.CancelAppJuPei.equals(NextNode)){
					this.claimCancelJuPei(prpLcancelTraceVo);
				}else{
				
					if (prpLcancelTraceVo.getFlags().trim().equals("1")) {
						// ??????
					} else if (prpLcancelTraceVo.getFlags().trim().equals("0")&& !wfTaskVo.getWorkStatus().equals("6")|| prpLcancelTraceVo.getFlags().trim().equals("5")) {
						this.claimCancelSave(prpLcancelTraceVo);// ??????
					} else if (prpLcancelTraceVo.getFlags().trim().equals("4")) {
						// ????????????claimCancelApplyEdit??????
						this.claimCancelSave(prpLcancelTraceVo);// ??????
					} else if (wfTaskVo.getWorkStatus().equals("6")) {
						this.claimCancelSave(prpLcancelTraceVo);// ??????
					} else if (prpLcancelTraceVo.getFlags().trim().equals("9")) {
						this.claimCancelSave(prpLcancelTraceVo);// ??????
					} else if (prpLcancelTraceVo.getFlags().trim().equals("8")) {
						this.claimCancelSave(prpLcancelTraceVo);// ??????
					} else if (prpLcancelTraceVo.getFlags().trim().equals("11")) {
						
					}
				}
			}
			
		}
		
		// ????????????????????????
		@RequestMapping("/claimInitZhanCunToF.do")
		@ResponseBody
		public void claimInitZhanCunToF(PrpLcancelTraceVo prpLcancelTraceVo) {
			
			
			PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());

			String subNodeCode = FlowNode.ClaimBI.name();
			if ("1101".equals(prpLClaimVo.getRiskCode().trim())) {
				subNodeCode = FlowNode.ClaimCI.name();
			}
			// ??????????????? regist subnodecode
			PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTaskId(
					prpLClaimVo.getFlowId(), subNodeCode);

			// ???????????????
			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
			taskVo.setRegistNo(prpLWfTaskVo.getRegistNo());
			taskVo.setHandlerIdKey(prpLWfTaskVo.getTaskId().toString());

			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			submitVo.setFlowId(prpLWfTaskVo.getFlowId());

			submitVo.setCurrentNode(FlowNode.valueOf(subNodeCode));
			//????????????
			String policyNoComCode = policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo());
			submitVo.setComCode(policyNoComCode);
			submitVo.setTaskInKey(prpLWfTaskVo.getRegistNo());
			submitVo.setTaskInUser(WebUserUtils.getUserCode());

			submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
			// ??????????????????????????????CancelVrf_LV
			if("3".equals(prpLcancelTraceVo.getDealReasoon()) || "4".equals(prpLcancelTraceVo.getDealReasoon())){
				submitVo.setNextNode(FlowNode.CancelAppJuPei);
			}else{
				submitVo.setNextNode(FlowNode.CancelApp);
			}
			submitVo.setAssignCom(policyNoComCode);
			PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
			pClaimCancelVo.setClaimCancelTime(new Date());
			pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
			pClaimCancelVo.setClaimNo(prpLClaimVo.getClaimNo());
			String applyReason = CodeTranUtil.transCode("ApplyReason",prpLcancelTraceVo.getApplyReason());
			pClaimCancelVo.setApplyReason(applyReason);
			PrpLWfTaskVo prpLWfTask = wfTaskHandleService.addCancelTask(taskVo,submitVo, pClaimCancelVo);
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLWfTask.getTaskId().doubleValue());
			wfTaskVo.setHandlerStatus("2");
			Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
			wfTaskHandleService.tempSaveTask(prpLWfTask.getTaskId().doubleValue(), id.toString(), WebUserUtils.getUserCode(),
					WebUserUtils.getComCode());
		}
		
		
		// ??????????????????id????????????start
		@RequestMapping("/findByHandlerIdKey.do")
		@ResponseBody
		public PrpLcancelTraceVo findByHandlerIdKey(String handlerIdKey) {
			BigDecimal ides = new BigDecimal(handlerIdKey);
			PrpLrejectClaimTextVo prpLrejectClaimTextVo = claimCancelService.findByCancelClaimTextId(ides);
			if(prpLrejectClaimTextVo==null){
				return null;
			}
			BigDecimal id = prpLrejectClaimTextVo.getPrplcancelTraceId();
			// ??????????????????id????????????end
			PrpLcancelTraceVo prpLcancelTraceVo = claimCancelService.findByCancelTraceId(id);
			return prpLcancelTraceVo;
		}
		
		
		// ????????????????????????
		@RequestMapping("/cancelCompel.do")
		@ResponseBody
		public void cancelCompel(PrpLcancelTraceVo prpLcancelTraceVo) {
			// ????????????????????????start
			PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
			// ???????????????
			SysUserVo userVo = WebUserUtils.getUser();
			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			submitVo.setFlowId(prpLWfTaskVo.getFlowId());
			submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
			//????????????
			String policyNoComCode = policyViewService.getPolicyComCode(prpLWfTaskVo.getRegistNo());
			submitVo.setComCode(policyNoComCode);
			submitVo.setTaskInKey(prpLWfTaskVo.getRegistNo());
			submitVo.setTaskInUser(WebUserUtils.getUserCode());
			submitVo.setHandleruser(userVo.getUserCode());
			submitVo.setAssignCom(policyNoComCode);
			// start
			String currentNode = prpLcancelTraceVo.getSubNodeCode();
			// ????????????
				PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(prpLcancelTraceVo.getTaskId());
				wfTaskVo.setHandlerStatus("3");
				Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
				// TODO zjd ???????????????
				WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
				taskVo.setRegistNo(wfTaskVo.getRegistNo());
				taskVo.setHandlerIdKey(id.toString());
				submitVo.setFlowId(wfTaskVo.getFlowId());
				submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
				submitVo.setNextNode(FlowNode.CancelApp);
				submitVo.setHandleIdKey(prpLWfTaskVo.getHandlerIdKey());
				PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
				String applyReason = CodeTranUtil.transCode("ApplyReason",prpLcancelTraceVo.getApplyReason());
				pClaimCancelVo.setApplyReason(applyReason);
				// pClaimCancelVo.setApplyReason(prpLcancelTraceVo.getApplyReason());
				pClaimCancelVo.setCancelTime(prpLcancelTraceVo.getInsertTimeForHis());
				PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLcancelTraceVo.getClaimNo());
				pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
				pClaimCancelVo.setClaimNo(prpLcancelTraceVo.getClaimNo());
				pClaimCancelVo.setRecoverReason(prpLcancelTraceVo.getCancelCode());// ??????/??????????????????
				pClaimCancelVo.setClaimRecoverTime(new Date());// ??????/??????????????????
				wfTaskHandleService.backCancelHandle(submitVo, pClaimCancelVo);
			prpLcancelTraceVo.setId(prpLcancelTraceVo.getId());
			prpLcancelTraceVo.setOpinionCode("04");
			//??????
			prpLcancelTraceVo.setStationName(submitVo.getCurrentNode().getName());
			claimCancelService.savePrpLrejectClaimText(prpLcancelTraceVo,userVo);


			//??????
			//SysUserVo userVo = WebUserUtils.getUser();
			claimCancelService.cancelUpdates(prpLcancelTraceVo,userVo);
			BigDecimal flowTaskId = new BigDecimal(0);
			if("1".equals(prpLcancelTraceVo.getDealReasoon()) || "2".equals(prpLcancelTraceVo.getDealReasoon())){//????????????
				flowTaskId = wfTaskHandleService.findTaskId(prpLcancelTraceVo.getClaimNo(), "CancelApp");
			}else{
				flowTaskId = wfTaskHandleService.findTaskId(prpLcancelTraceVo.getClaimNo(), "CancelAppJuPei");//??????
			}
			claimTaskService.cancleClaim(flowTaskId,userVo.getUserCode(),userVo);
		}
		
	/*
	 * 
	 * 
	?????? 0  8   9
	??????  1
	?????? 5 
	????????????
	
	
	??????------
	?????? 2
	?????? 3
	??????  4
	 */
}
