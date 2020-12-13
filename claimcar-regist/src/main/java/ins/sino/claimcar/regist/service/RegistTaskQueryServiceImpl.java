package ins.sino.claimcar.regist.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.utils.Beans;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.regist.po.PrpLRegist;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

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
@Path(value = "registTaskQueryService")
public class RegistTaskQueryServiceImpl implements RegistTaskQueryService {

	private static Logger logger = LoggerFactory.getLogger(RegistTaskQueryServiceImpl.class);

	@Autowired
	private DatabaseDao databaseDao;

/*	public void save(PrpLWfprpLRegistVo prpLRegistVo) {
		PrpLWfTaskQuery po = new PrpLWfTaskQuery();
		Beans.copy().from(prpLRegistVo).to(po);
		databaseDao.save(PrpLWfTaskQuery.class,po);
		logger.debug("WfTaskQueryService.po.id="+po.getFlowID());
	}*/
//PrpLRegistVo prpLRegistVo
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistTaskQueryService#findTaskForPage(ins.sino.claimcar.regist.vo.PrpLRegistVo, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public ResultPage<PrpLRegistVo> findTaskForPage(PrpLRegistVo prpLRegistVo,Integer start,Integer length,String keyProperty) throws Exception {

		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLRegist qry");
		// 根据任务状态区分
		//String registTaskFlag = prpLRegistVo.getRegistTaskFlag();
		sqlUtil.append(" WHERE RegistTaskFlag = ?");
		sqlUtil.addParamValue(prpLRegistVo.getRegistTaskFlag());

		//sqlUtil.addParamValue(0);
		String  table_query="qry";
		// 自动拼装等号条件
		sqlUtil.andEquals(prpLRegistVo,table_query,"registNo","policyNo");
		sqlUtil.andEquals(prpLRegistVo,table_query,"damageCode","firstRegUserCode","tempRegistFlag");
		sqlUtil.andDate(prpLRegistVo,table_query,"reportTime","damageTime");
		//sqlUtil.andLike(prpLRegistVo,table_query,"insuredName");
		/*licenseNo,insuredName,frameNo,linkerNumber,isOnSitReport*/
		// 排序
		sqlUtil.append(" ORDER BY qry.reportTime DESC");
		String sql = sqlUtil.getSql();
		logger.debug("taskQrySql="+sql);
		Page<PrpLRegist> page = databaseDao.findPageByHql(PrpLRegist.class, sql,start/length+1,length,sqlUtil.getParamValues());
		// 对象转换
		List<PrpLRegistVo> resultVoList=new ArrayList<PrpLRegistVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){
			PrpLRegist registPo= page.getResult().get(i);
			PrpLRegistVo resultVo = Beans.copyDepth().from(registPo).to(PrpLRegistVo.class);
			resultVoList.add(resultVo);
		}

		ResultPage<PrpLRegistVo> resultPage = new ResultPage<PrpLRegistVo> (start, length, page.getTotalCount(), resultVoList);

		return resultPage;
		
	}

}
