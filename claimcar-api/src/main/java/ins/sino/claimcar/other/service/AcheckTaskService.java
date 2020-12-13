package ins.sino.claimcar.other.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;

public interface AcheckTaskService {
	/**
	 * 查勘节点生成查勘费任务
	 * @param registNo
	 * @param userVo
	 */
	public void addCheckFeeTaskOfCheck(String registNo,SysUserVo userVo);
	/**
	 * 车辆定损节点生成查勘费任务
	 * @param lossCarMainVo
	 * @param userVo
	 * @param type--0-表示定损新增任务，1-表示核损回写任务字段
	 */
	public void addCheckFeeTaskOfDcar(PrpLDlossCarMainVo lossCarMainVo,SysUserVo userVo,String type);
	
	/**
	 * 人伤节点生成查勘费任务
	 * @param persTraceMainVo
	 * @param userVo
	 * @param type--0-表示新增任务，1-表示核损回写任务字段
	 */
	public void addCheckFeeTaskOfDpers(PrpLDlossPersTraceMainVo persTraceMainVo,SysUserVo userVo,String type);
	
	/**
	 * 财产节点生成查勘费任务
	 * @param lossPropMainVo
	 * @param userVo
	 * @param type--0-表示新增任务，1-表示核损回写任务字段
	 */
	public void addCheckFeeTaskOfDprop(PrpLdlossPropMainVo lossPropMainVo,SysUserVo userVo,String type);
	

}
