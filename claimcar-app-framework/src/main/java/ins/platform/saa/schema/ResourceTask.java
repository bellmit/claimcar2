package ins.platform.saa.schema;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "resource_url")
public class ResourceTask implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String taskCode;
	private String parentCode;
	private String taskCName;
	private String taskTName;
	private String taskEName;
	private String creatorCode;
	private Date createTime;
	private String updaterCode;
	private Date updateTime;
	private String validStatus;
	private String url;
	
	public ResourceTask(){
		
	};
	
	@Id
	@Column(name = "Id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "TaskCode")
	public String getTaskCode() {
		return taskCode;
	}
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	
	@Column(name = "ParentCode")
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	
	@Column(name = "TaskCName")
	public String getTaskCName() {
		return taskCName;
	}
	public void setTaskCName(String taskCName) {
		this.taskCName = taskCName;
	}
	
	@Column(name = "TaskTName")
	public String getTaskTName() {
		return taskTName;
	}
	public void setTaskTName(String taskTName) {
		this.taskTName = taskTName;
	}
	
	@Column(name = "TaskEName")
	public String getTaskEName() {
		return taskEName;
	}
	public void setTaskEName(String taskEName) {
		this.taskEName = taskEName;
	}
	
	@Column(name = "CreatorCode")
	public String getCreatorCode() {
		return creatorCode;
	}
	public void setCreatorCode(String creatorCode) {
		this.creatorCode = creatorCode;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CreateTime")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "UpdaterCode")
	public String getUpdaterCode() {
		return updaterCode;
	}
	public void setUpdaterCode(String updaterCode) {
		this.updaterCode = updaterCode;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "UpdateTime")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(name = "ValidStatus")
	public String getValidStatus() {
		return validStatus;
	}
	public void setValidStatus(String validStatus) {
		this.validStatus = validStatus;
	}
	
	@Column(name = "Url")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
