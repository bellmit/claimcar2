package ins.sino.claimcar.sunyardimage.vo.response;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("PAGEIDS")
public class ResPageIdVo implements Serializable{
	/**  */
	private static final long serialVersionUID = 1L;
	@XStreamAsAttribute
	@XStreamAlias("PAGEID")
	private String pageId;

	
	public String getPageId() {
		return pageId;
	}

	
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	
	
}
