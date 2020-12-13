/**
 * 
 */
package ins.sino.claimcar.common.web.action;

import freemarker.core.ParseException;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DateUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.ScoreNode;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.genilex.ReportInfoService;
import ins.sino.claimcar.genilex.vo.common.PrpLFraudScoreVo;
import ins.sino.claimcar.recloss.service.PrpLSurveyService;
import ins.sino.claimcar.recloss.vo.PrpLSurveyVo;
import ins.sino.claimcar.regist.service.PolicyViewService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



/**
 * @author Administrator
 */
@Controller
@RequestMapping("/survey")
public class SurveyAction {

	// 服务装载
	@Autowired
	private PrpLSurveyService prpLSurveyService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private ReportInfoService reportInfoService;
	@Autowired
	private PolicyViewService policyViewService;
	/**
	 * 发起调查任务初始化
	 * @param flowId
	 * @param flowTaskId
	 * @param registNo
	 * @param nodeCode
	 * @param handlerUser
	 * @return
	 */
	@RequestMapping(value = "/initSurvey.do")
	public ModelAndView initSurvey(String flowId,BigDecimal flowTaskId,String registNo,String nodeCode,String handlerUser) {
		Map<FlowNode, String> scoreMap = new HashMap<FlowNode, String>();
		scoreMap.put(FlowNode.Check, ScoreNode.Check);
		scoreMap.put(FlowNode.DLoss, ScoreNode.DLoss);
		ModelAndView mav = new ModelAndView();
		List<PrpLSurveyVo> prpLSurveyVoList = prpLSurveyService.findSurveyByRegistNo(registNo);
		PrpLSurveyVo surveyVo = new PrpLSurveyVo();
		surveyVo.setFlowId(flowId);
		surveyVo.setRegistNo(registNo);
		surveyVo.setNodeCode(nodeCode);
		surveyVo.setCreateUser(handlerUser);
		PrpLFraudScoreVo prpLFraudScoreVo = new PrpLFraudScoreVo();
		prpLFraudScoreVo = reportInfoService.findPrpLFraudScoreVoByRegistNo(registNo, scoreMap.get(nodeCode));
		mav.addObject("prpLFraudScoreVo",prpLFraudScoreVo);
		mav.addObject("prpLSurveyVoList",prpLSurveyVoList);
		mav.addObject("flowTaskId",flowTaskId);
		mav.addObject("surveyVo",surveyVo);
		mav.setViewName("survey/SurveyEdit");
		return mav;
	}

