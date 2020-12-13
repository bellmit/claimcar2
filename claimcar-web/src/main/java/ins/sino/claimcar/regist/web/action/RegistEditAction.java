/******************************************************************************
 * CREATETIME : 2015年11月17日 上午9:52:16
 ******************************************************************************/
package ins.sino.claimcar.regist.web.action;

import freemarker.core.ParseException;
import ins.framework.dao.database.support.Page;
import ins.framework.service.CodeTranService;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.apc.annotation.AvoidRepeatableCommit;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.saa.service.facade.SaaUserGradeService;
import ins.platform.saa.vo.SaaGradeVo;
import ins.platform.schema.SysAreaDict;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.DataUtils;
import ins.platform.utils.ReadConfigUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.ResultAmount;
import ins.sino.claimcar.carchild.service.CarchildService;
import ins.sino.claimcar.carchild.vo.PrplcarchildregistcancleVo;
import ins.sino.claimcar.carchild.vo.RegistInformationVo;
import ins.sino.claimcar.carchild.vo.RevokeBodyVo;
import ins.sino.claimcar.carchild.vo.RevokeInfoReqVo;
import ins.sino.claimcar.carchild.vo.RevokeInfoResVo;
import ins.sino.claimcar.carchild.vo.RevokeTaskInfoVo;
import ins.sino.claimcar.carchildCommon.vo.CarchildHeadVo;
import ins.sino.claimcar.carchildCommon.vo.CarchildResponseHeadVo;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLDisasterVo;
import ins.sino.claimcar.ciitc.service.AccidentService;
import ins.sino.claimcar.ciitc.vo.accident.CiitcAccidentResVo;
import ins.sino.claimcar.claim.service.ClaimCancelRecoverService;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ClaimTaskExtService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLcancelTraceVo;
import ins.sino.claimcar.claimjy.service.PrpLRegistToLossService;
import ins.sino.claimcar.common.filter.Timed;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.RepairFactoryService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossprop.service.PropLossService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.middlestagequery.service.ClaimToMiddleStageOfCaseService;
import ins.sino.claimcar.mobilecheck.service.SendMsgToMobileService;
import ins.sino.claimcar.platform.service.ClaimToPaltformService;
import ins.sino.claimcar.platform.service.RegistToPaltformService;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.regist.service.FestivalService;
import ins.sino.claimcar.regist.service.FounderCustomService;
import ins.sino.claimcar.regist.service.PolicyQueryService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistAddService;
import ins.sino.claimcar.regist.service.RegistHandlerService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.service.RegistTmpService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PolicyInfoVo;
import ins.sino.claimcar.regist.vo.PrpCiInsureValidVo;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPersonLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPropLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistRelationshipHisVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrpLfestivalVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import ins.sino.claimcar.trafficplatform.service.RegistToCarRiskPaltformService;
import ins.sino.claimcar.trafficplatform.service.SzpoliceCaseService;
import ins.sino.claimcar.trafficplatform.vo.AccidentResInfo;
import ins.sino.claimcar.utils.SaaPowerUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * 报案编辑Action
 * 
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2015年11月17日
 */
@Controller
@RequestMapping("/regist")
public class RegistEditAction {

	public Logger logger = LoggerFactory.getLogger(RegistEditAction.class);
    private static final String CT_02="dhDockingService.cancelCase";
	// 服务装载
	@Autowired
	SysUserService sysUserService;
	@Autowired
	PolicyQueryService policyQueryService;
	@Autowired
	RegistHandlerService registHandlerService;
	@Autowired
	PrpLCMainService prpLCMainService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	ManagerService managerService;
	@Autowired
	RegistService registService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	RegistTmpService registTmpService;
	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	private ClaimTaskExtService claimTaskExtService;
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	@Autowired
	ClaimToPaltformService claimToPaltformService;
	@Autowired
	AreaDictService areaDictService;
	@Autowired
	FounderCustomService founderService;
	@Autowired
	RegistToPaltformService registToPaltformService;
	@Autowired
	SaaUserGradeService saaUserGradeService;
	@Autowired
	LossCarService lossCarService;
	@Autowired
	RegistAddService registAddService;
	@Autowired 
	ClaimService claimService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	@Autowired
	SendMsgToMobileService sendMsgToMobileService;

	@Autowired
	ClaimCancelRecoverService claimCancelRecoverService;

	@Autowired
	WfMainService wfMainService;
	@Autowired
	CarchildService carchildService;
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	ClaimInterfaceLogService claimInterfaceLogService;

	@Autowired
	PropLossService propLossService;
	@Autowired
	ScheduleTaskService scheduleTaskService;

	@Autowired
	FestivalService festivalService;
    @Autowired
    RegistToCarRiskPaltformService registToCarRiskPaltformService;
    @Autowired
    SzpoliceCaseService szpoliceCaseService;
	@Autowired
	PrpLRegistToLossService prpLRegistToLossService; 
	@Autowired
	AccidentService accidentService;
	@Autowired
	RepairFactoryService repairFactoryService;
    @Autowired
    ClaimToMiddleStageOfCaseService claimToMiddleStageOfCaseService;
	public static final String AUTOSCHEDULE_URL_METHOD = "prplschedule/autoSchedule.do";
	/**
	 * 检查历史报案
	 * @param policyNo
	 * @param model
	 * @return
	 * @modified: ☆LiuPing(2015年11月17日 上午10:13:55): <br>
	 */
	@RequestMapping(value = "/checkHisRegist.ajax")
	@ResponseBody
	public AjaxResult checkHisRegist(String policyNo) {
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatusText("true");
		// ajaxResult.setData(data);//历史案件列表
		return ajaxResult;
	}

	/**
	 * 新增报案，修改报案 初始 编辑信息
	 * @param policyNo
	 * @param model
	 * @return
	 * @modified: ☆LiuPing(2015年11月17日 上午10:13:55): <br>
	 */
	@Timed("报案新增")
	@RequestMapping(value = "/addRegist.do")
	@AvoidRepeatableCommit
	public String addRegist(Model model,PolicyInfoVo policyInfoVo) {
		
		SysUserVo userVo = WebUserUtils.getUser();
		PrpLRegistVo prpLRegistVo = new PrpLRegistVo();
		// 出险日期大于报案日期后台管控
		long damageTime = policyInfoVo.getDamageTime().getTime();
		long nowDate = new Date().getTime();
		model.addAttribute("reportDate",new Date());
		model.addAttribute("damageTime",policyInfoVo.getDamageTime());
		
		PrpLConfigValueVo configValueVoByMap = ConfigUtil.findConfigByCode(CodeConstants.SWITCHMAP);
		if(configValueVoByMap!=null&&"1".equals(configValueVoByMap.getConfigValue())){// 开关
			model.addAttribute("switchMap","1");// 地图的开关
		}
		// model.addAttribute("switchMap","1");//地图的开关
		
		List<PrpLCMainVo> prpLCMains = null;

		// TASK 报案跳转到旧理赔系统，根据上线切换 END
		// 根据配置dhic.newclaim.comcode判断是否启用了新理赔
		/*	if(policyInfoVo.getComCode()!=null){
				if(isUseNewClaim(policyInfoVo.getComCode())==false){
					throw new IllegalArgumentException("此保单为非上线机构保单，请进入旧理赔报案菜单登记报案！");
		//				String oldClaimUrl = SpringProperties.getProperty("dhic.oldclaim.url");
		//				Date damageTime = policyInfoVo.getDamageTime();
		//				String damageDate = DateUtils.dateToStr(damageTime,DateUtils.YToDay);
		//				String oldRegistUrl = oldClaimUrl+"/registBeforeEdit.do?prpCmainPolicyNo="+policyInfoVo.getPolicyNo()+
		//															"&editType=ADD&damageDate="+damageDate+"&damageHour="+damageTime.getHours();
		//				model.addAttribute("pageURL",oldRegistUrl);
		//				// 跳转到旧理赔
		//				return "regist/common/OpenOldClaim";//
					// return "redirect:"+oldRegistUrl; 后面这种方式比较好，但在呼叫中心显示不了
				}
			}*/

		if (StringUtils.equals(policyInfoVo.getTempRegistFlag(), "1")) {
			prpLRegistVo.setTempRegistFlag(CodeConstants.TempReport.TEMPREPORT);
			//prpLCMains = validateReportTimeAndInitPrpcmains(policyInfoVo.getPolicyNo(),policyInfoVo.getRelatedPolicyNo());
			prpLCMains = registTmpService.findTempPolicyByRegistNo(policyInfoVo.getRegistNo());
			
			/*PrpLCInsuredVo A = new PrpLCInsuredVo();
			A.setInsuredName(prpLCMains.get(0).getInsuredName());
			List<PrpLCInsuredVo>  PrpLCInsuredVo = new ArrayList<PrpLCInsuredVo>();
			PrpLCInsuredVo.add(A);*/
			for(PrpLCMainVo prpLCMain:prpLCMains){
				List<PrpLCInsuredVo>  PrpLCInsuredVo = new ArrayList<PrpLCInsuredVo>();
				PrpLCInsuredVo A = new PrpLCInsuredVo();
				A.setInsuredName(prpLCMain.getInsuredName());
				PrpLCInsuredVo.add(A);
				prpLCMain.setPrpCInsureds(PrpLCInsuredVo);
				//prpLCMain.getPrpCInsureds().get(0).setInsuredName(prpLCMain.getInsuredName());
			}
			//prpLCMains.get(0).setPrpCInsureds(PrpLCInsuredVo);
			/*prpLCMains.get(0).setPrpCInsureds(A);
			prpLCMains.get(0).getPrpCInsureds().get(0).setInsuredName(prpLCMains.get(0).getInsuredName());*/
			for (PrpLCMainVo mainVo : prpLCMains) {
				mainVo.setId(null);
				if (mainVo.getPrpCItemCars() != null && mainVo.getPrpCItemCars().size() > 0) {
					for (PrpLCItemCarVo carVo :mainVo.getPrpCItemCars()) {
						carVo.setItemCarId(null);
					}
				}
				if (mainVo.getPrpCItemKinds() != null && mainVo.getPrpCItemKinds().size() > 0) {
					for (PrpLCItemKindVo kindVo : mainVo.getPrpCItemKinds()) {
						kindVo.setItemKindId(null);
					}
				}
			}
			/*model.addAttribute("states", 1);*/
		} else {
			prpLCMains = new ArrayList<PrpLCMainVo>();
			// 调用存储过程
			if(StringUtils.isNotBlank(policyInfoVo.getPolicyNo())){
				PrpLCMainVo prpLCMainVo = prpLCMainService.findRegistPolicy(policyInfoVo.getPolicyNo(), policyInfoVo.getDamageTime());
				if(prpLCMainVo!=null){
					// 业务分类
					business(prpLCMainVo);
					prpLCMains.add(prpLCMainVo);						
				}
			}
			if(StringUtils.isNotBlank(policyInfoVo.getRelatedPolicyNo())){
				PrpLCMainVo prpLCMainVo = prpLCMainService.findRegistPolicy(policyInfoVo.getRelatedPolicyNo(), policyInfoVo.getDamageTime());
				if(prpLCMainVo!=null){
					// 业务分类
					business(prpLCMainVo);
					prpLCMains.add(prpLCMainVo);
				}
			}
			
			// 以下是之前的代码调用
			//prpLCMains = validateReportTimeAndInitPrpcmains(policyInfoVo.getPolicyNo(),policyInfoVo.getRelatedPolicyNo());
		}
		// 出险所在地的省市默认保单所在地
		String policyNo = "";
		if( !( policyInfoVo.getRelatedPolicyNo()!="undefined" )&&StringUtils.isNotBlank(policyInfoVo.getRelatedPolicyNo())&&StringUtils
				.isNotBlank(policyInfoVo.getPolicyNo())){// 取商业的保单
			if(policyInfoVo.getRelatedPolicyNo().substring(11, 13).equals("12")){
				//System.out.println("kkkk"+policyInfoVo.getRelatedPolicyNo().substring(11, 13));
				policyNo = policyInfoVo.getRelatedPolicyNo();
			} else {
				policyNo = policyInfoVo.getPolicyNo();
			}
			
		} else {

			if(!(policyInfoVo.getRelatedPolicyNo()!="undefined") && StringUtils.isNotBlank(policyInfoVo.getRelatedPolicyNo())){
				policyNo = policyInfoVo.getRelatedPolicyNo();
			}else{
				policyNo = policyInfoVo.getPolicyNo();
			}
		}
		List<PrpLCMainVo> vos = registTmpService.findAreadictByPolicyNo(policyNo);
		if(vos.size() > 0){
			String code = vos.get(0).getComCode().substring(0, 4)+"0000";
			// 根据机构代码查地区代码
			
			SysAreaDict po = areaDictService.findAreaByComCode(code,new BigDecimal(2));
			if(po!=null){
				prpLRegistVo.setDamageAreaCode(po.getAreaCode());
			}
		}
		// 报案登记界面不论有没有车辆损失，默认生成标的车信息
		List<PrpLRegistCarLossVo> prpLRegistCarLosses = new ArrayList<PrpLRegistCarLossVo>();
		PrpLRegistCarLossVo carVo = new PrpLRegistCarLossVo();
		prpLRegistCarLosses.add(carVo);
		//model.addAttribute("prpLRegistCarLosses", prpLRegistCarLosses);carSize
		model.addAttribute("carSize", "1");
		
		// 初始化带入人员伤亡信息
		List<PrpLRegistPersonLossVo> prpLRegistPersonLosses = new ArrayList<PrpLRegistPersonLossVo>();
		PrpLRegistPersonLossVo personVo = new PrpLRegistPersonLossVo();
		personVo.setInjuredcount(0);
		personVo.setDeathcount(0);
		personVo.setLossparty(CodeConstants.LossParty.TARGET);
		prpLRegistPersonLosses.add(personVo);
		personVo.setLossparty(CodeConstants.LossParty.THIRD);
		prpLRegistPersonLosses.add(personVo);
		model.addAttribute("prpLRegistPersonLosses", prpLRegistPersonLosses);
		
		prpLRegistVo.setDamageTime(policyInfoVo.getDamageTime());
		prpLRegistVo.setReportTime(new Date());
		prpLRegistVo.setFirstRegUserCode(WebUserUtils.getUserCode());
		prpLRegistVo.setFirstRegUserName(WebUserUtils.getUserName());
		prpLRegistVo.setRegistNo(policyInfoVo.getRegistNo());
		prpLRegistVo.setCallId(policyInfoVo.getCallId());
		
		// 获取方正客服系统报案人电话号码
		String telephone="";
	    try {
	    	/* 2088 关于核心车险报案页面自动带入客服系统来电号码功能取消申请 2020年5月12日14:36:51
	    	telephone=founderService.carRegistPhoneToFounder(); */
	    	prpLRegistVo.setReportorPhone(telephone);
	    	prpLRegistVo.setLinkerMobile(telephone);
		} catch (Exception e) {
			logger.info("报案获取方正客服系统报案人电话号码失败："+e.getMessage());
		}
		// 查询工作流状态
		String workState = wfTaskHandleService.findByRegistNoAndNode(prpLRegistVo, FlowNode.Regis.name());
		if("0".equals(workState)|| "1".equals(workState) || "2".equals(workState) || "6".equals(workState) || "A".equals(workState) || StringUtils.isBlank(workState)){
		model.addAttribute("handlerStatus","1");
		}else{
		model.addAttribute("handlerStatus","3");
		}
	    //获取联共保标志
	    if(prpLCMains!=null && !prpLCMains.isEmpty()){
	    	String[] plyNoArr = new String[prpLCMains.size()];
	    	for(int i=0;i<prpLCMains.size();i++){
	    		plyNoArr[i]=prpLCMains.get(i).getPolicyNo();
	    	}
	    	List<Map<String,String>> prpccoins = policyQueryService.findPrpCCoins(plyNoArr);
	    	if(prpccoins!=null && !prpccoins.isEmpty()){
	    		model.addAttribute("prpccoins",prpccoins);
	    	}
	    }
	    
		/**
		 * 承保推修手机号与修理厂推修手机号匹配 
		 */
		String serviceMobile="";//推修手机号，有商业取商业，没商业取交强
		Long repairId=null;//修理厂主表Id
		String BIPolicyNo="";
		String CIPolicyNo="";
		if(prpLCMains!=null && prpLCMains.size()>0){
			for(PrpLCMainVo mainVo:prpLCMains){
				if("1101".equals(mainVo.getRiskCode())){
					CIPolicyNo=mainVo.getPolicyNo();
				}else{
					BIPolicyNo=mainVo.getPolicyNo();
				}
			}
		}
		
	    if(StringUtils.isNotBlank(BIPolicyNo)){
	      serviceMobile=policyQueryService.findPrpCMian(BIPolicyNo);
	    }
	    if(StringUtils.isNotBlank(CIPolicyNo) && StringUtils.isBlank(serviceMobile)){
	      serviceMobile=policyQueryService.findPrpCMian(CIPolicyNo);
	    }
	    if(StringUtils.isNotBlank(serviceMobile)){
	      repairId=repairFactoryService.findRepairFactoryBy(serviceMobile);
	    }
	    model.addAttribute("serviceMobile",serviceMobile);
	    model.addAttribute("repairId",repairId);
		// 关联工单和新建工单
	    Boolean hasRegistTask = SaaPowerUtils.hasTask(userVo.getUserCode(),"claim.regist.query");
  		String URL = SpringProperties.getProperty("FOCUS_URL");
  		model.addAttribute("hasRegistTask",hasRegistTask);
		model.addAttribute("addCode_URLS",URL);
		model.addAttribute("queryCode_URLS",URL);
	    
	   
	    model.addAttribute("prpLRegistVo", prpLRegistVo);
		model.addAttribute("prpLCMains", prpLCMains);
		String subComCode = "";
		if(prpLCMains!=null&&prpLCMains.size()>0&&prpLCMains.get(0).getComCode()!=null){
			subComCode = prpLCMains.get(0).getComCode().substring(0, 2);
		}
		model.addAttribute("subComCode", subComCode);
		model.addAttribute("prpTempLCMain", this.setPrpTempLCMain(prpLCMains));
		String prpUrl = SpringProperties.getProperty("PRPCAR_URL");
		model.addAttribute("prpUrl",prpUrl);
		return "regist/registEdit/ReportEdit";
	}
	
