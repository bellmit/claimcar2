package ins.sino.claimcar.check.service;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.carplatform.po.ClaimVinNoHis;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;

import java.text.ParseException;

public interface UpdateVINService {

	public ResultPage<PrpLWfTaskQueryVo> search(PrpLWfTaskQueryVo prplWfTaskQueryVo,int start,int length) throws ParseException;
	
	public void saveClaimvinnohis(ClaimVinNoHis claimvinNoHis);
}
