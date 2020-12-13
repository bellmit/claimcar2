package ins.sino.claimcar.regist.web.action;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.web.util.ResponseUtils;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLLawSuitVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimSummaryService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimSummaryVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLEndorVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistHandlerService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrplOldClaimRiskInfoVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/registCommon")
public class RegistCommonAction {
	
	private static Logger logger = LoggerFactory.getLogger(RegistCommonAction.class);
	
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	RegistService registService;
	@Autowired
	RegistHandlerService registHandlerService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	ClaimTaskService claimTaskService ;
	@Autowired
	DatabaseDao dataBaseDao;
	@Autowired
	ClaimSummaryService claimSummaryService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	PrpLCMainService prpLCMainService;
	
	/**
	 * 风险提示信息Action
	 * @param registNo
	 * @param policyNos
	 * @param damageTime
	 * @param flowNodeCode
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/registRiskInfo.do")
	public String registRiskInfo(String registNo, String policyNos, String damageTime, String flowNodeCode,String CIPolicyNo,String BIPolicyNo,Model model ) throws ParseException {

		//是否为现场调解案件标志
		String reconcileFlag = null;
		//是否为诉讼案件标志
		String index = "0";
		//商业是否发起追偿
		String recoveryFlag1 = "0";
		//交强是否发起追偿
		String recoveryFlag2 = "0";
	/*	//交强险保单号
		String cIPolicyNo=CIPolicyNo;
		//商业险保单号
		String bIPolicyNo=BIPolicyNo;*/
		if (!StringUtils.isEmpty(flowNodeCode) && flowNodeCode.equals("PLFirst") && !StringUtils.isEmpty(registNo)) {
			PrpLCheckVo prplcheckvo = checkTaskService.findCheckVoByRegistNo(registNo);
			if (prplcheckvo != null) {
				reconcileFlag = prplcheckvo.getReconcileFlag();
			}
		}
		if (!StringUtils.isEmpty(registNo)) {
			List<PrpLLawSuitVo> prpLLawSuitVos = claimTaskService.findPrpLLawSuitVoByRegistNo(registNo);
			if (prpLLawSuitVos != null && prpLLawSuitVos.size() > 0) {
				index = "1";
			}
		}


