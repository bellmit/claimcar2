package ins.sino.claimcar.pinganUnion.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ins.platform.utils.Base64EncodedUtil;
import ins.sino.claimcar.jiangxicourt.web.action.BaseServlet;
import ins.sino.claimcar.pinganUnion.enums.DealStatusEnum;
import ins.sino.claimcar.pinganUnion.service.PingAnApiService;
import ins.sino.claimcar.pinganUnion.service.PingAnDataNoticeService;
import ins.sino.claimcar.pinganUnion.util.PingAnMD5Utils;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResponseVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * 平安联盟对接-数据下发接口通知
 */
public class PingAnDataNoticeServlet extends BaseServlet {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(PingAnDataNoticeServlet.class);
    
	@Autowired
	private PingAnDataNoticeService pingAnDataNoticeService;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	/** 
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResponseVo responseVo = ResponseVo.success();
		PingAnDataNoticeVo dataNoticeVo = new PingAnDataNoticeVo();

		try {
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
			StringBuilder responseBuilder = new StringBuilder();
			String inputStr = null;
			while((inputStr = streamReader.readLine()) != null){
				responseBuilder.append(inputStr);
			}
			logger.info("接收平安联盟数据下发通知报文：{} ",responseBuilder.toString());
			if (StringUtils.isBlank(responseBuilder.toString())){
				throw new IllegalArgumentException("接收参数为空");
			}
			JSONObject jsonObject = JSON.parseObject(responseBuilder.toString());
			String siteCode = jsonObject.getString("siteCode");
			String reqData = jsonObject.getString("reqData");
			String signature = jsonObject.getString("signature");
			//验证数字签名是否正确
			String signData = siteCode + reqData;
			Boolean isVerified = PingAnMD5Utils.verifySign(signData, signature);
			if (!isVerified){
				throw new IllegalArgumentException("验签失败");
			}
			logger.info("接收平安联盟数据下发通知报文【解码后】：{} ",Base64EncodedUtil.decode(reqData));

			//解码获取数据
			dataNoticeVo = JSONObject.parseObject(Base64EncodedUtil.decode(reqData), PingAnDataNoticeVo.class);
			dataNoticeVo.setStatus(DealStatusEnum.CREATE.getCode());
			//从查询入参中获取案件号用于后续排查
			JSONObject object = JSON.parseObject(dataNoticeVo.getParamObj());
			if (object != null){
				dataNoticeVo.setReportNo(object.getString("reportNo"));//案件号
			}
			dataNoticeVo.setFailTimes(0);
			dataNoticeVo.setCreateTime(new Date());
			dataNoticeVo.setUpdateTime(new Date());

			dataNoticeVo = pingAnDataNoticeService.saveOrUpdateDataNotice(dataNoticeVo);
		} catch (Exception e) {
			e.printStackTrace();
			responseVo = responseVo.fail("500", e.getMessage());
		}finally {
			responseVo = PingAnMD5Utils.gengerateSign(responseVo);
			logger.info("平安联盟数据下发通知响应报文，reportNo={},response={}", dataNoticeVo.getReportNo(), JSON.toJSONString(responseVo));
		}

		PrintWriter out = null;
		try {
			out = resp.getWriter();
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json");
			out.print(JSON.toJSONString(responseVo));
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
}


