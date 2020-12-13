/******************************************************************************
 * CREATETIME : 2016年5月25日 上午10:36:25
 ******************************************************************************/
package ins.sino.claimcar.regist.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 被代位求偿信息列表（多条）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIRegistResBerecoveryDataVo {

	@XmlElement(name = "BERECOVERY_COM")
	private String berecoveryCom;// 被追偿保险公司代码

	@XmlElement(name = "BERECOVERY_REPORT_NO")
	private String berecoveryReportNo;// 被追偿保险公司报案号

	@XmlElement(name = "STATUS")
	private String status;// 被追偿保险公司案件状态

	/**
	 * @return 返回 berecoveryCom 被追偿保险公司代码
	 */
	public String getBerecoveryCom() {
		return berecoveryCom;
	}

	/**
	 * @param berecoveryCom 要设置的 被追偿保险公司代码
	 */
	public void setBerecoveryCom(String berecoveryCom) {
		this.berecoveryCom = berecoveryCom;
	}

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

	/**
	 * @return 返回 status 被追偿保险公司案件状态
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status 要设置的 被追偿保险公司案件状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
