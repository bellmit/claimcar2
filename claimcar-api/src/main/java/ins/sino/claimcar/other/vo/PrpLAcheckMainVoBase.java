package ins.sino.claimcar.other.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrpLAcheckMainVoBase implements Serializable{

private static final long serialVersionUID = 1L;
private Long id;
private String taskId;
private String checkcode;
private String checkname;
private BigDecimal sumAmount;
private String underWriteFlag;
private Date underWriteDate;
private String underwriteuser;
private String createUser;
private Date createTime;
private String updateUser;
private Date updateTime;
private String comCode;
private Long checkmId;
private String checkmNameDetail;
private List<PrpLCheckFeeVo> prpLCheckFees = new ArrayList<PrpLCheckFeeVo>(0);

protected PrpLAcheckMainVoBase() {

}


public Long getId() {
	return this.id;
}

public void setId(Long id) {
	this.id = id;
}

public String getTaskId() {
	return this.taskId;
}

public void setTaskId(String taskId) {
	this.taskId = taskId;
}

public String getCheckcode() {
	return checkcode;
}


public void setCheckcode(String checkcode) {
	this.checkcode = checkcode;
}


public String getCheckname() {
	return checkname;
}


public void setCheckname(String checkname) {
	this.checkname = checkname;
}


public String getComCode() {
	return comCode;
}


public void setComCode(String comCode) {
	this.comCode = comCode;
}


public Long getCheckmId() {
	return checkmId;
}


public void setCheckmId(Long checkmId) {
	this.checkmId = checkmId;
}


public String getCheckmNameDetail() {
	return checkmNameDetail;
}


public void setCheckmNameDetail(String checkmNameDetail) {
	this.checkmNameDetail = checkmNameDetail;
}


public BigDecimal getSumAmount() {
	return this.sumAmount;
}

public void setSumAmount(BigDecimal sumAmount) {
	this.sumAmount = sumAmount;
}

public String getUnderWriteFlag() {
	return this.underWriteFlag;
}

public void setUnderWriteFlag(String underWriteFlag) {
	this.underWriteFlag = underWriteFlag;
}

public Date getUnderWriteDate() {
	return this.underWriteDate;
}

public void setUnderWriteDate(Date underWriteDate) {
	this.underWriteDate = underWriteDate;
}

public String getUnderwriteuser() {
	return this.underwriteuser;
}

public void setUnderwriteuser(String underwriteuser) {
	this.underwriteuser = underwriteuser;
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

public String getUpdateUser() {
	return this.updateUser;
}

public void setUpdateUser(String updateUser) {
	this.updateUser = updateUser;
}

public Date getUpdateTime() {
	return this.updateTime;
}

public void setUpdateTime(Date updateTime) {
	this.updateTime = updateTime;
}


public List<PrpLCheckFeeVo> getPrpLCheckFees() {
	return prpLCheckFees;
}


public void setPrpLCheckFees(List<PrpLCheckFeeVo> prpLCheckFees) {
	this.prpLCheckFees = prpLCheckFees;
}

}