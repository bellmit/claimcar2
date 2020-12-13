package ins.sino.claimcar.claimjy.vo.vlossreturn;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class JyVLossReturnFitInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "PartId")
	private String partId;

	@XmlElement(name = "AuditMaterialFee")
	private String auditMaterialFee;

	@XmlElement(name = "AuditCount")
	private String auditCount;

	@XmlElement(name = "AuditManpowerFee")
	private String auditManpowerFee;

	@XmlElement(name = "ApprPartSum")
	private String apprPartSum;

	@XmlElement(name = "SelfPayPrice")
	private String selfPayPrice;

	@XmlElement(name = "ApprRemainsPrice")
	private String apprRemainsPrice;

	@XmlElement(name = "ManageRate")
	private String manageRate;

	@XmlElement(name = "ApprManageFee")
	private String apprManageFee;

	@XmlElement(name = "CheckState")
	private String checkState;

	@XmlElement(name = "Remark")
	private String remark;

	@XmlElement(name = "EvalPartSumFirst")
	private String evalPartSumFirst;

	@XmlElement(name = "FitBackFlag")
	private String fitBackFlag;
	
	@XmlElement(name = "RecheckFlag")
	private String recheckFlag;
	
	

	public String getRecheckFlag() {
		return recheckFlag;
	}

	public void setRecheckFlag(String recheckFlag) {
		this.recheckFlag = recheckFlag;
	}

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

	public String getFitBackFlag() {
		return fitBackFlag;
	}

	public void setFitBackFlag(String fitBackFlag) {
		this.fitBackFlag = fitBackFlag;
	}

	

}
