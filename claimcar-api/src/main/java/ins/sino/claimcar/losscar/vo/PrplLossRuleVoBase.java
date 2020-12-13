package ins.sino.claimcar.losscar.vo;


public class PrplLossRuleVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Integer reportAllRuleScore;
	private Integer evalAllRuleScore;
	private Integer evalRuleNumbers;
	private Integer pointRuleNumbers;
	private PrplLossRuleSubVo prplLossRuleSubVo;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getReportAllRuleScore() {
		return reportAllRuleScore;
	}
	
	public void setReportAllRuleScore(Integer reportAllRuleScore) {
		this.reportAllRuleScore = reportAllRuleScore;
	}
	
	public Integer getEvalAllRuleScore() {
		return evalAllRuleScore;
	}
	
	public void setEvalAllRuleScore(Integer evalAllRuleScore) {
		this.evalAllRuleScore = evalAllRuleScore;
	}
	
	public Integer getEvalRuleNumbers() {
		return evalRuleNumbers;
	}
	
	public void setEvalRuleNumbers(Integer evalRuleNumbers) {
		this.evalRuleNumbers = evalRuleNumbers;
	}
	
	public Integer getPointRuleNumbers() {
		return pointRuleNumbers;
	}
	
	public void setPointRuleNumbers(Integer pointRuleNumbers) {
		this.pointRuleNumbers = pointRuleNumbers;
	}

	public PrplLossRuleSubVo getPrplLossRuleSubVo() {
		return prplLossRuleSubVo;
	}

	public void setPrplLossRuleSubVo(PrplLossRuleSubVo prplLossRuleSubVo) {
		this.prplLossRuleSubVo = prplLossRuleSubVo;
	}
}
