package ins.sino.claimcar.pay.web.action;


import com.google.gson.Gson;

import ins.framework.lang.Springs;
import ins.sino.claimcar.newpayment.vo.PaymentConstants;
import ins.sino.claimcar.newpayment.vo.ResponseDto;
import ins.sino.claimcar.newpayment.service.PaymentRefundService;
import ins.sino.claimcar.newpayment.service.PaymentWriteBackService;
import ins.sino.claimcar.newpayment.service.UpdateClaimSettleNoService;
import ins.sino.claimcar.newpayment.service.UpdateClaimTaxService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

/**
 * 接收收付信息
 *
 * @author maofengning
 * @date 2020/5/14 17:13
 */
public class ReceivePaymentInfoServlet extends HttpServlet {

    private static final long serialVersionUID = 7521665262718674066L;

    private static final Logger logger = LoggerFactory.getLogger(ReceivePaymentInfoServlet.class);

    @Autowired
    private PaymentRefundService paymentRefundService;
    @Autowired
    private PaymentWriteBackService paymentWriteBackService;
    @Autowired
    private UpdateClaimSettleNoService updateClaimSettleNoService;
    @Autowired
    private UpdateClaimTaxService updateClaimTaxService;

    @Override
    public void init() throws ServletException {
        if (paymentRefundService == null) {
            paymentRefundService = Springs.getBean(PaymentRefundService.class);
        }
        if (paymentWriteBackService == null) {
            paymentWriteBackService = Springs.getBean(PaymentWriteBackService.class);
        }
        if (updateClaimSettleNoService == null) {
            updateClaimSettleNoService = Springs.getBean(UpdateClaimSettleNoService.class);
        }
        if (updateClaimTaxService == null) {
            updateClaimTaxService = Springs.getBean(UpdateClaimTaxService.class);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        ResponseDto responseDto = new ResponseDto();
        response.setContentType("text/plain;charset=utf-8");
        try {
            // 防止返回内容中文乱码
            request.setCharacterEncoding("utf-8");
            // url中的参数，用于判断调用哪个服务处理
            String requestService = request.getQueryString();
            String params = getPostParams(request);
            if (PaymentConstants.PAYMENTWRITEBACKPAY.equals(requestService)) {
                // 应收应付回写
                responseDto =  paymentWriteBackService.writeBackPaymentToClaim(params);
            } else if (PaymentConstants.PAYMENTREFUND.equals(requestService)) {
                // 收付退票
                responseDto =  paymentRefundService.paymentRefund(params);
            } else if (PaymentConstants.PAYMENTUPDATESETTLENO.equals(requestService)) {
                // 更新结算单号
                responseDto =  updateClaimSettleNoService.updateClaimSettleNo(params);
            } else if (PaymentConstants.PAYMENTWRITEBACKTAX.equals(requestService)) {
                // 价税回写
                responseDto =  updateClaimTaxService.updateClaimTax(params);
            } else if (PaymentConstants.RECEIVEVATCHARTE.equals(requestService)) {
                //价税拆分数据推送理赔
            	responseDto = updateClaimTaxService.verifyVatBackClaimKindPay(params);
            	Set<String> toReInsSet = updateClaimTaxService.recordClaimKindPayTrace(params,responseDto);
            	//推送再保
            	if(toReInsSet!= null && toReInsSet.size()>0){
            	      updateClaimTaxService.sendVatBackSumAmountNTToReins(toReInsSet);
            	}
            } else {
                responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
                responseDto.setErrorMessage("无法识别任务类型，理赔无法处理！");
            }
            logger.info("理赔返回收付报文：" + gson.toJson(responseDto));
            
        } catch (Exception e) {
            logger.info("理赔接收收付数据处理异常！", e);
            responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
            responseDto.setErrorMessage(e.getMessage());
        }
        response.getWriter().write(gson.toJson(responseDto));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    /**
     * 获取post请求中的参数
     * @param request http请求
     * @return 返回请求中的参数
     */
    private String getPostParams(HttpServletRequest request) {
        StringBuilder json = new StringBuilder("");
        String requestService = request.getQueryString();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                json.append(temp);
            }
            br.close();
        } catch (Exception e) {
            logger.info(requestService + " 参数获取异常！", e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                logger.info(requestService + " 字符流关闭异常！", e);
            }
        }
        return json.toString();
    }
}
