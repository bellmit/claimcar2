package ins.sino.claimcar.sunyardimage.vo.request;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("PAGES")
public class ReqPagesVo implements Serializable{
	@XStreamImplicit(itemFieldName="NODE")
	private List<ReqPageNodeVo> reqPageNodeVos;

	public List<ReqPageNodeVo> getReqPageNodeVos() {
		return reqPageNodeVos;
	}

	public void setReqPageNodeVos(List<ReqPageNodeVo> reqPageNodeVos) {
		this.reqPageNodeVos = reqPageNodeVos;
	}
	
}
