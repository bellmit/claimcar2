package ins.sino.claimcar.ilogFinalpowerInfo.po;

import java.io.Serializable;
import java.math.BigDecimal;
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
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PK_ILOGFINALPOWERINFO", allocationSize = 10)
@Table(name = "ILOGFINALPOWERINFO")
public class IlogFinalPowerInfo implements Serializable {
	
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
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")	
	@Column(name = "ID", unique = true, nullable = false, precision = 15, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "USERCODE", nullable = false, length = 10)
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	@Column(name = "USERNAME", nullable = false, length = 30)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Column(name = "GRADEPOWER", nullable = false, length = 8)
	public String getGradePower() {
		return gradePower;
	}
	public void setGradePower(String gradePower) {
		this.gradePower = gradePower;
	}
	
	@Column(name = "POWERLEVEL", nullable = false, length = 16)
	public String getPowerLevel() {
		return powerLevel;
	}
	public void setPowerLevel(String powerLevel) {
		this.powerLevel = powerLevel;
	}
	
	@Column(name = "GRADEAMOUNT", precision = 8, scale = 2)
	public BigDecimal getGradeAmount() {
		return gradeAmount;
	}
	public void setGradeAmount(BigDecimal gradeAmount) {
		this.gradeAmount = gradeAmount;
	}
	
	@Column(name = "BRANCHCOMCODE", nullable = false, length = 8)
	public String getBranchComcode() {
		return branchComcode;
	}
	public void setBranchComcode(String branchComcode) {
		this.branchComcode = branchComcode;
	}
	
	@Column(name = "SUBSIDIARYCOMCODE", length = 8)
	public String getSubSidiaryComcode() {
		return subSidiaryComcode;
	}
	public void setSubSidiaryComcode(String subSidiaryComcode) {
		this.subSidiaryComcode = subSidiaryComcode;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INPUTTIME", length = 7)
	public Date getInputTime() {
		return inputTime;
	}
	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME", length = 7)
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(name = "REMARK", length = 256)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	

}
