package ins.sino.claimcar.endcase.web.action;

import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.FeeType;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateExtVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPayHisVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.EndCaseService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;



/**
 * 结案Action
 * @author ★Luwei
 */
@Controller
@RequestMapping("/endCase")
public class EndCaseAction {
	private Logger logger = LoggerFactory.getLogger(EndCaseAction.class);
	@Autowired
	EndCaseService endCaseService;

	@Autowired
	ClaimTaskService claimTaskService;

	@Autowired
	CompensateTaskService compeService;

	@Autowired
	RegistQueryService registQueryService;

	@Autowired
	PolicyViewService policyViewService;

	@Autowired
	WfTaskHandleService wfTaskHandleService;

	@Autowired
	CheckTaskService checkTaskService;

	@Autowired
	PadPayPubService padPayService;
	
	@Autowired
	ManagerService managerService;
	
	@Autowired
    LossCarService lossCarService;
	
	@Autowired
	private AssessorService assessorService;
	
	@Autowired
	private CompensateTaskService compensateTaskService;

	
	@RequestMapping("/endCaseEdit.do")
	public ModelAndView endCaseEdit(Double flowTaskId) {
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		return initEndCaseByWf(wfTaskVo);
	}

	/**
	 * 初始化结案
	 * @param claimNo
	 * @return
	 * @modified: ☆Luwei(2016年4月5日 下午5:46:11): <br>
	 */
	@RequestMapping("/initEndCase")
	public ModelAndView initEndCaseByWf(PrpLWfTaskVo wfTaskVo) {
		ModelAndView mav = new ModelAndView();

		String registNo = wfTaskVo.getRegistNo();
		String compeNo = wfTaskVo.getHandlerIdKey();
		String claimNo = wfTaskVo.getClaimNo();

		// 获取结案信息
		PrpLEndCaseVo endCaseVo = endCaseService.queryEndCaseVo(registNo,claimNo);
		mav.addObject("endCaseVo",endCaseVo);
		int endCasNum = endCaseService.getEndCaseNum(registNo,claimNo,compeNo); //获取结案次数
		mav.addObject("num",endCasNum);

		String handlerCode = "";
		if(endCaseVo != null){
			String policyNo = endCaseVo.getPolicyNo();
			PrpLCMainVo cMainVo = policyViewService.getPolicyInfo(registNo,policyNo);
			handlerCode = cMainVo.getHandler1Name();
		}
		mav.addObject("handlerCode",handlerCode);

		// String claimNo = endCaseVo.getClaimNo();
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
		mav.addObject("claimVo",claimVo);

		// 获取标的车定损信息
		PrpLDlossCarMainVo dlossMainVo = endCaseService.getDLossCarInfo(registNo);
		mav.addObject("dlossMainVo",dlossMainVo);//
		mav.addObject("dlossCarVo",dlossMainVo.getLossCarInfoVo());
		if(dlossMainVo.getDeflossCarType().equals("1")){
			PrpLCItemCarVo prpLCItemCarVo = registQueryService.findCItemCarByRegistNo(registNo);
			mav.addObject("prpLCItemCarVo",prpLCItemCarVo);
		}

		// 获取查勘信息
		PrpLCheckVo  checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		mav.addObject("checkVo",checkVo);

		// 获取报案信息
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		mav.addObject("registVo",registVo);

		// 号牌底色
		PrpLCheckCarVo checkCarVo = checkTaskService.findCheckCarBySerialNo(registNo,1);
		PrpLCheckCarInfoVo carInfo = checkCarVo.getPrpLCheckCarInfo();
		mav.addObject("linceColor",carInfo.getLicenseColor());
		
		//理算信息
		PrpLCompensateVo prpLCompensateVo = compeService.findCompByPK(compeNo);
		if(prpLCompensateVo!=null){
			setPayeeInfo(prpLCompensateVo);
		}
		mav.addObject("compVo",prpLCompensateVo);

		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);
		if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){
			if(prpLCompensateVo != null && prpLCompensateVo.getPrpLPayments()!=null && prpLCompensateVo.getPrpLPayments().size()>0){
				for(int i=0 ; i < prpLCompensateVo.getPrpLPayments().size() ; i++){
					prpLCompensateVo.getPrpLPayments().get(i).setAccountNo(DataUtils.replacePrivacy(prpLCompensateVo.getPrpLPayments().get(i).getAccountNo()));
				}
			}
			if(prpLCompensateVo != null && prpLCompensateVo.getPrpLCharges()!=null && prpLCompensateVo.getPrpLCharges().size()>0){
				for(int i=0 ; i < prpLCompensateVo.getPrpLCharges().size() ; i++){
					prpLCompensateVo.getPrpLCharges().get(i).setAccountNo(DataUtils.replacePrivacy(prpLCompensateVo.getPrpLCharges().get(i).getAccountNo()));
				}
			}
		}
		
		//理算信息
		List<PrpLCompensateVo> PAY_CI = new ArrayList<PrpLCompensateVo>();//交强
		List<PrpLCompensateVo> PAY_BI = new ArrayList<PrpLCompensateVo>();//商业
		if(prpLCompensateVo!=null){
			if(Risk.DQZ.equals(prpLCompensateVo.getRiskCode())){
				PAY_CI.add(prpLCompensateVo);
			}else{
				PAY_BI.add(prpLCompensateVo);
			}
		}
