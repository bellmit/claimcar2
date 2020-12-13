/******************************************************************************
 * CREATETIME : 2016年5月30日 下午2:37:42
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 被代位求偿信息列表（多条）BERECOVERY_LIST属于代位求偿码信息
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIClaimResBerecoveryDataVo {

	@XmlElement(name = "BERECOVERY_COM")
	private String berecoveryCom;// 被追偿保险公司代码

	@XmlElement(name = "BERECOVERY_REPORT_NO")
	private String berecoveryReportNo;// 被追偿保险公司报案号

	@XmlElement(name = "STATUS")
	private String status;// 被追偿保险公司案件状态

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
