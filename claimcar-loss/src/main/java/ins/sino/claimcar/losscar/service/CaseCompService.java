package ins.sino.claimcar.losscar.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.losscar.po.PrpLCaseComponent;
import ins.sino.claimcar.losscar.vo.PrpLCaseComponentVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("caseCompService")
public class CaseCompService {
	
	@Autowired
	private DatabaseDao databaseDao;
	
	public List<PrpLCaseComponentVo> findCaseCompList(String compCode,String frameNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("compCode",compCode);
		queryRule.addEqual("frameNo",frameNo);
		List<PrpLCaseComponent> caseCompList = databaseDao.findAll(PrpLCaseComponent.class,queryRule);
		List<PrpLCaseComponentVo> caseCompVoList = new ArrayList<PrpLCaseComponentVo>();
		caseCompVoList = Beans.copyDepth().from(caseCompList).toList(PrpLCaseComponentVo.class);
		
		return caseCompVoList;
	}
	
	public Long countComp(String compCode,String frameNo){
		String sql =" from PrpLCaseComponent where compCode=? and frameNo=? ";
		Long size = databaseDao.getCount(sql,compCode,frameNo);
		
		return size;
	}
	
	public void saveCaseComp(PrpLCaseComponentVo caseCompVo){
		PrpLCaseComponent caseComp = Beans.copyDepth().from(caseCompVo).to(PrpLCaseComponent.class);
		
		databaseDao.save(PrpLCaseComponent.class,caseComp);
	}
	
	/**
	 * 核赔通过创建PrpLCaseComponent表
	 */
	public void createCaseComponent(List<PrpLCaseComponentVo> caseVoList) {
		if(caseVoList==null)return;
		for(PrpLCaseComponentVo caseVo : caseVoList){
			if(caseVo==null)continue;
			PrpLCaseComponent casePo = new PrpLCaseComponent();
			casePo = Beans.copyDepth().from(caseVo).to(PrpLCaseComponent.class);
			casePo.setCreateTime(new Date());
			databaseDao.save(PrpLCaseComponent.class,casePo);
		}
	}
}
