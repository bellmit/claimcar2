package ins.sino.claimcar.pinganunion.vo.prepay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 联盟中心响应数据-预赔费用
 *
 * @author mfn
 * @date 2020/7/20 18:48
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnionPrePayFeeItem implements Serializable {
    private static final long serialVersionUID = 4754101845793891586L;

    /**  保单号    是否非空：Y  编码：N */
    private String policyNo;
    /**  险种代码  代码定义3.2  是否非空：Y  编码：Y */
    private String planCode;
    /**  险别代码  代码定义3.3  是否非空：Y  编码：Y */
    private String dutyCode;
    /**  仲裁费    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal arbitrateFee;
    /**  诉讼费    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal lawsuitFee;
    /**  律师费    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal lawyerFee;
    /**  检验费    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal checkFee;
    /**  执行费    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal executeFee;
    /**  公估费    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal evaluationFee;
    /**  前置调查费    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal preInvestigateFee;

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getDutyCode() {
        return dutyCode;
    }

    public void setDutyCode(String dutyCode) {
        this.dutyCode = dutyCode;
    }

    public BigDecimal getArbitrateFee() {
        return arbitrateFee;
    }

    public void setArbitrateFee(BigDecimal arbitrateFee) {
        this.arbitrateFee = arbitrateFee;
    }

    public BigDecimal getLawsuitFee() {
        return lawsuitFee;
    }

    public void setLawsuitFee(BigDecimal lawsuitFee) {
        this.lawsuitFee = lawsuitFee;
    }

    public BigDecimal getLawyerFee() {
        return lawyerFee;
    }

    public void setLawyerFee(BigDecimal lawyerFee) {
        this.lawyerFee = lawyerFee;
    }

    public BigDecimal getCheckFee() {
        return checkFee;
    }

    public void setCheckFee(BigDecimal checkFee) {
        this.checkFee = checkFee;
    }

    public BigDecimal getExecuteFee() {
        return executeFee;
    }

    public void setExecuteFee(BigDecimal executeFee) {
        this.executeFee = executeFee;
    }

    public BigDecimal getEvaluationFee() {
        return evaluationFee;
    }

    public void setEvaluationFee(BigDecimal evaluationFee) {
        this.evaluationFee = evaluationFee;
    }

    public BigDecimal getPreInvestigateFee() {
        return preInvestigateFee;
    }

    public void setPreInvestigateFee(BigDecimal preInvestigateFee) {
        this.preInvestigateFee = preInvestigateFee;
    }
}
