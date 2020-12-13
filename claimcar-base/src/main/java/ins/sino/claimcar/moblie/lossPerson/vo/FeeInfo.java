package ins.sino.claimcar.moblie.lossPerson.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 跟踪费用记录信息
 * @author j2eel
 *
 */
@XStreamAlias("FEEINFO")
public class FeeInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	@XStreamAlias("ID")
	private String id;                  //主键
	@XStreamAlias("FEENAME")
	private String feeName;				//费用名称
	@XStreamAlias("ESTIMATEDLOSS")
	private String estimatedLoss;		//估损金额
	@XStreamAlias("CLAIMAMOUNT")
	private String claimAmount;			//索赔金额
	@XStreamAlias("COSTFORMULA")
	private String costFormula;			//费用公式
	@XStreamAlias("FIXEDLOSSAMOUNT")
	private String fixedLossAmount;		//定损金额
	@XStreamAlias("IMPAIRMENTAMOUNT")
	private String impairmentAmount;	//减损金额
	@XStreamAlias("FEEDESCRIPTION")
	private String feedEscription;      //费用说明
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFeeName() {
		return feeName;
	}
	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}
	public String getEstimatedLoss() {
		return estimatedLoss;
	}
	public void setEstimatedLoss(String estimatedLoss) {
		this.estimatedLoss = estimatedLoss;
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
	public String getFeedEscription() {
		return feedEscription;
	}
	public void setFeedEscription(String feedEscription) {
		this.feedEscription = feedEscription;
	}

}
