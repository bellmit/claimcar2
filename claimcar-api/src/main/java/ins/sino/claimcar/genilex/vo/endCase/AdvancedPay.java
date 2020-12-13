package ins.sino.claimcar.genilex.vo.endCase;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("AdvancedPay")
public class AdvancedPay implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ReportNo") 
	private String  reportNo;
	@XStreamAlias("ClaimNo") 
	private String  claimNo;
	@XStreamAlias("LossTime") 
	private String  lossTime;
	@XStreamAlias("DriverName") 
	private String  driverName;
	@XStreamAlias("DutyInd") 
	private String  dutyInd;
	@XStreamAlias("TrafficDepartment") 
	private String  trafficDepartment;
	@XStreamAlias("PointConstableTel") 
	private String  pointConstableTel;
	@XStreamAlias("NoticedAmt") 
	private String  noticedAmt;
	@XStreamAlias("NoticedTime") 
	private String  noticedTime;
	@XStreamAlias("ReceivedNoticeTime") 
	private String  receivedNoticeTime;
	@XStreamAlias("InjuredNumber") 
	private String  injuredNumber;
	@XStreamAlias("AdvancedPayNumber") 
	private String  advancedPayNumber;
	@XStreamAlias("AdvancedPayAmt") 
	private String  advancedPayAmt;
	@XStreamAlias("Remark") 
	private String  remark;
	@XStreamAlias("CreateBy") 
	private String  createBy;
	@XStreamAlias("CreateTime") 
	private String  createTime;
	@XStreamAlias("UpdateBy") 
	private String  updateBy;
	@XStreamAlias("UpdateTime") 
	private String  updateTime;
	@XStreamAlias("AdvancedPayDetails") 
	private List<AdvancedPayDetail>  advancedPayDetails;
	
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
	public String getLossTime() {
		return lossTime;
	}
	public void setLossTime(String lossTime) {
		this.lossTime = lossTime;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDutyInd() {
		return dutyInd;
	}
	public void setDutyInd(String dutyInd) {
		this.dutyInd = dutyInd;
	}
	public String getTrafficDepartment() {
		return trafficDepartment;
	}
	public void setTrafficDepartment(String trafficDepartment) {
		this.trafficDepartment = trafficDepartment;
	}
	public String getPointConstableTel() {
		return pointConstableTel;
	}
	public void setPointConstableTel(String pointConstableTel) {
		this.pointConstableTel = pointConstableTel;
	}
	public String getNoticedAmt() {
		return noticedAmt;
	}
	public void setNoticedAmt(String noticedAmt) {
		this.noticedAmt = noticedAmt;
	}
	public String getNoticedTime() {
		return noticedTime;
	}
	public void setNoticedTime(String noticedTime) {
		this.noticedTime = noticedTime;
	}
	public String getReceivedNoticeTime() {
		return receivedNoticeTime;
	}
	public void setReceivedNoticeTime(String receivedNoticeTime) {
		this.receivedNoticeTime = receivedNoticeTime;
	}
	public String getInjuredNumber() {
		return injuredNumber;
	}
	public void setInjuredNumber(String injuredNumber) {
		this.injuredNumber = injuredNumber;
	}
	public String getAdvancedPayNumber() {
		return advancedPayNumber;
	}
	public void setAdvancedPayNumber(String advancedPayNumber) {
		this.advancedPayNumber = advancedPayNumber;
	}
	public String getAdvancedPayAmt() {
		return advancedPayAmt;
	}
	public void setAdvancedPayAmt(String advancedPayAmt) {
		this.advancedPayAmt = advancedPayAmt;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public List<AdvancedPayDetail> getAdvancedPayDetails() {
		return advancedPayDetails;
	}
	public void setAdvancedPayDetails(List<AdvancedPayDetail> advancedPayDetails) {
		this.advancedPayDetails = advancedPayDetails;
	}
	
	
	
}
