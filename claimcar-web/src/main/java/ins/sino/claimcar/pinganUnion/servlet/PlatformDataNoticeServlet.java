package ins.sino.claimcar.pinganUnion.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ins.platform.utils.Base64EncodedUtil;
import ins.sino.claimcar.jiangxicourt.web.action.BaseServlet;
import ins.sino.claimcar.pinganUnion.enums.DealStatusEnum;
import ins.sino.claimcar.pinganUnion.service.PlatformDataNoticeService;
import ins.sino.claimcar.pinganUnion.util.PingAnMD5Utils;
import ins.sino.claimcar.pinganUnion.vo.PlatformDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResponseVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 平安联盟对接-上传平台报文推送接口
 */
public class PlatformDataNoticeServlet extends BaseServlet {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(PlatformDataNoticeServlet.class);
    
	@Autowired
	private PlatformDataNoticeService platformDataNoticeService;

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
		PlatformDataNoticeVo platformDataNoticeVo = new PlatformDataNoticeVo();
		try {
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
			StringBuilder responseBuilder = new StringBuilder();
			String inputStr = null;
			while((inputStr = streamReader.readLine()) != null){
				responseBuilder.append(inputStr);
			}
			if (StringUtils.isBlank(responseBuilder.toString())){
				throw new IllegalArgumentException("接收参数为空");
			}
			logger.info("接收平安联盟上传平台报文推送接口通知报文：{}",responseBuilder.toString());
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

			//解码获取数据
			JSONObject jsonObj = JSON.parseObject(Base64EncodedUtil.decode(reqData));
			String head = jsonObj.getString("head");
			String data = jsonObj.getString("data");
			if (StringUtils.isBlank(head) || StringUtils.isBlank(data)){
				throw new IllegalArgumentException("参数不完整");
			}

			JSONObject headObj = JSON.parseObject(head);//解析json字符串
			JSONObject dataObj = JSON.parseObject(data);////解析json字符串
			//组装数据对象
			platformDataNoticeVo.setHead(head);
			platformDataNoticeVo.setStatus(DealStatusEnum.CREATE.getCode());
			platformDataNoticeVo.setTransType(headObj.getString("transType"));//请求类型
			platformDataNoticeVo.setTransDate(headObj.getDate("transDate"));//交易时间
			platformDataNoticeVo.setReportNo(dataObj.getString("reportNo"));//案件号
			platformDataNoticeVo.setPolicyNo(dataObj.getString("policyNo"));//保单号
			platformDataNoticeVo.setComCode(dataObj.getString("comCode"));//机构代码
			platformDataNoticeVo.setRiskType(dataObj.getString("riskType"));//险种类型
			platformDataNoticeVo.setReqData(Base64EncodedUtil.decode(dataObj.getString("reqData")));//上传报文
			platformDataNoticeVo.setCreateTime(new Date());
			platformDataNoticeVo.setUpdateTime(new Date());
			//校验参数
			check(platformDataNoticeVo);

			logger.info("接收平安联盟上传平台报文推送接口通知报文【解码后】：reportNO={},reqData={} ",platformDataNoticeVo.getReportNo(),platformDataNoticeVo.getReqData());
			//先保存请求记录，再发起平台请求
			platformDataNoticeVo = platformDataNoticeService.saveOrUpdatePlatformDataNotice(platformDataNoticeVo);
			ResultBean resultBean = platformDataNoticeService.requestPlatform(platformDataNoticeVo);
			//回写结果
			platformDataNoticeVo.setStatus(resultBean.isSuccess() ? DealStatusEnum.SUCCESS.getCode():DealStatusEnum.FAIL.getCode());
			platformDataNoticeVo.setRemark(resultBean.getMessage());
			platformDataNoticeVo.setUpdateTime(new Date());
			platformDataNoticeService.saveOrUpdatePlatformDataNotice(platformDataNoticeVo);

			//设置响应结果参数
			Map<String,Object> respDataMap = new HashMap<String, Object>();
			Map<String,Object> headMap = new HashMap<String, Object>();
			Map<String,Object> dataMap = new HashMap<String, Object>();
			headMap.put("code",resultBean.isSuccess() ? "200" : "500");//200-成功 500-失败
			headMap.put("msg", resultBean.getMessage());
			headMap.put("transDate", platformDataNoticeVo.getTransDate());
			dataMap.put("resData",resultBean.getData());
			respDataMap.put("head", headMap);
			respDataMap.put("data", dataMap);
			responseVo.setRespData(respDataMap);
			logger.info("平安联盟上传平台报文推送接口响应报文【加密前】，reportNo={},response={}", platformDataNoticeVo.getReportNo(), JSON.toJSONString(responseVo));
		} catch (Exception e) {
			e.printStackTrace();
			responseVo = responseVo.fail("500", "响应失败");
		}finally {
			responseVo = PingAnMD5Utils.gengerateSign(responseVo);
			logger.info("平安联盟上传平台报文推送接口响应报文【加密后】，reportNo={},response={}", platformDataNoticeVo.getReportNo(), JSON.toJSONString(responseVo));
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

	/**
	 * 推送数据校验
	 * @param platformDataNoticeVo
	 */
	private void check(PlatformDataNoticeVo platformDataNoticeVo) {
		if (org.apache.commons.lang.StringUtils.isBlank(platformDataNoticeVo.getReportNo())){
			throw new IllegalArgumentException("案件号为空");
		}
		if (org.apache.commons.lang.StringUtils.isBlank(platformDataNoticeVo.getPolicyNo())){
			throw new IllegalArgumentException("保单号为空");
		}
		if (org.apache.commons.lang.StringUtils.isBlank(platformDataNoticeVo.getComCode())){
			throw new IllegalArgumentException("机构代码为空");
		}
		if (org.apache.commons.lang.StringUtils.isBlank(platformDataNoticeVo.getRiskType())){
			throw new IllegalArgumentException("险种类型为空");
		}
		if (org.apache.commons.lang.StringUtils.isBlank(platformDataNoticeVo.getReqData())){
			throw new IllegalArgumentException("上传报文为空");
		}
	}
}


