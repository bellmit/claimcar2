/******************************************************************************
 * CREATETIME : 2016年5月30日 下午1:26:51
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 基本信息
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIClaimResBasePartVo {

	@XmlElement(name = "CLAIM_CODE")
	private String claimCode;// 理赔编码

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "START_DATE")
	private String startDate;// 出险保单起期

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "END_DATE")
	private String endDate;// 出险保单止期

	public String getClaimCode() {
		return claimCode;
	}

	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
