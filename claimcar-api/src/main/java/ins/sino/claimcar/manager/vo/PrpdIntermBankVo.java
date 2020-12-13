package ins.sino.claimcar.manager.vo;

import java.util.Date;

/**
 * Custom VO class of PO PrpdIntermBank
 */ 
public class PrpdIntermBankVo extends PrpdIntermBankVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String remark;//公估费摘要
	
	private Long intermMianId;//公估银行主表Id
    
	
	public Long getIntermMianId() {
		return intermMianId;
	}

	public void setIntermMianId(Long intermMianId) {
		this.intermMianId = intermMianId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	 
}
