/******************************************************************************
 * CREATETIME : 2016年5月26日 下午12:04:04
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 基本信息
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCILossReqBasePartVo {

	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// 理赔编码
	
	@XmlElement(name = "REPORT_NO")
	private String reportNo;// 报案号

	@XmlElement(name = "MANAGE_TYPE")
	private String manageType;// 事故类型，

	@XmlElement(name = "LIABILITY_AMOUNT")
	private String liabilityAmount;// 事故责任，

	@XmlElement(name = "ESTIMATE_AMOUNT")
	private String estimateAmount;// 总定损金额

	@XmlElement(name = "CONFIRM_SEQUENCE_NO")
	private String confirmSequenceNo;// 投保确认码

	@XmlElement(name = "CAR_MARK")
	private String carMark;// 出险承保车辆号牌号码

	@XmlElement(name = "VEHICLE_TYPE")
	private String vehicleType;// 出险承保车辆号牌种类，

	@XmlElement(name = "SUBROGATION_FLAG")
	private String subrogationFlag;// 是否代位求偿标志，

	@XmlElement(name = "Remark")
	private String remark;// 案件备注，保险公司对案件的描述信息
	
	@XmlElement(name = "IsSingleAccident")
	private String isSingleAccident; //是否单车事故
	
	@XmlElement(name = "IsPersonInjured")
	private String isPersonInjured; //是否包含人伤
	
	@XmlElement(name = "IsProtectLoss")
	private String isProtectLoss; //是否包含财损
	
	@XmlElement(name = "UnderDefLoss")
	private String underDefLoss; //核损总金额

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

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	
	/**
	 * @return 返回 manageType 事故类型，
	 */
	public String getManageType() {
		return manageType;
	}

	/**
	 * @param manageType 要设置的 事故类型，
	 */
	public void setManageType(String manageType) {
		this.manageType = manageType;
	}

	/**
	 * @return 返回 liabilityAmount 事故责任，
	 */
	public String getLiabilityAmount() {
		return liabilityAmount;
	}

	/**
	 * @param liabilityAmount 要设置的 事故责任，
	 */
	public void setLiabilityAmount(String liabilityAmount) {
		this.liabilityAmount = liabilityAmount;
	}

	/**
	 * @return 返回 estimateAmount 总定损金额
	 */
	public String getEstimateAmount() {
		return estimateAmount;
	}

	/**
	 * @param estimateAmount 要设置的 总定损金额
	 */
	public void setEstimateAmount(String estimateAmount) {
		this.estimateAmount = estimateAmount;
	}

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
	 * @return 返回 carMark 出险承保车辆号牌号码
	 */
	public String getCarMark() {
		return carMark;
	}

	/**
	 * @param carMark 要设置的 出险承保车辆号牌号码
	 */
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	/**
	 * @return 返回 vehicleType 出险承保车辆号牌种类，
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType 要设置的 出险承保车辆号牌种类，
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * @return 返回 subrogationFlag 是否代位求偿标志，
	 */
	public String getSubrogationFlag() {
		return subrogationFlag;
	}

	/**
	 * @param subrogationFlag 要设置的 是否代位求偿标志，
	 */
	public void setSubrogationFlag(String subrogationFlag) {
		this.subrogationFlag = subrogationFlag;
	}

	/**
	 * @return 返回 remark 案件备注，保险公司对案件的描述信息
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark 要设置的 案件备注，保险公司对案件的描述信息
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsSingleAccident() {
		return isSingleAccident;
	}

	public void setIsSingleAccident(String isSingleAccident) {
		this.isSingleAccident = isSingleAccident;
	}

	public String getIsPersonInjured() {
		return isPersonInjured;
	}

	public void setIsPersonInjured(String isPersonInjured) {
		this.isPersonInjured = isPersonInjured;
	}

	public String getIsProtectLoss() {
		return isProtectLoss;
	}

	public void setIsProtectLoss(String isProtectLoss) {
		this.isProtectLoss = isProtectLoss;
	}

	public String getUnderDefLoss() {
		return underDefLoss;
	}

	public void setUnderDefLoss(String underDefLoss) {
		this.underDefLoss = underDefLoss;
	}
}
