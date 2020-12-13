package ins.sino.claimcar.moblie.loss.vo;


import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("FEEINFO")
public class DefLossFeeInfoVo implements Serializable{
	private static final long serialVersionUID = -7063363534115467065L;
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
	@XStreamAlias("PAYEE")
	private String payee;	//收款人名称
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
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
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public Double getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(Double feeAmount) {
		this.feeAmount = feeAmount;
	}
}
