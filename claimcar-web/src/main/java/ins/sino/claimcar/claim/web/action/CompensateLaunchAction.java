package ins.sino.claimcar.claim.web.action;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.support.Page;
import ins.framework.utils.Beans;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.support.ObjectToMapCallback;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.PayFlagType;
import ins.sino.claimcar.CodeConstants.YN01;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.CompensateHandleServiceIlogService;
import ins.sino.claimcar.claim.service.CompensateLaunchService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindVo;
import ins.sino.claimcar.claim.vo.PrpLClaimResultVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLKindAmtSummaryVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.claim.vo.SubmitNextVo;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.rule.vo.VerifyClaimRuleVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lanlei
 *
 */
@Controller
@RequestMapping("/compensate")
public class CompensateLaunchAction {
	private Logger logger = LoggerFactory.getLogger(CompensateLaunchAction.class);
	@Autowired
	private CompensateTaskService compensatateService;
	@Autowired
	private CompensateLaunchService compensateLaunchService;
	@Autowired
	private ClaimService claimService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private EndCasePubService endCasePubService;
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	@Autowired
	private AssignService assignService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private PersTraceService persTraceServices;
    @Autowired
    PrpLCMainService prpLCMainService;
    @Autowired
    CertifyPubService certifyPubService;
    @Autowired
    CompensateHandleServiceIlogService conpensateHandleServiceIlogService;
    @Autowired
    RegistService registService;
	/** ???????????????????????????*/
	public static final String COMP_CI = "1";
	/** ???????????????????????????*/
	public static final String COMP_BI = "2";
	/** ??????????????????*/
	public static final String PREFIX_CI = "11";
	/** ??????????????????*/
	public static final String PREFIX_BI = "12";
	
	
	/**
	 * ????????????????????????
	 * ????????????????????????????????? ?????????????????????
	 * @return
	 */
	@RequestMapping("/compensateLaunchSubmit.do")
	@ResponseBody
	public AjaxResult compensateLaunchSubmit(
			HttpServletRequest request,
			@FormModel(value = "prpLCompensate") List<PrpLCompensateVo> prpLCompensateVos,
			@FormModel(value = "bcFlag") String bcFlag) {
		//ModelAndView mv = new ModelAndView();
		AjaxResult ajaxResult = new AjaxResult();
		System.out.println(bcFlag);
		Map<String, Object> compMap = new HashMap<String, Object>();
		SysUserVo userVo = WebUserUtils.getUser();
		String compNo = "";
		//????????????
		// ???????????????????????????
		String reMsg = compensateLaunchService.validCheckComp(prpLCompensateVos.get(0).getRegistNo(), bcFlag);
		///reMsg="OK";
		//???????????????????????????????????????
		//List<PrpLWfTaskVo> prpLWfTaskVo= wfTaskHandleService.findCompeByRegistNo(prpLCompensateVos.get(0).getClaimNo());
		/*if(prpLWfTaskVo.size()>0||prpLWfTaskVo!=null){
			reMsg="????????????";
		}*/
		if (reMsg.equals("OK")) {
				// TODO ????????????????????????????????????
	
			
			List<PrpLCMainVo> cMainList = new ArrayList<PrpLCMainVo>();
			cMainList = policyViewService.getPolicyAllInfo(prpLCompensateVos.get(0)
					.getRegistNo());// ?????????????????????????????????????????????
			// ?????????????????????
			String CIPolicyNo = null;
			String BIPolicyNo = null;
			for (PrpLCMainVo cMain : cMainList) {
				if (cMain.getRiskCode().startsWith(PREFIX_CI)) {
					CIPolicyNo = cMain.getPolicyNo();
				}
				if (cMain.getRiskCode().startsWith(PREFIX_BI)) {
					BIPolicyNo = cMain.getPolicyNo();
				}
			}
	
			
			// TODO ?????????????????????
			String yewuNo = prpLCompensateVos.get(0).getRegistNo();
			if (bcFlag.equals("BI") || bcFlag.equals("CI")) {
				String handlerUser = request.getParameter("handlerUser");
				System.out.println(handlerUser);
				if (bcFlag.equals("CI")) {
					//???????????????????????????????????????
					List<PrpLWfTaskVo> prpLWfTaskVo= wfTaskHandleService.findCompeByRegistNo(yewuNo,"CompeCI");
					if(prpLWfTaskVo!=null && prpLWfTaskVo.size()>0){
						reMsg="??????????????????????????????";
					}else{
						prpLCompensateVos.get(0).setCreateUser(handlerUser);
						// TODO ??????????????????????????????????????????????????????Map,key=CI
						PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(prpLCompensateVos.get(0).getRegistNo(), CIPolicyNo);
						//compNo = billNoService.getCompensateNo(SecurityUtils.getComCode(),prpLClaimVo.getRiskCode());
						//compMap.put("CI", compNo);
						//???????????????
						compensateLaunchService.getSubmit(prpLClaimVo,compNo,FlowNode.CompeCI,FlowNode.ClaimCI,userVo);
						reMsg="yewuNo";
					}
					
				}
				if (bcFlag.equals("BI")) {
					//???????????????????????????????????????
					List<PrpLWfTaskVo> prpLWfTaskVo= wfTaskHandleService.findCompeByRegistNo(yewuNo,"CompeBI");
					if(prpLWfTaskVo!=null && prpLWfTaskVo.size()>0){
						reMsg="??????????????????????????????";
					}else{
						prpLCompensateVos.get(1).setCreateUser(handlerUser);
						// TODO ??????????????????????????????????????????????????????Map,key=BI
						PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(prpLCompensateVos.get(0).getRegistNo(), BIPolicyNo);
//						compNo = billNoService.getCompensateNo(SecurityUtils.getComCode(),prpLClaimVo.getRiskCode());
//						compMap.put("BI", compNo);
						//???????????????
						compensateLaunchService.getSubmit(prpLClaimVo,compNo,FlowNode.CompeBI,FlowNode.ClaimBI,userVo);
						reMsg="yewuNo";
					}
				}
			}
			if (bcFlag.equals("BC")) {
				List<PrpLWfTaskVo> prpLWfTaskVo1= wfTaskHandleService.findCompeByRegistNo(yewuNo,"CompeBI");
				List<PrpLWfTaskVo> prpLWfTaskVo2= wfTaskHandleService.findCompeByRegistNo(yewuNo,"CompeBI");
				if((prpLWfTaskVo1!=null && prpLWfTaskVo1.size()>0)|| (prpLWfTaskVo2!=null && prpLWfTaskVo2.size()>0)){
					reMsg="??????????????????????????????";
				}else{
					// ????????????????????????????????????????????????
					PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(prpLCompensateVos.get(0).getRegistNo(), CIPolicyNo);
//					compNo = billNoService.getCompensateNo(SecurityUtils.getComCode(),prpLClaimVo.getRiskCode());
//					compMap.put("CI", compNo);
					compensateLaunchService.getSubmit(prpLClaimVo,compNo,FlowNode.CompeCI,FlowNode.ClaimCI,userVo);
					prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(prpLCompensateVos.get(0).getRegistNo(), BIPolicyNo);
//					compNo = billNoService.getCompensateNo(SecurityUtils.getComCode(),prpLClaimVo.getRiskCode());
//					compMap.put("BI", compNo);
					compensateLaunchService.getSubmit(prpLClaimVo,compNo,FlowNode.CompeBI,FlowNode.ClaimBI,userVo);
					reMsg="yewuNo";
				}
			}
			/*ajaxResult.setData(yewuNo);*/
			if(reMsg.equals("??????????????????????????????")){
				ajaxResult.setStatus(HttpStatus.SC_OK);
				ajaxResult.setData(reMsg);
				ajaxResult.setStatusText("NO");
			return ajaxResult;
			}else{
			ajaxResult.setData(reMsg);
			ajaxResult.setDatas(compMap);
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setStatusText("OK");
			return ajaxResult;
			}
			} else {
			
			// ????????????????????????????????????????????????
				ajaxResult.setStatus(HttpStatus.SC_OK);
				ajaxResult.setData(reMsg);
				ajaxResult.setStatusText("NO");
			return ajaxResult;
	}
		//end

	/*	mv.addObject("yewuNo", yewuNo);
		mv.addObject("compMap", compMap);
		mv.addObject("status", 200);
		mv.setViewName("compensate/CompensateLaunchEdit_compLink");

		return mv;*/
		//ajaxResult.setData(yewuNo);
		
	}
	
