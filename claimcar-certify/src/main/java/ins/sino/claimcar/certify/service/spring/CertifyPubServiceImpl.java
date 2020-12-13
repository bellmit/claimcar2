package ins.sino.claimcar.certify.service.spring;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.certify.po.PrpLCertifyDirect;
import ins.sino.claimcar.certify.po.PrpLCertifyItem;
import ins.sino.claimcar.certify.po.PrpLCertifyMain;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.certify.vo.PrpLCertifyDirectVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;

/**
 * 
 * @author Dengkk
 * @CreateTime Mar 9, 2016
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "certifyPubService")
public class CertifyPubServiceImpl implements CertifyPubService{

	@Autowired
	private DatabaseDao databaseDao;
	
	@Autowired
	private CertifyService certifyService;
	
	public Long submitCertify(PrpLCertifyMainVo prpLCertifyMainVo) {
		/**
		 * 提交主表的时候判断是否有已经勾选的单证信息，如果有和主表关联起来。起因是查勘未提交之前可以勾选单证，
		 * 那时候还没有生成单证任务
		 */
		PrpLCertifyMain prpLCertifyMain = new PrpLCertifyMain();
		String registNo = prpLCertifyMainVo.getRegistNo();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLCertifyItem> prpLCertifyItemList = databaseDao.findAll(PrpLCertifyItem.class,queryRule);
		prpLCertifyMain = Beans.copyDepth().from(prpLCertifyMainVo).to(PrpLCertifyMain.class);
		if(prpLCertifyItemList != null){
			for(PrpLCertifyItem prpLCertifyItem:prpLCertifyItemList){
				prpLCertifyItem.setPrpLCertifyMain(prpLCertifyMain);
			}
			prpLCertifyMain.setPrpLCertifyItems(prpLCertifyItemList);
		}
		Long id = (Long)databaseDao.save(PrpLCertifyMain.class,prpLCertifyMain);
		return id;
	}

	@Override
	public PrpLCertifyMainVo findPrpLCertifyMainVoByRegistNo(String registNo) {
		return certifyService.findPrpLCertifyMainVo(registNo);
	}

	@Override
	public List<PrpLCertifyDirectVo> findCertifyDirectByRegistNo(String registNo) {
		List<PrpLCertifyDirectVo> certifyDirectVoList = null;
		// TODO Auto-generated method stub
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLCertifyDirect> poList = databaseDao.findAll(PrpLCertifyDirect.class,queryRule);
		if (poList != null && !poList.isEmpty()) {
			certifyDirectVoList = new ArrayList<PrpLCertifyDirectVo>();
			for (PrpLCertifyDirect po : poList) {
				PrpLCertifyDirectVo vo = new PrpLCertifyDirectVo();
				Beans.copy().from(po).to(vo);
				certifyDirectVoList.add(vo);
			}
		}
		return certifyDirectVoList;
	}

}
