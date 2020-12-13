/******************************************************************************
* CREATETIME : 2015年12月15日 上午8:45:12
* FILE       : ins.sino.claimcar.losscar.vo.DeflossActionVo
******************************************************************************/
package ins.sino.claimcar.claim.vo;

import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CompensateActionVo  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<PrpLCItemKindVo> claimKindMList;
	private boolean cprcCase; 
	private List<PrpLClaimDeductVo> claimDeductVos;
	
	private Map<String, String> deviceMap;
	
	private List<PrpLPadPayPersonVo> padPayPersons ;
	private List<PrpLPrePayVo> prpLPrePayP;
	private List<PrpLPrePayVo> prpLPrePayF;
	private Map<String,String> carMap;
	private String compFlag;
	private String handlerStatus;
	private String payrefFlag;
	//是否包含无法找到第三方条款
	private boolean haveKindNT = false;
	private String dwFlag;
	/**被保险人收款人 id **/
	private Long prpLId;
	/**11-交强 12-商业 **/
	private String bcFlag;
	/**无法找到第三方免赔条件  **/
	private boolean noFindThirdCond;
	// 理算中如果有车上乘客险的 要校验车上人员数据小于低于承保的人数
	private BigDecimal personCount; 
	private String buJiState;
	private String dwPersFlag;//非车代位标志
	
	private BigDecimal unitamount;
	private double flowTaskId;
	private String subRogationFlag;
	private PrpLWfTaskVo wfTaskVo;
	private PrpLRegistVo registVo;
	private List<PrpLPaymentVo> paymentVoList;
	private PrpLCompensateVo compensateVo;
	private List<PrpLLossItemVo>  lossItemVoList;
	private List<PrpLLossPropVo> lossPropList;
	private List<PrpLLossPropVo> lossPropOthVos;
	private List<PrpLLossPersonVo> lossPersonList;
	private List<PrpLLossPropVo> otherLossProps;
	private List<PrpLChargeVo> prpLChargees;
	private Map<String, String> kindForChaMap = new HashMap<String, String>();// 费用赔款信息表险别Map
	private Map<String, String> kindForOthMap = new HashMap<String, String>();// 其他损失表险别Map
	private Map<String,String> qfLicenseMap = new HashMap<String, String>();//追偿车牌号	
	
	
	public String getPayrefFlag() {
		return payrefFlag;
	}
	public void setPayrefFlag(String payrefFlag) {
		this.payrefFlag = payrefFlag;
	}
	public List<PrpLPaymentVo> getPaymentVoList() {
		return paymentVoList;
	}
	public void setPaymentVoList(List<PrpLPaymentVo> paymentVoList) {
		this.paymentVoList = paymentVoList;
	}
	public PrpLCompensateVo getCompensateVo() {
		return compensateVo;
	}
	public void setCompensateVo(PrpLCompensateVo compensateVo) {
		this.compensateVo = compensateVo;
	}
	public List<PrpLLossItemVo> getLossItemVoList() {
		return lossItemVoList;
	}
	public void setLossItemVoList(List<PrpLLossItemVo> lossItemVoList) {
		this.lossItemVoList = lossItemVoList;
	}
	public List<PrpLLossPropVo> getLossPropList() {
		return lossPropList;
	}
	public void setLossPropList(List<PrpLLossPropVo> lossPropList) {
		this.lossPropList = lossPropList;
	}
	public List<PrpLLossPropVo> getLossPropOthVos() {
		return lossPropOthVos;
	}
	public void setLossPropOthVos(List<PrpLLossPropVo> lossPropOthVos) {
		this.lossPropOthVos = lossPropOthVos;
	}
	public List<PrpLLossPersonVo> getLossPersonList() {
		return lossPersonList;
	}
	public void setLossPersonList(List<PrpLLossPersonVo> lossPersonList) {
		this.lossPersonList = lossPersonList;
	}
	public List<PrpLChargeVo> getPrpLChargees() {
		return prpLChargees;
	}
	public void setPrpLChargees(List<PrpLChargeVo> prpLChargees) {
		this.prpLChargees = prpLChargees;
	}
	public Map<String, String> getKindForChaMap() {
		return kindForChaMap;
	}
	public void setKindForChaMap(Map<String, String> kindForChaMap) {
		this.kindForChaMap = kindForChaMap;
	}
	public Map<String, String> getKindForOthMap() {
		return kindForOthMap;
	}
	public void setKindForOthMap(Map<String, String> kindForOthMap) {
		this.kindForOthMap = kindForOthMap;
	}
	public Map<String, String> getQfLicenseMap() {
		return qfLicenseMap;
	}
	public void setQfLicenseMap(Map<String, String> qfLicenseMap) {
		this.qfLicenseMap = qfLicenseMap;
	}
	public boolean isNoFindThirdCond() {
		return noFindThirdCond;
	}
	public void setNoFindThirdCond(boolean noFindThirdCond) {
		this.noFindThirdCond = noFindThirdCond;
	}
	public BigDecimal getAmountKindA() {
		return amountKindA;
	}
	public void setAmountKindA(BigDecimal amountKindA) {
		this.amountKindA = amountKindA;
	}
	public String getCaseFlag() {
		return caseFlag;
	}
	public void setCaseFlag(String caseFlag) {
		this.caseFlag = caseFlag;
	}
	private BigDecimal amountKindA;
	
	private String caseFlag;
	
	public List<PrpLCItemKindVo> getClaimKindMList() {
		return claimKindMList;
	}
	public void setClaimKindMList(List<PrpLCItemKindVo> claimKindMList) {
		this.claimKindMList = claimKindMList;
	}
	public boolean isCprcCase() {
		return cprcCase;
	}
	public void setCprcCase(boolean cprcCase) {
		this.cprcCase = cprcCase;
	}
	public List<PrpLClaimDeductVo> getClaimDeductVos() {
		return claimDeductVos;
	}
	public void setClaimDeductVos(List<PrpLClaimDeductVo> claimDeductVos) {
		this.claimDeductVos = claimDeductVos;
	}
	public Map<String, String> getDeviceMap() {
		return deviceMap;
	}
	public void setDeviceMap(Map<String, String> deviceMap) {
		this.deviceMap = deviceMap;
	}
	public List<PrpLPadPayPersonVo> getPadPayPersons() {
		return padPayPersons;
	}
	public void setPadPayPersons(List<PrpLPadPayPersonVo> padPayPersons) {
		this.padPayPersons = padPayPersons;
	}
	public List<PrpLPrePayVo> getPrpLPrePayP() {
		return prpLPrePayP;
	}
	public void setPrpLPrePayP(List<PrpLPrePayVo> prpLPrePayP) {
		this.prpLPrePayP = prpLPrePayP;
	}
	public List<PrpLPrePayVo> getPrpLPrePayF() {
		return prpLPrePayF;
	}
	public void setPrpLPrePayF(List<PrpLPrePayVo> prpLPrePayF) {
		this.prpLPrePayF = prpLPrePayF;
	}
	public Map<String,String> getCarMap() {
		return carMap;
	}
	public void setCarMap(Map<String,String> carMap) {
		this.carMap = carMap;
	}
	public String getCompFlag() {
		return compFlag;
	}
	public void setCompFlag(String compFlag) {
		this.compFlag = compFlag;
	}
	public String getHandlerStatus() {
		return handlerStatus;
	}
	public void setHandlerStatus(String handlerStatus) {
		this.handlerStatus = handlerStatus;
	}
	public boolean isHaveKindNT() {
		return haveKindNT;
	}
	public void setHaveKindNT(boolean haveKindNT) {
		this.haveKindNT = haveKindNT;
	}
	public String getDwFlag() {
		return dwFlag;
	}
	public void setDwFlag(String dwFlag) {
		this.dwFlag = dwFlag;
	}
	public Long getPrpLId() {
		return prpLId;
	}
	public void setPrpLId(Long prpLId) {
		this.prpLId = prpLId;
	}
	public String getBcFlag() {
		return bcFlag;
	}
	public void setBcFlag(String bcFlag) {
		this.bcFlag = bcFlag;
	}
	public BigDecimal getUnitamount() {
		return unitamount;
	}
	public void setUnitamount(BigDecimal unitamount) {
		this.unitamount = unitamount;
	}
	public double getFlowTaskId() {
		return flowTaskId;
	}
	public void setFlowTaskId(double flowTaskId) {
		this.flowTaskId = flowTaskId;
	}
	public String getSubRogationFlag() {
		return subRogationFlag;
	}
	public void setSubRogationFlag(String subRogationFlag) {
		this.subRogationFlag = subRogationFlag;
	}
	public PrpLWfTaskVo getWfTaskVo() {
		return wfTaskVo;
	}
	public void setWfTaskVo(PrpLWfTaskVo wfTaskVo) {
		this.wfTaskVo = wfTaskVo;
	}
	public PrpLRegistVo getRegistVo() {
		return registVo;
	}
	public void setRegistVo(PrpLRegistVo registVo) {
		this.registVo = registVo;
	}
	public BigDecimal getPersonCount() {
		return personCount;
	}
	public void setPersonCount(BigDecimal personCount) {
		this.personCount = personCount;
	}
	public List<PrpLLossPropVo> getOtherLossProps() {
		return otherLossProps;
	}
	public void setOtherLossProps(List<PrpLLossPropVo> otherLossProps) {
		this.otherLossProps = otherLossProps;
	}
	public String getBuJiState() {
		return buJiState;
	}
	public void setBuJiState(String buJiState) {
		this.buJiState = buJiState;
	}
	public String getDwPersFlag() {
		return dwPersFlag;
	}
	public void setDwPersFlag(String dwPersFlag) {
		this.dwPersFlag = dwPersFlag;
	}
	
	
	
	
	
}