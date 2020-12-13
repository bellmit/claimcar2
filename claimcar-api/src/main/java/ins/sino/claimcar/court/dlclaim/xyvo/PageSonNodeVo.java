package ins.sino.claimcar.court.dlclaim.xyvo;

import java.io.Serializable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("PAGE")
public class PageSonNodeVo implements Serializable{
private static final long serialVersionUID = 1L;
@XStreamAlias("PAGEID")
@XStreamAsAttribute
private String pageid;
@XStreamAlias("CREATE_USER")
private String create_user;
@XStreamAlias("CREATE_USERNAME")
private String create_username;
@XStreamAlias("CREATE_TIME")
private String create_time;
@XStreamAlias("MODIFY_USER")
private String modify_user;
@XStreamAlias("MODIFY_TIME")
private String modify_time;
@XStreamAlias("PAGE_URL")
private String page_url;
@XStreamAlias("THUM_URL")
private String thum_url;
@XStreamAlias("IS_LOCAL")
private String is_local;
@XStreamAlias("PAGE_VER")
private String page_ver;
@XStreamAlias("PAGE_DESC")
private String page_desc;
@XStreamAlias("UPLOAD_ORG")
private String upload_org;
@XStreamAlias("PAGE_CRC")
private String page_crc;
@XStreamAlias("PAGE_SIZE")
private String page_size;
@XStreamAlias("PAGE_FORMAT")
private String page_format;
@XStreamAlias("PAGE_ENCRYPT")
private String page_encrypt;
@XStreamAlias("ORIGINAL_NAME")
private String original_name;
@XStreamAlias("PAGE_EXT")
private PageExtVo pageExtVo;
@XStreamAlias("PAGE_DESCS")
private String page_descs;
public String getPageid() {
	return pageid;
}
public void setPageid(String pageid) {
	this.pageid = pageid;
}
public String getCreate_user() {
	return create_user;
}
public void setCreate_user(String create_user) {
	this.create_user = create_user;
}
public String getCreate_username() {
	return create_username;
}
public void setCreate_username(String create_username) {
	this.create_username = create_username;
}
public String getCreate_time() {
	return create_time;
}
public void setCreate_time(String create_time) {
	this.create_time = create_time;
}
public String getModify_user() {
	return modify_user;
}
public void setModify_user(String modify_user) {
	this.modify_user = modify_user;
}
public String getModify_time() {
	return modify_time;
}
public void setModify_time(String modify_time) {
	this.modify_time = modify_time;
}
public String getPage_url() {
	return page_url;
}
public void setPage_url(String page_url) {
	this.page_url = page_url;
}
public String getThum_url() {
	return thum_url;
}
public void setThum_url(String thum_url) {
	this.thum_url = thum_url;
}
public String getIs_local() {
	return is_local;
}
public void setIs_local(String is_local) {
	this.is_local = is_local;
}
public String getPage_ver() {
	return page_ver;
}
public void setPage_ver(String page_ver) {
	this.page_ver = page_ver;
}
public String getPage_desc() {
	return page_desc;
}
public void setPage_desc(String page_desc) {
	this.page_desc = page_desc;
}
public String getUpload_org() {
	return upload_org;
}
public void setUpload_org(String upload_org) {
	this.upload_org = upload_org;
}
public String getPage_crc() {
	return page_crc;
}
public void setPage_crc(String page_crc) {
	this.page_crc = page_crc;
}
public String getPage_size() {
	return page_size;
}
public void setPage_size(String page_size) {
	this.page_size = page_size;
}
public String getPage_format() {
	return page_format;
}
public void setPage_format(String page_format) {
	this.page_format = page_format;
}
public String getPage_encrypt() {
	return page_encrypt;
}
public void setPage_encrypt(String page_encrypt) {
	this.page_encrypt = page_encrypt;
}
public String getOriginal_name() {
	return original_name;
}
public void setOriginal_name(String original_name) {
	this.original_name = original_name;
}
public PageExtVo getPageExtVo() {
	return pageExtVo;
}
public void setPageExtVo(PageExtVo pageExtVo) {
	this.pageExtVo = pageExtVo;
}
public String getPage_descs() {
	return page_descs;
}
public void setPage_descs(String page_descs) {
	this.page_descs = page_descs;
}


}
