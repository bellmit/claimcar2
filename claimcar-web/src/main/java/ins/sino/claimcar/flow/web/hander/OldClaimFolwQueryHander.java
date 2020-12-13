/******************************************************************************
* CREATETIME : 2016年7月24日 上午10:48:57
******************************************************************************/
package ins.sino.claimcar.flow.web.hander;

import ins.framework.common.ResultPage;
import ins.platform.utils.HttpClientHander;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.SwfFlowLogDto;
import ins.sino.claimcar.flow.vo.SwfFlowQueryDto;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * 处理查询旧理赔的工作流
 * @author ★LiuPing
 * @CreateTime 2016年7月24日
 */
@Service(value = "oldClaimFolwQueryHander")
public class OldClaimFolwQueryHander {

	private final static String SESSION_FLOWQRY_KEY = "OLDCLAIM_FLOWQRY_CONDTION";

	public ResultPage<WfTaskQueryResultVo> findFlowForPage(PrpLWfTaskQueryVo taskQueryVo,HttpServletRequest request) throws Exception {
		int start = taskQueryVo.getStart();
		int pageSize = taskQueryVo.getLength();
		int pageNo = start/pageSize + 1;//需要根据 start和taskQueryVo.getLength()计算
		int totalCount = -1;// 等于-1表示需要查询总数
		String searchFlag = "true";// true表示第一次查询
		HttpSession session = request.getSession();
		String condition = (String)session.getAttribute(SESSION_FLOWQRY_KEY);// searchFlag==false时，调用condition作为第二次查询条件
		if(pageNo>1&&StringUtils.isNotBlank(condition)){// 第二页改为已分页
			searchFlag="false";
		}
		//将查询条件PrpLWfTaskQueryVo 转换为 SwfFlowQueryDto 对象
		SwfFlowQueryDto swfQueryDto = initSwfFlowQuery(taskQueryVo);

		// 请求中的parameter参数
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("searchFlag",searchFlag);
		paramMap.put("condition",condition);

		// json 数据请求参数
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		SysUserVo userVo = new SysUserVo();// 将用户信息传给旧理赔
		userVo.setUserCode(taskQueryVo.getUserCode());
		//userVo.setUserName("中文Name");
		userVo.setComCode(taskQueryVo.getComCode());

		jsonMap.put("pageNo",pageNo);
		jsonMap.put("pageSize",pageSize);
		jsonMap.put("totalCount",totalCount);
		jsonMap.put("user",userVo);
		jsonMap.put("workFlowQueryDto",swfQueryDto);

		// 调用旧理赔查询数据
		HttpClientHander httpClent = new HttpClientHander();
		String httpUrl = taskQueryVo.getOldClaimUrl();
		JSONObject responeJson = httpClent.doPostJSON(httpUrl,paramMap,jsonMap);

		// 获得返回值
		int status = responeJson.getIntValue("status");
		String message = responeJson.getString("message");
		pageNo = responeJson.getIntValue("pageNo");
		totalCount = responeJson.getIntValue("totalCount");
		pageSize = responeJson.getIntValue("pageSize");
		condition = responeJson.getString("condition");
		if(status!=200&&status!=0){// 出现异常
			throw new IllegalArgumentException(message);
		}

		JSONArray jsonArray = responeJson.getJSONArray("swfLogDtoList");
		System.out.println(responeJson.toJSONString());
		System.out.println(jsonArray);
		 //将查询结果放到 List<WfTaskQueryResultVo>
		List<WfTaskQueryResultVo> resultList = new ArrayList<WfTaskQueryResultVo>();
		for(int i = 0; i<jsonArray.size(); i++ ){
			SwfFlowLogDto swfLogDto = jsonArray.getObject(i,SwfFlowLogDto.class);
			WfTaskQueryResultVo wfTaskQueryResultVo = new WfTaskQueryResultVo();
			wfTaskQueryResultVo.setRegistNo(swfLogDto.getRegistNo());
			wfTaskQueryResultVo.setPolicyNo(swfLogDto.getPolicyNo());
			wfTaskQueryResultVo.setPolicyNoLink(swfLogDto.getOthPolicyno());
			wfTaskQueryResultVo.setLicenseNo(swfLogDto.getLossItemName());
			wfTaskQueryResultVo.setInsuredName(swfLogDto.getInsuredName());
			wfTaskQueryResultVo.setRiskCode(swfLogDto.getRiskCode());
			wfTaskQueryResultVo.setSerialNo(swfLogDto.getRiskCode());
			wfTaskQueryResultVo.setComCode(swfLogDto.getComCode());
			wfTaskQueryResultVo.setHandlerUser(swfLogDto.getHandlerName());
			SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			wfTaskQueryResultVo.setReportTime(format.parse(swfLogDto.getHandleTime()));//旧理赔用的报案时间是handleTime
			wfTaskQueryResultVo.setDamageTime(swfLogDto.getDamageTime());
			wfTaskQueryResultVo.setDamageAddress(swfLogDto.getDamageAddress());
			wfTaskQueryResultVo.setFlowId(swfLogDto.getFlowID());
			resultList.add(wfTaskQueryResultVo);
		}
		// 遗留问题 翻页查询时 condition 怎么处理，这个是不是要放到session中
		session.setAttribute(SESSION_FLOWQRY_KEY,condition);

		// 转换为新理赔能解析的ResultPage对象
		ResultPage<WfTaskQueryResultVo> resultPage = new ResultPage<WfTaskQueryResultVo>(
				taskQueryVo.getStart(),taskQueryVo.getLength(),totalCount,resultList);
		
		return resultPage;
	}
	
