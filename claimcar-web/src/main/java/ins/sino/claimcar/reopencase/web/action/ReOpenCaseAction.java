package ins.sino.claimcar.reopencase.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.ClaimToReinsuranceService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.endcase.service.ReOpenCaseService;
import ins.sino.claimcar.endcase.service.ReOpenQueryService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseTextVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;
import ins.sino.claimcar.platform.service.ClaimToPaltformService;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/reOpen")
public class ReOpenCaseAction {
    private static Logger logger = LoggerFactory.getLogger(ReOpenCaseAction.class);

	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	ReOpenCaseService reOpenCaseService;
	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	ClaimToPaltformService claimToPaltformService;
	@Autowired
	AssignService assignService;
	@Autowired
	ClaimToReinsuranceService claimToReinsuranceService;
	@Autowired
	private ReOpenQueryService reOpenQueryService;
	@Autowired
	private InterfaceAsyncService interfaceAsyncService;
	
	/**
	 * 重开赔案发起任务查询查询
	 */
	@RequestMapping(value = "/reOpenAppSearch.do", method = RequestMethod.POST)
	@ResponseBody
	public String reOpenAppSearch(Model model,
			@RequestParam("handleStatus") String handleStatus,
@FormModel("endCaseVo") PrpLEndCaseVo endCaseVo,// 页面组装VO
									@FormModel("wfTaskQueryVo") PrpLWfTaskQueryVo prplwftaskqueryvo,// 页面组装VO
									@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
			@RequestParam(value = "length", defaultValue = "10") Integer length) {
		String registNo = endCaseVo.getRegistNo();
		endCaseVo.setRegistNo(registNo.replaceAll("\\s*",""));// 去掉所有空格符
		if (handleStatus.equals("0")) {
			String jsonData = "";
			// 旧理赔查询
			if("1".equals(prplwftaskqueryvo.getQuerySystem())){
				ResultPage<WfTaskQueryResultVo> page = reOpenQueryService.searchOldClaim(endCaseVo, prplwftaskqueryvo, start, length);
				jsonData = ResponseUtils.toDataTableJson(page, "registNo",
						"policyNo", "mercyFlag:MercyFlag", "insuredName","serialNo",
						"riskCode:RiskCode", "endCaseTime");
			}else{
				ResultPage<WfTaskQueryResultVo> page = reOpenQueryService.search(endCaseVo, prplwftaskqueryvo, start, length);
				jsonData = ResponseUtils.toDataTableJson(page, "registNo",
						"policyNo", "mercyFlag:MercyFlag", "insuredName","serialNo",
						"riskCode:RiskCode", "endCaseTime");
			}
			return jsonData;
		} else {
			ResultPage<PrpLReCaseVo> reCase = reOpenQueryService.searchProcessed(
					endCaseVo, prplwftaskqueryvo, handleStatus, start, length);
			String jsonData = ResponseUtils.toDataTableJson(reCase, "registNo",
					"seriesNo", "flag", "mercyFlag:MercyFlag", "insuredName","remark","checkStatus:CheckStatus",
					"endCaseDate", "endCaseNo");
			
			return jsonData;
		}

	}
	
