/******************************************************************************
* CREATETIME : 2016年1月12日 下午5:37:49
******************************************************************************/
package ins.sino.claimcar.flow.vo;

import java.util.List;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年1月12日
 */
public class WfFlowNodeVo extends PrpLWfTaskVo {

	/**  */
	private static final long serialVersionUID = 1L;

    private WfFlowNodeVo parentNode;
	
	private List<WfFlowNodeVo> childNode;
	
	private int colum;
	
	private int row;
	
	private String url;
	
	private String nodeStatus;//节点状态翻译
	   
	private String nodeColor;//节点颜色 不同状态颜色不同
	 
	private String userName;//处理人
	
	private String flag;//特殊标志 rcCancel(报案注销和立案注销)

	public WfFlowNodeVo getParentNode() {
		return parentNode;
	}

	public void setParentNode(WfFlowNodeVo parentNode) {
		this.parentNode = parentNode;
	}

	public List<WfFlowNodeVo> getChildNode() {
		return childNode;
	}

	public void setChildNode(List<WfFlowNodeVo> childNode) {
		this.childNode = childNode;
	}

	public int getColum() {
		return colum;
	}

	public void setColum(int colum) {
		this.colum = colum;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	public String getNodeStatus() {
		return nodeStatus;
	}

	
	public void setNodeStatus(String nodeStatus) {
		this.nodeStatus = nodeStatus;
	}

	
	public String getNodeColor() {
		return nodeColor;
	}

	
	public void setNodeColor(String nodeColor) {
		this.nodeColor = nodeColor;
	}

	
	public String getUserName() {
		return userName;
	}

	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
}
