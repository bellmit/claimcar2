package ins.sino.claimcar.manager.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.platform.schema.PrpdAppraisa;
import ins.sino.claimcar.flow.service.AppraisaService;
import ins.sino.claimcar.manager.vo.PrpdAppraisaVo;








import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})

@Path("AppraisaService")
public class AppraisaServiceImpl implements AppraisaService{
   
	@Autowired
	DatabaseDao dataBaseDao;
	@Autowired
	CodeTranService codeTranService;
	private static Logger logger = LoggerFactory.getLogger(RepairFactoryServiceImpl.class);
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public Page<PrpdAppraisaVo> findAllAppraisa(PrpdAppraisaVo prpdAppraisaVo,int start, int length) {
		// 定义参数list，ps：执行查询时需要转换成object数组
				List<Object> paramValues = new ArrayList<Object>();
				// hql查询语句
				StringBuffer queryString = new StringBuffer("from PrpdAppraisa pf where 1=1 ");
				// 名称
				if (StringUtils.isNotBlank(prpdAppraisaVo.getAppraisaName())) {
					queryString.append(" AND pf.appraisaName like ? ");
					paramValues.add("%"+prpdAppraisaVo.getAppraisaName()+"%");
				}
				// 代码
				if (StringUtils.isNotBlank(prpdAppraisaVo.getAppraisaCode())) {
					queryString.append(" AND pf.appraisaCode like ? ");
					paramValues.add(prpdAppraisaVo.getAppraisaCode() + "%");
				}
				// 有效无效标志
				if(StringUtils.isNotBlank(prpdAppraisaVo.getValidStatus())){
					queryString.append(" AND pf.validStatus = ? ");
					paramValues.add(prpdAppraisaVo.getValidStatus());
				}
				
				if (StringUtils.isNotBlank(prpdAppraisaVo.getAreaCode())) {
					queryString.append(" AND pf.areaCode like ? ");
					paramValues.add(prpdAppraisaVo.getAreaCode() + "%");
				}
				
				queryString.append(" Order By pf.id ");
				
				// 执行查询
				Page page = dataBaseDao.findPageByHql(queryString.toString(), (start/ length + 1), length, paramValues.toArray());
				Page pageReturn = assemblyPolicyInfo(page,prpdAppraisaVo);
				
				
				// 返回结果ResultPage
				return pageReturn;
			}
	
	/*
	 *  循环查询结果集合
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Page assemblyPolicyInfo(Page page,PrpdAppraisaVo prpdAppraisaVo) {
		for (int i = 0; i < page.getResult().size(); i++) {
			PrpdAppraisaVo plyVo = new PrpdAppraisaVo();
			PrpdAppraisa pm = (PrpdAppraisa) page.getResult().get(i);
			Beans.copy().from(pm).to(plyVo);
			page.getResult().set(i, plyVo);
		}
		return page;
	}
	
	/**
	 * 通过Id查询伤残鉴定机构
	 */
	@Override
	public PrpdAppraisaVo findAppraisaById(long id) {
			QueryRule queryRule =QueryRule.getInstance();
			queryRule.addEqual("id",id);
			
			PrpdAppraisa prpdAppraisa= dataBaseDao.findAll(PrpdAppraisa.class,queryRule).get(0);
			PrpdAppraisaVo prpdAppraisaVo=Beans.copyDepth().from(prpdAppraisa).to(PrpdAppraisaVo.class);
			
			return prpdAppraisaVo;
	
	}

	/*
	 * 异步查询factorycode是否在数据库中存在
	 */
	@Override
	public boolean findAppraisaCode(String appraisaCode){
		QueryRule qr=QueryRule.getInstance();
		qr.addEqual("appraisaCode",appraisaCode);
		List<PrpdAppraisa> list=dataBaseDao.findAll(PrpdAppraisa.class,qr);
		return list.size()>0 ? false : true;
	}
	
/**
 * 保存或更新伤残鉴定机构
 */
	@Override
	public void savaOrUpDate(PrpdAppraisaVo prpdAppraisaVo,String userCode) {
		
		Long AppId = prpdAppraisaVo.getId();
		Date date = new Date();
		
		if(AppId==null){
			prpdAppraisaVo.setCreatUser(userCode);
			prpdAppraisaVo.setCreatTime(date);
			prpdAppraisaVo.setValidStatus("1");
		    String address=	codeTranService.transCode("AreaCode", prpdAppraisaVo.getAreaCode());
		    prpdAppraisaVo.setAddress(address);
			PrpdAppraisa prpdAppraisa=new PrpdAppraisa();
			Beans.copy().from(prpdAppraisaVo).to(prpdAppraisa);
			dataBaseDao.save(PrpdAppraisa.class,prpdAppraisa);
			
		}else{
			  
		      prpdAppraisaVo.setUpdateUser(userCode);
		      prpdAppraisaVo.setUpdateTime(date);
		      PrpdAppraisa prpdAppraisa=dataBaseDao.findByPK(PrpdAppraisa.class, AppId);
		      String address=codeTranService.transCode("AreaCode", prpdAppraisaVo.getAreaCode());
			    prpdAppraisaVo.setAddress(address);
		      Beans.copy().from(prpdAppraisaVo).excludeNull().to(prpdAppraisa);
		      dataBaseDao.update(PrpdAppraisa.class,prpdAppraisa);
		}
		
	}
	
	
//通过伤残机构代码查找对应的伤残机构名称
@Override
public String findAppraisaName(String appraisaCode) {
	String appraisaName="";
    QueryRule queryRule =QueryRule.getInstance();
    queryRule.addEqual("appraisaCode",appraisaCode);
    queryRule.addEqual("validStatus","1");
    List<PrpdAppraisa> list=dataBaseDao.findAll(PrpdAppraisa.class,queryRule);
    PrpdAppraisa prpdAppraisa=new PrpdAppraisa();
    if(list!=null && list.size()>0){
    	 prpdAppraisa=list.get(0);
    }
	if(prpdAppraisa!=null){
		appraisaName=prpdAppraisa.getAppraisaName();
	}
	return appraisaName;
}
	
			
}