package ins.sino.claimcar.sunyardimage.vo.response;

import ins.sino.claimcar.sunyardimage.vo.common.FatherNodeVo;
import ins.sino.claimcar.sunyardimage.vo.common.SonNodeVo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ResFatherNodeVo extends FatherNodeVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	@XStreamImplicit(itemFieldName = "NODE")
	private List<SonNodeVo> sonNodes;

	public List<SonNodeVo> getSonNodes() {
		return sonNodes;
	}

	public void setSonNodes(List<SonNodeVo> sonNodes) {
		this.sonNodes = sonNodes;
	}

}
