package ins.sino.claimcar.base.polisceSZ.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PK_PRPLDISPATCHINFO", allocationSize = 10)
@Table(name = "PRPLDISPATCHINFO")
public class PrpLDispatchInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String registNo;
	private Date receiveTime;
	private String dispatchType;
	private String taskType;
	private String cancelReason;
	private String userId;
	private String surveyorCo;
	private String surveyorName;
	private String surveyorPhone;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "REGISTNO")
	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECEIVETIME", nullable = false, length = 7)
	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	@Column(name = "DISPATCHTYPE")
	public String getDispatchType() {
		return dispatchType;
	}

	public void setDispatchType(String dispatchType) {
		this.dispatchType = dispatchType;
	}

	@Column(name = "TASKTYPE")
	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	@Column(name = "CANCELREASON")
	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	@Column(name = "USERID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "SURVEYORCO")
	public String getSurveyorCo() {
		return surveyorCo;
	}

	public void setSurveyorCo(String surveyorCo) {
		this.surveyorCo = surveyorCo;
	}

	@Column(name = "SURVEYORNAME")
	public String getSurveyorName() {
		return surveyorName;
	}

	public void setSurveyorName(String surveyorName) {
		this.surveyorName = surveyorName;
	}

	@Column(name = "SURVEYORPHONE")
	public String getSurveyorPhone() {
		return surveyorPhone;
	}

	public void setSurveyorPhone(String surveyorPhone) {
		this.surveyorPhone = surveyorPhone;
	}

}
