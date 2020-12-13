/******************************************************************************
 * CREATETIME : 2016年5月30日 上午11:57:56
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.Date;
import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 基本信息
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIClaimReqBasePartVo {

	@XmlElement(name = "CONFIRM_SEQUENCE_NO")
	private String confirmSequenceNo;// 投保确认码,

	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// 理赔编码

	@XmlElement(name = "ESTIMATE_AMOUNT", required = true)
	private Double estimateAmount;// 估损金额未决估计赔款，当前业务时间点的案件未决赔款金额

	@XmlElement(name = "REPORT_NO")
	private String reportNo;// 报案号，提供给客户的，且能够识别同一起事故保险公司的编号（交强和商业应为同一编号）

	@XmlElement(name = "CLAIM_NO", required = true)
	private String claimNo;// 赔案号，

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "REGISTRATION_DATE", required = true)
	private Date registrationDate;// 立案时间

	@XmlElement(name = "CLAIM_TYPE", required = true)
	private String claimType;// 理赔类型

	@XmlElement(name = "CAR_MARK")
	private String carMark;// 出险车辆号牌号码，事故中本次承保车辆

	@XmlElement(name = "VEHICLE_TYPE")
	private String vehicleType;// 出险车辆号牌种类

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "ACCIDENT_TIME", required = true)
	private Date accidentTime;// 出险时间，

	@XmlElement(name = "MANAGE_TYPE")
	private String manageType;// 事故处理类型

	@XmlElement(name = "POLICY_PLACE", required = true)
	private String policyPlace;// 保单归属地

	@XmlElement(name = "SUBROGATION_FLAG")
	private String subrogationFlag;// 是否代位求偿标志

	@XmlElement(name = "PERSON_NUM")
	private Integer personNum;// 事故伤亡人数
	
	/* 牛强  2017-3-14 改 */
	@XmlElement(name = "AccidentReason")
	private String accidentReason;  //事故原因
	
	@XmlElement(name = "AccidentDescription")
	private String accidentDescription; //出险经过

	/**
	 * @return 返回 confirmSequenceNo 投保确认码,
	 */
	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	/**
	 * @param confirmSequenceNo 要设置的 投保确认码,
	 */
	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}

	/**
	 * @return 返回 claimCode 理赔编码
	 */
	public String getClaimCode() {
		return claimCode;
	}

	/**
	 * @param claimCode 要设置的 理赔编码
	 */
	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	/**
	 * @return 返回 estimateAmount 估损金额未决估计赔款，当前业务时间点的案件未决赔款金额
	 */
	public Double getEstimateAmount() {
		return estimateAmount;
	}

	/**
	 * @param estimateAmount 要设置的 估损金额未决估计赔款，当前业务时间点的案件未决赔款金额
	 */
	public void setEstimateAmount(Double estimateAmount) {
		this.estimateAmount = estimateAmount;
	}

	/**
	 * @return 返回 reportNo 报案号，提供给客户的，且能够识别同一起事故保险公司的编号（交强和商业应为同一编号）
	 */
	public String getReportNo() {
		return reportNo;
	}

	/**
	 * @param reportNo 要设置的 报案号，提供给客户的，且能够识别同一起事故保险公司的编号（交强和商业应为同一编号）
	 */
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	/**
	 * @return 返回 claimNo 赔案号，
	 */
	public String getClaimNo() {
		return claimNo;
	}

	/**
	 * @param claimNo 要设置的 赔案号，
	 */
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	/**
	 * @return 返回 registrationDate 立案时间
	 */
	public Date getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * @param registrationDate 要设置的 立案时间
	 */
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * @return 返回 claimType 理赔类型
	 */
	public String getClaimType() {
		return claimType;
	}

	/**
	 * @param claimType 要设置的 理赔类型
	 */
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	/**
	 * @return 返回 carMark 出险车辆号牌号码，事故中本次承保车辆
	 */
	public String getCarMark() {
		return carMark;
	}

	/**
	 * @param carMark 要设置的 出险车辆号牌号码，事故中本次承保车辆
	 */
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	/**
	 * @return 返回 vehicleType 出险车辆号牌种类
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType 要设置的 出险车辆号牌种类
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * @return 返回 accidentTime 出险时间，
	 */
	public Date getAccidentTime() {
		return accidentTime;
	}

	/**
	 * @param accidentTime 要设置的 出险时间，
	 */
	public void setAccidentTime(Date accidentTime) {
		this.accidentTime = accidentTime;
	}

	/**
	 * @return 返回 manageType 事故处理类型
	 */
	public String getManageType() {
		return manageType;
	}

	/**
	 * @param manageType 要设置的 事故处理类型
	 */
	public void setManageType(String manageType) {
		this.manageType = manageType;
	}

	/**
	 * @return 返回 policyPlace 保单归属地
	 */
	public String getPolicyPlace() {
		return policyPlace;
	}

	/**
	 * @param policyPlace 要设置的 保单归属地
	 */
	public void setPolicyPlace(String policyPlace) {
		this.policyPlace = policyPlace;
	}

	/**
	 * @return 返回 subrogationFlag 是否代位求偿标志
	 */
	public String getSubrogationFlag() {
		return subrogationFlag;
	}

	/**
	 * @param subrogationFlag 要设置的 是否代位求偿标志
	 */
	public void setSubrogationFlag(String subrogationFlag) {
		this.subrogationFlag = subrogationFlag;
	}

	/**
	 * @return 返回 personNum 事故伤亡人数
	 */
	public Integer getPersonNum() {
		return personNum;
	}

	/**
	 * @param personNum 要设置的 事故伤亡人数
	 */
	public void setPersonNum(Integer personNum) {
		this.personNum = personNum;
	}

	public String getAccidentReason() {
		return accidentReason;
	}

	public void setAccidentReason(String accidentReason) {
		this.accidentReason = accidentReason;
	}

	public String getAccidentDescription() {
		return accidentDescription;
	}

	public void setAccidentDescription(String accidentDescription) {
		this.accidentDescription = accidentDescription;
	}
}
