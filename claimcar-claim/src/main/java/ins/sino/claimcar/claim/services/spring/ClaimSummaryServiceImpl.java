/******************************************************************************
* CREATETIME : 2016年7月28日 下午1:00:43
******************************************************************************/
package ins.sino.claimcar.claim.services.spring;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.utils.DataUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.po.PrpLClaimSummary;
import ins.sino.claimcar.claim.service.ClaimSummaryService;
import ins.sino.claimcar.claim.vo.PrpLClaimSummaryVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年7月28日
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("claimSummaryService")
public class ClaimSummaryServiceImpl implements ClaimSummaryService {

	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private RegistQueryService registQueryService;
	
	private static Logger logger = LoggerFactory.getLogger(ClaimSummaryServiceImpl.class);
		/* 
	 * @see ins.sino.claimcar.claim.service.ClaimSummaryService#updateByClaim(ins.sino.claimcar.claim.vo.PrpLClaimVo)
	 * @param claimVo
	 * @return
	 */
	@Override
	public PrpLClaimSummaryVo updateByClaim(PrpLClaimVo claimVo) {
		String claimNo = claimVo.getClaimNo();
		// TODO 检查PrpLClaimSummary表有没有此立案号的数据，如果有是更新操作，没有是新增操作
		PrpLClaimSummary summaryPo = databaseDao.findByPK(PrpLClaimSummary.class,claimNo);
		if(summaryPo==null){
			summaryPo = saveClaimSummaryFirst(claimVo);
		}
		// 根据立案信息set对象
		summaryPo.setWillPay(claimVo.getSumClaim());

		PrpLClaimSummaryVo returnVo = new PrpLClaimSummaryVo();
		Beans.copy().from(summaryPo).to(returnVo);
		return returnVo;
	}



	/**
	 * 某个立案第一次保存 PrpLClaimSummary
	 * @param registVo
	 * @param claimVo
	 * @return
	 * @modified: ☆LiuPing(2016年7月28日 ): <br>
	 */
	private PrpLClaimSummary saveClaimSummaryFirst(PrpLClaimVo claimVo) {
		logger.debug("立案案件信息总览表第一次保存"+claimVo.getClaimNo());
		String policyNo = claimVo.getPolicyNo();
		PrpLClaimSummary newSummaryPo = new PrpLClaimSummary();
		Beans.copy().from(claimVo).to(newSummaryPo);
		// 车牌号码，出险地点，结案时间，已决金额，赔案状态，这些需要录入
		PrpLRegistVo registVo = registQueryService.findByRegistNo(claimVo.getRegistNo());
		newSummaryPo.setLicenseNo(registVo.getPrpLRegistExt().getLicenseNo());
		newSummaryPo.setDamageAddress(registVo.getDamageAddress());
		newSummaryPo.setCaseStatus(CodeConstants.FlowStatus.NORMAL);
		// 根据保单号找到 PrpLClaimSummary表最新的一条记录，需要用里面的部分字段
		PrpLClaimSummary oldSummaryVo = findLastClaimByPolicy(policyNo);

		if(oldSummaryVo!=null){
			newSummaryPo.setLastEndCaseDate(oldSummaryVo.getEndCaseTime());
			newSummaryPo.setLastRegistNo(oldSummaryVo.getRegistNo());
			newSummaryPo.setKindLBalance(oldSummaryVo.getKindLBalance());// 把上次车身划痕险剩余金额带进来，
			newSummaryPo.setKindw1Balance(oldSummaryVo.getKindw1Balance());
		}else{//如果是第一次，就把保额赋予剩余金额
			List<PrpLCItemKindVo> prpLCItemKindVos= registQueryService.findItemKindVo(claimVo.getRegistNo(), CodeConstants.KINDCODE.KINDCODE_W1);
			if(prpLCItemKindVos!=null && prpLCItemKindVos.size()>0){
				newSummaryPo.setKindw1Balance(prpLCItemKindVos.get(0).getAmount());
			}

		}
		newSummaryPo.setWillPay(claimVo.getSumClaim());
		newSummaryPo.setCompensateTimes(0);
		databaseDao.save(PrpLClaimSummary.class,newSummaryPo);
		return newSummaryPo;
	}


