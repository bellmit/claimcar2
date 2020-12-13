package ins.sino.claimcar.schedule.vo;

import java.util.Map;

/**
 * Custom VO class of PO PrpLScheduleDefLoss
 */
public class PrpLScheduleDefLossVo extends PrpLScheduleDefLossVoBase implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String scheduleFlag;// 定损调度 1-勾选 ， 0-未勾选
	private String flowTaskId;// 调度注销功能时，保存工作流taskId数据
	private Map<String,String> showMap;// //展示移动查勘返回的定损员 key值存定损员名称，value值存定损员代码+定损员机构中间用，隔开
	private String taskFlag;// 是否生成定损任务
	private String oldUser;//
	private String modelName;
	private String license;
	private Long scheduleTaskId;
	public String getScheduleFlag() {
		return scheduleFlag;
	}

	public void setScheduleFlag(String scheduleFlag) {
		this.scheduleFlag = scheduleFlag;
	}

	public String getFlowTaskId() {
		return flowTaskId;
	}

	public void setFlowTaskId(String flowTaskId) {
		this.flowTaskId = flowTaskId;
	}

	public String getTaskFlag() {
		return taskFlag;
	}

	public void setTaskFlag(String taskFlag) {
		this.taskFlag = taskFlag;
	}

	public String getOldUser() {
		return oldUser;
	}

	public void setOldUser(String oldUser) {
		this.oldUser = oldUser;
	}

	public Map<String,String> getShowMap() {
		return showMap;
	}

	public void setShowMap(Map<String,String> showMap) {
		this.showMap = showMap;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public Long getScheduleTaskId() {
		return scheduleTaskId;
	}

	public void setScheduleTaskId(Long scheduleTaskId) {
		this.scheduleTaskId = scheduleTaskId;
	}

	
}
