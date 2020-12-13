package ins.sino.claimcar.sunyardimage.vo.response;

import ins.sino.claimcar.sunyardimage.vo.common.VtreeVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
@XStreamAlias("doc")
public class ResDocVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	@XStreamAsAttribute
	@XStreamAlias("version")
	private String version;
	@XStreamAlias("DocInfo")
	private ResDocInfoVo docInfoVo;
	@XStreamAlias("PageInfo")
	private ResPageInfoVo pageInfoVo;
	@XStreamAlias("VTREE")
	private ResVtreeVo vtreeVo;
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public ResDocInfoVo getDocInfoVo() {
		return docInfoVo;
	}
	
	public void setDocInfoVo(ResDocInfoVo docInfoVo) {
		this.docInfoVo = docInfoVo;
	}
	
	public ResPageInfoVo getPageInfoVo() {
		return pageInfoVo;
	}
	
	public void setPageInfoVo(ResPageInfoVo pageInfoVo) {
		this.pageInfoVo = pageInfoVo;
	}
	
	public ResVtreeVo getVtreeVo() {
		return vtreeVo;
	}
	
	public void setVtreeVo(ResVtreeVo vtreeVo) {
		this.vtreeVo = vtreeVo;
	}
	
}
