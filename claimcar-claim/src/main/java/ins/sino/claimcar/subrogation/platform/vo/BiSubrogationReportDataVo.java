/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

//报案信息
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiSubrogationReportDataVo {
	@XmlElement(name = "PolicyNo", required = true)
	private String policyNo;//保单号

	@XmlElement(name = "ClaimNotificationNo", required = true)
	private String claimNotificationNo;//报案号

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "NotificationTime", required = true)
	private Date notificationTime;//报案时间；精确到分

	@XmlElement(name = "Reporter", required = true)
	private String reporter;//报案人姓名

	@XmlElement(name = "DriverName")
	private String driverName;//出险驾驶员姓名

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "LossTime", required = true)
	private Date lossTime;//出险时间；精确到分

	@XmlElement(name = "LossArea", required = true)
	private String lossArea;//出险地点

	@XmlElement(name = "LossDesc", required = true)
	private String lossDesc;//出险经过

	@XmlElement(name = "LicensePlateNo")
	private String licensePlateNo;//出险标的车号牌号码

	@XmlElement(name = "LicensePlateType")
	private String licensePlateType;//出险标的车号牌种类代码；参见代码

	@XmlElement(name = "AccidentLiability")
	private String accidentLiability;//事故责任划分 参见代码

	@XmlElement(name = "OptionType")
	private String optionType;//事故处理方式；参见代码

	/**第三方车辆情况列表(隶属于报案信息)*/
	@XmlElement(name = "ThirdVehicleData")
	private List<BiReportThirdVehicleDataVo> thirdVehicleDataList;
	
	/**损失情况列表(隶属于报案信息)*/
	@XmlElement(name = "LossData")
	private List<BiReportLossDataVo> lossDataList;
	

	/** 
	 * @return 返回 policyNo  保单号
	 */ 
	public String getPolicyNo(){ 
	    return policyNo;
	}

	/** 
	 * @param policyNo 要设置的 保单号
	 */ 
	public void setPolicyNo(String policyNo){ 
	    this.policyNo=policyNo;
	}

	/** 
	 * @return 返回 claimNotificationNo  报案号
	 */ 
	public String getClaimNotificationNo(){ 
	    return claimNotificationNo;
	}

	/** 
	 * @param claimNotificationNo 要设置的 报案号
	 */ 
	public void setClaimNotificationNo(String claimNotificationNo){ 
	    this.claimNotificationNo=claimNotificationNo;
	}

	/** 
	 * @return 返回 notificationTime  报案时间；精确到分
	 */ 
	public Date getNotificationTime(){ 
	    return notificationTime;
	}

	/** 
	 * @param notificationTime 要设置的 报案时间；精确到分
	 */ 
	public void setNotificationTime(Date notificationTime){ 
	    this.notificationTime=notificationTime;
	}

	/** 
	 * @return 返回 reporter  报案人姓名
	 */ 
	public String getReporter(){ 
	    return reporter;
	}

	/** 
	 * @param reporter 要设置的 报案人姓名
	 */ 
	public void setReporter(String reporter){ 
	    this.reporter=reporter;
	}

	/** 
	 * @return 返回 driverName  出险驾驶员姓名
	 */ 
	public String getDriverName(){ 
	    return driverName;
	}

	/** 
	 * @param driverName 要设置的 出险驾驶员姓名
	 */ 
	public void setDriverName(String driverName){ 
	    this.driverName=driverName;
	}

	/** 
	 * @return 返回 lossTime  出险时间；精确到分
	 */ 
	public Date getLossTime(){ 
	    return lossTime;
	}

	/** 
	 * @param lossTime 要设置的 出险时间；精确到分
	 */ 
	public void setLossTime(Date lossTime){ 
	    this.lossTime=lossTime;
	}

	/** 
	 * @return 返回 lossArea  出险地点
	 */ 
	public String getLossArea(){ 
	    return lossArea;
	}

	/** 
	 * @param lossArea 要设置的 出险地点
	 */ 
	public void setLossArea(String lossArea){ 
	    this.lossArea=lossArea;
	}

	/** 
	 * @return 返回 lossDesc  出险经过
	 */ 
	public String getLossDesc(){ 
	    return lossDesc;
	}

	/** 
	 * @param lossDesc 要设置的 出险经过
	 */ 
	public void setLossDesc(String lossDesc){ 
	    this.lossDesc=lossDesc;
	}

	/** 
	 * @return 返回 licensePlateNo  出险标的车号牌号码
	 */ 
	public String getLicensePlateNo(){ 
	    return licensePlateNo;
	}

	/** 
	 * @param licensePlateNo 要设置的 出险标的车号牌号码
	 */ 
	public void setLicensePlateNo(String licensePlateNo){ 
	    this.licensePlateNo=licensePlateNo;
	}

	/** 
	 * @return 返回 licensePlateType  出险标的车号牌种类代码；参见代码
	 */ 
	public String getLicensePlateType(){ 
	    return licensePlateType;
	}

	/** 
	 * @param licensePlateType 要设置的 出险标的车号牌种类代码；参见代码
	 */ 
	public void setLicensePlateType(String licensePlateType){ 
	    this.licensePlateType=licensePlateType;
	}

	/** 
	 * @return 返回 accidentLiability  事故责任划分 参见代码
	 */ 
	public String getAccidentLiability(){ 
	    return accidentLiability;
	}

	/** 
	 * @param accidentLiability 要设置的 事故责任划分 参见代码
	 */ 
	public void setAccidentLiability(String accidentLiability){ 
	    this.accidentLiability=accidentLiability;
	}

	/** 
	 * @return 返回 optionType  事故处理方式；参见代码
	 */ 
	public String getOptionType(){ 
	    return optionType;
	}

	/** 
	 * @param optionType 要设置的 事故处理方式；参见代码
	 */ 
	public void setOptionType(String optionType){ 
	    this.optionType=optionType;
	}

	public List<BiReportThirdVehicleDataVo> getThirdVehicleDataList() {
		return thirdVehicleDataList;
	}

	public void setThirdVehicleDataList(
			List<BiReportThirdVehicleDataVo> thirdVehicleDataList) {
		this.thirdVehicleDataList = thirdVehicleDataList;
	}

	public List<BiReportLossDataVo> getLossDataList() {
		return lossDataList;
	}

	public void setLossDataList(List<BiReportLossDataVo> lossDataList) {
		this.lossDataList = lossDataList;
	}



	



}