	/* 
	 * @see ins.sino.claimcar.claim.service.ClaimSummaryService#updateByEndCase(ins.sino.claimcar.claim.vo.PrpLCompensateVo)
	 * @param compensateVo
	 * @return
	 */
	@Override
	public PrpLClaimSummaryVo updateByEndCase(PrpLCompensateVo compensateVo) {
		logger.debug("立案案件信息总览表结案更新"+compensateVo.getClaimNo());
		String claimNo = compensateVo.getClaimNo();
		PrpLClaimSummary summaryPo = databaseDao.findByPK(PrpLClaimSummary.class,claimNo);
		if(summaryPo==null){
			return null;
		}
		// TODO WLL 更新 案件赔付标识 ，CompensateTimes,已决金额,未决金额
		summaryPo.setCompensateTimes(1);//更新案件赔付标识
		summaryPo.setCaseStatus(CodeConstants.FlowStatus.END);
		summaryPo.setEndCaseTime(new Date());
		// 更新已决未决
		summaryPo.setRealPay(compensateVo.getSumPaidAmt());//取理算最后核赔金额
		summaryPo.setWillPay(BigDecimal.ZERO);

		// 计算车身划痕险
		BigDecimal kindLRest = BigDecimal.ZERO;
		BigDecimal kindW1Rest = BigDecimal.ZERO;
		if(summaryPo.getKindLBalance()!=null){
			kindLRest = summaryPo.getKindLBalance();// 车身划痕剩余赔偿金额
			kindW1Rest = summaryPo.getKindw1Balance();//附加车轮单独损失险剩余赔偿金额
			if(kindW1Rest==null) {
				kindW1Rest = BigDecimal.ZERO;
			}
		}
		
		BigDecimal kindLPay = BigDecimal.ZERO;// 车身划痕本次赔偿金额
		
		if(compensateVo.getPrpLLossItems()!=null&&compensateVo.getPrpLLossItems().size()>0){
			for(PrpLLossItemVo lossItemVo:compensateVo.getPrpLLossItems()){
				if(CodeConstants.KINDCODE.KINDCODE_L.equals(lossItemVo.getKindCode())){
					kindLPay = kindLPay.add(DataUtils.NullToZero(lossItemVo.getSumRealPay()));
				}
			}
		}
		BigDecimal kindW1Pay = BigDecimal.ZERO;// 附加车轮单独损失险本次赔偿金额
		if(compensateVo.getPrpLLossProps()!=null && compensateVo.getPrpLLossItems().size()>0){
			for(PrpLLossPropVo lossPropVo:compensateVo.getPrpLLossProps()){
				kindW1Pay=kindW1Pay.add(DataUtils.NullToZero(lossPropVo.getSumRealPay()));
			}
		}
		
		kindLRest = kindLRest.subtract(kindLPay);
		summaryPo.setKindLBalance(kindLRest);
		summaryPo.setKindLPayment(kindLPay);
		kindW1Rest=kindW1Rest.subtract(kindW1Pay);
		//当剩余保额小于或等于本次赔付的金额，则剩余保额直接默认值为0
		if(kindW1Rest.compareTo(new BigDecimal(0))<=0){
			summaryPo.setKindw1Balance(new BigDecimal(0));
		}else{
			summaryPo.setKindw1Balance(kindW1Rest);
		}
		
		
		databaseDao.update(PrpLClaimSummary.class,summaryPo);
		PrpLClaimSummaryVo returnVo = new PrpLClaimSummaryVo();
		Beans.copy().from(summaryPo).to(returnVo);
		return returnVo;
	}

	private PrpLClaimSummary findLastClaimByPolicy(String policyNo) {
		String hql = "From PrpLClaimSummary Where policyNo=? order by createTime Desc ";
		List<PrpLClaimSummary> lastSummaryList = databaseDao.findAllByHql(PrpLClaimSummary.class,hql,policyNo);
		PrpLClaimSummary lastSummary = null;
		if(lastSummaryList!=null&&lastSummaryList.size()>0){
			lastSummary = lastSummaryList.get(0);
		}
		return lastSummary;
	}


