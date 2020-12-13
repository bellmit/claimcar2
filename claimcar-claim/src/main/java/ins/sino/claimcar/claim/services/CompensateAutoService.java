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
 * @author ★WeiLanlei
 */
/**
 * 自动理算服务类
 * <pre></pre>
 * @author ★WeiLanlei
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
	
	//先调用单证提交生成理算任务方法，生成理算工作流任务，再调用自动理算方法

	public PrpLCompensateVo autoCompensate(Double flowTaskId,SysUserVo user) throws Exception{
		PrpLCompensateVo compVo = null;
		user.setUserCode("AUTO");
		user.setUserName("AUTO");
		//初始化理算compVo，包含车辆损失，财产损失，人伤损失
		CompensateActionVo actionVo = compHandleService.compensateEdit(flowTaskId,user); 
		compVo = actionVo.getCompensateVo();//TODO 确认此处的主表是否有子表数据
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
		//预付垫付金额拆分到损失项-不考虑，预付垫付案件不允许自动理算
		if("1101".equals(compVo.getRiskCode())){
			//交强理算调用算法并合计
			Map<String, Object> resultMap = this.calculateCIAuto(compVo);
			//计算结果赋值comp主表
			this.processCalculateCIData(resultMap,compVo);
		}else{
			//获取条件免赔数据
			// 获取免赔条件
			List<PrpLClaimDeductVo> claimDeductVoList = compensateTaskService.findDeductCond(compVo.getRegistNo(), compVo.getRiskCode());
			//获取免赔率并赋值
			CompensateVo compensateVo = compHandleService.compensateGetAllRate(compVo,claimDeductVoList,
										compVo.getPrpLLossItems(),compVo.getPrpLLossProps(),compVo.getPrpLLossPersons());
			this.processGetRateBIData(compVo,compensateVo,claimDeductVoList);
			//计算定损扣交强
			Map<String, Object> resultMap = this.bIGetBZAmt(compVo);
			this.processBIGetBZAmtData(resultMap,compVo);
			//商业理算调用商业算法
			Map<String, Object> resultMapBI = this.calculateBIAuto(compVo,claimDeductVoList);
			this.processCalculateBIData(compVo,resultMapBI,actionVo.getDwPersFlag());
		}
		//费用赔付合计处理
		this.feeProcess(compVo);
		//合计计算结果赋值comp主表
		this.sumAllAmt(compVo);
		//自动选择生成收款人payment表数据方法
		this.choosePayCustom(compVo);
		//暂存数据
		String compensateNo = compensateService.saveCompensates(compVo,compVo.getPrpLLossItems(),compVo.getPrpLLossProps(),
											compVo.getPrpLLossPersons(),compVo.getPrpLCharges(),compVo.getPrpLPayments(),user);
		//自动组织提交数据，提交工作流，变为正在处理
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		if(!StringUtils.isBlank(compensateNo)){
			compHandleService.syncInsuredPhone(compVo.getRegistNo(),compVo.getinsuredPhone());
			if(StringUtils.isBlank(wfTaskVo.getCompensateNo())){
				wfTaskHandleService.tempSaveTaskByComp(wfTaskVo.getTaskId().doubleValue(),compVo.getCompensateNo(),user.getUserCode(),user.getComCode());
			}
		}else{
			throw new IllegalArgumentException("理算信息保存失败！<br/>");
		}
		
		return compVo;
	}
	
	//深度赋值
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
	 * 交强理算计算
	 * <pre></pre>
	 * @param prpLCompensate
	 * @return
	 * @modified:
	 * ☆WLL(2018年1月30日 上午10:40:37): <br>
	 */
	public Map<String, Object> calculateCIAuto(PrpLCompensateVo prpLCompensate){

		int noDutyNum = 0;//无责代赔车辆数
		List<PrpLLossItemVo> prpLLossItemVoListForCal = prpLCompensate.getPrpLLossItems();//计算组织数据用新的list，不影响原List
		List<PrpLLossItemVo> prpLLossItemVoListOri = new ArrayList<PrpLLossItemVo>();
		for(PrpLLossItemVo vo : prpLCompensate.getPrpLLossItems()){
			PrpLLossItemVo newVo = new PrpLLossItemVo();
			Beans.copy().from(vo).to(newVo);
			prpLLossItemVoListOri.add(newVo);
		}
		List<PrpLLossPropVo> prpLLossPropVoList = prpLCompensate.getPrpLLossProps();
		List<PrpLLossPersonVo> prpLLossPersonVoList = prpLCompensate.getPrpLLossPersons();
		List<PrpLLossItemVo> noDutyLossItemVoList = new ArrayList<PrpLLossItemVo>();//无责代赔赔付List
		Iterator<PrpLLossItemVo> iterator = prpLLossItemVoListForCal.iterator();
		while (iterator.hasNext()) {
			PrpLLossItemVo lossItemVo = iterator.next();
			if ("4".equals(lossItemVo.getPayFlag())) {
				noDutyNum++;// 无责赔付的才加1
				noDutyLossItemVoList.add(lossItemVo);
				iterator.remove();
			}
		}
		String flag = "1101".equals(prpLCompensate.getRiskCode()) ? "1" : "2";//交强-1商业-2标志位

		// 加上主车数据
		if (!"2".equals(prpLCompensate.getCaseType())) {
			List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(prpLCompensate.getRegistNo());
		
			if (lossCarMainVos != null && !lossCarMainVos.isEmpty()) {
				for (PrpLDlossCarMainVo carMainVo : lossCarMainVos) {
					if (carMainVo.getSerialNo() == 1
							&& compensateTaskService.checkLossState(carMainVo.getLossState(), CompensateType.PREFIX_CI)) {
						//李华威确认，无责代赔扣金额，组织标的车损失，不管控是否是车损险
						PrpLLossItemVo lossItemVo = compHandleService.processLossCarVoList(carMainVo, null,"BZ", flag);
						prpLLossItemVoListForCal.add(lossItemVo);
					}
					//无损失需要考虑施救费
					BigDecimal sumLossFee =  DataUtils.NullToZero(carMainVo.getSumVeriLossFee())
							.add(DataUtils.NullToZero(carMainVo.getSumVeriRescueFee()));
					// 三者车无损失 也要参与交强计算
					if (carMainVo.getSerialNo() > 1
							&& BigDecimal.ZERO.compareTo(sumLossFee)==0
							&& compensateTaskService.checkLossState(carMainVo.getLossState(), CompensateType.PREFIX_CI)) {
						PrpLLossItemVo lossItemVo = compHandleService.processLossCarVoList(carMainVo, null,"BZ", flag);
						prpLLossItemVoListForCal.add(lossItemVo);
					}
				}
			}
		
			// 增加主车财产损失
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
		
			// 增加主车人伤损失
			List<PrpLDlossPersTraceMainVo> reLossPersTraceMainVos = compensateTaskService.getValidLossPersTraceMain(
							prpLCompensate.getRegistNo(), CompensateType.COMP_CI);
			List<PrpLDlossPersTraceVo> lossPersTraceList = new ArrayList<PrpLDlossPersTraceVo>();
			if (reLossPersTraceMainVos != null) {
				// 遍历获取有效的人伤跟踪任务子表PrpLDlossPersTrace
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
		// compensateList返回3条数据，财(车损和财损)，医疗(人)，死亡伤残(人)
		
		List<ThirdPartyLossInfo> thirdPartyLossInfolist = new ArrayList<ThirdPartyLossInfo>(0);
		// 使用公共方法调整一分钱
		thirdPartyLossInfolist = compensateTaskService.getThirdPartyLossInfolistBz(compensateListVo, "compensate");
		
		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的Map，包括itemLossIdAndAmtMap和理算公式
		// Map 主键为DlossId或PersonId(医疗和死伤时)，值为对应的SumRealPay金额
		Map<String, Double> itemLossIdAndAmtMap = new HashMap<String, Double>();
		String compText = "";//理算计算公式
		for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfolist) {
			String key = "";
			if (CodeConstants.InsteadFlag.NODUTY_INSTEAD.equals(thirdPartyLossInfo.getInsteadFlag())) {
				key = "noduty=" + thirdPartyLossInfo.getSerialNo();
			} else {
				if ("car".equals(thirdPartyLossInfo.getExpType())) {
					key = "car=" + thirdPartyLossInfo.getSerialNo();// 此处的getLicenseNo中的值是DlossId
				} else if ("prop".equals(thirdPartyLossInfo.getExpType())) {
					key = "prop=" + thirdPartyLossInfo.getSerialNo();
				} else if ("person".equals(thirdPartyLossInfo.getExpType())) {
					if ("医疗费".equals(thirdPartyLossInfo.getLossFeeName())) {
						key = "med=" + thirdPartyLossInfo.getLossIndex();
					} else if ("死亡伤残".equals(thirdPartyLossInfo
							.getLossFeeName())) {
						key = "pers=" + thirdPartyLossInfo.getLossIndex();
					}
				}
			
			}
			if (itemLossIdAndAmtMap.size() > 0 && itemLossIdAndAmtMap.containsKey(key)) {
				// 将定损金额和施救费相加
				Double temp = itemLossIdAndAmtMap.get(key) + thirdPartyLossInfo.getSumRealPay();
				itemLossIdAndAmtMap.put(key, temp);
			} else {
				itemLossIdAndAmtMap.put(key,thirdPartyLossInfo.getSumRealPay());
			}
			if (thirdPartyLossInfo.getBzCompensateText() != null) {
		
				compText += thirdPartyLossInfo.getBzCompensateText();
			}
		}
		
		// 展示无责代赔的数据
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
			compText += "\r\n\r\n交强险 :无责代赔金额 = " + nodDutyLoss;
			}
		}

		logger.info("报案号" + (prpLCompensate == null ? null : prpLCompensate.getRegistNo()) + "交强自动理算开始进行每辆车剩余额度的保存");
		this.saveBzLeftAmount(compensateListVo.getLeftAmountMap(),prpLCompensate.getRegistNo(), CompensateType.COMP_CI);
		logger.info("报案号" + (prpLCompensate == null ? null : prpLCompensate.getRegistNo()) + "交强自动理算完成每辆车剩余额度的保存");
		resultMap.put("itemLossIdAndAmtMap", itemLossIdAndAmtMap);
		if (StringUtils.isBlank(compText)) {
			compText = "没有损失内容可赔付";
		}
		resultMap.put("compText", compText);
		logger.debug("===Map1===" + itemLossIdAndAmtMap.toString());
		prpLCompensate.setPrpLLossItems(prpLLossItemVoListOri);
		return resultMap;
		
	}
	/**
	 * 保存每辆车的剩余额度
	 * 
	 * @param leftAmountMap
	 * @param registNo
	 * @param calType
	 * @modified: ☆YangKun(2016年6月29日 下午3:41:10): <br>
	 */
	public void saveBzLeftAmount(Map<String, Double> leftAmountMap,String registNo, String calType) {
		logger.info("报案号" + registNo + "理算类型为 "+ calType + "进入保存每辆车的剩余保额方法(自动理算)" );
		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(registNo);
		checkDutyList = compensateTaskService.calcCheckDutyList(leftAmountMap,checkDutyList,calType);
		checkTaskService.saveCheckDutyList(checkDutyList);
		logger.info("报案号" + registNo + "理算类型为 "+ calType + "结束保存每辆车的剩余保额方法(自动理算)" );
	}
	/**
	 * 将交强理算计算结果赋值到损失项Vo中，做数据处理
	 * <pre></pre>
	 * @param resultMap
	 * @param compVo
	 * @return
	 * @modified:
	 * ☆WLL(2018年1月30日 上午10:41:08): <br>
	 */
	public PrpLCompensateVo processCalculateCIData(Map<String, Object> resultMap,PrpLCompensateVo compVo){
		String lcText = (String)resultMap.get("compText");
		BigDecimal sumPaidAmt = new BigDecimal(0);//理算当次总赔款
		BigDecimal sumPaidFee = new BigDecimal(0);//理算当次总费用

		BigDecimal offPreAmt = new BigDecimal(0);//合计人伤总预付、垫付金额

		compVo.setLcText(lcText);
		Map<String, Double> itemLossIdAndAmtMap = (Map<String,Double>)resultMap.get("itemLossIdAndAmtMap");
		Map<String,BigDecimal> sumRealPays = new HashMap<String,BigDecimal>();
		Map<String,BigDecimal> sumlossPays = new HashMap<String,BigDecimal>();
		for (Map.Entry<String, Double> entry : itemLossIdAndAmtMap.entrySet()) { 
			String strKey[]=entry.getKey().split("=");		
			if("noduty".equals(strKey[0])){//无责代赔赔款赋值
				if(compVo.getPrpLLossItems()!=null && compVo.getPrpLLossItems().size() > 0){
					for(PrpLLossItemVo itemVo : compVo.getPrpLLossItems()){
						if(strKey[1].equals(itemVo.getItemId()) && "4".equals(itemVo.getPayFlag()) ){
							itemVo.setSumRealPay(new BigDecimal(entry.getValue()));
							sumPaidAmt = sumPaidAmt.add(itemVo.getSumRealPay());
						}
					}
				}
			}else if("car".equals(strKey[0])){//车辆赔付赋值
				if(compVo.getPrpLLossItems()!=null && compVo.getPrpLLossItems().size() > 0){
					for(PrpLLossItemVo itemVo : compVo.getPrpLLossItems()){
						if(strKey[1].equals(itemVo.getItemId()) && !"4".equals(itemVo.getPayFlag()) ){
							itemVo.setSumRealPay(new BigDecimal(entry.getValue()));
							sumPaidAmt = sumPaidAmt.add(itemVo.getSumRealPay());
						}
						if("2".equals(itemVo.getPayFlag())){//清付
							if(itemVo.getPlatLockList()!=null && itemVo.getPlatLockList().size() > 0){
								for(PrpLPlatLockVo lockVo : itemVo.getPlatLockList()){
									lockVo.setThisPaid(new BigDecimal(entry.getValue()));
								}
							}
							
						}
					}
				}

			}else if("prop".equals(strKey[0])){//财产赔付赋值
				if(compVo.getPrpLLossProps()!=null && compVo.getPrpLLossProps().size() > 0){
					for(PrpLLossPropVo itemVo : compVo.getPrpLLossProps()){
						if(strKey[1].equals(itemVo.getItemId())){
							itemVo.setSumRealPay(new BigDecimal(entry.getValue()));
							sumPaidAmt = sumPaidAmt.add(itemVo.getSumRealPay());
						}
					}
				}
			}else{//人伤
				if((!sumlossPays.isEmpty() && !sumlossPays.containsKey(strKey[1])) || (sumlossPays.isEmpty())){
						BigDecimal sumlossPay = new BigDecimal(0);//合计人伤总定损金额
						sumlossPays.put(strKey[1],sumlossPay);
				}
				if((!sumRealPays.isEmpty() && !sumRealPays.containsKey(strKey[1])) || (sumRealPays.isEmpty())){
					BigDecimal perSumRealPay = BigDecimal.ZERO;
					sumRealPays.put(strKey[1],perSumRealPay);
				}
				BigDecimal sumRealPay = sumRealPays.get(strKey[1]);
				BigDecimal sumLoss = sumlossPays.get(strKey[1]);//合计人伤总定损金额
				String typeVal = "00";//费用类型
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
							//遍历完后合计人伤主表的总金额
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
				sumPaidFee = sumPaidFee.add(chargVo.getFeeAmt());//自动理算不需考虑预付垫付和其他扣减
			}
		}
		//金额赋值完毕后汇总赋值给主表
		compVo.setSumAmt(sumPaidAmt);
		compVo.setSumPreAmt(BigDecimal.ZERO);//预付垫付案件不自动理算
		compVo.setSumPaidAmt(sumPaidAmt);
		compVo.setSumFee(sumPaidFee);
		compVo.setSumPreFee(BigDecimal.ZERO);
		compVo.setSumPaidFee(sumPaidFee);
		
		return compVo;
	}
	/**
	 * 赋值责任比例/免赔率到各个损失项
	 * <pre></pre>
	 * @param prpLCompensateVo
	 * @param compensateVo
	 * @modified:
	 * ☆WLL(2018年1月30日 下午2:56:54): <br>
	 */
	public void processGetRateBIData(PrpLCompensateVo prpLCompensateVo,CompensateVo compensateVo,List<PrpLClaimDeductVo> claimDeductVoList){
		if(compensateVo != null && prpLCompensateVo != null){
			boolean noFindThirdFlag = false;//无法找到第三方加扣免赔勾选标志位			
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
			                logger.info("未勾选无法找到第三方 并且带出了附加险无法找到第三方 需要删除"); 
			                //TODO 已确认此处删除是否就没保存了
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
	 * 调用扣交强计算方法
	 * <pre></pre>
	 * @param compeVo
	 * @modified:
	 * ☆WLL(2018年2月1日 上午9:59:58): <br>
	 */
	public Map<String, Object> bIGetBZAmt(PrpLCompensateVo compVo){
		//原理算书实体不可变更，复制一个compVo用于交强计算
		PrpLCompensateVo compeVoForBZAmt = Beans.copyDepth().from(compVo).to(PrpLCompensateVo.class);
		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的Map，包括itemLossIdAndAmtMap和理算公式
		
		List<ThirdPartyLossInfo> thirdPartyLossInfolist = new ArrayList<ThirdPartyLossInfo>(0);
		Iterator<PrpLLossItemVo> iterator = compeVoForBZAmt.getPrpLLossItems().iterator();
		while (iterator.hasNext()) {
			PrpLLossItemVo lossItemVo = iterator.next();
			if (!(CodeConstants.KINDCODE.KINDCODE_A.equals(lossItemVo.getKindCode()) 
					|| CodeConstants.KINDCODE.KINDCODE_B.equals(lossItemVo.getKindCode()))) {
				// 车辆只有 A和B条款需要扣交强
				iterator.remove();//TODO 确认此处是否可以移除主表中子表list实体
			}
		}
		CompensateListVo compensateListVo = compensateTaskService.calCulator(
				compeVoForBZAmt, compeVoForBZAmt.getPrpLLossItems(), compeVoForBZAmt.getPrpLLossProps(),
				compeVoForBZAmt.getPrpLLossPersons(),CodeConstants.CompensateKind.BI_COMPENSATE);
		// 使用公共方法调整一分钱
		thirdPartyLossInfolist = compensateTaskService.getThirdPartyLossInfolistBz(compensateListVo, "compensate");
			
		// Map 主键为DlossId或PersonId(医疗和死伤时)，值为对应的SumRealPay金额
		Map<String, Double> itemLossIdMap = new HashMap<String, Double>();// 定损扣交强
		Map<String, Double> itemAmtMap = new HashMap<String, Double>();// 施救费扣交强
		Map<String, Double> NoDutyCarPay = new HashMap<String, Double>();// 无责代赔金额
		String compText = "";

		for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfolist) {
			String key = "";
			if ("car".equals(thirdPartyLossInfo.getExpType())) {
				key = "car=" + thirdPartyLossInfo.getLicenseNo();// 此处的getLicenseNo中的值是DlossId
			} else if ("prop".equals(thirdPartyLossInfo.getExpType())) {
				key = "prop=" + thirdPartyLossInfo.getLicenseNo();
			} else if ("person".equals(thirdPartyLossInfo.getExpType())) {
				if ("医疗费".equals(thirdPartyLossInfo.getLossFeeName())) {
					key = "med=" + thirdPartyLossInfo.getLossIndex();
				} else if ("死亡伤残".equals(thirdPartyLossInfo
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
				// 定损扣交强
				if (itemLossIdMap.containsKey(key)) {
					Double temp1 = itemLossIdMap.get(key) + realPay;
					itemLossIdMap.put(key, temp1);
				} else {
					itemLossIdMap.put(key, realPay);
				}
				// 施救费扣交强
				if (itemAmtMap.containsKey(key)) {
					Double temp1 = itemAmtMap.get(key) + rescueFeePay;
					itemAmtMap.put(key, temp1);
				} else {
					itemAmtMap.put(key, rescueFeePay);
				}

			} else {// 人伤  定损扣交强
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
		logger.info("报案号" + (compVo == null ? null : compVo.getRegistNo()) + "定损扣交强开始进行每辆车剩余额度的保存");
		this.saveBzLeftAmount(compensateListVo.getLeftAmountMap(),
				compVo.getRegistNo(), CompensateType.COMP_BI);
		logger.info("报案号" + (compVo == null ? null : compVo.getRegistNo()) + "定损扣交强完成每辆车剩余额度的保存");
		resultMap.put("itemLossIdMap", itemLossIdMap);
		resultMap.put("itemAmtMap", itemAmtMap);
		resultMap.put("NoDutyCarPay", NoDutyCarPay);
		resultMap.put("compText", compText);
		return resultMap;
	}
	/**
	 * 复制商业理算书损失项的扣交强金额
	 * <pre></pre>
	 * @param resultMap
	 * @param compVo
	 * @modified:
	 * ☆WLL(2018年2月1日 下午12:00:20): <br>
	 */
	public void processBIGetBZAmtData(Map<String, Object> resultMap,PrpLCompensateVo compVo){
		Map<String, Double> itemLossIdMap = (Map<String,Double>)resultMap.get("itemLossIdMap");// 定损扣交强
		Map<String, Double> itemAmtMap = (Map<String,Double>)resultMap.get("itemAmtMap");// 施救费扣交强
		Map<String, Double> NoDutyCarPay = (Map<String,Double>)resultMap.get("NoDutyCarPay");// 无责代赔金额
		if(compVo.getPrpLLossItems()!=null&&compVo.getPrpLLossItems().size()>0){
			for(PrpLLossItemVo itemVo:compVo.getPrpLLossItems()){
				itemVo.setBzPaidLoss(BigDecimal.ZERO);//赋初始值
				itemVo.setBzPaidRescueFee(BigDecimal.ZERO);
				itemVo.setAbsolvePayAmt(BigDecimal.ZERO);
				itemVo.setAbsolvePayAmts(BigDecimal.ZERO);
				if(KINDCODE.KINDCODE_A.equals(itemVo.getKindCode())
						||KINDCODE.KINDCODE_B.equals(itemVo.getKindCode())){
					if(itemLossIdMap.containsKey("car="+itemVo.getDlossId())){//定损扣交强
						itemVo.setBzPaidLoss(new BigDecimal(itemLossIdMap.get("car="+itemVo.getDlossId())));
					}
					if(itemAmtMap.containsKey("car="+itemVo.getDlossId())){//施救费扣交强
						itemVo.setBzPaidRescueFee(new BigDecimal(itemAmtMap.get("car="+itemVo.getDlossId())));
					}
				}
				if(KINDCODE.KINDCODE_A.equals(itemVo.getKindCode())){//绝对免赔
					if(NoDutyCarPay.containsKey("noduty="+itemVo.getItemId())){//serialNo
						itemVo.setAbsolvePayAmt(new BigDecimal(NoDutyCarPay.get("noduty="+itemVo.getItemId())));
						itemVo.setAbsolvePayAmts(new BigDecimal(NoDutyCarPay.get("noduty="+itemVo.getItemId())));
					}
				}
			}
			
		}
		if(compVo.getPrpLLossProps()!=null&&compVo.getPrpLLossProps().size()>0){
			for(PrpLLossPropVo itemVo:compVo.getPrpLLossProps()){
				itemVo.setBzPaidLoss(BigDecimal.ZERO);//赋初始值
				itemVo.setBzPaidRescueFee(BigDecimal.ZERO);
				if(itemLossIdMap.containsKey("prop="+itemVo.getDlossId())){//定损扣交强
					itemVo.setBzPaidLoss(new BigDecimal(itemLossIdMap.get("prop="+itemVo.getDlossId())));
				}
				if(itemAmtMap.containsKey("prop="+itemVo.getDlossId())){//施救费扣交强
					itemVo.setBzPaidRescueFee(new BigDecimal(itemAmtMap.get("prop="+itemVo.getDlossId())));
				}
			}
		}
		if(compVo.getPrpLLossPersons()!=null&&compVo.getPrpLLossPersons().size()>0){
			for(PrpLLossPersonVo itemVo:compVo.getPrpLLossPersons()){
				BigDecimal sumBzPaidLoss = BigDecimal.ZERO;
				for(PrpLLossPersonFeeVo feeVo:itemVo.getPrpLLossPersonFees()){
					if(itemLossIdMap.containsKey("med="+itemVo.getPersonId())
							&& "03".equals(feeVo.getLossItemNo())){//定损扣交强 医疗
						feeVo.setBzPaidLoss(new BigDecimal(itemLossIdMap.get("med="+itemVo.getPersonId())));
						sumBzPaidLoss = sumBzPaidLoss.add(feeVo.getBzPaidLoss());
					}
					if(itemLossIdMap.containsKey("pers="+itemVo.getPersonId())
							&& "02".equals(feeVo.getLossItemNo())){//定损扣交强 死伤
						feeVo.setBzPaidLoss(new BigDecimal(itemLossIdMap.get("pers="+itemVo.getPersonId())));
						sumBzPaidLoss = sumBzPaidLoss.add(feeVo.getBzPaidLoss());
					}
				}
				itemVo.setBzPaidLoss(sumBzPaidLoss);//赋初始值
			}
		}
	}
	/**
	 * 调用商业理算算法
	 * <pre></pre>
	 * @param compVo（包含子表VoList）
	 * @param claimDeductVoList
	 * @return
	 * @modified:
	 * ☆WLL(2018年2月5日 上午10:40:17): <br>
	 */
	public Map<String, Object>  calculateBIAuto(PrpLCompensateVo compVo,List<PrpLClaimDeductVo> claimDeductVoList){
		Map<String, Object> resultMap = new HashMap<String,Object>();// 返回的Map，包括itemLossIdAndAmtMap和理算公式
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
		Map<String, Double> deductOffMap = new HashMap<String, Double>();// 不计免赔
		String key = "";
		PrpLCompensateVo compensateVo = compensateResult.getPrpLCompensateVo();
		List<PrpLLossItemVo> prpLLossItemVos = compensateResult.getPrpLLossItemVoList();// car
		List<PrpLLossPropVo> PrpLLossPropVos = compensateResult.getPrpLLossPropVoList();// 财
		List<PrpLLossPersonVo> PrpLLossPersonVos = compensateResult.getPrpLLossPersonVoList();// 人

		for (PrpLLossItemVo prpLLossItemVo : prpLLossItemVos) {
			if (CodeConstants.KINDCODE.KINDCODE_A.equals(prpLLossItemVo.getKindCode())
					|| CodeConstants.KINDCODE.KINDCODE_B.equals(prpLLossItemVo.getKindCode())) {
				if (CodeConstants.PayFlagType.INSTEAD_PAY.equals(prpLLossItemVo.getPayFlag())) {// 追偿
					key = "recovery=" + prpLLossItemVo.getItemId();
					// 核定金额Map
					itemLossIdAndAmtMap.put(key, prpLLossItemVo.getSumRealPay().doubleValue());
					// 不计免赔Map
					deductOffMap.put(key, prpLLossItemVo.getDeductOffAmt().doubleValue());
				} else {
					key = "car=" + prpLLossItemVo.getItemId();
					if (itemLossIdAndAmtMap.containsKey(key)) {
						// 将定损金额和施救费相加
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
					// 将定损金额和施救费相加
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
				// 将定损金额和施救费相加
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
					// 将定损金额和施救费相加
					Double temp = itemLossIdAndAmtMap.get(key) + prpLLossPersonFeeVo.getFeeRealPay().doubleValue();
					itemLossIdAndAmtMap.put(key, temp);
				} else {
					if (prpLLossPersonFeeVo.getFeeRealPay() != null) {// 如果为空就先写死
						itemLossIdAndAmtMap.put(key, prpLLossPersonFeeVo.getFeeRealPay().doubleValue());
					} else {
						itemLossIdAndAmtMap.put(key, (double) 0);
					}
				}

				if (deductOffMap.containsKey(key)) {
					Double temp = deductOffMap.get(key) + prpLLossPersonFeeVo.getDeductOffAmt().doubleValue();
					itemLossIdAndAmtMap.put(key, temp);
				} else {
					if (prpLLossPersonFeeVo.getFeeRealPay() != null) {// 如果为空就先写死
						deductOffMap.put(key, prpLLossPersonFeeVo.getDeductOffAmt().doubleValue());
					} else {
						deductOffMap.put(key, (double) 0);
					}
				}
			}

		}
		// 试用截取字符串
		String compText = compensateVo.getLcText();
		if (StringUtils.isBlank(compText)) {
			compText = "没有损失内容可赔付";
		}
		resultMap.put("compText", compText);
		resultMap.put("itemLossIdAndAmtMap", itemLossIdAndAmtMap);
		resultMap.put("deductOffMap", deductOffMap);
		return resultMap;
	}
	/**
	 * 处理商业核定金额计算结果赋值
	 * <pre></pre>
	 * @param compVo
	 * @param resultMap
	 * @param dwPersFlag 非机动车代位标志
	 * @modified:
	 * ☆WLL(2018年2月5日 下午5:49:29): <br>
	 */
	public void processCalculateBIData(PrpLCompensateVo compVo,Map<String, Object> resultMap,String dwPersFlag){
		Map<String, Double> itemLossIdAndAmtMap = (Map<String,Double>)resultMap.get("itemLossIdAndAmtMap");//核定金额
		Map<String, Double> deductOffMap = (Map<String,Double>)resultMap.get("deductOffMap");// 不计免赔
		String compText = (String)resultMap.get("compText");
		compVo.setLcText(compText);
		if(compVo.getPrpLLossItems()!=null&&compVo.getPrpLLossItems().size()>0){
			for(PrpLLossItemVo itemVo:compVo.getPrpLLossItems()){
				if("4".equals(itemVo.getPayFlag())){//无责代赔
					if(itemLossIdAndAmtMap.containsKey("noduty="+itemVo.getItemId())){
						BigDecimal sumRealPay = new BigDecimal(itemLossIdAndAmtMap.get("noduty="+itemVo.getItemId()));
						BigDecimal deductOffAmt = new BigDecimal(deductOffMap.get("noduty="+itemVo.getItemId()));
						itemVo.setSumRealPay(sumRealPay.add(deductOffAmt));//实赔加上不计免赔
						itemVo.setDeductOffAmt(deductOffAmt);
					}
				}else if("1".equals(itemVo.getPayFlag())){//追偿
					if(itemLossIdAndAmtMap.containsKey("recovery="+itemVo.getItemId())){
						BigDecimal sumRealPay = new BigDecimal(itemLossIdAndAmtMap.get("recovery="+itemVo.getItemId()));
						BigDecimal deductOffAmt = new BigDecimal(deductOffMap.get("recovery="+itemVo.getItemId()));
						itemVo.setSumRealPay(sumRealPay);
						itemVo.setDeductOffAmt(deductOffAmt);
						if("1".equals(dwPersFlag)){
							//非机动车代位明细赋值
							itemVo.getPlatLockList().get(0).setThisPaid(sumRealPay);
						}
					}
				}else{
					if(!KINDCODE.KINDCODE_A.equals(itemVo.getKindCode())//非主险
							&&!KINDCODE.KINDCODE_B.equals(itemVo.getKindCode())){
						if(itemLossIdAndAmtMap.containsKey("car="+itemVo.getKindCode())){
							BigDecimal sumRealPay = new BigDecimal(itemLossIdAndAmtMap.get("car="+itemVo.getKindCode()));
							BigDecimal deductOffAmt = new BigDecimal(deductOffMap.get("car="+itemVo.getKindCode()));
							itemVo.setSumRealPay(sumRealPay.add(deductOffAmt));//实赔加上不计免赔
							itemVo.setDeductOffAmt(deductOffAmt);
						}
					}else{
						//车辆考虑有责和无责方
						if("3".equals(itemVo.getPayFlag()) || "2".equals(itemVo.getPayFlag())){//自付和清付
							if(itemLossIdAndAmtMap.containsKey("car="+itemVo.getItemId())){
								BigDecimal sumRealPay = new BigDecimal(itemLossIdAndAmtMap.get("car="+itemVo.getItemId()));
								BigDecimal deductOffAmt = new BigDecimal(deductOffMap.get("car="+itemVo.getItemId()));
								itemVo.setSumRealPay(sumRealPay.add(deductOffAmt));//实赔加上不计免赔
								itemVo.setDeductOffAmt(deductOffAmt);
								if("2".equals(itemVo.getPayFlag())){//清付
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
				itemVo.setSumRealPay(sumRealPay.add(deductOffAmt));//实赔加上不计免赔
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
						feeVo.setFeeRealPay(feeRealPay.add(deductOffAmt));//实赔加上不计免赔
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
	 * 处理费用赔付金额赋值
	 * <pre></pre>
	 * @param compVo
	 * @modified:
	 * ☆WLL(2018年2月5日 下午6:35:27): <br>
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
	 * 合计各项总金额赋值到理算主表
	 * <pre></pre>
	 * @param compVo
	 * @modified:
	 * ☆WLL(2018年2月6日 下午3:22:25): <br>
	 */
	public void sumAllAmt(PrpLCompensateVo compVo){
		
		BigDecimal sumAmt = BigDecimal.ZERO;//总赔款金额
		BigDecimal sumPreAmt = BigDecimal.ZERO;//预付赔款-自动理算无预付垫付
		BigDecimal sumPaidAmt = BigDecimal.ZERO;//本次赔款金额
		BigDecimal sumFee = BigDecimal.ZERO;//总费用金额
		BigDecimal sumPreFee = BigDecimal.ZERO;//预付费用-自动理算无预付垫付
		BigDecimal sumPaidFee = BigDecimal.ZERO;//本次赔付费用
		BigDecimal deductOffALL = BigDecimal.ZERO;//不计免赔金额
		BigDecimal sumBzPaid = BigDecimal.ZERO;//扣交强总金额
		
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
			 //摘要取标的车车牌号+赔款
		    PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(compVo.getRegistNo());
			paymentVo.setSummary(prpLRegistVo.getPrpLRegistExt().getLicenseNo()+"赔款");
			if(!"2".equals(payCusVo.getPayObjectKind())){
			    paymentVo.setOtherFlag("1");
			    paymentVo.setOtherCause(payCusVo.getOtherCause());
			}
			payments.add(paymentVo);
			compVo.setPrpLPayments(payments);
		}
	}
	/**
	 * 判断商业理算时，是否不存在，标的车无损失或无赔付标的的险别
	 * <pre></pre> 有标的车损失返回false  没标的车损失返回true
	 * @modified:
	 * ☆WLL(2018年5月29日 上午9:19:44): <br>
	 */
	public boolean adjustNotExistObj(String registNo){
		boolean NotExistObj = false;
		List<PrpLDlossCarMainVo> carMains = lossCarService.findLossCarMainBySerialNo(registNo,1);
		for(PrpLDlossCarMainVo car : carMains){
			if(!"01".equals(car.getLossState())&&"1".equals(car.getUnderWriteFlag())){//标的车损失不为0
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
			for(String kindCode : kindCodeList){//标的车有险别可赔付
				if(CodeConstants.KindForSelfCar_List.contains(kindCode)){
					hasSelfKind = true;
					break;
				}
			}
			if(!hasSelfKind){
				NotExistObj = true;
			}
		}
		if(!NotExistObj){//如果录入的收款人不是被保险人，且收款人例外原因为空，不允许自动理算
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
