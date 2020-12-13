package ins.sino.claimcar.claim.calculator.kindCalculator;

import java.math.BigDecimal;

import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.CompensateVo;

/**
 * 车身划痕损失险计算器
 * 
 * <pre></pre>
 * @author ★ZhouYanBin
 */
public class KindCalculatorL extends AbstractKindCalculator {

	public KindCalculatorL(CompensateVo compensateVo,Object object,CompensateService compensateService){
		super(compensateVo,object,compensateService);
	}

//	@Override
//	public BigDecimal calDutyDeductibleRate(BigDecimal value) {
//
//		// 控制累计赔款限额,每次赔偿实行15%绝对免赔率.
//		BigDecimal dutyDeductibleRate = new BigDecimal(15d);
//
//		return super.calDutyDeductibleRate(dutyDeductibleRate);
//	}

	@Override
	public BigDecimal calSelectDeductibleRate(BigDecimal value) {
		return super.calSelectDeductibleRate(new BigDecimal(0d));
	}

	@Override
	/**
	 * 计算险别限额，该险别需要冲减。
	 */
	public BigDecimal calAmount(BigDecimal value) {
		return super.calAmount(super.queryAllSubAmount());
	}

}
