/******************************************************************************
 * CREATETIME : 2016年5月26日 下午6:21:22
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapterSH;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 争议信息列表（多条）属于代位求偿码信息
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ShCILossResReasonDataVo {

	/**
	 * 保险公司代码
	 */
	@XmlElement(name = "COMPANY_ID")
	private String companyId;

	/**
	 * 争议理由
	 */
	@XmlElement(name = "REASON")
	private String reason;

	/**
	 * 争议时间
	 */
	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "DISPUTE_TIME")
	private String disputeTime;

	
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDisputeTime() {
		return disputeTime;
	}

	public void setDisputeTime(String disputeTime) {
		this.disputeTime = disputeTime;
	}

}
