package ins.sino.claimcar.claim.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClaimFeeCondition implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	private String registNo;
	private String policyNo;
	private String claimNo;
	
	private String indemnityDuty;
	private String lossType;
	private String damageFlag;
	private BigDecimal ciIndemDuty;	//交强险有责无责
	private String ciDutyFlag;		//交强险有无赔偿责任
	private String claimSlefFlag;//是否互碰
	private String totalLossFlag;//是否全损
	private BigDecimal indemnityDutyRate;
	/** 处理机构 */
	private String makeCom = "";
	/** 赔案类别*/
	private String claimType;
	//汇总的损失列表
	//private LossInfo lossInfo;
	// 车辆损失列表
	private List<LossItem> carLossItems;
	// 人伤跟踪列表
	private List<LossItem> personLossItems;
	// 财产损失列表
	private List<LossItem> propLossItems;
	// 其他损失列表
	private List<LossItem> otherLossItems;
	// 所有损失项列表
	private List<LossItem> allLossItems;
	
	private Map<String, BigDecimal> sumClaimMap;	//分类估损
	
	private Map<String, BigDecimal> rescueFeeMap;	//分类施救费
	
	private Map<String, BigDecimal> rejectFeeMap;	//分类残值
	/**
	 * 交强险已经赔付的金额
	 */
	private BigDecimal oldSumClaimB1;
	private BigDecimal oldSumClaimB2;
	/**
	 * 交强险已经赔付的施救费、残值金额
	 */
	private BigDecimal oldRescueFeeB1;
	private BigDecimal oldRejectFeeB1;
	//PNCCAR.人伤跟踪.GAOANXU.2010-08-09.ADD 人伤金额拆分：将医药费、续医费、伙食补助费加入到医疗费中
	//已经立案的估损信息
	private BigDecimal oldSumClaimMedical;
	
	private List<ClaimFeeExt> claimFeeExts;
	private List<DeductCondInfo> deductCondInfoList;
	private String checkDeductFlag;
    private String subRogationFlag;//北分代位求偿
    
    public ClaimFeeCondition(){
		ciIndemDuty = BigDecimal.ZERO;
		sumClaimMap = new HashMap<String, BigDecimal>(0);
		rescueFeeMap = new HashMap<String, BigDecimal>(0);
		rejectFeeMap = new HashMap<String, BigDecimal>(0);
	}
	
	public BigDecimal getSumClaim(String lossItemType){
		if(sumClaimMap.get(lossItemType)==null){
			return new BigDecimal("0.00");
		}else{
			return sumClaimMap.get(lossItemType);
		}
	}
	
	public BigDecimal getRescueFee(String lossItemType){
		if(rescueFeeMap.get(lossItemType)==null){
			return new BigDecimal("0.00");
		}else{
			return rescueFeeMap.get(lossItemType);
		}
	}
	
	public BigDecimal getRejectFee(String lossItemType){
		if(rejectFeeMap.get(lossItemType)==null){
			return new BigDecimal("0.00");
		}else{
			return rejectFeeMap.get(lossItemType);
		}
	}
	
	public String getRegistNo() {
		return registNo;
	}
	
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	
	public String getPolicyNo() {
		return policyNo;
	}
	
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public String getClaimNo() {
		return claimNo;
	}
	
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	
	public String getIndemnityDuty() {
		return indemnityDuty;
	}
	
	public void setIndemnityDuty(String indemnityDuty) {
		this.indemnityDuty = indemnityDuty;
	}
	
	public String getLossType() {
		return lossType;
	}
	
	public void setLossType(String lossType) {
		this.lossType = lossType;
	}
	
	public String getDamageFlag() {
		return damageFlag;
	}
	
	public void setDamageFlag(String damageFlag) {
		this.damageFlag = damageFlag;
	}
	
	public BigDecimal getCiIndemDuty() {
		return ciIndemDuty;
	}
	
	public void setCiIndemDuty(BigDecimal ciIndemDuty) {
		this.ciIndemDuty = ciIndemDuty;
	}
	
	public String getCiDutyFlag() {
		return ciDutyFlag;
	}
	
	public void setCiDutyFlag(String ciDutyFlag) {
		this.ciDutyFlag = ciDutyFlag;
	}
	
	public BigDecimal getIndemnityDutyRate() {
		return indemnityDutyRate;
	}
	
	public void setIndemnityDutyRate(BigDecimal indemnityDutyRate) {
		this.indemnityDutyRate = indemnityDutyRate;
	}
	
	public String getMakeCom() {
		return makeCom;
	}
	
	public void setMakeCom(String makeCom) {
		this.makeCom = makeCom;
	}
	
	public String getClaimType() {
		return claimType;
	}
	
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	
	public List<LossItem> getCarLossItems() {
		return carLossItems;
	}
	
	public void setCarLossItems(List<LossItem> carLossItems) {
		this.carLossItems = carLossItems;
	}
	
	public List<LossItem> getPersonLossItems() {
		return personLossItems;
	}
	
	public void setPersonLossItems(List<LossItem> personLossItems) {
		this.personLossItems = personLossItems;
	}
	
	public List<LossItem> getPropLossItems() {
		return propLossItems;
	}
	
	public void setPropLossItems(List<LossItem> propLossItems) {
		this.propLossItems = propLossItems;
	}
	
	public List<LossItem> getOtherLossItems() {
		return otherLossItems;
	}
	
	public void setOtherLossItems(List<LossItem> otherLossItems) {
		this.otherLossItems = otherLossItems;
	}
	
	public List<LossItem> getAllLossItems() {
		return allLossItems;
	}
	
	public void setAllLossItems(List<LossItem> allLossItems) {
		this.allLossItems = allLossItems;
	}
	
	public Map<String,BigDecimal> getSumClaimMap() {
		return sumClaimMap;
	}
	
	public void setSumClaimMap(Map<String,BigDecimal> sumClaimMap) {
		this.sumClaimMap = sumClaimMap;
	}
	
	public Map<String,BigDecimal> getRescueFeeMap() {
		return rescueFeeMap;
	}
	
	public void setRescueFeeMap(Map<String,BigDecimal> rescueFeeMap) {
		this.rescueFeeMap = rescueFeeMap;
	}
	
	public Map<String,BigDecimal> getRejectFeeMap() {
		return rejectFeeMap;
	}
	
	public void setRejectFeeMap(Map<String,BigDecimal> rejectFeeMap) {
		this.rejectFeeMap = rejectFeeMap;
	}
	
	public BigDecimal getOldSumClaimB1() {
		return oldSumClaimB1;
	}
	
	public void setOldSumClaimB1(BigDecimal oldSumClaimB1) {
		this.oldSumClaimB1 = oldSumClaimB1;
	}
	
	public BigDecimal getOldSumClaimB2() {
		return oldSumClaimB2;
	}
	
	public void setOldSumClaimB2(BigDecimal oldSumClaimB2) {
		this.oldSumClaimB2 = oldSumClaimB2;
	}
	
	public BigDecimal getOldRescueFeeB1() {
		return oldRescueFeeB1;
	}
	
	public void setOldRescueFeeB1(BigDecimal oldRescueFeeB1) {
		this.oldRescueFeeB1 = oldRescueFeeB1;
	}
	
	public BigDecimal getOldRejectFeeB1() {
		return oldRejectFeeB1;
	}
	
	public void setOldRejectFeeB1(BigDecimal oldRejectFeeB1) {
		this.oldRejectFeeB1 = oldRejectFeeB1;
	}
	
	public BigDecimal getOldSumClaimMedical() {
		return oldSumClaimMedical;
	}
	
	public void setOldSumClaimMedical(BigDecimal oldSumClaimMedical) {
		this.oldSumClaimMedical = oldSumClaimMedical;
	}
	
	public List<ClaimFeeExt> getClaimFeeExts() {
		return claimFeeExts;
	}
	
	public void setClaimFeeExts(List<ClaimFeeExt> claimFeeExts) {
		this.claimFeeExts = claimFeeExts;
	}
	
	public List<DeductCondInfo> getDeductCondInfoList() {
		return deductCondInfoList;
	}
	
	public void setDeductCondInfoList(List<DeductCondInfo> deductCondInfoList) {
		this.deductCondInfoList = deductCondInfoList;
	}
	
	public String getCheckDeductFlag() {
		return checkDeductFlag;
	}
	
	public void setCheckDeductFlag(String checkDeductFlag) {
		this.checkDeductFlag = checkDeductFlag;
	}
	
	public String getSubRogationFlag() {
		return subRogationFlag;
	}
	
	public void setSubRogationFlag(String subRogationFlag) {
		this.subRogationFlag = subRogationFlag;
	}

	public String getClaimSlefFlag() {
		return claimSlefFlag;
	}

	public void setClaimSlefFlag(String claimSlefFlag) {
		this.claimSlefFlag = claimSlefFlag;
	}

	public String getTotalLossFlag() {
		return totalLossFlag;
	}

	public void setTotalLossFlag(String totalLossFlag) {
		this.totalLossFlag = totalLossFlag;
	}

    
}
