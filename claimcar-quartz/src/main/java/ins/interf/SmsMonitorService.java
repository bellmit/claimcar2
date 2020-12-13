package ins.interf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.lang.Springs;
import ins.framework.utils.Beans;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.vo.PrpsmsMessageVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.mail.service.MailModelService;
import ins.sino.claimcar.manager.vo.PrpdEmailVo;
import ins.sino.claimcar.manager.vo.PrpdJSONEmailVo;
import ins.sino.claimcar.other.service.MsgModelService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.jws.WebService;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@WebService(targetNamespace="http://interf/smsMonitorService/",serviceName="smsMonitorService")
public class SmsMonitorService extends SpringBeanAutowiringSupport {
    private Logger logger = LoggerFactory.getLogger(SmsMonitorService.class);
    private static final Map<String, String> SENDNODEMAP = new HashMap<String, String>(){
        {
            put("Regis", "报案");
            put("Sched", "调度");
            put("EndCas", "结案");
        }
    };
    private static final Map<String, String> SENDSTATUSMAP = new HashMap<String, String>(){
        {
            put("0", "推送失败");
            put("1", "推送成功");
            put("-1", "未推送");
        }
    };

    @Autowired
    MsgModelService msgModelService;
    @Autowired
    MailModelService mailModelService;

    public void  doJob () {
        logger.info("SmsMonitorService 短信监控开始执行！");
        init();
        List<String> mailaddrs = getMailAddress();
        if (mailaddrs.size() > 0) {
            String content = getSmsMonitorContent();
            for (String mail: mailaddrs) {
                sendNewEmail(mail, content);
            }
        }

        logger.info("SmsMonitorService 短信监控执行完成！");
    }

    private void init() {
        if (msgModelService == null) {
            msgModelService = Springs.getBean(MsgModelService.class);
        }
        if (mailModelService == null) {
            mailModelService = Springs.getBean(MailModelService.class);
        }
    }