	// ?????????
	@RequestMapping("/showCompLink.do")
	public ModelAndView showCompLink(@FormModel(value = "Bi") String Bi,@FormModel(value = "yewuNo") String yewuNo,@FormModel(value = "Ci") String Ci) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("Ci", Ci);
		mv.addObject("Bi", Bi);
		mv.addObject("yewuNo", yewuNo);
		mv.setViewName("compensate/CompensateLaunchEdit_compLink");

		return mv;
	}
	/**
	 * ??????????????????????????????
	 * 
	 * @param request
	 * @param prpLCompensateVos
	 * @return
	 */
	@RequestMapping("/compensateLaunchAutoSubmit.do")
	@ResponseBody
	public AjaxResult compensateLaunchAutoSubmit(
			HttpServletRequest request,
			@FormModel(value = "prpLCompensate") List<PrpLCompensateVo> prpLCompensateVos) {
		AjaxResult ar = new AjaxResult();
		System.out.print("kkkkkkkkkkkkkkk");
		String registNo = "";
		if (prpLCompensateVos.size() > 0) {
			registNo = prpLCompensateVos.get(0).getRegistNo();
		}
		String bcFlag = "";
		int bi = 0, ci = 0;
		List<PrpLClaimKindVo> claimKinds = claimService
				.findClaimKindVoListByRegistNo(registNo);
		// ????????????????????????????????????????????????????????????????????????????????????
		for (PrpLClaimKindVo claimKind : claimKinds) {
			if (claimKind.getRiskCode().substring(0, 2).equals("11")) {
				ci++;
			}
			if (claimKind.getRiskCode().substring(0, 2).equals("12")) {
				bi++;
			}
		}
		if (ci > 0 && bi == 0) {
			bcFlag = "CI";
		}
		if (ci == 0 && bi > 0) {
			bcFlag = "BI";
		}
		if (ci > 0 && bi > 0) {
			bcFlag = "BC";
		}
		// ???????????????????????????
		String reMsg = compensateLaunchService.validCheckComp(registNo, bcFlag);
		String compNo = "";
		String handlerUser = request.getParameter("handlerUser");
		System.out.println(handlerUser);
		if (reMsg.equals("OK")) {
			// TODO ????????????????????????????????????
			//???????????????start
			getSubmitCompe(prpLCompensateVos,bcFlag,handlerUser);
			
			//end
		} else {
			if (reMsg.equals("padPayFlag")) {
				// TODO ???????????????????????????????????? ?????????????????????????????????????????????
				prpLCompensateVos.get(0).setLawsuitFlag("???");
				getSubmitCompe(prpLCompensateVos,bcFlag,handlerUser);
			} else {
				// ????????????????????????????????????????????????
				ar.setStatus(201);
				ar.setData(reMsg);
				return ar;
			}
		}
		// ar.setStatus(HttpStatus.SC_CONTINUE);
		ar.setData("ssddddgggg");
		return ar;
	}

	/**
	 * ????????????????????????????????????
	 */
	public void getSubmitCompe(List<PrpLCompensateVo> prpLCompensateVos,String bcFlag,String handlerUser){
		
	}
	
	/**
	 * ?????????????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping("/compensateLaunchEdit.do")
	public ModelAndView compensateLaunchEdit(String registNo) {
		ModelAndView modelAndView = new ModelAndView();
		//String registNo = "4000000201611010000153";
		List<PrpLCompensateVo> compList = new ArrayList<PrpLCompensateVo>();
		List<PrpLCMainVo> cMainList = new ArrayList<PrpLCMainVo>();
		PrpLRegistVo registVo = new PrpLRegistVo();
		String insuredName = "";
		if (!StringUtils.isBlank(registNo)) {
			// ?????????????????????????????????????????????
			// underwriteFlag--????????????--??????????????????????????????????????????
			compList = compensatateService.findCompByRegistNo(registNo);
			registVo = registQueryService.findByRegistNo(registNo);// ????????????????????????????????????Vo
			cMainList = policyViewService.getPolicyAllInfo(registNo);// ?????????????????????????????????????????????
		}
		if (cMainList.size() > 0) {
			// ????????????????????????????????????????????????????????????
			insuredName = cMainList.get(0).getInsuredName();// ???????????????????????????????????????????????????
		}
		// ?????????????????????
		String CIPolicyNo = null;
		String BIPolicyNo = null;
		for (PrpLCMainVo cMain : cMainList) {
			if (cMain.getRiskCode().startsWith(PREFIX_CI)) {
				CIPolicyNo = cMain.getPolicyNo();
			}
			if (cMain.getRiskCode().startsWith(PREFIX_BI)) {
				BIPolicyNo = cMain.getPolicyNo();
			}
		}
		// ????????????????????????????????????
		List<PrpLCompensateVo> compCIList = new ArrayList<PrpLCompensateVo>();
		List<PrpLCompensateVo> compBIList = new ArrayList<PrpLCompensateVo>();
		for (PrpLCompensateVo compVo : compList) {
			if(compVo.getUnderwriteFlag().equals(CodeConstants.UnderWriteFlag.NORMAL)){
				if (compVo.getRiskCode().substring(0, 2).equals(PREFIX_CI)) {// ??????????????????
					compCIList.add(compVo);
				}
				if (compVo.getRiskCode().substring(0, 2).equals(PREFIX_BI)) {// ??????????????????
					compBIList.add(compVo);
				}
			}
		}

		// ??????-?????????????????? N-????????? Y-?????????
		String endCaseCI = "N";
		String endCaseBI = "N";
		// ??????/??????-????????????-???????????????????????????PrpLEndcase??????????????????????????????
		
		  List<PrpLEndCaseVo> endCaseVos = new ArrayList<PrpLEndCaseVo>();
		  endCaseVos = endCasePubService.queryAllByRegistNo(registNo); 
//		  if(endCaseVos!=null&&endCaseVos.size()>0){
//			  for (PrpLEndCaseVo endCaseVo : endCaseVos) { 
//				  if (endCaseVo.getRiskCode().substring(0,2).equals(PREFIX_CI)) { 
//					  endCaseCI = "Y"; 
//					  } 
//				  if(endCaseVo.getRiskCode().substring(0, 2).equals(PREFIX_BI)) { 
//					  endCaseBI = "Y"; 
//					  } 
//				} 
//		  }
		  
		 

		// ??????????????????-???????????????????????????PrpLRecase??????????????????????????????
		
		 /* List<PrpLReCaseVo> recaseVos = recaseService.findRecaseByRegistNo(registNo); 
		  for (PrpLReCaseVo recaseVo : recaseVos) { 
			  if (recaseVo.getRiskCode().substring(0, 2).equals("11")){ 
				  endCaseCI = "N"; 
				  } 
			  if (recaseVo.getRiskCode().substring(0,2).equals("12")) { 
				  endCaseBI = "N"; 
				  } 
			  }*/
		 
		// ????????????????????????
		String autoFlag = compensateLaunchService.validCheckCompAuto(registNo);

		modelAndView.addObject("endCaseCI", endCaseCI);
		modelAndView.addObject("endCaseBI", endCaseBI);
		modelAndView.addObject("autoFlag", autoFlag);

		modelAndView.addObject("CIPolicyNo", CIPolicyNo);
		modelAndView.addObject("BIPolicyNo", BIPolicyNo);

		modelAndView.addObject("registVo", registVo);
		modelAndView.addObject("insuredName", insuredName);
		modelAndView.addObject("compCIList", compCIList);
		modelAndView.addObject("compBIList", compBIList);
		modelAndView.setViewName("compensate/CompensateLaunchEdit");
		return modelAndView;
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping("/compLaunCheck.ajax")
	@ResponseBody
	public AjaxResult compLaunCheck(HttpServletRequest request) {
		AjaxResult ar = new AjaxResult();
		String registNo = request.getParameter("registNo");
		String bcFlag = request.getParameter("bcFlag");
		String reMsg = "???????????????";
		if (StringUtils.isNotBlank(registNo)) {
			reMsg = compensateLaunchService.validCheckComp(registNo, bcFlag);
		}

		ar.setStatus(HttpStatus.SC_OK);
		ar.setData(reMsg);
		return ar;
	}

	
	/**
	 * ?????????????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping("/compensateList.do")
	@ResponseBody
	public ModelAndView compensateList() {
		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("compensate/CompensateList");
		return modelAndView;
	}
		
	/**
	 * ????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping("/compensatefindList.do")
	@ResponseBody
	public String search(
			@FormModel(value="prpLClaimVo")PrpLClaimVo prpLClaimVo,
			@RequestParam(value="start",defaultValue="0")Integer start,
			@RequestParam(value="length",defaultValue="10")Integer length){
		Page<PrpLClaimResultVo> list = compensateLaunchService
				.findCompensateLaunchByHql(prpLClaimVo, start, length);
		String jsonData = ResponseUtils.toDataTableJson(list,searchCallBack(),"mercyFlag:MercyFlag",
				"registNo", "claimNo", "policyNo","insuredName",
				"comCode:ComCode","createUser:UserCode");
		return jsonData;
	}
	
	private ObjectToMapCallback searchCallBack() {
		ObjectToMapCallback callBack = new ObjectToMapCallback() {

			public Map<String,Object> toMap(Object object) {
				Map<String,Object> dataMap = new HashMap<String,Object>();
				PrpLClaimResultVo resultVo = (PrpLClaimResultVo)object;

				StringBuffer bussTagHtml = new StringBuffer();
				// bussTagHtml.append("<span class='badge label-danger radius '>vip</span>");
				if(YN01.Y.equals(resultVo.getMercyFlag())){
					bussTagHtml.append("<span class='badge label-warning radius' title='??????'>???</span>");
				}
				dataMap.put("bussTagHtml",bussTagHtml.toString());
				return dataMap;
			}
		};
		return callBack;
	}

	/**
	 * ?????????????????????????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping("/compeWriteOffLaunQueryList.do")
	@ResponseBody
	public ModelAndView compeWriteOfflaunList() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("compensate/CompWriteOffLaunQueryList");
		return modelAndView;
	}

	/**
	 * ??????????????????????????????
	 * @param 
	 * @param start
	 * @param length
	 * @author WLL
	 * @return
	 */
	@RequestMapping("/compeWriteOffLaunFind.do")
	@ResponseBody
	public String compeWriteOfflaunFind(
			@FormModel(value = "prpLCompensate") PrpLCompensateVo compVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) {
		SysUserVo userVo = WebUserUtils.getUser();
		ResultPage<PrpLCompensateVo> resPage = compensateLaunchService.findAllCompForWriteOff(compVo,start,length,userVo);
		String jsonData = ResponseUtils.toDataTableJson(resPage, "compensateKind:CompKind",
				"registNo", "compensateNo", "claimNo","comCode:ComCode", "underwriteDate", "createUser:UserCode");
		return jsonData;
	}
	/**
	 * ????????????????????????????????????????????????????????????
	 * <pre></pre>
	 * @param registNo
	 * @param compensateNo
	 * @param claimNo
	 * @return
	 * @modified:
	 * ???WLL(2016???8???30??? ??????9:21:36): <br>
	 */
	@RequestMapping("/compeWriteOff.do")
	@ResponseBody
	public ModelAndView compeWriteOff(String registNo,String compensateNo,String claimNo ) {
			ModelAndView modelAndView = new ModelAndView();
			//?????????
			PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
			List<PrpLCMainVo> prpLCMains = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
            PrpLCMainVo prpLCMain = new PrpLCMainVo();
            if(prpLCMains != null && prpLCMains.size() == 2){
                for(PrpLCMainVo vo : prpLCMains){
                   if("12".equals(vo.getRiskCode().substring(0, 2))){//?????????
                        prpLCMain = vo;
                    }
                 }
               }else{
                     prpLCMain = prpLCMains.get(0);
            }
            SysUserVo userVo = WebUserUtils.getUser();
            modelAndView.addObject("prpLCMain",prpLCMain);
            modelAndView.addObject("userVo", userVo);
            modelAndView.addObject("riskCode", registVo.getRiskCode());
			if(compensateNo!=null&&compensateNo!=""){
				PrpLCompensateVo compVo = compensatateService.findCompByPK(compensateNo);
				String subNodeCode = FlowNode.CompeWfBI.name();
				FlowNode compeNodeCode = FlowNode.CompeBI;
				if("1101".equals(compVo.getRiskCode())){
					subNodeCode = FlowNode.CompeWfCI.name();
					compeNodeCode = FlowNode.CompeCI;
				}
				PrpLWfTaskVo wfTaskVo = new PrpLWfTaskVo();
				//??????????????????????????????????????????????????????????????????
				wfTaskVo.setRegistNo(registNo);
				wfTaskVo.setWorkStatus("0");
				List<PrpLWfTaskVo> oldTaskVoList = wfTaskHandleService.findEndTask(registNo, compensateNo, compeNodeCode);
				if(oldTaskVoList != null && oldTaskVoList.size() > 0 ){
					PrpLWfTaskVo oldTaskVo = oldTaskVoList.get(0);
					List<PrpLWfTaskVo> compeWfCITaskVoList = wfTaskHandleService.findAllTaskVo(registNo, oldTaskVo.getTaskId(), subNodeCode);
					if(compeWfCITaskVoList != null && compeWfCITaskVoList.size() > 0){
						wfTaskVo = compeWfCITaskVoList.get(0);
					}
				}
				
				
				// ???????????????????????????????????????????????????????????????????????????????????????
				PrpLCompensateVo compVoWriteOff = compensateLaunchService.findWriteOffCompensateVoByOriCompNo(compensateNo);
				if(compVoWriteOff!=null){
					compVo = Beans.copyDepth().from(compVoWriteOff).to(PrpLCompensateVo.class);
				}
				//????????????
				List<PrpLClaimDeductVo> claimDeductVos = compensatateService.findDeductCond(registNo, compVo.getRiskCode());
				
				String flag = COMP_BI;
				String bcFlag = PREFIX_BI;
				if(compVo.getRiskCode().equals("1101")){
					flag = COMP_CI;
					bcFlag = PREFIX_CI;
					/**
					 * ???????????????
					 */
					List<PrpLPadPayPersonVo> padPayPersons = new ArrayList<PrpLPadPayPersonVo>();
					if (flag.equals(COMP_CI)) {// ????????????
						padPayPersons = compensatateService.getPadPayPersons(registNo,"0");
						modelAndView.addObject("prpLPadPayPersons", padPayPersons);
					}
				}
				//?????????
				Map<String,List<PrpLPrePayVo>> PrePayMap;
				try{
					PrePayMap = compensatateService.getPrePayMap(registNo, bcFlag,"0");
					modelAndView.addObject("prpLPrePayP", PrePayMap.get("prePay"));
					modelAndView.addObject("prpLPrePayF", PrePayMap.get("preFee"));
				}catch(Exception e){
					logger.error("??????????????????????????????????????????????????????????????????????????????????????????,registno="+registNo,e);
				}
				
				List<PrpLPaymentVo> prpLPayments = compVo.getPrpLPayments();
				for(PrpLPaymentVo payMentVo:prpLPayments){
					PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(payMentVo.getPayeeId());
					// ?????????voCustom??????????????????
					payMentVo.setPayeeName(payCustomVo.getPayeeName());
					payMentVo.setAccountNo(payCustomVo.getAccountNo());
					payMentVo.setBankName(payCustomVo.getBankName());
				}
				for(PrpLChargeVo chargeVo:compVo.getPrpLCharges()){
					PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(chargeVo.getPayeeId());
					// ?????????chargeVo??????????????????
					chargeVo.setPayeeName(payCustomVo.getPayeeName());
					chargeVo.setAccountNo(payCustomVo.getAccountNo());
					chargeVo.setBankName(payCustomVo.getBankName());
					chargeVo.setPayeeIdfNo(payCustomVo.getCertifyNo());
				}
				List<PrpLLossPropVo> otherLossProps = new ArrayList<PrpLLossPropVo>();
				
				//????????????????????????
			    String isMinorInjuryCases="";
			    List<PrpLDlossPersTraceMainVo> traceMains=persTraceServices.findPersTraceMainVo(registNo);
			    	if(traceMains!=null && traceMains.size()>0){
			    		for(PrpLDlossPersTraceMainVo traceMainVo:traceMains){
			    			isMinorInjuryCases=traceMainVo.getIsMinorInjuryCases();
			    			
			    		}
			    	}
			   if(StringUtils.isBlank(isMinorInjuryCases)){
			    	isMinorInjuryCases="0";
			    }
				
				//????????????
			    PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
			    String isFraud = "0"; // ???????????? ???
			    String fraudLogo = "03";  //????????????  ???????????? 
			    String isRefuse = "0";  //????????????
			    String refuseReason = "";
			    String compType = "1";
				if (certifyMainVo != null) {
					isFraud = certifyMainVo.getIsFraud();
					fraudLogo = certifyMainVo.getFraudLogo();
					if (flag.equals(COMP_CI)) {
						isRefuse = certifyMainVo.getIsJQFraud();
						compType = "1";
					} else if (flag.equals(COMP_BI)) {
						isRefuse = certifyMainVo.getIsSYFraud();
						compType = "2";
					}
					
					refuseReason = certifyMainVo.getFraudRefuseReason();
				}
				if(compVoWriteOff!=null){
					
					PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);

					if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){
						if(prpLPayments != null && prpLPayments.size()>0){
							for(int i=0; i<prpLPayments.size();i++){
								if(prpLPayments.get(i).getAccountNo() != null){
									prpLPayments.get(i).setAccountNo(DataUtils.replacePrivacy(prpLPayments.get(i).getAccountNo()));
								}
							}
						}
						if( compVo.getPrpLCharges() != null &&  compVo.getPrpLCharges().size()>0){
							for(int i=0; i< compVo.getPrpLCharges().size();i++){
								if( compVo.getPrpLCharges().get(i).getAccountNo() != null){
									 compVo.getPrpLCharges().get(i).setAccountNo(DataUtils.replacePrivacy( compVo.getPrpLCharges().get(i).getAccountNo()));
									 compVo.getPrpLCharges().get(i).setPayeeIdfNo(DataUtils.replacePrivacy(compVo.getPrpLCharges().get(i).getPayeeIdfNo()));
								}
							}
						}
					}
					
					for(PrpLLossPropVo lossPropVo:compVo.getPrpLLossProps()){
						if("9".equals(lossPropVo.getPropType())){
							otherLossProps.add(lossPropVo);
						}
					}
					String reOpenFlag="0";
					PrpLWfTaskVo taskVo2=wfTaskHandleService.findOutWfTaskVo(FlowNode.ReOpen.name(),registNo);
					PrpLCompensateVo compenVo=compensatateService.findCompByPK(compensateNo);
					if(taskVo2!=null){//?????????????????????
						if(compenVo.getPrpLCompensateExt()!=null && StringUtils.isNotBlank(compenVo.getPrpLCompensateExt().getOppoCompensateNo())){
							PrpLCompensateVo compenLinkVo=compensatateService.findCompByPK(compenVo.getPrpLCompensateExt().getOppoCompensateNo());
							if(compenLinkVo.getCreateTime().getTime()>=taskVo2.getTaskOutTime().getTime()){
								reOpenFlag="1";
							}
						}else{//???????????????
							if(compenVo.getCreateTime().getTime()>=taskVo2.getTaskOutTime().getTime()){
								reOpenFlag="1";
							}
						}
						
					}
					modelAndView.addObject("reOpenFlag",reOpenFlag);
					modelAndView.addObject("compType",compType);  
					modelAndView.addObject("refuseReason",refuseReason);  
					modelAndView.addObject("isRefuse",isRefuse);
					modelAndView.addObject("isFraud",isFraud);  
				    modelAndView.addObject("fraudLogo",fraudLogo);  
				    modelAndView.addObject("isMinorInjuryCases",isMinorInjuryCases);
					modelAndView.addObject("flag", flag);
					modelAndView.addObject("workStatus", wfTaskVo.getWorkStatus());
					modelAndView.addObject("prpLWfTaskVo", wfTaskVo);
					modelAndView.addObject("compWfFlag","1");//?????????????????????
					modelAndView.addObject("claimDeductVos", claimDeductVos);	
					modelAndView.addObject("prpLCompensate", compVo);
					modelAndView.addObject("prpLPaymentVos", prpLPayments);
					modelAndView.addObject("prpLLossItems", compVo.getPrpLLossItems());
					modelAndView.addObject("otherLossProps", otherLossProps);
					modelAndView.addObject("prpLLossProps", compVo.getPrpLLossProps());
					modelAndView.addObject("prpLLossPersons", compVo.getPrpLLossPersons());
					modelAndView.addObject("prpLChargeVos", compVo.getPrpLCharges());
					modelAndView.setViewName("compensate/CompensateEdit");
					return modelAndView;
				}
				
				compVo.setSumAmt(processAmt(compVo.getSumAmt()));
				compVo.setSumLoss(processAmt(compVo.getSumLoss()));
				compVo.setSumPreAmt(processAmt(compVo.getSumPreAmt()));
				compVo.setSumPaidAmt(processAmt(compVo.getSumPaidAmt()));
				compVo.setSumFee(processAmt(compVo.getSumFee()));
				compVo.setSumPreFee(processAmt(compVo.getSumPreFee()));
				compVo.setSumPaidFee(processAmt(compVo.getSumPaidFee()));
				compVo.setSumBzPaid(processAmt(compVo.getSumBzPaid()));
				compVo.setSumRealPay(processAmt(compVo.getSumRealPay()));
				List<PrpLLossItemVo> prpLLossItems = compVo.getPrpLLossItems();
				
				for(PrpLLossItemVo lossCarVo:prpLLossItems){
					//??????????????????????????????
					lossCarVo.setSumRealPay(processAmt(lossCarVo.getSumRealPay()));
					lossCarVo.setSumLoss(processAmt(lossCarVo.getSumLoss()));
					lossCarVo.setBzPaidRescueFee(processAmt(lossCarVo.getBzPaidRescueFee()));
					lossCarVo.setBzPaidLoss(processAmt(lossCarVo.getBzPaidLoss()));
					lossCarVo.setOffPreAmt(processAmt(lossCarVo.getOffPreAmt()));
					lossCarVo.setAbsolvePayAmt(processAmt(lossCarVo.getAbsolvePayAmt()));
					lossCarVo.setDeductDutyAmt(processAmt(lossCarVo.getDeductDutyAmt()));
					lossCarVo.setDeductAbsAmt(processAmt(lossCarVo.getDeductAbsAmt()));
					lossCarVo.setDeductAddAmt(processAmt(lossCarVo.getDeductAddAmt()));
					lossCarVo.setOtherDeductAmt(processAmt(lossCarVo.getOtherDeductAmt()));
					lossCarVo.setDeductOffAmt(processAmt(lossCarVo.getDeductOffAmt()));
					lossCarVo.setRescueFee(processAmt(lossCarVo.getRescueFee()));
				}
				List<PrpLLossPropVo> prpLLossProps = compVo.getPrpLLossProps();
				
				for(PrpLLossPropVo lossPropVo:prpLLossProps){
					lossPropVo.setSumRealPay(processAmt(lossPropVo.getSumRealPay()));
					lossPropVo.setSumLoss(processAmt(lossPropVo.getSumLoss()));
					lossPropVo.setBzPaidRescueFee(processAmt(lossPropVo.getBzPaidRescueFee()));
					lossPropVo.setBzPaidLoss(processAmt(lossPropVo.getBzPaidLoss()));
					lossPropVo.setOffPreAmt(processAmt(lossPropVo.getOffPreAmt()));
					lossPropVo.setDeductDutyAmt(processAmt(lossPropVo.getDeductDutyAmt()));
					lossPropVo.setDeductAbsAmt(processAmt(lossPropVo.getDeductAbsAmt()));
					lossPropVo.setDeductAddAmt(processAmt(lossPropVo.getDeductAddAmt()));
					lossPropVo.setOtherDeductAmt(processAmt(lossPropVo.getOtherDeductAmt()));
					lossPropVo.setDeductOffAmt(processAmt(lossPropVo.getDeductOffAmt()));
					lossPropVo.setRescueFee(processAmt(lossPropVo.getRescueFee()));
					if("9".equals(lossPropVo.getPropType())){
						otherLossProps.add(lossPropVo);
					}
				}

				List<PrpLLossPersonVo> prpLLossPersons = compVo.getPrpLLossPersons();
				for(PrpLLossPersonVo lossPersVo:prpLLossPersons){
					lossPersVo.setSumRealPay(processAmt(lossPersVo.getSumRealPay()));
					lossPersVo.setSumLoss(processAmt(lossPersVo.getSumLoss()));
					lossPersVo.setSumOffLoss(processAmt(lossPersVo.getSumOffLoss()));
					lossPersVo.setBzPaidLoss(processAmt(lossPersVo.getBzPaidLoss()));
					lossPersVo.setOffPreAmt(processAmt(lossPersVo.getOffPreAmt()));
					lossPersVo.setDeductDutyAmt(processAmt(lossPersVo.getDeductDutyAmt()));
					lossPersVo.setDeductAbsAmt(processAmt(lossPersVo.getDeductAbsAmt()));
					lossPersVo.setDeductAddAmt(processAmt(lossPersVo.getDeductAddAmt()));
					//????????????????????????????????????
					for(PrpLLossPersonFeeVo persFeeVo:lossPersVo.getPrpLLossPersonFees()){
						persFeeVo.setBzPaidLoss(processAmt(persFeeVo.getBzPaidLoss()));
						persFeeVo.setFeeLoss(processAmt(persFeeVo.getFeeLoss()));
						persFeeVo.setFeeRealPay(processAmt(persFeeVo.getFeeRealPay()));
						persFeeVo.setFeeOffLoss(processAmt(persFeeVo.getFeeOffLoss()));
						persFeeVo.setOffPreAmt(processAmt(persFeeVo.getOffPreAmt()));
					}
				}
				for(PrpLPaymentVo payMentVo:prpLPayments){
					payMentVo.setSumRealPay(processAmt(payMentVo.getSumRealPay()));
				}
				List<PrpLChargeVo> prpLCharges = compVo.getPrpLCharges();
				for(PrpLChargeVo chargeVo:prpLCharges){
					chargeVo.setFeeAmt(processAmt(chargeVo.getFeeAmt()));
					chargeVo.setOffAmt(processAmt(chargeVo.getOffAmt()));
					chargeVo.setOffPreAmt(processAmt(chargeVo.getOffPreAmt()));
					chargeVo.setFeeRealAmt(processAmt(chargeVo.getFeeRealAmt()));
				}
				PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);

				if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){
					if(prpLPayments != null && prpLPayments.size()>0){
						for(int i=0; i<prpLPayments.size();i++){
							if(prpLPayments.get(i).getAccountNo() != null){
								prpLPayments.get(i).setAccountNo(DataUtils.replacePrivacy(prpLPayments.get(i).getAccountNo()));
							}
						}
					}
					if( compVo.getPrpLCharges() != null &&  compVo.getPrpLCharges().size()>0){
						for(int i=0; i< compVo.getPrpLCharges().size();i++){
							if( compVo.getPrpLCharges().get(i).getAccountNo() != null){
								 compVo.getPrpLCharges().get(i).setAccountNo(DataUtils.replacePrivacy( compVo.getPrpLCharges().get(i).getAccountNo()));
								 compVo.getPrpLCharges().get(i).setPayeeIdfNo(DataUtils.replacePrivacy(compVo.getPrpLCharges().get(i).getPayeeIdfNo()));
							}
						}
					}
				}
				String reOpenFlag="0";
				PrpLWfTaskVo taskVo2=wfTaskHandleService.findOutWfTaskVo(FlowNode.ReOpen.name(),registNo);
				PrpLCompensateVo compenVo=compensatateService.findCompByPK(compensateNo);
				if(taskVo2!=null){//?????????????????????
					if(compenVo.getPrpLCompensateExt()!=null && StringUtils.isNotBlank(compenVo.getPrpLCompensateExt().getOppoCompensateNo())){
						PrpLCompensateVo compenLinkVo=compensatateService.findCompByPK(compenVo.getPrpLCompensateExt().getOppoCompensateNo());
						if(compenLinkVo.getCreateTime().getTime()>=taskVo2.getTaskOutTime().getTime()){
							reOpenFlag="1";
						}
					}else{//???????????????
						
						if(compenVo.getCreateTime().getTime()>=taskVo2.getTaskOutTime().getTime()){
							reOpenFlag="1";
						}
					}
					
				}
				modelAndView.addObject("reOpenFlag",reOpenFlag);
				modelAndView.addObject("prpLWfTaskVo", wfTaskVo);
				modelAndView.addObject("compType",compType);  
				modelAndView.addObject("refuseReason",refuseReason);  
				modelAndView.addObject("isRefuse",isRefuse);
				modelAndView.addObject("isFraud",isFraud);  
			    modelAndView.addObject("fraudLogo",fraudLogo); 
			    modelAndView.addObject("isMinorInjuryCases",isMinorInjuryCases);
