package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsuranceModificationVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String modification_bill_no;//批单号
	private String applicant_date;//申请日期
	private String modification_time;//批改时间
	private String content;//批改内容
	
	public String getModification_bill_no() {
		return modification_bill_no;
	}
	public void setModification_bill_no(String modification_bill_no) {
		this.modification_bill_no = modification_bill_no;
	}
	public String getApplicant_date() {
		return applicant_date;
	}
	public void setApplicant_date(String applicant_date) {
		this.applicant_date = applicant_date;
	}
	public String getModification_time() {
		return modification_time;
	}
	public void setModification_time(String modification_time) {
		this.modification_time = modification_time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
    
}
