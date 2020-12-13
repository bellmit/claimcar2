/******************************************************************************
* CREATETIME : 2016年4月13日 下午5:55:21
******************************************************************************/
package ins.sino.claimcar.platform.vo;

import java.math.BigDecimal;
import java.util.Date;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 车险信息平台交强险V6.0.0接口 --> 查勘登记vo --> 交强-->人员查勘情况列表vo
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "PERSON_DATA")
public class CICheckReqPersonDataVo {

	@XmlElement(name = "PERSON_PAY_TYPE", required = true)
	private String personPayType;//伤亡类型；参见代码

	@XmlElement(name = "ESTIMATE_AMOUNT")
	private BigDecimal estimateAmount;//估损金额

	@XmlElement(name = "PERSON_PROPERTY", required = true)
	private String personProperty;//人员属性(本车人员/车外人员)；参见代码

	@XmlElement(name = "TRAFFIC_TYPE")
	private String trafficType;//人员交通状态；参见代码

	@XmlElement(name = "CHECKER_NAME")
	private String checkerName;//查勘人员姓名

	@XmlElement(name = "CHECKER_CODE")
	private String checkerCode;//查勘人员代码

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "CHECK_START_TIME", required = true)
	private Date checkStartTime;//人员伤亡查勘调度开始时间 格式：YYYYMMDDHHMM

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "CHECK_END_TIME", required = true)
	private Date checkEndTime;//人员伤亡查勘结束时间 格式：YYYYMMDDHHMM

	@XmlElement(name = "CHECK_ADDR")
	private String checkAddr;//查勘地点

	@XmlElement(name = "CHECK_DES")
	private String checkDes;//查勘情况说明

	@XmlElement(name = "CHECKER_CERTI_CODE")
	private String checkerCertiCode;//查勘人员身份证号码

	@XmlElement(name = "PERSON_NAME")
	private String personName;//伤亡人员姓名

	@XmlElement(name = "CERTI_TYPE")
	private String certiType;//伤亡人员证件类型；参见代码

	@XmlElement(name = "CERTI_CODE")
	private String certiCode;//伤亡人员证件号码


	/** 
	 * @return 返回 personPayType  伤亡类型；参见代码
	 */ 
	public String getPersonPayType(){ 
	    return personPayType;
	}

	/** 
	 * @param personPayType 要设置的 伤亡类型；参见代码
	 */ 
	public void setPersonPayType(String personPayType){ 
	    this.personPayType=personPayType;
	}

	/** 
	 * @return 返回 estimateAmount  估损金额
	 */ 
	public BigDecimal getEstimateAmount(){ 
	    return estimateAmount;
	}

	/** 
	 * @param estimateAmount 要设置的 估损金额
	 */ 
	public void setEstimateAmount(BigDecimal estimateAmount){ 
	    this.estimateAmount=estimateAmount;
	}

	/** 
	 * @return 返回 personProperty  人员属性(本车人员/车外人员)；参见代码
	 */ 
	public String getPersonProperty(){ 
	    return personProperty;
	}

	/** 
	 * @param personProperty 要设置的 人员属性(本车人员/车外人员)；参见代码
	 */ 
	public void setPersonProperty(String personProperty){ 
	    this.personProperty=personProperty;
	}

	/** 
	 * @return 返回 trafficType  人员交通状态；参见代码
	 */ 
	public String getTrafficType(){ 
	    return trafficType;
	}

	/** 
	 * @param trafficType 要设置的 人员交通状态；参见代码
	 */ 
	public void setTrafficType(String trafficType){ 
	    this.trafficType=trafficType;
	}

	/** 
	 * @return 返回 checkerName  查勘人员姓名
	 */ 
	public String getCheckerName(){ 
	    return checkerName;
	}

	/** 
	 * @param checkerName 要设置的 查勘人员姓名
	 */ 
	public void setCheckerName(String checkerName){ 
	    this.checkerName=checkerName;
	}

	/** 
	 * @return 返回 checkerCode  查勘人员代码
	 */ 
	public String getCheckerCode(){ 
	    return checkerCode;
	}

	/** 
	 * @param checkerCode 要设置的 查勘人员代码
	 */ 
	public void setCheckerCode(String checkerCode){ 
	    this.checkerCode=checkerCode;
	}

	/** 
	 * @return 返回 checkStartTime  人员伤亡查勘调度开始时间 格式：YYYYMMDDHHMM
	 */ 
	public Date getCheckStartTime(){ 
	    return checkStartTime;
	}

	/** 
	 * @param checkStartTime 要设置的 人员伤亡查勘调度开始时间 格式：YYYYMMDDHHMM
	 */ 
	public void setCheckStartTime(Date checkStartTime){ 
	    this.checkStartTime=checkStartTime;
	}

	/** 
	 * @return 返回 checkEndTime  人员伤亡查勘结束时间 格式：YYYYMMDDHHMM
	 */ 
	public Date getCheckEndTime(){ 
	    return checkEndTime;
	}

	/** 
	 * @param checkEndTime 要设置的 人员伤亡查勘结束时间 格式：YYYYMMDDHHMM
	 */ 
	public void setCheckEndTime(Date checkEndTime){ 
	    this.checkEndTime=checkEndTime;
	}

	/** 
	 * @return 返回 checkAddr  查勘地点
	 */ 
	public String getCheckAddr(){ 
	    return checkAddr;
	}

	/** 
	 * @param checkAddr 要设置的 查勘地点
	 */ 
	public void setCheckAddr(String checkAddr){ 
	    this.checkAddr=checkAddr;
	}

	/** 
	 * @return 返回 checkDes  查勘情况说明
	 */ 
	public String getCheckDes(){ 
	    return checkDes;
	}

	/** 
	 * @param checkDes 要设置的 查勘情况说明
	 */ 
	public void setCheckDes(String checkDes){ 
	    this.checkDes=checkDes;
	}

	/** 
	 * @return 返回 checkerCertiCode  查勘人员身份证号码
	 */ 
	public String getCheckerCertiCode(){ 
	    return checkerCertiCode;
	}

	/** 
	 * @param checkerCertiCode 要设置的 查勘人员身份证号码
	 */ 
	public void setCheckerCertiCode(String checkerCertiCode){ 
	    this.checkerCertiCode=checkerCertiCode;
	}

	/** 
	 * @return 返回 personName  伤亡人员姓名
	 */ 
	public String getPersonName(){ 
	    return personName;
	}

	/** 
	 * @param personName 要设置的 伤亡人员姓名
	 */ 
	public void setPersonName(String personName){ 
	    this.personName=personName;
	}

	/** 
	 * @return 返回 certiType  伤亡人员证件类型；参见代码
	 */ 
	public String getCertiType(){ 
	    return certiType;
	}

	/** 
	 * @param certiType 要设置的 伤亡人员证件类型；参见代码
	 */ 
	public void setCertiType(String certiType){ 
	    this.certiType=certiType;
	}

	/** 
	 * @return 返回 certiCode  伤亡人员证件号码
	 */ 
	public String getCertiCode(){ 
	    return certiCode;
	}

	/** 
	 * @param certiCode 要设置的 伤亡人员证件号码
	 */ 
	public void setCertiCode(String certiCode){ 
	    this.certiCode=certiCode;
	}
}
