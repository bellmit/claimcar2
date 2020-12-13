package ins.sino.claim.founder.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("BODY")
public class FounderToClaimQueryRespBody implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("NodeInfoList")
	private List<NodeInfo> nodeInfoList;


	public List<NodeInfo> getNodeInfoList() {
		return nodeInfoList;
	}

	public void setNodeInfoList(List<NodeInfo> nodeInfoList) {
		this.nodeInfoList = nodeInfoList;
	}
	

}
