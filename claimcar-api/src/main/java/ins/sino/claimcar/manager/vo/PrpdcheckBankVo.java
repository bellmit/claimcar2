package ins.sino.claimcar.manager.vo;

import java.io.Serializable;

public class PrpdcheckBankVo extends PrpdcheckBankVoBase implements Serializable{
private static final long serialVersionUID = 1L; 
	
	private String remark;//查勘费摘要
	
	private Long checkMianId;//查勘银行主表Id

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getCheckMianId() {
		return checkMianId;
	}

	public void setCheckMianId(Long checkMianId) {
		this.checkMianId = checkMianId;
	}
	
	
    

}
