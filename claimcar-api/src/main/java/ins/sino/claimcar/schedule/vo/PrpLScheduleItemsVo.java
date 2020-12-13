package ins.sino.claimcar.schedule.vo;

/**
 * Custom VO class of PO PrpLScheduleItems
 */ 
public class PrpLScheduleItemsVo extends PrpLScheduleItemsVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String moblie;
	private String flowTaskId;//工作流taskId

	public String getFlowTaskId() {
		return flowTaskId;
	}
	
	public void setFlowTaskId(String flowTaskId) {
		this.flowTaskId = flowTaskId;
	}

	public String getMoblie() {
		return moblie;
	}

	public void setMoblie(String moblie) {
		this.moblie = moblie;
	}


	
	
	
}
