package ins.platform.shiro.web.filter;

import ins.platform.common.util.ConfigUtil;
import ins.platform.vo.PrpLConfigValueVo;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * Servlet Filter implementation class XssFilter
 */
public class XssFilter implements Filter {
    /**
     * 字符数组
     */
	private String[] charstr=null;
	private String decostr="";//特殊字符
    /**
     * Default constructor. 
     */
    public XssFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//特殊字符过滤器开关
		PrpLConfigValueVo configValueVoByMap = ConfigUtil.findConfigByCode("xssFilterCheck");
		//特殊字符
		PrpLConfigValueVo configValueVoBychar= ConfigUtil.findConfigByCode("xssFilterChar");
		//请求白名单
		String openFlag="0";
		String charUrl=configValueVoBychar.getRemark();
		String[] strUrl=null;
		if(StringUtils.isNotBlank(charUrl)){
			strUrl=charUrl.split(",");
		}
		StringBuffer url=new StringBuffer();
		HttpServletRequest requesturl=((HttpServletRequest)request);
		if(requesturl!=null){
			url=requesturl.getRequestURL();
		}
		if(strUrl!=null && strUrl.length>0 && StringUtils.isNotBlank(url.toString())){
			for(int i=0;i<strUrl.length;i++){
				if(url.toString().contains(strUrl[i])){
					openFlag="1";
					break;
				}
			}
		}
		
		if(configValueVoBychar!=null && StringUtils.isNotBlank(configValueVoBychar.getConfigValue())){
			charstr=configValueVoBychar.getConfigValue().split(",");
			
		}
		   if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue()) && "0".equals(openFlag)){
			  boolean flag=reqParameterValues((HttpServletRequest) request);
			  if(flag){
				  if(StringUtils.isNotBlank(decostr)){
				      throw new ServletException("输入的参数包含非法字符:"+decostr);
				  }else{
					  chain.doFilter(request, response);
				  }
			  }else{
				  chain.doFilter(request, response);
			  }
			    
		   }else{
			   chain.doFilter(request,response);
		   }
           
           
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
	}
	
	private boolean reqParameterValues(HttpServletRequest request) throws ServletException {
		boolean flag=false;//标志-false-表示没有包含特殊字符，true-表示包含特殊字符
		Map<String,String[]> paramMap = request.getParameterMap();
		if(paramMap!=null && paramMap.size()>0){
			for(Iterator iter=paramMap.entrySet().iterator();iter.hasNext();){
				if(flag){
        			break;
        		}
				Map.Entry entry=(Map.Entry)iter.next();  
		        //value,数组形式  
		        String[] charvalues=(String[])entry.getValue();
		        if(charvalues!=null && charvalues.length>0){
		        	for(int i=0;i<charvalues.length;i++){
		        		if(StringUtils.isNotBlank(charvalues[i])){
			        		flag = xssChar(charvalues[i]);
			        		if(flag){//如果为true,则代表有特殊字符，终止循环
			        			break;
			        		}
		        		}
		        		
		        	}
		        	
		        }
			}
		}
		return flag;
	}
	
	private boolean xssChar(String value) {
		boolean flag=false;//标志-false-表示没有包含特殊字符，true-表示包含特殊字符
		if(charstr!=null && charstr.length>0){
			for(int i=0;i<charstr.length;i++){
				if(value.contains(charstr[i])){
					decostr=charstr[i];
					flag=true;
					break;
				}
			}
		}
		
		return flag;
	}

}
