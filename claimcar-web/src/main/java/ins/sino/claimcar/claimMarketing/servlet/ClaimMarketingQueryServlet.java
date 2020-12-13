package ins.sino.claimcar.claimMarketing.servlet;

import com.alibaba.fastjson.JSON;
import ins.sino.claimcar.claimMarketing.service.ClaimMarketingService;
import ins.sino.claimcar.claimMarketing.vo.ClaimMarketingQueryVo;
import ins.sino.claimcar.claimMarketing.vo.ClaimMarketingResponseDataVo;
import ins.sino.claimcar.jiangxicourt.web.action.BaseServlet;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * description: ClaimMarketingQueryServlet 销售管理系统及移动展业工具 获取理赔信息
 * date: 2020/9/25 14:29
 * author: lk
 * version: 1.0
 */
public class ClaimMarketingQueryServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static Logger logger = LoggerFactory.getLogger(ClaimMarketingQueryServlet.class);

    @Autowired
    private ClaimMarketingService claimMarketingService;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ClaimMarketingResponseDataVo claimMarketingResponseDataVo = new ClaimMarketingResponseDataVo();
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
            StringBuilder responseBuilder = new StringBuilder();
            String inputStr = null;
            while ((inputStr = streamReader.readLine()) != null) {
                responseBuilder.append(inputStr);
            }
            logger.info("接收销售管理系统及移动展业工具的请求报文：{} ", responseBuilder.toString());
            if (StringUtils.isBlank(responseBuilder.toString())) {
                throw new IllegalArgumentException("接收参数为空");
            }
            ClaimMarketingQueryVo claimMarketingQueryVo = JSON.parseObject(responseBuilder.toString(), ClaimMarketingQueryVo.class);
            if (null == claimMarketingQueryVo) {
                throw new IllegalArgumentException("接收参数为空");
            }
            claimMarketingResponseDataVo = claimMarketingService.getClaimInfo(claimMarketingQueryVo);
        } catch (Exception e) {
            e.printStackTrace();
            claimMarketingResponseDataVo.setMessage(e.getMessage());
            claimMarketingResponseDataVo.setStatus("0");
        }
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            logger.info("返回销售管理系统及移动展业工具的返回报文：{} ", JSON.toJSONString(claimMarketingResponseDataVo));
            out.print(JSON.toJSONString(claimMarketingResponseDataVo));
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            out.close();
        }

    }
}