	private boolean isUseNewClaim(String comCode) {
		String newClaimCom = SpringProperties.getProperty("dhic.newclaim.comcode");
		// 先匹配4未，再匹配两位
		if(newClaimCom.indexOf(comCode.substring(0,4)+";")> -1){
			return true;
		}
		if(newClaimCom.indexOf(comCode.substring(0,2)+";")> -1){
			return true;
		}
		return false;
	}

	@Timed("报案编辑")
	@RequestMapping(value = "/edit.do")
	public String edit(Model model,String registNo) {
		SysUserVo userVo = WebUserUtils.getUser();
		PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
		PrpLConfigValueVo configValueVoByMap = ConfigUtil.findConfigByCode(CodeConstants.SWITCHMAP);
		if(configValueVoByMap!=null&&"1".equals(configValueVoByMap.getConfigValue())){// 开关
			model.addAttribute("switchMap","1");// 地图的开关
		}
		// model.addAttribute("switchMap","1");//地图的开关
		List<PrpLClaimVo> clamimVos=claimService.findClaimListByRegistNo(registNo);
		String completeClaimFlag="0";//是否立案，0-未立案，1-已立案
		if(clamimVos!=null && clamimVos.size()>0){
			if(clamimVos.get(0).getClaimTime()!=null){
				completeClaimFlag="1";//已立案
			}
		}
		model.addAttribute("completeClaimFlag", completeClaimFlag);
		model.addAttribute("prpLRegistVo", prpLRegistVo);
		if(StringUtils.isNotBlank(prpLRegistVo.getWeekDayriskFlag()) && "1".equals(prpLRegistVo.getWeekDayriskFlag())) {
			model.addAttribute("weekDayriskFlag","1");
		}else {
			model.addAttribute("weekDayriskFlag","0");
		}
		
		model.addAttribute("prpLRegistExtVo", prpLRegistVo.getPrpLRegistExt());
		model.addAttribute("prpLRegistCarLosses", prpLRegistVo.getPrpLRegistCarLosses());
		model.addAttribute("prpLRegistPropLosses", prpLRegistVo.getPrpLRegistPropLosses());
		model.addAttribute("carSize", prpLRegistVo.getPrpLRegistCarLosses().size());
		
		Map<String,String> carNoMap = new HashMap<String,String>();
		carNoMap.put("0","地面");
		String licenseId = prpLRegistVo.getPrpLRegistCarLosses().get(0).getLicenseNo();
		String licenseNo = "标的车("+prpLRegistVo.getPrpLRegistCarLosses().get(0).getLicenseNo()+")";
		carNoMap.put(licenseId,licenseNo);
		// 放标的车车牌，显示
		model.addAttribute("selfLisenceNo", prpLRegistVo.getPrpLRegistCarLosses().get(0).getLicenseNo());
		// 拼装财产损失，损失方
		if (prpLRegistVo.getPrpLRegistCarLosses().size() > 1) {
			for (int i = 1; i<prpLRegistVo.getPrpLRegistCarLosses().size();i++){
				String key = prpLRegistVo.getPrpLRegistCarLosses().get(i).getLicenseNo();
				String value = "三者车("+prpLRegistVo.getPrpLRegistCarLosses().get(i).getLicenseNo()+")";
				carNoMap.put(key,value);
			}
		}
		model.addAttribute("carNoMap", carNoMap);

		if (prpLRegistVo.getPrpLRegistPersonLosses() == null ||
				prpLRegistVo.getPrpLRegistPersonLosses().size() == 0) {
			// 初始化带入人员伤亡信息
			List<PrpLRegistPersonLossVo> prpLRegistPersonLosses = new ArrayList<PrpLRegistPersonLossVo>();
			PrpLRegistPersonLossVo personVo = new PrpLRegistPersonLossVo();
			personVo.setInjuredcount(0);
			personVo.setDeathcount(0);
			personVo.setLossparty(CodeConstants.LossParty.TARGET);
			prpLRegistPersonLosses.add(personVo);
			personVo.setLossparty(CodeConstants.LossParty.THIRD);
			prpLRegistPersonLosses.add(personVo);
			model.addAttribute("prpLRegistPersonLosses", prpLRegistPersonLosses);
		} else {
			model.addAttribute("prpLRegistPersonLosses", prpLRegistVo.getPrpLRegistPersonLosses());
		}
		if(prpLRegistVo.getTempRegistFlag()!=null){// 正常报案为null；无保单报案为1，无保转有保后为0
			if(prpLRegistVo.getTempRegistFlag().equals("1")){
				// 临时保单
				List<PrpLCMainVo> prpLCMains = policyViewService.getPolicyForPrpLTmpCMain(registNo);
			/*	for (int i = 0; i < prpLCMains.size(); i ++) {
					if (StringUtils.equals(prpLCMains.get(i).getValidFlag(), CodeConstants.ValidFlag.INVALID)) {
						prpLCMains.remove(i);
						i --;
					}
				}*/
				/*PrpLCInsuredVo A = new PrpLCInsuredVo();
				A.setInsuredName(prpLCMains.get(0).getInsuredName());*/
				/*List<PrpLCInsuredVo>  PrpLCInsuredVo = new ArrayList<PrpLCInsuredVo>();
				PrpLCInsuredVo.add(A);*/
				/*prpLCMains.get(0).setPrpCInsureds(PrpLCInsuredVo);*/
				
			/*	if(prpLCMains != null && prpLCMains.size() > 0){
					List<PrpLCInsuredVo>  PrpLCInsuredVo = new ArrayList<PrpLCInsuredVo>();
					PrpLCInsuredVo A = new PrpLCInsuredVo();
					A.setInsuredName(prpLCMains.get(0).getInsuredName());
					PrpLCInsuredVo.add(A);
					prpLCMains.get(0).setPrpCInsureds(PrpLCInsuredVo);
				}*/
				for(PrpLCMainVo prpLCMain:prpLCMains){
					List<PrpLCInsuredVo>  PrpLCInsuredVo = new ArrayList<PrpLCInsuredVo>();
					PrpLCInsuredVo A = new PrpLCInsuredVo();
					A.setInsuredName(prpLCMain.getInsuredName());
					PrpLCInsuredVo.add(A);
					prpLCMain.setPrpCInsureds(PrpLCInsuredVo);
					//prpLCMain.getPrpCInsureds().get(0).setInsuredName(prpLCMain.getInsuredName());
				}
				
				model.addAttribute("prpLCMains",prpLCMains);
				model.addAttribute("prpTempLCMain", this.setPrpTempLCMain(prpLCMains));
			}else{
				List<PrpLCMainVo> prpLCMains = policyViewService.getPolicyAllInfo(registNo);
				for (int i = 0; i < prpLCMains.size(); i ++) {
					if (StringUtils.equals(prpLCMains.get(i).getValidFlag(), CodeConstants.ValidFlag.INVALID)) {
						prpLCMains.remove(i);
						i --;
					}else{
						business(prpLCMains.get(i));						
					}
				}
				model.addAttribute("prpLCMains",prpLCMains);
				String subComCode = "";
				if(prpLCMains!=null&&prpLCMains.size()>0&&prpLCMains.get(0).getComCode()!=null){
					subComCode = prpLCMains.get(0).getComCode().substring(0, 2);
				}
				model.addAttribute("subComCode", subComCode);
				model.addAttribute("prpTempLCMain", this.setPrpTempLCMain(prpLCMains));
				   //获取联共保标志
			    if(prpLCMains!=null && !prpLCMains.isEmpty()){
			    	String[] plyNoArr = new String[prpLCMains.size()];
			    	for(int i=0;i<prpLCMains.size();i++){
			    		plyNoArr[i]=prpLCMains.get(i).getPolicyNo();
			    	}
			    	List<Map<String,String>> prpccoins = policyQueryService.findPrpCCoins(plyNoArr);
			    	if(prpccoins!=null && !prpccoins.isEmpty()){
			    		model.addAttribute("prpccoins",prpccoins);
			    	}
			    }
			}
		}else{
			List<PrpLCMainVo> prpLCMains = policyViewService.getPolicyAllInfo(registNo);
			for (int i = 0; i < prpLCMains.size(); i ++) {
				if (StringUtils.equals(prpLCMains.get(i).getValidFlag(), CodeConstants.ValidFlag.INVALID)) {
					prpLCMains.remove(i);
					i --;
				}else{
					business(prpLCMains.get(i));						
				}
			}
			model.addAttribute("prpLCMains",prpLCMains);
			model.addAttribute("prpTempLCMain", this.setPrpTempLCMain(prpLCMains));
			   //获取联共保标志
		    if(prpLCMains!=null && !prpLCMains.isEmpty()){
		    	String[] plyNoArr = new String[prpLCMains.size()];
		    	for(int i=0;i<prpLCMains.size();i++){
		    		plyNoArr[i]=prpLCMains.get(i).getPolicyNo();
		    	}
		    	List<Map<String,String>> prpccoins = policyQueryService.findPrpCCoins(plyNoArr);
		    	if(prpccoins!=null && !prpccoins.isEmpty()){
		    		model.addAttribute("prpccoins",prpccoins);
		    	}
		    }
		}
/*		for (int i = 0; i < prpLCMains.size(); i ++) {
			if (StringUtils.equals(prpLCMains.get(i).getValidFlag(), CodeConstants.ValidFlag.INVALID)) {
				prpLCMains.remove(i);
				i --;
			}
		}
		model.addAttribute("prpLCMains",prpLCMains);*/
		if(wfFlowQueryService.isCheckNodeEnd(registNo)){// 查勘是否提交
			model.addAttribute("isCheckNodeEnd","1");
		}
		// 判断能不能注销
		List<PrpLCMainVo> prpLCMaines = policyViewService.getPolicyForPrpLTmpCMain(registNo);
	/*	for(PrpLCMainVo prpLCMain:prpLCMaines){
			vos=claimTaskService.findprpLClaimVoListByRegistAndPolicyNo(registNo, prpLCMain.getPolicyNo(), "0");
		}*/
		
		List<PrpLClaimVo> vos = claimTaskService.findClaimListByRegistNo(registNo);
		String flags = vos!=null&&vos.size()>0 ? "1" : "0";// 1,不能注销
		// 报案注销需在报案录入提交完24小时之后才能注销
		/*Date date=new   Date();//取时间 
		Date reportTime = prpLRegistVo.getReportTime();
		Calendar   calendar   =   new   GregorianCalendar(); 
		calendar.setTime(reportTime); 
		calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
		reportTime = calendar.getTime();   //这个时间就是日期往后推一天的结果 
		int i = reportTime.compareTo(date);//如果报案时间加一天小于今天时间返回负数，可以注销
		if(i < 0){
			flags = "0";
		}else{
			flags = "1";
		}*/
	    
		model.addAttribute("flags",flags);
		
		// 查询工作流状态
		String workState = wfTaskHandleService.findByRegistNoAndNode(prpLRegistVo, FlowNode.Regis.name());
		model.addAttribute("workState",workState);
		if("0".equals(workState)|| "1".equals(workState) || "2".equals(workState) || "6".equals(workState) || "A".equals(workState)){
		model.addAttribute("handlerStatus","1");
		}else{
	    model.addAttribute("handlerStatus","3");
		}
		
		// 已经立案不能注销
		List<PrpLClaimVo> prpLClaimVos = claimTaskService.findClaimListByRegistNo(registNo);
		if(prpLClaimVos!=null){
			if(prpLClaimVos.size() > 0){
				model.addAttribute("claim","1");
			}else{
				model.addAttribute("claim","0");
			}
		}else{
			model.addAttribute("claim","0");
		}
		// 脱敏处理
		if(CodeConstants.WorkStatus.END.equals(workState)){
			//reportorPhone
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);
			if(configValueVo!=null&&"1".equals(configValueVo.getConfigValue())){// 开关
				prpLRegistVo.setReportorPhone(DataUtils .replacePrivacy(prpLRegistVo.getReportorPhone()));
				prpLRegistVo.setDriverPhone(DataUtils .replacePrivacy(prpLRegistVo.getDriverPhone()));
				prpLRegistVo.setLinkerMobile(DataUtils .replacePrivacy(prpLRegistVo.getLinkerMobile()));
				prpLRegistVo.setInsuredPhone(DataUtils .replacePrivacy(prpLRegistVo.getInsuredPhone()));
				prpLRegistVo.setReportorIdfNo(DataUtils .replacePrivacy(prpLRegistVo.getReportorIdfNo()));
				prpLRegistVo.setDriverIdfNo(DataUtils .replacePrivacy(prpLRegistVo.getDriverIdfNo()));
				prpLRegistVo.setLinkerPhone(DataUtils .replacePrivacy(prpLRegistVo.getLinkerPhone()));
				
			}
			model.addAttribute("prpLRegistVo", prpLRegistVo);
		}else{
			model.addAttribute("prpLRegistVo", prpLRegistVo);
		}
		/**
		 * 承保推修手机号与修理厂推修手机号匹配
		 */
		List<PrpLCMainVo> prpLCMains = prpLCMainService.findPrpLCMainsByRegistNo(prpLRegistVo.getRegistNo());
		String serviceMobile="";//推修手机号，有商业取商业，没商业取交强
		Long repairId=null;//修理厂主表Id
		String BIPolicyNo="";
		String CIPolicyNo="";
		if(prpLCMains!=null && prpLCMains.size()>0){
			for(PrpLCMainVo mainVo:prpLCMains){
				if("1101".equals(mainVo.getRiskCode())){
					CIPolicyNo=mainVo.getPolicyNo();
				}else{
					BIPolicyNo=mainVo.getPolicyNo();
				}
			}
		}
		
