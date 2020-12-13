package ins.sino.claimcar.other.vo;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Vo Base Class of PO PrpLAssessorMain
 */ 
public class PrpLAssessorMainVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String taskId;
	private String intermcode;
	private String intermname;
	private BigDecimal sumAmount;
	private String underWriteFlag;
	private Date underWriteDate;
	private String underwriteuser;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	private List<PrpLAssessorFeeVo> prpLAssessorFees = new ArrayList<PrpLAssessorFeeVo>(0);

    protected PrpLAssessorMainVoBase() {
	
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

	public String getIntermcode() {
		return this.intermcode;
	}

	public void setIntermcode(String intermcode) {
		this.intermcode = intermcode;
	}

	public String getIntermname() {
		return this.intermname;
	}

	public void setIntermname(String intermname) {
		this.intermname = intermname;
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

	public List<PrpLAssessorFeeVo> getPrpLAssessorFees() {
		return this.prpLAssessorFees;
	}

	public void setPrpLAssessorFees(List<PrpLAssessorFeeVo> prpLAssessorFees) {
		this.prpLAssessorFees = prpLAssessorFees;
	}
}