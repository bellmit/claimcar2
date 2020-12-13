package ins.sino.claimcar.claim.calculator.kindCalculator;

import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.CompensateVo;

import java.math.BigDecimal;

import org.springframework.util.Assert;


public class KindCalculatorBz extends AbstractKindCalculator{

    public KindCalculatorBz(CompensateVo compensateVo, Object object, CompensateService compensateService) {
        super(compensateVo, object, compensateService);
    }

    public BigDecimal calSelectDeductibleRate(BigDecimal value) {
        return super.calSelectDeductibleRate(new BigDecimal(0));
    }

    public BigDecimal calIndemnityDutyRate(BigDecimal value) {
        return super.calIndemnityDutyRate(this.prpLcompensate.getIndemnityDutyRate());
    }

    public BigDecimal calExceptDeductRate(BigDecimal value) {
        return super.calExceptDeductRate(new BigDecimal(0));
    }

//    public BigDecimal calDutyDeductibleRate(BigDecimal value) {
//        return super.calDutyDeductibleRate(new BigDecimal(0));
//    }
    
    /**
     * 获取交强险限额。(废弃使用，统一使用ConfigServicce.calBzAmount)
     * @param damageType 损失类型（车+财、医疗费、死亡伤残）。
     * @param isBzDutyType 交强险责任（有责、无责）。
     * @return
     */
    public static Double calBzAmount(String damageType , boolean isBzDutyType){
        Double amount = null;
        
        if(damageType.equals(CodeConstants.FeeTypeCode.PROPLOSS)){
            if(isBzDutyType){
                amount = 2000d;
            }
            else{
                amount = 100d;
            }
        }
        
        if(damageType.equals(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES)){
            if(isBzDutyType){
                amount = 10000d;
            }
            else{
                amount = 1000d;
            }
        }
        
        if(damageType.equals(CodeConstants.FeeTypeCode.PERSONLOSS)){
            if(isBzDutyType){
                amount = 110000d;
            }
            else{
                amount = 11000d;
            }
        }
        
        Assert.notNull(amount);
        return amount;
    }
    
    @Override
    /**
     * 交强calAmount默认返回0，由BzKindCalculator.calBzAmount(...)提供具体保额查询。
     * zhongyuhai 20121009
     */
    public BigDecimal calAmount(BigDecimal value) {
        value = BigDecimal.ZERO;
        return super.calAmount(value);
    }}
