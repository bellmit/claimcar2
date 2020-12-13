package ins.sino.claimcar.jiangxicourt.service;

import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.jiangxicourt.vo.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" })
@Path(value = "jiangXiCourtQueryService")
public class JiangXiCourtQueryServiceImpl implements JiangXiCourtQueryService{

	private static Logger logger = LoggerFactory.getLogger(JiangXiCourtQueryServiceImpl.class);
	
	@Autowired
	private BaseDaoService baseDaoService; 
	
	@Override
	public List<Data> findByPolicyNo(String taskId, String policyNo){
		
		List<Object[]> obj = new ArrayList<Object[]>();
		List<Data> dataList = new ArrayList<Data>();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SqlJoinUtils sqlUtil = new SqlJoinUtils();
        sqlUtil.append(" select d.sumrealpay, to_char(d.paytime,'yyyy-MM-dd hh:mm:ss'), a.accountno, a.bankoutlets, t.caseno  ");
        sqlUtil.append("   from claimuser.prplcompensate t, claimuser.prplpayment d, claimuser.prplpaycustom a ");
        sqlUtil.append("  where d.payeeid = a.id(+) ");  //  CompensateType为N表示正常理算书
        sqlUtil.append("    and t.compensateno = d.compensateno(+) ");
        sqlUtil.append("    and t.compensatetype = 'N' ");
        sqlUtil.append("    and t.policyno = ? ");
        sqlUtil.addParamValue(policyNo);
        //执行查询
        obj = baseDaoService.getAllBySql(sqlUtil.getSql(), sqlUtil.getParamValues());
        logger.info("sql: " + sqlUtil.getSql() + "paramVaule: " + policyNo);
        if(obj != null && obj.size() > 0){
        	for(int i = 0;i < obj.size(); i++){
        		Data data = new Data();
        		Object[] ob = obj.get(i);
        		//组装数据
        		data.setFY_RWLSH(taskId);    //流水号	
        		data.setBX_BDH(policyNo);    //保单号
        		if(ob[4] != null){
        			data.setBX_LPQK_SFYJ(1);    //结案号非空表示已决
        		}else{
        			data.setBX_LPQK_SFYJ(2);    //结案为空表示未决
        		}        		
        		data.setBX_LPQK_PFJE((BigDecimal) ob[0]); //实赔金额       		       		  
    			if(ob[1] != null){
    				String date = (String) ob[1];
    			//	Date date = format.parse(dt); 
    				data.setBX_LPQK_PFSJ(date); //赔付时间
    			}								
        		data.setBX_LPQK_KHHZH((String) ob[2]);  //开户行账号
        		data.setBX_LPQK_KHYH((String) ob[3]);   //开户银行
        		data.setBX_LPQK_ZFFS("转账"); //支付方式
        		data.setBX_XTMC("claimcar"); //系统名称               
                dataList.add(data);
        	}        	
        }else{
        	//未查询到结果，就只返回保单号，流水号，理赔事项为 未决
			Data da = new Data();
			da.setFY_RWLSH(taskId);
			da.setBX_BDH(policyNo);			
			da.setBX_LPQK_SFYJ(2);
            dataList.add(da);
        }
		return dataList;
	}

	

}
