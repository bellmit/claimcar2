package ins.sino.claimcar.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class BiLossProtectDataVo {

	@XmlElement(name = "ProtectName", required = true)
	private String protectName;// 损失财产名称

	@XmlElement(name = "LossDesc")
	private String lossDesc;// 损失描述

	@XmlElement(name = "LossNum")
	private String lossNum;// 损失数量

	@XmlElement(name = "UnderDefLoss", required = true)
	private String underDefLoss;// 核损金额

	@XmlElement(name = "Owner")
	private String owner;// 所有人/管理人

	@XmlElement(name = "ProtectProperty", required = true)
	private String protectProperty;// 财产属性；参见代码

	@XmlElement(name = "FieldType", required = true)
	private String fieldType;// 现场类别；参见代码

	@XmlElement(name = "EstimateName", required = true)
	private String estimateName;// 定损人员姓名

	@XmlElement(name = "EstimateCode")
	private String estimateCode;// 定损人员代码

	@XmlElement(name = "EstimateCertiCode", required = true)
	private String estimateCertiCode;// 定损人员身份证号码

	@XmlElement(name = "UnderWriteName")
	private String underWriteName;// 核损人员姓名

	@XmlElement(name = "UnderWriteCode")
	private String underWriteCode;// 核损人员代码

	@XmlElement(name = "UnderWriteCertiCode")
	private String underWriteCertiCode;// 核损人员身份证号码

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "EstimateStartTime", required = true)
	private Date estimateStartTime;// 财产损失定损开始时间 ；精确到分钟

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "UnderEndTime", required = true)
	private Date underEndTime;// 财产损失核损结束时间 ；精确到分钟

	@XmlElement(name = "EstimateAddr")
	private String estimateAddr;// 定损地点

	/**
	 * @return 返回 protectName 损失财产名称
	 */
	public String getProtectName() {
		return protectName;
	}

	/**
	 * @param protectName 要设置的 损失财产名称
	 */
	public void setProtectName(String protectName) {
		this.protectName = protectName;
	}

	/**
	 * @return 返回 lossDesc 损失描述
	 */
	public String getLossDesc() {
		return lossDesc;
	}

	/**
	 * @param lossDesc 要设置的 损失描述
	 */
	public void setLossDesc(String lossDesc) {
		this.lossDesc = lossDesc;
	}

	/**
	 * @return 返回 lossNum 损失数量
	 */
	public String getLossNum() {
		return lossNum;
	}

	/**
	 * @param lossNum 要设置的 损失数量
	 */
	public void setLossNum(String lossNum) {
		this.lossNum = lossNum;
	}

	/**
	 * @return 返回 underDefLoss 核损金额
	 */
	public String getUnderDefLoss() {
		return underDefLoss;
	}

	/**
	 * @param underDefLoss 要设置的 核损金额
	 */
	public void setUnderDefLoss(String underDefLoss) {
		this.underDefLoss = underDefLoss;
	}

	/**
	 * @return 返回 owner 所有人/管理人
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner 要设置的 所有人/管理人
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return 返回 protectProperty 财产属性；参见代码
	 */
	public String getProtectProperty() {
		return protectProperty;
	}

	/**
	 * @param protectProperty 要设置的 财产属性；参见代码
	 */
	public void setProtectProperty(String protectProperty) {
		this.protectProperty = protectProperty;
	}

	/**
	 * @return 返回 fieldType 现场类别；参见代码
	 */
	public String getFieldType() {
		return fieldType;
	}

	/**
	 * @param fieldType 要设置的 现场类别；参见代码
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * @return 返回 estimateName 定损人员姓名
	 */
	public String getEstimateName() {
		return estimateName;
	}

	/**
	 * @param estimateName 要设置的 定损人员姓名
	 */
	public void setEstimateName(String estimateName) {
		this.estimateName = estimateName;
	}

	/**
	 * @return 返回 estimateCode 定损人员代码
	 */
	public String getEstimateCode() {
		return estimateCode;
	}

	/**
	 * @param estimateCode 要设置的 定损人员代码
	 */
	public void setEstimateCode(String estimateCode) {
		this.estimateCode = estimateCode;
	}

	/**
	 * @return 返回 estimateCertiCode 定损人员身份证号码
	 */
	public String getEstimateCertiCode() {
		return estimateCertiCode;
	}

	/**
	 * @param estimateCertiCode 要设置的 定损人员身份证号码
	 */
	public void setEstimateCertiCode(String estimateCertiCode) {
		this.estimateCertiCode = estimateCertiCode;
	}

	/**
	 * @return 返回 underWriteName 核损人员姓名
	 */
	public String getUnderWriteName() {
		return underWriteName;
	}

	/**
	 * @param underWriteName 要设置的 核损人员姓名
	 */
	public void setUnderWriteName(String underWriteName) {
		this.underWriteName = underWriteName;
	}

	/**
	 * @return 返回 underWriteCode 核损人员代码
	 */
	public String getUnderWriteCode() {
		return underWriteCode;
	}

	/**
	 * @param underWriteCode 要设置的 核损人员代码
	 */
	public void setUnderWriteCode(String underWriteCode) {
		this.underWriteCode = underWriteCode;
	}

	/**
	 * @return 返回 underWriteCertiCode 核损人员身份证号码
	 */
	public String getUnderWriteCertiCode() {
		return underWriteCertiCode;
	}

	/**
	 * @param underWriteCertiCode 要设置的 核损人员身份证号码
	 */
	public void setUnderWriteCertiCode(String underWriteCertiCode) {
		this.underWriteCertiCode = underWriteCertiCode;
	}

	/**
	 * @return 返回 estimateStartTime 财产损失定损开始时间 ；精确到分钟
	 */
	public Date getEstimateStartTime() {
		return estimateStartTime;
	}

	/**
	 * @param estimateStartTime 要设置的 财产损失定损开始时间 ；精确到分钟
	 */
	public void setEstimateStartTime(Date estimateStartTime) {
		this.estimateStartTime = estimateStartTime;
	}

	/**
	 * @return 返回 underEndTime 财产损失核损结束时间 ；精确到分钟
	 */
	public Date getUnderEndTime() {
		return underEndTime;
	}

	/**
	 * @param underEndTime 要设置的 财产损失核损结束时间 ；精确到分钟
	 */
	public void setUnderEndTime(Date underEndTime) {
		this.underEndTime = underEndTime;
	}

	/**
	 * @return 返回 estimateAddr 定损地点
	 */
	public String getEstimateAddr() {
		return estimateAddr;
	}

	/**
	 * @param estimateAddr 要设置的 定损地点
	 */
	public void setEstimateAddr(String estimateAddr) {
		this.estimateAddr = estimateAddr;
	}

}
