package ins.sino.claimcar.other.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;

public interface AssessorsTaskService {
	
	/**
	 * 查勘、车辆定损、财损、人伤生成公估费任务
	 * @param registNo
	 * @param userVo
	 * @param type
	 * @modified:
	 * ☆XiaoHuYao(2019年8月14日 上午10:04:16): <br>
	 */
	public void addAssTaskOfDLoss(Object mainVo,SysUserVo userVo,String type,String taskType);


	public void addAssTaskOfCheck(String registNo, SysUserVo userVo);

}
