package ins.sino.claimcar.moblie.caselist.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CASEINFO")
public class MCqryCaseListResBody implements Serializable{

	/**  */
	private static final long serialVersionUID = 5073372372955307182L;
	
	@XStreamAlias("REGISTNO")
	private String registNo; //报案号
	
	@XStreamAlias("LICENSENO")
	private String licenseNo; //车牌号
	
	@XStreamAlias("INUREDNAME")
	private String insuredName; // 被保险人姓名 
	
	@XStreamAlias("CASESTATE")
	private String caseState;  // 案件状态
	
	@XStreamAlias("ISVIP")
	private String isVip;   //是否Vip
	
	@XStreamAlias("DRIVERNAME")
	private String driverName; //驾驶员姓名
	
	@XStreamAlias("DAMAGEDATE")
	private String damageDate;   //出险时间
	
	@XStreamAlias("ISJUDGE")
	private String isJudge="0";   //是否可以评价(查勘完成)
	
	@XStreamAlias("CASESTATUS")
	private String casesSatus;   //任务状态
	
	@XStreamAlias("CASEFLAG")
	private String caseFlag;   //案件标示
	
	@XStreamAlias("PRINTLIST")
	private List<PrintInfo> printInfos;

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getCaseState() {
		return caseState;
	}

	public void setCaseState(String caseState) {
		this.caseState = caseState;
	}

	public String getIsVip() {
		return isVip;
	}

	public void setIsVip(String isVip) {
		this.isVip = isVip;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDamageDate() {
		return damageDate;
	}

	public void setDamageDate(String damageDate) {
		this.damageDate = damageDate;
	}

	public String getIsJudge() {
		return isJudge;
	}

	public void setIsJudge(String isJudge) {
		this.isJudge = isJudge;
	}

	
	public String getCasesSatus() {
		return casesSatus;
	}

	public void setCasesSatus(String casesSatus) {
		this.casesSatus = casesSatus;
	}

	public String getCaseFlag() {
		return caseFlag;
	}

	public void setCaseFlag(String caseFlag) {
		this.caseFlag = caseFlag;
	}

	public List<PrintInfo> getPrintInfos() {
		return printInfos;
	}

	public void setPrintInfos(List<PrintInfo> printInfos) {
		this.printInfos = printInfos;
	}

	
}
