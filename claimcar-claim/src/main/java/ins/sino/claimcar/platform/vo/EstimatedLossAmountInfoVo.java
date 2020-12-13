/******************************************************************************
 * CREATETIME : 2016年6月7日 下午3:02:23
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
public class EstimatedLossAmountInfoVo {

	@XmlElement(name = "ClaimSequenceNo")
	private String claimsequenceno;// 理赔编号

	@XmlElement(name = "ConfirmSequenceNo")
	private String confirmsequenceno;// 投保确认码

	@XmlElement(name = "ClaimNotificationNo")
	private String claimnotificationno;// 报案号

	@XmlElement(name = "EstimatedLossAmount")
	private Double estimatedlossamount;// 估损金额

	/**
	 * @return 返回 claimsequenceno 理赔编号
	 */
	public String getClaimsequenceno() {
		return claimsequenceno;
	}

	/**
	 * @param claimsequenceno 要设置的 理赔编号
	 */
	public void setClaimsequenceno(String claimsequenceno) {
		this.claimsequenceno = claimsequenceno;
	}

	/**
	 * @return 返回 confirmsequenceno 投保确认码
	 */
	public String getConfirmsequenceno() {
		return confirmsequenceno;
	}

	/**
	 * @param confirmsequenceno 要设置的 投保确认码
	 */
	public void setConfirmsequenceno(String confirmsequenceno) {
		this.confirmsequenceno = confirmsequenceno;
	}

	/**
	 * @return 返回 claimnotificationno 报案号
	 */
	public String getClaimnotificationno() {
		return claimnotificationno;
	}

	/**
	 * @param claimnotificationno 要设置的 报案号
	 */
	public void setClaimnotificationno(String claimnotificationno) {
		this.claimnotificationno = claimnotificationno;
	}

	/**
	 * @return 返回 estimatedlossamount 估损金额
	 */
	public Double getEstimatedlossamount() {
		return estimatedlossamount;
	}

	/**
	 * @param estimatedlossamount 要设置的 估损金额
	 */
	public void setEstimatedlossamount(Double estimatedlossamount) {
		this.estimatedlossamount = estimatedlossamount;
	}

}
