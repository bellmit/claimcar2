package ins.sino.claimcar.moblie.commonmark.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("REMARKINFO")
public class MCCommonRemarksRemarkInfo  implements Serializable{

	/**  */
	private static final long serialVersionUID = 2564737683317211410L;
	
	@XStreamAlias("REMARKID")
	private String remarkId; //备注ID
	
	@XStreamAlias("REMARKTYPE")
	private String remarkType; //备注类型
	
	@XStreamAlias("HANDLERNAME")
	private String handlerName; //操作员姓名
	
	@XStreamAlias("MESSAGENODE")
	private String messageNode; //留言节点
	
	@XStreamAlias("INPUTDATE")
	private String inputDate; //填写时间
	
	@XStreamAlias("MESSAGEDESC")
	private String remark; //流言内容

	public String getRemarkId() {
		return remarkId;
	}

	public void setRemarkId(String remarkId) {
		this.remarkId = remarkId;
	}

	public String getRemarkType() {
		return remarkType;
	}

	public void setRemarkType(String remarkType) {
		this.remarkType = remarkType;
	}

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public String getMessageNode() {
		return messageNode;
	}

	public void setMessageNode(String messageNode) {
		this.messageNode = messageNode;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	

}
