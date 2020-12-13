package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;
import java.util.List;

public class SmallSparepartResVo implements Serializable{
private static final long serialVersionUID = 1L;
private String insurance_material_id;//保险公司辅料ID
private String material_name;//辅料名称
private String input_price;//保险公司提交单价
private String input_count;//保险公司提交数量
private String input_totalPrice;//保险公司提交单价*保险公司提交数量
private String feedback_price;//CE建议单价
private String feedback_quantity;//CE建议数量
private String feedback_total;//CE建议单价*CE建议数量
private String saving_price;//保险公司提交价格-CE建议价格
private List<String> remark;//减损描述列表	每一个子节点表示一项减损描述的信息,多项减损描述有多个子节点。
private String ceSaving;//Y: 有减损 N: 无减损
public String getInsurance_material_id() {
	return insurance_material_id;
}
public void setInsurance_material_id(String insurance_material_id) {
	this.insurance_material_id = insurance_material_id;
}
public String getMaterial_name() {
	return material_name;
}
public void setMaterial_name(String material_name) {
	this.material_name = material_name;
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


}
