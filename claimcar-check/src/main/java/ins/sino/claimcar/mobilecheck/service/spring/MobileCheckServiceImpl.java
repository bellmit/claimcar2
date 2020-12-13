package ins.sino.claimcar.mobilecheck.service.spring;

import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.HttpClientHander;
import ins.sino.claimcar.mobilecheck.service.MobileCheckService;
import ins.sino.claimcar.mobilecheck.util.QuickClaimUtil;
import ins.sino.claimcar.mobilecheck.vo.FixedPositionReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleDOrGBackReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleDOrGReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleSDBackReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleSDReqVo;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationReqVo;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationResVo;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 移动查勘接口
 * @author dengkk
 *
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("mobileCheckService")
public class MobileCheckServiceImpl implements MobileCheckService {

	private static Logger logger = LoggerFactory.getLogger(MobileCheckServiceImpl.class);
	
	/* 
	 * @see ins.sino.claimcar.mobilecheck.service.MobileCheckService#getPersonnelInformation(ins.sino.claimcar.mobilecheck.vo.PersonnelInformationReqVo)
	 * @param reqVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public PersonnelInformationResVo getPersonnelInformation(PersonnelInformationReqVo reqVo,String url) throws Exception{
        
		String xmlToSend = ClaimBaseCoder.objToXmlUtf(reqVo);
		HttpClientHander httpClent = new HttpClientHander();
		
		//String url = PropertiesUtils.getProperties("AUTOSCHEDULE_URL");
		//url = url+"?xml=";
		
		logger.info("报案提交调度(查勘提交)send---------------------------"+xmlToSend);
		
		String xmlReturn = QuickClaimUtil.requestScheduledPlatform(xmlToSend, url);
		
		logger.info("报案提交调度(查勘提交)return---------------------------"+xmlReturn);
		
		PersonnelInformationResVo resVo = ClaimBaseCoder.xmlToObj(xmlReturn, PersonnelInformationResVo.class);
	    
		return resVo;
	}
	
	/* 
	 * @see ins.sino.claimcar.mobilecheck.service.MobileCheckService#getPositionMapUrl(ins.sino.claimcar.mobilecheck.vo.FixedPositionReqVo, java.lang.String, java.lang.String)
	 * @param fixedPositionReqVo
	 * @param item
	 * @param backUrlName
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getPositionMapUrl(FixedPositionReqVo fixedPositionReqVo,String item,String backUrl,String url)throws Exception{
		String xml = ClaimBaseCoder.objToXmlUtf(fixedPositionReqVo);
		//String backUrl = PropertiesUtils.getProperties(backUrlName);
		backUrl = backUrl+"?xml="+item+";";
		logger.info("报案地图请求返回backUrl"+backUrl);
		//String url = PropertiesUtils.getProperties("HANDLSURVEYLOCATION_URL");
		url = url+"?xml=" + xml + "&backUrl="+backUrl;
		logger.info("报案地图请求返回url"+url);
		return url;
	}
	
	/* 
	 * @see ins.sino.claimcar.mobilecheck.service.MobileCheckService#getHandelScheduleUrl(ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqVo, java.lang.String)
	 * @param handleScheduleReqVo
	 * @param backUrl
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getHandelScheduleUrl(HandleScheduleReqVo handleScheduleReqVo,String backUrl,String url)throws Exception{
		String xml = ClaimBaseCoder.objToXmlUtf(handleScheduleReqVo);

		backUrl = backUrl + "?xml=";
		logger.info("调度地图请求返回backUrl：" + backUrl);

		url = url + "?xml=" + xml + "&backUrl=" + backUrl;
		logger.info("调度地图请求返回url：" + url);
		return url;
	}
	
	
	/* 
	 * @see ins.sino.claimcar.mobilecheck.service.MobileCheckService#getHandelScheduleSDUrl(ins.sino.claimcar.mobilecheck.vo.HandleScheduleSDReqVo)
	 * @param handleScheduleReqVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public HandleScheduleSDBackReqVo getHandelScheduleSDUrl(HandleScheduleSDReqVo handleScheduleReqVo,String url)throws Exception{
      /*  String xml = ClaimBaseCoder.objToXml(handleScheduleReqVo);
		
		backUrl = backUrl + "?xml=";
		
		String url = PropertiesUtils.getProperties("AUTOSCHEDULE_URL");
				
		//url = url+"?xml=" + xml + "&backUrl="+backUrl;
		url = url+"?xml=" + xml;
		System.out.println(url+"ppppppppp");
		return url;*/
		
		String xmlToSend = ClaimBaseCoder.objToXmlUtf(handleScheduleReqVo);
		
		//String url = PropertiesUtils.getProperties("AUTOSCHEDULE_URL");
		//url = url+"?xml=";
		
		logger.info("调度是否自定义复选框send---------------------------"+xmlToSend);
		
		String xmlReturn = QuickClaimUtil.requestScheduledPlatform(xmlToSend, url);
		
		logger.info("调度是否自定义复选框return---------------------------"+xmlReturn);
		
		HandleScheduleSDBackReqVo resVo = ClaimBaseCoder.xmlToObj(xmlReturn, HandleScheduleSDBackReqVo.class);
	    
		return resVo;
	}
	
	
	/* 
	 * @see ins.sino.claimcar.mobilecheck.service.MobileCheckService#getHandelScheduleDOrDUrl(ins.sino.claimcar.mobilecheck.vo.HandleScheduleDOrGReqVo)
	 * @param handleScheduleReqVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public HandleScheduleDOrGBackReqVo getHandelScheduleDOrDUrl(HandleScheduleDOrGReqVo handleScheduleReqVo,String url)throws Exception{

		String xmlToSend = ClaimBaseCoder.objToXmlUtf(handleScheduleReqVo);
		
		//String url = PropertiesUtils.getProperties("HANDLSCHEDDORGULE_URL");
		//url = url+"?xml=";
		
		logger.info("调度改派提交处理(平级移交)send---------------------------"+xmlToSend);
		
		String xmlReturn = QuickClaimUtil.requestScheduledPlatform(xmlToSend, url);
		
		logger.info("调度改派提交处理(平级移交)return---------------------------"+xmlReturn);
		
		HandleScheduleDOrGBackReqVo resVo = ClaimBaseCoder.xmlToObj(xmlReturn, HandleScheduleDOrGBackReqVo.class);
	    
		return resVo;
	}
}
