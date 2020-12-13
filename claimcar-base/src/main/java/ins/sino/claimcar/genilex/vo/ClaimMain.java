package ins.sino.claimcar.genilex.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ClaimMain")
public class ClaimMain implements Serializable {

	/** 保单信息 */
	private static final long serialVersionUID = 6606450331852430142L;
	
	@XStreamAlias("ReportNo")
	private String reportNo="";  //报案号
	
    @XStreamAlias("ClaimNo")
    private String claimNo="";  //赔案号
    
    @XStreamAlias("ClaimSequenceNo")
    private String claimSequenceNo="";  //交强险平台赔案编号
    
    @XStreamAlias("ClaimTimes")
    private String claimTimes="";  //赔付次数
    
    @XStreamAlias("ClaimStatus")
    private String claimStatus="";  //赔案主状态
    
    @XStreamAlias("DepartmentCode")
    private String departmentCode="";  //机构编码
    
    @XStreamAlias("PolicyNo")
    private String policyNo="";  //保单号
    
    @XStreamAlias("EdrPrjNo")
    private String edrPrjNo="";  //批改期次
	   
    @XStreamAlias("PlanCode")
    private String planCode="";  //险种编码
    
    @XStreamAlias("LossTime")
    private String lossTime="";  //出险时间
    
    @XStreamAlias("ReportTime")
    private String reportTime="";  //报案时间
    
    @XStreamAlias("CatastropheLossInd")
    private String catastropheLossInd="";  //重大赔案标志
    
    @XStreamAlias("DepositInd")
    private String depositInd="";  //垫付标
    
    @XStreamAlias("RegisterInd")
    private String registerInd="";  //立案标志
    
    @XStreamAlias("DocumentCompletedInd")
    private String documentCompletedInd="";  //资料齐全标志
    
    @XStreamAlias("PrepayInd")
    private String prepayInd="";  //预付标志
    
    @XStreamAlias("GettingbackInd")
    private String gettingbackInd="";  //追偿标志
    
    @XStreamAlias("LitigationInd")
    private String litigationInd="";  //诉讼标志
    
    @XStreamAlias("PaymentInd")
    private String paymentInd="";  //赔付标志
    
    @XStreamAlias("XEpaFlag")
    private String xEpaFlag="";  //小额赔案标识
    
    @XStreamAlias("ReportAmt")
    private String reportAmt="";  //报案金额
    
    @XStreamAlias("ReportCurrencyCode")
    private String reportCurrencyCode="";  //报案币种
    
    @XStreamAlias("TotalDepositAmt")
    private String totalDepositAmt="";  //垫付总金额
    
    @XStreamAlias("DepositCurrencyCode")
    private String depositCurrencyCode="";  //垫付币种
    
    @XStreamAlias("RegisterAmt")
    private String registerAmt="";  //立案金额
    
    @XStreamAlias("RegisterCurrencyCode")
    private String registerCurrencyCode="";  //立案币种
    
    @XStreamAlias("TotalVerifiedAmt")
    private String totalVerifiedAmt="";  //总核损金额
    
    @XStreamAlias("VerifiedCurrencyCode")
    private String verifiedCurrencyCode="";  //总核损币种
    
    @XStreamAlias("TotalPrepayAmt")
    private String totalPrepayAmt="";  //预付总金额
    
    @XStreamAlias("PrepayCurrencyCode")
    private String prepayCurrencyCode="";  //预付币种
    
    @XStreamAlias("TotalPaymentAmt")
    private String totalPaymentAmt="";  //赔款总金额
    
    @XStreamAlias("PaymentCurrencyCode")
    private String paymentCurrencyCode="";  //赔款币种
    
    @XStreamAlias("RegisterBy")
    private String registerBy="";  //立案人
    
    @XStreamAlias("RegisterTime")
    private String registerTime="";  //立案时间
    
    @XStreamAlias("EndcaseBy")
    private String endcaseBy="";  //结案人
    
    @XStreamAlias("EndcaseDepartmentCode")
    private String endcaseDepartmentCode="";  //结案受理机构
    
    @XStreamAlias("EndcaseTime")
    private String endcaseTime="";  //结案日期
    
    @XStreamAlias("EndcasePigeonholeNo")
    private String endcasePigeonholeNo="";  //结案归档号
    
    @XStreamAlias("EndcaseDesc")
    private String endcaseDesc="";  //结案说明
    
    @XStreamAlias("RevokecaseInd")
    private String revokecaseInd="";  //撤案标志
    
    @XStreamAlias("RevokecaseBy")
    private String revokecaseBy="";  //撤案人
    
    @XStreamAlias("RevokecaseTime")
    private String revokecaseTime="";  //撤案时间
    
    @XStreamAlias("RevokecaseDesc")
    private String revokecaseDesc="";  //撤案意见
    
    @XStreamAlias("HugeLossCode")
    private String hugeLossCode="";  //是否巨灾
    
    @XStreamAlias("TotalVerifiedTime")
    private String totalVerifiedTime="";  //总核损时间
    