//				modelAndView.addObject("dwFlag","0");
				modelAndView.addObject("flag", flag);
				modelAndView.addObject("compWfFlag","1");//?????????????????????
				modelAndView.addObject("claimDeductVos", claimDeductVos);	
				modelAndView.addObject("prpLCompensate", compVo);
				modelAndView.addObject("prpLPaymentVos", prpLPayments);
				modelAndView.addObject("prpLLossItems", prpLLossItems);
				modelAndView.addObject("otherLossProps", otherLossProps);
				modelAndView.addObject("prpLLossProps", prpLLossProps);
				modelAndView.addObject("prpLLossPersons", prpLLossPersons);
				modelAndView.addObject("prpLChargeVos", prpLCharges);
					
			}
			modelAndView.setViewName("compensate/CompensateEdit");
			return modelAndView;
	}
	/**
	 * ?????????????????????????????????????????????????????????
	 * <pre></pre>
	 * @param registNo
	 * @param handlerIdKey 
	 * @param claimNo
	 * @param flowTaskId ?????????taskID
	 * @param handlerStatus ???????????????
	 * @return
	 * @modified:
	 * ???WLL(2016???9???5??? ??????5:13:55): <br>
	 */
	@RequestMapping("/compeWriteOffLaunch.do")
	@ResponseBody
	public ModelAndView compeWriteOffLaunch(String registNo,String handlerIdKey,String claimNo,Double flowTaskId,String workStatus ){
			ModelAndView modelAndView = new ModelAndView();
			//?????????
			List<PrpLCMainVo> prpLCMains = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
			PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
            PrpLCMainVo prpLCMain = new PrpLCMainVo();
            if(prpLCMains != null && prpLCMains.size() == 2){
                for(PrpLCMainVo vo : prpLCMains){
                   if("12".equals(vo.getRiskCode().substring(0, 2))){//?????????
                        prpLCMain = vo;
                    }
                 }
               }else{
                     prpLCMain = prpLCMains.get(0);
            }
            SysUserVo userVo = WebUserUtils.getUser();
            modelAndView.addObject("prpLCMain",prpLCMain);
            modelAndView.addObject("userVo", userVo);
            modelAndView.addObject("riskCode", registVo.getRiskCode());
			String compensateNo = handlerIdKey;
			if(compensateNo!=null&&compensateNo!=""){
				PrpLCompensateVo compVo = compensatateService.findCompByPK(compensateNo);
				PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);
				//????????????
				List<PrpLClaimDeductVo> claimDeductVos = compensatateService.findDeductCond(registNo, compVo.getRiskCode());
				
				String flag = COMP_BI;
				String bcFlag = PREFIX_BI;
				if(compVo.getRiskCode().equals("1101")){
					flag = COMP_CI;
					bcFlag = PREFIX_CI;
					/**
					 * ???????????????
					 */
					List<PrpLPadPayPersonVo> padPayPersons = new ArrayList<PrpLPadPayPersonVo>();
					if (flag.equals(COMP_CI)) {// ????????????
						padPayPersons = compensatateService.getPadPayPersons(registNo,"0");
						modelAndView.addObject("prpLPadPayPersons", padPayPersons);
					}
				}
				//?????????
				Map<String,List<PrpLPrePayVo>> PrePayMap;
				try{
					PrePayMap = compensatateService.getPrePayMap(registNo, bcFlag,"0");
					modelAndView.addObject("prpLPrePayP", PrePayMap.get("prePay"));
					modelAndView.addObject("prpLPrePayF", PrePayMap.get("preFee"));
				}catch(Exception e){
					logger.error("???????????????????????????????????????????????????????????????????????????????????????,registno="+registNo,e);
				}
				
				List<PrpLPaymentVo> prpLPayments = compVo.getPrpLPayments();
				for(PrpLPaymentVo payMentVo:prpLPayments){
					PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(payMentVo.getPayeeId());
					// ?????????voCustom??????????????????
					payMentVo.setPayeeName(payCustomVo.getPayeeName());
					payMentVo.setAccountNo(payCustomVo.getAccountNo());
					payMentVo.setBankName(payCustomVo.getBankName());
					
				}
				for(PrpLChargeVo chargeVo:compVo.getPrpLCharges()){
					PrpLPayCustomVo payCustomVo = managerService.findPayCustomVoById(chargeVo.getPayeeId());
					// ?????????chargeVo??????????????????
					chargeVo.setPayeeName(payCustomVo.getPayeeName());
					chargeVo.setAccountNo(payCustomVo.getAccountNo());
					chargeVo.setBankName(payCustomVo.getBankName());
					chargeVo.setPayeeIdfNo(payCustomVo.getCertifyNo());
				}
				//???????????????
				PrpLCompensateVo oldCompVo = compVo;
				if(!"1".equals(workStatus)&&!"0".equals(workStatus)){
					String oldCompensateNo =  compVo.getPrpLCompensateExt().getOppoCompensateNo();
					if(!StringUtils.isBlank(oldCompensateNo)){
						oldCompVo = compensatateService.findCompByPK(oldCompensateNo);
					}
				}
				Map<String,String> carMap = new HashMap<String, String>();//????????????
				List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(registNo);
				for(PrpLCheckDutyVo checkDutyVo : checkDutyList){
					carMap.put(checkDutyVo.getLicenseNo(),checkDutyVo.getSerialNo().toString());
				}
				Map<String,String> qfLicenseMap = new HashMap<String, String>();//???????????????	
				List<PrpLPlatLockVo> platLockList = compensateLaunchService.organizeRecoveryList(oldCompVo);
				String dwFlag = this.organizeDwILossItem(platLockList, oldCompVo.getPrpLLossItems(), qfLicenseMap, carMap);
				
				modelAndView.addObject("dwFlag", dwFlag);
				modelAndView.addObject("qfLicenseMap", qfLicenseMap);
				List<PrpLLossPropVo> otherLossProps = new ArrayList<PrpLLossPropVo>();
				//????????????
			    PrpLCertifyMainVo certifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
			    String isFraud = "0"; // ???????????? ???
			    String fraudLogo = "03";  //????????????  ???????????? 
			    String isRefuse = "0";  //????????????
			    String refuseReason = "";
			    String compType = "1";
				if (certifyMainVo != null) {
					isFraud = certifyMainVo.getIsFraud();
					fraudLogo = certifyMainVo.getFraudLogo();
					if (flag.equals(COMP_CI)) {
						isRefuse = certifyMainVo.getIsJQFraud();
						compType = "1";
					} else if (flag.equals(COMP_BI)) {
						isRefuse = certifyMainVo.getIsSYFraud();
						compType = "2";
					}
					
					refuseReason = certifyMainVo.getFraudRefuseReason();
				}
				
				//????????????????????????
			    String isMinorInjuryCases="";
			    List<PrpLDlossPersTraceMainVo> traceMains=persTraceServices.findPersTraceMainVo(registNo);
			    	if(traceMains!=null && traceMains.size()>0){
			    		for(PrpLDlossPersTraceMainVo traceMainVo:traceMains){
			    			isMinorInjuryCases=traceMainVo.getIsMinorInjuryCases();
			    		}
			    	}
			    if(StringUtils.isBlank(isMinorInjuryCases)){
			    	isMinorInjuryCases="0";
			    }
				
				if(!"1".equals(workStatus)&&!"0".equals(workStatus)){
					// ??????
					PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);

					if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){
						if(prpLPayments != null && prpLPayments.size()>0){
							for(int i=0; i<prpLPayments.size();i++){
								if(prpLPayments.get(i).getAccountNo() != null){
									prpLPayments.get(i).setAccountNo(DataUtils.replacePrivacy(prpLPayments.get(i).getAccountNo()));
								}
							}
						}
						if( compVo.getPrpLCharges() != null &&  compVo.getPrpLCharges().size()>0){
							for(int i=0; i< compVo.getPrpLCharges().size();i++){
								if( compVo.getPrpLCharges().get(i).getAccountNo() != null){
									 compVo.getPrpLCharges().get(i).setAccountNo(DataUtils.replacePrivacy( compVo.getPrpLCharges().get(i).getAccountNo()));
									 compVo.getPrpLCharges().get(i).setPayeeIdfNo(DataUtils.replacePrivacy(compVo.getPrpLCharges().get(i).getPayeeIdfNo()));
								}
							}
						}
					}
					
					for(PrpLLossPropVo lossPropVo:compVo.getPrpLLossProps()){
						if("9".equals(lossPropVo.getPropType())){
							otherLossProps.add(lossPropVo);
						}
					}
					String reOpenFlag="0";
					PrpLWfTaskVo taskVo2=wfTaskHandleService.findOutWfTaskVo(FlowNode.ReOpen.name(),registNo);
					PrpLCompensateVo compenVo=compensatateService.findCompByPK(compensateNo);
					if(taskVo2!=null){//?????????????????????
						if(compenVo.getPrpLCompensateExt()!=null && StringUtils.isNotBlank(compenVo.getPrpLCompensateExt().getOppoCompensateNo())){
							PrpLCompensateVo compenLinkVo=compensatateService.findCompByPK(compenVo.getPrpLCompensateExt().getOppoCompensateNo());
							if(compenLinkVo.getCreateTime().getTime()>=taskVo2.getTaskOutTime().getTime()){
								reOpenFlag="1";
							}
						}else{//???????????????
							if(compenVo.getCreateTime().getTime()>=taskVo2.getTaskOutTime().getTime()){
								reOpenFlag="1";
							}
						}
						
					}
					// ???????????????????????????????????????????????????????????????
					modelAndView.addObject("reOpenFlag",reOpenFlag);
				    modelAndView.addObject("compType",compType);  
					modelAndView.addObject("refuseReason",refuseReason);  
					modelAndView.addObject("isRefuse",isRefuse);
					modelAndView.addObject("isFraud",isFraud);  
				    modelAndView.addObject("fraudLogo",fraudLogo);
				    modelAndView.addObject("isMinorInjuryCases",isMinorInjuryCases); 
					modelAndView.addObject("flag", flag);
					modelAndView.addObject("workStatus", workStatus);
					modelAndView.addObject("prpLWfTaskVo", taskVo);
					modelAndView.addObject("flowTaskId", flowTaskId);
					modelAndView.addObject("compWfFlag","2");//?????????????????????
					modelAndView.addObject("claimDeductVos", claimDeductVos);	
					modelAndView.addObject("prpLCompensate", compVo);
					modelAndView.addObject("prpLPaymentVos", prpLPayments);
					modelAndView.addObject("prpLLossItems", compVo.getPrpLLossItems());
					modelAndView.addObject("otherLossProps", otherLossProps);
					modelAndView.addObject("prpLLossProps", compVo.getPrpLLossProps());
					modelAndView.addObject("prpLLossPersons", compVo.getPrpLLossPersons());
					modelAndView.addObject("prpLChargeVos", compVo.getPrpLCharges());
					modelAndView.setViewName("compensate/CompensateEdit");
					return modelAndView;
				}
				
				compVo.setSumAmt(processAmt(compVo.getSumAmt()));
				compVo.setSumLoss(processAmt(compVo.getSumLoss()));
				compVo.setSumPreAmt(processAmt(compVo.getSumPreAmt()));
				compVo.setSumPaidAmt(processAmt(compVo.getSumPaidAmt()));
				compVo.setSumFee(processAmt(compVo.getSumFee()));
				compVo.setSumPreFee(processAmt(compVo.getSumPreFee()));
				compVo.setSumPaidFee(processAmt(compVo.getSumPaidFee()));
				compVo.setSumBzPaid(processAmt(compVo.getSumBzPaid()));
				compVo.setSumRealPay(processAmt(compVo.getSumRealPay()));
				List<PrpLLossItemVo> prpLLossItems = compVo.getPrpLLossItems();
				
				for(PrpLLossItemVo lossCarVo:prpLLossItems){
					//??????????????????????????????
					lossCarVo.setSumRealPay(processAmt(lossCarVo.getSumRealPay()));
					lossCarVo.setSumLoss(processAmt(lossCarVo.getSumLoss()));
					lossCarVo.setBzPaidRescueFee(processAmt(lossCarVo.getBzPaidRescueFee()));
					lossCarVo.setBzPaidLoss(processAmt(lossCarVo.getBzPaidLoss()));
					lossCarVo.setOffPreAmt(processAmt(lossCarVo.getOffPreAmt()));
					lossCarVo.setAbsolvePayAmt(processAmt(lossCarVo.getAbsolvePayAmt()));
					lossCarVo.setDeductDutyAmt(processAmt(lossCarVo.getDeductDutyAmt()));
					lossCarVo.setDeductAbsAmt(processAmt(lossCarVo.getDeductAbsAmt()));
					lossCarVo.setDeductAddAmt(processAmt(lossCarVo.getDeductAddAmt()));
					lossCarVo.setOtherDeductAmt(processAmt(lossCarVo.getOtherDeductAmt()));
					lossCarVo.setDeductOffAmt(processAmt(lossCarVo.getDeductOffAmt()));
					lossCarVo.setRescueFee(processAmt(lossCarVo.getRescueFee()));
				}
				List<PrpLLossPropVo> prpLLossProps = compVo.getPrpLLossProps();
				for(PrpLLossPropVo lossPropVo:prpLLossProps){
					lossPropVo.setSumRealPay(processAmt(lossPropVo.getSumRealPay()));
					lossPropVo.setSumLoss(processAmt(lossPropVo.getSumLoss()));
					lossPropVo.setBzPaidRescueFee(processAmt(lossPropVo.getBzPaidRescueFee()));
					lossPropVo.setBzPaidLoss(processAmt(lossPropVo.getBzPaidLoss()));
					lossPropVo.setOffPreAmt(processAmt(lossPropVo.getOffPreAmt()));
					lossPropVo.setDeductDutyAmt(processAmt(lossPropVo.getDeductDutyAmt()));
					lossPropVo.setDeductAbsAmt(processAmt(lossPropVo.getDeductAbsAmt()));
					lossPropVo.setDeductAddAmt(processAmt(lossPropVo.getDeductAddAmt()));
					lossPropVo.setOtherDeductAmt(processAmt(lossPropVo.getOtherDeductAmt()));
					lossPropVo.setDeductOffAmt(processAmt(lossPropVo.getDeductOffAmt()));
					lossPropVo.setRescueFee(processAmt(lossPropVo.getRescueFee()));
					if("9".equals(lossPropVo.getPropType())){
						otherLossProps.add(lossPropVo);
					}
				}

				List<PrpLLossPersonVo> prpLLossPersons = compVo.getPrpLLossPersons();
				for(PrpLLossPersonVo lossPersVo:prpLLossPersons){
					lossPersVo.setSumRealPay(processAmt(lossPersVo.getSumRealPay()));
					lossPersVo.setSumLoss(processAmt(lossPersVo.getSumLoss()));
					lossPersVo.setSumOffLoss(processAmt(lossPersVo.getSumOffLoss()));
					lossPersVo.setBzPaidLoss(processAmt(lossPersVo.getBzPaidLoss()));
					lossPersVo.setOffPreAmt(processAmt(lossPersVo.getOffPreAmt()));
					lossPersVo.setDeductDutyAmt(processAmt(lossPersVo.getDeductDutyAmt()));
					lossPersVo.setDeductAbsAmt(processAmt(lossPersVo.getDeductAbsAmt()));
					lossPersVo.setDeductAddAmt(processAmt(lossPersVo.getDeductAddAmt()));
					//????????????????????????????????????
					for(PrpLLossPersonFeeVo persFeeVo:lossPersVo.getPrpLLossPersonFees()){
						persFeeVo.setBzPaidLoss(processAmt(persFeeVo.getBzPaidLoss()));
						persFeeVo.setFeeLoss(processAmt(persFeeVo.getFeeLoss()));
						persFeeVo.setFeeRealPay(processAmt(persFeeVo.getFeeRealPay()));
						persFeeVo.setFeeOffLoss(processAmt(persFeeVo.getFeeOffLoss()));
						persFeeVo.setOffPreAmt(processAmt(persFeeVo.getOffPreAmt()));
					}
				}
				for(PrpLPaymentVo payMentVo:prpLPayments){
					payMentVo.setSumRealPay(processAmt(payMentVo.getSumRealPay()));
				}
				List<PrpLChargeVo> prpLCharges = compVo.getPrpLCharges();
				for(PrpLChargeVo chargeVo:prpLCharges){
					chargeVo.setFeeAmt(processAmt(chargeVo.getFeeAmt()));
					chargeVo.setOffAmt(processAmt(chargeVo.getOffAmt()));
					chargeVo.setOffPreAmt(processAmt(chargeVo.getOffPreAmt()));
					chargeVo.setFeeRealAmt(processAmt(chargeVo.getFeeRealAmt()));
				}
				
				modelAndView.addObject("prpLWfTaskVo", taskVo);
			    modelAndView.addObject("compType",compType);  
				modelAndView.addObject("refuseReason",refuseReason);  
				modelAndView.addObject("isRefuse",isRefuse);
				modelAndView.addObject("isFraud",isFraud);  
			    modelAndView.addObject("fraudLogo",fraudLogo);
			    modelAndView.addObject("isMinorInjuryCases",isMinorInjuryCases);
