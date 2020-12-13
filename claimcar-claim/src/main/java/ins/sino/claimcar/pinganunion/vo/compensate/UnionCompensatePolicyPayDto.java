package ins.sino.claimcar.pinganunion.vo.compensate;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 平安联盟中心-理算查询接口-查询结果-保单赔付信息
 *
 * @author mfn
 * @date 2020/7/29 16:48
 */
public class UnionCompensatePolicyPayDto implements Serializable {
    private static final long serialVersionUID = 6008704256873498363L;

    /**  报案号    是否非空：Y  编码：N */
    private String reportNo;
    /**  赔案号    是否非空：Y  编码：N */
    private String caseNo;
    /**  赔付次数    是否非空：Y  编码：N */
    private String caseTimes;
    /**  保单号    是否非空：Y  编码：N */
    private String policyNo;
    /**  保单总预陪    是否非空：N  编码：N */
    private String policyPrePay;
    /**  保单总预估金额    是否非空：N  编码：N */
    private String policySumEstimate;
    /**  保单总垫付金额    是否非空：N  编码：N */
    private String policyAdvancePay;
    /**  保单总赔付金额    是否非空：N  编码：N */
    private String policySumPay;
    /**  赔付金额    是否非空：N  编码：N */
    private String policyPay;
    /**  拒赔/注销金额    是否非空：N  编码：N */
    private String refuseAmount;
    /**  冲减金额    是否非空：N  编码：N */
    private String writeoffAmount;
    /**  保单总费用金额    是否非空：N  编码：N */
    private String policySumFee;
    /**  减损费用    是否非空：N  编码：N */
    private String decreaseFee;
    /**  垫付转回金额    是否非空：N  编码：N */
    private String policyAdvanceReturnPay;
    /**  超限金额    是否非空：N  编码：N */
    private String consultAmount;
    /**  保单总预付    是否非空：N  编码：N */
    private String policyPreFee;
    
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	public String getCaseTimes() {
		return caseTimes;
	}
	public void setCaseTimes(String caseTimes) {
		this.caseTimes = caseTimes;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getPolicyPrePay() {
		return policyPrePay;
	}
	public void setPolicyPrePay(String policyPrePay) {
		this.policyPrePay = policyPrePay;
	}
	public String getPolicySumEstimate() {
		return policySumEstimate;
	}
	public void setPolicySumEstimate(String policySumEstimate) {
		this.policySumEstimate = policySumEstimate;
	}
	public String getPolicyAdvancePay() {
		return policyAdvancePay;
	}
	public void setPolicyAdvancePay(String policyAdvancePay) {
		this.policyAdvancePay = policyAdvancePay;
	}
	public String getPolicySumPay() {
		return policySumPay;
	}
	public void setPolicySumPay(String policySumPay) {
		this.policySumPay = policySumPay;
	}
	public String getPolicyPay() {
		return policyPay;
	}
	public void setPolicyPay(String policyPay) {
		this.policyPay = policyPay;
	}
	public String getRefuseAmount() {
		return refuseAmount;
	}
	public void setRefuseAmount(String refuseAmount) {
		this.refuseAmount = refuseAmount;
	}
	public String getWriteoffAmount() {
		return writeoffAmount;
	}
	public void setWriteoffAmount(String writeoffAmount) {
		this.writeoffAmount = writeoffAmount;
	}
	public String getPolicySumFee() {
		return policySumFee;
	}
	public void setPolicySumFee(String policySumFee) {
		this.policySumFee = policySumFee;
	}
	public String getDecreaseFee() {
		return decreaseFee;
	}
	public void setDecreaseFee(String decreaseFee) {
		this.decreaseFee = decreaseFee;
	}
	public String getPolicyAdvanceReturnPay() {
		return policyAdvanceReturnPay;
	}
	public void setPolicyAdvanceReturnPay(String policyAdvanceReturnPay) {
		this.policyAdvanceReturnPay = policyAdvanceReturnPay;
	}
	public String getConsultAmount() {
		return consultAmount;
	}
	public void setConsultAmount(String consultAmount) {
		this.consultAmount = consultAmount;
	}
	public String getPolicyPreFee() {
		return policyPreFee;
	}
	public void setPolicyPreFee(String policyPreFee) {
		this.policyPreFee = policyPreFee;
	}
    
}
