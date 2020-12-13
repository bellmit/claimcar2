package ins.sino.claimcar.newpayment.vo;

import java.io.Serializable;

/**
 * 收付退票至理赔，信息接收实体类
 * @author maofengning 2020年4月29日15:18:00
 */
public class AccRollbackAccountMainDto implements Serializable {
    private static final long serialVersionUID = -2230061161420051626L;

    /** 业务号 必录标识:Y 说明:计算书号、预赔款 */
    private String certiNo;
    /** 序号 必录标识：Y 说明： */
    private Integer serialNo;
    /** 收付原因 必录标识:Y 说明: */
    private String payRefReason;
    /** 退票原因 必录标识:Y 说明: */
    private String errorMessage;
    /** 银行代码 必录标识:Y 说明: */
    private String bankCode;
    /** 开户银行名称 必录标识:Y 说明: */
    private String bankName;
    /** 账户名称 必录标识:Y 说明: */
    private String accountName;
    /** 银行账号 必录标识:N 说明: */
    private String accountCode;
    /** 收款人ID 必录表示:Y 说明: */
    private String payeeId;
    /** 账户币种 必录标识:N 说明: */
    private String payeeCurrency;
    /** 账户类型 必录标识:N 说明: */
    private String accountType;
    /** 卡折类型 必录标识:N 说明:理赔暂时传送空 */
    private String bankCardType;
    /** 银行省 必录标识:N 说明: */
    private String bankProvinceCode;
    /** 银行市 必录标识:N 说明: */
    private String bankCityCode;
    /** 退票时间 必录标识:Y 说明: */
    private String rollBackTime;
    /** 状态码 必录标识:Y 说明:0：已修改 2：未修改 */
    private String status;
    /** 申请人 必录标识:N 说明:收付系统当前登录用户 */
    private String rollBackCode;
    /** 错误类型 必录标识:N 说明:3：支付失败  6： 退票 */
    private String errorType;
    /** 修改类型 必录标识:N 说明:Y预赔 C实赔 Z追偿 P保单 S手续费 */
    private String modifyType;
    /** 是否送资金 必录标识:Y 说明:0-否，1-是 */
    private String isAutoPay;

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getCertiNo() {
        return certiNo;
    }

    public void setCertiNo(String certiNo) {
        this.certiNo = certiNo;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getPayRefReason() {
        return payRefReason;
    }

    public void setPayRefReason(String payRefReason) {
        this.payRefReason = payRefReason;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getPayeeCurrency() {
        return payeeCurrency;
    }

    public void setPayeeCurrency(String payeeCurrency) {
        this.payeeCurrency = payeeCurrency;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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

    public String getBankCityCode() {
        return bankCityCode;
    }

    public void setBankCityCode(String bankCityCode) {
        this.bankCityCode = bankCityCode;
    }

    public String getRollBackTime() {
        return rollBackTime;
    }

    public void setRollBackTime(String rollBackTime) {
        this.rollBackTime = rollBackTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRollBackCode() {
        return rollBackCode;
    }

    public void setRollBackCode(String rollBackCode) {
        this.rollBackCode = rollBackCode;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getModifyType() {
        return modifyType;
    }

    public void setModifyType(String modifyType) {
        this.modifyType = modifyType;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getIsAutoPay() {
        return isAutoPay;
    }

    public void setIsAutoPay(String isAutoPay) {
        this.isAutoPay = isAutoPay;
    }
}
