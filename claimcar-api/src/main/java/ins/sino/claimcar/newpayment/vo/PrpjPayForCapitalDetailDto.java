package ins.sino.claimcar.newpayment.vo;

import java.io.Serializable;

/**
 * 支付信息详细信息实体类 退票送收付数据封装实体类
 * @author maofengning 2020年5月2日17:12:05
 */
public class PrpjPayForCapitalDetailDto implements Serializable {

    private static final long serialVersionUID = 5548779504655250305L;

    /** 业务号 必录标识:Y 说明: */
    private String certiNo;
    /** 序号 必录标识:Y 说明: */
    private Integer serialNo;
    /** 是否委托付款标志 必录标识:N 说明:0否1是 */
    private String entrustRecFlag;
    /** 结算方式 必录标识:N 说明: */
    private String payWay;
    /** 收款方银行类别编码 必录标识:Y 说明: */
    private String bankType;
    /** 收款方银行类别名称 必录标识:Y 说明: */
    private String bankTypeName;
    /** 收款账户名称 必录标识:Y 说明: */
    private String accountName;
    /** 收款账号 必录标识:Y 说明: */
    private String accountCode;
    /** 收款方开户行联行号 必录标识:Y 说明: */
    private String bankCode;
    /** 收款方开户行名称 必录标识:Y 说明: */
    private String bankName;
    /** 收款账户省代码 必录标识:N 说明: */
    private String bankProvinceCode;
    /** 收款账户省名称 必录标识:N 说明: */
    private String bankProvinceName;
    /** 收款方开户行地区(市) 必录标识:N 说明: */
    private String bankCityCode;
    /** 开户行地区(市)名称 必录标识:N 说明: */
    private String bankCityName;
    /** 收款账号币种 必录标识:Y 说明: */
    private String payeeCurrency;
    /** 公私标志 必录标识:Y 说明:1-对私;0-对公 */
    private String accountType;
    /** 卡折类型 必录标识:Y 说明:见码表4.1.7 */
    private String bankCardType;
    /** 收款人手机号 必录标识:N 说明: */
    private String payeePhone;
    /** 邮件地址 必录标识:N 说明: */
    private String email;
    /** 收款人证件类型 必录标识:N 说明:见码表4.1.6 */
    private String payeeIdType;
    /** 收款人证件号 必录标识:N 说明: */
    private String payeeId;
    /** 银行摘要 必录标识:N 说明: */
    private String abstractContent;
    /** 是否自动支付 必录标识:Y 说明: */
    private String isAutoPay;
    /** 快速理赔标志 必录标识:Y 说明: */
    private String isSimpleCase;

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

    public String getEntrustRecFlag() {
        return entrustRecFlag;
    }

    public void setEntrustRecFlag(String entrustRecFlag) {
        this.entrustRecFlag = entrustRecFlag;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
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

    public String getPayeeIdType() {
        return payeeIdType;
    }

    public void setPayeeIdType(String payeeIdType) {
        this.payeeIdType = payeeIdType;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getAbstractContent() {
        return abstractContent;
    }

    public void setAbstractContent(String abstractContent) {
        this.abstractContent = abstractContent;
    }

    public String getIsAutoPay() {
        return isAutoPay;
    }

    public void setIsAutoPay(String isAutoPay) {
        this.isAutoPay = isAutoPay;
    }

    public String getIsSimpleCase() {
        return isSimpleCase;
    }

    public void setIsSimpleCase(String isSimpleCase) {
        this.isSimpleCase = isSimpleCase;
    }
}
