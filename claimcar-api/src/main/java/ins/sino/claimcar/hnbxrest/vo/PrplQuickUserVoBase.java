package ins.sino.claimcar.hnbxrest.vo;


import java.io.Serializable;
import java.util.Date;



public class PrplQuickUserVoBase implements Serializable{
    
	/**  */
    private static final long serialVersionUID = 1L;
    private String userCode;
	private String userName;
	private String comCode;
	private String comName;
	private String phone;
	private Integer times;
	private String validStatus;
	private Date createTime;
	private Date updateTime;
	private String isGBFlag;
    
    public String getUserCode() {
        return userCode;
    }
    
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }  
    
    public String getComCode() {
        return comCode;
    }
    
    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    public String getComName() {
        return comName;
    }
    
    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Integer getTimes() {
        return times;
    }
    
    public void setTimes(Integer times) {
        this.times = times;
    }
    
    public String getValidStatus() {
        return validStatus;
    }
    
    public void setValidStatus(String validStatus) {
        this.validStatus = validStatus;
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

	
	public String getIsGBFlag() {
		return isGBFlag;
	}

	
	public void setIsGBFlag(String isGBFlag) {
		this.isGBFlag = isGBFlag;
	}
    
    
}
