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
public class BiLockedQueryBaseVo {

	/** 本方理赔编号 **/ 
	@XmlElement(name="ClaimSequenceNo", required = true)
	private String claimSequenceNo; 

	/** 本方报案号 **/ 
	@XmlElement(name="ClaimNotificationNo", required = true)
	private String claimNotificationNo; 

	/** 责任对方车辆号牌号码 **/ 
	@XmlElement(name="OppoentLicensePlateNo")
	private String oppoentLicensePlateNo; 

	/** 责任对方车辆号牌种类 **/ 
	@XmlElement(name="OppoentLicensePlateType")
	private String oppoentLicensePlateType; 

	/** 责任对方车辆发动机号 **/ 
	@XmlElement(name="OppoentEngineNo")
	private String oppoentEngineNo; 

	/** 责任对方车辆VIN码 **/ 
	@XmlElement(name="OppoentVIN")
	private String oppoentVIN; 

	/** 责任对方保险公司 **/ 
	@XmlElement(name="OppoentInsurerCode")
	private String oppoentInsurerCode; 

	/** 责任对方承保地区 **/ 
	@XmlElement(name="OppoentInsurerArea", required = true)
	private String oppoentInsurerArea; 

	/** 责任对方保单号 **/ 
	@XmlElement(name="OppoentPolicyNo")
	private String oppoentPolicyNo; 

	/** 责任对方报案号 **/ 
	@XmlElement(name="OppoentClaimNotificationNo")
	private String oppoentClaimNotificationNo;

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

	public String getOppoentLicensePlateNo() {
		return oppoentLicensePlateNo;
	}

	public void setOppoentLicensePlateNo(String oppoentLicensePlateNo) {
		this.oppoentLicensePlateNo = oppoentLicensePlateNo;
	}

	public String getOppoentLicensePlateType() {
		return oppoentLicensePlateType;
	}

	public void setOppoentLicensePlateType(String oppoentLicensePlateType) {
		this.oppoentLicensePlateType = oppoentLicensePlateType;
	}

	public String getOppoentEngineNo() {
		return oppoentEngineNo;
	}

	public void setOppoentEngineNo(String oppoentEngineNo) {
		this.oppoentEngineNo = oppoentEngineNo;
	}

	public String getOppoentVIN() {
		return oppoentVIN;
	}

	public void setOppoentVIN(String oppoentVIN) {
		this.oppoentVIN = oppoentVIN;
	}

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

	public String getOppoentPolicyNo() {
		return oppoentPolicyNo;
	}

	public void setOppoentPolicyNo(String oppoentPolicyNo) {
		this.oppoentPolicyNo = oppoentPolicyNo;
	}

	public String getOppoentClaimNotificationNo() {
		return oppoentClaimNotificationNo;
	}

	public void setOppoentClaimNotificationNo(String oppoentClaimNotificationNo) {
		this.oppoentClaimNotificationNo = oppoentClaimNotificationNo;
	} 


}
