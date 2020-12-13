package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;


import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("FEEINFO")
public class DefLossFeeInfoVo {

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("SERIALNO")
	private String serialNo;	//序号
	@XStreamAlias("KINDCODE")
	private String kindCode;	//险别
	@XStreamAlias("FEETYPE")
	private String feeType;	//费用类型
	@XStreamAlias("FEEAMOUNT")
	private Double feeAmount;	//费用金额
	@XStreamAlias("ACCOUNTID")
	private String accountId;	//理赔收款人ID
	@XStreamAlias("PAYEETYPE")
	private String payeeType;	//收款人类型
	@XStreamAlias("NAME")
	private String name;	//姓名
	@XStreamAlias("IDENTIFYNUMBER")
    private String identifyNumber;    //身份证号码
	@XStreamAlias("BANKNAME")
    private String bankName;    //开户银行
	@XStreamAlias("ACCOUNTNAME")
    private String accountName;    //账户名
	@XStreamAlias("ACCOUNTNO")
    private String accountNo;    //银行账户
    @XStreamAlias("PHONE")
    private String phone;    //联系电话
    
    public String getKindCode() {
        return kindCode;
    }
    
    public void setKindCode(String kindCode) {
        this.kindCode = kindCode;
    }
    
    public String getFeeType() {
        return feeType;
    }
    
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
    
    public Double getFeeAmount() {
        return feeAmount;
    }
    
    public void setFeeAmount(Double feeAmount) {
        this.feeAmount = feeAmount;
    }
    
    public String getPayeeType() {
        return payeeType;
    }
    
    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getIdentifyNumber() {
        return identifyNumber;
    }
    
    public void setIdentifyNumber(String identifyNumber) {
        this.identifyNumber = identifyNumber;
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
    
    public String getAccountNo() {
        return accountNo;
    }
    
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
    
}