		// TODO PrpLRegistVo registVo到时再界面获取或者查询registno
		PrpLRegistVo registVo = new PrpLRegistVo();
		if (!StringUtils.isEmpty(registNo)) {
			registVo = registService.findRegistByRegistNo(registNo);
		}
		Map<String, String> registRiskInfoMap = new HashMap<String, String>();
		if (!StringUtils.isEmpty(registNo)) {
			registRiskInfoMap = registService.findRegistRiskInfoByRegistNo(registNo);
			List<PrpLCMainVo> prpLCMainVos=prpLCMainService.findPrpLCMainsByRegistNo(registNo);
			String bipolicyNo="";
			PrpLCMainVo prpLCMainVo=null;
			if(prpLCMainVos!=null && prpLCMainVos.size()>0) {
				for(PrpLCMainVo mainVo:prpLCMainVos) {
					if(!"1101".equals(mainVo.getRiskCode())) {
						bipolicyNo=mainVo.getPolicyNo();
						prpLCMainVo=new PrpLCMainVo();
						prpLCMainVo=mainVo;
						break;
					}
				}
			}
			if(StringUtils.isNotBlank(bipolicyNo)) {
				List<PrplOldClaimRiskInfoVo> InfoVo1 = registService.findPrploldclaimriskinfoByPolicyNo(bipolicyNo);
				List<PrpLEndorVo> prpEndorVos=compensateTaskService.findPrpEndorListByPolicyNo(bipolicyNo);
				//车身划痕保额
				BigDecimal amount=new BigDecimal(0);
				if(prpLCMainVo!=null && prpLCMainVo.getPrpCItemKinds()!=null && prpLCMainVo.getPrpCItemKinds().size()>0) {
					for(PrpLCItemKindVo kindVo:prpLCMainVo.getPrpCItemKinds()) {
						if(CodeConstants.KINDCODE.KINDCODE_L.equals(kindVo.getKindCode())) {
							amount=kindVo.getAmount();
							if(amount==null) {
								amount=new BigDecimal(0);
							}
							break;
						}
					}
				}
				BigDecimal BI_HHJE=amount;
				if(InfoVo1!=null && InfoVo1.size()>0){
				    for(PrplOldClaimRiskInfoVo infoVo:InfoVo1){
				      if("1".equals(infoVo.getIsDanageKindL())){
				    	   BI_HHJE=BI_HHJE.subtract(DataUtils.NullToZero(infoVo.getKindLPayment()));
				      }
				     }
				}
				if(prpEndorVos!=null && prpEndorVos.size()>0){
					for(PrpLEndorVo prpLEndorVo:prpEndorVos){
					  if("L".equals(prpLEndorVo.getKindCode())){
						  BI_HHJE= BI_HHJE.subtract(DataUtils.NullToZero(prpLEndorVo.getEndorAmount()));
					  }
					}
				}
				//当剩余保额小于0时，赋值0
				if(BI_HHJE.compareTo(new BigDecimal(0))<0) {
					BI_HHJE=new BigDecimal(0);
				}
				registRiskInfoMap.put("BI-HHJE",BI_HHJE+"");//车身划痕剩余保额
				
			}
			
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date damageDate = df.parse(damageTime);
			String[] policyNoArr = policyNos.split(",");
			if (policyNoArr.length == 2) {
				registRiskInfoMap = registHandlerService.supplementRiskInfo(registRiskInfoMap, damageDate, policyNoArr[0], policyNoArr[1], registVo);
			} else if (policyNoArr.length == 1) {
				registRiskInfoMap = registHandlerService.supplementRiskInfo(registRiskInfoMap, damageDate, policyNoArr[0], "", registVo);
			} else {

			}

		}

		//判断商业和交强核陪是否发起追偿
		if (!StringUtils.isEmpty(registNo)) {
			List<PrpLCompensateVo> compList = compensateTaskService.findCompByRegistNo(registNo);
			if (compList != null && compList.size() > 0) {
				for (PrpLCompensateVo prpLCompensateVo : compList) {
					if (prpLCompensateVo.getRiskCode().equals("1101")) {
						if (prpLCompensateVo.getRecoveryFlag().equals("1") && recoveryFlag2.equals("0")) {
							recoveryFlag2 = "1";
						}
					} else {
						if (prpLCompensateVo.getRecoveryFlag().equals("1") && recoveryFlag1.equals("0")) {
							recoveryFlag1 = "1";
						}
					}
				}
			}
		}
		
