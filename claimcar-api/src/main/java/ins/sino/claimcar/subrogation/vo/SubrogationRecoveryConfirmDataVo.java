package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;

/**
 *   追回款信息列表VO
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class SubrogationRecoveryConfirmDataVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 清付人 **/ 
	private String recoveryMan; 

	/** 结算码 **/ 
	private String recoveryCode;

	public String getRecoveryMan() {
		return recoveryMan;
	}

	public void setRecoveryMan(String recoveryMan) {
		this.recoveryMan = recoveryMan;
	}

	public String getRecoveryCode() {
		return recoveryCode;
	}

	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}
	
	

	
	

}
