package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimPhotosVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String img_name;//照片名称
	private String img_type;//照片类型
	private String license_plate_no;//车牌号
	private String ic_img_url;//保险公司照片路径
	private String img_code;//保险公司图片编码
	
	public String getImg_name() {
		return img_name;
	}
	public void setImg_name(String img_name) {
		this.img_name = img_name;
	}
	public String getImg_type() {
		return img_type;
	}
	public void setImg_type(String img_type) {
		this.img_type = img_type;
	}
	public String getLicense_plate_no() {
		return license_plate_no;
	}
	public void setLicense_plate_no(String license_plate_no) {
		this.license_plate_no = license_plate_no;
	}
	public String getIc_img_url() {
		return ic_img_url;
	}
	public void setIc_img_url(String ic_img_url) {
		this.ic_img_url = ic_img_url;
	}
	public String getImg_code() {
		return img_code;
	}
	public void setImg_code(String img_code) {
		this.img_code = img_code;
	}
		
		
		
		


}
