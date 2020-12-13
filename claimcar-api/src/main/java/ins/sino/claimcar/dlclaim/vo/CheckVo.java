package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String check_start_time;//查勘开始时间
	private String check_end_time;//查勘结束时间
	private String check_addr;//查勘地点
	private String appoint_check_addr;//约定查勘地点
	private String loss_task;//损失任务
	private String is_appoint_driver;//是否约定驾驶员
	private String checker_name;//查勘人
	private String checker_code;//查勘人编码
	private String checker_phone1;//查勘人电话
	private String danger_address;//出险地点
	private String danger_cause;//出险原因
	private String danger_desc;//出险经过
	private String case_type;//案件类型
	private String event_type;//事故类型
	private String event_process_mode;//事故处理方式
	private String event_accident_liability;//事故责任划分
	private String event_duty_ratio;//事故责任比例
	private String insured_relation;//与被保险人关系
	private String no_fault_compensate;//无责代赔
	private String water_level;//水淹等级
	private String fall_victim_driver;//受害方与驾驶员关系
	private String fall_victim_insured;//受害方与被保险人关系
	private String subrogation_flag;//是否要求代位
	private String is_rescue;//是否需施救
	private String is_disassemble;//是否需拆解
	private String is_first_scene;//是否第一现场查勘
	private String is_person_injured;//是否包含人伤
	private String claim_type;//代赔类型
	private String sub_certi_type;//责任认定书类型
	private String catastrophe_name;//巨灾名称
	private String catastrophe_type;//巨灾类型
	private String catastrophe_code;//巨灾代码
	private String verifier_name;//检测审核人名称
	private String verifier_code;//检测审核人编码
	private String buckle_ded_reason;//加扣免赔原因
	private String is_mutual_collision_self_compensation;//是否为互碰自赔
	private String event_address_type;//道路类型
	private String driver_review;//证件审核
	private String is_car_insurance;//车辆信息是否与保单相符
	private String is_car_annual;//车辆是否年检期内
	private String loading_condition;//装载情况
	private String check_desc;//查勘意见
	private String check_type;//查勘类型
	private String audit_name;//查勘审核人名称
	private String audit_code;//查勘审核人编码
	private String audit_opinion;//查勘审核意见
	private String audit_time;//查勘审核时间
	private DriverVo driver;//查勘驾驶员集合
	private VehicleVo vehicle;//查勘车辆集合
	public String getCheck_start_time() {
		return check_start_time;
	}
	public void setCheck_start_time(String check_start_time) {
		this.check_start_time = check_start_time;
	}
	public String getCheck_end_time() {
		return check_end_time;
	}
	public void setCheck_end_time(String check_end_time) {
		this.check_end_time = check_end_time;
	}
	public String getCheck_addr() {
		return check_addr;
	}
	public void setCheck_addr(String check_addr) {
		this.check_addr = check_addr;
	}
	public String getAppoint_check_addr() {
		return appoint_check_addr;
	}
	public void setAppoint_check_addr(String appoint_check_addr) {
		this.appoint_check_addr = appoint_check_addr;
	}
	public String getLoss_task() {
		return loss_task;
	}
	public void setLoss_task(String loss_task) {
		this.loss_task = loss_task;
	}
	public String getIs_appoint_driver() {
		return is_appoint_driver;
	}
	public void setIs_appoint_driver(String is_appoint_driver) {
		this.is_appoint_driver = is_appoint_driver;
	}
	public String getChecker_name() {
		return checker_name;
	}
	public void setChecker_name(String checker_name) {
		this.checker_name = checker_name;
	}
	public String getChecker_code() {
		return checker_code;
	}
	public void setChecker_code(String checker_code) {
		this.checker_code = checker_code;
	}
	public String getChecker_phone1() {
		return checker_phone1;
	}
	public void setChecker_phone1(String checker_phone1) {
		this.checker_phone1 = checker_phone1;
	}
	public String getDanger_address() {
		return danger_address;
	}
	public void setDanger_address(String danger_address) {
		this.danger_address = danger_address;
	}
	public String getDanger_cause() {
		return danger_cause;
	}
	public void setDanger_cause(String danger_cause) {
		this.danger_cause = danger_cause;
	}
	public String getDanger_desc() {
		return danger_desc;
	}
	public void setDanger_desc(String danger_desc) {
		this.danger_desc = danger_desc;
	}
	public String getCase_type() {
		return case_type;
	}
	public void setCase_type(String case_type) {
		this.case_type = case_type;
	}
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	public String getEvent_process_mode() {
		return event_process_mode;
	}
	public void setEvent_process_mode(String event_process_mode) {
		this.event_process_mode = event_process_mode;
	}
	public String getEvent_accident_liability() {
		return event_accident_liability;
	}
	public void setEvent_accident_liability(String event_accident_liability) {
		this.event_accident_liability = event_accident_liability;
	}
	public String getEvent_duty_ratio() {
		return event_duty_ratio;
	}
	public void setEvent_duty_ratio(String event_duty_ratio) {
		this.event_duty_ratio = event_duty_ratio;
	}
	public String getInsured_relation() {
		return insured_relation;
	}
	public void setInsured_relation(String insured_relation) {
		this.insured_relation = insured_relation;
	}
	public String getNo_fault_compensate() {
		return no_fault_compensate;
	}
	public void setNo_fault_compensate(String no_fault_compensate) {
		this.no_fault_compensate = no_fault_compensate;
	}
	public String getWater_level() {
		return water_level;
	}
	public void setWater_level(String water_level) {
		this.water_level = water_level;
	}
	public String getFall_victim_driver() {
		return fall_victim_driver;
	}
	public void setFall_victim_driver(String fall_victim_driver) {
		this.fall_victim_driver = fall_victim_driver;
	}
	public String getFall_victim_insured() {
		return fall_victim_insured;
	}
	public void setFall_victim_insured(String fall_victim_insured) {
		this.fall_victim_insured = fall_victim_insured;
	}
	public String getSubrogation_flag() {
		return subrogation_flag;
	}
	public void setSubrogation_flag(String subrogation_flag) {
		this.subrogation_flag = subrogation_flag;
	}
	public String getIs_rescue() {
		return is_rescue;
	}
	public void setIs_rescue(String is_rescue) {
		this.is_rescue = is_rescue;
	}
	public String getIs_disassemble() {
		return is_disassemble;
	}
	public void setIs_disassemble(String is_disassemble) {
		this.is_disassemble = is_disassemble;
	}
	public String getIs_first_scene() {
		return is_first_scene;
	}
	public void setIs_first_scene(String is_first_scene) {
		this.is_first_scene = is_first_scene;
	}
	public String getIs_person_injured() {
		return is_person_injured;
	}
	public void setIs_person_injured(String is_person_injured) {
		this.is_person_injured = is_person_injured;
	}
	public String getClaim_type() {
		return claim_type;
	}
	public void setClaim_type(String claim_type) {
		this.claim_type = claim_type;
	}
	public String getSub_certi_type() {
		return sub_certi_type;
	}
	public void setSub_certi_type(String sub_certi_type) {
		this.sub_certi_type = sub_certi_type;
	}
	public String getCatastrophe_name() {
		return catastrophe_name;
	}
	public void setCatastrophe_name(String catastrophe_name) {
		this.catastrophe_name = catastrophe_name;
	}
	public String getCatastrophe_type() {
		return catastrophe_type;
	}
	public void setCatastrophe_type(String catastrophe_type) {
		this.catastrophe_type = catastrophe_type;
	}
	public String getCatastrophe_code() {
		return catastrophe_code;
	}
	public void setCatastrophe_code(String catastrophe_code) {
		this.catastrophe_code = catastrophe_code;
	}
	public String getVerifier_name() {
		return verifier_name;
	}
	public void setVerifier_name(String verifier_name) {
		this.verifier_name = verifier_name;
	}
	public String getVerifier_code() {
		return verifier_code;
	}
	public void setVerifier_code(String verifier_code) {
		this.verifier_code = verifier_code;
	}
	public String getBuckle_ded_reason() {
		return buckle_ded_reason;
	}
	public void setBuckle_ded_reason(String buckle_ded_reason) {
		this.buckle_ded_reason = buckle_ded_reason;
	}
	public String getIs_mutual_collision_self_compensation() {
		return is_mutual_collision_self_compensation;
	}
	public void setIs_mutual_collision_self_compensation(
			String is_mutual_collision_self_compensation) {
		this.is_mutual_collision_self_compensation = is_mutual_collision_self_compensation;
	}
	public String getEvent_address_type() {
		return event_address_type;
	}
	public void setEvent_address_type(String event_address_type) {
		this.event_address_type = event_address_type;
	}
	public String getDriver_review() {
		return driver_review;
	}
	public void setDriver_review(String driver_review) {
		this.driver_review = driver_review;
	}
	public String getIs_car_insurance() {
		return is_car_insurance;
	}
	public void setIs_car_insurance(String is_car_insurance) {
		this.is_car_insurance = is_car_insurance;
	}
	public String getIs_car_annual() {
		return is_car_annual;
	}
	public void setIs_car_annual(String is_car_annual) {
		this.is_car_annual = is_car_annual;
	}
	public String getLoading_condition() {
		return loading_condition;
	}
	public void setLoading_condition(String loading_condition) {
		this.loading_condition = loading_condition;
	}
	public String getCheck_desc() {
		return check_desc;
	}
	public void setCheck_desc(String check_desc) {
		this.check_desc = check_desc;
	}
	public String getCheck_type() {
		return check_type;
	}
	public void setCheck_type(String check_type) {
		this.check_type = check_type;
	}
	public String getAudit_name() {
		return audit_name;
	}
	public void setAudit_name(String audit_name) {
		this.audit_name = audit_name;
	}
	public String getAudit_code() {
		return audit_code;
	}
	public void setAudit_code(String audit_code) {
		this.audit_code = audit_code;
	}
	public String getAudit_opinion() {
		return audit_opinion;
	}
	public void setAudit_opinion(String audit_opinion) {
		this.audit_opinion = audit_opinion;
	}
	public String getAudit_time() {
		return audit_time;
	}
	public void setAudit_time(String audit_time) {
		this.audit_time = audit_time;
	}
	public DriverVo getDriver() {
		return driver;
	}
	public void setDriver(DriverVo driver) {
		this.driver = driver;
	}
	public VehicleVo getVehicle() {
		return vehicle;
	}
	public void setVehicle(VehicleVo vehicle) {
		this.vehicle = vehicle;
	}
	
  }
