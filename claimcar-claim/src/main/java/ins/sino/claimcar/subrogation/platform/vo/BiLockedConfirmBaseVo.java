/******************************************************************************
* CREATETIME : 2016年3月30日 上午11:49:24
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月30日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiLockedConfirmBaseVo {

	@XmlElement(name = "ClaimSequenceNo", required = true)
	private String claimSequenceNo;//本方理赔编码

	@XmlElement(name = "ClaimNotificationNo", required = true)
	private String claimNotificationNo;//本方报案号

	@XmlElement(name = "OppoentInsurerCode", required = true)
	private String oppoentInsurerCode;//责任对方保险公司；参见代码 

	@XmlElement(name = "OppoentInsurerArea", required = true)
	private String oppoentInsurerArea;//责任对方承保地区；参见代码

	@XmlElement(name = "OppoentCoverageType", required = true)
	private String oppoentCoverageType;//责任对方保单险种类型；参见代码

	@XmlElement(name = "OppoentPolicyNo", required = true)
	private String oppoentPolicyNo;//责任对方保单号

	@XmlElement(name = "OppoentClaimNotificationNo", required = true)
	private String oppoentClaimNotificationNo;//责任对方报案号


	/** 
	 * @return 返回 claimSequenceNo  本方理赔编码
	 */ 
	public String getClaimSequenceNo(){ 
	    return claimSequenceNo;
	}

	/** 
	 * @param claimSequenceNo 要设置的 本方理赔编码
	 */ 
	public void setClaimSequenceNo(String claimSequenceNo){ 
	    this.claimSequenceNo=claimSequenceNo;
	}

	/** 
	 * @return 返回 claimNotificationNo  本方报案号
	 */ 
	public String getClaimNotificationNo(){ 
	    return claimNotificationNo;
	}

	/** 
	 * @param claimNotificationNo 要设置的 本方报案号
	 */ 
	public void setClaimNotificationNo(String claimNotificationNo){ 
	    this.claimNotificationNo=claimNotificationNo;
	}

	/** 
	 * @return 返回 oppoentInsurerCode  责任对方保险公司；参见代码 
	 */ 
	public String getOppoentInsurerCode(){ 
	    return oppoentInsurerCode;
	}

	/** 
	 * @param oppoentInsurerCode 要设置的 责任对方保险公司；参见代码 
	 */ 
	public void setOppoentInsurerCode(String oppoentInsurerCode){ 
	    this.oppoentInsurerCode=oppoentInsurerCode;
	}

	/** 
	 * @return 返回 oppoentInsurerArea  责任对方承保地区；参见代码
	 */ 
	public String getOppoentInsurerArea(){ 
	    return oppoentInsurerArea;
	}

	/** 
	 * @param oppoentInsurerArea 要设置的 责任对方承保地区；参见代码
	 */ 
	public void setOppoentInsurerArea(String oppoentInsurerArea){ 
	    this.oppoentInsurerArea=oppoentInsurerArea;
	}

	/** 
	 * @return 返回 oppoentCoverageType  责任对方保单险种类型；参见代码
	 */ 
	public String getOppoentCoverageType(){ 
	    return oppoentCoverageType;
	}

	/** 
	 * @param oppoentCoverageType 要设置的 责任对方保单险种类型；参见代码
	 */ 
	public void setOppoentCoverageType(String oppoentCoverageType){ 
	    this.oppoentCoverageType=oppoentCoverageType;
	}

	/** 
	 * @return 返回 oppoentPolicyNo  责任对方保单号
	 */ 
	public String getOppoentPolicyNo(){ 
	    return oppoentPolicyNo;
	}

	/** 
	 * @param oppoentPolicyNo 要设置的 责任对方保单号
	 */ 
	public void setOppoentPolicyNo(String oppoentPolicyNo){ 
	    this.oppoentPolicyNo=oppoentPolicyNo;
	}

	/** 
	 * @return 返回 oppoentClaimNotificationNo  责任对方报案号
	 */ 
	public String getOppoentClaimNotificationNo(){ 
	    return oppoentClaimNotificationNo;
	}

	/** 
	 * @param oppoentClaimNotificationNo 要设置的 责任对方报案号
	 */ 
	public void setOppoentClaimNotificationNo(String oppoentClaimNotificationNo){ 
	    this.oppoentClaimNotificationNo=oppoentClaimNotificationNo;
	}

}
