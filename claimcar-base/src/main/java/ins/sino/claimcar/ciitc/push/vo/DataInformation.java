package ins.sino.claimcar.ciitc.push.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("DataInformation")
public class DataInformation implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("acciNo")
	private String acciNo;//事故编号
	
	@XStreamAlias("acciAreaCode")
	private String acciAreaCode;//事故地区代码
	
	@XStreamAlias("caseFlag")
	private String caseFlag;//业务类型
	
	@XStreamAlias("acciDuty")
	private String acciDuty;//事故责任
	
	@XStreamAlias("comAreaCode")
	private String comAreaCode;//承保公司所在地区代码（省级）
	
	@XStreamAlias("licenseNo")
	private String licenseNo;//车辆号牌号码
	
	@XStreamAlias("licenseType")
	private String licenseType;//车辆号牌种类
	
	@XStreamAlias("engineNo")
	private String engineNo;//车辆发动机号

	@XStreamAlias("vin")
	private String vin;//车辆车架号
	
	@XStreamAlias("reportDate")
	private String reportDate;//事故发生时间 格式: YYYYMMDDHHMMSS
	
	@XStreamAlias("caseSource")
	private String caseSource;//案件来源 -01交警在线app-02北京交警app-03北京交警警用版-04北京交警短信版
	//-05北京交警微信版-06北京交警支付宝版-07其他-08交警12123-09地方自建平台

	public String getAcciNo() {
		return acciNo;
	}

	public void setAcciNo(String acciNo) {
		this.acciNo = acciNo;
	}

	public String getAcciAreaCode() {
		return acciAreaCode;
	}

	public void setAcciAreaCode(String acciAreaCode) {
		this.acciAreaCode = acciAreaCode;
	}

	public String getCaseFlag() {
		return caseFlag;
	}

	public void setCaseFlag(String caseFlag) {
		this.caseFlag = caseFlag;
	}

	public String getAcciDuty() {
		return acciDuty;
	}

	public void setAcciDuty(String acciDuty) {
		this.acciDuty = acciDuty;
	}

	public String getComAreaCode() {
		return comAreaCode;
	}

	public void setComAreaCode(String comAreaCode) {
		this.comAreaCode = comAreaCode;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getCaseSource() {
		return caseSource;
	}

	public void setCaseSource(String caseSource) {
		this.caseSource = caseSource;
	}

}