//				modelAndView.addObject("dwFlag","0");
				modelAndView.addObject("flag", flag);
				modelAndView.addObject("workStatus", workStatus);
				modelAndView.addObject("flowTaskId", flowTaskId);
				modelAndView.addObject("compWfFlag","2");//?????????????????????
				modelAndView.addObject("claimDeductVos", claimDeductVos);	
				modelAndView.addObject("prpLCompensate", compVo);
				modelAndView.addObject("prpLPaymentVos", prpLPayments);
				modelAndView.addObject("prpLLossItems", prpLLossItems);
				modelAndView.addObject("otherLossProps", otherLossProps);
				modelAndView.addObject("prpLLossProps", prpLLossProps);
				modelAndView.addObject("prpLLossPersons", prpLLossPersons);
				modelAndView.addObject("prpLChargeVos", prpLCharges);
					
			}
			modelAndView.setViewName("compensate/CompensateEdit");
			return modelAndView;
	}
	
	/**
	 * ???????????????????????????
	 * <pre></pre>
	 * @param amount
	 * @return
	 * @modified:
	 * ???WLL(2016???9???5??? ??????10:12:30): <br>
	 */
	private BigDecimal processAmt(BigDecimal amount){
		BigDecimal proAmt = new BigDecimal(0);
		if(amount!=null){
			proAmt = amount.multiply(new BigDecimal(-1));
		}
		return proAmt;
	}
	
	/**
	 * ????????????????????????????????????
	 * 
	 * @return
	 * @author lanlei
	 */
	@RequestMapping("/loadSubmitVClaimNext.ajax")
	public ModelAndView loadSubmitVClaimNext(String compNo,String currentNode,String auditStatus,String riskCode)throws Exception {

		ModelAndView mav = new ModelAndView();
		SubmitNextVo nextVo = new SubmitNextVo();
		PrpLCompensateVo compensateVo = compensatateService.findCompByPK(compNo);
		SysUserVo userVo = WebUserUtils.getUser();
		String nextNode =null;
        PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,userVo.getComCode());
        if("1".equals(configValueVo.getConfigValue())){//??????ilog
            //ilog????????????
            WfTaskSubmitVo submitVo = null;
            BigDecimal compeWfTaskId = new BigDecimal("0");
            List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findInTaskByOther(compensateVo.getRegistNo(),compensateVo.getCompensateNo(),FlowNode.CompeWf.name());
            if(prpLWfTaskVoList != null && prpLWfTaskVoList.size() > 0){
                compeWfTaskId = prpLWfTaskVoList.get(0).getTaskId();
            }
            Map<String,Object> params = new HashMap<String,Object>();
    		params.put("registNo",compensateVo.getRegistNo());
    		if(compeWfTaskId == null){
    			params.put("taskId",BigDecimal.ZERO.doubleValue());
    		}else{
        		params.put("taskId",compeWfTaskId.doubleValue());
    			
    		}
    		String isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);
            LIlogRuleResVo vPriceResVo = conpensateHandleServiceIlogService.organizaForCompensate(compNo,"2","04",compeWfTaskId,FlowNode.valueOf(currentNode).name(),userVo,null,isSubmitHeadOffice);
            if(Integer.parseInt(vPriceResVo.getMinUndwrtNode()) > Integer.parseInt(vPriceResVo.getMaxUndwrtNode())){
                nextVo.setSubmitLevel(Integer.parseInt(vPriceResVo.getMaxUndwrtNode()));
            }else{
            	nextVo.setSubmitLevel(Integer.parseInt(vPriceResVo.getMinUndwrtNode()));
            }
            if("1101".equals(riskCode)){
                nextNode = "VClaim_CI_LV" + nextVo.getSubmitLevel();
            }else{
                nextNode = "VClaim_BI_LV" + nextVo.getSubmitLevel();
            }
            int maxLevel = Integer.parseInt(vPriceResVo.getMaxUndwrtNode());
            boolean haveUser = false;
            for(int i = nextVo.getSubmitLevel(); i< maxLevel;i++){
                haveUser = assignService.existsGradeUser(FlowNode.valueOf(nextNode), WebUserUtils.getComCode());
                if(!haveUser){
                    if("1101".equals(riskCode)){
                        nextNode = "VClaim_CI_LV" + (i+1);
                    }else{
                        nextNode = "VClaim_BI_LV" + (i+1);
                    }
                }else{
                    break;
                }
            }
        }//else{
        PrpLConfigValueVo configRuleValueVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,userVo.getComCode());
        if("1".equals(configRuleValueVo.getConfigValue())){
            VerifyClaimRuleVo ruleVo = new VerifyClaimRuleVo();
            ruleVo = compensatateService.organizeRuleVo(compNo,userVo);
            if(ruleVo.getBackLevel() > ruleVo.getMaxLevel()){
                nextVo.setSubmitLevel(ruleVo.getMaxLevel());
            }else{
                nextVo.setSubmitLevel(ruleVo.getBackLevel());
            }
            if("1101".equals(riskCode)){
                nextNode = "VClaim_CI_LV" + nextVo.getSubmitLevel();
            }else{
                nextNode = "VClaim_BI_LV" + nextVo.getSubmitLevel();
            }
            boolean haveUser = false;
            for(int i=nextVo.getSubmitLevel();i<ruleVo.getMaxLevel();i++){
                haveUser = assignService.existsGradeUser(FlowNode.valueOf(nextNode), WebUserUtils.getComCode());
                if(!haveUser){
                    if("1101".equals(riskCode)){
                        nextNode = "VClaim_CI_LV" + (i+1);
                    }else{
                        nextNode = "VClaim_BI_LV" + (i+1);
                    }
                }else{
                    break;
                }
            }
        }
		

		nextVo.setNextNode(nextNode);
		nextVo.setNextName(FlowNode.valueOf(nextNode).getName());
		
		nextVo.setCurrentName(FlowNode.valueOf(currentNode).getName());
		nextVo.setCurrentNode(FlowNode.valueOf(currentNode).name());
		nextVo.setAuditStatus(auditStatus);
		//??????????????????
		SysUserVo assUserVo = assignService.execute(FlowNode.valueOf(nextNode),WebUserUtils.getComCode(),WebUserUtils.getUser(), "");
		if(assUserVo == null){
			throw new IllegalArgumentException(nextVo.getNextName()+"???????????????");
		}
		nextVo.setAssignUser(assUserVo.getUserCode());
		nextVo.setAssignCom(assUserVo.getComCode());
		nextVo.setComCode(assUserVo.getComCode());

		mav.addObject("nextVo",nextVo);

		mav.setViewName("prePay/SubmitVClaimNext");

		return mav;
	}
	/**
	 * ????????????????????????????????????
	 * @param compensateVo
	 * @param lossItemVos
	 * @param lossPropVos
	 * @param lossPersons
	 * @param charges
	 * @param payments
	 * @param prpLKindAmtSummaryVoList
	 * @param submitNextVo
	 * @return
	 */
	@RequestMapping(value = "/submitCompeWriteOff.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult submitPrePayWriteOff(
			@FormModel("prpLCompensate") PrpLCompensateVo compensateVo,
			@FormModel("prpLLossItem") List<PrpLLossItemVo> lossItemVos,
			@FormModel("prpLLossProp") List<PrpLLossPropVo> lossPropVos,
			@FormModel("prpLLossPerson") List<PrpLLossPersonVo> lossPersons,
			@FormModel("prpLCharge") List<PrpLChargeVo> charges,
			@FormModel("prpLPayment") List<PrpLPaymentVo> payments,
			@FormModel("prpLKindAmtSummary") List<PrpLKindAmtSummaryVo> prpLKindAmtSummaryVoList,
			@FormModel("submitNextVo") SubmitNextVo submitNextVo) {
			AjaxResult ajaxResult = new AjaxResult();
			String oldCompensate  = compensateVo.getCompensateNo();
			SysUserVo userVo = WebUserUtils.getUser();
			if(oldCompensate==null){
				return null;
			}
		try{
			//???????????? ?????????????????? ????????????????????????
			/*PrpLCompensateVo compOriginVo = compensatateService.findCompByPK(compensateVo.getCompensateNo());
			PrpLCompensateExtVo comExtVo = new PrpLCompensateExtVo();
			if(compOriginVo.getPrpLCompensateExt()!=null){
				comExtVo = compOriginVo.getPrpLCompensateExt();
			}*/

			String newCompNo = compensateLaunchService.submitPrePayWriteOff(compensateVo, lossItemVos, lossPropVos, lossPersons,
					charges, payments, prpLKindAmtSummaryVoList, submitNextVo,userVo);
			ajaxResult.setData(newCompNo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}
		catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}

		return ajaxResult;
	}
	
	
	private String organizeDwILossItem(List<PrpLPlatLockVo> platLockList,
			List<PrpLLossItemVo> lossItemVoList,Map<String,String> qfLicenseMap,Map<String,String> carMap) {
		String dwFlag = "0";
		List<PrpLPlatLockVo> recoveryLockList = new ArrayList<PrpLPlatLockVo>();
		boolean dfPayFlag = false; //????????????
		boolean qfPayFlag = false;//???????????? 
		if (platLockList != null && !platLockList.isEmpty()) {
			for (PrpLPlatLockVo platLockVo : platLockList) {
				if (PayFlagType.CLEAR_PAY.equals(platLockVo.getRecoveryOrPayFlag())) {// ??????
					for (PrpLLossItemVo lossItem : lossItemVoList) {
						if (lossItem.getItemName().equals(platLockVo.getOppoentLicensePlateNo())
								&& !CodeConstants.PayFlagType.NODUTY_INSTEAD_PAY.equals(lossItem.getPayFlag())) {// ???????????????????????????????????????????????????
							lossItem.setPayFlag(PayFlagType.CLEAR_PAY);
							List<PrpLPlatLockVo> itemPlatLockList = new ArrayList<PrpLPlatLockVo>();
							itemPlatLockList.add(platLockVo);
							lossItem.setPlatLockList(itemPlatLockList);
							if(!qfLicenseMap.containsKey(lossItem.getItemId())){
								qfLicenseMap.put(lossItem.getItemId(), lossItem.getItemName());
							}
						}
					}
					qfPayFlag = true;
				} else {// ??????
					recoveryLockList.add(platLockVo);
					dfPayFlag = true;
					String licenseNo = platLockVo.getOppoentLicensePlateNo();
					if(carMap.containsKey(licenseNo)){
						String itemId = carMap.get(licenseNo);
						if(!qfLicenseMap.containsKey(itemId)){
							qfLicenseMap.put(itemId, licenseNo);
						}
					}
					
					for (PrpLLossItemVo lossItem : lossItemVoList) {
						if(lossItem.getItemName().equals(platLockVo.getOppoentLicensePlateNo())){
							if(!qfLicenseMap.containsKey(lossItem.getItemId())){
								qfLicenseMap.put(lossItem.getItemId(), lossItem.getItemName());
							}
						}
					}
				}
			}
			// ?????????
			if (recoveryLockList != null && !recoveryLockList.isEmpty()) {
				PrpLLossItemVo lossItemAVo = null;
				boolean recovyFlag = false;// ???????????????????????????
				for (PrpLLossItemVo lossItem : lossItemVoList) {
					if (CodeConstants.KINDCODE.KINDCODE_A.equals(lossItem.getKindCode())
							&& !PayFlagType.NODUTY_INSTEAD_PAY.equals(lossItem.getPayFlag())) {
						if (PayFlagType.INSTEAD_PAY.equals(lossItem.getPayFlag())) {
							lossItem.setPlatLockList(recoveryLockList);
							recovyFlag = true;
						} else {
							lossItemAVo = Beans.copyDepth().from(lossItem).to(PrpLLossItemVo.class);
						}
					}
				}

				if (!recovyFlag) {
					lossItemAVo.setPayFlag(PayFlagType.INSTEAD_PAY);
					lossItemAVo.setPlatLockList(recoveryLockList);
					lossItemVoList.add(lossItemAVo);
				}
			}
		}

		if(dfPayFlag && qfPayFlag){
			dwFlag ="3";//?????? ??????
		}else if(dfPayFlag){
			dwFlag ="1";//??????
		}else if(qfPayFlag){
			dwFlag ="2";//??????
		}else{
			dwFlag ="0";//?????????
		}
		
		return dwFlag;
	}
}




