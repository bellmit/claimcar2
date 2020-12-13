package ins.sino.claimcar.regist.vo;

import java.io.Serializable;
import java.util.Date;

public class PrpDagentVo implements Serializable{
    /**  */
    private static final long serialVersionUID = 1L;
    /** 属性代理人代码 */
    private String agentCode = "";
    /** 属性代理人名称 */
    private String agentName = "";
    /** 属性代理人地址 */
    private String addressName = "";
    /** 属性邮政编码 */
    private String postCode = "";
    /** 属性代理人类型 */
    private String agentType = "";
    /** 属性许可证号 */
    private String permitNo = "";
    /** 属性联系人 */
    private String linkerName = "";
    /** 属性合同期 */
    private Date bargainDate;
    /** 属性电话 */
    private String phoneNumber = "";
    /** 属性传真 */
    private String faxNumber = "";
    /** 属性归属机构代码 */
    private String comCode = "";
    /** 属性上级代理人代码 */
    private String upperAgentCode = "";
    /** 属性新的代理人代码 */
    private String newAgentCode = "";
    /** 属性效力状态(0失效/1有效) */
    private String validStatus = "";
    /** 属性专项代码(对应会计科目) */
    private String articleCode = "";
    /** 属性标志字段 */
    private String flag = "";
    /**发送短信标志位 */
    private String sendmsgflag;
   /**手机号*/
    private String mobile;
    

	/**
     *  默认构造方法,构造一个默认的PrpDagentDtoBase对象
     */
    public PrpDagentVo(){
    }


    
    public String getAgentCode() {
        return agentCode;
    }


    
    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }


    
    public String getAgentName() {
        return agentName;
    }


    
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }


    
    public String getAddressName() {
        return addressName;
    }


    
    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }


    
    public String getPostCode() {
        return postCode;
    }


    
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }


    
    public String getAgentType() {
        return agentType;
    }


    
    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }


    
    public String getPermitNo() {
        return permitNo;
    }


    
    public void setPermitNo(String permitNo) {
        this.permitNo = permitNo;
    }


    
    public String getLinkerName() {
        return linkerName;
    }


    
    public void setLinkerName(String linkerName) {
        this.linkerName = linkerName;
    }


    
    public Date getBargainDate() {
        return bargainDate;
    }


    
    public void setBargainDate(Date bargainDate) {
        this.bargainDate = bargainDate;
    }


    
    public String getPhoneNumber() {
        return phoneNumber;
    }


    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    
    public String getFaxNumber() {
        return faxNumber;
    }


    
    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }


    
    public String getComCode() {
        return comCode;
    }


    
    public void setComCode(String comCode) {
        this.comCode = comCode;
    }


    
    public String getUpperAgentCode() {
        return upperAgentCode;
    }


    
    public void setUpperAgentCode(String upperAgentCode) {
        this.upperAgentCode = upperAgentCode;
    }


    
    public String getNewAgentCode() {
        return newAgentCode;
    }


    
    public void setNewAgentCode(String newAgentCode) {
        this.newAgentCode = newAgentCode;
    }


    
    public String getValidStatus() {
        return validStatus;
    }


    
    public void setValidStatus(String validStatus) {
        this.validStatus = validStatus;
    }


    
    public String getArticleCode() {
        return articleCode;
    }


    
    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }


    
    public String getFlag() {
        return flag;
    }


    
    public void setFlag(String flag) {
        this.flag = flag;
    }


    
    public String getSendmsgflag() {
        return sendmsgflag;
    }


    
    public void setSendmsgflag(String sendmsgflag) {
        this.sendmsgflag = sendmsgflag;
    }


    
    public String getMobile() {
        return mobile;
    }


    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
