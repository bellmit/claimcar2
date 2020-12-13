package ins.sino.claimcar.claim.services.spring;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.claim.po.PrplFeeStandard;
import ins.sino.claimcar.claim.service.PayFeeStandardService;
import ins.sino.claimcar.claim.vo.PrplFeeStandardVo;
@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"} , timeout = 1000*60*10)
@Path("payFeeStandardService")
public class PayFeeStandardServiceImpl implements PayFeeStandardService{
	private static Logger logger = LoggerFactory.getLogger(PayFeeStandardServiceImpl.class);
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	BaseDaoService baseDaoService;
	
	
	
	@Override
	public PrplFeeStandardVo findFeeStandardVoByAreaCodeAndYearCode(String areaCode, String yearCode) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("areaCode",areaCode);
		queryRule.addEqual("yearCode",yearCode);
		queryRule.addEqual("validStatus","1");
		List<PrplFeeStandard> prplFeeStandards=databaseDao.findAll(PrplFeeStandard.class, queryRule);
		PrplFeeStandardVo prplFeeStandardVo=null;
		if(prplFeeStandards!=null && prplFeeStandards.size()>0){
			prplFeeStandardVo=new PrplFeeStandardVo();
			Beans.copy().from(prplFeeStandards.get(0)).to(prplFeeStandardVo);
		}
		return prplFeeStandardVo;
	}

	@Override
	public void saveOrUpdatePrplFeeStandardVo(PrplFeeStandardVo prplFeeStandardVo) {
	    if(prplFeeStandardVo!=null && prplFeeStandardVo.getId()!=null){
			PrplFeeStandard prplFeeStandard=new PrplFeeStandard();
			Beans.copy().from(prplFeeStandardVo).to(prplFeeStandard);
			databaseDao.update(PrplFeeStandard.class,prplFeeStandard);
		}else{
			PrplFeeStandard prplFeeStandard=new PrplFeeStandard();
			Beans.copy().from(prplFeeStandardVo).to(prplFeeStandard);
			prplFeeStandard.setCreateTime(new Date());
			prplFeeStandard.setValidStatus("1");
			databaseDao.save(PrplFeeStandard.class, prplFeeStandard);
		}
		
	}

	@Override
	public ResultPage<PrplFeeStandardVo> findAllFeeStandard(PrplFeeStandardVo prplFeeStandardVo, int start, int length) throws Exception  {
			SqlJoinUtils sqlUtil=new SqlJoinUtils();
			/*select s.*  
			from ( 
			    select a.*,row_number() over (partition by a.imagebussno order by a.createtime) as group_idx  
			    from PrplFeeStandard a
			) s
			where s.group_idx = 1 order by s.createtime desc*/
			sqlUtil.append("select s.* from (select a.*,row_number() over (partition by a.imageBussNo order by a.createTime)"
					+ "as group_idx from PrplFeeStandard a where a.validstatus='1') s where s.group_idx = 1");
			
              if(StringUtils.isNotBlank(prplFeeStandardVo.getAreaCode())){
				if("0000".equals(prplFeeStandardVo.getAreaCode().substring(2))){
					sqlUtil.append(" AND s.areaCode like ?");
					sqlUtil.addParamValue(prplFeeStandardVo.getAreaCode().substring(0, 2)+"%");
				}else{
					sqlUtil.append(" AND s.areaCode = ?");
					sqlUtil.addParamValue(prplFeeStandardVo.getAreaCode());
				}
				
			}
			if(StringUtils.isNotBlank(prplFeeStandardVo.getYearCode())){
				sqlUtil.append(" AND s.yearCode = ?");
				sqlUtil.addParamValue(prplFeeStandardVo.getYearCode());
			}
			if(StringUtils.isNotBlank(prplFeeStandardVo.getMonthCode())){
				sqlUtil.append(" AND s.monthCode = ?");
				sqlUtil.addParamValue(prplFeeStandardVo.getMonthCode());
			}
            sqlUtil.append(" AND s.validStatus = ?");
			sqlUtil.addParamValue("1");
			sqlUtil.append(" order by s.createTime desc ");
			String sql = sqlUtil.getSql();
			Object[] values = sqlUtil.getParamValues();

			logger.info("taskQrySql=" + sql);
			System.out.println("taskQrySql=" + sql);
			logger.info("ParamValues=" + ArrayUtils.toString(values));
			System.out.println("ParamValues=" + ArrayUtils.toString(values));

			Page<Object[]> page = baseDaoService.pagedSQLQuery(sql, start, length,
					values);

			long pageLengthX = page.getTotalCount();
			//"imageBussNo-5","areaCode-1", "areaName-2", "dateCode";
			// 对象转换
			List<PrplFeeStandardVo> feeStandardVos = new ArrayList<PrplFeeStandardVo>();
			for (int i = 0; i < page.getResult().size(); i++) {

				Object[] obj = page.getResult().get(i);
				PrplFeeStandardVo resultVo = new PrplFeeStandardVo();
				resultVo.setAreaCode(obj[1]==null?"":obj[1].toString());
				resultVo.setAreaName(obj[2]==null?"":obj[2].toString());
				resultVo.setImageBussNo(obj[5]==null?"":obj[5].toString());
				resultVo.setYearCode(obj[3]==null?"":obj[3].toString());
				resultVo.setMonthCode(obj[9]==null?"":obj[9].toString());
				if(StringUtils.isNotBlank(resultVo.getMonthCode())){
					resultVo.setDateCode(resultVo.getYearCode()+"-"+resultVo.getMonthCode());
				}else{
					resultVo.setDateCode(resultVo.getYearCode());
				}
				
				feeStandardVos.add(resultVo);
			}
			// }
			ResultPage<PrplFeeStandardVo> resultPage = new ResultPage<PrplFeeStandardVo>(
					start, length, pageLengthX, feeStandardVos);
			return resultPage;
			
			/*sqlUtil.append(" FROM PrplFeeStandard standard  where 1=1");
			if(StringUtils.isNotBlank(prplFeeStandardVo.getAreaCode())){
				
				if("0000".equals(prplFeeStandardVo.getAreaCode().substring(2))){
					sqlUtil.append(" AND standard.areaCode like ?");
					sqlUtil.addParamValue(prplFeeStandardVo.getAreaCode().substring(0, 2)+"%");
				}else{
					sqlUtil.append(" AND standard.areaCode = ?");
					sqlUtil.addParamValue(prplFeeStandardVo.getAreaCode());
				}
				
			}
			if(StringUtils.isNotBlank(prplFeeStandardVo.getYearCode())){
				sqlUtil.append(" AND standard.yearCode = ?");
				sqlUtil.addParamValue(prplFeeStandardVo.getYearCode());
			}
			if(StringUtils.isNotBlank(prplFeeStandardVo.getMonthCode())){
				sqlUtil.append(" AND standard.monthCode = ?");
				sqlUtil.addParamValue(prplFeeStandardVo.getMonthCode());
			}

		    sqlUtil.append(" AND standard.validStatus = ?");
			sqlUtil.addParamValue("1");
			sqlUtil.append(" order by standard.createTime desc ");
			String sql = sqlUtil.getSql();
			Object[] values = sqlUtil.getParamValues();
			Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
			List<PrplFeeStandardVo> resultVoList=new ArrayList<PrplFeeStandardVo>();
			Set<String> bussNoSet=new HashSet<String>();
			for(int i = 0; i<page.getResult().size(); i++ ){
				PrplFeeStandardVo resultVo = new PrplFeeStandardVo();
				Object obj = page.getResult().get(i);
				PrplFeeStandard PrplFeeStandard = (PrplFeeStandard)obj;
				Beans.copy().from(PrplFeeStandard).to(resultVo);
				if(!bussNoSet.contains(resultVo.getImageBussNo())){
					bussNoSet.add(resultVo.getImageBussNo());//同一个案件号只查出一条数据
					if(StringUtils.isNotBlank(resultVo.getMonthCode())){
						resultVo.setDateCode(resultVo.getYearCode()+"-"+resultVo.getMonthCode());
					}else{
						resultVo.setDateCode(resultVo.getYearCode());
					}
					resultVoList.add(resultVo);
				}
				
			}*/
			/*ResultPage<PrplFeeStandardVo> resultPage = new ResultPage<PrplFeeStandardVo> (start, length, page.getTotalCount(), resultVoList);
			return resultPage;*/
		}

	@Override
	public PrplFeeStandardVo findPrplFeeStandardVoById(Long id) {
		PrplFeeStandardVo prplFeeStandardVo=null;
		if(id!=null){
			prplFeeStandardVo=new PrplFeeStandardVo();
			PrplFeeStandard po=databaseDao.findByPK(PrplFeeStandard.class,id);
			Beans.copy().from(po).to(prplFeeStandardVo);
		}
		return prplFeeStandardVo;
	}

	@Override
	public void deletePrplFeeStandardVoById(Long id) {
		if(id!=null){
			PrplFeeStandard po=databaseDao.findByPK(PrplFeeStandard.class,id);
			po.setValidStatus("0");//逻辑删除
		}
		
	}

	@Override
	public List<PrplFeeStandardVo> findPrplFeeStandardBybussNo(String bussNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("imageBussNo",bussNo);
		queryRule.addEqual("validStatus","1");
		List<PrplFeeStandard> prplFeeStandards=databaseDao.findAll(PrplFeeStandard.class, queryRule);
		List<PrplFeeStandardVo> prplFeeStandardVos=new ArrayList<PrplFeeStandardVo>();
		if(prplFeeStandards!=null && prplFeeStandards.size()>0){
			prplFeeStandardVos=Beans.copyDepth().from(prplFeeStandards).toList(PrplFeeStandardVo.class);
		}
		return prplFeeStandardVos;
	}

	@Override
	public void deletePrplFeeStandardVoByImagebussNo(String ImagebussNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("imageBussNo",ImagebussNo);
		queryRule.addEqual("validStatus","1");
		List<PrplFeeStandard> prplFeeStandards=databaseDao.findAll(PrplFeeStandard.class, queryRule);
		if(prplFeeStandards!=null && prplFeeStandards.size()>0){
			for(PrplFeeStandard po:prplFeeStandards){
				po.setValidStatus("0");
			}
		}
		
	}
}
