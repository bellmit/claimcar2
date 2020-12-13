package ins.sino.claimcar.claim.vo;

import java.util.ArrayList;
import java.util.List;

public class CompensateKindPay implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/** 险别代码*/
	private String kindCode;
	/** 损失分项*/
	private List compensateExpList = new ArrayList();
	/** 车辆实际价值*/
	private double damageItemRealCost;
	/** 承保险别分项保额*/
	private double damageAmount;
	/** 承保险别总保额*/
	private double totalDamageAmount;
	/** 险别总赔付金额*/
	private double kindSum;
	/** 险别总施救金额*/
	private double kindRescueSum;
	/** 超限额判断用的赔付金额,包含交强已赔付*/
	private double adjKindSum;
	/** 超限额判断用的施救金额,包含交强已赔付*/
	private double adjKindRescueSum;
	/** 单位保险金额*/
	private double unitAmount;
	/** 损失项个数*/
	private int kindLossNum;
	/**  险别总赔付金额 用来记录新增设备险下的无法找到第三方特约险损失的总损失，kindSum用来记录车损险及其他录入的无法找到第三方特约险损失的总损失 */
	private double kindSumForX;
	/** 险别总施救金额 用来记录新增设备险下的无法找到第三方特约险损失的总施救金额，kindSum用来记录车损险及其他录入的无法找到第三方特约险损失的总施救金额*/
	private double kindRescueSumForX;
	/** 损失项个数 用来记录新增设备险下的无法找到第三方特约险损失的总损失项个数，kindSum用来记录车损险及其他录入的无法找到第三方特约险损失的总损失项个数*/
	private int kindLossNumForX;
	
	public void addCompensateExp(CompensateExp compensateExp){
		compensateExpList.add(compensateExp);
	}
	
	public String getKindCode() {
		return kindCode;
	}
	
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}
	
	public List getCompensateExpList() {
		return compensateExpList;
	}
	
	public void setCompensateExpList(List compensateExpList) {
		this.compensateExpList = compensateExpList;
	}
	
	public double getDamageItemRealCost() {
		return damageItemRealCost;
	}
	
	public void setDamageItemRealCost(double damageItemRealCost) {
		this.damageItemRealCost = damageItemRealCost;
	}
	
	public double getDamageAmount() {
		return damageAmount;
	}
	
	public void setDamageAmount(double damageAmount) {
		this.damageAmount = damageAmount;
	}
	
	public double getTotalDamageAmount() {
		return totalDamageAmount;
	}
	
	public void setTotalDamageAmount(double totalDamageAmount) {
		this.totalDamageAmount = totalDamageAmount;
	}
	
	public double getKindSum() {
		return kindSum;
	}
	
	public void setKindSum(double kindSum) {
		this.kindSum = kindSum;
	}
	
	public double getKindRescueSum() {
		return kindRescueSum;
	}
	
	public void setKindRescueSum(double kindRescueSum) {
		this.kindRescueSum = kindRescueSum;
	}
	
	public double getAdjKindSum() {
		return adjKindSum;
	}
	
	public void setAdjKindSum(double adjKindSum) {
		this.adjKindSum = adjKindSum;
	}
	
	public double getAdjKindRescueSum() {
		return adjKindRescueSum;
	}
	
	public void setAdjKindRescueSum(double adjKindRescueSum) {
		this.adjKindRescueSum = adjKindRescueSum;
	}
	
	public double getUnitAmount() {
		return unitAmount;
	}
	
	public void setUnitAmount(double unitAmount) {
		this.unitAmount = unitAmount;
	}
	
	public int getKindLossNum() {
		return kindLossNum;
	}
	
	public void setKindLossNum(int kindLossNum) {
		this.kindLossNum = kindLossNum;
	}
	
	public double getKindSumForX() {
		return kindSumForX;
	}
	
	public void setKindSumForX(double kindSumForX) {
		this.kindSumForX = kindSumForX;
	}
	
	public double getKindRescueSumForX() {
		return kindRescueSumForX;
	}
	
	public void setKindRescueSumForX(double kindRescueSumForX) {
		this.kindRescueSumForX = kindRescueSumForX;
	}
	
	public int getKindLossNumForX() {
		return kindLossNumForX;
	}
	
	public void setKindLossNumForX(int kindLossNumForX) {
		this.kindLossNumForX = kindLossNumForX;
	}
	
}
