package ins.sino.claimcar.claim.service;

import ins.sino.claimcar.claim.vo.PrpDAccidentDeductVo;
import ins.sino.claimcar.claim.vo.PrpDDeprecateRateVo;
import ins.sino.claimcar.claim.vo.PrpLDLimitVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ConfigService {

	/**
	 * 限额表
	 * @param ciindemDuty 有责无责，1-有责 0-无责	CodeConstants.DutyType
	 * @return
	 * @modified:
	 */
	public List<PrpLDLimitVo> findPrpLDLimitList(String ciindemDuty,String registNo);

	/**
	 * 查询交强险某个损失类型的限额
	 * @param damageType 损失类型 CodeConstrants.FeeTypeCode
	 * @param ciIndemDuty 是否有责
	 * @param registNo 报案号
	 * @return
	 */
	public double calBzAmount(String damageType, Boolean ciIndemDuty, String registNo);

	/**
	 * 获取交强责任限额Map
	 * @param registNo
	 * @return
	 */
	public Map<String,BigDecimal> getCIDutyAmt(String registNo);

	/**
	 * 根据车辆种类和险种获取折旧率信息
	 * @param carKindCode
	 * @param riskCode
	 * @return
	 */
	public abstract PrpDDeprecateRateVo findDeprecateRate(String carKindCode,String riskCode,String clauseType);

	/**
	 * 获取不同险别的事故免赔率和绝对免赔率
	 * @param carKindCode
	 * @param riskCode
	 * @return
	 */
	public abstract PrpDAccidentDeductVo findAccidentDeduct(String riskCode,String kindCode,String clauseType,String indemnityDuty);

}
