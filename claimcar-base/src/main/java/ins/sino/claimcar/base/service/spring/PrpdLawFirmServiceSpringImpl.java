package ins.sino.claimcar.base.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.base.po.PrpdLawFirm;
import ins.sino.claimcar.claim.service.PrpdLawFirmService;
import ins.sino.claimcar.claim.vo.PrpdLawFirmVo;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * 统一分配方式实现类
 * @author zjd
 */
@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"})
@Path(value = "prpdLawFirmService")
public class PrpdLawFirmServiceSpringImpl implements PrpdLawFirmService {

	@Autowired
	private DatabaseDao databaseDao;

	@Override
	public List<PrpdLawFirmVo> findPrpdLawFirmVo(String comCode) {
		List<PrpdLawFirm> prpdLawFirmList = new ArrayList<PrpdLawFirm>();
		List<PrpdLawFirm> prpdLawFirmReSultList = new ArrayList<PrpdLawFirm>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addLike("comCode", comCode+"%");
		
		prpdLawFirmList = databaseDao.findAll(PrpdLawFirm.class, queryRule);
		if(!comCode.equals("0002")){
			for(PrpdLawFirm vo : prpdLawFirmList){
			 	if(!"0002".equals(vo.getComCode().substring(0, 4))){
			 		prpdLawFirmReSultList.add(vo);
			 	}
			}
			List<PrpdLawFirmVo> prpdLawFirmVos = Beans.copyDepth().from(prpdLawFirmReSultList).toList(PrpdLawFirmVo.class);
			return prpdLawFirmVos;
		}else{
			List<PrpdLawFirmVo> prpdLawFirmVos = Beans.copyDepth().from(prpdLawFirmList).toList(PrpdLawFirmVo.class);
			return prpdLawFirmVos;
		}
	
	}
	
}
