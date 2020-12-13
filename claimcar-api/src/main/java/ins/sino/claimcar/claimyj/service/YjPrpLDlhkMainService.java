package ins.sino.claimcar.claimyj.service;

import java.util.List;

import ins.sino.claimcar.claimcarYJ.vo.PrpLDlhkMainVo;


/**
 * 阳杰接收接口
 */

public interface YjPrpLDlhkMainService{
	
	/**
	 * 保存复勘主表PrpLDlhkMainVo
	 * @param prpLDlhkMainVo
	 * @return
	 */
	public void savePrpLDlhkMain(PrpLDlhkMainVo prpLDlhkMainVo);
	
	/**
	 * 
	 * @param registNo
	 * @return
	 */
	public List<PrpLDlhkMainVo> findPrpLDlhkMains(String registNo);
	
	/**
	 * 
	 * @param topActualId
	 * @return
	 */
	public List<PrpLDlhkMainVo> findPrpLDlhkMainsBytopActualId(String topActualId);
}
