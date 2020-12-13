/******************************************************************************
 * CREATETIME : 2016年5月26日 下午2:32:36
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 追偿保险公司（多条）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCILossReqSubrogationDataVo {

	/**
	 * 被追偿保险公司代码
	 */
	@XmlElement(name = "BERECOVERY_COM")
	private String berecoveryCom;// 被追偿保险公司代码

	/**
	 * 被追偿保险公司报案号
	 */
	@XmlElement(name = "BERECOVERY_REPORT_NO")
	private String berecoveryReportNo;// 被追偿保险公司报案号

	
	
	public String getBerecoveryCom() {
		return berecoveryCom;
	}

	public void setBerecoveryCom(String berecoveryCom) {
		this.berecoveryCom = berecoveryCom;
	}

	public String getBerecoveryReportNo() {
		return berecoveryReportNo;
	}

	public void setBerecoveryReportNo(String berecoveryReportNo) {
		this.berecoveryReportNo = berecoveryReportNo;
	}

}
