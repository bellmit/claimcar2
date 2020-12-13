package ins.sino.claimcar.sunyardimage.vo.response;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("PAGE")
public class RespageVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	@XStreamAsAttribute
	@XStreamAlias("PAGEID")
	private String pageId;
	@XStreamAsAttribute
	@XStreamAlias("PAGE_URL")
	private String pageUrl;
	@XStreamAsAttribute
	@XStreamAlias("THUM_URL")
	private String thumUrl;
	@XStreamAsAttribute
	@XStreamAlias("FILE_NO")
	private String fileNo;
	@XStreamAsAttribute
	@XStreamAlias("PAGE_VER")
	private String pageVer;
	
	public String getPageId() {
		return pageId;
	}
	
	public void setPageId(String pageId) {
		this.pageId = pageId;
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
	
	public String getFileNo() {
		return fileNo;
	}
	
	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}
	
	public String getPageVer() {
		return pageVer;
	}
	
	public void setPageVer(String pageVer) {
		this.pageVer = pageVer;
	}
	
}
