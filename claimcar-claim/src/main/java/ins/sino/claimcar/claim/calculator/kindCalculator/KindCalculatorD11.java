package ins.sino.claimcar.claim.calculator.kindCalculator;

import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.commom.vo.Refs;

import java.math.BigDecimal;



public class KindCalculatorD11 extends KindCalculatorD12 {
	public KindCalculatorD11(CompensateVo compensateVo, Object object, CompensateService compensateService) {
        super(compensateVo, object, compensateService);
    }
	
	 @Override
	    /**
	     * 计算险别限额，该险别不需要冲减。。
	     */
	    public BigDecimal calAmount(BigDecimal value) {
	        value = super.queryOnceSubAmount();
	        final String unitAmountField = "itemAmount";
	        Refs.set(this.lossItem , unitAmountField , value);
	        return super.calAmount(value);
	    }
}
