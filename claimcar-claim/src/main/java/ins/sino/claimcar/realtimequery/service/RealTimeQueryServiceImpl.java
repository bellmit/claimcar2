package ins.sino.claimcar.realtimequery.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.realtimequery.po.PrpLAntiFraud;
import ins.sino.claimcar.realtimequery.po.PrpLBasicsInfo;
import ins.sino.claimcar.realtimequery.po.PrpLCasualtyInfor;
import ins.sino.claimcar.realtimequery.po.PrpLDamageInfo;
import ins.sino.claimcar.realtimequery.po.PrpLInjuredDetails;
import ins.sino.claimcar.realtimequery.po.PrpLPropertyLoss;
import ins.sino.claimcar.realtimequery.po.PrpLRealTimeQuery;
import ins.sino.claimcar.realtimequery.po.PrpLVehicleInfo;
import ins.sino.claimcar.realtimequery.vo.PrpLAntiFraudVo;
import ins.sino.claimcar.realtimequery.vo.PrpLBasicsInfoVo;
import ins.sino.claimcar.realtimequery.vo.PrpLCasualtyInforVo;
import ins.sino.claimcar.realtimequery.vo.PrpLDamageInfoVo;
import ins.sino.claimcar.realtimequery.vo.PrpLInjuredDetailsVo;
import ins.sino.claimcar.realtimequery.vo.PrpLPropertyLossVo;
import ins.sino.claimcar.realtimequery.vo.PrpLRealTimeQueryVo;
import ins.sino.claimcar.realtimequery.vo.PrpLVehicleInfoVo;
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("realTimeQueryService")
public class RealTimeQueryServiceImpl implements RealTimeQueryService{
	private Logger logger = LoggerFactory.getLogger(RealTimeQueryServiceImpl.class);
	@Autowired
	private DatabaseDao databaseDao;
	
	@Override
	public PrpLRealTimeQueryVo savePrpLRealTimeQueryVo(PrpLRealTimeQueryVo prpLRealTimeQueryVo) {
		PrpLRealTimeQuery po = new PrpLRealTimeQuery();
		Beans.copy().from(prpLRealTimeQueryVo).to(po);
		databaseDao.save(PrpLRealTimeQuery.class,po);
		
		logger.debug("saveTaskIn.po.id="+po.getId());
		
		prpLRealTimeQueryVo.setId(po.getId());
		return prpLRealTimeQueryVo;
	}

	@Override
	public void saveprpLAntiFraudVos(List<PrpLAntiFraudVo> prpLAntiFraudVos) {
		List<PrpLAntiFraud> prpLAntiFraudList = new ArrayList<PrpLAntiFraud>();
		prpLAntiFraudList = Beans.copyDepth().from(prpLAntiFraudVos).toList(PrpLAntiFraud.class);
		if(prpLAntiFraudList != null && prpLAntiFraudList.size() > 0){
			for(PrpLAntiFraud prpLAntiFraud : prpLAntiFraudList){
				databaseDao.save(PrpLAntiFraud.class,prpLAntiFraud);
			}
		}
	}

	@Override
	public void saveBasicsInfo(List<PrpLBasicsInfoVo> prpLBasicsInfoVos) {
		List<PrpLBasicsInfo> prpLBasicsInfoList = new ArrayList<PrpLBasicsInfo>();
		prpLBasicsInfoList = Beans.copyDepth().from(prpLBasicsInfoVos).toList(PrpLBasicsInfo.class);
		if(prpLBasicsInfoList != null && prpLBasicsInfoList.size() > 0){
			for(PrpLBasicsInfo prpLBasicsInfo : prpLBasicsInfoList){
				databaseDao.save(PrpLBasicsInfo.class,prpLBasicsInfo);
			}
		}
	}

	@Override
	public void savePrpLVehicleInfoVos(List<PrpLVehicleInfoVo> prpLVehicleInfoVos) {
		List<PrpLVehicleInfo> prpLVehicleInfoList = new ArrayList<PrpLVehicleInfo>();
		prpLVehicleInfoList = Beans.copyDepth().from(prpLVehicleInfoVos).toList(PrpLVehicleInfo.class);
		if(prpLVehicleInfoList != null && prpLVehicleInfoList.size() > 0){
			for(PrpLVehicleInfo prpLVehicleInfo : prpLVehicleInfoList){
				databaseDao.save(PrpLVehicleInfo.class,prpLVehicleInfo);
			}
		}
	}

