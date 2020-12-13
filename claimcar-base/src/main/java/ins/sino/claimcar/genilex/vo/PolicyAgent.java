package ins.sino.claimcar.genilex.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PolicyAgent")
public class PolicyAgent implements Serializable {

	/**被保险人信息  */
	private static final long serialVersionUID = 6606450331852430142L;
	
	@XStreamAlias("AgentTypeCode")
	private String agentTypeCode="";  //代理人类型代码
	
    @XStreamAlias("AgentCode")
    private String agentCode="";  //中介机构代码/代理人代码
    
    @XStreamAlias("AgentName")
    private String agentName="";  //中介机构全称/个人代理人名称
    
    @XStreamAlias("CertificateNo")
    private String certificateNo="";  //中介机构许可证号/个人代理资格证号
    
    @XStreamAlias("GroupCompany")
    private String groupCompany="";  //所属中介集团
    
    @XStreamAlias("PracticeCertificateCode")
    private String practiceCertificateCode="";  //营销员展业证号
    
    @XStreamAlias("CreateBy")
    private String createBy="";  //创建者
    
    @XStreamAlias("CreateTime")
    private String createTime="";  //创建日期
    
    @XStreamAlias("UpdateBy")
    private String updateBy="";  //更新者
    
    @XStreamAlias("UpdateTime")
    private String updateTime="";  //更新日期

    
    public String getAgentTypeCode() {
        return agentTypeCode;
    }

    
    public void setAgentTypeCode(String agentTypeCode) {
        this.agentTypeCode = agentTypeCode;
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

    
    public String getCertificateNo() {
        return certificateNo;
    }

    
    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    
    public String getGroupCompany() {
        return groupCompany;
    }

    
    public void setGroupCompany(String groupCompany) {
        this.groupCompany = groupCompany;
    }

    
    public String getPracticeCertificateCode() {
        return practiceCertificateCode;
    }

    
    public void setPracticeCertificateCode(String practiceCertificateCode) {
        this.practiceCertificateCode = practiceCertificateCode;
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

    
   
}
