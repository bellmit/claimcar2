package ins.sino.claimcar.newpayment.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/** 理赔应收应付送收付实体类
 * @author maofengning 2020年5月2日16:53:41
 */
public class PrpJlossPlanDto implements Serializable {

    private static final long serialVersionUID = 5337784458260843959L;

    /** 业务类型 必录标识:Y 说明:见码表4.1.5 */
    private String certiType;
    /** 业务号 必录标识:Y 说明:计算书号，预赔号 */
    private String certiNo;
    /** 保单号 必录标识:Y 说明: */
    private String policyNo;
    /** 报案号 必录标识:Y 说明: */
    private String registNo;
    /** 支付对象序号 必录标识:Y 说明: */
    private Integer serialNo;
    /** 立案号 必录标识:Y 说明: */
    private String claimNo;
    /** 赔付类型 必录标识:Y 说明:见码表4.1.2 */
    private String lossType;
    /** 收付原因 必录标识:Y 说明:见码表4.1.1 */
    private String payRefReason;
    /** 险类 必录标识:Y 说明: */
    private String classCode;
    /** 险种 必录标识:Y 说明: */
    private String riskCode;
    /** 投保人代码 必录标识:Y 说明: */
    private String appliCode;
    /** 投保人名称 必录标识:Y 说明: */
    private String appliName;
    /** 被保人代码 必录标识:Y 说明: */
    private String insuredCode;
    /** 被保险人名称 必录标识:Y 说明: */
    private String insuredName;
    /** 起保日期 必录标识:Y 说明:yyyy-MM-dd */
    private String startDate;
    /** 终保日期 必录标识:Y 说明:yyyy-MM-dd */
    private String endDate;
    /** 核赔日期 必录标识:Y 说明: */
    private String underWriteDate;
    /** 结案日期 必录标识:Y 说明: */
    private String endCaseDate;
    /** 应收币别 必录标识:Y 说明: */
    private String currency;
    /** 归属机构 必录标识:Y 说明: */
    private String comCode;
    /** 出单机构 必录标识:Y 说明: */
    private String makeCom;
    /** 合同号 必录标识:N 说明: */
    private String contractNo;
    /** 代理人代码 必录标识:N 说明: */
    private String agentCode;
    /** 代理人名称 必录标识:N 说明: */
    private String AgentName;
    /** 业务归属人员 必录标识:Y 说明: */
    private String handler1Code;
    /** 业务归属名称 必录标识:Y 说明: */
    private String handler1CodeName;
    /** 经办人 必录标识:Y 说明: */
    private String handlerCode;
    /** 经办人名称 必录标识:N 说明: */
    private String handlerCodeName;
    /** 支付机构代码 必录标识:N 说明:一般为归属机构通赔除外 */
    private String payComCode;
    /** 操作员工号 必录标识:Y 说明: */
    private String operateCode;
    /** 操作员登录机构 必录标识:Y 说明: */
    private String operateComCode;
    /** 平台结算码 必录标识:N 说明:P6B、P6C、P6D、P6E时必传 */
    private String voucherNo2;
    /** 业务来源 必录标识:Y 说明:见码表4.1.11 */
    private String businessNature;
    /** 业务板块 必录标识:Y 说明:见码表4.1.12 */
    private String businessplate;
    /** 业务性质 必录标识:Y 说明:见码表4.1.13 */
    private String policySort;
    /** 车牌号 必录标识:N 说明:车险时必填 */
    private String licenseNo;
    /** 车辆性质 必录标识:N 说明:车险时必填,车辆9大类，车型 */
    private String carNatureCode;
    /** 联共保标识 必录标识:Y 说明:见码表4.1.3 */
    private String coinsFlag;
    /** 共保人代码 必录标识:N 说明: */
    private String coinsCode;
    /** 共转联标识 必录标识:Y 说明:见码表4.1.9 */
    private String coinsTypeLb;
    /** 结算对象标识 必录标识:Y 说明:见码表4.1.10 */
    private String coinsPayType;
    /** 共保人名称 必录标识:N 说明: */
    private String coinsName;
    /** 共保比例 必录标识:N 说明: */
    private BigDecimal coinsRate;
    /** 共保类型 必录标识:N 说明:见码表4.1.4 */
    private String coinsType;
    /** 快速理赔标志 必录标识:Y 说明:1-是；0-否 */
    private String isSimpleCase;
    /** 收付金额 必录标识:Y 说明: */
    private BigDecimal planFee;
    /** 数据来源 必录标识:Y 说明:车：car;非车：noCar */
    private String systemFlag;
    /** 境内外标识 必录标识:Y 说明:D-境内；F-境外 */
    private String locationFlag;
    /** 核算单位 必录标识:Y 说明: */
    private String centerCode;
    /** 使用性质 必录标识:N 说明:车险必填，家庭自用， */
    private String usenaturecode;
    /** 拆分险别标识 必录标识:Y 说明:1-拆分到险别，2-未拆分到险别 */
    private String splitRiskFlag;
    /** 支付对象代码 必录标识:N 说明: */
    private String payObjectCode;
    /** 支付对象名称 必录标识:N 说明: */
    private String payObjectName;
    /** 收款方联行号 必录标识:N 说明: */
    private String bankCode;
    /** 银行名称 必录标识:N 说明: */
    private String bankName;
    /** 收款方银行类型 必录标识:N 说明: */
    private String bankType;
    /** 银行类别名称 必录标识:N 说明: */
    private String bankTypeName;
    /** 收款方银行账号 必录标识:N 说明: */
    private String accountCode;
    /** 收款方账户名  必录标识:N 说明: */
    private String accountName;
    /** 收款方类型 必录标识:N 说明:公私标志（0：对私1：对公） */
    private String accountType;
    /** 收款方证件类型 必录标识:N 说明:见码表4.1.6 */
    private String certificateType;
    /** 收款方证件号 必录标识:N 说明: */
    private String certificateCode;
    /** 收款人手机号码 必录标识:N 说明: */
    private String payeePhone;
    /** 收款人邮件地址 必录标识:N 说明: */
    private String email;
    /** 银行摘要 必录标识:N 说明:理赔系统录入 */
    private String abstractContent;
    /** 收款账户币种 必录标识:Y 说明: */
    private String payeeCurrency;
    /** 卡折类型 必录标识:N 说明:见码表4.1.7 */
    private String bankCardType;
    /** 收款账户省 必录标识:N 说明: */
    private String bankProvinceCode;
    /** 收款账户省 必录标识:N 说明: */
    private String bankProvinceName;
    /** 收款方开户行地区(市) 必录标识:N 说明: */
    private String bankCityCode;
    /** 开户行地区(市)名称 必录标识:N 说明: */
    private String bankCityName;
    /** 委托收付标志 必录标识:Y 说明:Y-是；N-否 */
    private String entrustrecFlag;
    /** 用途 必录标识:Y 说明: */
    private String usage;
    /** 备注 必录标识:N 说明: */
    private String otherRemark;
    /** 短信内容 必录标识:Y 说明: */
    private String messageContent;
    /** 退回重付标识 必录标识:N 说明:1-是；0-否 */
    private String repayType;
    /** 退回重付关联ID 必录标识:N 说明: */
    private String correlateID;
    /** 是否直付例外 必录标识:Y 说明:是否直付例外（0：否 1：是） */
    private String payReasonFlag;
    /** 是否自动支付 必录标识:Y 说明:是否自动支付（0：否 1：是） */
    private String isAutoPay;
    /** 是否加急 必录标识:Y 说明:是否加急（0：否 1：是） */
    private String isExpress;
    /** 结算方式 必录标识:N 说明:12-银企直连，13-线下网银转账 */
    private String payWay;
    /** 预赔结案标识 必录标识:Y 说明: 0-否  1-是 */
    private String yuPayFlag;
    /** 收款人信息prplpaycustom.id  同prplpayment.payeeid */
    private String payeeId;
    /** 损失明细数据对象 */
    private List<PrpJlossPlanSubDto> prpJlossPlanSubDtos;

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getYuPayFlag() {
        return yuPayFlag;
    }

