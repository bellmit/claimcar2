/******************************************************************************
 * CREATETIME : 2016年5月26日 下午6:54:18
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 基本信息
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBILossReqBasePartVo {

	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// 理赔编码

	@XmlElement(name = "REPORT_NO")
	private String reportNo;// 报案号，
	
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

	@XmlElement(name = "IsSingleAccident", required = true)
	private String isSingleAccident;// 是否单车事故
	
	/*牛强  2017-03-15 改*/
	@XmlElement(name="IsPersonInjured")
	private String isPersonInjured; //是否包含人伤
	
	@XmlElement(name="IsProtectLoss")
	private String isProtectLoss; //是否包含财损
	
	@XmlElement(name="UnderDefLoss")
	private String underDefLoss; //核损总金额

	public String getClaimCode() {
		return claimCode;
	}

	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	public String getManageType() {
		return manageType;
	}

	public void setManageType(String manageType) {
		this.manageType = manageType;
	}

	public String getLiabilityAmount() {
		return liabilityAmount;
	}

	public void setLiabilityAmount(String liabilityAmount) {
		this.liabilityAmount = liabilityAmount;
	}

	public String getEstimateAmount() {
		return estimateAmount;
	}

	public void setEstimateAmount(String estimateAmount) {
		this.estimateAmount = estimateAmount;
	}

	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
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

	public String getSubrogationFlag() {
		return subrogationFlag;
	}

	public void setSubrogationFlag(String subrogationFlag) {
		this.subrogationFlag = subrogationFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsSingleAccident() {
		return isSingleAccident;
	}

	public void setIsSingleAccident(String isSingleAccident) {
		this.isSingleAccident = isSingleAccident;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
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
