package ins.sino.claimcar.pinganunion.vo.payment;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 平安联盟中心-支付信息查询接口-查询结果
 *
 * @author mfn
 * @date 2020/8/6 15:25
 */
public class UnionPaymentResultDto implements Serializable {

    private static final long serialVersionUID = -2457178248221967084L;

    /**  支付信息主键  支付信息主键  是否非空：Y  编码：N */
    private String idClmPaymentResult;
    /**  保单号  保单号  是否非空：Y  编码：N */
    private String policyNo;
    /**  赔案号  赔案号  是否非空：Y  编码：N */
    private String caseNo;
    /**  支付类型  01-赔款/02-费用  是否非空：Y  编码：Y */
    private String payType;
    /**  报案号    是否非空：Y  编码：N */
    private String reportNo;
    /**  赔付次数    是否非空：Y  编码：N */
    private Integer caseTimes;
    /**  支付信息类型  00-整案，01-先结，02-垫付，03-预付，04-减损费用(调查),05-垫付退款（收款无需支付）,06-追偿费用支出,07-追偿收入（赔款、费用收入，收款无需支付）,08-保单先结  是否非空：Y  编码：Y */
    private String paymentInfoType;
    /**  赔款类型次数预陪/垫付/追偿 次数  paymentInfoType=02、03、06、07时不为空  是否非空：CY  编码：Y */
    private String subTimes;
    /**  支付总金额    是否非空：Y  编码：N */
    private BigDecimal paymentAmount;
    /**  客户名称    是否非空：Y  编码：N */
    private String clientName;
    /**  客户证件类型  个人时非空  代码定义3.10  是否非空：CY  编码：Y */
    private String clientCertificateType;
    /**  客户证件号码  个人时非空  是否非空：CY  编码：N */
    private String clientCertificateNo;
    /**  客户联行号  paymentInfoType=05、07时可为空，账号302开头的为中信银行  是否非空：CY  编码：N */
    private String clientBankCode;
    /**  客户开户银行    是否非空：Y  编码：N */
    private String clientBankName;
    /**  客户帐号  paymentInfoType=05、07时可为空  是否非空：CY  编码：N */
    private String clientBankAccount;
    /**  客户类型  01-被保险人,02-受益人,03-受害人,04-法院,05-被委托人,06-代理索赔修理厂,10-交强险垫/预付,11-理赔费用支出对象,99-其他  是否非空：Y  编码：Y */
    private String clientType;
    /**  支付方式  01-公司柜面;02-实时支付03-批量转账,  是否非空：Y  编码：Y */
    private String collectPayApproach;
    /**  帐号类型  个人帐号=1,公司帐号=0  是否非空：Y  编码：Y */
    private String bankAccountAttribute;
    /**  客户手机    是否非空：N  编码：N */
    private String clientMobile;
    /**  备注/附言    是否非空：N  编码：N */
    private String remark;
    /**  组织机构号  团体时非空  是否非空：CY  编码：N */
    private String organizeCode;
    /**  原支付信息主键  支付退回重下发后，该字段为原支付信息主键，非必要参数，按需使用   是否非空：CY  编码：N */
    private String idClmPaymentResultOld;

    public String getIdClmPaymentResult() {
        return idClmPaymentResult;
    }

    public void setIdClmPaymentResult(String idClmPaymentResult) {
        this.idClmPaymentResult = idClmPaymentResult;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

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

    public String getPaymentInfoType() {
        return paymentInfoType;
    }

    public void setPaymentInfoType(String paymentInfoType) {
        this.paymentInfoType = paymentInfoType;
    }

    public String getSubTimes() {
		return subTimes;
	}
    
    public void setSubTimes(String subTimes) {
		this.subTimes = subTimes;
	}

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientCertificateType() {
        return clientCertificateType;
    }

    public void setClientCertificateType(String clientCertificateType) {
        this.clientCertificateType = clientCertificateType;
    }

    public String getClientCertificateNo() {
        return clientCertificateNo;
    }

    public void setClientCertificateNo(String clientCertificateNo) {
        this.clientCertificateNo = clientCertificateNo;
    }

    public String getClientBankCode() {
        return clientBankCode;
    }

    public void setClientBankCode(String clientBankCode) {
        this.clientBankCode = clientBankCode;
    }

    public String getClientBankName() {
        return clientBankName;
    }

    public void setClientBankName(String clientBankName) {
        this.clientBankName = clientBankName;
    }

    public String getClientBankAccount() {
        return clientBankAccount;
    }

    public void setClientBankAccount(String clientBankAccount) {
        this.clientBankAccount = clientBankAccount;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getCollectPayApproach() {
        return collectPayApproach;
    }

    public void setCollectPayApproach(String collectPayApproach) {
        this.collectPayApproach = collectPayApproach;
    }

    public String getBankAccountAttribute() {
        return bankAccountAttribute;
    }

    public void setBankAccountAttribute(String bankAccountAttribute) {
        this.bankAccountAttribute = bankAccountAttribute;
    }

    public String getClientMobile() {
        return clientMobile;
    }

    public void setClientMobile(String clientMobile) {
        this.clientMobile = clientMobile;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrganizeCode() {
        return organizeCode;
    }

    public void setOrganizeCode(String organizeCode) {
        this.organizeCode = organizeCode;
    }

    public String getIdClmPaymentResultOld() {
        return idClmPaymentResultOld;
    }

    public void setIdClmPaymentResultOld(String idClmPaymentResultOld) {
        this.idClmPaymentResultOld = idClmPaymentResultOld;
    }
}
