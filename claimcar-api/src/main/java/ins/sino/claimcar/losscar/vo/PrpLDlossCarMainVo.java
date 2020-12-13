package ins.sino.claimcar.losscar.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Custom VO class of PO PrpLDlossCarMain
 */ 
public class PrpLDlossCarMainVo extends PrpLDlossCarMainVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1234567L;
	/** 是否属于商业险保险责任 **/
	private String ciDutyFlag;
	/** 商业险责任代码 **/
	private String indemnityDuty;
	/** 商业险责任比例 **/
	private BigDecimal indemnityDutyRate;
	/** 互碰自赔标志 **/
	private String isClaimSelf;
	/** 工作流任务id **/
	private Double flowTaskId;
	
	/** 第一次进入精友标志 **/
	private String jyFlag;
	
	private String nextNode;

	private PrpLDlossCarInfoVo lossCarInfoVo;
	
	private List<PrpLDlossCarRepairVo> outRepairList = new ArrayList<PrpLDlossCarRepairVo>();
	
	private BigDecimal assessorFee;
	private BigDecimal checkFee;

	private BigDecimal invoiceFee;
	
	private String isTotalLoss; // 是否全损

	private String isHotSinceDetonation; // 是否火自爆

	private String isWaterFloaded; // 是否水淹

	private String waterFloodedLevel; // 水淹等级

	private String isMobileCase; // 是否移动端案子，Y-是，N-否
	
	private String vinNo; // VIN码
	

	


	public String getCiDutyFlag() {
		return ciDutyFlag;
	}
	
	public void setCiDutyFlag(String ciDutyFlag) {
		this.ciDutyFlag = ciDutyFlag;
	}

	public String getIndemnityDuty() {
		return indemnityDuty;
	}
	
	public void setIndemnityDuty(String indemnityDuty) {
		this.indemnityDuty = indemnityDuty;
	}
	
	public BigDecimal getIndemnityDutyRate() {
		return indemnityDutyRate;
	}
	
	public void setIndemnityDutyRate(BigDecimal indemnityDutyRate) {
		this.indemnityDutyRate = indemnityDutyRate;
	}

	public String getIsClaimSelf() {
		return isClaimSelf;
	}
	
	public void setIsClaimSelf(String isClaimSelf) {
		this.isClaimSelf = isClaimSelf;
	}

	public PrpLDlossCarInfoVo getLossCarInfoVo() {
		return lossCarInfoVo;
	}

	public void setLossCarInfoVo(PrpLDlossCarInfoVo lossCarInfoVo) {
		this.lossCarInfoVo = lossCarInfoVo;
	}

	
	public Double getFlowTaskId() {
		return flowTaskId;
	}
	
	public void setFlowTaskId(Double flowTaskId) {
		this.flowTaskId = flowTaskId;
	}
	
	public String getJyFlag() {
		return jyFlag;
	}
	
	public void setJyFlag(String jyFlag) {
		this.jyFlag = jyFlag;
	}
	
	public String getNextNode() {
		return nextNode;
	}
	
	public void setNextNode(String nextNode) {
		this.nextNode = nextNode;
	}

	public List<PrpLDlossCarRepairVo> getOutRepairList() {
		return outRepairList;
	}

	public void setOutRepairList(List<PrpLDlossCarRepairVo> outRepairList) {
		this.outRepairList = outRepairList;
	}

	public BigDecimal getAssessorFee() {
		return assessorFee;
	}

	public void setAssessorFee(BigDecimal assessorFee) {
		this.assessorFee = assessorFee;
	}
	
	public BigDecimal getInvoiceFee() {
		return invoiceFee;
	}

	public void setInvoiceFee(BigDecimal invoiceFee) {
		this.invoiceFee = invoiceFee;
	}

	public String getIsTotalLoss() {
		return isTotalLoss;
	}

	public void setIsTotalLoss(String isTotalLoss) {
		this.isTotalLoss = isTotalLoss;
	}

	public String getIsHotSinceDetonation() {
		return isHotSinceDetonation;
	}

	public void setIsHotSinceDetonation(String isHotSinceDetonation) {
		this.isHotSinceDetonation = isHotSinceDetonation;
	}

	public String getIsWaterFloaded() {
		return isWaterFloaded;
	}

	public void setIsWaterFloaded(String isWaterFloaded) {
		this.isWaterFloaded = isWaterFloaded;
	}

	public String getWaterFloodedLevel() {
		return waterFloodedLevel;
	}

	public void setWaterFloodedLevel(String waterFloodedLevel) {
		this.waterFloodedLevel = waterFloodedLevel;
	}

	public String getIsMobileCase() {
		return isMobileCase;
	}

	public void setIsMobileCase(String isMobileCase) {
		this.isMobileCase = isMobileCase;
	}

	public String getVinNo() {
		return vinNo;
	}

	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}

	public BigDecimal getCheckFee() {
		return checkFee;
	}

	public void setCheckFee(BigDecimal checkFee) {
		this.checkFee = checkFee;
	}
	

}
