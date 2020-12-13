package ins.sino.claimcar.regist.service;

import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistRelationshipHisVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RegistHandlerService {

	/**
	 * 报案暂存或提交
	 * @param prpLCMains
	 * @param prpLRegist
	 * @param flag 
	 * @param prpLCMains 
	 * @throws ParseException 
	 */
	public PrpLRegistVo save(PrpLRegistVo prpLRegist,
			List<PrpLCMainVo> prpLCMain, boolean flag, String BIPolicyNo,
			String CIPolicyNo) throws ParseException;

	/**
	 * 查询标的的车辆品牌名称
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆Luwei(2016年6月24日 上午9:13:51): <br>
	 */
	public String queryCarModel(String registNo);

	public void savePrpLCMains(List<PrpLCMainVo> prpLCMains,
			PrpLRegistVo prpLRegist);

	/**
	 * 保单关联操作
	 * @param prpLRegistVo
	 * @param toSaveMainVoList
	 * @param relationshipHisVo
	 * @return
	 */
	public List<PrpLCMainVo> relationPolicy(PrpLRegistVo prpLRegistVo,
			List<PrpLCMainVo> toSaveMainVoList,
			PrpLRegistRelationshipHisVo relationshipHisVo);

	/**
	 * 报案注销
	 * @param prpLRegistVo
	 */
	public PrpLRegistVo reportCancel(PrpLRegistVo prpLRegistVo);

	/**
	 * 无保传有保确认
	 * @param registVo
	 * @param prpLCMains
	 */
	public void noPolicyFindNoConfirm(PrpLRegistVo registVo,
			List<PrpLCMainVo> prpLCMains);

	/**
	 * 拼装风险提示信息
	 * @param registRiskInfoMap
	 * @param policyNoArr2 
	 * @param policyNoArr 
	 * @param policyNoArr3 
	 * @throws ParseException 
	 */
	public Map<String, String> supplementRiskInfo(Map<String, String> registRiskInfoMap,
			Date damageDate, String policyNo, String linkpolicyNo,
			PrpLRegistVo registVo) throws ParseException;

	public void updateRiskInfo(PrpLRegistVo prpLRegistVo) throws ParseException;

	/**
	 * 推送修,标的车通过承保的车辆品牌去对应修理厂,三者车页面选择
	 * @param PrpLRegistCarLossVo--报案号车辆信息
	 * @param brandName--车辆品牌名称
	 * @param address--出险地址
	 * @modified: ☆Luwei(2016年6月23日 下午12:51:32): <br>
	 */
	public void setRepairFactory(PrpLRegistCarLossVo vo, String brandName,
			String address, String registNo);
	
}