package ins.sino.claimcar.ciitc.service;

import ins.platform.vo.SysUserVo;

import java.util.List;

import ins.sino.claimcar.ciitc.vo.PrplZBXAreaVo;
import ins.sino.claimcar.ciitc.vo.PrplZBXPushInfoVo;
import ins.sino.claimcar.ciitc.vo.accident.CiitcAccidentResVo;


public interface AccidentService{

	/**
	 * 事故信息查询接口（保险公司请求中国保信）
	 * @param registNo
	 * @param userVo
	 * @return
	 */
	public CiitcAccidentResVo findAccidentInfoByRegistNo(String registNo,SysUserVo userVo);

	
	/**
	 * 根据registNo,licenseNo查询PrplZBXPushInfoVo
	 * @param registNo
	 * @param licenseNo
	 * @return
	 */
	public List<PrplZBXPushInfoVo> findAccidentInfoByOther(String registNo,String licenseNo);
	/**
	 * 中保信推送信息保存
	 * @param prplZBXPushInfoVos
	 */
	public void saveAccidentInfo(List<PrplZBXPushInfoVo> prplZBXPushInfoVos);
	
	/**
	 * 中保信根据行政区划的代码查询地区
	 * @param areaCode
	 * @return
	 */
	public PrplZBXAreaVo findZBXArea(String areaCode);
	
	
	public List<PrplZBXPushInfoVo> findAccidentInfo(String frameNo,
			String engineNo, String licenseNo);


	public PrplZBXPushInfoVo findInfoByAcciNo(String acciNo);
	/**
	 * 中保信推送信息更新
	 * @param prplZBXPushInfoVos
	 */
	public void updateAccidentInfo(PrplZBXPushInfoVo prplZBXPushInfo);
} 
