/******************************************************************************
 * CREATETIME : 2016年4月13日 下午5:14:34
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 车险信息平台交强险V6.0.0接口 --> 查勘登记vo --> 交强-->基本信息vo
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CICheckReqBasePartVo {

	@XmlElement(name = "CONFIRM_SEQUENCE_NO", required = true)
	private String confirmSequenceNo;// 投保确认号

	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// 理赔编号

	@XmlElement(name = "REPORT_NO", required = true)
	private String reportNo;// 报案号

	@XmlElement(name = "ACCIDENT_TYPE")
	private String accidentType;// 保险事故分类；参见代码

	@XmlElement(name = "ACCIDENT_LIABILITY", required = true)
	private String accidentLiability;// 事故责任划分代码；参见代码

	@XmlElement(name = "MANAGE_TYPE")
	private String manageType;// 事故处理方式代码；参见代码

	@XmlElement(name = "ACCIDENT_CAUSE", required = true)
	private String accidentCause;// 出险原因代码；参见代码

	@XmlElement(name = "PAY_SELF_FLAG", required = true)
	private String paySelfFlag;// 互碰自赔标志；参见代码

	@XmlElement(name = "IS_SINGLE_ACCIDENT")
	private String isSingleAccident;// 是否单车事故；参见代码

	@XmlElement(name = "IS_PERSON_INJURED")
	private String isPersonInjured;// 是否包含人伤；参见代码

	@XmlElement(name = "IS_PROTECT_LOSS")
	private String isProtectLoss;// 是否包含财损；参见代码

	/**
	 * @return 返回 confirmSequenceNo 投保确认号
	 */
	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	/**
	 * @param confirmSequenceNo 要设置的 投保确认号
	 */
	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}

	/**
	 * @return 返回 claimCode 理赔编号
	 */
	public String getClaimCode() {
		return claimCode;
	}

	/**
	 * @param claimCode 要设置的 理赔编号
	 */
	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	/**
	 * @return 返回 reportNo 报案号
	 */
	public String getReportNo() {
		return reportNo;
	}

	/**
	 * @param reportNo 要设置的 报案号
	 */
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	/**
	 * @return 返回 accidentType 保险事故分类；参见代码
	 */
	public String getAccidentType() {
		return accidentType;
	}

	/**
	 * @param accidentType 要设置的 保险事故分类；参见代码
	 */
	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}

	/**
	 * @return 返回 accidentLiability 事故责任划分代码；参见代码
	 */
	public String getAccidentLiability() {
		return accidentLiability;
	}

	/**
	 * @param accidentLiability 要设置的 事故责任划分代码；参见代码
	 */
	public void setAccidentLiability(String accidentLiability) {
		this.accidentLiability = accidentLiability;
	}

	/**
	 * @return 返回 manageType 事故处理方式代码；参见代码
	 */
	public String getManageType() {
		return manageType;
	}

	/**
	 * @param manageType 要设置的 事故处理方式代码；参见代码
	 */
	public void setManageType(String manageType) {
		this.manageType = manageType;
	}

	/**
	 * @return 返回 accidentCause 出险原因代码；参见代码
	 */
	public String getAccidentCause() {
		return accidentCause;
	}

	/**
	 * @param accidentCause 要设置的 出险原因代码；参见代码
	 */
	public void setAccidentCause(String accidentCause) {
		this.accidentCause = accidentCause;
	}

	/**
	 * @return 返回 paySelfFlag 互碰自赔标志；参见代码
	 */
	public String getPaySelfFlag() {
		return paySelfFlag;
	}

	/**
	 * @param paySelfFlag 要设置的 互碰自赔标志；参见代码
	 */
	public void setPaySelfFlag(String paySelfFlag) {
		this.paySelfFlag = paySelfFlag;
	}

	/**
	 * @return 返回 isSingleAccident 是否单车事故；参见代码
	 */
	public String getIsSingleAccident() {
		return isSingleAccident;
	}

	/**
	 * @param isSingleAccident 要设置的 是否单车事故；参见代码
	 */
	public void setIsSingleAccident(String isSingleAccident) {
		this.isSingleAccident = isSingleAccident;
	}

	/**
	 * @return 返回 isPersonInjured 是否包含人伤；参见代码
	 */
	public String getIsPersonInjured() {
		return isPersonInjured;
	}

	/**
	 * @param isPersonInjured 要设置的 是否包含人伤；参见代码
	 */
	public void setIsPersonInjured(String isPersonInjured) {
		this.isPersonInjured = isPersonInjured;
	}

	/**
	 * @return 返回 isProtectLoss 是否包含财损；参见代码
	 */
	public String getIsProtectLoss() {
		return isProtectLoss;
	}

	/**
	 * @param isProtectLoss 要设置的 是否包含财损；参见代码
	 */
	public void setIsProtectLoss(String isProtectLoss) {
		this.isProtectLoss = isProtectLoss;
	}
}
