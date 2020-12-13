package ins.sino.claimcar.schedule.vo;

/**
 * Custom VO class of PO PrpLScheduleTask
 */ 
public class PrpLScheduleTaskVo extends PrpLScheduleTaskVoBase implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L; 
	/**
	 * 人伤跟踪指派人员
	 */
	private String personScheduledComname;
	private String personScheduledComcode;
	private String personScheduledUsercode;
	private String personScheduledUsername;
	
	private String flowTaskId;//工作流taskId
	
	private String relateHandlerName;//关联查勘员
	private String relateHandlerMobile;//关联查勘员电话
	private String isComuserCode;//是否承保地人员
	private String selfDefinareaCode;//自定义区域编码
	private String personRelateHandlerName;//关联查勘员
	private String personRelateHandlerMobile;//关联查勘员电话
	private String personIsComuserCode;//是否承保地人员
	private String callNumber;//呼出电话
	private String types;
	private String isAutoCheckFlag;  //自助理赔的页面可编辑标志
	public String getPersonScheduledComname() {
		return personScheduledComname;
	}
	public void setPersonScheduledComname(String personScheduledComname) {
		this.personScheduledComname = personScheduledComname;
	}
	public String getPersonScheduledComcode() {
		return personScheduledComcode;
	}
	public void setPersonScheduledComcode(String personScheduledComcode) {
		this.personScheduledComcode = personScheduledComcode;
	}
	public String getPersonScheduledUsercode() {
		return personScheduledUsercode;
	}
	public void setPersonScheduledUsercode(String personScheduledUsercode) {
		this.personScheduledUsercode = personScheduledUsercode;
	}
	public String getPersonScheduledUsername() {
		return personScheduledUsername;
	}
	public void setPersonScheduledUsername(String personScheduledUsername) {
		this.personScheduledUsername = personScheduledUsername;
	}
	public String getFlowTaskId() {
		return flowTaskId;
	}
	public void setFlowTaskId(String flowTaskId) {
		this.flowTaskId = flowTaskId;
	}
	public String getRelateHandlerName() {
		return relateHandlerName;
	}
	public void setRelateHandlerName(String relateHandlerName) {
		this.relateHandlerName = relateHandlerName;
	}
	public String getRelateHandlerMobile() {
		return relateHandlerMobile;
	}
	public void setRelateHandlerMobile(String relateHandlerMobile) {
		this.relateHandlerMobile = relateHandlerMobile;
	}
	public String getIsComuserCode() {
		return isComuserCode;
	}
	public void setIsComuserCode(String isComuserCode) {
		this.isComuserCode = isComuserCode;
	}
	public String getSelfDefinareaCode() {
		return selfDefinareaCode;
	}
	public void setSelfDefinareaCode(String selfDefinareaCode) {
		this.selfDefinareaCode = selfDefinareaCode;
	}
	public String getPersonRelateHandlerName() {
		return personRelateHandlerName;
	}
	public void setPersonRelateHandlerName(String personRelateHandlerName) {
		this.personRelateHandlerName = personRelateHandlerName;
	}
	public String getPersonRelateHandlerMobile() {
		return personRelateHandlerMobile;
	}
	public void setPersonRelateHandlerMobile(String personRelateHandlerMobile) {
		this.personRelateHandlerMobile = personRelateHandlerMobile;
	}
	public String getPersonIsComuserCode() {
		return personIsComuserCode;
	}
	public void setPersonIsComuserCode(String personIsComuserCode) {
		this.personIsComuserCode = personIsComuserCode;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public String getCallNumber() {
		return callNumber;
	}
	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}
	public String getIsAutoCheckFlag() {
		return isAutoCheckFlag;
	}
	public void setIsAutoCheckFlag(String isAutoCheckFlag) {
		this.isAutoCheckFlag = isAutoCheckFlag;
	}

}