		//附加车轮单独损失险剩余保额
		 //剩余保额
	    BigDecimal restAmount=new BigDecimal(0);
	    //已减保额
	    BigDecimal ductAmount=new BigDecimal(0);
	    //险别保额
	    BigDecimal kindAmount=new BigDecimal(0);
	    if(StringUtils.isNotBlank(registNo)) {
	    	List<PrpLCMainVo> prpLCMainVos=prpLCMainService.findPrpLCMainsByRegistNo(registNo);
		    if(prpLCMainVos!=null && prpLCMainVos.size()>0){
		    	for(PrpLCMainVo cmainVo:prpLCMainVos){
		    		if(!"1101".equals(cmainVo.getRiskCode())){
		    			List<PrpLCItemKindVo> prpLCItemKindVos=cmainVo.getPrpCItemKinds();
		    			if(prpLCItemKindVos!=null && prpLCItemKindVos.size()>0) {
		    				for(PrpLCItemKindVo citemKindVo:prpLCItemKindVos) {
		    					if(CodeConstants.KINDCODE.KINDCODE_W1.equals(citemKindVo.getKindCode())) {
		    						kindAmount=DataUtils.NullToZero(citemKindVo.getAmount());
		    						break;
		    					}
		    				}
		    			}
		    			List<PrpLEndorVo> prpLEndorVos=compensateTaskService.findPrpEndorListByPolicyNo(cmainVo.getPolicyNo());
		    			if(prpLEndorVos!=null && prpLEndorVos.size()>0){
		    				for(PrpLEndorVo prpLEndorVo:prpLEndorVos) {
		    					if(CodeConstants.KINDCODE.KINDCODE_W1.equals(prpLEndorVo.getKindCode())) {
		    						ductAmount=ductAmount.add(DataUtils.NullToZero(prpLEndorVo.getEndorAmount()));
		    					}
		    					
		    				}
		    				
		    			}
		    		}
		    	}
		    }
		    if(ductAmount.compareTo(kindAmount)>=0) {
		    	restAmount=new BigDecimal(0);
		    }else {
		    	restAmount=kindAmount.subtract(ductAmount);
		    }
		    registRiskInfoMap.put("W1RESTVALUE",restAmount+"");
	    }else {
	    	registRiskInfoMap.put("W1RESTVALUE","");
	    }
		model.addAttribute("recoveryFlag1", recoveryFlag1);
		model.addAttribute("recoveryFlag2", recoveryFlag2);
		model.addAttribute("index", index);
		model.addAttribute("registNo", registNo);
		model.addAttribute("prpLRegistRiskInfoMap", registRiskInfoMap);
		model.addAttribute("reconcileFlag", reconcileFlag);
		return "regist/common/RegistRiskInfoList";
	}

	/**
	 * <pre>历史出险信息公共按钮</pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆Luwei(2016年9月18日 下午8:03:48): <br>
	 */
	@RequestMapping(value = "/claimSummary.ajax")
	@ResponseBody
	public ModelAndView historySafeInit(HttpServletRequest request) {
		String registNo = request.getParameter("registNo");
		ModelAndView mav = new ModelAndView();
		String policyNo_CI = "";
		String policyNo_BI = "";
		
		if(StringUtils.isEmpty(registNo)){
			policyNo_CI = request.getParameter("CIPolicyNo");
			policyNo_BI = request.getParameter("BIPolicyNo");
			
		}else{
			List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(registNo);
			for(PrpLCMainVo cMainVo:cMainVoList){
				String tempPolicy = cMainVo.getPolicyNo();
				if(Risk.DQZ.equals(cMainVo.getRiskCode())){
					policyNo_CI = tempPolicy;
				}else{
					policyNo_BI = tempPolicy;
				}
			}

		}
		mav.addObject("policyNo_CI",policyNo_CI);
		mav.addObject("policyNo_BI",policyNo_BI);

		mav.addObject("registNo",registNo);
		mav.setViewName("regist/registEdit/ReportEdit_History");
		return mav;
	}
	
	/**
	 * 历史出险信息
	 * @param policyNo
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/historySearch.do")
	@ResponseBody
	public String historySafe(HttpServletRequest request,
	    @RequestParam(value = "start", defaultValue = "0") int start,
	    @RequestParam(value = "length", defaultValue = "10") int length) {
		String policyNo = "";
		String handle = request.getParameter("handle");
		if("1".equals(handle)){
			policyNo = request.getParameter("policyNoCI");
		}else{
			policyNo = request.getParameter("policyNoBI");
		}
		if(StringUtils.isEmpty(policyNo)){
			return null;
		}
		ResultPage<PrpLClaimSummaryVo> resultPage = claimSummaryService.findPageForHistory(policyNo,start,length);
		String jsonData = ResponseUtils.toDataTableJson(resultPage,"licenseNo","damageCode:DamageCode","damageTime",
				"damageAddress","claimTime","endCaseTime","realPay","willPay","caseStatus","flowId");
		logger.debug("summaryVoList.jsonData="+jsonData);
		
		return jsonData;
	}
}
