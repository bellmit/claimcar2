package ins.sino.claimcar.newpayment.service;

import com.google.gson.Gson;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.newpayment.vo.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 调用收付接口，推送支付信息给收付
 *
 * @author ffsz
 * @date 2020/5/24 15:13
 */
@Service("sendToNewPaymentService")
public class SendToNewPaymentService {
    private static Logger logger = LoggerFactory.getLogger(SendToNewPaymentService.class);

    /**
     * 调用收付接口
     *
     * @param requestData  要推送给收付的数据
     * @param businessType 推送类型
     * @param certiNo      业务号（计算书号）
     * @return 收付返回信息
     * @throws Exception 数据推送异常
     */
    public ResponseDto callPaymentForClient(String requestData, BusinessType businessType, String certiNo) throws Exception {
        String node = getOperateNode(businessType);
        logger.info(node + " 业务号：{}" + "  推送收付数据为：{}", certiNo, requestData);

        String newPaymentUrl = SpringProperties.getProperty("NEWPAYMENT_URL");
        if (newPaymentUrl == null || newPaymentUrl.trim().length() == 0) {
            logger.info("业务号：{} 未配置收付地址，推送失败！推送数据为：{}", certiNo, requestData);
            throw new Exception("未配置收付服务地址!");
        }
        newPaymentUrl = newPaymentUrl + "/dataReception";
        String responseData = "";
        try {
            responseData = sendToNewPayment(requestData, certiNo, newPaymentUrl);
            logger.info(node + " 业务号：{}" + "  收付返回数据为：{}", certiNo, responseData);

        } catch (Exception e) {
            logger.info("业务号：" + certiNo + " 推送收付失败！", e);
            throw e;
        }
        Gson gson = new Gson();

        return gson.fromJson(responseData, ResponseDto.class);
    }

    /**
     * 推送理赔支付数据到收付系统
     *
     * @param sendData      发送报文
     * @param certiNo       业务号
     * @param newPaymentUrl 新收付地址
     * @return 收付响应数据
     * @throws Exception 送收付异常
     */
    private String sendToNewPayment(String sendData, String certiNo, String newPaymentUrl) throws Exception {
        if (sendData == null || sendData.trim().length() == 0) {
            logger.info("业务号：{} 推送收付数据为空，推送失败！推送地址为：{}", certiNo, newPaymentUrl);
            throw new Exception("业务号：" + certiNo + " 推送收付数据为空!");
        }

        BufferedReader bfreader = null;
        HttpURLConnection httpURLConnection = null;
        OutputStream outputStream = null;
        DataOutputStream out = null;
        StringBuilder buffer = new StringBuilder();

        try {
            URL url = new URL(newPaymentUrl);
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
            logger.info("业务号：{} 连接收付地址失败！", certiNo, ex);
            throw new Exception("连接收付地址失败，请稍候再试！", ex);
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
            logger.info("业务号：{} 读取收付接口返回数据失败！", certiNo, e);
            throw new Exception("读取收付接口返回数据失败", e);
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
                logger.info("核赔通过理算数据送收付流关闭异常！", e);
                e.printStackTrace();
            }

        }
        return buffer.toString();
    }

    /**
     * 送收付节点名称
     *
     * @param businessType 送收付时传入的类型
     * @return 返回节点名称
     */
    private String getOperateNode(BusinessType businessType) {
        String node = "";
        if (BusinessType.Payment_prePay.equals(businessType)) {
            node = FlowNode.PrePay.name();
        } else if (BusinessType.Payment_compe.equals(businessType)) {
            //理算完核赔通过后，才送收付，日志表里操作节点存成 核赔（VClaim）
            node = FlowNode.VClaim.name();
        } else if (BusinessType.Payment_padPay.equals(businessType)) {
            node = FlowNode.PadPay.name();
        } else if (BusinessType.Payment_recLoss.equals(businessType)) {
            node = FlowNode.RecLoss.name();
        } else if (BusinessType.Payment_recPay.equals(businessType)) {
            node = FlowNode.RecPay.name();
        } else {
            node = "";
        }
        return node;
    }


}
