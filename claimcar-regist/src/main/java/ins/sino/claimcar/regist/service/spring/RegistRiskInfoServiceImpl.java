package ins.sino.claimcar.regist.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.sino.claimcar.regist.po.PrpLRegistRiskInfo;
import ins.sino.claimcar.regist.service.RegistRiskInfoService;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path("registRiskInfoService")
public class RegistRiskInfoServiceImpl implements RegistRiskInfoService {

	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	SubrogationService subrogationService;

	@Override
	public void writePrpLRegistRiskInfo(String registNo,String flag,String handler) {
		Date date = new Date();
		List<PrpLRegistRiskInfo> registRiskInfos = null;

		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo",registNo);
		qr.addEqual("factorcode","DWQC");
		registRiskInfos = databaseDao.findAll(PrpLRegistRiskInfo.class,qr);

		if(registRiskInfos != null && registRiskInfos.size() > 0){
			for(PrpLRegistRiskInfo riskInfo:registRiskInfos){
				riskInfo.setFactorvalue(flag);
				riskInfo.setUpdateTime(date);
				riskInfo.setUpdateUser(handler);
				databaseDao.update(PrpLRegistRiskInfo.class,riskInfo);
			}
		}

	}
	
	@Override
	public void writeRiskInfoByLossCar(String registNo,String handler){
		Date date = new Date();
		List<PrpLRegistRiskInfo> registRiskInfos = null;
		PrpLSubrogationMainVo subrogationMainVo = null;
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo",registNo);
		qr.addEqual("factorcode","DWQC");
		registRiskInfos = databaseDao.findAll(PrpLRegistRiskInfo.class,qr);
		subrogationMainVo = subrogationService.find(registNo);
		if(registRiskInfos != null && subrogationMainVo != null && subrogationMainVo.getSubrogationFlag() != null){
			for(PrpLRegistRiskInfo riskInfo:registRiskInfos){
				if(!subrogationMainVo.getSubrogationFlag().equals(riskInfo.getFactorvalue())){
					riskInfo.setFactorvalue(subrogationMainVo.getSubrogationFlag());
					riskInfo.setUpdateTime(date);
					riskInfo.setUpdateUser(handler);
					databaseDao.update(PrpLRegistRiskInfo.class,riskInfo);
				}
			}
		}
	}
}
