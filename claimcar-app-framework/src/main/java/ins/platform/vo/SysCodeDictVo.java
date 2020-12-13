package ins.platform.vo;

/**
 * Custom VO class of PO SysCodeDict
 */ 
public class SysCodeDictVo extends SysCodeDictVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String sysAreaCode;
	private String userName;//人员姓名
	private String linkPhone;//联系电话
	

	public String getSysAreaCode() {
		return sysAreaCode;
	}

	public void setSysAreaCode(String sysAreaCode) {
		this.sysAreaCode = sysAreaCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	} 
	
	
	
}
