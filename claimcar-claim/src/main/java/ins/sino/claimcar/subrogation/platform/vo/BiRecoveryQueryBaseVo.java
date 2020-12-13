/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:19:11
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiRecoveryQueryBaseVo {
	/** 对方保险公司 **/ 
	@XmlElement(name="OppoentInsurerCode")
	private String oppoentInsurerCode; 

	/** 对方承保地区 **/ 
	@XmlElement(name="OppoentInsurerArea")
	private String oppoentInsurerArea; 

	/** 对方保单险种类型 **/ 
	@XmlElement(name="OppoentCoverageType")
	private String oppoentCoverageType; 

	/** 对方报案号 **/ 
	@XmlElement(name="OppoentClaimNotificationNo")
	private String oppoentClaimNotificationNo; 

	/** 本方追偿状态 **/ 
	@XmlElement(name="RecoverStatus")
	private String recoverStatus; 

	/** 本方理赔编码 **/ 
	@XmlElement(name="ClaimSequenceNo", required = true)
	private String claimSequenceNo; 

	/** 本方报案号 **/ 
	@XmlElement(name="ClaimNotificationNo", required = true)
	private String claimNotificationNo;

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

	public String getOppoentClaimNotificationNo() {
		return oppoentClaimNotificationNo;
	}

	public void setOppoentClaimNotificationNo(String oppoentClaimNotificationNo) {
		this.oppoentClaimNotificationNo = oppoentClaimNotificationNo;
	}

	public String getRecoverStatus() {
		return recoverStatus;
	}

	public void setRecoverStatus(String recoverStatus) {
		this.recoverStatus = recoverStatus;
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


}
