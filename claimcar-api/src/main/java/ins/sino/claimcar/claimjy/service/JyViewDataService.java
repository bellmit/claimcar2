package ins.sino.claimcar.claimjy.service;




import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claimjy.vo.JyResVo;
import ins.sino.claimcar.losscar.vo.ClaimFittingVo;



/**
 * 
 * @author zhujunde
 *
 */
public interface JyViewDataService {

	/**
	 * 定损查看接口 (从理赔请求定损工具)
	 * @param registNo
	 * @param dmgVhclId
	 * @param userVo
	 * @return
	 */
	public JyResVo sendViewDataService(ClaimFittingVo claimFittingVo,String registNo,String dmgVhclId,SysUserVo userVo);
}
