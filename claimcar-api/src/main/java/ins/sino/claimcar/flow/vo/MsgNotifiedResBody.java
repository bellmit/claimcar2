package ins.sino.claimcar.flow.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class MsgNotifiedResBody  implements Serializable{

	private static final long serialVersionUID = 2964968376557481068L;
	
	@XStreamAlias("REGISTNO")
	private String registNo;  //报案号
	@XStreamAlias("TASKID")
	private String taskId; // 任务ID
	@XStreamAlias("NEWTASKID")
	private String newTaskId; // 移交后的任务ID
	
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getNewTaskId() {
		return newTaskId;
	}
	public void setNewTaskId(String newTaskId) {
		this.newTaskId = newTaskId;
	}
	
}
