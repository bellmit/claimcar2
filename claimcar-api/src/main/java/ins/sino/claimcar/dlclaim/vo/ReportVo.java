package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String case_status;//案件状态
	private String cancel_reason;//报案注销原因
	private String cancel_date;//报案注销日期
	private String weather;//天气
	private String hurt_num;//受伤人数
	private String accept_ind;//受理标志
	private String is_person_injured;//是否有物损
	private String is_protect_loss;//是否有人伤
	private String is_outof_local_claim;//是否异地
	private String subrogation_flag;//是否要求代位
	private String is_closed;//是否结案
	private String is_simple_claim;//是否简易案件
	private String first_scene_ind;//是否第一现场
	private String accident_liability;//事故责任
	private String event_reason_type;//事故原因
	private String event_process_mode;//事故处理方式
	private String is_police;//是否已报交警
	private String driver_gender;//驾驶员性别
	private String is_mutual_collision_self_compensation;//互碰自赔
	private String event_address_type;//道路类型
	private String danger_cause;//出险原因
	private String danger_address;//出险所在地
	private String danger_time;//出险时间
	private String danger_area;//出险区域
	private String danger_desc;//出险经过
	private String danger_address_type;//出险地点分类
	private String remark;//备注
	private String report_type;//报案形式
	private String notification_time;//报案日期
	private String informant_is_driver;//报案人是否驾驶员
	private String informant_is_nsured;//报案人是否被保险人
	private String report_province;//报案所在省
	private String report_city;//报案所在市
	private String report_district;//报案所在区/县
	private String report_address;//报案详细地址
	private String status;//案件状态
	private String lindicator;//代赔类型
	private String catastrophe_name;//巨灾名称
	private String catastrophe_type;//巨灾类型
	private String catastrophe_code;//巨灾代码
	private String is_subject_normal;//标的车是否可正常行驶
	private String is_rescue;//是否进行相关施救
	private String is_self_survey;//是否自助查勘
	private String is_payment;//是否垫付
	private String is_driver_locale;//驾驶员是否在现场
	private String is_main_report;//是否主挂车补报案
	private String is_locale_survey;//客户是否要求第一现场查勘
	private String replace_survey_mark;//代勘标识
	private String is_edit_report;//是否补报案
	private String is_insured_phone;//是否提供被保险人电话
	private String is_take_effect;//是否即时生效期出险
	private String driver_name;//驾驶员姓名
	private String reporter;//报案人姓名
	private String reporter_phone;//报案人电话
	public String getCase_status() {
		return case_status;
	}
	public void setCase_status(String case_status) {
		this.case_status = case_status;
	}
	public String getCancel_reason() {
		return cancel_reason;
	}
	public void setCancel_reason(String cancel_reason) {
		this.cancel_reason = cancel_reason;
	}
	public String getCancel_date() {
		return cancel_date;
	}
	public void setCancel_date(String cancel_date) {
		this.cancel_date = cancel_date;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getHurt_num() {
		return hurt_num;
	}
	public void setHurt_num(String hurt_num) {
		this.hurt_num = hurt_num;
	}
	public String getAccept_ind() {
		return accept_ind;
	}
	public void setAccept_ind(String accept_ind) {
		this.accept_ind = accept_ind;
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
	public String getIs_outof_local_claim() {
		return is_outof_local_claim;
	}
	public void setIs_outof_local_claim(String is_outof_local_claim) {
		this.is_outof_local_claim = is_outof_local_claim;
	}
	public String getSubrogation_flag() {
		return subrogation_flag;
	}
	public void setSubrogation_flag(String subrogation_flag) {
		this.subrogation_flag = subrogation_flag;
	}
	public String getIs_closed() {
		return is_closed;
	}
	public void setIs_closed(String is_closed) {
		this.is_closed = is_closed;
	}
	public String getIs_simple_claim() {
		return is_simple_claim;
	}
	public void setIs_simple_claim(String is_simple_claim) {
		this.is_simple_claim = is_simple_claim;
	}
	public String getFirst_scene_ind() {
		return first_scene_ind;
	}
	public void setFirst_scene_ind(String first_scene_ind) {
		this.first_scene_ind = first_scene_ind;
	}
	public String getAccident_liability() {
		return accident_liability;
	}
	public void setAccident_liability(String accident_liability) {
		this.accident_liability = accident_liability;
	}
	public String getEvent_reason_type() {
		return event_reason_type;
	}
	public void setEvent_reason_type(String event_reason_type) {
		this.event_reason_type = event_reason_type;
	}
	public String getEvent_process_mode() {
		return event_process_mode;
	}
	public void setEvent_process_mode(String event_process_mode) {
		this.event_process_mode = event_process_mode;
	}
	public String getIs_police() {
		return is_police;
	}
	public void setIs_police(String is_police) {
		this.is_police = is_police;
	}
	public String getDriver_gender() {
		return driver_gender;
	}
	public void setDriver_gender(String driver_gender) {
		this.driver_gender = driver_gender;
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
	public String getDanger_cause() {
		return danger_cause;
	}
	public void setDanger_cause(String danger_cause) {
		this.danger_cause = danger_cause;
	}
	public String getDanger_address() {
		return danger_address;
	}
	public void setDanger_address(String danger_address) {
		this.danger_address = danger_address;
	}
	public String getDanger_time() {
		return danger_time;
	}
	public void setDanger_time(String danger_time) {
		this.danger_time = danger_time;
	}
	public String getDanger_area() {
		return danger_area;
	}
	public void setDanger_area(String danger_area) {
		this.danger_area = danger_area;
	}
	public String getDanger_desc() {
		return danger_desc;
	}
	public void setDanger_desc(String danger_desc) {
		this.danger_desc = danger_desc;
	}
	public String getDanger_address_type() {
		return danger_address_type;
	}
	public void setDanger_address_type(String danger_address_type) {
		this.danger_address_type = danger_address_type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getReport_type() {
		return report_type;
	}
	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}
	public String getNotification_time() {
		return notification_time;
	}
	public void setNotification_time(String notification_time) {
		this.notification_time = notification_time;
	}
	public String getInformant_is_driver() {
		return informant_is_driver;
	}
	public void setInformant_is_driver(String informant_is_driver) {
		this.informant_is_driver = informant_is_driver;
	}
	public String getInformant_is_nsured() {
		return informant_is_nsured;
	}
	public void setInformant_is_nsured(String informant_is_nsured) {
		this.informant_is_nsured = informant_is_nsured;
	}
	public String getReport_province() {
		return report_province;
	}
	public void setReport_province(String report_province) {
		this.report_province = report_province;
	}
	public String getReport_city() {
		return report_city;
	}
	public void setReport_city(String report_city) {
		this.report_city = report_city;
	}
	public String getReport_district() {
		return report_district;
	}
	public void setReport_district(String report_district) {
		this.report_district = report_district;
	}
	public String getReport_address() {
		return report_address;
	}
	public void setReport_address(String report_address) {
		this.report_address = report_address;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLindicator() {
		return lindicator;
	}
	public void setLindicator(String lindicator) {
		this.lindicator = lindicator;
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
	public String getIs_subject_normal() {
		return is_subject_normal;
	}
	public void setIs_subject_normal(String is_subject_normal) {
		this.is_subject_normal = is_subject_normal;
	}
	public String getIs_rescue() {
		return is_rescue;
	}
	public void setIs_rescue(String is_rescue) {
		this.is_rescue = is_rescue;
	}
	public String getIs_self_survey() {
		return is_self_survey;
	}
	public void setIs_self_survey(String is_self_survey) {
		this.is_self_survey = is_self_survey;
	}
	public String getIs_payment() {
		return is_payment;
	}
	public void setIs_payment(String is_payment) {
		this.is_payment = is_payment;
	}
	public String getIs_driver_locale() {
		return is_driver_locale;
	}
	public void setIs_driver_locale(String is_driver_locale) {
		this.is_driver_locale = is_driver_locale;
	}
	public String getIs_main_report() {
		return is_main_report;
	}
	public void setIs_main_report(String is_main_report) {
		this.is_main_report = is_main_report;
	}
	public String getIs_locale_survey() {
		return is_locale_survey;
	}
	public void setIs_locale_survey(String is_locale_survey) {
		this.is_locale_survey = is_locale_survey;
	}
	public String getReplace_survey_mark() {
		return replace_survey_mark;
	}
	public void setReplace_survey_mark(String replace_survey_mark) {
		this.replace_survey_mark = replace_survey_mark;
	}
	public String getIs_edit_report() {
		return is_edit_report;
	}
	public void setIs_edit_report(String is_edit_report) {
		this.is_edit_report = is_edit_report;
	}
	public String getIs_insured_phone() {
		return is_insured_phone;
	}
	public void setIs_insured_phone(String is_insured_phone) {
		this.is_insured_phone = is_insured_phone;
	}
	public String getIs_take_effect() {
		return is_take_effect;
	}
	public void setIs_take_effect(String is_take_effect) {
		this.is_take_effect = is_take_effect;
	}
	public String getDriver_name() {
		return driver_name;
	}
	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}
	public String getReporter() {
		return reporter;
	}
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}
	public String getReporter_phone() {
		return reporter_phone;
	}
	public void setReporter_phone(String reporter_phone) {
		this.reporter_phone = reporter_phone;
	}
		

}
