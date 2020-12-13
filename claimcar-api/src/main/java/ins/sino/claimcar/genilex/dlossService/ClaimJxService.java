package ins.sino.claimcar.genilex.dlossService;

import ins.platform.vo.SysUserVo;

public interface ClaimJxService {
	/**
	 * 理赔定损信息发送给精励联讯
	 * @param registNo
	 * @param taskId
	 * @param userVo
	 */
	public void sendDlossInfor(String registNo,String taskId, SysUserVo userVo);
}
