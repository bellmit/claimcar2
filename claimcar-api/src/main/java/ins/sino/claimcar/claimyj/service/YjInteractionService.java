package ins.sino.claimcar.claimyj.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claimcarYJ.vo.ResYJVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;

/**
 * 
 * @author zhujunde
 *
 */
public interface YjInteractionService {

	/**
	 * 范围内配件向阳杰发起复检
	 * @param dlossCarMainVo
	 * @param userVo
	 * @return
	 */
	public ResYJVo sendDlChkInfoService(PrpLDlossCarMainVo dlossCarMainVo,SysUserVo userVo);
	
	/**
	 * 阳杰汽配核损接口
	 * @param dlossCarMainVo
	 * @param userVo
	 * @return
	 */
	public ResYJVo sendVLossInfoService(PrpLDlossCarMainVo dlossCarMainVo,SysUserVo userVo);
}
