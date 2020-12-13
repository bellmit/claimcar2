package ins.sino.claimcar.claim.services;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.claim.po.PrplFraudrisk;
import ins.sino.claimcar.claim.po.PrplLabor;
import ins.sino.claimcar.claim.po.PrplcecheckResult;
import ins.sino.claimcar.claim.po.PrplnoOperation;
import ins.sino.claimcar.claim.po.PrplsmallSparepart;
import ins.sino.claimcar.claim.po.PrplspaRepart;
import ins.sino.claimcar.claim.vo.PrplcecheckResultVo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("claimcedDlservice")
public class ClaimcedDlservice {
	@Autowired
	private DatabaseDao databaseDao;
	
	
	public void saveCeInfo(PrplcecheckResultVo prplcecheckResultVo) {
		if(prplcecheckResultVo!=null){
			PrplcecheckResult prplcecheckResult=Beans.copyDepth().from(prplcecheckResultVo).to(PrplcecheckResult.class);
			if(prplcecheckResult!=null){
			  if(prplcecheckResult.getPrplpriceSummary()!=null){
				  prplcecheckResult.getPrplpriceSummary().setPrplcecheckResult(prplcecheckResult);
			  }
			  if(prplcecheckResult.getPrplspaReparts()!=null && prplcecheckResult.getPrplspaReparts().size()>0){
				  for(PrplspaRepart repartPo:prplcecheckResult.getPrplspaReparts()){
					  repartPo.setPrplcecheckResult(prplcecheckResult);
				  }
			  }
			  if(prplcecheckResult.getPrplLabors()!=null && prplcecheckResult.getPrplLabors().size()>0){
				  for(PrplLabor laborPo:prplcecheckResult.getPrplLabors()){
					  laborPo.setPrplcecheckResult(prplcecheckResult);
				  }
			  }
			  if(prplcecheckResult.getPrplsmallSpareparts()!=null && prplcecheckResult.getPrplsmallSpareparts().size()>0){
				  for(PrplsmallSparepart smallPo:prplcecheckResult.getPrplsmallSpareparts()){
					  smallPo.setPrplcecheckResult(prplcecheckResult);
				  }
			  }
			  if(prplcecheckResult.getPrplFraudrisks()!=null && prplcecheckResult.getPrplFraudrisks().size()>0){
				  for(PrplFraudrisk riskPo:prplcecheckResult.getPrplFraudrisks()){
					  riskPo.setPrplcecheckResult(prplcecheckResult);
				  }
			  }
			  if(prplcecheckResult.getPrplnoOperations()!=null && prplcecheckResult.getPrplnoOperations().size()>0){
				  for(PrplnoOperation operationPo:prplcecheckResult.getPrplnoOperations()){
					  operationPo.setPrplcecheckResult(prplcecheckResult);
				  }
			  }
			  
			  databaseDao.save(PrplcecheckResult.class,prplcecheckResult);
			}
		}
		
	}
	
	public List<PrplcecheckResultVo> findResultVoByRegistNo(String registNo) {
		List<PrplcecheckResultVo> prplcecheckResultVoList=null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("claimNotificationNo", registNo);
		queryRule.addAscOrder("createtime");
		List<PrplcecheckResult> prplcecheckResults=databaseDao.findAll(PrplcecheckResult.class, queryRule);
		if(prplcecheckResults!=null && prplcecheckResults.size()>0){
			prplcecheckResultVoList=new ArrayList<PrplcecheckResultVo>();
			prplcecheckResultVoList=Beans.copyDepth().from(prplcecheckResults).toList(PrplcecheckResultVo.class);
		}
		
		return prplcecheckResultVoList;
	}
	
	public PrplcecheckResultVo findResultVoById(Long id) {
		PrplcecheckResultVo prplcecheckResultVo=null;
		PrplcecheckResult prplcecheckResult=databaseDao.findByPK(PrplcecheckResult.class, id);
		if(prplcecheckResult!=null){
			prplcecheckResultVo=new PrplcecheckResultVo();
			prplcecheckResultVo=Beans.copyDepth().from(prplcecheckResult).to(PrplcecheckResultVo.class);
		}
		return prplcecheckResultVo;
	}

}
