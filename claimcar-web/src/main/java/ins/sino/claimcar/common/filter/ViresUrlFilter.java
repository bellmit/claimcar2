package ins.sino.claimcar.common.filter;

import ins.platform.common.service.facade.CodeConfigService;
import ins.platform.common.web.util.SpringUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.saa.schema.ResourceTask;
import ins.platform.saa.schema.SaaTask;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.vo.PrpLConfigValueVo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViresUrlFilter implements Filter{

	private static Logger logger = LoggerFactory.getLogger(ViresUrlFilter.class);

	private boolean openUrlFilter = false;  	  //菜单过滤开关
	private boolean openResourcesFilter = false;  //资源过滤开关
	private boolean openSwitchFilter = false;     //资源型过滤跳转前开关

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		initSwitch();
		HttpServletRequest req = (HttpServletRequest) request;
		String reqPath = req.getRequestURI();  //获取端口后之后的地址
		String queryUrl = req.getQueryString();//获取参数
		if(StringUtils.isNotBlank(queryUrl)){
			reqPath += "?" + queryUrl;         //拼接后的URL
		}
		SaaUserPowerService saaUserPowerService = (SaaUserPowerService) SpringUtils.getObject("saaUserPowerService");

		if(!openUrlFilter){
			filterChain.doFilter(request, response);
		}else{
			//获取登陆用户权限
			String userCode = WebUserUtils.getUserCode();
			SaaUserPowerVo saaPowerList = null;
			if(StringUtils.isNotBlank(userCode)){
				//查询当前用户所有权限
				saaPowerList = saaUserPowerService.findUserPower(userCode);
			}
			
			if(reqPath != null){
				logger.info("处理过的请求URL==========> " + reqPath);
				//工作台菜单,风险提示弹窗,保单信息查看不需要权限，直接通过
				if(reqPath.contains("/claimcar/workbench/showTaskList.do") || 
						reqPath.contains("/claimcar/registCommon/registRiskInfo.do") ||
						reqPath.contains("/claimcar/policyView/policyView.do") ||
						reqPath.contains("/claimcar/policyQuery/policySearchs.do/") ||
						reqPath.contains("/claimcar/flowQuery/showFlow.do") ||
						reqPath.contains("/claimcar/flowQuery/search.do")){
					filterChain.doFilter(request, response);
				}else{
					List<SaaTask> saaTaskList = saaUserPowerService.findAllSaaTask();
					Map<String, String> urlMap = new HashMap<String, String>();
					if(saaTaskList != null && saaTaskList.size() > 0){
						for(SaaTask task : saaTaskList){
							if(StringUtils.isNotBlank(task.getUrl())){
								String url = task.getUrl();
								String taskCode = task.getTaskCode();
								urlMap.put(taskCode, url);
							}
						}
					}
					//是否需要过滤标志
					String task = "";
					//比较URL
					if(urlMap != null && urlMap.size() > 0){
						for(Map.Entry<String, String> entry : urlMap.entrySet()){
							String urls = entry.getValue();
							if(reqPath.equals(urls)){
								task += entry.getKey();
							} else {
								if (reqPath.contains(urls)) {
									task += entry.getKey();
								}
							}
						}
					}
					if(task.equals("")){
						//没有匹配上，查看是否为资源型URL
/******************************************过滤资源型请求**********************************************/
						if(!openResourcesFilter){
							filterChain.doFilter(request, response);
						}else{
							// 资源型URL对应的taskNode
							String resTaskNode = "";
							//查询出所有
							List<ResourceTask> resTaskList = saaUserPowerService.findAllResUrl();
							if(resTaskList != null && resTaskList.size() > 0){
								for(ResourceTask resTask : resTaskList){
									//比较URL
									if(StringUtils.isNotBlank(resTask.getUrl())){
										if(reqPath.equals(resTask.getUrl())){
											resTaskNode += resTask.getTaskCode();
										}else{
											if(reqPath.contains(resTask.getUrl())){
												resTaskNode += resTask.getTaskCode();
											}
										}
									}
								}
							}
							
							if(resTaskNode.equals("")){
								//没有匹配到的路径，先记录下来，完善
								logger.info("该资源路径还没有整理到表中！=================>reqPath: " + reqPath + " 该用户工号： " + userCode);
								filterChain.doFilter(request, response);
							}else{
								//该用户是否有请求资源的权限标志
								boolean resFlag = false;
								//先判断是否为从流程图访问节点或者页面上部的按钮
								if(resTaskNode.contains("flowChart")){
									filterChain.doFilter(request, response);  //有权限放行
								}else{
									List<String> taskLists = saaPowerList.getTaskList();
									if(taskLists != null && taskLists.size() > 0){
										for(String taskCode : taskLists){
											if(resTaskNode.contains(taskCode)){
												resFlag = true;
												break;
											}
										}
									}
									//无访问权限的，先打印日志
									if(!resFlag){
										logger.info("该用户无权限请求该资源！=================>reqPath: " + reqPath + " 该用户工号： " + userCode);
									}
									//过滤前加个开关
									if(openSwitchFilter){
										if(resFlag){
											filterChain.doFilter(request, response);  //有权限放行
										}else{
											request.getRequestDispatcher("/NoAuthLogin.jsp").forward(request, response);
										}
									}else{
										filterChain.doFilter(request, response);
									}
								}
							}
						}
/**********************************************过滤资源型请求************************************************/
					}else{
						//该用户是否有请求权限标志
						boolean flag = false;
						List<String> taskList = saaPowerList.getTaskList();
						if(taskList != null && taskList.size() > 0){
							for(String taskCode : taskList){
								if(task.contains(taskCode)){
									flag = true;
									break;
								}
							}
						}
						if(!flag){
							request.getRequestDispatcher("/NoAuthLogin.jsp").forward(request, response);
						}else{
							filterChain.doFilter(request, response);
						}
					}
				}
			}else{
				//拦截的路径为空，放行
				filterChain.doFilter(request, response);
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}


	/**
	 * 初始化获取过滤开关（1：开，0：关）
	 */
	public void initSwitch(){

		CodeConfigService codeConfigService = (CodeConfigService) SpringUtils.getObject("codeConfigService");
		//菜单过滤开关
		PrpLConfigValueVo configValue = codeConfigService.findConfigValueByCode("authFilterCheck", "00000000");
		if(configValue != null){
			if(StringUtils.isNotBlank(configValue.getConfigValue()) && "1".equals(configValue.getConfigValue())){
				openUrlFilter = true;
			}
		}
		
		//资源型过滤开关
		PrpLConfigValueVo configValues = codeConfigService.findConfigValueByCode("resFilterCheck", "00000000");
		if(configValues != null){
			if(StringUtils.isNotBlank(configValues.getConfigValue()) && "1".equals(configValues.getConfigValue())){
				openResourcesFilter = true;
			}
		}
		
		//资源型过滤前开关
		PrpLConfigValueVo configValuess = codeConfigService.findConfigValueByCode("resUrlFilter", "00000000");
		if(configValuess != null){
			if(StringUtils.isNotBlank(configValuess.getConfigValue()) && "1".equals(configValuess.getConfigValue())){
				openSwitchFilter = true;
			}
		}	
	}
}
