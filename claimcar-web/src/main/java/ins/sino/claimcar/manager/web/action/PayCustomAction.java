package ins.sino.claimcar.manager.web.action;

import freemarker.core.ParseException;
import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.service.facade.CodeDictService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.bankaccount.service.BankAccountService;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLFxqFavoreeVo;
import ins.sino.claimcar.claim.vo.PrpLPayFxqCustomVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.AMLService;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.RepairFactoryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.AMLVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.manager.vo.AccBankNameVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.manager.vo.PrplOldaccbankCodeVo;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

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
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sinosoft.insaml.apiclient.FxqCrmRiskService;
import com.sinosoft.insaml.povo.vo.CrmRiskInfoVo;
import com.sinosoft.insaml.povo.vo.FreezeCustVo;


/**
 * @author lanlei ???????????????????????????
 */

@Controller
@RequestMapping("/payCustom")
public class PayCustomAction {

	@Autowired
	private PayCustomService payCustomService;
	@Autowired
	private BankAccountService bankAccountService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private RepairFactoryService repairFactoryService;
	@Autowired
	private CheckHandleService checkHandleService; 
	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private CompensateTaskService compensateTaskService;
    @Autowired
    private ClaimTaskService claimTaskService;
    @Autowired
    CodeDictService codeDictService;
    @Autowired
    WfTaskHandleService wfTaskHandleService;
    @Autowired
    AMLService amlService;
    @Autowired
    PrpLCMainService prpLCMainService;
    
