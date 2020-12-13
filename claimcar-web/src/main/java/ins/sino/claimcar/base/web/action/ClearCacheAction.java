package ins.sino.claimcar.base.web.action;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import ins.framework.web.AjaxResult;
import ins.platform.common.service.facade.CodeDictService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.common.util.AreaSelectUtil;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.ConfigUtil;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.sino.claimcar.addvaluetopolicy.vo.AddValueResponse;
import ins.sino.claimcar.base.web.action.vo.ClearCacheVo;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.PayCustomService;

import ins.sino.claimcar.web.service.ClearWebCacheService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.minlog.Log;
@Controller
@RequestMapping("/clearCache")
public class ClearCacheAction {
	@Autowired
	private  CodeTranService codeTranService;
	@Autowired
	private CodeDictService codeDictService;
	@Autowired
	private SaaUserPowerService saaUserPowerService;
	@Autowired
	private AssignService assignService;
	@Autowired
	private PayCustomService payCustomService;
	@Autowired
	private PingAnDictService pingAnDictService;

	@Autowired
	private ClearWebCacheService clearWebCacheService;

	private static Logger logger = LoggerFactory.getLogger(AssessorsAction.class);
	
	@RequestMapping("/allClearCache.do")
	public ModelAndView allClearCache(){
		ModelAndView mv =new ModelAndView();
		
		mv.setViewName("base/manage/ClearCache");
		return mv;
	}
	
    /**
     * 清除数据字典缓存
     * @return
     */
	@RequestMapping("/dataDictionaryClerarCache.do")
	@ResponseBody
	public AjaxResult dataDictionaryClearCache(HttpServletRequest req){
		AjaxResult ajaxResult =new AjaxResult();
		CodeTranUtil codeTranUtil=new CodeTranUtil();
		ConfigUtil configUtil=new ConfigUtil();
		try{
			codeTranService.clearCache();
			codeDictService.clearCache();
			codeTranUtil.clearCache();
			configUtil.clearCache();
			pingAnDictService.clearCache();
			clearWebCacheService.clearOtherServiceCache("1");
			ajaxResult.setStatus(HttpStatus.SC_OK);
			
			clearOtherCache("1",req);
			
		}catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.debug(e.getMessage());
		}
		
