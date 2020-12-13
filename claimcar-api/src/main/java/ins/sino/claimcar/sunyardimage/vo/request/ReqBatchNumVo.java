package ins.sino.claimcar.sunyardimage.vo.request;

import ins.sino.claimcar.sunyardimage.vo.common.BatchVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ReqBatchNumVo extends BatchVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamAlias("NODE_ID")
	private String nodeId;
	@XStreamAlias("CREATE_USER")
	private String createUser;
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	
}
