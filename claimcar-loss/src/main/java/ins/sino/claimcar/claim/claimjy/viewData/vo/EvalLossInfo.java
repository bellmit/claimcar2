package ins.sino.claimcar.claim.claimjy.viewData.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EvalLossInfo")
public class EvalLossInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	@XStreamAlias("LossNo")//定损单号
	private String lossNo="";
	@XStreamAlias("ReportCode")//报案号
	private String reportCode="";
	@XStreamAlias("DmgVhclId")//车损标的主键
	private String dmgVhclId="";
	@XStreamAlias("PlateNo")//车牌号码
	private String plateNo="";
	@XStreamAlias("NetworkFlag")//内外网标识
	private String networkFlag="";
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
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public String getNetworkFlag() {
		return networkFlag;
	}
	public void setNetworkFlag(String networkFlag) {
		this.networkFlag = networkFlag;
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
    

	
}