	/**
	 * 发起调查任务
	 * @param surveyVo
	 * @param flowTaskId
	 */
	@RequestMapping(value = "/save.do")
	@ResponseBody
	public AjaxResult surveySave(@FormModel("surveyVo") PrpLSurveyVo surveyVo,BigDecimal flowTaskId) {
		SysUserVo sysUserVo = WebUserUtils.getUser();
		AjaxResult ajaxResult = new AjaxResult();
		//调查任务开关控制调整		
		String policyNoComCode = policyViewService.getPolicyComCode(surveyVo.getRegistNo());
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.SURVEY,policyNoComCode);
        if(CodeConstants.IsSingleAccident.YES.equals(configValueVo.getConfigValue())){
        	boolean isExist = wfTaskHandleService.existTaskInBySubNodeCode(surveyVo.getRegistNo(),FlowNode.Survey.toString());
    		if(isExist){
    			ajaxResult.setData("已存在未完成调查任务！");
    			ajaxResult.setStatus(HttpStatus.SC_CREATED);
    		}else {
    			surveyVo.setIsAutoTrigger("0");
    			prpLSurveyService.saveSurvey(surveyVo,sysUserVo,flowTaskId);
    			ajaxResult.setStatus(HttpStatus.SC_OK);
    		}
        }else{
        	ajaxResult.setData("发起调查任务功能已关闭！");
        	ajaxResult.setStatus(HttpStatus.SC_CREATED);
        }
		return ajaxResult;
	}

	/**
	 * 调查处理初始化
	 * @param flowTaskId
	 * @return
	 */
	@RequestMapping(value = "/init.do")
	public ModelAndView init(double flowTaskId) {
		ModelAndView mav = new ModelAndView();
		SysUserVo sysUserVo = WebUserUtils.getUser();
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
		PrpLSurveyVo surveyVo = prpLSurveyService.findSurveyVo(id);
		if(surveyVo.getFraudScoreId()!=null){
			this.organizationScore(surveyVo);
		}
		List<PrpLSurveyVo> prpLSurveyVos = prpLSurveyService.findSurveyByRegistNo(wfTaskVo.getRegistNo());
		for (PrpLSurveyVo prpLSurveyVo : prpLSurveyVos) {
			if(prpLSurveyVo.getFraudScoreId()!=null){
				this.organizationScore(prpLSurveyVo);
			}
		}
		if(sysUserVo.getComCode().startsWith("0001")){
			mav.addObject("headOffice","1");
		}
		mav.addObject("surveyVo",surveyVo);
		mav.addObject("prpLSurveyVos",prpLSurveyVos);
		mav.addObject("wfTaskVo",wfTaskVo);
		mav.addObject("status",wfTaskVo.getHandlerStatus());
		mav.addObject("flowTaskId",flowTaskId);
		mav.setViewName("survey/SurveyAudit");
		return mav;
	}

	/**
	 * 组织精励联讯评分
	 * @param prpLSurveyVo
	 */
	private void organizationScore(PrpLSurveyVo prpLSurveyVo){
		PrpLFraudScoreVo prpLFraudScoreVo = reportInfoService.findPrpLFraudScoreById(prpLSurveyVo.getFraudScoreId());
		prpLSurveyVo.setScoreNode(prpLFraudScoreVo.getScoreNode());
		prpLSurveyVo.setScoreTime(prpLFraudScoreVo.getCreateTime());
		prpLSurveyVo.setFraudScore(prpLFraudScoreVo.getFraudScore());
		prpLSurveyVo.setRuleDesc(prpLFraudScoreVo.getRuleDesc());
		prpLSurveyVo.setAuxiliaryDesc(prpLFraudScoreVo.getReasonDesc());
	}
	
	/**
	 * 调查查询接收处理
	 * @param flowTaskId
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/acceptSurvey.do")
	@ResponseBody
	public ModelAndView acceptCheckTask(double flowTaskId) {
		ModelAndView mav = new ModelAndView();
		SysUserVo sysUserVo = WebUserUtils.getUser();
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		wfTaskVo.setHandlerStatus("2");
		Long id = Long.parseLong(wfTaskVo.getHandlerIdKey());
		// 根据id查prpLSurvey
		PrpLSurveyVo surveyVo = prpLSurveyService.findSurveyVo(id);
		
		mav.addObject("surveyVo",surveyVo);
		mav.addObject("wfTaskVo",wfTaskVo);
		mav.addObject("flowTaskId",flowTaskId);
		if(surveyVo!=null){
			surveyVo.setHandlerUser(sysUserVo.getUserCode());
			surveyVo.setHandlerTime(DateUtils.now());
			surveyVo.setHandlerStatus(HandlerStatus.DOING);
			prpLSurveyService.updateSurvey(surveyVo);
		}
		// 接收任务
		wfTaskHandleService.tempSaveTask(flowTaskId,id.toString(),sysUserVo.getUserCode(),sysUserVo.getComCode());

		mav.setViewName("survey/SurveyAudit");
		return mav;
	}

	/**
	 * 调查处理保存
	 */
	@RequestMapping(value = "/surveyAudit.do")
	@ResponseBody
	public AjaxResult surveyAudit(@FormModel("prpLSurveyVo") PrpLSurveyVo surveyVo,double flowTaskId) {
		AjaxResult ajaxResult=new AjaxResult();
		SysUserVo sysUserVo = WebUserUtils.getUser();
		PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		Long id = Long.parseLong(prpLWfTaskVo.getHandlerIdKey());
		PrpLSurveyVo prpLSurveyVo = prpLSurveyService.findSurveyVo(id);
		if(prpLSurveyVo!=null){
			prpLSurveyVo.setHandlerUser(sysUserVo.getUserCode());
			prpLSurveyVo.setHandlerStatus(HandlerStatus.END);
			prpLSurveyVo.setOpinionDesc(surveyVo.getOpinionDesc());
			prpLSurveyVo.setAmout(surveyVo.getAmout());
			prpLSurveyVo.setIsFraud(surveyVo.getIsFraud());
			prpLSurveyVo.setImpairmentCase(surveyVo.getImpairmentCase());
			if("1".equals(surveyVo.getIsFraud())){
				prpLSurveyVo.setFraudType(surveyVo.getFraudType());
			}
			prpLSurveyVo.setExternalSurvey(surveyVo.getExternalSurvey());
			prpLSurveyService.updateSurvey(prpLSurveyVo);
		}
		
		WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
		taskVo.setRegistNo(prpLSurveyVo.getRegistNo());
		taskVo.setHandlerIdKey(id.toString());
		
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(prpLWfTaskVo.getFlowId());
		submitVo.setCurrentNode(FlowNode.Survey);
		submitVo.setAssignUser(prpLWfTaskVo.getHandlerUser());
		submitVo.setAssignCom(prpLWfTaskVo.getAssignCom());
		submitVo.setComCode(prpLWfTaskVo.getComCode());
		submitVo.setTaskInKey(prpLWfTaskVo.getTaskInKey());
		submitVo.setTaskInUser(sysUserVo.getUserCode());
		submitVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
		submitVo.setNextNode(FlowNode.END);
		
		wfTaskHandleService.submitSimpleTask(taskVo,submitVo);
		ajaxResult.setStatus(200);
		return ajaxResult;
	}
}
