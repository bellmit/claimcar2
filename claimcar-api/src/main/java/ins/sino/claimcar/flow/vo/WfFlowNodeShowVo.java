package ins.sino.claimcar.flow.vo;

import java.util.List;

/**
 * 
 * @author dengkk
 * 节点图vo
 */
public class WfFlowNodeShowVo extends PrpLWfNodeVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<WfFlowNodeVo> childList;
	
	private int rootNodeNum;//该nodeType下面根节点数目
	
	private int rowNum;//该nodeType所在行
	
	public int getRootNodeNum() {
		return rootNodeNum;
	}

	public void setRootNodeNum(int rootNodeNum) {
		this.rootNodeNum = rootNodeNum;
	}
	

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public List<WfFlowNodeVo> getChildList() {
		return childList;
	}

	public void setChildList(List<WfFlowNodeVo> childList) {
		this.childList = childList;
	}

}
