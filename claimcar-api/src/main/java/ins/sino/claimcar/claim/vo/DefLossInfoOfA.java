package ins.sino.claimcar.claim.vo;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 属于本车的定损信息 
 * @author Administrator
 *
 */
public class DefLossInfoOfA implements Serializable{
	private static final long serialVersionUID = 1L;
	
     /** 损失项的名称 车 ：车牌号  财：财物名称  人：人名名称*/
	private String lossName;
	 /** 涉案id */
	private String prpLthirdPartyId;
	 /** 残值*/
	private double sumRest;
	 /** 定损金额*/
	private BigDecimal sumLoss;
	 /** 伤情类型*/
	private String woundCode;
	 /** 施救费*/
	private BigDecimal rescueFee;
	/** 费用代码*/
	private String chargeCode;
	/** 标的序号 */
	private Integer itemKindNo;
	/**唯一索引*/
	private Long lossIndex;
	
	public String getLossName() {
		return lossName;
	}
	
	public void setLossName(String lossName) {
		this.lossName = lossName;
	}
	
	public String getPrpLthirdPartyId() {
		return prpLthirdPartyId;
	}
	
	public void setPrpLthirdPartyId(String prpLthirdPartyId) {
		this.prpLthirdPartyId = prpLthirdPartyId;
	}
	
	public double getSumRest() {
		return sumRest;
	}
	
	public void setSumRest(double sumRest) {
		this.sumRest = sumRest;
	}
	
	public BigDecimal getSumLoss() {
		return sumLoss;
	}
	
	public void setSumLoss(BigDecimal sumLoss) {
		this.sumLoss = sumLoss;
	}
	
	public String getWoundCode() {
		return woundCode;
	}
	
	public void setWoundCode(String woundCode) {
		this.woundCode = woundCode;
	}
	
	public BigDecimal getRescueFee() {
		return rescueFee;
	}
	
	public void setRescueFee(BigDecimal rescueFee) {
		this.rescueFee = rescueFee;
	}
	
	public String getChargeCode() {
		return chargeCode;
	}
	
	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}
	
	public Integer getItemKindNo() {
		return itemKindNo;
	}
	
	public void setItemKindNo(Integer itemKindNo) {
		this.itemKindNo = itemKindNo;
	}
	
	public Long getLossIndex() {
		return lossIndex;
	}
	
	public void setLossIndex(Long lossIndex) {
		this.lossIndex = lossIndex;
	}
	
	

}
