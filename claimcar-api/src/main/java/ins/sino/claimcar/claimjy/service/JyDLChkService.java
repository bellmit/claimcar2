package ins.sino.claimcar.claimjy.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.losscar.vo.ClaimFittingVo;

/**
 * 精友复检接口(从理赔到定损系统)
 * 
 * <pre></pre>
 * @author ★LiYi
 */
public interface JyDLChkService {

	public String dLChkAskService(ClaimFittingVo claimFittingVo,SysUserVo sysUserVo);
}
