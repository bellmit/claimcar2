/******************************************************************************
* CREATETIME : 2016年4月29日 下午3:09:26
******************************************************************************/
package ins.sino.claimcar.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 车险信息平台交强险V6.0.0接口 -->查勘登记vo -->商业--> 财产损失情况列表
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BICheckReqProtectDataVo {

	@XmlElement(name = "ProtectName", required = true)
	private String protectName;//损失财产名称

	@XmlElement(name = "LossDesc")
	private String lossDesc;//损失描述

	@XmlElement(name = "LossNum")
	private String lossNum;//损失数量

	@XmlElement(name = "EstimatedLossAmount")
	private BigDecimal estimatedLossAmount;//估损金额

	@XmlElement(name = "Owner")
	private String owner;//所有人/管理人

	@XmlElement(name = "ProtectProperty")
	private String protectProperty;//财产属性；参见代码

	@XmlElement(name = "FieldType")
	private String fieldType;//现场类别；参见代码

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "CheckStartTime", required = true)
	private Date checkStartTime;//财产损失查勘调度开始时间；精确到分钟

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "CheckEndTime", required = true)
	private Date checkEndTime;//财产损失查勘结束时间；精确到分钟

	@XmlElement(name = "CheckerName", required = true)
	private String checkerName;//查勘人员姓名

	@XmlElement(name = "CheckerCode")
	private String checkerCode;//查勘人员代码

	@XmlElement(name = "CheckerCertiCode", required = true)
	private String checkerCertiCode;//查勘人员身份证号码

	@XmlElement(name = "CheckAddr")
	private String checkAddr;//查勘地点

	@XmlElement(name = "CheckDesc")
	private String checkDesc;//查勘情况说明


	/** 
	 * @return 返回 protectName  损失财产名称
	 */ 
	public String getProtectName(){ 
	    return protectName;
	}

	/** 
	 * @param protectName 要设置的 损失财产名称
	 */ 
	public void setProtectName(String protectName){ 
	    this.protectName=protectName;
	}

	/** 
	 * @return 返回 lossDesc  损失描述
	 */ 
	public String getLossDesc(){ 
	    return lossDesc;
	}

	/** 
	 * @param lossDesc 要设置的 损失描述
	 */ 
	public void setLossDesc(String lossDesc){ 
	    this.lossDesc=lossDesc;
	}

	/** 
	 * @return 返回 lossNum  损失数量
	 */ 
	public String getLossNum(){ 
	    return lossNum;
	}

	/** 
	 * @param lossNum 要设置的 损失数量
	 */ 
	public void setLossNum(String lossNum){ 
	    this.lossNum=lossNum;
	}

	/** 
	 * @return 返回 estimatedLossAmount  估损金额
	 */ 
	public BigDecimal getEstimatedLossAmount(){ 
	    return estimatedLossAmount;
	}

	/** 
	 * @param estimatedLossAmount 要设置的 估损金额
	 */ 
	public void setEstimatedLossAmount(BigDecimal estimatedLossAmount){ 
	    this.estimatedLossAmount=estimatedLossAmount;
	}

	/** 
	 * @return 返回 owner  所有人/管理人
	 */ 
	public String getOwner(){ 
	    return owner;
	}

	/** 
	 * @param owner 要设置的 所有人/管理人
	 */ 
	public void setOwner(String owner){ 
	    this.owner=owner;
	}

	/** 
	 * @return 返回 protectProperty  财产属性；参见代码
	 */ 
	public String getProtectProperty(){ 
	    return protectProperty;
	}

	/** 
	 * @param protectProperty 要设置的 财产属性；参见代码
	 */ 
	public void setProtectProperty(String protectProperty){ 
	    this.protectProperty=protectProperty;
	}

	/** 
	 * @return 返回 fieldType  现场类别；参见代码
	 */ 
	public String getFieldType(){ 
	    return fieldType;
	}

	/** 
	 * @param fieldType 要设置的 现场类别；参见代码
	 */ 
	public void setFieldType(String fieldType){ 
	    this.fieldType=fieldType;
	}

	/** 
	 * @return 返回 checkStartTime  财产损失查勘调度开始时间；精确到分钟
	 */ 
	public Date getCheckStartTime(){ 
	    return checkStartTime;
	}

	/** 
	 * @param checkStartTime 要设置的 财产损失查勘调度开始时间；精确到分钟
	 */ 
	public void setCheckStartTime(Date checkStartTime){ 
	    this.checkStartTime=checkStartTime;
	}

	/** 
	 * @return 返回 checkEndTime  财产损失查勘结束时间；精确到分钟
	 */ 
	public Date getCheckEndTime(){ 
	    return checkEndTime;
	}

	/** 
	 * @param checkEndTime 要设置的 财产损失查勘结束时间；精确到分钟
	 */ 
	public void setCheckEndTime(Date checkEndTime){ 
	    this.checkEndTime=checkEndTime;
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
	 * @return 返回 checkDesc  查勘情况说明
	 */ 
	public String getCheckDesc(){ 
	    return checkDesc;
	}

	/** 
	 * @param checkDesc 要设置的 查勘情况说明
	 */ 
	public void setCheckDesc(String checkDesc){ 
	    this.checkDesc=checkDesc;
	}
}
