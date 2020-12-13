package ins.sino.claimcar.platform.service.convertUtil;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.sino.claimcar.platform.service.CiCodeTranService;
import ins.sino.claimcar.platform.service.convertUtil.po.SysCicodeconvert;
import ins.sino.claimcar.platform.vo.transKindCodeVo.SysCicodeconvertVo;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" }, timeout = 1000000)
@Path("ciCodeTranService")
public class CiCodeTranServiceImpl implements CiCodeTranService {
    @Autowired
    DatabaseDao databaseDao;
    @Autowired
    BaseDaoService baseDaoService;
    @Override
    public String findTranCodeName(String codeType,String codeCode) {
        SysCicodeconvertVo sysCodeDictVo = null;
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("codeType", codeType);
        queryRule.addEqual("codeCode", codeCode);
        SysCicodeconvert sysCodeDict = databaseDao.findUnique(SysCicodeconvert.class, queryRule);
        if(sysCodeDict != null){
            sysCodeDictVo  = new SysCicodeconvertVo();
            Beans.copy().from(sysCodeDict).to(sysCodeDictVo);
            return sysCodeDictVo.getCiCodeName();
        }
        return null;
    }
    @Override
    public SysCicodeconvertVo findTranCodeDictVo(String codeType,String codeCode) {
        SysCicodeconvertVo sysCodeDictVo = null;
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("codeType", codeType);
        queryRule.addEqual("codeCode", codeCode);
        SysCicodeconvert sysCodeDict = databaseDao.findUnique(SysCicodeconvert.class, queryRule);
        if(sysCodeDict != null){
            sysCodeDictVo  = new SysCicodeconvertVo();
            Beans.copy().from(sysCodeDict).to(sysCodeDictVo);
        }
        return null;
    }
    @Override
    public String findTranCodeCode(String codeType,String codeCode) {
        SysCicodeconvertVo sysCodeDictVo = null;
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("codeType", codeType);
        queryRule.addEqual("codeCode", codeCode);
        System.out.println(queryRule.getQueryRuleList());
        SysCicodeconvert sysCodeDict = databaseDao.findUnique(SysCicodeconvert.class,queryRule);
        if(sysCodeDict != null){
            sysCodeDictVo  = new SysCicodeconvertVo();
            Beans.copy().from(sysCodeDict).to(sysCodeDictVo);
            return sysCodeDictVo.getCiCode();
        }
        return null;
    }
    
    public static void main(String args[]){
        CiCodeTranService impl = new CiCodeTranServiceImpl();
        System.out.println("========"+impl.findTranCodeCode("LossItemType","1"));
        System.out.println("========"+impl.findTranCodeCode("LossItemType","2"));
        System.out.println("========"+impl.findTranCodeCode("LossItemType","3"));
    }

}
