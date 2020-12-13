package ins.sino.claimcar.realtimequery.service;

import ins.platform.vo.PrpLConfigValueVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;

public interface VehicleInfoQueryService {
	public abstract void sendVehicleInfoQuery(PrpLWfTaskVo wfTaskVo, PrpLConfigValueVo configValueVo);
	
	public abstract void sendPersonInfoQuery(String registNo,PrpLConfigValueVo configValueVo);
	
	public abstract void sendReportPhoneInfoQuery(String registNo,PrpLConfigValueVo configValueVo);
}
