package ins.sino.claimcar.genilex.vo.endCase;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("AdvancedPayDetail")
public class AdvancedPayDetail implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("SerialNo") 
	private String  serialNo;
	@XStreamAlias("InjuredName") 
	private String  injuredName;
	@XStreamAlias("HospitalName") 
	private String  hospitalName;
	@XStreamAlias("HospitalLinkman") 
	private String  hospitalLinkman;
	@XStreamAlias("HospitalTel") 
	private String  hospitalTel;
	@XStreamAlias("HospitalAccounts") 
	private String  hospitalAccounts;
	@XStreamAlias("BankName") 
	private String  bankName;
	@XStreamAlias("SectionOfficeName") 
	private String  sectionOfficeName;
	@XStreamAlias("BedNo") 
	private String  bedNo;
	@XStreamAlias("AdvancedPayAmt") 
	private String  advancedPayAmt;
	@XStreamAlias("DiagnoseDetail") 
	private String  diagnoseDetail;
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
	
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getInjuredName() {
		return injuredName;
	}
	public void setInjuredName(String injuredName) {
		this.injuredName = injuredName;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getHospitalLinkman() {
		return hospitalLinkman;
	}
	public void setHospitalLinkman(String hospitalLinkman) {
		this.hospitalLinkman = hospitalLinkman;
	}
	public String getHospitalTel() {
		return hospitalTel;
	}
	public void setHospitalTel(String hospitalTel) {
		this.hospitalTel = hospitalTel;
	}
	public String getHospitalAccounts() {
		return hospitalAccounts;
	}
	public void setHospitalAccounts(String hospitalAccounts) {
		this.hospitalAccounts = hospitalAccounts;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getSectionOfficeName() {
		return sectionOfficeName;
	}
	public void setSectionOfficeName(String sectionOfficeName) {
		this.sectionOfficeName = sectionOfficeName;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getAdvancedPayAmt() {
		return advancedPayAmt;
	}
	public void setAdvancedPayAmt(String advancedPayAmt) {
		this.advancedPayAmt = advancedPayAmt;
	}
	public String getDiagnoseDetail() {
		return diagnoseDetail;
	}
	public void setDiagnoseDetail(String diagnoseDetail) {
		this.diagnoseDetail = diagnoseDetail;
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
	
	
	
	
}
