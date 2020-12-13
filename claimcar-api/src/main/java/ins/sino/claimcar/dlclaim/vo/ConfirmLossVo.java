package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfirmLossVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String estimate_task_no;//保险公司任务号
	private String add_count;//追加次数
	private String vehicle_color;//车身颜色
	private String vin;//车架号
	private String first_registration_date;//初登日期
	private String using_properties;//使用性质
	private String seats;//座位数
	private String vehicle_price;//实际价值
	private String new_vehicle_price;//新车购置价
	private String tonnage;//整备质量
	private String used_years;//车辆已使用年限
	private String car_affiliation;//车辆归属
	private String water_level;//水淹等级
	private String license_plate_type;//号牌种类
	private String model_code;//车型代码
	private String model_name;//车型名称
	private String driving_mileage;//行驶里程
	private String vehicle_series_name;//车系
	private String group_veh_name;//车组
	private String manu_name;//厂商名称
	private String transmission_code;//变速器类型
	private String transmission_name;//变速器类型名称
	private String driver_name;//驾驶员姓名
	private String driver_age;//驾驶员年龄
	private String driver_phone;//驾驶员联系电话
	private String driver_sex;//驾驶员性别
	private String driving_identify_type;//驾驶员证件类型
	private String allow_driving_vehicle;//驾驶证准驾车型
	private String informant_is_driver;//报案人是否驾驶员
	private String driving_license_date;//初次领证日期
	private String driver_license_institution;//颁证机关
	private String driving_license_archives_no;//驾驶证档案编码
	private String driving_validity;//驾驶证有效期
	private String estimate_start_time;//定损开始时间
	private String estimate_end_time;//定损结束时间
	private String under_start_Time;//核损开始时间
	private String under_end_Time;//核损结束时间
	private String estimate_address_type;//定损地点类型
	private String loss_task;//损失任务
	private String estimate_mode;//定损方式
	private String estimate_address;//定损地点
	private String repair_factory_code;//修理厂代码
	private String repair_factory_name;//修理厂名称
	private String repair_factory_contact;//修理厂联系人
	private String repair_factory_category;//修理厂类别
	private String repair_factory_type;//修理厂类型
	private String repair_factory_phone;//修理厂电话
	private String repair_factory_grade;//修理厂评分
	private String repair_factory_address;//修理厂地址
	private String estimate_code;//定损员编码
	private String estimate_name;//定损员姓名
	private String estimate_phone;//定损员电话
	private String estimate_first_total;//保险公司首次定损总金额
	private String insurance_estimate_total;//保险公司定损总金额
	private String insurance_first_rescue_fee;//保险公司首次定损施救费
	private String insurance_rescue_fee;//保险公司施救费
	private String verify_price_code;//核价员编码
	private String verify_price_name;//核价员姓名
	private String verify_price_phone;//核价员电话
	private String verify_price_total;//核价总金额
	private String under_write_code;//核损员编码
	private String under_write_name;//核损员姓名
	private String under_write_phone;//核损员电话
	private String under_first_total;//保险公司首次核损总金额
	private String insurance_under_total;//保险公司核损总金额
	private String management_fee;//管理费
	private String management_fee_rate;//管理费率
	private String insurance_coverage_code;//定损险别（保险公司）
	private String compulsory_duty_type;//交强险责任类型
	private String subject_third;//是否标的
	private String estimate_count;//定损次数
	private String loss_part_data;//损失部位
	private List<SparepartVo> sparepart;//配件集合
	private List<LaborVo> Labor;//工时集合
	private List<SmallSparepartVo> smallSparepart;//辅料集合
	private List<ConfirmLossDiscussionVo> confirmLossDiscussion;//定核损意见集合
	private List<EstimateRepairVo> estimateRepair;//定损外修配件
	private VehicleVo vehicle;//定损车辆
	private DriverVo driver;//车辆驾驶员信息
	public String getEstimate_task_no() {
		return estimate_task_no;
	}
	public void setEstimate_task_no(String estimate_task_no) {
		this.estimate_task_no = estimate_task_no;
	}
	public String getAdd_count() {
		return add_count;
	}
	public void setAdd_count(String add_count) {
		this.add_count = add_count;
	}
	public String getVehicle_color() {
		return vehicle_color;
	}
	public void setVehicle_color(String vehicle_color) {
		this.vehicle_color = vehicle_color;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getFirst_registration_date() {
		return first_registration_date;
	}
	public void setFirst_registration_date(String first_registration_date) {
		this.first_registration_date = first_registration_date;
	}
	public String getUsing_properties() {
		return using_properties;
	}
	public void setUsing_properties(String using_properties) {
		this.using_properties = using_properties;
	}
	public String getSeats() {
		return seats;
	}
	public void setSeats(String seats) {
		this.seats = seats;
	}
	public String getVehicle_price() {
		return vehicle_price;
	}
	public void setVehicle_price(String vehicle_price) {
		this.vehicle_price = vehicle_price;
	}
	public String getNew_vehicle_price() {
		return new_vehicle_price;
	}
	public void setNew_vehicle_price(String new_vehicle_price) {
		this.new_vehicle_price = new_vehicle_price;
	}
	public String getTonnage() {
		return tonnage;
	}
	public void setTonnage(String tonnage) {
		this.tonnage = tonnage;
	}
	public String getUsed_years() {
		return used_years;
	}
	public void setUsed_years(String used_years) {
		this.used_years = used_years;
	}
	public String getCar_affiliation() {
		return car_affiliation;
	}
	public void setCar_affiliation(String car_affiliation) {
		this.car_affiliation = car_affiliation;
	}
	public String getWater_level() {
		return water_level;
	}
	public void setWater_level(String water_level) {
		this.water_level = water_level;
	}
	public String getLicense_plate_type() {
		return license_plate_type;
	}
	public void setLicense_plate_type(String license_plate_type) {
		this.license_plate_type = license_plate_type;
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
	public String getDriving_mileage() {
		return driving_mileage;
	}
	public void setDriving_mileage(String driving_mileage) {
		this.driving_mileage = driving_mileage;
	}
	public String getVehicle_series_name() {
		return vehicle_series_name;
	}
	public void setVehicle_series_name(String vehicle_series_name) {
		this.vehicle_series_name = vehicle_series_name;
	}
	public String getGroup_veh_name() {
		return group_veh_name;
	}
	public void setGroup_veh_name(String group_veh_name) {
		this.group_veh_name = group_veh_name;
	}
	public String getManu_name() {
		return manu_name;
	}
	public void setManu_name(String manu_name) {
		this.manu_name = manu_name;
	}
	public String getTransmission_code() {
		return transmission_code;
	}
	public void setTransmission_code(String transmission_code) {
		this.transmission_code = transmission_code;
	}
	public String getTransmission_name() {
		return transmission_name;
	}
	public void setTransmission_name(String transmission_name) {
		this.transmission_name = transmission_name;
	}
	public String getDriver_name() {
		return driver_name;
	}
	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}
	public String getDriver_age() {
		return driver_age;
	}
	public void setDriver_age(String driver_age) {
		this.driver_age = driver_age;
	}
	public String getDriver_phone() {
		return driver_phone;
	}
	public void setDriver_phone(String driver_phone) {
		this.driver_phone = driver_phone;
	}
	public String getDriver_sex() {
		return driver_sex;
	}
	public void setDriver_sex(String driver_sex) {
		this.driver_sex = driver_sex;
	}
	public String getDriving_identify_type() {
		return driving_identify_type;
	}
	public void setDriving_identify_type(String driving_identify_type) {
		this.driving_identify_type = driving_identify_type;
	}
	public String getAllow_driving_vehicle() {
		return allow_driving_vehicle;
	}
	public void setAllow_driving_vehicle(String allow_driving_vehicle) {
		this.allow_driving_vehicle = allow_driving_vehicle;
	}
	public String getInformant_is_driver() {
		return informant_is_driver;
	}
	public void setInformant_is_driver(String informant_is_driver) {
		this.informant_is_driver = informant_is_driver;
	}
	public String getDriving_license_date() {
		return driving_license_date;
	}
	public void setDriving_license_date(String driving_license_date) {
		this.driving_license_date = driving_license_date;
	}
	public String getDriver_license_institution() {
		return driver_license_institution;
	}
	public void setDriver_license_institution(String driver_license_institution) {
		this.driver_license_institution = driver_license_institution;
	}
	public String getDriving_license_archives_no() {
		return driving_license_archives_no;
	}
	public void setDriving_license_archives_no(String driving_license_archives_no) {
		this.driving_license_archives_no = driving_license_archives_no;
	}
	public String getDriving_validity() {
		return driving_validity;
	}
	public void setDriving_validity(String driving_validity) {
		this.driving_validity = driving_validity;
	}
	public String getEstimate_start_time() {
		return estimate_start_time;
	}
	public void setEstimate_start_time(String estimate_start_time) {
		this.estimate_start_time = estimate_start_time;
	}
	public String getEstimate_end_time() {
		return estimate_end_time;
	}
	public void setEstimate_end_time(String estimate_end_time) {
		this.estimate_end_time = estimate_end_time;
	}
	public String getUnder_start_Time() {
		return under_start_Time;
	}
	public void setUnder_start_Time(String under_start_Time) {
		this.under_start_Time = under_start_Time;
	}
	public String getUnder_end_Time() {
		return under_end_Time;
	}
	public void setUnder_end_Time(String under_end_Time) {
		this.under_end_Time = under_end_Time;
	}
	public String getEstimate_address_type() {
		return estimate_address_type;
	}
	public void setEstimate_address_type(String estimate_address_type) {
		this.estimate_address_type = estimate_address_type;
	}
	public String getLoss_task() {
		return loss_task;
	}
	public void setLoss_task(String loss_task) {
		this.loss_task = loss_task;
	}
	public String getEstimate_mode() {
		return estimate_mode;
	}
	public void setEstimate_mode(String estimate_mode) {
		this.estimate_mode = estimate_mode;
	}
	public String getEstimate_address() {
		return estimate_address;
	}
	public void setEstimate_address(String estimate_address) {
		this.estimate_address = estimate_address;
	}
	public String getRepair_factory_code() {
		return repair_factory_code;
	}
	public void setRepair_factory_code(String repair_factory_code) {
		this.repair_factory_code = repair_factory_code;
	}
	public String getRepair_factory_name() {
		return repair_factory_name;
	}
	public void setRepair_factory_name(String repair_factory_name) {
		this.repair_factory_name = repair_factory_name;
	}
	public String getRepair_factory_contact() {
		return repair_factory_contact;
	}
	public void setRepair_factory_contact(String repair_factory_contact) {
		this.repair_factory_contact = repair_factory_contact;
	}
	public String getRepair_factory_category() {
		return repair_factory_category;
	}
	public void setRepair_factory_category(String repair_factory_category) {
		this.repair_factory_category = repair_factory_category;
	}
	public String getRepair_factory_type() {
		return repair_factory_type;
	}
	public void setRepair_factory_type(String repair_factory_type) {
		this.repair_factory_type = repair_factory_type;
	}
	public String getRepair_factory_phone() {
		return repair_factory_phone;
	}
	public void setRepair_factory_phone(String repair_factory_phone) {
		this.repair_factory_phone = repair_factory_phone;
	}
	public String getRepair_factory_grade() {
		return repair_factory_grade;
	}
	public void setRepair_factory_grade(String repair_factory_grade) {
		this.repair_factory_grade = repair_factory_grade;
	}
	public String getRepair_factory_address() {
		return repair_factory_address;
	}
	public void setRepair_factory_address(String repair_factory_address) {
		this.repair_factory_address = repair_factory_address;
	}
	public String getEstimate_code() {
		return estimate_code;
	}
	public void setEstimate_code(String estimate_code) {
		this.estimate_code = estimate_code;
	}
	public String getEstimate_name() {
		return estimate_name;
	}
	public void setEstimate_name(String estimate_name) {
		this.estimate_name = estimate_name;
	}
	public String getEstimate_phone() {
		return estimate_phone;
	}
	public void setEstimate_phone(String estimate_phone) {
		this.estimate_phone = estimate_phone;
	}
	public String getEstimate_first_total() {
		return estimate_first_total;
	}
	public void setEstimate_first_total(String estimate_first_total) {
		this.estimate_first_total = estimate_first_total;
	}
	public String getInsurance_estimate_total() {
		return insurance_estimate_total;
	}
	public void setInsurance_estimate_total(String insurance_estimate_total) {
		this.insurance_estimate_total = insurance_estimate_total;
	}
	public String getInsurance_first_rescue_fee() {
		return insurance_first_rescue_fee;
	}
	public void setInsurance_first_rescue_fee(String insurance_first_rescue_fee) {
		this.insurance_first_rescue_fee = insurance_first_rescue_fee;
	}
	public String getInsurance_rescue_fee() {
		return insurance_rescue_fee;
	}
	public void setInsurance_rescue_fee(String insurance_rescue_fee) {
		this.insurance_rescue_fee = insurance_rescue_fee;
	}
	public String getVerify_price_code() {
		return verify_price_code;
	}
	public void setVerify_price_code(String verify_price_code) {
		this.verify_price_code = verify_price_code;
	}
	public String getVerify_price_name() {
		return verify_price_name;
	}
	public void setVerify_price_name(String verify_price_name) {
		this.verify_price_name = verify_price_name;
	}
	public String getVerify_price_phone() {
		return verify_price_phone;
	}
	public void setVerify_price_phone(String verify_price_phone) {
		this.verify_price_phone = verify_price_phone;
	}
	public String getVerify_price_total() {
		return verify_price_total;
	}
	public void setVerify_price_total(String verify_price_total) {
		this.verify_price_total = verify_price_total;
	}
	public String getUnder_write_code() {
		return under_write_code;
	}
	public void setUnder_write_code(String under_write_code) {
		this.under_write_code = under_write_code;
	}
	public String getUnder_write_name() {
		return under_write_name;
	}
	public void setUnder_write_name(String under_write_name) {
		this.under_write_name = under_write_name;
	}
	public String getUnder_write_phone() {
		return under_write_phone;
	}
	public void setUnder_write_phone(String under_write_phone) {
		this.under_write_phone = under_write_phone;
	}
	public String getUnder_first_total() {
		return under_first_total;
	}
	public void setUnder_first_total(String under_first_total) {
		this.under_first_total = under_first_total;
	}
	public String getInsurance_under_total() {
		return insurance_under_total;
	}
	public void setInsurance_under_total(String insurance_under_total) {
		this.insurance_under_total = insurance_under_total;
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
	public String getInsurance_coverage_code() {
		return insurance_coverage_code;
	}
	public void setInsurance_coverage_code(String insurance_coverage_code) {
		this.insurance_coverage_code = insurance_coverage_code;
	}
	public String getCompulsory_duty_type() {
		return compulsory_duty_type;
	}
	public void setCompulsory_duty_type(String compulsory_duty_type) {
		this.compulsory_duty_type = compulsory_duty_type;
	}
	public List<SparepartVo> getSparepart() {
		return sparepart;
	}
	public void setSparepart(List<SparepartVo> sparepart) {
		this.sparepart = sparepart;
	}
	public List<LaborVo> getLabor() {
		return Labor;
	}
	public void setLabor(List<LaborVo> labor) {
		Labor = labor;
	}
	public List<SmallSparepartVo> getSmallSparepart() {
		return smallSparepart;
	}
	public void setSmallSparepart(List<SmallSparepartVo> smallSparepart) {
		this.smallSparepart = smallSparepart;
	}
	public List<ConfirmLossDiscussionVo> getConfirmLossDiscussion() {
		return confirmLossDiscussion;
	}
	public void setConfirmLossDiscussion(
			List<ConfirmLossDiscussionVo> confirmLossDiscussion) {
		this.confirmLossDiscussion = confirmLossDiscussion;
	}
	public VehicleVo getVehicle() {
		return vehicle;
	}
	public void setVehicle(VehicleVo vehicle) {
		this.vehicle = vehicle;
	}
	public String getSubject_third() {
		return subject_third;
	}
	public void setSubject_third(String subject_third) {
		this.subject_third = subject_third;
	}
	public String getEstimate_count() {
		return estimate_count;
	}
	public void setEstimate_count(String estimate_count) {
		this.estimate_count = estimate_count;
	}
	public String getLoss_part_data() {
		return loss_part_data;
	}
	public void setLoss_part_data(String loss_part_data) {
		this.loss_part_data = loss_part_data;
	}
	public DriverVo getDriver() {
		return driver;
	}
	public void setDriver(DriverVo driver) {
		this.driver = driver;
	}
	public List<EstimateRepairVo> getEstimateRepair() {
		return estimateRepair;
	}
	public void setEstimateRepair(List<EstimateRepairVo> estimateRepair) {
		this.estimateRepair = estimateRepair;
	}
	
	
	

}
