/******************************************************************************
* CREATETIME : 2016年4月5日 下午3:57:05
******************************************************************************/
package ins.sino.claimcar.endcase.service;

import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;

import java.util.Date;
import java.util.List;
import java.util.Map;


/** 结案、重开赔案-->API服务
 * @author ★Luwei
 */
public interface EndCasePubService {
	
	/** 核赔-->结案提交（1-自动结案auto--EndCase，2-手动..）
	 * @param registNo、compensatenNo、claimNo、endType(结案类型)
	 * @modified: 1
	 * ☆Luwei(2016年4月5日 下午4:06:34): <br>
	 */
	public PrpLEndCaseVo autoEndCase(PrpLWfTaskVo wfTaskVo,PrpLCompensateVo compeVo);

	/**
	 * 【【平安联盟对接使用】】
	 * 自动结案
	 * @param wfTaskVo
	 * @param compeVo
	 * @param endCaseDate
	 * @return
	 */
	public PrpLEndCaseVo autoEndCaseForPingAn(PrpLWfTaskVo wfTaskVo, PrpLCompensateVo compeVo, Date endCaseDate);
	
	/**	根据报案号查询所有的结案信息
	 * @param registNo
	 * @modified:
	 * ☆Luwei(2016年4月5日 下午3:59:24): <br>
	 */
	public List<PrpLEndCaseVo> queryAllByRegistNo(String registNo);
	
	/**
	 * <pre></pre>
	 * @param registNo
	 * @param endCaseType
	 * @modified:
	 * ☆Luwei(2016年11月4日 下午5:01:52): <br>
	 */
	public PrpLEndCaseVo findEndCaseByType(String registNo,String endCaseType);
	
	/** 根据id 或者 endCaseNo查询唯一的结案
	 * @param id、endCaseNo
	 * @modified:
	 * ☆Luwei(2016年4月5日 下午4:02:17): <br>
	 */
	public PrpLEndCaseVo findEndCaseByPK(Long id,String endCaseNo);
	
	/**
	 * 取最新的一条结案数据
	 * @param claimNo
	 * @return
	 */
	public PrpLEndCaseVo findEndCaseByClaimNo(String claimNo);
	
	/**
	 * 根据报案号查找是否存在重开赔案信息   queryMap中的key必须和PrpLReCase表中字段保持一致
	 * @param queryMap 
	 * @return
	 */
	public List<String> findPrpLReCaseVoList(Map<String,String> queryMap);
	
	
	public List<PrpLReCaseVo> findReCaseListByClaimNo(String claimNo);
	
	//
	public List<PrpLReCaseVo> findReCaseVoListByqueryMap(Map<String,String> queryMap);
}
