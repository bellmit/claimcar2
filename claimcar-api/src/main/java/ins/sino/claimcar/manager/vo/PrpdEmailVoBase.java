/******************************************************************************
* CREATETIME : 2019年3月14日 下午12:54:12
******************************************************************************/
package ins.sino.claimcar.manager.vo;

import java.util.Date;


/**
 * <pre></pre>
 * @author ★XHY
 */
public class PrpdEmailVoBase implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String position;
	private String comCode;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	private String validFlag;
	private String remark;
	private String caseType;
	private String email;
	
	
	public String getEmail() {
		return email;
	}

	
	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPosition() {
		return position;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getComCode() {
		return comCode;
	}
	
	public void setComCode(String comCode) {
		this.comCode = comCode;
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
	
	public String getUpdateUser() {
		return updateUser;
	}
	
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getValidFlag() {
		return validFlag;
	}
	
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getCaseType() {
		return caseType;
	}
	
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	
}
