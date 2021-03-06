package ins.sino.claimcar.common.web.action;


import ins.framework.utils.Beans;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.newpayment.service.ClaimToNewPaymentService;
import ins.sino.claimcar.payment.service.ClaimToPaymentService;
import ins.sino.claimcar.recloss.service.RecLossService;
import ins.sino.claimcar.recloss.vo.PrpLRecLossDetailVo;
import ins.sino.claimcar.recloss.vo.PrpLRecLossVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/recLoss")
public class RecLossAction {

	@Autowired
	private RecLossService recLossService;
	
	@Autowired
	private ClaimTextService claimTextService;
	
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	
	@Autowired
	private BillNoService billNoService;
	
	@Autowired
	private RegistQueryService registQueryService;
	
	@Autowired
	private PolicyViewService policyViewService;
	
	@Autowired
	private ClaimTaskService claimTaskService;
	
	@Autowired
	private LossCarService lossCarService;
	
	@Autowired
	private PropTaskService propTaskService;
	
	@Autowired
	private ClaimToPaymentService claimToPaymentService;
	
	@Autowired
	private ClaimToNewPaymentService claimToNewPaymentService;
	
	
	/**
	 * ?????????????????????
	 * @return
	 */
	@RequestMapping(value = "/initQuery.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initQuery() {
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		ModelAndView mav = new ModelAndView();
		mav.addObject("taskInTimeStart", startDate);
		mav.addObject("taskInTimeEnd", endDate);

		mav.setViewName("recLoss/recLossTaskQueryList");
		return mav;
	}
	
	/**
	 * ???????????????????????????
	 * @param taskIds
	 * @return
	 */
	@RequestMapping(value = "/initFirst.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initFirst(@RequestParam(value = "tasks")List<BigDecimal> taskIds) {
		ModelAndView mav = new ModelAndView();
		List<PrpLWfTaskVo> prpLWfTaskVoList = recLossService.queryTaskList(taskIds);
		String recLossMain = null;
		for(PrpLWfTaskVo prpLWfTaskVo:prpLWfTaskVoList){
			String showInfoXML = prpLWfTaskVo.getShowInfoXML();
			if (StringUtils.isNotBlank(showInfoXML)) {
				Map<String, String> showInfoMap = TaskExtMapUtils
						.jsonToMap(showInfoXML);
				prpLWfTaskVo.setLossItemName(showInfoMap.get("serialNo")+":"+showInfoMap.get("license"));//?????????????????????
			}
			String recLossId = prpLWfTaskVo.getHandlerIdKey();
			PrpLRecLossVo  prpLRecLossVo = recLossService.findRecLossByRecLossId(recLossId);
			boolean recLossInd =  true;
			List<PrpLRecLossDetailVo> prpLRecLossDetailVoList = prpLRecLossVo.getPrpLRecLossDetails();
			if(prpLRecLossDetailVoList != null && !prpLRecLossDetailVoList.isEmpty()){
				for(PrpLRecLossDetailVo prpLRecLossDetailVo:prpLRecLossDetailVoList){
					if("0".equals(prpLRecLossDetailVo.getRecLossInd())){
						recLossInd = false;
					}
				}
			}
			recLossMain = prpLRecLossVo.getRecLossMainId();
			prpLRecLossVo.setRecLossInd(recLossInd?"1":"0");	
			prpLWfTaskVo.setPrpLRecLossVo(prpLRecLossVo);
		}
		if(StringUtils.isNotBlank(recLossMain)){
			//???????????????????????????
			Map<String,String> map = new HashMap<String,String>();
			map.put("bussNo",recLossMain);
			map.put("registNo",recLossMain);
			map.put("bigNode",FlowNode.RecLoss.toString());
			List<PrpLClaimTextVo> claimTextList = claimTextService.findClaimTextList(map);
			mav.addObject("claimTextVos",claimTextList);
			if(claimTextList != null && !claimTextList.isEmpty()){
				mav.addObject("claimTextVo",claimTextList.get(0));
			}
		}
		String operatorCode = prpLWfTaskVoList.get(0).getPrpLRecLossVo().getOperatorCode();
		if(StringUtils.isBlank(operatorCode)){
			operatorCode = WebUserUtils.getUserCode();
		}
		mav.addObject("comCode",WebUserUtils.getComCode());
		mav.addObject("userCode",operatorCode);
		mav.addObject("prpLWfTaskVoList",prpLWfTaskVoList);
		mav.setViewName("recLoss/recLossEdit");
		return mav;
	}
	
