package ins.sino.claimcar.carplatform.po;

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
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PK_CICLAIMPLATFORMTASK", allocationSize = 10)
@Table(name = "CICLAIMPLATFORMTASK")
public class CiClaimPlatformTask  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String registNo;
	private String bussNo;
	private String claimSeqNo;
	private String requestType;
	private String requestName;
	private String status;
	private String taskParams;
	private Date startDate;
	private Date endDate;
	private Date lastDate;
	private String remark;
	private int redoTimes;
	private String operateStatus;
	private int taskLevel;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "ID", unique = true, nullable = false, precision=15, scale=0)
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
	@Column(name = "BUSSNO")
	public String getBussNo() {
		return bussNo;
	}
	public void setBussNo(String bussNo) {
		this.bussNo = bussNo;
	}
	@Column(name = "CLAIMSEQNO")
	public String getClaimSeqNo() {
		return claimSeqNo;
	}
	public void setClaimSeqNo(String claimSeqNo) {
		this.claimSeqNo = claimSeqNo;
	}
	@Column(name = "REQUESTTYPE")
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	@Column(name = "REQUESTNAME")
	public String getRequestName() {
		return requestName;
	}
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name = "TASKPARAMS")
	public String getTaskParams() {
		return taskParams;
	}
	public void setTaskParams(String taskParams) {
		this.taskParams = taskParams;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STARTDATE", length=7)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENDDATE", length=7)
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LASTDATE", length=7)
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "REDOTIMES")
	public int getRedoTimes() {
		return redoTimes;
	}
	public void setRedoTimes(int redoTimes) {
		this.redoTimes = redoTimes;
	}
	@Column(name = "OPERATESTATUS")
	public String getOperateStatus() {
		return operateStatus;
	}
	public void setOperateStatus(String operateStatus) {
		this.operateStatus = operateStatus;
	}
	@Column(name = "TASKLEVEL")
	public int getTaskLevel() {
		return taskLevel;
	}
	public void setTaskLevel(int taskLevel) {
		this.taskLevel = taskLevel;
	}
	
}
