package ins.sino.claimcar.endcase.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.endcase.po.PrpLReCase;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;
import ins.framework.dao.database.support.Page;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "reOpenService")
public class ReOpenService {
	
	@Autowired
	DatabaseDao databaseDao;
	
	public ResultPage<PrpLReCaseVo> searchProcessed(PrpLEndCaseVo endCaseVo,PrpLWfTaskQueryVo wftaskqueryvo,
			String handleStatus,int start, int length){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLReCase r where 1=1 ");
		if(StringUtils.isNotBlank(endCaseVo.getRegistNo())){
			sqlUtil.append(" and r.registNo like ?");
			sqlUtil.addParamValue("%"+endCaseVo.getRegistNo()  );
		}
		if(StringUtils.isNotBlank(endCaseVo.getClaimNo())){
			sqlUtil.append(" and r.claimNo like ?");
			sqlUtil.addParamValue("%"+endCaseVo.getClaimNo()  );
		}
		if(StringUtils.isNotBlank(wftaskqueryvo.getInsuredName())){
			sqlUtil.append(" and r.insuredName like ?");
			sqlUtil.addParamValue("%"+wftaskqueryvo.getInsuredName()+"%");
		}
		if(StringUtils.isNotBlank(wftaskqueryvo.getMercyFlag())){
			sqlUtil.append(" and r.insuredName = ?");
			sqlUtil.addParamValue(wftaskqueryvo.getMercyFlag());
		}
		if(wftaskqueryvo.getTaskInTimeStart() !=null &&
				wftaskqueryvo.getTaskInTimeEnd() != null ){
			sqlUtil.append(" and r.openCaseDate >= ? and r.openCaseDate <= ?");
			sqlUtil.addParamValue(wftaskqueryvo.getTaskInTimeStart());
			sqlUtil.addParamValue(DateUtils.toDateEnd(wftaskqueryvo.getTaskInTimeEnd()));
		}
		if(handleStatus.equals("3")){
			sqlUtil.append(" and r.checkStatus >3 ");
		}
		sqlUtil.append("Order By r.openCaseDate desc");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		List<PrpLReCaseVo> reCaseVoList = new ArrayList<PrpLReCaseVo>();
		for(int i=0;i<page.getResult().size();i++){
			PrpLReCaseVo prpLReCaseVo = new PrpLReCaseVo();
			Object obj = page.getResult().get(i);
			prpLReCaseVo=Beans.copyDepth().from(obj).to(PrpLReCaseVo.class);
			reCaseVoList.add(prpLReCaseVo);
		}
		ResultPage<PrpLReCaseVo> resultPage = new ResultPage<PrpLReCaseVo>(start, length, page.getTotalCount(), reCaseVoList);
		return resultPage;
	}
}
