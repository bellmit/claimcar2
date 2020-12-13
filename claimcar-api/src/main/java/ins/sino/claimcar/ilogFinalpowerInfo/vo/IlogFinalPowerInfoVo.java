package ins.sino.claimcar.ilogFinalpowerInfo.vo;

import java.math.BigDecimal;
import java.util.Date;

public class IlogFinalPowerInfoVo implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long id;            // id
	private String userCode;    //用户代码（工号）
	private String userName;    //用户姓名
	private String gradePower;  //岗位权限
	private String powerLevel;  //权限等级
	private BigDecimal gradeAmount; //岗位金额
	private String branchComcode;  //二级机构
	private String subSidiaryComcode; //三级机构
	private Date inputTime;     //首次录入时间
	private Date updateTime;    //更新时间
	private String remark;      //备注信息
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getGradePower() {
		return gradePower;
	}
	public void setGradePower(String gradePower) {
		this.gradePower = gradePower;
	}
	public String getPowerLevel() {
		return powerLevel;
	}
	public void setPowerLevel(String powerLevel) {
		this.powerLevel = powerLevel;
	}
	public BigDecimal getGradeAmount() {
		return gradeAmount;
	}
	public void setGradeAmount(BigDecimal gradeAmount) {
		this.gradeAmount = gradeAmount;
	}
	public String getBranchComcode() {
		return branchComcode;
	}
	public void setBranchComcode(String branchComcode) {
		this.branchComcode = branchComcode;
	}
	public String getSubSidiaryComcode() {
		return subSidiaryComcode;
	}
	public void setSubSidiaryComcode(String subSidiaryComcode) {
		this.subSidiaryComcode = subSidiaryComcode;
	}
	public Date getInputTime() {
		return inputTime;
	}
	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
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
