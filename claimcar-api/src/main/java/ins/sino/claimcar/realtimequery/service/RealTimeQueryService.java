package ins.sino.claimcar.realtimequery.service;

import java.util.List;

import ins.sino.claimcar.realtimequery.vo.PrpLAntiFraudVo;
import ins.sino.claimcar.realtimequery.vo.PrpLBasicsInfoVo;
import ins.sino.claimcar.realtimequery.vo.PrpLCasualtyInforVo;
import ins.sino.claimcar.realtimequery.vo.PrpLDamageInfoVo;
import ins.sino.claimcar.realtimequery.vo.PrpLInjuredDetailsVo;
import ins.sino.claimcar.realtimequery.vo.PrpLPropertyLossVo;
import ins.sino.claimcar.realtimequery.vo.PrpLRealTimeQueryVo;
import ins.sino.claimcar.realtimequery.vo.PrpLVehicleInfoVo;


public interface RealTimeQueryService {
	
	public abstract void saveBasicsInfo(List<PrpLBasicsInfoVo> prpLBasicsInfoVos);
	
	public abstract void savePrpLVehicleInfoVos(List<PrpLVehicleInfoVo> prpLVehicleInfoVos);
	
	public abstract void savePrpLPropertyLossVos(List<PrpLPropertyLossVo> prpLPropertyLossVos);
	
	public abstract PrpLRealTimeQueryVo savePrpLRealTimeQueryVo(PrpLRealTimeQueryVo prpLRealTimeQueryVo);
	
	public abstract void saveprpLAntiFraudVos(List<PrpLAntiFraudVo>  prpLAntiFraudVos);
	
	public abstract List<PrpLRealTimeQueryVo> findPrpLRealTimeQueryVo(String reportNo);
	
	public abstract List<PrpLVehicleInfoVo> findPrpLVehicleInfoVos(String reportNo,Long upperId);
	
	public abstract List<PrpLAntiFraudVo> findPrpLAntiFraudVos(String reportNo,Long upperId);
	
	public abstract List<PrpLBasicsInfoVo> findPrpLBasicsInfoVos(String reportNo,Long upperId);
	
	public abstract List<PrpLPropertyLossVo> findPrpLPropertyLossVos(String reportNo,Long upperId);
	
	public abstract void saveprpLCasualtyInforVos(List<PrpLCasualtyInforVo> prpLCasualtyInforVos);
	
	public abstract List<PrpLCasualtyInforVo> findPrpLCasualtyInforVos(String reportNo,Long upperId);
	
	public abstract List<PrpLInjuredDetailsVo> findPrpLInjuredDetailsVos(Long id);
	
	public abstract List<PrpLDamageInfoVo> findPrpLDamageInfoVos(Long id);
}
