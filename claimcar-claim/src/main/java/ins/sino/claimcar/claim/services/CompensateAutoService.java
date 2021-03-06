package ins.sino.claimcar.claim.services;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.platform.utils.DataUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.CompensateType;
import ins.sino.claimcar.CodeConstants.KINDCODE;
import ins.sino.claimcar.CodeConstants.PayFlagType;
import ins.sino.claimcar.CodeConstants.YN01;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.service.CompensateHandleService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.CompensateActionVo;
import ins.sino.claimcar.claim.vo.CompensateListVo;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateExtVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.ThirdPartyLossInfo;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <pre></pre>
 * @author ???WeiLanlei
 */
/**
 * ?????????????????????
 * <pre></pre>
 * @author ???WeiLanlei
 */
@Service("compensateAutoService")
public class CompensateAutoService {
	private static final Log logger = LogFactory.getLog(CompensateAutoService.class);
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private CompensateHandleService compHandleService;
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private PropTaskService propTaskService; 
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired 
	private PayCustomService payCustomService;
	@Autowired
	private CompensateService compensateService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private AssignService assignService;
	@Autowired
	private RegistQueryService registQueryService;
	
	//?????????????????????????????????????????????????????????????????????????????????????????????????????????

	public PrpLCompensateVo autoCompensate(Double flowTaskId,SysUserVo user) throws Exception{
		PrpLCompensateVo compVo = null;
		user.setUserCode("AUTO");
		user.setUserName("AUTO");
		//???????????????compVo???????????????????????????????????????????????????
		CompensateActionVo actionVo = compHandleService.compensateEdit(flowTaskId,user); 
		compVo = actionVo.getCompensateVo();//TODO ??????????????????????????????????????????
		List<PrpLLossPropVo> props = new ArrayList<PrpLLossPropVo>();
		if(actionVo.getLossPropList()!=null){
			props.addAll(actionVo.getLossPropList());
		}
		if(actionVo.getOtherLossProps()!=null){
			props.addAll(actionVo.getOtherLossProps());
		}
		compVo.setPrpLLossItems(actionVo.getLossItemVoList());
		compVo.setPrpLLossProps(props);
		compVo.setPrpLLossPersons(actionVo.getLossPersonList());
		compVo.setPrpLCompensateExt(new PrpLCompensateExtVo());
		compVo.setRecoveryFlag(CodeConstants.ValidFlag.INVALID);
		if (CodeConstants.ValidFlag.VALID.equals(actionVo.getDwPersFlag())
				||CodeConstants.ValidFlag.VALID.equals(actionVo.getDwFlag())) {
			compVo.setRecoveryFlag(CodeConstants.ValidFlag.VALID);
		}
		//????????????????????????????????????-???????????????????????????????????????????????????
		if("1101".equals(compVo.getRiskCode())){
			//?????????????????????????????????
			Map<String, Object> resultMap = this.calculateCIAuto(compVo);
			//??????????????????comp??????
			this.processCalculateCIData(resultMap,compVo);
		}else{
			//????????????????????????
			// ??????????????????
			List<PrpLClaimDeductVo> claimDeductVoList = compensateTaskService.findDeductCond(compVo.getRegistNo(), compVo.getRiskCode());
			//????????????????????????
			CompensateVo compensateVo = compHandleService.compensateGetAllRate(compVo,claimDeductVoList,
										compVo.getPrpLLossItems(),compVo.getPrpLLossProps(),compVo.getPrpLLossPersons());
			this.processGetRateBIData(compVo,compensateVo,claimDeductVoList);
			//?????????????????????
			Map<String, Object> resultMap = this.bIGetBZAmt(compVo);
			this.processBIGetBZAmtData(resultMap,compVo);
			//??????????????????????????????
			Map<String, Object> resultMapBI = this.calculateBIAuto(compVo,claimDeductVoList);
			this.processCalculateBIData(compVo,resultMapBI,actionVo.getDwPersFlag());
		}
		//????????????????????????
		this.feeProcess(compVo);
		//????????????????????????comp??????
		this.sumAllAmt(compVo);
		//???????????????????????????payment???????????????
		this.choosePayCustom(compVo);
		//????????????
		String compensateNo = compensateService.saveCompensates(compVo,compVo.getPrpLLossItems(),compVo.getPrpLLossProps(),
											compVo.getPrpLLossPersons(),compVo.getPrpLCharges(),compVo.getPrpLPayments(),user);
		//???????????????????????????????????????????????????????????????
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		if(!StringUtils.isBlank(compensateNo)){
			compHandleService.syncInsuredPhone(compVo.getRegistNo(),compVo.getinsuredPhone());
			if(StringUtils.isBlank(wfTaskVo.getCompensateNo())){
				wfTaskHandleService.tempSaveTaskByComp(wfTaskVo.getTaskId().doubleValue(),compVo.getCompensateNo(),user.getUserCode(),user.getComCode());
			}
		}else{
			throw new IllegalArgumentException("???????????????????????????<br/>");
		}
		
		return compVo;
	}
	
