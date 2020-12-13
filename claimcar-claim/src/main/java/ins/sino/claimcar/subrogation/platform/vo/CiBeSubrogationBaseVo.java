/******************************************************************************
* CREATETIME : 2016年3月16日 下午12:10:37
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.Date;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiBeSubrogationBaseVo {
	/** 追偿方保险公司 **/ 
	@XmlElement(name="INSURER_CODE")
	private String insurerCode; 

	/** 追偿方承保地区 **/ 
	@XmlElement(name="INSURER_AREA")
	private String insurerArea; 

	/** 本方理赔编码 **/ 
	@XmlElement(name="CLAIM_CODE")
	private String claimCode; 

	/** 本方报案号 **/ 
	@XmlElement(name="REPORT_NO")
	private String reportNo; 

	/** 本方投保确认码 **/ 
	@XmlElement(name="CONFIRM_SEQUENCE_NO")
	private String confirmSequenceNo; 

	/** 锁定时间起始 **/ 
	@XmlElement(name="LOCKED_TIME_START")
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date lockedTimeStart; 

	/** 锁定时间终止 **/ 
	@XmlElement(name="LOCKED_TIME_END")
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date lockedTimeEnd;

	public String getInsurerCode() {
		return insurerCode;
	}

	public void setInsurerCode(String insurerCode) {
		this.insurerCode = insurerCode;
	}

	public String getInsurerArea() {
		return insurerArea;
	}

	public void setInsurerArea(String insurerArea) {
		this.insurerArea = insurerArea;
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

	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}

	public Date getLockedTimeStart() {
		return lockedTimeStart;
	}

	public void setLockedTimeStart(Date lockedTimeStart) {
		this.lockedTimeStart = lockedTimeStart;
	}

	public Date getLockedTimeEnd() {
		return lockedTimeEnd;
	}

	public void setLockedTimeEnd(Date lockedTimeEnd) {
		this.lockedTimeEnd = lockedTimeEnd;
	} 



	
}
