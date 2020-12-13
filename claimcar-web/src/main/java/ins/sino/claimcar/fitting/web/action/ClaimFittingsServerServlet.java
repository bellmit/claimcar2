/******************************************************************************
 * CREATETIME : 2015年12月19日 下午4:44:15
* FILE       : ins.sino.claimcar.fitting.web.action.ClaimFittingsServerServlet
******************************************************************************/
package ins.sino.claimcar.fitting.web.action;

import ins.sino.claimcar.losscar.service.LossCarService;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**理赔-配件系统接口
 * 接收配件系统的请求
 * @author ★yangkun
 */
public class ClaimFittingsServerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(ClaimFittingsServerServlet.class);

	public void init() throws ServletException {
	}

	/**
	 * Process the HTTP Get request，集中控制方法，负责调用XML解析方法、回写数据方法等
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
		String strReturn = "";
		BufferedOutputStream output = null;
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		try{
			// 接收完毕
			String strData  = receiveStream(request);
			log.info("\n----------接收数据完毕！！！-------------\n "+strData);
//			System.out.println("---------接收数据完毕！！！-------------\n "+strData);
			// 解析数据并响应相应的方法
			String flag = null;
			LossCarService lossCarService = (LossCarService) context.getBean(LossCarService.class);
			String operateType = request.getParameter("operateType");
			
			flag = lossCarService.doTransData(operateType,strData);
			
			// 输出成功标识
			output = new BufferedOutputStream(response.getOutputStream());
			strReturn = flag+"";
			String returnStr = refreshStrXml(strReturn);
			//System.out.println("fanhui:   "+refreshStrXml(strReturn));
			log.info("fanhui:   "+returnStr);
			output.write(returnStr.getBytes());
			output.close();
		}catch(Exception e){
			e.printStackTrace();
			output = new BufferedOutputStream(response.getOutputStream());
			strReturn = "499";
			output.write(refreshStrXml(strReturn).getBytes());
			output.close();
		}
	
	}
	
	private String getMsgByCode(String code){
		String msg="";
		if( "000".equals(code)){
			msg="成功";
		}
		if( "400".equals(code)){
			msg="系统认证错误";
		}
		if( "401".equals(code)){
			msg="数据包格式错误";
		}
		if( "402".equals(code)){
			msg="数据完整性错误";
		}
		if( "403".equals(code)){
			msg="请求类型错误";
		}
		if( "408".equals(code)){
			msg="安全验证错误";
		}
		if( "409".equals(code)){
			msg="其它异常错误";
		}
		return msg;
	}
	private String refreshStrXml(String code){
		StringBuffer xmlData=new StringBuffer();
		xmlData.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		xmlData.append("<LossRtnReturnData>");
		xmlData.append("<RESPONSE_CODE>").append(code).append("</RESPONSE_CODE>");
		xmlData.append("<ERROR_MESSAGE>").append(getMsgByCode(code)).append("</ERROR_MESSAGE>");
		xmlData.append("</LossRtnReturnData>");
		return xmlData.toString();
	}
	
	/*
	 * 数据接收
	 * */
	public String receiveStream(HttpServletRequest request) throws IOException{
		// 获取接收对象
		java.io.InputStream in = request.getInputStream();
		InputStreamReader insreader = new InputStreamReader(in,"GBK");
		BufferedReader bin = new BufferedReader(insreader);
		
		String read = "";
		StringBuffer strBuffer = new StringBuffer();
		// 转换数据流
		while(( read = bin.readLine() )!=null){
			strBuffer.append(read);
		}
		bin.close();
		insreader.close();
		in.close();
		
		return strBuffer.toString();
	}
	
	/**
	 * 解释头信息
	 **/
	// public String decodeHeadInfo(HttpServletRequest httpServletRequest,HttpServletResponse response,String iData) throws SQLException,Exception {
	// OperXML operXML = new OperXML();
	// operXML.parserFromXMLString(iData);
	// Element head = operXML.getElement("LossRtnHead");
	// String requestType = operXML.getKeyValue(head,"RequestType");
	// return requestType;
	// }
		

	/**
	 * Process the HTTP Post request
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
		doGet(request,response);
	}

	/**
	 * Clean up resources
	 */
	public void destroy() {
	}

}
