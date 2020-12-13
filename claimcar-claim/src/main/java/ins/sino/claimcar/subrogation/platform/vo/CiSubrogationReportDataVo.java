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
public class CiSubrogationReportDataVo {
	/** 保单号 **/ 
	@XmlElement(name="POLICY_NO", required = true)
	private String policyNo; 

	/** 报案号 **/ 
	@XmlElement(name="REPORT_NO", required = true)
	private String reportNo; 

	/** 出险时间 **/ 
	@XmlElement(name="ACCIDENT_TIME", required = true)
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date accidentTime; 

	/** 出险承保车辆号牌号码;未上牌车辆可为空 **/ 
	@XmlElement(name="CAR_MARK")
	private String carMark; 

	/** 出险承保车辆号牌种类;未上牌车辆可以为空 **/ 
	@XmlElement(name="VEHICLE_TYPE")
	private String vehicleType; 

	/** 出险地点 **/ 
	@XmlElement(name="ACCIDENT_PLACE", required = true)
	private String accidentPlace; 

	/** 报案时间；格式YYYYMMDDHHMM **/ 
	@XmlElement(name="REPORT_TIME", required = true)
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	private Date reportTime; 

	/** 出险经过 **/ 
	@XmlElement(name="ACCIDENT_DESCRIPTION", required = true)
	private String accidentDescription; 

	/** 出险驾驶员姓名 **/ 
	@XmlElement(name="DRIVER_NAME")
	private String driverName; 

	/** 报案人姓名 **/ 
	@XmlElement(name="REPORTER_NAME", required = true)
	private String reporterName; 

	/** 事故处理方式代码；参见代码 **/ 
	@XmlElement(name="MANAGE_TYPE")
	private String manageType; 

	/** 事故责任划分代码；参见代码 **/ 
	@XmlElement(name="ACCIDENT_LIABILITY")
	private String accidentLiability;
	
	/**第三方车辆情况列表(隶属于报案信息)*/
	@XmlElementWrapper(name = "THIRD_VEHICLE_LIST")
	@XmlElement(name = "THIRD_VEHICLE_DATA")
	private List<CiReportThirdVehicleDataVo> thirdVehicleDataList;
	
	/**损失情况列表(隶属于报案信息)*/
	@XmlElementWrapper(name = "LOSS_LIST")
	@XmlElement(name = "LOSS_DATA")
	private List<CiReportLossDataVo> lossDataList;

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public Date getAccidentTime() {
		return accidentTime;
	}

	public void setAccidentTime(Date accidentTime) {
		this.accidentTime = accidentTime;
	}

	public String getCarMark() {
		return carMark;
	}

	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getAccidentPlace() {
		return accidentPlace;
	}

	public void setAccidentPlace(String accidentPlace) {
		this.accidentPlace = accidentPlace;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public String getAccidentDescription() {
		return accidentDescription;
	}

	public void setAccidentDescription(String accidentDescription) {
		this.accidentDescription = accidentDescription;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getReporterName() {
		return reporterName;
	}

	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}

	public String getManageType() {
		return manageType;
	}

	public void setManageType(String manageType) {
		this.manageType = manageType;
	}

	public String getAccidentLiability() {
		return accidentLiability;
	}

	public void setAccidentLiability(String accidentLiability) {
		this.accidentLiability = accidentLiability;
	}

	
	public List<CiReportThirdVehicleDataVo> getThirdVehicleDataList() {
		return thirdVehicleDataList;
	}

	
	public void setThirdVehicleDataList(List<CiReportThirdVehicleDataVo> thirdVehicleDataList) {
		this.thirdVehicleDataList = thirdVehicleDataList;
	}

	
	public List<CiReportLossDataVo> getLossDataList() {
		return lossDataList;
	}

	
	public void setLossDataList(List<CiReportLossDataVo> lossDataList) {
		this.lossDataList = lossDataList;
	} 

	

}
