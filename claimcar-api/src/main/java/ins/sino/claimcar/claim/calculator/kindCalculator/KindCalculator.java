package ins.sino.claimcar.claim.calculator.kindCalculator;


import java.math.BigDecimal;

/**
 * 险别赔款计算器接口。
 * @author zhongyuhai
 */
public interface KindCalculator{
    
    /**
     * 返回本计算器涉及的险别。
     * @return
     */
    public String getKindCode();
    
    /**
     * 计算事故责任免赔率。 或绝对免赔率
     */
    public BigDecimal calDutyDeductibleRate(BigDecimal value);
    
    /**
     * 计算商业险责任比例。
     */
    public BigDecimal calIndemnityDutyRate(BigDecimal value);
    
    /**
     * 计算可选免赔率。
     */
    public BigDecimal calSelectDeductibleRate(BigDecimal value);
    
    /**
     * 计算不计免赔免赔率。
     * @param value
     */
    public BigDecimal calExceptDeductRate(BigDecimal value);
    
    /**
     * 计算险别保额。
     * @param value
     */
    public BigDecimal calAmount(BigDecimal value);
    
    /**
     * 计算实际价值。
     * @param value
     */
    public BigDecimal calItemValue(BigDecimal value);
    
    /**
     * 计算折旧率。
     * @param value
     */
    public BigDecimal calDepreRate(BigDecimal value);
    
    /**
     * 计算免赔率。
     * @param value
     */
    public BigDecimal calDeductibleRate(BigDecimal value);
}