/******************************************************************************
 * CREATETIME : 2016年5月30日 下午1:07:12
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapterSH;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 基本信息
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIClaimReqBasePartVo {

	@XmlElement(name = "CONFIRM_SEQUENCE_NO")
	private String confirmSequenceNo;// 投保确认码

	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// 理赔编码

	@XmlElement(name = "REPORT_NO", required = true)
	private String reportNo;// 报案号,提供给客户的，且能够识别同一起事故保险公司的编号（交强和商业应为同一编号）

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "ACCIDENT_TIME", required = true)
	private Date accidentTime;// 出险时间，YYYYMMDDHHMMSS

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "REGISTRATION_DATE", required = true)
	private Date registrationDate;// 立案时间，YYYYMMDDHHMMSS

	@XmlElement(name = "ACCIDENT_DESCRIPTION", required = true)
	private String accidentDescription;// 出险经过

	@XmlElement(name = "CAR_MARK", required = true)
	private String carMark;// 出险承保车辆号牌号码，事故中本次承保车辆

	@XmlElement(name = "VEHICLE_TYPE", required = true)
	private String vehicleType;// 出险承保车辆号牌种类

	@XmlElement(name = "POLICY_NO", required = true)
	private String policyNo;// 商业险保单号码

	@XmlElement(name = "ESTIMATE", required = true)
	private String estimate;// 估损金额未决估计赔款，当前业务时间点的案件未决赔款金额

	@XmlElement(name = "REPORTER", required = true)
	private String reporter;// 报案人姓名

	@XmlElement(name = "OPTION_TYPE")
	private String optionType;// 事故处理类型

	@XmlElement(name = "ACCIDENT_REASON")
	private String accidentReason;// 事故原因

	@XmlElement(name = "POLICY_PLACE", required = true)
	private String policyPlace;// 保单归属地

	@XmlElement(name = "SUBROGATION_FLAG")
	private String subrogationFlag;// 是否代位求偿标志

	@XmlElement(name = "PERSON_NUM")
	private Integer personNum;// 事故伤亡人数

	/**
	 * @return 返回 confirmSequenceNo 投保确认码
	 */
	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	/**
	 * @param confirmSequenceNo 要设置的 投保确认码
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
	 * @return 返回 reportNo 报案号,提供给客户的，且能够识别同一起事故保险公司的编号（交强和商业应为同一编号）
	 */
	public String getReportNo() {
		return reportNo;
	}

	/**
	 * @param reportNo 要设置的 报案号,提供给客户的，且能够识别同一起事故保险公司的编号（交强和商业应为同一编号）
	 */
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	/**
	 * @return 返回 accidentTime 出险时间，YYYYMMDDHHMMSS
	 */
	public Date getAccidentTime() {
		return accidentTime;
	}

	/**
	 * @param accidentTime 要设置的 出险时间，YYYYMMDDHHMMSS
	 */
	public void setAccidentTime(Date accidentTime) {
		this.accidentTime = accidentTime;
	}

	/**
	 * @return 返回 registrationDate 立案时间，YYYYMMDDHHMMSS
	 */
	public Date getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * @param registrationDate 要设置的 立案时间，YYYYMMDDHHMMSS
	 */
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * @return 返回 accidentDescription 出险经过
	 */
	public String getAccidentDescription() {
		return accidentDescription;
	}

	/**
	 * @param accidentDescription 要设置的 出险经过
	 */
	public void setAccidentDescription(String accidentDescription) {
		this.accidentDescription = accidentDescription;
	}

	/**
	 * @return 返回 carMark 出险承保车辆号牌号码，事故中本次承保车辆
	 */
	public String getCarMark() {
		return carMark;
	}

	/**
	 * @param carMark 要设置的 出险承保车辆号牌号码，事故中本次承保车辆
	 */
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	/**
	 * @return 返回 vehicleType 出险承保车辆号牌种类
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType 要设置的 出险承保车辆号牌种类
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * @return 返回 policyNo 商业险保单号码
	 */
	public String getPolicyNo() {
		return policyNo;
	}

	/**
	 * @param policyNo 要设置的 商业险保单号码
	 */
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	/**
	 * @return 返回 estimate 估损金额未决估计赔款，当前业务时间点的案件未决赔款金额
	 */
	public String getEstimate() {
		return estimate;
	}

	/**
	 * @param estimate 要设置的 估损金额未决估计赔款，当前业务时间点的案件未决赔款金额
	 */
	public void setEstimate(String estimate) {
		this.estimate = estimate;
	}

	/**
	 * @return 返回 reporter 报案人姓名
	 */
	public String getReporter() {
		return reporter;
	}

	/**
	 * @param reporter 要设置的 报案人姓名
	 */
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	/**
	 * @return 返回 optionType 事故处理类型
	 */
	public String getOptionType() {
		return optionType;
	}

	/**
	 * @param optionType 要设置的 事故处理类型
	 */
	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	/**
	 * @return 返回 accidentReason 事故原因
	 */
	public String getAccidentReason() {
		return accidentReason;
	}

	/**
	 * @param accidentReason 要设置的 事故原因
	 */
	public void setAccidentReason(String accidentReason) {
		this.accidentReason = accidentReason;
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
}
