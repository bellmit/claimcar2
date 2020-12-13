package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsuranceItemVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String code;//险别代码
	private String name;//险别名称
	private String insurance_amout;//保额/限额
	private String unit_count;//单位数量
	private String unit_amount;//单位保额
	private String non_deductible;//承保不计免赔
	private String deductible_rate;//免赔率
	private String deductible_money;//免赔额
	private String insurance_fee;//标准保费
	private String premiums;//保费
	private String remark;//备注
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInsurance_amout() {
		return insurance_amout;
	}
	public void setInsurance_amout(String insurance_amout) {
		this.insurance_amout = insurance_amout;
	}
	public String getUnit_count() {
		return unit_count;
	}
	public void setUnit_count(String unit_count) {
		this.unit_count = unit_count;
	}
	public String getUnit_amount() {
		return unit_amount;
	}
	public void setUnit_amount(String unit_amount) {
		this.unit_amount = unit_amount;
	}
	public String getNon_deductible() {
		return non_deductible;
	}
	public void setNon_deductible(String non_deductible) {
		this.non_deductible = non_deductible;
	}
	public String getDeductible_rate() {
		return deductible_rate;
	}
	public void setDeductible_rate(String deductible_rate) {
		this.deductible_rate = deductible_rate;
	}
	public String getDeductible_money() {
		return deductible_money;
	}
	public void setDeductible_money(String deductible_money) {
		this.deductible_money = deductible_money;
	}
	public String getInsurance_fee() {
		return insurance_fee;
	}
	public void setInsurance_fee(String insurance_fee) {
		this.insurance_fee = insurance_fee;
	}
	public String getPremiums() {
		return premiums;
	}
	public void setPremiums(String premiums) {
		this.premiums = premiums;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}


}
