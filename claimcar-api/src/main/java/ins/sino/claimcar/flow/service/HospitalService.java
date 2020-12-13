package ins.sino.claimcar.flow.service;

import ins.framework.common.ResultPage;
import ins.platform.vo.PrpDHospitalVo;

public interface HospitalService {

	public abstract ResultPage<PrpDHospitalVo> find(
			PrpDHospitalVo prpDHospitalVo, int start, int length);

	public abstract PrpDHospitalVo findHospitalByCode(String hospitalCode);

	public abstract PrpDHospitalVo findHospitalById(long id);

	//更新
	public abstract void updateOrSaveHospital(PrpDHospitalVo prpDHospitalVo);

	//保存
	public abstract void save(PrpDHospitalVo prpDHospitalVo);

}