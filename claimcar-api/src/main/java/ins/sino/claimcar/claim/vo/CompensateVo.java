package ins.sino.claimcar.claim.vo;

import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CompensateVo implements Serializable{
private static final long serialVersionUID = 1L;
	
	/** 理算对象*/
	private PrpLCompensateVo prpLCompensateVo;
	/** 理算诉讼对象 */
//	private PrpLcompensateExt prpLcompensateExt;
	/** 无保单代赔理算对象*/
	private CompensateVoResult insteadPrpLcompensateVo;
	/** 医疗审核任务主表ID*/
	private Long personAuditMainId;
	/** 车辆损失信息*/
	private List<PrpLLossItemVo> prpLLossItemVoList;
	/** 清付车辆损失项 **/
	private List<PrpLLossItemVo> prpLLossItemVoQFList;
	/** 代位车辆损失项 **/
	private List<PrpLLossItemVo> prpLLossItemVoZCList;
	/** 自付车辆损失项 **/
	private List<PrpLLossItemVo> prpLLossItemVoZFList;
	/** 费用信息*/
	private List<PrpLChargeVo> prpLChargeVoList;
	/** 财产损失信息*/
	private List<PrpLLossPropVo> prpLLossPropVoList;
	/** 其它损失信息*/
	private List<PrpLLossPropVo> prpLOtherLossPropVoList;
	/** 人员损失信息*/
	private List<PrpLLossPersonVo> prpLLossPersonVoList;
	/** 涉案人员损失信息*/
//	private List<PrpLauditInjured> auditPrpLinjureds;
	/** 无保单代赔涉案人员损失信息*/
//	private List<PrpLauditInjured> insteadAuditPrpLinjureds;
	/** 工作流参数传送对象*/
//	private BpmBusiness bpmBusiness;
	/** 工作流业务表ID*/
	private String taskId; 
	/** 提交类型*/
	private String saveType; 
	/** 共保信息*/
//	private List<PrpLcoins> prpLcoinses;
	/** 主车驾驶员信息*/
//	private List<PrpLdriver> prpLdrivers;
	/** 车辆定损信息*/
	private List<PrpLDlossCarMainVo> prpLDlossCarMainVoList;
	/** 财产定损信息*/
	private List<PrpLdlossPropMainVo> prpLdlossPropMainVoList;
	/** 计算书类型*/
	private String compensateType;
	/** 操作员代码*/
	private String userCode;
	/** 自动核赔审核通过*/
	private boolean  pass;
	/** 人伤跟踪人员信息*/
	private List<PrpLDlossPersTraceVo> prpLDlossPersTraceVoList;
	/** 协议金额*/
	private BigDecimal exgratiaFee;
	/** 属于本车的车损信息*/
	private List<DefLossInfoOfA> carDefLossInfoOfAList = new ArrayList<DefLossInfoOfA>(0);
	/** 属于本车的财损信息*/
	private List<DefLossInfoOfA> propDefLossInfoOfAList = new ArrayList<DefLossInfoOfA>(0);
	/** 属于本车的人损信息*/
	private List<DefLossInfoOfA> personDefLossInfoOfAList = new ArrayList<DefLossInfoOfA>(0);
	/** 无责代赔车辆损失信息*/
	private List<PrpLLossItemVo> dutyInsteadPayCarInfos = new ArrayList<PrpLLossItemVo>(0);
	/** 无保单代赔车辆损失信息*/
//	private List<PrpLLossItemVo> policyInsteadPayCarInfos = new ArrayList<PrpLLossItemVo>(0);
	/** 无责代赔财产损失信息*/
//	private List<PrpLLossPropVo> dutyInsteadPayPropInfos = new ArrayList<PrpLLossPropVo>(0);
	/** 无保单代赔财产信息*/
//	private List<PrpLLossPropVo> policyInsteadPayPropInfos = new ArrayList<PrpLLossPropVo>(0);
//	/** 无保单代赔人员损失信息*/
//	private List<PrpLLossPersonVo> policyInsteadPayPersonInfos = new ArrayList<PrpLLossPersonVo>(0);
	/** personItemId串*/
	private String personItemIdStr;
	/** 本車車輛損失的一些數據，商業險有X1,X險時用*/
	private PrpLLossItemVo defLossSimpleInfoOfA;
	/** 是否将该人员损失信息作为医疗审核任务发送*/
	private List<String> auditPersonFlagList = new ArrayList<String>(0);
	/** 理算提示信息*/
	private String message;
//	
//	private List<PrpLvalueHis> carLossess=new ArrayList<PrpLvalueHis>(0);
//	
//	private List<PrpLvalueHis> propLossess=new ArrayList<PrpLvalueHis>(0);
	
//	private List<FeeOfPersonVo> feeOfPersonVos=new ArrayList<FeeOfPersonVo>(0);
	
	/** 人员损失代赔---医疗审核费*/
	private double insteadMedFee;
	/** 人员损失代赔---死亡伤残费*/
	private double insteadDeadFee;
	
	/** 广东车险赔款支付全额转账 */
//	private List<PrpLpayeeInfo> prpLpayeeInfos = new ArrayList<PrpLpayeeInfo>(0);
	
	private PrpLCMainVo prpLCMainVo;
	
	private List<ThirdPartyRecoveryInfo> thirdPartyRecoveryInfoList = new ArrayList<ThirdPartyRecoveryInfo>(0);
	
	/** M险不计免赔险别、不计免赔率、不计免赔额List*/
	private List<MItemKindVo> mExceptKinds = new ArrayList<MItemKindVo>(0);
	
	/** 车辆实际价值对象 */
	ThirdPartyDepreRate thirdPartyDepreRate;
	
	/** 险种代码 */
	private String riskCode;
	
	/** 条款代码 */
	private String clauseType;
	
	/** A险免赔额 */
	private BigDecimal deductibleValue;
	
	/** 承保不计算免赔险别List */
	private List<PrpLCItemKindVo> isMitemKindList;
	
	/** 该保单出险时承保信息 */
	private List<PrpLCItemKindVo> prpLCItemKindVoList;
	
	/** 承保日期,prpLcMain.operateDate */
	private Date operateDate;
	/**  理算或者立案 compensate claim**/
	private String calculateType;
	/** 工保信息 */
//	private List<PrpLcCoins> prpLcCoinses = new ArrayList<PrpLcCoins>(0);
	
	/** 已赔付车辆损失项 **/
	private List<PrpLLossItemVo> prpLlossesByLossState;
	
//    private List<KindCalculator> kindCalculatorList = new ArrayList<KindCalculator>();
    /** 可选免赔勾选 **/
    private List<PrpLClaimDeductVo> claimDeductList = new ArrayList<PrpLClaimDeductVo>();
    /** 已冲销金额 **/
    private Map<String,BigDecimal> eMotorMap = new HashMap<String,BigDecimal>();
    
    /** 本案件已赔付金额 **/
    private Map<String,BigDecimal> kindPaidMap = new HashMap<String,BigDecimal>();
    /** 车损险保额 **/
    private BigDecimal amountKindA;
    /** 是否扣减过绝对免赔**/
	private boolean deductibleFlag;
	
	public PrpLCompensateVo getPrpLCompensateVo() {
		return prpLCompensateVo;
	}
	
	public void setPrpLCompensateVo(PrpLCompensateVo prpLCompensateVo) {
		this.prpLCompensateVo = prpLCompensateVo;
	}

	public CompensateVoResult getInsteadPrpLcompensateVo() {
		return insteadPrpLcompensateVo;
	}

	public void setInsteadPrpLcompensateVo(CompensateVoResult insteadPrpLcompensateVo) {
		this.insteadPrpLcompensateVo = insteadPrpLcompensateVo;
	}

	public Long getPersonAuditMainId() {
		return personAuditMainId;
	}

	public void setPersonAuditMainId(Long personAuditMainId) {
		this.personAuditMainId = personAuditMainId;
	}

	public List<PrpLLossItemVo> getPrpLLossItemVoList() {
		return prpLLossItemVoList;
	}

	public void setPrpLLossItemVoList(List<PrpLLossItemVo> prpLLossItemVoList) {
		this.prpLLossItemVoList = prpLLossItemVoList;
	}

	public List<PrpLLossItemVo> getPrpLLossItemVoQFList() {
		return prpLLossItemVoQFList;
	}

	public void setPrpLLossItemVoQFList(List<PrpLLossItemVo> prpLLossItemVoQFList) {
		this.prpLLossItemVoQFList = prpLLossItemVoQFList;
	}

	public List<PrpLLossItemVo> getPrpLLossItemVoZCList() {
		return prpLLossItemVoZCList;
	}

	public void setPrpLLossItemVoZCList(List<PrpLLossItemVo> prpLLossItemVoZCList) {
		this.prpLLossItemVoZCList = prpLLossItemVoZCList;
	}

	public List<PrpLLossItemVo> getPrpLLossItemVoZFList() {
		return prpLLossItemVoZFList;
	}

	public void setPrpLLossItemVoZFList(List<PrpLLossItemVo> prpLLossItemVoZFList) {
		this.prpLLossItemVoZFList = prpLLossItemVoZFList;
	}

	public List<PrpLChargeVo> getPrpLChargeVoList() {
		return prpLChargeVoList;
	}

	public void setPrpLChargeVoList(List<PrpLChargeVo> prpLChargeVoList) {
		this.prpLChargeVoList = prpLChargeVoList;
	}

	public List<PrpLLossPropVo> getPrpLLossPropVoList() {
		return prpLLossPropVoList;
	}
	
	public void setPrpLLossPropVoList(List<PrpLLossPropVo> prpLLossPropVoList) {
		this.prpLLossPropVoList = prpLLossPropVoList;
	}
	
	public List<PrpLLossPropVo> getPrpLOtherLossPropVoList() {
		return prpLOtherLossPropVoList;
	}
	
	public void setPrpLOtherLossPropVoList(List<PrpLLossPropVo> prpLOtherLossPropVoList) {
		this.prpLOtherLossPropVoList = prpLOtherLossPropVoList;
	}
	
	public List<PrpLLossPersonVo> getPrpLLossPersonVoList() {
		return prpLLossPersonVoList;
	}
	
	public void setPrpLLossPersonVoList(List<PrpLLossPersonVo> prpLLossPersonVoList) {
		this.prpLLossPersonVoList = prpLLossPersonVoList;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getSaveType() {
		return saveType;
	}

	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}
	
	public List<PrpLDlossCarMainVo> getPrpLDlossCarMainVoList() {
		return prpLDlossCarMainVoList;
	}
	
	public void setPrpLDlossCarMainVoList(List<PrpLDlossCarMainVo> prpLDlossCarMainVoList) {
		this.prpLDlossCarMainVoList = prpLDlossCarMainVoList;
	}

	public List<PrpLdlossPropMainVo> getPrpLdlossPropMainVoList() {
		return prpLdlossPropMainVoList;
	}
	
	public void setPrpLdlossPropMainVoList(List<PrpLdlossPropMainVo> prpLdlossPropMainVoList) {
		this.prpLdlossPropMainVoList = prpLdlossPropMainVoList;
	}

	public String getCompensateType() {
		return compensateType;
	}
	
	public void setCompensateType(String compensateType) {
		this.compensateType = compensateType;
	}
	
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public List<PrpLDlossPersTraceVo> getPrpLDlossPersTraceVoList() {
		return prpLDlossPersTraceVoList;
	}

	public void setPrpLDlossPersTraceVoList(List<PrpLDlossPersTraceVo> prpLDlossPersTraceVoList) {
		this.prpLDlossPersTraceVoList = prpLDlossPersTraceVoList;
	}
	
	public BigDecimal getExgratiaFee() {
		return exgratiaFee;
	}
	
	public void setExgratiaFee(BigDecimal exgratiaFee) {
		this.exgratiaFee = exgratiaFee;
	}

	public List<DefLossInfoOfA> getCarDefLossInfoOfAList() {
		return carDefLossInfoOfAList;
	}
	
	public void setCarDefLossInfoOfAList(List<DefLossInfoOfA> carDefLossInfoOfAList) {
		this.carDefLossInfoOfAList = carDefLossInfoOfAList;
	}

	public List<DefLossInfoOfA> getPropDefLossInfoOfAList() {
		return propDefLossInfoOfAList;
	}

	public void setPropDefLossInfoOfAList(List<DefLossInfoOfA> propDefLossInfoOfAList) {
		this.propDefLossInfoOfAList = propDefLossInfoOfAList;
	}

	public List<DefLossInfoOfA> getPersonDefLossInfoOfAList() {
		return personDefLossInfoOfAList;
	}
	
	public void setPersonDefLossInfoOfAList(List<DefLossInfoOfA> personDefLossInfoOfAList) {
		this.personDefLossInfoOfAList = personDefLossInfoOfAList;
	}

	public List<PrpLLossItemVo> getDutyInsteadPayCarInfos() {
		return dutyInsteadPayCarInfos;
	}
	
	public void setDutyInsteadPayCarInfos(List<PrpLLossItemVo> dutyInsteadPayCarInfos) {
		this.dutyInsteadPayCarInfos = dutyInsteadPayCarInfos;
	}
	
//	public List<PrpLLossItemVo> getPolicyInsteadPayCarInfos() {
//		return policyInsteadPayCarInfos;
//	}
//
//	public void setPolicyInsteadPayCarInfos(List<PrpLLossItemVo> policyInsteadPayCarInfos) {
//		this.policyInsteadPayCarInfos = policyInsteadPayCarInfos;
//	}
//	
//	public List<PrpLLossPropVo> getDutyInsteadPayPropInfos() {
//		return dutyInsteadPayPropInfos;
//	}
//
//	public void setDutyInsteadPayPropInfos(List<PrpLLossPropVo> dutyInsteadPayPropInfos) {
//		this.dutyInsteadPayPropInfos = dutyInsteadPayPropInfos;
//	}
//	
//	public List<PrpLLossPropVo> getPolicyInsteadPayPropInfos() {
//		return policyInsteadPayPropInfos;
//	}
//
//	public void setPolicyInsteadPayPropInfos(List<PrpLLossPropVo> policyInsteadPayPropInfos) {
//		this.policyInsteadPayPropInfos = policyInsteadPayPropInfos;
//	}
//	
//	public List<PrpLLossPersonVo> getPolicyInsteadPayPersonInfos() {
//		return policyInsteadPayPersonInfos;
//	}
//
//	public void setPolicyInsteadPayPersonInfos(List<PrpLLossPersonVo> policyInsteadPayPersonInfos) {
//		this.policyInsteadPayPersonInfos = policyInsteadPayPersonInfos;
//	}
	
	public String getPersonItemIdStr() {
		return personItemIdStr;
	}

	public void setPersonItemIdStr(String personItemIdStr) {
		this.personItemIdStr = personItemIdStr;
	}

	public PrpLLossItemVo getDefLossSimpleInfoOfA() {
		return defLossSimpleInfoOfA;
	}

	public void setDefLossSimpleInfoOfA(PrpLLossItemVo defLossSimpleInfoOfA) {
		this.defLossSimpleInfoOfA = defLossSimpleInfoOfA;
	}

	public List<String> getAuditPersonFlagList() {
		return auditPersonFlagList;
	}

	public void setAuditPersonFlagList(List<String> auditPersonFlagList) {
		this.auditPersonFlagList = auditPersonFlagList;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public double getInsteadMedFee() {
		return insteadMedFee;
	}
	
	public void setInsteadMedFee(double insteadMedFee) {
		this.insteadMedFee = insteadMedFee;
	}

	public double getInsteadDeadFee() {
		return insteadDeadFee;
	}

	public void setInsteadDeadFee(double insteadDeadFee) {
		this.insteadDeadFee = insteadDeadFee;
	}

	public PrpLCMainVo getPrpLCMainVo() {
		return prpLCMainVo;
	}

	public void setPrpLCMainVo(PrpLCMainVo prpLCMainVo) {
		this.prpLCMainVo = prpLCMainVo;
	}

	public List<ThirdPartyRecoveryInfo> getThirdPartyRecoveryInfoList() {
		return thirdPartyRecoveryInfoList;
	}

	public void setThirdPartyRecoveryInfoList(List<ThirdPartyRecoveryInfo> thirdPartyRecoveryInfoList) {
		this.thirdPartyRecoveryInfoList = thirdPartyRecoveryInfoList;
	}
	
	public List<MItemKindVo> getmExceptKinds() {
		return mExceptKinds;
	}

	public void setmExceptKinds(List<MItemKindVo> mExceptKinds) {
		this.mExceptKinds = mExceptKinds;
	}
	
	public ThirdPartyDepreRate getThirdPartyDepreRate() {
		return thirdPartyDepreRate;
	}
	
	public void setThirdPartyDepreRate(ThirdPartyDepreRate thirdPartyDepreRate) {
		this.thirdPartyDepreRate = thirdPartyDepreRate;
	}

	public String getRiskCode() {
		return riskCode;
	}
	
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	
	public String getClauseType() {
		return clauseType;
	}

	public void setClauseType(String clauseType) {
		this.clauseType = clauseType;
	}

	public BigDecimal getDeductibleValue() {
		return deductibleValue;
	}

	public void setDeductibleValue(BigDecimal deductibleValue) {
		this.deductibleValue = deductibleValue;
	}

	public List<PrpLCItemKindVo> getIsMitemKindList() {
		return isMitemKindList;
	}

	public void setIsMitemKindList(List<PrpLCItemKindVo> isMitemKindList) {
		this.isMitemKindList = isMitemKindList;
	}

	public List<PrpLCItemKindVo> getPrpLCItemKindVoList() {
		return prpLCItemKindVoList;
	}

	public void setPrpLCItemKindVoList(List<PrpLCItemKindVo> prpLCItemKindVoList) {
		this.prpLCItemKindVoList = prpLCItemKindVoList;
	}
	
	public Date getOperateDate() {
		return operateDate;
	}
	
	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public List<PrpLLossItemVo> getPrpLlossesByLossState() {
		return prpLlossesByLossState;
	}
	
	public void setPrpLlossesByLossState(List<PrpLLossItemVo> prpLlossesByLossState) {
		this.prpLlossesByLossState = prpLlossesByLossState;
	}

//	public List<KindCalculator> getKindCalculatorList() {
//		return kindCalculatorList;
//	}
//	
//	public void setKindCalculatorList(List<KindCalculator> kindCalculatorList) {
//		this.kindCalculatorList = kindCalculatorList;
//	}

	public String getCalculateType() {
		return calculateType;
	}

	public void setCalculateType(String calculateType) {
		this.calculateType = calculateType;
	}

	public List<PrpLClaimDeductVo> getClaimDeductList() {
		return claimDeductList;
	}

	public void setClaimDeductList(List<PrpLClaimDeductVo> claimDeductList) {
		this.claimDeductList = claimDeductList;
	}

	public Map<String,BigDecimal> geteMotorMap() {
		return eMotorMap;
	}

	public void seteMotorMap(Map<String,BigDecimal> eMotorMap) {
		this.eMotorMap = eMotorMap;
	}

	public Map<String,BigDecimal> getKindPaidMap() {
		return kindPaidMap;
	}

	public void setKindPaidMap(Map<String,BigDecimal> kindPaidMap) {
		this.kindPaidMap = kindPaidMap;
	}

	public BigDecimal getAmountKindA() {
		return amountKindA;
	}

	public void setAmountKindA(BigDecimal amountKindA) {
		this.amountKindA = amountKindA;
	}

	public boolean isDeductibleFlag() {
		return deductibleFlag;
	}

	public void setDeductibleFlag(boolean deductibleFlag) {
		this.deductibleFlag = deductibleFlag;
	}
	
}
