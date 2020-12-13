package ins.sino.claimcar.claim.calculator;

import ins.framework.common.DateTime;
import ins.framework.exception.BusinessException;
import ins.platform.utils.DataUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.KINDCODE;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.CalculatorService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateExtVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.ThirdPartyDepreRate;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarSubRiskVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("calculatorService")
public class CalculatorServiceSpringImpl implements CalculatorService {

	private final Log logger = LogFactory.getLog(CalculatorServiceSpringImpl.class);
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private PersTraceDubboService persTraceDubboService;
	@Autowired
	private LossCarService lossCarService;// 车辆定损
	@Autowired
	private PropTaskService propTaskService;// 财产定损
	@Autowired
	private CompensateTaskService compensateTaskService;// 理算
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private CompensateService compensateService;
	@Autowired
	private ScheduleService scheduleService;
	
	/**
	 * 核损通过标志List
	 */
	public static List<String> UnderWritePassList = new ArrayList<String>();
	static{
		UnderWritePassList.add(CodeConstants.VeriFlag.PASS);
		UnderWritePassList.add(CodeConstants.VeriFlag.AUTOPASS);
	}
	

	public CompensateVo orgnizeCalculateData(PrpLRegistVo prpLregistVo, PrpLCMainVo prpLcMainVo, PrpLCheckVo prpLCheckVo, PrpLClaimVo prpLClaimVo, String compensateType) {
		CompensateVo compensateVo = new CompensateVo();
		compensateVo.setCalculateType("claim");
		PrpLCompensateVo prpLcompensateVo = new PrpLCompensateVo();// 虚拟计算书prpLcompensate
		PrpLCompensateExtVo extVo = new PrpLCompensateExtVo();
		prpLcompensateVo.setPrpLCompensateExt(extVo);
		// ThirdPartyDepreRate thirdPartyDepreRate = new ThirdPartyDepreRate();// 新车购置价对象
		PrpLCItemCarVo prpLCItemCarVo = prpLcMainVo.getPrpCItemCars().get(0);
		if (prpLCItemCarVo != null && prpLCItemCarVo.getClauseType() != null) {
			compensateVo.setClauseType(prpLCItemCarVo.getClauseType());// 条款代码
		}
		compensateVo.setOperateDate(prpLcMainVo.getOperateDate());// 承保日期
		compensateVo.setRiskCode(prpLcMainVo.getRiskCode());// 险种代码
		compensateVo.setPrpLCItemKindVoList(prpLcMainVo.getPrpCItemKinds());// 承保信息
		
		// 是否为新条款
		boolean clauseIssue = false;
		if(!"1101".equals(prpLcMainVo.getRiskCode().trim())){
			clauseIssue = CodeConstants.ISNEWCLAUSECODE_MAP.get(prpLcMainVo.getRiskCode().trim());
		}
		BigDecimal amountKindA = BigDecimal.ZERO;
		//免赔金额  如果是新条款直接从A条款的Deductible取值，如果是旧条款则从M1条款的Value取值
		BigDecimal deductibleValue = BigDecimal.ZERO ;
		if(compensateVo.getPrpLCItemKindVoList() != null && !compensateVo.getPrpLCItemKindVoList().isEmpty()){
			for(PrpLCItemKindVo prpLCItemKindVo : compensateVo.getPrpLCItemKindVoList()){
				if(CodeConstants.KINDCODE_A_LIST.contains(prpLCItemKindVo.getKindCode().trim())){
					amountKindA = prpLCItemKindVo.getAmount();
					compensateVo.setAmountKindA(amountKindA);
					deductibleValue = prpLCItemKindVo.getDeductible();
					break;
				}
			}
			for(PrpLCItemKindVo prpLCItemKindVo : compensateVo.getPrpLCItemKindVoList()){
				if(CodeConstants.KINDCODE.KINDCODE_X1.equals(prpLCItemKindVo.getKindCode().trim()) ||
						CodeConstants.KINDCODE.KINDCODE_X2.equals(prpLCItemKindVo.getKindCode().trim()) ||
						CodeConstants.KINDCODE.KINDCODE_F.equals(prpLCItemKindVo.getKindCode().trim()) ||
							CodeConstants.KINDCODE.KINDCODE_NT.equals(prpLCItemKindVo.getKindCode().trim())){
					prpLCItemKindVo.setAmount(amountKindA);
				}
				
				if(!clauseIssue){
					if(CodeConstants.KINDCODE.KINDCODE_M1.equals(prpLCItemKindVo.getKindCode().trim())){
						deductibleValue = prpLCItemKindVo.getValue();
					}
				}
			}
		}
			
		compensateVo.setDeductibleValue(deductibleValue);

		if (prpLClaimVo.getClaimType() != null) {
			logger.debug("报案号："+prpLClaimVo.getRegistNo()+"的赔案类别为"+prpLClaimVo.getClaimType());

			prpLcompensateVo.setHandler1Code(prpLcMainVo.getHandlerCode());
			// prpLcompensate.setLflag(prpLClaimVo.getLflag()); //prpLClaimVo理赔类型
			prpLcompensateVo.setRegistNo(prpLregistVo.getRegistNo()); // 报案号
			prpLcompensateVo.setPolicyNo(prpLClaimVo.getPolicyNo()); // 保单号
			prpLcompensateVo.setClaimNo(prpLClaimVo.getClaimNo()); // 立案号
			prpLcompensateVo.setCaseType(prpLClaimVo.getClaimType());// 赔案类别
			prpLcompensateVo.setComCode(prpLClaimVo.getComCode()); // 业务归属机构
			prpLcompensateVo.setRiskCode(prpLClaimVo.getRiskCode()); // 险种代码
			// 事故责任比例和事故责任类型取标的车的checkduty表
			PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(prpLClaimVo.getRegistNo(),1);
			prpLcompensateVo.setIndemnityDutyRate(checkDutyVo.getIndemnityDutyRate()); // 责任比例
			prpLcompensateVo.setIndemnityDuty(checkDutyVo.getIndemnityDuty()); // 事故责任类型

			prpLcompensateVo.setCurrency(prpLcMainVo.getCurrency()); // 币别代码
			// prpLcompensateVo.setClassCode(prpLcMainVo.getClassCode()); // 险类代码
			prpLcompensateVo.setCompensateKind(compensateType); // 计算书类型
			prpLcompensateVo.setUnderwriteFlag("0"); // 设置核赔标识为默认问题
			// 初始化理算主表的标志位时，空出的位数
			prpLcompensateVo.setFlag(space(5) + "21");
			// 一般赔案
			// 组织车辆损失信息
			compensateVo = getPrpLlosses(compensateVo, prpLCheckVo, prpLcMainVo, compensateType, prpLClaimVo);
			prpLcompensateVo.setPrpLLossItems(compensateVo.getPrpLLossItemVoList());
			// // 组织财产损失信息
			compensateVo = getPrpLpropLosses(compensateVo, prpLCheckVo, prpLcMainVo, compensateType, prpLClaimVo);
			prpLcompensateVo.setPrpLLossProps(compensateVo.getPrpLLossPropVoList());
			// // 组织人伤损失信息
			compensateVo = getPrpLpersonItems(compensateVo, prpLCheckVo, prpLcMainVo, compensateType, prpLClaimVo);
			prpLcompensateVo.setPrpLLossPersons(compensateVo.getPrpLLossPersonVoList());
			compensateVo.setPrpLLossItemVoList(prpLcompensateVo.getPrpLLossItems());
			compensateVo.setPrpLCompensateVo(prpLcompensateVo);
			compensateVo.setPrpLCMainVo(prpLcMainVo);
			
			// 商业险增加免赔条件
			if(CodeConstants.CompensateKind.BI_COMPENSATE.equals(compensateType)){
				// 计算车辆实际价值
				ThirdPartyDepreRate thirdPartyDepreRate = compensateService.queryThirdPartyDepreRate(prpLregistVo.getRegistNo());
				compensateVo.setThirdPartyDepreRate(thirdPartyDepreRate);// 新车购置价对象
				
				List<PrpLClaimDeductVo> claimDeductList = registQueryService.findIsCheckClaimDeducts(prpLregistVo.getRegistNo());
				
				//eMotorMap kindPaidMap
				Map<String,BigDecimal> eMotorMap = compensateService.queryEmotorMap(prpLcMainVo.getPolicyNo());
				Map<String,BigDecimal> kindPaidMap = compensateService.querySumRealPay(prpLClaimVo.getClaimNo(),prpLcompensateVo.getCompensateNo());
				
				compensateVo.setClaimDeductList(claimDeductList);
				compensateVo.seteMotorMap(eMotorMap);
				compensateVo.setKindPaidMap(kindPaidMap);
				
				//核赔通过的计算书 TODO
				boolean firstCompFlag = true;//第一张 商业/交强计算书
				List<PrpLCompensateVo> compensateList = compensateService.findCompensateByClaimno(prpLClaimVo.getClaimNo(),"N");
				for(PrpLCompensateVo compVo : compensateList){
					PrpLCompensateExtVo compExtVo = compVo.getPrpLCompensateExt();
					if(compExtVo.getWriteOffFlag()==null ||"0".equals(compExtVo.getWriteOffFlag())){//不是冲销的案件
						if(compExtVo.getOppoCompensateNo() == null){
							firstCompFlag = false;
							break;
						}
					}	
				}
				
				if(firstCompFlag){
					boolean deductibleFlag = false;
					for(PrpLCompensateVo compVo : compensateList){
						List<PrpLLossItemVo> lossItemList = compVo.getPrpLLossItems();
						List<PrpLLossPropVo> propList = compVo.getPrpLLossProps();
						
						if(lossItemList!=null && !lossItemList.isEmpty()){
							for(PrpLLossItemVo lossItem : lossItemList){
								if(CodeConstants.KINDCODE_A_LIST.contains(lossItem.getKindCode())){
									deductibleFlag = true;
								}
							}
						}
						
						if(!deductibleFlag && lossItemList!=null && !lossItemList.isEmpty()){
							for(PrpLLossPropVo propVo : propList){
								if("9".equals(propVo.getPropType()) 
										&& CodeConstants.KINDCODE.KINDCODE_X.equals(propVo.getKindCode())){
									deductibleFlag = true;
								}
							}
						}
						compensateVo.setDeductibleFlag(deductibleFlag);
					}
				}
			}
		} else {
			logger.debug("未组织赔案类别，报案号："+prpLClaimVo.getRegistNo());
			throw new BusinessException("报案号："+prpLClaimVo.getRegistNo()+"没有赔案类别,请到查勘页面填写！",false);
		}
		return compensateVo;
	}

