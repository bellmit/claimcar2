package ins.sino.claimcar.pinganunion.vo.lawsuit;

import java.io.Serializable;
/**
 * 
 * @Description: 诉讼代理信息
 * @author: zhubin
 * @date: 2020年8月12日 下午7:22:01
 */
public class LawsuitAgentDTO implements Serializable{
	private static final long serialVersionUID = 2145923066917799635L;
	private String lawsuitNo;//诉讼号：
	private String lawsuitStage;//诉讼阶段：1-一审、2-二审
	private String sessionType;//出庭方式：1-员工、2-律师、3-答辩状
	private String sessionEmployee;//出庭员工：
	private String lawyerType;//律师类型：1-库内律师、2-库外律师
	private String mobilPhone;//手机：
	private String lawyerName;//律师姓名：
	private String practiceName;//律所名称：
	private String agentType;//代理方式 ：1-一般、2-风险
	private String engageLawyerReason;//聘请律师理由：
	private String lawyerCertificateNo;//律师执业证件号：
	public String getLawsuitNo() {
		return lawsuitNo;
	}
	public void setLawsuitNo(String lawsuitNo) {
		this.lawsuitNo = lawsuitNo;
	}
	public String getLawsuitStage() {
		return lawsuitStage;
	}
	public void setLawsuitStage(String lawsuitStage) {
		this.lawsuitStage = lawsuitStage;
	}
	public String getSessionType() {
		return sessionType;
	}
	public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}
	public String getSessionEmployee() {
		return sessionEmployee;
	}
	public void setSessionEmployee(String sessionEmployee) {
		this.sessionEmployee = sessionEmployee;
	}
	public String getLawyerType() {
		return lawyerType;
	}
	public void setLawyerType(String lawyerType) {
		this.lawyerType = lawyerType;
	}
	public String getMobilPhone() {
		return mobilPhone;
	}
	public void setMobilPhone(String mobilPhone) {
		this.mobilPhone = mobilPhone;
	}
	public String getLawyerName() {
		return lawyerName;
	}
	public void setLawyerName(String lawyerName) {
		this.lawyerName = lawyerName;
	}
	public String getPracticeName() {
		return practiceName;
	}
	public void setPracticeName(String practiceName) {
		this.practiceName = practiceName;
	}
	public String getAgentType() {
		return agentType;
	}
	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
	public String getEngageLawyerReason() {
		return engageLawyerReason;
	}
	public void setEngageLawyerReason(String engageLawyerReason) {
		this.engageLawyerReason = engageLawyerReason;
	}
	public String getLawyerCertificateNo() {
		return lawyerCertificateNo;
	}
	public void setLawyerCertificateNo(String lawyerCertificateNo) {
		this.lawyerCertificateNo = lawyerCertificateNo;
	}
	
	
}
