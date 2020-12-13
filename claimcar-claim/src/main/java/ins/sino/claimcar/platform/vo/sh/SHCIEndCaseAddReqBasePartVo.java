/******************************************************************************
 * CREATETIME : 2016年6月1日 上午10:38:10
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.Date;
import ins.sino.claimcar.carplatform.adapter.DateXmlAdapterSH;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 基本信息
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIEndCaseAddReqBasePartVo {

	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// 理赔编码

	@XmlElement(name = "REASON_TYPE", required = true)
	private String reasonType;// 追加原因

	/**
	 * 首次向客户索取单证的开始时间--YYYYMMDDHHMMSS
	 */
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "DOC_START_TIME")
	private Date docStartTime;// 单证收集开始时间

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "DOC_END_TIME")
	private Date docEndTime;// 单证收集结束时间

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "NUMERATION_START_TIME")
	private Date numerationStartTime;// 厘算开始时间

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "NUMERATION_END_TIME")
	private Date numerationEndTime;// 厘算结束时间

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "ADD_TIME", required = true)
	private Date addTime;// 结案追加时间

	/**
	 * 是否临时车辆
	 */
	@XmlElement(name = "TemporaryFlag", required = true)
	private String temporaryFlag;// 是否临时车辆

	/**
	 * 保险公司对案件的描述信息
	 */
	@XmlElement(name = "Remark")
	private String Remark;// 案件备注

	/**
	 * 追加未决估计赔款
	 */
	@XmlElement(name = "ESTIMATE", required = true)
	private Double estimate;// 当前业务时间点的案件追加的未决赔款金额
	
	@XmlElement(name = "REPORT_NO")
	private String registNo;// 
	
	
	/*牛强  2017-03-15 改*/
	@XmlElement(name="IsSingleAccident")
	private String isSingleAccident; //是否单车事故
	
	@XmlElement(name="IsPersonInjured")
	private String isPersonInjured; //是否包含人伤
	
	@XmlElement(name="IsProtectLoss")
	private String isProtectLoss; //是否包含财损
	
	@XmlElement(name="UnderDefLoss")
	private double underDefLoss; //核损总金额
	
	
	
	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getClaimCode() {
		return claimCode;
	}

	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	public String getReasonType() {
		return reasonType;
	}

	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}

	public Date getDocStartTime() {
		return docStartTime;
	}

	public void setDocStartTime(Date docStartTime) {
		this.docStartTime = docStartTime;
	}

	public Date getDocEndTime() {
		return docEndTime;
	}

	public void setDocEndTime(Date docEndTime) {
		this.docEndTime = docEndTime;
	}

	public Date getNumerationStartTime() {
		return numerationStartTime;
	}

	public void setNumerationStartTime(Date numerationStartTime) {
		this.numerationStartTime = numerationStartTime;
	}

	public Date getNumerationEndTime() {
		return numerationEndTime;
	}

	public void setNumerationEndTime(Date numerationEndTime) {
		this.numerationEndTime = numerationEndTime;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public Double getEstimate() {
		return estimate;
	}

	public void setEstimate(Double estimate) {
		this.estimate = estimate;
	}

	public String getTemporaryFlag() {
		return temporaryFlag;
	}

	public void setTemporaryFlag(String temporaryFlag) {
		this.temporaryFlag = temporaryFlag;
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

	public double getUnderDefLoss() {
		return underDefLoss;
	}

	public void setUnderDefLoss(double underDefLoss) {
		this.underDefLoss = underDefLoss;
	}

}
