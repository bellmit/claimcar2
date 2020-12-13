package ins.sino.claimcar.manager.vo;

import java.io.Serializable;
import java.util.Date;

public class PrpdcheckServerVoBase implements Serializable{

private static final long serialVersionUID = 1L;
private Long id;
private String serviceType;
private String feeStandard;
private String updateUser;
private Date createTime;
private String createUser;
private Date updateTime;

protected PrpdcheckServerVoBase() {

}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getServiceType() {
	return serviceType;
}

public void setServiceType(String serviceType) {
	this.serviceType = serviceType;
}

public String getFeeStandard() {
	return feeStandard;
}

public void setFeeStandard(String feeStandard) {
	this.feeStandard = feeStandard;
}

public String getUpdateUser() {
	return updateUser;
}

public void setUpdateUser(String updateUser) {
	this.updateUser = updateUser;
}

public Date getCreateTime() {
	return createTime;
}

public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}

public String getCreateUser() {
	return createUser;
}

public void setCreateUser(String createUser) {
	this.createUser = createUser;
}

public Date getUpdateTime() {
	return updateTime;
}

public void setUpdateTime(Date updateTime) {
	this.updateTime = updateTime;
}



}