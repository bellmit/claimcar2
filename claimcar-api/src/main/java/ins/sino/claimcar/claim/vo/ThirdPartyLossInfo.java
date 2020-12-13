package ins.sino.claimcar.claim.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ThirdPartyLossInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 车牌号码*/
	private String licenseNo;
	/** 车牌序号*/
	private Integer serialNo;
	/** 核定损金额*/
	private double sumLoss;
	/** 单位限额*/
	private double damageAmount;
	/** 本车应赔付金额*/
	private double itemCarRealPay;
	/** 赔付其它车辆金额*/
	private List thirdPartyCompInfos = new ArrayList();
	/** 赔付类型*/
	private String objectType;
	/** 人员损失序号*/
	private String lossIndex;
	/** 损失项序号*/
	private String objectIndex;
	/** 涉案车辆限额赔付金额*/
	private double thirdPartyLimitPay;
	/** 实赔金额*/
	private double sumRealPay;
	/** 理算公式*/
	private String bzCompensateText="";
	/** 损失项名称  车：车牌号  财：财物名称  人：人名*/
	private String lossName;
	/** 损失费用名称  车/财："定损金额"  人：费用名称*/
	private String lossFeeName;
	/** 费用类型*/
	private String FeeTypeCode;
	/** 本车标志位*/
	private String lossItemType;
	/** 归属涉案车辆责任类型*/
	private String bzDutyType;
	
	private Boolean bzDutyFlag;
	/** 无责方赔付金额*/
	private Double noDutyCarPay =0D;
	/** 代赔标志位 1:无责代赔 2:无保单代赔*/
	private String insteadFlag;
	/** 车财产标志位 1:车 2:财*/
	private String expType;
	/** 车辆总损失金额*/
	private double carLossSum;
	/** 车辆总损失金额*/
	private double carPaySum;
	/** 交强赔付类型49   51   52*/
	private String damageType;
	/** 裁定金额*/
	private double sumRealPayJud;
	/** 交强计算书诉讼案格式*/
	private String suitCompensateExp;
	/** 交强计算书旧格式 */
	private String oldCompensateExp;
	/** 超限额标志为(交强计算书旧格式) */
	private String overFlag;
	/** 理算金额(未超限额的金额取出来供--16按损失项顺序返回实赔规则使用) */
	private double sumDefLoss;
	/** 赔付车辆限额 */
	private double payCarAmout;
	/** 分摊责任限额之和(所有限额) */
	private double allAmount;
	/** 供CompensateExp中使用,remove之后是否add */
	private String isAdd = "0";
	/** 协议金额*/
	private double exgratiaFee;
	
	/** add by zhouwen 2009-04-16 军车交强赔付方案时添加*/
	/** 军车车损及车上财产损失视为路面财产损失进行理算,军车车上人员视为路面人员进行理算.规则中需要按承保机构11进行区分.*/
	/** 承保机构*/
	private String insureComCode;
	
	/** 2012-02-06 代位求偿需求添加 */
	/** 责任对方追偿款 */
	private double recoveryPay;
	
	/** 乘追偿比例前实赔金额 */
	private double noRecoveryPay;
	
	/** 商业计算书：标的车代责任对方交强车辆*/
	private List thirdPartyRecoveryInfoAry = new ArrayList();
	
	/** 代位求偿,商业计算书,自付标志 */
	private String zfFlag;
	private double sumRest;//残值
	private double rescueFee;
	private String rescueFeeFlag; //施救费标志 1 表示sumLoss为施救费，0或null表示sumLoss为损失金额-残值
	private Integer itemKindNo;//标的序号
	private double noPolicySumRealPaid;//无保代赔金额
	private String roundFlag;//四舍五入标志 2:五入 1：四舍 0：不需四舍五入
	private String roundFlagExp;//每个理算大对象四舍五入标志 2:五入 1：四舍 0：不需四舍五入
	/** 损失项序号*/
	private String objectIndexForAll;//objectIndex的复制
	/** 车辆、财产定损主表id 车辆 prpLprpLdefLossMainId 财产prpLpropMainId */
	private String prpLpropOrCarMainId;//商业险扣交强使用
	/** 对损失进行排序*/
	private Integer index;//商业险扣交强使用
	/** 险别代码 */
	private String kindCode;//商业险扣交强使用
	
	private String recoveryOrPayFlag; // 扣交强使用
	
    private Object lossItem;
    
    private double sumPrePaid = 0;
    
    private boolean isSameCompensate = false;
    
    private double bzRestAmount = 0;

	@SuppressWarnings("unchecked")
	public void addThirdPartyPayAmount(String localLicenseNo, String objectIndex, double adjPayAmount, String bzCompensateText,Integer serialNo) {
		ThirdPartyCompInfo thirdPartyCompInfo = new ThirdPartyCompInfo();
		thirdPartyCompInfo.setSerialNo(serialNo);
		thirdPartyCompInfo.setObjectIndex(objectIndex);
		thirdPartyCompInfo.setLicenseNo(localLicenseNo);
		thirdPartyCompInfo.setAdjPayAmount(adjPayAmount);
		thirdPartyCompInfo.setBzCompensateText(bzCompensateText);
		thirdPartyCompInfos.add(thirdPartyCompInfo);
	}
	
    @SuppressWarnings("unchecked")
    public void addThirdPartyPayAmount(String localLicenseNo, 
                                       String objectIndex, 
                                       double adjPayAmount,
                                       String bzCompensateText,
                                       String insteadFlag,
                                       Integer serialNo) {
        ThirdPartyCompInfo thirdPartyCompInfo = new ThirdPartyCompInfo();
        thirdPartyCompInfo.setObjectIndex(objectIndex);
        thirdPartyCompInfo.setLicenseNo(localLicenseNo);
        thirdPartyCompInfo.setAdjPayAmount(adjPayAmount);
        thirdPartyCompInfo.setBzCompensateText(bzCompensateText);
        thirdPartyCompInfo.setInsteadFlag(insteadFlag);
        thirdPartyCompInfo.setSerialNo(serialNo);
        thirdPartyCompInfos.add(thirdPartyCompInfo);
    }
	
	public String getLicenseNo() {
		return licenseNo;
	}

	
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	
	public double getSumLoss() {
		return sumLoss;
	}

	
	public void setSumLoss(double sumLoss) {
		this.sumLoss = sumLoss;
	}

	
	public double getDamageAmount() {
		return damageAmount;
	}

	
	public void setDamageAmount(double damageAmount) {
		this.damageAmount = damageAmount;
	}

	
	public double getItemCarRealPay() {
		return itemCarRealPay;
	}

	
	public void setItemCarRealPay(double itemCarRealPay) {
		this.itemCarRealPay = itemCarRealPay;
	}

	
	public List getThirdPartyCompInfos() {
		return thirdPartyCompInfos;
	}

	
	public void setThirdPartyCompInfos(List thirdPartyCompInfos) {
		this.thirdPartyCompInfos = thirdPartyCompInfos;
	}

	
	public String getObjectType() {
		return objectType;
	}

	
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	
	public String getLossIndex() {
		return lossIndex;
	}

	
	public void setLossIndex(String lossIndex) {
		this.lossIndex = lossIndex;
	}

	
	public String getObjectIndex() {
		return objectIndex;
	}

	
	public void setObjectIndex(String objectIndex) {
		this.objectIndex = objectIndex;
	}

	
	public double getThirdPartyLimitPay() {
		return thirdPartyLimitPay;
	}

	
	public void setThirdPartyLimitPay(double thirdPartyLimitPay) {
		this.thirdPartyLimitPay = thirdPartyLimitPay;
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

	
	public String getLossName() {
		return lossName;
	}

	
	public void setLossName(String lossName) {
		this.lossName = lossName;
	}

	
	public String getLossFeeName() {
		return lossFeeName;
	}

	
	public void setLossFeeName(String lossFeeName) {
		this.lossFeeName = lossFeeName;
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

	
	public Double getNoDutyCarPay() {
		return noDutyCarPay;
	}

	
	public void setNoDutyCarPay(Double noDutyCarPay) {
		this.noDutyCarPay = noDutyCarPay;
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

	
	public String getDamageType() {
		return damageType;
	}

	
	public void setDamageType(String damageType) {
		this.damageType = damageType;
	}

	
	public double getSumRealPayJud() {
		return sumRealPayJud;
	}

	
	public void setSumRealPayJud(double sumRealPayJud) {
		this.sumRealPayJud = sumRealPayJud;
	}

	
	public String getSuitCompensateExp() {
		return suitCompensateExp;
	}

	
	public void setSuitCompensateExp(String suitCompensateExp) {
		this.suitCompensateExp = suitCompensateExp;
	}

	
	public String getOldCompensateExp() {
		return oldCompensateExp;
	}

	
	public void setOldCompensateExp(String oldCompensateExp) {
		this.oldCompensateExp = oldCompensateExp;
	}

	
	public String getOverFlag() {
		return overFlag;
	}

	
	public void setOverFlag(String overFlag) {
		this.overFlag = overFlag;
	}

	
	public double getSumDefLoss() {
		return sumDefLoss;
	}

	
	public void setSumDefLoss(double sumDefLoss) {
		this.sumDefLoss = sumDefLoss;
	}

	
	public double getPayCarAmout() {
		return payCarAmout;
	}

	
	public void setPayCarAmout(double payCarAmout) {
		this.payCarAmout = payCarAmout;
	}

	
	public double getAllAmount() {
		return allAmount;
	}

	
	public void setAllAmount(double allAmount) {
		this.allAmount = allAmount;
	}

	
	public String getIsAdd() {
		return isAdd;
	}

	
	public void setIsAdd(String isAdd) {
		this.isAdd = isAdd;
	}

	
	public double getExgratiaFee() {
		return exgratiaFee;
	}

	
	public void setExgratiaFee(double exgratiaFee) {
		this.exgratiaFee = exgratiaFee;
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

	
	public List getThirdPartyRecoveryInfoAry() {
		return thirdPartyRecoveryInfoAry;
	}

	
	public void setThirdPartyRecoveryInfoAry(List thirdPartyRecoveryInfoAry) {
		this.thirdPartyRecoveryInfoAry = thirdPartyRecoveryInfoAry;
	}

	
	public String getZfFlag() {
		return zfFlag;
	}

	
	public void setZfFlag(String zfFlag) {
		this.zfFlag = zfFlag;
	}

	
	public double getSumRest() {
		return sumRest;
	}

	
	public void setSumRest(double sumRest) {
		this.sumRest = sumRest;
	}

	
	public String getRescueFeeFlag() {
		return rescueFeeFlag;
	}

	
	public void setRescueFeeFlag(String rescueFeeFlag) {
		this.rescueFeeFlag = rescueFeeFlag;
	}

	
	public Integer getItemKindNo() {
		return itemKindNo;
	}

	
	public void setItemKindNo(Integer itemKindNo) {
		this.itemKindNo = itemKindNo;
	}

	
	public double getNoPolicySumRealPaid() {
		return noPolicySumRealPaid;
	}

	
	public void setNoPolicySumRealPaid(double noPolicySumRealPaid) {
		this.noPolicySumRealPaid = noPolicySumRealPaid;
	}

	
	public String getRoundFlag() {
		return roundFlag;
	}

	
	public void setRoundFlag(String roundFlag) {
		this.roundFlag = roundFlag;
	}

	
	public String getRoundFlagExp() {
		return roundFlagExp;
	}

	
	public void setRoundFlagExp(String roundFlagExp) {
		this.roundFlagExp = roundFlagExp;
	}

	
	public String getObjectIndexForAll() {
		return objectIndexForAll;
	}

	
	public void setObjectIndexForAll(String objectIndexForAll) {
		this.objectIndexForAll = objectIndexForAll;
	}

	
	public String getPrpLpropOrCarMainId() {
		return prpLpropOrCarMainId;
	}

	
	public void setPrpLpropOrCarMainId(String prpLpropOrCarMainId) {
		this.prpLpropOrCarMainId = prpLpropOrCarMainId;
	}

	
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getKindCode() {
		return kindCode;
	}
	
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}
	
	public String getRecoveryOrPayFlag() {
		return recoveryOrPayFlag;
	}
	
	public void setRecoveryOrPayFlag(String recoveryOrPayFlag) {
		this.recoveryOrPayFlag = recoveryOrPayFlag;
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
	
	public boolean setBzDutyFlag(){
		return bzDutyFlag;
	}

	public Object getLossItem() {
		return lossItem;
	}

	public void setLossItem(Object lossItem) {
		this.lossItem = lossItem;
	}

	public double getSumPrePaid() {
		return sumPrePaid;
	}
	
	public void setSumPrePaid(double sumPrePaid) {
		this.sumPrePaid = sumPrePaid;
	}

	public boolean isSameCompensate() {
		return isSameCompensate;
	}

	public void setSameCompensate(boolean isSameCompensate) {
		this.isSameCompensate = isSameCompensate;
	}
	
    public boolean isIsSameCompensate() {
        return isSameCompensate;
    }

	public double getBzRestAmount() {
		return bzRestAmount;
	}

	public void setBzRestAmount(double bzRestAmount) {
		this.bzRestAmount = bzRestAmount;
	}
	
	public boolean isBzDutyType(){
		if(bzDutyType!=null && bzDutyType.trim().equals("1")){
			return true;
		}else{
			return false;
		}
	}

	public double getRescueFee() {
		return rescueFee;
	}

	public void setRescueFee(double rescueFee) {
		this.rescueFee = rescueFee;
	}
    
}
