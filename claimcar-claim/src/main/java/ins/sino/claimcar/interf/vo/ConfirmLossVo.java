package ins.sino.claimcar.interf.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ConfirmLoss")
public class ConfirmLossVo {
	/*查勘地址*/
	@XStreamAlias("ConfirmLossAddress")
	private String confirmLossAddress;
	/*定损机构*/
	@XStreamAlias("ConfirmLossCompany")
	private String confirmLossCompany;
	/*定损机构Code*/
	@XStreamAlias("ConfirmLossCompanyCode")
	private String confirmLossCompanyCode;
	/*定损时间*/
	@XStreamAlias("ConfirmLossDate")
	private String confirmLossDate;
	/*定核损意见列表*/
	@XStreamAlias("ConfirmLossDiscussions")
	private List<ConfirmLossDiscussionVo> confirmLossDiscussions;
	/*定损员*/
	@XStreamAlias("ConfirmLossEmployee")
	private String confirmLossEmployee;
	/*定损员代码*/
	@XStreamAlias("ConfirmLossEmployeeCode")
	private String confirmLossEmployeeCode;
	/*本案定损险种*/
	@XStreamAlias("ConfirmLossInsuranceItem")
	private String confirmLossInsuranceItem;
	/*定损意见*/
	@XStreamAlias("ConfirmOpinion")
	private String confirmOpinion;
	/*定损类型*/
	@XStreamAlias("EvaluationType")
	private String evaluationType;
	/*是否无责代赔*/
	@XStreamAlias("IsPayBackForOthers")
	private String isPayBackForOthers;
	/*工时列表*/
	@XStreamAlias("Labors")
	private List<LaborVo> labors;
	/*损失类型*/
	@XStreamAlias("LossType")
	private String lossType;
	/*修理厂代码*/
	@XStreamAlias("RepairFactoryCode")
	private String repairFactoryCode;
	/*修理厂名称*/
	@XStreamAlias("RepairFactoryName")
	private String repairFactoryName;
	/*修理厂类型*/
	@XStreamAlias("RepairFactoryType")
	private String repairFactoryType;
	/*辅料列表*/
	@XStreamAlias("SmallSpareparts")
	private List<SmallSparepartVo> smallSpareparts;
	/*配件列表*/
	@XStreamAlias("Spareparts")
	private List<SparepartVo> spareparts;
	/*施救费*/
	@XStreamAlias("RescueFee")
	private String rescueFee;
	public String getConfirmLossAddress() {
		return confirmLossAddress;
	}
	public void setConfirmLossAddress(String confirmLossAddress) {
		this.confirmLossAddress = confirmLossAddress;
	}
	public String getConfirmLossCompany() {
		return confirmLossCompany;
	}
	public void setConfirmLossCompany(String confirmLossCompany) {
		this.confirmLossCompany = confirmLossCompany;
	}
	public String getConfirmLossCompanyCode() {
		return confirmLossCompanyCode;
	}
	public void setConfirmLossCompanyCode(String confirmLossCompanyCode) {
		this.confirmLossCompanyCode = confirmLossCompanyCode;
	}
	public String getConfirmLossDate() {
		return confirmLossDate;
	}
	public void setConfirmLossDate(String confirmLossDate) {
		this.confirmLossDate = confirmLossDate;
	}
	public List<ConfirmLossDiscussionVo> getConfirmLossDiscussions() {
		return confirmLossDiscussions;
	}
	public void setConfirmLossDiscussions(
			List<ConfirmLossDiscussionVo> confirmLossDiscussions) {
		this.confirmLossDiscussions = confirmLossDiscussions;
	}
	public String getConfirmLossEmployee() {
		return confirmLossEmployee;
	}
	public void setConfirmLossEmployee(String confirmLossEmployee) {
		this.confirmLossEmployee = confirmLossEmployee;
	}
	public String getConfirmLossEmployeeCode() {
		return confirmLossEmployeeCode;
	}
	public void setConfirmLossEmployeeCode(String confirmLossEmployeeCode) {
		this.confirmLossEmployeeCode = confirmLossEmployeeCode;
	}
	public String getConfirmLossInsuranceItem() {
		return confirmLossInsuranceItem;
	}
	public void setConfirmLossInsuranceItem(String confirmLossInsuranceItem) {
		this.confirmLossInsuranceItem = confirmLossInsuranceItem;
	}
	public String getConfirmOpinion() {
		return confirmOpinion;
	}
	public void setConfirmOpinion(String confirmOpinion) {
		this.confirmOpinion = confirmOpinion;
	}
	public String getEvaluationType() {
		return evaluationType;
	}
	public void setEvaluationType(String evaluationType) {
		this.evaluationType = evaluationType;
	}
	public String getIsPayBackForOthers() {
		return isPayBackForOthers;
	}
	public void setIsPayBackForOthers(String isPayBackForOthers) {
		this.isPayBackForOthers = isPayBackForOthers;
	}
	public List<LaborVo> getLabors() {
		return labors;
	}
	public void setLabors(List<LaborVo> labors) {
		this.labors = labors;
	}
	public String getLossType() {
		return lossType;
	}
	public void setLossType(String lossType) {
		this.lossType = lossType;
	}
	public String getRepairFactoryCode() {
		return repairFactoryCode;
	}
	public void setRepairFactoryCode(String repairFactoryCode) {
		this.repairFactoryCode = repairFactoryCode;
	}
	public String getRepairFactoryName() {
		return repairFactoryName;
	}
	public void setRepairFactoryName(String repairFactoryName) {
		this.repairFactoryName = repairFactoryName;
	}
	public String getRepairFactoryType() {
		return repairFactoryType;
	}
	public void setRepairFactoryType(String repairFactoryType) {
		this.repairFactoryType = repairFactoryType;
	}
	public List<SmallSparepartVo> getSmallSpareparts() {
		return smallSpareparts;
	}
	public void setSmallSpareparts(List<SmallSparepartVo> smallSpareparts) {
		this.smallSpareparts = smallSpareparts;
	}
	public List<SparepartVo> getSpareparts() {
		return spareparts;
	}
	public void setSpareparts(List<SparepartVo> spareparts) {
		this.spareparts = spareparts;
	}
	public String getRescueFee() {
		return rescueFee;
	}
	public void setRescueFee(String rescueFee) {
		this.rescueFee = rescueFee;
	}
	


}
