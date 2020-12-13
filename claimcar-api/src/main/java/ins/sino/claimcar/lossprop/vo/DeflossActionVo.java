package ins.sino.claimcar.lossprop.vo;

import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.vo.DefCommonVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.List;
import java.util.Map;

public class DeflossActionVo  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String registNo;

	private Long businessId;

	private String userCode;

	private String userName;

	private String comCode;

	private String comName;

	private PrpLdlossPropMainVo lossPropMainVo;

	private PrpLClaimTextVo claimTextVo;

	private List<PrpLDlossChargeVo> lossChargeVos;

	private List<PrpLClaimTextVo> claimTextVos;

	private Map<String,String> kindMap;

	private DefCommonVo commonVo;

	private WfTaskSubmitVo taskSubmitVo;

	private PrpLWfTaskVo taskVo;
	
	private PrpLRegistVo registVo;

	private Map<String,String> intermMap;
	
	private List<PrpLdlossPropMainVo> lossPropMainVos;

	public Map<String,String> getIntermMap() {
		return intermMap;
	}

	/** 损失信息 车辆损失 **/
	private List<PrpLDlossCarMainVo> lossCarMainList;

	/** 损失信息 财产损失 **/
	private List<PrpLdlossPropFeeVo> lossPropFeeVos;

	/** 损失信息 人伤损失 **/
	private List<PrpLDlossPersTraceVo> lossPersTraceList;

	public void setIntermMap(Map<String,String> intermMap) {
		this.intermMap = intermMap;
	}

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public PrpLdlossPropMainVo getLossPropMainVo() {
		return lossPropMainVo;
	}

	public void setLossPropMainVo(PrpLdlossPropMainVo lossPropMainVo) {
		this.lossPropMainVo = lossPropMainVo;
	}

	public PrpLClaimTextVo getClaimTextVo() {
		return claimTextVo;
	}

	public void setClaimTextVo(PrpLClaimTextVo claimTextVo) {
		this.claimTextVo = claimTextVo;
	}

	public List<PrpLDlossChargeVo> getLossChargeVos() {
		return lossChargeVos;
	}

	public void setLossChargeVos(List<PrpLDlossChargeVo> lossChargeVos) {
		this.lossChargeVos = lossChargeVos;
	}

	public List<PrpLClaimTextVo> getClaimTextVos() {
		return claimTextVos;
	}

	public void setClaimTextVos(List<PrpLClaimTextVo> claimTextVos) {
		this.claimTextVos = claimTextVos;
	}

	public Map<String,String> getKindMap() {
		return kindMap;
	}

	public void setKindMap(Map<String,String> kindMap) {
		this.kindMap = kindMap;
	}

	public DefCommonVo getCommonVo() {
		return commonVo;
	}

	public void setCommonVo(DefCommonVo commonVo) {
		this.commonVo = commonVo;
	}

	public WfTaskSubmitVo getTaskSubmitVo() {
		return taskSubmitVo;
	}

	public void setTaskSubmitVo(WfTaskSubmitVo taskSubmitVo) {
		this.taskSubmitVo = taskSubmitVo;
	}

	public PrpLWfTaskVo getTaskVo() {
		return taskVo;
	}

	public void setTaskVo(PrpLWfTaskVo taskVo) {
		this.taskVo = taskVo;
	}

	public PrpLRegistVo getRegistVo() {
		return registVo;
	}

	public void setRegistVo(PrpLRegistVo registVo) {
		this.registVo = registVo;
	}

	public List<PrpLDlossCarMainVo> getLossCarMainList() {
		return lossCarMainList;
	}

	public void setLossCarMainList(List<PrpLDlossCarMainVo> lossCarMainList) {
		this.lossCarMainList = lossCarMainList;
	}

	public List<PrpLDlossPersTraceVo> getLossPersTraceList() {
		return lossPersTraceList;
	}

	public void setLossPersTraceList(List<PrpLDlossPersTraceVo> lossPersTraceList) {
		this.lossPersTraceList = lossPersTraceList;
	}

	public List<PrpLdlossPropFeeVo> getLossPropFeeVos() {
		return lossPropFeeVos;
	}

	public void setLossPropFeeVos(List<PrpLdlossPropFeeVo> lossPropFeeVos) {
		this.lossPropFeeVos = lossPropFeeVos;
	}

	public List<PrpLdlossPropMainVo> getLossPropMainVos() {
		return lossPropMainVos;
	}

	public void setLossPropMainVos(List<PrpLdlossPropMainVo> lossPropMainVos) {
		this.lossPropMainVos = lossPropMainVos;
	}

}
