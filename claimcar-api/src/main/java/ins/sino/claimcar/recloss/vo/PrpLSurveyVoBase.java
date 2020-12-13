package ins.sino.claimcar.recloss.vo;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.util.Date;


/**
 * Vo Base Class of PO PrpLSurvey
 */ 
public class PrpLSurveyVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String registNo;
	private Long fraudScoreId;
	private String nodeCode;
	private String reason;
	private String reasonDesc;
	private String createUser;
	private Date createTime;
	private String handlerUser;
	private Date handlerTime;
	private String handlerStatus;
	private String isMinorCases;
	private String isInjuryCases;
	private String isAutoTrigger;
	private String opinionDesc;
	
    
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegistNo() {
		return this.registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public Long getFraudScoreId() {
		return fraudScoreId;
	}

	public void setFraudScoreId(Long fraudScoreId) {
		this.fraudScoreId = fraudScoreId;
	}

	public String getNodeCode() {
		return this.nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReasonDesc() {
		return this.reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getHandlerUser() {
		return this.handlerUser;
	}

	public void setHandlerUser(String handlerUser) {
		this.handlerUser = handlerUser;
	}

	public Date getHandlerTime() {
		return this.handlerTime;
	}

	public void setHandlerTime(Date handlerTime) {
		this.handlerTime = handlerTime;
	}

	public String getHandlerStatus() {
		return handlerStatus;
	}

	public void setHandlerStatus(String handlerStatus) {
		this.handlerStatus = handlerStatus;
	}

	public String getIsMinorCases() {
		return isMinorCases;
	}

	public void setIsMinorCases(String isMinorCases) {
		this.isMinorCases = isMinorCases;
	}

	public String getIsInjuryCases() {
		return isInjuryCases;
	}

	public void setIsInjuryCases(String isInjuryCases) {
		this.isInjuryCases = isInjuryCases;
	}

	public String getIsAutoTrigger() {
		return isAutoTrigger;
	}

	public void setIsAutoTrigger(String isAutoTrigger) {
		this.isAutoTrigger = isAutoTrigger;
	}

	public String getOpinionDesc() {
		return opinionDesc;
	}

	public void setOpinionDesc(String opinionDesc) {
		this.opinionDesc = opinionDesc;
	}
	
}