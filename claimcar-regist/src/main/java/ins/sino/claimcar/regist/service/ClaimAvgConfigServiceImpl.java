package ins.sino.claimcar.regist.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.regist.po.PrpDClaimAvg;
import ins.sino.claimcar.regist.vo.PrpDClaimAvgVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;



/**
 * <pre></pre>
 * @author ★LiuPing
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path("claimAvgConfigService")
public class ClaimAvgConfigServiceImpl implements ClaimAvgConfigService {

	private static Logger logger = LoggerFactory.getLogger(ClaimAvgConfigServiceImpl.class);

	@Autowired
	private DatabaseDao databaseDao;

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.ClaimAvgConfigService#findTaskForPage(ins.sino.claimcar.regist.vo.PrpDClaimAvgVo, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ResultPage<PrpDClaimAvgVo> findTaskForPage(PrpDClaimAvgVo prpDClaimAvgVo,Integer start,Integer length) throws Exception {

		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpDClaimAvg qry where 1=1");
		sqlUtil.append(" and avgYear = ?");
		sqlUtil.addParamValue(prpDClaimAvgVo.getAvgYear());
		//sqlUtil.addParamValue(0);
		String  table_query="qry";
		// 自动拼装等号条件
		if(prpDClaimAvgVo.getValidFlag().equals("全部")){
			if("0002".equals(prpDClaimAvgVo.getComCode().substring(0, 4))){
				sqlUtil.append(" and comCode like ?");
				sqlUtil.addParamValue(prpDClaimAvgVo.getComCode().substring(0, 4)+"%");
			}else{
				sqlUtil.append(" and comCode like ?");
				sqlUtil.addParamValue(prpDClaimAvgVo.getComCode().substring(0, 2)+"%");
			}
			
		}else{
			if("0002".equals(prpDClaimAvgVo.getComCode().substring(0, 4))){
				sqlUtil.append(" and comCode like ?");
				sqlUtil.addParamValue(prpDClaimAvgVo.getComCode().substring(0, 4)+"%");
			}else{
				sqlUtil.append(" and comCode like ?");
				sqlUtil.addParamValue(prpDClaimAvgVo.getComCode().substring(0, 2)+"%");
			}
			sqlUtil.andEquals(prpDClaimAvgVo,table_query,"validFlag");
			}
		/*
		sqlUtil.andDate(prpLRegistVo,table_query,"reportTime","damageTime");*/
		// 排序
		/*sqlUtil.append(" ORDER BY qry.reportTime DESC");*/
		String sql = sqlUtil.getSql();
		logger.debug("taskQrySql="+sql);
		logger.debug("==getAvgYear=="+prpDClaimAvgVo.getAvgYear());
		logger.debug("==getComCode=="+prpDClaimAvgVo.getComCode());
		logger.debug("==getValidFlag=="+prpDClaimAvgVo.getValidFlag());
		// 开始记录数
		start = prpDClaimAvgVo.getStart();
		// 查询记录数量
		length = prpDClaimAvgVo.getLength();
		Page<PrpDClaimAvg> page = databaseDao.findPageByHql(PrpDClaimAvg.class, sql,start/length+1,length,sqlUtil.getParamValues());
		// 对象转换
		List<PrpDClaimAvgVo> resultVoList=new ArrayList<PrpDClaimAvgVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
			PrpDClaimAvg prpDClaimAvg= page.getResult().get(i);
			PrpDClaimAvgVo resultVo = Beans.copyDepth().from(prpDClaimAvg).to(PrpDClaimAvgVo.class);
			resultVoList.add(resultVo);
		}

	
		ResultPage<PrpDClaimAvgVo> resultPage = new ResultPage<PrpDClaimAvgVo> (start, length, page.getTotalCount(), resultVoList);

		return resultPage;
		
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.ClaimAvgConfigService#updates(ins.sino.claimcar.regist.vo.PrpDClaimAvgVo)
	 */
	@Override
	public String updates(PrpDClaimAvgVo prpDClaimAvgVo){
		PrpDClaimAvg prpDClaimAvg = new PrpDClaimAvg();
		Beans.copy().from(prpDClaimAvgVo).excludeNull().to(prpDClaimAvg);
		databaseDao.save(PrpDClaimAvg.class, prpDClaimAvg);
		return "success";
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.ClaimAvgConfigService#findAvgConfig(java.math.BigDecimal, java.lang.String, java.util.List)
	 */
	@Override
	public List<PrpDClaimAvgVo> findAvgConfig(BigDecimal avgYear,String comCode, List<String> riskCodes) {
		
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("validFlag", CodeConstants.ValidFlag.VALID);
		queryRule.addEqual("avgYear", avgYear);
		queryRule.addEqual("comCode", comCode);
		if (riskCodes != null && riskCodes.size()>0) {
			queryRule.addIn("riskCode", riskCodes);
		}
		List<PrpDClaimAvg> oldTaskInPo = databaseDao.findAll(PrpDClaimAvg.class,queryRule);
		
		PrpDClaimAvgVo prpLSurveyVo = new PrpDClaimAvgVo();
		List<PrpDClaimAvgVo> prpLSurveyVoList = new ArrayList<PrpDClaimAvgVo>();
		for(int i = 0; i<oldTaskInPo.size(); i++ ){

			PrpDClaimAvg prpLSurvey = oldTaskInPo.get(i);
			// po转vo
			prpLSurveyVo = Beans.copyDepth().from(prpLSurvey).to(PrpDClaimAvgVo.class);
			prpLSurveyVoList.add(prpLSurveyVo);
		}
		return prpLSurveyVoList;
		}

// 更新
		/* (non-Javadoc)
		 * @see ins.sino.claimcar.regist.service.ClaimAvgConfigService#updateSurvey(java.util.List)
		 */
		@Override
		public void updatePrpDClaimAvg(List<PrpDClaimAvgVo> prpDClaimAvgVoList) {
				for(PrpDClaimAvgVo claimAvgVo : prpDClaimAvgVoList){
					if(claimAvgVo.getId()!=null){
						PrpDClaimAvg claimAvgPo = new PrpDClaimAvg();
						Beans.copy().from(claimAvgVo).to(claimAvgPo);
						databaseDao.update(PrpDClaimAvg.class, claimAvgPo);
					}
				}
			}
}
