/******************************************************************************
* CREATETIME : 2015年12月15日 上午8:45:12
* FILE       : ins.sino.claimcar.losscar.vo.DeflossActionVo
******************************************************************************/
package ins.sino.claimcar.losscar.vo;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * <pre></pre>
 * @author ★yangkun
 */
public class DeflossActionVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String registNo;
	
	private Long businessId;
	
	private SysUserVo userVo;
	
	private PrpLDlossCarMainVo lossCarMainVo;
	
	private PrpLClaimTextVo claimTextVo;
	
	private List<PrpLDlossChargeVo> lossChargeVos;
	
	private PrpLDlossChargeVo checkChargeVo;
	
	private List<PrpLClaimTextVo> claimTextVos;
	
	private PrpLDlossCarInfoVo carInfoVo;
	
	private Map<String,String> kindMap ;
	private PrpLSubrogationMainVo subrogationMainVo;
	private DefCommonVo commonVo;
	
	private PrpLRegistVo registVo;
	
	private WfTaskSubmitVo taskSubmitVo;
	
	private PrpLWfTaskVo taskVo;
	
	private Map<String,String> intermMap;
	
	private List<PrpLdlossPropMainVo> lossPropMainVos;
	
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

	
	public PrpLDlossCarMainVo getLossCarMainVo() {
		return lossCarMainVo;
	}

	
	public void setLossCarMainVo(PrpLDlossCarMainVo lossCarMainVo) {
		this.lossCarMainVo = lossCarMainVo;
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

	
	public PrpLDlossCarInfoVo getCarInfoVo() {
		return carInfoVo;
	}

	
	public void setCarInfoVo(PrpLDlossCarInfoVo carInfoVo) {
		this.carInfoVo = carInfoVo;
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
	
	public Map<String,String> getIntermMap() {
		return intermMap;
	}
	
	public void setIntermMap(Map<String,String> intermMap) {
		this.intermMap = intermMap;
	}


	public PrpLSubrogationMainVo getSubrogationMainVo() {
		return subrogationMainVo;
	}


	public void setSubrogationMainVo(PrpLSubrogationMainVo subrogationMainVo) {
		this.subrogationMainVo = subrogationMainVo;
	}


	public PrpLDlossChargeVo getCheckChargeVo() {
		return checkChargeVo;
	}


	public void setCheckChargeVo(PrpLDlossChargeVo checkChargeVo) {
		this.checkChargeVo = checkChargeVo;
	}


	public PrpLRegistVo getRegistVo() {
		return registVo;
	}


	public void setRegistVo(PrpLRegistVo registVo) {
		this.registVo = registVo;
	}


	public SysUserVo getUserVo() {
		return userVo;
	}


	public void setUserVo(SysUserVo userVo) {
		this.userVo = userVo;
	}


	public List<PrpLdlossPropMainVo> getLossPropMainVos() {
		return lossPropMainVos;
	}


	public void setLossPropMainVos(List<PrpLdlossPropMainVo> lossPropMainVos) {
		this.lossPropMainVos = lossPropMainVos;
	}
	
	
}