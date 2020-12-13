package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleVo implements Serializable{

	private static final long serialVersionUID = 1L;
    
	private String subject_third;//是否标的
	private String license_plate_no;//车牌号码
	private String hkmac_license_plate_no;//粤港澳车牌号码
	private String owner;//车主
	private String bz_policy_no;//交强险保单号
	private String bz_company_code;//交强险承保公司
	private String sub_company_name;//承保分公司名称
	private String model_code;//车辆型号
	private String model_name;//车型名称
	private String vin;//车架号
	private String car_kind_code;//车辆种类
	private String veh_type_code;//车身大类
	private String body_type_code;//车身类型
	private String brand_model;//厂牌型号
	private String license_plate_type;//号牌种类
	private String engine_no;//发动机号
	private String plate_color;//号牌底色
	private String seat_count;//核定载客
	private String vehicle_color;//车身颜色
	private String is_modification;//是否改装或加装
	private String driving_license_address;//行驶本地址
	private String check_num;//查勘次数
	private String reserve_estimate_address;//预约定损地点类型
	private String new_vehicle_price;//新车购置价
	private String is_loss;//有无损失
	private String is_total_loss;//是否全损车
	private String loss_degree;//损失程度
	private String loss_part_data;//损失部位
	private String is_carrying_dangerous_goods;//是否运载危险品
	private String has_car_license;//是否已上牌
	private String is_person_injured;//是否包含人伤
	private String is_protect_loss;//是否包含财损
	private String using_properties;//使用性质
	private String rescue_company;//施救单位
	private String rescue_way;//施救方式
	private String rescue_traffic;//施救路况
	private String rescue_distance;//施救距离（千米）
	private String specific_factor;//特殊因素
	private String rescue_amount;//施救金额
	private String estimated_loss_amount;//估损金额
	private String tonnage;//整备质量
	private String vehicle_price;//实际价值
	private String vehicle_brand_code;//车辆品牌
	private String vehicle_series_code;//车系
	private String group_veh_code;//车组;
	private String used_years;//使用已年限
	private String remark;//备注
	public String getSubject_third() {
		return subject_third;
	}
	public void setSubject_third(String subject_third) {
		this.subject_third = subject_third;
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
	public String getBz_policy_no() {
		return bz_policy_no;
	}
	public void setBz_policy_no(String bz_policy_no) {
		this.bz_policy_no = bz_policy_no;
	}
	public String getBz_company_code() {
		return bz_company_code;
	}
	public void setBz_company_code(String bz_company_code) {
		this.bz_company_code = bz_company_code;
	}
	public String getSub_company_name() {
		return sub_company_name;
	}
	public void setSub_company_name(String sub_company_name) {
		this.sub_company_name = sub_company_name;
	}
	public String getModel_code() {
		return model_code;
	}
	public void setModel_code(String model_code) {
		this.model_code = model_code;
	}
	public String getModel_name() {
		return model_name;
	}
	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
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
	public String getBrand_model() {
		return brand_model;
	}
	public void setBrand_model(String brand_model) {
		this.brand_model = brand_model;
	}
	public String getLicense_plate_type() {
		return license_plate_type;
	}
	public void setLicense_plate_type(String license_plate_type) {
		this.license_plate_type = license_plate_type;
	}
	public String getEngine_no() {
		return engine_no;
	}
	public void setEngine_no(String engine_no) {
		this.engine_no = engine_no;
	}
	public String getPlate_color() {
		return plate_color;
	}
	public void setPlate_color(String plate_color) {
		this.plate_color = plate_color;
	}
	public String getSeat_count() {
		return seat_count;
	}
	public void setSeat_count(String seat_count) {
		this.seat_count = seat_count;
	}
	public String getVehicle_color() {
		return vehicle_color;
	}
	public void setVehicle_color(String vehicle_color) {
		this.vehicle_color = vehicle_color;
	}
	public String getIs_modification() {
		return is_modification;
	}
	public void setIs_modification(String is_modification) {
		this.is_modification = is_modification;
	}
	public String getDriving_license_address() {
		return driving_license_address;
	}
	public void setDriving_license_address(String driving_license_address) {
		this.driving_license_address = driving_license_address;
	}
	public String getCheck_num() {
		return check_num;
	}
	public void setCheck_num(String check_num) {
		this.check_num = check_num;
	}
	public String getReserve_estimate_address() {
		return reserve_estimate_address;
	}
	public void setReserve_estimate_address(String reserve_estimate_address) {
		this.reserve_estimate_address = reserve_estimate_address;
	}
	public String getNew_vehicle_price() {
		return new_vehicle_price;
	}
	public void setNew_vehicle_price(String new_vehicle_price) {
		this.new_vehicle_price = new_vehicle_price;
	}
	public String getIs_loss() {
		return is_loss;
	}
	public void setIs_loss(String is_loss) {
		this.is_loss = is_loss;
	}
	public String getIs_total_loss() {
		return is_total_loss;
	}
	public void setIs_total_loss(String is_total_loss) {
		this.is_total_loss = is_total_loss;
	}
	public String getLoss_degree() {
		return loss_degree;
	}
	public void setLoss_degree(String loss_degree) {
		this.loss_degree = loss_degree;
	}
	public String getLoss_part_data() {
		return loss_part_data;
	}
	public void setLoss_part_data(String loss_part_data) {
		this.loss_part_data = loss_part_data;
	}
	public String getIs_carrying_dangerous_goods() {
		return is_carrying_dangerous_goods;
	}
	public void setIs_carrying_dangerous_goods(String is_carrying_dangerous_goods) {
		this.is_carrying_dangerous_goods = is_carrying_dangerous_goods;
	}
	public String getHas_car_license() {
		return has_car_license;
	}
	public void setHas_car_license(String has_car_license) {
		this.has_car_license = has_car_license;
	}
	public String getIs_person_injured() {
		return is_person_injured;
	}
	public void setIs_person_injured(String is_person_injured) {
		this.is_person_injured = is_person_injured;
	}
	public String getIs_protect_loss() {
		return is_protect_loss;
	}
	public void setIs_protect_loss(String is_protect_loss) {
		this.is_protect_loss = is_protect_loss;
	}
	public String getUsing_properties() {
		return using_properties;
	}
	public void setUsing_properties(String using_properties) {
		this.using_properties = using_properties;
	}
	public String getRescue_company() {
		return rescue_company;
	}
	public void setRescue_company(String rescue_company) {
		this.rescue_company = rescue_company;
	}
	public String getRescue_way() {
		return rescue_way;
	}
	public void setRescue_way(String rescue_way) {
		this.rescue_way = rescue_way;
	}
	public String getRescue_traffic() {
		return rescue_traffic;
	}
	public void setRescue_traffic(String rescue_traffic) {
		this.rescue_traffic = rescue_traffic;
	}
	public String getRescue_distance() {
		return rescue_distance;
	}
	public void setRescue_distance(String rescue_distance) {
		this.rescue_distance = rescue_distance;
	}
	public String getSpecific_factor() {
		return specific_factor;
	}
	public void setSpecific_factor(String specific_factor) {
		this.specific_factor = specific_factor;
	}
	public String getRescue_amount() {
		return rescue_amount;
	}
	public void setRescue_amount(String rescue_amount) {
		this.rescue_amount = rescue_amount;
	}
	public String getEstimated_loss_amount() {
		return estimated_loss_amount;
	}
	public void setEstimated_loss_amount(String estimated_loss_amount) {
		this.estimated_loss_amount = estimated_loss_amount;
	}
	
	
	public String getTonnage() {
		return tonnage;
	}
	public void setTonnage(String tonnage) {
		this.tonnage = tonnage;
	}
	public String getVehicle_price() {
		return vehicle_price;
	}
	public void setVehicle_price(String vehicle_price) {
		this.vehicle_price = vehicle_price;
	}
	public String getVehicle_brand_code() {
		return vehicle_brand_code;
	}
	public void setVehicle_brand_code(String vehicle_brand_code) {
		this.vehicle_brand_code = vehicle_brand_code;
	}
	public String getVehicle_series_code() {
		return vehicle_series_code;
	}
	public void setVehicle_series_code(String vehicle_series_code) {
		this.vehicle_series_code = vehicle_series_code;
	}
	public String getGroup_veh_code() {
		return group_veh_code;
	}
	public void setGroup_veh_code(String group_veh_code) {
		this.group_veh_code = group_veh_code;
	}
	public String getUsed_years() {
		return used_years;
	}
	public void setUsed_years(String used_years) {
		this.used_years = used_years;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
		
		
		
		
		
		
}
