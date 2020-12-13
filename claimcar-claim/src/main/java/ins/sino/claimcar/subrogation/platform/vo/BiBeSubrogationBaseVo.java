/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:19:11
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
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiBeSubrogationBaseVo {

	/** 追偿方保险公司  **/ 
	@XmlElement(name="InsurerCode")
	private String insurerCode; 

	/** 追偿方承保地区 **/ 
	@XmlElement(name="InsurerArea")
	private String insurerArea; 

	/** 本方理赔编码 **/ 
	@XmlElement(name="ClaimSequenceNo")
	private String claimSequenceNo; 

	/** 本方报案号 **/ 
	@XmlElement(name="ClaimNotificationNo")
	private String claimNotificationNo; 

	/** 本方投保确认码 **/ 
	@XmlElement(name="ConfirmSequenceNo")
	private String confirmSequenceNo; 

	/** 锁定时间起始 **/ 
	@XmlElement(name="LockedTimeStart")
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date lockedTimeStart; 

	/** 锁定时间终止 **/ 
	@XmlElement(name="LockedTimeEnd")
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

	public String getClaimSequenceNo() {
		return claimSequenceNo;
	}

	public void setClaimSequenceNo(String claimSequenceNo) {
		this.claimSequenceNo = claimSequenceNo;
	}

	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}

	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
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
