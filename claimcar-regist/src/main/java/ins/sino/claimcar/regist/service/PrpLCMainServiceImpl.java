package ins.sino.claimcar.regist.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.spring.ProcWork;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.flow.vo.AMLVo;
import ins.sino.claimcar.regist.po.PrpCiInsureValid;
import ins.sino.claimcar.regist.po.PrpLCInsured;
import ins.sino.claimcar.regist.po.PrpLCItemCar;
import ins.sino.claimcar.regist.po.PrpLCItemKind;
import ins.sino.claimcar.regist.po.PrpLCMain;
import ins.sino.claimcar.regist.po.PrpLCengage;
import ins.sino.claimcar.regist.vo.PrpCiInsureValidVo;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Path;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path(value = "prpLCMainService")
public class PrpLCMainServiceImpl implements PrpLCMainService {

	@Autowired
	private DatabaseDao databaseDao;
	
	@Autowired
	private BaseDaoService baseDaoService;
	
	@Autowired
	private SessionFactory sessionFactory;
	private static Logger logger = LoggerFactory.getLogger(PrpLCMainServiceImpl.class);

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.PrpLCMainService#findRegistPolicy(java.lang.String, java.util.Date)
	 */
	@Override
	public PrpLCMainVo findRegistPolicy(String policyNo,Date damageDate) {
		String tempRegistNo = UUID.randomUUID().toString().substring(0, 24);
		// 调用存储过程PolicyCheck_Package.getRegistTempPolicy
		Long start = System.currentTimeMillis();
		Session session = sessionFactory.getCurrentSession();
		session.doWork(new ProcWork("{call POLICYCHECK_PACKAGE.getRegistTempPolicy(?,?,?)}",tempRegistNo,policyNo,damageDate){
			@Override
			public void execute(Connection conn) throws SQLException {
				CallableStatement statement = conn.prepareCall(this.getProSql());
				//为存储过程设置参数
				statement.setString(1, this.getPro().get("1").toString());
				statement.setString(2, this.getPro().get("2").toString());
				Date s =  (Date) this.getPro().get("3");
				statement.setTimestamp(3,new java.sql.Timestamp (s.getTime()));
				
				statement.execute();
			}
		});
		Long end = System.currentTimeMillis();
		logger.info("报案临时查询承保时间"+(end-start));
		
		
		// 查询出 PrpLCMainVo
		return findPrpLCMain(tempRegistNo,policyNo);
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.PrpLCMainService#findPrpLCMain(java.lang.String, java.lang.String)
	 */
	@Override
	public PrpLCMainVo findPrpLCMain(String registNo,String policyNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("registNo", registNo);
		queryRule.addIn("policyNo", policyNo);
		List<PrpLCMain> prpLCMains = databaseDao.findAll(PrpLCMain.class, queryRule);
		List<PrpLCMainVo> returnList = new ArrayList<PrpLCMainVo>();
		for (PrpLCMain po : prpLCMains) {
			if (po != null) {
				returnList.add(Beans.copyDepth().from(po).to(PrpLCMainVo.class));
			}
		}
		if(returnList.size()>0){
			return returnList.get(0);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.PrpLCMainService#saveRegistPolicy(java.lang.String, java.lang.String, java.util.Date)
	 */
	@Override
	public PrpLCMainVo saveRegistPolicy(String registNo,String policyNo,Date damageDate) {
		// 调用存储过程PolicyCheck_Package.saveRegistPolicy
		Long start = System.currentTimeMillis();
		Session session = sessionFactory.getCurrentSession();
		session.doWork(new ProcWork("{call POLICYCHECK_PACKAGE.saveRegistPolicy(?,?,?)}",registNo,policyNo,damageDate){
			@Override
			public void execute(Connection conn) throws SQLException {
				CallableStatement statement = conn.prepareCall(this.getProSql());
				//为存储过程设置参数
				statement.setString(1, this.getPro().get("1").toString());
				statement.setString(2, this.getPro().get("2").toString());
				Date s =  (Date) this.getPro().get("3");
				statement.setTimestamp(3,new java.sql.Timestamp (s.getTime()));
				statement.execute();
			}
		});
		
		Long end = System.currentTimeMillis();
		logger.info("报案查询承保数据时间"+ (end-start));
		
		// 查询出 PrpLCMainVo
		return findPrpLCMain(registNo,policyNo);
	}
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.PrpLCMainService#saveOrUpdate(java.util.List, java.lang.String)
	 */
	@Override
	public void saveOrUpdate(List<PrpLCMainVo> prpLCMains, String registNo) {
		
		for (PrpLCMainVo prpLCMainVo : prpLCMains) {
			
			PrpLCMain poMainPo = Beans.copyDepth().from(prpLCMainVo).to(PrpLCMain.class);
			
			poMainPo.setRegistNo(registNo);
			if (poMainPo.getId() == null) {
				poMainPo.setCreateUser("555");
				poMainPo.setCreateTime(new Date());
			}
			poMainPo.setUpdateUser("555");
			poMainPo.setUpdateTime(new Date());
			
			if (poMainPo.getPrpCInsureds() != null &&
					poMainPo.getPrpCInsureds().size()>0) {
				for (PrpLCInsured insuredPo : poMainPo.getPrpCInsureds()) {
					insuredPo.setPrpLCMain(poMainPo);
					insuredPo.setCreateUser(poMainPo.getCreateUser());
					insuredPo.setCreateTime(poMainPo.getCreateTime());
					insuredPo.setUpdateUser(poMainPo.getUpdateUser());
					insuredPo.setUpdateTime(poMainPo.getUpdateTime());
				}
			}
			
			if (poMainPo.getPrpCItemCars() != null &&
					poMainPo.getPrpCItemCars().size()>0) {
				for (PrpLCItemCar itemCarPo : poMainPo.getPrpCItemCars()) {
					itemCarPo.setPrpLCMain(poMainPo);
					itemCarPo.setRegistNo(registNo);
					itemCarPo.setCreateUser(poMainPo.getCreateUser());
					itemCarPo.setCreateTime(poMainPo.getCreateTime());
					itemCarPo.setUpdateUser(poMainPo.getUpdateUser());
					itemCarPo.setUpdateTime(poMainPo.getUpdateTime());
				}
			}
			
			if (poMainPo.getPrpCItemCars() != null &&
					poMainPo.getPrpCItemCars().size()>0) {
				for (PrpLCItemKind itemKindPo : poMainPo.getPrpCItemKinds()) {
					itemKindPo.setPrpLCMain(poMainPo);
					itemKindPo.setRegistNo(registNo);
					itemKindPo.setCreateUser(poMainPo.getCreateUser());
					itemKindPo.setCreateTime(poMainPo.getCreateTime());
					itemKindPo.setUpdateUser(poMainPo.getUpdateUser());
					itemKindPo.setUpdateTime(poMainPo.getUpdateTime());
				}
			}
			if (poMainPo.getPrpCengages() != null &&
					poMainPo.getPrpCengages().size()>0) {
				for (PrpLCengage cengagePo : poMainPo.getPrpCengages()) {
					cengagePo.setPrpLCMain(poMainPo);
					cengagePo.setRegistNo(registNo);
					cengagePo.setCreateUser(poMainPo.getCreateUser());
					cengagePo.setCreateTime(poMainPo.getCreateTime());
					cengagePo.setUpdateUser(poMainPo.getUpdateUser());
					cengagePo.setUpdateTime(poMainPo.getUpdateTime());
				}
			}
			
			if (poMainPo.getId() == null) {
				databaseDao.save(PrpLCMain.class, poMainPo);
			} else if(poMainPo.getValidFlag().equals("0")){
				databaseDao.deleteByPK(PrpLCMain.class, poMainPo.getId());
			} else{
				databaseDao.update(PrpLCMain.class, poMainPo);
			}
			
			
		}
	}
	/*
	 * private List<PrpLCInsured> prpCInsureds = new ArrayList<PrpLCInsured>(0);
	private List<PrpLCItemCar> prpCItemCars = new ArrayList<PrpLCItemCar>(0);
	private List<PrpLCengage> prpCengages = new ArrayList<PrpLCengage>(0);
	private List<PrpLCItemKind> prpCItemKinds = new ArrayList<PrpLCItemKind>(0); 
	 */

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.PrpLCMainService#findPrpLCMainsByRegistNo(java.lang.String)
	 */
	@Override
	public List<PrpLCMainVo> findPrpLCMainsByRegistNo(String registNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("registNo", registNo);
		List<PrpLCMain> prpLCMains = databaseDao.findAll(PrpLCMain.class, queryRule);
		List<PrpLCMainVo> returnList = new ArrayList<PrpLCMainVo>();
		for (PrpLCMain po : prpLCMains) {
			if (po != null) {
				returnList.add(Beans.copyDepth().from(po).to(PrpLCMainVo.class));
			}
		}
		return returnList;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.PrpLCMainService#updateValidFlag(ins.sino.claimcar.regist.vo.PrpLCMainVo)
	 */
	@Override
	public List<PrpLCMainVo> updateValidFlag(PrpLCMainVo prpLCMainVo) {
//		StringBuffer hql = new StringBuffer("update PrpLCMain pm set pm.validFlag = 0");
//		hql.append("where pm.policyNo =");
//		hql.append(prpLCMainVo.getPolicyNo());
//		hql.append("where pm.registNo =");
//		hql.append(prpLCMainVo.getRegistNo());
//		//databaseDao.
//		List<PrpLCMain> prpLCMains = databaseDao.findAll(PrpLCMain.class, queryRule);
//		List<PrpLCMainVo> returnList = new ArrayList<PrpLCMainVo>();
//		for (PrpLCMain po : prpLCMains) {
//			if (po != null) {
//				returnList.add(Beans.copyDepth().from(po).to(PrpLCMainVo.class));
//			}
//		}
//		return returnList;
		return null;
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.PrpLCMainService#deleteByRegistNo(ins.sino.claimcar.regist.vo.PrpLRegistVo)
	 */
	@Override
	public void deleteByRegistNo(PrpLRegistVo registVo) {
		// TODO Auto-generated method stub
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("registNo", registVo.getRegistNo());
		List<PrpLCMain> prpLCMains = databaseDao.findAll(PrpLCMain.class, queryRule);
		databaseDao.deleteAll(PrpLCMain.class, prpLCMains);
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.PrpLCMainService#updateItemCar(ins.sino.claimcar.regist.vo.PrpLCItemCarVo)
	 */
	@Override
	public void updateItemCar(PrpLCItemCarVo vo) {
		PrpLCItemCar itemCarPo = databaseDao.findByPK(PrpLCItemCar.class,vo.getItemCarId());
		Beans.copy().from(vo).to(itemCarPo);
		databaseDao.save(PrpLCItemCar.class,itemCarPo);
	}
	@Override
	public void updateItemKind(PrpLCItemKindVo vo) {
		PrpLCItemKind itemKindPo = databaseDao.findByPK(PrpLCItemKind.class,vo.getItemKindId());
		Beans.copy().from(vo).to(itemKindPo);
		databaseDao.update(PrpLCItemCar.class,itemKindPo);
	}
	
	//查询
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.PrpLCMainService#findPrpCiInsureValidByPolicyNo(java.lang.String)
	 */
	@Override
	public List<PrpCiInsureValidVo> findPrpCiInsureValidByPolicyNo (String policyNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("policyNo", policyNo );
		List<PrpCiInsureValid> prpCiInsureValidVos = databaseDao.findAll(PrpCiInsureValid.class, queryRule);
		List<PrpCiInsureValidVo> returnList = new ArrayList<PrpCiInsureValidVo>();
		for (PrpCiInsureValid po : prpCiInsureValidVos) {
			if (po != null) {
				returnList.add(Beans.copyDepth().from(po).to(PrpCiInsureValidVo.class));
			}
		}
		return returnList;
	}

    @Override
    public AMLVo findAMLInfoByRegistNo(String registNo) {
        AMLVo amlVo = new AMLVo();
        List<PrpLCMainVo> prpLCMainList = findPrpLCMainsByRegistNo(registNo);
        PrpLCMainVo cMain = prpLCMainList.get(0);
        List<PrpLCInsuredVo> PrpLCInsuredList = cMain.getPrpCInsureds();
        if(prpLCMainList != null && prpLCMainList.size() > 0 ){
            for(PrpLCInsuredVo cInsured:PrpLCInsuredList){
                if("1".equals(cInsured.getInsuredFlag())){
                    amlVo.setInsuredType(cInsured.getInsuredType());//被保险人类型
                    amlVo.setInsuredName(cInsured.getInsuredName());
                    amlVo.setSex(cInsured.getSex());
                    amlVo.setEducationCode(cInsured.getEducationCode());
                    amlVo.setIdentifyType(cInsured.getIdentifyType());
                    amlVo.setIdentifyNumber(cInsured.getIdentifyNumber());
                    amlVo.setCertifyDate(cInsured.getPossessNature());//证件有效期
                    amlVo.setMobile(cInsured.getMobile());
                    amlVo.setAdress(cInsured.getInsuredAddress());
                    amlVo.setPolicyNo(cInsured.getPolicyNo());
                    amlVo.setBusinessArea(cInsured.getAccountName());//经营范围
                    amlVo.setProfession(cInsured.getOccupationCode());//个人职业代码 
                }
                if("2".equals(cInsured.getInsuredFlag())){
                    amlVo.setInsureRelation(cInsured.getInsuredIdentity());//被保险人与投保人的关系
                }
            }
        }
        
        
        List<Object[]> objects = new ArrayList<Object[]>();
        SqlJoinUtils sqlUtil=new SqlJoinUtils();
        if("1".equals(amlVo.getInsuredType())){
            sqlUtil.append(" SELECT * from ywuser.prpcinsurednature WHERE insuredFlag = ?");
            sqlUtil.addParamValue("1");
            sqlUtil.append(" AND policyNo = ?");
            sqlUtil.addParamValue(amlVo.getPolicyNo());
            try{ 
                objects = baseDaoService.findListBySql(sqlUtil.getSql(),sqlUtil.getParamValues());
                if(objects!=null && objects.size()>0){
                    amlVo.setSex((String)objects.get(0)[3]);
                }
            } catch(Exception e){
                e.printStackTrace();
            }       
        }else {
            sqlUtil.append(" SELECT * from ywuser.prpcinsuredartif WHERE insuredFlag = ?");
            sqlUtil.addParamValue("1");
            sqlUtil.append(" AND policyNo = ?");
            sqlUtil.addParamValue(amlVo.getPolicyNo());
            try{
                objects = baseDaoService.findListBySql(sqlUtil.getSql(),sqlUtil.getParamValues());
                if(objects!=null && objects.size()>0){
                    amlVo.setBusinessCode((String)objects.get(0)[7]);
                    amlVo.setRevenueRegistNo((String)objects.get(0)[8]);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }            
        return amlVo;
    }

	@Override
	public List<PrpLCMainVo> findPrpLCMainsByClaimSequenceNo(String claimSequenceNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("claimSequenceNo", claimSequenceNo);
		List<PrpLCMain> prpLCMains = databaseDao.findAll(PrpLCMain.class, queryRule);
		List<PrpLCMainVo> returnList = new ArrayList<PrpLCMainVo>();
		for (PrpLCMain po : prpLCMains) {
			if (po != null) {
				returnList.add(Beans.copyDepth().from(po).to(PrpLCMainVo.class));
			}
		}
		return returnList;
		
	}

    @Override
    public PrpLCMainVo updatePrpLCMain(PrpLCMainVo prpLCMainVo) {
    	logger.info("----------main保存开始--------------------------------------");
        PrpLCMain poMainPo = databaseDao.findByPK(PrpLCMain.class,prpLCMainVo.getId());
        prpLCMainVo.setUpdateDate(new Date());
        Beans.copy().excludeNull().from(prpLCMainVo).to(poMainPo);
        databaseDao.update(PrpLCMain.class, poMainPo);
        PrpLCMainVo cMainVo = Beans.copyDepth().from(poMainPo).to(PrpLCMainVo.class);
        logger.info("----------main保存结束-------------------------------------");
        return cMainVo;
    }

    @Override
    public PrpLCMainVo findPrpLCMainByClaimSequenceNo(String claimSequenceNo) {
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addIn("claimSequenceNo", claimSequenceNo);
        PrpLCMain prpLCMain = databaseDao.findUnique(PrpLCMain.class, queryRule);
        PrpLCMainVo vo = Beans.copyDepth().from(prpLCMain).to(PrpLCMainVo.class);
        return vo;
    }
   
	
/*	public List<PrpLCMainVo> findPrpLCMainByPolicyNo(String policyNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("policyNo", policyNo);
		List<PrpLCMain> prpLRegistPos = databaseDao.findAll(PrpLCMain.class, qr);
		List<PrpLCMainVo> returnList = new ArrayList<PrpLCMainVo>();
		for (PrpLCMain po : prpLRegistPos) {
			if (po != null) {
				returnList.add(Beans.copyDepth().from(po).to(PrpLCMainVo.class));
			}
		}
		return returnList;
	}*/
}
