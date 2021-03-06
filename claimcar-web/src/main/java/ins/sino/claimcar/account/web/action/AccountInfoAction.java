package ins.sino.claimcar.account.web.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import ins.framework.utils.Beans;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.RepairFactoryService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.manager.vo.PrplOldaccbankCodeVo;
import ins.sino.claimcar.other.service.AccountInfoService;
import ins.sino.claimcar.other.vo.PrpDAccRollBackAccountVo;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;
import ins.sino.claimcar.pay.service.PadPayPubService;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/accountInfo")
public class AccountInfoAction {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountInfoAction.class);
	
	@Autowired
	AccountInfoService accountInfoService;
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	ManagerService managerService;
	@Autowired
	private PayCustomService payCustomService;
	@Autowired
	PadPayPubService padPayPubService;
	
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private RepairFactoryService repairFactoryService;
	
	//?????????
	@RequestMapping("/accountInfoInit.do")
	@ResponseBody
	public ModelAndView accountInfoInit(String compensateNo,String handleStatus,
	    String registNo,String payeeId,String isVerify,String payType,String serialNo) throws ParseException {
		ModelAndView modelAndView = new ModelAndView();
		PrpLPayBankVo payBankVo = new PrpLPayBankVo();
		List<PrpLPayBankVo> payBankVoList = new ArrayList<PrpLPayBankVo>();
		PrpLClaimVo claimVo = new PrpLClaimVo();
		PrpDAccRollBackAccountVo rollBackAccountVo = new PrpDAccRollBackAccountVo();
		PrpLPayCustomVo paycustomVo =  new PrpLPayCustomVo();
		String chargeCode = CodeConstants.TransPayTypeMap.get(payType);
		//???????????????
		if(compensateNo.startsWith("P") || compensateNo.startsWith("G") || compensateNo.startsWith("J")){
			if("0".equals(handleStatus) && "0".equals(isVerify)){
				rollBackAccountVo = accountInfoService.findRollBackByOldAccountId(compensateNo, serialNo, payType);
				payBankVoList = accountInfoService.organizePayBankVo(rollBackAccountVo);
				payBankVo = payBankVoList.get(0);
				payBankVo.setSerialNo(serialNo);
			}else{
				rollBackAccountVo = accountInfoService.findRollBackAccount(compensateNo, serialNo, payType);
				if(rollBackAccountVo==null || rollBackAccountVo.getId()==null){
					rollBackAccountVo = accountInfoService.findRollBackByOldAccountId(compensateNo, serialNo, payType);
				}
				payBankVoList = accountInfoService.organizePayBankVo(rollBackAccountVo);
				payBankVo = accountInfoService.findOldPayBank(registNo,compensateNo,payeeId,payType);
				payBankVo.setSerialNo(serialNo);
			}
		} else {//??????????????????
			PrpLPayBankVo oldPayBankVo = null;
			// ?????????????????????
			paycustomVo = managerService.findPayCustomVoById(Long.valueOf(payeeId));
			// ??????????????????????????????????????????
			oldPayBankVo = accountInfoService.findOldPayBank(registNo,compensateNo,payeeId,payType);

			if(!"0".equals(handleStatus) || !"0".equals(isVerify)){
				rollBackAccountVo = accountInfoService.findRollBackAccount(compensateNo,paycustomVo.getAccountId(),payType);
			}

			if (oldPayBankVo != null) {
				// ??????????????????
				Beans.copy().from(oldPayBankVo).to(payBankVo);
				payBankVo.setErrorType(rollBackAccountVo.getErrorType());
				if("0".equals(handleStatus) && "0".equals(isVerify)){
					payBankVo.setIsAutoPay(rollBackAccountVo.getIsAutoPay());
				}
			} else {
				// ??????????????????????????????????????????????????????
				payBankVo = accountInfoService.getPayBankVo(compensateNo,paycustomVo,registNo,payType);
				payBankVo.setChargeCode(chargeCode);
				payBankVo.setPayType(payType);
				payBankVo.setIsAutoPay(rollBackAccountVo.getIsAutoPay());
				payBankVo.setErrorType(rollBackAccountVo.getErrorType());
			}
			if (compensateNo.startsWith("D")) {
				// ??????????????????
				PrpLPadPayMainVo padPayVo = padPayPubService.queryPadPay(registNo, compensateNo);
				if(padPayVo.getUnderwriteDate() != null){
					//??????????????????
					payBankVo.setvClaimTime(padPayVo.getUnderwriteDate());
				}
				claimVo = claimTaskService.findClaimVoByClaimNo(padPayVo.getClaimNo());
			} else {
				//???????????????????????????
				PrpLCompensateVo compensateVo = compensateTaskService.findPrpLCompensateVoByPK(compensateNo);
				if (compensateVo.getUnderwriteDate() != null) {
					//??????????????????
					payBankVo.setvClaimTime(compensateVo.getUnderwriteDate());
				}
				claimVo = claimTaskService.findClaimVoByClaimNo(compensateVo.getClaimNo());
			}
			if (rollBackAccountVo.getRollbackCode()!=null) {
				payBankVo.setAppUser(rollBackAccountVo.getRollbackCode());
			}
			if (claimVo.getEndCaseTime() != null) {
				// ??????????????????
				payBankVo.setEndCaseTime(claimVo.getEndCaseTime());
			}
			//??????????????????
			payBankVo.setInsuredCode(payBankVo.getRemark());
			payBankVo.setSettleNo(compensateNo);
			payBankVo.setSerialNo(serialNo);
			payBankVoList.add(payBankVo);
		}
		
		modelAndView.addObject("compensateNo", compensateNo);
		modelAndView.addObject("payBankVo", payBankVo);
		modelAndView.addObject("payBankVoList", payBankVoList);
		modelAndView.addObject("status", rollBackAccountVo.getStatus());
		if(rollBackAccountVo.getRollBackTime() != null){
			modelAndView.addObject("appTime", rollBackAccountVo.getRollBackTime());
		}
		modelAndView.addObject("appUser", rollBackAccountVo.getRollbackCode());
		modelAndView.addObject("errorMessage", rollBackAccountVo.getErrorMessage());
		modelAndView.addObject("accountId", rollBackAccountVo.getAccountId());
		modelAndView.addObject("handleStatus", handleStatus);
		// ????????????
		modelAndView.addObject("isVerify", isVerify);
		modelAndView.setViewName("base/accountInfo/AccountInfoEdit");
		return modelAndView;
	}
	
	/**
	 * <pre>???????????????????????????????????????prplpaybank???</pre>
	 * @param compensateNo
	 * @param payCustomVo
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ???Luwei(2016???12???27??? ??????5:11:55): <br>
	 */
	@RequestMapping("/saveAccountInfo.do")
	@ResponseBody
	public AjaxResult saveAccountInfo(@FormModel("compensateNo") String compensateNo,
	       @FormModel("prpLPayCustomVo") PrpLPayCustomVo payCustomVo,
	       @FormModel(value = "payBankVo") PrpLPayBankVo payBankVo){
		AjaxResult ajaxResult = new AjaxResult();
		
		SysUserVo userVo = WebUserUtils.getUser();
		try{
			//??????PrpDAccRollBackAccount.status??????0?????????????????????????????????????????????????????????????????????
			payBankVo.setFlag("0");
			payBankVo.setCompensateNo(compensateNo);
			accountInfoService.updateAccRollBackAccountStatus(payBankVo);
			
			//??????????????????
			String verifyStatus = accountInfoService.saveRollbackAccountInfo(compensateNo, payCustomVo, payBankVo, userVo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(verifyStatus);
			ajaxResult.setStatusText("???????????????");
		}catch(Exception e){
			// TODO Auto-generated catch block
			ajaxResult.setStatusText("???????????????"+e.getMessage());
		}
		return ajaxResult;
	}
	
	/**
	 * <pre>????????????????????????</pre>
	 * @param payBankVo
	 * @return
	 * @modified:
	 * ???Luwei(2016???12???28??? ??????9:54:14): <br>
	 */
	@RequestMapping("/submitAccountVerify.do")
	@ResponseBody
	public AjaxResult saveAccountVerify(@FormModel("payBankVo") PrpLPayBankVo payBankVo,
	       @RequestParam("originalAccountNo") String originalAccountNo){
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo userVo = WebUserUtils.getUser();
		try{
			//????????????????????????????????????
			if("1".equals(payBankVo.getVerifyHandle())){
				accountInfoService.updatePayBank(payBankVo.getId());
			}
			String returnPayeeId=accountInfoService.accountSubmit(payBankVo,userVo,originalAccountNo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(returnPayeeId);
			ajaxResult.setStatusText("???????????????");
		}catch(Exception e){
			// TODO Auto-generated catch block
			ajaxResult.setStatusText("???????????????"+e.getMessage());
		}
		return ajaxResult;
	}
	
	@RequestMapping("/saveAccountHis.do")
	@ResponseBody
	public AjaxResult saveAccountHis(String payeeId){
		AjaxResult ajaxResult = new AjaxResult();
		PrpLPayCustomVo paycustomVo = managerService.findPayCustomVoById(Long.valueOf(payeeId));
		accountInfoService.savePayBankHis(paycustomVo);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		return ajaxResult;
	}
	
	@RequestMapping("/saveData.do")
	@ResponseBody
	public AjaxResult saveData(@FormModel(value = "payBankVo") PrpLPayBankVo payBankVo,
								@RequestParam("originalAccountNo") String originalAccountNo) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		Date date = new Date();
		payBankVo.setCreateTime(date);
		payBankVo.setUpdateTime(date);
		payBankVo.setCreateUser(WebUserUtils.getUserCode());
		payBankVo.setUpdateUser(WebUserUtils.getUserCode());
		SysUserVo userVo = WebUserUtils.getUser();
		try{
			PrpLPayCustomVo paycustomVo = managerService.findPayCustomVoById(payBankVo.getPayeeId());
			accountInfoService.savePayBankHis(paycustomVo);
			payBankVo.setRemark("2");
			accountInfoService.saveAcc(payBankVo,userVo,originalAccountNo,payBankVo.getPayeeId());
			ajaxResult.setStatusText("???????????????");
			ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
		}
		catch(Exception e){
			logger.info("??????????????????????????????" + e);
			ajaxResult.setStatusText("" + e);
			e.printStackTrace();
		}
		return ajaxResult;
	}
	
	@RequestMapping("/payCustomView.do")
	@ResponseBody
	public ModelAndView payCustomView(String payeeId,String registNo,String compensateNo,String payType){
		ModelAndView modelAndView = new ModelAndView();
		PrpLPayCustomVo payCustomVo = payCustomService.findPayCustomVoById(Long.valueOf(payeeId));
		PrpLPayBankVo payBankVo = accountInfoService.findOldPayBank(registNo, compensateNo, payeeId, payType);
		if(payBankVo != null){
			payCustomVo.setPayObjectKind(payBankVo.getPayObjectKind());
			payCustomVo.setPayObjectType(payBankVo.getPayObjectType());
			payCustomVo.setCertifyNo(payBankVo.getPayeeIDNumber());
			payCustomVo.setPayeeName(payBankVo.getPayee());
			payCustomVo.setAccountNo(payBankVo.getAccountNo());
			payCustomVo.setBankType(payBankVo.getBankType());
			payCustomVo.setBankName(payBankVo.getBankName());
			payCustomVo.setBankNo(payBankVo.getBankNo());
			payCustomVo.setBankOutlets(payBankVo.getBankOutlets());
			payCustomVo.setProvinceCode(payBankVo.getProvinceCode());
			payCustomVo.setProvince(payBankVo.getProvince());
			payCustomVo.setCity(payBankVo.getCity());
			payCustomVo.setCityCode(payBankVo.getCityCode());
			payCustomVo.setPriorityFlag(payBankVo.getPriorityFlag());
			payCustomVo.setPayeeMobile(payBankVo.getPayeeMobile());
			payCustomVo.setPurpose(payBankVo.getPurpose());
			payCustomVo.setPublicAndPrivate(payBankVo.getPublicAndPrivate());
			payCustomVo.setSummary(payBankVo.getSummary());
			payCustomVo.setCertifyType(payBankVo.getCertifyType());
			payCustomVo.setRepairFactoryCode(payBankVo.getRepairFactoryId());//?????????ID
			
			if("6".equals(payCustomVo.getPayObjectKind()) && StringUtils.isNotBlank(payCustomVo.getRepairFactoryCode())){
				PrpLRepairFactoryVo prpLRepairFactoryVo=repairFactoryService.findFactoryById(payCustomVo.getRepairFactoryCode());
				if(prpLRepairFactoryVo!=null && prpLRepairFactoryVo.getId()!=null){
					modelAndView.addObject("factoryName", prpLRepairFactoryVo.getFactoryName());
					
				}
			}
		}
	
	Map<String,String> bankCodeMap = new HashMap<String,String>();
	List<PrplOldaccbankCodeVo> listVo=payCustomService.findPrplOldaccbankCodeByFlag("1");
	if(listVo!=null && listVo.size()>0){
     for(PrplOldaccbankCodeVo vo:listVo){
	   bankCodeMap.put(vo.getBankCode(), vo.getBankName());
	  }
	}
        modelAndView.addObject("bankCodeMap",bankCodeMap);
        modelAndView.addObject("prpLPayCustom", payCustomVo);
		modelAndView.addObject("flag", "N");
//		modelAndView.addObject("prpLPayBankHisVo", prpLPayBankHisVo);
		modelAndView.setViewName("base/accountInfo/PayCustomView");
		return modelAndView;
	}
	
	/**
	 * ????????????????????????Action
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/payCustomEdit.do")
	public ModelAndView intermediaryEdit(HttpServletRequest request) {
		String flag = request.getParameter("flag");
		String registNo = request.getParameter("registNo");
		// ???????????????paycustom.id
		String payId = request.getParameter("payId");
		String nodeCode = request.getParameter("nodeCode");
		String compensateNo = request.getParameter("compensateNo");
		String accountId = request.getParameter("accountId");
		String chargeCode = request.getParameter("chargeCode");
		String payType = request.getParameter("payType");
		String isAutoPay = request.getParameter("isAutoPay");
		String errorType = request.getParameter("errorType");
		String payRefReason = request.getParameter("payRefReason");
		// ?????????????????? payment.serialNo
		String serialNo = request.getParameter("serialNo");
		ModelAndView mv = new ModelAndView();
		PrpLPayCustomVo payCustomVo = new PrpLPayCustomVo();
		Long paycustomId = null;
		if (StringUtils.isNotBlank(payId)) {
			paycustomId = Long.parseLong(payId);
		}

		if (paycustomId != null) {
			// ????????????????????????
			payCustomVo = payCustomService.findPayCustomVoById(paycustomId);
		}
		// ??????????????????
		PrpLCompensateVo compensateVo = compensateTaskService.findPrpLCompensateVoByPK(compensateNo);
		if (compensateVo != null && compensateVo.getLawsuitFlag() != null) {
			mv.addObject("payLawsuitFlag", compensateVo.getLawsuitFlag());
		} else {
			mv.addObject("payLawsuitFlag", "0");
		}

		PrpLPayBankVo payBankVo = new PrpLPayBankVo();
		// ????????????????????????????????????
		PrpLPayBankVo oldPayBankVo = accountInfoService.findOldPayBank(registNo, compensateNo, payId, payType);
		if (oldPayBankVo != null) {
			payBankVo = oldPayBankVo;
		} else {
			// ????????????????????????????????????????????????
			PrpDAccRollBackAccountVo rollBackAccountVo = accountInfoService.findRollBackBySerialNo(compensateNo, serialNo, payType);
			if (compensateNo.startsWith("P") || compensateNo.startsWith("G") || compensateNo.startsWith("J")) {
				payBankVo = accountInfoService.organizePayBankVo(rollBackAccountVo).get(0);
			} else {
				payBankVo = accountInfoService.getPayBankVo(compensateNo, payCustomVo, registNo, payType);
				payBankVo.setChargeCode(chargeCode);
				payBankVo.setPayType(payType);
				payBankVo.setErrorMessage(rollBackAccountVo.getErrorMessage());
			}
		}
		payBankVo.setIsAutoPay(isAutoPay);
		payBankVo.setErrorType(errorType);
		payBankVo.setSerialNo(serialNo);
		if (payBankVo.getSummary() != null && !payBankVo.getSummary().isEmpty()) {
			payCustomVo.setSummary(payBankVo.getSummary());
		}

		// ???????????????????????????????????????
		Map<String, String> dictMap = new HashMap<String, String>();
		List<PrpLDlossCarMainVo> lossCarMains = lossCarService.findLossCarMainByRegistNo(registNo);
		if (lossCarMains != null && lossCarMains.size() > 0) {
			for (PrpLDlossCarMainVo lossCarMainVo : lossCarMains) {
				if (!StringUtils.isBlank(lossCarMainVo.getRepairFactoryCode())) {
					// PrpLDlossCarMain?????????RepairFactoryCode??????????????????PrpLRepairFactory??????Id
					PrpLRepairFactoryVo factoryCodevo = repairFactoryService.findFactoryById(lossCarMainVo.getRepairFactoryCode());
					if (factoryCodevo != null) {
						dictMap.put(lossCarMainVo.getRepairFactoryCode(), factoryCodevo.getFactoryName());
					}
				}
			}
		}
		if ("N".equals(flag) || "S".equals(flag)) {
			//????????????????????????Map??????
			Map<String, String> bankCodeMap = new HashMap<String, String>();
			List<PrplOldaccbankCodeVo> listVo = payCustomService.findPrplOldaccbankCodeByFlag("1");
			if (listVo != null && listVo.size() > 0) {
				for (PrplOldaccbankCodeVo vo : listVo) {
					bankCodeMap.put(vo.getBankCode(), vo.getBankName());
				}
			}
			mv.addObject("bankCodeMap", bankCodeMap);
		}
		mv.addObject("dictMap", dictMap);
		mv.addObject("payBankVo", payBankVo);
		mv.addObject("prpLPayCustom", payCustomVo);
		mv.addObject("compensateNo", compensateNo);
		mv.addObject("registNo", registNo);
		mv.addObject("nodeCode", nodeCode);
		mv.addObject("flag", flag);
		mv.addObject("payRefReason", payRefReason);
		// mv.addObject("originalAccountNo", accountId);//?????????
		mv.setViewName("base/accountInfo/ModPayCustomEdit");

		return mv;
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
			
			//??????????????????????????????
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}
}
