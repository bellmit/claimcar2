package ins.sino.claimcar.claim.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompensateVoResult implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/** 赔款计算书号 */
	private String compensateNo;

	/** 主计算书号 */
	private String mainCompensateNo;

	/** 业务数据ID号码 */
	private Long businessId;

	/** 理赔类型 */
	private String lflag;

	/** 计算书类型 */
	private String compensateType;

	/** 赔案号 */
	private String caseNo;

	/** 次数 */
	private Integer times;

	/** 险类代码 */
	private String classCode;

	/** 险种代码 */
	private String riskCode;

	/** 立案号 */
	private String claimNo;

	/** 报案号码 */
	private String registNo;

	/** 保单号 */
	private String policyNo;

	/** 客户等级 */
	private String customLevel;

	/** 案件紧急程度 */
	private String mercyFlag;

	/** 免赔条件字段 */
	private String deductCond;

	/** 终到日期 */
	private Date preserveDate;

	/** 理赔代理人代码 */
	private String checkAgentCode;

	/** 理赔代理人名称 */
	private String checkAgentName;

	/** 检验人名称 */
	private String surveyorName;

	/** 索赔人名称 */
	private String counterClaimerName;

	/** 航方责任 */
	private String dutyDescription;

	/** 币别 */
	private String currency;

	/** 商业险责任代码 */
	private String indemnityDuty;

	/** 商业险责任比例 */
	private BigDecimal indemnityDutyRate;

	/** 标的损失金额 */
	private BigDecimal sumLoss;

	/** 残值 */
	private BigDecimal sumRest;

	/** 不计免赔金额总计 */
	private BigDecimal sumNoDeductFee;

	/** 责任赔款合计 */
	private BigDecimal sumDutyPaid;

	/** 不计入赔款的费用金额 */
	private BigDecimal sumNoDutyFee;

	/** 赔付金额 */
	private BigDecimal sumPaid;

	/** 已预付赔款 */
	private BigDecimal sumPrePaid;

	/** 本次赔付金额 */
	private BigDecimal sumThisPaid;
	
	/** 交强险已赔付总金额*/
	private BigDecimal sumBzPaid;

	/** 协议金额 */
	private BigDecimal exgratiaFee;

	/** 协议备注 */
	private String exgratiaRemark;

	/** 领赔款单位/代理人/索赔人 */
	private String receiverName;

	/** 开户银行 */
	private String bank;

	/** 帐号 */
	private String account;

	/** 交强险理算公式代码 */
	private String bzExpCode;

	/** 是否诉讼案件 */
	private String isSuitFlag;

	/** 注销/拒赔（恢复）申请处理机构 */
	private String makeCom;

	/** 机构代码 */
	private String comCode;

	/** 经办人代码 */
	private String handlerCode;

	/** 归属业务员代码 */
	private String handler1Code;

	/** 险别计算公式 */
	private String compensateExp;
	
	/** 险别计算公式 */
	private String newCompensateExp;
	
	/** 险别计算公式 */
	private String oldCompensateExp;

	/** 险别计算公式 */
	private String compensateKindExp;

	/** 理算报告 */
	private String lctext;

	/** 确认人代码 */
	private String approverCode;

	/** 最终核保人代码 */
	private String underwriteCode;

	/** 最终核保人名称 */
	private String underwriteName;

	/** 统计年月 */
	private Date statisticsYm;

	/** 操作员代码 */
	private String operatorCode;

	/** 操作员名称 */
	private String operatorName;

	/** 最后修改人代码 */
	private String lastModifyCode;

	/** 最终修改人名称 */
	private String lastModifyName;

	/** 最后修改时间 */
	private Date lastModifyTime;

	/** 录入日期 */
	private Date inputTime;

	/** 核保完成日期 */
	private Date underwriteEndDate;
	
	/** 核保标志 */
	private String underWriteFlag;
	
	/** 赔案类别 */
	private String claimType;
	
	/** 1 追偿  2清付  3自付*/
	private String payType;

	/** 备注 */
	private String remark;

	/** 标志字段 */
	private String flag;

	/** 是否需要追偿 */
	private String replevyFlag;

	private List<PrpLLossPropVo> prpLLossPropVoList = new ArrayList<PrpLLossPropVo>(0);

	private List<PrpLLossPersonVo> prpLLossPersonVoList = new ArrayList<PrpLLossPersonVo>(0);

	private List<PrpLChargeVo> prpLChargeVoList = new ArrayList<PrpLChargeVo>(0);

	private List<PrpLLossItemVo> prpLLossItemVoList = new ArrayList<PrpLLossItemVo>(0);
	
//	private List<PrpLcfee> prpLcfees = new ArrayList<PrpLcfee>(0);
	
//	private List<PrpLcompensateExt> prpLcompensateExts = new ArrayList<PrpLcompensateExt>(0);

