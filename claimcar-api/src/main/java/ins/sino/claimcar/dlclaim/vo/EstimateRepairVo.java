package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;

public class EstimateRepairVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String fit_name;//外修配件名称
	private String estimate_total;//外修配件维修定损金额
	private String verify_price_total;//外修配件维修核价金额
	private String under_total;//外修配件维修核损金额
	private String company_name;//外修单位名称
	private String is_add;//是否追加
	private String description;//保险留言意见
	public String getFit_name() {
		return fit_name;
	}
	public void setFit_name(String fit_name) {
		this.fit_name = fit_name;
	}
	public String getEstimate_total() {
		return estimate_total;
	}
	public void setEstimate_total(String estimate_total) {
		this.estimate_total = estimate_total;
	}
	public String getVerify_price_total() {
		return verify_price_total;
	}
	public void setVerify_price_total(String verify_price_total) {
		this.verify_price_total = verify_price_total;
	}
	public String getUnder_total() {
		return under_total;
	}
	public void setUnder_total(String under_total) {
		this.under_total = under_total;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getIs_add() {
		return is_add;
	}
	public void setIs_add(String is_add) {
		this.is_add = is_add;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}
