/******************************************************************************
* CREATETIME : 2019年3月19日 下午1:06:34
******************************************************************************/
package ins.sino.claimcar.base.po;

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
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPSMSEMAIL_PK", allocationSize = 10)
@Table(name = "PRPSMSEMAIL")
public class PrpsmsEmail implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private BigDecimal misId;
	private String businessNo;
	private String sendText;
	private String userCode;
	private String sendNodecode;
	private Date createTime;
	private Date updateTime;
	private String status ;
	private String email;
    private String comCode;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	public BigDecimal getMisId() {
		return misId;
	}
	
	public void setMisId(BigDecimal misId) {
		this.misId = misId;
	}
	
	@Column(name = "BUSINESSNO", nullable = false)
	public String getBusinessNo() {
		return businessNo;
	}
	
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	@Column(name = "SENDTEXT", length=1000)
	public String getSendText() {
		return sendText;
	}
	
	public void setSendText(String sendText) {
		this.sendText = sendText;
	}

	@Column(name = "USERCODE", length=30)
	public String getUserCode() {
		return userCode;
	}
	
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@Column(name = "SENDNODECODE", length=30)
	public String getSendNodecode() {
		return sendNodecode;
	}
	
	public void setSendNodecode(String sendNodecode) {
		this.sendNodecode = sendNodecode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME", length=7)
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME", length=7)
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Column(name = "STATUS", length=20)
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name = "EMAIL", nullable = false, length=50)
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	@Column(name = "COMCODE", length=25)
	public String getComCode() {
		return comCode;
	}
	
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
}