//	private List<PrpLcompensateDef> prpLcompensateDefs = new ArrayList<PrpLcompensateDef>(0);
	
	public String getCompensateNo() {
		return compensateNo;
	}

	public void setCompensateNo(String compensateNo) {
		this.compensateNo = compensateNo;
	}

	public String getMainCompensateNo() {
		return mainCompensateNo;
	}

	public void setMainCompensateNo(String mainCompensateNo) {
		this.mainCompensateNo = mainCompensateNo;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	
	public String getLflag() {
		return lflag;
	}
	
	public void setLflag(String lflag) {
		this.lflag = lflag;
	}

	public String getCompensateType() {
		return compensateType;
	}

	public void setCompensateType(String compensateType) {
		this.compensateType = compensateType;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}
	
	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	
	public String getRiskCode() {
		return riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	
	public String getClaimNo() {
		return claimNo;
	}
	
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
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

	public String getCustomLevel() {
		return customLevel;
	}

	public void setCustomLevel(String customLevel) {
		this.customLevel = customLevel;
	}
	
	public String getMercyFlag() {
		return mercyFlag;
	}
	
	public void setMercyFlag(String mercyFlag) {
		this.mercyFlag = mercyFlag;
	}

	public String getDeductCond() {
		return deductCond;
	}

	public void setDeductCond(String deductCond) {
		this.deductCond = deductCond;
	}

	public Date getPreserveDate() {
		return preserveDate;
	}

	public void setPreserveDate(Date preserveDate) {
		this.preserveDate = preserveDate;
	}
	
	public String getCheckAgentCode() {
		return checkAgentCode;
	}

	public void setCheckAgentCode(String checkAgentCode) {
		this.checkAgentCode = checkAgentCode;
	}
	
	public String getCheckAgentName() {
		return checkAgentName;
	}

	public void setCheckAgentName(String checkAgentName) {
		this.checkAgentName = checkAgentName;
	}

	public String getSurveyorName() {
		return surveyorName;
	}
	
	public void setSurveyorName(String surveyorName) {
		this.surveyorName = surveyorName;
	}

	public String getCounterClaimerName() {
		return counterClaimerName;
	}

	public void setCounterClaimerName(String counterClaimerName) {
		this.counterClaimerName = counterClaimerName;
	}

	public String getDutyDescription() {
		return dutyDescription;
	}
	
	public void setDutyDescription(String dutyDescription) {
		this.dutyDescription = dutyDescription;
	}

	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
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
	
	public BigDecimal getSumLoss() {
		return sumLoss;
	}

	public void setSumLoss(BigDecimal sumLoss) {
		this.sumLoss = sumLoss;
	}

	public BigDecimal getSumRest() {
		return sumRest;
	}

	public void setSumRest(BigDecimal sumRest) {
		this.sumRest = sumRest;
	}

	public BigDecimal getSumNoDeductFee() {
		return sumNoDeductFee;
	}
	
	public void setSumNoDeductFee(BigDecimal sumNoDeductFee) {
		this.sumNoDeductFee = sumNoDeductFee;
	}

	public BigDecimal getSumDutyPaid() {
		return sumDutyPaid;
	}
	
	public void setSumDutyPaid(BigDecimal sumDutyPaid) {
		this.sumDutyPaid = sumDutyPaid;
	}

	public BigDecimal getSumNoDutyFee() {
		return sumNoDutyFee;
	}

	public void setSumNoDutyFee(BigDecimal sumNoDutyFee) {
		this.sumNoDutyFee = sumNoDutyFee;
	}
	
	public BigDecimal getSumPaid() {
		return sumPaid;
	}

	public void setSumPaid(BigDecimal sumPaid) {
		this.sumPaid = sumPaid;
	}

	public BigDecimal getSumPrePaid() {
		return sumPrePaid;
	}
	
	public void setSumPrePaid(BigDecimal sumPrePaid) {
		this.sumPrePaid = sumPrePaid;
	}

	public BigDecimal getSumThisPaid() {
		return sumThisPaid;
	}
	
	public void setSumThisPaid(BigDecimal sumThisPaid) {
		this.sumThisPaid = sumThisPaid;
	}

	public BigDecimal getSumBzPaid() {
		return sumBzPaid;
	}

	public void setSumBzPaid(BigDecimal sumBzPaid) {
		this.sumBzPaid = sumBzPaid;
	}

	public BigDecimal getExgratiaFee() {
		return exgratiaFee;
	}
	
	public void setExgratiaFee(BigDecimal exgratiaFee) {
		this.exgratiaFee = exgratiaFee;
	}

	public String getExgratiaRemark() {
		return exgratiaRemark;
	}

	public void setExgratiaRemark(String exgratiaRemark) {
		this.exgratiaRemark = exgratiaRemark;
	}
	
	public String getReceiverName() {
		return receiverName;
	}
	
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getBzExpCode() {
		return bzExpCode;
	}

	public void setBzExpCode(String bzExpCode) {
		this.bzExpCode = bzExpCode;
	}

	public String getIsSuitFlag() {
		return isSuitFlag;
	}
	
	public void setIsSuitFlag(String isSuitFlag) {
		this.isSuitFlag = isSuitFlag;
	}

	public String getMakeCom() {
		return makeCom;
	}
	
	public void setMakeCom(String makeCom) {
		this.makeCom = makeCom;
	}
	
	public String getComCode() {
		return comCode;
	}
	
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	
	public String getHandlerCode() {
		return handlerCode;
	}

	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}
	
	public String getHandler1Code() {
		return handler1Code;
	}

	public void setHandler1Code(String handler1Code) {
		this.handler1Code = handler1Code;
	}
	
	public String getCompensateExp() {
		return compensateExp;
	}
	
	public void setCompensateExp(String compensateExp) {
		this.compensateExp = compensateExp;
	}
	
	public String getNewCompensateExp() {
		return newCompensateExp;
	}
	
	public void setNewCompensateExp(String newCompensateExp) {
		this.newCompensateExp = newCompensateExp;
	}

	public String getOldCompensateExp() {
		return oldCompensateExp;
	}

	public void setOldCompensateExp(String oldCompensateExp) {
		this.oldCompensateExp = oldCompensateExp;
	}

	public String getCompensateKindExp() {
		return compensateKindExp;
	}
	
	public void setCompensateKindExp(String compensateKindExp) {
		this.compensateKindExp = compensateKindExp;
	}

	public String getLctext() {
		return lctext;
	}

	public void setLctext(String lctext) {
		this.lctext = lctext;
	}
	
	public String getApproverCode() {
		return approverCode;
	}
	
	public void setApproverCode(String approverCode) {
		this.approverCode = approverCode;
	}
	
	public String getUnderwriteCode() {
		return underwriteCode;
	}

	public void setUnderwriteCode(String underwriteCode) {
		this.underwriteCode = underwriteCode;
	}
	
	public String getUnderwriteName() {
		return underwriteName;
	}

	public void setUnderwriteName(String underwriteName) {
		this.underwriteName = underwriteName;
	}

	public Date getStatisticsYm() {
		return statisticsYm;
	}
	
	public void setStatisticsYm(Date statisticsYm) {
		this.statisticsYm = statisticsYm;
	}
	
	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	
	public String getLastModifyCode() {
		return lastModifyCode;
	}

	public void setLastModifyCode(String lastModifyCode) {
		this.lastModifyCode = lastModifyCode;
	}

	public String getLastModifyName() {
		return lastModifyName;
	}
	
	public void setLastModifyName(String lastModifyName) {
		this.lastModifyName = lastModifyName;
	}
	
	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public Date getInputTime() {
		return inputTime;
	}

	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}

	public Date getUnderwriteEndDate() {
		return underwriteEndDate;
	}

	public void setUnderwriteEndDate(Date underwriteEndDate) {
		this.underwriteEndDate = underwriteEndDate;
	}

	public String getUnderWriteFlag() {
		return underWriteFlag;
	}

	public void setUnderWriteFlag(String underWriteFlag) {
		this.underWriteFlag = underWriteFlag;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getPayType() {
		return payType;
	}
	
	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getReplevyFlag() {
		return replevyFlag;
	}

	public void setReplevyFlag(String replevyFlag) {
		this.replevyFlag = replevyFlag;
	}

	public List<PrpLLossPropVo> getPrpLLossPropVoList() {
		return prpLLossPropVoList;
	}
	
	public void setPrpLLossPropVoList(List<PrpLLossPropVo> prpLLossPropVoList) {
		this.prpLLossPropVoList = prpLLossPropVoList;
	}
	
	public List<PrpLChargeVo> getPrpLChargeVoList() {
		return prpLChargeVoList;
	}

	public void setPrpLChargeVoList(List<PrpLChargeVo> prpLChargeVoList) {
		this.prpLChargeVoList = prpLChargeVoList;
	}

	public List<PrpLLossItemVo> getPrpLLossItemVoList() {
		return prpLLossItemVoList;
	}
	
	public void setPrpLLossItemVoList(List<PrpLLossItemVo> prpLLossItemVoList) {
		this.prpLLossItemVoList = prpLLossItemVoList;
	}
	
	public List<PrpLLossPersonVo> getPrpLLossPersonVoList() {
		return prpLLossPersonVoList;
	}

	public void setPrpLLossPersonVoList(List<PrpLLossPersonVo> prpLLossPersonVoList) {
		this.prpLLossPersonVoList = prpLLossPersonVoList;
	}

}
