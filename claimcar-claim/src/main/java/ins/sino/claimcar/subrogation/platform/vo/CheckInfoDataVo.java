/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.Date;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

//互审信息
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckInfoDataVo {
	/** 追偿金额 **/ 
	@XmlElement(name="RecoverAmount", required = true)
	private Double recoverAmount; 

	/** 清付金额 **/ 
	@XmlElement(name="CompensateAmount")
	private Double compensateAmount; 

	/** 互审时间 **/ 
	@XmlElement(name="CheckDate", required = true)
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date checkDate; 

	/** 互审意见 **/ 
	@XmlElement(name="CheckOpinion")
	private String checkOpinion; 

	/** 审核方类型；参见代码 **/ 
	@XmlElement(name="CheckOwnType", required = true)
	private String checkOwnType; 

	/** 互审状态；参见代码 **/ 
	@XmlElement(name="CheckStats", required = true)
	private String checkStats; 

	/** 责任对方案件状态；参见代码 **/ 
	@XmlElement(name="ClaimStatus")
	private String claimStatus; 

	/** 责任对方案件进展；参见代码 **/ 
	@XmlElement(name="ClaimProgress")
	private String claimProgress;

	public Double getRecoverAmount() {
		return recoverAmount;
	}

	public void setRecoverAmount(Double recoverAmount) {
		this.recoverAmount = recoverAmount;
	}

	public Double getCompensateAmount() {
		return compensateAmount;
	}

	public void setCompensateAmount(Double compensateAmount) {
		this.compensateAmount = compensateAmount;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public String getCheckOpinion() {
		return checkOpinion;
	}

	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
	}

	public String getCheckOwnType() {
		return checkOwnType;
	}

	public void setCheckOwnType(String checkOwnType) {
		this.checkOwnType = checkOwnType;
	}

	public String getCheckStats() {
		return checkStats;
	}

	public void setCheckStats(String checkStats) {
		this.checkStats = checkStats;
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



}
