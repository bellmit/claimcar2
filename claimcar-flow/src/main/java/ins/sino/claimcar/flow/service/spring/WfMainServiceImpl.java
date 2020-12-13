/******************************************************************************
* CREATETIME : 2016年1月9日 上午10:04:37
******************************************************************************/
package ins.sino.claimcar.flow.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.CodeConstants.FlowStatus;
import ins.sino.claimcar.flow.po.PrpLWfMain;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年1月9日
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "wfMainService")
public class WfMainServiceImpl implements WfMainService {

	// private static Logger logger = LoggerFactory.getLogger(WfMainServiceImpl.class);

	@Autowired
	private DatabaseDao databaseDao;

	/* 
	 * @see ins.sino.claimcar.flow.service.WfMainService#save(ins.sino.claimcar.flow.vo.PrpLWfMainVo)
	 * @param wfMainVo
	 */
	@Override
	public void save(PrpLWfMainVo wfMainVo) {
		PrpLWfMain po=new PrpLWfMain();
		Beans.copy().from(wfMainVo).to(po);
		po.setCreateTime(new Date());
		po.setUpdateTime(new Date());
		databaseDao.save(PrpLWfMain.class,po);
	}

	/* 
	 * @see ins.sino.claimcar.flow.service.WfMainService#update(ins.sino.claimcar.flow.vo.PrpLWfMainVo)
	 * @param wfMainVo
	 */
	@Override
	public void update(PrpLWfMainVo wfMainVo) {
		PrpLWfMain po = databaseDao.findByPK(PrpLWfMain.class,wfMainVo.getFlowId());
		Beans.copy().from(wfMainVo).excludeNull().excludeEmpty().to(po);
		po.setPolicyNoLink(wfMainVo.getPolicyNoLink());
		po.setUpdateTime(new Date());
		databaseDao.update(PrpLWfMain.class,po);
	}
	
	/* 
	 * @see ins.sino.claimcar.flow.service.WfMainService#findPrpLWfMainVoByFlowId(java.lang.String)
	 * @param flowId
	 * @return
	 */
	@Override
	public PrpLWfMainVo findPrpLWfMainVoByFlowId(String flowId){
		PrpLWfMainVo prpLWfMainVo = null;
		PrpLWfMain po = databaseDao.findByPK(PrpLWfMain.class,flowId);
		if(po != null){
			prpLWfMainVo = new PrpLWfMainVo();
			Beans.copy().from(po).to(prpLWfMainVo);
		}
		return prpLWfMainVo;
	}

    @Override
    public PrpLWfMainVo findPrpLWfMainVoByRegistNo(String registNo) {
        PrpLWfMainVo prpLWfMainVo = null;
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        List<PrpLWfMain> poList = databaseDao.findAll(PrpLWfMain.class,queryRule);
        if(poList != null && poList.size() ==1){
            prpLWfMainVo = new PrpLWfMainVo();
            Beans.copy().from(poList.get(0)).to(prpLWfMainVo);
        }
        return prpLWfMainVo;
    }

    @Override
    public PrpLWfMainVo findPrpLWfMainVoByLicenseNo(String licenseNo) {
        PrpLWfMainVo prpLWfMainVo = null;
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("licenseNo",licenseNo);
        queryRule.addEqual("flowStatus",FlowStatus.NORMAL);
        List<PrpLWfMain> poList = databaseDao.findAll(PrpLWfMain.class,queryRule);
        if(poList != null && poList.size() > 0){
            prpLWfMainVo = new PrpLWfMainVo();
            Beans.copy().from(poList.get(0)).to(prpLWfMainVo);
        }
        return prpLWfMainVo;
    }
		
}
