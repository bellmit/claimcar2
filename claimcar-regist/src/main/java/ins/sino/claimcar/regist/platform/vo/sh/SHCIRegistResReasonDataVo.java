/******************************************************************************
 * CREATETIME : 2016年5月24日 下午6:56:26
 ******************************************************************************/
package ins.sino.claimcar.regist.platform.vo.sh;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 争议信息列表（多条）属于代位求偿码信息,BERECOVERY_LIST属于代位求偿码信息,
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIRegistResReasonDataVo {

	@XmlElement(name = "COMPANY_ID")
	private String companyId;// 保险公司代码

	@XmlElement(name = "REASON")
	private String reason;// 争议理由

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "DISPUTE_TIME")
	private String disputeTime;// 争议时间

	/**
	 * @return 返回 companyId 保险公司代码
	 */
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId 要设置的 保险公司代码
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return 返回 reason 争议理由
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason 要设置的 争议理由
	 */
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