	/**
	 * ?????????????????????????????????????????????
	 * @param model
	 * @param flowTaskId
	 * @return
	 */
	@RequestMapping(value = "/init.do")
	public ModelAndView init(Model model,@RequestParam(value = "flowTaskId")String flowTaskId) {
		List<BigDecimal> tasks = new ArrayList<BigDecimal>();
		PrpLWfTaskVo prpLWfTaskVo  = wfTaskHandleService.queryTask(new Double(flowTaskId));
		String recLossId = prpLWfTaskVo.getHandlerIdKey();
		String registNo = prpLWfTaskVo.getRegistNo();
		//??????????????????
		PrpLRecLossVo  prpLRecLossVo  = recLossService.findRecLossByRecLossId(recLossId);
		if(StringUtils.isNotBlank(prpLRecLossVo.getRecLossMainId())){
			List<PrpLRecLossVo> prpLRecLossVoList = recLossService.findPrpLRecLossListByMainId(prpLRecLossVo.getRecLossMainId());
			List<String> handlerIdKeys = new ArrayList<String>();
			for(PrpLRecLossVo recLossVo:prpLRecLossVoList){
				String handlerIdKey = recLossVo.getPrpLRecLossId();
				handlerIdKeys.add(handlerIdKey);
			}
			List<PrpLWfTaskVo> prpLWfTaskVoList = recLossService.queryTaskList(handlerIdKeys,FlowNode.RecLoss.name(),registNo);
			for(PrpLWfTaskVo taskVo:prpLWfTaskVoList){
				tasks.add(taskVo.getTaskId());
			}
		}else{
			tasks.add(new BigDecimal(flowTaskId));
		}
		return initFirst(tasks);
	}
	
	/**
	 * ???????????????????????????
	 * @param model
	 * @param flowTaskId
	 * @return
	 */
	@RequestMapping(value = "/recLossInit.do")
	public ModelAndView recLossInit(Model model,@RequestParam(value = "flowTaskId")String flowTaskId) {
		ModelAndView mav = new ModelAndView();
		boolean tci = false;//??????
		boolean vci = false;//???????????????
		
		PrpLWfTaskVo prpLWfTaskVo  = wfTaskHandleService.queryTask(new Double(flowTaskId));
		String registNo = prpLWfTaskVo.getRegistNo();
		String recLossId = prpLWfTaskVo.getHandlerIdKey();
		//????????????
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		
//		List<PrpLCMainVo> prpLCMainVoList = policyViewService.getPolicyAllInfo(registNo);
//		for(PrpLCMainVo prpLCMainVo:prpLCMainVoList){
//			if("1101".equals(prpLCMainVo.getRiskCode())){//?????????
//				tci =true;
//			}else{
//				vci = true;
//			}
//		}
		
		//???????????? 
		List<PrpLClaimVo> prpLClaimList = claimTaskService.findClaimListByRegistNo(registNo);
		//??????????????????
		PrpLRecLossVo  prpLRecLossVo  = recLossService.findRecLossByRecLossId(recLossId);
		
		String recLossType = prpLRecLossVo.getRecLossType();//1?????????2??????
		
		//????????????
		PrpLDlossCarMainVo prpLDlossCarMainVo = null;
		PrpLdlossPropMainVo prpLdlossPropMainVo = null;

		if("1".equals(recLossType)){
			prpLDlossCarMainVo = lossCarService.findLossCarMainById(prpLRecLossVo.getLossMainId());
		}else if("2".equals(recLossType)){
			prpLdlossPropMainVo = propTaskService.findPropMainVoById(prpLRecLossVo.getLossMainId());
		}
		
		if(prpLDlossCarMainVo != null){
			model.addAttribute("prpLDlossCarMainVo",prpLDlossCarMainVo);
			if("1".equals(prpLDlossCarMainVo.getDeflossCarType())){
				tci =true;
			}else{
				vci = true;
			}
		}
		if(prpLdlossPropMainVo != null){
			model.addAttribute("prpLdlossPropMainVo",prpLdlossPropMainVo);
			if("1".equals(prpLdlossPropMainVo.getLossType())){
				tci =true;
			}else{
				vci = true;
			}
		}
		model.addAttribute("prpLWfTaskVo",prpLWfTaskVo);
		model.addAttribute("recLossType",recLossType);
		model.addAttribute("prpLRecLossVo",prpLRecLossVo);
		model.addAttribute("prpLRegistVo",prpLRegistVo);
		model.addAttribute("prpLClaimList",prpLClaimList);
		model.addAttribute("tci",tci);
		model.addAttribute("vci",vci);
		model.addAttribute("userCode",WebUserUtils.getUserCode());
		mav.setViewName("recLoss/recLossDetail");
		return mav;
	}
	
