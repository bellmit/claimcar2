package ins.sino.claimcar.claim.vo;

import java.io.Serializable;


public class ThirdPartyCompInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 赔付车牌号码*/
	private String licenseNo;
	
	/** 赔付车牌序号*/
	private Integer serialNo;
	/** 被赔付车牌号码*/
	private String objectLicenseNo;
	/** 被赔付车牌序号*/
	private Integer objectSerialNo;
	/** 赔付类型*/
	private String objectType;
	/** 赔付序号*/
	private String objectIndex;
	/** 限额比例赔偿金额*/
	private double payAmount;
	/** 超限额调整赔偿金额*/
	private double adjPayAmount;
	/** 剩余限额赔偿金额*/
	private double leftPayAmount;
	/** 未赔足金额*/
	private double leftPay;
	/** 实赔金额*/
	private double sumRealPay;
	/** 赔付公式*/
	private String bzCompensateText="";
	/** 费用类型*/
	private String FeeTypeCode;
	/** 本车标志位*/
	private String lossItemType;
	/** 归属涉案车辆责任类型*/
	private String bzDutyType;
	/** 归属涉案车辆责任类型*/
	private Boolean bzDutyFlag =false;
	/** 核定损金额*/
	private double sumLoss;
	/** 损失项名称  车：车牌号  财：财物名称  人：人名*/
	private String lossName;
	/** 代赔标志位*/
	private String insteadFlag;
	/** 车财产标志位 1:车 2:财*/
	private String expType;
	/** 损失费用名称  车/财："定损金额"  人：费用名称*/
	private String lossFeeName;
	/** 赔付车辆限额*/
	private double payCarAmout;
	/** 车辆总损失金额*/
	private double carLossSum;
	/** 车辆总损失金额*/
	private double carPaySum;
	/** 本车赔付限额 */
	private double payCarAmount;
	/** 交强赔付类型49   51   52*/
	private String damageType;
	/** add by zhouwen 2009-04-16 军车交强赔付方案时添加*/
	/** 军车车损及车上财产损失视为路面财产损失进行理算,军车车上人员视为路面人员进行理算.规则中需要按承保机构11进行区分.*/
	/** 承保机构*/
	private String insureComCode;
	
	/** 2012-02-06 代位求偿需求添加 */
	/** 责任对方损失 */
	private double recoveryPay;
	
	/** 乘追偿比例前实赔金额 */
	private double noRecoveryPay;
	
	/** 重复的车牌号_ThirdPartyId(一车多定损的情况) */
	private String doubleLicenseNo;
	
	/** 代位求偿,商业计算书,自付标志 */
	private String zfFlag;
	
    private Object lossItem;
	    
    private ThirdPartyLossInfo thirdPartyLossInfo;

	
	public String getLicenseNo() {
		return licenseNo;
	}

	
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	
	public String getObjectLicenseNo() {
		return objectLicenseNo;
	}

	
	public void setObjectLicenseNo(String objectLicenseNo) {
		this.objectLicenseNo = objectLicenseNo;
	}

	
	public String getObjectType() {
		return objectType;
	}

	
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	
	public String getObjectIndex() {
		return objectIndex;
	}

	
	public void setObjectIndex(String objectIndex) {
		this.objectIndex = objectIndex;
	}

	
	public double getPayAmount() {
		return payAmount;
	}

	
	public void setPayAmount(double payAmount) {
		this.payAmount = payAmount;
	}

	
	public double getAdjPayAmount() {
		return adjPayAmount;
	}

	
	public void setAdjPayAmount(double adjPayAmount) {
		this.adjPayAmount = adjPayAmount;
	}

	
	public double getLeftPayAmount() {
		return leftPayAmount;
	}

	
	public void setLeftPayAmount(double leftPayAmount) {
		this.leftPayAmount = leftPayAmount;
	}

	
	public double getLeftPay() {
		return leftPay;
	}

	
	public void setLeftPay(double leftPay) {
		this.leftPay = leftPay;
	}

	
	public double getSumRealPay() {
		return sumRealPay;
	}

	
	public void setSumRealPay(double sumRealPay) {
		this.sumRealPay = sumRealPay;
	}

	
	public String getBzCompensateText() {
		return bzCompensateText;
	}

	
	public void setBzCompensateText(String bzCompensateText) {
		this.bzCompensateText = bzCompensateText;
	}

	
	public String getFeeTypeCode() {
		return FeeTypeCode;
	}

	
	public void setFeeTypeCode(String feeTypeCode) {
		FeeTypeCode = feeTypeCode;
	}

	
	public String getLossItemType() {
		return lossItemType;
	}

	
	public void setLossItemType(String lossItemType) {
		this.lossItemType = lossItemType;
	}

	
	public String getBzDutyType() {
		return bzDutyType;
	}

	
	public void setBzDutyType(String bzDutyType) {
		this.bzDutyType = bzDutyType;
	}

	
	public double getSumLoss() {
		return sumLoss;
	}

	
	public void setSumLoss(double sumLoss) {
		this.sumLoss = sumLoss;
	}

	
	public String getLossName() {
		return lossName;
	}

	
	public void setLossName(String lossName) {
		this.lossName = lossName;
	}

	
	public String getInsteadFlag() {
		return insteadFlag;
	}

	
	public void setInsteadFlag(String insteadFlag) {
		this.insteadFlag = insteadFlag;
	}

	
	public String getExpType() {
		return expType;
	}

	
	public void setExpType(String expType) {
		this.expType = expType;
	}

	
	public String getLossFeeName() {
		return lossFeeName;
	}

	
	public void setLossFeeName(String lossFeeName) {
		this.lossFeeName = lossFeeName;
	}

	
	public double getPayCarAmout() {
		return payCarAmout;
	}

	
	public void setPayCarAmout(double payCarAmout) {
		this.payCarAmout = payCarAmout;
	}

	
	public double getCarLossSum() {
		return carLossSum;
	}

	
	public void setCarLossSum(double carLossSum) {
		this.carLossSum = carLossSum;
	}

	
	public double getCarPaySum() {
		return carPaySum;
	}

	
	public void setCarPaySum(double carPaySum) {
		this.carPaySum = carPaySum;
	}

	
	public double getPayCarAmount() {
		return payCarAmount;
	}

	
	public void setPayCarAmount(double payCarAmount) {
		this.payCarAmount = payCarAmount;
	}

	
	public String getDamageType() {
		return damageType;
	}

	
	public void setDamageType(String damageType) {
		this.damageType = damageType;
	}

	
	public String getInsureComCode() {
		return insureComCode;
	}

	
	public void setInsureComCode(String insureComCode) {
		this.insureComCode = insureComCode;
	}

	
	public double getRecoveryPay() {
		return recoveryPay;
	}

	
	public void setRecoveryPay(double recoveryPay) {
		this.recoveryPay = recoveryPay;
	}

	
	public double getNoRecoveryPay() {
		return noRecoveryPay;
	}

	
	public void setNoRecoveryPay(double noRecoveryPay) {
		this.noRecoveryPay = noRecoveryPay;
	}

	
	public String getDoubleLicenseNo() {
		return doubleLicenseNo;
	}

	
	public void setDoubleLicenseNo(String doubleLicenseNo) {
		this.doubleLicenseNo = doubleLicenseNo;
	}

	
	public String getZfFlag() {
		return zfFlag;
	}

	
	public void setZfFlag(String zfFlag) {
		this.zfFlag = zfFlag;
	}


	public Integer getSerialNo() {
		return serialNo;
	}


	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}


	public Boolean getBzDutyFlag() {
		return bzDutyFlag;
	}


	public void setBzDutyFlag(Boolean bzDutyFlag) {
		this.bzDutyFlag = bzDutyFlag;
	}
	
	public Object getLossItem() {
		return lossItem;
	}

	public void setLossItem(Object lossItem) {
		this.lossItem = lossItem;
	}
	
	public ThirdPartyLossInfo getThirdPartyLossInfo() {
		return thirdPartyLossInfo;
	}

	public void setThirdPartyLossInfo(ThirdPartyLossInfo thirdPartyLossInfo) {
		this.thirdPartyLossInfo = thirdPartyLossInfo;
	}
	
	public boolean isBzDutyType() {
		if(bzDutyType != null && "1".equals(bzDutyType)){
			return true;
		}else{
			return false;
		}
    }


	public Integer getObjectSerialNo() {
		return objectSerialNo;
	}


	public void setObjectSerialNo(Integer objectSerialNo) {
		this.objectSerialNo = objectSerialNo;
	}
}
