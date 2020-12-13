package ins.sino.claimcar.sunyardimage.vo.common;

import java.io.File;
import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class BaseDataVo implements Serializable{
	@XStreamAlias("USER_CODE")
	private String userCode;
	@XStreamAlias("USER_NAME")
	private String userName;
	@XStreamAlias("ORG_CODE")
	private String orgCode;
	@XStreamAlias("COM_CODE")
	private String comCode;
	@XStreamAlias("ORG_NAME")
	private String orgName;
	@XStreamAlias("ROLE_CODE")
	private String roleCode;
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
}
