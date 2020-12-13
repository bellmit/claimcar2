/******************************************************************************
* CREATETIME : 2016年3月16日 下午12:10:37
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiRecoveryQueryBaseVo {
	/** 对方保险公司 **/ 
	@XmlElement(name="OPPOENT_INSURER_CODE")
	private String oppoentInsurerCode; 

	/** 对方承保地区 **/ 
	@XmlElement(name="OPPOENT_INSURER_AREA")
	private String oppoentInsurerArea; 

	/** 对方保单险种类型 **/ 
	@XmlElement(name="OPPOENT_COVERAGE_TYPE")
	private String oppoentCoverageType; 

	/** 对方报案号 **/ 
	@XmlElement(name="OPPOENT_REPORT_NO")
	private String oppoentReportNo; 

	/** 本方追偿状态 **/ 
	@XmlElement(name="RECOVER_STATUS")
	private String recoverStatus; 

	/** 本方理赔编码 **/ 
	@XmlElement(name="CLAIM_CODE", required = true)
	private String claimCode; 

	/** 本方报案号 **/ 
	@XmlElement(name="REPORT_NO", required = true)
	private String reportNo;

	public String getOppoentInsurerCode() {
		return oppoentInsurerCode;
	}

	public void setOppoentInsurerCode(String oppoentInsurerCode) {
		this.oppoentInsurerCode = oppoentInsurerCode;
	}

	public String getOppoentInsurerArea() {
		return oppoentInsurerArea;
	}

	public void setOppoentInsurerArea(String oppoentInsurerArea) {
		this.oppoentInsurerArea = oppoentInsurerArea;
	}

	public String getOppoentCoverageType() {
		return oppoentCoverageType;
	}

	public void setOppoentCoverageType(String oppoentCoverageType) {
		this.oppoentCoverageType = oppoentCoverageType;
	}

	public String getOppoentReportNo() {
		return oppoentReportNo;
	}

	public void setOppoentReportNo(String oppoentReportNo) {
		this.oppoentReportNo = oppoentReportNo;
	}

	public String getRecoverStatus() {
		return recoverStatus;
	}

	public void setRecoverStatus(String recoverStatus) {
		this.recoverStatus = recoverStatus;
	}

	public String getClaimCode() {
		return claimCode;
	}

	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	} 
	
	
	

	
}
