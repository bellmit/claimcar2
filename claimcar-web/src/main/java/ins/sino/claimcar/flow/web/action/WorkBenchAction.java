/******************************************************************************
 * CREATETIME : 2016年1月10日 下午10:35:07
 ******************************************************************************/
package ins.sino.claimcar.flow.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.support.ObjectToMapCallback;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLIndiQuotaInfoVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.util.TaskQueryUtils;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;
import ins.sino.claimcar.other.service.AccountQueryService;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;
import ins.sino.claimcar.utils.SaaPowerUtils;
import ins.sino.claimcar.vat.service.BilllclaimService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

/**
 * 工作台
 * 
 * @author ★LiuPing
 * @CreateTime 2016年1月10日
 */
@Controller
@RequestMapping("/workbench")
public class WorkBenchAction {

	private static Logger logger = LoggerFactory.getLogger(WfTaskQueryAction.class);

	@Autowired
	private WfTaskQueryService wfTaskQueryService;
	@Autowired
	private AccountQueryService accountQueryService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private BilllclaimService billlclaimService;

	//
	@RequestMapping(value = "/showTaskList.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initWorkbench(PrpLWfTaskQueryVo taskQueryVo,SysUserVo loginUser,String intoPage) throws Exception {

		ModelAndView mav = new ModelAndView();
		Boolean hasPerLossTask = SaaPowerUtils.hasTask(loginUser.getUserCode(),"claim.certainpersonloss","claim.auditing");
		Boolean hasAccBackTask = SaaPowerUtils.hasTask(loginUser.getUserCode(),"claim.compensate");
		int taskNum=0;
		try{
			taskNum=billlclaimService.findRegisterTask(loginUser.getComCode());
		}catch(Exception e){
			logger.error("发票查询任务数错误：", e);
		}
		if(hasPerLossTask && (intoPage == null || "FlowWorkbenchList".equals(intoPage))){
			if(hasPerLossTask){
				
				taskQueryVo.setNodeCode("PLoss");
				taskQueryVo.setSubNodeCode("PLFirst");
				taskQueryVo.setHandleStatus("0,1,2");
				ResultPage<WfTaskQueryResultVo> resultPage = wfTaskQueryService.findPLossTaskForPage(taskQueryVo);
				
				taskQueryVo.setSubNodeCode("PLNext");
				taskQueryVo.setHandleStatus("0,1,2,5,6");
				ResultPage<WfTaskQueryResultVo> resultPage1 = wfTaskQueryService.findPLossTaskForPage(taskQueryVo);
				
				taskQueryVo.setSubNodeCode("PLVerify");
				taskQueryVo.setHandleStatus("0,1,2,5");
				ResultPage<WfTaskQueryResultVo> resultPage2 = wfTaskQueryService.findPLossTaskForPage(taskQueryVo);
				
				taskQueryVo.setSubNodeCode("PLCharge");
				taskQueryVo.setHandleStatus("0,1,2,5");
				ResultPage<WfTaskQueryResultVo> resultPage3 = wfTaskQueryService.findPLossTaskForPage(taskQueryVo);
				
				taskQueryVo.setSubNodeCode("PLBig");
				taskQueryVo.setHandleStatus("0,1,2");
				ResultPage<WfTaskQueryResultVo> resultPage4 = wfTaskQueryService.findPLossTaskForPage(taskQueryVo);
				
				
				mav.addObject("PLFirstNum",resultPage.getTotalCount());
				if(resultPage!=null && resultPage.getData().size()>0){
					taskQueryVo.setSubNodeCode("PLFirst");
					taskQueryVo.setHandleStatus("0,1,2");
					mav.addObject("timeOutNum", wfTaskQueryService.countLossTimeOut(taskQueryVo));
				}else{
					mav.addObject("timeOutNum", 0);
				}
				mav.addObject("PLNextNum",resultPage1.getTotalCount());
				mav.addObject("PLVerifyNum",resultPage2.getTotalCount());
				mav.addObject("PLChargeNum",resultPage3.getTotalCount());
				mav.addObject("PLBigNum",resultPage4.getTotalCount());
				mav.addObject("taskNum",taskNum);
			}
			//退票查询
			if(hasAccBackTask){
				//ComCode用saa_permitcompany.comcode
				List<SysCodeDictVo> sysCodeList=wfTaskQueryService.findAllPermitcompanyByUserCode(loginUser.getUserCode());
				/*if(sysCodeList != null && sysCodeList.size() >0){
					taskQueryVo.setComCode(sysCodeList.get(0).getCodeCode());
				}else{
					taskQueryVo.setComCode(loginUser.getComCode());
				}*/
				
				String  comCode=wfTaskQueryService.getAllPermitcompanyByUserCode(sysCodeList,loginUser.getComCode());
				taskQueryVo.setComCode(comCode);
				
				taskQueryVo.setUserCode(loginUser.getUserCode());
				ResultPage<PrpLPayBankVo> resultPage5 = accountQueryService.search(taskQueryVo,0,10);
				
				mav.addObject("AccBackNum",resultPage5.getTotalCount());
			}
			mav.addObject("hasPerLossTask",hasPerLossTask);
			mav.addObject("hasAccBackTask",hasAccBackTask);
			mav.setViewName("flowQuery/persbench/FlowWorkbenchList");

		}else{
			taskQueryVo.setStart(0);
			taskQueryVo.setLength(10);
			taskQueryVo.setComCode(loginUser.getComCode());
			taskQueryVo.setUserCode(loginUser.getUserCode());
			taskQueryVo.setHandleStatus("0");
			ResultPage<WfTaskQueryResultVo> resultPage = wfTaskQueryService.findTaskForWorkBench(taskQueryVo);
			taskQueryVo.setHandleStatus("1");
			ResultPage<WfTaskQueryResultVo> resultPage2 = wfTaskQueryService.findTaskForWorkBench(taskQueryVo);
			taskQueryVo.setHandleStatus("2");
			ResultPage<WfTaskQueryResultVo> resultPage3 = wfTaskQueryService.findTaskForWorkBench(taskQueryVo);
			taskQueryVo.setHandleStatus("3");
			ResultPage<WfTaskQueryResultVo> resultPage4 = wfTaskQueryService.findTaskForWorkBench(taskQueryVo);
			//退票查询
			if(hasAccBackTask){
				taskQueryVo.setUserCode(loginUser.getUserCode());
				List<SysCodeDictVo> sysCodeList=wfTaskQueryService.findAllPermitcompanyByUserCode(loginUser.getUserCode());
				if(sysCodeList != null && sysCodeList.size() >0){
					taskQueryVo.setComCode(sysCodeList.get(0).getCodeCode());
				}else{
					taskQueryVo.setComCode(loginUser.getComCode());
				}
				ResultPage<PrpLPayBankVo> resultPage5 = accountQueryService.search(taskQueryVo,0,10);
				mav.addObject("AccBackNum",resultPage5.getTotalCount());
			}
			mav.addObject("hasAccBackTask",hasAccBackTask);
			
			if(resultPage!=null){
				mav.addObject("resultPageNum",resultPage.getTotalCount());
				taskQueryVo.setHandleStatus("0");
				mav.addObject("timeOutNum",wfTaskQueryService.countTimeOut(taskQueryVo));
			}else{
				mav.addObject("resultPageNum",0);
				mav.addObject("timeOutNum",0);
			}
			if(resultPage2!=null){
				mav.addObject("resultPage2Num",resultPage2.getTotalCount());
			}else{
				mav.addObject("resultPage2Num",0);
			}
			if(resultPage3!=null){
				mav.addObject("resultPage3Num",resultPage3.getTotalCount());
			}else{
				mav.addObject("resultPage3Num",0);
			}
			if(resultPage4!=null){
				mav.addObject("resultPage4Num",resultPage4.getTotalCount());
			}else{
				mav.addObject("resultPage4Num",0);
			}
			mav.setViewName("flowQuery/FlowWorkbench");
			logger.debug(mav.getViewName());
		}
		// 登录人员是否为查勘员，以展示查勘指标信息
		Boolean hasCheckRole = SaaPowerUtils.hasTask(loginUser.getUserCode(),"claim.check");
		if (hasCheckRole) {
			PrpLIndiQuotaInfoVo prpLIndiQuotaInfoVo = null;
			try {
				prpLIndiQuotaInfoVo = checkTaskService.getCheckIndexInfoByUserCode(loginUser.getUserCode());
				mav.addObject("indexInfo", prpLIndiQuotaInfoVo);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				mav.addObject("isCheckRole", hasCheckRole);
			}
		}
		return mav;
	}

