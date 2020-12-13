/******************************************************************************
* CREATETIME : 2016年1月12日 下午5:46:06
******************************************************************************/
package ins.sino.claimcar.flow.vo;

import java.util.List;



/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年1月12日
 */
public class WfFlowShowVo  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private List<WfFlowNodeVo> wfFlowNodeList;
	private List<WfFlowNodeShowVo> wfFlowNodeShowList;
	private int maxColum;
	private int maxRow;
	private String bussTag;//业务标示
	
	public List<WfFlowNodeVo> getWfFlowNodeList() {
		return wfFlowNodeList;
	}
	public void setWfFlowNodeList(List<WfFlowNodeVo> wfFlowNodeList) {
		this.wfFlowNodeList = wfFlowNodeList;
	}
	public List<WfFlowNodeShowVo> getWfFlowNodeShowList() {
		return wfFlowNodeShowList;
	}
	public void setWfFlowNodeShowList(List<WfFlowNodeShowVo> wfFlowNodeShowList) {
		this.wfFlowNodeShowList = wfFlowNodeShowList;
	}
	public int getMaxColum() {
		return maxColum;
	}
	public void setMaxColum(int maxColum) {
		this.maxColum = maxColum;
	}
	public int getMaxRow() {
		return maxRow;
	}
	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}
	
	public String getBussTag() {
		return bussTag;
	}
	
	public void setBussTag(String bussTag) {
		this.bussTag = bussTag;
	}
	
	
	
	
}
