package ins.sino.claimcar.base.po;

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
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLQUARTZFAILLOG_PK", allocationSize = 10)
@Table(name = "PRPLQUARTZLOG")
public class PrplQuartzLog implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String registNo;
	private String jobType;
	private String failReason;
	private String status;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")   //自动生成主键
	@Column(name = "ID", unique = true, nullable = false, precision=15, scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    
	@Column(name = "REGISTNO", length=25)
    public String getRegistNo() {
        return registNo;
    }
    
    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }
    
    @Column(name = "JOBTYPE", length=30)
    public String getJobType() {
        return jobType;
    }
    
    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
    
    @Column(name = "FAILREASON")
    public String getFailReason() {
        return failReason;
    }
    
    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }
    
    @Column(name = "STATUS" , length=1)
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Column(name = "CREATEUSER", length=10)
    public String getCreateUser() {
        return createUser;
    }
    
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATETIME", length=7)
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    @Column(name = "UPDATEUSER", length=10)
    public String getUpdateUser() {
        return updateUser;
    }
    
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATETIME", length=7)
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
