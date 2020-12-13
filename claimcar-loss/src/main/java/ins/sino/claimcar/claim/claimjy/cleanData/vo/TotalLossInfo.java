package ins.sino.claimcar.claim.claimjy.cleanData.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("TotalLossInfo")
public class TotalLossInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	@XStreamAlias("LossNo")//定损单号
	private String lossNo="";
	@XStreamAlias("ReportCode")//报案号
	private String reportCode="";
	@XStreamAlias("DmgVhclId")//车损标的主键
	private String dmgVhclId="";
	@XStreamAlias("LossPrice")//全损金额
	private String lossPrice="";
	@XStreamAlias("ComCode")//操作人所属分机构代码
	private String comCode="";
	@XStreamAlias("Company")//操作人所属分机构名称
	private String company="";
	@XStreamAlias("BranchComCode")//操作人所属中支代码
	private String branchComCode="";
	@XStreamAlias("BranchComName")//操作人所属中支名称
	private String branchComName="";
	@XStreamAlias("HandlerCode")//操作人代码
	private String handlerCode="";
	@XStreamAlias("HandlerName")//操作人名称
	private String handlerName="";
	@XStreamAlias("Remark")//意见
	private String remark="";
	public String getLossNo() {
		return lossNo;
	}
	public void setLossNo(String lossNo) {
		this.lossNo = lossNo;
	}
	public String getReportCode() {
		return reportCode;
	}
	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
	public String getDmgVhclId() {
		return dmgVhclId;
	}
	public void setDmgVhclId(String dmgVhclId) {
		this.dmgVhclId = dmgVhclId;
	}
	public String getLossPrice() {
		return lossPrice;
	}
	public void setLossPrice(String lossPrice) {
		this.lossPrice = lossPrice;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getBranchComCode() {
		return branchComCode;
	}
	public void setBranchComCode(String branchComCode) {
		this.branchComCode = branchComCode;
	}
	public String getBranchComName() {
		return branchComName;
	}
	public void setBranchComName(String branchComName) {
		this.branchComName = branchComName;
	}
	public String getHandlerCode() {
		return handlerCode;
	}
	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}
	public String getHandlerName() {
		return handlerName;
	}
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