//		List<PrpLCompensateVo> compeVoList = compeService.queryCompensate(registNo,"N");
//		for(PrpLCompensateVo compeVo : compeVoList){
//			setPayeeInfo(compeVo);
//			if(Risk.DQZ.equals(compeVo.getRiskCode())){
//				PAY_CI.add(compeVo);
//			}else{
//				PAY_BI.add(compeVo);
//			}
//		}
		
		mav.addObject("PAY_CI",PAY_CI);
		mav.addObject("PAY_BI",PAY_BI);
		
		// 预付
		List<PrpLCompensateVo> compePrepayVos = compeService.queryCompensate(registNo,"Y");
		List<PrpLPrePayVo> prePay_ci = new ArrayList<PrpLPrePayVo>();// 交强预付
		List<PrpLPrePayVo> prePay_bi = new ArrayList<PrpLPrePayVo>();// 商业预付
		if(compePrepayVos != null && compePrepayVos.size() > 0){
			for(PrpLCompensateVo compePrepayVo : compePrepayVos){
				PrpLCompensateExtVo extVo = compePrepayVo.getPrpLCompensateExt();
				if(extVo != null && "1".equals(extVo.getIsCompDeduct())){
					setPayeeInfo(compePrepayVo);
					String comNo = compePrepayVo.getCompensateNo();
					List<PrpLPrePayVo> prePayVos  = new ArrayList<PrpLPrePayVo>();
				
					List<PrpLPrePayVo>  prePayPVos =  null;
				
					//获取剩余预付冲销费用
					List<PrpLPrePayVo> prePayFVos = null;
					try{
						prePayPVos =  compensateTaskService.getRemainPrePayWriteVo(comNo,"P");
						if(prePayPVos != null && !prePayPVos.isEmpty()){
							prePayVos.addAll(prePayPVos);
						}
						prePayFVos = compensateTaskService.getRemainPrePayWriteVo(comNo,"F");

						if(prePayFVos != null && !prePayFVos.isEmpty()){
							prePayVos.addAll(prePayFVos);
						}
					}catch(Exception e){
						logger.error("获取预付数据费用数据comNo=" + comNo,e);
					}
					if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){  //脱敏
						if(prePayVos != null && prePayVos.size()>0 ){
							for(int i=0 ; i < prePayVos.size() ; i++){
								prePayVos.get(i).setAccountNo(DataUtils.replacePrivacy(prePayVos.get(i).getAccountNo()));
							}
						}
					}
					
					if(prePayVos != null && prePayVos.size() > 0){
						for(PrpLPrePayVo prePayVo:prePayVos){
							if(Risk.DQZ.equals(prePayVo.getRiskCode())){// 交强
								prePay_ci.add(prePayVo);
							}else{
								prePay_bi.add(prePayVo);
							}
						}
					}
				}
			}
		}
		mav.addObject("prePay_ci",prePay_ci);
		mav.addObject("prePay_bi",prePay_bi);

		// 垫付
