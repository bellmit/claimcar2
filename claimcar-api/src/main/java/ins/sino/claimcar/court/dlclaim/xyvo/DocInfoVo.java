package ins.sino.claimcar.court.dlclaim.xyvo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("DocInfo")
public class DocInfoVo implements Serializable{
private static final long serialVersionUID = 1L;
	@XStreamAlias("BATCH_ID")
	private String batch_id;//图像所属的批次号
    @XStreamAlias("BUSI_NUM")
    private String busi_num;//图像所属的业务号，例如赔案号,鼎和项目目前对应接口交互报文中的BUSI_NO参数-
    @XStreamAlias("BIZ_ORG")
	private String biz_org;//图像所属机构号
    @XStreamAlias("APP_CODE")
    private String app_code;
    @XStreamAlias("APP_NAME")
    private String app_name;
    @XStreamAlias("BATCH_VER")
    private String batch_ver;
    @XStreamAlias("INTER_VER")
    private String inter_ver;
    @XStreamAlias("STATUS")
    private String status;
    @XStreamAlias("CREATE_USER")
    private String create_user;
    @XStreamAlias("CREATE_DATE")
    private String create_date;
    @XStreamAlias("MODIFY_USER")
    private String modify_user;
    @XStreamAlias("MODIFY_DATE")
    private String modify_date;
    @XStreamAlias("DOC_EXT")
    private DocextVo docextVo;
	public String getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}
	public String getBusi_num() {
		return busi_num;
	}
	public void setBusi_num(String busi_num) {
		this.busi_num = busi_num;
	}
	public String getBiz_org() {
		return biz_org;
	}
	public void setBiz_org(String biz_org) {
		this.biz_org = biz_org;
	}
	public String getApp_code() {
		return app_code;
	}
	public void setApp_code(String app_code) {
		this.app_code = app_code;
	}
	public String getBatch_ver() {
		return batch_ver;
	}
	public void setBatch_ver(String batch_ver) {
		this.batch_ver = batch_ver;
	}
	public String getInter_ver() {
		return inter_ver;
	}
	public void setInter_ver(String inter_ver) {
		this.inter_ver = inter_ver;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getModify_user() {
		return modify_user;
	}
	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}
	public String getModify_date() {
		return modify_date;
	}
	public void setModify_date(String modify_date) {
		this.modify_date = modify_date;
	}
	public DocextVo getDocextVo() {
		return docextVo;
	}
	public void setDocextVo(DocextVo docextVo) {
		this.docextVo = docextVo;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
    
}
