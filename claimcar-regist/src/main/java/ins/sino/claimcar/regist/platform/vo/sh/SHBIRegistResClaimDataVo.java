/******************************************************************************
 * CREATETIME : 2016年5月25日 上午10:26:22
 ******************************************************************************/
package ins.sino.claimcar.regist.platform.vo.sh;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 出险车辆历史理赔案件列表（多条）：
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIRegistResClaimDataVo {

	@XmlElement(name = "CLAIM_COMPANY", required = true)
	private String claimCompany;// 理赔受理公司

	@XmlElement(name = "CAR_MARK", required = true)
	private String carMark;// 涉案车辆号牌种类

	@XmlElement(name = "VEHICLE_TYPE", required = true)
	private String vehicleType;// 涉案车辆号牌号码

	@XmlElement(name = "IS_INSURED_CAR", required = true)
	private String isInsuredCar;// 是否是承保车辆

	@XmlElement(name = "COVERAGE_TYPE")
	private String coverageType;// 涉案险种类型

	@XmlElement(name = "REPORT_NO", required = true)
	private String reportNo;// 报案号，

	@XmlElement(name = "POLICY_NO", required = true)
	private String policyNo;// 保单号

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "ACCIDENT_TIME", required = true)
	private Date accidentTime;// 出险时间

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "REPORT_TIME", required = true)
	private Date reportTime;// 报案时间

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "ENDCASE_DATE")
	private Date endcaseDate;// 结案时间

	@XmlElement(name = "UNCLAIM_AMOUNT")
	private Double unclaimAmount;// 估损金额未决估计赔款

	@XmlElement(name = "CLAIM_AMOUNT")
	private Double claimAmount;// 赔款金额

	@XmlElement(name = "STATUS", required = true)
	private String status;// 案件状态

	@XmlElement(name = "ACCIDENT_PLACE", required = true)
	private String accidentPlace;// 出险地点

	@XmlElement(name = "ACCIDENT_DESCRIPTION", required = true)
	private String accidentDescription;// 出险经过

	
	public String getClaimCompany() {
		return claimCompany;
	}

	public void setClaimCompany(String claimCompany) {
		this.claimCompany = claimCompany;
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

	public String getIsInsuredCar() {
		return isInsuredCar;
	}

	public void setIsInsuredCar(String isInsuredCar) {
		this.isInsuredCar = isInsuredCar;
	}

	public String getCoverageType() {
		return coverageType;
	}

	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Date getAccidentTime() {
		return accidentTime;
	}

	public void setAccidentTime(Date accidentTime) {
		this.accidentTime = accidentTime;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public Date getEndcaseDate() {
		return endcaseDate;
	}

	public void setEndcaseDate(Date endcaseDate) {
		this.endcaseDate = endcaseDate;
	}

	public Double getUnclaimAmount() {
		return unclaimAmount;
	}

	public void setUnclaimAmount(Double unclaimAmount) {
		this.unclaimAmount = unclaimAmount;
	}

	public Double getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccidentPlace() {
		return accidentPlace;
	}

	public void setAccidentPlace(String accidentPlace) {
		this.accidentPlace = accidentPlace;
	}

	public String getAccidentDescription() {
		return accidentDescription;
	}

	public void setAccidentDescription(String accidentDescription) {
		this.accidentDescription = accidentDescription;
	}

}
