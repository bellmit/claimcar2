/******************************************************************************
* CREATETIME : 2019年3月19日 下午2:24:12
******************************************************************************/
package ins.sino.claimcar.claim.vo;

import java.math.BigDecimal;
import java.util.Date;


/**
 * <pre></pre>
 * @author ★XHY
 */
public class PrpsmsEmailVoBase implements java.io.Serializable{
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
	
	
	
	


	public BigDecimal getMisId() {
		return misId;
	}
	
	public void setMisId(BigDecimal misId) {
		this.misId = misId;
	}
	
	public String getBusinessNo() {
		return businessNo;
	}
	
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	
	public String getSendText() {
		return sendText;
	}
	
	public void setSendText(String sendText) {
		this.sendText = sendText;
	}
	
	public String getUserCode() {
		return userCode;
	}
	
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	public String getSendNodecode() {
		return sendNodecode;
	}
	
	public void setSendNodecode(String sendNodecode) {
		this.sendNodecode = sendNodecode;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getComCode() {
		return comCode;
	}
	
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

}
