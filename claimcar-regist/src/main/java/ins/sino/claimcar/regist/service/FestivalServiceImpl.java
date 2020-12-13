package ins.sino.claimcar.regist.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.regist.po.PrpLfestival;
import ins.sino.claimcar.regist.vo.PrpLfestivalVo;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path(value = "festivalService")
public class FestivalServiceImpl implements FestivalService{
	private static Logger logger = LoggerFactory.getLogger(FestivalServiceImpl.class);
	@Autowired
	DatabaseDao databaseDao;
	@Override
	public PrpLfestivalVo findPrpLfestivalVoByFestivalType(String festivalType,String year) {
		QueryRule query=QueryRule.getInstance();
		query.addEqual("festivalType",festivalType);
		query.addEqual("year",year);
		PrpLfestivalVo prpLfestivalVo=null;
		List<PrpLfestival> poList=databaseDao.findAll(PrpLfestival.class, query);
		if(poList!=null && poList.size()>0){
			prpLfestivalVo=new PrpLfestivalVo();
			Beans.copy().from(poList.get(0)).to(prpLfestivalVo);
		}
		
		return prpLfestivalVo;
	}

}
