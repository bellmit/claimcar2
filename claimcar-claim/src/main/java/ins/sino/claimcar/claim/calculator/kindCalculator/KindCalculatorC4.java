package ins.sino.claimcar.claim.calculator.kindCalculator;

import java.math.BigDecimal;

import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;


/**
 * 家庭自用车代步车费用险计算器。
 * @author
 */
public class KindCalculatorC4 extends AbstractKindCalculator{
    public KindCalculatorC4(CompensateVo compensateVo, Object object, CompensateService compensateService) {
        super(compensateVo, object, compensateService);
    }

//    @Override
//    public BigDecimal calDutyDeductibleRate(BigDecimal value) {
//        
//        BigDecimal dutyDeductibleRate = new BigDecimal(0d);
//
//        return super.calDutyDeductibleRate(dutyDeductibleRate);
//    }

    @Override
    public BigDecimal calSelectDeductibleRate(BigDecimal value) {
        return super.calSelectDeductibleRate(new BigDecimal(0d));
    }
    
    @Override
    /**
     * 计算险别限额，该险别需要冲减。
     * 代步车险保额为0，需要冲减PrpLcItemKind.unitAmount。
     */
    public BigDecimal calAmount(BigDecimal value) {
        //险别总保额。
        BigDecimal amount = this.prpLcItemKind.getAmount();
        //代步车险保额为0。
        super.calAmount(new BigDecimal(0));
        
        //eMotor扣减。
        BigDecimal eMotorAmount = compensateService.queryEmotorKindLossfee(prpLcItemKind.getPolicyNo(),this.kindCode);
        
        value = MoneyFormator.format(amount.subtract(eMotorAmount));
        logger.info("value = " + value);
        logger.info("amount = " + amount);
        logger.info("eMotorAmount = " + eMotorAmount);
        
        PrpLLossPropVo prpLLossPropVo = (PrpLLossPropVo)this.lossItem;
        prpLLossPropVo.setItemAmount(new BigDecimal(value.intValue()));
        prpLLossPropVo.setPropType("9");
        return value;
    }
}
