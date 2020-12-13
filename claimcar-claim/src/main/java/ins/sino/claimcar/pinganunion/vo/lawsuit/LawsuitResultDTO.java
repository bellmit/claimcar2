package ins.sino.claimcar.pinganunion.vo.lawsuit;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
/**
 * 
 * @Description: 审判结果信息
 * @author: zhubin
 * @date: 2020年7月31日 下午6:48:11
 */
public class LawsuitResultDTO implements Serializable{
	private static final long serialVersionUID = -4918071457532922146L;
	private String lawsuitNo;//诉讼号
	private String appealOpinion;//是否上诉：1上诉、2不上诉
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="appealDeadline", format="yyyy-MM-dd hh:mm:ss")
	private Date appealDeadline;//上诉截止时间
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="lawSignDate", format="yyyy-MM-dd hh:mm:ss")
	private Date lawSignDate;//法律签收日期
	private String trialResult;//审判结果：1庭前调解、2判决、3诉中调解、4撤诉
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="lastPerformDate", format="yyyy-MM-dd hh:mm:ss")
	private Date lastPerformDate;//最后履行日期
	private String isRecovery;//是否追偿：Y是、N否
	private String nolleAppealReason;//撤诉原因：1资料不全、2庭外和解、3其他
	private String trialDescription;//庭审情况
	private String trialResultDescription;//结果说明
	private String judgeOpinion;//判决意见：1拒赔、2非拒赔
	private String lawsuitStage;//诉讼阶段：1-一审、2-二审
	private String refuseReason;//拒赔原因
	private String originalAgentType;//原告代理人类型：1律师、2法律工作者、3家属、4无代理人
	private String practice;//律所
	private String agentName;//代理人姓名
	private String totalCosts;//合计诉讼费
	private String companyCosts;//我司诉讼费
	private String lawyerFee;//律师费
	private String appellor;//上诉方：1我司、2原告方、3被保险人、4其他
	private String chaseCause;//追偿类型：0-其他、1-超赔、6-代位、7-垫付
	private String preChaseIndemnity;//需追偿赔案赔款
	private String preChaseFee;//需追偿赔案费用
	private String baseAgentFee;//基础代理费
	private String travelFee;//差旅费
	private String reduceAgentFee;//减损代理费
	private String upfrontFee;//前期费用
	private String carProReduceFee;//车物减损费用
	private String personLossReduceFee;//人伤减损费用
	private String reduceRate;//减损比例
	public String getLawsuitNo() {
		return lawsuitNo;
	}
	public void setLawsuitNo(String lawsuitNo) {
		this.lawsuitNo = lawsuitNo;
	}
	public String getAppealOpinion() {
		return appealOpinion;
	}
	public void setAppealOpinion(String appealOpinion) {
		this.appealOpinion = appealOpinion;
	}
	public Date getAppealDeadline() {
		return appealDeadline;
	}
	public void setAppealDeadline(Date appealDeadline) {
		this.appealDeadline = appealDeadline;
	}
	public Date getLawSignDate() {
		return lawSignDate;
	}
	public void setLawSignDate(Date lawSignDate) {
		this.lawSignDate = lawSignDate;
	}
	public String getTrialResult() {
		return trialResult;
	}
	public void setTrialResult(String trialResult) {
		this.trialResult = trialResult;
	}
	public Date getLastPerformDate() {
		return lastPerformDate;
	}
	public void setLastPerformDate(Date lastPerformDate) {
		this.lastPerformDate = lastPerformDate;
	}
	public String getIsRecovery() {
		return isRecovery;
	}
	public void setIsRecovery(String isRecovery) {
		this.isRecovery = isRecovery;
	}
	public String getNolleAppealReason() {
		return nolleAppealReason;
	}
	public void setNolleAppealReason(String nolleAppealReason) {
		this.nolleAppealReason = nolleAppealReason;
	}
	public String getTrialDescription() {
		return trialDescription;
	}
	public void setTrialDescription(String trialDescription) {
		this.trialDescription = trialDescription;
	}
	public String getTrialResultDescription() {
		return trialResultDescription;
	}
	public void setTrialResultDescription(String trialResultDescription) {
		this.trialResultDescription = trialResultDescription;
	}
	public String getJudgeOpinion() {
		return judgeOpinion;
	}
	public void setJudgeOpinion(String judgeOpinion) {
		this.judgeOpinion = judgeOpinion;
	}
	public String getLawsuitStage() {
		return lawsuitStage;
	}
	public void setLawsuitStage(String lawsuitStage) {
		this.lawsuitStage = lawsuitStage;
	}
	public String getRefuseReason() {
		return refuseReason;
	}
	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}
	public String getOriginalAgentType() {
		return originalAgentType;
	}
	public void setOriginalAgentType(String originalAgentType) {
		this.originalAgentType = originalAgentType;
	}
	public String getPractice() {
		return practice;
	}
	public void setPractice(String practice) {
		this.practice = practice;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getTotalCosts() {
		return totalCosts;
	}
	public void setTotalCosts(String totalCosts) {
		this.totalCosts = totalCosts;
	}
	public String getCompanyCosts() {
		return companyCosts;
	}
	public void setCompanyCosts(String companyCosts) {
		this.companyCosts = companyCosts;
	}
	public String getLawyerFee() {
		return lawyerFee;
	}
	public void setLawyerFee(String lawyerFee) {
		this.lawyerFee = lawyerFee;
	}
	public String getAppellor() {
		return appellor;
	}
	public void setAppellor(String appellor) {
		this.appellor = appellor;
	}
	public String getChaseCause() {
		return chaseCause;
	}
	public void setChaseCause(String chaseCause) {
		this.chaseCause = chaseCause;
	}
	public String getPreChaseIndemnity() {
		return preChaseIndemnity;
	}
	public void setPreChaseIndemnity(String preChaseIndemnity) {
		this.preChaseIndemnity = preChaseIndemnity;
	}
	public String getPreChaseFee() {
		return preChaseFee;
	}
	public void setPreChaseFee(String preChaseFee) {
		this.preChaseFee = preChaseFee;
	}
	public String getBaseAgentFee() {
		return baseAgentFee;
	}
	public void setBaseAgentFee(String baseAgentFee) {
		this.baseAgentFee = baseAgentFee;
	}
	public String getTravelFee() {
		return travelFee;
	}
	public void setTravelFee(String travelFee) {
		this.travelFee = travelFee;
	}
	public String getReduceAgentFee() {
		return reduceAgentFee;
	}
	public void setReduceAgentFee(String reduceAgentFee) {
		this.reduceAgentFee = reduceAgentFee;
	}
	public String getUpfrontFee() {
		return upfrontFee;
	}
	public void setUpfrontFee(String upfrontFee) {
		this.upfrontFee = upfrontFee;
	}
	public String getCarProReduceFee() {
		return carProReduceFee;
	}
	public void setCarProReduceFee(String carProReduceFee) {
		this.carProReduceFee = carProReduceFee;
	}
	public String getPersonLossReduceFee() {
		return personLossReduceFee;
	}
	public void setPersonLossReduceFee(String personLossReduceFee) {
		this.personLossReduceFee = personLossReduceFee;
	}
	public String getReduceRate() {
		return reduceRate;
	}
	public void setReduceRate(String reduceRate) {
		this.reduceRate = reduceRate;
	}
	
	

}
