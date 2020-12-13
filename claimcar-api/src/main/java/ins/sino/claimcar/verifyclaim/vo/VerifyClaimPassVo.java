package ins.sino.claimcar.verifyclaim.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 核赔通过清单查询
 * 
 * <pre></pre>
 * @author ★LinYi
 */
public class VerifyClaimPassVo implements java.io.Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    private String registNo;//报案号
    private Date verifyClaimPassTimeStart;
    private Date verifyClaimPassTimeEnd;
    private String claimNo;// 立案号
    private String compensateNo;// 计算书号
    private String insuredName;// 被保险人
    private String compensateType;// 计算书类型
    private Date createTime;// 流入时间
    private Date createTimeStart;
    private Date createTimeEnd;
    private Date handleTime;//核赔通过时间
    private String policyNo;//保单号
    private BigDecimal sumRealPay;// 金额
    private String payeeName;// 赔款接收人
    private String bankOutLets;// 赔款接收人银行
    private String accountNo;// 赔款接收人帐号
    private String payStatus;// 是否已赔付
    private String comCode;// 机构代码
    private String handleUser;//核赔通过处理人
    private String autoType;//是否自动核赔标志
    
    
    public String getRegistNo() {
        return registNo;
    }
    
    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }
    
    public Date getVerifyClaimPassTimeStart() {
        return verifyClaimPassTimeStart;
    }
    
    public void setVerifyClaimPassTimeStart(Date verifyClaimPassTimeStart) {
        this.verifyClaimPassTimeStart = verifyClaimPassTimeStart;
    }
    
    public Date getVerifyClaimPassTimeEnd() {
        return verifyClaimPassTimeEnd;
    }
    
    public void setVerifyClaimPassTimeEnd(Date verifyClaimPassTimeEnd) {
        this.verifyClaimPassTimeEnd = verifyClaimPassTimeEnd;
    }
    
    public String getClaimNo() {
        return claimNo;
    }
    
    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }
    
    public String getCompensateNo() {
        return compensateNo;
    }
    
    public void setCompensateNo(String compensateNo) {
        this.compensateNo = compensateNo;
    }
    
    public String getInsuredName() {
        return insuredName;
    }
    
    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }
    
    public String getCompensateType() {
        return compensateType;
    }

    public void setCompensateType(String compensateType) {
        this.compensateType = compensateType;
    }

    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getCreateTimeStart() {
        return createTimeStart;
    }
    
    public void setCreateTimeStart(Date createTimeStart) {
        this.createTimeStart = createTimeStart;
    }
    
    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }
    
    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }
    
    public Date getHandleTime() {
        return handleTime;
    }
    
    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }
    
    public String getPolicyNo() {
        return policyNo;
    }
    
    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }
    
    public BigDecimal getSumRealPay() {
        return sumRealPay;
    }
    
    public void setSumRealPay(BigDecimal sumRealPay) {
        this.sumRealPay = sumRealPay;
    }

    public String getPayeeName() {
        return payeeName;
    }
    
    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }
    
    public String getBankOutLets() {
        return bankOutLets;
    }
    
    public void setBankOutLets(String bankOutLets) {
        this.bankOutLets = bankOutLets;
    }
    
    public String getAccountNo() {
        return accountNo;
    }
    
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
    
    public String getPayStatus() {
        return payStatus;
    }
    
    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    
    public String getComCode() {
        return comCode;
    }

    
    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

	public String getHandleUser() {
		return handleUser;
	}

	public void setHandleUser(String handleUser) {
		this.handleUser = handleUser;
	}

	public String getAutoType() {
		return autoType;
	}

	public void setAutoType(String autoType) {
		this.autoType = autoType;
	}
    
    
    
}
