package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("Item")
public class JyDLChkLossFitInfoItemVo implements Serializable {
	
	@XStreamAlias("PartId")
	private String partId;
	
	@XStreamAlias("AuditMaterialFee")
	private String auditMaterialFee;
	
	@XStreamAlias("AuditCount")
	private String auditCount;
	
	@XStreamAlias("AuditManpowerFee")
	private String auditManpowerFee;
	
	@XStreamAlias("ApprPartSum")
	private String apprPartSum;
	
	@XStreamAlias("SelfPayPrice")
	private String selfPayPrice;
	
	@XStreamAlias("ApprRemainsPrice")
	private String apprRemainsPrice;
	
	@XStreamAlias("ManageRate")
	private String manageRate;
	
	@XStreamAlias("ApprManageFee")
	private String apprManageFee;
	
	@XStreamAlias("CheckState")
	private String checkState;
	
	@XStreamAlias("Remark")
	private String remark;
	
	@XStreamAlias("EvalPartSumFirst")
	private String evalPartSumFirst;

	public String getPartId() {
		return partId;
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public String getAuditMaterialFee() {
		return auditMaterialFee;
	}

	public void setAuditMaterialFee(String auditMaterialFee) {
		this.auditMaterialFee = auditMaterialFee;
	}

	public String getAuditCount() {
		return auditCount;
	}

	public void setAuditCount(String auditCount) {
		this.auditCount = auditCount;
	}

	public String getAuditManpowerFee() {
		return auditManpowerFee;
	}

	public void setAuditManpowerFee(String auditManpowerFee) {
		this.auditManpowerFee = auditManpowerFee;
	}

	public String getApprPartSum() {
		return apprPartSum;
	}

	public void setApprPartSum(String apprPartSum) {
		this.apprPartSum = apprPartSum;
	}

	public String getSelfPayPrice() {
		return selfPayPrice;
	}

	public void setSelfPayPrice(String selfPayPrice) {
		this.selfPayPrice = selfPayPrice;
	}

	public String getApprRemainsPrice() {
		return apprRemainsPrice;
	}

	public void setApprRemainsPrice(String apprRemainsPrice) {
		this.apprRemainsPrice = apprRemainsPrice;
	}

	public String getManageRate() {
		return manageRate;
	}

	public void setManageRate(String manageRate) {
		this.manageRate = manageRate;
	}

	public String getApprManageFee() {
		return apprManageFee;
	}

	public void setApprManageFee(String apprManageFee) {
		this.apprManageFee = apprManageFee;
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

	public String getEvalPartSumFirst() {
		return evalPartSumFirst;
	}

	public void setEvalPartSumFirst(String evalPartSumFirst) {
		this.evalPartSumFirst = evalPartSumFirst;
	}

	public JyDLChkLossFitInfoItemVo(String partId,String auditMaterialFee,String auditCount,String auditManpowerFee,String apprPartSum,
			String selfPayPrice,String apprRemainsPrice,String manageRate,String apprManageFee,String checkState,String remark,String evalPartSumFirst){
		super();
		this.partId = partId;
		this.auditMaterialFee = auditMaterialFee;
		this.auditCount = auditCount;
		this.auditManpowerFee = auditManpowerFee;
		this.apprPartSum = apprPartSum;
		this.selfPayPrice = selfPayPrice;
		this.apprRemainsPrice = apprRemainsPrice;
		this.manageRate = manageRate;
		this.apprManageFee = apprManageFee;
		this.checkState = checkState;
		this.remark = remark;
		this.evalPartSumFirst = evalPartSumFirst;
	}

	public JyDLChkLossFitInfoItemVo(){
		super();
	}
	
	
}
