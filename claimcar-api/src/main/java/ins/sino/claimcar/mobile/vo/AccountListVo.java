package ins.sino.claimcar.mobile.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ACCOUNTLIST")
public class AccountListVo  implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;


	/*
	 * 收款人信息
	 */
	@XStreamAlias("ACCOUNT")
	private AccountVo account;


	public AccountVo getAccount() {
		return account;
	}


	public void setAccount(AccountVo account) {
		this.account = account;
	}



	
	
	
}
