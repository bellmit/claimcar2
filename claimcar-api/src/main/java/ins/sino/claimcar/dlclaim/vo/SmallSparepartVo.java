package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmallSparepartVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String insurance_material_id;//保险公司辅料ID
	private String batch_no;//批次号
	private String price_scheme;//价格方案
	private String material_code;//辅料编码
	private String material_name;//辅料名称
	private String first_estimate_price;//保险公司首次定损单价
	private String first_estimate_quantity;//保险公司首次定损数量
	private String first_estimate_total;//保险公司首次定损总价
	private String estimate_price;//定损单价
	private String estimate_quantity;//定损数量
	private String estimate_total;//定损总价
	private String verify_price_price;//核价单价
	private String verify_price_quantity;//核价数量
	private String verify_price_total;//核价总价
	private String under_price;//核损单价
	private String under_quantity;//核损数量
	private String under_total;//核损总价
	private String is_hand;//是否手动录入
	private String operate_code;//保险公司操作人编码
	private String operate_name;//保险公司操作人姓名
	private String description;//描述
	public String getInsurance_material_id() {
		return insurance_material_id;
	}
	public void setInsurance_material_id(String insurance_material_id) {
		this.insurance_material_id = insurance_material_id;
	}
	public String getBatch_no() {
		return batch_no;
	}
	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}
	public String getPrice_scheme() {
		return price_scheme;
	}
	public void setPrice_scheme(String price_scheme) {
		this.price_scheme = price_scheme;
	}
	public String getMaterial_code() {
		return material_code;
	}
	public void setMaterial_code(String material_code) {
		this.material_code = material_code;
	}
	public String getMaterial_name() {
		return material_name;
	}
	public void setMaterial_name(String material_name) {
		this.material_name = material_name;
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
	public String getFirst_estimate_total() {
		return first_estimate_total;
	}
	public void setFirst_estimate_total(String first_estimate_total) {
		this.first_estimate_total = first_estimate_total;
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
