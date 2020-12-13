package ins.sino.claimcar.sunyardimage.vo.response;

import ins.sino.claimcar.sunyardimage.vo.request.ReqGetMetaDataVo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("META_DATA")
public class ResMetaDataVo extends ReqGetMetaDataVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("PAGEIDS")
	private List<ResPageIdVo> pages;
	
	public List<ResPageIdVo> getPages() {
		return pages;
	}
	
	public void setPages(List<ResPageIdVo> pages) {
		this.pages = pages;
	}

}
