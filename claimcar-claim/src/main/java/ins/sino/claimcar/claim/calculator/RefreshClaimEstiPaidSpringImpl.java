package ins.sino.claimcar.claim.calculator;

import ins.framework.exception.BusinessException;
import ins.framework.lang.Datas;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.KINDCODE;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ConfigService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.ClaimFeeExt;
import ins.sino.claimcar.claim.vo.CompensateExp;
import ins.sino.claimcar.claim.vo.CompensateListVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.ThirdPartyLossInfo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("refreshClaimEstiPaidSpring")
public class RefreshClaimEstiPaidSpringImpl implements RefreshClaimEstiPaidService {

	private static final Log logger = LogFactory.getLog(RefreshClaimEstiPaidSpringImpl.class);
	@Autowired
	private RegistQueryService registQueryService;//报案Service
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private ClaimService claimService;
	@Autowired
	private CompensateService compensateService;
	@Autowired
	private ConfigService configService;
	
	public List<ClaimFeeExt> refreshClaimEstiPaid(CompensateListVo compensateList) {
		List<ClaimFeeExt> fees = new ArrayList<ClaimFeeExt>(0);
		ClaimFeeExt claimFeeExt ;
		String claimType=compensateList.getClaimFeeCondition().getClaimType();
		BigDecimal ciIndemDuty;//责任比例
		ciIndemDuty=compensateList.getClaimFeeCondition().getCiIndemDuty();
		Boolean isCiIndemDuty;//是否有责
		if (ciIndemDuty.compareTo(BigDecimal.ZERO) == 1) {
			isCiIndemDuty = true;//有责
		} else {
			isCiIndemDuty = false;//无责
		}
		Date damageTime;//出险日期
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(compensateList.getClaimFeeCondition().getRegistNo());
		damageTime = prpLRegistVo.getDamageTime();
		double unitAmount=0.0000;//限额
		double sumClaim=0.0000;//估损
		double rescueFee=0.0000;//施救费
		double estiPaid=0.0000;//估计赔款
		double sumRest=0.0000;//残值deductRate
		double deductRate=0.0000;//免赔率
		Long itemKindNo = compensateList.getPrpLCItemKindVo().getItemKindNo();//标的序号
		claimFeeExt=this.createClaimFeeExt();//添加车财损
		claimFeeExt.setItemKindNo(itemKindNo.intValue());
		claimFeeExt.setFeeTypeCode(CodeConstants.FeeTypeCode.PROPLOSS);
		//double unitAmountTempCar=this.getBzSumAmount(claimFeeExt.getFeeTypeCode(), ciIndemDuty, damageTime);
		double unitAmountTempCar=configService.calBzAmount(claimFeeExt.getFeeTypeCode(), isCiIndemDuty, prpLRegistVo.getRegistNo());
		unitAmount=unitAmountTempCar;
		claimFeeExt.setUnitAmount(new BigDecimal(unitAmount));
		fees.add(claimFeeExt);
		claimFeeExt=this.createClaimFeeExt();// 添加死亡伤残
		claimFeeExt.setFeeTypeCode(CodeConstants.FeeTypeCode.PERSONLOSS);
		claimFeeExt.setItemKindNo(itemKindNo.intValue());
		//double unitAmountTempPerson=this.getBzSumAmount(claimFeeExt.getFeeTypeCode(), ciIndemDuty, damageTime);
		double unitAmountTempPerson=configService.calBzAmount(claimFeeExt.getFeeTypeCode(), isCiIndemDuty, prpLRegistVo.getRegistNo());
		unitAmount=unitAmountTempPerson;
		claimFeeExt.setUnitAmount(new BigDecimal(unitAmount));
		fees.add(claimFeeExt);
		claimFeeExt = this.createClaimFeeExt();//添加医疗
		claimFeeExt.setItemKindNo(itemKindNo.intValue());
		claimFeeExt.setFeeTypeCode(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES);
		//double unitAmountTemp=this.getBzSumAmount(claimFeeExt.getFeeTypeCode(), ciIndemDuty, damageTime);
		double unitAmountTemp=configService.calBzAmount(claimFeeExt.getFeeTypeCode(), isCiIndemDuty, prpLRegistVo.getRegistNo());
		unitAmount=unitAmountTemp;
		claimFeeExt.setUnitAmount(new BigDecimal(unitAmount));
		fees.add(claimFeeExt);
		//理算一分钱误差处理 add by chenrong 2013-09-22 begin 比较总的赔付金额存在的误差
		Double thirdPartyLossSumRealPayFour=0.0000;//用每个理算大对象的实赔金额之和保留四位小数的进行相加
		Double thirdPartyLossSumRealPayTwo=0.00;//用每个理算大对象的实赔金额之和保留两位小数的进行相加
		Double allDiffer=0.00;//本次赔付总金额相差金额
		// 理算一分钱误差处理 add by chenrong 2013-09-22 end 比较总的赔付金额存在的误差
		
		PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(compensateList.getClaimFeeCondition().getRegistNo(), compensateList.getClaimFeeCondition().getPolicyNo());
		Map<String,Object> estipaidKindCode = new HashMap<String,Object>();
		if(null != prpLClaimVo){
			List<PrpLClaimKindVo> prpLClaimKindVoList = prpLClaimVo.getPrpLClaimKinds();
			if(null != prpLClaimKindVoList && prpLClaimKindVoList.size() >0){
				for(PrpLClaimKindVo prpLClaimKindVo : prpLClaimKindVoList){
					if(KINDCODE.KINDCODE_BZ.equals(prpLClaimKindVo.getKindCode())){
						//交强计算
						if(prpLClaimKindVo.getLossItemNo() != null && !"".equals(prpLClaimKindVo.getLossItemNo().trim())){
							estipaidKindCode.put(prpLClaimKindVo.getLossItemNo().trim(), prpLClaimKindVo);
						}else{
							logger.debug("刷新交强立案时，立案号："+prpLClaimVo.getClaimNo()+"，险别代码:"+prpLClaimKindVo.getKindCode()+"，PrpLclaimFee.FeeTypeCode（损失类型）为null或空字符串！");
						}
					
					}
				}
			}
		}
		List<PrpLCompensateVo> prpLcompensateList = null;
		if(null != prpLClaimVo && prpLClaimVo.getClaimNo()!=null){
			prpLcompensateList =  compensateService.findPrpLCompensateVoListByClaimNo(prpLClaimVo.getClaimNo().trim());//需要包含无保代赔计算书
		}
		if(compensateList.getCompensateType()==2) {
			if(compensateList!=null && compensateList.getReturnCompensateList()!=null &&compensateList.getReturnCompensateList().size()>0) {
				List<CompensateExp> compensateExpAry = compensateList.getReturnCompensateList();
				//for(ClaimFeeExt claimFeeExtTemp:fees) {	
				for(Iterator<ClaimFeeExt> iterator = fees.iterator(); iterator.hasNext();) {	
					ClaimFeeExt claimFeeExtTemp = iterator.next();
					for(CompensateExp compensateExp:compensateExpAry) {
						sumClaim=0.0000;//估损
						rescueFee=0.0000;//施救费
						estiPaid=0.0000;//估计赔款
						sumRest=0.0000;//残值deductRate
						deductRate=0.0000;//免赔率
						Double estiPaidTemp=0.000;//防止超限额后再对此理算对象进行总赔款一分钱处理
						if(compensateExp.getDamageType().trim().equals(claimFeeExtTemp.getFeeTypeCode())&& CodeConstants.FeeTypeCode.PROPLOSS.equals(claimFeeExtTemp.getFeeTypeCode())) {
							//车财损失
							if(compensateExp.getThirdPartyLossInfos()!=null&&compensateExp.getThirdPartyLossInfos().size()>0) {
								List<ThirdPartyLossInfo> thirdPartyLossInfos =compensateExp.getThirdPartyLossInfos();
								for (ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos) {
									logger.debug("交强车财计算返回："+thirdPartyLossInfo.getLicenseNo()+",损失名称："+thirdPartyLossInfo.getLossName()+",");
									logger.debug("  赔款金额------------"+thirdPartyLossInfo.getSumRealPay());
									 if (Double.isInfinite(thirdPartyLossInfo.getSumRealPay()) || Double.isNaN(thirdPartyLossInfo.getSumRealPay())){
										 logger.debug("thirdPartyLossInfo.getSumRealPay()赔款金额不是正常数据！");
										 thirdPartyLossInfo.setSumRealPay(0.00);
										 logger.debug("赔款金额兼容后thirdPartyLossInfo.getSumRealPay()"+thirdPartyLossInfo.getSumRealPay());
									 }
									if(thirdPartyLossInfo.getRescueFeeFlag()==null||thirdPartyLossInfo.getRescueFeeFlag().trim().equals("0")) {//估损金额需加上残值
										//一般赔案  人保无责代赔需要加上标的车的损失 
										/*if(thirdPartyLossInfo.getLossItemType()!=null && thirdPartyLossInfo.getSerialNo() == 1 ) {
											if(thirdPartyLossInfo.getInsteadFlag().equals("1")||thirdPartyLossInfo.getInsteadFlag().equals("2")){//无保或无责时
												sumClaim=sumClaim+Datas.round(thirdPartyLossInfo.getSumLoss(),4);
												sumClaim=sumClaim+Datas.round(thirdPartyLossInfo.getSumRest(),4);
												sumRest=sumRest+Datas.round(thirdPartyLossInfo.getSumRest(),4);
											}	
										} else {
											sumClaim=sumClaim+Datas.round(thirdPartyLossInfo.getSumLoss(),4);
											sumClaim=sumClaim+Datas.round(thirdPartyLossInfo.getSumRest(),4);
											sumRest=sumRest+Datas.round(thirdPartyLossInfo.getSumRest(),4);
										}*/
										if(thirdPartyLossInfo.getSerialNo() != 1){
											sumClaim=sumClaim+Datas.round(thirdPartyLossInfo.getSumLoss(),4);
											sumClaim=sumClaim+Datas.round(thirdPartyLossInfo.getSumRest(),4);
											//在计算时将施救费直接加入了估损金额中计算，在刷新立案估损的时候将其减去
											sumClaim=sumClaim-Datas.round(thirdPartyLossInfo.getRescueFee(),4);
											sumRest=sumRest+Datas.round(thirdPartyLossInfo.getSumRest(),4);
											rescueFee=rescueFee+Datas.round(thirdPartyLossInfo.getRescueFee(),4);
										}
									} else if(thirdPartyLossInfo.getRescueFeeFlag()!=null&&thirdPartyLossInfo.getRescueFeeFlag().trim().equals("1")) {//估损金额为施救费
										//一般赔案  人保无责代赔需要加上标的车的损失 
										/*if(thirdPartyLossInfo.getLossItemType()!=null && thirdPartyLossInfo.getSerialNo() == 1 ) {
											if(thirdPartyLossInfo.getInsteadFlag().equals("1")||thirdPartyLossInfo.getInsteadFlag().equals("2")){//无保或无责时
												rescueFee=rescueFee+Datas.round(thirdPartyLossInfo.getSumLoss(),4);
											}	
										} else {
											rescueFee=rescueFee+Datas.round(thirdPartyLossInfo.getSumLoss(),4);
										}*/
										if(thirdPartyLossInfo.getSerialNo() != 1){
											//RescueFeeFlag=1时表示该条损失数据只包含施救费，因此RescueFee=sumLoss
											rescueFee=rescueFee+Datas.round(thirdPartyLossInfo.getSumLoss(),4);
										}
									}
									if(thirdPartyLossInfo.getItemKindNo()!=null&&!thirdPartyLossInfo.getItemKindNo().toString().trim().equals("")) {
										itemKindNo=thirdPartyLossInfo.getItemKindNo().longValue();
									}
									estiPaid=estiPaid+Datas.round(thirdPartyLossInfo.getSumRealPay(),4);
									estiPaidTemp=estiPaidTemp+Datas.round(thirdPartyLossInfo.getSumRealPay(),6);
								}
							}
							estiPaidTemp=Datas.round(estiPaid,4);//防止超限额后再对此理算对象进行总赔款一分钱处理
							//add by yangweijun20140207,刷新立案加上管控，险别估损金额不能超过已经核赔通过的Begin
							if(prpLClaimVo != null && prpLClaimVo.getClaimNo()!=null){
								List<PrpLClaimKindVo> prpLclaimFees = new ArrayList<PrpLClaimKindVo>(0);
								PrpLClaimKindVo prpLClaimKindVo = new PrpLClaimKindVo();
								prpLClaimKindVo.setKindCode(claimFeeExtTemp.getKindCode());
								prpLClaimKindVo.setClaimLoss(new BigDecimal(Datas.round(estiPaid,2)));
								if(estipaidKindCode.containsKey(prpLClaimKindVo.getKindCode())){
									PrpLClaimKindVo feeOld = (PrpLClaimKindVo) estipaidKindCode.get(prpLClaimKindVo.getKindCode());
									claimFeeExtTemp.setEstiPaid(feeOld.getClaimLoss());//如果下面的金额校验不通过，则该险别估计赔款还是原来的，不改变
									thirdPartyLossSumRealPayFour=thirdPartyLossSumRealPayFour+estiPaid;
									thirdPartyLossSumRealPayTwo=thirdPartyLossSumRealPayTwo+Datas.round(estiPaid,2);
									if(claimFeeExtTemp.getEstiPaid().doubleValue()>estiPaidTemp) {//进行了五入操作
										claimFeeExtTemp.setRoundFlag("2");
									} else if (claimFeeExtTemp.getEstiPaid().doubleValue()>estiPaidTemp) {//进行了四舍操作
										claimFeeExtTemp.setRoundFlag("1");
									} else {
										claimFeeExtTemp.setRoundFlag("0");
									}
//									claimFeeExtTemp.setItemKindNo(prpLClaimKindVo.getItemKindNo);
									claimFeeExtTemp.setSumAmount(BigDecimal.valueOf(0));
									claimFeeExtTemp.setDeductRate(new BigDecimal(Datas.round(deductRate,2)));
									claimFeeExtTemp.setSumClaim(feeOld.getDefLoss());
									claimFeeExtTemp.setSumRest(feeOld.getRestFee());
									claimFeeExtTemp.setRescueFee(feeOld.getRescueFee());
									claimFeeExtTemp.setIndemnityDutyRate(BigDecimal.valueOf(0));
								}
								prpLclaimFees.add(prpLClaimKindVo);
//								Boolean isPass = claimService.checkModifiedFee(prpLclaimFees, prpLcompensateList, prpLClaimVo.getClaimNo(),true);
//								if(!isPass){//校验未通过
//									logger.debug("刷新交强立案时，050100中小险别"+claimFeeExtTemp.getFeeTypeCode()+"估计赔款少于已经赔付的金额,故不刷新此险别claimno="+compensateList.getClaimCond().getClaimNo());
//									break;
//								}
								if(estiPaid < 0) {
									estiPaid = 0;
								}
								claimFeeExtTemp.setEstiPaid(new BigDecimal(Datas.round(estiPaid,2)));
								prpLclaimFees.clear();
							}else{//如立案初始化，未产生立案号
								if(estiPaid < 0) {
									estiPaid = 0;
								}
								claimFeeExtTemp.setEstiPaid(new BigDecimal(Datas.round(estiPaid,2)));
							}
							//add by yangweijun20140207,刷新立案加上管控，险别估损金额不能超过已经核赔通过的END
							thirdPartyLossSumRealPayFour=thirdPartyLossSumRealPayFour+estiPaid;
							thirdPartyLossSumRealPayTwo=thirdPartyLossSumRealPayTwo+Datas.round(estiPaid,2);
							if(claimFeeExtTemp.getEstiPaid().doubleValue()>estiPaidTemp) {//进行了五入操作
								claimFeeExtTemp.setRoundFlag("2");
							} else if (claimFeeExtTemp.getEstiPaid().doubleValue()>estiPaidTemp) {//进行了四舍操作
								claimFeeExtTemp.setRoundFlag("1");
							} else {
								claimFeeExtTemp.setRoundFlag("0");
							}
//							claimFeeExtTemp.setItemKindNo(itemKindNo);
							claimFeeExtTemp.setSumAmount(BigDecimal.valueOf(0));
							claimFeeExtTemp.setDeductRate(new BigDecimal(Datas.round(deductRate,2)));
							claimFeeExtTemp.setSumClaim(new BigDecimal(Datas.round(sumClaim,2)));
							claimFeeExtTemp.setSumRest(new BigDecimal(Datas.round(sumRest,2)));
							claimFeeExtTemp.setRescueFee(new BigDecimal(Datas.round(rescueFee,2)));
							claimFeeExtTemp.setIndemnityDutyRate(BigDecimal.valueOf(0));
							logger.debug("立案估计赔款估损信息(52)：");
							logger.debug(claimFeeExtTemp.getFeeTypeCode()+",限额:"+claimFeeExtTemp.getUnitAmount()+",估损:"+claimFeeExtTemp.getSumClaim()+",施救费:"+
									claimFeeExtTemp.getRescueFee()+",残值:"+claimFeeExtTemp.getSumRest()+",估计赔款:"+claimFeeExtTemp.getEstiPaid()+",标的序号:"+claimFeeExtTemp.getItemKindNo());
						
						} else if(compensateExp.getDamageType().trim().equals(claimFeeExtTemp.getFeeTypeCode())) {// 医药，死亡伤残
							if(compensateExp.getThirdPartyLossInfos()!=null&&compensateExp.getThirdPartyLossInfos().size()>0) {
								List<ThirdPartyLossInfo> thirdPartyLossInfos =compensateExp.getThirdPartyLossInfos();
								for (ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfos) {
									logger.debug("交强医疗/死亡计算返回（"+compensateExp.getDamageType().trim()+"）："+thirdPartyLossInfo.getLicenseNo()+",损失名称："+thirdPartyLossInfo.getLossName()+",");
									logger.debug("   赔款金额------------"+Datas.round(thirdPartyLossInfo.getSumRealPay(),4));
									//一般赔案
									if(thirdPartyLossInfo.getLossItemType()!=null && thirdPartyLossInfo.getSerialNo() == 1) {
										if(thirdPartyLossInfo.getInsteadFlag().equals("2")){//无保时
											sumClaim=sumClaim+Datas.round(thirdPartyLossInfo.getSumLoss(),4);
										}	
									} else {
										sumClaim=sumClaim+Datas.round(thirdPartyLossInfo.getSumLoss(),4);
									}
									if(thirdPartyLossInfo.getItemKindNo()!=null&&!thirdPartyLossInfo.getItemKindNo().toString().trim().equals("")) {
										itemKindNo=thirdPartyLossInfo.getItemKindNo().longValue();
									}
									estiPaid=estiPaid+Datas.round(thirdPartyLossInfo.getSumRealPay(),4);
									estiPaidTemp=estiPaidTemp+Datas.round(thirdPartyLossInfo.getSumRealPay(),6);
								}
							}
							estiPaidTemp=Datas.round(estiPaid,4);//防止超限额后再对此理算对象进行一分钱处理
							//add by yangweijun20140207,刷新立案加上管控，险别估损金额不能超过已经核赔通过的Begin
							if(prpLClaimVo != null && prpLClaimVo.getClaimNo()!=null){
								List<PrpLClaimKindVo> prpLClaimKindVoList = new ArrayList<PrpLClaimKindVo>(0);
								PrpLClaimKindVo prpLClaimKindVo = new PrpLClaimKindVo();
								prpLClaimKindVo.setKindCode(claimFeeExtTemp.getFeeTypeCode());
//								prpLClaimKindVo.setFeeTypeCode(claimFeeExtTemp.getFeeTypeCode());
								prpLClaimKindVo.setClaimLoss(new BigDecimal(Datas.round(estiPaid,2)));
								if(estipaidKindCode.containsKey(prpLClaimKindVo.getKindCode())){
									PrpLClaimKindVo feeOld = (PrpLClaimKindVo) estipaidKindCode.get(prpLClaimKindVo.getKindCode());
									claimFeeExtTemp.setEstiPaid(feeOld.getClaimLoss());//如果下面的金额校验不通过，则该险别估计赔款还是原来的，不改变
									thirdPartyLossSumRealPayTwo=thirdPartyLossSumRealPayTwo+Datas.round(estiPaid,2);
									if(claimFeeExtTemp.getEstiPaid().doubleValue()>estiPaidTemp) {//进行了五入操作
										claimFeeExtTemp.setRoundFlag("2");
									} else if (claimFeeExtTemp.getEstiPaid().doubleValue()>estiPaidTemp) {//进行了四舍操作
										claimFeeExtTemp.setRoundFlag("1");
									} else {
										claimFeeExtTemp.setRoundFlag("0");
									}
//									claimFeeExtTemp.setItemKindNo(feeOld.getItemKindNo());
									claimFeeExtTemp.setSumAmount(BigDecimal.valueOf(0));
									claimFeeExtTemp.setDeductRate(new BigDecimal(Datas.round(deductRate,2)));
									claimFeeExtTemp.setSumClaim(feeOld.getDefLoss());
									claimFeeExtTemp.setSumRest(feeOld.getRestFee());
									claimFeeExtTemp.setRescueFee(feeOld.getRescueFee());
									claimFeeExtTemp.setIndemnityDutyRate(BigDecimal.valueOf(0));
								}
								prpLClaimKindVoList.add(prpLClaimKindVo);
//								Boolean isPass = claimService.checkModifiedFee(prpLClaimKindVoList, prpLcompensateList, prpLClaimVo.getClaimNo(),true);
//								if(!isPass){//校验未通过
//									logger.debug("刷新交强立案时，050100中小险别"+claimFeeExtTemp.getFeeTypeCode()+"估计赔款少于已经赔付的金额,故不刷新此险别claimno="+compensateList.getClaimCond().getClaimNo());
//									break;
//								}
								if(estiPaid < 0) {
									estiPaid = 0;
								}
								claimFeeExtTemp.setEstiPaid(new BigDecimal(Datas.round(estiPaid,2)));
								prpLClaimKindVoList.clear();
							}else{//如立案初始化，未产生立案号
								if(estiPaid < 0) {
									estiPaid = 0;
								}
								claimFeeExtTemp.setEstiPaid(new BigDecimal(Datas.round(estiPaid,2)));
							}
							//add by yangweijun20140207,刷新立案加上管控，险别估损金额不能超过已经核赔通过的END
							thirdPartyLossSumRealPayFour=thirdPartyLossSumRealPayFour+estiPaid;
							thirdPartyLossSumRealPayTwo=thirdPartyLossSumRealPayTwo+Datas.round(estiPaid,2);
							if(claimFeeExtTemp.getEstiPaid().doubleValue()>estiPaidTemp) {//进行了五入操作
								claimFeeExtTemp.setRoundFlag("2");
							} else if (claimFeeExtTemp.getEstiPaid().doubleValue()>estiPaidTemp) {//进行了四舍操作
								claimFeeExtTemp.setRoundFlag("1");
							} else {
								claimFeeExtTemp.setRoundFlag("0");
							}
//							claimFeeExtTemp.setItemKindNo(itemKindNo);
							claimFeeExtTemp.setSumAmount(BigDecimal.valueOf(0));
							claimFeeExtTemp.setDeductRate(new BigDecimal(Datas.round(deductRate,2)));
							claimFeeExtTemp.setSumClaim(new BigDecimal(Datas.round(sumClaim,2)));
							claimFeeExtTemp.setSumRest(new BigDecimal(Datas.round(sumRest,2)));
							claimFeeExtTemp.setRescueFee(new BigDecimal(Datas.round(rescueFee,2)));
							claimFeeExtTemp.setIndemnityDutyRate(BigDecimal.valueOf(0));
							logger.debug("立案估计赔款估损信息(49/51)：");
							logger.debug(claimFeeExtTemp.getFeeTypeCode()+",限额:"+claimFeeExtTemp.getUnitAmount()+",估损:"+claimFeeExtTemp.getSumClaim()+",施救费:"+
									claimFeeExtTemp.getRescueFee()+",残值:"+claimFeeExtTemp.getSumRest()+",估计赔款:"+claimFeeExtTemp.getEstiPaid()+",标的序号:"+claimFeeExtTemp.getItemKindNo());
						} 
					}
				}
			}
		}
		allDiffer=thirdPartyLossSumRealPayFour-thirdPartyLossSumRealPayTwo;
        allDiffer=Datas.round(allDiffer,2);
    	if (allDiffer != 0) {//总金额调整一分钱时，按52 49 51顺序
    		for(ClaimFeeExt claimFeeExtTemp:fees) {
				if(allDiffer>0 && claimFeeExtTemp.getRoundFlag() != null && claimFeeExtTemp.getRoundFlag().trim().equals("1")) {//舍得多，将进行四舍部分加一
					claimFeeExtTemp.setEstiPaid(new BigDecimal(claimFeeExtTemp.getEstiPaid().doubleValue()+0.01));
					claimFeeExtTemp.setRoundFlag("0");
					allDiffer=allDiffer-0.01;
					logger.debug("进行一分钱调整加1分："+claimFeeExtTemp.getFeeTypeCode());
				} else if(allDiffer<0 && claimFeeExtTemp.getRoundFlag() != null && claimFeeExtTemp.getRoundFlag().trim().equals("2")) {//进得多，将进行五入部分减一
					claimFeeExtTemp.setEstiPaid(new BigDecimal(claimFeeExtTemp.getEstiPaid().doubleValue()-0.01));
					claimFeeExtTemp.setRoundFlag("0");
					allDiffer=allDiffer+0.01;
					logger.debug("进行一分钱调整减1分："+claimFeeExtTemp.getFeeTypeCode());
				}
				if (allDiffer == 0) {
					break;
				}
    		}
    	}
    	//按52（车财） 51（死亡） 49（医疗）的顺序展示
    	java.util.Collections.sort(fees, new java.util.Comparator<ClaimFeeExt>(){
			public int compare(ClaimFeeExt o1, ClaimFeeExt o2) {
				if(Integer.parseInt(o1.getFeeTypeCode()) < Integer.parseInt(o2.getFeeTypeCode())){
					return 1;
				}else if(Integer.parseInt(o1.getFeeTypeCode()) == Integer.parseInt(o1.getFeeTypeCode())){
					return 0;
				}else{							
					return -1;
				}
			}});
		return fees;
	}
//	 根据交强险有责无责和出险日期获取估损(废弃，统一使用ConfigService.calBzAmount)
	private double getBzSumAmount(String damageType, BigDecimal ciIndemDuty, Date damageDate) {
		String key = "";
		if (damageDate == null) {
			throw new BusinessException("该立案的的出险日期DamageStartDate,或报案的出险日期DamageDate字段为空,请修改报案和立案信息,或联系数据库管理员！", false);
		}
		if (CodeConstants.DAMAGE_DATE.before(damageDate)) {
			key += "N";
		}
		if (ciIndemDuty.compareTo(new BigDecimal("0.00")) == 1) {
			key += "1";
		} else {
			key += "0";
		}
		return CodeConstants.BZ_LIMIT_AMOUNT.get(key + damageType).doubleValue();
	}
	//交强险创建ClaimFeeExt
	private ClaimFeeExt createClaimFeeExt() {
		ClaimFeeExt claimFeeExt = new ClaimFeeExt();
		claimFeeExt.setEstiPaid(new BigDecimal("0.00"));
		claimFeeExt.setKindCode(KINDCODE.KINDCODE_BZ);
		claimFeeExt.setRiskCode("1101");
		claimFeeExt.setSumAmount(new BigDecimal("0.00"));
		claimFeeExt.setDeductRate(new BigDecimal("0.00"));
		claimFeeExt.setSumClaim(new BigDecimal("0.00"));
		claimFeeExt.setSumRest(new BigDecimal("0.00"));
		claimFeeExt.setRescueFee(new BigDecimal("0.00"));
		claimFeeExt.setIndemnityDutyRate(new BigDecimal("0.00"));
		claimFeeExt.setUnitAmount(new BigDecimal("0.00"));
		claimFeeExt.setRoundFlag("0");//判断四舍五入标志
		return claimFeeExt;
	}
	/**
	 * 赔款理算完成后,刷新立案险别估计赔款(商业)
	 * 
	 * @author xiachengcheng
	 * 
	 */
	public List<ClaimFeeExt> refreshClaimEstiPaidBI(CompensateListVo compensateList){
		List<ClaimFeeExt> fees = new ArrayList<ClaimFeeExt>(0);
		String registNo = compensateList.getClaimFeeCondition().getRegistNo();
		String policyNo = compensateList.getClaimFeeCondition().getPolicyNo();
		PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(registNo,policyNo);
		List<PrpLCItemKindVo> prpLCItemKindVoList = prpLCMainVo.getPrpCItemKinds();
		
		PrpLClaimVo oldClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo,policyNo);
		Map<String,BigDecimal> estipaidKindCode = new HashMap<String,BigDecimal>();
		if(oldClaimVo != null){
			List<PrpLClaimKindVo> prpLClaimKindVoList = oldClaimVo.getPrpLClaimKinds();
			if(prpLClaimKindVoList != null && !prpLClaimKindVoList.isEmpty()){
				for(PrpLClaimKindVo prpLClaimKindVo : prpLClaimKindVoList){
					estipaidKindCode.put(prpLClaimKindVo.getKindCode(), prpLClaimKindVo.getClaimLoss());
				}
			}
		}
		List<PrpLCompensateVo> prpLcompensateList = null;
		if(oldClaimVo != null && oldClaimVo.getClaimNo() != null) {
			prpLcompensateList = compensateService.findCompListByClaimNo(oldClaimVo.getClaimNo(),"N");
		}
		String riskCode = "";
		Set<String> kindCodeSet = new HashSet<String>(0);
		if (prpLCItemKindVoList != null && prpLCItemKindVoList.size() > 0) {
			for (PrpLCItemKindVo prpLCItemKindVo : prpLCItemKindVoList) {
				if( !KINDCODE.KINDCODE_M.equals(prpLCItemKindVo.getKindCode())){// 非M险才配置有事故免赔率
					riskCode = prpLCItemKindVo.getRiskCode();
					kindCodeSet.add(prpLCItemKindVo.getKindCode());
				}
			}
		}

