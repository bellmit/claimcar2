package ins.sino.claimcar.claim.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ThirdPartyInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 涉案车辆车牌号*/
	private String licenseNo;
	/** 车辆序号 */
	private Integer serialNo;
	
	/** 涉案车辆责任限额*/
	private double dutyAmount;
	/** 赔付其它车辆金额*/
	private List thirdPartyCompInfos = new ArrayList();
	/** 限额计算后赔付金额*/
	private double dutyCompPay;
	/** 限额比例赔偿金额*/
	private double payAmount;
	/** 超限额调整赔偿金额*/
	private double adjPayAmount;
	/** 剩余限额赔偿金额*/
	private double leftPayAmount;
	/** 剩余限额*/
	private double leftAmount;
	/** 未赔足损失项合计*/
	private double sumNoenoughPay;
	/** 涉案车辆责任类型*/
	private String bzDutyType;
	
	private Boolean bzDutyFlag =false;
	/** 本车标志位*/
	private String lossItemType;
	/** 涉案车辆承保类型*/
	private String insuredFlag;
	/** 合计赔付金额*/
	private double sumRealPay;
	/** 赔付比例*/
	private double claimRate;
	/** 车辆的损失个数*/
	private double carLossNum;
	/** 车辆总损失金额*/
	private double carLossSum;
	/** 超限额标志*/
	private String overRideFlag;
	/** 车辆总损失金额(未试算)*/
	private double carPaySum;
	
	/** 2012-02-06 代位求偿需求添加 */
	/** 责任对方追偿金额 */
	private double recoveryPay;
	/** 是否理算过 **/
	private boolean haveCal =false;
	/**  无责代赔是否赔付  默认扣除100 交强险理算三者车根据页面信息，
	 * 没有录入无责方保险公司或选择不扣 则考虑不扣*/
	private String nodutyPayFlag="1";
	
	private Map exceptPayAmountMap = new HashMap(0);

	//String objectIndex, double payAmount, String bzCompensateText, String lossItemType, String bzDutyType, Double sumLoss, String lossName, String objectLicenseNo, String expType, String lossFeeName, CompensateExp compensateExp) {
	
	@SuppressWarnings("unchecked")
	public void addThirdPartyPayAmount(double payAmount, String bzCompensateText, ThirdPartyLossInfo lossInfoVo, CompensateExp compensateExp) {
		ThirdPartyCompInfo thirdPartyCompInfo = new ThirdPartyCompInfo();
		thirdPartyCompInfo.setObjectIndex(lossInfoVo.getObjectIndex());
		thirdPartyCompInfo.setLicenseNo(licenseNo);
//		if (compensateExp.getPayType() != null && "1".equals(compensateExp.getPayType().trim())) {
//			if (exceptPayAmountMap.get(licenseNo) != null) {
//				thirdPartyCompInfo.setDoubleLicenseNo("true");// 重复的车牌号
//				thirdPartyCompInfo.setPayAmount(0.00);
//			} else {
//				thirdPartyCompInfo.setDoubleLicenseNo("false");// 非重复的车牌号
//				thirdPartyCompInfo.setPayAmount(payAmount);
//				exceptPayAmountMap.put(licenseNo, licenseNo);
//			}
//		} else {
			thirdPartyCompInfo.setDoubleLicenseNo("false");// 非重复的车牌号
			thirdPartyCompInfo.setPayAmount(payAmount);
//		}
		thirdPartyCompInfo.setBzCompensateText(bzCompensateText);
		thirdPartyCompInfo.setLossItemType(lossItemType);
		thirdPartyCompInfo.setBzDutyType(lossInfoVo.getBzDutyType());
		if(lossInfoVo.getBzDutyType()!=null && lossInfoVo.getBzDutyType().trim().equals("1")) {
			thirdPartyCompInfo.setBzDutyFlag(true);
    	}
		
		if (bzDutyType == null) {
			thirdPartyCompInfo.setBzDutyType("null");
		}
		if (lossItemType == null) {
			thirdPartyCompInfo.setLossItemType("null");
		}
		thirdPartyCompInfo.setSumLoss(lossInfoVo.getSumLoss());
		thirdPartyCompInfo.setLossName(lossInfoVo.getLossName());
		thirdPartyCompInfo.setExpType(lossInfoVo.getExpType());
		thirdPartyCompInfo.setLossFeeName(lossInfoVo.getLossFeeName());
		thirdPartyCompInfo.setDamageType(compensateExp.getDamageType());
		thirdPartyCompInfo.setSerialNo(lossInfoVo.getSerialNo());
		thirdPartyCompInfo.setPayCarAmount(this.dutyAmount);
		thirdPartyCompInfo.setRecoveryPay(this.recoveryPay);
		thirdPartyCompInfo.setObjectLicenseNo(lossInfoVo.getLicenseNo());
		for(int i=0;i<compensateExp.getThirdPartyLossInfos().size();i++){
			ThirdPartyLossInfo thirdPartyLossInfo = (ThirdPartyLossInfo) compensateExp.getThirdPartyLossInfos().get(i);
			if (lossInfoVo.getObjectIndex().equals(thirdPartyLossInfo.getObjectIndex())) {
				thirdPartyCompInfo.setInsureComCode(thirdPartyLossInfo.getInsureComCode());
				thirdPartyCompInfo.setRecoveryPay(thirdPartyLossInfo.getRecoveryPay());
				thirdPartyCompInfo.setZfFlag(thirdPartyLossInfo.getZfFlag());
			}
		}
		thirdPartyCompInfos.add(thirdPartyCompInfo);
	}
	
	  @SuppressWarnings("unchecked")
	    public void addThirdPartyPayAmount(double payAmount,String bzCompensateText,ThirdPartyLossInfo lossInfoVo) {
	        ThirdPartyCompInfo thirdPartyCompInfo = new ThirdPartyCompInfo();
	        thirdPartyCompInfo.setObjectIndex(lossInfoVo.getObjectIndex());
	        thirdPartyCompInfo.setLicenseNo(licenseNo);
	        thirdPartyCompInfo.setDoubleLicenseNo("false"); // 非重复的车牌号
	        thirdPartyCompInfo.setPayAmount(payAmount);
	        thirdPartyCompInfo.setBzCompensateText(bzCompensateText);
	        thirdPartyCompInfo.setLossItemType(lossItemType);
	        thirdPartyCompInfo.setBzDutyFlag(lossInfoVo.isBzDutyType());
	        thirdPartyCompInfo.setBzDutyType(lossInfoVo.getBzDutyType());
//	        if(lossInfoVo.isBzDutyType()){
//	        	thirdPartyCompInfo.setBzDutyType("1");
//	        }else{
//	        	thirdPartyCompInfo.setBzDutyType("0");
//	        }
	        thirdPartyCompInfo.setSerialNo(lossInfoVo.getSerialNo());
	        thirdPartyCompInfo.setSumLoss(lossInfoVo.getSumLoss());
	        thirdPartyCompInfo.setLossName(lossInfoVo.getLossName());
	        thirdPartyCompInfo.setExpType(lossInfoVo.getExpType());
	        thirdPartyCompInfo.setLossFeeName(lossInfoVo.getLossFeeName());
	        thirdPartyCompInfo.setDamageType(lossInfoVo.getDamageType());
	        thirdPartyCompInfo.setPayCarAmount(this.dutyAmount);
	        thirdPartyCompInfo.setObjectLicenseNo(lossInfoVo.getLicenseNo());
	        thirdPartyCompInfo.setLossItem(lossInfoVo.getLossItem());
	        thirdPartyCompInfo.setRecoveryPay(recoveryPay);
	        thirdPartyCompInfo.setZfFlag(lossInfoVo.getZfFlag());
	        thirdPartyCompInfo.setThirdPartyLossInfo(lossInfoVo);
	        
	        thirdPartyCompInfos.add(thirdPartyCompInfo);
	    }
	
	public void deleteThirdPartyPayAmount(){
		thirdPartyCompInfos.clear();
	}
	public void deleteOneThirdPartyPayAmount(ThirdPartyCompInfo thirdPartyCompInfo) {
		thirdPartyCompInfos.remove(thirdPartyCompInfo);
	}
	public ThirdPartyCompInfo getThirdPartyCompInfo(int objectIndex){
		return (ThirdPartyCompInfo)thirdPartyCompInfos.get(objectIndex);
	}
	
	public String getLicenseNo() {
		return licenseNo;
	}

	
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	
	public double getDutyAmount() {
		return dutyAmount;
	}

	
	public void setDutyAmount(double dutyAmount) {
		this.dutyAmount = dutyAmount;
	}

	
	public List getThirdPartyCompInfos() {
		return thirdPartyCompInfos;
	}

	
	public void setThirdPartyCompInfos(List thirdPartyCompInfos) {
		this.thirdPartyCompInfos = thirdPartyCompInfos;
	}

	
	public double getDutyCompPay() {
		return dutyCompPay;
	}

	
	public void setDutyCompPay(double dutyCompPay) {
		this.dutyCompPay = dutyCompPay;
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

	
	public double getLeftAmount() {
		return leftAmount;
	}

	
	public void setLeftAmount(double leftAmount) {
		this.leftAmount = leftAmount;
	}

	
	public double getSumNoenoughPay() {
		return sumNoenoughPay;
	}

	
	public void setSumNoenoughPay(double sumNoenoughPay) {
		this.sumNoenoughPay = sumNoenoughPay;
	}

	
	public String getBzDutyType() {
		return bzDutyType;
	}

	
	public void setBzDutyType(String bzDutyType) {
		this.bzDutyType = bzDutyType;
	}

	
	public String getLossItemType() {
		return lossItemType;
	}

	
	public void setLossItemType(String lossItemType) {
		this.lossItemType = lossItemType;
	}

	
	public String getInsuredFlag() {
		return insuredFlag;
	}

	
	public void setInsuredFlag(String insuredFlag) {
		this.insuredFlag = insuredFlag;
	}

	
	public double getSumRealPay() {
		return sumRealPay;
	}

	
	public void setSumRealPay(double sumRealPay) {
		this.sumRealPay = sumRealPay;
	}

	
	public double getClaimRate() {
		return claimRate;
	}

	
	public void setClaimRate(double claimRate) {
		this.claimRate = claimRate;
	}

	
	public double getCarLossNum() {
		return carLossNum;
	}

	
	public void setCarLossNum(double carLossNum) {
		this.carLossNum = carLossNum;
	}

	
	public double getCarLossSum() {
		return carLossSum;
	}

	
	public void setCarLossSum(double carLossSum) {
		this.carLossSum = carLossSum;
	}

	
	public String getOverRideFlag() {
		return overRideFlag;
	}

	
	public void setOverRideFlag(String overRideFlag) {
		this.overRideFlag = overRideFlag;
	}

	
	public double getCarPaySum() {
		return carPaySum;
	}

	
	public void setCarPaySum(double carPaySum) {
		this.carPaySum = carPaySum;
	}

	
	public double getRecoveryPay() {
		return recoveryPay;
	}

	
	public void setRecoveryPay(double recoveryPay) {
		this.recoveryPay = recoveryPay;
	}

	
	public Map getExceptPayAmountMap() {
		return exceptPayAmountMap;
	}

	
	public void setExceptPayAmountMap(Map exceptPayAmountMap) {
		this.exceptPayAmountMap = exceptPayAmountMap;
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
	
	
	public boolean isBzDutyType(){
		if(bzDutyType!=null && bzDutyType.trim().equals("1")){
			return true;
		}else{
			return false;
		}
	}

	public boolean isHaveCal() {
		return haveCal;
	}

	public void setHaveCal(boolean haveCal) {
		this.haveCal = haveCal;
	}

	public String getNodutyPayFlag() {
		return nodutyPayFlag;
	}

	public void setNodutyPayFlag(String nodutyPayFlag) {
		this.nodutyPayFlag = nodutyPayFlag;
	}
}
