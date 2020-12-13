package ins.sino.claimcar.trafficplatform.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ProtectData")
public class ProtectDataVo {
/**
 * 损失财产名称
 */
@XStreamAlias("ProtectName")
private String protectName;

/**
 * 损失描述
 */
@XStreamAlias("LossDesc")
private String lossDesc;

/**
 * 损失数量
 */
@XStreamAlias("LossNum")
private String lossNum;

/**
 * 核损金额
 */
@XStreamAlias("UnderDefLoss")
private String underDefLoss;

/**
 * 所有人/管理人
 */
@XStreamAlias("Owner")
private String owner;

/**
 * 财产属性；参见代码
 */
@XStreamAlias("ProtectProperty")
private String protectProperty;

/**
 * 现场类别
 */
@XStreamAlias("FieldType")
private String fieldType;

/**
 * 定损人员姓名
 */
@XStreamAlias("EstimateName")
private String estimateName;

/**
 * 定损人员代码
 */
@XStreamAlias("EstimateCode")
private String estimateCode;

/**
 * 定损人员身份证号码
 */
@XStreamAlias("EstimateCertiCode")
private String estimateCertiCode;

/**
 * 核损人员姓名
 */
@XStreamAlias("UnderWriteName")
private String underWriteName;

/**
 * 核损人员代码 
 */
@XStreamAlias("UnderWriteCode")
private String underWriteCode;

/**
 * 核损人员身份证号码
 */
@XStreamAlias("UnderWriteCertiCode")
private String underWriteCertiCode;

/**
 * 财产损失定损开始时间 ；精确到分钟
 */
@XStreamAlias("EstimateStartTime")
private String estimateStartTime;

/**
 * 财产损失核损结束时间 ；精确到分钟
 */
@XStreamAlias("UnderEndTime")
private String underEndTime;

/**
 * 定损地点
 */
@XStreamAlias("EstimateAddr")
private String estimateAddr;

public String getProtectName() {
	return protectName;
}

public void setProtectName(String protectName) {
	this.protectName = protectName;
}

public String getLossDesc() {
	return lossDesc;
}

public void setLossDesc(String lossDesc) {
	this.lossDesc = lossDesc;
}

public String getLossNum() {
	return lossNum;
}

public void setLossNum(String lossNum) {
	this.lossNum = lossNum;
}

public String getUnderDefLoss() {
	return underDefLoss;
}

public void setUnderDefLoss(String underDefLoss) {
	this.underDefLoss = underDefLoss;
}

public String getOwner() {
	return owner;
}

public void setOwner(String owner) {
	this.owner = owner;
}

public String getProtectProperty() {
	return protectProperty;
}

public void setProtectProperty(String protectProperty) {
	this.protectProperty = protectProperty;
}

public String getFieldType() {
	return fieldType;
}

public void setFieldType(String fieldType) {
	this.fieldType = fieldType;
}

public String getEstimateName() {
	return estimateName;
}

public void setEstimateName(String estimateName) {
	this.estimateName = estimateName;
}

public String getEstimateCode() {
	return estimateCode;
}

public void setEstimateCode(String estimateCode) {
	this.estimateCode = estimateCode;
}

public String getEstimateCertiCode() {
	return estimateCertiCode;
}

public void setEstimateCertiCode(String estimateCertiCode) {
	this.estimateCertiCode = estimateCertiCode;
}

public String getUnderWriteName() {
	return underWriteName;
}

public void setUnderWriteName(String underWriteName) {
	this.underWriteName = underWriteName;
}

public String getUnderWriteCode() {
	return underWriteCode;
}

public void setUnderWriteCode(String underWriteCode) {
	this.underWriteCode = underWriteCode;
}

public String getUnderWriteCertiCode() {
	return underWriteCertiCode;
}

public void setUnderWriteCertiCode(String underWriteCertiCode) {
	this.underWriteCertiCode = underWriteCertiCode;
}

public String getEstimateStartTime() {
	return estimateStartTime;
}

public void setEstimateStartTime(String estimateStartTime) {
	this.estimateStartTime = estimateStartTime;
}

public String getUnderEndTime() {
	return underEndTime;
}

public void setUnderEndTime(String underEndTime) {
	this.underEndTime = underEndTime;
}

public String getEstimateAddr() {
	return estimateAddr;
}

public void setEstimateAddr(String estimateAddr) {
	this.estimateAddr = estimateAddr;
}


}