		Map<String, Double> dutyDeductRateMap = new HashMap<String,Double>();//.getDeductRate(
			//riskCode, kindCodeSet, compensateList.getClaimFeeCondition().getIndemnityDuty(), compensateList.getClaimFeeCondition().getRegistNo());// 免赔率

		if (prpLCItemKindVoList != null && prpLCItemKindVoList.size() > 0) {
			for (PrpLCItemKindVo prpLCItemKindVo : prpLCItemKindVoList) {
				String plyItemKindCode = prpLCItemKindVo.getKindCode();
				if(plyItemKindCode!=null&& !"".equals(plyItemKindCode)){// 险别代码
					if (compensateList.getCompensateType() == 1) {// 商业
						ClaimFeeExt claimFeeExt = new ClaimFeeExt();
						BigDecimal unitAmount = new BigDecimal("0.00");// 限额
						BigDecimal sumClaim = new BigDecimal("0.00");// 估损
						BigDecimal rescueFee = new BigDecimal("0.00");// 施救费
						BigDecimal estiPaid = new BigDecimal("0.00");// 估计赔款
						BigDecimal personSumClaim = new BigDecimal("0.00");// 估损费-人
						BigDecimal personEstiPaid = new BigDecimal("0.00");// 估计赔款-人
						BigDecimal sumRest = new BigDecimal("0.00");// 残值deductRate
						// BigDecimal deductRate = new BigDecimal("0.0");// 免赔率
						BigDecimal deductLoss = new BigDecimal("0.0");// 不计免赔损失(050900)
						BigDecimal exceptDeductible = new BigDecimal("0.0");// 不计免赔金额(050900)
//						Integer itemKindNo = prpLCItemKindVo.getItemKindNo();// 标的序号

						if(KINDCODE.KINDCODE_M.equals(plyItemKindCode)){
							claimFeeExt.setmKindCode(true);
							// liuping 2016-6-16 不计免赔不保存到prplclaimkind。不计免赔金额保存在prplclaimkind.Deductible
							continue;
						}

						if (compensateList != null && compensateList.getReturnCompensateList() != null && compensateList.getReturnCompensateList().size() > 0) {
							List<CompensateExp> compensateExpAry = new ArrayList<CompensateExp>(0);
							compensateExpAry = compensateList.getReturnCompensateList();
							
							if (compensateExpAry != null && compensateExpAry.size() > 0) {
								for (CompensateExp compensateExp : compensateExpAry) {
									String expKindCode = compensateExp.getKindCode();

									if(expKindCode!=null&&plyItemKindCode.trim().equals(expKindCode.trim())){// 险别匹配时才组织展示
										if(KINDCODE.KINDCODE_B.equals(expKindCode.trim())){
											//统计B险人伤的估损金额PersonSumClaim和估计赔款PersonEstiPaid
											if (compensateExp.getExpType() != null && compensateExp.getExpType().trim().equals(CodeConstants.ExpType.PERSON)) {
												if (!Double.isInfinite(compensateExp.getSumLoss()) && !Double.isNaN(compensateExp.getSumLoss())
														&&compensateExp.getMsumFlag()==0) {
													//当前compExp为不计免赔部分时(MsumFlag==1)，sumloss不累加到定损，只累加赔款部分
													/*if(!Double.isInfinite(compensateExp.getRescueFeeBZPaid()) && !Double.isNaN(compensateExp.getRescueFeeBZPaid())){
														claimFeeExt.setPersonSumClaim(personSumClaim.add(new BigDecimal(compensateExp.getSumLoss() - compensateExp.getSumLossBZPaid())));
													} else {
														claimFeeExt.setPersonSumClaim(personSumClaim.add(new BigDecimal(compensateExp.getSumLoss())));
													}*/
													personSumClaim = personSumClaim.add(new BigDecimal(compensateExp.getSumLoss()));
													
												}
												if (!Double.isInfinite(compensateExp.getSumRealPay()) && !Double.isNaN(compensateExp.getSumRealPay())) {
													personEstiPaid = personEstiPaid.add(new BigDecimal(compensateExp.getSumRealPay()));
													//claimFeeExt.setPersonEstiPaid(personSumClaim.add(new BigDecimal(compensateExp.getSumRealPay())));
												} 
											}
										} /*else {
											personSumClaim = BigDecimal.ZERO;
											personEstiPaid = BigDecimal.ZERO;
											claimFeeExt.setPersonSumClaim(BigDecimal.ZERO);
											claimFeeExt.setPersonEstiPaid(BigDecimal.ZERO);
										}*/
										if(KINDCODE.KINDCODE_B.equals(compensateExp.getClaimType())
												&&KINDCODE.KINDCODE_A.equals(expKindCode)
											    && "1".equals(compensateExp.getRecoveryOrPayFlag()) 
											    && ("1".equals(compensateExp.getOppoentCoverageType()) 
											    		|| "2".equals(compensateExp.getOppoentCoverageType()))){//代商业 或 代交强
											
										} else {
											if(!Double.isInfinite(compensateExp.getSumRest()) && !Double.isNaN(compensateExp.getSumRest())) {
												sumRest = sumRest.add(new BigDecimal(compensateExp.getSumRest()));// 残值
											}
											if(!Double.isInfinite(compensateExp.getSumLoss()) && !Double.isNaN(compensateExp.getSumLoss())&&compensateExp.getMsumFlag()!=1) {
												//compensateExp.getMsumFlag()!=1 表示： 2016-6-16  鼎和 不计免赔的金额不计入估损金额
												sumClaim = sumClaim.add(new BigDecimal(compensateExp.getSumLoss()));// 估损金额+
												// 修理期间费用补偿险也直接取估损，不再进行特殊计算和处理 0922
												/*if(KINDCODE.KINDCODE_RF.equals(expKindCode)){// 修理期间费用补偿险
													if(compensateExp.getLossQuantity() > 0) {
														sumClaim = prpLCItemKindVo.getUnitAmount().multiply(new BigDecimal(compensateExp.getLossQuantity())) ;
													}
												}*/
											}
											if(compensateExp.getMsumFlag()==1){// 2016-6-16 不计免赔列
												deductLoss = deductLoss.add(new BigDecimal(compensateExp.getSumLoss()));
											}
											if(!Double.isInfinite(compensateExp.getRescueFee()) && !Double.isNaN(compensateExp.getRescueFee())) {
												rescueFee = rescueFee.add(new BigDecimal(compensateExp.getRescueFee()));// 施救费
											}
										}
										if(Double.isInfinite(compensateExp.getSumRealPay()) || Double.isNaN(compensateExp.getSumRealPay())) {
											logger.debug("compensateExp.getSumRealPay()不是正常数据");
											logger.debug("prpLcItemKind.getKindCode()-"+plyItemKindCode+",compensateExp.getKindCode()-"+expKindCode+","+compensateExp
													.getSumRealPay());
											compensateExp.setSumRealPay(0.00);
										}
										estiPaid = estiPaid.add(new BigDecimal(compensateExp.getSumRealPay()));// 估计赔款
										exceptDeductible = exceptDeductible.add(new BigDecimal(compensateExp.getExceptDeductible()));
										// liuping 2016-6-16 人保的估损金额要减去交强赔付部分，鼎和不需要减，直接取估损金额
										// if(!KINDCODE.KINDCODE_B.equals(compensateExp.getClaimType())
										// || (KINDCODE.KINDCODE_B.equals(compensateExp.getClaimType()) && !KINDCODE.KINDCODE_A.equals(expKindCode))){
										// if(!Double.isInfinite(compensateExp.getRescueFeeBZPaid()) && !Double.isNaN(compensateExp.getRescueFeeBZPaid())) {
										// sumClaim = sumClaim.subtract(new BigDecimal(compensateExp.getSumLossBZPaid()));//估损金额-交强赔付部分
										// }
										// if(!Double.isInfinite(compensateExp.getRescueFeeBZPaid()) && !Double.isNaN(compensateExp.getRescueFeeBZPaid())) {
										// rescueFee = rescueFee.subtract(new BigDecimal(compensateExp.getRescueFeeBZPaid()));//施救费-交强赔付部分
										// }
										// }
										logger.debug("prpLcItemKind.getKindCode()-"+plyItemKindCode+",compensateExp.getKindCode()-"+expKindCode+","+compensateExp
												.getSumRealPay());
										//break;
									}
								}
							}
						}
//						deductRate = BigDecimal.valueOf(claimService.getDeductRate(prpLcItemKind.getRiskCode(), prpLcItemKind.getKindCode(), compensateList.getClaimCond().getIndemnityDuty(), "Y", prpLcItemKind.getRegistNo()));// 免赔率
						unitAmount = prpLCItemKindVo.getAmount();// 限额
						BigDecimal unitAmountTemp = unitAmount;
						if(policyViewService.isNewClauseCode(prpLCMainVo.getRegistNo())){//新条款对发动机涉水险、玻璃单独破碎险、无法找到第三方进行限额调整为车损险的
							List<PrpLCItemKindVo> prpLcItemKinds = prpLCMainVo.getPrpCItemKinds();
							double amountForA = 0.00;
							double amountForX = 0.00;
							for (PrpLCItemKindVo prpLcItemKind : prpLcItemKinds) {
								if(KINDCODE.KINDCODE_A.equals(prpLcItemKind.getKindCode().trim())){
									amountForA = prpLcItemKind.getAmount().doubleValue();
									break;
								}else if(KINDCODE.KINDCODE_X.equals(prpLcItemKind.getKindCode().trim())){
									amountForX = prpLcItemKind.getAmount().doubleValue();
									break;
								}
							}
							if(KINDCODE.KINDCODE_X1.equals(plyItemKindCode.trim())||KINDCODE.KINDCODE_F.equals(prpLCItemKindVo
									.getKindCode().trim())){
								unitAmountTemp = new BigDecimal(amountForA);
							}else if(KINDCODE.KINDCODE_NT.equals(plyItemKindCode.trim())){// 无法找到第三方进行限额调整为车损险与新增设备限额之和的30%
								unitAmountTemp = new BigDecimal((amountForA*2+amountForX)*0.3);
							}
						}
						if(( KINDCODE.KINDCODE_A.equals(plyItemKindCode.trim())||KINDCODE.KINDCODE_Z.equals(prpLCItemKindVo
								.getKindCode().trim()) )
								&& estiPaid.doubleValue() > 2*unitAmountTemp.doubleValue()) {
							estiPaid = unitAmountTemp.multiply(new BigDecimal("2"));
						}else if( !KINDCODE.KINDCODE_A.equals(plyItemKindCode.trim())&& !KINDCODE.KINDCODE_Z.equals(prpLCItemKindVo
								.getKindCode().trim())
								&& unitAmountTemp.doubleValue() > 0.00
								&& estiPaid.doubleValue() > unitAmountTemp.doubleValue()) {
							estiPaid = unitAmountTemp;
						}
						
						claimFeeExt.setPersonSumClaim(personSumClaim);//B险人伤估损金额
						claimFeeExt.setPersonEstiPaid(personEstiPaid);//B险人伤估计赔款
						
						claimFeeExt.setKindCode(plyItemKindCode);
//						claimFeeExt.setItemKindNo(itemKindNo);
						claimFeeExt.setRiskCode(prpLCItemKindVo.getRiskCode());
						claimFeeExt.setSumRest(sumRest);// 残值
						claimFeeExt.setSumAmount(BigDecimal.valueOf(0));// 未确定
						claimFeeExt.setUnitAmount(unitAmount);
						if(dutyDeductRateMap.get(plyItemKindCode)!=null){
							claimFeeExt.setDeductRate(BigDecimal.valueOf(dutyDeductRateMap.get(plyItemKindCode)));
						} else {
							claimFeeExt.setDeductRate(BigDecimal.valueOf(0));
						}
						claimFeeExt.setDeductLoss(deductLoss);
						claimFeeExt.setSumClaim(sumClaim);
						claimFeeExt.setRescueFee(rescueFee);// 施救费
						if(KINDCODE.KINDCODE_RF.equals(plyItemKindCode)){// 修理期间费用补偿险
							claimFeeExt.setUnitAmount(prpLCItemKindVo.getUnitAmount().multiply(prpLCItemKindVo.getQuantity()));
						}
						claimFeeExt.setIndemnityDutyRate(compensateList.getClaimFeeCondition().getIndemnityDutyRate());//事故责任比例
						// claimFeeExt.setFeeTypeCode(compensateExp.getFeeTypeCode());//损失类型代码
						if(oldClaimVo != null && oldClaimVo.getClaimNo()!=null){
							List<PrpLClaimKindVo> prpLClaimKindVoList = new ArrayList<PrpLClaimKindVo>(0);
							PrpLClaimKindVo prpLClaimKindVo = new PrpLClaimKindVo();
							prpLClaimKindVo.setKindCode(claimFeeExt.getKindCode());
							prpLClaimKindVo.setClaimLoss(estiPaid);
							prpLClaimKindVoList.add(prpLClaimKindVo);
							if(estipaidKindCode.containsKey(prpLClaimKindVo.getKindCode())){
								claimFeeExt.setEstiPaid(estipaidKindCode.get(prpLClaimKindVo.getKindCode()));//如果下面的金额校验不通过，则该险别估计赔款还是原来的，不改变
							}
//							Boolean isPass = claimService.checkModifiedFee(prpLClaimKindVoList, prpLcompensateList, prpLClaimVo.getClaimNo(),true);
//							if(!isPass){
//								logger.debug("刷新立案时，险别"+fee.getKindCode()+"估计赔款少于已经赔付的金额，故不刷新此险别claimno="+compensateList.getClaimCond().getClaimNo());
//							}
							if(estiPaid.doubleValue() < 0) {
								estiPaid = new BigDecimal("0.00");
							}
							claimFeeExt.setEstiPaid(estiPaid);
							if(prpLcompensateList != null){
								prpLcompensateList.clear();
							}
						}else{//如立案初始化，未产生立案号
							if(estiPaid.doubleValue() < 0) {
								estiPaid = new BigDecimal("0.00");
							}
							claimFeeExt.setEstiPaid(estiPaid);
						}
						if(claimFeeExt.getSumClaim().compareTo(claimFeeExt.getPersonSumClaim())==0
								&&claimFeeExt.getEstiPaid().compareTo(claimFeeExt.getPersonEstiPaid())==0
								&&claimFeeExt.getPersonSumClaim().compareTo(BigDecimal.ZERO)==1){
							// 设置只包含人伤claimFeeExt的feetypeCode为pers，用于给claimKind的LossitemName赋值时做判断
							claimFeeExt.setFeeTypeCode("pers");
							logger.debug("B险只包含人伤设置FeeTypeCode为Pers");
						}
						logger.debug("立案估计赔款估损信息(商业)：KindCode="+plyItemKindCode);
						logger.debug( "限额:"+ claimFeeExt.getUnitAmount() +
								",估损:" + claimFeeExt.getSumClaim() + 
								",施救费:" + claimFeeExt.getRescueFee() +
								",残值:" + claimFeeExt.getSumRest() + 
								",估计赔款:" + claimFeeExt.getEstiPaid());
						fees.add(claimFeeExt);
					}
				}
			}
		}
		return fees;
	}

}