	@Override
	public void savePrpLPropertyLossVos(List<PrpLPropertyLossVo> prpLPropertyLossVos) {
		List<PrpLPropertyLoss> prpLPropertyLossList = new ArrayList<PrpLPropertyLoss>();
		prpLPropertyLossList = Beans.copyDepth().from(prpLPropertyLossVos).toList(PrpLPropertyLoss.class);
		if(prpLPropertyLossList != null && prpLPropertyLossList.size() > 0){
			for(PrpLPropertyLoss prpLPropertyLoss : prpLPropertyLossList){
				databaseDao.save(PrpLPropertyLoss.class,prpLPropertyLoss);
			}
		}
	}

	@Override
	public List<PrpLRealTimeQueryVo> findPrpLRealTimeQueryVo(String reportNo) {
		List<PrpLRealTimeQueryVo> realTimeQueryVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("reportNo",reportNo);
		List<PrpLRealTimeQuery> prpLRealTimeQueryList = databaseDao.findAll(PrpLRealTimeQuery.class,queryRule);
		if(prpLRealTimeQueryList != null && prpLRealTimeQueryList.size() > 0){
			realTimeQueryVoList = Beans.copyDepth().from(prpLRealTimeQueryList).toList(PrpLRealTimeQueryVo.class);
		}
		return realTimeQueryVoList;
	}

	@Override
	public List<PrpLVehicleInfoVo> findPrpLVehicleInfoVos(String reportNo,
			Long upperId) {
		List<PrpLVehicleInfoVo> prpLVehicleInfoVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		//queryRule.addEqual("reportNo",reportNo);
		queryRule.addEqual("upperId",upperId);
		List<PrpLVehicleInfo> prpLVehicleInfoList = databaseDao.findAll(PrpLVehicleInfo.class,queryRule);
		if(prpLVehicleInfoList != null && prpLVehicleInfoList.size() > 0){
			prpLVehicleInfoVoList = Beans.copyDepth().from(prpLVehicleInfoList).toList(PrpLVehicleInfoVo.class);
		}
		return prpLVehicleInfoVoList;
	}

	@Override
	public List<PrpLAntiFraudVo> findPrpLAntiFraudVos(String reportNo,
			Long upperId) {
		List<PrpLAntiFraudVo> prpLAntiFraudVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		//queryRule.addEqual("reportNo",reportNo);
		queryRule.addEqual("upperId",upperId);
		List<PrpLAntiFraud> prpLAntiFraudList = databaseDao.findAll(PrpLAntiFraud.class,queryRule);
		if(prpLAntiFraudList != null && prpLAntiFraudList.size() > 0){
			prpLAntiFraudVoList = Beans.copyDepth().from(prpLAntiFraudList).toList(PrpLAntiFraudVo.class);
		}
		return prpLAntiFraudVoList;
	}

	@Override
	public List<PrpLBasicsInfoVo> findPrpLBasicsInfoVos(String reportNo,
			Long upperId) {
		List<PrpLBasicsInfoVo> prpLBasicsInfoVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		//queryRule.addEqual("reportNo",reportNo);
		queryRule.addEqual("upperId",upperId);
		List<PrpLBasicsInfo> prpLBasicsInfoList = databaseDao.findAll(PrpLBasicsInfo.class,queryRule);
		if(prpLBasicsInfoList != null && prpLBasicsInfoList.size() > 0){
			prpLBasicsInfoVoList = Beans.copyDepth().from(prpLBasicsInfoList).toList(PrpLBasicsInfoVo.class);
		}
		return prpLBasicsInfoVoList;
	}

	@Override
	public List<PrpLPropertyLossVo> findPrpLPropertyLossVos(String reportNo,
			Long upperId) {
		List<PrpLPropertyLossVo> prpLPropertyLossVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		//queryRule.addEqual("reportNo",reportNo);
		queryRule.addEqual("upperId",upperId);
		List<PrpLPropertyLoss> prpLPropertyLossList = databaseDao.findAll(PrpLPropertyLoss.class,queryRule);
		if(prpLPropertyLossList != null && prpLPropertyLossList.size() > 0){
			prpLPropertyLossVoList = Beans.copyDepth().from(prpLPropertyLossList).toList(PrpLPropertyLossVo.class);
		}
		return prpLPropertyLossVoList;
	}

