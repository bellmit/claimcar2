package ins.sino.claimcar.claim.calculator.kindCalculator;

import ins.framework.lang.Springs;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.commom.vo.Refs;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;

import java.math.BigDecimal;
import java.util.List;


public class KindCalculatorD12 extends AbstractKindCalculator {

	private RegistQueryService registQueryService = (RegistQueryService)Springs.getBean("registQueryService");
	private PolicyViewService policyViewService = (PolicyViewService)Springs.getBean("policyViewService");;

	public KindCalculatorD12(CompensateVo compensateVo,Object lossItem,CompensateService compensateService){
		super(compensateVo,lossItem,compensateService);
	}

//	@Override
//	public BigDecimal calDutyDeductibleRate(BigDecimal value) {
//		// 商业险责任比例代码。
//		final String indemnityDuty = this.prpLcompensate.getIndemnityDuty();
//		BigDecimal dutyDeductibleRate = null;
//		// 全责。
//		if(cprc){
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.FULL_DUTY)){
//				dutyDeductibleRate = new BigDecimal(20d);
//			}
//			// 主责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.MAIN_DUTY)){
//				dutyDeductibleRate = new BigDecimal(15d);
//			}
//			// 同责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.EQUAL_DUTY)){
//				dutyDeductibleRate = new BigDecimal(10d);
//			}
//			// 次责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.SECOND_DUTY)){
//				dutyDeductibleRate = new BigDecimal(5d);
//			}
//			// 无责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.NO_DUTY)){
//				dutyDeductibleRate = new BigDecimal(0d);
//			}
//		}else{
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.FULL_DUTY)){
//				dutyDeductibleRate = new BigDecimal(15d);
//			}
//			// 主责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.MAIN_DUTY)){
//				dutyDeductibleRate = new BigDecimal(10d);
//			}
//			// 同责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.EQUAL_DUTY)){
//				dutyDeductibleRate = new BigDecimal(8d);
//			}
//			// 次责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.SECOND_DUTY)){
//				dutyDeductibleRate = new BigDecimal(5d);
//			}
//			// 无责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.NO_DUTY)){
//				dutyDeductibleRate = new BigDecimal(0d);
//			}
//		}
//		return super.calDutyDeductibleRate(dutyDeductibleRate);
//	}

	@Override
	public BigDecimal calSelectDeductibleRate(BigDecimal value) {
		BigDecimal selectDeductibleRate = new BigDecimal(0d);
		selectDeductibleRate = sommeDeductibleRate(selectDeductibleRate);
		return super.calSelectDeductibleRate(selectDeductibleRate);
	}

	/**
	 * 返回计算后的绝对免赔率
	 * @return BigDecimal
	 */

	public BigDecimal sommeDeductibleRate(BigDecimal selectDeductibleRate) {
		List<PrpLClaimDeductVo> prpLClaimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(this.compensateVo.getPrpLCompensateVo().getRegistNo());
		if(prpLClaimDeductVoList != null && !prpLClaimDeductVoList.isEmpty()){
			for(PrpLClaimDeductVo prpLClaimDeductVo:prpLClaimDeductVoList){
				// 选上就叠加 Yes为选上
				if("1".equals(prpLClaimDeductVo.getIsCheck()) && CodeConstants.KINDCODE.KINDCODE_D12.equals(prpLClaimDeductVo.getKindCode().trim())){
					selectDeductibleRate = selectDeductibleRate.add(prpLClaimDeductVo.getDeductRate());
				}
			}
		}
		return selectDeductibleRate;
	}

	/**
	 * 计算险别限额，该险别不需要冲减。限额=保额 / (座位数 - 1) - 该人员在本案已经核赔费用。
	 */
	@Override
	public BigDecimal calAmount(BigDecimal value) {
		if(value==null){
			PrpLLossPersonFeeVo personLoss = (PrpLLossPersonFeeVo)this.lossItem;
			final BigDecimal sumRealPay4KindAndPerson = this.compensateService.querySumRealPay4KindAndPerson(this.prpLcompensate.getRegistNo(),
					this.kindCode,personLoss.getPersonId());

			value = this.prpLcItemKind.getAmount();
			PrpLCItemCarVo prpLCItemCarVo = policyViewService.findItemCarByRegistNoAndPolicyNo(this.prpLcItemKind.getRegistNo(),this.prpLcItemKind.getPolicyNo());
			BigDecimal searCount = prpLCItemCarVo.getSeatCount();
			// 减去司机座位数量。
			searCount = searCount.subtract(new BigDecimal(1));
			value = MoneyFormator.format(value.divide(searCount).subtract(sumRealPay4KindAndPerson));

			final String unitAmountField = "itemAmount";
			Refs.set(this.lossItem,unitAmountField,value);
		}

		return super.calAmount(this.prpLcItemKind.getAmount());
	}

	public BigDecimal calIndemnityDutyRate(BigDecimal value) {
		if(value!=null){
			return super.calIndemnityDutyRate(value);
		}
		return super.calIndemnityDutyRate(super.queryMainCarIndemnityDutyRate());
	}
}
