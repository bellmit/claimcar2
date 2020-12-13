package ins.sino.claimcar.regist.web.action;

import ins.framework.web.AjaxResult;
import ins.platform.utils.ClaimBaseCoder;
import ins.sino.claimcar.mobilecheck.service.MobileCheckService;
import ins.sino.claimcar.mobilecheck.vo.FixedPositionBackReqVo;
import ins.sino.claimcar.mobilecheck.vo.FixedPositionReqBody;
import ins.sino.claimcar.mobilecheck.vo.FixedPositionReqVo;
import ins.sino.claimcar.mobilecheck.vo.HeadReq;
import ins.sino.claimcar.regist.web.utils.PositionMapUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author zy
 *
 */
@Controller
@RequestMapping("/fixedPosition")
public class FixedPositionAction {

	private static Logger logger = LoggerFactory.getLogger(FixedPositionAction.class);
	
	@Autowired
	private MobileCheckService mobileCheckService;
	
	public static final String HANDLSURVEYLOCATION_URL_METHOD = "prplschedule/handlSurveyLocation.do";
	public static final String REGISLOCATION_BACKURL_METHOD = "/fixedPosition/backMapValue.do";
	public static final String SCHEDLOCATION_BACKURL_METHOD = "/manualSchedule/backPositionMapValue.do";
	
	
	@RequestMapping(value = "/openQuickClaimMapBefore.ajax")
	@ResponseBody
	public AjaxResult openQuickClaimMapBefore(String item,String areaCode, 
			String address,String node,String provinceCode,String cityCode,HttpServletRequest request) throws Exception {
		String isShPolicy;
		
		if (StringUtils.equals(StringUtils.substring(areaCode, 0, 2), "31")) {
			isShPolicy = "0";
		} else {
			isShPolicy = "1";
		}
		
		String regionCode = areaCode;
		if(StringUtils.isBlank(address)){
			address = "";
		}
		address = address;
		logger.debug("address:"+address);
		
		FixedPositionReqVo vo = new FixedPositionReqVo();
		
		HeadReq head = new HeadReq();
		head.setRequestType("FixedPosition");
		
		FixedPositionReqBody body = new FixedPositionReqBody();
		body.setProvinceCode(provinceCode);
		body.setCityCode(cityCode);
		body.setRegionCode(regionCode);
		body.setDamageAddress(address);
		body.setIsSHPolicy(isShPolicy);
		
		vo.setHead(head);
		vo.setBody(body);
		String urlAtion =  SpringProperties.getProperty("MClaimPlatform_URL")+HANDLSURVEYLOCATION_URL_METHOD;
		String backUrlName = PositionMapUtils.getMapBackUrl(node);
		
		String serverPort = request.getServerPort()+"";
		String serverName = request.getServerName();
		String contextPath = request.getContextPath();
		String net_protocol = SpringProperties.getProperty("net_protocol");
		String backUrl = net_protocol + serverName + ":"+ serverPort + contextPath;
		logger.info("报案电子地图部分请求参数：serverName=" + serverName + ", serverPort= " + serverPort + "contextPath=" + contextPath + ", backUrl="+backUrl);
		if("REGISLOCATION_BACKURL".equals(backUrlName)){
			backUrl = backUrl+REGISLOCATION_BACKURL_METHOD;
		}
		if("SCHEDLOCATION_BACKURL".equals(backUrlName)){
			backUrl = backUrl+SCHEDLOCATION_BACKURL_METHOD;
		}
		String url = mobileCheckService.getPositionMapUrl(vo, item,backUrl,urlAtion);
		url = url.replaceAll("\"", "\'");
		logger.info("报案电子地图完整请求："+url);
		
		AjaxResult result = new AjaxResult();
		// lundy 2018-12-18 去掉回车换行符，解决谷歌浏览器无法打开电子地图的问题
		result.setData(url.replaceAll("[\r\n]",""));
		result.setStatus(HttpStatus.SC_OK);
		
		return result;
	}
	
	@RequestMapping(value = "/backMapValue.do", method = RequestMethod.GET)
	public ModelAndView backMapValue(String xml) throws UnsupportedEncodingException {
		URLDecoder urlDecoder = new URLDecoder();
		String returnXml[] = xml.split(";");
		logger.info(returnXml[0]+":"+returnXml[1]);
        xml = urlDecoder.decode(returnXml[1], "GBK");
        FixedPositionBackReqVo fixedPositionBackReqVo = ClaimBaseCoder.xmlToObj(xml, FixedPositionBackReqVo.class);
		ModelAndView mav = new ModelAndView();
		logger.info("报案返回==========="+xml);
		mav.addObject("res", fixedPositionBackReqVo.getBody());
		mav.addObject("item", returnXml[0]);
		mav.setViewName("regist/registEdit/ReportEdit_OpenQuickClaimMap");
		return mav;
	}
	
}
