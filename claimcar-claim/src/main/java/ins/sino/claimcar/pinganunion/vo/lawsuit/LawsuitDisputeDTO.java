package ins.sino.claimcar.pinganunion.vo.lawsuit;

import java.io.Serializable;
/**
 * 
 * @Description: 诉讼争议点信息
 * @author: zhubin
 * @date: 2020年8月3日 上午8:43:30
 */
public class LawsuitDisputeDTO implements Serializable{
	private static final long serialVersionUID = -7326524135161428564L;
	private String lawsuitNo;//诉讼号：
	private String lawsuitStage;//诉讼阶段：1-一审、2-二审
	private String disputePoint;//争议点：代码定义3.17
	private String winOrLoseLawsuit;//胜败诉情况 ：1胜诉、2部分胜诉、3败诉
	private String firstLoseLawsuitReason;//一级败诉原因：代码定义3.24
	private String secondLoseLawsuitReason;//二级败诉原因：代码定义3.24
	private String refuseDisputeReason;//拒赔原因争议：代码定义3.16
	private String refuseDisputeOtherReason;//拒赔原因为其他的原因：
	public String getLawsuitNo() {
		return lawsuitNo;
	}
	public void setLawsuitNo(String lawsuitNo) {
		this.lawsuitNo = lawsuitNo;
	}
	public String getLawsuitStage() {
		return lawsuitStage;
	}
	public void setLawsuitStage(String lawsuitStage) {
		this.lawsuitStage = lawsuitStage;
	}
	public String getDisputePoint() {
		return disputePoint;
	}
	public void setDisputePoint(String disputePoint) {
		this.disputePoint = disputePoint;
	}
	public String getWinOrLoseLawsuit() {
		return winOrLoseLawsuit;
	}
	public void setWinOrLoseLawsuit(String winOrLoseLawsuit) {
		this.winOrLoseLawsuit = winOrLoseLawsuit;
	}
	public String getFirstLoseLawsuitReason() {
		return firstLoseLawsuitReason;
	}
	public void setFirstLoseLawsuitReason(String firstLoseLawsuitReason) {
		this.firstLoseLawsuitReason = firstLoseLawsuitReason;
	}
	public String getSecondLoseLawsuitReason() {
		return secondLoseLawsuitReason;
	}
	public void setSecondLoseLawsuitReason(String secondLoseLawsuitReason) {
		this.secondLoseLawsuitReason = secondLoseLawsuitReason;
	}
	public String getRefuseDisputeReason() {
		return refuseDisputeReason;
	}
	public void setRefuseDisputeReason(String refuseDisputeReason) {
		this.refuseDisputeReason = refuseDisputeReason;
	}
	public String getRefuseDisputeOtherReason() {
		return refuseDisputeOtherReason;
	}
	public void setRefuseDisputeOtherReason(String refuseDisputeOtherReason) {
		this.refuseDisputeOtherReason = refuseDisputeOtherReason;
	}
	
	

}
