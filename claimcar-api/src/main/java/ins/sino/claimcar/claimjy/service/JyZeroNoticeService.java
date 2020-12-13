package ins.sino.claimcar.claimjy.service;




import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claimjy.vo.JyResVo;



/**
 * 
 * @author zhujunde
 *
 */
public interface JyZeroNoticeService {

	/**
	 * 定损零结通知接口
	 * @param registNo
	 * @param dmgVhclId
	 * @param remark
	 * @param userVo
	 * @return
	 */
	public JyResVo sendZeroNoticeService(String registNo,String dmgVhclId,SysUserVo userVo);
}
