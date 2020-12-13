package ins.sino.claimcar.genilex.vo.endCase;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ClaimPay")
public class ClaimPay implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ReportNo") 
	private String  reportNo;
	@XStreamAlias("ClaimNo") 
	private String  claimNo;
	@XStreamAlias("PayAmount") 
	private String  payAmount;
	@XStreamAlias("PaymentType") 
	private String  paymentType;
	@XStreamAlias("CreateBy") 
	private String  createBy;
	@XStreamAlias("CreateTime") 
	private String  createTime;
	@XStreamAlias("UpdateBy") 
	private String  updateBy;
	@XStreamAlias("UpdateTime") 
	private String  updateTime;
	@XStreamAlias("PayItems") 
	private List<PayItem>  payItems;
	
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public List<PayItem> getPayItems() {
		return payItems;
	}
	public void setPayItems(List<PayItem> payItems) {
		this.payItems = payItems;
	}
	
	
	
}
