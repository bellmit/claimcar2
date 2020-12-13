package ins.sino.claimcar.claim.claimjy.service;


import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.claim.claimjy.task.vo.EvalLossInfo;
import ins.sino.claimcar.claim.claimjy.task.vo.JyTaskReqBodyVo;
import ins.sino.claimcar.claim.claimjy.task.vo.JyTaskReqVo;
import ins.sino.claimcar.claimjy.service.JyTaskService;
import ins.sino.claimcar.claimjy.vo.JyReqHeadVo;
import ins.sino.claimcar.claimjy.vo.JyResVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;


/**
 * 任务状态同步接口（从理赔系统请求定损系统）
 * @author zhujunde
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "jyTaskService")
public class JyTaskServiceImpl implements JyTaskService {
    private static Logger LOGGER = LoggerFactory.getLogger(JyTaskServiceImpl.class);
    @Autowired
    private ClaimInterfaceLogService interfaceLogService;
    @Autowired
    private ClaimTextService claimTextService;
    @Autowired
    private WfTaskHandleService wfTaskHandleService;
    
	private static final String FALSEFLAG = "false";
	
	@Override
	public JyResVo sendTaskInfoService(String registNo, String dmgVhclId,
			String operationLink, String operationResults,String subNodeCode, SysUserVo userVo) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JyTaskReqVo reqVo = new JyTaskReqVo();
		JyResVo resVo = new JyResVo();
		String url = SpringProperties.getProperty("JY2_STATUS");
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
			JyTaskReqBodyVo body = new JyTaskReqBodyVo();
			
			head.setUserCode(userCode);
			head.setPassWord(passWord);
			head.setRequestSourceCode("DHIC");
			head.setRequestSourceName("鼎和财产保险股份有限公司");
			head.setRequestType("007");
			head.setOperatingTime(sdf.format(new Date()));
			
			EvalLossInfo evalLossInfo = new EvalLossInfo();
			evalLossInfo.setLossNo(dmgVhclId);
			evalLossInfo.setReportCode(registNo);
			evalLossInfo.setDmgVhclId(dmgVhclId);
			evalLossInfo.setEvalLossType("1");
			evalLossInfo.setStatusCode(operationResults);
			String statusName = CodeTranUtil.transCode("StatusName", operationResults);
			evalLossInfo.setStatusName(statusName);// 做成翻译
			evalLossInfo.setComCode(comCode);
			evalLossInfo.setCompany(CodeTranUtil.transCode("ComCodeFull",comCode));
			evalLossInfo.setBranchComCode(branchComCode);
			evalLossInfo.setBranchComName(CodeTranUtil.transCode("ComCodeFull",branchComCode));
			evalLossInfo.setHandlerCode(userVo.getUserCode());
			evalLossInfo.setHandlerName(userVo.getUserName());
			evalLossInfo.setOperationLink(operationLink);
			evalLossInfo.setOperationResults(operationResults);
			/*String nodeCode = "";
			if("05".equals(operationLink)){
				nodeCode = FlowNode.DLCar.name();
			}else if("06".equals(operationLink)){
				nodeCode = FlowNode.VPrice.name();
			}else{
				nodeCode = FlowNode.VLoss.name();
			}*/
			PrpLClaimTextVo textVo = claimTextService.findClaimTextByLossCarMainIdAndNodeCode(Long.valueOf(dmgVhclId),subNodeCode);
			if(textVo != null){
				evalLossInfo.setOperationOpinion(textVo.getDescription());// 整单备注[当前节点意见]
			}
			evalLossInfo.setSelfEstiFlag("0");// 自核价标记
			List<PrpLWfTaskVo> prpLWfTaskVPCarVos = wfTaskHandleService.findEndTask(registNo,dmgVhclId, FlowNode.VPrice);
			if(prpLWfTaskVPCarVos != null && prpLWfTaskVPCarVos.size() > 0){
				for(PrpLWfTaskVo vo : prpLWfTaskVPCarVos){
					if (FlowNode.VPCar_LV0.name().equals(vo.getSubNodeCode())) {
						evalLossInfo.setSelfEstiFlag("1");// 自核价标记
					}
				}
			}
			evalLossInfo.setSelfApproveFlag("0");// 自核损标记
			List<PrpLWfTaskVo> prpLWfTaskVLCarVos = wfTaskHandleService.findEndTask(registNo,dmgVhclId, FlowNode.VLoss);
			if(prpLWfTaskVLCarVos != null && prpLWfTaskVLCarVos.size() > 0){
				for(PrpLWfTaskVo vo : prpLWfTaskVLCarVos){
					if (FlowNode.VLCar_LV0.name().equals(vo.getSubNodeCode())) {
						evalLossInfo.setSelfApproveFlag("1");// 自核损标记
					}
				}
			}
			body.setEvalLossInfo(evalLossInfo);
			reqVo.setHead(head);
			reqVo.setBody(body);
			
	        String xmlToSend = "";
	        String xmlReturn = "";
	        xmlToSend = ClaimBaseCoder.objToXml(reqVo);
			LOGGER.info("任务状态同步接口提交send---------------------------"+xmlToSend);
	        xmlReturn = requestPlatform(xmlToSend,url);
			LOGGER.info("任务状态同步接口提交return---------------------------"+xmlReturn);
	        resVo = ClaimBaseCoder.xmlToObj(xmlReturn, JyResVo.class);
		} catch (Exception e) {
			LOGGER.error("任务状态同步接口返回报文send异常",e);
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
	private String requestPlatform(String requestXML,String urlStr) throws Exception {
		long t1 = System.currentTimeMillis();
		String responseXml="";
		  StringBuffer buffer = new StringBuffer();    
	        try {    
	         
	            URL url = new URL(urlStr);
	            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
	            httpUrlConn.setDoOutput(true);    
	            httpUrlConn.setDoInput(true);    
	            httpUrlConn.setUseCaches(false);    
			// 设置请求方式（GET/POST）
	            httpUrlConn.setRequestMethod("POST"); 
	            httpUrlConn.setRequestProperty("Content-Type", "text/xml;charset=GBK");
			// 获取超时时间
				String seconds = SpringProperties.getProperty("HTTP_TIMEOUT");
				if(StringUtils.isBlank(seconds)){
					seconds = "20";
				}
				httpUrlConn.setConnectTimeout(Integer.valueOf(seconds) * 1000);
				httpUrlConn.setReadTimeout(Integer.valueOf(seconds) * 1000);
		        
	            httpUrlConn.connect();    
	    
	            String outputStr =requestXML;
	            			
	            OutputStream outputStream = httpUrlConn.getOutputStream();  		        
			// 当有数据需要提交时
	            if (null != outputStr) {    
				// 注意编码格式，防止中文乱码 outputStream.write
	                outputStream.write(outputStr.getBytes("GBK"));    
	            }    
	    
			// 将返回的输入流转换成字符串
	            InputStream inputStream = httpUrlConn.getInputStream();    
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");    
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);    
	    
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
	            responseXml=buffer.toString();
	            
	        } catch (ConnectException ce) {
			throw new Exception("与精友连接失败，请稍候再试",ce);
	        } catch (Exception e) {
	        	e.printStackTrace();
			throw new Exception("读取精友返回数据失败",e);
	        	
	        } finally {
			LOGGER.warn("接口({})调用耗时{}ms",urlStr,System.currentTimeMillis()-t1);
			}    
	        return responseXml;
	}
}