    /**
	 * 重开赔案登记处理初始化
	 * @throws Exception
	 */
	@RequestMapping("/reOpenAppInit.do")
	@ResponseBody
	public ModelAndView reOpenAppInit(String handleStatus,String num) throws Exception {
		ModelAndView mv = new ModelAndView();
		int flag=0;
		Date date=new Date();
		// 判断险种和HandlerStatus
		if(handleStatus.equals("0")){
			List<PrpLEndCaseVo> endCaseList=reOpenCaseService.findEndCaseByRegistNo(num);
			List<PrpLEndCaseVo> endCaseVoList = new ArrayList<PrpLEndCaseVo>();
			for(int i=0;i<endCaseList.size();i++){
				Boolean bl = reOpenCaseService.isFail(endCaseList.get(i).getEndCaseNo());
				Boolean exist = wfTaskHandleService.existTaskByNodeCode(endCaseList.get(i).getRegistNo(), 
						FlowNode.ReOpen, endCaseList.get(i).getEndCaseNo(), "");
				if(!exist || bl){
					endCaseVoList.add(endCaseList.get(i));
					if(endCaseList.get(i).getRiskCode().equals("1101")){// 交强险
						//mv.addObject("compelClaimNo", endCaseList.get(i).getClaimNo());
						mv.addObject("openCaseDate", date);
						mv.addObject("endCompelCaseDate", endCaseList.get(i).getEndCaseDate());
						flag=flag+1;
					}else{// 商业险
						//mv.addObject("businessClaimNo", endCaseList.get(i).getClaimNo());
						mv.addObject("openCaseDate", date);
						mv.addObject("endBusinessCaseDate", endCaseList.get(i).getEndCaseDate());
						flag=flag+2;
					}
				}
			}
			mv.addObject("registNo",num);
			mv.addObject("endCaseList", endCaseVoList);
			if(endCaseVoList.size() > 0){
				mv.addObject("claimNo", endCaseVoList.get(0).getClaimNo());
			}else{
				throw new Exception("已经发起重开赔案！");
			}
		}else{
			PrpLReCaseVo reCaseVo=reOpenCaseService.findReCaseVoByEndCaseNo(num);
			PrpLEndCaseVo endCaseVo=reOpenCaseService.findEndCaseVoByEndCaseNo(num);
			if(endCaseVo.getRiskCode().equals("1101")){
				//mv.addObject("compelClaimNo", reCaseVo.getClaimNo());
				mv.addObject("openCaseDate", reCaseVo.getOpenCaseDate());
				mv.addObject("endCompelCaseDate", reCaseVo.getEndCaseDate());
				flag=flag+1;
			}else{
				//mv.addObject("businessClaimNo", reCaseVo.getClaimNo());
				mv.addObject("openCaseDate", reCaseVo.getOpenCaseDate());
				mv.addObject("endBusinessCaseDate", reCaseVo.getEndCaseDate());
				flag=flag+2;
			}
			List<PrpLReCaseTextVo> reCaseTextVoList = reOpenCaseService.findReCaseTextByReCaseId(reCaseVo.getId());
			List<PrpLEndCaseVo> endCaseList=new ArrayList<PrpLEndCaseVo>();
			endCaseList.add(endCaseVo);
			mv.addObject("endCaseList", endCaseList);
			mv.addObject("claimNo", reCaseVo.getClaimNo());
			mv.addObject("reCaseTexts", reCaseTextVoList);
			mv.addObject("openReasonDetail", reCaseVo.getOpenReasonDetail());
			mv.addObject("endCaseNo", num);
			mv.addObject("registNo",reCaseVo.getRegistNo());
		}
		mv.addObject("openCaseUserName", WebUserUtils.getUserName());
		mv.addObject("handleStatus", handleStatus);
		mv.addObject("flag", flag);
		mv.setViewName("reOpenCase/ReOpenAppEdit");
		return mv;
	}
	
	@RequestMapping("/reOpenApp.do")
	@ResponseBody
	public ModelAndView reOpenApp(Double flowTaskId) {
		String handleStatus="3";
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		String num=wfTaskVo.getHandlerIdKey();
		ModelAndView mv = new ModelAndView();
		int flag=0;
		Date date=new Date();
		
		// 判断险种和HandlerStatus
		if(handleStatus.equals("0")){
			List<PrpLEndCaseVo> endCaseList=reOpenCaseService.findEndCaseByRegistNo(num);
			for(int i=0;i<endCaseList.size();i++){
				if(endCaseList.get(i).getRiskCode().equals("1101")){// 交强险
					//mv.addObject("compelClaimNo", endCaseList.get(i).getClaimNo());
					mv.addObject("openCaseDate", date);
					mv.addObject("endCompelCaseDate", endCaseList.get(i).getEndCaseDate());
					flag=flag+1;
				}else{// 商业险
					//mv.addObject("businessClaimNo", endCaseList.get(i).getClaimNo());
					mv.addObject("openCaseDate", date);
					mv.addObject("endBusinessCaseDate", endCaseList.get(i).getEndCaseDate());
					flag=flag+2;
				}
			}
			mv.addObject("registNo",num);
			mv.addObject("endCaseList", endCaseList);
		}else{
			Integer reOpenCount=wfTaskHandleService.findTaskReOpenCount(wfTaskVo);
			PrpLReCaseVo reCaseVo=reOpenCaseService.findReCaseVoByEndCaseNoA(num,reOpenCount);
			PrpLEndCaseVo endCaseVo=reOpenCaseService.findEndCaseVoByEndCaseNo(num);
			if(endCaseVo.getRiskCode().equals("1101")){
				//mv.addObject("compelClaimNo", reCaseVo.getClaimNo());
				mv.addObject("openCaseDate", reCaseVo.getOpenCaseDate());
				mv.addObject("endCompelCaseDate", reCaseVo.getEndCaseDate());
				flag=flag+1;
			}else{
				//mv.addObject("businessClaimNo", reCaseVo.getClaimNo());
				mv.addObject("openCaseDate", reCaseVo.getOpenCaseDate());
				mv.addObject("endBusinessCaseDate", reCaseVo.getEndCaseDate());
				flag=flag+2;
			}
			List<PrpLReCaseTextVo> reCaseTextVoList = reOpenCaseService.findReCaseTextByReCaseId(reCaseVo.getId());
			List<PrpLEndCaseVo> endCaseList=new ArrayList<PrpLEndCaseVo>();
			endCaseList.add(endCaseVo);
			mv.addObject("endCaseList", endCaseList);
			mv.addObject("claimNo", reCaseVo.getClaimNo());
			mv.addObject("reCaseTexts", reCaseTextVoList);
			mv.addObject("openReasonDetail", reCaseVo.getOpenReasonDetail());
			mv.addObject("endCaseNo", num);
			mv.addObject("registNo", reCaseVo.getRegistNo());
		}
		mv.addObject("openCaseUserName", WebUserUtils.getUserName());
		mv.addObject("handleStatus", handleStatus);
		mv.addObject("flag", flag);
		mv.setViewName("reOpenCase/ReOpenAppEdit");
		return mv;
	}
	
