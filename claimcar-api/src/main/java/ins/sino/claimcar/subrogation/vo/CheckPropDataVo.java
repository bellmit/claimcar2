package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;

/**
 *  开始追偿确认查询页面VO
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class CheckPropDataVo implements Serializable{
	/** 损失财产名称 **/ 
	private String protectName; 

	/** 损失描述 **/ 
	private String lossDesc; 

	/** 损失数量 **/ 
	private String lossNum; 

	/** 估损金额 **/ 
	private Double estimateAmount; 

	/** 财产属性 **/ 
	private String protectProperty; 

	/** 现场类别 **/ 
	private String fieldType; 

	/** 查勘地点 **/ 
	private String checkAddr; 

	/** 查勘情况说明 **/ 
	private String checkDes;

	private Double estimatedLossAmount;//估损金额

	private String checkDesc;//查勘情况说明

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

	public Double getEstimateAmount() {
		return estimateAmount;
	}

	public void setEstimateAmount(Double estimateAmount) {
		this.estimateAmount = estimateAmount;
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

	public String getCheckAddr() {
		return checkAddr;
	}

	public void setCheckAddr(String checkAddr) {
		this.checkAddr = checkAddr;
	}

	public String getCheckDes() {
		return checkDes;
	}

	public void setCheckDes(String checkDes) {
		this.checkDes = checkDes;
	}

	public Double getEstimatedLossAmount() {
		return estimatedLossAmount;
	}

	public void setEstimatedLossAmount(Double estimatedLossAmount) {
		this.estimatedLossAmount = estimatedLossAmount;
	}

	public String getCheckDesc() {
		return checkDesc;
	}

	public void setCheckDesc(String checkDesc) {
		this.checkDesc = checkDesc;
	}
	
	

}
