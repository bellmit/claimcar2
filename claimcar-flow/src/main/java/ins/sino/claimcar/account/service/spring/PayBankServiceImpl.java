package ins.sino.claimcar.account.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.sino.claimcar.account.po.PrpLPayBank;
import ins.sino.claimcar.account.po.PrpLPayBankHis;
import ins.sino.claimcar.other.service.PayBankService;
import ins.sino.claimcar.other.vo.PrpLPayBankHisVo;
import ins.sino.claimcar.other.vo.PrpLPayBankVo;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "payBankService")
public class PayBankServiceImpl implements PayBankService {

	@Autowired
	DatabaseDao databaseDao;

	@Override
	public PrpLPayBankVo findPayBankVoByPK(Long id) {
		PrpLPayBankVo prpLPayBankVo = new PrpLPayBankVo();
		if(id!=null){
			PrpLPayBank prpLPayBank = databaseDao.findByPK(PrpLPayBank.class,id);
			prpLPayBankVo = Beans.copyDepth().from(prpLPayBank).to(PrpLPayBankVo.class);
		}
		return prpLPayBankVo;

	}

	@Override
	public PrpLPayBankHisVo findPayBankHisVoByPK(Long id) {
		PrpLPayBankHisVo prpLPayBankHisVo = new PrpLPayBankHisVo();
		if(id!=null){
			PrpLPayBankHis prpLPayBankHis = databaseDao.findByPK(PrpLPayBankHis.class,id);
			prpLPayBankHisVo = Beans.copyDepth().from(prpLPayBankHis).to(PrpLPayBankHisVo.class);
		}
		return prpLPayBankHisVo;

	}

	@Override
	public void saveOrUpdatePayBank(PrpLPayBankHisVo prpLPayBankHisVo) {
		PrpLPayBank prpLPayBank = Beans.copyDepth().from(prpLPayBankHisVo).to(PrpLPayBank.class);
		databaseDao.save(PrpLPayBank.class,prpLPayBank);
		// return prpLPayBank;
	}

}
