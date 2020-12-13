package ins.sino.claimcar.hnbxrest.vo;

import java.math.BigDecimal;


public class CasecarfeeVo implements java.io.Serializable {
	  
	private static final long serialVersionUID = 1L;
    private String casecarno;//案件车牌号
    private BigDecimal feepaymoney;//定损金额
    private String feetime;//定损时间
    private String feeinstructions;//定损说明
    
    public String getCasecarno() {
        return casecarno;
    }
    
    public void setCasecarno(String casecarno) {
        this.casecarno = casecarno;
    }
    
    
    public String getFeetime() {
        return feetime;
    }
    
    public void setFeetime(String feetime) {
        this.feetime = feetime;
    }
    
    public String getFeeinstructions() {
        return feeinstructions;
    }
    
    public void setFeeinstructions(String feeinstructions) {
        this.feeinstructions = feeinstructions;
    }

	public BigDecimal getFeepaymoney() {
		return feepaymoney;
	}

	public void setFeepaymoney(BigDecimal feepaymoney) {
		this.feepaymoney = feepaymoney;
	}
    
}
