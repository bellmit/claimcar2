package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;

/**
 *  追偿/清付信息列表
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class UndwrtWriteRecoveryOrPayDataVo implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 追偿/清付序号 **/ 
	private String serialNo; 
	
	/** 追偿/清付标志；参见代码 **/ 
	private String recoveryOrPayFlag; 

	/** 追偿/清付类型；参见代码 **/ 
	private String recoveryOrPayType; 

	/** 追偿/清付人 **/ 
	private String recoveryOrPayMan; 

	/** 结算码 **/ 
	private String recoveryCode; 

	/** 追偿/清付金额 **/ 
	private Double recoveryOrPayAmount; 

	/** 清付备注 **/ 
	private String recoveryRemark;

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getRecoveryOrPayFlag() {
		return recoveryOrPayFlag;
	}

	public void setRecoveryOrPayFlag(String recoveryOrPayFlag) {
		this.recoveryOrPayFlag = recoveryOrPayFlag;
	}

	public String getRecoveryOrPayType() {
		return recoveryOrPayType;
	}

	public void setRecoveryOrPayType(String recoveryOrPayType) {
		this.recoveryOrPayType = recoveryOrPayType;
	}

	public String getRecoveryOrPayMan() {
		return recoveryOrPayMan;
	}

	public void setRecoveryOrPayMan(String recoveryOrPayMan) {
		this.recoveryOrPayMan = recoveryOrPayMan;
	}

	public String getRecoveryCode() {
		return recoveryCode;
	}

	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}

	public Double getRecoveryOrPayAmount() {
		return recoveryOrPayAmount;
	}

	public void setRecoveryOrPayAmount(Double recoveryOrPayAmount) {
		this.recoveryOrPayAmount = recoveryOrPayAmount;
	}

	public String getRecoveryRemark() {
		return recoveryRemark;
	}

	public void setRecoveryRemark(String recoveryRemark) {
		this.recoveryRemark = recoveryRemark;
	}
	
}
