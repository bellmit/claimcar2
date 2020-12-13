/******************************************************************************
 * CREATETIME : 2016年1月9日 下午7:45:46
 ******************************************************************************/
package ins.sino.claimcar.flow.web.action;

import ins.framework.common.ResultPage;
import ins.framework.service.CodeTranService;
import ins.framework.web.support.ObjectToMapCallback;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.util.TaskQueryUtils;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.ArrayList;
import java.util.Date;
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
 * <pre>工作流任务查询Action</pre>
 * @author ★LiuPing
 * @CreateTime 2016年1月9日
 */
@Controller
@RequestMapping("/taskQuery")
public class WfTaskQueryAction {

	private static Logger logger = LoggerFactory.getLogger(WfTaskQueryAction.class);
	// 常用格式定义
	private static final String FM_DATE_dd = "#yyyy-MM-dd";
	private static final String FM_DATE_mm = "#yyyy-MM-dd HH:mm";
	private static final String FM_NUMB_00 = "$0.00";
	@Autowired
	private WfTaskQueryService wfTaskQueryService;
	@Autowired
	private SaaUserPowerService saaUserPowerService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private ClaimService claimService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	RegistQueryService registQueryService;
	
	@RequestMapping(value = "/initQuery.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initPage(@RequestParam(value = "node") String nodeCode,String subNode,SysUserVo loginUser,String queryRange) {
		String comCode = WebUserUtils .getComCode();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		ModelAndView mav = new ModelAndView();
		mav.addObject("nodeCode", nodeCode);
		mav.addObject("taskInTimeStart", startDate);
		mav.addObject("taskInTimeEnd", endDate);
		mav.addObject("reportTimeStart", startDate);
		mav.addObject("reportTimeEnd", endDate);
		
		
		// 根据请求路径的FlowNode节点名 进入各自的查询界面
		String forwordPage = null;
		if (StringUtils.isNotBlank(subNode)) {
			forwordPage = "taskQuery/" + subNode + "TaskQueryList";
		} else {
			if(FlowNode.SHandover.name().equals(nodeCode)){//超级平级移交和普通平级移交一个页面
				nodeCode = FlowNode.Handover.name();
			}
			forwordPage = "taskQuery/" + nodeCode + "TaskQueryList";
		}
		
		if(FlowNode.Interm.equals(nodeCode)){
			List<PrpdIntermMainVo> prpdIntermlist = managerService.findIntermListByHql(comCode);
			List<SysCodeDictVo> dictVos = new ArrayList<SysCodeDictVo>();
			for(PrpdIntermMainVo itemKind:prpdIntermlist){
				SysCodeDictVo dict1Vo = new SysCodeDictVo();
				dict1Vo.setCodeCode(itemKind.getId()+"");
				dict1Vo.setCodeName(itemKind.getIntermCode()+"-"+itemKind.getIntermName());
				dictVos.add(dict1Vo);
			}
			mav.addObject("dictVos",dictVos);
		}
		
		if(FlowNode.ChkBig.equals(nodeCode)||FlowNode.PLBig.equals(nodeCode)){
			SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(loginUser.getUserCode());
			// 获取当前人员在这个节点的最高审核等级
			String userMaxLevel = userPowerVo.getMaxVerifyLVMap().get(nodeCode);
			if(StringUtils.isNotEmpty(userMaxLevel)){
				subNode = userMaxLevel;
			}
		}
		
		if("CancelDLCar".equals(subNode) || "CancelDLProp".equals(subNode)
				|| "CDLCar".equals(subNode) || "CDLProp".equals(subNode)){
			mav.addObject("comCode", comCode.startsWith("00") ? comCode.substring(0,4)+"0000":comCode.substring(0,2)+"000000");
		}
		
		mav.addObject("subNodeCode", subNode);
		mav.addObject("queryRange", queryRange);
		mav.setViewName(forwordPage);
		logger.debug(nodeCode + ".forwordPage=" + forwordPage);

		return mav;
	}
   
