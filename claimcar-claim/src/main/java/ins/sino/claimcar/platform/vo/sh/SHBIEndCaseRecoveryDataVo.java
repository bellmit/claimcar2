/******************************************************************************
 * CREATETIME : 2016年6月6日 下午7:36:02
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIEndCaseRecoveryDataVo {

	@XmlElement(name = "BERECOVERY_REPORT_NO")
	private String berecoveryReportNo;// 被追偿公司报案号

	@XmlElement(name = "RECOVERY_AMOUNT ")
	private Double recoveryAmount;// 追偿金额

	/**
	 * @return 返回 berecoveryReportNo 被追偿公司报案号
	 */
	public String getBerecoveryReportNo() {
		return berecoveryReportNo;
	}

	/**
	 * @param berecoveryReportNo 要设置的 被追偿公司报案号
	 */
	public void setBerecoveryReportNo(String berecoveryReportNo) {
		this.berecoveryReportNo = berecoveryReportNo;
	}

	/**
	 * @return 返回 recoveryAmount 追偿金额
	 */
	public Double getRecoveryAmount() {
		return recoveryAmount;
	}

	/**
	 * @param recoveryAmount 要设置的 追偿金额
	 */
	public void setRecoveryAmount(Double recoveryAmount) {
		this.recoveryAmount = recoveryAmount;
	}

}
