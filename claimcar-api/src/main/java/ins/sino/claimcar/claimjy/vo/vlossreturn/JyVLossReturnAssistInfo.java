package ins.sino.claimcar.claimjy.vo.vlossreturn;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class JyVLossReturnAssistInfo  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "AssistId")
	private String assistId;

	@XmlElement(name = "AuditPrice")
	private String auditPrice;

	@XmlElement(name = "AuditCount")
	private String auditCount;

	@XmlElement(name = "ApprMateSum")
	private String apprMateSum;

	@XmlElement(name = "CheckState")
	private String checkState;

	@XmlElement(name = "Remark")
	private String remark;

	public String getAssistId() {
		return assistId;
	}

	public void setAssistId(String assistId) {
		this.assistId = assistId;
	}

	public String getAuditPrice() {
		return auditPrice;
	}

	public void setAuditPrice(String auditPrice) {
		this.auditPrice = auditPrice;
	}

	public String getAuditCount() {
		return auditCount;
	}

	public void setAuditCount(String auditCount) {
		this.auditCount = auditCount;
	}

	public String getApprMateSum() {
		return apprMateSum;
	}

	public void setApprMateSum(String apprMateSum) {
		this.apprMateSum = apprMateSum;
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
