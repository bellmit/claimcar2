package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;
import java.util.List;

public class LaborResVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String insurance_labor_id;//保险公司工时ID
	private String repair_type;//工种，见数据字典《工种类型》
	private String repair_name;//配件名称
	private String input_price;//保险公司提交单价
	private String input_count;//保险公司提交数量
	private String input_totalPrice;//保险公司提交单价*保险公司提交数量
	private String feedback_unit_price;//CE建议单价	CE建议单价
	private String feedback_quantity;//CE建议数量	CE建议数量
	private String feedback_price;//CE建议单价*CE建议数量
	private String saving_price;//保险公司提交价格-CE建议价格
	private List<String> remark;//每一个子节点表示一项减损描述的信息，多项减损描述有多个子节点。["减损提示1", "减损提示2"]
	private String ceSaving;//是否减损	Y: 有减损N: 无减损
	private String insurance_fit_id;//保险公司配件ID
	
	public String getInsurance_labor_id() {
		return insurance_labor_id;
	}
	public void setInsurance_labor_id(String insurance_labor_id) {
		this.insurance_labor_id = insurance_labor_id;
	}
	public String getRepair_type() {
		return repair_type;
	}
	public void setRepair_type(String repair_type) {
		this.repair_type = repair_type;
	}
	public String getRepair_name() {
		return repair_name;
	}
	public void setRepair_name(String repair_name) {
		this.repair_name = repair_name;
	}
	public String getInput_price() {
		return input_price;
	}
	public void setInput_price(String input_price) {
		this.input_price = input_price;
	}
	public String getInput_count() {
		return input_count;
	}
	public void setInput_count(String input_count) {
		this.input_count = input_count;
	}
	public String getInput_totalPrice() {
		return input_totalPrice;
	}
	public void setInput_totalPrice(String input_totalPrice) {
		this.input_totalPrice = input_totalPrice;
	}
	public String getFeedback_unit_price() {
		return feedback_unit_price;
	}
	public void setFeedback_unit_price(String feedback_unit_price) {
		this.feedback_unit_price = feedback_unit_price;
	}
	public String getFeedback_quantity() {
		return feedback_quantity;
	}
	public void setFeedback_quantity(String feedback_quantity) {
		this.feedback_quantity = feedback_quantity;
	}
	public String getFeedback_price() {
		return feedback_price;
	}
	public void setFeedback_price(String feedback_price) {
		this.feedback_price = feedback_price;
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
	public String getInsurance_fit_id() {
		return insurance_fit_id;
	}
	public void setInsurance_fit_id(String insurance_fit_id) {
		this.insurance_fit_id = insurance_fit_id;
	}
    
}
