package ins.sino.claimcar.claim.claimjy.service;

import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.claim.claimjy.zeroNotice.vo.JyZeroNoticeBodyVo;
import ins.sino.claimcar.claim.claimjy.zeroNotice.vo.JyZeroNoticeReqVo;
import ins.sino.claimcar.claim.claimjy.zeroNotice.vo.ZeroLossInfo;
import ins.sino.claimcar.claimjy.service.JyZeroNoticeService;
import ins.sino.claimcar.claimjy.vo.JyReqHeadVo;
import ins.sino.claimcar.claimjy.vo.JyResVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * 定损零结通知接口
 * @author zhujunde
 */
@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" })
@Path(value = "jyZeroNoticeService")
public class JyZeroNoticeServiceImpl implements JyZeroNoticeService {
	private static Logger LOGGER = LoggerFactory
			.getLogger(JyZeroNoticeServiceImpl.class);
	@Autowired
	private ClaimInterfaceLogService interfaceLogService;
    @Autowired
    private ClaimTextService claimTextService;
	@Autowired
	private LossCarService lossCarService;
	private static final String FALSEFLAG = "false";

	@Override
	public JyResVo sendZeroNoticeService(String registNo, String dmgVhclId,
			SysUserVo userVo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JyZeroNoticeReqVo reqVo = new JyZeroNoticeReqVo();
		JyResVo resVo = new JyResVo();
		String url = SpringProperties.getProperty("JY2_DLOSS_ZERO");
		String userCode = SpringProperties.getProperty("JY_USER");
		String passWord = SpringProperties.getProperty("JY_PAW");
		String branchComCode = userVo.getComCode();
		String comCode = "";
		if(branchComCode.startsWith("00")){
			comCode = branchComCode.substring(0,4)+"0000";
		}else{
			comCode = branchComCode.substring(0,2)+"000000";
		}
		try {

			JyReqHeadVo head = new JyReqHeadVo();
			JyZeroNoticeBodyVo body = new JyZeroNoticeBodyVo();

			head.setUserCode(userCode);
			head.setPassWord(passWord);
			head.setRequestSourceCode("DHIC");
			head.setRequestSourceName("鼎和财产保险股份有限公司");
			head.setRequestType("013");
			head.setOperatingTime(sdf.format(new Date()));

			ZeroLossInfo zeroLossInfo = new ZeroLossInfo();
			zeroLossInfo.setLossNo(dmgVhclId);
			zeroLossInfo.setReportCode(registNo);
			zeroLossInfo.setDmgVhclId(dmgVhclId);
			PrpLDlossCarMainVo vo = lossCarService.findLossCarMainById(Long.parseLong(dmgVhclId));
			zeroLossInfo.setLossPrice(vo.getSumLossFee().toString());// 全损金额
			zeroLossInfo.setComCode(comCode);
			zeroLossInfo.setCompany(CodeTranUtil.transCode("ComCodeFull",comCode));
			zeroLossInfo.setBranchComCode(branchComCode);
			zeroLossInfo.setBranchComName(CodeTranUtil.transCode("ComCodeFull",branchComCode));
			zeroLossInfo.setHandlerCode(userVo.getUserCode());
			zeroLossInfo.setHandlerName(userVo.getUserName());
			PrpLClaimTextVo textVo = claimTextService.findClaimTextByLossCarMainIdAndNodeCode(Long.valueOf(dmgVhclId),FlowNode.DLCar.name());
			if(textVo != null){
				zeroLossInfo.setRemark(textVo.getDescription());
			}
			body.setZeroLossInfo(zeroLossInfo);
			reqVo.setHead(head);
			reqVo.setBody(body);

			String xmlToSend = "";
			String xmlReturn = "";
			xmlToSend = ClaimBaseCoder.objToXml(reqVo);
			LOGGER.info("定损零结通知接口提交send---------------------------"+xmlToSend);
			xmlReturn = requestPlatform(xmlToSend, url);
			LOGGER.info("定损零结通知接口提交return---------------------------"
					+ xmlReturn);
			resVo = ClaimBaseCoder.xmlToObj(xmlReturn, JyResVo.class);
		} catch (Exception e) {
			LOGGER.error("定损零结通知接口返回报文send异常",e);
		}
		return resVo;
	}

	/**
	 * 接口组装数据
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
			// 获取超时时间
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
			throw new Exception("与精友连接失败，请稍候再试",ce);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("读取精友返回数据失败",e);

		} finally {
			LOGGER.warn("接口({})调用耗时{}ms",urlStr,System.currentTimeMillis()
					- t1);
		}
		return responseXml;
	}
}