//		List<PrpLPadPayMainVo> padPayMainVos = padPayService.findPadPay(registNo);
		PrpLPadPayMainVo padPayMainVo = padPayService.queryPadPay(registNo,null);
		if(configValueVo!=null && "1".equals(configValueVo.getConfigValue())){
			if(padPayMainVo != null && padPayMainVo.getPrpLPadPayPersons()!=null && padPayMainVo.getPrpLPadPayPersons().size()>0){
				for(int i=0 ; i < padPayMainVo.getPrpLPadPayPersons().size() ; i++){
					padPayMainVo.getPrpLPadPayPersons().get(i).setAccountNo(DataUtils.replacePrivacy(padPayMainVo.getPrpLPadPayPersons().get(i).getAccountNo()));
				}
			}
		}
		mav.addObject("padPayVo",padPayMainVo);

		//垫付金额
		Double sumPad = calculatePad(padPayMainVo);
		
		 //核损清单打印
	       List<PrpLDlossCarMainVo> carMainVos= lossCarService.findLossCarMainByRegistNo(registNo);
	       String lossCarSign="0";//核损清单打印按钮是否置灰标志
	       if(carMainVos!=null && carMainVos.size()>0){
	    	   for(PrpLDlossCarMainVo vo:carMainVos){
	    		   if("1".equals(vo.getUnderWriteFlag())){
	    			   lossCarSign="1";
	    			   break;
	    		   }
	    	   }
	       }
	       
	       //理算计算书打印和赔款收据打印
	       String compensateSign="0";//页面按钮是否置灰的标志
	        List<PrpLCompensateVo> compensates=compensateTaskService.findCompensatevosByRegistNo(registNo);
	     	   if(compensates!=null && compensates.size()>0){
	     		   for(PrpLCompensateVo vo:compensates){
	     	        if("N".equals(vo.getCompensateType())){
	     	        	compensateSign="1";
	     	        	break;
	     	        }
	     			      
	     	         }
	     	      
	     	   }
	    	 
	    //页面公估费查看按钮是否亮显
	   	String assessSign="0";
	   	List<PrpLAssessorFeeVo> listFeeVo=assessorService.findPrpLAssessorFeeVoByRegistNo(registNo);
	   	if(listFeeVo!=null && listFeeVo.size()>0){
	   		for(PrpLAssessorFeeVo vo :listFeeVo){
				PrpLAssessorMainVo assessMainVo=assessorService.findAssessorMainVoById(vo.getAssessMainId());
				if(assessMainVo!=null && "3".equals(assessMainVo.getUnderWriteFlag())){
					assessSign="1";
					break;
				}
			}
	   	}
	   	mav.addObject("assessSign",assessSign);
	    mav.addObject("compensateSign",compensateSign);
	    mav.addObject("lossCarSign",lossCarSign);
		mav.addObject("realAmt_CI",calculateSum(PAY_CI,prePay_ci,"2"));//交强费用、赔款金额（1，赔款，2-费用）
		mav.addObject("sumPay_CI",calculateSum(PAY_CI,prePay_ci,"1")+sumPad);
		//商业费用、赔款金额
		mav.addObject("realAmt_BI",calculateSum(PAY_BI,prePay_bi,"2"));
		mav.addObject("sumPay_BI",calculateSum(PAY_BI,prePay_bi,"1"));

		mav.setViewName("endCase/EndCaseEdit");
		return mav;
	}
	
	@RequestMapping("/showPayHisEdit.do")
	public ModelAndView showPayHisEdit(Long otherId,String hisType) {
		ModelAndView mv = new ModelAndView();
		List<PrpLPayHisVo> PrpLPayHisVoList = compeService.showPayHis(otherId,hisType);
		mv.addObject("PrpLPayHisVoList",PrpLPayHisVoList);
		mv.setViewName("endCase/PayHisEdit");
		return mv;
	}

	/**
	 * <pre>计算费用，赔款金额</pre>
	 * @param compeVoList
	 * @param prePay
	 * @param payType 1，赔款，2-费用
	 * ☆Luwei(2016年9月30日 下午5:16:14): <br>
	 */
	private Double calculateSum(List<PrpLCompensateVo> compeVoList,List<PrpLPrePayVo> prePay,String payType) {
		BigDecimal sumPay = new BigDecimal(0);
		if(compeVoList!=null&&compeVoList.size()>0){
		for(PrpLCompensateVo compeVo : compeVoList){
			if("1".equals(payType)){//赔款
				for(PrpLPaymentVo sumRealPay : compeVo.getPrpLPayments()){// 实赔
					sumPay = sumPay.add(sumRealPay.getSumRealPay());
				}
			}
			if("2".equals(payType)){//费用
				sumPay = sumPay.add(compeVo.getSumPaidFee());
//				for(PrpLChargeVo sumRealPay : compeVo.getPrpLCharges()){// 实赔
//					sumPay = sumPay.add(sumRealPay.getFeeAmt());
//				}
			}
			for(PrpLPrePayVo per : prePay){// 预付
				String feeType = per.getFeeType();
				if("2".equals(payType)&&FeeType.FEE.equals(feeType)){//费用
					sumPay = sumPay.add(per.getPayAmt());
				}
				if("1".equals(payType)&&FeeType.PAY.equals(feeType)){//赔款
					sumPay = sumPay.add(per.getPayAmt());
				}
			}
		}
		}
		return sumPay.doubleValue();
	}
	
	/**
	 * <pre>计算垫付的金额</pre>
	 * @param padPayMainVo
	 * @modified:
	 * ☆Luwei(2016年9月30日 下午5:09:09): <br>
	 */
	private Double calculatePad(PrpLPadPayMainVo padPayMainVo){
		Double sumRealPay = 0.0;
		if(padPayMainVo != null){
			List<PrpLPadPayPersonVo> persons = padPayMainVo.getPrpLPadPayPersons();
			for(PrpLPadPayPersonVo person : persons){// 垫付
				sumRealPay += DataUtils.NullToZero(person.getCostSum()).doubleValue();
			}
		}
		return sumRealPay;
	}
	
	/**
	 * <pre>set赔款人信息</pre>
	 * @param compeVo
	 * @modified:
	 * ☆Luwei(2016年9月30日 下午5:23:11): <br>
	 */
	private void setPayeeInfo(PrpLCompensateVo compeVo){
		List<PrpLPaymentVo> paymentVoList = compeVo.getPrpLPayments();
		if(paymentVoList != null && paymentVoList.size() > 0){
			for(PrpLPaymentVo paymentVo : paymentVoList){
				PrpLPayCustomVo payCustomVo = managerService.
						findPayCustomVoById(paymentVo.getPayeeId());
				paymentVo.setPayeeName(payCustomVo.getPayeeName());
				paymentVo.setAccountNo(payCustomVo.getAccountNo());
				paymentVo.setBankName(payCustomVo.getBankName());
				paymentVo.setPayObjectKind(payCustomVo.getPayObjectKind());
			}
		}
		List<PrpLChargeVo> chargeVoList = compeVo.getPrpLCharges();
		if(chargeVoList != null && chargeVoList.size() > 0){
			for(PrpLChargeVo chargeVo : chargeVoList){
				PrpLPayCustomVo payCustomVo = managerService.
						findPayCustomVoById(chargeVo.getPayeeId());
				chargeVo.setPayeeName(payCustomVo.getPayeeName());
				chargeVo.setAccountNo(payCustomVo.getAccountNo());
				chargeVo.setBankName(payCustomVo.getBankName());
				chargeVo.setPayeeIdfNo(payCustomVo.getCertifyNo());
			}
		}
	}

}
