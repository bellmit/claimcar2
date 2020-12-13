package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;

/**
 *  开始追偿确认查询返回页面VO
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class CheckPersonDataVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 伤亡类型 **/ 
	private String personPayType; 

	/** 估损金额 **/ 
	private Double estimateAmount;

	private Double estimatedLossAmount;//估损金额
	public String getPersonPayType() {
		return personPayType;
	}

	public void setPersonPayType(String personPayType) {
		this.personPayType = personPayType;
	}

	public Double getEstimateAmount() {
		return estimateAmount;
	}

	public void setEstimateAmount(Double estimateAmount) {
		this.estimateAmount = estimateAmount;
	}

	public Double getEstimatedLossAmount() {
		return estimatedLossAmount;
	}

	public void setEstimatedLossAmount(Double estimatedLossAmount) {
		this.estimatedLossAmount = estimatedLossAmount;
	}

	

}
