
package ins.sino.claimcar.moblie.lossPerson.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 费用赔款信息
 * @author j2eel
 *
 */
@XStreamAlias("FEEINFOS")
public class FeeInfoVo implements Serializable {
	private static final long serialVersionUID = -1568493102386689963L;
	
	@XStreamAlias("FEEID")
	private String feeId;
	@XStreamAlias("KINDCODE")
	private String kindCode;
	@XStreamAlias("FEETYPE")
	private String feeType;
	@XStreamAlias("ACCOUNTID")
	private String accountId;
	@XStreamAlias("AMOUNT")
	private String amount;
	@XStreamAlias("SUMAMOUNT")
	private String sumAmount;

	public String getKindCode() {
		return kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}


	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
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

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(String sumAmount) {
		this.sumAmount = sumAmount;
	}

	
}
