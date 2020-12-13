package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoricalClaimVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String status;//案件状态
	private String insurance_category;//险种
	private String paid_times;//赔付次数
	private String pay_claim_number;//赔案号
	private String claim_end_date;//结案时间
	private String claim_result;//结案类型
	private String estimate_amount;//估损金额
	private String claim_date;//出险时间
	private String event_address;//出险地点
	private String license_plate_no;//车牌号
	private String report_date;//报案时间
	private String paid_amount;//保单结案金额
	private String danger_desc;//出险经过
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInsurance_category() {
		return insurance_category;
	}
	public void setInsurance_category(String insurance_category) {
		this.insurance_category = insurance_category;
	}
	public String getPaid_times() {
		return paid_times;
	}
	public void setPaid_times(String paid_times) {
		this.paid_times = paid_times;
	}
	public String getPay_claim_number() {
		return pay_claim_number;
	}
	public void setPay_claim_number(String pay_claim_number) {
		this.pay_claim_number = pay_claim_number;
	}
	public String getClaim_end_date() {
		return claim_end_date;
	}
	public void setClaim_end_date(String claim_end_date) {
		this.claim_end_date = claim_end_date;
	}
	public String getClaim_result() {
		return claim_result;
	}
	public void setClaim_result(String claim_result) {
		this.claim_result = claim_result;
	}
	public String getEstimate_amount() {
		return estimate_amount;
	}
	public void setEstimate_amount(String estimate_amount) {
		this.estimate_amount = estimate_amount;
	}
	public String getClaim_date() {
		return claim_date;
	}
	public void setClaim_date(String claim_date) {
		this.claim_date = claim_date;
	}
	public String getEvent_address() {
		return event_address;
	}
	public void setEvent_address(String event_address) {
		this.event_address = event_address;
	}
	public String getLicense_plate_no() {
		return license_plate_no;
	}
	public void setLicense_plate_no(String license_plate_no) {
		this.license_plate_no = license_plate_no;
	}
	public String getReport_date() {
		return report_date;
	}
	public void setReport_date(String report_date) {
		this.report_date = report_date;
	}
	public String getPaid_amount() {
		return paid_amount;
	}
	public void setPaid_amount(String paid_amount) {
		this.paid_amount = paid_amount;
	}
	public String getDanger_desc() {
		return danger_desc;
	}
	public void setDanger_desc(String danger_desc) {
		this.danger_desc = danger_desc;
	}
	
	
	
}
