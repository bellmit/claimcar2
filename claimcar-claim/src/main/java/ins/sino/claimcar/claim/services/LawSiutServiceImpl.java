/**
 * 
 */
package ins.sino.claimcar.claim.services;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.vo.PrpLLawSuitVo;
import ins.sino.claimcar.claim.po.PrpLLawSuit;
import ins.sino.claimcar.claim.service.LawSiutService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * @author CMQ
 *
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("lawsiutService")
public class LawSiutServiceImpl implements LawSiutService {
	
	@Autowired
	private DatabaseDao databaseDao;

	/* 
	 * @see ins.sino.claimcar.claim.services.LawSiutService#save(ins.platform.vo.PrpLLawSuitVo)
	 * @param lawSuitVo
	 */
	@Override
	public void save(PrpLLawSuitVo lawSuitVo){//更新保存医院信息
		
		PrpLLawSuit lawSuitPo= new PrpLLawSuit();
	/*	prpDHospitalVo.setCreateTime(new Date());
		prpDHospitalVo.setCreatorCode(SecurityUtils.getUserCode());
		prpDHospitalVo.setUpdateCode(SecurityUtils.getUserCode());
		prpDHospitalVo.setUpdateTime(new Date());*/
		//prpDHospitalVo.setValidFlag("1");
		lawSuitVo.setCreateUser(ServiceUserUtils.getUserCode());
		lawSuitVo.setCreateTime(new Date());
		Beans.copy().from(lawSuitVo).excludeNull().to(lawSuitPo);
		databaseDao.save(PrpLLawSuit.class, lawSuitPo);
		}
	
	/* 
	 * @see ins.sino.claimcar.claim.services.LawSiutService#findByRegistNo(java.lang.String)
	 * @param RegistNo
	 * @return
	 */
	@Override
	public List<PrpLLawSuitVo> findByRegistNo(String RegistNo){
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", RegistNo);
		List<PrpLLawSuit> lawSuitList= databaseDao.findAll(PrpLLawSuit.class,queryRule);
		List<PrpLLawSuitVo> lawSuitVoList = new ArrayList<PrpLLawSuitVo>();
		for(int i=0;i<lawSuitList.size();i++){
			PrpLLawSuitVo lawSuitVo=Beans.copyDepth().from(lawSuitList.get(i)).to(PrpLLawSuitVo.class);
			lawSuitVoList.add(lawSuitVo);
		}
		return lawSuitVoList;
		
	}
	
	
	//更新
		/* 
		 * @see ins.sino.claimcar.claim.services.LawSiutService#updateLawSuit(ins.platform.vo.PrpLLawSuitVo)
		 * @param lawSuitVo
		 */
		@Override
		public void updateLawSuit(PrpLLawSuitVo lawSuitVo){//更新保存医院信息
			PrpLLawSuit lawSuit = new PrpLLawSuit();
			Beans.copy().from(lawSuitVo).to(lawSuit);
			/*prpDHospital.setUpdateTime(new Date());
			prpDHospital.setUpdateCode(SecurityUtils.getUserCode());*/
			databaseDao.update(PrpLLawSuit.class, lawSuit);
		}
		//删除
		
		/* 
		 * @see ins.sino.claimcar.claim.services.LawSiutService#deleteLawSuit(ins.platform.vo.PrpLLawSuitVo)
		 * @param lawSuitVo
		 */
		@Override
		public void deleteLawSuit(PrpLLawSuitVo lawSuitVo){//更新保存医院信息
			databaseDao.deleteByPK(PrpLLawSuit.class, lawSuitVo.getId());
		}

		@Override
		public void pinganSave(PrpLLawSuitVo lawSuitVo) {
			PrpLLawSuit lawSuitPo= new PrpLLawSuit();
			Beans.copy().from(lawSuitVo).excludeNull().to(lawSuitPo);
			databaseDao.save(PrpLLawSuit.class, lawSuitPo);
		}
		
}
