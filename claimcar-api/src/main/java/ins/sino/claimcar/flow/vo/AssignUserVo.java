package ins.sino.claimcar.flow.vo;

import java.util.Date;

/**
 * 分配的人封装的vo
 * @author zhouyanbin
 *
 */
public class AssignUserVo  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/** 员工号 */
	private String userCode;
	/** 任务数 */
	private int taskCount;
	/** 分配时间 */
	private Date assignTime;
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public int getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}
	public Date getAssignTime() {
		return assignTime;
	}
	public void setAssignTime(Date assignTime) {
		this.assignTime = assignTime;
	}
	
}
