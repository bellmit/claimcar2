package ins.sino.claimcar.claim.vo;

import java.io.Serializable;
import java.math.BigDecimal;


public class ThirdPartyRecoveryInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 标的车车牌号 */
	private String mainCarLicenseNo;
	
	/** 标的车实际损失车牌号 */
	private String mainCarLossName;

	/** 代责任对方交强车辆车牌号:PrpLthirdpartyId */
	private String thirdCarlicenseNo;

	/** 代责任对方交强应赔付金额 */
	private BigDecimal recoverySumRealPay;
	
	/** 代责任对方交强应赔付金额(定损) */
	private BigDecimal lossOfRecoverySumRealPay;
	
	/** 代责任对方交强应赔付金额(施救) */
	private BigDecimal rescueOfRecoverySumRealPay;

	
	public String getMainCarLicenseNo() {
		return mainCarLicenseNo;
	}

	
	public void setMainCarLicenseNo(String mainCarLicenseNo) {
		this.mainCarLicenseNo = mainCarLicenseNo;
	}

	
	public String getMainCarLossName() {
		return mainCarLossName;
	}

	
	public void setMainCarLossName(String mainCarLossName) {
		this.mainCarLossName = mainCarLossName;
	}

	
	public String getThirdCarlicenseNo() {
		return thirdCarlicenseNo;
	}

	
	public void setThirdCarlicenseNo(String thirdCarlicenseNo) {
		this.thirdCarlicenseNo = thirdCarlicenseNo;
	}

	
	public BigDecimal getRecoverySumRealPay() {
		return recoverySumRealPay;
	}

	
	public void setRecoverySumRealPay(BigDecimal recoverySumRealPay) {
		this.recoverySumRealPay = recoverySumRealPay;
	}

	
	public BigDecimal getLossOfRecoverySumRealPay() {
		return lossOfRecoverySumRealPay;
	}

	
	public void setLossOfRecoverySumRealPay(BigDecimal lossOfRecoverySumRealPay) {
		this.lossOfRecoverySumRealPay = lossOfRecoverySumRealPay;
	}

	
	public BigDecimal getRescueOfRecoverySumRealPay() {
		return rescueOfRecoverySumRealPay;
	}

	
	public void setRescueOfRecoverySumRealPay(BigDecimal rescueOfRecoverySumRealPay) {
		this.rescueOfRecoverySumRealPay = rescueOfRecoverySumRealPay;
	}

}
