package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;

/**
 *  人员损失费用明细列表（隶属于人员损失情况）
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class PersonLossFeeDataVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 费用类型；参见代码 **/ 
	private String feeType; 

	/** 核损金额 **/ 
	private Double underDefLoss;

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public Double getUnderDefLoss() {
		return underDefLoss;
	}

	public void setUnderDefLoss(Double underDefLoss) {
		this.underDefLoss = underDefLoss;
	}
	
}
