/******************************************************************************
 * CREATETIME : 2016年5月23日 下午4:15:19
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
public class BiCertifyBasePartVo {

	@XmlElement(name = "ClaimSequenceNo", required = true)
	private String claimSequenceNo;// 理赔编号

	@XmlElement(name = "ClaimNotificationNo", required = true)
	private String claimNotificationNo;// 报案号

	@XmlElement(name = "ConfirmSequenceNo", required = true)
	private String confirmSequenceNo;// 投保确认号

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "DocEndTime", required = true)
	private Date docEndTime;// 单证首次收集齐全时间；精确到分钟

	@XmlElement(name = "SubrogationFlag", required = true)
	private String subrogationFlag;// 是否要求代位；参见代码

	@XmlElement(name = "SubCertiType")
	private String subCertiType;// 责任认定书类型：参见代码

	@XmlElement(name = "SubClaimFlag")
	private String subClaimFlag;// 代位求偿案件索赔申请书标志；参见代码

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
	 * @return 返回 docEndTime 单证首次收集齐全时间；精确到分钟
	 */
	public Date getDocEndTime() {
		return docEndTime;
	}

	/**
	 * @param docEndTime 要设置的 单证首次收集齐全时间；精确到分钟
	 */
	public void setDocEndTime(Date docEndTime) {
		this.docEndTime = docEndTime;
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

}
