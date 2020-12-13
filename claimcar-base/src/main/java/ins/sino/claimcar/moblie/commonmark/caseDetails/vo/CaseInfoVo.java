package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;



import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("CASEINFO")
public class CaseInfoVo implements Serializable{
    
    /**  */
    private static final long serialVersionUID = 1L;

	@XStreamAlias("STICPSTYPE")
    private String sticpsType;//交强险理赔状态
    
	@XStreamAlias("BUSICPSTYPE")
    private String busicpsType;//商业险理赔状态
	
    @XStreamAlias("CASESTATUS")
    private String caseStatus; //任务状态
    
    @XStreamAlias("DAMAGETIME")
    private String damageTime; //出险时间
    
    @XStreamAlias("REPORTORNAME")
    private String reportorName; //报案人

    @XStreamAlias("REPORTORPHONE")
    private String reportorPhone; //报案人手机号

    @XStreamAlias("DUTYTYPE")
    private String dutyType; //责任类型
    
    @XStreamAlias("CASEFLAG")
    private String caseFlag; //案件标示
    



	public String getSticpsType() {
		return sticpsType;
	}

	public void setSticpsType(String sticpsType) {
		this.sticpsType = sticpsType;
	}

	public String getBusicpsType() {
		return busicpsType;
	}

	public void setBusicpsType(String busicpsType) {
		this.busicpsType = busicpsType;
	}

	public String getDamageTime() {
		return damageTime;
	}

	public void setDamageTime(String damageTime) {
		this.damageTime = damageTime;
	}

	public String getReportorName() {
		return reportorName;
	}

	public void setReportorName(String reportorName) {
		this.reportorName = reportorName;
	}

	public String getReportorPhone() {
		return reportorPhone;
	}

	public void setReportorPhone(String reportorPhone) {
		this.reportorPhone = reportorPhone;
	}

	public String getDutyType() {
		return dutyType;
	}

	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}

	public String getCaseFlag() {
		return caseFlag;
	}

	public void setCaseFlag(String caseFlag) {
		this.caseFlag = caseFlag;
	}

	public String getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}
    
    
}
