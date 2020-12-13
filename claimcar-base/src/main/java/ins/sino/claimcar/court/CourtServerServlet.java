package ins.sino.claimcar.court;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.court.service.CourtService;
import ins.sino.claimcar.court.vo.MessageNoticReqVo;
import ins.sino.claimcar.court.vo.MessageNoticResVo;
import ins.sino.claimcar.court.vo.PrpLCourtMessageVo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 高院信息消息通知接收服务(messageNotic )
 * @author wurh
 *
 */
public class CourtServerServlet  extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(CourtServerServlet.class);
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
		request.setCharacterEncoding("UTF-8");
	    response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String jsonStr = "";
        MessageNoticReqVo reqVo = new MessageNoticReqVo();
        MessageNoticResVo resVo = new MessageNoticResVo();
        try{
        	WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        	CourtService courtServer = context.getBean(CourtService.class);
        	InputStreamReader read = new InputStreamReader(request.getInputStream(),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String temp = "";
            while(( temp = bufferedReader.readLine() )!=null){
                jsonStr += temp;
            }
            read.close();
            
            JSONObject rejson = JSONObject.fromObject(jsonStr);
            log.info("=================高院信息消息通知报文："+rejson.toString());
            reqVo = (MessageNoticReqVo) JSONObject.toBean(JSONObject.fromObject(rejson.get("arg0")), MessageNoticReqVo.class);
            resVo.setJkptUuid(reqVo.getJkptUuid());
            checkRequest(reqVo);
            PrpLCourtMessageVo prpLCourtMessageVo  = this.setCourtMessageVo(reqVo);
            courtServer.saveByCourtMessage(prpLCourtMessageVo);
            resVo.setRetCode("1");
        }catch(Exception e){
            log.error("高院信息消息通知接收服务报错", e.getMessage(), e);
            resVo.setRetCode("0");
            resVo.setRetMessage(e.getMessage());
        }finally{
        	out.print(JSONObject.fromObject(resVo));
        	out.flush();
	        out.close();
        }
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
		doPost(request, response);
	}
	
	private PrpLCourtMessageVo setCourtMessageVo(MessageNoticReqVo reqVo) throws ParseException{
        PrpLCourtMessageVo prpLCourtMessageVo = new PrpLCourtMessageVo();
        prpLCourtMessageVo.setCaseNo(reqVo.getCaseNo());
        prpLCourtMessageVo.setAreaCode(reqVo.getAreaCode());
        prpLCourtMessageVo.setSearchCode(reqVo.getSearchCode());
        prpLCourtMessageVo.setDutyNo(reqVo.getDutyNo());
        prpLCourtMessageVo.setReportDate(DateUtils.strToDate(reqVo.getReportDate(),"yyyyMMdd HH:mm"));
        prpLCourtMessageVo.setAcciAddress(reqVo.getAcciAddress());
        prpLCourtMessageVo.setAcciType(reqVo.getAcciType());
        prpLCourtMessageVo.setJkptUuid(reqVo.getJkptUuid());
        prpLCourtMessageVo.setAcciResult(reqVo.getAcciType());
        prpLCourtMessageVo.setCreateTime(new Date());
        prpLCourtMessageVo.setRetCode("0");
        prpLCourtMessageVo.setAcciNo(reqVo.getAcciNo());
        prpLCourtMessageVo.setUserName(reqVo.getUserName());
        prpLCourtMessageVo.setPassWord(reqVo.getPassWord());
        prpLCourtMessageVo.setExecuteTimes(0);
		return prpLCourtMessageVo;
	}
	private void checkRequest(MessageNoticReqVo reqVo){
		if(StringUtils.isBlank(reqVo.getCaseNo())){
            throw new IllegalArgumentException("报案号不能为空");
        }
		if(StringUtils.isBlank(reqVo.getSearchCode())){
			throw new IllegalArgumentException("查询码不能为空");
		}
		if(StringUtils.isBlank(reqVo.getDutyNo())){
			throw new IllegalArgumentException("事故认定书编号不能为空");
		}
	}
	
	
}
