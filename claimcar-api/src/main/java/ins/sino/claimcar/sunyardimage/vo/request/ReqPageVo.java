package ins.sino.claimcar.sunyardimage.vo.request;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("PAGE")
public class ReqPageVo implements Serializable{
	@XStreamAsAttribute
	@XStreamAlias("FILE_NAME")
	private String fileName;
	@XStreamAsAttribute
	@XStreamAlias("REMARK")
	private String remark;
	@XStreamAsAttribute
	@XStreamAlias("UPUSER")
	private String upUser;
	@XStreamAsAttribute
	@XStreamAlias("UP_USER_NAME")
	private String upUsername;
	@XStreamAsAttribute
	@XStreamAlias("UP_ORGNAME")
	private String upOrgname;
	@XStreamAsAttribute
	@XStreamAlias("UP_ORG")
	private String up_org;
	@XStreamAsAttribute
	@XStreamAlias("UP_TIME")
	private String upTime;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUpUser() {
		return upUser;
	}
	public void setUpUser(String upUser) {
		this.upUser = upUser;
	}
	public String getUpUsername() {
		return upUsername;
	}
	public void setUpUsername(String upUsername) {
		this.upUsername = upUsername;
	}
	public String getUpOrgname() {
		return upOrgname;
	}
	public void setUpOrgname(String upOrgname) {
		this.upOrgname = upOrgname;
	}
	public String getUpTime() {
		return upTime;
	}
	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}
	public String getUp_org() {
		return up_org;
	}
	public void setUp_org(String up_org) {
		this.up_org = up_org;
	}
   
}
