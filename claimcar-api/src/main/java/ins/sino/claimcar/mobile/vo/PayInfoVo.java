package ins.sino.claimcar.mobile.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("PAYINFO")
public class PayInfoVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ACCOUNTNAME")
	private String accountName; //支付对象
	
	@XStreamAlias("PAYDATE")
	private String payDate; //支付时间
	
	@XStreamAlias("LOSSAMOUNT")
	private String lossAmount; //支付金额
	
	@XStreamAlias("LOSSNAME")
	private String lossName; //标的名称

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getLossAmount() {
		return lossAmount;
	}

	public void setLossAmount(String lossAmount) {
		this.lossAmount = lossAmount;
	}

	public String getLossName() {
		return lossName;
	}

	public void setLossName(String lossName) {
		this.lossName = lossName;
	}
	
	
}
