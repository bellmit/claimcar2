package ins.sino.claimcar.court.dlclaim.xyvo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("NODE")
public class NodeImageVo implements Serializable{

	private static final long serialVersionUID = 1L;
	@XStreamAsAttribute
	@XStreamAlias("ID")
	private String id;
	@XStreamAsAttribute
	@XStreamAlias("NAME")
	private String name;
	@XStreamImplicit(itemFieldName="LEAF")
    private List<LeafVo> leafVos;
	@XStreamImplicit(itemFieldName="NODE")
	private List<NodeImageVo> nodeImageVos;
	public List<LeafVo> getLeafVos() {
		return leafVos;
	}
	public void setLeafVos(List<LeafVo> leafVos) {
		this.leafVos = leafVos;
	}
	public List<NodeImageVo> getNodeImageVos() {
		return nodeImageVos;
	}
	public void setNodeImageVos(List<NodeImageVo> nodeImageVos) {
		this.nodeImageVos = nodeImageVos;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
