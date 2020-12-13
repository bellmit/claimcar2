package ins.sino.claimcar.ciitc.vo;

import java.io.Serializable;
import java.util.Date;

public class PrplZBXAreaVoBase implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String areaCode;
	private String areaName;
	private String validFlag;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	
	public String getAreaCode() {
		return areaCode;
	}
	
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	public String getAreaName() {
		return areaName;
	}
	
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	public String getValidFlag() {
		return validFlag;
	}
	
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
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
	
}
