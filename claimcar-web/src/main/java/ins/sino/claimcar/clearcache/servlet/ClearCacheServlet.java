package ins.sino.claimcar.clearcache.servlet;

import ins.platform.common.service.facade.CodeDictService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.common.util.AreaSelectUtil;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.ConfigUtil;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.sino.claimcar.addvaluetopolicy.vo.AddValueResponse;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.jiangxicourt.web.action.BaseServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ins.sino.claimcar.web.service.ClearWebCacheService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ClearCacheServlet extends BaseServlet{
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(ClearCacheServlet.class);
	
	@Autowired
	private  CodeTranService codeTranService;
	@Autowired
	private CodeDictService codeDictService;
	@Autowired
	private SaaUserPowerService saaUserPowerService;
	@Autowired
	private AssignService assignService;
	@Autowired
	private PayCustomService payCustomService;
	@Autowired
	private PingAnDictService pingAnDictService;

	@Autowired
	private ClearWebCacheService clearWebCacheService;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AddValueResponse responseVo = AddValueResponse.success();

		try {
			//String type = req.getParameter("type");
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
			StringBuilder responseBuilder = new StringBuilder();
			String inputStr = null;
			while((inputStr = streamReader.readLine()) != null){
				responseBuilder.append(inputStr);
			}
			logger.info("清缓存查询报文：{} ",responseBuilder.toString());
			if (StringUtils.isBlank(responseBuilder.toString())){
				throw new IllegalArgumentException("接收参数为空");
			}
			JSONObject jsonObject = JSON.parseObject(responseBuilder.toString());
			String type = jsonObject.getString("type");
			if(StringUtils.isEmpty(type)){
				throw new IllegalArgumentException("参数为空");
			}
			if("1".equals(type)){
				CodeTranUtil codeTranUtil=new CodeTranUtil();
				ConfigUtil configUtil=new ConfigUtil();
				codeTranService.clearCache();
				codeDictService.clearCache();
				pingAnDictService.clearCache();
				codeTranUtil.clearCache();
				configUtil.clearCache();
				clearWebCacheService.clearOtherServiceCache("1");
			}else if("2".equals(type)){
				AreaSelectUtil areaSelectUtil=new AreaSelectUtil();
				areaSelectUtil.clearCache();
				payCustomService.clearCache();
				clearWebCacheService.clearOtherServiceCache("2");
			}else if("3".equals(type)){
				saaUserPowerService.clearCache();
				clearWebCacheService.clearOtherServiceCache("3");
			}else{
				assignService.clearRule();
				clearWebCacheService.clearOtherServiceCache("4");
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseVo = responseVo.fail("500", e.getMessage());
		}finally {
			logger.info("清理缓存响应报文，reportNo={},response={}", "", JSON.toJSONString(responseVo));
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
