package ins.sino.claimcar.mobile.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 
 * 收款人信息（快赔请求理赔）
 * @author ★zhujunde
 */
@XStreamAlias("ACCOUNTINFO")
public class AccountResVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("REGISTNO")
    private String registNo; //报案号
	
	@XStreamAlias("ACCOUNTID")
	private String accountId; //理赔收款人ID
	
	@XStreamAlias("ACCOUNTSERIALNO")
	private String accountSerialNo; //移动终端收款人序号
	
	
	
	
    public String getRegistNo() {
        return registNo;
    }

    
    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }

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