    private void sendMail(String mailaddress, String mailContent) {
        if (StringUtils.isBlank(mailaddress) || StringUtils.isBlank(mailContent)) {
            return;
        }

        try {
            // 邮件类型  车物重案上报  人伤重案上报  短信
            String mailSubject = "短信监控邮件";
            // 邮件发送所需参数
            String username= SpringProperties.getProperty("mailusername");
            String password= SpringProperties.getProperty("mailpassword");
            String sendNick = SpringProperties.getProperty("mailNick");
            // 邮件服务器地址
            HttpPost httpPost = new HttpPost(SpringProperties.getProperty("Mail_URL"));

            String requestXML = CodeConstants.MailXML_Head+"<arg0>"+mailaddress+"</arg0><arg1>"+mailSubject+"</arg1><arg2>"+mailContent+"</arg2><arg3>"
                    +username+ "</arg3><arg4>"+ password +"</arg4><arg5>"+ sendNick +"</arg5>" + CodeConstants.MailXML_End;
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
            StringEntity requestEntity = new StringEntity(requestXML, Charset.forName("UTF-8"));
            httpPost.setEntity(requestEntity);
            // 邮件发送
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpPost);
            // 获取邮件发送返回报文
            HttpEntity httpEntity = response.getEntity();
            String responContent = EntityUtils.toString(httpEntity);

            // 返回报文中是否有返回状态
            String sendStatusMsg = "SmsMonitorService 短信监控邮件发送成功！";
            if (responContent.contains("<return>") && responContent.contains("</return>")) {
                int startIndex = responContent.indexOf("<return>");
                int endIndex = responContent.indexOf("</return>");
                // 如果返回报文中<return>标签内容小写为true，方可证明邮件发送成功，其他情况一致认定为发送失败
                if (!"true".equals(responContent.substring(startIndex+"<return>".length(), endIndex).toLowerCase())) {
                    sendStatusMsg = "SmsMonitorService 短信监控邮件发送失败！";
                }
            } else {
                sendStatusMsg = "SmsMonitorService 短信监控邮件发送失败！";
            }
            logger.info("\n"+sendStatusMsg+"\n");
        } catch (Exception e) {
            logger.info("\nSmsMonitorService 短信监控邮件发送失败！\n", e);
            e.printStackTrace();
        }
    }
    private void sendMailNew(String mailaddress, String mailContent) {
        if (StringUtils.isBlank(mailaddress) || StringUtils.isBlank(mailContent)) {
            return;
        }

        try {
            // 邮件类型  车物重案上报  人伤重案上报  短信
            String mailSubject = "短信监控邮件";
            // 邮件发送所需参数
            String username= SpringProperties.getProperty("mailusername");
            String password= SpringProperties.getProperty("mailpassword");
            String sendNick = SpringProperties.getProperty("mailNick");
            // 邮件服务器地址
            HttpPost httpPost = new HttpPost(SpringProperties.getProperty("Mail_URL"));

            String requestXML = CodeConstants.MailXML_Head+"<arg0>"+mailaddress+"</arg0><arg1>"+mailSubject+"</arg1><arg2>"+mailContent+"</arg2><arg3>"
                    +username+ "</arg3><arg4>"+ password +"</arg4><arg5>"+ sendNick +"</arg5>" + CodeConstants.MailXML_End;
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
            StringEntity requestEntity = new StringEntity(requestXML, Charset.forName("UTF-8"));
            httpPost.setEntity(requestEntity);
            // 邮件发送
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpPost);
            // 获取邮件发送返回报文
            HttpEntity httpEntity = response.getEntity();
            String responContent = EntityUtils.toString(httpEntity);

            // 返回报文中是否有返回状态
            String sendStatusMsg = "SmsMonitorService 短信监控邮件发送成功！";
            if (responContent.contains("<return>") && responContent.contains("</return>")) {
                int startIndex = responContent.indexOf("<return>");
                int endIndex = responContent.indexOf("</return>");
                // 如果返回报文中<return>标签内容小写为true，方可证明邮件发送成功，其他情况一致认定为发送失败
                if (!"true".equals(responContent.substring(startIndex+"<return>".length(), endIndex).toLowerCase())) {
                    sendStatusMsg = "SmsMonitorService 短信监控邮件发送失败！";
                }
            } else {
                sendStatusMsg = "SmsMonitorService 短信监控邮件发送失败！";
            }
            logger.info("\n"+sendStatusMsg+"\n");
        } catch (Exception e) {
            logger.info("\nSmsMonitorService 短信监控邮件发送失败！\n", e);
            e.printStackTrace();
        }
    }
    /**
     * 获取发送失败与未发送的短信记录
     * 报案号-短信内容-发送节点
     * @return
     */
    private String getSmsMonitorContent() {
        StringBuilder sb = new StringBuilder();
        List<String> status = new ArrayList<String>();
        status.add("0");
        List<PrpsmsMessageVo> smslist = msgModelService.findPrpSmsMessagesByStatus(status);

        if (smslist != null) {
            for(PrpsmsMessageVo smsVo : smslist) {
                sb.append(smsVo.getBusinessNo()).append("-").append(SENDNODEMAP.get(smsVo.getSendNodecode())).append("-").append(SENDSTATUSMAP.get(smsVo.getStatus())).append("-").append(smsVo.getSendText()).append("\n");
            }
        }
        if (sb.length() > 0) {
            logger.info("SmsMonitorService 短信监控邮件内容\n" + sb.toString());
            sb = new StringBuilder("以下短信发送失败或未发送：\n\n").append(sb);
        }
        return sb.toString();
    }

    private List<String> getMailAddress() {
        List<String> mailAddressList = new ArrayList<String>();
        List<PrpdEmailVo> mailList = mailModelService.findMailAddressByCaseType("99");
        if (mailList != null) {
            for (PrpdEmailVo mailVo : mailList) {
                if (mailVo != null && StringUtils.isNotBlank(mailVo.getEmail())) {
                    mailAddressList.add(mailVo.getEmail());
                }
            }
        }
        return mailAddressList;
    }
    /**
     * 发送邮件(最新)，对接邮件平台
     * @param toAddress
     * @param mailContent
     * @return
     */
    private boolean sendNewEmail(String toAddress, String mailContent){
        boolean flag= true; //默认成功
        //获取连接信息
        String appCode = SpringProperties.getProperty("appCode");
        String appSecret = SpringProperties.getProperty("appSecret");
        String sendNick = SpringProperties.getProperty("mailNick");
        // 邮件类型  车物重案上报  人伤重案上报  短信
        String mailSubject = "短信监控邮件";
        //组装邮箱数据
        PrpdJSONEmailVo newMailVo = new PrpdJSONEmailVo();
        newMailVo.setAppCode(appCode);
        newMailVo.setAppSecret(appSecret);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        String dateTime = df.format(new Date());
        newMailVo.setAtTime(dateTime);
        newMailVo.setIsIm("1");//是否即时消息：1-是，0-否
        newMailVo.setEmailTo(toAddress);//收件人地址
        newMailVo.setSubject(mailSubject);//主题
        newMailVo.setContent(mailContent);//内容
        newMailVo.setBizType("CC");//业务类型
        newMailVo.setEmailNickName(sendNick);
        //发送并接受返回数据
        String responsejson = "";
        String requestjson = null;
        ObjectMapper om = new ObjectMapper();
        try {
            requestjson = om.writeValueAsString(newMailVo);//转JSON格式
            logger.info("邮件发送内容=============："+requestjson);
            responsejson = this.sendEmailJSON(requestjson);
            logger.info("邮件返回内容=============："+responsejson);
            JsonParser parser = new JsonParser();
            JsonObject jsonobject = parser.parse(responsejson).getAsJsonObject();// 解析返回的json报文
            String result = jsonobject.get("result").getAsString();
            if(!"0".equals(result)){   //非0表示失败
                flag= false;
            }
        } catch (Exception e) {
            flag= false;
            logger.info("邮件发送异常："+e.getMessage());
        }
        return flag;
    }
    /**
     * 使用JSON格式发送邮件
     *
     * @param sendData      发送报文
     * @return 收付响应数据
     * @throws Exception 送收付异常
     */
    private String sendEmailJSON(String sendData) throws Exception {
        if (sendData == null || sendData.trim().length() == 0) {
            throw new Exception("推送邮件数据为空!");
        }

        BufferedReader bfreader = null;
        HttpURLConnection httpURLConnection = null;
        OutputStream outputStream = null;
        DataOutputStream out = null;
        StringBuilder buffer = new StringBuilder();

        try {
            URL url = new URL(SpringProperties.getProperty("MailJSON_URL"));
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            // post方式不能使用缓存
            httpURLConnection.setUseCaches(false);
            // 配置本次连接的Content-Type，配置为text/xml
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            // 维持长连接
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setConnectTimeout(20 * 1000);
            httpURLConnection.setReadTimeout(20 * 1000);
            httpURLConnection.setAllowUserInteraction(true);
            httpURLConnection.connect();
        } catch (Exception ex) {
            logger.info("短信监控连接邮件地址失败！", ex);
            throw new Exception("连接邮件地址失败，请稍候再试！", ex);
        }
        try {
            outputStream = httpURLConnection.getOutputStream();
            out = new DataOutputStream(outputStream);
            out.write(sendData.getBytes("utf-8"));
            out.flush();
            bfreader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
            String strLine = "";
            while ((strLine = bfreader.readLine()) != null) {
                buffer.append(strLine);
            }
        } catch (Exception e) {
            logger.info("短信监控邮件接口返回数据失败！", e);
            throw new Exception("短信监控邮件接口返回数据失败！", e);
        } finally {
            try {
                if (bfreader != null) {
                    bfreader.close();
                }

                if (out != null) {
                    out.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                logger.info("邮件发送流关闭异常！", e);
                e.printStackTrace();
            }

        }
        return buffer.toString();
    }
}
