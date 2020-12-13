package ins.sino.claimcar.sunyardimage.vo.response;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("PAGE")
public class ResUploadPageVo implements Serializable{
	@XStreamAsAttribute
	@XStreamAlias("PAGEID")
	private String pageId;
	@XStreamAsAttribute
	@XStreamAlias("FILE_NAME")
	private String fileName;
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
