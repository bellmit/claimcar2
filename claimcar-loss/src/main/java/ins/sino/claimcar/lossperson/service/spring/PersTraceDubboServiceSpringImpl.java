package ins.sino.claimcar.lossperson.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersInjured;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersTrace;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersTraceMain;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;

import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("persTraceDubboService")
public class PersTraceDubboServiceSpringImpl implements PersTraceDubboService {

	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private WfFlowQueryService wfFlowQueryService;

	@Override
	public List<PrpLDlossPersTraceMainVo> findPersTraceMainVoList(String registNo) {
		List<PrpLDlossPersTraceMain> persTraceMains = null;

		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);

		persTraceMains = databaseDao.findAll(PrpLDlossPersTraceMain.class,queryRule);
		List<PrpLDlossPersTraceMainVo> persTraceMainVos = null;

		if(persTraceMains!=null&&persTraceMains.size()>0){
			persTraceMainVos = Beans.copyDepth().from(persTraceMains).toList(PrpLDlossPersTraceMainVo.class);

			for(PrpLDlossPersTraceMainVo persTraceMain:persTraceMainVos){
				List<PrpLDlossPersTraceVo> persTraces = this.findPersTraceVo(registNo,persTraceMain.getId());
				persTraceMain.setPrpLDlossPersTraces(persTraces);
			}
		}
		return persTraceMainVos;
	}

	public List<PrpLDlossPersTraceVo> findPersTraceVo(String registNo,Long persTraceMainId) {
		// TODO Auto-generated method stub
		List<PrpLDlossPersTrace> persTraces = null;

		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("persTraceMainId",persTraceMainId);

		persTraces = databaseDao.findAll(PrpLDlossPersTrace.class,queryRule);
		List<PrpLDlossPersTraceVo> persTraceVos = null;

		if(persTraces!=null&&persTraces.size()>0){
			persTraceVos = Beans.copyDepth().from(persTraces).toList(PrpLDlossPersTraceVo.class);
		}
		return persTraceVos;
	}

	@Override
	public PrpLDlossPersInjuredVo findPersInjuredByPK(Long id) {
		PrpLDlossPersInjuredVo persInjuredVo = null;
		PrpLDlossPersInjured persInjured = databaseDao.findByPK(PrpLDlossPersInjured.class,id);
		if(persInjured != null){
			persInjuredVo = Beans.copyDepth().from(persInjured).to(PrpLDlossPersInjuredVo.class);
		}
		return persInjuredVo;
	}

	@Override
	public List<PrpLDlossPersInjuredVo> findPersInjuredByRegistNo(String registNo) {
		List<PrpLDlossPersInjuredVo> persInjuredVos = null;
		List<PrpLDlossPersInjured> persInjured = null;

		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		persInjured = databaseDao.findAll(PrpLDlossPersInjured.class,queryRule);
		
		if(persInjured != null && persInjured.size() > 0){
			persInjuredVos = Beans.copyDepth().from(persInjured).toList(PrpLDlossPersInjuredVo.class);
		}
		return persInjuredVos;
	}

	@Override
	public PrpLDlossPersTraceVo findPersTraceByPK(Long id) {
		PrpLDlossPersTraceVo persTraceVo = null;
		PrpLDlossPersTrace persTrace = databaseDao.findByPK(PrpLDlossPersTrace.class,id);
		if(persTrace != null){
			persTraceVo = Beans.copyDepth().from(persTrace).to(PrpLDlossPersTraceVo.class);
		}
		return persTraceVo;
	}

	@Override
	public List<PrpLDlossPersTraceVo> findPrpLDlossPersTraceVoListByRegistNo(String registNo) {
		List<PrpLDlossPersTraceVo> prpLDlossPersTraceVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLDlossPersTrace> prpLDlossPersTraceList = databaseDao.findAll(PrpLDlossPersTrace.class,queryRule);
		if(prpLDlossPersTraceList != null && !prpLDlossPersTraceList.isEmpty()){
			prpLDlossPersTraceVoList = Beans.copyDepth().from(prpLDlossPersTraceList).toList(PrpLDlossPersTraceVo.class);
		}
		return prpLDlossPersTraceVoList;
	}

	@Override
	public List<PrpLDlossPersTraceMainVo> findPrpLDlossPersTraceMainVoListByRegistNoDesc(String registNo) {
		List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addNotEqual("underwriteFlag","7");
		queryRule.addDescOrder("undwrtFeeEndDate");
		List<PrpLDlossPersTraceMain> prpLDlossPersTraceMainList = databaseDao.findAll(PrpLDlossPersTraceMain.class,queryRule);
		if(prpLDlossPersTraceMainList != null && !prpLDlossPersTraceMainList.isEmpty()){
			prpLDlossPersTraceMainVoList = Beans.copyDepth().from(prpLDlossPersTraceMainList).toList(PrpLDlossPersTraceMainVo.class);
		}
		return prpLDlossPersTraceMainVoList;
	}
	
	/** 核赔回写  **/
	@Override
	public void writeBackDLossPerson(PrpLDlossPersTraceMainVo DlossPersTraceMainVo) {
		PrpLDlossPersTraceMain dlossPers = databaseDao.findByPK(PrpLDlossPersTraceMain.class,
				DlossPersTraceMainVo.getId());
		if(dlossPers!=null){
			Beans.copy().from(DlossPersTraceMainVo).excludeNull().to(dlossPers);
			databaseDao.update(PrpLDlossPersTraceMain.class,dlossPers);
		}
		
	}

	/**
	 * 根据条件查新信息
	 * <pre></pre>
	 * @param registNo
	 * @param lossState 理算状态
	 * @param underwriteFlag 核损标注
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月26日 下午5:50:08): <br>
	 */
	@Override
	public PrpLDlossPersTraceMainVo findPersTraceMainVoByCondition(String registNo, List<String> lossState,String underWriteFlag) {
		PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		if(lossState != null && !"".equals(lossState)){
			queryRule.addIn("lossState",lossState);	
		}
		if(underWriteFlag != null && !"".equals(underWriteFlag)){
			queryRule.addIn("underwriteFlag",underWriteFlag);	
		}
		
		queryRule.addEqual("validFlag","1");
		
		PrpLDlossPersTraceMain prpLDlossPersTraceMain = databaseDao.findUnique(PrpLDlossPersTraceMain.class,queryRule);
		if(prpLDlossPersTraceMain != null){
			prpLDlossPersTraceMainVo = Beans.copyDepth().from(prpLDlossPersTraceMain).to(PrpLDlossPersTraceMainVo.class);
		}
		return prpLDlossPersTraceMainVo;
	}

	/**
	 * 根据QueryRule查找记录
	 * <pre></pre>
	 * @param queryRule
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月27日 下午5:03:55): <br>
	 */
	@Override
	public List<PrpLDlossPersTraceVo> findPrpLDlossPersTraceVoListByRule(QueryRule queryRule) {
		List<PrpLDlossPersTraceVo> prpLDlossPersTraceVoList = null;
		
		List<PrpLDlossPersTrace> prpLDlossPersTraceList = databaseDao.findAll(PrpLDlossPersTrace.class,queryRule);
		if(prpLDlossPersTraceList != null && !prpLDlossPersTraceList.isEmpty()){
			prpLDlossPersTraceVoList = Beans.copyDepth().from(prpLDlossPersTraceList).toList(PrpLDlossPersTraceVo.class);
		}
		return prpLDlossPersTraceVoList;
	}

	@Override
	public PrpLDlossPersTraceMainVo findPersTraceMainByPk(Long id) {
		// TODO Auto-generated method stub
		PrpLDlossPersTraceMain persTraceMain = databaseDao.findByPK(PrpLDlossPersTraceMain.class,id);

		PrpLDlossPersTraceMainVo persTraceMainVo = null;
		if(persTraceMain!=null){
			persTraceMainVo = Beans.copyDepth().from(persTraceMain).to(PrpLDlossPersTraceMainVo.class);
		}
		return persTraceMainVo;
	}

	@Override
	public boolean isDlossPersonAllPassed(String registNo) {
	   List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList = findPersTraceMainVoList(registNo);
	   //校验人伤任务
	   boolean pLoss = true;
	   if(prpLDlossPersTraceMainVoList != null){
		   for(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo:prpLDlossPersTraceMainVoList){
			   if((!CodeConstants.AuditStatus.SUBMITCHARGE.equals(prpLDlossPersTraceMainVo.getAuditStatus()))
			           && (!"7".equals(prpLDlossPersTraceMainVo.getUnderwriteFlag()))){//加上人伤注销
				   pLoss = false;
			   }
		   } 
	   }
	   if(pLoss){//判断是否有未接收的人伤任务
		   List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService.findUnAcceptTask(registNo,FlowNode.PLoss.name());
		   if(prpLWfTaskVoList != null && prpLWfTaskVoList.size() > 0){
			   pLoss =  false;
		   }
	   }
	return pLoss;
	}

	
	@Override
	public PrpLDlossPersTraceMainVo findPersTraceMainVoById(Long id) {
		// TODO Auto-generated method stub
				PrpLDlossPersTraceMain persTraceMain = databaseDao.findByPK(PrpLDlossPersTraceMain.class,id);

				PrpLDlossPersTraceMainVo persTraceMainVo = null;
				if(persTraceMain!=null){
					persTraceMainVo = Beans.copyDepth().from(persTraceMain).to(PrpLDlossPersTraceMainVo.class);
				}
				return persTraceMainVo;
	}
	
	@Override
	public Long saveOrUpdatePersTraceMain(PrpLDlossPersTraceMainVo persTraceMainVo){
		PrpLDlossPersTraceMain persTraceMain = null;

		if(persTraceMainVo.getId()==null){
			persTraceMain = new PrpLDlossPersTraceMain();
			Beans.copy().from(persTraceMainVo).to(persTraceMain);
		}else{
			persTraceMain = databaseDao.findByPK(PrpLDlossPersTraceMain.class,persTraceMainVo.getId());
			Beans.copy().from(persTraceMainVo).excludeNull().to(persTraceMain);
		}
		databaseDao.save(PrpLDlossPersTraceMain.class,persTraceMain);

		return persTraceMain.getId();
	}

	/* 
	 * @see ins.sino.claimcar.lossperson.service.PersTraceDubboService#getTraceTimes()
	 * @return
	 */
	@Override
	public int getTraceTimes(String registNo,Long injuredId) {
		// TODO Auto-generated method stub
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		
		sqlUtil.append("select count(*) from PrpLDlossPersTraceHis where registNo=? and injuredId=? ");
		sqlUtil.addParamValue(registNo);
		sqlUtil.addParamValue(injuredId);
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		
		return (int)databaseDao.getCount(sql,values);
	}
}
