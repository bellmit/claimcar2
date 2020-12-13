/******************************************************************************
 * CREATETIME : 2016年5月24日 下午5:19:13
 ******************************************************************************/
package ins.sino.claimcar.regist.platform.vo.sh;

import java.util.Date;
import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 出险车辆历史理赔案件列表（多条）：
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIRegistResClaimDataVo {

	@XmlElement(name = "CLAIM_COMPANY", required = true)
	private String claimCompany;//理赔受理公司

	@XmlElement(name = "CAR_MARK")
	private String carMark;//涉案车辆号牌种类

	@XmlElement(name = "VEHICLE_TYPE")
	private String vehicleType;//涉案车辆号牌号码

	@XmlElement(name = "IS_INSURED_CAR", required = true)
	private String isInsuredCar;//是否是承保车辆

	@XmlElement(name = "COVERAGE_TYPE")
	private String coverageType;//涉案险种类型

	@XmlElement(name = "REPORT_NO", required = true)
	private String reportNo;// 报案号，

	@XmlElement(name = "CLAIM_NO")
	private String claimNo;// 赔案号

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

	@XmlElement(name = "LIABILITY_AMOUNT")
	private String liabilityAmount;// 事故责任划分

	/**
	 * @return 返回 claimCompany Y
	 */
	public String getClaimCompany() {
		return claimCompany;
	}

	/**
	 * @param claimCompany 要设置的 Y
	 */
	public void setClaimCompany(String claimCompany) {
		this.claimCompany = claimCompany;
	}

	/**
	 * @return 返回 carMark
	 */
	public String getCarMark() {
		return carMark;
	}

	/**
	 * @param carMark 要设置的
	 */
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	/**
	 * @return 返回 vehicleType
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType 要设置的
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * @return 返回 isInsuredCar Y
	 */
	public String getIsInsuredCar() {
		return isInsuredCar;
	}

	/**
	 * @param isInsuredCar 要设置的 Y
	 */
	public void setIsInsuredCar(String isInsuredCar) {
		this.isInsuredCar = isInsuredCar;
	}

	/**
	 * @return 返回 coverageType 涉案险种类型
	 */
	public String getCoverageType() {
		return coverageType;
	}

	/**
	 * @param coverageType 要设置的 涉案险种类型
	 */
	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}

	/**
	 * @return 返回 reportNo Y
	 */
	public String getReportNo() {
		return reportNo;
	}

	/**
	 * @param reportNo 要设置的 Y
	 */
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	/**
	 * @return 返回 claimNo 赔案号
	 */
	public String getClaimNo() {
		return claimNo;
	}

	/**
	 * @param claimNo 要设置的 赔案号
	 */
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	/**
	 * @return 返回 policyNo Y
	 */
	public String getPolicyNo() {
		return policyNo;
	}

	/**
	 * @param policyNo 要设置的 Y
	 */
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	/**
	 * @return 返回 accidentTime Y
	 */
	public Date getAccidentTime() {
		return accidentTime;
	}

	/**
	 * @param accidentTime 要设置的 Y
	 */
	public void setAccidentTime(Date accidentTime) {
		this.accidentTime = accidentTime;
	}

	/**
	 * @return 返回 reportTime Y
	 */
	public Date getReportTime() {
		return reportTime;
	}

	/**
	 * @param reportTime 要设置的 Y
	 */
	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	/**
	 * @return 返回 endcaseDate 结案时间
	 */
	public Date getEndcaseDate() {
		return endcaseDate;
	}

	/**
	 * @param endcaseDate 要设置的 结案时间
	 */
	public void setEndcaseDate(Date endcaseDate) {
		this.endcaseDate = endcaseDate;
	}

	/**
	 * @return 返回 unclaimAmount 估损金额未决估计赔款
	 */
	public Double getUnclaimAmount() {
		return unclaimAmount;
	}

	/**
	 * @param unclaimAmount 要设置的 估损金额未决估计赔款
	 */
	public void setUnclaimAmount(Double unclaimAmount) {
		this.unclaimAmount = unclaimAmount;
	}

	/**
	 * @return 返回 claimAmount 赔款金额
	 */
	public Double getClaimAmount() {
		return claimAmount;
	}

	/**
	 * @param claimAmount 要设置的 赔款金额
	 */
	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	/**
	 * @return 返回 status Y
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status 要设置的 Y
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return 返回 accidentPlace Y
	 */
	public String getAccidentPlace() {
		return accidentPlace;
	}

	/**
	 * @param accidentPlace 要设置的 Y
	 */
	public void setAccidentPlace(String accidentPlace) {
		this.accidentPlace = accidentPlace;
	}

	/**
	 * @return 返回 accidentDescription Y
	 */
	public String getAccidentDescription() {
		return accidentDescription;
	}

	/**
	 * @param accidentDescription 要设置的 Y
	 */
	public void setAccidentDescription(String accidentDescription) {
		this.accidentDescription = accidentDescription;
	}

	/**
	 * @return 返回 liabilityAmount 事故责任划分
	 */
	public String getLiabilityAmount() {
		return liabilityAmount;
	}

	/**
	 * @param liabilityAmount 要设置的 事故责任划分
	 */
	public void setLiabilityAmount(String liabilityAmount) {
		this.liabilityAmount = liabilityAmount;
	}

}
