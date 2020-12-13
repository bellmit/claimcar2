package ins.sino.claimcar.selfHelpClaimCar.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.claim.selfHelpClaimCar.po.PrpLAutocasestateInfo;
import ins.sino.claimcar.selfHelpClaimCar.vo.DlossAmoutConfirmVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.PrpLAutocasestateInfoVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.casecarStateVo;
@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"} , timeout = 1000*60*10)
@Path("selfHelpClaimCarService")
public class SelfHelpClaimCarServiceImpl implements SelfHelpClaimCarService{
	@Autowired
	private DatabaseDao databaseDao;

	@Override
	public void updateOrSavePrpLAutocasestateInfo(DlossAmoutConfirmVo reqVo) {
		if(reqVo!=null && reqVo.getBodyVo()!=null && reqVo.getBodyVo().getCasecarStates()!=null && reqVo.getBodyVo().getCasecarStates().size()>0){
			for(casecarStateVo vo:reqVo.getBodyVo().getCasecarStates()){
				QueryRule  query=QueryRule.getInstance();
				query.addEqual("registNo",vo.getInscaseNo());
				query.addEqual("licenseNo",vo.getCasecarNo());
				List<PrpLAutocasestateInfo> lists=databaseDao.findAll(PrpLAutocasestateInfo.class,query);
				if(lists!=null && lists.size()>0){
					Beans.copy().excludeNull().from(vo).to(lists.get(0));
					lists.get(0).setUpdateTime(new Date());;
				}else{
					PrpLAutocasestateInfo po=new PrpLAutocasestateInfo();
					po.setLicenseNo(vo.getCasecarNo());
					po.setCreateTime(new Date());
					po.setRegistNo(vo.getInscaseNo());
					po.setDlosscarFlag(vo.getCasecarType());
					po.setStatus(vo.getState());
					po.setFeepayMoney(new BigDecimal(vo.getFeepayMoney()));
					databaseDao.save(PrpLAutocasestateInfo.class,po);
				}
			}
		}
		
	}

	@Override
	public PrpLAutocasestateInfoVo findPrpLAutocasestateInfoByRegistNoAndLicenseNo(String registNo, String licenseNo) {
		QueryRule query=QueryRule.getInstance();
		query.addEqual("registNo",registNo);
		query.addEqual("licenseNo",licenseNo);
		List<PrpLAutocasestateInfo> lists=databaseDao.findAll(PrpLAutocasestateInfo.class,query);
		PrpLAutocasestateInfoVo vo=null;
		if(lists!=null && lists.size()>0){
			vo=new PrpLAutocasestateInfoVo();
			Beans.copy().from(lists.get(0)).to(vo);
		}
		return vo;
	}
}