	//????????????
	public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {  
	    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();  
	    ObjectOutputStream out = new ObjectOutputStream(byteOut);  
	    out.writeObject(src);  

	    ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());  
	    ObjectInputStream in = new ObjectInputStream(byteIn);  
	    @SuppressWarnings("unchecked")  
	    List<T> dest = (List<T>) in.readObject();  
	    return dest;  
	}  
	
	/**
	 * ??????????????????
	 * <pre></pre>
	 * @param prpLCompensate
	 * @return
	 * @modified:
	 * ???WLL(2018???1???30??? ??????10:40:37): <br>
	 */
	public Map<String, Object> calculateCIAuto(PrpLCompensateVo prpLCompensate){

		int noDutyNum = 0;//?????????????????????
		List<PrpLLossItemVo> prpLLossItemVoListForCal = prpLCompensate.getPrpLLossItems();//???????????????????????????list???????????????List
		List<PrpLLossItemVo> prpLLossItemVoListOri = new ArrayList<PrpLLossItemVo>();
		for(PrpLLossItemVo vo : prpLCompensate.getPrpLLossItems()){
			PrpLLossItemVo newVo = new PrpLLossItemVo();
			Beans.copy().from(vo).to(newVo);
			prpLLossItemVoListOri.add(newVo);
		}
		List<PrpLLossPropVo> prpLLossPropVoList = prpLCompensate.getPrpLLossProps();
		List<PrpLLossPersonVo> prpLLossPersonVoList = prpLCompensate.getPrpLLossPersons();
		List<PrpLLossItemVo> noDutyLossItemVoList = new ArrayList<PrpLLossItemVo>();//??????????????????List
		Iterator<PrpLLossItemVo> iterator = prpLLossItemVoListForCal.iterator();
		while (iterator.hasNext()) {
			PrpLLossItemVo lossItemVo = iterator.next();
			if ("4".equals(lossItemVo.getPayFlag())) {
				noDutyNum++;// ?????????????????????1
				noDutyLossItemVoList.add(lossItemVo);
				iterator.remove();
			}
		}
		String flag = "1101".equals(prpLCompensate.getRiskCode()) ? "1" : "2";//??????-1??????-2?????????

		// ??????????????????
		if (!"2".equals(prpLCompensate.getCaseType())) {
			List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(prpLCompensate.getRegistNo());
		
			if (lossCarMainVos != null && !lossCarMainVos.isEmpty()) {
				for (PrpLDlossCarMainVo carMainVo : lossCarMainVos) {
					if (carMainVo.getSerialNo() == 1
							&& compensateTaskService.checkLossState(carMainVo.getLossState(), CompensateType.PREFIX_CI)) {
						//?????????????????????????????????????????????????????????????????????????????????????????????
						PrpLLossItemVo lossItemVo = compHandleService.processLossCarVoList(carMainVo, null,"BZ", flag);
						prpLLossItemVoListForCal.add(lossItemVo);
					}
					//??????????????????????????????
					BigDecimal sumLossFee =  DataUtils.NullToZero(carMainVo.getSumVeriLossFee())
							.add(DataUtils.NullToZero(carMainVo.getSumVeriRescueFee()));
					// ?????????????????? ????????????????????????
					if (carMainVo.getSerialNo() > 1
							&& BigDecimal.ZERO.compareTo(sumLossFee)==0
							&& compensateTaskService.checkLossState(carMainVo.getLossState(), CompensateType.PREFIX_CI)) {
						PrpLLossItemVo lossItemVo = compHandleService.processLossCarVoList(carMainVo, null,"BZ", flag);
						prpLLossItemVoListForCal.add(lossItemVo);
					}
				}
			}
		
			// ????????????????????????
			List<PrpLdlossPropMainVo> lossPropMainVos = propTaskService.findPropMainListByRegistNo(prpLCompensate.getRegistNo());
			List<PrpLdlossPropMainVo> reLossPropMainVos = new ArrayList<PrpLdlossPropMainVo>();
			if (lossPropMainVos != null && !lossPropMainVos.isEmpty()) {
				for (PrpLdlossPropMainVo propMainVo : lossPropMainVos) {
					if (propMainVo.getSerialNo() == 1
							&& compensateTaskService.checkLossState(propMainVo.getLossState(), CompensateType.PREFIX_CI)) {
						reLossPropMainVos.add(propMainVo);
					}
				}
			}
			PrpLCMainVo prplcMainVo = policyViewService.getPrpLCMainByRegistNoAndPolicyNo(
														prpLCompensate.getRegistNo(),
														prpLCompensate.getPolicyNo());
			List<PrpLLossPropVo> reLossPropList = compHandleService.processLossPropVoList(
					reLossPropMainVos, prplcMainVo.getPrpCItemKinds(),CompensateType.COMP_CI);
			
			prpLLossPropVoList.addAll(reLossPropList);
		
			// ????????????????????????
			List<PrpLDlossPersTraceMainVo> reLossPersTraceMainVos = compensateTaskService.getValidLossPersTraceMain(
							prpLCompensate.getRegistNo(), CompensateType.COMP_CI);
			List<PrpLDlossPersTraceVo> lossPersTraceList = new ArrayList<PrpLDlossPersTraceVo>();
			if (reLossPersTraceMainVos != null) {
				// ?????????????????????????????????????????????PrpLDlossPersTrace
				for (PrpLDlossPersTraceMainVo lossPersTraceMain : reLossPersTraceMainVos) {
					if (!compensateTaskService.checkLossState(lossPersTraceMain.getLossState(), CompensateType.PREFIX_CI)) {
						continue;
					}
					List<PrpLDlossPersTraceVo> persTraceList = lossPersTraceMain.getPrpLDlossPersTraces();
					if (persTraceList != null && !persTraceList.isEmpty()) {
						for (PrpLDlossPersTraceVo lossPersTrace : lossPersTraceMain.getPrpLDlossPersTraces()) {
							if (lossPersTrace.getPrpLDlossPersInjured().getSerialNo() == 1
									&& lossPersTrace.getValidFlag().equals("1")) {
									lossPersTraceList.add(lossPersTrace);
								}
							}
						}
					}
				}
			List<PrpLLossPersonVo> reLossPersonList = compHandleService.processLossPersonVoList(
					lossPersTraceList, CompensateType.COMP_CI);
			prpLLossPersonVoList.addAll(reLossPersonList);
		
		}
		
		if (noDutyLossItemVoList != null && !noDutyLossItemVoList.isEmpty()) {
			for (PrpLLossItemVo noDutyItemVo : noDutyLossItemVoList) {
				for (PrpLLossItemVo lossItemVo : prpLLossItemVoListForCal) {
					if (noDutyItemVo.getItemName().equals(lossItemVo.getItemName())) {
						lossItemVo.setNoDutyPayFlag(noDutyItemVo.getNoDutyPayFlag());
					}
				}
			}
		}
		
		CompensateListVo compensateListVo = compensateTaskService.calCulator(prpLCompensate, prpLLossItemVoListForCal,
				prpLLossPropVoList,prpLLossPersonVoList,CodeConstants.CompensateKind.CI_COMPENSATE);
		// compensateList??????3???????????????(???????????????)?????????(???)???????????????(???)
		
		List<ThirdPartyLossInfo> thirdPartyLossInfolist = new ArrayList<ThirdPartyLossInfo>(0);
		// ?????????????????????????????????
		thirdPartyLossInfolist = compensateTaskService.getThirdPartyLossInfolistBz(compensateListVo, "compensate");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();// ?????????Map?????????itemLossIdAndAmtMap???????????????
		// Map ?????????DlossId???PersonId(??????????????????)??????????????????SumRealPay??????
		Map<String, Double> itemLossIdAndAmtMap = new HashMap<String, Double>();
		String compText = "";//??????????????????
		for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfolist) {
			String key = "";
			if (CodeConstants.InsteadFlag.NODUTY_INSTEAD.equals(thirdPartyLossInfo.getInsteadFlag())) {
				key = "noduty=" + thirdPartyLossInfo.getSerialNo();
			} else {
				if ("car".equals(thirdPartyLossInfo.getExpType())) {
					key = "car=" + thirdPartyLossInfo.getSerialNo();// ?????????getLicenseNo????????????DlossId
				} else if ("prop".equals(thirdPartyLossInfo.getExpType())) {
					key = "prop=" + thirdPartyLossInfo.getSerialNo();
				} else if ("person".equals(thirdPartyLossInfo.getExpType())) {
					if ("?????????".equals(thirdPartyLossInfo.getLossFeeName())) {
						key = "med=" + thirdPartyLossInfo.getLossIndex();
					} else if ("????????????".equals(thirdPartyLossInfo
							.getLossFeeName())) {
						key = "pers=" + thirdPartyLossInfo.getLossIndex();
					}
				}
			
			}
			if (itemLossIdAndAmtMap.size() > 0 && itemLossIdAndAmtMap.containsKey(key)) {
				// ?????????????????????????????????
				Double temp = itemLossIdAndAmtMap.get(key) + thirdPartyLossInfo.getSumRealPay();
				itemLossIdAndAmtMap.put(key, temp);
			} else {
				itemLossIdAndAmtMap.put(key,thirdPartyLossInfo.getSumRealPay());
			}
			if (thirdPartyLossInfo.getBzCompensateText() != null) {
		
				compText += thirdPartyLossInfo.getBzCompensateText();
			}
		}
		
		// ???????????????????????????
		if (itemLossIdAndAmtMap.get("noduty=1") != null) {
		Double sumNodutyLoss = itemLossIdAndAmtMap.get("noduty=1");
		Double nodDutyLoss =0D;
		for (PrpLLossItemVo lossItemVo : noDutyLossItemVoList) {
			if ("1".equals(lossItemVo.getNoDutyPayFlag())) {
				itemLossIdAndAmtMap.put("noduty=" + lossItemVo.getItemId(),sumNodutyLoss / noDutyNum);
				nodDutyLoss = nodDutyLoss + sumNodutyLoss / noDutyNum;
			} else {
				itemLossIdAndAmtMap.put("noduty=" + lossItemVo.getItemId(), 0d);
			}
		}
		
		if (nodDutyLoss > 0) {
			compText += "\r\n\r\n????????? :?????????????????? = " + nodDutyLoss;
			}
		}

		logger.info("?????????" + (prpLCompensate == null ? null : prpLCompensate.getRegistNo()) + "????????????????????????????????????????????????????????????");
		this.saveBzLeftAmount(compensateListVo.getLeftAmountMap(),prpLCompensate.getRegistNo(), CompensateType.COMP_CI);
		logger.info("?????????" + (prpLCompensate == null ? null : prpLCompensate.getRegistNo()) + "??????????????????????????????????????????????????????");
		resultMap.put("itemLossIdAndAmtMap", itemLossIdAndAmtMap);
		if (StringUtils.isBlank(compText)) {
			compText = "???????????????????????????";
		}
		resultMap.put("compText", compText);
		logger.debug("===Map1===" + itemLossIdAndAmtMap.toString());
		prpLCompensate.setPrpLLossItems(prpLLossItemVoListOri);
		return resultMap;
		
	}
	/**
	 * ??????????????????????????????
	 * 
	 * @param leftAmountMap
	 * @param registNo
	 * @param calType
	 * @modified: ???YangKun(2016???6???29??? ??????3:41:10): <br>
	 */
	public void saveBzLeftAmount(Map<String, Double> leftAmountMap,String registNo, String calType) {
		logger.info("?????????" + registNo + "??????????????? "+ calType + "??????????????????????????????????????????(????????????)" );
		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(registNo);
		checkDutyList = compensateTaskService.calcCheckDutyList(leftAmountMap,checkDutyList,calType);
		checkTaskService.saveCheckDutyList(checkDutyList);
		logger.info("?????????" + registNo + "??????????????? "+ calType + "??????????????????????????????????????????(????????????)" );
	}
	/**
	 * ?????????????????????????????????????????????Vo?????????????????????
	 * <pre></pre>
	 * @param resultMap
	 * @param compVo
	 * @return
	 * @modified:
	 * ???WLL(2018???1???30??? ??????10:41:08): <br>
	 */
	public PrpLCompensateVo processCalculateCIData(Map<String, Object> resultMap,PrpLCompensateVo compVo){
		String lcText = (String)resultMap.get("compText");
		BigDecimal sumPaidAmt = new BigDecimal(0);//?????????????????????
		BigDecimal sumPaidFee = new BigDecimal(0);//?????????????????????

		BigDecimal offPreAmt = new BigDecimal(0);//????????????????????????????????????

		compVo.setLcText(lcText);
		Map<String, Double> itemLossIdAndAmtMap = (Map<String,Double>)resultMap.get("itemLossIdAndAmtMap");
		Map<String,BigDecimal> sumRealPays = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> sumlossPays = new HashMap<String,BigDecimal>();
		for (Map.Entry<String, Double> entry : itemLossIdAndAmtMap.entrySet()) { 
			String strKey[]=entry.getKey().split("=");		
			if("noduty".equals(strKey[0])){//????????????????????????
				if(compVo.getPrpLLossItems()!=null && compVo.getPrpLLossItems().size() > 0){
					for(PrpLLossItemVo itemVo : compVo.getPrpLLossItems()){
						if(strKey[1].equals(itemVo.getItemId()) && "4".equals(itemVo.getPayFlag()) ){
							itemVo.setSumRealPay(new BigDecimal(entry.getValue()));
							sumPaidAmt = sumPaidAmt.add(itemVo.getSumRealPay());
						}
					}
				}
			}else if("car".equals(strKey[0])){//??????????????????
				if(compVo.getPrpLLossItems()!=null && compVo.getPrpLLossItems().size() > 0){
					for(PrpLLossItemVo itemVo : compVo.getPrpLLossItems()){
						if(strKey[1].equals(itemVo.getItemId()) && !"4".equals(itemVo.getPayFlag()) ){
							itemVo.setSumRealPay(new BigDecimal(entry.getValue()));
							sumPaidAmt = sumPaidAmt.add(itemVo.getSumRealPay());
						}
						if("2".equals(itemVo.getPayFlag())){//??????
							if(itemVo.getPlatLockList()!=null && itemVo.getPlatLockList().size() > 0){
								for(PrpLPlatLockVo lockVo : itemVo.getPlatLockList()){
									lockVo.setThisPaid(new BigDecimal(entry.getValue()));
								}
							}
							
						}
					}
				}

			}else if("prop".equals(strKey[0])){//??????????????????
				if(compVo.getPrpLLossProps()!=null && compVo.getPrpLLossProps().size() > 0){
					for(PrpLLossPropVo itemVo : compVo.getPrpLLossProps()){
						if(strKey[1].equals(itemVo.getItemId())){
							itemVo.setSumRealPay(new BigDecimal(entry.getValue()));
							sumPaidAmt = sumPaidAmt.add(itemVo.getSumRealPay());
						}
					}
				}
			}else{//??????
				if((!sumlossPays.isEmpty() && !sumlossPays.containsKey(strKey[1])) || (sumlossPays.isEmpty())){
						BigDecimal sumlossPay = new BigDecimal(0);//???????????????????????????
						sumlossPays.put(strKey[1],sumlossPay);
				}
				if((!sumRealPays.isEmpty() && !sumRealPays.containsKey(strKey[1])) || (sumRealPays.isEmpty())){
					BigDecimal perSumRealPay = BigDecimal.ZERO;
					sumRealPays.put(strKey[1],perSumRealPay);
				}
				BigDecimal sumRealPay = sumRealPays.get(strKey[1]);
				BigDecimal sumLoss = sumlossPays.get(strKey[1]);//???????????????????????????
				String typeVal = "00";//????????????
				if("med".equals(strKey[0])){
					typeVal = "03";
				}
				if("pers".equals(strKey[0])){
					typeVal = "02";
				}
				if(compVo.getPrpLLossPersons()!=null && compVo.getPrpLLossPersons().size() > 0){
					for(PrpLLossPersonVo itemVo : compVo.getPrpLLossPersons()){
						if(strKey[1].equals(itemVo.getPersonId().toString())){
							for(PrpLLossPersonFeeVo feeVo : itemVo.getPrpLLossPersonFees()){
								if(typeVal.equals(feeVo.getLossItemNo())){
									feeVo.setFeeRealPay(new BigDecimal(entry.getValue()));
									sumRealPay = sumRealPay.add(feeVo.getFeeRealPay());
									sumLoss = sumLoss.add(feeVo.getFeeLoss());
									sumPaidAmt = sumPaidAmt.add(feeVo.getFeeRealPay());
								}
							}
							//??????????????????????????????????????????
							sumlossPays.put(strKey[1],sumLoss);
							sumRealPays.put(strKey[1],sumRealPay);
							itemVo.setSumRealPay(sumRealPay);
							itemVo.setSumLoss(sumLoss);
							itemVo.setOffPreAmt(offPreAmt);
						}
					}
				}
			}
		} 
		if(compVo.getPrpLCharges()!=null && compVo.getPrpLCharges().size()>0){
			for(PrpLChargeVo chargVo : compVo.getPrpLCharges()){
				sumPaidFee = sumPaidFee.add(chargVo.getFeeAmt());//???????????????????????????????????????????????????
			}
		}
		//??????????????????????????????????????????
		compVo.setSumAmt(sumPaidAmt);
		compVo.setSumPreAmt(BigDecimal.ZERO);//?????????????????????????????????
		compVo.setSumPaidAmt(sumPaidAmt);
		compVo.setSumFee(sumPaidFee);
		compVo.setSumPreFee(BigDecimal.ZERO);
		compVo.setSumPaidFee(sumPaidFee);
		
		return compVo;
	}
	/**
	 * ??????????????????/???????????????????????????
	 * <pre></pre>
	 * @param prpLCompensateVo
	 * @param compensateVo
	 * @modified:
	 * ???WLL(2018???1???30??? ??????2:56:54): <br>
	 */
	public void processGetRateBIData(PrpLCompensateVo prpLCompensateVo,CompensateVo compensateVo,List<PrpLClaimDeductVo> claimDeductVoList){
		if(compensateVo != null && prpLCompensateVo != null){
			boolean noFindThirdFlag = false;//????????????????????????????????????????????????			
			for (PrpLClaimDeductVo claimDeductVo : claimDeductVoList) {
				if ("1".equals(claimDeductVo.getIsCheck())
						&& ("120".equals(claimDeductVo.getDeductCondCode()) 
								|| "320".equals(claimDeductVo.getDeductCondCode()))) {
					noFindThirdFlag = true;
				}
			}
			BigDecimal dutyRate = prpLCompensateVo.getIndemnityDutyRate();
			if(compensateVo.getPrpLLossItemVoList()!=null&&prpLCompensateVo.getPrpLLossItems()!=null){
				for(PrpLLossItemVo itemVoForDuty : compensateVo.getPrpLLossItemVoList()){
					for(PrpLLossItemVo itemVo :prpLCompensateVo.getPrpLLossItems()){
						if(itemVo.getKindCode().equals(itemVoForDuty.getKindCode()) ){
							itemVo.setDutyRate(dutyRate);
							itemVo.setDeductAddRate(itemVoForDuty.getDeductAddRate());
							if(KINDCODE.KINDCODE_A.equals(itemVo.getKindCode())
									|| KINDCODE.KINDCODE_B.equals(itemVo.getKindCode())
									|| KINDCODE.KINDCODE_X.equals(itemVo.getKindCode())){
								if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(itemVo.getRiskCode()) != null &&
										CodeConstants.ISNEWCLAUSECODE2020_MAP.get(itemVo.getRiskCode())){
									itemVo.setDeductAbsRate(DataUtils.NullToZero(itemVoForDuty.getDeductDutyRate()));
								}else{
									itemVo.setDeductAbsRate(new BigDecimal(0));
								}
								itemVo.setDeductDutyRate(itemVoForDuty.getDeductDutyRate());
							}else{
								itemVo.setDeductAbsRate(itemVoForDuty.getDeductDutyRate());
								itemVo.setDeductDutyRate(new BigDecimal(0));
							}
							break;
						}
					}
				}
			}
			
			if(compensateVo.getPrpLLossPropVoList()!=null&&prpLCompensateVo.getPrpLLossProps()!=null){
				for(PrpLLossPropVo itemVoForDuty : compensateVo.getPrpLLossPropVoList()){
					for(PrpLLossPropVo itemVo :prpLCompensateVo.getPrpLLossProps()){
						if(itemVo.getKindCode().equals(itemVoForDuty.getKindCode())){
							itemVo.setDutyRate(dutyRate);
							itemVo.setDeductAddRate(itemVoForDuty.getDeductAddRate());
							if(KINDCODE.KINDCODE_A.equals(itemVo.getKindCode())
									|| KINDCODE.KINDCODE_B.equals(itemVo.getKindCode())
									|| KINDCODE.KINDCODE_X.equals(itemVo.getKindCode())){
								if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(itemVo.getRiskCode()) != null &&
										CodeConstants.ISNEWCLAUSECODE2020_MAP.get(itemVo.getRiskCode())){
									itemVo.setDeductAbsRate(DataUtils.NullToZero(itemVoForDuty.getDeductDutyRate()));
								}else{
									itemVo.setDeductAbsRate(new BigDecimal(0));
								}
								
								itemVo.setDeductDutyRate(itemVoForDuty.getDeductDutyRate());
							}else{
								itemVo.setDeductAbsRate(itemVoForDuty.getDeductDutyRate());
								itemVo.setDeductDutyRate(new BigDecimal(0));
							}
							break;
						}
					}
				}
				if(!noFindThirdFlag){
					Iterator<PrpLLossPropVo> it = prpLCompensateVo.getPrpLLossProps().iterator();  
					while(it.hasNext()){
						PrpLLossPropVo propVo = it.next();
						if(KINDCODE.KINDCODE_NT.equals(propVo.getKindCode())){
							it.remove();  
			                logger.info("?????????????????????????????? ????????????????????????????????????????????? ????????????"); 
			                //TODO ??????????????????????????????????????????
						}
					}
				}
			}
			
			if(compensateVo.getPrpLLossPersonVoList()!=null&&prpLCompensateVo.getPrpLLossPersons()!=null){
				for(PrpLLossPersonVo itemVoForDuty : compensateVo.getPrpLLossPersonVoList()){
					for(PrpLLossPersonVo itemVo :prpLCompensateVo.getPrpLLossPersons()){
						if(itemVo.getKindCode().equals(itemVoForDuty.getKindCode()) &&  itemVo.getPersonId().equals(itemVoForDuty.getPersonId())){
							itemVo.setDutyRate(dutyRate);
							itemVo.setDeductAddRate(itemVoForDuty.getDeductAddRate());
							if(KINDCODE.KINDCODE_B.equals(itemVo.getKindCode())
									|| KINDCODE.KINDCODE_D11.equals(itemVo.getKindCode())
									|| KINDCODE.KINDCODE_D12.equals(itemVo.getKindCode())){
								if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(itemVo.getRiskCode()) != null &&
										CodeConstants.ISNEWCLAUSECODE2020_MAP.get(itemVo.getRiskCode())){
									itemVo.setDeductAbsRate(DataUtils.NullToZero(itemVoForDuty.getDeductDutyRate()));
								}else{
									itemVo.setDeductAbsRate(new BigDecimal(0));
								}
								itemVo.setDeductDutyRate(itemVoForDuty.getDeductDutyRate());
							}else{
								itemVo.setDeductAbsRate(itemVoForDuty.getDeductDutyRate());
								itemVo.setDeductDutyRate(new BigDecimal(0));
							}
						}
					}
				}
			}
		}
	}
	/**
	 * ???????????????????????????
	 * <pre></pre>
	 * @param compeVo
	 * @modified:
	 * ???WLL(2018???2???1??? ??????9:59:58): <br>
	 */
	public Map<String, Object> bIGetBZAmt(PrpLCompensateVo compVo){
		//?????????????????????????????????????????????compVo??????????????????
		PrpLCompensateVo compeVoForBZAmt = Beans.copyDepth().from(compVo).to(PrpLCompensateVo.class);
		Map<String, Object> resultMap = new HashMap<String, Object>();// ?????????Map?????????itemLossIdAndAmtMap???????????????
		
		List<ThirdPartyLossInfo> thirdPartyLossInfolist = new ArrayList<ThirdPartyLossInfo>(0);
		Iterator<PrpLLossItemVo> iterator = compeVoForBZAmt.getPrpLLossItems().iterator();
		while (iterator.hasNext()) {
			PrpLLossItemVo lossItemVo = iterator.next();
			if (!(CodeConstants.KINDCODE.KINDCODE_A.equals(lossItemVo.getKindCode()) 
					|| CodeConstants.KINDCODE.KINDCODE_B.equals(lossItemVo.getKindCode()))) {
				// ???????????? A???B?????????????????????
				iterator.remove();//TODO ?????????????????????????????????????????????list??????
			}
		}
		CompensateListVo compensateListVo = compensateTaskService.calCulator(
				compeVoForBZAmt, compeVoForBZAmt.getPrpLLossItems(), compeVoForBZAmt.getPrpLLossProps(),
				compeVoForBZAmt.getPrpLLossPersons(),CodeConstants.CompensateKind.BI_COMPENSATE);
		// ?????????????????????????????????
		thirdPartyLossInfolist = compensateTaskService.getThirdPartyLossInfolistBz(compensateListVo, "compensate");
			
		// Map ?????????DlossId???PersonId(??????????????????)??????????????????SumRealPay??????
		Map<String, Double> itemLossIdMap = new HashMap<String, Double>();// ???????????????
		Map<String, Double> itemAmtMap = new HashMap<String, Double>();// ??????????????????
		Map<String, Double> NoDutyCarPay = new HashMap<String, Double>();// ??????????????????
		String compText = "";

		for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfolist) {
			String key = "";
			if ("car".equals(thirdPartyLossInfo.getExpType())) {
				key = "car=" + thirdPartyLossInfo.getLicenseNo();// ?????????getLicenseNo????????????DlossId
			} else if ("prop".equals(thirdPartyLossInfo.getExpType())) {
				key = "prop=" + thirdPartyLossInfo.getLicenseNo();
			} else if ("person".equals(thirdPartyLossInfo.getExpType())) {
				if ("?????????".equals(thirdPartyLossInfo.getLossFeeName())) {
					key = "med=" + thirdPartyLossInfo.getLossIndex();
				} else if ("????????????".equals(thirdPartyLossInfo
						.getLossFeeName())) {
					key = "pers=" + thirdPartyLossInfo.getLossIndex();
				}
			}
			if (CodeConstants.InsteadFlag.NODUTY_INSTEAD.equals(thirdPartyLossInfo.getInsteadFlag())) {
				if (thirdPartyLossInfo.getSerialNo() == 1) {
					String dutyKey = "noduty=" + thirdPartyLossInfo.getSerialNo();
					if (NoDutyCarPay.containsKey(dutyKey)) {
						Double temp1 = NoDutyCarPay.get(dutyKey) + thirdPartyLossInfo.getNoDutyCarPay();
						NoDutyCarPay.put(dutyKey, temp1);
					} else {
						NoDutyCarPay.put(dutyKey, thirdPartyLossInfo.getNoDutyCarPay());
					}
				} else {
					String dutyKey = "car=" + thirdPartyLossInfo.getLicenseNo();
					if (itemLossIdMap.containsKey(dutyKey)) {
						Double temp1 = NoDutyCarPay.get(dutyKey) + thirdPartyLossInfo.getNoDutyCarPay();
						itemLossIdMap.put(dutyKey, temp1);
					} else {
						itemLossIdMap.put(dutyKey, thirdPartyLossInfo.getNoDutyCarPay());
					}
				}
			}
			if ("car".equals(thirdPartyLossInfo.getExpType())
					|| "prop".equals(thirdPartyLossInfo.getExpType())) {
				double sumLoss = thirdPartyLossInfo.getSumLoss();
				double rescueFee = thirdPartyLossInfo.getRescueFee();
				double lossRate = (sumLoss - rescueFee) / sumLoss;
				if(sumLoss==0.0){
					lossRate=0.0;
				}
				double sumRealPay = thirdPartyLossInfo.getSumRealPay();
				if ("car".equals(thirdPartyLossInfo.getExpType())
						&& thirdPartyLossInfo.getNoDutyCarPay() > 0) {
					sumRealPay = sumRealPay - thirdPartyLossInfo.getNoDutyCarPay();
				}

				double realPay = sumRealPay * lossRate;
				double rescueFeePay = sumRealPay - realPay;
				// ???????????????
				if (itemLossIdMap.containsKey(key)) {
					Double temp1 = itemLossIdMap.get(key) + realPay;
					itemLossIdMap.put(key, temp1);
				} else {
					itemLossIdMap.put(key, realPay);
				}
				// ??????????????????
				if (itemAmtMap.containsKey(key)) {
					Double temp1 = itemAmtMap.get(key) + rescueFeePay;
					itemAmtMap.put(key, temp1);
				} else {
					itemAmtMap.put(key, rescueFeePay);
				}

			} else {// ??????  ???????????????
				if (itemLossIdMap.containsKey(key)) {
					Double temp1 = itemLossIdMap.get(key) + thirdPartyLossInfo.getSumRealPay();
					itemLossIdMap.put(key, temp1);
				} else {
					itemLossIdMap.put(key, thirdPartyLossInfo.getSumRealPay());
				}
			}

			logger.debug("=====000====" + thirdPartyLossInfo.getLicenseNo()
					+ "," + thirdPartyLossInfo.getLossName() + ","
					+ thirdPartyLossInfo.getSumRealPay());
			logger.debug("=====010===="
					+ thirdPartyLossInfo.getBzCompensateText()
					+ "=====010end====");

			compText += thirdPartyLossInfo.getBzCompensateText();

		}
		logger.info("?????????" + (compVo == null ? null : compVo.getRegistNo()) + "?????????????????????????????????????????????????????????");
		this.saveBzLeftAmount(compensateListVo.getLeftAmountMap(),
				compVo.getRegistNo(), CompensateType.COMP_BI);
		logger.info("?????????" + (compVo == null ? null : compVo.getRegistNo()) + "???????????????????????????????????????????????????");
		resultMap.put("itemLossIdMap", itemLossIdMap);
		resultMap.put("itemAmtMap", itemAmtMap);
		resultMap.put("NoDutyCarPay", NoDutyCarPay);
		resultMap.put("compText", compText);
		return resultMap;
	}
	/**
	 * ????????????????????????????????????????????????
	 * <pre></pre>
	 * @param resultMap
	 * @param compVo
	 * @modified:
	 * ???WLL(2018???2???1??? ??????12:00:20): <br>
	 */
	public void processBIGetBZAmtData(Map<String, Object> resultMap,PrpLCompensateVo compVo){
		Map<String, Double> itemLossIdMap = (Map<String,Double>)resultMap.get("itemLossIdMap");// ???????????????
		Map<String, Double> itemAmtMap = (Map<String,Double>)resultMap.get("itemAmtMap");// ??????????????????
		Map<String, Double> NoDutyCarPay = (Map<String,Double>)resultMap.get("NoDutyCarPay");// ??????????????????
		if(compVo.getPrpLLossItems()!=null&&compVo.getPrpLLossItems().size()>0){
			for(PrpLLossItemVo itemVo:compVo.getPrpLLossItems()){
				itemVo.setBzPaidLoss(BigDecimal.ZERO);//????????????
				itemVo.setBzPaidRescueFee(BigDecimal.ZERO);
				itemVo.setAbsolvePayAmt(BigDecimal.ZERO);
				itemVo.setAbsolvePayAmts(BigDecimal.ZERO);
				if(KINDCODE.KINDCODE_A.equals(itemVo.getKindCode())
						||KINDCODE.KINDCODE_B.equals(itemVo.getKindCode())){
					if(itemLossIdMap.containsKey("car="+itemVo.getDlossId())){//???????????????
						itemVo.setBzPaidLoss(new BigDecimal(itemLossIdMap.get("car="+itemVo.getDlossId())));
					}
					if(itemAmtMap.containsKey("car="+itemVo.getDlossId())){//??????????????????
						itemVo.setBzPaidRescueFee(new BigDecimal(itemAmtMap.get("car="+itemVo.getDlossId())));
					}
				}
				if(KINDCODE.KINDCODE_A.equals(itemVo.getKindCode())){//????????????
					if(NoDutyCarPay.containsKey("noduty="+itemVo.getItemId())){//serialNo
						itemVo.setAbsolvePayAmt(new BigDecimal(NoDutyCarPay.get("noduty="+itemVo.getItemId())));
						itemVo.setAbsolvePayAmts(new BigDecimal(NoDutyCarPay.get("noduty="+itemVo.getItemId())));
					}
				}
			}
			
		}
		if(compVo.getPrpLLossProps()!=null&&compVo.getPrpLLossProps().size()>0){
			for(PrpLLossPropVo itemVo:compVo.getPrpLLossProps()){
				itemVo.setBzPaidLoss(BigDecimal.ZERO);//????????????
				itemVo.setBzPaidRescueFee(BigDecimal.ZERO);
				if(itemLossIdMap.containsKey("prop="+itemVo.getDlossId())){//???????????????
					itemVo.setBzPaidLoss(new BigDecimal(itemLossIdMap.get("prop="+itemVo.getDlossId())));
				}
				if(itemAmtMap.containsKey("prop="+itemVo.getDlossId())){//??????????????????
					itemVo.setBzPaidRescueFee(new BigDecimal(itemAmtMap.get("prop="+itemVo.getDlossId())));
				}
			}
		}
		if(compVo.getPrpLLossPersons()!=null&&compVo.getPrpLLossPersons().size()>0){
			for(PrpLLossPersonVo itemVo:compVo.getPrpLLossPersons()){
				BigDecimal sumBzPaidLoss = BigDecimal.ZERO;
				for(PrpLLossPersonFeeVo feeVo:itemVo.getPrpLLossPersonFees()){
					if(itemLossIdMap.containsKey("med="+itemVo.getPersonId())
							&& "03".equals(feeVo.getLossItemNo())){//??????????????? ??????
						feeVo.setBzPaidLoss(new BigDecimal(itemLossIdMap.get("med="+itemVo.getPersonId())));
						sumBzPaidLoss = sumBzPaidLoss.add(feeVo.getBzPaidLoss());
					}
					if(itemLossIdMap.containsKey("pers="+itemVo.getPersonId())
							&& "02".equals(feeVo.getLossItemNo())){//??????????????? ??????
						feeVo.setBzPaidLoss(new BigDecimal(itemLossIdMap.get("pers="+itemVo.getPersonId())));
						sumBzPaidLoss = sumBzPaidLoss.add(feeVo.getBzPaidLoss());
					}
				}
				itemVo.setBzPaidLoss(sumBzPaidLoss);//????????????
			}
		}
	}
	/**
	 * ????????????????????????
	 * <pre></pre>
	 * @param compVo???????????????VoList???
	 * @param claimDeductVoList
	 * @return
	 * @modified:
	 * ???WLL(2018???2???5??? ??????10:40:17): <br>
	 */
	public Map<String, Object>  calculateBIAuto(PrpLCompensateVo compVo,List<PrpLClaimDeductVo> claimDeductVoList){
		Map<String, Object> resultMap = new HashMap<String,Object>();// ?????????Map?????????itemLossIdAndAmtMap???????????????
		List<PrpLLossPropVo> propList = new ArrayList<PrpLLossPropVo>();
		List<PrpLLossPropVo> othList = new ArrayList<PrpLLossPropVo>();
//		if(compVo.getPrpLLossProps()!=null&&compVo.getPrpLLossProps().size()>0){
//			for(PrpLLossPropVo itemVo:compVo.getPrpLLossProps()){
//				if("1".equals(itemVo.getPropType())){
//					propList.add(itemVo);
//				}else{
//					othList.add(itemVo);
//				}
//			}
//		}
		CompensateVo compensateResult = compHandleService.calculateBI(compVo, claimDeductVoList,
				compVo.getPrpLLossItems(),compVo.getPrpLLossProps(), compVo.getPrpLLossProps(), compVo.getPrpLLossPersons());
		
		Map<String, Double> itemLossIdAndAmtMap = new HashMap<String, Double>();
		Map<String, Double> deductOffMap = new HashMap<String, Double>();// ????????????
		String key = "";
		PrpLCompensateVo compensateVo = compensateResult.getPrpLCompensateVo();
		List<PrpLLossItemVo> prpLLossItemVos = compensateResult.getPrpLLossItemVoList();// car
		List<PrpLLossPropVo> PrpLLossPropVos = compensateResult.getPrpLLossPropVoList();// ???
		List<PrpLLossPersonVo> PrpLLossPersonVos = compensateResult.getPrpLLossPersonVoList();// ???

		for (PrpLLossItemVo prpLLossItemVo : prpLLossItemVos) {
			if (CodeConstants.KINDCODE.KINDCODE_A.equals(prpLLossItemVo.getKindCode())
					|| CodeConstants.KINDCODE.KINDCODE_B.equals(prpLLossItemVo.getKindCode())) {
				if (CodeConstants.PayFlagType.INSTEAD_PAY.equals(prpLLossItemVo.getPayFlag())) {// ??????
					key = "recovery=" + prpLLossItemVo.getItemId();
					// ????????????Map
					itemLossIdAndAmtMap.put(key, prpLLossItemVo.getSumRealPay().doubleValue());
					// ????????????Map
					deductOffMap.put(key, prpLLossItemVo.getDeductOffAmt().doubleValue());
				} else {
					key = "car=" + prpLLossItemVo.getItemId();
					if (itemLossIdAndAmtMap.containsKey(key)) {
						// ?????????????????????????????????
						Double temp = itemLossIdAndAmtMap.get(key) + prpLLossItemVo.getSumRealPay().doubleValue();
						itemLossIdAndAmtMap.put(key, temp);
					} else {
						itemLossIdAndAmtMap.put(key, prpLLossItemVo.getSumRealPay().doubleValue());
					}
					if (deductOffMap.containsKey(key)) {
						Double temp = deductOffMap.get(key) + prpLLossItemVo.getDeductOffAmt().doubleValue();
						deductOffMap.put(key, temp);
					} else {
						deductOffMap.put(key, prpLLossItemVo.getDeductOffAmt().doubleValue());
					}
				}
			} else {
				key = "car=" + prpLLossItemVo.getKindCode();
				if (itemLossIdAndAmtMap.containsKey(key)) {
					// ?????????????????????????????????
					Double temp = itemLossIdAndAmtMap.get(key) + prpLLossItemVo.getSumRealPay().doubleValue();
					itemLossIdAndAmtMap.put(key, temp);
				} else {
					itemLossIdAndAmtMap.put(key, prpLLossItemVo.getSumRealPay().doubleValue());
				}

				if (deductOffMap.containsKey(key)) {
					Double temp = deductOffMap.get(key) + prpLLossItemVo.getDeductOffAmt().doubleValue();
					deductOffMap.put(key, temp);
				} else {
					deductOffMap.put(key, prpLLossItemVo.getDeductOffAmt().doubleValue());
				}
			}
		}

		for (PrpLLossPropVo prpLLossPropVo : PrpLLossPropVos) {
			if ("1".equals(prpLLossPropVo.getPropType())) {
				key = "prop=" + prpLLossPropVo.getItemId();
			} else {
				key = "other=" + prpLLossPropVo.getKindCode();
			}

			if (itemLossIdAndAmtMap.containsKey(key)) {
				// ?????????????????????????????????
				Double temp = itemLossIdAndAmtMap.get(key) + prpLLossPropVo.getSumRealPay().doubleValue();
				itemLossIdAndAmtMap.put(key, temp);
			} else {
				itemLossIdAndAmtMap.put(key, prpLLossPropVo.getSumRealPay().doubleValue());
			}

			if (deductOffMap.containsKey(key)) {
				Double temp = itemLossIdAndAmtMap.get(key) + prpLLossPropVo.getDeductOffAmt().doubleValue();
				deductOffMap.put(key, temp);
			} else {
				deductOffMap.put(key, prpLLossPropVo.getDeductOffAmt().doubleValue());
			}
		}

		for (PrpLLossPersonVo prpLLossPersonVo : PrpLLossPersonVos) {
			key = "person";

			List<PrpLLossPersonFeeVo> prpLLossPersonFeeVos = prpLLossPersonVo.getPrpLLossPersonFees();
			for (PrpLLossPersonFeeVo prpLLossPersonFeeVo : prpLLossPersonFeeVos) {
				if (prpLLossPersonFeeVo.getLossItemNo().equals("02")) {
					key = "pers=" + prpLLossPersonVo.getPersonId();
				}
				if (prpLLossPersonFeeVo.getLossItemNo().equals("03")) {
					key = "med=" + prpLLossPersonVo.getPersonId();
				}
				if (itemLossIdAndAmtMap.containsKey(key)) {
					// ?????????????????????????????????
					Double temp = itemLossIdAndAmtMap.get(key) + prpLLossPersonFeeVo.getFeeRealPay().doubleValue();
					itemLossIdAndAmtMap.put(key, temp);
				} else {
					if (prpLLossPersonFeeVo.getFeeRealPay() != null) {// ????????????????????????
						itemLossIdAndAmtMap.put(key, prpLLossPersonFeeVo.getFeeRealPay().doubleValue());
					} else {
						itemLossIdAndAmtMap.put(key, (double) 0);
					}
				}

				if (deductOffMap.containsKey(key)) {
					Double temp = deductOffMap.get(key) + prpLLossPersonFeeVo.getDeductOffAmt().doubleValue();
					itemLossIdAndAmtMap.put(key, temp);
				} else {
					if (prpLLossPersonFeeVo.getFeeRealPay() != null) {// ????????????????????????
						deductOffMap.put(key, prpLLossPersonFeeVo.getDeductOffAmt().doubleValue());
					} else {
						deductOffMap.put(key, (double) 0);
					}
				}
			}

		}
		// ?????????????????????
		String compText = compensateVo.getLcText();
		if (StringUtils.isBlank(compText)) {
			compText = "???????????????????????????";
		}
		resultMap.put("compText", compText);
		resultMap.put("itemLossIdAndAmtMap", itemLossIdAndAmtMap);
		resultMap.put("deductOffMap", deductOffMap);
		return resultMap;
	}
	/**
	 * ??????????????????????????????????????????
	 * <pre></pre>
	 * @param compVo
	 * @param resultMap
	 * @param dwPersFlag ????????????????????????
	 * @modified:
	 * ???WLL(2018???2???5??? ??????5:49:29): <br>
	 */
	public void processCalculateBIData(PrpLCompensateVo compVo,Map<String, Object> resultMap,String dwPersFlag){
		Map<String, Double> itemLossIdAndAmtMap = (Map<String,Double>)resultMap.get("itemLossIdAndAmtMap");//????????????
		Map<String, Double> deductOffMap = (Map<String,Double>)resultMap.get("deductOffMap");// ????????????
		String compText = (String)resultMap.get("compText");
		compVo.setLcText(compText);
		if(compVo.getPrpLLossItems()!=null&&compVo.getPrpLLossItems().size()>0){
			for(PrpLLossItemVo itemVo:compVo.getPrpLLossItems()){
				if("4".equals(itemVo.getPayFlag())){//????????????
					if(itemLossIdAndAmtMap.containsKey("noduty="+itemVo.getItemId())){
						BigDecimal sumRealPay = new BigDecimal(itemLossIdAndAmtMap.get("noduty="+itemVo.getItemId()));
						BigDecimal deductOffAmt = new BigDecimal(deductOffMap.get("noduty="+itemVo.getItemId()));
						itemVo.setSumRealPay(sumRealPay.add(deductOffAmt));//????????????????????????
						itemVo.setDeductOffAmt(deductOffAmt);
					}
				}else if("1".equals(itemVo.getPayFlag())){//??????
					if(itemLossIdAndAmtMap.containsKey("recovery="+itemVo.getItemId())){
						BigDecimal sumRealPay = new BigDecimal(itemLossIdAndAmtMap.get("recovery="+itemVo.getItemId()));
						BigDecimal deductOffAmt = new BigDecimal(deductOffMap.get("recovery="+itemVo.getItemId()));
						itemVo.setSumRealPay(sumRealPay);
						itemVo.setDeductOffAmt(deductOffAmt);
						if("1".equals(dwPersFlag)){
							//??????????????????????????????
							itemVo.getPlatLockList().get(0).setThisPaid(sumRealPay);
						}
					}
				}else{
					if(!KINDCODE.KINDCODE_A.equals(itemVo.getKindCode())//?????????
							&&!KINDCODE.KINDCODE_B.equals(itemVo.getKindCode())){
						if(itemLossIdAndAmtMap.containsKey("car="+itemVo.getKindCode())){
							BigDecimal sumRealPay = new BigDecimal(itemLossIdAndAmtMap.get("car="+itemVo.getKindCode()));
							BigDecimal deductOffAmt = new BigDecimal(deductOffMap.get("car="+itemVo.getKindCode()));
							itemVo.setSumRealPay(sumRealPay.add(deductOffAmt));//????????????????????????
							itemVo.setDeductOffAmt(deductOffAmt);
						}
					}else{
						//??????????????????????????????
						if("3".equals(itemVo.getPayFlag()) || "2".equals(itemVo.getPayFlag())){//???????????????
							if(itemLossIdAndAmtMap.containsKey("car="+itemVo.getItemId())){
								BigDecimal sumRealPay = new BigDecimal(itemLossIdAndAmtMap.get("car="+itemVo.getItemId()));
								BigDecimal deductOffAmt = new BigDecimal(deductOffMap.get("car="+itemVo.getItemId()));
								itemVo.setSumRealPay(sumRealPay.add(deductOffAmt));//????????????????????????
								itemVo.setDeductOffAmt(deductOffAmt);
								if("2".equals(itemVo.getPayFlag())){//??????
									itemVo.getPlatLockList().get(0).setThisPaid(sumRealPay);
								}
							}
						}
					}
				}
			}
		}
		
		if(compVo.getPrpLLossProps()!=null&&compVo.getPrpLLossProps().size()>0){
			for(PrpLLossPropVo itemVo:compVo.getPrpLLossProps()){
				String key = "";
				if("1".equals(itemVo.getPropType())){
					key = "prop="+itemVo.getItemId();
				}else{
					key = "other="+itemVo.getKindCode();
				}
				BigDecimal sumRealPay = new BigDecimal(itemLossIdAndAmtMap.get(key));
				BigDecimal deductOffAmt = new BigDecimal(deductOffMap.get(key));
				itemVo.setSumRealPay(sumRealPay.add(deductOffAmt));//????????????????????????
				itemVo.setDeductOffAmt(deductOffAmt);
			}
		}
		
		if(compVo.getPrpLLossPersons()!=null&&compVo.getPrpLLossPersons().size()>0){
			for(PrpLLossPersonVo itemVo:compVo.getPrpLLossPersons()){
				BigDecimal sumRealPay = BigDecimal.ZERO;
				BigDecimal sumLoss = BigDecimal.ZERO;
				BigDecimal sumOffLoss = BigDecimal.ZERO;
				for(PrpLLossPersonFeeVo feeVo:itemVo.getPrpLLossPersonFees()){
					String key = "";
					if(CodeConstants.FeeTypeCode.PERSONLOSS.equals(feeVo.getLossItemNo())){
						key = "pers="+itemVo.getPersonId();
					}else if(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES.equals(feeVo.getLossItemNo())){
						key = "med="+itemVo.getPersonId();
					}
					if(itemLossIdAndAmtMap.containsKey(key)){
						BigDecimal feeRealPay = new BigDecimal(itemLossIdAndAmtMap.get(key));
						BigDecimal deductOffAmt = new BigDecimal(deductOffMap.get(key));
						feeVo.setFeeRealPay(feeRealPay.add(deductOffAmt));//????????????????????????
						feeVo.setDeductOffAmt(deductOffAmt);
						sumRealPay = sumRealPay.add(feeRealPay);
						sumLoss = sumLoss.add(feeVo.getFeeLoss());
					}
				}
				itemVo.setSumRealPay(sumRealPay);
				itemVo.setSumLoss(sumLoss);
				itemVo.setSumOffLoss(sumOffLoss);
			}
		}
	}
	/**
	 * ??????????????????????????????
	 * <pre></pre>
	 * @param compVo
	 * @modified:
	 * ???WLL(2018???2???5??? ??????6:35:27): <br>
	 */
	public void feeProcess(PrpLCompensateVo compVo){
		if(compVo.getPrpLCharges()!=null&&compVo.getPrpLCharges().size()>0){
			for(PrpLChargeVo feeVo:compVo.getPrpLCharges()){
				BigDecimal feeAmt = feeVo.getFeeAmt();
				BigDecimal offAmt = DataUtils.NullToZero(feeVo.getOffAmt());
				BigDecimal offPreAmt = DataUtils.NullToZero(feeVo.getOffPreAmt());
				BigDecimal feeRealAmt = feeAmt.subtract(offAmt).subtract(offPreAmt);
				feeVo.setFeeRealAmt(feeRealAmt);
			}
		}
	}
	
	/**
	 * ??????????????????????????????????????????
	 * <pre></pre>
	 * @param compVo
	 * @modified:
	 * ???WLL(2018???2???6??? ??????3:22:25): <br>
	 */
	public void sumAllAmt(PrpLCompensateVo compVo){
		
		BigDecimal sumAmt = BigDecimal.ZERO;//???????????????
		BigDecimal sumPreAmt = BigDecimal.ZERO;//????????????-???????????????????????????
		BigDecimal sumPaidAmt = BigDecimal.ZERO;//??????????????????
		BigDecimal sumFee = BigDecimal.ZERO;//???????????????
		BigDecimal sumPreFee = BigDecimal.ZERO;//????????????-???????????????????????????
		BigDecimal sumPaidFee = BigDecimal.ZERO;//??????????????????
		BigDecimal deductOffALL = BigDecimal.ZERO;//??????????????????
		BigDecimal sumBzPaid = BigDecimal.ZERO;//??????????????????
		
		if(compVo.getPrpLLossItems()!=null&&compVo.getPrpLLossItems().size()>0){
			for(PrpLLossItemVo itemVo:compVo.getPrpLLossItems()){
				if(!"1101".equals(compVo.getRiskCode())){
					sumBzPaid = sumBzPaid.add(itemVo.getBzPaidLoss()).add(itemVo.getBzPaidRescueFee());
					deductOffALL = deductOffALL.add(itemVo.getDeductOffAmt());
				}
				sumPaidAmt = sumPaidAmt.add(itemVo.getSumRealPay());
			}
		}
		if(compVo.getPrpLLossProps()!=null&&compVo.getPrpLLossProps().size()>0){
			for(PrpLLossPropVo itemVo:compVo.getPrpLLossProps()){
				sumPaidAmt = sumPaidAmt.add(itemVo.getSumRealPay());
				if(!"1101".equals(compVo.getRiskCode())){
					sumBzPaid = sumBzPaid.add(itemVo.getBzPaidLoss()).add(itemVo.getBzPaidRescueFee());
					deductOffALL = deductOffALL.add(itemVo.getDeductOffAmt());
				}
			}
		}
		if(compVo.getPrpLLossPersons()!=null&&compVo.getPrpLLossPersons().size()>0){
			for(PrpLLossPersonVo itemVo:compVo.getPrpLLossPersons()){
				for(PrpLLossPersonFeeVo feeVo:itemVo.getPrpLLossPersonFees()){
					sumPaidAmt = sumPaidAmt.add(feeVo.getFeeRealPay());
					if(!"1101".equals(compVo.getRiskCode())){
						sumBzPaid = sumBzPaid.add(feeVo.getBzPaidLoss());
						deductOffALL = deductOffALL.add(feeVo.getDeductOffAmt());
					}
				}
			}
		}
		if(compVo.getPrpLCharges()!=null&&compVo.getPrpLCharges().size()>0){
			for(PrpLChargeVo feeVo:compVo.getPrpLCharges()){
				sumFee = sumFee.add(feeVo.getFeeAmt());
				sumPaidFee = sumPaidFee.add(feeVo.getFeeRealAmt());
				
			}
		}
		sumAmt = sumPaidAmt.add(sumPreAmt);
		compVo.setSumAmt(sumAmt);
		compVo.setSumPreAmt(sumPreAmt);
		compVo.setSumPaidAmt(sumPaidAmt);
		compVo.setSumFee(sumFee);
		compVo.setSumPreFee(sumPreFee);
		compVo.setSumPaidFee(sumPaidFee);
	}
	
	public void choosePayCustom(PrpLCompensateVo compVo){
		List<PrpLPayCustomVo> payCustList = payCustomService.findPayCustomVoByRegistNo(compVo.getRegistNo());
		List<PrpLPaymentVo> payments = new ArrayList<PrpLPaymentVo>();
		if(payCustList!=null&&payCustList.size()>0){
			PrpLPayCustomVo payCusVo = payCustList.get(0);
			PrpLPaymentVo paymentVo = new PrpLPaymentVo();
			paymentVo.setPayeeId(payCusVo.getId());
			paymentVo.setPayFlag(PayFlagType.COMPENSATE_PAY);
			paymentVo.setSerialNo("0");
			paymentVo.setPayeeName(payCusVo.getPayeeName());
			paymentVo.setOtherFlag(YN01.N);
			paymentVo.setSumRealPay(compVo.getSumPaidAmt());
			 //???????????????????????????+??????
		    PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(compVo.getRegistNo());
			paymentVo.setSummary(prpLRegistVo.getPrpLRegistExt().getLicenseNo()+"??????");
			if(!"2".equals(payCusVo.getPayObjectKind())){
			    paymentVo.setOtherFlag("1");
			    paymentVo.setOtherCause(payCusVo.getOtherCause());
			}
			payments.add(paymentVo);
			compVo.setPrpLPayments(payments);
		}
	}
	/**
	 * ???????????????????????????????????????????????????????????????????????????????????????
	 * <pre></pre> ????????????????????????false  ????????????????????????true
	 * @modified:
	 * ???WLL(2018???5???29??? ??????9:19:44): <br>
	 */
	public boolean adjustNotExistObj(String registNo){
		boolean NotExistObj = false;
		List<PrpLDlossCarMainVo> carMains = lossCarService.findLossCarMainBySerialNo(registNo,1);
		for(PrpLDlossCarMainVo car : carMains){
			if(!"01".equals(car.getLossState())&&"1".equals(car.getUnderWriteFlag())){//?????????????????????0
				if(BigDecimal.ZERO.compareTo(DataUtils.NullToZero(car.getSumVeriLossFee()))==0
						&&BigDecimal.ZERO.compareTo(DataUtils.NullToZero(car.getSumVeriRescueFee()))==0){
					NotExistObj = true;
					break;
				}
			}
		}
		if(!NotExistObj){
			List<PrpLCMainVo> cmainList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);
			List<String> kindCodeList = new ArrayList<String>();
			if(cmainList != null && cmainList.size() > 0 ){
				for(PrpLCMainVo cmain : cmainList){
					for(PrpLCItemKindVo item : cmain.getPrpCItemKinds()){
						kindCodeList.add(item.getKindCode());
					}
				}
			}
			boolean hasSelfKind = false;
			for(String kindCode : kindCodeList){//???????????????????????????
				if(CodeConstants.KindForSelfCar_List.contains(kindCode)){
					hasSelfKind = true;
					break;
				}
			}
			if(!hasSelfKind){
				NotExistObj = true;
			}
		}
		if(!NotExistObj){//???????????????????????????????????????????????????????????????????????????????????????????????????
			List<PrpLPayCustomVo> payCustList = payCustomService.findPayCustomVoByRegistNo(registNo);
			if(payCustList!=null 
					&& payCustList.size()==1
					&& !"2".equals(payCustList.get(0).getPayObjectKind())
					&& StringUtils.isBlank(payCustList.get(0).getOtherCause())){
				NotExistObj = true;
			}
		}
		return NotExistObj;
	}
	
}