	@Override
	public void saveprpLCasualtyInforVos(
			List<PrpLCasualtyInforVo> prpLCasualtyInforVos) {
		List<PrpLCasualtyInfor> prpLCasualtyInforList = new ArrayList<PrpLCasualtyInfor>();
		for (PrpLCasualtyInforVo prpLCasualtyInforVo : prpLCasualtyInforVos) {
			PrpLCasualtyInfor PrpLCasualtyInfor = new PrpLCasualtyInfor();
			PrpLCasualtyInfor.setCsCertiCode(prpLCasualtyInforVo.getCsCertiCode());
			PrpLCasualtyInfor.setCsCertiType(prpLCasualtyInforVo.getCsCertiType());
			PrpLCasualtyInfor.setCsMedicalType(prpLCasualtyInforVo.getCsMedicalType());
			PrpLCasualtyInfor.setId(prpLCasualtyInforVo.getId());
			PrpLCasualtyInfor.setMedicalType(prpLCasualtyInforVo.getMedicalType());
			PrpLCasualtyInfor.setPersonName(prpLCasualtyInforVo.getPersonName());
			PrpLCasualtyInfor.setPersonProperty(prpLCasualtyInforVo.getPersonProperty());
			PrpLCasualtyInfor.setUpperId(prpLCasualtyInforVo.getUpperId());
			List<PrpLInjuredDetails> prpLInjuredDetailsList = null;
			if(prpLCasualtyInforVo.getPrpLInjuredDetailsVo() != null && prpLCasualtyInforVo.getPrpLInjuredDetailsVo().size() > 0){
				prpLInjuredDetailsList = new ArrayList<PrpLInjuredDetails>();
				PrpLInjuredDetails prpLInjuredDetails = new PrpLInjuredDetails();
				for (PrpLInjuredDetailsVo prpLInjuredDetailsVo : prpLCasualtyInforVo.getPrpLInjuredDetailsVo()) {
					prpLInjuredDetails.setDeathTime(prpLInjuredDetailsVo.getDeathTime());
					prpLInjuredDetails.setId(prpLInjuredDetailsVo.getId());
					prpLInjuredDetails.setInjuryLevelCode(prpLInjuredDetailsVo.getInjuryLevelCode());
					prpLInjuredDetails.setUpperId(prpLInjuredDetailsVo.getUpperId());
					prpLInjuredDetails.setInjuryPart(prpLInjuredDetailsVo.getInjuryPart());
					prpLInjuredDetailsList.add(prpLInjuredDetails);
				}
			}
			PrpLCasualtyInfor.setPrpLInjuredDetailsList(prpLInjuredDetailsList);
			prpLCasualtyInforList.add(PrpLCasualtyInfor);
		}
		if(prpLCasualtyInforList != null && prpLCasualtyInforList.size() > 0){
			for(PrpLCasualtyInfor prpLCasualtyInfor : prpLCasualtyInforList){
				databaseDao.save(PrpLVehicleInfo.class,prpLCasualtyInfor);
			}
		}
	}

	@Override
	public List<PrpLCasualtyInforVo> findPrpLCasualtyInforVos(String reportNo,
			Long upperId) {
		List<PrpLCasualtyInforVo> prpLCasualtyInforVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		//queryRule.addEqual("reportNo",reportNo);
		queryRule.addEqual("upperId",upperId);
		List<PrpLCasualtyInfor> prpLCasualtyInforList = databaseDao.findAll(PrpLCasualtyInfor.class,queryRule);
		if(prpLCasualtyInforList != null && prpLCasualtyInforList.size() > 0){
			prpLCasualtyInforVoList = Beans.copyDepth().from(prpLCasualtyInforList).toList(PrpLCasualtyInforVo.class);
		}
		return prpLCasualtyInforVoList;
	}

	@Override
	public List<PrpLInjuredDetailsVo> findPrpLInjuredDetailsVos(Long upperId) {
		List<PrpLInjuredDetailsVo> prpLInjuredDetailsVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
 		queryRule.addEqual("upperId",upperId);
		List<PrpLInjuredDetails> prpLInjuredDetailsList = databaseDao.findAll(PrpLInjuredDetails.class,queryRule);
		if(prpLInjuredDetailsList != null && prpLInjuredDetailsList.size() > 0){
			prpLInjuredDetailsVoList = Beans.copyDepth().from(prpLInjuredDetailsList).toList(PrpLInjuredDetailsVo.class);
		}
		return prpLInjuredDetailsVoList;
	}

	@Override
	public List<PrpLDamageInfoVo> findPrpLDamageInfoVos(Long id) {
		List<PrpLDamageInfoVo> prpLInjuredDetailsVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
 		queryRule.addEqual("upperId",id);
		List<PrpLDamageInfo> prpLDamageInfoVoList = databaseDao.findAll(PrpLDamageInfo.class,queryRule);
		if(prpLDamageInfoVoList != null && prpLDamageInfoVoList.size() > 0){
			prpLInjuredDetailsVoList = Beans.copyDepth().from(prpLDamageInfoVoList).toList(PrpLDamageInfoVo.class);
		}
		return prpLInjuredDetailsVoList;
	}

	
}
