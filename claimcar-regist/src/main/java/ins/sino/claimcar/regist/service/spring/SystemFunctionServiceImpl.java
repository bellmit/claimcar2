package ins.sino.claimcar.regist.service.spring;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;








import com.alibaba.dubbo.config.annotation.Service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.regist.po.PrpLRegist;
import ins.sino.claimcar.regist.service.SystemFunctionService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path("systemFunctionService")
public class SystemFunctionServiceImpl implements SystemFunctionService{
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	CodeTranService codeTranService;
	@Override
	public ResultPage<PrpLRegistVo> findRegistPage(PrpLWfTaskQueryVo taskQueryVo, int start, int length) throws Exception{
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLRegist regist where 1=1 ");
		if(StringUtils.isNotBlank(taskQueryVo.getRegistNo())){
			sqlUtil.append(" AND regist.registNo like ? ");
			sqlUtil.addParamValue("%"+taskQueryVo.getRegistNo()+"%");
		}
		String  table_task="regist";
		sqlUtil.andDate(taskQueryVo,table_task,"reportTime");
		sqlUtil.append(" AND regist.isQuickCase=? ");
		sqlUtil.addParamValue("1");
		sqlUtil.append(" ORDER by regist.reportTime desc ");
		
		Object[] values=sqlUtil.getParamValues();
		String sql=sqlUtil.getSql();
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		
		List<PrpLRegistVo> prpLRegistVos=new ArrayList<PrpLRegistVo>();
		if(page!=null && page.getResult()!=null){
			for(int i=0;i<page.getResult().size();i++){
				PrpLRegistVo registVo=new PrpLRegistVo();
				Object obj=page.getResult().get(i);
				PrpLRegist prpLRegist = (PrpLRegist)obj;//codeTranService
				String comCode = codeTranService.transCode("ComCode",prpLRegist.getComCode());
				prpLRegist.setComCode(comCode);
				Beans.copy().from(prpLRegist).to(registVo);
				registVo.setRemark("河南快赔");
				prpLRegistVos.add(registVo);
			}
		}
		ResultPage<PrpLRegistVo> resultPage = new ResultPage<PrpLRegistVo> (start, length, page.getTotalCount(),prpLRegistVos);
		return resultPage;
	}
	@Override
	public void updateIsQuickCase(String registNo) throws Exception{
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		PrpLRegist po=databaseDao.findUnique(PrpLRegist.class, qr);
		po.setIsQuickCase("2");
		
	}

}