    public void setYuPayFlag(String yuPayFlag) {
        this.yuPayFlag = yuPayFlag;
    }

    public String getCertiType() {
        return certiType;
    }

    public void setCertiType(String certiType) {
        this.certiType = certiType;
    }

    public String getCertiNo() {
        return certiNo;
    }

    public void setCertiNo(String certiNo) {
        this.certiNo = certiNo;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getRegistNo() {
        return registNo;
    }

    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getClaimNo() {
        return claimNo;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public String getLossType() {
        return lossType;
    }

    public void setLossType(String lossType) {
        this.lossType = lossType;
    }

    public String getPayRefReason() {
        return payRefReason;
    }

    public void setPayRefReason(String payRefReason) {
        this.payRefReason = payRefReason;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getRiskCode() {
        return riskCode;
    }

    public void setRiskCode(String riskCode) {
        this.riskCode = riskCode;
    }

    public String getAppliCode() {
        return appliCode;
    }

    public void setAppliCode(String appliCode) {
        this.appliCode = appliCode;
    }

    public String getAppliName() {
        return appliName;
    }

    public void setAppliName(String appliName) {
        this.appliName = appliName;
    }

    public String getInsuredCode() {
        return insuredCode;
    }

    public void setInsuredCode(String insuredCode) {
        this.insuredCode = insuredCode;
    }

    public String getInsuredName() {
        return insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getUnderWriteDate() {
        return underWriteDate;
    }

    public void setUnderWriteDate(String underWriteDate) {
        this.underWriteDate = underWriteDate;
    }

    public String getEndCaseDate() {
        return endCaseDate;
    }

    public void setEndCaseDate(String endCaseDate) {
        this.endCaseDate = endCaseDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    public String getMakeCom() {
        return makeCom;
    }

    public void setMakeCom(String makeCom) {
        this.makeCom = makeCom;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getAgentName() {
        return AgentName;
    }

    public void setAgentName(String agentName) {
        AgentName = agentName;
    }

    public String getHandler1Code() {
        return handler1Code;
    }

    public void setHandler1Code(String handler1Code) {
        this.handler1Code = handler1Code;
    }

    public String getHandler1CodeName() {
        return handler1CodeName;
    }

    public void setHandler1CodeName(String handler1CodeName) {
        this.handler1CodeName = handler1CodeName;
    }

    public String getHandlerCode() {
        return handlerCode;
    }

    public void setHandlerCode(String handlerCode) {
        this.handlerCode = handlerCode;
    }

    public String getHandlerCodeName() {
        return handlerCodeName;
    }

    public void setHandlerCodeName(String handlerCodeName) {
        this.handlerCodeName = handlerCodeName;
    }

    public String getPayComCode() {
        return payComCode;
    }

    public void setPayComCode(String payComCode) {
        this.payComCode = payComCode;
    }

    public String getOperateCode() {
        return operateCode;
    }

    public void setOperateCode(String operateCode) {
        this.operateCode = operateCode;
    }

    public String getOperateComCode() {
        return operateComCode;
    }

    public void setOperateComCode(String operateComCode) {
        this.operateComCode = operateComCode;
    }

    public String getVoucherNo2() {
        return voucherNo2;
    }

    public void setVoucherNo2(String voucherNo2) {
        this.voucherNo2 = voucherNo2;
    }

    public String getBusinessNature() {
        return businessNature;
    }

    public void setBusinessNature(String businessNature) {
        this.businessNature = businessNature;
    }

    public String getBusinessplate() {
        return businessplate;
    }

    public void setBusinessplate(String businessplate) {
        this.businessplate = businessplate;
    }

    public String getPolicySort() {
        return policySort;
    }

    public void setPolicySort(String policySort) {
        this.policySort = policySort;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getCarNatureCode() {
        return carNatureCode;
    }

    public void setCarNatureCode(String carNatureCode) {
        this.carNatureCode = carNatureCode;
    }

    public String getCoinsFlag() {
        return coinsFlag;
    }

    public void setCoinsFlag(String coinsFlag) {
        this.coinsFlag = coinsFlag;
    }

    public String getCoinsCode() {
        return coinsCode;
    }

    public void setCoinsCode(String coinsCode) {
        this.coinsCode = coinsCode;
    }

    public String getCoinsTypeLb() {
        return coinsTypeLb;
    }

    public void setCoinsTypeLb(String coinsTypeLb) {
        this.coinsTypeLb = coinsTypeLb;
    }

    public String getCoinsPayType() {
        return coinsPayType;
    }

    public void setCoinsPayType(String coinsPayType) {
        this.coinsPayType = coinsPayType;
    }

    public String getCoinsName() {
        return coinsName;
    }

    public void setCoinsName(String coinsName) {
        this.coinsName = coinsName;
    }

    public BigDecimal getCoinsRate() {
        return coinsRate;
    }

    public void setCoinsRate(BigDecimal coinsRate) {
        this.coinsRate = coinsRate;
    }

    public String getCoinsType() {
        return coinsType;
    }

    public void setCoinsType(String coinsType) {
        this.coinsType = coinsType;
    }

    public String getIsSimpleCase() {
        return isSimpleCase;
    }

    public void setIsSimpleCase(String isSimpleCase) {
        this.isSimpleCase = isSimpleCase;
    }

    public BigDecimal getPlanFee() {
        return planFee;
    }

    public void setPlanFee(BigDecimal planFee) {
        this.planFee = planFee;
    }

    public String getSystemFlag() {
        return systemFlag;
    }

    public void setSystemFlag(String systemFlag) {
        this.systemFlag = systemFlag;
    }

    public String getLocationFlag() {
        return locationFlag;
    }

    public void setLocationFlag(String locationFlag) {
        this.locationFlag = locationFlag;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }

    public String getUsenaturecode() {
        return usenaturecode;
    }

    public void setUsenaturecode(String usenaturecode) {
        this.usenaturecode = usenaturecode;
    }

    public String getSplitRiskFlag() {
        return splitRiskFlag;
    }

    public void setSplitRiskFlag(String splitRiskFlag) {
        this.splitRiskFlag = splitRiskFlag;
    }

    public String getPayObjectCode() {
        return payObjectCode;
    }

    public void setPayObjectCode(String payObjectCode) {
        this.payObjectCode = payObjectCode;
    }

    public String getPayObjectName() {
        return payObjectName;
    }

    public void setPayObjectName(String payObjectName) {
        this.payObjectName = payObjectName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankTypeName() {
        return bankTypeName;
    }

    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateCode() {
        return certificateCode;
    }

    public void setCertificateCode(String certificateCode) {
        this.certificateCode = certificateCode;
    }

    public String getPayeePhone() {
        return payeePhone;
    }

    public void setPayeePhone(String payeePhone) {
        this.payeePhone = payeePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbstractContent() {
        return abstractContent;
    }

    public void setAbstractContent(String abstractContent) {
        this.abstractContent = abstractContent;
    }

    public String getPayeeCurrency() {
        return payeeCurrency;
    }

    public void setPayeeCurrency(String payeeCurrency) {
        this.payeeCurrency = payeeCurrency;
    }

    public String getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(String bankCardType) {
        this.bankCardType = bankCardType;
    }

    public String getBankProvinceCode() {
        return bankProvinceCode;
    }

    public void setBankProvinceCode(String bankProvinceCode) {
        this.bankProvinceCode = bankProvinceCode;
    }

    public String getBankProvinceName() {
        return bankProvinceName;
    }

    public void setBankProvinceName(String bankProvinceName) {
        this.bankProvinceName = bankProvinceName;
    }

    public String getBankCityCode() {
        return bankCityCode;
    }

    public void setBankCityCode(String bankCityCode) {
        this.bankCityCode = bankCityCode;
    }

    public String getBankCityName() {
        return bankCityName;
    }

    public void setBankCityName(String bankCityName) {
        this.bankCityName = bankCityName;
    }

    public String getEntrustrecFlag() {
        return entrustrecFlag;
    }

    public void setEntrustrecFlag(String entrustrecFlag) {
        this.entrustrecFlag = entrustrecFlag;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getOtherRemark() {
        return otherRemark;
    }

    public void setOtherRemark(String otherRemark) {
        this.otherRemark = otherRemark;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getCorrelateID() {
        return correlateID;
    }

    public void setCorrelateID(String correlateID) {
        this.correlateID = correlateID;
    }

    public String getPayReasonFlag() {
        return payReasonFlag;
    }

    public void setPayReasonFlag(String payReasonFlag) {
        this.payReasonFlag = payReasonFlag;
    }

    public String getIsAutoPay() {
        return isAutoPay;
    }

    public void setIsAutoPay(String isAutoPay) {
        this.isAutoPay = isAutoPay;
    }

    public String getIsExpress() {
        return isExpress;
    }

    public void setIsExpress(String isExpress) {
        this.isExpress = isExpress;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public List<PrpJlossPlanSubDto> getPrpJlossPlanSubDtos() {
        return prpJlossPlanSubDtos;
    }

    public void setPrpJlossPlanSubDtos(List<PrpJlossPlanSubDto> prpJlossPlanSubDtos) {
        this.prpJlossPlanSubDtos = prpJlossPlanSubDtos;
    }

}
