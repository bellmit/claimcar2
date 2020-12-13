package ins.sino.claimcar.ilog.rule.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


public class IlogDataProcessingVo implements Serializable{
	private static final long serialVersionUID = 1L;

	private String businessNo;//业务号 报案号
	private String compensateNo; //计算书号
	private String comCode;//业务归属机构
	private String riskCode;//险种
	private String operateType;//操作类型  1：自动  2：人工权限
	private String ruleType;//任务类型 0：车辆; 1：财产
	private PrpLRuleBaseInfoVo prpLRuleBaseInfoVo; //规则信息表
	private List<PrpLRuleDetailInfoVo> prpLRuleDetailInfoVoList;//规则明细信息表
	private List<PrpLRuleDetailInfoVo> vPriceDetailInfoVoList;//核价规则
	private List<PrpLRuleDetailInfoVo> vLCarDetailInfoVoList;//车辆核损规则
	private List<PrpLRuleDetailInfoVo> vLPropDetailInfoVoList;//财产核损规则
	private List<PrpLRuleDetailInfoVo> pLossDetailInfoVoList;//人伤核损规则
	private List<PrpLRuleDetailInfoVo> certiDetailInfoVoList;//单证规则   
	private List<PrpLRuleDetailInfoVo> compeDetailInfoVoList;//理算规则    
	private List<PrpLRuleDetailInfoVo> vClaimDetailInfoVoList;//核赔规则     
	private String ruleNode;//任务节点
	private BigDecimal taskId;//工作流ID
	private String lossParty;//损失方
	private String licenseNo;//损失方的车牌号
	private String triggerNode;//触发节点
	private String operatorCode;//操作人员 
	
	public String getBusinessNo() {
		return businessNo;
	}
	
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	
	public String getCompensateNo() {
		return compensateNo;
	}
	
	public void setCompensateNo(String compensateNo) {
		this.compensateNo = compensateNo;
	}
	
	public String getComCode() {
		return comCode;
	}
	
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	
	public String getRiskCode() {
		return riskCode;
	}
	
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	
	public String getOperateType() {
		return operateType;
	}
	
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	
	public String getRuleType() {
		return ruleType;
	}
	
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	
	public PrpLRuleBaseInfoVo getPrpLRuleBaseInfoVo() {
		return prpLRuleBaseInfoVo;
	}

	
	public void setPrpLRuleBaseInfoVo(PrpLRuleBaseInfoVo prpLRuleBaseInfoVo) {
		this.prpLRuleBaseInfoVo = prpLRuleBaseInfoVo;
	}

	
	public List<PrpLRuleDetailInfoVo> getPrpLRuleDetailInfoVoList() {
		return prpLRuleDetailInfoVoList;
	}

	
	public void setPrpLRuleDetailInfoVoList(List<PrpLRuleDetailInfoVo> prpLRuleDetailInfoVoList) {
		this.prpLRuleDetailInfoVoList = prpLRuleDetailInfoVoList;
	}

	
	public String getRuleNode() {
		return ruleNode;
	}

	
	public void setRuleNode(String ruleNode) {
		this.ruleNode = ruleNode;
	}
	
	public BigDecimal getTaskId() {
		return this.taskId;
	}

	public void setTaskId(BigDecimal taskId) {
		this.taskId = taskId;
	}
	
	public String getLossParty() {
		return lossParty;
	}
	
	public void setLossParty(String lossParty) {
		this.lossParty = lossParty;
	}
	
	public String getLicenseNo() {
		return licenseNo;
	}
	
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getTriggerNode() {
		return triggerNode;
	}

	public void setTriggerNode(String triggerNode) {
		this.triggerNode = triggerNode;
	}

	
	public String getOperatorCode() {
		return operatorCode;
	}

	
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	
	public List<PrpLRuleDetailInfoVo> getvPriceDetailInfoVoList() {
		return vPriceDetailInfoVoList;
	}

	
	public void setvPriceDetailInfoVoList(List<PrpLRuleDetailInfoVo> vPriceDetailInfoVoList) {
		this.vPriceDetailInfoVoList = vPriceDetailInfoVoList;
	}

	
	public List<PrpLRuleDetailInfoVo> getvLCarDetailInfoVoList() {
		return vLCarDetailInfoVoList;
	}

	
	public void setvLCarDetailInfoVoList(List<PrpLRuleDetailInfoVo> vLCarDetailInfoVoList) {
		this.vLCarDetailInfoVoList = vLCarDetailInfoVoList;
	}

	
	public List<PrpLRuleDetailInfoVo> getvLPropDetailInfoVoList() {
		return vLPropDetailInfoVoList;
	}

	
	public void setvLPropDetailInfoVoList(List<PrpLRuleDetailInfoVo> vLPropDetailInfoVoList) {
		this.vLPropDetailInfoVoList = vLPropDetailInfoVoList;
	}

	
	public List<PrpLRuleDetailInfoVo> getpLossDetailInfoVoList() {
		return pLossDetailInfoVoList;
	}

	
	public void setpLossDetailInfoVoList(List<PrpLRuleDetailInfoVo> pLossDetailInfoVoList) {
		this.pLossDetailInfoVoList = pLossDetailInfoVoList;
	}

	
	public List<PrpLRuleDetailInfoVo> getCertiDetailInfoVoList() {
		return certiDetailInfoVoList;
	}

	
	public void setCertiDetailInfoVoList(List<PrpLRuleDetailInfoVo> certiDetailInfoVoList) {
		this.certiDetailInfoVoList = certiDetailInfoVoList;
	}

	
	public List<PrpLRuleDetailInfoVo> getCompeDetailInfoVoList() {
		return compeDetailInfoVoList;
	}

	
	public void setCompeDetailInfoVoList(List<PrpLRuleDetailInfoVo> compeDetailInfoVoList) {
		this.compeDetailInfoVoList = compeDetailInfoVoList;
	}

	
	public List<PrpLRuleDetailInfoVo> getvClaimDetailInfoVoList() {
		return vClaimDetailInfoVoList;
	}

	
	public void setvClaimDetailInfoVoList(List<PrpLRuleDetailInfoVo> vClaimDetailInfoVoList) {
		this.vClaimDetailInfoVoList = vClaimDetailInfoVoList;
	}
	
	
}
