/******************************************************************************
 * CREATETIME : 2016年5月24日 上午10:07:31
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 理算核赔登记(商业) 基本信息
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiVClaimBasePartVo {

	@XmlElement(name = "ClaimSequenceNo", required = true)
	private String claimSequenceNo;// 理赔编码

	@XmlElement(name = "ClaimNotificationNo", required = true)
	private String claimNotificationNo;// 报案号

	@XmlElement(name = "ConfirmSequenceNo", required = true)
	private String confirmSequenceNo;// 投保确认码
	@XmlElement(name = "IsInvolving", required = true)
	private String isInVovling;// 是否涉诉
	
	public String getIsInVovling() {
		return isInVovling;
	}

	public void setIsInVovling(String isInVovling) {
		this.isInVovling = isInVovling;
	}

	/**
	 * @return 返回 claimSequenceNo 理赔编码
	 */
	public String getClaimSequenceNo() {
		return claimSequenceNo;
	}

	/**
	 * @param claimSequenceNo 要设置的 理赔编码
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

}
