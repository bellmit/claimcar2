package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("Item")
public class JyDLChkLossAssistInfoItemVo implements Serializable {

	@XStreamAlias("AssistId")
	private String assistId;

	@XStreamAlias("AuditPrice")
	private String auditPrice;

	@XStreamAlias("AuditCount")
	private String auditCount;

	@XStreamAlias("ApprMateSum")
	private String apprMateSum;

	@XStreamAlias("CheckState")
	private String checkState;

	@XStreamAlias("Remark")
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

	public JyDLChkLossAssistInfoItemVo(){
		super();
	}

}
