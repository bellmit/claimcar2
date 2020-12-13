package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("Item")
public class JyDLChkLossRepairInfoItemVo implements Serializable {

	@XStreamAlias("RepairId")
	private String repairId;

	@XStreamAlias("AuditManpowerFee")
	private String auditManpowerFee;

	@XStreamAlias("AuditMaterialFee")
	private String auditMaterialFee;

	@XStreamAlias("ApprHour")
	private String apprHour;

	@XStreamAlias("ApprRepairSum")
	private String apprRepairSum;

	@XStreamAlias("CheckState")
	private String checkState;

	@XStreamAlias("Remark")
	private String remark;

	public String getRepairId() {
		return repairId;
	}

	public void setRepairId(String repairId) {
		this.repairId = repairId;
	}

	public String getAuditManpowerFee() {
		return auditManpowerFee;
	}

	public void setAuditManpowerFee(String auditManpowerFee) {
		this.auditManpowerFee = auditManpowerFee;
	}

	public String getAuditMaterialFee() {
		return auditMaterialFee;
	}

	public void setAuditMaterialFee(String auditMaterialFee) {
		this.auditMaterialFee = auditMaterialFee;
	}

	public String getApprHour() {
		return apprHour;
	}

	public void setApprHour(String apprHour) {
		this.apprHour = apprHour;
	}

	public String getApprRepairSum() {
		return apprRepairSum;
	}

	public void setApprRepairSum(String apprRepairSum) {
		this.apprRepairSum = apprRepairSum;
	}

	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public JyDLChkLossRepairInfoItemVo(){
		super();
	}

}
