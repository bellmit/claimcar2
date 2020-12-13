package ins.sino.claimcar.manager.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.schema.PrpDHospital;
import ins.platform.vo.PrpDHospitalVo;
import ins.sino.claimcar.flow.service.HospitalService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})

@Path("HospitalService")
public class HospitalServiceImpl implements HospitalService {
	@Autowired
	private DatabaseDao databaseDao;
	
	
	/* 
	 * @see ins.sino.claimcar.manager.service.HospitalService#find(ins.platform.vo.PrpDHospitalVo, int, int)
	 * @param prpDHospitalVo
	 * @param start
	 * @param length
	 * @return
	 */
	@Override
	public ResultPage<PrpDHospitalVo> find(PrpDHospitalVo prpDHospitalVo,
			int start, int length) {
		// 定义参数list，ps：执行查询时需要转换成object数组
				List<Object> paramValues = new ArrayList<Object>();
				// hql查询语句,关联公估表和公估银行信息表查询
				StringBuffer queryString = new StringBuffer(
						"from PrpDHospital h ");
				if(StringUtils.isNotBlank(prpDHospitalVo.getHospitalCode()) || StringUtils.isNotBlank(prpDHospitalVo.getHospitalCName())
						|| StringUtils.isNotBlank(prpDHospitalVo.getValidFlag())){
					queryString.append(" where ");
				}
				// 根据公估机构代码查询
				if (StringUtils.isNotBlank(prpDHospitalVo.getHospitalCode())) {
					queryString.append(" h.hospitalCode like ? ");
					paramValues.add(prpDHospitalVo.getHospitalCode() + "%");
				}
				//queryString.append(" AND exists ( select 1 from PrpdIntermBank bk where bk.prpdIntermMain.id=pim.id and pim.intermCode like ? )  ");
				if (StringUtils.isNotBlank(prpDHospitalVo.getHospitalCode())
						&& StringUtils.isNotBlank(prpDHospitalVo.getHospitalCName())) {
					queryString.append(" AND ");
				}
				// 根据公估机构名称查询
				if (StringUtils.isNotBlank(prpDHospitalVo.getHospitalCName())) {
					queryString.append("  h.hospitalCName like ? ");
					paramValues.add("%"+prpDHospitalVo.getHospitalCName() + "%");
				}
				if(StringUtils.isNotBlank(prpDHospitalVo.getHospitalCode()) && StringUtils.isNotBlank(prpDHospitalVo.getValidFlag())){
					queryString.append(" AND ");
				}
	        	if (StringUtils.isNotBlank(prpDHospitalVo.getValidFlag())) {
	        		queryString.append(" h.validFlag = ? ");
					paramValues.add(prpDHospitalVo.getValidFlag());
		         }
				queryString.append(" Order By h.hospitalCode ");
				// 执行查询
				Page<PrpDHospital>  page=databaseDao.findPageByHql(PrpDHospital.class, queryString.toString(), (start
						/ length + 1), length, paramValues.toArray());
				 
				// 返回结果ResultPage
				ResultPage<PrpDHospitalVo> pageReturn = assemblyPolicyInfo(page,prpDHospitalVo);
				
				return pageReturn;
	}
	
	private ResultPage<PrpDHospitalVo> assemblyPolicyInfo(Page<PrpDHospital> page,PrpDHospitalVo prpDHospitalVo) {
		List<PrpDHospital> poList= page.getResult();
		List<PrpDHospitalVo> voList= new ArrayList<PrpDHospitalVo>();

		for(PrpDHospital po:poList){
			PrpDHospitalVo mainVo=Beans.copyDepth().from(po).to(PrpDHospitalVo.class);
			voList.add(mainVo);
		}
		
		ResultPage<PrpDHospitalVo> reslutPage=new ResultPage<PrpDHospitalVo>(page.getStart(), page.getPageSize(), page.getTotalCount(), voList);
				
		
		return reslutPage;
	}
	
	/* 
	 * @see ins.sino.claimcar.manager.service.HospitalService#findHospitalByCode(java.lang.String)
	 * @param hospitalCode
	 * @return
	 */
	@Override
	public PrpDHospitalVo findHospitalByCode(String hospitalCode){//通过医院代码查询医院信息
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("hospitalCode", hospitalCode);
		PrpDHospital prpDHospital= databaseDao.findAll(PrpDHospital.class,queryRule).get(0);
		PrpDHospitalVo prpDHospitalVo=Beans.copyDepth().from(prpDHospital).to(PrpDHospitalVo.class);
		return prpDHospitalVo;
	}
	
	/* 
	 * @see ins.sino.claimcar.manager.service.HospitalService#findHospitalById(long)
	 * @param id
	 * @return
	 */
	@Override
	public PrpDHospitalVo findHospitalById(long id){//通过医院代码查询医院信息
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("id", id);
		PrpDHospital prpDHospital= databaseDao.findAll(PrpDHospital.class,queryRule).get(0);
		PrpDHospitalVo prpDHospitalVo=Beans.copyDepth().from(prpDHospital).to(PrpDHospitalVo.class);
		return prpDHospitalVo;
	}
	//更新
	/* 
	 * @see ins.sino.claimcar.manager.service.HospitalService#updateOrSaveHospital(ins.platform.vo.PrpDHospitalVo)
	 * @param prpDHospitalVo
	 */
	@Override
	public void updateOrSaveHospital(PrpDHospitalVo prpDHospitalVo){//更新保存医院信息
		PrpDHospital prpDHospital = new PrpDHospital();
		Beans.copy().from(prpDHospitalVo).to(prpDHospital);
		prpDHospital.setUpdateTime(new Date());
		prpDHospital.setUpdateCode(ServiceUserUtils.getUserCode());
		databaseDao.update(PrpDHospital.class, prpDHospital);
		//PrpDHospital prpDHospital = Beans.copyDepth().from(prpDHospitalVo).to(PrpDHospital.class);
		//if(prpDHospital.getHospitalCode()!=null){//更新
		/*if(prpDHospitalVo.getHospitalCode()!=null){//更新
			prpDHospital.setUpdateTime(new Date());
			prpDHospital.setUpdateCode(SecurityUtils.getUserCode());
			System.out.println("yyyyyyyyy==="+prpDHospitalVo.getRemark());
			databaseDao.update(PrpDHospital.class, prpDHospital);
		
			databaseDao.update(PrpDHospital.class, prpDHospital);
		}else{//保存
			prpDHospital.setCreateTime(new Date());
			prpDHospital.setCreatorCode(SecurityUtils.getUserCode());
			prpDHospital.setValidFlag("1");
			databaseDao.save(PrpDHospital.class, prpDHospital);
		}*/
	}
	//保存
	/* 
	 * @see ins.sino.claimcar.manager.service.HospitalService#save(ins.platform.vo.PrpDHospitalVo)
	 * @param prpDHospitalVo
	 */
	@Override
	public void save(PrpDHospitalVo prpDHospitalVo){//更新保存医院信息
		
		PrpDHospital prpDHospital = new PrpDHospital();
		prpDHospitalVo.setCreateTime(new Date());
		prpDHospitalVo.setCreatorCode(ServiceUserUtils.getUserCode());
		prpDHospitalVo.setUpdateCode(ServiceUserUtils.getUserCode());
		prpDHospitalVo.setUpdateTime(new Date());
		//prpDHospitalVo.setValidFlag("1");
		prpDHospital = Beans.copyDepth().from(prpDHospitalVo).to(PrpDHospital.class);
		databaseDao.save(PrpDHospital.class, prpDHospital);
		}
}
