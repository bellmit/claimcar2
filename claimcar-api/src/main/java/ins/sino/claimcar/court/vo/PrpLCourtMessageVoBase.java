package ins.sino.claimcar.court.vo;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PrpLCourtMessageVoBase implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;	
	private String areaCode;
	private String caseNo;
	private String searchCode;
	private String dutyNo;
	private String acciNo;
	private Date reportDate;
	private String acciAddress;
	private String acciResult;
	private String acciType;
	private String jkptUuid;
	private String userName;
	private String passWord;
	private Date createTime;
	private Date executeTime;
	private int executeTimes;
	private String retCode;
	private String retMessage;
	private PrpLCourtAccidentVo prpLCourtAccident;
	private List<PrpLCourtPartyVo> prpLCourtPartys;
	private List<PrpLCourtAgentVo> prpLCourtAgents;
	private List<PrpLCourtIdentifyVo> prpLCourtIdentifys;
	private List<PrpLCourtMediationVo> prpLCourtMediations;
	private List<PrpLCourtCompensationVo> prpLCourtCompensations;
	private List<PrpLCourtClaimVo> prpLCourtClaims;
	private List<PrpLCourtLitigationVo> prpLCourtLitigations;
	private PrpLCourtRegistVo prpLCourtRegist;
	private List<PrpLCourtConfirmVo> prpLCourtConfirms;
	private List<PrpLCourtFileVo> prpLCourtFiles;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	public String getSearchCode() {
		return searchCode;
	}
	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}
	public String getDutyNo() {
		return dutyNo;
	}
	public void setDutyNo(String dutyNo) {
		this.dutyNo = dutyNo;
	}
	public String getAcciNo() {
		return acciNo;
	}
	public void setAcciNo(String acciNo) {
		this.acciNo = acciNo;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public String getAcciAddress() {
		return acciAddress;
	}
	public void setAcciAddress(String acciAddress) {
		this.acciAddress = acciAddress;
	}
	public String getAcciResult() {
		return acciResult;
	}
	public void setAcciResult(String acciResult) {
		this.acciResult = acciResult;
	}
	public String getAcciType() {
		return acciType;
	}
	public void setAcciType(String acciType) {
		this.acciType = acciType;
	}
	public String getJkptUuid() {
		return jkptUuid;
	}
	public void setJkptUuid(String jkptUuid) {
		this.jkptUuid = jkptUuid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}
	public PrpLCourtAccidentVo getPrpLCourtAccident() {
		return prpLCourtAccident;
	}
	public void setPrpLCourtAccident(PrpLCourtAccidentVo prpLCourtAccident) {
		this.prpLCourtAccident = prpLCourtAccident;
	}
	public List<PrpLCourtPartyVo> getPrpLCourtPartys() {
		return prpLCourtPartys;
	}
	public void setPrpLCourtPartys(List<PrpLCourtPartyVo> prpLCourtPartys) {
		this.prpLCourtPartys = prpLCourtPartys;
	}
	public List<PrpLCourtAgentVo> getPrpLCourtAgents() {
		return prpLCourtAgents;
	}
	public void setPrpLCourtAgents(List<PrpLCourtAgentVo> prpLCourtAgents) {
		this.prpLCourtAgents = prpLCourtAgents;
	}
	public List<PrpLCourtIdentifyVo> getPrpLCourtIdentifys() {
		return prpLCourtIdentifys;
	}
	public void setPrpLCourtIdentifys(List<PrpLCourtIdentifyVo> prpLCourtIdentifys) {
		this.prpLCourtIdentifys = prpLCourtIdentifys;
	}
	public List<PrpLCourtMediationVo> getPrpLCourtMediations() {
		return prpLCourtMediations;
	}
	public void setPrpLCourtMediations(
			List<PrpLCourtMediationVo> prpLCourtMediations) {
		this.prpLCourtMediations = prpLCourtMediations;
	}
	public List<PrpLCourtCompensationVo> getPrpLCourtCompensations() {
		return prpLCourtCompensations;
	}
	public void setPrpLCourtCompensations(
			List<PrpLCourtCompensationVo> prpLCourtCompensations) {
		this.prpLCourtCompensations = prpLCourtCompensations;
	}
	public List<PrpLCourtClaimVo> getPrpLCourtClaims() {
		return prpLCourtClaims;
	}
	public void setPrpLCourtClaims(List<PrpLCourtClaimVo> prpLCourtClaims) {
		this.prpLCourtClaims = prpLCourtClaims;
	}
	public List<PrpLCourtLitigationVo> getPrpLCourtLitigations() {
		return prpLCourtLitigations;
	}
	public void setPrpLCourtLitigations(
			List<PrpLCourtLitigationVo> prpLCourtLitigations) {
		this.prpLCourtLitigations = prpLCourtLitigations;
	}
	public PrpLCourtRegistVo getPrpLCourtRegist() {
		return prpLCourtRegist;
	}
	public void setPrpLCourtRegist(PrpLCourtRegistVo prpLCourtRegist) {
		this.prpLCourtRegist = prpLCourtRegist;
	}
	public List<PrpLCourtConfirmVo> getPrpLCourtConfirms() {
		return prpLCourtConfirms;
	}
	public void setPrpLCourtConfirms(List<PrpLCourtConfirmVo> prpLCourtConfirms) {
		this.prpLCourtConfirms = prpLCourtConfirms;
	}
	public List<PrpLCourtFileVo> getPrpLCourtFiles() {
		return prpLCourtFiles;
	}
	public void setPrpLCourtFiles(List<PrpLCourtFileVo> prpLCourtFiles) {
		this.prpLCourtFiles = prpLCourtFiles;
	}
	public int getExecuteTimes() {
		return executeTimes;
	}
	public void setExecuteTimes(int executeTimes) {
		this.executeTimes = executeTimes;
	}
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getRetMessage() {
		return retMessage;
	}
	public void setRetMessage(String retMessage) {
		this.retMessage = retMessage;
	}
	
	
	
}
