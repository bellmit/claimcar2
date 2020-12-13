package ins.sino.claimcar.genilex.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ClaimSuccor")
public class ClaimSuccor implements Serializable {

	/**  */
	private static final long serialVersionUID = 6606450331852430142L;
	
	@XStreamAlias("SerialNo")
	private String serialNo;  //顺序号
	
    @XStreamAlias("LinkmanName")
    private String linkmanName;  //联系人名称
    
    @XStreamAlias("LinkTel")
    private String linkTel;  //联系人手机
    
    @XStreamAlias("SuccorDescribe")
    private String succorDescribe;  //救援描述
    
    @XStreamAlias("SuccorSpot")
    private String succorSpot;  //救援地点
    
    @XStreamAlias("Issuccorflag")
    private String issuccorflag;  //是否符合免费道路救援
    
    @XStreamAlias("AidCompany")
    private String aidCompany;  //救援公司编码
    
    @XStreamAlias("AidCompanyname")
    private String aidCompanyname;  //救援公司名称
    
    @XStreamAlias("ServiceItem")
    private String serviceItem;  //服务项目编码
    
    @XStreamAlias("ServiceItemName")
    private String serviceItemName;  //服务项目名称
    
    @XStreamAlias("HandleTime")
    private String handleTime;  //受理时间
    
    @XStreamAlias("DispatchCenterNo")
    private String dispatchCenterNo;  //调度中心
    
    @XStreamAlias("Remark")
    private String remark;  //备注
    
    
    @XStreamAlias("CreateBy")
    private String createBy;  //创建者
    
    @XStreamAlias("CreateTime")
    private String createTime;  //创建日期
    
    @XStreamAlias("UpdateBy")
    private String updateBy;  //更新者
    
    @XStreamAlias("UpdateTime")
    private String updateTime;  //更新日期

    
    public String getSerialNo() {
        return serialNo;
    }

    
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    
    public String getLinkmanName() {
        return linkmanName;
    }

    
    public void setLinkmanName(String linkmanName) {
        this.linkmanName = linkmanName;
    }

    
    public String getLinkTel() {
        return linkTel;
    }

    
    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    
    public String getSuccorDescribe() {
        return succorDescribe;
    }

    
    public void setSuccorDescribe(String succorDescribe) {
        this.succorDescribe = succorDescribe;
    }

    
    public String getSuccorSpot() {
        return succorSpot;
    }

    
    public void setSuccorSpot(String succorSpot) {
        this.succorSpot = succorSpot;
    }

    
    public String getIssuccorflag() {
        return issuccorflag;
    }

    
    public void setIssuccorflag(String issuccorflag) {
        this.issuccorflag = issuccorflag;
    }

    
    public String getAidCompany() {
        return aidCompany;
    }

    
    public void setAidCompany(String aidCompany) {
        this.aidCompany = aidCompany;
    }

    
    public String getAidCompanyname() {
        return aidCompanyname;
    }

    
    public void setAidCompanyname(String aidCompanyname) {
        this.aidCompanyname = aidCompanyname;
    }

    
    public String getServiceItem() {
        return serviceItem;
    }

    
    public void setServiceItem(String serviceItem) {
        this.serviceItem = serviceItem;
    }

    
    public String getServiceItemName() {
        return serviceItemName;
    }

    
    public void setServiceItemName(String serviceItemName) {
        this.serviceItemName = serviceItemName;
    }

    
    public String getHandleTime() {
        return handleTime;
    }

    
    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
    }

    
    public String getRemark() {
        return remark;
    }

    
    public void setRemark(String remark) {
        this.remark = remark;
    }

    
    public String getCreateBy() {
        return createBy;
    }

    
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    
    public String getCreateTime() {
        return createTime;
    }

    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    
    public String getUpdateBy() {
        return updateBy;
    }

    
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    
    public String getUpdateTime() {
        return updateTime;
    }

    
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }


    
    public String getDispatchCenterNo() {
        return dispatchCenterNo;
    }


    
    public void setDispatchCenterNo(String dispatchCenterNo) {
        this.dispatchCenterNo = dispatchCenterNo;
    }


}
