/******************************************************************************
* CREATETIME : 2016年4月7日 下午4:38:10
******************************************************************************/
package ins.sino.claimcar.pay.service;

import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;

/** 垫付 dubbo 服务接口
 * @author ★Luwei
 */
public interface PadPayPubService {

	/** 查询 垫付 主表
	 * @param registNo
	 */
	public PrpLPadPayMainVo queryPadPay(String registNo,String compeNo);
	
	/**	查询垫付信息
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆Luwei(2016年4月22日 上午10:49:59): <br>
	 */
	public PrpLPadPayMainVo findPadPay(String registNo);
	
	/**	更新垫付-回写
	 * @param padPayVo
	 * @modified:
	 * ☆Luwei(2016年4月22日 上午10:51:05): <br>
	 */
	public void updatePadPay(PrpLPadPayMainVo padPayVo);
	
	/**
	 * 报案号下的垫付任务是否都审核通过
	 * @param registNo
	 * @return
	 */
	public boolean isPadPayAllPassed(String registNo);
	
	/**
	 * 更新垫付子表的结算单号
	 * @param settleNo
	 * @param accountNo
	 * @param operateType
	 * @param compensateNo
	 * @param serialNo
	 */
	public int saveOrUpdateSettleNo(String settleNo,String accountNo,String operateType,
			String compensateNo,String serialNo);
	/**
	 * 根据compeNo查询 垫付 主表
	 * @param registNo
	 * @param compeNo
	 * @return
	 */
	public PrpLPadPayMainVo queryPadPayByCompeNo(String registNo,String compeNo);

	public PrpLPadPayMainVo queryByPolicyNo(String registNo,String policyNo);
	
	/**
	 * 更新垫付子表-回写
	 * <pre></pre>
	 * @param padPayPersonVo
	 * @modified:
	 * ☆zhujunde(2019年3月20日 下午12:55:57): <br>
	 */
	public void updatePadPay(PrpLPadPayPersonVo padPayPersonVo);
}
