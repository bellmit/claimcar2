package ins.sino.claimcar.hnbxrest.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.hnbxrest.po.PrplQuickCaseCar;
import ins.sino.claimcar.hnbxrest.po.PrplQuickCaseInfor;

import ins.sino.claimcar.hnbxrest.service.SubmitcaseinforService;
import ins.sino.claimcar.hnbxrest.vo.PrplQuickCaseInforVo;

import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;



@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "submitcaseinforService")
public class SubmitcaseinforServiceImpl implements SubmitcaseinforService{

    @Autowired
    private DatabaseDao databaseDao;
    
    @Override
    public void save(PrplQuickCaseInforVo prplQuickCaseInforVo)
    {   
        PrplQuickCaseInfor prplQuickCaseInfor = new PrplQuickCaseInfor();
        prplQuickCaseInfor = Beans.copyDepth().from(prplQuickCaseInforVo).to(PrplQuickCaseInfor.class);
        if(prplQuickCaseInfor.getCasecarlist()!=null&&prplQuickCaseInfor.getCasecarlist().size()>0){
            for(PrplQuickCaseCar prplQuickCaseCar:prplQuickCaseInfor.getCasecarlist()){
                prplQuickCaseCar.setPrplquickcaseinfor(prplQuickCaseInfor);
            }
            
        }
        databaseDao.save(PrplQuickCaseInfor.class,prplQuickCaseInfor);
    }

    @Override
    public PrplQuickCaseInforVo findByCasenumber(String casenumber) {
        PrplQuickCaseInforVo prplQuickCaseInforVo = null;
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("casenumber",casenumber);
        PrplQuickCaseInfor prplQuickCaseInfor = databaseDao.findUnique(PrplQuickCaseInfor.class,queryRule);
        if(prplQuickCaseInfor!=null){
            prplQuickCaseInforVo = new PrplQuickCaseInforVo();
            prplQuickCaseInforVo = Beans.copyDepth().from(prplQuickCaseInfor).to(PrplQuickCaseInforVo.class);
        }
        return prplQuickCaseInforVo;
    }
    
    @Override
    public PrplQuickCaseInforVo findByRegistNo(String registNo){
    	PrplQuickCaseInforVo prplQuickCaseInforVo = null;
    	 QueryRule queryRule = QueryRule.getInstance();
         queryRule.addEqual("registno",registNo);
         List<PrplQuickCaseInfor> prplQuickCaseInforList = databaseDao.findAll(PrplQuickCaseInfor.class,queryRule);
         if(prplQuickCaseInforList!=null && prplQuickCaseInforList.size()>0){
             prplQuickCaseInforVo = new PrplQuickCaseInforVo();
             prplQuickCaseInforVo = Beans.copyDepth().from(prplQuickCaseInforList.get(0)).to(PrplQuickCaseInforVo.class);
//             Beans.copy().from(prplQuickCaseInfor).to(prplQuickCaseInforVo);
         }
         return prplQuickCaseInforVo;
    }

	

}
