package ins.sino.claimcar.endcase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.claim.po.PrpLClaim;
import ins.sino.claimcar.endcase.po.PrpLEndCase;
import ins.sino.claimcar.endcase.po.PrpLReCase;
import ins.sino.claimcar.endcase.service.ReOpenQueryService;
import ins.sino.claimcar.endcase.service.ReOpenService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.flow.po.PrpLWfTaskQuery;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;



@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path("reOpenCaseService")
public class ReOpenQueryServiceImpl implements ReOpenQueryService {
	
	@Autowired
	ReOpenService reOpenService;
	
	@Autowired
	DatabaseDao databaseDao;
	
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.endcase.ReOpenCaseService#findResultVo(ins.sino.claimcar.endcase.vo.PrpLEndCaseVo, ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo, int, int)
	 */
	@Override
	public ResultPage<WfTaskQueryResultVo> findResultVo(PrpLEndCaseVo endCaseVo,PrpLWfTaskQueryVo wftaskqueryvo,
			int start, int length){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLEndCase endCase,PrpLWfTaskQuery query where 1=1");
		sqlUtil.append(" and query.registNo=endCase.registNo ");
		if(StringUtils.isNotBlank(endCaseVo.getRegistNo())){
			sqlUtil.append(" and endCase.registNo like ?");
			sqlUtil.addParamValue("%"+endCaseVo.getRegistNo()  );
		}
		if(StringUtils.isNotBlank(endCaseVo.getPolicyNo())){
			sqlUtil.append(" and endCase.policyNo like ?");
			sqlUtil.addParamValue("%"+endCaseVo.getPolicyNo()  );
		}
		if(StringUtils.isNotBlank(endCaseVo.getClaimNo())){
			sqlUtil.append(" and endCase.claimNo like ?");
			sqlUtil.addParamValue("%"+endCaseVo.getClaimNo()  );
		}
		if(StringUtils.isNotBlank(wftaskqueryvo.getInsuredName())){
			sqlUtil.append(" and query.insuredName like ?");
			sqlUtil.addParamValue("%"+wftaskqueryvo.getInsuredName()+"%");
		}
		if(StringUtils.isNotBlank(sdf.format(wftaskqueryvo.getTaskInTimeStart()))&&
				StringUtils.isNotBlank(sdf.format(wftaskqueryvo.getTaskInTimeEnd()))){
			sqlUtil.append(" and endCase.endCaseDate >= ? and endCase.endCaseDate <= ?");
			sqlUtil.addParamValue(wftaskqueryvo.getTaskInTimeStart());
			sqlUtil.addParamValue(DateUtils.toDateEnd(wftaskqueryvo.getTaskInTimeEnd()));
		}
		if(StringUtils.isNotBlank(wftaskqueryvo.getMercyFlag())){
			sqlUtil.append(" and query.mercyFlag = ?");
			sqlUtil.addParamValue(wftaskqueryvo.getMercyFlag());
		}
		//排序
		sqlUtil.append(" Order By endCase.endCaseDate desc");
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		
		//对象转换
		List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
		int j = 0;
		for(int i=0;i<page.getResult().size();i++){
			WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
			Object[] obj = page.getResult().get(i);
			PrpLEndCase prpLEndCase=(PrpLEndCase)obj[0];
			if(notExistsForReOpen(prpLEndCase.getClaimNo())){
				PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[1];
				Beans.copy().from(wfTaskQuery).to(resultVo);
				Beans.copy().from(prpLEndCase).excludeNull().to(resultVo);
				
				resultVo.setComCode(resultVo.getComCodePly());
				resultVo.setClaimNo(prpLEndCase.getClaimNo());
				resultVo.setEndCaseTime(prpLEndCase.getEndCaseDate());
				resultVo.setSerialNo(countReCaseByClaimNo(prpLEndCase.getClaimNo()));//设置重开赔案次数
				resultVoList.add(resultVo);
			}else{
				j=j+1;
			}
		}
		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo>(start, length, page.getTotalCount(), resultVoList);
		return resultPage;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.endcase.ReOpenCaseService#search(ins.sino.claimcar.endcase.vo.PrpLEndCaseVo, ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo, int, int)
	 */
	@Override
	public ResultPage<WfTaskQueryResultVo> search(PrpLEndCaseVo endCaseVo,PrpLWfTaskQueryVo wftaskqueryvo,
			int start, int length){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLClaim claim,PrpLWfTaskQuery query where 1=1");
		sqlUtil.append(" and query.registNo=claim.registNo ");
		if(StringUtils.isNotBlank(endCaseVo.getRegistNo())){
			sqlUtil.append(" and claim.registNo like ?");
			sqlUtil.addParamValue("%"+endCaseVo.getRegistNo()  );
		}
		if(StringUtils.isNotBlank(endCaseVo.getPolicyNo())){
			sqlUtil.append(" and claim.policyNo like ?");
			sqlUtil.addParamValue("%"+endCaseVo.getPolicyNo()  );
		}
		if(StringUtils.isNotBlank(endCaseVo.getClaimNo())){
			sqlUtil.append(" and claim.claimNo like ?");
			sqlUtil.addParamValue("%"+endCaseVo.getClaimNo()  );
		}
		if(StringUtils.isNotBlank(wftaskqueryvo.getInsuredName())){
			sqlUtil.append(" and query.insuredName like ?");
			sqlUtil.addParamValue("%"+wftaskqueryvo.getInsuredName()+"%");
		}
		sqlUtil.append(" and claim.endCaserCode <> null ");
		if(wftaskqueryvo.getTaskInTimeStart() != null&&
				wftaskqueryvo.getTaskInTimeEnd() != null){
			sqlUtil.append(" and claim.endCaseTime >= ? and claim.endCaseTime <= ?");
			sqlUtil.addParamValue(wftaskqueryvo.getTaskInTimeStart());
			sqlUtil.addParamValue(DateUtils.toDateEnd(wftaskqueryvo.getTaskInTimeEnd()));
		}
		if(StringUtils.isNotBlank(wftaskqueryvo.getMercyFlag())){
			sqlUtil.append(" and query.mercyFlag = ?");
			sqlUtil.addParamValue(wftaskqueryvo.getMercyFlag());
		}
		//排序
		sqlUtil.append(" Order By claim.endCaseTime desc");
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		
		//对象转换
		List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
		for(int i=0;i<page.getResult().size();i++){
			WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
			Object[] obj = page.getResult().get(i);
			PrpLClaim prpLClaim=(PrpLClaim)obj[0];
//			if(notExistsForReOpen(prpLEndCase.getClaimNo())){
				PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[1];
				Beans.copy().from(wfTaskQuery).to(resultVo);
				Beans.copy().from(prpLClaim).excludeNull().to(resultVo);
				
				resultVo.setComCode(resultVo.getComCodePly());
				resultVo.setClaimNo(prpLClaim.getClaimNo());
				resultVo.setEndCaseTime(prpLClaim.getEndCaseTime());
				resultVo.setSerialNo(countReCaseByClaimNo(prpLClaim.getClaimNo()));//设置重开赔案次数
				resultVoList.add(resultVo);
//			}
		}
		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo>(start, length, page.getTotalCount(), resultVoList);
		return resultPage;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.endcase.ReOpenCaseService#searchProcessed(ins.sino.claimcar.endcase.vo.PrpLEndCaseVo, ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo, java.lang.String, int, int)
	 */
	@Override
	public ResultPage<PrpLReCaseVo> searchProcessed(PrpLEndCaseVo endCaseVo,PrpLWfTaskQueryVo wftaskqueryvo,
			String handleStatus,int start, int length){
		return reOpenService.searchProcessed(endCaseVo, wftaskqueryvo, handleStatus, start, length);
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.endcase.ReOpenCaseService#findProcessed(ins.sino.claimcar.endcase.vo.PrpLEndCaseVo, ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo, java.lang.String, int, int)
	 */
	@Override
	public List<PrpLReCaseVo> findProcessed(PrpLEndCaseVo endCaseVo,PrpLWfTaskQueryVo wftaskqueryvo,
			String handleStatus,int start, int length){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addLikeIfExist("registNo", endCaseVo.getRegistNo());
		queryRule.addLikeIfExist("claimNo",endCaseVo.getClaimNo());
		queryRule.addEqualIfExist("insuredName", wftaskqueryvo.getInsuredName()+"%");
		queryRule.addEqualIfExist("mercyFlag", wftaskqueryvo.getMercyFlag());
		//queryRule.addBetweenIfExist("openCaseDate", wftaskqueryvo.getTaskInTimeStart(),wftaskqueryvo.getTaskInTimeEnd());
		queryRule.addGreaterEqualIfExist("openCaseDate", wftaskqueryvo.getTaskInTimeStart());
		
		if(handleStatus.equals("3")){
			queryRule.addInIfExist("checkStatus", "4","5","6","7","9");
		}else if(handleStatus.equals("6")){
			queryRule.addEqualIfExist("checkStatus", "8");
		}
		List<PrpLReCase> reCaseList=databaseDao.findAll(PrpLReCase.class, queryRule);//(PrpLReCase.class, queryRule, start, length);
		List<PrpLReCaseVo> reCase=Beans.copyDepth().from(reCaseList).toList(PrpLReCaseVo.class);
		return reCase;
	}
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.endcase.ReOpenCaseService#notExistsForReOpen(java.lang.String)
	 */
	@Override
	public boolean notExistsForReOpen(String claimNo){
		boolean bool=true;
		List<PrpLEndCase> endCase=findEndCaseByClaimNo(claimNo);
		for(int i=0;i<endCase.size();i++){
			List<PrpLReCase> reCase=findReCaseByEndCaseNo(endCase.get(i).getEndCaseNo());
			if(reCase.size()>0){
				for(int n=0;i<reCase.size();i++){
					if(!(reCase.get(n).getCheckStatus().equals("7")||reCase.get(n).getCheckStatus().equals("8"))){
						bool=false;break;
					}
				}
			}
		}
		return bool;
	}
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.endcase.ReOpenCaseService#findReCaseByEndCaseNo(java.lang.String)
	 */
	private List<PrpLReCase> findReCaseByEndCaseNo(String endCaseNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("endCaseNo", endCaseNo);
		List<PrpLReCase> reCase=databaseDao.findAll(PrpLReCase.class, queryRule);
		return reCase;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.endcase.ReOpenCaseService#findEndCaseByClaimNo(java.lang.String)
	 */
	private List<PrpLEndCase> findEndCaseByClaimNo(String claimNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo",claimNo);
		List<PrpLEndCase> endCase=databaseDao.findAll(PrpLEndCase.class, queryRule);
		return endCase;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.endcase.ReOpenCaseService#countReCaseByClaimNo(java.lang.String)
	 */
	@Override
	public String countReCaseByClaimNo(String claimNo){
		QueryRule queryRule = QueryRule.getInstance();
		List<PrpLReCaseVo> reCaseVoList=new ArrayList<PrpLReCaseVo>();
		List<PrpLReCase> reCaseList=new ArrayList<PrpLReCase>();
		queryRule.addEqual("claimNo", claimNo);
		reCaseList=databaseDao.findAll(PrpLReCase.class, queryRule);
		reCaseVoList=Beans.copyDepth().from(reCaseList).toList(PrpLReCaseVo.class);
		return String.valueOf(reCaseVoList.size());
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.endcase.ReOpenCaseService#findReCaseByClaimNo(java.lang.String)
	 */
	@Override
	public List<PrpLReCaseVo> findReCaseByClaimNo(String claimNo){
		List<PrpLReCaseVo> reCaseVoList=new ArrayList<PrpLReCaseVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNo", claimNo);
		queryRule.addAscOrder("seriesNo");
		List<PrpLReCase> reCaseList=databaseDao.findAll(PrpLReCase.class, queryRule);
		if(reCaseList!=null&&reCaseList.size()>0){
			reCaseVoList=Beans.copyDepth().from(reCaseList).toList(PrpLReCaseVo.class);
		}
		return reCaseVoList;
	}
	
	@Override
	public ResultPage<WfTaskQueryResultVo> searchOldClaim(PrpLEndCaseVo endCaseVo,PrpLWfTaskQueryVo wftaskqueryvo,
			int start, int length){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLClaim claim,PrpLRegist regist where 1=1");
		sqlUtil.append(" and regist.registNo=claim.registNo ");
		if(StringUtils.isNotBlank(endCaseVo.getRegistNo())){
			sqlUtil.append(" and claim.registNo like ?");
			sqlUtil.addParamValue("%"+endCaseVo.getRegistNo()  );
		}
		sqlUtil.append(" and regist.flag = ?");
		sqlUtil.addParamValue("oldClaim");
		if(StringUtils.isNotBlank(endCaseVo.getPolicyNo())){
			sqlUtil.append(" and claim.policyNo like ?");
			sqlUtil.addParamValue("%"+endCaseVo.getPolicyNo()  );
		}
		if(StringUtils.isNotBlank(endCaseVo.getClaimNo())){
			sqlUtil.append(" and claim.claimNo like ?");
			sqlUtil.addParamValue("%"+endCaseVo.getClaimNo()  );
		}
		/*if(StringUtils.isNotBlank(wftaskqueryvo.getInsuredName())){
			sqlUtil.append(" and query.insuredName like ?");
			sqlUtil.addParamValue("%"+wftaskqueryvo.getInsuredName()+"%");
		}*/
		sqlUtil.append(" and claim.endCaserCode <> null ");
		if(wftaskqueryvo.getTaskInTimeStart() != null&&
				wftaskqueryvo.getTaskInTimeEnd() != null){
			sqlUtil.append(" and claim.endCaseTime >= ? and claim.endCaseTime <= ?");
			sqlUtil.addParamValue(wftaskqueryvo.getTaskInTimeStart());
			sqlUtil.addParamValue(DateUtils.toDateEnd(wftaskqueryvo.getTaskInTimeEnd()));
		}
		/*if(StringUtils.isNotBlank(wftaskqueryvo.getMercyFlag())){
			sqlUtil.append(" and query.mercyFlag = ?");
			sqlUtil.addParamValue(wftaskqueryvo.getMercyFlag());
		}*/
		//排序
		sqlUtil.append(" Order By claim.endCaseTime desc");
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		
		//对象转换
		List<WfTaskQueryResultVo> resultVoList=new ArrayList<WfTaskQueryResultVo>();
		for(int i=0;i<page.getResult().size();i++){
			WfTaskQueryResultVo resultVo=new WfTaskQueryResultVo();
			Object[] obj = page.getResult().get(i);
			PrpLClaim prpLClaim=(PrpLClaim)obj[0];
//			if(notExistsForReOpen(prpLEndCase.getClaimNo())){
				/*PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[1];
				Beans.copy().from(wfTaskQuery).to(resultVo);*/
				Beans.copy().from(prpLClaim).excludeNull().to(resultVo);
				
				resultVo.setComCode(resultVo.getComCodePly());
				resultVo.setClaimNo(prpLClaim.getClaimNo());
				resultVo.setEndCaseTime(prpLClaim.getEndCaseTime());
				resultVo.setSerialNo(countReCaseByClaimNo(prpLClaim.getClaimNo()));//设置重开赔案次数
				resultVoList.add(resultVo);
//			}
		}
		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo>(start, length, page.getTotalCount(), resultVoList);
		return resultPage;
	}

}
