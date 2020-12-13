package ins.sino.claimcar.claimjy.service;




import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claimjy.vo.JyResVo;



/**
 * 
 * @author zhujunde
 *
 */
public interface JyCleanDataService {

	/**
	 * 推定全损数据清空接口
	 * @param registNo
	 * @param dmgVhclId
	 * @param remark
	 * @param userVo
	 * @return
	 */
	public JyResVo sendCleanDataService(String registNo,String dmgVhclId,SysUserVo userVo);
}
