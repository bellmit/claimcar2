package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("PERSONINFO")
public class PersonInfoVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ISADJUST")
	private String isAdjust; //是否现场调解
	
	@XStreamAlias("LOSSTYPE")
	private String losstype; //损失方
	
	@XStreamAlias("NAME")
	private String name; //姓名
	
	@XStreamAlias("SEX")
	private String sex; //性别
	
	@XStreamAlias("CERTITYPE")
	private String certiType; //证件类型
	
	@XStreamAlias("IDENTIFYNUMBER")
	private String identifyNumber; //证件号码
	
	@XStreamAlias("AGE")
	private String age; //年龄
	
	@XStreamAlias("ISHOSPITAL")
	private String ishospital; //是否就医（下拉框）
	
	@XStreamAlias("DEGREE")
	private String degree; //伤情类别
	
	@XStreamAlias("SURVERYTPE")
	private String surverytpe; //查勘处理方式
	
	@XStreamAlias("INDUSTRY")
	private String industry; //从事行业
	
	@XStreamAlias("THERAPEUTICAGENCY")
	private String therapeuticagency; //治疗机构
	
	@XStreamAlias("THERAPEUTICAGENCYCODE")
	private String therapeuticagencyCode; //治疗结构组织机构代码
	
	@XStreamAlias("SUMCLAIM")
	private String sumClaim; //定损金额
	
	@XStreamAlias("DEGREEDESC")
	private String degreedesc; //伤情描述

	@XStreamAlias("PERSHANDLETYPE")
    private String persHandleType; //案件处理类型

	public String getIsAdjust() {
		return isAdjust;
	}

	public void setIsAdjust(String isAdjust) {
		this.isAdjust = isAdjust;
	}

	public String getLosstype() {
		return losstype;
	}

	public void setLosstype(String losstype) {
		this.losstype = losstype;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCertiType() {
		return certiType;
	}

	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	public String getIdentifyNumber() {
		return identifyNumber;
	}

	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getIshospital() {
		return ishospital;
	}

	public void setIshospital(String ishospital) {
		this.ishospital = ishospital;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getSurverytpe() {
		return surverytpe;
	}

	public void setSurverytpe(String surverytpe) {
		this.surverytpe = surverytpe;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getTherapeuticagency() {
		return therapeuticagency;
	}

	public void setTherapeuticagency(String therapeuticagency) {
		this.therapeuticagency = therapeuticagency;
	}

	public String getTherapeuticagencyCode() {
		return therapeuticagencyCode;
	}

	public void setTherapeuticagencyCode(String therapeuticagencyCode) {
		this.therapeuticagencyCode = therapeuticagencyCode;
	}

	public String getSumClaim() {
		return sumClaim;
	}

	public void setSumClaim(String sumClaim) {
		this.sumClaim = sumClaim;
	}

	public String getDegreedesc() {
		return degreedesc;
	}

	public void setDegreedesc(String degreedesc) {
		this.degreedesc = degreedesc;
	}
    
    public String getPersHandleType() {
        return persHandleType;
    }
    
    public void setPersHandleType(String persHandleType) {
        this.persHandleType = persHandleType;
    }
	
}
