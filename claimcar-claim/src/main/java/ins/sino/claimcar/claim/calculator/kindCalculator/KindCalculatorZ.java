package ins.sino.claimcar.claim.calculator.kindCalculator;

import java.math.BigDecimal;
import java.util.List;


import ins.framework.lang.Springs;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;

public class KindCalculatorZ extends AbstractKindCalculator {
	private RegistQueryService registQueryService = (RegistQueryService)Springs.getBean("registQueryService");;
	
	public KindCalculatorZ(CompensateVo compensateVo,Object object,CompensateService compensateService){
		super(compensateVo,object,compensateService);
	}

//	@Override
//	public BigDecimal calDutyDeductibleRate(BigDecimal value) {
//		BigDecimal dutyDeductibleRate = new BigDecimal(20d);
//		return super.calDutyDeductibleRate(dutyDeductibleRate);
//	}

	@Override
	public BigDecimal calSelectDeductibleRate(BigDecimal selectDeductibleRate) {
		selectDeductibleRate = new BigDecimal(0d);
		List<PrpLClaimDeductVo> prpLClaimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(this.compensateVo.getPrpLCompensateVo().getRegistNo());
		if(prpLClaimDeductVoList != null && !prpLClaimDeductVoList.isEmpty()){
			for(PrpLClaimDeductVo prpLClaimDeductVo:prpLClaimDeductVoList){
				// 选上就叠加 Yes为选上
				if("1".equals(prpLClaimDeductVo.getIsCheck()) && CodeConstants.KINDCODE.KINDCODE_Z.equals(prpLClaimDeductVo.getKindCode().trim())){
					selectDeductibleRate = selectDeductibleRate.add(prpLClaimDeductVo.getDeductRate());
				}
			}
		}
		return super.calSelectDeductibleRate(selectDeductibleRate);
	}

	@Override
	public BigDecimal calAmount(BigDecimal value) {
		return super.calAmount(super.queryOnceSubAmount());
	}
}
