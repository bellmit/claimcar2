/******************************************************************************
 * CREATETIME : 2016年5月24日 下午2:44:24
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiEndCaseBasePartVo {

	@XmlElement(name = "CONFIRM_SEQUENCE_NO", required = true)
	private String confirmSequenceNo;// 投保确认号

	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// 理赔编号

	@XmlElement(name = "CLAIM_AMOUNT", required = true)
	private Double claimAmount;// 赔款总金额；不含无责代赔金额

	@XmlElement(name = "REPORT_NO", required = true)
	private String reportNo;// 报案编号

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "ENDCASE_DATE", required = true)
	private Date endcaseDate;// 结案时间；格式YYYYMMDDHHMM

	@XmlElement(name = "INSURED", required = true)
	private String insured;// 是否属于保险责任；参见代码

	@XmlElement(name = "CLAIM_TYPE", required = true)
	private String claimType;// 理赔类型代码；参见代码

	@XmlElement(name = "PAY_CAUSE")
	private String payCause;// 垫付原因代码；参见代码

	@XmlElement(name = "REFUSE_CAUSE")
	private String refuseCause;// 拒赔原因

	@XmlElement(name = "LIABILITY_AMOUNT", required = true)
	private String liabilityAmount;// 事故责任划分代码；参见代码

	@XmlElement(name = "ACCIDENT_DEATH", required = true)
	private String accidentDeath;// 是否发生有责任交通死亡事故的标志；

	@XmlElement(name = "PAY_SELF_FLAG", required = true)
	private String paySelfFlag;// 互碰自赔标志；参见代码

	@XmlElement(name = "CLAIM_CONFIRM_CODE")
	private String claimConfirmCode;// 结案校验码

	@XmlElement(name = "ACCIDENT_TYPE", required = true)
	private String accidentType;// 保险事故分类；参见代码

/*	@XmlElement(name = "OTHER_FEE")
	private Double otherFee;// 其他费用
*/
	@XmlElement(name = "IS_REFUSE_CASE", required = true)
	private String isRefuseCase;// 是否拒赔案件；参见代码

	@XmlElement(name = "NORES_INSTEAD_AMOUNT")
	private Double noresInsteadAmount;// 无责代赔金额

	@XmlElement(name = "FRAUD_LOGO")
	private String fraudLogo;// 欺诈标志；参见代码

	@XmlElement(name = "FRAUD_RECOVER_AMOUNT")
	private Double fraudRecoverAmount;// 欺诈挽回损失金额

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
	 * @return 返回 claimAmount 赔款总金额；不含无责代赔金额
	 */
	public Double getClaimAmount() {
		return claimAmount;
	}

	/**
	 * @param claimAmount 要设置的 赔款总金额；不含无责代赔金额
	 */
	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	/**
	 * @return 返回 reportNo 报案编号
	 */
	public String getReportNo() {
		return reportNo;
	}

	/**
	 * @param reportNo 要设置的 报案编号
	 */
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	/**
	 * @return 返回 endcaseDate 结案时间；格式YYYYMMDDHHMM
	 */
	public Date getEndcaseDate() {
		return endcaseDate;
	}

	/**
	 * @param endcaseDate 要设置的 结案时间；格式YYYYMMDDHHMM
	 */
	public void setEndcaseDate(Date endcaseDate) {
		this.endcaseDate = endcaseDate;
	}

	/**
	 * @return 返回 insured 是否属于保险责任；参见代码
	 */
	public String getInsured() {
		return insured;
	}

	/**
	 * @param insured 要设置的 是否属于保险责任；参见代码
	 */
	public void setInsured(String insured) {
		this.insured = insured;
	}

	/**
	 * @return 返回 claimType 理赔类型代码；参见代码
	 */
	public String getClaimType() {
		return claimType;
	}

	/**
	 * @param claimType 要设置的 理赔类型代码；参见代码
	 */
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	/**
	 * @return 返回 payCause 垫付原因代码；参见代码
	 */
	public String getPayCause() {
		return payCause;
	}

	/**
	 * @param payCause 要设置的 垫付原因代码；参见代码
	 */
	public void setPayCause(String payCause) {
		this.payCause = payCause;
	}

	/**
	 * @return 返回 refuseCause 拒赔原因
	 */
	public String getRefuseCause() {
		return refuseCause;
	}

	/**
	 * @param refuseCause 要设置的 拒赔原因
	 */
	public void setRefuseCause(String refuseCause) {
		this.refuseCause = refuseCause;
	}

	/**
	 * @return 返回 liabilityAmount 事故责任划分代码；参见代码
	 */
	public String getLiabilityAmount() {
		return liabilityAmount;
	}

	/**
	 * @param liabilityAmount 要设置的 事故责任划分代码；参见代码
	 */
	public void setLiabilityAmount(String liabilityAmount) {
		this.liabilityAmount = liabilityAmount;
	}

	/**
	 * @return 返回 accidentDeath 是否发生有责任交通死亡事故的标志；
	 */
	public String getAccidentDeath() {
		return accidentDeath;
	}

	/**
	 * @param accidentDeath 要设置的 是否发生有责任交通死亡事故的标志；
	 */
	public void setAccidentDeath(String accidentDeath) {
		this.accidentDeath = accidentDeath;
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
	 * @return 返回 claimConfirmCode 结案校验码
	 */
	public String getClaimConfirmCode() {
		return claimConfirmCode;
	}

	/**
	 * @param claimConfirmCode 要设置的 结案校验码
	 */
	public void setClaimConfirmCode(String claimConfirmCode) {
		this.claimConfirmCode = claimConfirmCode;
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
	 * @return 返回 otherFee 其他费用
	 */
/*	public Double getOtherFee() {
		return otherFee;
	}
*/
	/**
	 * @param otherFee 要设置的 其他费用
	 */
/*	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}
*/
	/**
	 * @return 返回 isRefuseCase 是否拒赔案件；参见代码
	 */
	public String getIsRefuseCase() {
		return isRefuseCase;
	}

	/**
	 * @param isRefuseCase 要设置的 是否拒赔案件；参见代码
	 */
	public void setIsRefuseCase(String isRefuseCase) {
		this.isRefuseCase = isRefuseCase;
	}

	/**
	 * @return 返回 noresInsteadAmount 无责代赔金额
	 */
	public Double getNoresInsteadAmount() {
		return noresInsteadAmount;
	}

	/**
	 * @param noresInsteadAmount 要设置的 无责代赔金额
	 */
	public void setNoresInsteadAmount(Double noresInsteadAmount) {
		this.noresInsteadAmount = noresInsteadAmount;
	}

	/**
	 * @return 返回 fraudLogo 欺诈标志；参见代码
	 */
	public String getFraudLogo() {
		return fraudLogo;
	}

	/**
	 * @param fraudLogo 要设置的 欺诈标志；参见代码
	 */
	public void setFraudLogo(String fraudLogo) {
		this.fraudLogo = fraudLogo;
	}

	/**
	 * @return 返回 fraudRecoverAmount 欺诈挽回损失金额
	 */
	public Double getFraudRecoverAmount() {
		return fraudRecoverAmount;
	}

	/**
	 * @param fraudRecoverAmount 要设置的 欺诈挽回损失金额
	 */
	public void setFraudRecoverAmount(Double fraudRecoverAmount) {
		this.fraudRecoverAmount = fraudRecoverAmount;
	}

}
