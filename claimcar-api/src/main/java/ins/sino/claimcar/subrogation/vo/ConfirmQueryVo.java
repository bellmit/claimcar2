package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;

/**
 *  开始追偿确认查询页面VO
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class ConfirmQueryVo implements Serializable{

	/**  */
	private static final long serialVersionUID = 1L;
    private String comCode;
	private String accountsNo;   //结算码
	private String registNo;   //报案号
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getAccountsNo() {
		return accountsNo;
	}
	public void setAccountsNo(String accountsNo) {
		this.accountsNo = accountsNo;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	
	
	

}