	@RequestMapping(value = "/search.do", method = RequestMethod.POST)
	@ResponseBody
	public String search(PrpLWfTaskQueryVo taskQueryVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
			@RequestParam(value = "length", defaultValue = "10") Integer length) throws Exception {
		// 当前登陆用户
		/*
		 * for(int i=0;i<subNodes.length;i++){ System.out.println("String[] subNodes===="+subNodes[i]); }
		 */
		String userCode = WebUserUtils.getUserCode();
		taskQueryVo.setUserCode(userCode);
		String nodeCode = taskQueryVo.getNodeCode();
		//
		String subNodeCode = taskQueryVo.getSubNodeCode();
		//账号信息修改查询
		if("AccBack".equals(subNodeCode)){
			//ComCode用saa_permitcompany.comcode
			List<SysCodeDictVo> sysCodeList=wfTaskQueryService.findAllPermitcompanyByUserCode(WebUserUtils.getUserCode());
			/*if(sysCodeList != null && sysCodeList.size() >0){
				taskQueryVo.setComCode(sysCodeList.get(0).getCodeCode());
			}else{
				taskQueryVo.setComCode(WebUserUtils.getComCode());
			}*/
			
			String  comCode=wfTaskQueryService.getAllPermitcompanyByUserCode(sysCodeList,WebUserUtils.getComCode());
			taskQueryVo.setComCode(comCode);
			
			taskQueryVo.setUserCode(WebUserUtils.getUserCode());
			
			ResultPage<PrpLPayBankVo> page = accountQueryService.search(taskQueryVo,start,length);
			String jsonData = ResponseUtils.toDataTableJson(page,"compensateNo","policyNo","insuredName","bankName:BankCode","accountId",
					"accountName","remark","accountNo","appTime","flag","registNo","chargeCode","payType:PayRefReason","payType","payeeId","serialNo");
			return jsonData;
		}else{
			ResultPage<WfTaskQueryResultVo> resultPage = wfTaskQueryService.findPLossTaskForPage(taskQueryVo);
			String jsonData = null;
			// 下面的数据返回只需要区分节点，状态不同时返回多余的数据没关系
				jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel",
						"registNo","comCode:ComCode","isOnSitReport","reportTime","reportDay","appointmentDay","reconcileFlag:YN01","handlerStatus:HandlerStatus",
						"policyNo","taskInTime","taskInUser:UserCode","licenseNo","insuredName","damageAddress","backFlags:YN01");

			logger.debug(nodeCode+",s="+subNodeCode+".jsonData="+jsonData);
			return jsonData;
		}
	}
	
	@RequestMapping(value = "/searchWorkbench.do", method = RequestMethod.POST)
	@ResponseBody
	public  String searchWorkbench(PrpLWfTaskQueryVo taskQueryVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
			@RequestParam(value = "length", defaultValue = "10") Integer length) throws Exception {
		
		taskQueryVo.setUserCode(WebUserUtils.getUserCode());
		taskQueryVo.setComCode(WebUserUtils.getComCode());
		String subNodeCode = taskQueryVo.getSubNodeCode();
		
		if("AccBack".equals(subNodeCode)){
			//ComCode用saa_permitcompany.comcode  
			List<SysCodeDictVo> sysCodeList=wfTaskQueryService.findAllPermitcompanyByUserCode(WebUserUtils.getUserCode());
		/*	if(sysCodeList != null && sysCodeList.size() >0){
				taskQueryVo.setComCode(sysCodeList.get(0).getCodeCode());
			}else{
				taskQueryVo.setComCode(WebUserUtils.getComCode());
			}*/
			
			String  comCode=wfTaskQueryService.getAllPermitcompanyByUserCode(sysCodeList,WebUserUtils.getComCode());
			taskQueryVo.setComCode(comCode);
			
			ResultPage<PrpLPayBankVo> page = accountQueryService.search(taskQueryVo,start,length);
			String jsonData = ResponseUtils.toDataTableJson(page,"compensateNo","policyNo","insuredName","bankName:BankCode","accountId",
					"accountName","remark","accountNo","appTime","flag","registNo","chargeCode","payType:PayRefReason","payType","payeeId");
			return jsonData;
		}else{
			ResultPage<WfTaskQueryResultVo> resultPage = wfTaskQueryService.findTaskForWorkBench(taskQueryVo);
			String jsonData = new String();
			if(resultPage != null){
				jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(FlowNode.Regis.name(),null),
						"registNo","comCode:ComCode","handlerStatus","policyNo","taskInTime","licenseNo",
						"taskInUser:UserCode","insuredName","damageAddress","nodeCode","subNodeCode","taskName",
						"deflossCarType","reconcileFlag:YN01","backFlags:YN01","taskId","workStatus","handlerIdKey","claimNo");
			}else{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("recordsTotal", 0);
				jsonObject.put("recordsFiltered",0);
				jsonObject.put("data", new ArrayList());
				jsonData = ResponseUtils.jsonToString(jsonObject);
			}
			
		return jsonData;
		}
	}

	private ObjectToMapCallback searchCallBack(final String nodeCode,final String subNodeCode) {
		ObjectToMapCallback callBack = new ObjectToMapCallback() {

			public Map<String,Object> toMap(Object object) {
				Map<String,Object> dataMap = new HashMap<String,Object>();
				WfTaskQueryResultVo resultVo = (WfTaskQueryResultVo)object;
				String registNo = resultVo.getRegistNo();
				String policyNo = resultVo.getPolicyNo();
				String policyNoLink = resultVo.getPolicyNoLink();

				String claimNo = resultVo.getClaimNo();
				FlowNode rootNode = null;
				if(subNodeCode==null){
					rootNode = FlowNode.valueOf(nodeCode);
				}else{
					rootNode = FlowNode.valueOf(subNodeCode).getRootNode();
				}
				StringBuffer regNoUrl = new StringBuffer();
				regNoUrl.append(TaskQueryUtils.getTaskHandUrl(nodeCode,subNodeCode));
				regNoUrl.append("?flowId=").append(resultVo.getFlowId());
				regNoUrl.append("&flowTaskId=").append(resultVo.getTaskId());
				regNoUrl.append("&taskInKey=").append(resultVo.getTaskInKey());// 工作流流入的时的业务号，可能是报案号，立案号，计算书号
				regNoUrl.append("&handlerIdKey=").append(resultVo.getHandlerIdKey());// 流入业务Id
				regNoUrl.append("&registNo=").append(registNo);
				StringBuffer regHtml = new StringBuffer();
					regHtml.append("<a onClick=openTaskEditWin('"+resultVo.getTaskName()+"处理','"+regNoUrl.toString()+"') >");
				regHtml.append(registNo);
				regHtml.append("</a>");
				dataMap.put("registNoHtml",regHtml.toString());
				String plyNoUrl = TaskQueryUtils.getTaskHandUrl("PolicyView",null);
				plyNoUrl += "?registNo="+registNo;

				StringBuffer plyNoHtml = new StringBuffer();
				plyNoHtml.append("<a href='").append(plyNoUrl).append("'   target='_blank' >");
				plyNoHtml.append(policyNo);
				if(StringUtils.isNotBlank(policyNoLink)){
					plyNoHtml.append("<br>");
					plyNoHtml.append(policyNoLink);
				}
				plyNoHtml.append("</a>");
				dataMap.put("policyNoHtml",plyNoHtml.toString());

				// 点击立案号进入立案编辑页面
				StringBuffer claimNoHtml = new StringBuffer();
				StringBuffer claimUrl = new StringBuffer();
				claimUrl.append(TaskQueryUtils.getTaskHandUrl(nodeCode,subNodeCode));
				claimUrl.append("?claimNo=").append(claimNo);
				claimUrl.append("&flowTaskId=").append(resultVo.getTaskId());
				claimNoHtml
						.append("<a onClick=openTaskEditWin('"+resultVo.getTaskName()+"处理','"+claimUrl.toString()+"') >");
				claimNoHtml.append(resultVo.getClaimNo());
				claimNoHtml.append("</a>");
				dataMap.put("claimNoHtml",claimNoHtml.toString());
				// 立案注销 申请
				if(FlowNode.CancelApp.equals(subNodeCode)||FlowNode.ReCanApp.equals(subNodeCode)||FlowNode.CancelAppJuPei
						.equals(subNodeCode)){
					StringBuffer a = new StringBuffer();
					a.append(TaskQueryUtils.getTaskHandUrl(nodeCode,subNodeCode));
					a.append("?flowId=").append(resultVo.getFlowId());
					a.append("&taskId=").append(resultVo.getTaskId());
					a.append("&taskInKey=").append(resultVo.getTaskInKey());// 工作流流入的时的业务号，可能是报案号，立案号，计算书号
					a.append("&handlerIdKey=").append(resultVo.getHandlerIdKey());// 流入业务Id
					a.append("&claimNo=").append(resultVo.getClaimNo());
					a.append("&handlerStatus=").append(resultVo.getHandlerStatus());
					a.append("&workStatus=").append(resultVo.getWorkStatus());
					a.append("&subNodeCode=").append(resultVo.getSubNodeCode());
					StringBuffer c = new StringBuffer();
					c.append("<a onClick=openTaskEditWin('注销申请处理','"+a.toString()+"') >");
					c.append(claimNo);
					c.append("</a>");
					dataMap.put("claimCancelHtml",c.toString());
					System.out.println("claimCancelHtml=="+c.toString());
				}
				// 立案注销审核
				if(FlowNode.CancelVrf.equals(subNodeCode)||FlowNode.CancelLVrf.equals(subNodeCode)){
					StringBuffer a = new StringBuffer();
					a.append(TaskQueryUtils.getTaskHandUrl(nodeCode,subNodeCode));
					a.append("?flowId=").append(resultVo.getFlowId());
					a.append("&flowTaskId=").append(resultVo.getTaskId());
					a.append("&taskInKey=").append(resultVo.getTaskInKey());// 工作流流入的时的业务号，可能是报案号，立案号，计算书号
					a.append("&handlerIdKey=").append(resultVo.getHandlerIdKey());// 流入业务Id
					a.append("&claimNo=").append(resultVo.getClaimNo());
					a.append("&handlerStatus=").append(resultVo.getHandlerStatus());
					a.append("&workStatus=").append(resultVo.getWorkStatus());
					a.append("&subNodeCode=").append(resultVo.getSubNodeCode());
					if(FlowNode.CancelVrf.equals(subNodeCode.substring(0,9))){
						a.append("&types=").append("1");
					}else{
						a.append("&types=").append("2");
					}
					StringBuffer c = new StringBuffer();
					c.append("<a onClick=openTaskEditWin('注销审核处理','"+a.toString()+"') >");
					c.append(claimNo);
					c.append("</a>");
					dataMap.put("claimCancelHtml",c.toString());
					System.out.println("claimCancelHtml=="+c.toString());
				}

				// 立案注销恢复审核申请
				if(FlowNode.ReCanVrf.equals(subNodeCode)||FlowNode.ReCanLVrf.equals(subNodeCode)){
					StringBuffer a = new StringBuffer();
					a.append(TaskQueryUtils.getTaskHandUrl(nodeCode,subNodeCode));
					a.append("?flowId=").append(resultVo.getFlowId());
					a.append("&flowTaskId=").append(resultVo.getTaskId());
					a.append("&taskInKey=").append(resultVo.getTaskInKey());// 工作流流入的时的业务号，可能是报案号，立案号，计算书号
					a.append("&handlerIdKey=").append(resultVo.getHandlerIdKey());// 流入业务Id
					a.append("&claimNo=").append(resultVo.getClaimNo());
					a.append("&handlerStatus=").append(resultVo.getHandlerStatus());
					a.append("&workStatus=").append(resultVo.getWorkStatus());
					a.append("&subNodeCode=").append(resultVo.getSubNodeCode());
					if(FlowNode.ReCanVrf.equals(subNodeCode.substring(0,8))){
						a.append("&types=").append("1");
					}else{
						a.append("&types=").append("2");
					}
					StringBuffer c = new StringBuffer();
					c.append("<a onClick=openTaskEditWin('注销审核处理','"+a.toString()+"') >");
					c.append(claimNo);
					c.append("</a>");
					dataMap.put("claimCancelHtml",c.toString());
					System.out.println("claimCancelHtml=="+c.toString());
				}

				Map<String,String> otherTagMap = new HashMap<String,String>();
				if(FlowNode.Regis==rootNode||FlowNode.DLoss==rootNode){// 定损报案 取自己的紧急程度
					otherTagMap = null;
				}else{
					otherTagMap = new HashMap<String,String>();
					otherTagMap.put("mercyFlag",resultVo.getMercyFlag());
				}
				String bussTag = resultVo.getBussTag();
				logger.debug("bussTag==" + bussTag);
				StringBuffer bussTagHtml = TaskExtMapUtils.jsonTagToHtml(bussTag,otherTagMap);
				
			/*	String bussTag = resultVo.getBussTag();
				logger.debug("bussTag=="+bussTag);
				StringBuffer bussTagHtml = TaskExtMapUtils.jsonTagToHtml(bussTag);*/
				dataMap.put("bussTagHtml",bussTagHtml.toString());

				StringBuffer unHandBtnUrl = new StringBuffer();
				unHandBtnUrl.append("?taskId=").append(resultVo.getTaskId());
				unHandBtnUrl.append("&flowId=").append(resultVo.getFlowId());
				unHandBtnUrl.append("&registNo=").append(resultVo.getRegistNo());
				unHandBtnUrl.append("&nodeCode=").append(nodeCode);
				unHandBtnUrl.append("&subNodeCode=").append(subNodeCode);

				String showInfoXML = resultVo.getShowInfoXML();
				logger.debug("showInfoXML=="+showInfoXML);
				if(StringUtils.isNotBlank(showInfoXML)){
					Map<String,String> showInfoMap = TaskExtMapUtils.jsonToMap(showInfoXML);
					dataMap.putAll(showInfoMap);
				}
				return dataMap;
			}
		};
		return callBack;
	}

}
