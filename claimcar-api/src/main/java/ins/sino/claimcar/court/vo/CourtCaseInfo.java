package ins.sino.claimcar.court.vo;

import java.io.Serializable;
import java.util.List;

public class CourtCaseInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private CourtAccidentInfo accidentInfo;//事故基本信息
	private List<CourtPartyInfo> partyInfo;//当事人信息
	private List<CourtAgentInfo> agentInfo;//代理人信息
	private List<CourtIdentifyInfo> identifyInfo;//鉴定基本信息
	private List<CourtMediationInfo> mediationInfo;//调解信息
	private List<CourtCompensationInfo> compensationInfo;//赔偿明细
	private List<CourtOnlineRegistrationInfo> onlineRegistrationInfo;//网上立案信息
	private List<CourtLitigationMediationInfo> litigationMediationInfo;//诉前调解信息
	private CourtRegistInfo registInfo;//案件基本信息
	private List<CourtJudicialConfirmInfo> judicialconfirmInfo;//司法确认信息
	private List<CourtFileInfo> fileInfo;//文件信息
	
	public CourtAccidentInfo getAccidentInfo() {
		return accidentInfo;
	}
	public void setAccidentInfo(CourtAccidentInfo accidentInfo) {
		this.accidentInfo = accidentInfo;
	}
	public List<CourtPartyInfo> getPartyInfo() {
		return partyInfo;
	}
	public void setPartyInfo(List<CourtPartyInfo> partyInfo) {
		this.partyInfo = partyInfo;
	}
	public List<CourtAgentInfo> getAgentInfo() {
		return agentInfo;
	}
	public void setAgentInfo(List<CourtAgentInfo> agentInfo) {
		this.agentInfo = agentInfo;
	}
	public List<CourtIdentifyInfo> getIdentifyInfo() {
		return identifyInfo;
	}
	public void setIdentifyInfo(List<CourtIdentifyInfo> identifyInfo) {
		this.identifyInfo = identifyInfo;
	}
	public List<CourtMediationInfo> getMediationInfo() {
		return mediationInfo;
	}
	public void setMediationInfo(List<CourtMediationInfo> mediationInfo) {
		this.mediationInfo = mediationInfo;
	}
	public List<CourtCompensationInfo> getCompensationInfo() {
		return compensationInfo;
	}
	public void setCompensationInfo(List<CourtCompensationInfo> compensationInfo) {
		this.compensationInfo = compensationInfo;
	}
	public List<CourtOnlineRegistrationInfo> getOnlineRegistrationInfo() {
		return onlineRegistrationInfo;
	}
	public void setOnlineRegistrationInfo(
			List<CourtOnlineRegistrationInfo> onlineRegistrationInfo) {
		this.onlineRegistrationInfo = onlineRegistrationInfo;
	}
	public List<CourtLitigationMediationInfo> getLitigationMediationInfo() {
		return litigationMediationInfo;
	}
	public void setLitigationMediationInfo(
			List<CourtLitigationMediationInfo> litigationMediationInfo) {
		this.litigationMediationInfo = litigationMediationInfo;
	}
	public CourtRegistInfo getRegistInfo() {
		return registInfo;
	}
	public void setRegistInfo(CourtRegistInfo registInfo) {
		this.registInfo = registInfo;
	}
	public List<CourtFileInfo> getFileInfo() {
		return fileInfo;
	}
	public void setFileInfo(List<CourtFileInfo> fileInfo) {
		this.fileInfo = fileInfo;
	}
	public List<CourtJudicialConfirmInfo> getJudicialconfirmInfo() {
		return judicialconfirmInfo;
	}
	public void setJudicialconfirmInfo(
			List<CourtJudicialConfirmInfo> judicialconfirmInfo) {
		this.judicialconfirmInfo = judicialconfirmInfo;
	}
	
	
}
