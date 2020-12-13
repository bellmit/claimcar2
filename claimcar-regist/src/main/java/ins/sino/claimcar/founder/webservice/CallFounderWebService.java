/******************************************************************************
* CREATETIME : 2016年8月4日 上午11:34:03
******************************************************************************/
package ins.sino.claimcar.founder.webservice;

import ins.sino.claimcar.founder.vo.CarRegistResVo;
import ins.sino.claimcar.founder.vo.RegistPhoneResVo;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneReqVo;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneResVo;

import java.net.URL;

import org.apache.axis.client.Service;
import org.apache.axis.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;


/**
 * 理赔调用方正客服系统
 * @author ★Luwei
 */
@org.springframework.stereotype.Service("callFounderWebService")
public class CallFounderWebService {
	
	private static Logger logger = LoggerFactory.getLogger(CallFounderWebService.class);
	
	//private static final String founder_url = SpringProperties.getProperty("FOUNDER_URL");
	
	/**
	 * 调用方正客服系统
	 * @param requestXml
	 * @throws Exception
	 * @modified:
	 * ☆Luwei(2016年8月4日 上午11:56:06): <br>
	 */
	public CarRegistResVo callFounderForClient(String requesXml) throws Exception{
		String founder_url = SpringProperties.getProperty("FOUNDER_URL");
		if(StringUtils.isEmpty(founder_url)){
			throw new Exception("未配置方正客服系统服务地址！");
		}
		
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		
		URL endpointURL = new URL(founder_url);
		WsSoap11BindingStub stub = new WsSoap11BindingStub(endpointURL,new Service());
		stub.setTimeout(5000);
		String returnXml = stub.send(requesXml);
		
		logger.debug("理赔调用方正客服系统返回：\n"+returnXml);
		stream.processAnnotations(CarRegistResVo.class);
		CarRegistResVo returnVo = (CarRegistResVo)stream.fromXML(returnXml);
		
		if("0".equals(returnVo.getHead().getResponseType())){
			logger.debug("理赔调用方正客服系统返回失败："+returnVo.getHead().getErrorMessage());
//			throw new Exception(returnVo.getHead().getErrorMessage());
		}
		return returnVo;
	}
	
	/**
	 * 报案获取报案电话
	 * @param requesXml
	 * @return
	 * @throws Exception
	 */
	public RegistPhoneResVo PhoneFounderForClient(String requesXml)throws Exception{
		String founder_url = SpringProperties.getProperty("FOUNDER_URL");
		if(StringUtils.isEmpty(founder_url)){
			throw new Exception("未配置方正客服系统服务地址！");
		}
		
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		
		URL endpointURL = new URL(founder_url);
		WsSoap11BindingStub stub = new WsSoap11BindingStub(endpointURL,new Service());
		stub.setTimeout(5000);
		String returnXml = stub.send(requesXml);
		
		logger.info("理赔调用方正客服系统返回：\n"+returnXml);
		System.out.print("理赔调用方正客服系统返回：\n"+returnXml);
		stream.processAnnotations(RegistPhoneResVo.class);
		RegistPhoneResVo returnVo = (RegistPhoneResVo)stream.fromXML(returnXml);
		
		if("0".equals(returnVo.getHead().getResponseType())){
			logger.info("理赔调用方正客服系统返回失败："+returnVo.getHead().getErrorMessage());

		}
		
		return returnVo;
		
	}
	
	
	/**
	 * 查勘员号码更新
	 * @param requestXml
	 * @throws Exception
	 * @modified:
	 * ☆Luwei(2016年8月4日 上午11:56:06): <br>
	 */
	public CallPhoneResVo sendCallPhone(String requesXml) throws Exception{
		String founder_url = SpringProperties.getProperty("FOUNDER_URL");
		if(StringUtils.isEmpty(founder_url)){
			throw new Exception("未配置方正客服系统服务地址！");
		}
		
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		
		URL endpointURL = new URL(founder_url);
		WsSoap11BindingStub stub = new WsSoap11BindingStub(endpointURL,new Service());
		stub.setTimeout(5000);
		String returnXml = stub.send(requesXml);
		
		logger.info("发送查勘员号码更新========理赔调用方正客服系统返回：\n"+returnXml);
		stream.processAnnotations(CallPhoneResVo.class);
		CallPhoneResVo returnVo = (CallPhoneResVo)stream.fromXML(returnXml);
		
		if("0".equals(returnVo.getHead().getResponseType())){
			logger.info("发送查勘员号码更新========理赔调用方正客服系统返回失败："+returnVo.getHead().getErrorMessage());
//			throw new Exception(returnVo.getHead().getErrorMessage());
		}
		return returnVo;
	}
	/**
	 * 查勘员号码更新
	 * @param handleScheduleReqVo
	 * @return
	 * @throws Exception
	 */
	public CallPhoneResVo sendCallPhone(CallPhoneReqVo callPhoneReqVo) throws Exception {
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		String headXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>";
		
		String requestXml = headXml+stream.toXML(callPhoneReqVo);
		logger.info("发送查勘员号码更新========"+requestXml+"\n");
		
		//发送
		CallPhoneResVo returnVo = this.sendCallPhone(requestXml);
		return returnVo;
	}
}
