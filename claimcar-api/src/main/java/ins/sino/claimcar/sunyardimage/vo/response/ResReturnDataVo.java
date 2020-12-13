package ins.sino.claimcar.sunyardimage.vo.response;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class ResReturnDataVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAsAttribute
	@XStreamAlias("SUM_IMGS")
	private String sumImgs;
	
	@XStreamAsAttribute
	@XStreamAlias("NODES")
	private List<NodeNumVo> nodeNumVos;

	public String getSumImgs() {
		return sumImgs;
	}

	public void setSumImgs(String sumImgs) {
		this.sumImgs = sumImgs;
	}

	public List<NodeNumVo> getNodeNumVos() {
		return nodeNumVos;
	}

	public void setNodeNumVos(List<NodeNumVo> nodeNumVos) {
		this.nodeNumVos = nodeNumVos;
	}
	
	
}
