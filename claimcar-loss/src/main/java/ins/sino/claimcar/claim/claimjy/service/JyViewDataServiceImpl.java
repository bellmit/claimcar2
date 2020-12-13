package ins.sino.claimcar.claim.claimjy.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.claimjy.viewData.vo.EvalLossInfo;
import ins.sino.claimcar.claim.claimjy.viewData.vo.JyViewDataReqBodyVo;
import ins.sino.claimcar.claim.claimjy.viewData.vo.JyViewDataReqVo;
import ins.sino.claimcar.claimjy.service.JyViewDataService;
import ins.sino.claimcar.claimjy.vo.JyReqHeadVo;
import ins.sino.claimcar.claimjy.vo.JyResVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.ClaimFittingVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.SendVo;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 定损查看接口 (从理赔请求定损工具)
 * 
 * @author zhujunde
 *
 */
@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" })
@Path(value = "jyViewDataService")
public class JyViewDataServiceImpl implements JyViewDataService {
	private static Logger LOGGER = LoggerFactory
			.getLogger(JyViewDataServiceImpl.class);
	@Autowired
	private ClaimInterfaceLogService interfaceLogService;
	@Autowired
	private LossCarService lossCarService;
	
	private static final String FALSEFLAG = "false";

	@Override
	public JyResVo sendViewDataService(ClaimFittingVo claimFittingVo,String registNo,String dmgVhclId,SysUserVo userVo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SendVo sendVo = claimFittingVo.getSendVo();
		String ip = "";
		String networkFlag = "";
		JyViewDataReqVo reqVo = new JyViewDataReqVo();
		JyResVo resVo = new JyResVo();
		String url = SpringProperties.getProperty("JY2_INFO");
		String userCode = SpringProperties.getProperty("JY_USER");
		String passWord = SpringProperties.getProperty("JY_PAW");
		String inUrl = SpringProperties.getProperty("CLAIMCARJY2_INFO_INURL");
		String outUrl = SpringProperties.getProperty("CLAIMCARJY2_INFO_OUTURL");
		String branchComCode = userVo.getComCode();
		String comCode = "";
		if(branchComCode.startsWith("00")){
			comCode = branchComCode.substring(0,4)+"0000";
		}else{
			comCode = branchComCode.substring(0,2)+"000000";
		}
		try {

			JyReqHeadVo head = new JyReqHeadVo();
			JyViewDataReqBodyVo body = new JyViewDataReqBodyVo();

			head.setUserCode(userCode);
			head.setPassWord(passWord);
			head.setRequestSourceCode("DHIC");
			head.setRequestSourceName("鼎和财产保险股份有限公司");
			head.setRequestType("014");
			head.setOperatingTime(sdf.format(new Date()));

			EvalLossInfo evalLossInfo = new EvalLossInfo();
			evalLossInfo.setLossNo(dmgVhclId);
			evalLossInfo.setReportCode(registNo);
			evalLossInfo.setDmgVhclId(dmgVhclId);
			PrpLDlossCarMainVo lossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(dmgVhclId));
			evalLossInfo.setPlateNo(lossCarMainVo.getLicenseNo());//PlateNo
			ip = sendVo.getServerName();
			if(inUrl.equals(ip)){
				networkFlag = "0";//内网
			}else if(outUrl.equals(ip)){
				networkFlag = "1";//外网
			}
			evalLossInfo.setNetworkFlag(networkFlag);
			evalLossInfo.setComCode(comCode);
			evalLossInfo.setCompany(CodeTranUtil.transCode("ComCodeFull",comCode));
			evalLossInfo.setBranchComCode(branchComCode);
			evalLossInfo.setBranchComName(CodeTranUtil.transCode("ComCodeFull",branchComCode));
			evalLossInfo.setHandlerCode(userVo.getUserCode());
			evalLossInfo.setHandlerName(userVo.getUserName());
			
			
			body.setEvalLossInfo(evalLossInfo);
			reqVo.setHead(head);
			reqVo.setBody(body);

			String xmlToSend = "";
			String xmlReturn = "";
			xmlToSend = ClaimBaseCoder.objToXml(reqVo);
			LOGGER.info("定损查看接口提交send---------------------------" + xmlToSend);
			xmlReturn = requestPlatform(xmlToSend, url);
			LOGGER.info("定损查看接口提交return---------------------------"
					+ xmlReturn);
			resVo = ClaimBaseCoder.xmlToObj(xmlReturn, JyResVo.class);
		} catch (Exception e) {
			LOGGER.error("定损查看接口返回报文send异常", e);
		}
		return resVo;
	}

	/**
	 * 接口组装数据
	 * 
	 * @param requestXML
	 * @param urlStr
	 * @param seconds
	 * @return
	 * @throws Exception
	 */
	private String requestPlatform(String requestXML, String urlStr)
			throws Exception {
		long t1 = System.currentTimeMillis();
		String responseXml = "";
		StringBuffer buffer = new StringBuffer();
		try {

			URL url = new URL(urlStr);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod("POST");
			httpUrlConn.setRequestProperty("Content-Type",
					"text/xml;charset=GBK");
			//获取超时时间
			String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
			if(StringUtils.isBlank(seconds)){
				seconds = "20";
			}
			httpUrlConn.setConnectTimeout(Integer.valueOf(seconds) * 1000);
			httpUrlConn.setReadTimeout(Integer.valueOf(seconds) * 1000);

			httpUrlConn.connect();

			String outputStr = requestXML;

			OutputStream outputStream = httpUrlConn.getOutputStream();
			// 当有数据需要提交时
			if (null != outputStr) {
				// 注意编码格式，防止中文乱码 outputStream.write
				outputStream.write(outputStr.getBytes("GBK"));
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "GBK");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {

				buffer.append(str);
			}
			if (buffer.length() < 1) {
				throw new Exception("精友返回数据失败");
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			responseXml = buffer.toString();

		} catch (ConnectException ce) {
			throw new Exception("与精友连接失败，请稍候再试", ce);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("读取精友返回数据失败", e);

		} finally {
			LOGGER.warn("接口({})调用耗时{}ms", urlStr, System.currentTimeMillis()
					- t1);
		}
		return responseXml;
	}
}
