package ins.sino.claimcar.claim.vo;

import java.util.Date;


public class PrpLPayHisVo implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 

	private Long id;
	private Long otherId;
	private String payStatus;
	private Date inputTime;
	private String claimNo;
	private String compensateNo;
	private Date createTime;
	private Date updateTime;
	private String hisType;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOtherId() {
		return otherId;
	}
	public void setOtherId(Long otherId) {
		this.otherId = otherId;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public Date getInputTime() {
		return inputTime;
	}
	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getCompensateNo() {
		return compensateNo;
	}
	public void setCompensateNo(String compensateNo) {
		this.compensateNo = compensateNo;
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
	public String getHisType() {
		return hisType;
	}
	public void setHisType(String hisType) {
		this.hisType = hisType;
	}
	
		
}