		return ajaxResult;
	}
	
	/**
	 * 地区缓存清除
	 * @return
	 */
	@RequestMapping("/areaClerarCache.do")
	@ResponseBody
	public AjaxResult areaClearCache(HttpServletRequest req){
		AjaxResult ajaxResult =new AjaxResult();
		AreaSelectUtil areaSelectUtil=new AreaSelectUtil();
		try{
			
			areaSelectUtil.clearCache();
			payCustomService.clearCache();
			clearWebCacheService.clearOtherServiceCache("2");
			ajaxResult.setStatus(HttpStatus.SC_OK);
			
			clearOtherCache("2",req);
			
		}catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.debug(e.getMessage());
		}
		
		return ajaxResult;
	}
	
	/**
	 * 用户数据缓存清除
	 * @return
	 */
	@RequestMapping("/userClearCache.do")
	@ResponseBody
	public AjaxResult userClearCache(HttpServletRequest req){
		AjaxResult ajaxResult=new AjaxResult();
//		RetryLimitHashedCredentialsMatcher ret=new RetryLimitHashedCredentialsMatcher();
		try{
			saaUserPowerService.clearCache();
			clearWebCacheService.clearOtherServiceCache("3");
//			ret.clearCache();
			ajaxResult.setStatus(HttpStatus.SC_OK);
			clearOtherCache("3",req);
		}catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			logger.debug(e.getMessage());
		}
		return ajaxResult;
	}
	
	
	/**
	 *  规则数据缓存清除
	 */
    @RequestMapping("/ruleClearCache.do")
	@ResponseBody
	public AjaxResult ruleClearCache(HttpServletRequest req){
		 AjaxResult ajaxResult=new AjaxResult();
		 try{
			 assignService.clearRule();
			 clearWebCacheService.clearOtherServiceCache("4");
			 ajaxResult.setStatus(HttpStatus.SC_OK);
			 clearOtherCache("4",req);
		 }catch(Exception e){
			 ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			 logger.debug(e.getMessage());
		 }
		
		 return ajaxResult;
	 }
    
    public void clearOtherCache(String type,HttpServletRequest req) throws Exception {
		Inet4Address address = this.getLocalIp4Address(); 
		int port = 0;
		port = req.getServerPort();
		Log.info("当前ip的地址为:" +address.getHostAddress() +"当前端口号为:" + port);
		if(StringUtils.isNotBlank(address.getHostAddress()) && port != 0){
			String curUrl = address.getHostAddress()+":"+port;
			List<String> urlList = new ArrayList<String>();
			String url1 = SpringProperties.getProperty("ClearCache_URL1");
			String url2 = SpringProperties.getProperty("ClearCache_URL2");
			String url3 = SpringProperties.getProperty("ClearCache_URL3");
			String url4 = SpringProperties.getProperty("ClearCache_URL4");
			if(StringUtils.isNotBlank(url1) && !url1.contains(curUrl)){
				urlList.add(url1);
			}
			if(StringUtils.isNotBlank(url2) && !url2.contains(curUrl)){
				urlList.add(url2);
			}
			if(StringUtils.isNotBlank(url3) && !url3.contains(curUrl)){
				urlList.add(url3);
			}
			if(StringUtils.isNotBlank(url4) && !url4.contains(curUrl)){
				urlList.add(url4);
			}
			if(urlList != null && urlList.size() > 0){
				Log.info("需要清理缓存的地址为:" +urlList.toString());
				for (String string : urlList) {
					InputStream inputStream = null;
					InputStreamReader inputStreamReader = null;
					BufferedReader bufferedReader = null;
					OutputStream outputStream = null;
					HttpURLConnection httpUrlConn = null;
					try {    
			            URL url = new URL(string);
			            httpUrlConn = (HttpURLConnection) url.openConnection();
			            httpUrlConn.setDoOutput(true);    
			            httpUrlConn.setDoInput(true);    
			            httpUrlConn.setUseCaches(false);    
			            // 设置请求方式（GET/POST）    
			            httpUrlConn.setRequestMethod("POST"); 
			            httpUrlConn.setRequestProperty("Content-Type", "text/xml;charset=GBK");
			            httpUrlConn.setConnectTimeout(10 * 1000);
			            httpUrlConn.connect();    
			            outputStream = httpUrlConn.getOutputStream();  		        
			            // 当有数据需要提交时    
			            ClearCacheVo clearVo = new ClearCacheVo();
			            clearVo.setType(type);
			            outputStream.write((JSON.toJSONString(clearVo)).getBytes("GBK"));   
			    
			            // 将返回的输入流转换成字符串    
			            inputStream = httpUrlConn.getInputStream();    
			            inputStreamReader = new InputStreamReader(inputStream, "GBK");    
			            bufferedReader = new BufferedReader(inputStreamReader);    
			            StringBuffer buffer = new StringBuffer();
			            String str = null;    
			            while ((str = bufferedReader.readLine()) != null) {
			                buffer.append(str);    
			            } 
			            if (buffer.length() < 1) {
							throw new Exception("返回数据失败!");
						}
			            AddValueResponse res = JSON.parseObject(buffer.toString(), AddValueResponse.class);
			            if("200".equals(res.getCode())){
			            	logger.info(urlList+"清除缓存成功!");
			            }else{
			            	logger.info(urlList+"清理缓存失败!");
			            }
			        }catch (Exception e) {
			        	e.printStackTrace();
			        	throw new Exception(urlList+"清理缓存失败", e);
			        }finally{
			        	// 释放资源 
			        	httpUrlConn.disconnect(); 
			        	bufferedReader.close();    
			            inputStreamReader.close();  
			            outputStream.flush();
			            outputStream.close();
			            inputStream.close();    
			            inputStream = null;
			        }
				}
			}
		}
	}
    
    public List<Inet4Address> getLocalIp4AddressFromNetworkInterface() throws SocketException {
		List<Inet4Address> addresses = new ArrayList<Inet4Address>(1);
		Enumeration e = NetworkInterface.getNetworkInterfaces();
		if (e == null) {
			return addresses;
		}
		while (e.hasMoreElements()) {
			NetworkInterface n = (NetworkInterface) e.nextElement();
			if (!this.isValidInterface(n)) {
				continue;
			}
			Enumeration ee = n.getInetAddresses();
			while (ee.hasMoreElements()) {
				InetAddress i = (InetAddress) ee.nextElement();
				if (this.isValidAddress(i)) {
					addresses.add((Inet4Address) i);
				}
			}
		}
		return addresses;
	}

	/**
	 * 过滤回环网卡、点对点网卡、非活动网卡、虚拟网卡并要求网卡名字是eth或ens开头
	 *
	 * @param ni 网卡
	 * @return 如果满足要求则true，否则false
	 */
	private boolean isValidInterface(NetworkInterface ni) throws SocketException {
		return !ni.isLoopback() && !ni.isPointToPoint() && ni.isUp() && !ni.isVirtual()
				&& (ni.getName().startsWith("eth") || ni.getName().startsWith("ens"));
	}

	/**
	 * 判断是否是IPv4，并且内网地址并过滤回环地址.
	 */
	private boolean isValidAddress(InetAddress address) {
		return address instanceof Inet4Address && address.isSiteLocalAddress() && !address.isLoopbackAddress();
	}

	private Inet4Address getIpBySocket() throws SocketException {
		try {
			final DatagramSocket socket = new DatagramSocket();
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			if (socket.getLocalAddress() instanceof Inet4Address) {
				Inet4Address in = (Inet4Address)socket.getLocalAddress();
				return in;
			}
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public Inet4Address getLocalIp4Address() throws SocketException {
		final Inet4Address ipBySocketOpt = this.getIpBySocket();
		if (ipBySocketOpt != null) {
			return ipBySocketOpt;
		} else {
			List<Inet4Address> ipByNi = this.getLocalIp4AddressFromNetworkInterface();
			if(ipByNi != null && ipByNi.size() > 0){
				return ipByNi.get(0);
			}
		}
		return null;
	}
    
    
    
}
