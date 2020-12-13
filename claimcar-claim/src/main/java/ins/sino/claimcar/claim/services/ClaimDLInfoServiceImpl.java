package ins.sino.claimcar.claim.services;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import ins.sino.claimcar.claim.service.ClaimDLInfoService;
import ins.sino.claimcar.claim.vo.PrplcecheckResultVo;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout =600*1000)
@Path("claimDLInfoService")
public class ClaimDLInfoServiceImpl implements ClaimDLInfoService{
	@Autowired
	ClaimcedDlservice claimcedDlservice;
	
	@Override
	public void saveCeInfo(PrplcecheckResultVo prplcecheckResultVo) {
		claimcedDlservice.saveCeInfo(prplcecheckResultVo);
	}
	@Override
	public List<PrplcecheckResultVo> findResultVoByRegistNo(String registNo) {
		return claimcedDlservice.findResultVoByRegistNo(registNo);
	}
	@Override
	public PrplcecheckResultVo findResultVoById(Long id) {
		return claimcedDlservice.findResultVoById(id);
	}

}
