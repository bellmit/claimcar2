package ins.sino.claimcar.claim.service;

import ins.platform.vo.PrpLLawSuitVo;

import java.util.List;

public interface LawSiutService {

	public abstract void save(PrpLLawSuitVo lawSuitVo);

	public abstract List<PrpLLawSuitVo> findByRegistNo(String RegistNo);

	//更新
	public abstract void updateLawSuit(PrpLLawSuitVo lawSuitVo);

	//删除

	public abstract void deleteLawSuit(PrpLLawSuitVo lawSuitVo);
	
	public abstract void pinganSave(PrpLLawSuitVo lawSuitVo);

}
