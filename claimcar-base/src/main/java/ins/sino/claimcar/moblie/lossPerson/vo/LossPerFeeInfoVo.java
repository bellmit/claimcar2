
package ins.sino.claimcar.moblie.lossPerson.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("FEEINFO")
public class LossPerFeeInfoVo implements Serializable {
	private static final long serialVersionUID = -1528493102386689963L;

	@XStreamAlias("FEENAME")
	protected String feeName;
	@XStreamAlias("ESTIMATEDLOSS")
	protected String estimateDloss;
	@XStreamAlias("CLAIMAMOUNT")
	protected String claimAmount;
	@XStreamAlias("COSTFORMULA")
	protected String costFormula;
	@XStreamAlias("FIXEDLOSSAMOUNT")
	protected String fixedLossAmount;
	@XStreamAlias("IMPAIRMENTAMOUNT")
	protected String impairmentAmount;
	@XStreamAlias("FEEDESCRIPTION")
	protected String feeDescription;

	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}

	public String getEstimateDloss() {
		return estimateDloss;
	}

	public void setEstimateDloss(String estimateDloss) {
		this.estimateDloss = estimateDloss;
	}

	public String getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(String claimAmount) {
		this.claimAmount = claimAmount;
	}

	public String getCostFormula() {
		return costFormula;
	}

	public void setCostFormula(String costFormula) {
		this.costFormula = costFormula;
	}

	public String getFixedLossAmount() {
		return fixedLossAmount;
	}

	public void setFixedLossAmount(String fixedLossAmount) {
		this.fixedLossAmount = fixedLossAmount;
	}

	public String getImpairmentAmount() {
		return impairmentAmount;
	}

	public void setImpairmentAmount(String impairmentAmount) {
		this.impairmentAmount = impairmentAmount;
	}

	public String getFeeDescription() {
		return feeDescription;
	}

	public void setFeeDescription(String feeDescription) {
		this.feeDescription = feeDescription;
	}

}
