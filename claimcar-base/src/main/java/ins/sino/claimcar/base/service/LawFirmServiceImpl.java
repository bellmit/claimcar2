package ins.sino.claimcar.base.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.utils.Beans;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.base.po.PrpdLawFirm;
import ins.sino.claimcar.claim.vo.PrpdLawFirmVo;
import ins.sino.claimcar.other.service.LawFirmService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"})
@Path(value = "LawFirmService")
public class LawFirmServiceImpl implements LawFirmService {
	@Autowired
	DatabaseDao databaseDao;
	
	/**
	 * 保存或更新律师事务所
	 * 
	 * @param PrpdLawFirmVo
	 * @return prpdLawFirm
	 */
	@Override
	public void saveOrUpdatePrpdLawFirm(PrpdLawFirmVo prpdLawFirmVo) {
			
		    PrpdLawFirm prpdLawFirm = Beans.copyDepth().from(prpdLawFirmVo)
				.to(PrpdLawFirm.class);
			
		if (prpdLawFirm.getId() != null) {
			databaseDao.update(PrpdLawFirm.class, prpdLawFirm);
		} else {
			databaseDao.save(PrpdLawFirm.class, prpdLawFirm);
		}
			
//		return prpdLawFirm;

	}
	
	/**
	 * 根据主键find PrpdLawFirmVo
	 * 
	 * @param id
	 * @return prpdLawFirmVo
	 */
	@Override
	public PrpdLawFirmVo findPrpdLawFirmVoByPK(Long id) {

		PrpdLawFirmVo prpdLawFirmVo = new PrpdLawFirmVo();
		if (id != null) {
			PrpdLawFirm prpdLawFirm = databaseDao.findByPK(
					PrpdLawFirm.class, id);

			prpdLawFirmVo = Beans.copyDepth().from(prpdLawFirm)
					.to(PrpdLawFirmVo.class);
		}

		return prpdLawFirmVo;

	}
	
	/**
	 * find PrpdLawFirmVo
	 * @param id
	 * @return prpdLawFirmVos
	 */
	@Override
	public ResultPage<PrpdLawFirmVo> findAllPrpdLawFirmByHql(
			PrpdLawFirmVo prpdLawFirmVo, int start, int length) {
		// 定义参数list，执行查询时需要转换成object数组
		/*QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqualIfExist("lawFirmCode",prpdLawFirmVo.getLawFirmCode());
		queryRule.addEqualIfExist("lawFirmName",prpdLawFirmVo.getLawFirmName());
		List<PrpdLawFirm> prpdLawFirmList = databaseDao.findAll(PrpdLawFirm.class,queryRule);
		List<PrpdLawFirmVo> prpdLawFirmVos = new ArrayList<PrpdLawFirmVo>();
		if(prpdLawFirmList!=null||prpdLawFirmList.size()>0){
			for(int i=1;i<=prpdLawFirmList.size();i++){
				prpdLawFirmVos=Beans.copyDepth().from(prpdLawFirmList).toList(PrpdLawFirmVo.class);
			}
		}
		return prpdLawFirmVos;*/
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpdLawFirm msg  where 1=1");
//		sqlUtil.append(" where 1=1 ");
		if(StringUtils.isNotBlank(prpdLawFirmVo.getLawFirmCode())){
			sqlUtil.append(" AND msg.lawFirmCode = ?");
			sqlUtil.addParamValue(prpdLawFirmVo.getLawFirmCode());
		}
		if(StringUtils.isNotBlank(prpdLawFirmVo.getLawFirmName())){
			sqlUtil.append(" AND msg.lawFirmName = ?");
			sqlUtil.addParamValue(prpdLawFirmVo.getLawFirmName());
			}
		
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		System.out.println("=========xxsql="+sql);
		System.out.println("=========values="+ArrayUtils.toString(values));
	   Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		System.out.println("=========page="+page.getPageSize());
        
		// 对象转换
		List<PrpdLawFirmVo> resultVoList=new ArrayList<PrpdLawFirmVo>();
		for(int i = 0; i<page.getResult().size(); i++ ){

			PrpdLawFirmVo resultVo=new PrpdLawFirmVo();
			Object obj = page.getResult().get(i);

			/*PrpLWfTaskQuery wfTaskQuery = (PrpLWfTaskQuery)obj[0];//强制转换
			Beans.copy().from(wfTaskQuery).to(resultVo);*/
			
			PrpdLawFirm prpdLawFirm = (PrpdLawFirm)obj;
			Beans.copy().from(prpdLawFirm).excludeNull().to(resultVo);
			resultVoList.add(resultVo);
		}

		ResultPage<PrpdLawFirmVo> resultPage = new ResultPage<PrpdLawFirmVo> (start, length, page.getTotalCount(), resultVoList);
		return resultPage;
	}
	
	
	
	@Override
	public List<PrpdLawFirmVo> findAllPrpdLawFirm(){
		List<PrpdLawFirm> prpdLawFirmList = databaseDao.findAll(PrpdLawFirm.class);
		List<PrpdLawFirmVo> prpdLawFirmVos = new ArrayList<PrpdLawFirmVo>();
		if(prpdLawFirmList!=null||prpdLawFirmList.size()>0){
			for(int i=1;i<=prpdLawFirmList.size();i++){
				prpdLawFirmVos=Beans.copyDepth().from(prpdLawFirmList).toList(PrpdLawFirmVo.class);
			}
		}
		return prpdLawFirmVos;
	}
	
	@Override
	public String updates(PrpdLawFirmVo prpdLawFirmVo){
		PrpdLawFirm prpdLawFirm = new PrpdLawFirm();
		Beans.copy().from(prpdLawFirmVo).excludeNull().to(prpdLawFirm);
		databaseDao.save(PrpdLawFirm.class, prpdLawFirm);
		return "success";
	}
	
	/**
	 * 创建一个Excel
	 * @return
	 * @throws Exception
	 */
	@Override
	public  List<PrpdLawFirmVo> getPrpdLawFirm() throws Exception  {
        List list = new ArrayList();  
        list=findAllPrpdLawFirm();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");  
        for(int i=0;i<list.size();i++){
        	
        }
        PrpdLawFirm user1 = new PrpdLawFirm();  
        list.add(user1);  
  
        return list;  
    }  

}
