package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BiLossBaseVo {

	@XmlElement(name = "ConfirmSequenceNo", required = true)
	private String confirmSequenceNo;// 投保确认号

	@XmlElement(name = "ClaimSequenceNo", required = true)
	private String claimSequenceNo;// 理赔编号

	@XmlElement(name = "ClaimNotificationNo", required = true)
	private String claimNotificationNo;// 报案号

	@XmlElement(name = "AccidentType")
	private String accidentType;// 保险事故分类；参见代码

	@XmlElement(name = "AccidentLiability", required = true)
	private String accidentLiability;// 事故责任划分 参见代码

	@XmlElement(name = "OptionType", required = true)
	private String optionType;// 事故处理方式；参见代码

	@XmlElement(name = "SubrogationFlag", required = true)
	private String subrogationFlag;// 是否要求代位；参见代码

	@XmlElement(name = "SubCertiType")
	private String subCertiType;// 责任认定书类型：参见代码

	@XmlElement(name = "SubClaimFlag")
	private String subClaimFlag;// 代位求偿案件索赔申请书标志；参见代码

	@XmlElement(name = "IsSingleAccident", required = true)
	private String isSingleAccident;// 是否单车事故；参见代码

	@XmlElement(name = "UnderTotalDefLoss", required = true)
	private String underTotalDefLoss;// 核损总金额

	@XmlElement(name = "IsPersonInjured", required = true)
	private String isPersonInjured;// 是否包含人伤；参见代码

	@XmlElement(name = "IsProtectLoss", required = true)
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
	 * @return 返回 claimSequenceNo 理赔编号
	 */
	public String getClaimSequenceNo() {
		return claimSequenceNo;
	}

	/**
	 * @param claimSequenceNo 要设置的 理赔编号
	 */
	public void setClaimSequenceNo(String claimSequenceNo) {
		this.claimSequenceNo = claimSequenceNo;
	}

	/**
	 * @return 返回 claimNotificationNo 报案号
	 */
	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}

	/**
	 * @param claimNotificationNo 要设置的 报案号
	 */
	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
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
	 * @return 返回 accidentLiability 事故责任划分 参见代码
	 */
	public String getAccidentLiability() {
		return accidentLiability;
	}

	/**
	 * @param accidentLiability 要设置的 事故责任划分 参见代码
	 */
	public void setAccidentLiability(String accidentLiability) {
		this.accidentLiability = accidentLiability;
	}

	/**
	 * @return 返回 optionType 事故处理方式；参见代码
	 */
	public String getOptionType() {
		return optionType;
	}

	/**
	 * @param optionType 要设置的 事故处理方式；参见代码
	 */
	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	/**
	 * @return 返回 subrogationFlag 是否要求代位；参见代码
	 */
	public String getSubrogationFlag() {
		return subrogationFlag;
	}

	/**
	 * @param subrogationFlag 要设置的 是否要求代位；参见代码
	 */
	public void setSubrogationFlag(String subrogationFlag) {
		this.subrogationFlag = subrogationFlag;
	}

	/**
	 * @return 返回 subCertiType 责任认定书类型：参见代码
	 */
	public String getSubCertiType() {
		return subCertiType;
	}

	/**
	 * @param subCertiType 要设置的 责任认定书类型：参见代码
	 */
	public void setSubCertiType(String subCertiType) {
		this.subCertiType = subCertiType;
	}

	/**
	 * @return 返回 subClaimFlag 代位求偿案件索赔申请书标志；参见代码
	 */
	public String getSubClaimFlag() {
		return subClaimFlag;
	}

	/**
	 * @param subClaimFlag 要设置的 代位求偿案件索赔申请书标志；参见代码
	 */
	public void setSubClaimFlag(String subClaimFlag) {
		this.subClaimFlag = subClaimFlag;
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
	 * @return 返回 underTotalDefLoss 核损总金额
	 */
	public String getUnderTotalDefLoss() {
		return underTotalDefLoss;
	}

	/**
	 * @param underTotalDefLoss 要设置的 核损总金额
	 */
	public void setUnderTotalDefLoss(String underTotalDefLoss) {
		this.underTotalDefLoss = underTotalDefLoss;
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