	/**
	 * 工作流统一查询方法
	 */
	@RequestMapping(value = "/search.do", method = RequestMethod.POST)
	@ResponseBody
	public String search(PrpLWfTaskQueryVo taskQueryVo) throws Exception {
		// 当前登陆用户
		/*for(int i=0;i<subNodes.length;i++){
			System.out.println("String[] subNodes===="+subNodes[i]);
		}*/
		String str=taskQueryVo.getRegistNo();
		System.out.print("----------------------------------------------->"+str);
		String userCode = WebUserUtils .getUserCode();
		String comCode = WebUserUtils.getComCode();
		logger.debug("userCode============"+userCode+"comCode============"+comCode);
		taskQueryVo.setUserCode(userCode);
		if(comCode!=null && comCode!=""){
			taskQueryVo.setAssignCode(comCode.substring(0, 4));
		}
		String nodeCode = taskQueryVo.getNodeCode();
		//
		String subNodeCode = taskQueryVo.getSubNodeCode();
		ResultPage<WfTaskQueryResultVo> resultPage = wfTaskQueryService.findTaskForPage(taskQueryVo);
		String jsonData = null;
		// 下面的数据返回只需要区分节点，状态不同时返回多余的数据没关系
		if(FlowNode.Regis.equals(nodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"registNo","comCode:ComCode","itemName","insuredName",
					"damageTime"+FM_DATE_mm,"reportTime"+FM_DATE_mm,"policyType","handlerUser:UserCode","damageAddress");
		}else if(FlowNode.Sched.equals(nodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"registNo","comCode:ComCode","itemName",
					"insuredName","license","assignUser","damageTime","taskInTime"+FM_DATE_mm,"taskInUser","taskInUser:UserCode","isOnSitReport:YN01",
					"reportTime"+FM_DATE_mm);
		}else if(FlowNode.ChkBig.equals(nodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel","registNo","comCode:ComCode",
					"isOnSitReport","reportTime","taskInTime","taskInUser:UserCode","policyNo","licenseNo","damageTime","comCodePly:ComCode",
					"insuredName","taskName",
					"scheduleType","deflossCarType","lossFee","modelName","sumLossFee");
		}else if(FlowNode.Check.equals(nodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel","registNo","policyNo",
					"insuredName","licenseNo","comCode:ComCode","damageTime","taskInTime","taskInUser:UserCode","comCodePly:ComCode","scheduleType","taskName");
		}else if(FlowNode.DLProp.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel","registNo","taskName",
					"sumVeripLoss","comCode:ComCode","taskInTime","taskInUser:UserCode","policyNo","insuredName","serialNo","licenseNo","lossFee","sumLossFee","taskName","license",
					"taskInKey","handlerIdKey","flowId","taskId");
		}else if(FlowNode.DLoss.equals(nodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel","registNo","comCode:ComCode","comCodePly:ComCode",
					"deflossCarType","taskInTime","taskInUser:UserCode","policyNo","serialNo","licenseNo","license","modelName","lossFee","insuredName","taskName","sumLossFee","taskId",
					"taskInKey","handlerIdKey","flowId");
		}else if(FlowNode.DLChk.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel","registNo","comCode:ComCode","comCodePly:ComCode",
					"isOnSitReport","reportTime","taskInTime","taskInUser:UserCode","policyNo","licenseNo","damageTime","insuredName","scheduleType",
					"deflossCarType","lossFee","modelName","sumLossFee");
		}else if(FlowNode.PLFirst.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel","registNo","comCode:ComCode",
					"isOnSitReport","reportTime","reportDay","reconcileFlag:YN01","policyNo","taskInTime","taskInUser:UserCode","licenseNo",
					"insuredName");
		}else if(FlowNode.PLoss.equals(nodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel","registNo","comCode:ComCode","taskInNode:FlowNode",
					"isOnSitReport","reportTime","appointmentTime","appointmentDay","taskInNodeName","reportDay","reconcileFlag:YN01","policyNo","taskInTime","taskInUser:UserCode",
					"licenseNo","insuredName","taskInNode:FlowNode","comCodePly:ComCode","appointTaskInTime");
		}else if(FlowNode.PLBig.equals(nodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel","registNo","comCode:ComCode",
					"isOnSitReport","reportTime","appointmentTime","reportDay","reconcileFlag:YN01","policyNo","taskInTime","taskInUser:UserCode",
					"licenseNo","insuredName","comCodePly:ComCode");
		}else if(FlowNode.PLNext.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel","registNo","comCode:ComCode",
					"isOnSitReport","reportTime","appointmentDay","reportDay","reconcileFlag:YN01","appointmentTime","taskInNode:FlowNode",
					"policyNo","taskInTime","taskInUser:UserCode","licenseNo","insuredName");
		}else if(FlowNode.PLVerify.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel","registNo","comCode:ComCode",
					"isOnSitReport","reportTime","appointmentTime","reportDay","reconcileFlag:YN01","policyNo","taskInTime","taskInUser:UserCode",
					"licenseNo","insuredName","appointTaskInTime");
		}else if(FlowNode.PLCharge.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel","registNo","comCode:ComCode",
					"remark","isOnSitReport","reportTime","appointmentTime","reportDay","reconcileFlag:YN01","policyNo","taskInTime",
					"taskInUser:UserCode","licenseNo","insuredName");
		}else if(FlowNode.VLoss.equals(nodeCode)&&StringUtils.isBlank(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel","registNo","policyNo",
					"licenseNo","insuredName","underWriteFlag","comCode:ComCode","taskInTime","taskInUser:UserCode","comCodePly:ComCode","lossFee",
					"sumLossFee","underwriteName","sumLossFee","modelName","isRecheck","taskName");
		}else if(FlowNode.VLProp.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel","registNo","policyNo",
					"licenseNo","insuredName","comCode:ComCode","taskInTime","taskInUser:UserCode","license","sumVeriLoss","sumVeripLoss","sumLossFee","isRecheck",
					"assignUser:UserCode","handlerUser:UserCode","taskOutUser:UserCode","subNodeCode:FlowNode","modelName");
		}else if(FlowNode.VLCar.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel","registNo","policyNo","taskInNode",
					"licenseNo","insuredName","comCodePly:ComCode","taskInTime","taskInUser:UserCode","license","sumVeriLoss","sumVeripLoss","modelName",
					"sumLossFee","isRecheck","assignUser:UserCode","handlerUser:UserCode","taskOutUser:UserCode","taskInNode:FlowNode","sumVeriLossFee","money");
		}else if(FlowNode.VPCar.equals(subNodeCode)){
			logger.debug("hsuhsuhsu核价=======================");
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel","registNo","policyNo",
					"licenseNo","insuredName","license","sumVeripLoss","sumLossFee","riskCode:RiskCode","taskInTime","taskInUser:UserCode",
					"deflossCarType","assignUser:UserCode","handlerUser:UserCode","taskOutUser:UserCode","taskInNode:FlowNode");
		}else if(FlowNode.RecLoss.equals(nodeCode)){
			for(WfTaskQueryResultVo wfTaskQueryResultVo:resultPage.getData()){
				wfTaskQueryResultVo.setTaskName("损余回收");
			}
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"registNo","policyNo",
					"taskInTime"+FM_DATE_mm,"insuredName","riskCode:RiskCode",
					"assignUser:UserCode","handlerUser:UserCode","taskOutUser:UserCode","taskId");
		}else if(FlowNode.Certi.equals(nodeCode)){// 单证
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"registNo","policyNo",
					"taskInTime"+FM_DATE_mm,"insuredName","sumVeriLossFee","taskInUser:UserCode","taskOutUser:UserCode","license","licenseNo","isVLoss::YN01");
		}else if(FlowNode.VClaim.equals(nodeCode)){//核赔
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"registNo","policyNo","licenseNo","handlerIdKey",
					"taskName","underwriteName","taskInTime","insuredName","licenseNo","taskInUser:UserCode","taskOutUser:UserCode","ywTaskType:FlowNode","claimNo","comCodePly:ComCode","taskInNode","compensateAmount","assignUser");
		}else if(FlowNode.Compe.equals(nodeCode)){//理算查询
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"registNo","policyNo","taskInTime","compensateNo",
					"handlerIdKey","taskName","underwriteName","taskInTime"+FM_DATE_mm,"insuredName","handlerUser:UserCode","assignUser:UserCode","taskInUser:UserCode","taskOutUser:UserCode","scheduleType","claimNo","comCodePly:ComCode","licenseNo");
		}else if(FlowNode.Claim.equals(nodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"mercyFlag:MercyFlag","license",
					"claimNo","registNo","policyNo","riskCode:CarRiskCode","insuredName","taskInUser:UserCode","taskInTime"+FM_DATE_mm);
		}else if(FlowNode.PadPay.equals(nodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"mercyFlag","comCodePly","comCode:ComCode",
					"taskName","taskInKey","assignUser:UserCode","claimNo","registNo","policyNo","underwriteName","insuredName","taskInUser","taskInTime","damageAddress");
		}else if(FlowNode.EndCas.equals(nodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"mercyFlag","comCodePly","comCode:ComCode",
					"riskCode:RiskCode","claimNo","registNo","policyNo","underwriteName","insuredName","taskInUser:UserCode","taskInTime","damageAddress","taskOutKey");
		}else if(FlowNode.CancelVrf.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"mercyFlag","customerLevel",
						"applyReason:ApplyReason","claimNo","registNo","policyNo","riskCode","insuredName","taskInUser","taskInTime"+FM_DATE_mm,"claimTime"+FM_DATE_mm,"claimCancelTime"+FM_DATE_mm,"assignUser:UserCode");
		}else if(FlowNode.CancelLVrf.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"mercyFlag","customerLevel","applyReason",
					"applyReason:ApplyReason","claimNo","registNo","policyNo","riskCode","insuredName","taskInUser","taskInTime"+FM_DATE_mm,"claimTime"+FM_DATE_mm,"claimCancelTime"+FM_DATE_mm,"assignUser:UserCode");
		}else if(FlowNode.ReCanVrf.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"mercyFlag","customerLevel",
					"applyReason:ApplyReason","claimNo","registNo","policyNo","riskCode","insuredName","taskInUser","taskInTime"+FM_DATE_mm,"recoverReason","claimRecoverTime"+FM_DATE_mm,"underwriteName");
		}else if(FlowNode.ReCanLVrf.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"mercyFlag","customerLevel",
				"applyReason:ApplyReason","claimNo","registNo","policyNo","riskCode","insuredName","taskInUser","taskInTime"+FM_DATE_mm,"recoverReason","claimRecoverTime"+FM_DATE_mm,"underwriteName");
		}else if(FlowNode.PrePay.equals(nodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"mercyFlag:MercyFlag","comCodePly:ComCode",
					"claimNo","registNo","policyNo","underwriteName","insuredName","taskInUser:UserCode","handlerUser:UserCode","taskInTime"+FM_DATE_mm,"damageAddress","policyNoLink","riskCode");
		}else if(FlowNode.PrePayWf.equals(nodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"mercyFlag","comCodePly:ComCode","compensateNo","assignUser:UserCode",
					"claimNo","registNo","policyNo","underwriteName","insuredName","taskInUser:UserCode","handlerUser:UserCode","taskInTime"+FM_DATE_mm,"damageAddress","riskCode");
		}else if(FlowNode.RecPay.equals(nodeCode)){//追偿查询
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"mercyFlag:MercyFlag","registNo","compensateNo","policyNo","taskInNode:FlowNode","claimNo",
					"taskInTime","replevyType","sumPlanReplevy");
		}else if(FlowNode.CompeWf.equals(nodeCode)){//理算冲销查询
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"registNo","policyNo","taskInTime","compensateNo",
					"taskName","underwriteName","taskInTime"+FM_DATE_mm,"insuredName","taskInUser:UserCode","taskOutUser:UserCode","scheduleType","claimNo","comCodePly:ComCode","licenseNo");
		}else if(FlowNode.ReCanVrf_LV1.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"mercyFlag","customerLevel",
					"applyReason","claimNo","registNo","policyNo","riskCode","insuredName","taskInUser","taskInTime"+FM_DATE_mm);
		}else if(FlowNode.ReCanVrf_LV1.equals(subNodeCode)){
		jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"mercyFlag","customerLevel",
				"applyReason","claimNo","registNo","policyNo","riskCode","insuredName","taskInUser","taskInTime"+FM_DATE_mm);
		}else if(FlowNode.ReOpenVrf.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"claimNo","policyNo","riskCode:RiskCode","insuredName","taskInUser:UserCode","registNo","policyNoLink","riskCode");
		}else if(FlowNode.CancelApp.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"mercyFlag","customerLevel",
					"applyReason","claimNo","registNo","policyNo","riskCode","insuredName","taskInUser","taskInTime"+FM_DATE_mm,"claimTime"+FM_DATE_mm,"claimCancelTime"+FM_DATE_mm,"assignUser:UserCode");
		}else if(FlowNode.CancelAppJuPei.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"mercyFlag","customerLevel",
					"applyReason:ApplyReason","claimNo","registNo","policyNo","riskCode","insuredName","taskInUser","taskInTime"+FM_DATE_mm,"claimTime"+FM_DATE_mm,"claimCancelTime"+FM_DATE_mm,"assignUser:UserCode");
		}else if(FlowNode.ReCanApp.equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"mercyFlag","customerLevel",
					"applyReason","claimNo","registNo","policyNo","riskCode","insuredName","taskInUser","taskInTime"+FM_DATE_mm);
		}else if(FlowNode.IntermQuery.equals(subNodeCode) ){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"taskId","IntermCode",
					"taskInUser:UserCode","taskInTime"+FM_DATE_mm);
		}else if(FlowNode.IntermCheckQuery.equals(subNodeCode) ){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"taskId","IntermCode",
					"registNo",	"taskInUser:UserCode","taskInTime"+FM_DATE_mm);
		}else if(FlowNode.Survey.equals(nodeCode) ){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"registNo","comCode:ComCode","damageTime#"+DateUtils.YToSec,
					"reportTime#"+DateUtils.YToSec,"taskInTime#"+DateUtils.YToSec,"insuredName","taskInUser:UserCode","handlerUser:UserCode","handlerTime#"+DateUtils.YToSec,"handlerStatus:HandlerStatus","taskId");
		}else{
			throw new IllegalArgumentException("未知节点："+nodeCode);
		}
		
		logger.debug(nodeCode+",s="+subNodeCode+".jsonData="+jsonData);
		return jsonData;
	}

	/**
	 * 已调度未接收任务查询
	 */
	@RequestMapping(value = "/searchSchedUnWork.do", method = RequestMethod.POST)
	@ResponseBody
	public String searchSchedUnWork(PrpLWfTaskQueryVo taskQueryVo)
			throws Exception {
		// 因套用工作流的javascript，handleStatus 需要保存为 subNodeCode，handleStatus=1
		FlowNode queryNode = FlowNode.valueOf(taskQueryVo.getHandleStatus());
		String nodeCode = queryNode.getUpperNode();
		String subNodeCode = queryNode.name();
		taskQueryVo.setNodeCode(nodeCode);
		taskQueryVo.setSubNodeCode(subNodeCode);
		taskQueryVo.setHandleStatus(HandlerStatus.INIT);
		String userCode = WebUserUtils.getUserCode();
		taskQueryVo.setUserCode(userCode);
		ResultPage<WfTaskQueryResultVo> resultPage = wfTaskQueryService
				.findTaskForPage(taskQueryVo);
		for(WfTaskQueryResultVo wfTaskQueryResultVo:resultPage.getData()){
			wfTaskQueryResultVo.setTaskName("调度");
		}
		String jsonData = ResponseUtils.toDataTableJson(resultPage,
				searchCallBack(FlowNode.Sched.name(), null), "registNo",
				"comCodePly:ComCode", "itemName", "insuredName","licenseNo",
				"assignUser:UserCode", "assignUserPhone", "damageTime"
				+ FM_DATE_mm, "taskInTime" + FM_DATE_mm,  
				"schedFirstTime", "schedLastTime", "taskInUser:UserCode",
				"overTime", "reportTime" + FM_DATE_mm);
		return jsonData;
	}

	/**
	 * 查询平级移交任务
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchHandoverTask.do", method = RequestMethod.POST)
	@ResponseBody
	public String searchHandoverTask(PrpLWfTaskQueryVo taskQueryVo)
			throws Exception {
		String nodeCode = taskQueryVo.getNodeCode();
		ResultPage<WfTaskQueryResultVo> resultPage = wfTaskQueryService
				.findHandoverTaskForPage(taskQueryVo);
		String jsonData = ResponseUtils.toDataTableJson(resultPage,
				searchCallBack(nodeCode,null), "handoverTimes","subNodeCode:FlowNode",
				"assignUser:UserCode", "itemName", "handlerStatus:HandlerStatus", "taskInTime"
						, "handlerUser:UserCode");
		return jsonData;
	}

	private ObjectToMapCallback searchCallBack(final String nodeCode,
			final String subNodeCode) {
		ObjectToMapCallback callBack = new ObjectToMapCallback() {

			public Map<String, Object> toMap(Object object) {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				WfTaskQueryResultVo resultVo = (WfTaskQueryResultVo) object;
				String registNo = resultVo.getRegistNo();
				String policyNo = resultVo.getPolicyNo();
				String policyNoLink = resultVo.getPolicyNoLink();
				String claimNo = resultVo.getClaimNo();
				PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
				if(CodeConstants.GBFlag.MAJORCOMMON.equals(registVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag()) || CodeConstants.GBFlag.MAJORRELATION.equals(registVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
					if(resultVo.getBussTag()!=null){
						String bussTagTemp = resultVo.getBussTag();
						bussTagTemp = bussTagTemp.substring(0,bussTagTemp.length()-1)+",\"isGBFlag\":\"1\"}";
						resultVo.setBussTag(bussTagTemp);
					}
				}
				FlowNode rootNode = null;
				if(subNodeCode==null || "".equals(subNodeCode)){
					rootNode = FlowNode.valueOf(nodeCode);
				}else{
					rootNode = FlowNode.valueOf(subNodeCode).getRootNode();
				}
				StringBuffer regNoUrl = new StringBuffer();
				regNoUrl.append(TaskQueryUtils.getTaskHandUrl(nodeCode,
						subNodeCode));
				regNoUrl.append("?flowId=").append(resultVo.getFlowId());
				regNoUrl.append("&flowTaskId=").append(resultVo.getTaskId());
				regNoUrl.append("&taskInKey=").append(resultVo.getTaskInKey());// 工作流流入的时的业务号，可能是报案号，立案号，计算书号
				regNoUrl.append("&handlerIdKey=").append(
						resultVo.getHandlerIdKey());// 流入业务Id
				regNoUrl.append("&registNo=").append(registNo);
				StringBuffer regHtml = new StringBuffer();
				if (FlowNode.Handover.name().equals(nodeCode)) {// 平级移交
					regNoUrl.append("&queryRange=").append("0");
					regHtml.append("<a onClick=openTaskEditWin('平级移交处理','"
							+ regNoUrl.toString() + "') >");
				}else if (FlowNode.SHandover.name().equals(nodeCode)) {// 超级平级移交
					regHtml.append("<a onClick=openTaskEditWin('超级平级移交处理','"
							+ regNoUrl.toString() + "') >");
				} else {
					regHtml.append("<a onClick=openTaskEditWin('"
							+ resultVo.getTaskName() + "处理','"
							+ regNoUrl.toString() + "') >");
				}
				regHtml.append(registNo);
				regHtml.append("</a>");
				dataMap.put("registNoHtml", regHtml.toString());
				String plyNoUrl = TaskQueryUtils.getTaskHandUrl("PolicyView",
						null);
				plyNoUrl += "?registNo=" + registNo;

				StringBuffer plyNoHtml = new StringBuffer();
				plyNoHtml.append("<a href='").append(plyNoUrl)
						.append("'   target='_blank' >");
				plyNoHtml.append(policyNo);
				if (StringUtils.isNotBlank(policyNoLink)) {
					plyNoHtml.append("<br>");
					plyNoHtml.append(policyNoLink);
				}
				plyNoHtml.append("</a>");
				dataMap.put("policyNoHtml", plyNoHtml.toString());

				// 点击立案号进入立案编辑页面
				StringBuffer claimNoHtml = new StringBuffer();
				StringBuffer claimUrl = new StringBuffer();
				claimUrl.append(TaskQueryUtils.getTaskHandUrl(nodeCode,
						subNodeCode));
				claimUrl.append("?claimNo=").append(claimNo);
				claimUrl.append("&flowTaskId=").append(resultVo.getTaskId());
				claimNoHtml.append("<a onClick=openTaskEditWin('"
						+ resultVo.getTaskName() + "处理','"
						+ claimUrl.toString() + "') >");
				claimNoHtml.append(resultVo.getClaimNo());
				claimNoHtml.append("</a>");
				dataMap.put("claimNoHtml", claimNoHtml.toString());
				// 立案注销 申请
				if (FlowNode.ReCanApp.equals(subNodeCode)) {
					StringBuffer a = new StringBuffer();
					a.append(TaskQueryUtils.getTaskHandUrl(nodeCode,
							subNodeCode));
					a.append("?flowId=").append(resultVo.getFlowId());
					a.append("&taskId=").append(resultVo.getTaskId());
					a.append("&taskInKey=").append(resultVo.getTaskInKey());// 工作流流入的时的业务号，可能是报案号，立案号，计算书号
					a.append("&handlerIdKey=").append(
							resultVo.getHandlerIdKey());// 流入业务Id
					a.append("&claimNo=").append(resultVo.getClaimNo());
					a.append("&handlerStatus=").append(
							resultVo.getHandlerStatus());
					a.append("&workStatus=").append(resultVo.getWorkStatus());
					a.append("&subNodeCode=").append(resultVo.getSubNodeCode());
					StringBuffer c = new StringBuffer();
					c.append("<a onClick=openTaskEditWin('注销/拒赔恢复申请处理','"
							+ a.toString() + "') >");
					c.append(claimNo);
					c.append("</a>");
					dataMap.put("claimCancelHtml", c.toString());
					System.out.println("claimCancelHtml==" + c.toString());
				}
				if (FlowNode.CancelApp.equals(subNodeCode)||FlowNode.CancelAppJuPei.equals(subNodeCode)) {
					StringBuffer a = new StringBuffer();
					a.append(TaskQueryUtils.getTaskHandUrl(nodeCode,
							subNodeCode));
					a.append("?flowId=").append(resultVo.getFlowId());
					a.append("&taskId=").append(resultVo.getTaskId());
					a.append("&taskInKey=").append(resultVo.getTaskInKey());// 工作流流入的时的业务号，可能是报案号，立案号，计算书号
					a.append("&handlerIdKey=").append(
							resultVo.getHandlerIdKey());// 流入业务Id
					a.append("&claimNo=").append(resultVo.getClaimNo());
					a.append("&handlerStatus=").append(
							resultVo.getHandlerStatus());
					a.append("&workStatus=").append(resultVo.getWorkStatus());
					a.append("&subNodeCode=").append(resultVo.getSubNodeCode());
					StringBuffer c = new StringBuffer();
					if (FlowNode.CancelApp.equals(resultVo.getSubNodeCode())){
						c.append("<a onClick=openTaskEditWin('注销/拒赔申请处理','"
								+ a.toString() + "') >");
					}
					if (FlowNode.CancelAppJuPei.equals(resultVo.getSubNodeCode())){
						c.append("<a onClick=openTaskEditWin('拒赔申请处理','"
								+ a.toString() + "') >");
					}
					c.append(claimNo);
					c.append("</a>");
					dataMap.put("claimCancelHtml", c.toString());
					System.out.println("claimCancelH31231tml==" + c.toString());
				}
				// 立案注销审核
				if (FlowNode.CancelVrf.equals(subNodeCode)
						|| FlowNode.CancelLVrf.equals(subNodeCode)) {
					StringBuffer a = new StringBuffer();
					a.append(TaskQueryUtils.getTaskHandUrl(nodeCode,
							subNodeCode));
					a.append("?flowId=").append(resultVo.getFlowId());
					a.append("&flowTaskId=").append(resultVo.getTaskId());
					a.append("&taskInKey=").append(resultVo.getTaskInKey());// 工作流流入的时的业务号，可能是报案号，立案号，计算书号
					a.append("&handlerIdKey=").append(
							resultVo.getHandlerIdKey());// 流入业务Id
					a.append("&claimNo=").append(resultVo.getClaimNo());
					a.append("&handlerStatus=").append(
							resultVo.getHandlerStatus());
					a.append("&workStatus=").append(resultVo.getWorkStatus());
					a.append("&subNodeCode=").append(resultVo.getSubNodeCode());
					if (FlowNode.CancelVrf.equals(subNodeCode.substring(0, 9))) {
						a.append("&types=").append("1");
					} else {
						a.append("&types=").append("2");
					}
					StringBuffer c = new StringBuffer();
					if(FlowNode.CancelVrf.equals(subNodeCode)){
						c.append("<a onClick=openTaskEditWin('注销分公司审核处理','"
								+ a.toString() + "') >");
					}
					if(FlowNode.CancelLVrf.equals(subNodeCode)){
						c.append("<a onClick=openTaskEditWin('注销总公司审核处理','"
								+ a.toString() + "') >");
					}
					c.append(claimNo);
					c.append("</a>");
					dataMap.put("claimCancelHtml", c.toString());
					System.out.println("claimCancelHtml==" + c.toString());
				}

				//立案注销恢复审核申请
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
					if(FlowNode.ReCanVrf.equals(subNodeCode.substring(0, 8))){
						a.append("&types=").append("1");
					} else{
						a.append("&types=").append("2");
					}
				StringBuffer c = new StringBuffer();
				c.append("<a onClick=openTaskEditWin('注销/拒赔恢复审核处理','"+a.toString()+"') >");
				c.append(claimNo);
				c.append("</a>");
				dataMap.put("claimCancelHtml", c.toString());
				//System.out.println("claimCancelHtml=="+c.toString());
				}
				
				Map<String,String> otherTagMap = new HashMap<String,String>();
				if(FlowNode.Regis==rootNode||FlowNode.DLoss==rootNode){// 定损报案 取自己的紧急程度
					otherTagMap = null;
				}else{
					otherTagMap = new HashMap<String,String>();
					otherTagMap.put("mercyFlag",resultVo.getMercyFlag());
				}
				
				String bussTag = resultVo.getBussTag();
				if(resultVo.getSubNodeCode().contains("CI")){
				    bussTag = StringUtils.remove(bussTag,"\"isSYFraud\":\"1\",");
                }
				if(resultVo.getSubNodeCode().contains("BI")){
				    bussTag = StringUtils.remove(bussTag,"\"isJQFraud\":\"1\",");
                }

				logger.debug("bussTag==" + bussTag);
				StringBuffer bussTagHtml = TaskExtMapUtils.jsonTagToHtml(bussTag,otherTagMap);
				/*
				 * bussTagHtml.append(
				 * "<span class='badge label-danger radius '>vip</span>");
				 * if(YN01.Y.equals(resultVo.getMercyFlag())){
				 * bussTagHtml.append
				 * ("<span class='badge label-warning radius' title='紧急'>急</span>"
				 * ); } bussTagHtml.append(
				 * "<span class='badge label-primary radius' title='互碰自赔'>互</span>"
				 * );
				 */
				dataMap.put("bussTagHtml", bussTagHtml.toString());

				StringBuffer unHandBtnUrl = new StringBuffer();
				unHandBtnUrl.append("?taskId=").append(resultVo.getTaskId());
				unHandBtnUrl.append("&flowId=").append(resultVo.getFlowId());
				unHandBtnUrl.append("&registNo=")
						.append(resultVo.getRegistNo());
				unHandBtnUrl.append("&nodeCode=").append(nodeCode);
				unHandBtnUrl.append("&subNodeCode=").append(subNodeCode);
				dataMap.put("unHandleBtn",
						"<button class='btn btn-zd' onclick=modification('"
								+ unHandBtnUrl.toString()
								+ "'); >未处理原因</button>");
				dataMap.put(
						"imageBtn",
						"<button onclick=imageMovieUpload('"
								+ resultVo.getTaskId() + "');>照片上传</button>");
				dataMap.put("certifyBtn",
						"<button class='btn btn-zd' onClick=openTaskEditWin('"
							+ resultVo.getTaskName() + "处理','"
							+ regNoUrl.toString() + "&certifyMakeup=yes')>单证补录</button>");

				String showInfoXML = resultVo.getShowInfoXML();
				logger.debug("showInfoXML==" + showInfoXML);
				if (StringUtils.isNotBlank(showInfoXML)) {
					Map<String, String> showInfoMap = TaskExtMapUtils
							.jsonToMap(showInfoXML);
					dataMap.putAll(showInfoMap);
					// 有首次调度时间，计算超时时间
					if (showInfoMap.containsKey("schedFirstTime")) {
						dataMap.put("overTime", "6时12分");
					}
				}
				String modelName = (String)dataMap.get("modelName");
				if(!StringUtils.isBlank(modelName) && !"null".equals(modelName) && modelName.length() > 3){
					dataMap.put("modelName", "<span title='"+modelName+"'>"+modelName.substring(0, 3)+"</span>");
				}else {
					dataMap.put("modelName", "<span title='"+modelName+"'>"+""+"</span>");
				}
				return dataMap;
			}
		};
		return callBack;
	}
	//公估费任务查询
	@RequestMapping(value = "/AssessorFeeTaskQuery.do", method = RequestMethod.POST)
	@ResponseBody
	public String assessorFeeTaskQuery( PrpLWfTaskQueryVo taskQueryVo// 页面组装VO
			)throws Exception {
		
		//ComCode用saa_permitcompany.comcode
		/*List<SysCodeDictVo> sysCodeList=wfTaskQueryService.findAllPermitcompanyByUserCode(WebUserUtils.getUserCode());
		if(sysCodeList != null && sysCodeList.size() >0){
			taskQueryVo.setComCode(sysCodeList.get(0).getCodeCode());
		}else{
			taskQueryVo.setComCode(WebUserUtils.getComCode());
		}*/
		taskQueryVo.setUserCode(WebUserUtils.getUserCode());
		taskQueryVo.setComCode(WebUserUtils.getComCode());
		ResultPage<WfTaskQueryResultVo> page = wfTaskQueryService.findAssessorFeeTaskQuery(taskQueryVo);
		String jsonData = ResponseUtils.toDataTableJson(page,"registNo","taskId","intermCode:GongGuPayCode","intermCode",
				"taskInUser:UserCode","taskInTime"+FM_DATE_mm);
		logger.debug("jsonData="+jsonData);
		return jsonData;

	
	}
	
	//公估费审核查询
	@RequestMapping(value = "/assessorFeeVeriTaskQuery.do", method = RequestMethod.POST)
	@ResponseBody
	public String assessorFeeVeriTaskQuery( PrpLWfTaskQueryVo taskQueryVo)throws Exception {// 页面组装VO
		/*String userCode=WebUserUtils.getUserCode();
		List<SysCodeDictVo> sysCodeList=wfTaskQueryService.findPermitcompanyByUserCode(userCode);
		String comCode=WebUserUtils.getComCode();
		if(sysCodeList!=null && sysCodeList.size()>0){
			comCode=sysCodeList.get(0).getCodeCode();
		}*/
		taskQueryVo.setUserCode(WebUserUtils.getUserCode());
		taskQueryVo.setComCode(WebUserUtils.getComCode());
		ResultPage<WfTaskQueryResultVo> page = wfTaskQueryService.findAssessorFeeVeriTaskQuery(taskQueryVo);
		String jsonData = ResponseUtils.toDataTableJson(page,"taskId","intermCode:GongGuPayCode",
				"registNo",	"taskInUser:UserCode","taskInTime"+FM_DATE_mm);
		return jsonData;

	
	}
	
	
	//立案注销申请未处理查询业务表
	@RequestMapping(value = "/CancelAppTaskQuery.do", method = RequestMethod.POST)
	@ResponseBody
	public String CancelAppTaskQuery( PrpLWfTaskQueryVo taskQueryVo)throws Exception {// 页面组装VO
		ResultPage<PrpLClaimVo> page = claimService.findCancelAppTaskQuery(taskQueryVo);
		String jsonData = ResponseUtils.toDataTableJson(page,searchCallBackAppTask(FlowNode.CancelApp.toString(),""),
				"registNo","claimNo","policyNo","insuredName","startDate","endDate","claimTime");
		return jsonData;
	
	}
	
	//立案注销申请未处理查询业务表
	@RequestMapping(value = "/RecanAppTaskQuery.do", method = RequestMethod.POST)
	@ResponseBody
	public String RecanAppTaskQuery( PrpLWfTaskQueryVo taskQueryVo)throws Exception {// 页面组装VO
		ResultPage<PrpLClaimVo> page = claimService.findRecanAppTaskQuery(taskQueryVo);
		String jsonData = ResponseUtils.toDataTableJson(page,searchCallBackAppTask(FlowNode.ReCanApp.toString(),""),
				"registNo","claimNo","policyNo","insuredName","startDate","endDate","claimTime");
		return jsonData;
	
	}
	
	private ObjectToMapCallback searchCallBackAppTask(final String nodeCode,
			final String subNodeCode) {
		ObjectToMapCallback callBack = new ObjectToMapCallback() {

			public Map<String, Object> toMap(Object object) {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				PrpLClaimVo resultVo = (PrpLClaimVo) object;
				StringBuffer regHtml = new StringBuffer();
				StringBuffer c = new StringBuffer();
				c.append(TaskQueryUtils.getTaskHandUrl(nodeCode,
						subNodeCode));
				regHtml.append("<a onClick=openTaskEditWin('立案注销拒赔申请处理','"
						+ c.toString()+"?claimNo="+resultVo.getClaimNo());
				regHtml.append("&taskId=c" + "&workStatus=c"+"&handlerIdKey=c"+"') >");
				regHtml.append(resultVo.getClaimNo());
				regHtml.append("</a>");
				dataMap.put("claimCancelHtml", regHtml.toString());
				return dataMap;
			}
		};
		return callBack;
	}
	
	/**
	 * 任务注销查询
	 * @param taskQueryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/searchCancelTask.do", method = RequestMethod.POST)
	@ResponseBody
	public String searchCancelTask(PrpLWfTaskQueryVo taskQueryVo)
			throws Exception {
		String nodeCode = taskQueryVo.getNodeCode();
		String subNodeCode = taskQueryVo.getSubNodeCode();
		taskQueryVo.setComCode(WebUserUtils .getComCode());
		ResultPage<WfTaskQueryResultVo> resultPage = wfTaskQueryService.findCancelTaskForPage(taskQueryVo);
		String jsonData = "";
		if(FlowNode.DLCar.name().equals(subNodeCode)||FlowNode.DLCarMod.name().equals(subNodeCode)){
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,subNodeCode),"customerLevel","registNo","comCode:ComCode","comCodePly:ComCode",
					"deflossCarType","taskInTime","taskInUser:UserCode","policyNo","serialNo","licenseNo","license","modelName","lossFee","insuredName","taskName","sumLossFee","taskId",
					"taskInKey","handlerIdKey","flowId");
		}else{
			jsonData = ResponseUtils.toDataTableJson(resultPage,searchCallBack(nodeCode,FlowNode.DLProp.name()),"customerLevel","registNo","taskName",
					"sumVeripLoss","comCode:ComCode","taskInTime","taskInUser:UserCode","policyNo","insuredName","serialNo","licenseNo","lossFee","sumLossFee","taskName","license",
					"taskInKey","handlerIdKey","flowId","taskId");
		}
		
		return jsonData;
	}
	
}
