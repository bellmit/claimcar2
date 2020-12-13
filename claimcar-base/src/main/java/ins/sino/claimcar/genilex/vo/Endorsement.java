package ins.sino.claimcar.genilex.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Endorsement")
public class Endorsement implements Serializable {

	/**被保险人信息  */
	private static final long serialVersionUID = 6606450331852430142L;
	
	@XStreamAlias("EndorsementNo")
	private String endorsementNo="";  //批单号
	
    @XStreamAlias("EdrPrjNo")
    private String edrPrjNo="";  //批改期次
    
    @XStreamAlias("EdrType")
    private String edrType="";  //批改类型
    
    @XStreamAlias("EdrReson")
    private String edrReson="";  //批改原因
    
    @XStreamAlias("EdrPremium")
    private String edrPremium="";  //批改保费
    
    @XStreamAlias("SpecialFlag")
    private String specialFlag="";  //特殊批改标志
    
    @XStreamAlias("SpecialAgreementDesc")
    private String specialAgreementDesc="";  //特别约定
    
    @XStreamAlias("EffectiveTime")
    private String effectiveTime="";  //批改生效时间
	   
    @XStreamAlias("EdrPrjType")
    private String edrPrjType="";  //批改结论
    
    @XStreamAlias("EdrPrjMemo")
    private String edrPrjMemo="";  //批改结论描述
    
    @XStreamAlias("Agent")
    private String agent="";  //经办人
    
    @XStreamAlias("Operator")
    private String operator="";  //操作员
    
    @XStreamAlias("Remark")
    private String remark="";  //备注
    
    @XStreamAlias("CreateBy")
    private String createBy;  //创建者
    
    @XStreamAlias("CreateTime")
    private String createTime;  //创建日期
    
    @XStreamAlias("UpdateBy")
    private String updateBy;  //更新者
    
    @XStreamAlias("UpdateTime")
    private String updateTime;  //更新日期

    
    public String getEndorsementNo() {
        return endorsementNo;
    }

    
    public void setEndorsementNo(String endorsementNo) {
        this.endorsementNo = endorsementNo;
    }

    
    public String getEdrPrjNo() {
        return edrPrjNo;
    }

    
    public void setEdrPrjNo(String edrPrjNo) {
        this.edrPrjNo = edrPrjNo;
    }

    
    public String getEdrType() {
        return edrType;
    }

    
    public void setEdrType(String edrType) {
        this.edrType = edrType;
    }

    
    public String getEdrReson() {
        return edrReson;
    }

    
    public void setEdrReson(String edrReson) {
        this.edrReson = edrReson;
    }

    
    public String getEdrPremium() {
        return edrPremium;
    }

    
    public void setEdrPremium(String edrPremium) {
        this.edrPremium = edrPremium;
    }

    
    public String getSpecialFlag() {
        return specialFlag;
    }

    
    public void setSpecialFlag(String specialFlag) {
        this.specialFlag = specialFlag;
    }

    
    public String getSpecialAgreementDesc() {
        return specialAgreementDesc;
    }

    
    public void setSpecialAgreementDesc(String specialAgreementDesc) {
        this.specialAgreementDesc = specialAgreementDesc;
    }

    
    public String getEffectiveTime() {
        return effectiveTime;
    }

    
    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    
    public String getEdrPrjType() {
        return edrPrjType;
    }

    
    public void setEdrPrjType(String edrPrjType) {
        this.edrPrjType = edrPrjType;
    }

    
    public String getEdrPrjMemo() {
        return edrPrjMemo;
    }

    
    public void setEdrPrjMemo(String edrPrjMemo) {
        this.edrPrjMemo = edrPrjMemo;
    }

    
    public String getAgent() {
        return agent;
    }

    
    public void setAgent(String agent) {
        this.agent = agent;
    }

    
    public String getOperator() {
        return operator;
    }

    
    public void setOperator(String operator) {
        this.operator = operator;
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

    
 
    
}
