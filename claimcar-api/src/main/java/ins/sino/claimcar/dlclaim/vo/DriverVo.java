package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String license_plate_no;//车牌号
	private String hkmac_license_plate_no;//粤港澳车牌号码
	private String driver_birth;//出生日期
	private String identify_type;//证件类型
	private String driving_license_archives_no;//驾驶证档案编码
	private String driving_identify_type;//驾照类型
	private String allow_driving_vehicle;//准驾车型
	private String driver_occupation;//驾驶员职业
	private String driver_sex;//驾驶员性别
	private String driver_age;//驾驶员年龄
	private String is_driving_license_effective;//驾驶员的驾驶证是否有效
	private String driving_license_date;//初次领证日期
	private String driver_license_institution;//颁证机关
	private String is_drinking_driving;//是否为酒后驾车
	private String driver_name;//驾驶员姓名
	private String driving_license_no;//驾驶证号码
	
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
	public String getDriver_birth() {
		return driver_birth;
	}
	public void setDriver_birth(String driver_birth) {
		this.driver_birth = driver_birth;
	}
	public String getIdentify_type() {
		return identify_type;
	}
	public void setIdentify_type(String identify_type) {
		this.identify_type = identify_type;
	}
	public String getDriving_license_archives_no() {
		return driving_license_archives_no;
	}
	public void setDriving_license_archives_no(String driving_license_archives_no) {
		this.driving_license_archives_no = driving_license_archives_no;
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
	public String getDriver_occupation() {
		return driver_occupation;
	}
	public void setDriver_occupation(String driver_occupation) {
		this.driver_occupation = driver_occupation;
	}
	public String getDriver_sex() {
		return driver_sex;
	}
	public void setDriver_sex(String driver_sex) {
		this.driver_sex = driver_sex;
	}
	public String getDriver_age() {
		return driver_age;
	}
	public void setDriver_age(String driver_age) {
		this.driver_age = driver_age;
	}
	public String getIs_driving_license_effective() {
		return is_driving_license_effective;
	}
	public void setIs_driving_license_effective(String is_driving_license_effective) {
		this.is_driving_license_effective = is_driving_license_effective;
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
	public String getIs_drinking_driving() {
		return is_drinking_driving;
	}
	public void setIs_drinking_driving(String is_drinking_driving) {
		this.is_drinking_driving = is_drinking_driving;
	}
	public String getDriver_name() {
		return driver_name;
	}
	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}
	public String getDriving_license_no() {
		return driving_license_no;
	}
	public void setDriving_license_no(String driving_license_no) {
		this.driving_license_no = driving_license_no;
	}
		
    
}
