package ins.sino.claimcar.claimcarYJ.service;

import java.util.List;
import java.util.Map;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claimcarYJ.vo.SupplyYjVo;

public interface ClaimcarYJService {
    /**
     * 阳杰询价新增接口
     * @param id --定损车辆主表ID
     * @param userVo
     */
	public void claimcarYJAskPriceAdd(Long id,SysUserVo userVo);
	/**
	 * 阳杰配件下单接口
	 * @param id
	 * @param userVo
	 */
	public Map<String,String> claimcarYJOrder(Long id,SysUserVo userVo,List<SupplyYjVo> supplyVos);
}
