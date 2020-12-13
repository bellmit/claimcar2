package ins.sino.claimcar.pinganunion.vo.prepay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.io.Serializable;

/**
 * 联盟中心响应数据-预赔信息
 *
 * @author mfn
 * @date 2020/7/20 18:31
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnionPrePayInfo implements Serializable {
    private static final long serialVersionUID = 7342623381200754059L;
    /**  报案号    是否非空：Y  编码：N */
    private String reportNo;
    /**  赔付次数    是否非空：Y  编码：N */
    private Integer caseTimes;
    /**  预付申请次数    是否非空：Y  编码：N */
    private Integer applyTimes;
    /**  初次申请人    是否非空：Y  编码：N */
    private String applyBy;
    /**  最终审批人    是否非空：N  编码：N */
    private String approverBy;
    /**  初次申请时间    是否非空：Y  编码：N */
    private String applyDate;
    /**  审批时间最终审批时间    是否非空：N  编码：N */
    private String approverDate;
    /**  预付状态  1:暂存，2：待审批，3：审批通过，4：审批退回，5：放弃预付  是否非空：Y  编码：Y */
    private String prepayStatus;
    /**  是否有效  (Y;有效,N:无效)  是否非空：N  编码：Y */
    private String status;
    /**  预赔人伤  Y为选中,N未选中  是否非空：N  编码：Y */
    private String prepayPerson;
    /**  预赔车物  Y为选中, N未选中  是否非空：N  编码：Y */
    private String prepayPropertyCar;
    /**  预赔费用  Y为选中, N未选中  是否非空：N  编码：Y */
    private String prepayFee;
    /**  医疗费直付  Y为选中, N未选中  是否非空：N  编码：Y */
    private String medicalDirectPay;
    /**  医疗费直付申请说明    是否非空：N  编码：N */
    private String medicalDirectPayDesc;

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public Integer getCaseTimes() {
        return caseTimes;
    }

    public void setCaseTimes(Integer caseTimes) {
        this.caseTimes = caseTimes;
    }

    public Integer getApplyTimes() {
        return applyTimes;
    }

    public void setApplyTimes(Integer applyTimes) {
        this.applyTimes = applyTimes;
    }

    public String getApplyBy() {
        return applyBy;
    }

    public void setApplyBy(String applyBy) {
        this.applyBy = applyBy;
    }

    public String getApproverBy() {
        return approverBy;
    }

    public void setApproverBy(String approverBy) {
        this.approverBy = approverBy;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getApproverDate() {
        return approverDate;
    }

    public void setApproverDate(String approverDate) {
        this.approverDate = approverDate;
    }

    public String getPrepayStatus() {
        return prepayStatus;
    }

    public void setPrepayStatus(String prepayStatus) {
        this.prepayStatus = prepayStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrepayPerson() {
        return prepayPerson;
    }

    public void setPrepayPerson(String prepayPerson) {
        this.prepayPerson = prepayPerson;
    }

    public String getPrepayPropertyCar() {
        return prepayPropertyCar;
    }

    public void setPrepayPropertyCar(String prepayPropertyCar) {
        this.prepayPropertyCar = prepayPropertyCar;
    }

    public String getPrepayFee() {
        return prepayFee;
    }

    public void setPrepayFee(String prepayFee) {
        this.prepayFee = prepayFee;
    }

    public String getMedicalDirectPay() {
        return medicalDirectPay;
    }

    public void setMedicalDirectPay(String medicalDirectPay) {
        this.medicalDirectPay = medicalDirectPay;
    }

    public String getMedicalDirectPayDesc() {
        return medicalDirectPayDesc;
    }

    public void setMedicalDirectPayDesc(String medicalDirectPayDesc) {
        this.medicalDirectPayDesc = medicalDirectPayDesc;
    }
}
