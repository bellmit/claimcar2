package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SparepartVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String insurance_fit_id;//保险公司配件ID
	private String batch_no;//批次号
	private String oem_code;//OEM编码
	private String fitting_name;//配件名称
	private String fitting_num;//配件件数
	private String price_scheme;//价格方案
	private String first_estimate_price;//保险公司首次定损单价
	private String first_estimate_quantity;//保险公司首次定损数量
	private String first_estimate_remnant;//保险公司首次定损残值
	private String first_estimate_total;//保险公司首次定损总价
	private String first_estimate_is_recycle;//保险公司首次定损是否回收
	private String estimate_price;//定损单价
	private String estimate_quantity;//定损数量
	private String estimate_total;//定损总价
	private String estimate_remnant;//定损残值
	private String estimate_is_recycle;//定损是否回收
	private String verify_price_price;//核价单价
	private String verify_price_quantity;//核价数量
	private String verify_price_total;//核价总价
	private String verify_price_remnant;//核价残值
	private String verify_price_is_recycle;//核价是否回收
	private String under_price;//核损单价
	private String under_quantity;//核损数量
	private String under_total;//核损总价
	private String under_remnant;//核损残值
	private String under_is_recycle;//核损是否回收
	private String management_fee;//管理费
	private String management_fee_rate;//管理费率
	private String cooperation_fitting_discount;//合作配件折扣
	private String is_add;//是否追加
	private String operation_status;//追加状态 
	private String is_hand;//是否手动添加
	private String operate_code;//保险公司操作人编码
	private String operate_name;//保险公司操作人姓名
	private String description;//描述
	public String getInsurance_fit_id() {
		return insurance_fit_id;
	}
	public void setInsurance_fit_id(String insurance_fit_id) {
		this.insurance_fit_id = insurance_fit_id;
	}
	public String getBatch_no() {
		return batch_no;
	}
	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}
	public String getOem_code() {
		return oem_code;
	}
	public void setOem_code(String oem_code) {
		this.oem_code = oem_code;
	}
	public String getFitting_name() {
		return fitting_name;
	}
	public void setFitting_name(String fitting_name) {
		this.fitting_name = fitting_name;
	}
	public String getFitting_num() {
		return fitting_num;
	}
	public void setFitting_num(String fitting_num) {
		this.fitting_num = fitting_num;
	}
	public String getPrice_scheme() {
		return price_scheme;
	}
	public void setPrice_scheme(String price_scheme) {
		this.price_scheme = price_scheme;
	}
	public String getFirst_estimate_price() {
		return first_estimate_price;
	}
	public void setFirst_estimate_price(String first_estimate_price) {
		this.first_estimate_price = first_estimate_price;
	}
	public String getFirst_estimate_quantity() {
		return first_estimate_quantity;
	}
	public void setFirst_estimate_quantity(String first_estimate_quantity) {
		this.first_estimate_quantity = first_estimate_quantity;
	}
	public String getFirst_estimate_remnant() {
		return first_estimate_remnant;
	}
	public void setFirst_estimate_remnant(String first_estimate_remnant) {
		this.first_estimate_remnant = first_estimate_remnant;
	}
	public String getFirst_estimate_total() {
		return first_estimate_total;
	}
	public void setFirst_estimate_total(String first_estimate_total) {
		this.first_estimate_total = first_estimate_total;
	}
	public String getFirst_estimate_is_recycle() {
		return first_estimate_is_recycle;
	}
	public void setFirst_estimate_is_recycle(String first_estimate_is_recycle) {
		this.first_estimate_is_recycle = first_estimate_is_recycle;
	}
	public String getEstimate_price() {
		return estimate_price;
	}
	public void setEstimate_price(String estimate_price) {
		this.estimate_price = estimate_price;
	}
	public String getEstimate_quantity() {
		return estimate_quantity;
	}
	public void setEstimate_quantity(String estimate_quantity) {
		this.estimate_quantity = estimate_quantity;
	}
	public String getEstimate_total() {
		return estimate_total;
	}
	public void setEstimate_total(String estimate_total) {
		this.estimate_total = estimate_total;
	}
	public String getEstimate_remnant() {
		return estimate_remnant;
	}
	public void setEstimate_remnant(String estimate_remnant) {
		this.estimate_remnant = estimate_remnant;
	}
	public String getEstimate_is_recycle() {
		return estimate_is_recycle;
	}
	public void setEstimate_is_recycle(String estimate_is_recycle) {
		this.estimate_is_recycle = estimate_is_recycle;
	}
	public String getVerify_price_price() {
		return verify_price_price;
	}
	public void setVerify_price_price(String verify_price_price) {
		this.verify_price_price = verify_price_price;
	}
	public String getVerify_price_quantity() {
		return verify_price_quantity;
	}
	public void setVerify_price_quantity(String verify_price_quantity) {
		this.verify_price_quantity = verify_price_quantity;
	}
	public String getVerify_price_total() {
		return verify_price_total;
	}
	public void setVerify_price_total(String verify_price_total) {
		this.verify_price_total = verify_price_total;
	}
	public String getVerify_price_remnant() {
		return verify_price_remnant;
	}
	public void setVerify_price_remnant(String verify_price_remnant) {
		this.verify_price_remnant = verify_price_remnant;
	}
	public String getVerify_price_is_recycle() {
		return verify_price_is_recycle;
	}
	public void setVerify_price_is_recycle(String verify_price_is_recycle) {
		this.verify_price_is_recycle = verify_price_is_recycle;
	}
	public String getUnder_price() {
		return under_price;
	}
	public void setUnder_price(String under_price) {
		this.under_price = under_price;
	}
	public String getUnder_quantity() {
		return under_quantity;
	}
	public void setUnder_quantity(String under_quantity) {
		this.under_quantity = under_quantity;
	}
	public String getUnder_total() {
		return under_total;
	}
	public void setUnder_total(String under_total) {
		this.under_total = under_total;
	}
	public String getUnder_remnant() {
		return under_remnant;
	}
	public void setUnder_remnant(String under_remnant) {
		this.under_remnant = under_remnant;
	}
	public String getUnder_is_recycle() {
		return under_is_recycle;
	}
	public void setUnder_is_recycle(String under_is_recycle) {
		this.under_is_recycle = under_is_recycle;
	}
	public String getManagement_fee() {
		return management_fee;
	}
	public void setManagement_fee(String management_fee) {
		this.management_fee = management_fee;
	}
	public String getManagement_fee_rate() {
		return management_fee_rate;
	}
	public void setManagement_fee_rate(String management_fee_rate) {
		this.management_fee_rate = management_fee_rate;
	}
	public String getCooperation_fitting_discount() {
		return cooperation_fitting_discount;
	}
	public void setCooperation_fitting_discount(String cooperation_fitting_discount) {
		this.cooperation_fitting_discount = cooperation_fitting_discount;
	}
	public String getIs_add() {
		return is_add;
	}
	public void setIs_add(String is_add) {
		this.is_add = is_add;
	}
	public String getOperation_status() {
		return operation_status;
	}
	public void setOperation_status(String operation_status) {
		this.operation_status = operation_status;
	}
	public String getIs_hand() {
		return is_hand;
	}
	public void setIs_hand(String is_hand) {
		this.is_hand = is_hand;
	}
	public String getOperate_code() {
		return operate_code;
	}
	public void setOperate_code(String operate_code) {
		this.operate_code = operate_code;
	}
	public String getOperate_name() {
		return operate_name;
	}
	public void setOperate_name(String operate_name) {
		this.operate_name = operate_name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

}
