/******************************************************************************
 * CREATETIME : 2016年6月1日 下午6:58:00
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIVClaimSubrogationDataVo {

	@XmlElement(name = "BERECOVERY_REPORT_NO")
	private String berecoveryReportNo;// 被追偿保险公司报案号

	/**
	 * @return 返回 berecoveryReportNo 被追偿保险公司报案号
	 */
	public String getBerecoveryReportNo() {
		return berecoveryReportNo;
	}

	/**
	 * @param berecoveryReportNo 要设置的 被追偿保险公司报案号
	 */
	public void setBerecoveryReportNo(String berecoveryReportNo) {
		this.berecoveryReportNo = berecoveryReportNo;
	}

}
