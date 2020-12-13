package ins.sino.claimcar.pay.service;

import java.math.BigDecimal;
import java.util.List;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.vo.PrplReplevyDetailVo;
import ins.sino.claimcar.claim.vo.PrplReplevyMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;

public interface RecPayService {

	/**
	 * 保存或更新追偿处理
	 */
	public abstract PrplReplevyMainVo saveOrUpdatePrplReplevyMain(
			PrplReplevyMainVo prplReplevyMainVo, Double flowTaskId);

//	public abstract PrplReplevyDetail saveOrUpdatePrplReplevyDetail(
//			PrplReplevyDetailVo prplReplevyDetailVo);

	public abstract void commitRecPay(Double flowTaskId,
			PrplReplevyMainVo prplReplevyMainVo,SysUserVo userVo);

	public abstract PrplReplevyMainVo findPrplReplevyMainVoByPK(Long id);

	public abstract PrplReplevyMainVo findPrplReplevyMainVoByClaimNo(
			String claimNo);
	
	/**
	 * 发起追偿
	 * @param claimNo
	 * @param registNo
	 * @return
	 */
	public abstract BigDecimal startRecPay(String claimNo,String registNo);
	
	/**
	 * 根據COMPENSATENO 查
	 * @param id
	 * @return
	 */
	public abstract PrplReplevyMainVo findPrplReplevyMainVoByComPensateNo(String comPensateNo);
}