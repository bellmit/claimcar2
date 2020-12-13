package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("InsuranceModification")
public class InsuranceModificationVo {
	/*批改内容*/
	@XStreamAlias("Content")
	private String Content;
	/*保单号*/
	@XStreamAlias("InsuranceNumber")
	private String insuranceNumber;
	/*批单号*/
	@XStreamAlias("ModificationBillNo")
	private String modificationBillNo;
	/*批改时间*/
	@XStreamAlias("MofificationDate")
	private String mofificationDate;
	/*序号*/
	@XStreamAlias("Order")
	private String order;
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getInsuranceNumber() {
		return insuranceNumber;
	}
	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}
	public String getModificationBillNo() {
		return modificationBillNo;
	}
	public void setModificationBillNo(String modificationBillNo) {
		this.modificationBillNo = modificationBillNo;
	}
	public String getMofificationDate() {
		return mofificationDate;
	}
	public void setMofificationDate(String mofificationDate) {
		this.mofificationDate = mofificationDate;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
}
