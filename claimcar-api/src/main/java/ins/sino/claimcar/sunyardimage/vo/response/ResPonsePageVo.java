package ins.sino.claimcar.sunyardimage.vo.response;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("PAGE")
public class ResPonsePageVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	@XStreamAsAttribute
	@XStreamAlias("PAGEID")
	private String pageId;
	@XStreamAlias("CREATE_USER")
	private String createUser;
	@XStreamAlias("CREATE_USERNAME")
	private String createUsername;
	@XStreamAlias("CREATE_TIME")
	private String createTime;
	@XStreamAlias("MODIFY_USER")
	private String modifyUser;
	@XStreamAlias("MODIFY_TIME")
	private String modifyTime;
	@XStreamAlias("PAGE_URL")
	private String pageUrl;
	@XStreamAlias("THUM_URL")
	private String thumUrl;
	@XStreamAlias("IS_LOCAL")
	private String isLocal;
	@XStreamAlias("PAGE_VER")
	private String pageVer;
	@XStreamAlias("PAGE_DESC")
	private String pageDesc;
	@XStreamAlias("UPLOAD_ORG")
	private String uploadOrg;
	@XStreamAlias("PAGE_CRC")
	private String pageCrc;
	@XStreamAlias("PAGE_SIZE")
	private String pageSize;
	@XStreamAlias("PAGE_FORMAT")
	private String pageFormat;
	@XStreamAlias("PAGE_ENCRYPT")
	private String pageEncrypt;
	@XStreamAlias("ORIGINAL_NAME")
	private String originalName;
	@XStreamAlias("PAGE_EXT")
	private ResPageExtVo pageExt;
	
	@XStreamAlias("PAGE_DESCS")
	private ResPageExtVo pageDescs;//待处理
	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateUsername() {
		return createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getThumUrl() {
		return thumUrl;
	}

	public void setThumUrl(String thumUrl) {
		this.thumUrl = thumUrl;
	}

	public String getIsLocal() {
		return isLocal;
	}

	public void setIsLocal(String isLocal) {
		this.isLocal = isLocal;
	}

	public String getPageVer() {
		return pageVer;
	}

	public void setPageVer(String pageVer) {
		this.pageVer = pageVer;
	}

	public String getPageDesc() {
		return pageDesc;
	}

	public void setPageDesc(String pageDesc) {
		this.pageDesc = pageDesc;
	}

	public String getUploadOrg() {
		return uploadOrg;
	}

	public void setUploadOrg(String uploadOrg) {
		this.uploadOrg = uploadOrg;
	}

	public String getPageCrc() {
		return pageCrc;
	}

	public void setPageCrc(String pageCrc) {
		this.pageCrc = pageCrc;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getPageFormat() {
		return pageFormat;
	}

	public void setPageFormat(String pageFormat) {
		this.pageFormat = pageFormat;
	}

	public String getPageEncrypt() {
		return pageEncrypt;
	}

	public void setPageEncrypt(String pageEncrypt) {
		this.pageEncrypt = pageEncrypt;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public ResPageExtVo getPageExt() {
		return pageExt;
	}

	public void setPageExt(ResPageExtVo pageExt) {
		this.pageExt = pageExt;
	}

	public ResPageExtVo getPageDescs() {
		return pageDescs;
	}

	public void setPageDescs(ResPageExtVo pageDescs) {
		this.pageDescs = pageDescs;
	}

}
