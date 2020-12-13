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
public class BiSubrogationClaimDataVo {

	@XmlElement(name = "ClaimRegistrationNo", required = true)
	private String claimRegistrationNo;//立案号

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "ClaimRegistrationTime", required = true)
	private Date claimRegistrationTime;//立案时间；格式：精确到分钟

	@XmlElement(name = "EstimatedLossAmount", required = true)
	private Double estimatedLossAmount;//估损金额


	/** 
	 * @return 返回 claimRegistrationNo  立案号
	 */ 
	public String getClaimRegistrationNo(){ 
	    return claimRegistrationNo;
	}

	/** 
	 * @param claimRegistrationNo 要设置的 立案号
	 */ 
	public void setClaimRegistrationNo(String claimRegistrationNo){ 
	    this.claimRegistrationNo=claimRegistrationNo;
	}

	/** 
	 * @return 返回 claimRegistrationTime  立案时间；格式：精确到分钟
	 */ 
	public Date getClaimRegistrationTime(){ 
	    return claimRegistrationTime;
	}

	/** 
	 * @param claimRegistrationTime 要设置的 立案时间；格式：精确到分钟
	 */ 
	public void setClaimRegistrationTime(Date claimRegistrationTime){ 
	    this.claimRegistrationTime=claimRegistrationTime;
	}

	/** 
	 * @return 返回 estimatedLossAmount  估损金额
	 */ 
	public Double getEstimatedLossAmount(){ 
	    return estimatedLossAmount;
	}

	/** 
	 * @param estimatedLossAmount 要设置的 估损金额
	 */ 
	public void setEstimatedLossAmount(Double estimatedLossAmount){ 
	    this.estimatedLossAmount=estimatedLossAmount;
	}




}
