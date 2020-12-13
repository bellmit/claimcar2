package ins.sino.claimcar.addvaluetopolicy.servlet;

import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.addvaluetopolicy.vo.AddValueResponse;
import ins.sino.claimcar.addvaluetopolicy.vo.AddValueServicesReq;
import ins.sino.claimcar.addvaluetopolicy.vo.KindDto;
import ins.sino.claimcar.addvaluetopolicy.vo.ReturnDto;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.jiangxicourt.web.action.BaseServlet;
import ins.sino.claimcar.regist.service.RegistService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class AddValueServicesServlet extends BaseServlet{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(AddValueServicesServlet.class);
	@Autowired
	private CompensateTaskService compensateTaskService;
    
	@Autowired
	private RegistService registService;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AddValueResponse responseVo = AddValueResponse.success();
		AddValueServicesReq addValueServicesReq = new AddValueServicesReq();

		try {
			//String policyNo = req.getParameter("policyNo");
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
			StringBuilder responseBuilder = new StringBuilder();
			String inputStr = null;
			while((inputStr = streamReader.readLine()) != null){
				responseBuilder.append(inputStr);
			}
			logger.info("接收车承保系统增值条款理赔次数查询报文：{} ",responseBuilder.toString());
			if (StringUtils.isBlank(responseBuilder.toString())){
				throw new IllegalArgumentException("接收参数为空");
			}
			JSONObject jsonObject = JSON.parseObject(responseBuilder.toString());
			String policyNo = jsonObject.getString("policyNo");
			//logger.info("接收承保系统增值条款理赔次数查询【解码后】：{} ",Base64EncodedUtil.decode(reqData));

			//解码获取数据
			//addValueServicesReq = JSONObject.parseObject(reqData, AddValueServicesReq.class);
			if(StringUtils.isEmpty(policyNo)){
				throw new IllegalArgumentException("保单号policyNo为空");
			}
			addValueServicesReq.setPolicyNo(policyNo);
			List<PrpLCompensateVo> compensateVoLists = compensateTaskService.queryCompensateByPolicyNo(policyNo);
			//组织返回数据
			ReturnDto returnDto = new ReturnDto();
			returnDto.setPolicyNo(addValueServicesReq.getPolicyNo());
			List<KindDto> kindDtoList = new ArrayList<KindDto>();
			KindDto kindDto = null;
			if(compensateVoLists != null && compensateVoLists.size() > 0){
				Map<String,Integer> map = new HashMap<String,Integer>();
				for (PrpLCompensateVo compensateVo : compensateVoLists) {
					List<PrpLLossPropVo> prpLLossProps = compensateVo.getPrpLLossProps();
					if(prpLLossProps != null && prpLLossProps.size() > 0){
						for (PrpLLossPropVo prpLLossPropVo : prpLLossProps) {
							if(map.containsKey(prpLLossPropVo.getKindCode())){
								int count = map.get(prpLLossPropVo.getKindCode());
								map.put(prpLLossPropVo.getKindCode(),count+1);
							}else{
								map.put(prpLLossPropVo.getKindCode(),1);
							}
						}
					}
				}
				Iterator<Entry<String, Integer>> iterator = map.entrySet().iterator();
				while(iterator.hasNext()){
					 Entry<String, Integer> next = iterator.next();
					 if(CodeConstants.KINDCODE.KINDCODE_RS.equals(next.getKey()) || CodeConstants.KINDCODE.KINDCODE_VS.equals(next.getKey()) ||
							 CodeConstants.KINDCODE.KINDCODE_DS.equals(next.getKey()) || CodeConstants.KINDCODE.KINDCODE_DC.equals(next.getKey())){
						 kindDto = new KindDto();
						 kindDto.setKindCode(next.getKey());
						 kindDto.setTimes(next.getValue().toString());
						 kindDtoList.add(kindDto);
					 }
				}
			}
			
			returnDto.setKindDtoList(kindDtoList); 
			responseVo.setRespData(returnDto);
		} catch (Exception e) {
			e.printStackTrace();
			responseVo = responseVo.fail("500", e.getMessage());
		}finally {
			//responseVo = PingAnMD5Utils.gengerateSign(responseVo);
			logger.info("承保系统增值条款理赔次数查询响应报文，reportNo={},response={}", addValueServicesReq.getPolicyNo(), JSON.toJSONString(responseVo));
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
