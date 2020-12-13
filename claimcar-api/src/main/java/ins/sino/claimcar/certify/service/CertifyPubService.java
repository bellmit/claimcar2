package ins.sino.claimcar.certify.service;

import java.util.List;

import ins.sino.claimcar.certify.vo.PrpLCertifyDirectVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;

/**
 * 
 * @author dengkk
 * @CreateTime Mar 9, 2016
 */
public interface CertifyPubService {
  
	public Long submitCertify(PrpLCertifyMainVo prpLCertifyMainVo);
	
	
	public PrpLCertifyMainVo findPrpLCertifyMainVoByRegistNo(String registNo);
	
	public List<PrpLCertifyDirectVo> findCertifyDirectByRegistNo(String registNo);
		
}