	/**
	 * 获取定损车辆的各项费用
	 * @param prpLloss
	 * @param prpLdeflossMain
	 * @param configValue
	 * @return
	 */
	public PrpLLossItemVo setSumLossValues(PrpLLossItemVo prpLLossItemVo, PrpLDlossCarMainVo prpLDlossCarMainVo) {
		// 总金额
		prpLLossItemVo.setSumLoss(prpLDlossCarMainVo.getSumVeriLossFee().setScale(2, BigDecimal.ROUND_HALF_UP));
		// 施救费
		prpLLossItemVo.setRescueFee(prpLDlossCarMainVo.getSumVeriRescueFee().setScale(2,BigDecimal.ROUND_HALF_UP));// 施救费
		return prpLLossItemVo;
	}

	/**
	 * 组织车辆损失信息
	 * @param compensateVo
	 * @param prpLcheck
	 * @param prpLcMain
	 * @param compensateType
	 * @param prpLclaim
	 * @return
	 */
	public CompensateVo getPrpLlosses(CompensateVo compensateVo, PrpLCheckVo prpLCheckVo, PrpLCMainVo prpLCMainVo,
			String compensateType, PrpLClaimVo prpLClaimVo) {
		String message = "";
		boolean bzDutyCar = false;
		List<String> prpLcheckCarIdList = new ArrayList<String>(0);// 防止查勘与定损重复或覆盖
		List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(prpLClaimVo.getRegistNo());
		if (CodeConstants.CompensateKind.CI_COMPENSATE.equals(compensateType)) {
			// 交强险计算书
			bzDutyCar = this.checkBzDutyCar(prpLClaimVo, bzDutyCar);
			// 2008年2月1日后，交强险，如果本车无责，不初始化出车，财损失的数据
			if (prpLClaimVo.getCiIndemDuty() == null) {
				throw new BusinessException(
						"报案号："+prpLClaimVo.getRegistNo()+" 的【交强责任类型PrpLclaim.CiIndemDuty为空】,请根据业务实际情况,进行立案修改补充交强责任类型后进行理算,或联系管理员补齐该数据",false);
			}
			if (!"1".equals(prpLClaimVo.getCiIndemDuty())&& CodeConstants.DAMAGE_DATE.before(prpLClaimVo.getDamageTime()) && bzDutyCar) {
				message += "本车无责，不赔付三者车辆损失!";
				logger.debug("本车无责，不赔付三者车辆损失!");
				compensateVo.setMessage(message);
				return compensateVo;
			}
		}
		
		
		/**
		 * 组织车损数据(后续专门提取成一个方法)
		 */
		List<PrpLLossItemVo> prpLLossItemVoList = new ArrayList<PrpLLossItemVo>();
		String kindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
		
		// 因为代位求偿没有开发完毕，此处为了测试不报错，先全部设置为自付，后续一定记得调整 begin
		String isSubRogation = CodeConstants.PayFlagType.COMPENSATE_PAY;
		// 因为代位求偿没有开发完毕，此处为了测试不报错，先全部设置为自付，后续一定记得调整 end
		
		if(prpLDlossCarMainVoList != null && !prpLDlossCarMainVoList.isEmpty()){
			for(PrpLDlossCarMainVo prpLDlossCarMainVo : prpLDlossCarMainVoList){
				if(!UnderWritePassList.contains(prpLDlossCarMainVo.getUnderWriteFlag())
						&&!CodeConstants.defLossSourceFlag.MODIFYDEFLOSS.equals(prpLDlossCarMainVo.getDeflossSourceFlag())){
					// 核损金额为空或者未通过核损且不是定损修改不组织该数据
					continue;
				}
				if(CodeConstants.CompensateKind.CI_COMPENSATE.equals(compensateType)){
					// 理算已扣减不组织该数据
					if( !prpLDlossCarMainVo.getLossState().substring(0,1).equals("0")){
						prpLcheckCarIdList.add(prpLDlossCarMainVo.getSerialNo().toString());
						continue;
					}
				}
				if(CodeConstants.CompensateKind.BI_COMPENSATE.equals(compensateType)){
					if( !prpLDlossCarMainVo.getLossState().substring(1,2).equals("0")){
						// 理算已扣减不组织该数据
						prpLcheckCarIdList.add(prpLDlossCarMainVo.getSerialNo().toString());
						continue;
					}
				}
				
				if(CodeConstants.CetainLossType.DEFLOSS_NULL.equals(prpLDlossCarMainVo.getCetainLossType())){
					//当车损为无损失的时候核损和查勘都不组织数据
					prpLcheckCarIdList.add(prpLDlossCarMainVo.getSerialNo().toString());
					continue;
				}
				prpLcheckCarIdList.add(prpLDlossCarMainVo.getSerialNo().toString());
				// 车损数据组织直接取主表的金额，标的险别根据定损方式来判断获取，三者的险别为B或BZ
				BigDecimal sumLossX2 = BigDecimal.ZERO;// 涉水险核损金额汇总
				if(prpLDlossCarMainVo.getSerialNo()==1){
					// 险别根据损失方式赋值
					if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(prpLDlossCarMainVo.getRiskCode()) != null &&
							CodeConstants.ISNEWCLAUSECODE2020_MAP.get(prpLDlossCarMainVo.getRiskCode())){
						kindCode = CodeConstants.LossFee2020Kind_Map.get(prpLDlossCarMainVo.getRiskCode()+prpLDlossCarMainVo.getLossFeeType());
					}else{
						kindCode = CodeConstants.LossFeeKind_Map.get(prpLDlossCarMainVo.getLossFeeType());
						// 对涉水险做特殊处理，获取主表金额和险别之后，遍历dlossCarComp表的wadflag = 1 的金额，主表减去该金额，该金额为涉水险金额
						List<PrpLDlossCarCompVo> prpLDlossCarCompVoList = prpLDlossCarMainVo.getPrpLDlossCarComps();// 换件项目清单
						if(prpLDlossCarCompVoList != null && !prpLDlossCarCompVoList.isEmpty()){
							for(PrpLDlossCarCompVo prpLDlossCarCompVo:prpLDlossCarCompVoList){
								if("1".equals(prpLDlossCarCompVo.getWadFlag())){
									sumLossX2 = sumLossX2.add(prpLDlossCarCompVo.getSumVeriLoss());
								}
							}
						}
					}
					
					//如果承保了A1-全面型车损险，则险别不是A，是A1
					if(HadBuyTheKind(KINDCODE.KINDCODE_A1,prpLCMainVo)&&KINDCODE.KINDCODE_A.equals(kindCode)){
						kindCode = KINDCODE.KINDCODE_A1;
					}
					
					if(sumLossX2.compareTo(BigDecimal.ZERO)==1){
						if(!CodeConstants.CompensateKind.CI_COMPENSATE.equals(compensateType)&&!HadBuyTheKind(CodeConstants.KINDCODE.KINDCODE_X2,prpLCMainVo)){
							logger.debug("未购买赋值险别"+kindCode+"该条数据不组织");
						}else{
							// 涉水金额大于0则需要新增一条涉水险车损数据
							PrpLLossItemVo prpLLossItemVo = new PrpLLossItemVo();
							//判断并赋值车损的lossfeetype字段
							prpLLossItemVo.setLossFeeType(prpLDlossCarMainVo.getLossFeeType());
							prpLLossItemVo.setRescueFee(BigDecimal.ZERO);
							prpLLossItemVo.setItemId("1");
							prpLLossItemVo.setItemName(prpLDlossCarMainVo.getLicenseNo());
							prpLLossItemVo.setDlossId(prpLDlossCarMainVo.getId());
							prpLLossItemVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_X2);
							prpLLossItemVo.setRiskCode(prpLDlossCarMainVo.getRiskCode());
							prpLLossItemVo.setSumLoss(sumLossX2);//估损需减去涉水险金额
							prpLLossItemVo.setPayFlag(isSubRogation);
							prpLLossItemVo.setOtherDeductAmt(BigDecimal.ZERO);
							prpLLossItemVo.setNoDutyPayFlag("1");//都需要赔付
							// 此处为了后续测试需要，预赔金额没有从表中取值，直接取的零，后续一定要记得改正 begin
							prpLLossItemVo.setSumRealPay(prpLLossItemVo.getSumLoss().subtract(BigDecimal.ZERO));
							// 此处为了后续测试需要，预赔金额没有从表中取值，直接取的零，后续一定要记得改正 end
							
							prpLLossItemVoList.add(prpLLossItemVo);
							logger.debug("涉水险"+prpLLossItemVo.getItemName()+" 车辆从定损获取：损失金额SumLoss("+prpLLossItemVo.getSumLoss()+"),施救费RescueFee("+prpLLossItemVo
									.getRescueFee()+")");
						}
						
					}
					
				}else{
					if("1".equals(compensateType)){
						kindCode = CodeConstants.KINDCODE.KINDCODE_B;
					}else{
						kindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
					}
				}
				
				if(!CodeConstants.CompensateKind.CI_COMPENSATE.equals(compensateType)&&!HadBuyTheKind(kindCode,prpLCMainVo)){
					logger.debug("未购买赋值险别"+kindCode+"该条数据不组织");
					continue;
				}
				PrpLLossItemVo prpLLossItemVo = new PrpLLossItemVo();
				//判断并赋值车损的lossfeetype字段
				prpLLossItemVo.setLossFeeType(prpLDlossCarMainVo.getLossFeeType());
				prpLLossItemVo.setRescueFee(prpLDlossCarMainVo.getSumVeriRescueFee());
				prpLLossItemVo.setItemId(prpLDlossCarMainVo.getSerialNo().toString());
				prpLLossItemVo.setItemName(prpLDlossCarMainVo.getLicenseNo());
				prpLLossItemVo.setDlossId(prpLDlossCarMainVo.getId());
				prpLLossItemVo.setKindCode(kindCode);
				prpLLossItemVo.setRiskCode(prpLDlossCarMainVo.getRiskCode());
				prpLLossItemVo.setSumLoss(prpLDlossCarMainVo.getSumVeriLossFee().subtract(sumLossX2));//估损需减去涉水险金额
				prpLLossItemVo.setPayFlag(isSubRogation);
				prpLLossItemVo.setOtherDeductAmt(BigDecimal.ZERO);
				prpLLossItemVo.setNoDutyPayFlag("1");//都需要赔付
				// 此处为了后续测试需要，预赔金额没有从表中取值，直接取的零，后续一定要记得改正 begin
				prpLLossItemVo.setSumRealPay(prpLLossItemVo.getSumLoss().subtract(BigDecimal.ZERO));
				// 此处为了后续测试需要，预赔金额没有从表中取值，直接取的零，后续一定要记得改正 end
				
				prpLLossItemVoList.add(prpLLossItemVo);
				logger.debug(prpLLossItemVo.getItemName()+" 车辆从定损获取：损失金额SumLoss("+prpLLossItemVo.getSumLoss()+"),施救费RescueFee("+prpLLossItemVo
						.getRescueFee()+")");
				
			}
		}
		
		PrpLCheckTaskVo prpLCheckTaskVo = prpLCheckVo.getPrpLCheckTask(); // 从查勘获取车辆损失信息
		List<PrpLCheckCarVo> prpLCheckCarVoList = prpLCheckTaskVo.getPrpLCheckCars();
		//获取调度注销信息，注销则不组织该数据
		List<PrpLScheduleDefLossVo> prpLScheduleDefLossVoList = scheduleService.findPrpLScheduleDefLossList(prpLCheckTaskVo.getRegistNo());
		for (PrpLCheckCarVo prpLCheckCarVo : prpLCheckCarVoList) {
			if( !prpLcheckCarIdList.contains(prpLCheckCarVo.getSerialNo().toString())){// 判断是否此车辆进行的定损，若有定损则取定损
				//判断此任务是否已经被调度注销，注销则不组织该数据
				boolean schFlag = false;
				if(prpLScheduleDefLossVoList != null && !prpLScheduleDefLossVoList.isEmpty()){
					for(PrpLScheduleDefLossVo schVo:prpLScheduleDefLossVoList){
						if("1".equals(schVo.getDeflossType())&&prpLCheckCarVo.getSerialNo()==schVo.getSerialNo()&&"9".equals(schVo.getScheduleStatus())){
							//当前调度任务为车损任务deflosstype=1，且已被注销schStatus=9
							schFlag = true;
							continue;
						}
					}
				}
				if(schFlag){
					continue;
				}
				PrpLCheckCarInfoVo prpLCheckCarInfoVo = prpLCheckCarVo.getPrpLCheckCarInfo();
				PrpLLossItemVo prpLLossItemVo = new PrpLLossItemVo();
				
				if("1".equals(prpLCheckVo.getLossType())&&"1".equals(prpLCheckCarVo.getSerialNo())){
					//如果车辆为标的车且全损，赋值为03（G险全损），其他赋值为00
					prpLLossItemVo.setLossFeeType("03");
				}else{
					prpLLossItemVo.setLossFeeType("00");
				}
				prpLLossItemVo.setItemId(prpLCheckCarVo.getSerialNo().toString());
				prpLLossItemVo.setItemName(prpLCheckCarVo.getPrpLCheckCarInfo().getLicenseNo());
				prpLLossItemVo.setDlossId(prpLCheckCarInfoVo.getCarid());
				prpLLossItemVo.setOtherDeductAmt(BigDecimal.ZERO);
//					prpLLossItemVo.setRecoveryOrPayFlag("3");
				if(prpLCheckCarVo.getSerialNo() == 1){
					if(prpLCheckCarVo.getKindCode()==null||!CodeConstants.KindForSelfCar_List.contains(prpLCheckCarVo.getKindCode())){
						// 标的车险别为空或者对应的险别不是AZLGF，即标的车没有购买赔付标的的险别，则不带入计算
						logger.debug("标的车没有购买赔付标的的险别，不带入计算");
						continue;
					}else{
						// 主车默认带出处理页面勾选的险别
						if(!CodeConstants.CompensateKind.CI_COMPENSATE.equals(compensateType)
								&&!HadBuyTheKind(prpLCheckCarVo.getKindCode(),prpLCMainVo)){
							continue;//未购买赋值的该险别则该条数据不组织
						}
						prpLLossItemVo.setKindCode(prpLCheckCarVo.getKindCode());
						logger.debug("查勘标的车代入计算-险别："+prpLCheckCarVo.getKindCode());
					}
				}else{
					// 三者车默认三者险
					if(!CodeConstants.CompensateKind.CI_COMPENSATE.equals(compensateType)
							&&!HadBuyTheKind(CodeConstants.KINDCODE.KINDCODE_B,prpLCMainVo)){
						continue;//未购买赋值的该险别则该条数据不组织
					}
					prpLLossItemVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_B);
				}
				
				prpLLossItemVo.setSumLoss(prpLCheckCarVo.getLossFee());
				prpLLossItemVo.setRescueFee(prpLCheckCarVo.getRescueFee());
				prpLLossItemVo.setRiskCode(prpLCMainVo.getRiskCode().trim());
				prpLLossItemVo.setPayFlag(isSubRogation);
				prpLLossItemVo.setNoDutyPayFlag("1");//都需要赔付
				
				// 此处为了后续测试需要，预赔金额没有从表中取值，直接取的零，后续一定要记得改正 begin
				prpLLossItemVo.setSumRealPay(prpLLossItemVo.getSumLoss().subtract(BigDecimal.ZERO));
				// 此处为了后续测试需要，预赔金额没有从表中取值，直接取的零，后续一定要记得改正 end
				
				
//					prpLLossItemVo.setSumRest(new BigDecimal("0.00"));
				logger.debug(prpLLossItemVo.getItemName()+" 车辆从查勘获取：损失金额SumLoss("+prpLLossItemVo.getSumLoss()+"),施救费RescueFee("+prpLLossItemVo
						.getRescueFee()+")");
				prpLLossItemVoList.add(prpLLossItemVo);
			}
		}
		
		/**
		 *  2016-10-18 防止重开赔案-理算冲销-追加定损 后车财人损失一个损失项有多条损失主表 超保额管控出错
		 *  故在组织完损失项后遍历损失项List按照serialNo汇总一次金额（施救费和定损金额）,DLossIdExt字段存多张主表的ID
		 */
		if(prpLLossItemVoList != null&&prpLLossItemVoList.size()>0){
			Map<String,PrpLLossItemVo> seriNoItemVoMap = new HashMap<String,PrpLLossItemVo>();//损失项序号 和 损失项Vo Map
			for(PrpLLossItemVo itemVo:prpLLossItemVoList){
				String NoKindKey = itemVo.getItemId()+itemVo.getKindCode();
				if(seriNoItemVoMap==null||seriNoItemVoMap.size()==0){
					seriNoItemVoMap.put(NoKindKey,itemVo);
				}else{
					if(seriNoItemVoMap.containsKey(NoKindKey)){
						seriNoItemVoMap.get(NoKindKey).setRescueFee(DataUtils.NullToZero(seriNoItemVoMap.get(NoKindKey).getRescueFee())
								.add(DataUtils.NullToZero(itemVo.getRescueFee())));//施救费
						seriNoItemVoMap.get(NoKindKey).setSumLoss(seriNoItemVoMap.get(NoKindKey).getSumLoss().add(itemVo.getSumLoss()));//定损金额
						String DlossId = null;
						if(seriNoItemVoMap.get(NoKindKey).getDlossIdExt()==null){
							DlossId = itemVo.getDlossId().toString();
						}else{
							DlossId = seriNoItemVoMap.get(NoKindKey).getDlossIdExt()+","+itemVo.getDlossId();//定损主表扩展ID
						}
						seriNoItemVoMap.get(NoKindKey).setDlossIdExt(DlossId);
					}else{
						seriNoItemVoMap.put(NoKindKey,itemVo);
					}
				}
			}
			prpLLossItemVoList = new ArrayList<PrpLLossItemVo>();
			// 重新添加Vo
			for (PrpLLossItemVo vo : seriNoItemVoMap.values()) {
				prpLLossItemVoList.add(vo);
			}
		}

		compensateVo.setPrpLLossItemVoList(prpLLossItemVoList);
		compensateVo.setPrpLLossItemVoZFList(prpLLossItemVoList);
		compensateVo.setMessage(message);
		return compensateVo;
	}

	/**
	 * 组织财产损失信息
	 * @param compensateVo
	 * @param prpLcheck
	 * @param prpLcMain
	 * @param compensateType
	 * @param prpLclaim
	 * @return
	 */
	private CompensateVo getPrpLpropLosses(CompensateVo compensateVo, PrpLCheckVo prpLCheckVo, PrpLCMainVo prpLCMainVo, String compensateType, PrpLClaimVo prpLClaimVo) {
		String message = "";
		boolean bzDutyCar = false;
		// 广州个性化处理
		DateTime damageDateConst = null;
		List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPropMainListByRegistNo(prpLCMainVo.getRegistNo());
		// 因为财产定损环节没有存 查勘环节Prplcheckprop的id，所以此处只能通过SerialNo去判断（某一损失项，查勘定损环节存的SerialNo一样）
		List<String> prpLcheckPropIdList = new ArrayList<String>(0);
	
		damageDateConst = CodeConstants.DAMAGE_DATE;
		bzDutyCar = this.checkBzDutyCar(prpLClaimVo, bzDutyCar);
		// 2008年2月1日后，交强险，如果本车无责，不初始化出车，财损失的数据
		if("2".equals(compensateType)){
			if (prpLClaimVo.getCiIndemDuty() == null) {
				throw new BusinessException(
						"报案号："+prpLClaimVo.getRegistNo()+" 的【交强责任类型PrpLclaim.CiIndemDuty为空】,请根据业务实际情况,进行立案修改补充交强责任类型后进行理算,或联系管理员补齐该数据",false);
			}
			if (!"1".equals(prpLClaimVo.getCiIndemDuty())&& damageDateConst.before(prpLClaimVo.getDamageTime()) && bzDutyCar) {
				message += "本车无责，本计算书不赔付三者财产损失|";
				compensateVo.setMessage(message);
				return compensateVo;
			}
		}
		List<PrpLLossPropVo> prpLLossPropVoList = new ArrayList<PrpLLossPropVo>(0);
		PrpLLossPropVo prpLLossPropVo;
		if (prpLdlossPropMainVoList != null && !prpLdlossPropMainVoList.isEmpty()) {
			for (PrpLdlossPropMainVo prpLdlossPropMainVo : prpLdlossPropMainVoList) {
				if(!CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE.equals(prpLdlossPropMainVo.getUnderWriteFlag())
						&&!CodeConstants.defLossSourceFlag.MODIFYDEFLOSS.equals(prpLdlossPropMainVo.getDeflossSourceFlag())){
					// 既没有核损通过 也没有定损修改
						continue;
				}
				if(CodeConstants.CompensateKind.CI_COMPENSATE.equals(compensateType)){
					if( !prpLdlossPropMainVo.getLossState().substring(0,1).equals("0")){
						continue;
					}
				}
				if(CodeConstants.CompensateKind.BI_COMPENSATE.equals(compensateType)){
					if( !prpLdlossPropMainVo.getLossState().substring(1,2).equals("0")){
						continue;
					}
				}
				prpLcheckPropIdList.add(prpLdlossPropMainVo.getSerialNo().toString());
				prpLLossPropVo = new PrpLLossPropVo();
				prpLLossPropVo.setDlossId(prpLdlossPropMainVo.getId());
				if(prpLdlossPropMainVo.getSerialNo() == 1){
					if(!CodeConstants.CompensateKind.CI_COMPENSATE.equals(compensateType)
							&&!HadBuyTheKind(CodeConstants.KINDCODE.KINDCODE_D2,prpLCMainVo)){
						continue;//未购买赋值的该险别则该条数据不组织
					}
					prpLLossPropVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_D2);
				}else{
					if(!CodeConstants.CompensateKind.CI_COMPENSATE.equals(compensateType)
							&&!HadBuyTheKind(CodeConstants.KINDCODE.KINDCODE_B,prpLCMainVo)){
						continue;//未购买赋值的该险别则该条数据不组织
					}
					prpLLossPropVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_B);
				}
				
				prpLLossPropVo.setItemId(prpLdlossPropMainVo.getSerialNo().toString()); // 损失类型代码
				// 此处存车牌号或者地面
				if("0".equals(prpLdlossPropMainVo.getSerialNo())){
					prpLLossPropVo.setItemName("地面"); // 损失类型名称
				}else{
					prpLLossPropVo.setItemName(prpLdlossPropMainVo.getLicense());
				}
				prpLLossPropVo.setOtherDeductAmt(new BigDecimal(0));
				prpLLossPropVo.setSumLoss(prpLdlossPropMainVo.getSumVeriLoss()); // 受损金额（定损金额）
				// prpLLossPropVo.setSumRest(new BigDecimal(sumDefReject).setScale(2, BigDecimal.ROUND_HALF_UP)); // 残值
				if(prpLdlossPropMainVo.getVerirescueFee()!=null){
					prpLLossPropVo.setRescueFee(prpLdlossPropMainVo.getVerirescueFee()); // 施救费
				}else{
					prpLLossPropVo.setRescueFee(BigDecimal.ZERO); // 施救费
				}
				prpLLossPropVo.setLossType("1");
				prpLLossPropVo.setPropType("1");// 设置损失类型 1为财产损失 2为其它损失
				prpLLossPropVo.setRiskCode(prpLdlossPropMainVo.getRiskCode()); // 险种代码
				prpLLossPropVo.setLossQuantity(BigDecimal.ONE);
				
				// 此处为了后续测试需要，预赔金额没有从表中取值，直接取的零，后续一定要记得改正 begin
				prpLLossPropVo.setSumRealPay(prpLLossPropVo.getSumLoss().subtract(BigDecimal.ZERO));
				// 此处为了后续测试需要，预赔金额没有从表中取值，直接取的零，后续一定要记得改正 end
				
				logger.debug(prpLLossPropVo.getItemName()+" 财产损失从定损获取:受损金额("+prpLLossPropVo.getSumLoss()+")施救费("+prpLLossPropVo.getRescueFee()+")");
				prpLLossPropVoList.add(prpLLossPropVo);
			}
		}
		// 从查勘获取
		PrpLCheckTaskVo prpLCheckTaskVo = prpLCheckVo.getPrpLCheckTask();
		List<PrpLCheckPropVo> prpLCheckPropVoList = prpLCheckTaskVo.getPrpLCheckProps();
		List<PrpLScheduleDefLossVo> prpLScheduleDefLossVoList = scheduleService.findPrpLScheduleDefLossList(prpLCheckTaskVo.getRegistNo());
		for(PrpLCheckPropVo prpLCheckPropVo:prpLCheckPropVoList){
			if( !prpLcheckPropIdList.contains(prpLCheckPropVo.getLossPartyId().toString())){// 判断定损中是否已经存在该项目
				//判断此任务是否已经被调度注销，注销则不组织该数据
				boolean schFlag = false;
				if(prpLScheduleDefLossVoList!=null&&prpLScheduleDefLossVoList.size()>0){
				for(PrpLScheduleDefLossVo schVo:prpLScheduleDefLossVoList){
					if("2".equals(schVo.getDeflossType())&&prpLCheckPropVo.getLossPartyId().intValue()==schVo.getSerialNo()&&"9".equals(schVo.getScheduleStatus())){
						//当前调度任务为车损任务deflosstype=1，且已被注销schStatus=9				
						schFlag = true;
						continue;
					}
				}
				}
				if(schFlag){
					continue;
				}
				prpLLossPropVo = new PrpLLossPropVo();
				prpLLossPropVo.setDlossId(prpLCheckPropVo.getId());
				if(prpLCheckPropVo.getLossPartyId() == 1){
					if(!CodeConstants.CompensateKind.CI_COMPENSATE.equals(compensateType)
							&&!HadBuyTheKind(CodeConstants.KINDCODE.KINDCODE_D2,prpLCMainVo)){
						continue;//未购买赋值的该险别则该条数据不组织
					}
					prpLLossPropVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_D2);
				}else{
					if(!CodeConstants.CompensateKind.CI_COMPENSATE.equals(compensateType)
							&&!HadBuyTheKind(CodeConstants.KINDCODE.KINDCODE_B,prpLCMainVo)){
						continue;//未购买赋值的该险别则该条数据不组织
					}
					prpLLossPropVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_B);
				}
				prpLLossPropVo.setItemId(prpLCheckPropVo.getLossPartyId().toString());
				if(prpLCheckPropVo.getLossPartyId() == 0){
					prpLLossPropVo.setItemName("地面");
				}else{
					List<PrpLCheckCarVo> prpLCheckCarVoList = checkTaskService.findCheckCarVo(prpLCheckPropVo.getRegistNo());
					for(PrpLCheckCarVo prpLCheckCarVo:prpLCheckCarVoList){
						if(prpLCheckCarVo.getSerialNo().toString().equals(prpLCheckPropVo.getLossPartyId().toString())){
							prpLLossPropVo.setItemName(prpLCheckCarVo.getPrpLCheckCarInfo().getLicenseNo());
							break;
						}
					}
				}
				prpLLossPropVo.setOtherDeductAmt(new BigDecimal(0));
				prpLLossPropVo.setSumLoss(prpLCheckPropVo.getLossFee()); // 受损金额（查勘估损金额）
				// prpLLossPropVo.setSumRest(new BigDecimal(sumDefReject).setScale(2,BigDecimal.ROUND_HALF_UP)); // 残值
				prpLLossPropVo.setRescueFee(BigDecimal.ZERO); // 施救费
				prpLLossPropVo.setLossType("1");// 设置损失类型 1为财产损失 2为其它损失
				prpLLossPropVo.setPropType("1");// 设置损失类型 1为财产损失 9为其它损失
				prpLLossPropVo.setRiskCode(prpLCMainVo.getRiskCode()); // 险种代码
				prpLLossPropVo.setLossQuantity(BigDecimal.ONE);
				

				// 此处为了后续测试需要，预赔金额没有从表中取值，直接取的零，后续一定要记得改正 begin
				prpLLossPropVo.setSumRealPay(NullToZero(prpLLossPropVo.getSumLoss()).subtract(BigDecimal.ZERO));
				// 此处为了后续测试需要，预赔金额没有从表中取值，直接取的零，后续一定要记得改正 end

				logger.debug(prpLLossPropVo.getItemName()+" 财产损失从查勘获取:受损金额("+prpLLossPropVo.getSumLoss()+")施救费("+prpLLossPropVo.getRescueFee()+")");
				prpLLossPropVoList.add(prpLLossPropVo);
			}
		}
		// 附加险也存在prpLLossPropVo中，setPropType为9，需要组织附加险数据

		// 查询车辆定损主表，然后获取子表PrpLDlossCarSubRiskVos
		List<PrpLDlossCarMainVo> PrpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(prpLCMainVo.getRegistNo());
		List<PrpLDlossCarSubRiskVo> PrpLDlossCarSubRiskVoList = new ArrayList<PrpLDlossCarSubRiskVo>();
		// 标的车车牌号，存储在损失类型名称字段
		String selfLicense = "";
		Long selfLossCarMainId = null;
		for(PrpLDlossCarMainVo dlossCarMainVo:PrpLDlossCarMainVoList){
			if(dlossCarMainVo.getSerialNo()==1){
				selfLicense = dlossCarMainVo.getLicenseNo();
				selfLossCarMainId = dlossCarMainVo.getId();
			}
			if(CodeConstants.CompensateKind.CI_COMPENSATE.equals(compensateType)){
				// 理算已扣减不组织该数据
				if( !dlossCarMainVo.getLossState().substring(0,1).equals("0")){
					continue;
				}
			}
			if(CodeConstants.CompensateKind.BI_COMPENSATE.equals(compensateType)){
				if( !dlossCarMainVo.getLossState().substring(1,2).equals("0")){
					// 理算已扣减不组织该数据
					continue;
				}
			}
			PrpLDlossCarSubRiskVoList.addAll(dlossCarMainVo.getPrpLDlossCarSubRisks());
		}

		if(PrpLDlossCarSubRiskVoList!=null&& !PrpLDlossCarSubRiskVoList.isEmpty()){
			for(PrpLDlossCarSubRiskVo carSubRiskVo:PrpLDlossCarSubRiskVoList){
				if(carSubRiskVo.getVeriSubRiskFee()==null){
					continue;
				}
				prpLLossPropVo = new PrpLLossPropVo();
				// 此处的dlossId是车辆定损主表ID
				prpLLossPropVo.setDlossId(selfLossCarMainId);
				prpLLossPropVo.setKindCode(carSubRiskVo.getKindCode());

				// 附加险-标的车-1
				prpLLossPropVo.setItemId("1"); // 损失类型代码
				// 此处存车牌号或者地面-默认存标的车车牌号
				prpLLossPropVo.setItemName(selfLicense);
				if(CodeConstants.KINDCODE.KINDCODE_X.equals(carSubRiskVo.getKindCode())){
					prpLLossPropVo.setItemName(carSubRiskVo.getItemName());
				}

				prpLLossPropVo.setOtherDeductAmt(BigDecimal.ZERO);
				prpLLossPropVo.setSumLoss(carSubRiskVo.getSubRiskFee()); // 受损金额（定损金额）
				prpLLossPropVo.setRescueFee(BigDecimal.ZERO); // 施救费

				prpLLossPropVo.setLossType("2");
				prpLLossPropVo.setPropType("9");// 设置损失类型 1为财产损失 9为其它损失
				prpLLossPropVo.setRiskCode(carSubRiskVo.getRiskCode()); // 险种代码
				prpLLossPropVo.setLossQuantity(DataUtils.NullToZero(carSubRiskVo.getCount()));//数量/天数
				
				// 此处为了后续测试需要，预赔金额没有从表中取值，直接取的零，后续一定要记得改正 begin
				prpLLossPropVo.setSumRealPay(prpLLossPropVo.getSumLoss().subtract(BigDecimal.ZERO));
				// 此处为了后续测试需要，预赔金额没有从表中取值，直接取的零，后续一定要记得改正 end
				
				logger.debug(prpLLossPropVo.getItemName()+" 财产损失从附加险明细获取:受损金额("+prpLLossPropVo.getSumLoss()+")施救费("+prpLLossPropVo.getRescueFee()+")");
				prpLLossPropVoList.add(prpLLossPropVo);
			}
		}
		
		/**
		 *  2016-10-18 防止重开赔案-理算冲销-追加定损 后车财人损失一个损失项有多条损失主表 超保额管控出错
		 *  故在组织完损失项后遍历损失项List按照serialNo汇总一次金额（施救费和定损金额）,DLossIdExt字段存多张主表的ID
		 */
		if(prpLLossPropVoList != null&&prpLLossPropVoList.size()>0){
			Map<String,PrpLLossPropVo> seriNoPropVoMap = new HashMap<String,PrpLLossPropVo>();//损失项序号 和 损失项Vo Map
			for(PrpLLossPropVo itemVo:prpLLossPropVoList){
				String NoKindKey = itemVo.getItemId()+itemVo.getKindCode()+itemVo.getPropType();
				if(seriNoPropVoMap==null||seriNoPropVoMap.size()==0){
					seriNoPropVoMap.put(NoKindKey,itemVo);
				}else{
					if(seriNoPropVoMap.containsKey(NoKindKey)){
						seriNoPropVoMap.get(NoKindKey).setRescueFee(DataUtils.NullToZero(seriNoPropVoMap.get(NoKindKey).getRescueFee())
								.add(DataUtils.NullToZero(itemVo.getRescueFee())));//施救费
						seriNoPropVoMap.get(NoKindKey).setSumLoss(seriNoPropVoMap.get(NoKindKey).getSumLoss().add(itemVo.getSumLoss()));//定损金额
						// 当附加险为 修理补偿   代步  停驶时需要累加天数
						if("9".equals(itemVo.getPropType())){
							if(CodeConstants.KINDCODE.KINDCODE_C.equals(itemVo.getKindCode())
									||CodeConstants.KINDCODE.KINDCODE_T.equals(itemVo.getKindCode())
									||CodeConstants.KINDCODE.KINDCODE_RF.equals(itemVo.getKindCode())){
								seriNoPropVoMap.get(NoKindKey).setLossQuantity(seriNoPropVoMap.get(NoKindKey).getLossQuantity().add(itemVo.getLossQuantity()));
							}
						}
						String DlossId = null;
						if(seriNoPropVoMap.get(NoKindKey).getDlossIdExt()==null){
							DlossId = itemVo.getDlossId().toString();
						}else{
							DlossId = seriNoPropVoMap.get(NoKindKey).getDlossIdExt()+","+itemVo.getDlossId();//定损主表扩展ID
						}
						seriNoPropVoMap.get(NoKindKey).setDlossIdExt(DlossId);
					}else{
						seriNoPropVoMap.put(NoKindKey,itemVo);
					}
				}
			}
			prpLLossPropVoList = new ArrayList<PrpLLossPropVo>();
			// 重新添加Vo
			for (PrpLLossPropVo vo : seriNoPropVoMap.values()) {
				prpLLossPropVoList.add(vo);
			}
		}

		compensateVo.setPrpLLossPropVoList(prpLLossPropVoList);
		compensateVo.setMessage(message);
		return compensateVo;
	}

	/**
	 * 获取人伤损失
	 * @param compensateVo
	 * @param prpLcheck
	 * @param prpLcMain
	 * @param compensateType
	 * @param prpLclaim
	 * @return
	 */
	public CompensateVo getPrpLpersonItems(CompensateVo compensateVo, PrpLCheckVo prpLCheckVo, PrpLCMainVo prpLCMainVo, String compensateType, PrpLClaimVo prpLClaimVo) {
		//List<PrpLDlossPersTraceVo> prpLDlossPersTraceVoList = persTraceDubboService.findPrpLDlossPersTraceVoListByRegistNo(prpLClaimVo.getRegistNo());
		List<PrpLDlossPersTraceMainVo> PrpLDlossPersTraceMainVoList = persTraceDubboService.findPersTraceMainVoList(prpLClaimVo.getRegistNo());
		List<PrpLLossPersonVo> prpLLossPersonVoList = new ArrayList<PrpLLossPersonVo>();
		if(PrpLDlossPersTraceMainVoList!=null&&PrpLDlossPersTraceMainVoList.size()>0){
		boolean SHFeeFlag = false;
		// 上海机构的费用所用字典不同-判断人伤是否是上海机构人伤
		if(prpLCMainVo!=null&&"22".equals(prpLCMainVo.getComCode().substring(0,2))){
			SHFeeFlag = true;
		}
		for(PrpLDlossPersTraceMainVo traceMainVo : PrpLDlossPersTraceMainVoList){
			if(!CodeConstants.AuditStatus.SUBMITCHARGE.equals(traceMainVo.getAuditStatus())
					&&!CodeConstants.AuditStatus.SUBMITVERIFY.equals(traceMainVo.getAuditStatus())
					||"10".equals(traceMainVo.getCaseProcessType())){
				// 人伤最高级跟踪审核或者最高级费用审核通过时 刷新才代入该人伤任务金额 
				// 未核损通过不组织数据  人伤只有无需赔付时才会自动审核通过 此时人伤不组织
				continue;
			}
			// 无保单代赔人员损失信息
			if(traceMainVo.getPrpLDlossPersTraces() == null || traceMainVo.getPrpLDlossPersTraces().isEmpty()){
				compensateVo.setPrpLLossPersonVoList(prpLLossPersonVoList);
				return compensateVo;
			}else{
				for(PrpLDlossPersTraceVo prpLDlossPersTraceVo:traceMainVo.getPrpLDlossPersTraces()){
					if(prpLDlossPersTraceVo.getSumVeriDefloss()==null&&prpLDlossPersTraceVo.getSumVeriReportFee()==null){
						continue;
					}
					if(CodeConstants.CompensateKind.CI_COMPENSATE.equals(compensateType)){
						if( !traceMainVo.getLossState().substring(0,1).equals("0")){
							continue;
						}
					}
					if(CodeConstants.CompensateKind.BI_COMPENSATE.equals(compensateType)){
						if( !traceMainVo.getLossState().substring(1,2).equals("0")){
							continue;
						}
					}
					if(CodeConstants.ValidFlag.INVALID.equals(prpLDlossPersTraceVo.getValidFlag())){
						// 人伤任务如果注销，不组织该条数据
						continue;
					}
					PrpLDlossPersInjuredVo prpLDlossPersInjuredVo = prpLDlossPersTraceVo.getPrpLDlossPersInjured();
					PrpLLossPersonVo prpLLossPersonVo = new PrpLLossPersonVo();
					List<PrpLLossPersonFeeVo> prpLLossPersonFeeVoList = new ArrayList<PrpLLossPersonFeeVo>(0);
					prpLLossPersonVo.setRegistNo(prpLDlossPersTraceVo.getRegistNo());
					// 伤亡类型
					prpLLossPersonVo.setInjuryType(prpLDlossPersInjuredVo.getWoundCode());
					prpLLossPersonVo.setDlossId(prpLDlossPersTraceVo.getId());
					prpLLossPersonVo.setRiskCode(prpLDlossPersInjuredVo.getRiskCode());
					prpLLossPersonVo.setItemId(prpLDlossPersInjuredVo.getSerialNo().toString());
					if(CodeConstants.CompensateKind.CI_COMPENSATE.equals(compensateType)){
						if(prpLDlossPersInjuredVo.getSerialNo()==1){
							if("2".equals(prpLDlossPersInjuredVo.getLossItemType())){
								if(!HadBuyTheKind(CodeConstants.KINDCODE.KINDCODE_D12,prpLCMainVo)){
									continue;// 未购买赋值的该险别则该条数据不组织
								}
								prpLLossPersonVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_D12);
							}else if("3".equals(prpLDlossPersInjuredVo.getLossItemType())){
								if(!HadBuyTheKind(CodeConstants.KINDCODE.KINDCODE_D11,prpLCMainVo)){
									continue;// 未购买赋值的该险别则该条数据不组织
								}
								prpLLossPersonVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_D11);
							}
						}else{
							prpLLossPersonVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_BZ);
						}
						prpLLossPersonVo.setItemName(prpLDlossPersInjuredVo.getLicenseNo());
					}else {
						if(prpLDlossPersInjuredVo.getSerialNo()==0){
							if(!HadBuyTheKind(CodeConstants.KINDCODE.KINDCODE_B,prpLCMainVo)){
								continue;// 未购买赋值的该险别则该条数据不组织
							}
							prpLLossPersonVo.setItemName("地面");
							prpLLossPersonVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_B);
						}else if(prpLDlossPersInjuredVo.getSerialNo()==1){
							if("2".equals(prpLDlossPersInjuredVo.getLossItemType())){
								if(!HadBuyTheKind(CodeConstants.KINDCODE.KINDCODE_D12,prpLCMainVo)){
									continue;// 未购买赋值的该险别则该条数据不组织
								}
								prpLLossPersonVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_D12);
							}else if("3".equals(prpLDlossPersInjuredVo.getLossItemType())){
								if(!HadBuyTheKind(CodeConstants.KINDCODE.KINDCODE_D11,prpLCMainVo)){
									continue;// 未购买赋值的该险别则该条数据不组织
								}
								prpLLossPersonVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_D11);
							}
							prpLLossPersonVo.setItemName(prpLDlossPersInjuredVo.getLicenseNo());
						}else{
							if(!HadBuyTheKind(CodeConstants.KINDCODE.KINDCODE_B,prpLCMainVo)){
								continue;// 未购买赋值的该险别则该条数据不组织
							}
							prpLLossPersonVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_B);
							prpLLossPersonVo.setItemName(prpLDlossPersInjuredVo.getLicenseNo());
						}
					}
					prpLLossPersonVo.setPersonId(prpLDlossPersTraceVo.getId());
					prpLLossPersonVo.setPersonName(prpLDlossPersInjuredVo.getPersonName());
					prpLLossPersonVo.setPersonAge(DataUtils.NullToZero(prpLDlossPersInjuredVo.getPersonAge()).intValue());
					prpLLossPersonVo.setCurrency("CNY");
					prpLLossPersonVo.setSumOffLoss(prpLDlossPersTraceVo.getSumVeriDetractionFee());
					
					// 此处为了后续测试需要，预赔金额没有从表中取值，直接取的零，后续一定要记得改正 end
				// if("A".equals(prpLDlossPersInjuredVo.getWoundCode()) || "B".equals(prpLDlossPersInjuredVo.getWoundCode())){
				// prpLLossPersonVo.setLossType(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES);
				// }else if("A".equals(prpLDlossPersInjuredVo.getWoundCode())){
				// prpLLossPersonVo.setLossType(CodeConstants.FeeTypeCode.PERSONLOSS);
				// }
	
					PrpLLossPersonFeeVo medFeeVo = new PrpLLossPersonFeeVo();
					PrpLLossPersonFeeVo dehFeeVo = new PrpLLossPersonFeeVo();
					medFeeVo.setLossItemNo(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES);
					dehFeeVo.setLossItemNo(CodeConstants.FeeTypeCode.PERSONLOSS);
					medFeeVo.setFeeLoss(BigDecimal.ZERO);
					dehFeeVo.setFeeLoss(BigDecimal.ZERO);
					medFeeVo.setFeeRealPay(BigDecimal.ZERO);
					dehFeeVo.setFeeRealPay(BigDecimal.ZERO);
					medFeeVo.setFeeOffLoss(BigDecimal.ZERO);
					dehFeeVo.setFeeOffLoss(BigDecimal.ZERO);
					medFeeVo.setKindCode(prpLLossPersonVo.getKindCode());
					dehFeeVo.setKindCode(prpLLossPersonVo.getKindCode());
	
					List<PrpLDlossPersTraceFeeVo> prpLDlossPersTraceFeeVoList = prpLDlossPersTraceVo.getPrpLDlossPersTraceFees();// 人伤跟踪估计费用明细表
					for(PrpLDlossPersTraceFeeVo persTraceFeeVo:prpLDlossPersTraceFeeVoList){
						BigDecimal veriDefloss = BigDecimal.ZERO;
						BigDecimal VeriRealFee = DataUtils.NullToZero(persTraceFeeVo.getVeriRealFee());
						// 人伤核损未录入定损金额且录入了估损金额（审核）则取后者的值
						if(persTraceFeeVo.getVeriDefloss()==null&&persTraceFeeVo.getVeriReportFee()!=null){
							veriDefloss = DataUtils.NullToZero(persTraceFeeVo.getVeriReportFee());
						}else{
							veriDefloss = DataUtils.NullToZero(persTraceFeeVo.getVeriDefloss());
						}
						if(SHFeeFlag){
							if(CodeConstants.MedicalFee_Map_SH.containsKey(persTraceFeeVo.getFeeTypeCode())){//
								medFeeVo.setFeeLoss(medFeeVo.getFeeLoss().add(veriDefloss));
								medFeeVo.setFeeRealPay(medFeeVo.getFeeRealPay().add(VeriRealFee));
								medFeeVo.setFeeOffLoss(BigDecimal.ZERO);
							}
							if(CodeConstants.DisabilityFee_Map_SH.containsKey(persTraceFeeVo.getFeeTypeCode())){
								dehFeeVo.setFeeLoss(dehFeeVo.getFeeLoss().add(veriDefloss));
								dehFeeVo.setFeeRealPay(dehFeeVo.getFeeRealPay().add(VeriRealFee));
								dehFeeVo.setFeeOffLoss(BigDecimal.ZERO);
							}
						}else{
							if(CodeConstants.MedicalFee_Map.containsKey(persTraceFeeVo.getFeeTypeCode())){//
								medFeeVo.setFeeLoss(medFeeVo.getFeeLoss().add(veriDefloss));
								medFeeVo.setFeeRealPay(medFeeVo.getFeeRealPay().add(VeriRealFee));
								medFeeVo.setFeeOffLoss(BigDecimal.ZERO);
							}
							if(CodeConstants.DisabilityFee_Map.containsKey(persTraceFeeVo.getFeeTypeCode())){
								dehFeeVo.setFeeLoss(dehFeeVo.getFeeLoss().add(veriDefloss));
								dehFeeVo.setFeeRealPay(dehFeeVo.getFeeRealPay().add(VeriRealFee));
								dehFeeVo.setFeeOffLoss(BigDecimal.ZERO);
							}
						}
						
					}
					prpLLossPersonFeeVoList.add(medFeeVo);
					prpLLossPersonFeeVoList.add(dehFeeVo);
					
					// 人伤核损未录入定损金额且录入了估损金额（审核）则取后者的值（因为界面以tracefee表录入数据为准，故主表字段直接累加子表）
					BigDecimal sumVeriReportFee = medFeeVo.getFeeLoss().add(dehFeeVo.getFeeLoss());
					prpLLossPersonVo.setSumLoss(sumVeriReportFee);
					prpLLossPersonVo.setPrpLLossPersonFees(prpLLossPersonFeeVoList);
					// 此处为了后续测试需要，预赔金额没有从表中取值，直接取的零，后续一定要记得改正 begin
					prpLLossPersonVo.setSumRealPay(prpLLossPersonVo.getSumLoss().subtract(BigDecimal.ZERO));
	
					prpLLossPersonVoList.add(prpLLossPersonVo);
	
					logger.debug(prpLLossPersonVo.getItemName()+prpLLossPersonVo.getPersonName()+" 人伤医疗审核从定损获取:受损金额("+medFeeVo.getFeeLoss()+")");
					logger.debug(prpLLossPersonVo.getItemName()+prpLLossPersonVo.getPersonName()+" 人伤死亡伤残从定损获取:受损金额("+dehFeeVo.getFeeLoss()+")");
				}
			}
		}
	}
		
		compensateVo.setPrpLLossPersonVoList(prpLLossPersonVoList);
		return compensateVo;
	}

	/**
	 * 判断该保单是否承保了某个险别
	 * 
	 * <pre></pre>
	 * @param kindCode 险别
	 * @param cMainVo 保单信息主表
	 * @return
	 * @modified:
	 * ☆Weilanlei(2016年7月15日 下午2:35:03): <br>
	 */
	@Override
	public boolean HadBuyTheKind(String kindCode,PrpLCMainVo cMainVo){
		boolean isHad = false;
		if(kindCode==null){
			return isHad;
		}else{
			if(cMainVo.getPrpCItemKinds()!=null&&cMainVo.getPrpCItemKinds().size()>0){
				for(PrpLCItemKindVo cItemKind:cMainVo.getPrpCItemKinds()){
					if(kindCode.equals(cItemKind.getKindCode())){
						isHad = true;
						break;
					}
				}
			}
		}
		
		return isHad;
	}
	
	/**
	 * 返回填充n个空格的字符串
	 */
	public String space(int n) {
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < n; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	public boolean checkBzDutyCar(PrpLClaimVo prpLClaimVo, boolean bzDutyCar) {
		// 获取是否存在有责三者车
		List<PrpLCheckDutyVo> prpLCheckDutyVoList = checkTaskService.findCheckDutyByRegistNo(prpLClaimVo.getRegistNo());
		if(prpLCheckDutyVoList == null || prpLCheckDutyVoList.isEmpty()) {
			logger.error("不存在PrpLCheckDuty信息，案件号为:"+prpLClaimVo.getRegistNo());
			throw new BusinessException("不存在PrpLCheckDuty信息，案件号为:"+prpLClaimVo.getRegistNo(),false);
		}
		
		for(PrpLCheckDutyVo prpLCheckDutyVo:prpLCheckDutyVoList){
			if("1".equals(prpLCheckDutyVo.getCiDutyFlag()) && prpLCheckDutyVo.getSerialNo() > 1){
				bzDutyCar = true;
			}
		}
		return bzDutyCar;
	}

	public List<PrpLDlossCarInfoVo> getThirdPartiesInfo(PrpLClaimVo prpLClaimVo, String registNo, String compensateType) {
		// 将查勘，定损两个涉案表里的数据合在一起 ，借用PrpLDlossCarInfoVo来存放,用map便于覆盖
		Map<Integer, PrpLDlossCarInfoVo> prpLDlossCarInfoVoMap = new HashMap<Integer, PrpLDlossCarInfoVo>();
		List<PrpLDlossCarInfoVo> prpLdefLossThirdParties = new ArrayList<PrpLDlossCarInfoVo>(0); // 用于存放返回的值
		// 取得查勘---涉案信息
		List<PrpLCheckCarInfoVo> prpLCheckCarInfoVoList = checkTaskService.findPrpLCheckCarInfoVoListByRegistNo(registNo);
		PrpLDlossCarInfoVo thirdPartyItem = null;// 主车 不一定有
		PrpLDlossCarInfoVo prpLdefLossThirdPartyTemp;
		// 将查勘的对象转换为定损的 存在map里
		for (PrpLCheckCarInfoVo prpLCheckCarInfoVo : prpLCheckCarInfoVoList) {
			if(CodeConstants.ValidFlag.INVALID.equals(prpLCheckCarInfoVo.getValidFlag())){
				continue;// 定损注销的车辆不带入损失项计算
			}
			PrpLCheckCarVo prpLCheckCarVo = checkTaskService.findByCheckId(prpLCheckCarInfoVo.getCarid());
			
			prpLdefLossThirdPartyTemp = new PrpLDlossCarInfoVo();
			prpLdefLossThirdPartyTemp.setRegistNo(prpLCheckCarInfoVo.getRegistNo());
			prpLdefLossThirdPartyTemp.setId(prpLCheckCarInfoVo.getCarid());
//			if (!"1".equals(prpLCheckCarVo.getSerialNo())) {
//				
			// prpLdefLossThirdPartyTemp.setCiIndemDuty(prpLCheckCarVo.getCiIndemDuty()); // 交强责任类型 100.0000有责 0.0000无责
//			}
			prpLdefLossThirdPartyTemp.setLossItemType(prpLCheckCarVo.getSerialNo().toString()); // 损失类型
			prpLdefLossThirdPartyTemp.setSerialNo(prpLCheckCarVo.getSerialNo()); // 车辆序号
			prpLdefLossThirdPartyTemp.setLicenseNo(prpLCheckCarInfoVo.getLicenseNo()); // 车牌号码
			// prpLdefLossThirdPartyTemp.setInsuredFlag(prpLCheckCarInfoVo.getInsuredFlag()); // 承保类型
			prpLdefLossThirdPartyTemp.setInsureComCode(prpLCheckCarInfoVo.getInsurecomcode());// 承保机构代码..
			prpLdefLossThirdPartyTemp.setInsureComName(prpLCheckCarInfoVo.getInsurecomname());// 承保机构名称..
			if (prpLCheckCarVo.getSerialNo() == 1) {
				thirdPartyItem = prpLdefLossThirdPartyTemp; // 先把主车拿出来，暂不存放
			}else{ // 不是主车的，放在map里，可能会北覆盖
				prpLDlossCarInfoVoMap.put(prpLCheckCarVo.getSerialNo(), prpLdefLossThirdPartyTemp);
			}
		}

		// 获取定损--涉案项的信息
		List<PrpLDlossCarMainVo> lossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
		if(lossCarMainVoList !=null && !lossCarMainVoList.isEmpty()){
			for(PrpLDlossCarMainVo prpLDlossCarMainVo : lossCarMainVoList){
				if(!CodeConstants.VeriFlag.PASS.equals(prpLDlossCarMainVo.getUnderWriteFlag())){
					continue;
				}
				
				if (prpLDlossCarMainVo.getSerialNo()==1) {
					thirdPartyItem = prpLDlossCarMainVo.getLossCarInfoVo();
				}else{
					if(prpLDlossCarInfoVoMap.containsKey(prpLDlossCarMainVo.getSerialNo().toString())){
						prpLDlossCarInfoVoMap.put(prpLDlossCarMainVo.getSerialNo(), prpLDlossCarMainVo.getLossCarInfoVo());
					}else{// map 防止追加的情况
					//	prpLdefLossThirdParties.add(prpLDlossCarMainVo.getLossCarInfoVo());
						prpLDlossCarInfoVoMap.put(prpLDlossCarMainVo.getSerialNo(), prpLDlossCarMainVo.getLossCarInfoVo());
					}
				}
			}
		}
		
		// 如果查勘和定损都有主车，取定损的(在上面的循环体里已经取得）,如果查勘有定损没有，取查勘的
		if (thirdPartyItem != null) {
			// thirdPartyItem.setCiIndemDuty(prpLClaimVo.getCiIndemDuty()); // 立案表里的主车交强责任类型才是最终的
			prpLdefLossThirdParties.add(0,thirdPartyItem);// 第一条为标的车
		}
		// 如果没有主车信息，无法获取交强险试算限额
		if ("2".equals(compensateType) || CodeConstants.CompensateKind.ADVANCE.equals(compensateType)) {
			if (thirdPartyItem == null) {
				throw new BusinessException("没有主车涉案信息，交强险试算无法获取限额，请先进行主车查勘",false);
			}
		}
		for (Iterator iter = prpLDlossCarInfoVoMap.values().iterator(); iter.hasNext();) {
			prpLdefLossThirdParties.add((PrpLDlossCarInfoVo)iter.next());
		}
		return prpLdefLossThirdParties;
	}
	
	
	public CompensateVo orgnizeCompensateData(PrpLCompensateVo prpLCompensateVo,List<PrpLLossItemVo> prpLLossItemVoList,List<PrpLLossPropVo> prpLLossPropVoList,List<PrpLLossPersonVo> prpLLossPersonVoList, PrpLClaimVo prpLClaimVo,String compensateType) {
		//String compensateType = "2";
		
		CompensateVo compensateVo = new CompensateVo();
		// PrpLCompensateVo compensateResultVo = new PrpLCompensateVo();// 虚拟计算书prpLcompensate
		
		compensateVo.setCalculateType("compensate");
		PrpLCMainVo prpLcMainVo = policyViewService.getPrpLCMainByRegistNoAndPolicyNo(prpLCompensateVo.getRegistNo(),prpLCompensateVo.getPolicyNo());
		// ThirdPartyDepreRate thirdPartyDepreRate = new ThirdPartyDepreRate();// 新车购置价对象
		PrpLCItemCarVo prpLCItemCarVo = prpLcMainVo.getPrpCItemCars().get(0);
		if (prpLCItemCarVo != null && prpLCItemCarVo.getClauseType() != null) {
			compensateVo.setClauseType(prpLCItemCarVo.getClauseType());// 条款代码
		}
		compensateVo.setOperateDate(prpLcMainVo.getOperateDate());// 承保日期
		compensateVo.setRiskCode(prpLCompensateVo.getRiskCode());// 险种代码
		compensateVo.setPrpLCItemKindVoList(prpLcMainVo.getPrpCItemKinds());// 承保信息
		
		// 是否为新条款
		boolean clauseIssue = false;
		if(!"1101".equals(prpLcMainVo.getRiskCode().trim())){
			//2020新条款
			if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(prpLcMainVo.getRiskCode().trim()) != null &&
					CodeConstants.ISNEWCLAUSECODE2020_MAP.get(prpLcMainVo.getRiskCode().trim())){
				clauseIssue=true;
			}else{
				clauseIssue = CodeConstants.ISNEWCLAUSECODE_MAP.get(prpLcMainVo.getRiskCode().trim());
			}
			
		}
		//免赔金额  如果是新条款直接从A条款的Deductible取值，如果是旧条款则从M1条款的Value取值
		BigDecimal deductibleValue = BigDecimal.ZERO ; 

		BigDecimal amountKindA = BigDecimal.ZERO;
		List<PrpLCItemKindVo> itemkindList = compensateVo.getPrpLCItemKindVoList();
		if(itemkindList != null && !itemkindList.isEmpty()){
			for(PrpLCItemKindVo prpLCItemKindVo : compensateVo.getPrpLCItemKindVoList()){
				if(CodeConstants.KINDCODE.KINDCODE_A.equals(prpLCItemKindVo.getKindCode().trim())){
					amountKindA = prpLCItemKindVo.getAmount();
					compensateVo.setAmountKindA(amountKindA);
					deductibleValue = prpLCItemKindVo.getDeductible();
					break;
				}
			}
			for(PrpLCItemKindVo prpLCItemKindVo : itemkindList){
				if(CodeConstants.KINDCODE.KINDCODE_X1.equals(prpLCItemKindVo.getKindCode().trim()) ||
						CodeConstants.KINDCODE.KINDCODE_X2.equals(prpLCItemKindVo.getKindCode().trim()) ||
						CodeConstants.KINDCODE.KINDCODE_F.equals(prpLCItemKindVo.getKindCode().trim()) ||
							CodeConstants.KINDCODE.KINDCODE_NT.equals(prpLCItemKindVo.getKindCode().trim())){
					prpLCItemKindVo.setAmount(amountKindA);
				}
				
				if(!clauseIssue){
					if(CodeConstants.KINDCODE.KINDCODE_M1.equals(prpLCItemKindVo.getKindCode().trim())){
						deductibleValue = prpLCItemKindVo.getValue();
					}
				}
			}
		}

		
		// 计算车辆实际价值
		ThirdPartyDepreRate thirdPartyDepreRate = compensateService.queryThirdPartyDepreRate(prpLCompensateVo.getRegistNo());
		if (thirdPartyDepreRate == null) {
			throw new BusinessException("调用规则引擎获取出险时车辆实际价值出错，请稍后重试或联系管理员！",false);
		}
		compensateVo.setThirdPartyDepreRate(thirdPartyDepreRate);// 新车购置价对象

//		if(prpLClaimVo.getClaimType() != null){
			prpLCompensateVo.setHandler1Code(prpLCompensateVo.getHandler1Code());
		// prpLCompensateVo.setLflag(prpLClaimVo.getLflag()); //prpLClaimVo理赔类型
		// 一样的删除
		// prpLCompensateVo.setRegistNo(prpLCompensateVo.getRegistNo()); // 报案号
		// prpLCompensateVo.setPolicyNo(prpLCompensateVo.getPolicyNo()); // 保单号
		// prpLCompensateVo.setClaimNo(prpLCompensateVo.getClaimNo()); // 立案号
		// prpLCompensateVo.setCaseType(prpLCompensateVo.getCaseType());// 赔案类别
		// prpLCompensateVo.setComCode(prpLCompensateVo.getComCode()); // 业务归属机构
		// prpLCompensateVo.setRiskCode(prpLCompensateVo.getRiskCode()); // 险种代码
		// prpLCompensateVo.setCurrency(prpLCompensateVo.getCurrency()); // 币别代码
			
		prpLCompensateVo.setIndemnityDutyRate(prpLCompensateVo.getIndemnityDutyRate()); // 责任比例
		prpLCompensateVo.setIndemnityDuty(prpLCompensateVo.getIndemnityDuty()); // 事故责任类型
		// prpLCompensateVo.setClassCode(prpLcMainVo.getClassCode()); // 险类代码
		prpLCompensateVo.setCompensateKind(compensateType); // 计算书类型
		prpLCompensateVo.setUnderwriteFlag("0"); // 设置核赔标识为默认问题
		// 初始化理算主表的标志位时，空出的位数
			prpLCompensateVo.setFlag(space(5) + "21");
			
			for(PrpLLossPersonVo lossPersonVo : prpLLossPersonVoList){
				for(PrpLLossPersonFeeVo personFeeVo : lossPersonVo.getPrpLLossPersonFees()){
					personFeeVo.setKindCode(lossPersonVo.getKindCode());
					personFeeVo.setPersonId(lossPersonVo.getPersonId());
				}
			}
			
		compensateVo.setDeductibleValue(deductibleValue);
		// 一般赔案
		// 组织车辆损失信息
			prpLCompensateVo.setPrpLLossItems(prpLLossItemVoList);
		// 组织财产损失信息
			prpLCompensateVo.setPrpLLossProps(prpLLossPropVoList);
		// 组织人伤损失信息
			prpLCompensateVo.setPrpLLossPersons(prpLLossPersonVoList);
//		}
		
		compensateVo.setPrpLLossItemVoList(prpLCompensateVo.getPrpLLossItems());
		compensateVo.setPrpLLossPropVoList(prpLCompensateVo.getPrpLLossProps());
		compensateVo.setPrpLLossPersonVoList(prpLCompensateVo.getPrpLLossPersons());
		
		
		compensateVo.setPrpLCompensateVo(prpLCompensateVo);
		compensateVo.setPrpLCMainVo(prpLcMainVo);
		// 商业险增加免赔条件
		if(CodeConstants.CompensateKind.BI_COMPENSATE.equals(compensateType)){
			List<PrpLClaimDeductVo> claimDeductList = registQueryService.findIsCheckClaimDeducts(prpLCompensateVo.getRegistNo());
			compensateVo.setClaimDeductList(claimDeductList);
			
			//eMotorMap kindPaidMap
			Map<String,BigDecimal> eMotorMap = compensateService.queryEmotorMap(prpLcMainVo.getPolicyNo());
			Map<String,BigDecimal> kindPaidMap = compensateService.querySumRealPay(prpLClaimVo.getClaimNo(),prpLCompensateVo.getCompensateNo());
			
			compensateVo.setClaimDeductList(claimDeductList);
			compensateVo.seteMotorMap(eMotorMap);
			compensateVo.setKindPaidMap(kindPaidMap);
			
			//核赔通过的计算书 TODO
			boolean firstCompFlag = true;//第一张 商业/交强计算书
			List<PrpLCompensateVo> compensateList = compensateService.findCompensateByClaimno(prpLCompensateVo.getClaimNo(),"N");
			for(PrpLCompensateVo compVo : compensateList){
				PrpLCompensateExtVo compExtVo = compVo.getPrpLCompensateExt();
				if(compExtVo.getWriteOffFlag()==null ||"0".equals(compExtVo.getWriteOffFlag())){//不是冲销的案件
					if(compExtVo.getOppoCompensateNo() == null){
						firstCompFlag = false;
						break;
					}
				}	
			}
			
			if(!firstCompFlag){//不是第一张计算书 则需要判断是否扣减过免赔额
				boolean deductibleFlag = false;
				for(PrpLCompensateVo compVo : compensateList){
					List<PrpLLossItemVo> lossItemList = compVo.getPrpLLossItems();
					List<PrpLLossPropVo> propList = compVo.getPrpLLossProps();
					
					if(lossItemList!=null && !lossItemList.isEmpty()){
						for(PrpLLossItemVo lossItem : lossItemList){
							if(CodeConstants.KINDCODE.KINDCODE_A.equals(lossItem.getKindCode())){
								deductibleFlag = true;
							}
						}
					}
					
					if(!deductibleFlag && lossItemList!=null && !lossItemList.isEmpty()){
						for(PrpLLossPropVo propVo : propList){
							if("9".equals(propVo.getPropType()) 
									&& CodeConstants.KINDCODE.KINDCODE_X.equals(propVo.getKindCode())){
								deductibleFlag = true;
							}
						}
					}
					compensateVo.setDeductibleFlag(deductibleFlag);
				}
			}
		}
		
		
		return compensateVo;
	}
	
	private static BigDecimal NullToZero(BigDecimal strNum) {
		if(strNum==null){
			return new BigDecimal("0");
		}
		return strNum;
	}
}
