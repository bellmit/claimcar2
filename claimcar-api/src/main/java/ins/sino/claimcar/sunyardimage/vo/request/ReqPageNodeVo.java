package ins.sino.claimcar.sunyardimage.vo.request;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("NODE")
public class ReqPageNodeVo implements Serializable{
	@XStreamAsAttribute
	@XStreamAlias("ID")
	private String id;
	@XStreamAsAttribute
	@XStreamAlias("ACTION")
	private String action;
	@XStreamImplicit(itemFieldName="PAGE")
	private List<ReqPageVo> pageVos;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public List<ReqPageVo> getPageVos() {
		return pageVos;
	}
	public void setPageVos(List<ReqPageVo> pageVos) {
		this.pageVos = pageVos;
	}
	
	

}
