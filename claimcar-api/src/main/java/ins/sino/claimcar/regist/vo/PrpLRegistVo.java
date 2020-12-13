package ins.sino.claimcar.regist.vo;


/**
 * Custom VO class of PO PrpLRegist
 */ 
public class PrpLRegistVo extends PrpLRegistVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private String license;
	private String selfDefinareaCode;
	private String isPeopleflag;//调度是否带出人伤标志
	private String accidentReason; //事故原因  送上海平台
	private String isMobileCase; //是否为移动端案件
	private String isQuickCase; //是否为快赔案件
	private String accidentNo;//事故编号
	private String serviceMobile;//推送修手机号
	private Long repairId;//推送修手机号匹配的修理厂主表Id
	private String paicReportNo;//平安联盟案件号
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getSelfDefinareaCode() {
		return selfDefinareaCode;
	}
	public void setSelfDefinareaCode(String selfDefinareaCode) {
		this.selfDefinareaCode = selfDefinareaCode;
	}
	public String getIsPeopleflag() {
		return isPeopleflag;
	}
	public void setIsPeopleflag(String isPeopleflag) {
		this.isPeopleflag = isPeopleflag;
	}
	public String getAccidentReason() {
		return accidentReason;
	}
	public void setAccidentReason(String accidentReason) {
		this.accidentReason = accidentReason;
	}
    
    public String getIsMobileCase() {
        return isMobileCase;
    }
    
    public void setIsMobileCase(String isMobileCase) {
        this.isMobileCase = isMobileCase;
    }
    
    public String getIsQuickCase() {
        return isQuickCase;
    }
    
    public void setIsQuickCase(String isQuickCase) {
        this.isQuickCase = isQuickCase;
    }
	public String getAccidentNo() {
		return accidentNo;
	}
	public void setAccidentNo(String accidentNo) {
		this.accidentNo = accidentNo;
	}
	public String getServiceMobile() {
		return serviceMobile;
	}
	public void setServiceMobile(String serviceMobile) {
		this.serviceMobile = serviceMobile;
	}
	public Long getRepairId() {
		return repairId;
	}
	public void setRepairId(Long repairId) {
		this.repairId = repairId;
	}

	public String getPaicReportNo() {
		return paicReportNo;
	}

	public void setPaicReportNo(String paicReportNo) {
		this.paicReportNo = paicReportNo;
	}
}