    @XStreamAlias("CollidedThirdInd")
    private String collidedThirdInd="";  //互碰自赔标识
    
    @XStreamAlias("Actiontype")
    private String actiontype="";  //操作方式
    
    @XStreamAlias("SubrogationFlag")
    private String subrogationFlag="";  //代位求偿标识
    
    @XStreamAlias("Remark")
    private String remark="";  //备注

    @XStreamAlias("CreateBy")
    private String createBy;  //创建者
    
    @XStreamAlias("CreateTime")
    private String createTime;  //创建日期
    
    @XStreamAlias("UpdateBy")
    private String updateBy;  //更新者
    
    @XStreamAlias("UpdateTime")
    private String updateTime;  //更新日期

    
    public String getReportNo() {
        return reportNo;
    }

    
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    
    public String getClaimNo() {
        return claimNo;
    }

    
    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    
    public String getClaimSequenceNo() {
        return claimSequenceNo;
    }

    
    public void setClaimSequenceNo(String claimSequenceNo) {
        this.claimSequenceNo = claimSequenceNo;
    }

    
    public String getClaimTimes() {
        return claimTimes;
    }

    
    public void setClaimTimes(String claimTimes) {
        this.claimTimes = claimTimes;
    }

    
    public String getClaimStatus() {
        return claimStatus;
    }

    
    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    
    public String getDepartmentCode() {
        return departmentCode;
    }

    
    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    
    public String getPolicyNo() {
        return policyNo;
    }

    
    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    
    public String getEdrPrjNo() {
        return edrPrjNo;
    }

    
    public void setEdrPrjNo(String edrPrjNo) {
        this.edrPrjNo = edrPrjNo;
    }

    
    public String getPlanCode() {
        return planCode;
    }

    
    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    
    public String getLossTime() {
        return lossTime;
    }

    
    public void setLossTime(String lossTime) {
        this.lossTime = lossTime;
    }

    
    public String getReportTime() {
        return reportTime;
    }

    
    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    
    public String getCatastropheLossInd() {
        return catastropheLossInd;
    }

    
    public void setCatastropheLossInd(String catastropheLossInd) {
        this.catastropheLossInd = catastropheLossInd;
    }

    
    public String getDepositInd() {
        return depositInd;
    }

    
    public void setDepositInd(String depositInd) {
        this.depositInd = depositInd;
    }

    
    public String getRegisterInd() {
        return registerInd;
    }

    
    public void setRegisterInd(String registerInd) {
        this.registerInd = registerInd;
    }

    
    public String getDocumentCompletedInd() {
        return documentCompletedInd;
    }

    
    public void setDocumentCompletedInd(String documentCompletedInd) {
        this.documentCompletedInd = documentCompletedInd;
    }

    
    public String getPrepayInd() {
        return prepayInd;
    }

    
    public void setPrepayInd(String prepayInd) {
        this.prepayInd = prepayInd;
    }

    
    public String getGettingbackInd() {
        return gettingbackInd;
    }

    
    public void setGettingbackInd(String gettingbackInd) {
        this.gettingbackInd = gettingbackInd;
    }

    
    public String getLitigationInd() {
        return litigationInd;
    }

    
    public void setLitigationInd(String litigationInd) {
        this.litigationInd = litigationInd;
    }

    
    public String getPaymentInd() {
        return paymentInd;
    }

    
    public void setPaymentInd(String paymentInd) {
        this.paymentInd = paymentInd;
    }

    
    public String getxEpaFlag() {
        return xEpaFlag;
    }

    
    public void setxEpaFlag(String xEpaFlag) {
        this.xEpaFlag = xEpaFlag;
    }

    
    public String getReportAmt() {
        return reportAmt;
    }

    
    public void setReportAmt(String reportAmt) {
        this.reportAmt = reportAmt;
    }

    
    public String getReportCurrencyCode() {
        return reportCurrencyCode;
    }

    
    public void setReportCurrencyCode(String reportCurrencyCode) {
        this.reportCurrencyCode = reportCurrencyCode;
    }

    
    public String getTotalDepositAmt() {
        return totalDepositAmt;
    }

    
    public void setTotalDepositAmt(String totalDepositAmt) {
        this.totalDepositAmt = totalDepositAmt;
    }

    
    public String getDepositCurrencyCode() {
        return depositCurrencyCode;
    }

    
    public void setDepositCurrencyCode(String depositCurrencyCode) {
        this.depositCurrencyCode = depositCurrencyCode;
    }

    
    public String getRegisterAmt() {
        return registerAmt;
    }

    
    public void setRegisterAmt(String registerAmt) {
        this.registerAmt = registerAmt;
    }

    
    public String getRegisterCurrencyCode() {
        return registerCurrencyCode;
    }

    
    public void setRegisterCurrencyCode(String registerCurrencyCode) {
        this.registerCurrencyCode = registerCurrencyCode;
    }

    
    public String getTotalVerifiedAmt() {
        return totalVerifiedAmt;
    }

    
    public void setTotalVerifiedAmt(String totalVerifiedAmt) {
        this.totalVerifiedAmt = totalVerifiedAmt;
    }

    
    public String getVerifiedCurrencyCode() {
        return verifiedCurrencyCode;
    }

    
    public void setVerifiedCurrencyCode(String verifiedCurrencyCode) {
        this.verifiedCurrencyCode = verifiedCurrencyCode;
    }

    
    public String getTotalPrepayAmt() {
        return totalPrepayAmt;
    }

    
    public void setTotalPrepayAmt(String totalPrepayAmt) {
        this.totalPrepayAmt = totalPrepayAmt;
    }

    
    public String getPrepayCurrencyCode() {
        return prepayCurrencyCode;
    }

    
    public void setPrepayCurrencyCode(String prepayCurrencyCode) {
        this.prepayCurrencyCode = prepayCurrencyCode;
    }

    
    public String getTotalPaymentAmt() {
        return totalPaymentAmt;
    }

    
    public void setTotalPaymentAmt(String totalPaymentAmt) {
        this.totalPaymentAmt = totalPaymentAmt;
    }

    
    public String getPaymentCurrencyCode() {
        return paymentCurrencyCode;
    }

    
    public void setPaymentCurrencyCode(String paymentCurrencyCode) {
        this.paymentCurrencyCode = paymentCurrencyCode;
    }

    
    public String getRegisterBy() {
        return registerBy;
    }

    
    public void setRegisterBy(String registerBy) {
        this.registerBy = registerBy;
    }

    
    public String getRegisterTime() {
        return registerTime;
    }

    
    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    
    public String getEndcaseBy() {
        return endcaseBy;
    }

    
    public void setEndcaseBy(String endcaseBy) {
        this.endcaseBy = endcaseBy;
    }

    
    public String getEndcaseDepartmentCode() {
        return endcaseDepartmentCode;
    }

    
    public void setEndcaseDepartmentCode(String endcaseDepartmentCode) {
        this.endcaseDepartmentCode = endcaseDepartmentCode;
    }

    
    public String getEndcaseTime() {
        return endcaseTime;
    }

    
    public void setEndcaseTime(String endcaseTime) {
        this.endcaseTime = endcaseTime;
    }

    
    public String getEndcasePigeonholeNo() {
        return endcasePigeonholeNo;
    }

    
    public void setEndcasePigeonholeNo(String endcasePigeonholeNo) {
        this.endcasePigeonholeNo = endcasePigeonholeNo;
    }

    
    public String getEndcaseDesc() {
        return endcaseDesc;
    }

    
    public void setEndcaseDesc(String endcaseDesc) {
        this.endcaseDesc = endcaseDesc;
    }

    
    public String getRevokecaseInd() {
        return revokecaseInd;
    }

    
    public void setRevokecaseInd(String revokecaseInd) {
        this.revokecaseInd = revokecaseInd;
    }

    
    public String getRevokecaseBy() {
        return revokecaseBy;
    }

    
    public void setRevokecaseBy(String revokecaseBy) {
        this.revokecaseBy = revokecaseBy;
    }

    
    public String getRevokecaseTime() {
        return revokecaseTime;
    }

    
    public void setRevokecaseTime(String revokecaseTime) {
        this.revokecaseTime = revokecaseTime;
    }

    
    public String getRevokecaseDesc() {
        return revokecaseDesc;
    }

    
    public void setRevokecaseDesc(String revokecaseDesc) {
        this.revokecaseDesc = revokecaseDesc;
    }

    
    public String getHugeLossCode() {
        return hugeLossCode;
    }

    
    public void setHugeLossCode(String hugeLossCode) {
        this.hugeLossCode = hugeLossCode;
    }

    
    public String getTotalVerifiedTime() {
        return totalVerifiedTime;
    }

    
    public void setTotalVerifiedTime(String totalVerifiedTime) {
        this.totalVerifiedTime = totalVerifiedTime;
    }

    
    public String getCollidedThirdInd() {
        return collidedThirdInd;
    }

    
    public void setCollidedThirdInd(String collidedThirdInd) {
        this.collidedThirdInd = collidedThirdInd;
    }

    
    public String getActiontype() {
        return actiontype;
    }

    
    public void setActiontype(String actiontype) {
        this.actiontype = actiontype;
    }

    
    public String getSubrogationFlag() {
        return subrogationFlag;
    }

    
    public void setSubrogationFlag(String subrogationFlag) {
        this.subrogationFlag = subrogationFlag;
    }

    
    public String getRemark() {
        return remark;
    }

    
    public void setRemark(String remark) {
        this.remark = remark;
    }

    
    public String getCreateBy() {
        return createBy;
    }

    
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    
    public String getCreateTime() {
        return createTime;
    }

    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    
    public String getUpdateBy() {
        return updateBy;
    }

    
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    
    public String getUpdateTime() {
        return updateTime;
    }

    
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
   
    
}