    /**
	 * 审核界面初始化
	 * @param flowTaskId
	 * @return
	 */
	@RequestMapping("/reOpenCheckEdit.do")
	@ResponseBody
	public ModelAndView reOpenCheckEdit(Double flowTaskId) {
		
		ModelAndView mv = new ModelAndView();
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		Integer reOpenCount=wfTaskHandleService.findTaskReOpenCount(wfTaskVo);
		PrpLReCaseVo reCaseVo = reOpenCaseService.findReCaseVoByEndCaseNoA(wfTaskVo.getTaskInKey(),reOpenCount);
		PrpLEndCaseVo endCaseVo = reOpenCaseService.findEndCaseVoByEndCaseNo(wfTaskVo.getTaskInKey());
		List<PrpLReCaseTextVo> reCaseTextVoList = reOpenCaseService.findReCaseTextByReCaseId(reCaseVo.getId());
		reCaseVo.setFlag(endCaseVo.getRiskCode());
		mv.addObject("handlerStatus", wfTaskVo.getHandlerStatus());
		mv.addObject("currentNode", wfTaskVo.getSubNodeCode());
		mv.addObject("flowId", wfTaskVo.getFlowId());
		mv.addObject("status", reCaseVo.getCheckStatus());
		mv.addObject("prpLReCase",reCaseVo);
		mv.addObject("reCaseTexts",reCaseTextVoList);
		mv.addObject("flowTaskId", flowTaskId);
		mv.setViewName("reOpenCase/ReOpenCheckEdit");
		return mv;
	}
	
