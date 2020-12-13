package ins.sino.claimcar.other.service;


import ins.sino.claimcar.other.vo.PrpLPayBankHisVo;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;

public interface PayBankService {

	public abstract PrpLPayBankVo findPayBankVoByPK(Long id);

	public abstract PrpLPayBankHisVo findPayBankHisVoByPK(Long id);

	public abstract void saveOrUpdatePayBank(
			PrpLPayBankHisVo prpLPayBankHisVo);

}