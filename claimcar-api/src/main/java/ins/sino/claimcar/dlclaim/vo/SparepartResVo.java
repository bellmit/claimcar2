package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;
import java.util.List;

public class SparepartResVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String insurance_fit_id;//保险公司传过来的配件ID
	private String fitting_name;//保险公司提交配件名称
	private String input_price;//保险公司提交单价
	private String input_quantity;//保险公司提交数量
	private String input_remnant;//保险公司提交残值
	private String input_totalPrice;//保险公司提交单价*保险公司提交数量
	private String feedback_price;//CE建议单价	CE建议单价
	private String feedback_quantity;//CE建议数量	CE建议数量
	private String feedback_remnant;//CE建议残值	CE建议残值
	private String feedback_total;//CE建议价格	CE建议单价*CE建议数量
	private String saving_price;//保险公司提交价格-CE建议价格
	private List<String> remark;//减损描述列表	每一个子节点表示一项减损描述的信息，多项减损描述有多个子节点。
	private String ceSaving;//Y: 有减损 N: 无减损
	private String oem_code;//OEM编码
	public String getInsurance_fit_id() {
		return insurance_fit_id;
	}
	public void setInsurance_fit_id(String insurance_fit_id) {
		this.insurance_fit_id = insurance_fit_id;
	}
	public String getFitting_name() {
		return fitting_name;
	}
	public void setFitting_name(String fitting_name) {
		this.fitting_name = fitting_name;
	}
	public String getInput_price() {
		return input_price;
	}
	public void setInput_price(String input_price) {
		this.input_price = input_price;
	}
	public String getInput_quantity() {
		return input_quantity;
	}
	public void setInput_quantity(String input_quantity) {
		this.input_quantity = input_quantity;
	}
	public String getInput_remnant() {
		return input_remnant;
	}
	public void setInput_remnant(String input_remnant) {
		this.input_remnant = input_remnant;
	}
	public String getInput_totalPrice() {
		return input_totalPrice;
	}
	public void setInput_totalPrice(String input_totalPrice) {
		this.input_totalPrice = input_totalPrice;
	}
	public String getFeedback_price() {
		return feedback_price;
	}
	public void setFeedback_price(String feedback_price) {
		this.feedback_price = feedback_price;
	}
	public String getFeedback_quantity() {
		return feedback_quantity;
	}
	public void setFeedback_quantity(String feedback_quantity) {
		this.feedback_quantity = feedback_quantity;
	}
	public String getFeedback_remnant() {
		return feedback_remnant;
	}
	public void setFeedback_remnant(String feedback_remnant) {
		this.feedback_remnant = feedback_remnant;
	}
	public String getFeedback_total() {
		return feedback_total;
	}
	public void setFeedback_total(String feedback_total) {
		this.feedback_total = feedback_total;
	}
	public String getSaving_price() {
		return saving_price;
	}
	public void setSaving_price(String saving_price) {
		this.saving_price = saving_price;
	}
	public List<String> getRemark() {
		return remark;
	}
	public void setRemark(List<String> remark) {
		this.remark = remark;
	}
	public String getCeSaving() {
		return ceSaving;
	}
	public void setCeSaving(String ceSaving) {
		this.ceSaving = ceSaving;
	}
	public String getOem_code() {
		return oem_code;
	}
	public void setOem_code(String oem_code) {
		this.oem_code = oem_code;
	}
    
	
}
