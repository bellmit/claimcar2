package ins.sino.claimcar.longrise.gxService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.config.annotation.Service;

import ins.platform.utils.Base64EncodedUtil;
import ins.sino.claimcar.carinterface.service.CaseLeapGXService;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.carinterface.vo.GXCaseBean;
import ins.sino.claimcar.commom.vo.StringUtils;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("caseLeapGXService")
public class CaseLeapGXServiceImpl implements CaseLeapGXService {
	
	private Logger logger = LoggerFactory.getLogger(CaseLeapGXServiceImpl.class);
	
	@Autowired
    ClaimInterfaceLogService interfaceLogService;
	
	@Override
	public  void importCaseData(List<GXCaseBean> caseBeans,String url,String user,String pwd) throws ParseException {
		
		Map<String,GXCaseBean> beanMap = new HashMap<String,GXCaseBean>();
		Map<String,String> caseNoMap = new HashMap<String,String>();
		
		try{
			IleapProxy ileapProxy = new IleapProxy(url);
			user = Base64EncodedUtil.encode(StringUtils.rightTrim(user).getBytes());
			pwd = Base64EncodedUtil.encode(StringUtils.rightTrim(pwd).getBytes());
			
			for(GXCaseBean caseBean:caseBeans){
				String key = caseBean.getCaseno()+","+caseBean.getInssort();
				beanMap.put(key, caseBean);
				caseNoMap.put(key, caseBean.getEndorsementno());
				caseBean.setEndorsementno(null);
			}

			JSONArray jArray = JSONArray.fromObject(caseBeans);
			byte[] reBytes = ileapProxy.importCaseinfo(jArray.toString().getBytes("UTF-8"),user,pwd);
			logger.info("=======================广西消保返回信息："+new String(reBytes,"UTF-8"));
			
			if(reBytes != null){
				jArray = JSONArray.fromObject(new String(reBytes,"UTF-8"));
				List<GXCaseBean> resultBeans = JSONArray.toList(jArray, GXCaseBean.class);
				//失败的数据要替换原数据
				for(GXCaseBean resultBean:resultBeans){
					if(!"5".equals(resultBean.getIstatus())){
						String key = resultBean.getCaseno()+","+resultBean.getInssort();
						resultBean.setEndorsementno(caseNoMap.get(key));
						beanMap.put(key, resultBean);
					}
				}
			}else{
				for(GXCaseBean caseBean:caseBeans){
					caseBean.setIstatus("-3");
					caseBean.setResultdesc("平台没有返回信息");
					String key = caseBean.getCaseno()+","+caseBean.getInssort();
					beanMap.put(key, caseBean);
				}
			}
			
		}
		catch(Exception e){
			for(GXCaseBean caseBean:caseBeans){
				caseBean.setIstatus("-3");
				caseBean.setResultdesc(e.getMessage());
				String key = caseBean.getCaseno()+","+caseBean.getInssort();
				beanMap.put(key, caseBean);
			}
			e.printStackTrace();
			logger.info("==========广西消保定时任务报错："+e.getMessage());
		}
		finally{
			Set keys = beanMap.keySet( );
			Iterator iterator = keys.iterator( );
			while(iterator.hasNext( )) {
				Object key = iterator.next( );
				GXCaseBean  caseBean = beanMap.get(key);
				ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
				
				logVo.setRegistNo(caseBean.getCaseno());
				if("2".equals(caseBean.getPhase())){
					logVo.setBusinessType(BusinessType.GXIS_regist.name());
					logVo.setBusinessName(BusinessType.GXIS_regist.getName());
				}else{
					logVo.setBusinessType(BusinessType.GXIS_endCase.name());
					logVo.setBusinessName(BusinessType.GXIS_endCase.getName());
					logVo.setCompensateNo(caseBean.getEndorsementno());
				}
				logVo.setCreateTime(new Date());
				logVo.setCreateUser("AUTO");
				if(caseBean.getIstatus()==null){
					logVo.setStatus("1");
				}else{
					logVo.setStatus("0");
					logVo.setErrorCode(caseBean.getIstatus());
					logVo.setErrorMessage(caseBean.getResultdesc());
				}
				logVo.setRequestTime(new Date());
				logVo.setRequestUrl(url);
				caseBean.setEndorsementno(null);
				JSONObject resJson = JSONObject.fromObject(caseBean);
				logVo.setResponseXml(resJson.toString());
				caseBean.setIstatus(null);
				caseBean.setResultdesc(null);
				JSONObject reqJson = JSONObject.fromObject(caseBean);
				logVo.setRequestXml(reqJson.toString());
				
				interfaceLogService.save(logVo);
			}
			
		}
		
	}
	
	public  void main(String[] args) throws ParseException{
		List<GXCaseBean> caseBeans = new ArrayList<GXCaseBean>();
		String url = "http://59.173.241.186:21980/GXISTEST/services/com.longrise.services.leap";
		String user = "450000003065";
		String pwd = "111111";
		GXCaseBean caseBean1 = new GXCaseBean();
		GXCaseBean caseBean2 = new GXCaseBean();
		
		caseBean1.setPhase("2");
		caseBean1.setInssort("交强险");
		caseBean1.setIstatus("4");
		caseBean1.setName("报案人");
		caseBean1.setIspeoplehurt("否");
		caseBean1.setAccidenttime("2017-12-01");
		caseBean1.setStarttime("2017-12-1");
		caseBean1.setCaseno("4000298465846514653");
		caseBean1.setMobile("13726444498");
		caseBean1.setSex("男");
		caseBean1.setCarbrandno("豫AH123");
		caseBean1.setInstype("1");
		caseBean1.setPolicyno("20160615TEST8");
		caseBean1.setEffectivedate("2016-06-14");
		caseBean1.setCarframeno("LHGNC566422020002");
		caseBean1.setAreaid("广西壮族自治区南宁市");
		
		caseBean2.setPhase("2");
		caseBean2.setInssort("交强险");
		caseBean2.setIstatus("4");
		caseBean2.setName("报案人");
		caseBean2.setIspeoplehurt("否");
		caseBean2.setAccidenttime("2017-12-01");
		caseBean2.setStarttime("2017-12-1");
		caseBean2.setCaseno("4000298465846514654");
		caseBean2.setMobile("13726444498");
		caseBean2.setSex("男");
		caseBean2.setCarbrandno("豫AH124");
		caseBean2.setInstype("1");
		caseBean2.setPolicyno("20160615TEST9");
		caseBean2.setEffectivedate("2016-06-14");
		caseBean2.setCarframeno("LHGNC566422020002");
		caseBean2.setAreaid("广西壮族自治区南宁市");
		
		caseBeans.add(caseBean1);
		caseBeans.add(caseBean2);
		
		importCaseData(caseBeans,url,user,pwd);
		
		
		/*Map<String,GXCaseBean> beanMap = new HashMap<String,GXCaseBean>();
		GXCaseBean bean1 = new GXCaseBean();
		GXCaseBean bean2 = new GXCaseBean();
		bean1.setCaseno("1");
		bean2.setCaseno("2");
		beanMap.put("1", bean1);
		beanMap.put("2", bean2);
		Set keys = beanMap.keySet( );
		Iterator iterator = keys.iterator( );
		while(iterator.hasNext( )) {       
	        Object key = iterator.next( );       
	        GXCaseBean bean = beanMap.get(key);
	        System.out.println("=======================map:"+bean.getCaseno());
	    }*/
		
	}
	
}