	/**
	 * 重开交强险需要商业险已核赔通过，重开商业险同样
	 * @param regsitNo
	 * @return
	 */
	@RequestMapping("/reOpenValid.do")
	@ResponseBody
	public AjaxResult reOpenValid(String registNo){
		AjaxResult ajaxResult = new AjaxResult();
		String result = reOpenCaseService.reOpenValid(registNo);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(result);
		return ajaxResult;
	}
	
	
	/**
	 * 重开赔案任务发起
	 * @return
	 */
	@RequestMapping("/appSubmit.do")
	@ResponseBody
	public AjaxResult appSubmit(String registNo,String[] claimNoArr,String handleStatus,
			@FormModel(value="prpLReCaseVo")PrpLReCaseVo prpLReCaseVo){
		SysUserVo userVo = WebUserUtils.getUser();
		AjaxResult ajaxResult = new AjaxResult();
		try{
			// 旧理赔
			int length = registNo.length();
			if(length == 21){
				// 生成旧理赔流程节点
				wfTaskHandleService.generateFlow(registNo);
				wfTaskHandleService.generateFlowLevel(registNo);
			}
			reOpenCaseService.appSubmit(registNo, claimNoArr, handleStatus, prpLReCaseVo, userVo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}
	
	/**
	 * 选择重开赔案处理人
	 * @return
	 */
	@RequestMapping("/initReOpenSubmit.ajax")
	public ModelAndView initReOpenSubmit(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("reOpenCase/ReOpen_Submit");
		return modelAndView;
	}
	
	// 暂存
	@RequestMapping("/saveOrUpdata.do")
	@ResponseBody
	public AjaxResult saveOrUpdata(Double flowTaskId,
			@FormModel(value="prpLReCaseVo")PrpLReCaseVo prpLReCaseVo){
		AjaxResult ajaxResult = new AjaxResult();
		try{
			SysUserVo userVo = WebUserUtils.getUser();
			Long reOpenId = reOpenCaseService.save(prpLReCaseVo,flowTaskId,userVo);
			String returnStr=reOpenId+","+flowTaskId;
			ajaxResult.setData(returnStr);
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		}catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
			logger.error(prpLReCaseVo.getRegistNo()+"重开赔案暂存失败：",e);
		}
		return ajaxResult;
		
	}
	
	@RequestMapping("/submitNextPage.do")
	@ResponseBody
	public ModelAndView submitNextPage(Double flowTaskId,String saveType,
			Long reOpenId,String currentNode ){
		ModelAndView mv = new ModelAndView();	
		PrpLReCaseVo reCaseVo = reOpenCaseService.findReCaseByPk(reOpenId);
		Map<String,String> nextNodeMap = new TreeMap<String,String>();
		String nextNode = currentNode;
		if("superior".equals(saveType)){
			StringBuffer nodeLevel = new StringBuffer(currentNode);
			nodeLevel.delete(0, 12);
			int level = Integer.valueOf(nodeLevel.toString())+1;
			nextNode = "ReOpenVrf_LV"+level;
		}
		nextNodeMap.put(nextNode,FlowNode.valueOf(nextNode).getName());
		
		mv.addObject("registNo", reCaseVo.getRegistNo());
		mv.addObject("nextNodeMap", nextNodeMap);
		mv.addObject("saveType", saveType);
		mv.addObject("reOpenId", reOpenId);
		mv.addObject("flowTaskId", flowTaskId);
		mv.addObject("nextNode", nextNode);
		mv.addObject("currentName", FlowNode.valueOf(currentNode).getName());
		mv.setViewName("reOpenCase/ReOpenVrf_Audit");
		
		return mv;
	}
	
	@RequestMapping("/submitNextNode.do")
	@ResponseBody
	public ModelAndView submitNextNode(String saveType,Long reOpenId,Double flowTaskId,String nextNode){
		ModelAndView mv = new ModelAndView();
		Date date = new Date();
		PrpLReCaseVo reCaseVo = reOpenCaseService.findReCaseByPk(reOpenId);
		String registNo = reCaseVo.getRegistNo();
		String policyNo = reCaseVo.getRemark();
		reCaseVo.setCheckCaseUserCode(WebUserUtils.getUserCode());
		reCaseVo.setCheckCaseUserName(WebUserUtils.getUserName());
		reCaseVo.setDealCaseDate(date);
		reCaseVo.setUpdateTime(date);
		reCaseVo.setUpdateUser(WebUserUtils.getUserCode());
		SysUserVo userVo = WebUserUtils.getUser();
		if(saveType.equals("superior")){
			reOpenCaseService.superior(reCaseVo,flowTaskId,nextNode,userVo);
		}else{
			reOpenCaseService.submit(flowTaskId,saveType,reCaseVo,userVo);
			
			// 重开赔案审核通过送平台
			interfaceAsyncService.reOpenAppToPaltform(reCaseVo.getEndCaseNo());
			
			// 再保处理重开赔案业务 niuqiang businessType=3
			try{
				String claimNo = reCaseVo.getClaimNo();
				PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
				ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // 填写日志表
				claimInterfaceLogVo.setClaimNo(claimNo);
				claimInterfaceLogVo.setRegistNo(reCaseVo.getRegistNo());
				claimInterfaceLogVo.setCompensateNo(reCaseVo.getCompensateNo());
				claimInterfaceLogVo.setComCode(userVo.getComCode());
				claimInterfaceLogVo.setCreateUser(WebUserUtils.getUserCode());
				claimInterfaceLogVo.setCreateTime(new Date());
				claimInterfaceLogVo.setOperateNode(FlowNode.ReOpen.getName());
				interfaceAsyncService.TransDataForReinsCaseVo("3", claimVo,claimInterfaceLogVo);
			}catch(Exception e){
				logger.error(reCaseVo.getClaimNo()+"重开赔案送再保报错："+e);
			}
			
			PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(registNo, policyNo);	
			if(SendPlatformUtil.isMor(prpLCMainVo)){
				// 山东预警
				String warnswitch = SpringProperties.getProperty("WARN_SWITCH");// 62,10,50
				if(warnswitch.contains(prpLCMainVo.getComCode().substring(0,2))){// prpLCMainVo.getComCode().startsWith("62")
					try {
						interfaceAsyncService.SendReopenCaseRegister(reCaseVo.getEndCaseNo(), policyNo, userVo);
					} catch (Exception e) {
						logger.error(reCaseVo.getClaimNo()+"预警重开赔案异常信息-------------->"+e);
					}
				}
			}		
		}
		String taskName = reOpenCaseService.getTaskName(saveType, nextNode);
		mv.addObject("taskName", taskName);
		mv.addObject("registNo", reCaseVo.getRegistNo());
		mv.addObject("userName", WebUserUtils.getUserName());
		mv.setViewName("reOpenCase/NextTaskVeiw");
		return mv;
	}
	
//	@RequestMapping("/reOpenVrfSubmit.do")
//	@ResponseBody
//	public AjaxResult reOpenVrfSubmit(Double flowTaskId,String auditStatus,
//			@FormModel(value="prpLReCaseVo")PrpLReCaseVo prpLReCaseVo){
//		Date date = new Date();
//		prpLReCaseVo.setCheckCaseUserCode(SecurityUtils.getUserCode());
//		prpLReCaseVo.setCheckCaseUserName(SecurityUtils.getUserName());
//		prpLReCaseVo.setDealCaseDate(date);
//		prpLReCaseVo.setUpdateTime(date);
//		prpLReCaseVo.setUpdateUser(SecurityUtils.getUserName());
//		
//		AjaxResult ajaxResult = new AjaxResult();
//		try{
//			if(auditStatus.equals("superior")){
////				reOpenCaseService.superior(prpLReCaseVo,flowTaskId);
//			}else if(auditStatus.equals("save")){
//				reOpenCaseService.save(prpLReCaseVo,flowTaskId);
//				ajaxResult.setData("save");
//			}else if(auditStatus.equals("pass")||auditStatus.equals("failed")){
//				reOpenCaseService.submit(flowTaskId,auditStatus,prpLReCaseVo);
//				
	// //重开赔案审核通过送平台
//				String endCaseNo = prpLReCaseVo.getEndCaseNo();
//				String type = Risk.DQZ.equals(prpLReCaseVo.getFlag())?"11":"12";
//				String comCode = policyViewService.findPolicyComCode(prpLReCaseVo.getRegistNo(),type);
	// if("22".equals(comCode.substring(0,2))){//上海平台
//					sendEndCaseAddToSH.sendEndCaseAddToSH(endCaseNo);
	// }else{//全国平台
//					if(Risk.DQZ.equals(prpLReCaseVo.getFlag())){
//						sendReOpenApp.sendCIReOpenAppToPlatform(endCaseNo);
//					}else{
//						sendReOpenApp.sendBIReOpenAppToPlatform(endCaseNo);
//					}
//				}
//				
//				
//			}else if(auditStatus.equals("return")){
//				reOpenCaseService.returnAndModify(flowTaskId,prpLReCaseVo);
//			}
//			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
//		}catch(Exception e){
//			ajaxResult.setStatusText(e.getMessage());
//			e.printStackTrace();
//		}
//		return ajaxResult;
//	}
    
	@RequestMapping("/reOpenCaseView.do")
	@ResponseBody
	public ModelAndView reOpenCaseView(String claimNo) {
		ModelAndView mv = new ModelAndView();
		List<PrpLReCaseVo> reCaseVoList=new ArrayList<PrpLReCaseVo>();
		reCaseVoList=reOpenCaseService.findReCaseByClaimNo(claimNo);
		mv.addObject("reCaseList", reCaseVoList);
		mv.setViewName("reOpenCase/ReOpenCaseView");
		return mv;
	}
	
	@RequestMapping("/findReOpenCase.do")
	@ResponseBody
	public ModelAndView findReOpenCase() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("reOpenCase/findReOpenCase");
		return mv;
	}
	
}
