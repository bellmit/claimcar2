package ins.sino.claimcar.claimjy.vo.vlossreturn;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class JyVLossReturnRepairInfo  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "RepairId")
	private String repairId;

	@XmlElement(name = "AuditManpowerFee")
	private String auditManpowerFee;

	@XmlElement(name = "AuditMaterialFee")
	private String auditMaterialFee;

	@XmlElement(name = "ApprHour")
	private String apprHour;

	@XmlElement(name = "ApprRepairSum")
	private String apprRepairSum;

	@XmlElement(name = "CheckState")
	private String checkState;

	@XmlElement(name = "Remark")
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
	
	

}
