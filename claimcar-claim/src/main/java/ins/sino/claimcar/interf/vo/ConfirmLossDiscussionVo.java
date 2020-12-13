package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/*定核损意见*/
@XStreamAlias("ConfirmLossDiscussion")
public class ConfirmLossDiscussionVo {
	/*总金额*/
	@XStreamAlias("Amount")
	private String amount;
	/*意见内容*/
	@XStreamAlias("Comment")
	private String comment;
	/*分公司名称*/
	@XStreamAlias("Companyname")
	private String companyname;
	/*意见发表时间*/
	@XStreamAlias("Date")
	private String date;
	/*意见提出人姓名*/
	@XStreamAlias("PersonName")
	private String personName;
	/*状态：通过/未通过*/
	@XStreamAlias("Status")
	private String status;
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

}
