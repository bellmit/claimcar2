/******************************************************************************
 * CREATETIME : 2016年9月24日 下午6:32:25
 ******************************************************************************/
package ins.sino.claimcar.other.service;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.other.vo.PrpDAccRollBackAccountVo;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;

import java.text.ParseException;
import java.util.List;

/**
 * 账号信息修改任务查询service
 * @author 吴儒华
 */
public interface AccountQueryService {

	/**
	 * 账号信息修改(未处理查询)
	 * @param prplWfTaskQueryVo
	 * @param start
	 * @param length
	 * @return
	 * @throws ParseException
	 * @modified:
	 */
	public abstract ResultPage<PrpLPayBankVo> search(PrpLWfTaskQueryVo prplWfTaskQueryVo,int start,int length)  throws Exception;
	/**
	 * 自动理算退票信息修改(未处理查询)
	 * @param prplWfTaskQueryVo
	 * @param start
	 * @param length
	 * @return
	 * @throws ParseException
	 */
	public abstract ResultPage<PrpLPayBankVo> returnTticketInfoSearch(PrpLWfTaskQueryVo prplWfTaskQueryVo,int start,int length) throws ParseException;

	public abstract List<PrpLPayBankVo> searchByhandle(PrpLWfTaskQueryVo prplWfTaskQueryVo,int start,int length);

	/**
	 * 账号修改查询（账号修改的已处理和审核的未处理、已处理查询）
	 * @param prplWfTaskQueryVo
	 * @param start
	 * @param length
	 * @return
	 * @modified:
	 */
	public abstract ResultPage<PrpLPayBankVo> searchByHandle(PrpLWfTaskQueryVo prplWfTaskQueryVo,int start,int length);
	/**
	 * 自动理算退票信息修改（账号修改的已处理和审核的未处理、已处理查询）
	 * @param prplWfTaskQueryVo
	 * @param start
	 * @param length
	 * @return
	 */
	public abstract ResultPage<PrpLPayBankVo> returnTticketInfoSearchByHandle(PrpLWfTaskQueryVo prplWfTaskQueryVo,int start, int length);
	
	public abstract PrpDAccRollBackAccountVo findRollBackAccountById(Long id);
	/**
	 * 通过业务号和业务类型以及accountId查PrpDAccRollBackAccount表
	 * @param certiNo
	 * @param payType
	 * @param accountId
	 * @return
	 */
	public List<PrpDAccRollBackAccountVo> findPrpDAccRollBackAccountVosByCertiNoAndPayTypeAndAccountId(String certiNo,String payType,String accountId);

}