    private static Logger logger = LoggerFactory.getLogger(PayCustomAction.class);
	/**
	 * ????????????????????????Action
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/payCustomEdit.do")
	public ModelAndView intermediaryEdit(HttpServletRequest request) {
		String flag = request.getParameter("flag");
		String registNo = request.getParameter("registNo");
		String payId = request.getParameter("payId");
		String nodeCode = request.getParameter("nodeCode");
		String lawsuitFlag = request.getParameter("lawsuitFlag");
		String frostFlags = request.getParameter("frostFlags");
		ModelAndView mv = new ModelAndView();
		PrpLPayCustomVo payCustomVo = new PrpLPayCustomVo();
		SysUserVo userVo = WebUserUtils.getUser();
		Long pid = null;
		if (StringUtils.isNotBlank(payId)) {
			pid = Long.parseLong(payId);
		}
		
		if (pid != null) {
			payCustomVo = payCustomService.findPayCustomVoById(pid);
		}
		String certiNotModiPay = "N";
//		if(FlowNode.Certi.toString().equals(nodeCode)){
			// ???????????????????????????
			// ?????????????????????????????????????????????  ????????????????????????
			//??????????????????????????????????????????????????????????????????????????????
			List<PrpLCompensateVo> compensateVoList = compensateTaskService.findNotCancellCompensatevosByRegistNo(registNo);
			if(compensateVoList!=null&&compensateVoList.size()>0){
			    List<PrpLWfTaskVo> wfTaskVos = new ArrayList<PrpLWfTaskVo>();
				for(PrpLCompensateVo compVo : compensateVoList){
				    //?????????????????????
				    List<PrpLPrePayVo> prePayVoList = compensateTaskService.findPrePayList(compVo.getCompensateNo());
				    if("1101".equals(compVo.getRiskCode())){
                        wfTaskVos = wfTaskHandleService.findInTaskbyCompensateNo(registNo,compVo.getCompensateNo(),FlowNode.PrePayCI.toString());
                    }else {
                        wfTaskVos = wfTaskHandleService.findInTaskbyCompensateNo(registNo,compVo.getCompensateNo(),FlowNode.PrePayBI.toString());
                    }
				    if(prePayVoList != null&&prePayVoList.size() > 0){
                        for(PrpLPrePayVo prpLPrePayVo:prePayVoList){
                            if(prpLPrePayVo.getPayeeId().equals(payCustomVo.getId())){
                                if(prpLPrePayVo.getPayStatus()!=null||wfTaskVos.size()==0){                                                            
                                    certiNotModiPay = "Y";
                                    break;
                                }
                            }
                        }
                    }
					if(compVo.getPrpLPayments()!=null&&compVo.getPrpLPayments().size()>0){
						for(PrpLPaymentVo payMent : compVo.getPrpLPayments()){
							if(payMent.getPayeeId().equals(payCustomVo.getId())){
							    // ??????????????????????????????????????????????????????
							    
							    if("1101".equals(compVo.getRiskCode())){
							        wfTaskVos = wfTaskHandleService.findInTaskbyCompensateNo(registNo,compVo.getCompensateNo(),FlowNode.CompeCI.toString());
							    }else {
							        wfTaskVos = wfTaskHandleService.findInTaskbyCompensateNo(registNo,compVo.getCompensateNo(),FlowNode.CompeBI.toString());
                                }
							    
								if(payMent.getPayStatus()!=null||wfTaskVos.size()==0){					    									
								    certiNotModiPay = "Y";
	                                break;
									
								}
							}
						}
					}
					if(compVo.getPrpLCharges()!=null&&compVo.getPrpLCharges().size()>0){
						for(PrpLChargeVo chargeVo : compVo.getPrpLCharges()){
							if(chargeVo.getPayeeId().equals(payCustomVo.getId())){
                                if("1101".equals(compVo.getRiskCode())){
                                    wfTaskVos = wfTaskHandleService.findInTaskbyCompensateNo(registNo,compVo.getCompensateNo(),FlowNode.CompeCI.toString());
                                }else {
                                    wfTaskVos = wfTaskHandleService.findInTaskbyCompensateNo(registNo,compVo.getCompensateNo(),FlowNode.CompeBI.toString());
                                }
								if(chargeVo.getPayStatus()!=null||wfTaskVos.size()==0){
									// ????????????????????????????????????
									certiNotModiPay = "Y";
									break;
								}
							}
						}
					}
				}
			}
			//???????????????????????????
			PrpLPadPayMainVo padPayMainVo = compensateTaskService.findPadPayMain(registNo);
			List<PrpLWfTaskVo> wfTaskVos = wfTaskHandleService.findInTaskbyCompensateNo(registNo,padPayMainVo.getCompensateNo(),FlowNode.PadPay.toString());
			List<PrpLPadPayPersonVo> padPayPersonVoList = padPayMainVo.getPrpLPadPayPersons();
			//?????????????????????
			if(padPayPersonVoList != null&&padPayPersonVoList.size() > 0){
				for(PrpLPadPayPersonVo prpLPadPayPersonVo:padPayPersonVoList){
					if(prpLPadPayPersonVo.getPayeeId().equals(payCustomVo.getId())){
						if(prpLPadPayPersonVo.getPayStatus()!=null||wfTaskVos.size()==0){
							certiNotModiPay = "Y";
							break;
						}
					}
				}
			}
			
			// ??????????????????????????????  ????????????   ??????????????????
//		}
		if (payCustomVo.getId()!=null&&"N".equals(flag)&&!payCustomVo.getRegistNo().equals(registNo)) {
			//????????????????????????????????????????????????????????????????????????????????????????????????????????????
			payCustomVo.setId(null);
			//???????????????
			payCustomVo.setSummary(null);
		}
		if(flag.equals("Y")){
			//?????????????????????-?????????????????????????????????
			List<PrpLPayCustomVo> payList = payCustomService.findPayCustomVoByRegistNo(registNo);
			for(PrpLPayCustomVo payVo:payList){
				if(payVo.getPayObjectKind().equals("2")){
					//??????????????????????????????PayObjectKind=2
					payCustomVo = payVo;
				}
			}
		}else{
			mv.addObject("payLawsuitFlag", lawsuitFlag);
			//??????????????????
				PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
				if(prpLRegistVo !=null && prpLRegistVo.getPrpLRegistExt()!=null){
					if(StringUtils.isNotBlank(prpLRegistVo.getPrpLRegistExt().getLicenseNo())){
						mv.addObject("licenseNo",prpLRegistVo.getPrpLRegistExt().getLicenseNo()+"??????");
					}
				}
				//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
				if(StringUtils.isNotBlank(prpLRegistVo.getReportorPhone())){
                    mv.addObject("reportorPhone",prpLRegistVo.getReportorPhone());
				}
		}
		
		if("N".equals(flag) || "S".equals(flag)){
			//????????????????????????Map??????
			Map<String,String> bankCodeMap = new HashMap<String,String>();
			List<PrplOldaccbankCodeVo> listVo=payCustomService.findPrplOldaccbankCodeByFlag("1");
			if(listVo!=null && listVo.size()>0){
				for(PrplOldaccbankCodeVo vo:listVo){
					bankCodeMap.put(vo.getBankCode(), vo.getBankName());
				}
			}
			mv.addObject("bankCodeMap",bankCodeMap);
		}
		mv.addObject("certiNotModiPay",certiNotModiPay);
		mv.addObject("prpLPayCustom", payCustomVo);
		mv.addObject("registNo", registNo);
		mv.addObject("nodeCode", nodeCode);
		mv.addObject("flag", flag);
		
		//???????????????????????????
		mv.addObject("frostFlags", frostFlags);
		mv.addObject("payCustomFrostFlags", payCustomVo.getFrostFlag());
		//???????????????????????????????????????
		Map<String,String> dictMap = new HashMap<String,String>();
		List<PrpLDlossCarMainVo> lossCarMains=lossCarService.findLossCarMainByRegistNo(registNo);
	
		if(lossCarMains!=null && lossCarMains.size()>0){
			for(PrpLDlossCarMainVo lossCarMainVo:lossCarMains){
				if(!StringUtils.isBlank(lossCarMainVo.getRepairFactoryCode())){
					//PrpLDlossCarMain?????????RepairFactoryCode??????????????????PrpLRepairFactory??????Id
					 PrpLRepairFactoryVo factoryCodevo=repairFactoryService.findFactoryById(lossCarMainVo.getRepairFactoryCode());
				  if(factoryCodevo!=null){
				     dictMap.put(lossCarMainVo.getRepairFactoryCode(),factoryCodevo.getFactoryName());
				  }
				}
			}
		}
		mv.addObject("dictMap",dictMap);
		
		mv.addObject("userVo",userVo);
		mv.setViewName("payCustom/PayCustomEdit");

		return mv;
	}

	/**
	 * ???????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(value = "/savePayCustomInfo.do")
	@ResponseBody
	public AjaxResult savePayCustomInfo(@FormModel("prpLPayCustomVo") PrpLPayCustomVo payCustomVo,
	                                    @FormModel("flag") String flag ,@FormModel("frostFlags") String frostFlags) {
		AjaxResult ajaxResult = new AjaxResult();
		//????????????
		if(StringUtils.isNotBlank(payCustomVo.getSummary())){
		    payCustomVo.setSummary(payCustomVo.getSummary().replaceAll("\\s*", ""));
		}
		
		SysUserVo userVo = WebUserUtils.getUser();
		// ????????????????????????????????????????????? ????????????????????????????????????  ---??????remark??????Y????????????????????????
		payCustomVo.setRemark(flag);
		try{
//			String rNo = payCustomVo.getRegistNo();
			payCustomVo.setAccountNo(payCustomVo.getAccountNo().replaceAll("\\s*", ""));
			payCustomVo.setCertifyNo(payCustomVo.getCertifyNo().replaceAll("\\s*", ""));
			payCustomVo.setPayeeName(payCustomVo.getPayeeName().replaceAll("\\s*", ""));
			Long id = (long)0;
			if("2".equals(frostFlags)){//????????????2???????????????????????????
			    String amlUrl = SpringProperties.getProperty("dhic.aml.saveurl");
			    logger.info("-----????????????----------amlUrl============================"+amlUrl);
			    FxqCrmRiskService crmRiskService = new FxqCrmRiskService(amlUrl);
			    FreezeCustVo custVo = new FreezeCustVo();
			    custVo.setCustCode(payCustomVo.getCertifyNo());
			    custVo.setCustName(payCustomVo.getPayeeName());
			    custVo.setOprateCode(userVo.getUserCode());
			    if("1".equals(payCustomVo.getFrostFlag())){//??????
			        custVo.setOprateType("0"); 
			        custVo.setRemark("");
			    }else{//??????
			        custVo.setOprateType("1");
			        custVo.setRemark(payCustomVo.getReason());
			    }
			    custVo.setSystemCode("claimcar");
			    crmRiskService.saveFreezeInfo(custVo);
			    id = payCustomService.saveOrUpdatePayCustomById(payCustomVo,userVo);
			}else{
			    id = payCustomService.saveOrUpdatePayCustom(payCustomVo,userVo);
			}
			
			/*if("1".equals(frostFlags)){//??????
                claimTaskService.cancleClaimForOther(payCustomVo.getRegistNo(), WebUserUtils.getUserCode());
			}else if("0".equals(frostFlags)){//??????
			    claimTaskService.recoverFlow(payCustomVo.getRegistNo(), WebUserUtils.getUserCode());
			}*/
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(id);
			
		}catch(Exception e){
			
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
			
		
		}
		return ajaxResult;
	}

	/**
	 * ??????????????????????????????????????????????????????????????????????????????????????? ????????????????????????????????????
	 */
	@RequestMapping(value = "/getCInsuredInfo.ajax")
	@ResponseBody
	public AjaxResult getCInsuredInfo(HttpServletRequest request) {
		AjaxResult ar = new AjaxResult();
		String registNo = request.getParameter("registNo");
		PrpLCInsuredVo cInsuredVo = payCustomService.getCInsuredInfoByRegistNo(registNo);
		ar.setData(cInsuredVo);
		ar.setStatus(200);
		return ar;

	}

	/**
	 * ?????????????????????????????????????????????ajax??????
	 */
	@RequestMapping(value = "/getPayCusInfo.ajax")
	@ResponseBody
	public AjaxResult getPayCusInfo(HttpServletRequest request) {
		AjaxResult ar = new AjaxResult();
		Long pid = Long.parseLong(request.getParameter("pid"));
		PrpLPayCustomVo rePayCusVo = payCustomService.findPayCustomVoById(pid);
		ar.setStatus(200);
		ar.setData(rePayCusVo);
		return ar;
	}

	/**
	 * ???????????????????????????????????????????????????
	 * <pre></pre>
	 * @param request
	 * @return
	 * @modified:
	 * ???WLL(2016???11???23??? ??????11:30:51): <br>
	 */
	@RequestMapping("/payCustomList.do")
	@ResponseBody
	public ModelAndView findPayCustom(HttpServletRequest request) {
		String registNo = request.getParameter("registNo");
		String tdName = request.getParameter("tdName");
		String compFlag = request.getParameter("compFlag");
		String flag = request.getParameter("flag");
		String dwPayFlag = request.getParameter("dwPayFlag");
//		List<PrpLPayCustomVo> payCustomVos = new ArrayList<PrpLPayCustomVo>();
		ModelAndView mv = new ModelAndView();
//		payCustomVos = payCustomService.findPayCustomVoByRegistNo(registNo);
		mv.setViewName("payCustom/PayCustomList");
//		mv.addObject("payCustomVos", payCustomVos);
		mv.addObject("registNo", registNo);
		mv.addObject("tdName", tdName);
		mv.addObject("compFlag", compFlag);
		mv.addObject("dwPayFlag", dwPayFlag);
		mv.addObject("flag", flag);
		return mv;
	}
	/**
	 * ?????????????????????1123??????  ???????????????
	 * <pre></pre>
	 * @param request
	 * @return
	 * @modified:
	 * ???WLL(2016???11???23??? ??????11:32:57): <br>
	 */
	@RequestMapping(value = "/payCustomQueryList.do")
	@ResponseBody
	public ModelAndView payCustomQueryList(HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		String registNo = request.getParameter("registNo");
		String  lawsuitFlag= request.getParameter("lawsuitFlag");
		mv.addObject("registNo", registNo);
		mv.addObject("lawsuitFlag", lawsuitFlag);
		mv.setViewName("payCustom/PayCustomQueryList");
		return mv;
	}
	
	/**
	 * ?????????????????????????????????????????????????????????????????????????????????????????????
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/payCustomSearchList.do")
	@ResponseBody
	public ModelAndView payCustomSearchList(HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		String registNo = request.getParameter("registNo");
		String  lawsuitFlag= request.getParameter("lawsuitFlag");
		String flag = request.getParameter("flag");
		String nodeCode = request.getParameter("nodeCode");
		mv.addObject("registNo", registNo);
		mv.addObject("lawsuitFlag", lawsuitFlag);
		mv.addObject("flag", flag);
		mv.addObject("nodeCode", nodeCode);
		mv.setViewName("payCustom/PayCustomSearchList");
		return mv;
	}
	
	/**
	 * ?????????????????????1123?????? ????????????
	 * <pre></pre>
	 * @param request
	 * @return
	 * @modified:
	 * ???WLL(2016???11???23??? ??????11:32:57): <br>
	 */
	@RequestMapping("/payCustomQuery.do")
	@ResponseBody
	public String payCustomQuery(
			@FormModel("prpLPayCustomVo") PrpLPayCustomVo prpLPayCustomVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) {
		ResultPage<PrpLPayCustomVo> page = payCustomService.findPrpLPayCustomByNameAccNo(prpLPayCustomVo,start,length);
		String jsonData = ResponseUtils.toDataTableJson(page, "id","payeeName", "accountNo", "bankOutlets","certifyNo","payObjectKind:PayObjectKind","payObjectKind","registNo");
		return jsonData;
	}
	
	/**
	 * ??????????????????????????????????????????????????????????????????????????????????????????
	 * @param prpLPayCustomVo
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping("/payCustomSearch.do")
	@ResponseBody
	public String payCustomSearch(
			@FormModel("prpLPayCustomVo") PrpLPayCustomVo prpLPayCustomVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) {
		ResultPage<PrpLPayCustomVo> page = payCustomService.findPayCustomByKindNameAccNo(prpLPayCustomVo,start,length);
		String jsonData = ResponseUtils.toDataTableJson(page, "id","payeeName", "accountNo", "bankOutlets");
		return jsonData;
	}
	/**
	 * ??????????????????????????????
	 * <pre></pre>
	 * @param request
	 * @return
	 * @modified:
	 * ???WLL(2017???6???24??? ??????12:12:30): <br>
	 */
	@RequestMapping(value = "/btnPayCustomEdit.do")
	public ModelAndView btnIntermediaryEdit(HttpServletRequest request) {
		String flag = request.getParameter("flag");
		String registNo = request.getParameter("registNo");
		String nodeCode = request.getParameter("nodeCode");
		Long id = Long.parseLong(request.getParameter("payId"));
		String sign=request.getParameter("sign");
		ModelAndView mv = new ModelAndView();
		PrpLPayCustomVo payCustomVo = new PrpLPayCustomVo();
		// ????????????????????????????????????????????????  flag = V,???????????????????????????????????????,????????????,??????viewFlag = 1 ????????????????????????????????????
		String viewFlag = "0";
		if("V".equals(flag)){
			flag = "Y";
			viewFlag = "1";
		}
		if("Y".equals(flag)){
			payCustomVo=payCustomService.findPayCustomVoById(id);
			String registNo1=payCustomVo.getRegistNo();//???id?????????????????????
			String policyNo1=null;//?????????????????????????????????
			PrpLCMainVo prpLCMainVo1=null;
			if(payCustomVo!=null){
				if(StringUtils.isNotBlank(registNo1)){
				List<PrpLCMainVo> prpLCMainVos=	checkHandleService.getPolicyAllInfo(registNo1);
				  if(prpLCMainVos!=null && prpLCMainVos.size()>0){
					  for(PrpLCMainVo prpLCMainVo:prpLCMainVos){
						  if(!"1101".equals(prpLCMainVo.getRiskCode())){
							  policyNo1=prpLCMainVo.getPolicyNo();//?????????????????????????????????
							  prpLCMainVo1=prpLCMainVo;
							  break;
						  }else{
							  policyNo1=prpLCMainVo.getPolicyNo();//??????????????????????????????
							  prpLCMainVo1=prpLCMainVo;
						  }
					  }
				  }
				}
			}
			//????????????????????????????????????????????????????????????????????????????????????????????????
			if(prpLCMainVo1!=null){
			   List<PrpLCInsuredVo> prpLCInsureds=prpLCMainVo1.getPrpCInsureds();
			   if("2".equals(payCustomVo.getPayObjectKind())){//??????????????????
				  for(PrpLCInsuredVo prpLCInsuredVo:prpLCInsureds){
					 if(prpLCInsuredVo.getPolicyNo().equals(policyNo1) && "1".equals(prpLCInsuredVo.getInsuredFlag())){
						 payCustomVo.setAdress(prpLCInsuredVo.getInsuredAddress());
						 if(StringUtils.isNotBlank(prpLCInsuredVo.getOccupationCode())){
							 payCustomVo.setProfession(codeTranService.transCode("OccupationCode",prpLCInsuredVo.getOccupationCode()));
							
						 }
						 break;
					 }
				  }
			   }
			}
		}
		//??????????????????????????????
		if("1".equals(sign)){
			payCustomVo=payCustomService.findPayCustomVoById(id);
			payCustomVo.setStatus("1");
			payCustomService.updatePaycustom(payCustomVo);
		}
		
		//???????????????????????????????????????
				Map<String,String> dictMap = new HashMap<String,String>();
				List<PrpLDlossCarMainVo> lossCarMains=lossCarService.findLossCarMainByRegistNo(registNo);
			
				if(lossCarMains!=null && lossCarMains.size()>0){
					for(PrpLDlossCarMainVo lossCarMainVo:lossCarMains){
						if(!StringUtils.isBlank(lossCarMainVo.getRepairFactoryCode())){
							//PrpLDlossCarMain?????????RepairFactoryCode??????????????????PrpLRepairFactory??????Id
							 PrpLRepairFactoryVo factoryCodevo=repairFactoryService.findFactoryById(lossCarMainVo.getRepairFactoryCode());
						  if(factoryCodevo!=null){
						     dictMap.put(lossCarMainVo.getRepairFactoryCode(),factoryCodevo.getFactoryName());
						  }
						}
					}
				}
		mv.addObject("dictMap",dictMap);
		mv.addObject("sign",sign);
		mv.addObject("prpLPayCustom", payCustomVo);
		mv.addObject("registNo", registNo);
		mv.addObject("nodeCode", nodeCode);
		mv.addObject("flag", flag);
		mv.addObject("viewFlag", viewFlag);
		mv.setViewName("payCustom/PayCustomEdit");

		return mv;
	}

	/**
	 * ????????????????????????ajax??????
	 * @param infoSize
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/addRowInfo.do")
	public ModelAndView addRowInfo(Long pid,String registNo,String claimNo,String signFlag)throws ParseException {
		ModelAndView mv = new ModelAndView();
		PrpLPayCustomVo payCus = new PrpLPayCustomVo();
		AMLVo amlVo = new AMLVo();
		amlVo = amlService.findAMLInfo(pid,registNo,claimNo,signFlag);
		 try {
			 if("SY".equals(amlVo.getFxqSeeflag())){
				 PrpLFxqFavoreeVo prpLFxqFavoreeVo= amlVo.getFavoreeVo();
				    prpLFxqFavoreeVo.setSeeFlag("1");
				    if(prpLFxqFavoreeVo!=null && prpLFxqFavoreeVo.getId()!=null){
				    	payCustomService.saveOrupdatePrpLFxqFavoreeCustom(prpLFxqFavoreeVo,null);
				    }
					
			 }
     		    
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		if(StringUtils.isNotBlank(amlVo.getInsureRelation()) && amlVo.getInsureRelation().startsWith("0")){
		    amlVo.setInsureRelation("0");
        }
		
		mv.addObject("amlVo",amlVo);
		mv.addObject("prpLPayCustom", payCus);
		mv.addObject("signFlag",signFlag);
		
		if("1".equals(amlVo.getInsuredType())){
			mv.setViewName("payCustom/PayCustomEdit_AMLforPe");
		}else if("2".equals(amlVo.getInsuredType())){
			mv.setViewName("payCustom/PayCustomEdit_AMLforIn");
		}
		return mv;
	}
	/**
	 * ?????????????????????????????????
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ???WLL(2016???9???22??? ??????11:01:15): <br>
	 */
	@RequestMapping(value = "/findBankNoQueryList.do")
	public ModelAndView findBankNoInit() {
		ModelAndView mv = new ModelAndView();	
		mv.setViewName("payCustom/PayCustomEdit_BankNumQueryList");

		return mv;
	}
	
	/**
	 * ????????????
	 * <pre></pre>
	 * @return
	 * @modified:
	 * ???WLL(2016???9???22??? ??????11:01:15): <br>
	 */
	@RequestMapping(value = "/bankNumberFind.do")
	@ResponseBody
	public String bankNumberFind(
			Model model,
			@FormModel("AccBankNameVo") AccBankNameVo accBankNameVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) {
		ResultPage<AccBankNameVo> page = payCustomService.findBankNum(accBankNameVo,start,length);
		String jsonData = ResponseUtils.toDataTableJson(page, "bankCode", "bankName","belongBank","provincial","city" );
		return jsonData;
	}
	
	/**
	 * ???????????????????????????
	 * @return
	 */
	@RequestMapping(value = "/modPayCustomInfo.do")
	@ResponseBody
	public AjaxResult modPayCustomInfo(
			@FormModel("prpLPayCustomVo") PrpLPayCustomVo payCustomVo,
			@FormModel("compensateNo") String compensateNo) {
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo userVo = WebUserUtils.getUser();
		try{
			payCustomService.modPayCustomInfo(payCustomVo,userVo,compensateNo);
			
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}
	
	/**
	 * ????????????????????????????????????????????????
	 * @param registNo
	 * @return
	 */
	@RequestMapping(value = "/repairfactoryInfo.do")
	@ResponseBody
	public AjaxResult searchRepairFactory(String strText){
		AjaxResult ajax = new AjaxResult();
		String payeeName="";
		String accountNo="";
		PrpLRepairFactoryVo	 prpLRepairFactoryVo=repairFactoryService.findFactoryById(strText);
		if(prpLRepairFactoryVo!=null){
			payeeName=prpLRepairFactoryVo.getPayeeName();
			accountNo=prpLRepairFactoryVo.getAccountNo();
		}
		
		ajax.setData(accountNo);
		ajax.setStatusText(payeeName);
		return ajax;
	}
	
	/**
	 * 
	 * ????????????????????????????????????
	 * @param prpLPayCustomVo
	 * @param start
	 * @param length
	 * @return
	 * @modified:
	 * ???zhujunde(2017???6???21??? ??????5:49:31): <br>
	 */
	@RequestMapping("/payCustomSearchByRegistNo.do")
    @ResponseBody
    public String payCustomSearchByRegistNo(
            @FormModel("prpLPayCustomVo") PrpLPayCustomVo prpLPayCustomVo,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "length", defaultValue = "10") Integer length) {
        ResultPage<PrpLPayCustomVo> page = payCustomService.findPayCustomByRegistNo(prpLPayCustomVo,start,length);
        String jsonData = ResponseUtils.toDataTableJson(page, "id","payeeName", "accountNo", "bankOutlets");
        return jsonData;
    }
	
	/**
	 * 
	 * <pre></pre>
	 * @param request
	 * @return
	 * @modified:
	 * ???zhujunde(2017???6???23??? ??????4:47:38): <br>
	 */
    @RequestMapping(value = "/getCustRiskInfo.ajax")
    @ResponseBody
    public AjaxResult getCustRiskInfo(String payeeName,String certifyNo,Long id) {
        AjaxResult ar = new AjaxResult();
        SysUserVo userVo = WebUserUtils.getUser();
        String amlUrl = SpringProperties.getProperty("dhic.aml.saveurl");
        logger.info("amlUrl============================"+amlUrl);
        FxqCrmRiskService crmRiskService = new FxqCrmRiskService(amlUrl);
        CrmRiskInfoVo vo = new CrmRiskInfoVo();
        //yzyfxq
        try{
            vo = crmRiskService.getCustRiskInfo(certifyNo,payeeName, certifyNo,"",userVo.getUserCode());
            
            if(vo!=null && "1".equals(vo.getIsFreeze())){
            	
                 PrpLPayCustomVo payCustomVo = new PrpLPayCustomVo();
                 payCustomVo.setId(id);
                 payCustomVo.setFrostFlag("1");
                
                 id = payCustomService.saveOrUpdatePayCustomById(payCustomVo,userVo);
            }
           
        }
        catch(Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
      
        ar.setData(vo);
        ar.setStatus(200);
        return ar;

    }

    
    /**
     * 
     * @param payeeName
     * @param accountNo
     * @return
     */
   @RequestMapping(value = "/vaildAccoundNo.ajax")
   @ResponseBody
    public AjaxResult vaildAccoundNo(String payeeName,String accountNo,Long paycustId,String fxqIndex,String compensateNo,String payReason){
	      AjaxResult ajax=new AjaxResult();
	      String index="0";
	      try{
	    	  if(StringUtils.isBlank(fxqIndex)){
	    		  // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? ?????????
		    	  PrpLPayCustomVo payCustomVo=new PrpLPayCustomVo();
		    	  payCustomVo.setAccountNo(accountNo);
		    	  payCustomVo.setPayeeName(payeeName);
		    	  payCustomVo.setId(paycustId);
		  		  PrpLPayCustomVo existPayCus = payCustomService.adjustExistSamePayCusDifName(payCustomVo);
		  		if(existPayCus!=null){
		  			logger.info("????????????????????????????????????????????????????????? " + existPayCus.getRegistNo() + ", ???????????? " + existPayCus.getPayeeName() + ", ????????? " + existPayCus.getAccountNo());
		  			throw new IllegalArgumentException("???????????????????????????????????????"+existPayCus.getRegistNo()+"???????????????"+existPayCus.getPayeeName()+"???");
		  		}
	    	  }
	    	  if(StringUtils.isNotBlank(compensateNo) && paycustId != null ){
	    		  if(compensateTaskService.existPayCustom(accountNo,compensateNo,paycustId,payReason)){
			  			throw new IllegalArgumentException("??????????????????????????????????????????");
	    		  }
	    	  }
	    	  PrpLPaymentVo paymentVo= compensateTaskService.findByPayeeNameAndAccountNo(payeeName,accountNo,paycustId);
	    	  if(paymentVo!=null && StringUtils.isNotBlank(paymentVo.getPayeeName())){
	    		  index="1";
	    	  }
	     
	      ajax.setData(index);
	      ajax.setStatusText(paymentVo.getRegistNo()+","+paymentVo.getPayeeName());
	      ajax.setStatus(HttpStatus.SC_OK);
	     
	      }catch(Exception e){
	    	  ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	    	  ajax.setStatusText("???????????????"+e.getMessage());
	    	  e.printStackTrace();
	      }
	      return ajax;
    	
    }
   @RequestMapping(value = "/validAccountIsSame.ajax")
   @ResponseBody
    public AjaxResult validAccountIsSame(String payeeName,String accountNo) {
	      AjaxResult ajax=new AjaxResult();
	      SysUserVo userVo = WebUserUtils.getUser();
	      String index="0";
	      try{
		      String oldPayName = payCustomService.findByPayeeNameAndAccountNo(payeeName,accountNo,userVo);
		      if(StringUtils.isNotBlank(oldPayName)){
		    	  index = "1";
			      ajax.setStatusText(oldPayName);
		      }
		      ajax.setData(index);
		      ajax.setStatus(HttpStatus.SC_OK);
	      }catch(Exception e){
	    	  ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	    	  ajax.setStatusText("???????????????"+e.getMessage());
	    	  logger.error("????????????" + payeeName + "???????????????:" +accountNo + "??????????????????????????????????????????",e);
	      }
	      return ajax;
    	
    }
    
    /**
     * 
     * <pre></pre>
     * @param request
     * @return
     * @modified:
     * ???zhujunde(2017???6???23??? ??????4:47:38): <br>
     */
    @RequestMapping(value = "/getCustRiskInfoOrSave.do")
    @ResponseBody
    public AjaxResult getCustRiskInfo(@FormModel("prpLPayCustomVo") PrpLPayCustomVo payCustomVo,
                                      @FormModel("flag") String flag ,@FormModel("frostFlags") String frostFlags) {
        AjaxResult ar = new AjaxResult();
        String amlUrl = SpringProperties.getProperty("dhic.aml.saveurl");
        logger.info("amlUrl============================"+amlUrl);
        String isFrostFlag = "1";
        //????????????
        if(StringUtils.isNotBlank(payCustomVo.getSummary())){
            payCustomVo.setSummary(payCustomVo.getSummary().replaceAll("\\s*", ""));
        }
        
        SysUserVo userVo = WebUserUtils.getUser();
        // ????????????????????????????????????????????? ????????????????????????????????????  ---??????remark??????Y????????????????????????
        payCustomVo.setRemark(flag);
        payCustomVo.setAccountNo(payCustomVo.getAccountNo().replaceAll("\\s*", ""));
        payCustomVo.setCertifyNo(payCustomVo.getCertifyNo().replaceAll("\\s*", ""));
        payCustomVo.setPayeeName(payCustomVo.getPayeeName().replaceAll("\\s*", ""));
        Long id = (long)0;
		try {

			id = payCustomService.saveOrUpdatePayCustom(payCustomVo, userVo);
			ar.setData("N");
			ar.setStatus(200);
			ar.setStatusText("????????????!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ar.setStatusText(e.getMessage());
			e.printStackTrace();
		}

		return ar;
    }

    @RequestMapping(value = "/saveMoneyInfo.do")
    @ResponseBody
    public AjaxResult saveMoneyInfo(@FormModel("prpLPayFxqCustomVo") PrpLPayFxqCustomVo payFxqCustomVo,
                                    @FormModel("prpLFxqFavoreeVo") PrpLFxqFavoreeVo lFxqFavoreeVo){
    	AjaxResult ajax=new AjaxResult();
    	 SysUserVo userVo = WebUserUtils.getUser();
    	 try {
			payCustomService.saveOrupdatePrpLFxqFavoreeCustom(lFxqFavoreeVo, userVo);
			payCustomService.saveOrupdatePrpLPayFxqCustom(payFxqCustomVo, userVo);
			ajax.setStatus(200);
			ajax.setStatusText("????????????");
		} catch (Exception e) {
			ajax.setStatus(500);
			ajax.setStatusText("????????????"+e.getMessage());
			e.printStackTrace();
		}
    	
    	return ajax;
    }
    
    @RequestMapping(value = "/isFrostFlag.ajax")
    @ResponseBody
    public AjaxResult isFrostFlag(Long id) {
        AjaxResult ar = new AjaxResult();
        PrpLPayCustomVo vo = payCustomService.findPayCustomVoById(id);
        if("0".equals(vo.getFrostFlag())){//??????
            ar.setData("0"); 
        }else{
            ar.setData("1");
        }
        ar.setStatus(200);
        return ar;

    }
    
    
    	/**
    	 * ???????????????
    	 * @param claimNo
    	 * @param registNo
    	 * @param riskCode
    	 * @return
    	 */
    	@RequestMapping(value = "/freezeUp.do")
    	public ModelAndView freezeUp(String claimNo,String registNo,String riskCode){
    		ModelAndView  mv=new ModelAndView();
    		AMLVo amlVo = new AMLVo();
    		String sign="3";//1-?????????????????????2-??????????????????
            PrpLPayCustomVo prpLPayCustomVo = new PrpLPayCustomVo();
            PrpLPayFxqCustomVo prpLPayFxqCustomVo = new PrpLPayFxqCustomVo();
            PrpLFxqFavoreeVo prpLFxqFavoreeVo = new PrpLFxqFavoreeVo();
            List<PrpLPayFxqCustomVo> prpLPayFxqCustomVoList=new ArrayList<PrpLPayFxqCustomVo>();
            List<PrpLFxqFavoreeVo> prpLFxqFavoreeVoList=new ArrayList<PrpLFxqFavoreeVo>();
            Long mainId=null;
            List<PrpLPayCustomVo>  custmVos=payCustomService.findPayCustomVoByRegistNo(registNo);
            if(custmVos!=null && custmVos.size()>0){
            	prpLPayCustomVo=custmVos.get(0);
            	mainId=prpLPayCustomVo.getId();
            }
            if(StringUtils.isNotBlank(claimNo)){
            	prpLPayFxqCustomVoList = payCustomService.findPrpLPayFxqCustomVoByclaimNo(claimNo);
            	if(prpLPayFxqCustomVoList!=null && prpLPayFxqCustomVoList.size()>0){
                    prpLPayFxqCustomVo = prpLPayFxqCustomVoList.get(0);
                }
            	
            	 prpLFxqFavoreeVoList = payCustomService.findPrpLFxqFavoreeVoByclaimNo(claimNo);
                if(prpLFxqFavoreeVoList!=null && prpLFxqFavoreeVoList.size()>0){
                    prpLFxqFavoreeVo = prpLFxqFavoreeVoList.get(0);
                }
                if((prpLPayFxqCustomVoList==null && prpLFxqFavoreeVoList==null) || (prpLPayFxqCustomVoList.size()==0 && prpLFxqFavoreeVoList.size()==0)){
                	 if(mainId!=null){
                     	prpLPayCustomVo = payCustomService.findPayCustomVoById(mainId);
                         prpLPayFxqCustomVoList = payCustomService.findPrpLPayFxqCustomVoByPayId(mainId);
                         if(prpLPayFxqCustomVoList!=null && prpLPayFxqCustomVoList.size()>0){
                             prpLPayFxqCustomVo = prpLPayFxqCustomVoList.get(0);
                         }
                          prpLFxqFavoreeVoList = payCustomService.findPrpLFxqFavoreeVoByPayId(mainId);
                         if(prpLFxqFavoreeVoList!=null && prpLFxqFavoreeVoList.size()>0){
                             prpLFxqFavoreeVo = prpLFxqFavoreeVoList.get(0);
                         }
                         sign="1";
                     }
                }else{
                	sign="2";
                }
            }else{
            	 if(mainId!=null){
                 	prpLPayCustomVo = payCustomService.findPayCustomVoById(mainId);
                    prpLPayFxqCustomVoList = payCustomService.findPrpLPayFxqCustomVoByPayId(mainId);
                     if(prpLPayFxqCustomVoList!=null && prpLPayFxqCustomVoList.size()>0){
                         prpLPayFxqCustomVo = prpLPayFxqCustomVoList.get(0);
                     }
                     prpLFxqFavoreeVoList = payCustomService.findPrpLFxqFavoreeVoByPayId(mainId);
                     if(prpLFxqFavoreeVoList!=null && prpLFxqFavoreeVoList.size()>0){
                         prpLFxqFavoreeVo = prpLFxqFavoreeVoList.get(0);
                     }
                     sign="1";
                 }
            }
            //???????????????
            if("1".equals(sign)){
            	amlVo.setAuthorityName(prpLPayCustomVo.getAuthorityName());
                amlVo.setAuthorityNo(prpLPayCustomVo.getAuthorityNo());
                
            }else if("2".equals(sign)){
            	amlVo.setAuthorityName(prpLPayFxqCustomVo.getAuthorityName());
                amlVo.setAuthorityNo(prpLPayFxqCustomVo.getAuthorityNo());
                  
            }
            
            //?????????
            if(prpLFxqFavoreeVo!=null){
            	amlVo.setFavoreeIdentifyCode(prpLFxqFavoreeVo.getFavoreeIdentifyCode());
            	amlVo.setFavoreeName(prpLFxqFavoreeVo.getFavoreeName());
            }
            
            //????????????
            List<PrpLCMainVo> cmainVos= prpLCMainService.findPrpLCMainsByRegistNo(registNo);
            if(cmainVos!=null && cmainVos.size()>0){
            	for(PrpLCMainVo vo:cmainVos){
            		if(StringUtils.isNotBlank(riskCode) && riskCode.equals(vo.getRiskCode())){
            			 List<PrpLCInsuredVo> PrpLCInsuredList = vo.getPrpCInsureds();
            		            for(PrpLCInsuredVo cInsured:PrpLCInsuredList){
            		                if("1".equals(cInsured.getInsuredFlag())){
            		                	
            		                    amlVo.setInsuredName(cInsured.getInsuredName());
            		                    amlVo.setIdentifyNumber(cInsured.getIdentifyNumber());
            		                    
            		                }
            		               
            		            }
            		        }
            	}
            }
            mv.addObject("amlVo", amlVo);
            mv.setViewName("payCustom/PaycustmfreezeUp");
    		return mv;
    	}
  /**
   * 
   * @param name
   * @param code yzyfxq
   * @return
   */
    @RequestMapping(value = "/payfreezeUp.ajax")
    @ResponseBody
    public AjaxResult payfreezeUp(String name,String code){
    	AjaxResult ajax=new AjaxResult();
    	SysUserVo userVo = WebUserUtils.getUser();
    	 String amlUrl = SpringProperties.getProperty("dhic.aml.saveurl");
         logger.info("amlUrl============================"+amlUrl);
        
         try {
        	 FxqCrmRiskService crmRiskService = new FxqCrmRiskService(amlUrl);
             FreezeCustVo  freezeCustVo=new FreezeCustVo();
             freezeCustVo.setSystemCode("claimcar");
             freezeCustVo.setCustCode(code);
             freezeCustVo.setCustName(name);
             freezeCustVo.setOprateCode(userVo.getUserCode());
             freezeCustVo.setOprateType("0");
             freezeCustVo.setRemark("??????");
			 crmRiskService.saveFreezeInfo(freezeCustVo);
			ajax.setData("1");
			ajax.setStatus(200);
			ajax.setStatusText("????????????!");
		} catch (Exception e) {
			ajax.setStatusText("????????????!"+e.getMessage());
			e.printStackTrace();
		}
    	return ajax;
    }
    	
}
