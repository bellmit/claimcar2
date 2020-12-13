package ins.sino.claimcar.claim.calculator.kindCalculator;

import ins.framework.lang.Springs;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.ThirdPartyDepreRate;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;

import java.math.BigDecimal;
import java.util.List;


/**
 *  玻璃单独破碎计算器
 * <pre></pre>
 * @author ★ZhouYanBin
 */
public class KindCalculatorF extends AbstractKindCalculator {
	private RegistQueryService registQueryService = (RegistQueryService)Springs.getBean("registQueryService");
	private PolicyViewService policyViewService = (PolicyViewService)Springs.getBean("policyViewService");
	
	public KindCalculatorF(CompensateVo compensateVo,Object object,CompensateService compensateService){
		super(compensateVo,object,compensateService);
	}

//	@Override
//	public BigDecimal 	(BigDecimal value) {
//		// 没有免赔.
//		BigDecimal dutyDeductibleRate = new BigDecimal(0d);
//
//		return super.calDutyDeductibleRate(dutyDeductibleRate);
//	}

	@Override
	public BigDecimal calSelectDeductibleRate(BigDecimal selectDeductibleRate) {
		selectDeductibleRate = new BigDecimal(0d);
		if( !cprc){
			List<PrpLClaimDeductVo> prpLClaimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(this.compensateVo.getPrpLCompensateVo().getRegistNo());
			if(prpLClaimDeductVoList != null && !prpLClaimDeductVoList.isEmpty()){
				for(PrpLClaimDeductVo prpLClaimDeductVo:prpLClaimDeductVoList){
					// 选上就叠加 Yes为选上
					if("1".equals(prpLClaimDeductVo.getIsCheck()) && CodeConstants.KINDCODE.KINDCODE_A.equals(prpLClaimDeductVo.getKindCode().trim())){
						selectDeductibleRate = selectDeductibleRate.add(prpLClaimDeductVo.getDeductRate());
					}
				}
			}
		}
		return super.calSelectDeductibleRate(selectDeductibleRate);
	}

	// 本险保额取车辆实际价值。
	public BigDecimal calAmount(BigDecimal value) {
		PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(prpLcItemKind.getRegistNo(),prpLcItemKind.getPolicyNo());
		ThirdPartyDepreRate thirdPartyDepreRate = compensateService.queryThirdPartyDepreRate(prpLcompensate.getRegistNo());
		//BigDecimal sumRealPay4Kind = compensateService.querySumRealPay4Kind(prpLcompensate.getClaimNo(),prpLcompensate.getCompensateNo(),kindCode);
		BigDecimal sumRealPay4Kind = BigDecimal.ZERO;
		return super.calAmount(BigDecimal.valueOf(thirdPartyDepreRate.getActualValue()).subtract(sumRealPay4Kind));
	}
}