	/**
	 * 初始化SwfFlowQueryDto
	 * @param taskQueryVo
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	private SwfFlowQueryDto initSwfFlowQuery(PrpLWfTaskQueryVo taskQueryVo)  throws Exception{
		SwfFlowQueryDto swfFlowQueryDto = new SwfFlowQueryDto();
		Date reportTimeStart =  taskQueryVo.getReportTimeStart();
		Date reportTimeEnd = taskQueryVo.getReportTimeEnd();
		Date damageTimeStart = taskQueryVo.getDamageTimeStart();
		Date damageTimeEnd = taskQueryVo.getDamageTimeEnd();
		String flowStatus = taskQueryVo.getFlowStatus();
		
		swfFlowQueryDto.setEngineNo(taskQueryVo.getEngineNo());
		swfFlowQueryDto.setThirdEngineNo(taskQueryVo.getThirdEngineNo());
		setTheSamePropertys(taskQueryVo,swfFlowQueryDto,"registNo","policyNo","claimNo","licenseNo");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(reportTimeStart != null){
			swfFlowQueryDto.setReportDate(dateFormat.format(reportTimeStart));
		}
		if(reportTimeEnd != null){
			swfFlowQueryDto.setReportEndDate(dateFormat.format(reportTimeEnd));
		}
		if(damageTimeStart != null){
			swfFlowQueryDto.setDamageStartDate(dateFormat.format(damageTimeStart));
		}
		if(damageTimeEnd != null){
			swfFlowQueryDto.setDamageEndDate(dateFormat.format(damageTimeEnd));
		}
		if(StringUtils.isBlank(flowStatus)){
			taskQueryVo.setFlowStatus("");
		}else{
			swfFlowQueryDto.setCaseType("1".equals(flowStatus) ? "0":"1");
		}
		
		return swfFlowQueryDto;
		
	}
	
	
	private void setTheSamePropertys(Object bean,Object bean1,String... propertys) throws IllegalAccessException,InvocationTargetException,NoSuchMethodException {
		PropertyUtilsBean beanUtil = BeanUtilsBean.getInstance().getPropertyUtils();
		Object value;
		String valueStr = null;
		for(String prop:propertys){
			value = beanUtil.getSimpleProperty(bean,prop);
			if(value!=null&&value instanceof String){
				valueStr = (String)value;
				if(StringUtils.isNotBlank(valueStr)){
					if(valueStr.length() > 6){
						beanUtil.setSimpleProperty(bean1, prop, valueStr);
					}else{
						beanUtil.setSimpleProperty(bean1, prop+"Last", valueStr);
					}
				}
			}

		}
	}

}