	/**
	 * ??????????????????????????????
	 * @param prpLRecLossVo
	 * @param claimTextVo
	 * @param prpLRecLossDetails
	 * @param wfTaskSubmitVo
	 * @param saveType
	 * @return
	 */
	@RequestMapping(value = "/save.do")
	@ResponseBody
	public AjaxResult saveRecLoss(@FormModel("prpLRecLossVo") PrpLRecLossVo prpLRecLossVo,
	 	                              @FormModel("prpLRecLossDetails") List<PrpLRecLossDetailVo> prpLRecLossDetails) {
		Date nowDate = new Date();
		//??????????????????(??????+?????????(10???))
		for(PrpLRecLossDetailVo prpLRecLossDetailVo:prpLRecLossDetails){
			if(StringUtils.isBlank(prpLRecLossDetailVo.getRecLossInd())){//??????????????????1???????????????0
				prpLRecLossDetailVo.setRecLossInd("0");
			}
			if(StringUtils.isBlank(prpLRecLossDetailVo.getRecLossNo())){
				prpLRecLossDetailVo.setRecLossNo(billNoService.getRecLossNo());
			}
		}
		prpLRecLossVo.setUpdateTime(nowDate);
		prpLRecLossVo.setUpdateUser(WebUserUtils.getUserCode());
		prpLRecLossVo.setPrpLRecLossDetails(prpLRecLossDetails);
		recLossService.saveOrUpdate(prpLRecLossVo);
		String returnStr ="";
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(returnStr);
		
		return ajaxResult;
	}
	
