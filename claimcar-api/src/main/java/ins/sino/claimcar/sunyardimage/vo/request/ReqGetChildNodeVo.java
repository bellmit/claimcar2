package ins.sino.claimcar.sunyardimage.vo.request;

import ins.sino.claimcar.sunyardimage.vo.common.FatherNodeVo;
import ins.sino.claimcar.sunyardimage.vo.common.SonNodeVo;
import ins.sino.claimcar.sunyardimage.vo.response.ResSonNodeVo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("NODE")
public class ReqGetChildNodeVo extends FatherNodeVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	@XStreamImplicit(itemFieldName = "NODE")
	private List<ResSonNodeVo> sonNodes;

	public List<ResSonNodeVo> getSonNodes() {
		return sonNodes;
	}

	public void setSonNodes(List<ResSonNodeVo> sonNodes) {
		this.sonNodes = sonNodes;
	}

}
