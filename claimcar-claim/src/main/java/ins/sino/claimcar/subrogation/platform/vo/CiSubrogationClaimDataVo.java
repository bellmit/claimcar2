/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.Date;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

//立案信息
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiSubrogationClaimDataVo {

	/** 立案号 **/ 
	@XmlElement(name="REGISTRATION_NO", required = true)
	private String registrationNo; 

	/** 立案时间 **/ 
	@XmlElement(name="REGISTRATION_TIME", required = true)
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date registrationTime; 

	/** 估损金额 **/ 
	@XmlElement(name="ESTIMATE_AMOUNT", required = true)
	private Double estimateAmount;

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public Date getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(Date registrationTime) {
		this.registrationTime = registrationTime;
	}

	public Double getEstimateAmount() {
		return estimateAmount;
	}

	public void setEstimateAmount(Double estimateAmount) {
		this.estimateAmount = estimateAmount;
	} 



}
