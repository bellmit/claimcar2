/******************************************************************************
 * CREATETIME : 2015年12月23日 下午7:49:46
 ******************************************************************************/
package ins.sino.claimcar.check.vo;

import java.util.*;

import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.regist.vo.*;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;

/**
 * @author ★Luwei
 */
public class CheckActionVo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private boolean checkflag = false;
	private String acceptFlag;
	private String registNo;
	private String nodeCode;// 父节点的名称
	private String subNodeCode; // 子节点的名称
	private String handlerStatus;// 案件状态 0.未处理 １．正在处理 ２．已处理 3．退回
	private String workStatus;// 工作状态
	private String businessType = "0"; // 查勘审核退回标识为VC
	private String makeCom = "000000";// 保单归属机构
	private String grading; // 案件等级
	private Long scheduledTaskId = null;// 调度任务Id
	private Long flowTaskId = null;// 工作流任务Id
	private String flowId = null;// 工作流Id
	private Long checkTaskId = null;// 查勘任务Id
	private Long carId;
	private Map<String,String> lossItem;// 损失方
	private Map<String,String> kindMap;// 保单承保险别
	private String saveType;// 保存类型
	private Map<String,String> GServiceType;//公估服务类型

	private String isAccept = "";// 是否已接收
	private String coinsFlag;//联共保标志
	private String payrefFlag;//是否实收

	private PrpLCheckVo prpLcheckVo;
	private PrpLCheckTaskVo prpLcheckTaskVo;
	private List<PrpLCheckExtVo> checkExtList;
	private PrpLRegistVo prpLregistVo;
	private List<PrpLClaimTextVo> claimTextVoList;
	private List<PrpLCMainVo> policyInfo;

	private PrpLCheckCarVo checkMainCarVo;// 标的车
	private List<PrpLCheckCarVo> checkThirdCarList;// 三者车
	private List<PrpLCheckPropVo> checkPropList;// 财产损失
	private List<PrpLCheckPersonVo> checkPersonList;// 人伤损失
	private PrpLDisasterVo disasterVo;// 巨灾
	private List<PrpLDlossChargeVo> lossChargeVo;// 查勘除公估费的费用信息
	private PrpLCheckDutyVo checkDutyVo;//车辆责任比例
	private List<PrpLClaimDeductVo> claimDeductVoList;//案件理赔免赔率
	private PrpLSubrogationMainVo subrogationMainVo;//案件理赔免赔率
	
	
	
	public String getCoinsFlag() {
		return coinsFlag;
	}

	public void setCoinsFlag(String coinsFlag) {
		this.coinsFlag = coinsFlag;
	}

	public String getPayrefFlag() {
		return payrefFlag;
	}

	public void setPayrefFlag(String payrefFlag) {
		this.payrefFlag = payrefFlag;
	}

	public String getAcceptFlag() {
		return acceptFlag;
	}

	public void setAcceptFlag(String acceptFlag) {
		this.acceptFlag = acceptFlag;
	}

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public String getSubNodeCode() {
		return subNodeCode;
	}

	public void setSubNodeCode(String subNodeCode) {
		this.subNodeCode = subNodeCode;
	}

	public String getHandlerStatus() {
		return handlerStatus;
	}

	public void setHandlerStatus(String handlerStatus) {
		this.handlerStatus = handlerStatus;
	}

	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getMakeCom() {
		return makeCom;
	}

	public void setMakeCom(String makeCom) {
		this.makeCom = makeCom;
	}

	public String getGrading() {
		return grading;
	}

	public void setGrading(String grading) {
		this.grading = grading;
	}

	public Long getScheduledTaskId() {
		return scheduledTaskId;
	}

	public void setScheduledTaskId(Long scheduledTaskId) {
		this.scheduledTaskId = scheduledTaskId;
	}

	public Long getFlowTaskId() {
		return flowTaskId;
	}

	public void setFlowTaskId(Long flowTaskId) {
		this.flowTaskId = flowTaskId;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public Long getCheckTaskId() {
		return checkTaskId;
	}

	public void setCheckTaskId(Long checkTaskId) {
		this.checkTaskId = checkTaskId;
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public Map<String,String> getLossItem() {
		return lossItem;
	}

	public void setLossItem(Map<String,String> lossItem) {
		this.lossItem = lossItem;
	}

	public String getSaveType() {
		return saveType;
	}

	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}

	public String getIsAccept() {
		return isAccept;
	}

	public void setIsAccept(String isAccept) {
		this.isAccept = isAccept;
	}

	public List<PrpLCMainVo> getPolicyInfo() {
		return policyInfo;
	}

	public void setPolicyInfo(List<PrpLCMainVo> policyInfo) {
		this.policyInfo = policyInfo;
	}

	public PrpLCheckTaskVo getPrpLcheckTaskVo() {
		return prpLcheckTaskVo;
	}

	public void setPrpLcheckTaskVo(PrpLCheckTaskVo prpLcheckTaskVo) {
		this.prpLcheckTaskVo = prpLcheckTaskVo;
	}

	public PrpLCheckVo getPrpLcheckVo() {
		return prpLcheckVo;
	}

	public void setPrpLcheckVo(PrpLCheckVo prpLcheckVo) {
		this.prpLcheckVo = prpLcheckVo;
	}

	public PrpLRegistVo getPrpLregistVo() {
		return prpLregistVo;
	}

	public void setPrpLregistVo(PrpLRegistVo prpLregistVo) {
		this.prpLregistVo = prpLregistVo;
	}

	public List<PrpLClaimTextVo> getClaimTextVoList() {
		return claimTextVoList;
	}

	public void setClaimTextVoList(List<PrpLClaimTextVo> claimTextVoList) {
		this.claimTextVoList = claimTextVoList;
	}

	public boolean isCheckflag() {
		return checkflag;
	}

	public void setCheckflag(boolean checkflag) {
		this.checkflag = checkflag;
	}

	public PrpLCheckCarVo getCheckMainCarVo() {
		return checkMainCarVo;
	}

	public void setCheckMainCarVo(PrpLCheckCarVo checkMainCarVo) {
		this.checkMainCarVo = checkMainCarVo;
	}

	public List<PrpLCheckCarVo> getCheckThirdCarList() {
		return checkThirdCarList;
	}

	public void setCheckThirdCarList(List<PrpLCheckCarVo> checkThirdCarList) {
		this.checkThirdCarList = checkThirdCarList;
	}

	public List<PrpLCheckPropVo> getCheckPropList() {
		return checkPropList;
	}

	public void setCheckPropList(List<PrpLCheckPropVo> checkPropList) {
		this.checkPropList = checkPropList;
	}

	public List<PrpLCheckPersonVo> getCheckPersonList() {
		return checkPersonList;
	}

	public void setCheckPersonList(List<PrpLCheckPersonVo> checkPersonList) {
		this.checkPersonList = checkPersonList;
	}

	public List<PrpLCheckExtVo> getCheckExtList() {
		return checkExtList;
	}

	public void setCheckExtList(List<PrpLCheckExtVo> checkExtList) {
		this.checkExtList = checkExtList;
	}

	public PrpLDisasterVo getDisasterVo() {
		return disasterVo;
	}

	public void setDisasterVo(PrpLDisasterVo disasterVo) {
		this.disasterVo = disasterVo;
	}

	public Map<String,String> getKindMap() {
		return kindMap;
	}

	public void setKindMap(Map<String,String> kindMap) {
		this.kindMap = kindMap;
	}

	public List<PrpLDlossChargeVo> getLossChargeVo() {
		return lossChargeVo;
	}

	public void setLossChargeVo(List<PrpLDlossChargeVo> lossChargeVo) {
		this.lossChargeVo = lossChargeVo;
	}

	public PrpLCheckDutyVo getCheckDutyVo() {
		return checkDutyVo;
	}

	public void setCheckDutyVo(PrpLCheckDutyVo checkDutyVo) {
		this.checkDutyVo = checkDutyVo;
	}

	public List<PrpLClaimDeductVo> getClaimDeductVoList() {
		return claimDeductVoList;
	}

	public void setClaimDeductVoList(List<PrpLClaimDeductVo> claimDeductVoList) {
		this.claimDeductVoList = claimDeductVoList;
	}

	
	public Map<String,String> getGServiceType() {
		return GServiceType;
	}

	
	public void setGServiceType(Map<String,String> gServiceType) {
		GServiceType = gServiceType;
	}

	
	public PrpLSubrogationMainVo getSubrogationMainVo() {
		return subrogationMainVo;
	}

	
	public void setSubrogationMainVo(PrpLSubrogationMainVo subrogationMainVo) {
		this.subrogationMainVo = subrogationMainVo;
	}

}
