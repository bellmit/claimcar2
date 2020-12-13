package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolicyVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String policy_no;//保单号
	private String company_code;//	保险公司
	private String company_id;//承保公司ID
	private String company_name;//承保保险公司名称
	private String plate_color;//号牌底色
	private String appoint_driver1;//承保指定驾驶员1
	private String appoint_driver2;//承保指定驾驶员2
	private String insurance_category;//险种
	private String insurance_standard_fee;//标准保费
	private String insurance_fee;//总保费
	private String insurance_amount;//总保额
	private String start_date;//起保日期
	private String end_date;//终保日期
	private String sign_date;//签单日期
	private String first_registration_date;//初登日期
	private String tonnage;//整备质量
	private String discount;//折扣率
	private String new_vehicle_price;//新车购置价
	private String vehicle_price;//实际价值
	private String nature;//所属性质
	private String is_continuous_policy;//是否续保业务
	private String is_transfer;//是否过户车
	private String using_properties;//使用性质
	private String paid_times;//赔款次数
	private String paid_total;//赔款总计
	private String driving_mileage;//行驶里程
	private String engine_no;//发动机号
	private String seats_qualities;//核定载客载质量（人/千克）
	private String event_count;//出险次数
	private String issue_company_code;//出单机构编码
	private String issue_company_name;//出单机构名称
	private String issue_company_address;//出单机构地址
	private String model_name;//车型名称
	private String model_code;//车辆型号
	private String brand_model;//厂牌型号
	private String vehicle_color;//车身颜色
	private String is_employee_car;//是否员工车
	private String license_plate_no;//车牌号
	private String hkmac_license_plate_no;//港澳号码
	private String car_kind_code;//车辆种类
	private String veh_type_code;//车身大类
	private String body_type_code;//车身类型
	private String used_years;//车辆已使用年限
	private String import_domestic;//进口/国产车
	private String vehicle_frame_no;//车架号
	private String insured_gender;//被保险人性别
	private String insured_identify_type;//被保险人证件类型
	private String insured_unit_nature;//被保险人单位性质
	private String remark;//备注
	private String agency_name;//代理人/公司名称
	private String insured_identify_no;//被保险人证件号码
	private String insured_name;//被保险人
	
	private List<InsuranceItemVo> insuranceItem;//保单险别集合
	private List<InsuranceModificationVo> insuranceModification;//保单批单集合
	private List<HistoricalClaimVo> historicalClaim;//保单历史出险信息集合
	public String getPolicy_no() {
		return policy_no;
	}
	public void setPolicy_no(String policy_no) {
		this.policy_no = policy_no;
	}
	public String getCompany_code() {
		return company_code;
	}
	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getPlate_color() {
		return plate_color;
	}
	public void setPlate_color(String plate_color) {
		this.plate_color = plate_color;
	}
	public String getAppoint_driver1() {
		return appoint_driver1;
	}
	public void setAppoint_driver1(String appoint_driver1) {
		this.appoint_driver1 = appoint_driver1;
	}
	public String getAppoint_driver2() {
		return appoint_driver2;
	}
	public void setAppoint_driver2(String appoint_driver2) {
		this.appoint_driver2 = appoint_driver2;
	}
	public String getInsurance_category() {
		return insurance_category;
	}
	public void setInsurance_category(String insurance_category) {
		this.insurance_category = insurance_category;
	}
	public String getInsurance_standard_fee() {
		return insurance_standard_fee;
	}
	public void setInsurance_standard_fee(String insurance_standard_fee) {
		this.insurance_standard_fee = insurance_standard_fee;
	}
	public String getInsurance_fee() {
		return insurance_fee;
	}
	public void setInsurance_fee(String insurance_fee) {
		this.insurance_fee = insurance_fee;
	}
	public String getInsurance_amount() {
		return insurance_amount;
	}
	public void setInsurance_amount(String insurance_amount) {
		this.insurance_amount = insurance_amount;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getSign_date() {
		return sign_date;
	}
	public void setSign_date(String sign_date) {
		this.sign_date = sign_date;
	}
	public String getFirst_registration_date() {
		return first_registration_date;
	}
	public void setFirst_registration_date(String first_registration_date) {
		this.first_registration_date = first_registration_date;
	}
	public String getTonnage() {
		return tonnage;
	}
	public void setTonnage(String tonnage) {
		this.tonnage = tonnage;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getNew_vehicle_price() {
		return new_vehicle_price;
	}
	public void setNew_vehicle_price(String new_vehicle_price) {
		this.new_vehicle_price = new_vehicle_price;
	}
	public String getVehicle_price() {
		return vehicle_price;
	}
	public void setVehicle_price(String vehicle_price) {
		this.vehicle_price = vehicle_price;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
	public String getIs_continuous_policy() {
		return is_continuous_policy;
	}
	public void setIs_continuous_policy(String is_continuous_policy) {
		this.is_continuous_policy = is_continuous_policy;
	}
	public String getIs_transfer() {
		return is_transfer;
	}
	public void setIs_transfer(String is_transfer) {
		this.is_transfer = is_transfer;
	}
	public String getUsing_properties() {
		return using_properties;
	}
	public void setUsing_properties(String using_properties) {
		this.using_properties = using_properties;
	}
	public String getPaid_times() {
		return paid_times;
	}
	public void setPaid_times(String paid_times) {
		this.paid_times = paid_times;
	}
	public String getPaid_total() {
		return paid_total;
	}
	public void setPaid_total(String paid_total) {
		this.paid_total = paid_total;
	}
	public String getDriving_mileage() {
		return driving_mileage;
	}
	public void setDriving_mileage(String driving_mileage) {
		this.driving_mileage = driving_mileage;
	}
	public String getEngine_no() {
		return engine_no;
	}
	public void setEngine_no(String engine_no) {
		this.engine_no = engine_no;
	}
	public String getSeats_qualities() {
		return seats_qualities;
	}
	public void setSeats_qualities(String seats_qualities) {
		this.seats_qualities = seats_qualities;
	}
	public String getEvent_count() {
		return event_count;
	}
	public void setEvent_count(String event_count) {
		this.event_count = event_count;
	}
	public String getIssue_company_code() {
		return issue_company_code;
	}
	public void setIssue_company_code(String issue_company_code) {
		this.issue_company_code = issue_company_code;
	}
	public String getIssue_company_name() {
		return issue_company_name;
	}
	public void setIssue_company_name(String issue_company_name) {
		this.issue_company_name = issue_company_name;
	}
	public String getIssue_company_address() {
		return issue_company_address;
	}
	public void setIssue_company_address(String issue_company_address) {
		this.issue_company_address = issue_company_address;
	}
	public String getModel_name() {
		return model_name;
	}
	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}
	public String getModel_code() {
		return model_code;
	}
	public void setModel_code(String model_code) {
		this.model_code = model_code;
	}
	public String getBrand_model() {
		return brand_model;
	}
	public void setBrand_model(String brand_model) {
		this.brand_model = brand_model;
	}
	public String getVehicle_color() {
		return vehicle_color;
	}
	public void setVehicle_color(String vehicle_color) {
		this.vehicle_color = vehicle_color;
	}
	public String getIs_employee_car() {
		return is_employee_car;
	}
	public void setIs_employee_car(String is_employee_car) {
		this.is_employee_car = is_employee_car;
	}
	public String getLicense_plate_no() {
		return license_plate_no;
	}
	public void setLicense_plate_no(String license_plate_no) {
		this.license_plate_no = license_plate_no;
	}
	public String getHkmac_license_plate_no() {
		return hkmac_license_plate_no;
	}
	public void setHkmac_license_plate_no(String hkmac_license_plate_no) {
		this.hkmac_license_plate_no = hkmac_license_plate_no;
	}
	public String getCar_kind_code() {
		return car_kind_code;
	}
	public void setCar_kind_code(String car_kind_code) {
		this.car_kind_code = car_kind_code;
	}
	public String getVeh_type_code() {
		return veh_type_code;
	}
	public void setVeh_type_code(String veh_type_code) {
		this.veh_type_code = veh_type_code;
	}
	public String getBody_type_code() {
		return body_type_code;
	}
	public void setBody_type_code(String body_type_code) {
		this.body_type_code = body_type_code;
	}
	public String getUsed_years() {
		return used_years;
	}
	public void setUsed_years(String used_years) {
		this.used_years = used_years;
	}
	public String getImport_domestic() {
		return import_domestic;
	}
	public void setImport_domestic(String import_domestic) {
		this.import_domestic = import_domestic;
	}
	public String getVehicle_frame_no() {
		return vehicle_frame_no;
	}
	public void setVehicle_frame_no(String vehicle_frame_no) {
		this.vehicle_frame_no = vehicle_frame_no;
	}
	public String getInsured_gender() {
		return insured_gender;
	}
	public void setInsured_gender(String insured_gender) {
		this.insured_gender = insured_gender;
	}
	public String getInsured_identify_type() {
		return insured_identify_type;
	}
	public void setInsured_identify_type(String insured_identify_type) {
		this.insured_identify_type = insured_identify_type;
	}
	public String getInsured_unit_nature() {
		return insured_unit_nature;
	}
	public void setInsured_unit_nature(String insured_unit_nature) {
		this.insured_unit_nature = insured_unit_nature;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAgency_name() {
		return agency_name;
	}
	public void setAgency_name(String agency_name) {
		this.agency_name = agency_name;
	}
	public List<InsuranceItemVo> getInsuranceItem() {
		return insuranceItem;
	}
	public void setInsuranceItemVos(List<InsuranceItemVo> insuranceItem) {
		this.insuranceItem = insuranceItem;
	}
	public List<InsuranceModificationVo> getInsuranceModification() {
		return insuranceModification;
	}
	public void setInsuranceModification(
			List<InsuranceModificationVo> insuranceModification) {
		this.insuranceModification = insuranceModification;
	}
	public List<HistoricalClaimVo> getHistoricalClaim() {
		return historicalClaim;
	}
	public void setHistoricalClaimVos(List<HistoricalClaimVo> historicalClaim) {
		this.historicalClaim = historicalClaim;
	}
	public String getInsured_identify_no() {
		return insured_identify_no;
	}
	public void setInsured_identify_no(String insured_identify_no) {
		this.insured_identify_no = insured_identify_no;
	}
	public String getInsured_name() {
		return insured_name;
	}
	public void setInsured_name(String insured_name) {
		this.insured_name = insured_name;
	}
	
		
		
		
		
		


}
