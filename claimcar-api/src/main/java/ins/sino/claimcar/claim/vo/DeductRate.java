package ins.sino.claimcar.claim.vo;

import java.io.Serializable;


public class DeductRate implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 免赔条件*/
	private String deductCond;
	/** 次数*/
	private int times;
	/** 免赔率*/
	private double deductRate;
	/** 不计免赔率*/
	private double exceptDeductRate;
	
	public String getDeductCond() {
		return deductCond;
	}
	
	public void setDeductCond(String deductCond) {
		this.deductCond = deductCond;
	}
	
	public int getTimes() {
		return times;
	}
	
	public void setTimes(int times) {
		this.times = times;
	}
	
	public double getDeductRate() {
		return deductRate;
	}
	
	public void setDeductRate(double deductRate) {
		this.deductRate = deductRate;
	}
	
	public double getExceptDeductRate() {
		return exceptDeductRate;
	}
	
	public void setExceptDeductRate(double exceptDeductRate) {
		this.exceptDeductRate = exceptDeductRate;
	}
	
	

}
