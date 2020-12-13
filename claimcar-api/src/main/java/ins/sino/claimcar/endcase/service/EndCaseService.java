/******************************************************************************
 * CREATETIME : 2016年9月24日 下午4:25:24
 ******************************************************************************/
package ins.sino.claimcar.endcase.service;

import java.util.Date;
import java.util.List;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.other.vo.RecPayLaunchResultVo;
import ins.sino.claimcar.other.vo.RecPayLaunchVo;

/**
 * <pre></pre>
 * @author ★Luwei
 */
public interface EndCaseService {

	/**
	 * 获取 结案信息
	 * @param claimNo
	 * @modified: ☆Luwei(2016年4月5日 下午5:42:06): <br>
	 */
	public abstract PrpLEndCaseVo queryEndCaseVo(String registNo,String claimNo);
	
	public List<PrpLEndCaseVo> searchEndCaseVo(String registNo,String claimNo);
	
	public List<PrpLEndCaseVo> findEndCaseVo(String registNo);

	//
	public abstract int getEndCaseNum(String registNo,String claimNo,String compeNo);

	/**
	 * @param registNo ☆Luwei(2016年4月6日 下午3:31:23): <br>
	 */
	public abstract PrpLDlossCarMainVo getDLossCarInfo(String registNo);

	// 获取查勘标的车号牌底色
	public abstract String getCarLinceColor(String registNo);

	/**
	 * 创建结案表 flag : 垫付核赔提交，交强、商业理算计算书核赔通过提交
	 * @modified:☆Luwei(2016年4月5日 上午10:56:29): <br>
	 */
	public abstract Long saveEndCase(PrpLCompensateVo compeVo,String userCode);

	/**
	 * 【【平安联盟对接使用】】
	 * 创建结案表 flag : 垫付核赔提交，交强、商业理算计算书核赔通过提交
	 * @modified:☆Luwei(2016年4月5日 上午10:56:29): <br>
	 */
	public abstract Long saveEndCaseForPingAn(PrpLCompensateVo compeVo,String userCode, Date endCaseDate);
	
	public List<PrpLEndCaseVo>  findEndCaseByEndCaseDate(Date beginDate,Date endDate) ;
	
	/**
	 * zhubin
	 * 查出所有已经结案的数据
	 * 有重开结案的案子且未结案则不查出此数据
	 * 有重开结案的案子且已结案查出最后一次结案工作流
	 * @param recPayLaunchVo
	 * @param start
	 * @param length
	 * @return
	 * @throws Exception
	 */
	public ResultPage<RecPayLaunchResultVo> find(RecPayLaunchVo recPayLaunchVo, int start, int length,String comCode) throws Exception;

	/**
	 * 保存结案校验码到结案表
	 * @param id
	 * @param confirmCode
	 */
	public void saveConfirmCode(Long id,String confirmCode);

	public List<PrpLEndCaseVo> findEndCaseVoByRegistNoAndCompeNo(String registNo,String compensateNo);

	public void saveOrUpdateEndCase(PrpLEndCaseVo prpLEndCaseVo);
}
