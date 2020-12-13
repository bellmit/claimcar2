package ins.sino.claimcar.base.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants.JobStatus;
import ins.sino.claimcar.CodeConstants.JobType;
import ins.sino.claimcar.base.po.PrplQuartzLog;
import ins.sino.claimcar.other.service.QuartzLogService;
import ins.sino.claimcar.other.vo.PrplQuartzLogVo;
import ins.sino.claimcar.regist.service.RegistQueryService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 
 * <pre></pre>
 * @author ★LinYi
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("quartzLogService")
public class QuartzLogServiceImpl implements QuartzLogService {

//	private static Logger logger = LoggerFactory.getLogger(QuartzLogServiceImpl.class);

	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	BaseDaoService baseDaoService;
	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	private RegistQueryService registQueryService;
	
	
	@Override
    public void saveOrUpdateQuartzLog(PrplQuartzLogVo prplQuartzLogVo,SysUserVo sysUserVo) {
        Date date = new Date();
        PrplQuartzLog prplQuartzLog = null;
        if(prplQuartzLogVo.getId()==null){
            prplQuartzLogVo.setCreateUser(sysUserVo.getUserCode());
            prplQuartzLogVo.setCreateTime(date);
            prplQuartzLogVo.setUpdateUser(sysUserVo.getUserCode());
            prplQuartzLogVo.setUpdateTime(date);
            prplQuartzLog = new PrplQuartzLog();
            Beans.copy().from(prplQuartzLogVo).to(prplQuartzLog);
        }else {
            prplQuartzLog = databaseDao.findByPK(PrplQuartzLog.class,prplQuartzLogVo.getId());
            Beans.copy().excludeNull().excludeEmpty().from(prplQuartzLogVo).to(prplQuartzLog);
        }
        databaseDao.save(PrplQuartzLog.class,prplQuartzLog);
    }
	
	
	@Override
    public PrplQuartzLogVo findQuartzLogById(Long id) {
        PrplQuartzLogVo prplQuartzLogVo = null;
        PrplQuartzLog prplQuartzLog = databaseDao.findByPK(PrplQuartzLog.class,id);
        if(prplQuartzLog!=null){
            prplQuartzLogVo = new PrplQuartzLogVo();
            Beans.copy().from(prplQuartzLog).to(prplQuartzLogVo);
        }
        return prplQuartzLogVo;
    }
	
	
	@Override
    public PrplQuartzLogVo findQuartzLogByRegistNoAndJobType(String registNo,String jobType) {
	    PrplQuartzLogVo prplQuartzLogVo = null;
	    QueryRule queryRule = QueryRule.getInstance();
	    queryRule.addEqual("registNo",registNo);
	    queryRule.addEqual("jobType",jobType);
	    PrplQuartzLog prplQuartzLog = databaseDao.findUnique(PrplQuartzLog.class,queryRule);
	    if(prplQuartzLog!=null){
	        prplQuartzLogVo = new PrplQuartzLogVo();
	        Beans.copy().from(prplQuartzLog).to(prplQuartzLogVo);
	    }
        return prplQuartzLogVo;
    }
	
	
	@Override
    public List<PrplQuartzLogVo> findClaimForceFirstFail() {
		SqlJoinUtils sqluUtils = new SqlJoinUtils();
		sqluUtils.append(" FROM PrplQuartzLog reg where 1=1 ");
		sqluUtils.append(" and reg.jobType = ? ");
		sqluUtils.append(" and reg.status = ? ");
		sqluUtils.append(" and not exists (select 1 from PrpLClaim b where b.registNo=reg.registNo)");
		sqluUtils.addParamValue(JobType.CLAIMFORCE);
		sqluUtils.addParamValue(JobStatus.FIRST);
		System.out.println("++++++++++++++++++++++++++++++++++"+sqluUtils.getSql());
		List<PrplQuartzLog> prplQuartzLogs = databaseDao.findAllByHql(PrplQuartzLog.class,sqluUtils.getSql(),sqluUtils.getParamValues());
		List<PrplQuartzLogVo> prplQuartzLogVos = null;
        if(prplQuartzLogs!=null && prplQuartzLogs.size()>0){
            prplQuartzLogVos = new ArrayList<PrplQuartzLogVo>();
            prplQuartzLogVos = Beans.copyDepth().from(prplQuartzLogs).toList(PrplQuartzLogVo.class);
        }
        return prplQuartzLogVos;
    }
	
	
    @Override
    public ResultPage<PrplQuartzLogVo> findClaimForceForPage(PrplQuartzLogVo prplQuartzLogVo,int start,int length) throws Exception {
        
        SqlJoinUtils sqlUtil=new SqlJoinUtils();
        sqlUtil.append(" SELECT quartzLog.*,regist.reportTime FROM PrplQuartzLog quartzLog,PrplRegist regist ");
        sqlUtil.append(" WHERE quartzLog.registNo=regist.registNo ");
        sqlUtil.append(" AND quartzLog.jobType = ? ");
        sqlUtil.addParamValue(JobType.CLAIMFORCE);
        if("1".equals(prplQuartzLogVo.getStatus())){
            sqlUtil.andEquals(prplQuartzLogVo,"quartzLog","status");
        }else if("0".equals(prplQuartzLogVo.getStatus())){
            sqlUtil.addIn("quartzLog","status",JobStatus.FIRST,JobStatus.SECEND);
        }
        sqlUtil.andDate(prplQuartzLogVo,"quartzLog","createTime");
        sqlUtil.andLike2(prplQuartzLogVo,"quartzLog","registNo");
        sqlUtil.andDate(prplQuartzLogVo,"regist","reportTime");
        String sql = sqlUtil.getSql();
        Object[] param = sqlUtil.getParamValues();
        Page<Object[]> page = new Page<Object[]>();
        try{
            page = baseDaoService.pagedSQLQuery(sql,start,length,param);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        List<PrplQuartzLogVo> resultVoList = new ArrayList<PrplQuartzLogVo>();
        for(int i = 0; i<page.getResult().size(); i++ ){
            PrplQuartzLogVo prplQuartzLog = new PrplQuartzLogVo();
            Object[] obj = (Object[])page.getResult().get(i);
            prplQuartzLog.setId(((BigDecimal)obj[0]).longValue());
            prplQuartzLog.setRegistNo((String)obj[1]);
            prplQuartzLog.setJobType((String)obj[2]);
            String failReason = (String)obj[3];
            prplQuartzLog.setException(failReason.substring(0,-1==failReason.indexOf(":")?failReason.length():failReason.indexOf(":")));
            prplQuartzLog.setFailReason(failReason);
            prplQuartzLog.setStatus((String)obj[4]);
            prplQuartzLog.setCreateUser((String)obj[5]);
            prplQuartzLog.setCreateTime((Date)obj[6]);
            prplQuartzLog.setUpdateUser((String)obj[7]);
            prplQuartzLog.setUpdateTime((Date)obj[8]);
            prplQuartzLog.setReportTime((Date)obj[9]);
            resultVoList.add(prplQuartzLog);
        }
        
        ResultPage<PrplQuartzLogVo> resultPage = new ResultPage<PrplQuartzLogVo>(start,length,page.getTotalCount(),resultVoList);

        return resultPage;
    }
}
