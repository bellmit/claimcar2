package ins.sino.claimcar.moblie.lossPerson.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CERTAINSTASKINFO")
public class CertainsLossPerTaskRequestVo implements Serializable{
	private static final long serialVersionUID = -1568491102386689963L;
	@XStreamAlias("REGISTNO")
	private String registNo;  //报案号
	@XStreamAlias("CERTAINSID")
	private String certainsId;  //理赔定损ID
	@XStreamAlias("TASKID")
	private String taskId;  //任务id
	@XStreamAlias("NODETYPE")
	private String nodeType;  //调度节点
	@XStreamAlias("OPTIONTYPE")
	private String optionType;  //操作类型
	@XStreamAlias("ITEMNO")
	private String itemNo;  //标的序号
	@XStreamAlias("ITEMNONAME")
	private String itemNoName;  //标的名称
	@XStreamAlias("ISOBJECT")
	private String isObject;  //是否标的
	@XStreamAlias("NEXTHANDLERCODE")
	private String nextHandlerCode;  //处理人代码
	@XStreamAlias("NEXTHANDLERNAME")
	private String nextHandlerName;  //处理人名称
	@XStreamAlias("SCHEDULEOBJECTID")
	private String scheduleObjectId;  //处理人员归属机构编码
	@XStreamAlias("SCHEDULEOBJECTNAME")
	private String scheduleObjectName;  //处理人员归属机构名称
	@XStreamAlias("INDEMNITYDUTY")
	private String indemnityDuty;//事故责任
	@XStreamAlias("INDEMNITYDUTYRATE")
	private String indemnityDutyRate;//事故责任比例
	@XStreamAlias("HANDLERTYPE")
	private String handlerType;  //案件处理类型
	@XStreamAlias("LOSSTYPE")
	private String lossType;  //定损类别
	@XStreamAlias("TRACER")
	private String tracer;  //跟踪人员
	@XStreamAlias("TRACERIDENTIFYNO")
	private String tracerIdentifyNo;  //跟踪员身份证
	@XStreamAlias("ISSMALLCASE")
	private String isSmallCase;  //是否小额案件
	@XStreamAlias("ISDEROGATIONIS")
	private String isDerogationis;  //是否减损
	@XStreamAlias("JQINDEMNITYDUTY")
	private String jqIndemnityDuty;//交强险责任类型
	@XStreamAlias("DEROGATIONISAMOUNT")
	private String derogationisAmount;//减损金额
	@XStreamAlias("INSIDEDEROGATIONIS")
	private String insideDerogationis;//内部减损
	@XStreamAlias("TRACKOPINIONS")
	private String trackOpinions;//跟踪意见
	@XStreamAlias("ISBIGCASE")
	private String isBigCase;//是否重大赔案上报
	
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getCertainsId() {
		return certainsId;
	}
	public void setCertainsId(String certainsId) {
		this.certainsId = certainsId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getOptionType() {
		return optionType;
	}
	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getItemNoName() {
		return itemNoName;
	}
	public void setItemNoName(String itemNoName) {
		this.itemNoName = itemNoName;
	}
	public String getIsObject() {
		return isObject;
	}
	public void setIsObject(String isObject) {
		this.isObject = isObject;
	}
	public String getNextHandlerCode() {
		return nextHandlerCode;
	}
	public void setNextHandlerCode(String nextHandlerCode) {
		this.nextHandlerCode = nextHandlerCode;
	}
	public String getNextHandlerName() {
		return nextHandlerName;
	}
	public void setNextHandlerName(String nextHandlerName) {
		this.nextHandlerName = nextHandlerName;
	}
	public String getScheduleObjectId() {
		return scheduleObjectId;
	}
	public void setScheduleObjectId(String scheduleObjectId) {
		this.scheduleObjectId = scheduleObjectId;
	}
	public String getScheduleObjectName() {
		return scheduleObjectName;
	}
	public void setScheduleObjectName(String scheduleObjectName) {
		this.scheduleObjectName = scheduleObjectName;
	}
	public String getIndemnityDuty() {
		return indemnityDuty;
	}
	public void setIndemnityDuty(String indemnityDuty) {
		this.indemnityDuty = indemnityDuty;
	}
	public String getIndemnityDutyRate() {
		return indemnityDutyRate;
	}
	public void setIndemnityDutyRate(String indemnityDutyRate) {
		this.indemnityDutyRate = indemnityDutyRate;
	}
	public String getHandlerType() {
		return handlerType;
	}
	public void setHandlerType(String handlerType) {
		this.handlerType = handlerType;
	}
	public String getlossType() {
		return lossType;
	}
	public void setlossType(String lossType) {
		this.lossType = lossType;
	}
	public String getTracer() {
		return tracer;
	}
	public void setTracer(String tracer) {
		this.tracer = tracer;
	}
	public String getTracerIdentifyNo() {
		return tracerIdentifyNo;
	}
	public void setTracerIdentifyNo(String tracerIdentifyNo) {
		this.tracerIdentifyNo = tracerIdentifyNo;
	}
	public String getIsSmallCase() {
		return isSmallCase;
	}
	public void setIsSmallCase(String isSmallCase) {
		this.isSmallCase = isSmallCase;
	}
	public String getIsDerogationis() {
		return isDerogationis;
	}
	public void setIsDerogationis(String isDerogationis) {
		this.isDerogationis = isDerogationis;
	}
	public String getLossType() {
		return lossType;
	}
	public void setLossType(String lossType) {
		this.lossType = lossType;
	}
	public String getJqIndemnityDuty() {
		return jqIndemnityDuty;
	}
	public void setJqIndemnityDuty(String jqIndemnityDuty) {
		this.jqIndemnityDuty = jqIndemnityDuty;
	}
	public String getDerogationisAmount() {
		return derogationisAmount;
	}
	public void setDerogationisAmount(String derogationisAmount) {
		this.derogationisAmount = derogationisAmount;
	}
	public String getInsideDerogationis() {
		return insideDerogationis;
	}
	public void setInsideDerogationis(String insideDerogationis) {
		this.insideDerogationis = insideDerogationis;
	}
	public String getTrackOpinions() {
		return trackOpinions;
	}
	public void setTrackOpinions(String trackOpinions) {
		this.trackOpinions = trackOpinions;
	}
	public String getIsBigCase() {
		return isBigCase;
	}
	public void setIsBigCase(String isBigCase) {
		this.isBigCase = isBigCase;
	}
	
	
}