	/* 
	 * @see ins.sino.claimcar.claim.service
	 * .ClaimSummaryService#claimNumber(java.lang.String)
	 * @param policyNo
	 * @return
	 */
	@Override
	public int claimNumber(String policyNo) {
		// TODO Auto-generated method stub
		int returnVal = 0;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("policyNo",policyNo);
		List<PrpLClaimSummary> poList = 
				databaseDao.findAll(PrpLClaimSummary.class,queryRule);
		if(poList!=null){
			returnVal = poList.size();
		}
		return returnVal;
	}


	@Override
	public ResultPage<PrpLClaimSummaryVo> findPageForHistory(String policyNo,int start,int length) {
		List<PrpLClaimSummaryVo> summaryVoList = new ArrayList<PrpLClaimSummaryVo>();
		// 定义参数list，ps：执行查询时需要转换成object数组
		List<Object> paramValues = new ArrayList<Object>();
		// hql查询语句
		StringBuffer queryString = new StringBuffer("from PrpLClaimSummary cs where 1=1 ");
		// 代码
		if(StringUtils.isNotBlank(policyNo)){
			queryString.append("AND cs.policyNo = ? ");
			paramValues.add(policyNo);
		}
		queryString.append(" Order By cs.createTime DESC");

		logger.debug("sql="+queryString);
		logger.debug("ParamValues="+ArrayUtils.toString(paramValues));
		// 执行查询
		Page<PrpLClaimSummary> page = databaseDao.findPageByHql
				(PrpLClaimSummary.class,queryString.toString(),(start/length + 1),length,paramValues.toArray());

		// 对象转换
		summaryVoList = Beans.copyDepth().from(page.getResult()).toList(PrpLClaimSummaryVo.class);
		
		ResultPage<PrpLClaimSummaryVo> resultPage = new ResultPage<PrpLClaimSummaryVo>
		(start, length, page.getTotalCount(), summaryVoList);
		return resultPage;
	}




	@Override
	public List<PrpLClaimSummaryVo> findPrpLClaimSummaryVoList(String policyNo,String excludeRegistNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("policyNo",policyNo);
		queryRule.addNotEqual("registNo", excludeRegistNo);
		List<PrpLClaimSummary> PrpLClaimSummaryList = databaseDao.findAll(PrpLClaimSummary.class,queryRule);
		List<PrpLClaimSummaryVo> PrpLClaimSummaryVoList = new ArrayList<PrpLClaimSummaryVo>();
		PrpLClaimSummaryVoList = Beans.copyDepth().from(PrpLClaimSummaryList).toList(PrpLClaimSummaryVo.class);
		return PrpLClaimSummaryVoList;
	}



	@Override
	public List<PrpLClaimSummaryVo> findPrpLClaimSummaryVoList(String policyNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("policyNo",policyNo);
		List<PrpLClaimSummary> PrpLClaimSummaryList = databaseDao.findAll(PrpLClaimSummary.class,queryRule);
		List<PrpLClaimSummaryVo> PrpLClaimSummaryVoList = new ArrayList<PrpLClaimSummaryVo>();
		PrpLClaimSummaryVoList = Beans.copyDepth().from(PrpLClaimSummaryList).toList(PrpLClaimSummaryVo.class);
		return PrpLClaimSummaryVoList;
	}



	@Override
	public PrpLClaimSummaryVo findPrpLClaimSummaryDescVo(String policyNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("policyNo",policyNo);
		queryRule.addDescOrder("createTime");
		PrpLClaimSummaryVo summaryVo=null;
		List<PrpLClaimSummary> PrpLClaimSummaryList = databaseDao.findAll(PrpLClaimSummary.class,queryRule);
		if(PrpLClaimSummaryList!=null && PrpLClaimSummaryList.size()>0){
			summaryVo=new PrpLClaimSummaryVo();
			Beans.copy().from(PrpLClaimSummaryList.get(0)).to(summaryVo);
		}
		
		return summaryVo;
	}


}