	/**
	 * ???????????????????????????????????????
	 * @param prpLRecLossVo
	 * @param claimTextVo
	 * @param prpLRecLossDetails
	 * @param wfTaskSubmitVo
	 * @param saveType
	 * @return
	 */
	@RequestMapping(value = "/saveAll.do")
	@ResponseBody
	public AjaxResult saveAllRecLoss(@FormModel("prpLRecLossVo") PrpLRecLossVo prpLRecLossVo,
	 	                              @FormModel("claimTextVo") PrpLClaimTextVo claimTextVo,
	 	                              @FormModel("wfTaskSubmitVo") List<WfTaskSubmitVo> wfTaskSubmitVos,
	 	                              String saveType) {
		String comCode = wfTaskSubmitVos.get(0).getComCode();
		String recLossMainId = prpLRecLossVo.getRecLossMainId();
		if(StringUtils.isBlank(recLossMainId)){//??????????????????
			recLossMainId = billNoService.getRecLossMainId(comCode);
		}
		Date nowDate = new Date();
		//??????
		claimTextVo.setRegistNo(recLossMainId);
		claimTextVo.setBussTaskId(new Long(recLossMainId.substring(6)));
		claimTextVo.setBussNo(recLossMainId);
		claimTextVo.setTextType(CodeConstants.ClaimText.OPINION);
		claimTextVo.setNodeCode(FlowNode.RecLoss.name());
		claimTextVo.setOperatorCode(WebUserUtils.getUserCode());
		claimTextVo.setOperatorName(WebUserUtils.getUserName());
		claimTextVo.setComCode(WebUserUtils.getComCode());
//		claimTextVo.setComName(WebUserUtils.getComName());
		claimTextVo.setInputTime(nowDate);
		claimTextVo.setBigNode(FlowNode.RecLoss.name());
		claimTextVo.setCreateUser(WebUserUtils.getUserCode());
		claimTextVo.setCreateTime(nowDate);
		claimTextVo.setUpdateUser(WebUserUtils.getUserCode());
		claimTextVo.setUpdateTime(nowDate);
		if(saveType.equals("1")){//??????
			claimTextVo.setStatus(CodeConstants.AuditStatus.SAVE);
			claimTextVo.setFlag("0");
		}else{//??????
			claimTextVo.setStatus(CodeConstants.AuditStatus.SUBMITRECLOSS);
			claimTextVo.setFlag("1");
		}
		claimTextService.saveOrUpdte(claimTextVo);
		String lossItemName = "";
		for(WfTaskSubmitVo wfTaskSubmitVo:wfTaskSubmitVos){
			PrpLRecLossVo recLossVo = recLossService.findRecLossByRecLossId(wfTaskSubmitVo.getHandleIdKey());
			if("1".equals(recLossVo.getRecLossType())){// ??????
				PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(recLossVo.getLossMainId());
				//???????????????
				if(!"1".equals(prpLDlossCarMainVo.getUnderWriteFlag())){
					lossItemName = prpLDlossCarMainVo.getLicenseNo();
					break;
				}
			}else{//??????
				PrpLdlossPropMainVo prpLdlossPropMainVo = propTaskService.findPropMainVoById(recLossVo.getLossMainId());
				//???????????????
				if(!"1".equals(prpLdlossPropMainVo.getUnderWriteFlag())){
					lossItemName = prpLdlossPropMainVo.getLicense();
					break;
				}
			}
			Beans.copy().from(prpLRecLossVo).excludeNull().excludeEmpty().to(recLossVo);
			recLossVo.setRecLossMainId(recLossMainId);
			if(saveType.equals("1")){//??????
				recLossVo.setUnderwriteFlag("0");
			}else{//??????
				recLossVo.setUnderwriteFlag("1");
			}
			recLossService.saveOrUpdate(recLossVo);
			this.saveWorkFlow(wfTaskSubmitVo,recLossVo,saveType); 
		}
		String returnStr ="";
		if(StringUtils.isNotBlank(lossItemName)){
			returnStr = lossItemName;
		}
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(returnStr);
		
		return ajaxResult;
	}
	
	
	/**
	 * ?????????????????????
	 * @param wfTaskSubmitVo
	 * @param prpLRecLossVo
	 * @param saveType
	 */
	private void saveWorkFlow(WfTaskSubmitVo wfTaskSubmitVo,PrpLRecLossVo prpLRecLossVo,String saveType){
		if("2".equals(saveType)){//??????
			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
			taskVo.setRegistNo(prpLRecLossVo.getRegistNo());
			taskVo.setHandlerIdKey(prpLRecLossVo.getPrpLRecLossId().toString());
			//taskVo.setItemName(itemName);

			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			submitVo.setFlowId(wfTaskSubmitVo.getFlowId());
			submitVo.setFlowTaskId(wfTaskSubmitVo.getFlowTaskId());
			submitVo.setComCode(WebUserUtils.getComCode());
			submitVo.setTaskInUser(WebUserUtils.getUserCode());
			submitVo.setTaskInKey(prpLRecLossVo.getRegistNo());

			submitVo.setCurrentNode(wfTaskSubmitVo.getCurrentNode());
			submitVo.setNextNode(FlowNode.END);

			wfTaskHandleService.submitSimpleTask(taskVo,submitVo);
			
			try {
				// claimToPaymentService.recLossToPayment(prpLRecLossVo);//?????????????????????
				claimToNewPaymentService.recLossToNewPayment(prpLRecLossVo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if("1".equals(saveType)){//??????
			wfTaskHandleService.tempSaveTask(wfTaskSubmitVo.getFlowTaskId().doubleValue(),prpLRecLossVo.getPrpLRecLossId(),WebUserUtils.getUserCode(),WebUserUtils.getComCode());
		}
		
	}
	
}
