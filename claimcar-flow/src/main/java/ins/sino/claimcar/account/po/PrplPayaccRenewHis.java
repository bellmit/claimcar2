/******************************************************************************
* CREATETIME : 2019年3月17日 上午10:30:22
******************************************************************************/
package ins.sino.claimcar.account.po;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * <pre></pre>
 * @author ★XHY
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLPAYBANKHIS_PK", allocationSize = 10)
@Table(name = "PRPLPAYACCRENEWHIS")
public class PrplPayaccRenewHis implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private BigDecimal id;
	private String registNo;
	private String accountNo;
	private String patAccountNo;
	private String payeeName;
	private String patPayName;
	private String createUser;
	private Date createTime;
	private Long payeeId;
	private String patId;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")   //自动生成主键
	@Column(name = "ID", unique = true, nullable = false, scale=0)
	public BigDecimal getId() {
		return id;
	}
	
	public void setId(BigDecimal id) {
		this.id = id;
	}

	@Column(name = "REGISTNO", nullable = false, length=22)
	public String getRegistNo() {
		return registNo;
	}
	
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	
	@Column(name = "ACCOUNTNO", length=35)
	public String getAccountNo() {
		return accountNo;
	}

	
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}


	@Column(name = "PATACCOUNTNO", length=35)
	public String getPatAccountNo() {
		return patAccountNo;
	}

	
	public void setPatAccountNo(String patAccountNo) {
		this.patAccountNo = patAccountNo;
	}
	
	
	@Column(name = "PAYNAME", length=200)
	public String getPayeeName() {
		return payeeName;
	}
	
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	
	@Column(name = "PATPAYNAME", length=200)
	public String getPatPayName() {
		return patPayName;
	}
	
	public void setPatPayName(String patPayName) {
		this.patPayName = patPayName;
	}
	
	@Column(name = "CREATEUSER", length=10)
	public String getCreateUser() {
		return createUser;
	}
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME", length=7)
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "PAYEEID", length=19)
	public Long getPayeeId() {
		return payeeId;
	}
	
	public void setPayeeId(Long payeeId) {
		this.payeeId = payeeId;
	}

	@Column(name = "PATID", length=40)
	public String getPatId() {
		return patId;
	}
	
	public void setPatId(String patId) {
		this.patId = patId;
	}

}
