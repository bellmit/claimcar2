package ins.sino.claimcar.mobile.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("ACCOUNTINFO")
public class AccountInfoResVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ACCOUNTID")
	private String accountId; //理赔收款人ID
	
	@XStreamAlias("ACCOUNTSERIALNO")
	private String accountSerialNo; //移动终端收款人序号
	
	
	
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountSerialNo() {
		return accountSerialNo;
	}

	public void setAccountSerialNo(String accountSerialNo) {
		this.accountSerialNo = accountSerialNo;
	}

	
}
