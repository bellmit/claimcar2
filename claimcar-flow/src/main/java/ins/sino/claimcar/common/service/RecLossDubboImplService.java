/******************************************************************************
* CREATETIME : 2016年9月22日 下午4:51:01
******************************************************************************/
package ins.sino.claimcar.common.service;

import ins.sino.claimcar.recloss.service.RecLossDubboService;
import ins.sino.claimcar.recloss.service.RecLossService;
import ins.sino.claimcar.recloss.vo.PrpLRecLossVo;

import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * @author ★XMSH
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("recLossDubboService")
public class RecLossDubboImplService implements RecLossDubboService {
	
	@Autowired
	private RecLossService recLossService;

	@Override
	public List<PrpLRecLossVo> findPrpLRecLossListByMainId(String recLossMainId) {
		// TODO Auto-generated method stub
		return recLossService.findPrpLRecLossListByMainId(recLossMainId);
	}

}
