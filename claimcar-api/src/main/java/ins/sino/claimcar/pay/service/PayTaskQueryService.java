/******************************************************************************
 * CREATETIME : 2016年9月24日 下午6:30:13
 ******************************************************************************/
package ins.sino.claimcar.pay.service;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;

import java.lang.reflect.InvocationTargetException;

/**
 * <pre></pre>
 * @author ★Luwei
 */
public interface PayTaskQueryService {

	public abstract ResultPage<WfTaskQueryResultVo> find(PrpLClaimVo prpLClaimVo,
			PrpLWfTaskQueryVo prplwftaskqueryvo,int start,int length) 
					throws IllegalAccessException,InvocationTargetException,NoSuchMethodException,Exception;

	public abstract ResultPage<WfTaskQueryResultVo> findByPre(PrpLClaimVo prpLClaimVo,
			PrpLWfTaskQueryVo prplwftaskqueryvo,int start,int length) 
					throws IllegalAccessException,InvocationTargetException,NoSuchMethodException,Exception;

	public abstract ResultPage<WfTaskQueryResultVo> findByPreWriteOff(PrpLCompensateVo prpLCompensateVo,
			PrpLWfTaskQueryVo prplwftaskqueryvo,int start,int length) 
					throws IllegalAccessException,InvocationTargetException,NoSuchMethodException;

}
