package ins.sino.claimcar.trafficplatform.service;

import java.util.List;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.trafficplatform.vo.PrpLsdpoliceInfoVo;

public interface SdpoliceService {
	
	 /**
	  * 山东预警定核损登记功能(接口)
	  * @param registNo
	  * @throws Exception
	  */
	public void sendDlossRegister(PrpLCMainVo prpLCMainVo,SysUserVo userVo)throws Exception;
	
	/**
	 * 保存山东预警信息表
	 * @param infoListVo
	 * @throws Exception
	 */
	public void saveprplsdpoliceInfo(List<PrpLsdpoliceInfoVo> infoVoList)throws Exception;
	/**
	 * 通过报案号查询山东预警信息表
	 * @param registNo
	 * @return
	 */
	public List<PrpLsdpoliceInfoVo> findPrpLsdpoliceInfoVoByRegistNo(String registNo);
	
	
}
