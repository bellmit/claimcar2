/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//追偿/清付信息列表(隶属于结案信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiClaimCloseRecoveryOrPayDataVo {
	/** 追偿/清付序号 **/ 
	@XmlElement(name="SERIAL_NO", required = true)
	private String serialNo; 
	
	/** 追偿/清付标志；参见代码 **/ 
	@XmlElement(name="RECOVERY_OR_PAY_FLAG", required = true)
	private String recoveryOrPayFlag; 

	/** 追偿/清付类型；参见代码 **/ 
	@XmlElement(name="RECOVERY_OR_PAY_TYPE", required = true)
	private String recoveryOrPayType; 

	/** 追偿/清付人 **/ 
	@XmlElement(name="RECOVERY_OR_PAY_MAN")
	private String recoveryOrPayMan; 

	/** 结算码 **/ 
	@XmlElement(name="RECOVERY_CODE")
	private String recoveryCode; 

	/** 追偿/清付金额 **/ 
	@XmlElement(name="RECOVERY_OR_PAY_AMOUNT")
	private Double recoveryOrPayAmount; 

	/** 清付备注 **/ 
	@XmlElement(name="RECOVERY_REMARK")
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
