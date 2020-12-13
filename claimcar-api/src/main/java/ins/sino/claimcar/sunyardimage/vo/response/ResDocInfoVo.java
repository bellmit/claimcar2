package ins.sino.claimcar.sunyardimage.vo.response;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("DocInfo")
public class ResDocInfoVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	@XStreamAlias("BATCH_ID")
	private String batchId;
	@XStreamAlias("BUSI_NUM")
	private String busiNo;
	@XStreamAlias("BIZ_ORG")
	private String bizOrg;
	@XStreamAlias("APP_CODE")
	private String appCode;
	@XStreamAlias("APP_NAME")
	private String appName;
	@XStreamAlias("BATCH_VER")
	private String batchVer;
	@XStreamAlias("INTER_VER")
	private String interVer;
	@XStreamAlias("STATUS")
	private String status;
	@XStreamAlias("CREATE_USER")
	private String createUser;
	@XStreamAlias("CREATE_DATE")
	private String createDate;
	@XStreamAlias("MODIFY_USER")
	private String modifyUser;
	@XStreamAlias("MODIFY_DATE")
	private String modifyDate;
	
	@XStreamAlias("DOC_EXT")
	private ResDocExtVo docextVo;

	
	public String getBatchId() {
		return batchId;
	}

	
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	
	public String getBusiNo() {
		return busiNo;
	}

	
	public void setBusiNo(String busiNo) {
		this.busiNo = busiNo;
	}

	
	public String getBizOrg() {
		return bizOrg;
	}

	
	public void setBizOrg(String bizOrg) {
		this.bizOrg = bizOrg;
	}

	
	public String getAppCode() {
		return appCode;
	}

	
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	
	public String getAppName() {
		return appName;
	}

	
	public void setAppName(String appName) {
		this.appName = appName;
	}

	
	public String getBatchVer() {
		return batchVer;
	}

	
	public void setBatchVer(String batchVer) {
		this.batchVer = batchVer;
	}

	
	public String getInterVer() {
		return interVer;
	}

	
	public void setInterVer(String interVer) {
		this.interVer = interVer;
	}

	
	public String getStatus() {
		return status;
	}

	
	public void setStatus(String status) {
		this.status = status;
	}

	
	public String getCreateUser() {
		return createUser;
	}

	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	
	public String getCreateDate() {
		return createDate;
	}

	
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	
	public String getModifyUser() {
		return modifyUser;
	}

	
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	
	public String getModifyDate() {
		return modifyDate;
	}

	
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	
	public ResDocExtVo getDocextVo() {
		return docextVo;
	}

	
	public void setDocextVo(ResDocExtVo docextVo) {
		this.docextVo = docextVo;
	}
	
}
