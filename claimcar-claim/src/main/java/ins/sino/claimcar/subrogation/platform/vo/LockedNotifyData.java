/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class LockedNotifyData {
	/** 责任对方报案号 **/ 
	@XmlElement(name="ClaimNotificationNo", required = true)
	private String claimNotificationNo; 

	/** 出险时间 **/ 
	@XmlElement(name="LossTime", required = true)
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date lossTime; 

	/** 出险地点 **/ 
	@XmlElement(name="LossArea", required = true)
	private String lossArea; 

	/** 出险经过 **/ 
	@XmlElement(name="LossDesc")
	private String lossDesc; 

	/** 案件状态 **/ 
	@XmlElement(name="ClaimStatus", required = true)
	private String claimStatus; 

	/** 案件进展 **/ 
	@XmlElement(name="ClaimProgress", required = true)
	private String claimProgress;

	@XmlElement(name = "LockedThirdPartyData")
	private List<LockedThirdPartyData> prplLockedThirdParties;
	
	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}

	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
	}

	public Date getLossTime() {
		return lossTime;
	}

	public void setLossTime(Date lossTime) {
		this.lossTime = lossTime;
	}

	public String getLossArea() {
		return lossArea;
	}

	public void setLossArea(String lossArea) {
		this.lossArea = lossArea;
	}

	public String getLossDesc() {
		return lossDesc;
	}

	public void setLossDesc(String lossDesc) {
		this.lossDesc = lossDesc;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getClaimProgress() {
		return claimProgress;
	}

	public void setClaimProgress(String claimProgress) {
		this.claimProgress = claimProgress;
	}

	
	public List<LockedThirdPartyData> getPrplLockedThirdParties() {
		return prplLockedThirdParties;
	}

	
	public void setPrplLockedThirdParties(List<LockedThirdPartyData> prplLockedThirdParties) {
		this.prplLockedThirdParties = prplLockedThirdParties;
	}

	

}
