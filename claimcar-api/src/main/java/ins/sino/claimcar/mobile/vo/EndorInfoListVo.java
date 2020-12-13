package ins.sino.claimcar.mobile.vo;


import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ENDORINFO")
public class EndorInfoListVo  implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/*
	 * 批单号
	 */
	@XStreamAlias("ENDORNO")
	private String endorNo;
	
	/*
	 * 批改日期
	 */
	@XStreamAlias("ENDORDATE")
	private String endorDate;
	
	/*
	 * 批单审核通过日期
	 */
	@XStreamAlias("ENDORVERIFIEDDATE")
	private String endorVerifiedDate;
	
	/*
	 * 批改内容
	 */
	@XStreamAlias("ENDORCONTENT")
	private String endorContent;
	
	/*
	 * 保额变化信息
	 */
	@XStreamAlias("AMOUNTCHANGE")
	private String amountChange;
	
	/*
	 * 险种变化信息
	 */
	@XStreamAlias("RISKCHANGE")
	private String riskChange;
	
	/*
	 * 联系电话
	 */
	/*@XStreamAlias("PHONENUMBER")
	private String phoneNumber;*/

	public String getEndorNo() {
		return endorNo;
	}

	public void setEndorNo(String endorNo) {
		this.endorNo = endorNo;
	}

	public String getEndorDate() {
		return endorDate;
	}

	public void setEndorDate(String endorDate) {
		this.endorDate = endorDate;
	}

	public String getEndorVerifiedDate() {
		return endorVerifiedDate;
	}

	public void setEndorVerifiedDate(String endorVerifiedDate) {
		this.endorVerifiedDate = endorVerifiedDate;
	}

	public String getEndorContent() {
		return endorContent;
	}

	public void setEndorContent(String endorContent) {
		this.endorContent = endorContent;
	}

	public String getAmountChange() {
		return amountChange;
	}

	public void setAmountChange(String amountChange) {
		this.amountChange = amountChange;
	}

	public String getRiskChange() {
		return riskChange;
	}

	public void setRiskChange(String riskChange) {
		this.riskChange = riskChange;
	}


	
	
}
