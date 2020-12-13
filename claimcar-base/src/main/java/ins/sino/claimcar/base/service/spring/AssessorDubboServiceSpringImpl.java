/******************************************************************************
* CREATETIME : 2016年8月16日 下午5:40:32
******************************************************************************/
package ins.sino.claimcar.base.service.spring;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.other.service.AssessorDubboService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAssessorVo;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * @author ★XMSH
 */
@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"}, timeout=100000)
@Path(value = "assessorDubboService")
public class AssessorDubboServiceSpringImpl implements AssessorDubboService {

	@Autowired
	private AssessorService assessorService;
	
	/* 
	 * @see ins.sino.claimcar.base.service.AssessorDubboService#saveOrUpdatePrpLAssessor(ins.sino.claimcar.base.vo.PrpLAssessorVo)
	 * @param assessorVo
	 */
	@Override
	public void saveOrUpdatePrpLAssessor(PrpLAssessorVo assessorVo,SysUserVo userVo) {
		// TODO Auto-generated method stub
		assessorService.saveOrUpdatePrpLAssessor(assessorVo,userVo);
	}

	/* 
	 * @see ins.sino.claimcar.base.service.AssessorDubboService#findAssessorByLossId(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String)
	 * @param registNo
	 * @param taskType
	 * @param serialNo
	 * @param intermcode
	 * @return
	 */
	@Override
	public PrpLAssessorVo findAssessorByLossId(String registNo,String taskType,Integer serialNo,String intermcode) {
		// TODO Auto-generated method stub
		return assessorService.findAssessorByLossId(registNo,taskType,serialNo,intermcode);
	}

	@Override
	public PrpLAssessorVo findAssessorByLossId(String registNo,String taskType,String intermcode) {
		// TODO Auto-generated method stub
		return assessorService.findAssessorByLossId(registNo,taskType,intermcode);
	}
	
}
