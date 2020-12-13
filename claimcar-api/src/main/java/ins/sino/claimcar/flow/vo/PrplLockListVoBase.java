package ins.sino.claimcar.flow.vo;

import java.util.Date;


/**
 * Vo Base Class of PO PrpLWfMain
 */ 
public class PrplLockListVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long yWMainId;
	private String lockType;
	private Date createTime;
	private Date updateTime;
	private String remark;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getyWMainId() {
		return yWMainId;
	}
	public void setyWMainId(Long yWMainId) {
		this.yWMainId = yWMainId;
	}
	public String getLockType() {
		return lockType;
	}
	public void setLockType(String lockType) {
		this.lockType = lockType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
