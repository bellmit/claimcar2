package ins.platform.vo;

/**
 * Custom VO class of PO SysUser
 */ 
public class SysUserVo extends SysUserVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String comName;
	private String comFullName;// 机构名称全称

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getComFullName() {
		return comFullName;
	}

	public void setComFullName(String comFullName) {
		this.comFullName = comFullName;
	}

}