	    if(StringUtils.isNotBlank(BIPolicyNo)){
	      serviceMobile=policyQueryService.findPrpCMian(BIPolicyNo);
	    }
	    if(StringUtils.isNotBlank(CIPolicyNo) && StringUtils.isBlank(serviceMobile)){
	      serviceMobile=policyQueryService.findPrpCMian(CIPolicyNo);
	    }
	    if(StringUtils.isNotBlank(serviceMobile)){
	      repairId=repairFactoryService.findRepairFactoryBy(serviceMobile);
	    }
	    model.addAttribute("serviceMobile",serviceMobile);
	    model.addAttribute("repairId",repairId);
		
		// 巨灾
		PrpLDisasterVo disasterVo = checkTaskService.findDisasterVoByRegistNo(registNo);
		
		// 关联工单和新建工单
		Boolean hasRegistTask = SaaPowerUtils.hasTask(userVo.getUserCode(),"claim.regist.query");
		String URL = SpringProperties.getProperty("FOCUS_URL");
		model.addAttribute("hasRegistTask",hasRegistTask);
		model.addAttribute("addCode_URLS",URL);
		model.addAttribute("queryCode_URLS",URL);
		
		model.addAttribute("disasterVo",disasterVo);
		String prpUrl = SpringProperties.getProperty("PRPCAR_URL");
		model.addAttribute("prpUrl",prpUrl);
		return "regist/registEdit/ReportEdit";
	}

	private List<PrpLCMainVo> validateReportTimeAndInitPrpcmains(String BIPolicyNo,String CIPolicyNo) {
		List<String> policyNoList = new ArrayList<String>();
		if(StringUtils.isNotBlank(BIPolicyNo)){
			policyNoList.add(BIPolicyNo);
			//policyQueryService.findValidNo(BIPolicyNo);
		}
		if(StringUtils.isNotBlank(CIPolicyNo)){
			policyNoList.add(CIPolicyNo);
			//policyQueryService.findValidNo(CIPolicyNo);
		}
		
		
		List<PrpLCMainVo> cmainList = policyQueryService.findPrpcMainByPolicyNos(policyNoList);
		
		return cmainList;
	}

	/**
	 * 保存暂存报案信息
	 * @param policyNo
	 * @return
	 * @throws java.text.ParseException
	 * @modified: ☆LiuPing(2015年11月17日 上午10:21:49): <br>
	 */
	@Timed("报案暂存/保存")
	@RequestMapping(value = "/save.do")
	@ResponseBody
	public AjaxResult save(String BIPolicyNo, String CIPolicyNo,String CIComCode, String BIComCode,String CIRiskCode, String BIRiskCode,
			@FormModel(value = "prpLRegistVo") PrpLRegistVo prpLRegistVo,
			@FormModel(value = "prpLRegistExtVo") PrpLRegistExtVo prpLRegistExt,
			@FormModel(value = "prpLRegistCarLosses") List<PrpLRegistCarLossVo> prpLRegistCarLosses,
			@FormModel(value = "prpLRegistPersonLosses") List<PrpLRegistPersonLossVo> prpLRegistPersonLosses,
			@FormModel(value = "prpLRegistPropLosses") List<PrpLRegistPropLossVo> prpLRegistPropLosses,
			@FormModel("disasterVo") PrpLDisasterVo disasterVo) throws java.text.ParseException {
		
		AjaxResult ajaxResult = new AjaxResult();
		try{
		long start = System.currentTimeMillis();
	
			// 后台校验，当后台获取出险地址为空的是抛出异常
		if(prpLRegistVo.getDamageAddress()==null || "".equals(prpLRegistVo.getDamageAddress())
				|| prpLRegistVo.getDamageAreaCode()==null || "".equals(prpLRegistVo.getDamageAreaCode())){
				throw new IllegalArgumentException("获取不到出险地址!");
		}
		
			// 判断出险原因是否选其他，如果是进行设值
		if(prpLRegistVo.getDamageCode()!=null && prpLRegistVo.getDamageCode()!=""){
			if(prpLRegistVo.getDamageCode().equals("DM99")){
			}else{
				prpLRegistVo.setDamageOtherCode(null);
			}
		}
		
			// 是否已暂存标志 false-已暂存， true-新增
		boolean flag = false;
		if (StringUtils.isEmpty(prpLRegistVo.getRegistNo()) 
				|| StringUtils.isEmpty(prpLRegistVo.getRegistTaskFlag()) ) {
			flag = true;
		}
		if (prpLRegistExt.getLicenseNo() != null && prpLRegistCarLosses != null && prpLRegistCarLosses.size() > 0) {
			prpLRegistExt.setLicenseNo(prpLRegistCarLosses.get(0).getLicenseNo());
		}
		
		prpLRegistVo.setPrpLRegistExt(prpLRegistExt);
		for(PrpLRegistCarLossVo vo: prpLRegistCarLosses){
			logger.info("车牌号: " + vo.getLicenseNo() + " --- createuser: " + vo.getCreateUser());
			if(vo!=null){
					if( !"其他".equals(vo.getLoss())){
					vo.setLossremark(null);
				}
			}
		}
		
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@==1000====="+(System.currentTimeMillis()-start));
		start = System.currentTimeMillis();
		
		
		prpLRegistVo.setPrpLRegistCarLosses(prpLRegistCarLosses);
		prpLRegistVo.setPrpLRegistPersonLosses(prpLRegistPersonLosses);
		if(prpLRegistPropLosses.size() > 0 && prpLRegistPropLosses.get(0).getLossitemname()!=null&&prpLRegistPropLosses.get(0).getLossitemname()!=""){
			 prpLRegistVo.setPrpLRegistPropLosses(prpLRegistPropLosses);
		 }
			// 单交强报案时，只录入了标的人伤没有三者人伤，允许报案提交，但是这个标的人伤不带到调度。
		
		if(!StringUtils.isNotBlank(BIPolicyNo)&&StringUtils.isNotBlank(CIPolicyNo)){
				/*	if(prpLRegistCarLosses.size()==1){
						for(PrpLRegistPropLossVo vo : prpLRegistPropLosses){
							if(!"1".equals(vo.getLossparty())){//不是标的财
								prpLRegistVo.setIsPeopleflag("0");//但是这个标的人伤不带到调度。1不带，0带
							}
						}
						if(prpLRegistPersonLosses.get(1).getInjuredcount() > 0 || 
								prpLRegistPersonLosses.get(1).getDeathcount() > 0){
							prpLRegistVo.setIsPeopleflag("0");//但是这个标的人伤不带到调度。
						}
					}else{
						prpLRegistVo.setIsPeopleflag("0");//但是这个标的人伤不带到调度。
					}*/
			if(prpLRegistPersonLosses!=null && prpLRegistPersonLosses.size()>1){
				if(prpLRegistPersonLosses.get(1).getInjuredcount() > 0 || 
						prpLRegistPersonLosses.get(1).getDeathcount() > 0){
						prpLRegistVo.setIsPeopleflag("0");// 带
				}else{
						prpLRegistVo.setIsPeopleflag("1");// 但是这个标的人伤不带到调度。
				}
			}else{
					prpLRegistVo.setIsPeopleflag("0");// 带
			}
			
		}else{
				prpLRegistVo.setIsPeopleflag("0");// 带
		}
		
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@==1010====="+(System.currentTimeMillis()-start));
		start = System.currentTimeMillis();
		
			// 校验必须有勾选的保单
		if(StringUtils.isEmpty(BIPolicyNo) && StringUtils.isEmpty(CIPolicyNo)){
				throw new IllegalArgumentException("所选保单信息有误");
		}
		if("1".equals(prpLRegistExt.getIsSubRogation())){
			validSubrogation(BIPolicyNo);
		}
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@==1020====="+(System.currentTimeMillis()-start));
		start = System.currentTimeMillis();
		
		
			// 给报案信息表赋值保单号，主表优先保存商业保单号，如果没有商业险保单则存交强险保单号，关联报案时，报案扩展表存储交强保单，否则不存
		if (!StringUtils.isEmpty(BIPolicyNo)) {
			prpLRegistVo.setPolicyNo(BIPolicyNo);
			if (!StringUtils.isEmpty(CIPolicyNo)) {
				prpLRegistExt.setPolicyNoLink(CIPolicyNo);
			}
		} else {
			prpLRegistVo.setPolicyNo(CIPolicyNo);
		}
		
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@==1030====="+(System.currentTimeMillis()-start));
		start = System.currentTimeMillis();
		
		List<PrpLCMainVo> prpLCMains = null;
			// 无保单报案
		if (StringUtils.equals(prpLRegistVo.getTempRegistFlag(), CodeConstants.TempReport.TEMPREPORT)) {
			prpLCMains = registTmpService.findTempPolicyByRegistNo(prpLRegistVo.getRegistNo());
			for (PrpLCMainVo mainVo : prpLCMains) {
				mainVo.setId(null);
				if (mainVo.getPrpCItemCars() != null && mainVo.getPrpCItemCars().size() > 0) {
					for (PrpLCItemCarVo carVo :mainVo.getPrpCItemCars()) {
						carVo.setItemCarId(null);
					}
				}
				if (mainVo.getPrpCItemKinds() != null && mainVo.getPrpCItemKinds().size() > 0) {
					for (PrpLCItemKindVo kindVo : mainVo.getPrpCItemKinds()) {
						kindVo.setItemKindId(null);
					}
				}
			}
				// 更新临时保单
				if(StringUtils.isEmpty(BIPolicyNo)){// 删除商业
				registTmpService.deleteByRegistNoAndRiskCode(prpLRegistVo.getRegistNo(), "12");
			}
				if(StringUtils.isEmpty(CIPolicyNo)){// 删除交强
				registTmpService.deleteByRegistNoAndRiskCode(prpLRegistVo.getRegistNo(), "11");
			}
			prpLRegistVo = registHandlerService.save(prpLRegistVo, prpLCMains, flag,BIPolicyNo,CIPolicyNo);
			logger.info("@@@@@@@@@@@@@@@@@@@@@@@==1040====="+(System.currentTimeMillis()-start));
			start = System.currentTimeMillis();

		} else {
				// 根据页面传回的保单号，查询保单信息，并处理生成报案时保单信息
			
		//	prpLCMains = validateReportTimeAndInitPrpcmains(BIPolicyNo,CIPolicyNo);
			
			List<PrpLCMainVo> prpLCMain = new ArrayList<PrpLCMainVo>();
			if (!StringUtils.isEmpty(BIPolicyNo)) {
				PrpLCMainVo Vo = new PrpLCMainVo();
				Vo.setRiskCode(BIRiskCode);
				Vo.setComCode(BIComCode);
				Vo.setPolicyNo(BIPolicyNo);
				prpLCMain.add(Vo);
			}
			if (!StringUtils.isEmpty(CIPolicyNo)) {
				PrpLCMainVo Vo = new PrpLCMainVo();
				Vo.setRiskCode(CIRiskCode);
				Vo.setComCode(CIComCode);
				Vo.setPolicyNo(CIPolicyNo);
				prpLCMain.add(Vo);
			}
			prpLRegistVo = registHandlerService.save(prpLRegistVo, prpLCMain, flag,BIPolicyNo,CIPolicyNo);
			
			logger.info("@@@@@@@@@@@@@@@@@@@@@@@==1040====="+(System.currentTimeMillis()-start));
			start = System.currentTimeMillis();
		}
		
		
		
		/*prpLCMains.get(0).getPrpCItemCars().get(0).getCarKindCode()
		prpLRegistVo.getPrpLRegistCarLosses().get(0).getLicenseNo()*/
			// 如果是报案新增，设置初始值
		//supplementInfo(prpLRegistVo, prpLCMains);
		
		//prpLRegistVo = registHandlerService.save(prpLRegistVo, prpLCMain, flag,BIPolicyNo,CIPolicyNo);
			// 保存案件理赔免赔条件
		registService.saveDeductCond(prpLRegistVo.getRegistNo());
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@==1050====="+(System.currentTimeMillis()-start));
		start = System.currentTimeMillis();
			// 巨灾
		if(StringUtils.isNotBlank(disasterVo.getDisasterCodeOne()) ||
				StringUtils.isNotBlank(disasterVo.getDisasterCodeTwo())){
			Date nowDate = new Date();
			String userCode = WebUserUtils.getUserCode();
			if(disasterVo.getId() == null){
				disasterVo.setRegistNo(prpLRegistVo.getRegistNo());
				disasterVo.setPolicyNo(prpLRegistVo.getPolicyNo());
				disasterVo.setCreateTime(nowDate);
				disasterVo.setCreateUser(userCode);
				disasterVo.setValidFlag("1");
				disasterVo.setUpdateTime(nowDate);
				disasterVo.setUpdateUser(userCode);
			}else{
				disasterVo.setUpdateTime(nowDate);
				disasterVo.setUpdateUser(userCode);
			}
			checkTaskService.saveDisasterVo(disasterVo);
			logger.info("@@@@@@@@@@@@@@@@@@@@@@@==1060====="+(System.currentTimeMillis()-start));
			start = System.currentTimeMillis();
			}else{// 如果都为空 巨灾有数据就删除
			PrpLDisasterVo prpLDisasterVo = checkTaskService.findDisasterVoByRegistNo(prpLRegistVo.getRegistNo());
			if(prpLDisasterVo != null && prpLDisasterVo.getId() != null){
				checkTaskService.deleteDisasterVo(prpLDisasterVo.getId());
			}
			logger.info("@@@@@@@@@@@@@@@@@@@@@@@==1060====="+(System.currentTimeMillis()-start));
			start = System.currentTimeMillis();
		}
		
			// 更新工作流的标的车车牌号
		List<PrpLCMainVo> prpLCMaines = policyViewService.getPolicyAllInfo(prpLRegistVo.getRegistNo());
		if(prpLCMaines!=null&&prpLCMaines.size()>0){
			//prpLRegistVo.setRegistNo(prpLCMaines.get(0).getRegistNo());
			//prpLRegistVo.getPrpLRegistExt().setLicenseNo(prpLCMaines.get(0).getPrpCItemCars().get(0).getLicenseNo());
			for(PrpLCMainVo vos:prpLCMaines){
				for(PrpLCItemCarVo vo:vos.getPrpCItemCars()){
					if(vo.getLicenseNo()==null){
						vo.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
						prpLCMainService.updateItemCar(vo);
					}
				}
			}
//			prpLCMainService.saveOrUpdate(prpLCMaines, prpLRegistVo.getRegistNo());
		}
		
			// 写入假日翻倍案件标志
		if(!"1".equals(prpLRegistVo.getWeekDayriskFlag())){
		if(prpLRegistVo.getDamageTime()!=null && StringUtils.isNotBlank(prpLRegistVo.getRegistNo())){
		boolean  festivalFlag=validateFestivalRisk(prpLRegistVo.getDamageTime(),prpLRegistVo.getRegistNo());
		if(festivalFlag){
			prpLRegistVo.setWeekDayriskFlag("1");
			registService.updatePrpLRegist(prpLRegistVo);
			List<PrpLCMainVo> cmainVoList=prpLCMainService.findPrpLCMainsByRegistNo(prpLRegistVo.getRegistNo());
			PrpLCItemKindVo kindVo=null;
			if(cmainVoList!=null && cmainVoList.size()>0){
				for(PrpLCMainVo vo:cmainVoList){
					if(!"1101".equals(vo.getRiskCode())){
						List<PrpLCItemKindVo> citemKindList=vo.getPrpCItemKinds();
						if(citemKindList!=null && citemKindList.size()>0){
							for(PrpLCItemKindVo citemvo:citemKindList){
								if("B".equals(citemvo.getKindCode())){
									kindVo=citemvo;
									break;
								}
							}
						}
					}
					
					
				}
			}
						// 三者险限额翻倍
			if(kindVo!=null){
				kindVo.setAmount(kindVo.getAmount().add(kindVo.getAmount()));
				prpLCMainService.updateItemKind(kindVo);
			}
		}
		}
		}
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@==1070====="+(System.currentTimeMillis()-start));
		start = System.currentTimeMillis();
			// 首次保存需提交工作流
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(prpLRegistVo.getFlowId());
		submitVo.setTaskInKey(prpLRegistVo.getRegistNo());
		submitVo.setFlowTaskId(BigDecimal.ZERO);

		if(CodeConstants.TempReport.TEMPREPORT.equals(prpLRegistVo.getTempRegistFlag())){
			submitVo.setComCode(WebUserUtils.getComCode());
		}else{
			submitVo.setComCode(policyViewService.getPolicyComCode(prpLRegistVo.getRegistNo()));
		}
			submitVo.setAssignUser("ANYONE");// 报案案件查询特殊处理,taskIn表assignUser和handlerUser赋值为ANYONE,允许全部报案权限工号查询
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setAssignCom(WebUserUtils.getComCode());
			submitVo.setSubmitType(flag ? SubmitType.TMP : SubmitType.U);// 修改报案工作流信息
			// 提交工作流
		wfTaskHandleService.submitRegist(prpLRegistVo,submitVo);
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@==1080====="+(System.currentTimeMillis()-start));
		start = System.currentTimeMillis();
		logger.info("@@@@@@@@@@@@@@@@@@@@@@@==1090====="+(System.currentTimeMillis()-start));
		//logger.info("====fffffffffffffff=="+"020-123456789".replace("-", ""));
		start = System.currentTimeMillis();
			/*if (flag) {
				WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
				submitVo.setFlowId(prpLRegistVo.getFlowId());
				submitVo.setTaskInKey(prpLRegistVo.getRegistNo());
				submitVo.setFlowTaskId(BigDecimal.ZERO);
				submitVo.setComCode(SecurityUtils.getComCode());
				submitVo.setAssignUser(SecurityUtils.getUserCode());
				submitVo.setTaskInUser(SecurityUtils.getUserCode());
				submitVo.setAssignCom(SecurityUtils.getComCode());
				submitVo.setSubmitType(SubmitType.TMP);
				//提交工作流
				PrpLWfTaskVo taskVo = wfTaskHandleService.submitRegist(prpLRegistVo,submitVo);
			}else{//修改报案工作流信息
				WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
				submitVo.setFlowId(prpLRegistVo.getFlowId());
				submitVo.setTaskInKey(prpLRegistVo.getRegistNo());
				submitVo.setFlowTaskId(BigDecimal.ZERO);
				submitVo.setComCode(SecurityUtils.getComCode());
				submitVo.setAssignUser(SecurityUtils.getUserCode());
				submitVo.setTaskInUser(SecurityUtils.getUserCode());
				submitVo.setAssignCom(SecurityUtils.getComCode());
				submitVo.setSubmitType(SubmitType.U);
				//提交工作流
				PrpLWfTaskVo taskVo = wfTaskHandleService.submitRegist(prpLRegistVo,submitVo);
			}*/
		
		logger.debug(prpLRegistVo.getRegistNo());
		
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		ajaxResult.setData(prpLRegistVo.getRegistNo());
		}catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}

	/**
	 * 校验代位求偿 未勾选商业险保单或保单未承保商业车损险，选择是否代为求偿为是时，系统提示“本车无车损险，不能选择代位求偿！”
	 * @param bIPolicyNo
	 * @modified: ☆YangKun(2016年4月12日 上午11:29:04): <br>
	 */
	private void validSubrogation(String bIPolicyNo) {
		Boolean iskindA = false;
		if(!StringUtils.isEmpty(bIPolicyNo)){
			List<String> policyNoList = new ArrayList<String>();
			policyNoList.add(bIPolicyNo);
			List<PrpLCMainVo> prpCmainVo = policyQueryService.findPrpcMainByPolicyNos(policyNoList);
			for(PrpLCItemKindVo itemKindVo : prpCmainVo.get(0).getPrpCItemKinds()){
				if("A".equals(itemKindVo.getKindCode()) || "A1".equals(itemKindVo.getKindCode())){
					iskindA =true;
					break;
				}
			}
		}
		if(!iskindA){
			throw new IllegalArgumentException("本车无车损险，不能选择代位求偿！");
		}
		
	}
	

	/**
	 * 报案登记新增时，初始化信息
	 * @param prpLRegist
	 * @param prpLCMains
	 */
	private void supplementInfo(PrpLRegistVo prpLRegist, List<PrpLCMainVo> prpLCMains) {
		if (StringUtils.isEmpty(prpLRegist.getRegistNo())
				|| StringUtils.isEmpty(prpLRegist.getRegistTaskFlag())) {
			// 初登人员和机构
			prpLRegist.setFirstRegUserCode(WebUserUtils.getUserCode());
			prpLRegist.setFirstRegComCode(WebUserUtils.getComCode());
			prpLRegist.setFirstRegUserName("初登姓名");
			// 创建人和时间
			prpLRegist.setCreateUser(WebUserUtils.getUserCode());
			prpLRegist.setCreateTime(new Date());
			// 设置案件紧急程度
			if (StringUtils.equals(prpLRegist.getPrpLRegistExt().getCheckType(), "3")) {
				prpLRegist.setMercyFlag(CodeConstants.CaseTag.EMERGENCY);
			} else {
				prpLRegist.setMercyFlag(CodeConstants.CaseTag.NORMAL);
			}
			// 迭代保单信息
			if (prpLCMains != null && prpLCMains.size() > 0) {
				for (PrpLCMainVo vo : prpLCMains) {
					if ( StringUtils.equals(vo.getPolicyNo(),prpLRegist.getPolicyNo()) ) {
						prpLRegist.setComCode(vo.getComCode());
						prpLRegist.setRiskCode(vo.getRiskCode());
					}
					vo.setValidFlag(CodeConstants.ValidFlag.VALID);
					vo.setCreateUser(WebUserUtils.getUserCode());
					vo.setCreateTime(new Date());
					vo.setUpdateUser(WebUserUtils.getUserCode());
					vo.setUpdateTime(new Date());
				}
			}
		}
		
		
		prpLRegist.setUpdateUser(WebUserUtils.getUserCode());
		prpLRegist.setUpdateTime(new Date());
		prpLRegist.getPrpLRegistExt().setUpdateUser(WebUserUtils.getUserCode());
		prpLRegist.getPrpLRegistExt().setUpdateTime(new Date());
		
		List<PrpLRegistCarLossVo> carList = prpLRegist.getPrpLRegistCarLosses();
		// 迭代车辆信息,设置初始值
		if (carList != null && carList.size() > 0) {
			for (int i = 0; i < carList.size(); i ++) {
				PrpLRegistCarLossVo vo = carList.get(i);
				if (vo != null) {
					String brand = vo.getBrand();
					String address = prpLRegist.getPrpLRegistExt().getCheckAddressCode();
					this.setRepairFactory(vo,brand,address,prpLRegist.getRegistNo());
					if (StringUtils.isEmpty(vo.getCreateUser())) {
						vo.setCreateUser(WebUserUtils.getUserCode());
						vo.setCreateTime(new Date());
					}
//					if(vo.getLossparty().equals("1")){
//						vo.setLicenseType(prpLCMains.get(0).getPrpCItemCars().get(0).getLicenseKindCode());
//					}
					vo.setUpdateUser(WebUserUtils.getUserCode());
					vo.setUpdateTime(new Date());
					// 默认将界面驾驶员信息写入车辆损失表中
					if (i == 0) {
						vo.setThriddrivername(prpLRegist.getDriverName());
						vo.setThriddriverphone(prpLRegist.getDriverPhone());
						vo.setThriddrivingno(prpLRegist.getDriverIdfNo());
					}
				} else {
					carList.remove(i);
					i --;
				}
			}
		} else {
			prpLRegist.getPrpLRegistExt().setIsCarLoss("0");
		}
		
		List<PrpLRegistPropLossVo> propList = prpLRegist.getPrpLRegistPropLosses();
		// 迭代物损信息，设置初始值
		if (propList != null && propList.size() > 0) {
			for (int i = 0; i < propList.size(); i ++) {
				PrpLRegistPropLossVo vo = propList.get(i);
				if (vo != null) {
					if (StringUtils.isEmpty(vo.getCreateUser())) {
						vo.setCreateUser(WebUserUtils.getUserCode());
						vo.setCreateTime(new Date());
					}
					vo.setUpdateUser(WebUserUtils.getUserCode());
					vo.setUpdateTime(new Date());
				} else {
					propList.remove(i);
					i --;
				}
			}
		} else {
			prpLRegist.getPrpLRegistExt().setIsPropLoss("0");
		}
		
		List<PrpLRegistPersonLossVo> personList = prpLRegist.getPrpLRegistPersonLosses();
		// 迭代人伤信息，设置初始值
		if (personList != null && personList.size() > 0) {
			boolean flag = true;
			for (PrpLRegistPersonLossVo vo : personList) {
				if (vo != null) {
					if (vo.getInjuredcount() > 0 || vo.getDeathcount() > 0) {
						flag = false;
					}
					if (StringUtils.isEmpty(vo.getCreateUser())) {
						vo.setCreateUser(WebUserUtils.getUserCode());
						vo.setCreateTime(new Date());
					}
					vo.setUpdateUser(WebUserUtils.getUserCode());
					vo.setUpdateTime(new Date());
				}
			}
			/*if (flag) {
				prpLRegist.getPrpLRegistExt().setIsPersonLoss("0");
			}*/
		}
	}
	
	
	/**
	 * 推送修,标的车通过承保的车辆品牌去对应修理厂,三者车页面选择
	 * @param PrpLRegistCarLossVo--报案号车辆信息
	 * @param brandName--车辆品牌名称
	 * @param address--出险地址
	 * @modified: ☆Luwei(2016年6月23日 下午12:51:32): <br>
	 */
	public void setRepairFactory(PrpLRegistCarLossVo vo,String brandName,
	       String address,String registNo){
		// 查询标的车的品牌
		if(StringUtils.isEmpty(brandName)&&"1".equals(vo.getLossparty())){
			brandName = registHandlerService.queryCarModel(registNo);
		}
		vo.setBrand(brandName);
		// 查询出该出险地区所有修理厂
		List<PrpLRepairFactoryVo> list = null;
		list = managerService.findFactoryByArea(address,brandName);
		List<PrpLRepairFactoryVo> listTemp = new ArrayList<PrpLRepairFactoryVo>();
		if(list!=null&&!list.isEmpty()){
			for(PrpLRepairFactoryVo repairVo : list){
				String flag = repairVo.getPreferredFlag();
				if(CodeConstants.RadioValue.RADIO_YES.equals(flag)){
					listTemp.add(repairVo);
				}
			}
		}
		if(listTemp != null && listTemp.size() > 0){
			// 随机一条修理厂信息
			int index=(int)(Math.random()*listTemp.size());
			PrpLRepairFactoryVo factory = listTemp.get(index);
			vo.setRepairCode(factory.getId().toString());
			vo.setRepairName(factory.getFactoryName());
			if(factory.getMobile() != null && factory.getMobile() != ""){
				vo.setRepairMobile(factory.getMobile());
			}else{
				vo.setRepairMobile(factory.getTelNo());
			}
			vo.setRepairAddress(factory.getAddress());
			vo.setRepairLinker(factory.getLinker());
		}
		if(listTemp.size()==0&&list!=null&&list.size()>0){
			// 随机一条修理厂信息
			int index=(int)(Math.random()*list.size());
			PrpLRepairFactoryVo factory = list.get(index);
			vo.setRepairCode(factory.getId().toString());
			vo.setRepairName(factory.getFactoryName());
			if(factory.getMobile() != null && factory.getMobile() != ""){
				vo.setRepairMobile(factory.getMobile());
			}else{
				vo.setRepairMobile(factory.getTelNo());
			}
			vo.setRepairAddress(factory.getAddress());
			vo.setRepairLinker(factory.getLinker());
		}
	}
	
	// ---------------------------------------下面的代码为Action参考示例----------------------------------------------
	/**
	 * 用户注册
	 * @param sysUser
	 * @param safecode
	 * @param session
	 * @return
	 * @modified: ☆LiuPing(2015年11月17日 上午10:04:13): <br>
	 */
	@RequestMapping(value = "/userRegister.do", method = RequestMethod.POST)
	public ModelAndView userRegister(@ModelAttribute("sysUser") @Valid SysUserVo sysUser,
										@RequestParam("safecode") String safecode,HttpSession session) {
		// 参数校验
		Assert.hasText(sysUser.getUserCode(),"用户名不能为空");
		Assert.hasText(sysUser.getPassword(),"密码不为空");
		Assert.hasText(sysUser.getUserName(),"真实姓名不能为空");
		Assert.hasText(sysUser.getEmail(),"邮箱地址不能为空");
		String imageCode = (String)session.getAttribute("imageCode");
		// 返回处理结果
		ModelAndView mav = new ModelAndView();
		if(safecode==null||safecode.equals("")|| !safecode.equals(imageCode)){
			String message = "验证码不正确，请重新输入";
			mav.addObject("message",message);
			mav.addObject("sysUser",sysUser);
			mav.setViewName("/sysUser/Register");
		}else{
			// sysUserService.save(sysUser);
			mav.setViewName("/index/login");
		}

		return mav;
	}
	
	/**
	 * 跳转到无保转有保
	 * @param policyNo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/noPolicyFindNoList.do")
	public String noPolicyFindNoList(String registNo, Model model) {
		
		PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
		PolicyInfoVo policyInfoVo = new PolicyInfoVo();
		policyInfoVo.setDamageTime(registVo.getDamageTime());
		
		model.addAttribute("policyInfoVo", policyInfoVo);
		model.addAttribute("registNo", registNo);
		return "regist/registEdit/ReportEdit_NoPolicyFindNo";
	}
	
	/**
	 * 无保转有保处理，并返回报案编辑界面
	 * @param registNo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/noPolicyFindNoConfirm.do")
	public String noPolicyFindNoConfirm(String registNo, String policyNo, String relatedPlyNo, Model model) {
		
		PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
		// 多保单关联刷新界面
		if(relatedPlyNo==null||relatedPlyNo==""){
			List<PrpLCMainVo> prpLCMains = policyViewService.getPolicyAllInfo(registNo);
			if(prpLCMains!=null&&prpLCMains.size()==2){
				if("1101".equals(prpLCMains.get(0).getRiskCode())){
					relatedPlyNo = prpLCMains.get(0).getPolicyNo();
					policyNo = prpLCMains.get(1).getPolicyNo();
				}else{
					policyNo=prpLCMains.get(0).getPolicyNo();
					relatedPlyNo=prpLCMains.get(1).getPolicyNo();
				}
					
			}
		}
		List<PrpLCMainVo> prpLCMains = validateReportTimeAndInitPrpcmains(policyNo,relatedPlyNo);
		if(prpLCMains.size() >0 ){
			registVo.getPrpLRegistExt().setLicenseNo(prpLCMains.get(0).getPrpCItemCars().get(0).getLicenseNo());
		}
		// 判断是否删除立案节点
		// flags为1要创建立案
		logger.info("policyNo========================================================="+policyNo);
		logger.info("relatedPlyNo========================================================="+relatedPlyNo);
		if(policyNo==null || "".equals(policyNo) || relatedPlyNo==null || "".equals(relatedPlyNo)){
			if(policyNo==null|| "".equals(policyNo) ){
				logger.info(relatedPlyNo.substring(11, 13));
				if(relatedPlyNo.substring(11, 13).equals("11")){
					// 删除商业
					BigDecimal flags = wfTaskHandleService.deleteByRegists(registVo,"ClaimBI");
					if(flags!=null){
						this.setSubmits(registVo,relatedPlyNo,flags);
					}
					logger.info("删除商业policyNo========================================================="+policyNo);
					logger.info("删除商业relatedPlyNo========================================================="+relatedPlyNo);
				}else{// 删除交强
					//wfTaskHandleService.deleteByRegists(registVo,"ClaimCI");
					BigDecimal flags = wfTaskHandleService.deleteByRegists(registVo,"ClaimCI");
					if(flags!=null){
						this.setSubmits(registVo,relatedPlyNo,flags);
					}
					logger.info("删除交强policyNo========================================================="+policyNo);
					logger.info("删除交强relatedPlyNo========================================================="+relatedPlyNo);
				}
			}else{
				logger.info("删除商业policyNo========================================================="+policyNo);
				logger.info("删除商业relatedPlyNo========================================================="+relatedPlyNo);
				logger.info(policyNo.substring(11, 13));
				if(policyNo.substring(11, 13).equals("11")){
					
					//wfTaskHandleService.deleteByRegists(registVo,"ClaimBI");
					// 删除商业
					BigDecimal flags = wfTaskHandleService.deleteByRegists(registVo,"ClaimBI");
					if(flags!=null){
						this.setSubmits(registVo,policyNo,flags);
					}
					logger.info("删除商业policyNo========================================================="+policyNo);
					logger.info("删除商业relatedPlyNo========================================================="+relatedPlyNo);
				}else{// 删除交强
					logger.info("删除交强policyNo========================================================="+policyNo);
					logger.info("删除交强relatedPlyNo========================================================="+relatedPlyNo);
					//wfTaskHandleService.deleteByRegists(registVo,"ClaimCI");
					BigDecimal flags = wfTaskHandleService.deleteByRegists(registVo,"ClaimCI");
					if(flags!=null){
						this.setSubmits(registVo,policyNo,flags);
					}
					logger.info("删除交强policyNo========================================================="+policyNo);
					logger.info("删除交强relatedPlyNo========================================================="+relatedPlyNo);
				}
			}
		}
		//registVo.setComCode(policyViewService.getPolicyComCode(registVo.getRegistNo()));
		registHandlerService.noPolicyFindNoConfirm(registVo, prpLCMains);
		
		registVo = registService.findRegistByRegistNo(registNo);
		registVo.setComCode(policyViewService.getPolicyComCode(registVo.getRegistNo()));
		registVo = registService.saveOrUpdate(registVo);
		// 无保转有保更新工作流
		// 更新保单
		//registVo.setPolicyNo(policyNo);
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(registVo.getFlowId());
		submitVo.setTaskInKey(registVo.getRegistNo());
		submitVo.setFlowTaskId(BigDecimal.ZERO);
		submitVo.setComCode(policyViewService.getPolicyComCode(registVo.getRegistNo()));
		//submitVo.setComCode(WebUserUtils.getComCode());
		submitVo.setAssignUser(WebUserUtils.getUserCode());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setAssignCom(WebUserUtils.getComCode());
		submitVo.setSubmitType(SubmitType.U);
		// 提交工作流
		
		wfTaskHandleService.submitRegist(registVo,submitVo);
		// 报案新增保存是保存风险提示信息
		/** 交强险种前缀 */
		/*
		public static final String PREFIX_CI = "11";
		*//** 商业险种前缀 */
		/*
		public static final String PREFIX_BI = "12";*/
		registVo = registService.findRegistByRegistNo(registNo);
		/*String flag = registVo.getRiskCode().substring(0, 2);
		if(flag.equals("12")){
			registService.updateRiskInfo(registVo,"BI-No");
		}else if(flag.equals("11")){
			registService.updateRiskInfo(registVo,"CI-No");
		}*/
		
		
		// 无保转有保送平台,try,catch报案上传平台失败不需要展示在页面
		interfaceAsyncService.sendRegistToPlatform(registNo);
		
		// 报案提交调用方正客服系统(车险报案接口)
		try{
			//scheduleTaskVo.setRelateHandlerMobile(item.getRelateHandlerMobile());
			interfaceAsyncService.carRegistToFounder(registNo);
		}catch(Exception e){
			logger.debug("报案提交调用方正客服系统失败："+e.getMessage());
		}
		
		// 移动查勘交互更新调度任务表
		String url = SpringProperties.getProperty("MClaimPlatform_URL")+AUTOSCHEDULE_URL_METHOD;
		PrpLScheduleTaskVo scheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(registNo);
		registService.updateScheduleTask(registVo,scheduleTaskVo,url);

		if(!CodeConstants.TempReport.TEMPREPORT.equals(registVo.getTempRegistFlag())){
			List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
	        prpLCMainVoList = policyViewService.getPolicyAllInfo(registNo);
	        if(!prpLCMainVoList.isEmpty()){
	        	try{
	        		SysUserVo userVo = WebUserUtils.getUser();
	        		interfaceAsyncService.sendRegistForGenilex(registVo, userVo, prpLCMainVoList.get(0));
	        	} catch(Exception e) {
					logger.error("报案送精励联讯接口失败：",e);
	        	}
	        }
		}
		return edit(model, registNo);
	}
	
	/**
	 * 多保单关联与取消轨迹查看界面
	 * @param registNo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/relationshipHis.do")
	public String relationshipHis(String registNo,Model model) {
		List<PrpLRegistRelationshipHisVo> relationshipHisVos = registService.findRelationshipHisByRegistNo(registNo);
		model.addAttribute("registNo", registNo);
		model.addAttribute("relationshipHisVos", relationshipHisVos);
		return "regist/registEdit/ReportEdit_RelationshipHis";
	}
	
	/**
	 * 跳转到保单关联与取消界面
	 * @param registNo
	 * @return
	 */
	@RequestMapping(value = "/relationshipList.do")
	public ModelAndView relationshipList(String registNo, String flag,String relationFlag) {
		
		PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
		PolicyInfoVo policyInfoVo = new PolicyInfoVo();
		policyInfoVo.setDamageTime(registVo.getDamageTime());
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("policyInfoVo",policyInfoVo);
		
		List<PrpLCMainVo> prpLCMains = policyViewService.getPolicyAllInfo(registNo);
		mav.addObject("prpLCMains",prpLCMains);
		//TODO
		if("registAdd".equals(relationFlag)){
			// 当前保单关联环节为报案补登
			relationFlag = "regist";
			mav.addObject("regAddFlag","1");
		}
		mav.addObject("flag",flag);
		mav.addObject("relationFlag",relationFlag);
		mav.setViewName("regist/registEdit/ReportEdit_RelationList");
		return mav;
	}
	
	/**
	 * 保单关联与取消查询
	 * @param policyInfoVo
	 * @param start
	 * @param length
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/search.do/", method = RequestMethod.POST)
	@ResponseBody
	@AvoidRepeatableCommit
	public String search(
			@FormModel("policyInfoVo") PolicyInfoVo policyInfoVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) throws Exception{
		try{
			String propertyName = ResultAmount.policyResultAmt;
			//输入的保单查询条件，初步根据条件筛查出的保单超过100条则要求输入更加准确的条件
			if(!policyQueryService.calculateResultCount(policyInfoVo,propertyName)){
				throw new IllegalAccessException("查询结果过大（大于" + ReadConfigUtils.getResultCount(propertyName) + "），请输入更精确的查询条件");
			}
			Page<PolicyInfoVo> page = policyQueryService.findPrpCMainForPage(policyInfoVo,start,length);
			//ins.framework.dao.database.support.Page<PolicyInfoVo> page = policyQueryService.findPrpCMainForPage(policyInfoVo,start,length);
			String jsonData = ResponseUtils.toDataTableJson(page,"policyNo","licenseNo","comCode:ComCode","insuredName","brandName",
					"frameNo","engineNo","startDate","endDate","riskType","validFlag","relatedPolicyNo","comCode","startDateHour","endDateHour");
			
			return jsonData;
		}catch(Exception e){
			logger.error("保单关联与取消查询失败",e);
			throw e;
		}
	}
	
	/**
	 * 
	 * @param registNo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/submitSchedule")
	public String submitSchedule(String registNo,Model model) {
		model.addAttribute("registNo", registNo);
		model.addAttribute("userCode", WebUserUtils.getUserCode());
		model.addAttribute("comCode", WebUserUtils.getComCode());
		return "regist/registEdit/ReportEdit_SubmitSchedule";
	}
	
	@Timed("报案提交")
	@RequestMapping(value = "/submit.do")
	public String submit(String registNo, WfTaskSubmitVo submitVo, Model model) throws Exception {
		PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
		List<PrpLCMainVo> prpLCMainVos=prpLCMainService.findPrpLCMainsByRegistNo(registNo);
		if(!CodeConstants.RegistTaskFlag.SUBMIT.equals(registVo.getRegistTaskFlag())){
			// 将报案状态设为已提交
			registVo.setRegistTaskFlag(CodeConstants.RegistTaskFlag.SUBMIT);
			try {
				// 关联报案号与事故编号prpLRegistCarLosses
				if(registVo.getComCode().startsWith("0002")){
					List<PrpLRegistCarLossVo> prpLRegistCarLossList = registVo.getPrpLRegistCarLosses();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(prpLRegistCarLossList != null && prpLRegistCarLossList.size() > 0 && registVo.getDamageTime() != null){
						String accidentTime = formatter.format(registVo.getDamageTime());
						for(PrpLRegistCarLossVo registCarLossVo : prpLRegistCarLossList){
							if("1".equals(registCarLossVo.getLossparty())){
								String plateNo = registCarLossVo.getLicenseNo();
								String accidentNo = szpoliceCaseService.findAccidentInfoByPlateNoAndTime(plateNo, accidentTime,"","0");
								registVo.setAccidentNo(accidentNo);
								if(StringUtils.isNotBlank(accidentNo)){
									AccidentResInfo resInfo = szpoliceCaseService.findAccidentResInfoByAccidentNo(accidentNo);
									resInfo.setRegistNo(registNo);
									szpoliceCaseService.updateAccidentResInfo(resInfo);
								}
								break;
							}
						}
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("关联报案号与事故编号失败================"+registNo);
			}
			
			registVo = registService.saveOrUpdate(registVo);
			BigDecimal flowTaskId = BigDecimal.ZERO;
			submitVo.setFlowId(registVo.getFlowId());
			submitVo.setTaskInKey(registNo);
			submitVo.setFlowTaskId(flowTaskId);
			submitVo.setTaskInUser(WebUserUtils.getUserCode());
			if(CodeConstants.TempReport.TEMPREPORT.equals(registVo.getTempRegistFlag())){
				submitVo.setComCode(WebUserUtils.getComCode());
			}else{
				submitVo.setComCode(policyViewService.getPolicyComCode(registVo.getRegistNo()));
			}
			//submitVo.setComCode(policyViewService.getPolicyComCode(registNo));
			//submitVo.setComCode(WebUserUtils.getComCode());
			submitVo.setAssignUser(WebUserUtils.getUserCode());
			submitVo.setAssignCom(WebUserUtils.getComCode());
			submitVo.setSubmitType(SubmitType.N);
			
			
			PrpLScheduleTaskVo scheduleTaskVo = new PrpLScheduleTaskVo();
			
			scheduleTaskVo.setPosition(WebUserUtils.getComCode());
			scheduleTaskVo.setCreateUser(WebUserUtils.getUserCode());
			scheduleTaskVo.setCreateTime(new Date());
			scheduleTaskVo.setUpdateUser(WebUserUtils.getUserCode());
			scheduleTaskVo.setUpdateTime(new Date());
			String url = SpringProperties.getProperty("MClaimPlatform_URL")+AUTOSCHEDULE_URL_METHOD;
			registService.submitSchedule(registVo, submitVo, scheduleTaskVo,url);
			// 提交工作流
			PrpLWfTaskVo taskVo = wfTaskHandleService.submitRegist(registVo,submitVo);
			// 上传平台
			interfaceAsyncService.sendRegistToPlatform(registNo);
			// 上传深圳警保
			// if(registVo.getComCode().startsWith("0002")){
			// interfaceAsyncService.sendRegistInfoToSZJB(registNo);
			// }
			
			SysUserVo userVo = WebUserUtils.getUser();
			// 德联易控yzy,1--代表报案请求节点
			List<PrpLRegistCarLossVo> prpLRegistCarLosses = new ArrayList<PrpLRegistCarLossVo>();
			prpLRegistCarLosses=registVo.getPrpLRegistCarLosses();
				String Qurl=SpringProperties.getProperty("YX_QUrl");
				interfaceAsyncService.SendControlExpert(registVo.getRegistNo(), userVo, "", "","01",Qurl);
			
			//埋点把理赔信息推送到rabbitMq中间件，供中台使用
			claimToMiddleStageOfCaseService.middleStageQuery(registNo, "Regis");
  
			// 报案提交调用方正客服系统(车险报案接口)
			try{
				//scheduleTaskVo.setRelateHandlerMobile(item.getRelateHandlerMobile());
				interfaceAsyncService.carRegistToFounder(registNo);
			}catch(Exception e){
				logger.debug("报案提交调用方正客服系统失败："+e.getMessage());
			}
			try{
				interfaceAsyncService.sendRegistDataToJy(registNo);
			}catch(Exception e){
				logger.info("报案提交精友系统失败："+e.getMessage());
			}
			//报案送中保信
			interfaceAsyncService.reqByRegist(registVo, userVo, "01");
			// 发送短信
			try{
				String serviceMobile="";//推修手机号，有商业取商业，没商业取交强
				Long repairId=null;//修理厂主表Id
				String BIPolicyNo="";
				String CIPolicyNo="";
				if(prpLCMainVos!=null && prpLCMainVos.size()>0){
					for(PrpLCMainVo mainVo:prpLCMainVos){
						if("1101".equals(mainVo.getRiskCode())){
							CIPolicyNo=mainVo.getPolicyNo();
						}else{
							BIPolicyNo=mainVo.getPolicyNo();
						}
					}
				}
				
			    if(StringUtils.isNotBlank(BIPolicyNo)){
			      serviceMobile=policyQueryService.findPrpCMian(BIPolicyNo);
			    }
			    if(StringUtils.isNotBlank(CIPolicyNo) && StringUtils.isBlank(serviceMobile)){
			      serviceMobile=policyQueryService.findPrpCMian(CIPolicyNo);
			    }
			    if(StringUtils.isNotBlank(serviceMobile)){
			      repairId=repairFactoryService.findRepairFactoryBy(serviceMobile);
			    }
			    registVo.setServiceMobile(serviceMobile);
			    registVo.setRepairId(repairId);
				registService.sendMsg(registVo);
			}catch(Exception e){
				e.printStackTrace();
				logger.error("报案提交发送短信失败：",e.getMessage(),e);
			}
			if(!CodeConstants.TempReport.TEMPREPORT.equals(registVo.getTempRegistFlag())){
				List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
		        prpLCMainVoList = policyViewService.getPolicyAllInfo(registNo);
		        if(!prpLCMainVoList.isEmpty()){
		        	try{
		        		interfaceAsyncService.sendRegistForGenilex(registVo, userVo, prpLCMainVoList.get(0));
		        	} catch(Exception e) {
						logger.error("报案送精励联讯接口失败：",e);
		        	}
		        }
			}

			// 判断当前工号有无处理调度的权限
			List<SaaGradeVo> saaGradeVoList = saaUserGradeService.findUserGrade(WebUserUtils.getUserCode());
			Boolean flag = false;
			if(saaGradeVoList != null && saaGradeVoList.size() > 0){
				for(SaaGradeVo saaGradeVo:saaGradeVoList){
					if(FlowNode.Sched.getRoleCode().equals(saaGradeVo.getId().toString())){// 调度岗的ID是5003
						flag = true;
						break;
					}
				}
			}

			model.addAttribute("flag", flag);
			model.addAttribute("registNo", registNo);
			model.addAttribute("flowTaskId", taskVo.getTaskId());
			logger.debug("===testSubmitRegist=taskVo="+taskVo.getTaskId());
			model.addAttribute("reportFinish", "0");
			logger.debug(registNo);
		}else{
			// 已经提交
			model.addAttribute("registNo", registNo);
			model.addAttribute("reportFinish", "1");
		}
		return "regist/registEdit/ReportEdit_QuickHandle";
	}

	/**
	 * 增加三者车损失项
	 * @return
	 * @modified: ☆Luwei <br>
	 */
	@RequestMapping(value = "/addThirdCar.ajax")
	@ResponseBody
	public ModelAndView addThirdCar(int carSize) throws ParseException {
		ModelAndView mv = new ModelAndView();
		List<PrpLRegistCarLossVo> registCarLosses = new ArrayList<PrpLRegistCarLossVo>();
		PrpLRegistCarLossVo registCarLoss = new PrpLRegistCarLossVo();
		registCarLoss.setCreateUser(WebUserUtils.getUserCode());
		registCarLoss.setCreateTime(new Date());
		registCarLoss.setUpdateUser(WebUserUtils.getUserCode());
		registCarLoss.setUpdateTime(new Date());
		registCarLosses.add(registCarLoss);
		mv.addObject("prpLRegistCarLosses",registCarLosses);
		mv.addObject("carSize",carSize);
		mv.setViewName("regist/registEdit/ReportEdit_AddThirdCar");
		return mv;
	}
	
	/**
	 * 增加财产损失项
	 * @return
	 * @modified: ☆Luwei <br>
	 */
	@RequestMapping(value = "/addProp.ajax")
	@ResponseBody
	public ModelAndView addProp(int propSize,String licenseNo,String thirdCarNos) throws ParseException {
		ModelAndView mv = new ModelAndView();
		List<PrpLRegistPropLossVo> prpLRegistPropLosses = new ArrayList<PrpLRegistPropLossVo>();
		PrpLRegistPropLossVo prpLRegistPropLoss = new PrpLRegistPropLossVo();
		prpLRegistPropLoss.setCreateUser(WebUserUtils.getUserCode());
		prpLRegistPropLoss.setCreateTime(new Date());
		prpLRegistPropLoss.setUpdateUser(WebUserUtils.getUserCode());
		prpLRegistPropLoss.setUpdateTime(new Date());
		prpLRegistPropLosses.add(prpLRegistPropLoss);
		// 拼装财产损失方-如 标的车+车牌号 或 三者车+车牌号
		Map<String,String> carNoMap = new HashMap<String,String>();
		carNoMap.put("0","地面");
		carNoMap.put(licenseNo,"标的车("+licenseNo+")");
		if(!StringUtils.isEmpty(thirdCarNos)){
			String[] thirdCarNoArr = thirdCarNos.split(",");
			for (int i=0;i<thirdCarNoArr.length;i++){
				carNoMap.put(thirdCarNoArr[i],"三者车("+thirdCarNoArr[i]+")");
			}
		}

		
		mv.addObject("prpLRegistPropLosses",prpLRegistPropLosses);
		mv.addObject("propSize", propSize);
		mv.addObject("carNoMap", carNoMap);
		mv.setViewName("regist/registEdit/ReportEdit_Prop");
		return mv;
	}
	
	/**
	 * 保单取消关联操作
	 * @param policyNo
	 * @param registNo
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/cancelPolicy.ajax")
	@ResponseBody
	public String cancelPolicy(String policyNo, String registNo,String relationFlag) throws ParseException {
		Date currentDate = new Date();
		logger.info("保单取消关联操作开始=============================start,policyNo="+policyNo+"registNo="+registNo);
		SysUserVo sysUserVo = WebUserUtils.getUser();
		List<PrpLClaimVo> prpLClaimVoList = claimTaskService.findprpLClaimVoListByRegistAndPolicyNo(registNo,policyNo,"1");
		/*//查询查勘是否提交
		PrpLCheckVo prpLCheckVo = checkTaskService.findCheckVoByRegistNo(registNo);
		//如果查看任务已提交，返回2，前台提示已立案，不能取消关联
		if (prpLCheckVo != null && prpLCheckVo.getChkSubmitTime() != null) {
			return "2";
		}*/
		if(prpLClaimVoList != null && prpLClaimVoList.size() > 0){
			return "2";
		}
		// 查询报案信息，并将损失子表置空，无需更新，并将扩展表的关联保单号置空
		PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
		registVo.setPrpLRegistCarLosses(null);
		registVo.setPrpLRegistPersonLosses(null);
		registVo.setPrpLRegistPropLosses(null);
		registVo.getPrpLRegistExt().setPolicyNoLink(null);
		// 获取现有保单数据
		List<PrpLCMainVo> returnMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
		// 拼装log表数据
		PrpLRegistRelationshipHisVo relationshipHisVo = new PrpLRegistRelationshipHisVo();
		relationshipHisVo.setRegistNo(registVo.getRegistNo());// 设置报案号
		// 判断是不是报案环节relationFlag
		if("regist".equals(relationFlag)){
			relationshipHisVo.setOperationtype(CodeConstants.RelateOperatType.CANCEL_RELATION);// 设置操作类型位取消关联
		}else{
			relationshipHisVo.setOperationtype(CodeConstants.RelateOperatType.CANCEL_RELATION_CHECK);// 设置操作类型位取消关联
		}
		
		relationshipHisVo.setCreateUser(WebUserUtils.getUserCode());
		relationshipHisVo.setCreateTime(new Date());
		relationshipHisVo.setUpdateUser(WebUserUtils.getUserCode());
		relationshipHisVo.setUpdateTime(new Date());
		if (returnMainVoList != null && returnMainVoList.size() == 2) {//
			relationshipHisVo.setOperationbefore(returnMainVoList.get(0).getPolicyNo() + "," + returnMainVoList.get(1).getPolicyNo());
		} else {
			throw new IllegalArgumentException("案件信息有误，请确认后再进行操作");
		}
		// 如果可以取消关联，获取该保单的抄单信息，置为无效
		List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
		if (returnMainVoList.size()>0) {
			for (PrpLCMainVo vo : returnMainVoList) {
				// 将要取消关联的保单信息设置为失效
				if (vo != null && StringUtils.equals(vo.getPolicyNo(), policyNo)) {
					vo.setValidFlag(CodeConstants.ValidFlag.INVALID);
					vo.setUpdateUser(WebUserUtils.getUserCode());
					vo.setUpdateTime(new Date());
					vo.setPrpCengages(null);
					vo.setPrpCInsureds(null);
					vo.setPrpCItemCars(null);
					vo.setPrpCItemKinds(null);
					prpLCMainVoList.add(vo);
					logger.info("保单取消关联操作prpLCMainVoList有数据=============================policyNo="+policyNo+"registNo="+registNo);
				}else if(vo!=null){// 剩余有效保单号和险别存入报案主表
					registVo.setPolicyNo(vo.getPolicyNo());
					registVo.setRiskCode(vo.getRiskCode());
					registVo.setReportType(StringUtils.equals(vo.getRiskCode(), "1101")?CodeConstants.ReportType.CI:CodeConstants.ReportType.BI);
					relationshipHisVo.setOperationafter(vo.getPolicyNo());
				}
			}
		}
		// registHandlerService更新风险提示表
		try {
			registHandlerService.updateRiskInfo(registVo);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 保单取消送平台,try,catch报案上传平台失败不需要展示在页面
		try {
			interfaceAsyncService.sendCancelToPaltformRe(registNo,policyNo,"1");
		} catch (Exception e) {}
		
		PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(registNo, policyNo);
		if(SendPlatformUtil.isMor(prpLCMainVo)){
			// 山东预警
			String warnswitch = SpringProperties.getProperty("WARN_SWITCH");// 62,10,50
			if(warnswitch.contains(prpLCMainVo.getComCode().substring(0,2))){// prpLCMainVo.getComCode().startsWith("62")
				try {
					// 报案注销送山东预警
					interfaceAsyncService.sendCaseCancleRegister(prpLCMainVo,"registCancle","21",sysUserVo);
				} catch (Exception e) {
					logger.info("案件注销送预警异常信息-------------->"+e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
		// 保单注销送方正客服系统
		try{
			interfaceAsyncService.PolicyRelationToFounder(registNo,policyNo,"0");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		// 注销无效的保单
		// 设置投保确认码
		for(PrpLCMainVo vo:prpLCMainVoList){
			List<PrpCiInsureValidVo> vos= prpLCMainService.findPrpCiInsureValidByPolicyNo(vo.getPolicyNo());
			if(vos!=null && vos.size()>0){
				vo.setValidNo(vos.get(0).getValidNo());
			}
		}
		registService.cancelPolicy(prpLCMainVoList,registVo,relationshipHisVo);
		logger.info("保单取消关联保存relationshipHis数据结束=============================policyNo="+policyNo+"registNo="+registNo);
		
		// 删除工作流立案的节点
		 BigDecimal upPerTaskId = wfTaskHandleService.deleteByRegist(registVo);
		// 提交工作流操作
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(registVo.getFlowId());
		submitVo.setTaskInKey(registVo.getRegistNo());
		//submitVo.setFlowTaskId(BigDecimal.ZERO);
		submitVo.setFlowTaskId(upPerTaskId);
		submitVo.setComCode(policyViewService.getPolicyComCode(registVo.getRegistNo()));
		//submitVo.setComCode(WebUserUtils.getComCode());
		submitVo.setAssignUser(WebUserUtils.getUserCode());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setAssignCom(WebUserUtils.getComCode());
		submitVo.setSubmitType(SubmitType.U);
		// 创建新的立案
		// 判断是否有强制立案，没有就创建新的立案
		int biaoshi=0;
		List<PrpLClaimVo> prpLClaimVos = claimTaskService.findClaimListByRegistNo(registNo);
		if(prpLClaimVos!=null&&prpLClaimVos.size()>0){
			for(PrpLClaimVo vo : prpLClaimVos){
				if(vo.getClaimFlag().equals("5")){
					biaoshi+=1;
				}
			}
		}
		if(biaoshi==0){
			if(upPerTaskId != null){
				wfTaskHandleService.submitClaimHandl(registVo, submitVo);
			}
		}
		// 提交工作流
		if(upPerTaskId==null||upPerTaskId.equals("")){
		submitVo.setFlowTaskId(BigDecimal.ZERO);
		}
		PrpLWfTaskVo taskVo = wfTaskHandleService.submitRegist(registVo, submitVo);
		logger.info("保单取消关联操作结束=============================end,policyNo="+policyNo+"registNo="+registNo +"耗时:"+(System.currentTimeMillis() - currentDate.getTime())+"毫秒");
		// 返回1，可以取消关联
		return "1";
	}
	
	/**
	 * 保单关联前校验操作
	 * @param policyNo
	 * @param registNo
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/checkRelationPolicy.ajax")
	@ResponseBody
	public String checkRelationPolicy(String policyNo, String registNo) throws ParseException {
		List<PrpLCMainVo> toSaveMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
		if (toSaveMainVoList != null && toSaveMainVoList.size() > 0) {
			for (PrpLCMainVo vo : toSaveMainVoList) {
				if (vo != null && StringUtils.equals(vo.getPolicyNo(), policyNo)) {
					return "2";
				}
			}
		}
		
		return "1";
	}
	
	/**
	 * 保单关联操作
	 * @param policyNo
	 * @param registNo
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/relationPolicy.ajax")
	@ResponseBody
	public ModelAndView relationPolicy(String policyNo, String registNo,String relationFlag) throws ParseException {
		Date currentDate = new Date();
		logger.info("保单关联操作开始=============================start,policyNo="+policyNo+"registNo="+registNo);
		PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
		prpLRegistVo.setUpdateUser(WebUserUtils.getUserCode());
		prpLRegistVo.setUpdateTime(new Date());
		prpLRegistVo.getPrpLRegistExt().setUpdateUser(WebUserUtils.getUserCode());
		prpLRegistVo.getPrpLRegistExt().setUpdateTime(new Date());
		// 设置关联保单
		prpLRegistVo.getPrpLRegistExt().setPolicyNoLink(policyNo);
		// 获取现有保单数据
		List<PrpLCMainVo> beforeMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
		// 拼装log表数据
		PrpLRegistRelationshipHisVo relationshipHisVo = new PrpLRegistRelationshipHisVo();
		relationshipHisVo.setRegistNo(prpLRegistVo.getRegistNo());// 设置报案号
		if("regist".equals(relationFlag)||"registAdd".equals(relationFlag)){
			relationshipHisVo.setOperationtype(CodeConstants.RelateOperatType.TO_RELATION);// 设置操作类型位取消关联
		}else{
			relationshipHisVo.setOperationtype(CodeConstants.RelateOperatType.TO_RELATION_CHECK);// 设置操作类型位取消关联
		}
		relationshipHisVo.setCreateUser(WebUserUtils.getUserCode());
		relationshipHisVo.setCreateTime(new Date());
		relationshipHisVo.setUpdateUser(WebUserUtils.getUserCode());
		relationshipHisVo.setUpdateTime(new Date());
		if (beforeMainVoList != null && beforeMainVoList.size() == 1) {
			relationshipHisVo.setOperationbefore(beforeMainVoList.get(0).getPolicyNo());
			relationshipHisVo.setOperationafter(beforeMainVoList.get(0).getPolicyNo() + "," + policyNo);
		} else {
			throw new IllegalArgumentException("案件信息有误，请确认后再进行操作");
		}
				
		List<String> policyNoList = new ArrayList<String>();
		policyNoList.add(policyNo);
		
		List<PrpLCMainVo> toSaveMainVoList = policyQueryService.findPrpcMainByPolicyNos(policyNoList);
		toSaveMainVoList.get(0).setValidFlag(CodeConstants.ValidFlag.VALID);
		toSaveMainVoList.get(0).setCreateUser(WebUserUtils.getUserCode());
		toSaveMainVoList.get(0).setCreateTime(new Date());
		toSaveMainVoList.get(0).setUpdateUser(WebUserUtils.getUserCode());
		toSaveMainVoList.get(0).setUpdateTime(new Date());
		// 设置投保确认码
		for(PrpLCMainVo vo:toSaveMainVoList){
			List<PrpCiInsureValidVo> vos= prpLCMainService.findPrpCiInsureValidByPolicyNo(vo.getPolicyNo());
			if(vos!=null && vos.size()>0){
				vo.setValidNo(vos.get(0).getValidNo());
			}
			logger.info("保单关联操作toSaveMainVoList有数据=============================policyNo="+policyNo+"registNo="+registNo);
		}
		List<PrpLCMainVo> returnMainVoList = registHandlerService.relationPolicy(prpLRegistVo,toSaveMainVoList, relationshipHisVo);
		logger.info("保单关联操作relationshipHis保存end=============================policyNo="+policyNo+"registNo="+registNo);
		// registHandlerService更新风险提示表
		try {
			registHandlerService.updateRiskInfo(prpLRegistVo);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 删除工作流立案的节点
		 BigDecimal upPerTaskId = wfTaskHandleService.findByRegist(prpLRegistVo);
		
		// 提交工作流操作
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(prpLRegistVo.getFlowId());
		submitVo.setTaskInKey(prpLRegistVo.getRegistNo());
		submitVo.setFlowTaskId(BigDecimal.ZERO);
		submitVo.setComCode(policyViewService.getPolicyComCode(prpLRegistVo.getRegistNo()));
		//submitVo.setComCode(WebUserUtils.getComCode());
		submitVo.setAssignUser(WebUserUtils.getUserCode());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setAssignCom(WebUserUtils.getComCode());
		submitVo.setSubmitType(SubmitType.U);
		
			
		// 提交工作流
		PrpLWfTaskVo taskVo = wfTaskHandleService.submitRegist(prpLRegistVo,submitVo);

		// 判断是否存在立案数据 不存在则创建新的立案工作流节点
		List<PrpLClaimVo> claimVoList = claimTaskService.findprpLClaimVoListByRegistAndPolicyNo(registNo,policyNo,CodeConstants.ValidFlag.VALID);
		if(claimVoList==null||claimVoList.size()==0){
			// 创建新的立案
			//System.out.println("ppppppp"+upPerTaskId);
			submitVo.setFlowTaskId(upPerTaskId);
			prpLRegistVo.setPolicyNo(policyNo);
			if(upPerTaskId != null){
				wfTaskHandleService.submitClaimHandls(prpLRegistVo, submitVo);
			}
		}
		
		// 保单关联--报案送平台,try,catch报案上传平台失败不需要展示在页面
		try{
			// 保单关联 如果报案未提交 不送平台 等报案提交一起送 防止重复送平台
			PrpLWfTaskVo taskInVo = wfTaskHandleService.findWftaskInByRegistnoAndSubnode(registNo,FlowNode.Regis.toString());
			if(taskInVo == null){
				interfaceAsyncService.sendCancelToPaltformRe(registNo,policyNo,"2");
			}
		}catch(Exception e){}
		
		// 保单关联送方正客服系统
		try{
			interfaceAsyncService.PolicyRelationToFounder(registNo,policyNo,"1");
		}catch(Exception e){
			e.printStackTrace();
		}
		// 创建立案节点需要判断，如果是补登报案，则判断，立案前正常走，已立案走刷新立案
		claimTaskExtService.regsitAddCaseAfterClaim(registNo,policyNo);
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("prpLCMains", returnMainVoList);
		mv.setViewName("regist/registEdit/ReportEdit_RelationListSub");
		logger.info("保单关联操作结束=============================end,policyNo="+policyNo+"registNo="+registNo +"耗时:"+(System.currentTimeMillis() - currentDate.getTime())+"毫秒");
		return mv;
	}
	
	
	@RequestMapping(value = "/reportCancels.do")
	@ResponseBody
	public AjaxResult reportCancels(String registNo){
		AjaxResult ajaxResult = new AjaxResult();
		// 已经立案不能注销
		List<PrpLClaimVo> prpLClaimVos = claimTaskService.findClaimListByRegistNo(registNo);
		if(prpLClaimVos!=null){
			if(prpLClaimVos.size() > 0){
				ajaxResult.setData(0);// 不能提交
			}else{
				
				ajaxResult.setData(1);// 能提交
			}
		}else{
			ajaxResult.setData(1);// 能提交
		}
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	@RequestMapping(value="isSelfHelpSurVey.ajax")
	@ResponseBody
	public  AjaxResult isSelfHelpSurVey(String registNo){
		AjaxResult ajaxResult = new AjaxResult();
		Boolean isSelfHelpSurVey =  registService.isSelfHelpSurVey(registNo);
		if(isSelfHelpSurVey){
			ajaxResult.setData("现场报案，无人伤和物损的单车事故，服务时段为工作日9：00-17：00，可进行自助查勘");
		} 
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK); 
		return ajaxResult;
	}
	
	/**
	 * 报案注销
	 * @param policyNo
	 * @param registNo
	 * @return
	 * @throws Exception
	 */ 
	@Timed("报案注销")
	@RequestMapping(value = "/reportCancel.ajax")
	@ResponseBody
	public ModelAndView reportCancel(String cancelReason, String registNo) throws Exception {
		logger.info("报案号registNo={}进行报案注销回写RegistTaskFlag的方法",registNo);
		SysUserVo userVo = WebUserUtils.getUser();
		PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
		prpLRegistVo.setUpdateUser(WebUserUtils.getUserCode());
		prpLRegistVo.setUpdateTime(new Date());
		prpLRegistVo.getPrpLRegistExt().setUpdateUser(WebUserUtils.getUserCode());
		prpLRegistVo.getPrpLRegistExt().setUpdateTime(new Date());
		logger.info("报案号registNo={}给报案任务状态RegistTaskFlag赋值,值为={}",registNo,CodeConstants.RegistTaskFlag.CANCELED);
		prpLRegistVo.setRegistTaskFlag(CodeConstants.RegistTaskFlag.CANCELED);
		prpLRegistVo.setCancelFlag(CodeConstants.CancelFlag.CANCEL);
		prpLRegistVo.getPrpLRegistExt().setCancelReason(cancelReason);
		prpLRegistVo.getPrpLRegistExt().setCancelUser(WebUserUtils.getUserCode());
		prpLRegistVo.setCancelTime(new Date());
		prpLRegistVo = registHandlerService.reportCancel(prpLRegistVo);
		
		// 提交工作流操作
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(prpLRegistVo.getFlowId());
		submitVo.setTaskInKey(prpLRegistVo.getRegistNo());
		submitVo.setFlowTaskId(BigDecimal.ZERO);
		if(CodeConstants.TempReport.TEMPREPORT.equals(prpLRegistVo.getTempRegistFlag())){
			submitVo.setComCode(WebUserUtils.getComCode());
		}else{
			submitVo.setComCode(policyViewService.getPolicyComCode(prpLRegistVo.getRegistNo()));
		}
		
		//submitVo.setComCode(WebUserUtils.getComCode());
		submitVo.setAssignUser(WebUserUtils.getUserCode());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setAssignCom(WebUserUtils.getComCode());
		submitVo.setSubmitType(SubmitType.C);
		
		// 获取out表的所有车童网/民太安任务
        String flagCT = "0";
        String flagMTA = "0";
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
                if("2".equals(vo.getIsMobileAccept())){
                    flagCT = "1";
                    prpLWfTaskCTVos.add(vo);
                }else if("3".equals(vo.getIsMobileAccept())){
                    flagMTA = "1";
                    prpLWfTaskMTAVos.add(vo);
                }
            }
        }

        
        
		// 提交工作流
		PrpLWfTaskVo taskVo = wfTaskHandleService.submitRegist(prpLRegistVo,submitVo);
		// 报案注销送平台,try,catch报案上传平台失败不需要展示在页面
		List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(registNo);
		for(PrpLCMainVo cMainVo : cMainVoList){
			try{
				String policyNo = cMainVo.getPolicyNo();
				interfaceAsyncService.sendCancelToPaltformRe(registNo,policyNo,"1");
			}catch(Exception e){}
			
			if(SendPlatformUtil.isMor(cMainVo)){
				String warnswitch = SpringProperties.getProperty("WARN_SWITCH");// 62,10,50
				if(warnswitch.contains(cMainVo.getComCode().substring(0,2))){// prpLCMainVo.getComCode().startsWith("62")
					try {
						// 报案注销送山东预警
						interfaceAsyncService.sendCaseCancleRegister(cMainVo, "registCancle", cancelReason, userVo);
					} catch (Exception e) {
						logger.info("案件注销送预警异常信息-------------->"+e.getMessage());
						e.printStackTrace();
					}
					try {
						// 重复案件送山东预警
						if("2".equals(cancelReason)){
							// TODO 因为报案没有三者车架号所以暂时不上传山东平台
							//interfaceAsyncService.sendFalseCaseToEWByRegist(cancelReason, registNo,null);
						}
					} catch (Exception e) {
						logger.info("重复/虚假案件标记送山东预警异常信息-------------->"+e.getMessage());
						e.printStackTrace();
					}			
				}
			}
		}
		try {
			// 报案注销推送至客服系统接口
			if(cMainVoList!=null&&cMainVoList.size()>0){// 无保单报案 报案注销不送方正
				interfaceAsyncService.registCancelToFounder(registNo);
			}
		} catch (Exception e) {
			logger.debug("报案注销调用方正客服系统失败："+e.getMessage());
		}

		 //报案注销理赔通知自助理赔系统
		if(prpLRegistVo!=null && "1".equals(prpLRegistVo.getSelfClaimFlag())){
			interfaceAsyncService.sendClaimResultToSelfClaim(registNo,userVo,"4","4",null);
		}
		//报案注销送中保信
		interfaceAsyncService.reqByRegist(prpLRegistVo, userVo, "03");
		
		//移动端案件理赔须通知移动端
		PrpLWfMainVo prpLWfMainVo = sendMsgToMobileService.findPrpLWfMainVoByRegistNo(registNo);
		if(prpLWfMainVo!=null&&"1".equals(prpLWfMainVo.getIsMobileCase())){ // 移动端案件
			taskVo.setHandlerStatus("9"); // 已注销
			taskVo.setWorkStatus("9"); // 注销
			taskVo.setMobileOperateType(CodeConstants.MobileOperationType.REGISTCANCLE);
			try{
				String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
				interfaceAsyncService.packMsg(taskVo,url);
			}catch(Exception e){
				e.printStackTrace();
				logger.debug("理赔通知移动查勘失败："+e.getMessage());
			}
			
		}
		/*//河南快赔案件注销通知河南快赔系统 --暂时去掉yzy
		try{
			if(prpLRegistVo!=null && "1".equals(prpLRegistVo.getIsQuickCase())){
				SysUserVo userVo = WebUserUtils.getUser();
				interfaceAsyncService.receivecpsresult(registNo,"4", userVo);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.debug("理赔通知河南快赔失败："+e.getMessage());
		}
		*/

       
        RevokeInfoReqVo reqVo = new RevokeInfoReqVo();
        CarchildHeadVo head = new CarchildHeadVo();
        PrpLConfigValueVo configValueMTACheckVo = ConfigUtil.findConfigByCode(CodeConstants.MTACheck,prpLRegistVo.getComCode());
        PrpLConfigValueVo configValueCTCheckVo = ConfigUtil.findConfigByCode(CodeConstants.CTCheck,prpLRegistVo.getComCode());
        if("1".equals(flagCT)){
			// 理赔请求车童网
            head.setRequestType("CT_006");
            head.setUser("claim_user");
            head.setPassWord("claim_psd");
            reqVo.setHead(head);
			// 报案注销通知车童网/民太安
            if(configValueCTCheckVo != null && "1".equals(configValueCTCheckVo.getConfigValue())){
                organizationAndSendCTorMTA(prpLWfTaskCTVos,prpLRegistVo,reqVo);
            }
        }
        if("1".equals(flagMTA)){
			// 理赔请求民太安
            head.setRequestType("MTA_006");
            head.setUser("claim_user");
            head.setPassWord("claim_psd");
            reqVo.setHead(head);
			// 报案注销通知车童网/民太安
            if(configValueMTACheckVo != null && "1".equals(configValueMTACheckVo.getConfigValue())){
                organizationAndSendCTorMTA(prpLWfTaskMTAVos,prpLRegistVo,reqVo);
            }
        }

		// 回写案件注销标志(如果是车童或者民太安请求注销申请，则进行报案注销后回写注销标识)
		// 由于注销申请接口将案子标识为不是公估案件了，所以上面代码不会进去，就不会回写案件注销标识，所以将案件注销标识代码挪出来
		try {
			PrplcarchildregistcancleVo prplcarchildregistcancleVo = carchildService.findPrplcarchildregistcancleVoByRegistNo(prpLRegistVo.getRegistNo());
			if(prplcarchildregistcancleVo!=null){
				prplcarchildregistcancleVo.setExamineRusult("1");
				prplcarchildregistcancleVo.setStatus("1");
				prplcarchildregistcancleVo.setHandleUser(WebUserUtils.getUserName());
				prplcarchildregistcancleVo.setHandleDate(prpLRegistVo.getCancelTime());
				carchildService.updatePrplcarchildregistcancle(prplcarchildregistcancleVo);
			}
		} catch (Exception e) {
			logger.debug("报案注销回写公估案件注销标志报错："+e.getMessage());
		}

		ModelAndView mv = new ModelAndView();
		mv.addObject("prpLRegistVo", prpLRegistVo);
		mv.addObject("prpLRegistExtVo", prpLRegistVo.getPrpLRegistExt());
		mv.setViewName("regist/registEdit/ReportEdit_CancelReturn");
		logger.info("报案号registNo={}结束报案注销回写RegistTaskFlag的方法",registNo);
		return mv;
	}
    	
	
    @RequestMapping(value = "/checkCode.do")
	@ResponseBody
	public AjaxResult checkCode(String BIPolicyNo,String BIRiskCode){
		List<PrpLCItemKindVo> vos = registService.findPrpLCItemKindByOther(BIPolicyNo,BIRiskCode);
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		ajaxResult.setData(vos.size()==0||vos==null ? 1 : 0);// 不能提交
		return ajaxResult;
	}
	@RequestMapping(value = "/checkCodes.do")
	@ResponseBody
	public AjaxResult checkCodes(String BIPolicyNo,String kindCode,String BIRiskCode){
		List<PrpLCItemKindVo> vos = registService.findPrpLCItemKindByOthers(BIPolicyNo,kindCode,BIRiskCode);
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		ajaxResult.setData(vos.size()==0||vos==null ? 1 : 0);// 不能提交
		return ajaxResult;
	}
	public void setSubmits(PrpLRegistVo registVo,String policyNo,BigDecimal flags){
		// 删除工作流立案的节点
		 BigDecimal upPerTaskId = wfTaskHandleService.findByRegist(registVo);
		
		// 提交工作流操作
		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(registVo.getFlowId());
		submitVo.setTaskInKey(registVo.getRegistNo());
		submitVo.setFlowTaskId(BigDecimal.ZERO);
		submitVo.setComCode(policyViewService.getPolicyComCode(registVo.getRegistNo()));
		//submitVo.setComCode(WebUserUtils.getComCode());
		submitVo.setAssignUser(WebUserUtils.getUserCode());
		submitVo.setTaskInUser(WebUserUtils.getUserCode());
		submitVo.setAssignCom(WebUserUtils.getComCode());
		submitVo.setSubmitType(SubmitType.U);
		// 创建新的立案
		submitVo.setFlowTaskId(flags);
		registVo.setPolicyNo(policyNo);
		if(flags != null){
			wfTaskHandleService.submitClaimHandls(registVo, submitVo);
		}
	}
	
	/**
	 * 报案提交时，历次出险出现过全车盗抢、推定全损，报案提交需给出软提示
	 * 
	 * <pre></pre>
	 * @param BIPolicyNo
	 * @param CIPolicyNo
	 * @return
	 * @modified: ☆WLL(2016年12月14日 上午10:46:03): <br>
	 */
	@RequestMapping(value = "/checkAllLossHis.ajax")
	@ResponseBody
	public AjaxResult checkAllLossHis(String BIPolicyNo,String CIPolicyNo){
		AjaxResult ajaxResult = new AjaxResult();
		Boolean checkCarAllLossFlag = false;
		if(BIPolicyNo!=null){
			checkCarAllLossFlag = lossCarService.checkCarAllLossHisByPolicyNo(BIPolicyNo);
		}
		if(!checkCarAllLossFlag&&CIPolicyNo!=null){
			// 如果商业保单已经有推定全损记录 不需要再核实 交强保单
			checkCarAllLossFlag = lossCarService.checkCarAllLossHisByPolicyNo(CIPolicyNo);
		}
		if(checkCarAllLossFlag){
			ajaxResult.setData("该保单存在全车盗抢、推定全损风险，请核实。");
		}else{
			ajaxResult.setData("NoAllLossHis");// 表示没有推定全损和全车盗抢的记录
		}
		ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK); 
		return ajaxResult;
	}
	
	@RequestMapping(value = "/addserviceInfo.do")
	public  ModelAndView addserviceInfo(){
		ModelAndView mv =new ModelAndView();
		mv.setViewName("regist/registEdit/ReportAddServiceInfo");
		
		return mv;
	}
	
	/**
	 * 案件注销发起
	 * @return
	 */
	@Timed("案件注销发起")
	@RequestMapping(value = "/registCancle.do")
	public  ModelAndView registCancle(String registNo){
		ModelAndView mv =new ModelAndView();
		String isclaim = "0";// 案件是否已立案，0-未立案，1-已立案
		String iscanceBI = "1";// 商业案件是否已注销发起，0-未发起，1-已发起
		String iscanceCI = "1";// 交强案件是否已注销发起，0-未发起，1-已发起
		String claimNoBI = "";// 商业立案号
		String claimNoCI = "";// 交强立案号
		List<PrpLClaimVo> prpLClaimVos = claimTaskService.findClaimListByRegistNo(registNo);
		if(prpLClaimVos!=null && prpLClaimVos.size()>0){
			isclaim="1";
			for(PrpLClaimVo vo:prpLClaimVos){
				if("1101".equals(vo.getRiskCode()) && "1".equals(vo.getValidFlag())){
					PrpLcancelTraceVo traceVo=claimCancelRecoverService.findByClaimNo(vo.getClaimNo());
					if(traceVo!=null && StringUtils.isNotBlank(traceVo.getFlags())){
						if("4".equals(traceVo.getFlags().trim()) || "11".equals(traceVo.getFlags().trim())){
							iscanceCI="0";
							claimNoCI=vo.getClaimNo();
						}
					}else if(traceVo==null){
						iscanceCI="0";
						claimNoCI=vo.getClaimNo();
					}
				}
				if(!"1101".equals(vo.getRiskCode()) && "1".equals(vo.getValidFlag())){
					PrpLcancelTraceVo traceVo=claimCancelRecoverService.findByClaimNo(vo.getClaimNo());
					if(traceVo!=null && StringUtils.isNotBlank(traceVo.getFlags())){
						if("4".equals(traceVo.getFlags().trim()) || "11".equals(traceVo.getFlags().trim())){
							iscanceBI="0";
							claimNoBI=vo.getClaimNo();
						}
					}else if(traceVo==null){
						iscanceBI="0";
						claimNoBI=vo.getClaimNo();
					}
					
				}
			}
		} 
		mv.addObject("registNo",registNo);
		mv.addObject("isclaim", isclaim);
		mv.addObject("iscanceBI",iscanceBI);
		mv.addObject("iscanceCI",iscanceCI);
		mv.addObject("claimNoBI",claimNoBI);
		mv.addObject("claimNoCI",claimNoCI);
		mv.setViewName("regist/registEdit/ReportEdit_CancleChose");
		
		return mv;
	}
	
	/**
	 * 在报案、调度页面保单信息增加显示业务板块、 业务分类，若业务板块为会员业务，业务分类为“ 30-南网员工、31-员工亲属、34-浦发员工、35-农行员工、 36-深圳海关员工”这5类客户，则业务板块和业务分类显示的值标红
	 * @param prpLCMainVo
	 * @author huanggusheng
	 */
	 public void business(PrpLCMainVo prpLCMainVo){
			Map<String,String> businessClassCheckMsg = codeTranService.findCodeNameMap("BusinessClassCheckMsg");
			if(businessClassCheckMsg.containsKey(prpLCMainVo.getBusinessClass())){																					
				prpLCMainVo.setMemberFlag("1");
			}
	 }
	 
	 
	 public PrpLCMainVo setPrpTempLCMain(List<PrpLCMainVo> prpLCMains){
		 if(prpLCMains != null && prpLCMains.size() == 2){
				for(PrpLCMainVo vo : prpLCMains){
				if("12".equals(vo.getRiskCode().substring(0,2))){// 取商业
						return vo;
					}
				}
			}else{
				return prpLCMains.get(0);
			}
		return prpLCMains.get(0);
	 }


	 
	 
	

	 
	 private void organizationAndSendCTorMTA(List<PrpLWfTaskVo> prpLWfTaskVos,PrpLRegistVo prpLRegistVo,RevokeInfoReqVo reqVo) {
         List<RevokeTaskInfoVo> revokeTaskInfoVos = new ArrayList<RevokeTaskInfoVo>();
         String url = null;
         RevokeBodyVo body = new RevokeBodyVo();
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
         Date date = prpLRegistVo.getCancelTime();
         String timeStamp = dateFormat.format(date);
         if(prpLWfTaskVos != null && prpLWfTaskVos.size() > 0){
             for(PrpLWfTaskVo prpLWfTask:prpLWfTaskVos){
                 RevokeTaskInfoVo revokeTaskInfoVo = new RevokeTaskInfoVo();
                 if("0".equals(prpLWfTask.getHandlerStatus())){
                     revokeTaskInfoVo.setTaskId(prpLWfTask.getHandlerIdKey());
                 }else if ("2".equals(prpLWfTask.getHandlerStatus())) {
                     if(FlowNode.DLoss.name().equals(prpLWfTask.getNodeCode())){
                         if(FlowNode.DLCar.name().equals(prpLWfTask.getSubNodeCode())){
                             PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(prpLWfTask.getHandlerIdKey()));
                             revokeTaskInfoVo.setTaskId(prpLDlossCarMainVo.getScheduleDeflossId().toString());
                         }else if(FlowNode.DLProp.name().equals(prpLWfTask.getSubNodeCode())){
                             PrpLdlossPropMainVo prpLdlossPropMainVo = propLossService.findPropMainVoById(Long.parseLong(prpLWfTask.getHandlerIdKey()));
							// 根据序号，报案号，定损类别查询
                             PrpLScheduleDefLossVo vo = scheduleTaskService.findPrpLScheduleDefLossVoByOther(prpLdlossPropMainVo.getRegistNo(),prpLdlossPropMainVo.getSerialNo(),"2");
                             revokeTaskInfoVo.setTaskId(vo.getId().toString());
                         }
                     }else {
                    	 
                         //PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.getScheduleTask(prpLRegistVo.getRegistNo(),ScheduleStatus.CHECK_SCHEDULED);
                         PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskByOther(prpLWfTask.getRegistNo(),"1","1");
                         revokeTaskInfoVo.setTaskId(prpLScheduleTaskVo.getId().toString());
                     }
                 }    
                 revokeTaskInfoVo.setRegistNo(prpLWfTask.getRegistNo());
                 revokeTaskInfoVo.setNodeType(prpLWfTask.getNodeCode());
                 revokeTaskInfoVo.setRevokeType("4");
                 revokeTaskInfoVo.setReason(prpLRegistVo.getPrpLRegistExt().getCancelReason());
                 revokeTaskInfoVo.setRemark("");
                 revokeTaskInfoVo.setTimeStamp(timeStamp);
                 revokeTaskInfoVos.add(revokeTaskInfoVo);
             }
         }      
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
         }
         finally{
			// 交互日志保存
             SysUserVo userVo = new SysUserVo();
             userVo.setComCode(WebUserUtils.getComCode());
             userVo.setUserCode(WebUserUtils.getUserCode());
             RegistInformationVo informationVo = new RegistInformationVo();
             informationVo.setRevokeInfoReqVo(reqVo);
             informationVo.setRevokeInfoResVo(resVo);
             informationVo.setSchType("4");
             if("CT_006".equals(reqVo.getHead().getRequestType())){
                 url = SpringProperties.getProperty("CT_URL")+CT_02;
             }
             carchildService.saveCTorMTACarchildInterfaceLog(informationVo,url,null,userVo);
			// 回写案件注销标志
             CarchildResponseHeadVo carChildResponseHead = resVo.getHead();
             if("1".equals(carChildResponseHead.getErrNo()+"")){
                 PrplcarchildregistcancleVo prplcarchildregistcancleVo = carchildService.findPrplcarchildregistcancleVoByRegistNo(prpLRegistVo.getRegistNo());
                 if(prplcarchildregistcancleVo!=null){
                     prplcarchildregistcancleVo.setExamineRusult("1");
                     prplcarchildregistcancleVo.setStatus("1");
                     prplcarchildregistcancleVo.setHandleUser(WebUserUtils.getUserName());
                     prplcarchildregistcancleVo.setHandleDate(date);
                     carchildService.updatePrplcarchildregistcancle(prplcarchildregistcancleVo);
                 }
                 
             }
         }
     }
	 
	 /**
	 * 验证是否符合节日翻倍险的应用条件
	 * @param damageDate
	 * @param registNo
	 * @return
	 */
	 private boolean validateFestivalRisk(Date damageDate,String registNo){
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTime(damageDate);
		 int weekDay=calendar.get(Calendar.DAY_OF_WEEK);
		 int year = calendar.get(Calendar.YEAR);
		 boolean flag=false;
		 List<PrpLCMainVo> cmainVoList=prpLCMainService.findPrpLCMainsByRegistNo(registNo);
		 PrpLfestivalVo festivalVo1= festivalService.findPrpLfestivalVoByFestivalType("3",String.valueOf(year));
		 PrpLfestivalVo festivalVo2= festivalService.findPrpLfestivalVoByFestivalType("4",String.valueOf(year));
		// 节日类型集合
		 String festivals=festivalVo1.getRemark();
		// 工作日周末类型集合
		 String weekDays=festivalVo2.getRemark();
		List<PrpLCItemKindVo> citemKindvoList = null;// 承保险别信息列表
		String signB2 = "0";// 是否买了假日翻倍险（B2）标志0-未买，1-买了
		     if(cmainVoList!=null && cmainVoList.size()>0){
		    	 for(PrpLCMainVo cmainVo:cmainVoList){
		    		 if(!"1101".equals(cmainVo.getRiskCode())){
		    			 citemKindvoList=cmainVo.getPrpCItemKinds();
		    			 if(citemKindvoList!=null && citemKindvoList.size()>0){
		    				 for(PrpLCItemKindVo kindVo:citemKindvoList){
		    					 if("B2".equals(kindVo.getKindCode())){
		    						 signB2="1";
		    					 }
		    				 }
		    			 }
		    		 }
		    	 }
		     }
		// 判断是否符合假日翻倍险应用条件
		// 1、出险时间为国家法定节假日，并且承保法定节假日翻倍险则，三者险翻倍
		// 2、出险时间非国家法定节假日，或未承保法定节假日翻倍险则，三者险不翻倍
		    if(((weekDay==1 || weekDay==7) || festivals.contains(DateFormatString(damageDate))) && !weekDays.contains(DateFormatString(damageDate)) && "1".equals(signB2)){
		    	flag=true;
		    }
		     
		 return flag;
	 }
	 
    /**
	 * 时间转换方法 Date 类型转换 String类型
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private String DateFormatString(Date strDate){
		String str="";
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		  if(strDate!=null){
			  str=format.format(strDate);
		}
		  
		return str;
	}
	
	@RequestMapping(value = "/accidentInfo.do")
	@ResponseBody
	public AjaxResult accidentInfo(String registNo){
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo userVo = WebUserUtils.getUser();
		try {
			CiitcAccidentResVo ciitcAccidentResVo = accidentService.findAccidentInfoByRegistNo(registNo, userVo);
			if(ciitcAccidentResVo != null && ciitcAccidentResVo.getHead() != null &&
					"00000".equals(ciitcAccidentResVo.getHead().getErrorCode())){
				ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
				ajaxResult.setData("1");
			}else{
				ajaxResult.setStatus(org.apache.http.HttpStatus.SC_NOT_FOUND);
				ajaxResult.setData("0");
			}
		} catch (Exception e) {
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_NOT_FOUND);
			ajaxResult.setData("0");
		}
		return ajaxResult;
	}

	@RequestMapping(value = "/getDisasterTwoInfo.ajax")
	@ResponseBody
	public AjaxResult initDisasterTwos(String disasterOneCode) {
		AjaxResult ajaxResult = new AjaxResult();
		if (StringUtils.isBlank(disasterOneCode)) {
			ajaxResult.setStatus(HttpStatus.SC_NOT_FOUND);
			return ajaxResult;
		}
		List<String> list = registService.findLevelTwoDisasterInfo(disasterOneCode);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(list);

		return ajaxResult;
	}
}
