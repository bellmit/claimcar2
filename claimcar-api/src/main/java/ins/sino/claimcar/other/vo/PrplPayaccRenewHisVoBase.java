/******************************************************************************
* CREATETIME : 2019年3月17日 上午10:47:26
******************************************************************************/
package ins.sino.claimcar.other.vo;

import java.math.BigDecimal;
import java.util.Date;


/**
 * <pre></pre>
 * @author ★XHY
 */
public class PrplPayaccRenewHisVoBase  implements java.io.Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;

	private BigDecimal id;
	private String registNo;
	private String accountNo;
	
	public String getAccountNo() {
		return accountNo;
	}

	
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	
	public String getPatAccountNo() {
		return patAccountNo;
	}

	
	public void setPatAccountNo(String patAccountNo) {
		this.patAccountNo = patAccountNo;
	}


	private String patAccountNo;

	private String payeeName;
	private String patPayName;
	private String createUser;
	private Date createTime;
	private Long payeeId;
	private String patId;
	
	public BigDecimal getId() {
		return id;
	}
	
	public void setId(BigDecimal id) {
		this.id = id;
	}
	
	public String getRegistNo() {
		return registNo;
	}
	
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	
	
	
	public String getPayeeName() {
		return payeeName;
	}
	
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	
	public String getPatPayName() {
		return patPayName;
	}
	
	public void setPatPayName(String patPayName) {
		this.patPayName = patPayName;
	}
	
	public String getCreateUser() {
		return createUser;
	}
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Long getPayeeId() {
		return payeeId;
	}
	
	public void setPayeeId(Long payeeId) {
		this.payeeId = payeeId;
	}
	
	public String getPatId() {
		return patId;
	}
	
	public void setPatId(String patId) {
		this.patId = patId;
	}
	
}
