package ins.sino.claimcar.flow.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class NotifyMobileBody implements Serializable {

	/**  */
	private static final long serialVersionUID = 2964968376557481068L;
	
	@XStreamAlias("REGISTNO")
	private String registNo;  //报案号
	
	@XStreamAlias("TYPE")
	private String type; //类型
	
	@XStreamAlias("CANCELTYPE")
	private String cancelType; //注销类型
	
	@XStreamAlias("TASKLIST")
	private List<NotifyMobileItem> taskList;

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCancelType() {
		return cancelType;
	}

	public void setCancelType(String cancelType) {
		this.cancelType = cancelType;
	}

	public List<NotifyMobileItem> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<NotifyMobileItem> taskList) {
		this.taskList = taskList;
	}
}
