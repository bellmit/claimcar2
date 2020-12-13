package ins.sino.claimcar.interf.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("TransitClaimDocumentBase")
public class TransitClaimDocumentBaseVo {
	/*贵公司分支机构Code*/
	@XStreamAlias("BranchCompanyCode")
	private String branchCompanyCode;
	/*贵公司分支机构名称*/
	@XStreamAlias("BranchCompanyname")
	private String branchCompanyname;
	/*查勘信息*/
	@XStreamAlias("Check")
	private CheckVo checkVo;
	/*报案号*/
	@XStreamAlias("ClaimNumber")
	private String claimNumber;
    /*商业险信息*/
	@XStreamAlias("CommercialInsurance")
	private CommercialInsuranceVo commercialInsuranceVo;
    /*交强险信息 */
	@XStreamAlias("CompulsoryInsurance")
	private CompulsoryInsuranceVo compulsoryInsuranceVo;
	/*定损信息*/
	@XStreamAlias("ConfirmLoss")
	private ConfirmLossVo confirmLossVo;
	/*贵公司ID：401*/
	@XStreamAlias("InsuranceCompanyID")
	private String insuranceCompanyID;
	/*无责代赔*/
	@XStreamAlias("IsPayBackForOthers")
	private String isPayBackForOthers;
	/*车牌号*/
	@XStreamAlias("RegistrationNumber")
	private String registrationNumber;
	/*报案信息*/
	@XStreamAlias("Report")
	private ReportVo reportVo;
	/*车辆信息*/
	@XStreamAlias("Vehicle")
	private VehicleVo vehicleVo;
	/*本案其它涉案车辆列表*/
	@XStreamAlias("RelatingVehicles")
	private List<RelatingVehicleVo> relatingVehicles;
	public String getBranchCompanyCode() {
		return branchCompanyCode;
	}
	public void setBranchCompanyCode(String branchCompanyCode) {
		this.branchCompanyCode = branchCompanyCode;
	}
	public String getBranchCompanyname() {
		return branchCompanyname;
	}
	public void setBranchCompanyname(String branchCompanyname) {
		this.branchCompanyname = branchCompanyname;
	}
	public CheckVo getCheckVo() {
		return checkVo;
	}
	public void setCheckVo(CheckVo checkVo) {
		this.checkVo = checkVo;
	}
	public String getClaimNumber() {
		return claimNumber;
	}
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	public CommercialInsuranceVo getCommercialInsuranceVo() {
		return commercialInsuranceVo;
	}
	public void setCommercialInsuranceVo(CommercialInsuranceVo commercialInsuranceVo) {
		this.commercialInsuranceVo = commercialInsuranceVo;
	}
	public CompulsoryInsuranceVo getCompulsoryInsuranceVo() {
		return compulsoryInsuranceVo;
	}
	public void setCompulsoryInsuranceVo(CompulsoryInsuranceVo compulsoryInsuranceVo) {
		this.compulsoryInsuranceVo = compulsoryInsuranceVo;
	}
	public ConfirmLossVo getConfirmLossVo() {
		return confirmLossVo;
	}
	public void setConfirmLossVo(ConfirmLossVo confirmLossVo) {
		this.confirmLossVo = confirmLossVo;
	}
	public String getInsuranceCompanyID() {
		return insuranceCompanyID;
	}
	public void setInsuranceCompanyID(String insuranceCompanyID) {
		this.insuranceCompanyID = insuranceCompanyID;
	}
	public String getIsPayBackForOthers() {
		return isPayBackForOthers;
	}
	public void setIsPayBackForOthers(String isPayBackForOthers) {
		this.isPayBackForOthers = isPayBackForOthers;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public ReportVo getReportVo() {
		return reportVo;
	}
	public void setReportVo(ReportVo reportVo) {
		this.reportVo = reportVo;
	}
	public VehicleVo getVehicleVo() {
		return vehicleVo;
	}
	public void setVehicleVo(VehicleVo vehicleVo) {
		this.vehicleVo = vehicleVo;
	}
	public List<RelatingVehicleVo> getRelatingVehicles() {
		return relatingVehicles;
	}
	public void setRelatingVehicles(List<RelatingVehicleVo> relatingVehicles) {
		this.relatingVehicles = relatingVehicles;
	}
	



}
