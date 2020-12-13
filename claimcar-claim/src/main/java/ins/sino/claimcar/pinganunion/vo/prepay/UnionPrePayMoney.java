package ins.sino.claimcar.pinganunion.vo.prepay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 联盟中心响应数据-预赔金额
 *
 * @author mfn
 * @date 2020/7/20 18:46
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnionPrePayMoney implements Serializable {
    private static final long serialVersionUID = -1268619963420287208L;
    /**  报案号    是否非空：Y  编码：N */
    private String reportNo;
    /**  保单号    是否非空：Y  编码：N */
    private String policyNo;
    /**  险种代码  代码定义3.2  是否非空：Y  编码：Y */
    private String planCode;
    /**  险别代码  代码定义3.3  是否非空：Y  编码：Y */
    private String dutyCode;
    /**  保单限额    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal dutyPayLimit;
    /**  未决金额    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal dutyPayAmount;
    /**  已赔付金额    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal lossAmount;
    /**  本次预赔的申请金额    是否非空：N  编码：N */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private BigDecimal dutyPayMoney;
    /**  状态  Y有效，N无效  是否非空：Y  编码：Y */
    private String status;

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

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

    public BigDecimal getDutyPayLimit() {
        return dutyPayLimit;
    }

    public void setDutyPayLimit(BigDecimal dutyPayLimit) {
        this.dutyPayLimit = dutyPayLimit;
    }

    public BigDecimal getDutyPayAmount() {
        return dutyPayAmount;
    }

    public void setDutyPayAmount(BigDecimal dutyPayAmount) {
        this.dutyPayAmount = dutyPayAmount;
    }

    public BigDecimal getLossAmount() {
        return lossAmount;
    }

    public void setLossAmount(BigDecimal lossAmount) {
        this.lossAmount = lossAmount;
    }

    public BigDecimal getDutyPayMoney() {
        return dutyPayMoney;
    }

    public void setDutyPayMoney(BigDecimal dutyPayMoney) {
        this.dutyPayMoney = dutyPayMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
